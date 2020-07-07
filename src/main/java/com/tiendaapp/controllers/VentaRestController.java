package com.tiendaapp.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendaapp.models.entity.Venta;
import com.tiendaapp.models.services.ClienteService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class VentaRestController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/ventas/{id}")
	public ResponseEntity<?> showVenta(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Optional<Venta> optional = clienteService.findVentaById(id);
			if(optional.isPresent()) {
				return new ResponseEntity<Optional<Venta>>(optional, HttpStatus.OK);
			} else {
				response.put("message", "Error er");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.put("message", "Error ");
			response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/ventas")
	public ResponseEntity<?> createVenta(@RequestBody Venta venta) {
		Map<String, Object> response = new HashMap<>();
		try {
			Venta newVenta = clienteService.saveVenta(venta);
			return new ResponseEntity<Venta>(newVenta, HttpStatus.CREATED);
		} catch (Exception e) {
			response.put("message", "Error ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/ventas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Optional<Venta> venta = clienteService.findVentaById(id);
			if (venta.isPresent()) {
				clienteService.deleteVenteById(id);
				response.put("mensaje ", "Venta ha sido eliminado con exito!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			} else {
				response.put("mensaje", "400");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

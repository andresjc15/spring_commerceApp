package com.tiendaapp.controllers;

import java.util.HashMap;
import java.util.List;
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

import com.tiendaapp.models.entity.ItemVenta;
import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.entity.Venta;
import com.tiendaapp.models.services.ClienteService;
import com.tiendaapp.models.services.ProductoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class VentaRestController {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ProductoService productoService;
	
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
			List<ItemVenta> items = venta.getItems();
			for (int i = 0; i < items.size(); i++) {
				Optional<Producto> stockProducto = productoService.findById(items.get(i).getProducto().getId());
				if (items.get(i).getCantidad() > stockProducto.get().getCantidad()) {
					response.put("msg", "Producto: " + items.get(i).getProducto().getNombre());
					response.put("det", " No hay stock suficiente");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
				} else {
					Integer newCantidadP = stockProducto.get().getCantidad() - items.get(i).getCantidad();
					if (newCantidadP == 0) {
						stockProducto.get().setEstado("AGOTADO");
					}
					stockProducto.get().setCantidad(newCantidadP);
					productoService.save(stockProducto.get());
				}
			}
			Venta newVenta = clienteService.saveVenta(venta);
			return new ResponseEntity<Venta>(newVenta, HttpStatus.CREATED);
		} catch (Exception e) {
			response.put("message", e.getMessage());
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

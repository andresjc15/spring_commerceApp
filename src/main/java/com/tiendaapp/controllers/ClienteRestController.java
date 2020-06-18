package com.tiendaapp.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tiendaapp.models.entity.Cliente;
import com.tiendaapp.models.services.ClienteService;
import com.tiendaapp.models.services.UploadFileService;

@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	@GetMapping(path = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> index() {
		try {
			List<Cliente> clientes = clienteService.findAll();
			return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Cliente>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/clientes/page/{page}")
	public ResponseEntity<?> indexPage(@PathVariable Integer page) {
		try {
			Pageable pageable = PageRequest.of(page, 5);
			Page<Cliente> paginator = clienteService.findAll(pageable);
			return new ResponseEntity<Page<Cliente>>(paginator, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Page<Cliente>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Cliente> cliente = clienteService.findById(id);
			if (cliente.isPresent()) {
				return new ResponseEntity<Cliente>(cliente.get(), HttpStatus.OK);
			} else {
				response.put("mensaje", "El cliente no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors()
						.stream()
						.map(err -> "El campo '" + err.getField() +"' " + err.getDefaultMessage())
						.collect(Collectors.toList());
				
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			} else {
				Cliente newCliente = clienteService.save(cliente);
				return new ResponseEntity<Cliente>(newCliente, HttpStatus.CREATED);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente,
			BindingResult result, @PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			if(result.hasErrors()) {
				List<String> errors = result.getFieldErrors()
						.stream()
						.map(err -> "El campo '" + err.getField() +"' " + err.getDefaultMessage())
						.collect(Collectors.toList());
				
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			} else {
				Optional<Cliente> clienteActual = clienteService.findById(id);
				if (clienteActual.isEmpty()) {
					response.put("mensaje", "Error: no se pudo editar, el cliente ID: "
							.concat(id.toString().concat(" no existe en la base de datos!")));
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				} else {
					clienteActual.get().setApellido(cliente.getApellido());
					clienteActual.get().setNombre(cliente.getNombre());
					clienteActual.get().setEmail(cliente.getEmail());
					clienteActual.get().setObservacion(cliente.getObservacion());
					clienteActual.get().setEstado(cliente.getEstado());
					clienteActual.get().setFechaNac(cliente.getFechaNac());
					
					 Cliente clienteUpdated = clienteService.save(clienteActual.get());
					 
					response.put("mensaje", "El cliente ha sido actualizado con exito!");
					response.put("cliente", clienteUpdated);
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
				}
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Optional<Cliente> cliente = clienteService.findById(id);
			if (cliente.isEmpty()) {
				String nombreFotoAnterior = cliente.get().getFoto();
				
				uploadFileService.eliminar(nombreFotoAnterior);
				clienteService.deleteById(id);
				response.put("mensaje ", "El cliente ha sido eliminado con exito!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			} else {
				response.put("mensaje", "El cliente ha sido actualizado con exito!");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar al cliente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findByIdCliente(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = null;
			
			try {
				nombreArchivo = uploadFileService.copiar(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = cliente.getFoto();
			uploadFileService.eliminar(nombreFotoAnterior);
			
			cliente.setFoto(nombreArchivo);
			
			try {
				clienteService.save(cliente);
			} catch (Exception e) {
			}
			
			response.put("cliente", cliente);
			response.put("mensaje", "has subi correctamente la imagen" + nombreArchivo);
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
		
		Resource recurso = null;
		
		try {
			recurso = uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//dowload
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
		
	}
	
}

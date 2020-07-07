package com.tiendaapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendaapp.models.entity.Cliente;
import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.services.ProductoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ProductoRestController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("/productos/filtro/{term}")
	public ResponseEntity<List<Producto>> filtrarProducto(@PathVariable String term) {
		try {
			List<Producto> productos = productoService.findByNombreContainingIgnoreCase(term);
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/productos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> index() {
		try {
			List<Producto> productos = productoService.findAll();
			return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/productos/page/{page}")
	public ResponseEntity<?> indexPage(@PathVariable Integer page) {
		Map<String, Object> response = new HashMap<>();
		try {
			Pageable pageable = PageRequest.of(page, 20);
			Boolean term = true;
			Page<Producto> paginator = productoService.findAll(term, pageable);
			return new ResponseEntity<Page<Producto>>(paginator, HttpStatus.OK);
		} catch (Exception e) {
			response.put("error", e.getMessage().concat(": "));
			return new ResponseEntity<Page<Producto>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Producto> producto = productoService.findById(id);
			if (producto.isPresent()) {
				return new ResponseEntity<Producto>(producto.get(), HttpStatus.OK);
			} else {
				response.put("mensaje", "El producto no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/productos")
	public ResponseEntity<?> create(@Valid @RequestBody Producto producto, BindingResult result) {
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
				if (producto.getDescuento() == null) {
					producto.setDescuento(0.0);
				}
				producto.setEstado("EN STOCK");
				Producto newProducto = productoService.save(producto);
				return new ResponseEntity<Producto>(newProducto, HttpStatus.CREATED);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/productos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Producto producto,
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
				Optional<Producto> productoActual = productoService.findById(id);
				if (productoActual.isEmpty()) {
					response.put("mensaje", "Error: no se pudo editar, el producto ID: "
							.concat(id.toString().concat(" no existe en la base de datos!")));
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				} else {
					productoActual.get().setNombre(producto.getNombre());
					productoActual.get().setDescripcion(producto.getDescripcion());
					
					 Producto productoUpdated = productoService.save(productoActual.get());
					 
					response.put("mensaje", "El producto ha sido actualizado con exito!");
					response.put("producto", productoUpdated);
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
	
	@DeleteMapping("/productos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			Optional<Producto> productoActual = productoService.findById(id);
			if (productoActual.isEmpty()) {
				response.put("mensaje", "Error: no se pudo eliminar, el producto ID: "
						.concat(id.toString().concat(" no existe en la base de datos!")));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			} else {
				productoActual.get().setEstado("ELIMINADO");
				
				 Producto productoUpdated = productoService.save(productoActual.get());
				 
				response.put("mensaje", "El producto ha sido ELIMINADO con exito!");
				response.put("producto", productoUpdated);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

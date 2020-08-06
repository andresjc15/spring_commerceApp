package com.tiendaapp.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.Cliente;
import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.entity.SubCategoria;
import com.tiendaapp.models.services.CategoriaService;
import com.tiendaapp.models.services.ProductoService;
import com.tiendaapp.models.services.SubCategoriaService;
import com.tiendaapp.models.services.UploadFileService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	@GetMapping(path = "/categorias", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> indexCategorias() {
		try {
			List<Categoria> categorias = categoriaService.findAll();
			return new ResponseEntity<List<Categoria>>(categorias, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/categorias/{id}")
	public ResponseEntity<?> showCategoria(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Categoria> categoria = categoriaService.findById(id);
			if (categoria.isPresent()) {
				return new ResponseEntity<Categoria>(categoria.get(), HttpStatus.OK);
			} else {
				response.put("mensaje", "Categoria no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/categorias")
	public ResponseEntity<?> createCategoria(@Valid @RequestBody Categoria categoria, BindingResult result) {
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
				Categoria newCategoria = categoriaService.save(categoria);
				return new ResponseEntity<Categoria>(newCategoria, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/cat/{nombre}/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> indexProductosByCategoria(@PathVariable("nombre") String nombre, @PathVariable("page") Integer page) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<Categoria> categoria = categoriaService.findByNombre(nombre);
			if (categoria.isPresent()) {
				Pageable pageable = PageRequest.of(page, 20);
				Page<Producto> paginator = productoService.findByNombreCategoria(categoria.get(), pageable);
				return new ResponseEntity<Page<Producto>>(paginator, HttpStatus.OK);
			} else {
				response.put("msg", "Categoria no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/categorias/{id}/sub", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> indexSubCategoriasById(@PathVariable Long id) {
		try {
			List<SubCategoria> subCategorias = categoriaService.findById(id).get().getSubCategorias();
			return new ResponseEntity<List<SubCategoria>>(subCategorias, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/categorias/subn/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> indexSubCategoriasByNombre(@PathVariable String nombre) {
		try {
			List<SubCategoria> subCategorias = categoriaService.findByNombre(nombre).get().getSubCategorias();
			return new ResponseEntity<List<SubCategoria>>(subCategorias, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/sub/{nombre}/page/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> indexProductosBySubCategorias(@PathVariable("nombre") String nombre, @PathVariable("page") Integer page) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<SubCategoria> subCategoria = subCategoriaService.findByNombre(nombre);
			if (subCategoria.isPresent()) {
				Pageable pageable = PageRequest.of(page, 20);
				Page<Producto> paginator = productoService.findByNombreSubCategoria(subCategoria.get(), pageable);
				return new ResponseEntity<Page<Producto>>(paginator, HttpStatus.OK);
			} else {
				response.put("msg", "Subcategoria no existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/categorias/{id}/sub")
	public ResponseEntity<?> createSubCategoria(@Valid @RequestBody SubCategoria subCategoria, BindingResult result,
			@PathVariable Long id) {
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
				Categoria categoria = categoriaService.findById(id).get();
				subCategoria.setCategoria(categoria);
				SubCategoria newSubCategoria = subCategoriaService.save(subCategoria);
				return new ResponseEntity<SubCategoria>(newSubCategoria, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/subcategoria/upload")
	public ResponseEntity<?> uploadSubCategoria(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		
		SubCategoria subCategoria = null;
		try {
			subCategoria = subCategoriaService.findById(id).get();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(!file.isEmpty()) {
			String nombreFile = null;
			
			try {
				nombreFile = uploadFileService.copiar(file);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen de la categoria");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String nombreFotoAnterior = subCategoria.getFoto();
			uploadFileService.eliminar(nombreFotoAnterior);
			
			subCategoria.setFoto(nombreFile);
			
			try {
				subCategoriaService.save(subCategoria);
			} catch (Exception e) {
			}
			
			response.put("subCategoria", subCategoria);
			response.put("mensaje", "has subi correctamente la imagen" + nombreFile);
		}
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

}

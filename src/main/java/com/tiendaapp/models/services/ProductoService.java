package com.tiendaapp.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.entity.SubCategoria;

public interface ProductoService extends CrudService<Producto, Long> {
	
	public List<Producto> findByNombreContainingIgnoreCase(String term);
	
	public List<Producto> findByNombreContainingIgnoreCaseAndByEstado(String term, String estado);
	
	public Page<Producto> findByNombreContainingIgnoreCaseAndisActivePage(String term, Boolean term2, Pageable pageable);
	
	public Page<Producto> findAll(Boolean term, Pageable pageable);
	
	public Page<Producto> findAllByEstado(String term, Pageable pageable);
	
	public Page<Producto> findByNombreSubCategoria(SubCategoria subCategoria, Pageable pageable);
	
	public Page<Producto> findByNombreCategoria(Categoria categoria, Pageable pageable);

}

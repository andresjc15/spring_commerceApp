package com.tiendaapp.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tiendaapp.models.entity.Producto;

public interface ProductoService extends CrudService<Producto, Long> {
	
	public List<Producto> findByNombreContainingIgnoreCase(String term);
	
	public Page<Producto> findAll(Boolean term, Pageable pageable);

}

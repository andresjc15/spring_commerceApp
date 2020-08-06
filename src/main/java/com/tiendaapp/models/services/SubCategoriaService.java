package com.tiendaapp.models.services;

import java.util.Optional;

import com.tiendaapp.models.entity.SubCategoria;

public interface SubCategoriaService extends CrudService<SubCategoria, Long> {
	
	public Optional<SubCategoria> findByNombre(String nombre);

}

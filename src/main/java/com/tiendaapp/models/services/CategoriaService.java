package com.tiendaapp.models.services;

import java.util.List;
import java.util.Optional;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.SubCategoria;

public interface CategoriaService extends CrudService<Categoria, Long> {
	
	public Optional<Categoria> findByNombre(String nombre);
	
	public List<SubCategoria> findAllByNombre(String nombre);

}

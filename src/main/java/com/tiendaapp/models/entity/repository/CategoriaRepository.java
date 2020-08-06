package com.tiendaapp.models.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.SubCategoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public Optional<Categoria> findByNombre(String nombre);
	
	public List<SubCategoria> findAllByNombre(String nombre);

}

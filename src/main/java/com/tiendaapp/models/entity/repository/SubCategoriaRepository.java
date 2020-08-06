package com.tiendaapp.models.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendaapp.models.entity.SubCategoria;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {
	
	public Optional<SubCategoria> findByNombre(String nombre);

}

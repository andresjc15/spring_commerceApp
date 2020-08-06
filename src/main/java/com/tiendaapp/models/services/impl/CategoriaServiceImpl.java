package com.tiendaapp.models.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.SubCategoria;
import com.tiendaapp.models.entity.repository.CategoriaRepository;
import com.tiendaapp.models.services.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Categoria> findAll() throws Exception {
		return categoriaRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Categoria> findById(Long id) throws Exception {
		return categoriaRepository.findById(id);
	}

	@Transactional
	@Override
	public Categoria save(Categoria entity) throws Exception {
		return categoriaRepository.save(entity);
	}

	@Transactional
	@Override
	public Categoria update(Categoria entity) throws Exception {
		return categoriaRepository.save(entity);
	}
	
	@Transactional
	@Override
	public void deleteById(Long id) throws Exception {
		categoriaRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Categoria> findByNombre(String nombre) {
		return categoriaRepository.findByNombre(nombre);
	}

	@Override
	public List<SubCategoria> findAllByNombre(String nombre) {
		return categoriaRepository.findAllByNombre(nombre);
	}
}

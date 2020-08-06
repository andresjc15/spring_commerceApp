package com.tiendaapp.models.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.SubCategoria;
import com.tiendaapp.models.entity.repository.SubCategoriaRepository;
import com.tiendaapp.models.services.SubCategoriaService;

@Service
public class SubCategoriaServiceImpl implements SubCategoriaService {
	
	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	@Transactional(readOnly = true)
	@Override
	public List<SubCategoria> findAll() throws Exception {
		return subCategoriaRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<SubCategoria> findById(Long id) throws Exception {
		return subCategoriaRepository.findById(id);
	}

	@Transactional
	@Override
	public SubCategoria save(SubCategoria entity) throws Exception {
		return subCategoriaRepository.save(entity);
	}

	@Transactional
	@Override
	public SubCategoria update(SubCategoria entity) throws Exception {
		return subCategoriaRepository.save(entity);
	}

	@Transactional
	@Override
	public void deleteById(Long id) throws Exception {
		subCategoriaRepository.deleteById(id);
	}

	@Override
	public Optional<SubCategoria> findByNombre(String nombre) {
		return subCategoriaRepository.findByNombre(nombre);
	}

}

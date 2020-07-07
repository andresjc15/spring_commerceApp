package com.tiendaapp.models.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.entity.repository.ProductoRepository;
import com.tiendaapp.models.services.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Producto> findByNombreContainingIgnoreCase(String term) {
		return productoRepository.findByNombreContainingIgnoreCase(term);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Producto> findAll() throws Exception {
		return productoRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Producto> findById(Long id) throws Exception {
		return productoRepository.findById(id);
	}

	@Transactional
	@Override
	public Producto save(Producto entity) throws Exception {
		return productoRepository.save(entity);
	}

	@Transactional
	@Override
	public Producto update(Producto entity) throws Exception {
		return productoRepository.save(entity);
	}

	@Transactional
	@Override
	public void deleteById(Long id) throws Exception {
		productoRepository.deleteById(id);
	}

	@Transactional
	@Override
	public Page<Producto> findAll(Boolean term, Pageable pageable) {
		return productoRepository.findAllByisActive(term, pageable);
	}

}

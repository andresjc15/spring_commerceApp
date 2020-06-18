package com.tiendaapp.models.services.impl;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tiendaapp.models.entity.Cliente;
import com.tiendaapp.models.entity.Venta;
import com.tiendaapp.models.entity.repository.ClienteRepository;
import com.tiendaapp.models.entity.repository.VentaRepository;
import com.tiendaapp.models.services.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private VentaRepository ventaRepository;

	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() throws Exception {
		return clienteRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Cliente> findById(Long id) throws Exception {
		return clienteRepository.findById(id);
	}

	@Transactional
	@Override
	public Cliente save(Cliente entity) throws Exception {
		return clienteRepository.save(entity);
	}

	@Transactional
	@Override
	public Cliente update(Cliente entity) throws Exception {
		return clienteRepository.save(entity);
	}

	@Transactional
	@Override
	public void deleteById(Long id) throws Exception {
		clienteRepository.deleteById(id);
	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	public Optional<Venta> findVentaById(Long id) {
		return ventaRepository.findById(id);
	}

	@Override
	public Venta saveVenta(Venta venta) {
		return ventaRepository.save(venta);
	}

	@Override
	public void deleteVenteById(Long id) {
		ventaRepository.deleteById(id);
	}

	@Override
	public Cliente findByIdCliente(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

}

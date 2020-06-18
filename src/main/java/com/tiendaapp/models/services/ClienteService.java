package com.tiendaapp.models.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tiendaapp.models.entity.Cliente;
import com.tiendaapp.models.entity.Venta;

public interface ClienteService extends CrudService<Cliente, Long> {
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente findByIdCliente(Long id);
	
	public Optional<Venta> findVentaById(Long id);
	
	public Venta saveVenta(Venta venta);
	
	public void deleteVenteById(Long id);

}

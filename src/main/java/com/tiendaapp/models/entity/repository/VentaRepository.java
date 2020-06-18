package com.tiendaapp.models.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tiendaapp.models.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

}

package com.tiendaapp.models.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tiendaapp.models.entity.Categoria;
import com.tiendaapp.models.entity.Producto;
import com.tiendaapp.models.entity.SubCategoria;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);
	
	public List<Producto> findByNombreContainingIgnoreCase(String term);
	
	public List<Producto> findByNombreContainingIgnoreCaseAndEstado(String term, String estado);
	
	public Page<Producto> findByNombreContainingIgnoreCase(String term, Pageable pageable);
	
	public Page<Producto> findAllByisActive(Boolean term, Pageable pageable);
	
	public Page<Producto> findAllByEstado(String term, Pageable pageable);
	
	public Page<Producto> findBySubCategoria(SubCategoria subCategoria, Pageable pageable);
	
	public Page<Producto> findBySubCategoriaCategoria(Categoria categoria, Pageable pageable);

}

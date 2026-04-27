package com.example.joyarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{

	List<Producto> findByNombreContainingIgnoreCase(String texto);
    List<Producto> findByCategoria_NombreAndPrecioBetween(String nombreCategoria, Double min, Double max);
    List<Producto> findTop8ByOrderByIdDesc();
    List<Producto> findByUsuarioIdAndStockLessThan(Long usuarioId, int cantidad);
    List<Producto> findByNombreContainingIgnoreCaseAndPrecioBetween(String nombre, Double min, Double max);
    List<Producto> findByPrecioBetween(Double min, Double max);
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria_NombreAndPrecioBetween(
        String nombre, String categoria, Double min, Double max);
    List<Producto> findByCategoria_Nombre(String nombreCategoria);
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria_Nombre(String nombre, String nombreCategoria);
    List<Producto> findByUsuarioId(Long usuarioId);
	
}

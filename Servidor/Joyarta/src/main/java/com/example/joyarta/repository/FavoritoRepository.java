package com.example.joyarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Favorito;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long>{
	
	boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
	
	void deleteByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
	
	List<Favorito> findByUsuarioId(Long usuarioId);
}

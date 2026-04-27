package com.example.joyarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.joyarta.model.Favorito;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long>{
	
	boolean existsByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
	
	@Modifying
	@Transactional
	void deleteByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
	
	java.util.List<Favorito> findByUsuarioId(Long usuarioId);
}

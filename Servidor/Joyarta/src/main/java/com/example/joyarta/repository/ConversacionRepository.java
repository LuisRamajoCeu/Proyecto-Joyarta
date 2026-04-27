package com.example.joyarta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Conversacion;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {
	
	List<Conversacion> findByUsuario1IdOrUsuario2IdOrderByFechaCreacionDesc(Long idUsuario1, Long idUsuario2);
	
	Optional<Conversacion> findByUsuario1IdAndUsuario2IdOrUsuario1IdAndUsuario2Id(
		Long idUsuario1A, Long idUsuario2A, Long idUsuario1B, Long idUsuario2B);
	
	Optional<Conversacion> findByUsuario1IdAndUsuario2IdAndProductoIdOrUsuario1IdAndUsuario2IdAndProductoId(
		Long idUsuario1A, Long idUsuario2A, Long idProductoA, 
		Long idUsuario1B, Long idUsuario2B, Long idProductoB);
}

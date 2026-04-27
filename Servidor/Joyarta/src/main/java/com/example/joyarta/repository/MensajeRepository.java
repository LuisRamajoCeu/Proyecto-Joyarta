package com.example.joyarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
	
	List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Long conversacionId);
}

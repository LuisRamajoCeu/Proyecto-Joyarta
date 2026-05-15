package com.example.joyarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

	List<Solicitud> findByClienteId(Long clienteId);

	List<Solicitud> findByEstado(String estado);

	List<Solicitud> findAllByOrderByFechaCreacionDesc();
}

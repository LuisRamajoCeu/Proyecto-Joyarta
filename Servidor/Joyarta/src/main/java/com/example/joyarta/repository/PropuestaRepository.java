package com.example.joyarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Propuesta;

@Repository
public interface PropuestaRepository extends JpaRepository<Propuesta, Long> {

	List<Propuesta> findBySolicitudId(Long solicitudId);

	List<Propuesta> findByArtesanoId(Long artesanoId);
}

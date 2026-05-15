package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Propuesta;

public interface PropuestaService {

	Propuesta crear(Propuesta propuesta);

	List<Propuesta> getBySolicitudId(Long solicitudId);

	List<Propuesta> getByArtesanoId(Long artesanoId);

	Propuesta getById(Long id);

	Propuesta aceptar(Long id);

	Propuesta rechazar(Long id);
}

package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Solicitud;

public interface SolicitudService {

	Solicitud crear(Solicitud solicitud);

	List<Solicitud> getAll();

	Solicitud getById(Long id);

	void delete(Long id);

	List<Solicitud> getByClienteId(Long clienteId);

	List<Solicitud> getAbiertas();
}

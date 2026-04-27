package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Mensaje;

public interface MensajeService {

	Mensaje crearMensaje(Mensaje mensaje);
	
	List<Mensaje> getMensajesByConversacion(Long idConversacion);

	List<Mensaje> getAll();

	Mensaje getById(Long id);

	Mensaje update(Long id, Mensaje actualizar);

	void delete(Long id);
	
	List<Mensaje> obtenerHistorialChat(Long idConversacion);
}

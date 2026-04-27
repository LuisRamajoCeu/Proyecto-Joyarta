package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Conversacion;

public interface ConversacionService {

	List<Conversacion> getConversacionesByUsuario(Long idUsuario);
	
	Conversacion crearConversacion(Long idUsuario1, Long idUsuario2, Long idProducto);
	
	Conversacion buscarConversacion(Long idUsuario1, Long idUsuario2);
	
	Conversacion buscarConversacionPorProducto(Long idUsuario1, Long idUsuario2, Long idProducto);
	
	Conversacion getById(Long id);
	
	List<Conversacion> getAll();
	Conversacion update(Long id, Conversacion actualizar);
	void delete(Long id);

}

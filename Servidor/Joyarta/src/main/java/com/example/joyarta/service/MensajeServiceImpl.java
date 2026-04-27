package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Mensaje;
import com.example.joyarta.repository.MensajeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MensajeServiceImpl implements MensajeService{

	@Autowired
	private MensajeRepository mensRepo;
	
	@Override
	public Mensaje crearMensaje(Mensaje mensaje) {
		return mensRepo.save(mensaje);
	}
	
	@Override
	public List<Mensaje> getMensajesByConversacion(Long idConversacion) {
		return mensRepo.findByConversacionIdOrderByFechaEnvioAsc(idConversacion);
	}
	
	@Override
	public List<Mensaje> getAll() {
		return mensRepo.findAll();
	}

	@Override
	public Mensaje getById(Long id) {
		return mensRepo.findById(id).orElse(null);
	}

	@Override
	public Mensaje update(Long id, Mensaje actualizar) {
		Mensaje existente = mensRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setContenido(actualizar.getContenido());
			return mensRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		mensRepo.deleteById(id);
	}
	
	@Override
    public List<Mensaje> obtenerHistorialChat(Long idConversacion) {
        return mensRepo.findByConversacionIdOrderByFechaEnvioAsc(idConversacion);
    }
	
}

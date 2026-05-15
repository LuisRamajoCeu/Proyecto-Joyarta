package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Solicitud;
import com.example.joyarta.repository.SolicitudRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SolicitudServiceImpl implements SolicitudService {

	@Autowired
	private SolicitudRepository solicitudRepo;

	@Override
	public Solicitud crear(Solicitud solicitud) {
		return solicitudRepo.save(solicitud);
	}

	@Override
	public List<Solicitud> getAll() {
		return solicitudRepo.findAllByOrderByFechaCreacionDesc();
	}

	@Override
	public Solicitud getById(Long id) {
		return solicitudRepo.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		solicitudRepo.deleteById(id);
	}

	@Override
	public List<Solicitud> getByClienteId(Long clienteId) {
		return solicitudRepo.findByClienteId(clienteId);
	}

	@Override
	public List<Solicitud> getAbiertas() {
		return solicitudRepo.findByEstado("ABIERTA");
	}
}

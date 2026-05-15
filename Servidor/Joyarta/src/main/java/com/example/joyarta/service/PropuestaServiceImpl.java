package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Propuesta;
import com.example.joyarta.model.Solicitud;
import com.example.joyarta.repository.PropuestaRepository;
import com.example.joyarta.repository.SolicitudRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PropuestaServiceImpl implements PropuestaService {

	@Autowired
	private PropuestaRepository propuestaRepo;

	@Autowired
	private SolicitudRepository solicitudRepo;

	@Override
	public Propuesta crear(Propuesta propuesta) {
		return propuestaRepo.save(propuesta);
	}

	@Override
	public List<Propuesta> getBySolicitudId(Long solicitudId) {
		return propuestaRepo.findBySolicitudId(solicitudId);
	}

	@Override
	public List<Propuesta> getByArtesanoId(Long artesanoId) {
		return propuestaRepo.findByArtesanoId(artesanoId);
	}

	@Override
	public Propuesta getById(Long id) {
		return propuestaRepo.findById(id).orElse(null);
	}

	@Override
	public Propuesta aceptar(Long id) {
		Propuesta propuesta = propuestaRepo.findById(id).orElse(null);
		if (propuesta != null) {
			propuesta.setEstado("ACEPTADA");
			propuestaRepo.save(propuesta);

			Solicitud solicitud = propuesta.getSolicitud();
			solicitud.setEstado("EN_PROCESO");
			solicitudRepo.save(solicitud);

			List<Propuesta> otras = propuestaRepo.findBySolicitudId(solicitud.getId());
			for (Propuesta otra : otras) {
				if (!otra.getId().equals(id) && "PENDIENTE".equals(otra.getEstado())) {
					otra.setEstado("RECHAZADA");
					propuestaRepo.save(otra);
				}
			}
		}
		return propuesta;
	}

	@Override
	public Propuesta rechazar(Long id) {
		Propuesta propuesta = propuestaRepo.findById(id).orElse(null);
		if (propuesta != null) {
			propuesta.setEstado("RECHAZADA");
			propuestaRepo.save(propuesta);
		}
		return propuesta;
	}
}

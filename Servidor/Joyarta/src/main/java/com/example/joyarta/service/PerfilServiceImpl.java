package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Perfil;
import com.example.joyarta.repository.PerfilRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PerfilServiceImpl implements PerfilService{

	@Autowired
	private PerfilRepository perfilRepo; 
	
	@Override
	public Perfil crearPerfil(Perfil perfil) {
		return perfilRepo.save(perfil);
	}
	
	@Override
	public List<Perfil> getAll() {
		return perfilRepo.findAll();
	}

	@Override
	public Perfil getById(Long id) {
		return perfilRepo.findById(id).orElse(null);
	}

	@Override
	public Perfil update(Long id, Perfil actualizar) {
		Perfil existente = perfilRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setNombre(actualizar.getNombre());
	        existente.setBiografia(actualizar.getBiografia());
	        existente.setAvatarUrl(actualizar.getAvatarUrl());
	        existente.setDireccion(actualizar.getDireccion());
			return perfilRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		perfilRepo.deleteById(id);
	}
	
	
	
}

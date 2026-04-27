package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Perfil;

public interface PerfilService {

	Perfil crearPerfil(Perfil perfil);

	List<Perfil> getAll();

	Perfil getById(Long id);

	Perfil update(Long id, Perfil actualizar);

	void delete(Long id);

}

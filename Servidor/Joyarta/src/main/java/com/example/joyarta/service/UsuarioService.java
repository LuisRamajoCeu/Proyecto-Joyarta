package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Usuario;

public interface UsuarioService {

	Usuario crearUsuario(Usuario usuario);

	List<Usuario> getAll();

	Usuario getById(Long id);

	Usuario update(Long id, Usuario actualizar);

	void delete(Long id);

	boolean existeEmail(String email);

	Usuario findByEmail(String email);

	Usuario login(String email, String password);
}

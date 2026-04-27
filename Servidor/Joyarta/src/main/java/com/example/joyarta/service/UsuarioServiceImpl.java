package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Usuario;
import com.example.joyarta.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioRepository usuarioRepo; 
	
	@Override
	public Usuario crearUsuario(Usuario usuario) {
		if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
			if (usuario.getPerfil() != null && usuario.getPerfil().getNombre() != null) {
				usuario.setNombre(usuario.getPerfil().getNombre());
			}
		}
		if (usuario.getRol() == null || usuario.getRol().isBlank()) {
			usuario.setRol("CLIENTE");
		}
		if (usuario.getFechaRegistro() == null) {
			usuario.setFechaRegistro(java.time.LocalDate.now());
		}
		return usuarioRepo.save(usuario);
	}
	
	@Override
	public List<Usuario> getAll() {
		return usuarioRepo.findAll();
	}

	@Override
	public Usuario getById(Long id) {
		return usuarioRepo.findById(id).orElse(null);
	}

	@Override
	public Usuario update(Long id, Usuario actualizar) {
		Usuario existente = usuarioRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setEmail(actualizar.getEmail());
	        existente.setPassword(actualizar.getPassword());	        
	        existente.setRol(actualizar.getRol());
			return usuarioRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		usuarioRepo.deleteById(id);
	}
	
	@Override
    public boolean existeEmail(String email) {
        return usuarioRepo.existsByEmail(email);
    }

	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepo.findByEmail(email);
	}
	
	@Override
	public Usuario login(String email, String password) {
		Usuario usuario = usuarioRepo.findByEmail(email);
		if(usuario != null && usuario.getPassword().equals(password)) {
			return usuario;
		}
		return null;
	}
	
}

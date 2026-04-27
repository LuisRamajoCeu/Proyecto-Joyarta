package com.example.joyarta.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Conversacion;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.repository.ConversacionRepository;
import com.example.joyarta.repository.ProductoRepository;
import com.example.joyarta.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ConversacionServiceImpl implements ConversacionService{

	@Autowired
	private ConversacionRepository convRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	@Override
	public List<Conversacion> getConversacionesByUsuario(Long idUsuario) {
		return convRepo.findByUsuario1IdOrUsuario2IdOrderByFechaCreacionDesc(idUsuario, idUsuario);
	}
	
	@Override
	public Conversacion crearConversacion(Long idUsuario1, Long idUsuario2, Long idProducto) {
		Conversacion existente = null;
		if (idProducto != null) {
			existente = convRepo.findByUsuario1IdAndUsuario2IdAndProductoIdOrUsuario1IdAndUsuario2IdAndProductoId(
				idUsuario1, idUsuario2, idProducto, idUsuario2, idUsuario1, idProducto).orElse(null);
		} else {
			existente = convRepo.findByUsuario1IdAndUsuario2IdOrUsuario1IdAndUsuario2Id(
				idUsuario1, idUsuario2, idUsuario2, idUsuario1).orElse(null);
		}
		
		if (existente != null) {
			return existente;
		}
		
		Conversacion nueva = new Conversacion();
		nueva.setFechaCreacion(LocalDate.now());
		nueva.setActivo(true);
		
		Usuario usuario1 = usuarioRepo.findById(idUsuario1).orElse(null);
		Usuario usuario2 = usuarioRepo.findById(idUsuario2).orElse(null);
		
		if (usuario1 == null || usuario2 == null) {
			return null;
		}
		
		nueva.setUsuario1(usuario1);
		nueva.setUsuario2(usuario2);
		
		if (idProducto != null) {
			Producto producto = productoRepo.findById(idProducto).orElse(null);
			nueva.setProducto(producto);
		}
		
		return convRepo.save(nueva);
	}
	
	@Override
	public Conversacion buscarConversacion(Long idUsuario1, Long idUsuario2) {
		return convRepo.findByUsuario1IdAndUsuario2IdOrUsuario1IdAndUsuario2Id(
			idUsuario1, idUsuario2, idUsuario2, idUsuario1).orElse(null);
	}
	
	@Override
	public Conversacion buscarConversacionPorProducto(Long idUsuario1, Long idUsuario2, Long idProducto) {
		return convRepo.findByUsuario1IdAndUsuario2IdAndProductoIdOrUsuario1IdAndUsuario2IdAndProductoId(
			idUsuario1, idUsuario2, idProducto, idUsuario2, idUsuario1, idProducto).orElse(null);
	}
	
	@Override
	public List<Conversacion> getAll() {
		return convRepo.findAll();
	}

	@Override
	public Conversacion getById(Long id) {
		return convRepo.findById(id).orElse(null);
	}

	@Override
	public Conversacion update(Long id, Conversacion actualizar) {
		Conversacion existente = convRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setActivo(actualizar.getActivo());
			return convRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		convRepo.deleteById(id);
	}
	
}

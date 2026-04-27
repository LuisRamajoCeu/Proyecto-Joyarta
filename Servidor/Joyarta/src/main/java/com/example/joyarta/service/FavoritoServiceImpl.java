package com.example.joyarta.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Favorito;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.repository.FavoritoRepository;
import com.example.joyarta.repository.ProductoRepository;
import com.example.joyarta.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FavoritoServiceImpl implements FavoritoService{

	@Autowired
	private FavoritoRepository favRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private ProductoRepository productoRepo;
	
	@Override
	public Favorito crearFavorito(Favorito favorito) {
		return favRepo.save(favorito);
	}
	
	@Override
	public List<Favorito> getAll() {
		return favRepo.findAll();
	}

	@Override
	public Favorito getById(Long id) {
		return favRepo.findById(id).orElse(null);
	}

	@Override
	public Favorito update(Long id, Favorito actualizar) {
		Favorito existente = favRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setFechaAgregado(actualizar.getFechaAgregado());
			return favRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		favRepo.deleteById(id);
	}
	
	@Override
    public boolean esFavorito(Long idUsuario, Long idProducto) {
        return favRepo.existsByUsuarioIdAndProductoId(idUsuario, idProducto);
    }
	
	@Override
	public void darLike(Long idUsuario, Long idProducto) {
		if (favRepo.existsByUsuarioIdAndProductoId(idUsuario, idProducto)) {
            return;
        }
        Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
        Producto producto = productoRepo.findById(idProducto).orElse(null);
        if (usuario != null && producto != null) {
            Favorito fav = new Favorito();
            fav.setUsuario(usuario);
            fav.setProducto(producto);
            fav.setFechaAgregado(LocalDate.now());
            favRepo.save(fav);
        }
	}
	
	@Override
	public void quitarLike(Long idUsuario, Long idProducto) {
		if (!favRepo.existsByUsuarioIdAndProductoId(idUsuario, idProducto)) {
            return;
        }
		favRepo.deleteByUsuarioIdAndProductoId(idUsuario, idProducto);
	}

	@Override
	public List<Favorito> obtenerFavoritosUsuario(Long idUsuario) {
		return favRepo.findByUsuarioId(idUsuario);
	}
	
}

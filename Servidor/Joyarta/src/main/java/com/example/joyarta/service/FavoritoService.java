package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Favorito;

public interface FavoritoService {

	Favorito crearFavorito(Favorito favorito);

	List<Favorito> getAll();

	Favorito getById(Long id);

	Favorito update(Long id, Favorito actualizar);

	void delete(Long id);

	boolean esFavorito(Long idUsuario, Long idProducto);

	void darLike(Long idUsuario, Long idProducto);

	void quitarLike(Long idUsuario, Long idProducto);

	List<Favorito> obtenerFavoritosUsuario(Long idUsuario);

}

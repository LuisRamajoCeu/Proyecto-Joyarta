package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Categoria;

public interface CategoriaService {

	Categoria crearCategoria(Categoria categoria);

	List<Categoria> getAll();

	Categoria getById(Long id);

	Categoria update(Long id, Categoria actualizar);

	void delete(Long id);

}

package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Categoria;
import com.example.joyarta.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService{

	@Autowired
	private CategoriaRepository catRepo;

	@Override
	public Categoria crearCategoria(Categoria categoria) {
		return catRepo.save(categoria);
	}
	
	@Override
	public List<Categoria> getAll() {
		return catRepo.findAll();
	}

	@Override
	public Categoria getById(Long id) {
		return catRepo.findById(id).orElse(null);
	}

	@Override
	public Categoria update(Long id, Categoria actualizar) {
		Categoria existente = catRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setNombre(actualizar.getNombre());
			existente.setDescripcion(actualizar.getDescripcion());
			return catRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		catRepo.deleteById(id);
	}
	
}

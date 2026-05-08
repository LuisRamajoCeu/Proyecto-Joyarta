package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Categoria;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.repository.CategoriaRepository;
import com.example.joyarta.repository.ProductoRepository;
import com.example.joyarta.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoRepository prodRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private CategoriaRepository catRepo;
	
	@Override
	public Producto crearProducto(Producto producto) {
		if (producto.getUsuario() != null && producto.getUsuario().getId() != null) {
			Usuario usuario = usuarioRepo.findById(producto.getUsuario().getId()).orElse(null);
			producto.setUsuario(usuario);
		}
		if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
			Categoria categoria = catRepo.findById(producto.getCategoria().getId()).orElse(null);
			producto.setCategoria(categoria);
		}
		return prodRepo.save(producto);
	}
	
	@Override
	public List<Producto> getAll() {
		return prodRepo.findAll();
	}

	@Override
	public Producto getById(Long id) {
		return prodRepo.findById(id).orElse(null);
	}

	@Override
	public Producto update(Long id, Producto actualizar) {
		Producto existente = prodRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setNombre(actualizar.getNombre());
	        existente.setDescripcion(actualizar.getDescripcion());
	        existente.setPrecio(actualizar.getPrecio());
	        existente.setStock(actualizar.getStock());
	        existente.setImagenUrl(actualizar.getImagenUrl());
			return prodRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		prodRepo.deleteById(id);
	}
	
	@Override
    public List<Producto> buscarPorNombre(String texto) {
        return prodRepo.findByNombreContainingIgnoreCase(texto);
    }

    @Override
    public List<Producto> filtrarCatalogo(String categoria, Double minPrecio, Double maxPrecio) {
        return prodRepo.findByCategoria_NombreAndPrecioBetween(categoria, minPrecio, maxPrecio);
    }

    @Override
    public List<Producto> obtenerUltimosProductos() {
        return prodRepo.findTop8ByOrderByIdDesc();
    }

    @Override
    public List<Producto> obtenerProductosConStockBajo(Long idArtesano, int cantidadMinima) {
        return prodRepo.findByUsuarioIdAndStockLessThan(idArtesano, cantidadMinima);
    }
    
    @Override
    public List<Producto> buscarConFiltros(String nombre, String categoria, Double precioMin, Double precioMax) {
        Double min = (precioMin != null) ? precioMin : 0.0;
        Double max = (precioMax != null) ? precioMax : Double.MAX_VALUE;
        
        boolean tieneNombre = nombre != null && !nombre.trim().isEmpty();
        boolean tieneCategoria = categoria != null && !categoria.trim().isEmpty();
        boolean tienePrecio = precioMin != null || precioMax != null;
        
        if (tieneNombre && tieneCategoria && tienePrecio) {
            return prodRepo.findByNombreContainingIgnoreCaseAndCategoria_NombreAndPrecioBetween(
                nombre, categoria, min, max);
        } else if (tieneNombre && tienePrecio) {
            return prodRepo.findByNombreContainingIgnoreCaseAndPrecioBetween(nombre, min, max);
        } else if (tieneCategoria && tienePrecio) {
            return prodRepo.findByCategoria_NombreAndPrecioBetween(categoria, min, max);
        } else if (tieneNombre && tieneCategoria) {
            return prodRepo.findByNombreContainingIgnoreCaseAndCategoria_Nombre(nombre, categoria);
        } else if (tieneNombre) {
            return prodRepo.findByNombreContainingIgnoreCase(nombre);
        } else if (tieneCategoria) {
            return prodRepo.findByCategoria_Nombre(categoria);
        } else if (tienePrecio) {
            return prodRepo.findByPrecioBetween(min, max);
        } else {
            return prodRepo.findAll();
        }
    }
	
    @Override
    public List<Producto> obtenerProductosPorUsuario(Long usuarioId) {
        return prodRepo.findByUsuarioId(usuarioId);
    }
	
}

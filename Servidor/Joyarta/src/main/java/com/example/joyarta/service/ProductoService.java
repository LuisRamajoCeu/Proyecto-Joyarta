package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Producto;

public interface ProductoService {

	Producto crearProducto(Producto producto);

	List<Producto> getAll();

	Producto getById(Long id);

	Producto update(Long id, Producto actualizar);

	void delete(Long id);

	List<Producto> buscarPorNombre(String texto);

	List<Producto> filtrarCatalogo(String categoria, Double minPrecio, Double maxPrecio);

	List<Producto> obtenerUltimosProductos();

	List<Producto> obtenerProductosConStockBajo(Long idArtesano, int cantidadMinima);
	
	List<Producto> buscarConFiltros(String nombre, String categoria, Double precioMin, Double precioMax);

    List<Producto> obtenerProductosPorUsuario(Long usuarioId);

}

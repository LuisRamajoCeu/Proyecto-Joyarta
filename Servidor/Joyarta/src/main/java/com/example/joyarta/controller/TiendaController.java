package com.example.joyarta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.joyarta.model.Categoria;
import com.example.joyarta.model.Producto;
import com.example.joyarta.service.CategoriaService;
import com.example.joyarta.service.ProductoService;

@RestController
@RequestMapping("/api/tienda")
@CrossOrigin(origins = "http://localhost:4200")
public class TiendaController {
	
	@Autowired private ProductoService productoService;
	@Autowired private CategoriaService categoriaService;
	
	@GetMapping("/productos")
	public ResponseEntity<List<Producto>> listarProductos(
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String categoria,
			@RequestParam(required = false) Double precioMin,
			@RequestParam(required = false) Double precioMax) {
		
		List<Producto> productos = productoService.buscarConFiltros(nombre, categoria, precioMin, precioMax);
        return ResponseEntity.ok(productos);
	}
	
	@PostMapping("/productos")
	public ResponseEntity<Producto> subirProducto(@RequestBody Producto producto){
		Producto creado = productoService.crearProducto(producto);
		if(creado != null) {
			return ResponseEntity.ok(creado);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/productos/{id}")
    public ResponseEntity<Producto> verProducto(@PathVariable Long id) {
        Producto producto = productoService.getById(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	@PutMapping("/productos/{id}")
	public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, 
			@RequestBody Producto producto) {
		Producto actualizado = productoService.update(id, producto);
		if (actualizado != null) {
			return ResponseEntity.ok(actualizado);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/productos/{id}")
	public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
		Producto existente = productoService.getById(id);
		if (existente != null) {
			productoService.delete(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/productos/recientes")
	public ResponseEntity<List<Producto>> obtenerRecientes() {
		List<Producto> recientes = productoService.obtenerUltimosProductos();
		return ResponseEntity.ok(recientes);
	}

	@GetMapping("/productos/stock-bajo/{idArtesano}")
	public ResponseEntity<List<Producto>> obtenerStockBajo(
			@PathVariable Long idArtesano,
			@RequestParam(defaultValue = "5") int minimo) {
		List<Producto> productos = productoService.obtenerProductosConStockBajo(idArtesano, minimo);
		return ResponseEntity.ok(productos);
	}
    
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        List<Categoria> categorias = categoriaService.getAll();
        return ResponseEntity.ok(categorias);
    }
	
}

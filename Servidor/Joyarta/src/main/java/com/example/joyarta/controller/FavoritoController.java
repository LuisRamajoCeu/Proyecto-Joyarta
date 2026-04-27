package com.example.joyarta.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joyarta.model.Favorito;
import com.example.joyarta.service.FavoritoService;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "http://localhost:4200")
public class FavoritoController {

	@Autowired
	private FavoritoService favoritoService;

	@PostMapping
	public ResponseEntity<String> agregarFavorito(@RequestBody Map<String, Long> datos) {
		Long idUsuario = datos.get("idUsuario");
		Long idProducto = datos.get("idProducto");

		if (idUsuario == null || idProducto == null) {
			return ResponseEntity.badRequest().body("idUsuario e idProducto son requeridos");
		}

		if (favoritoService.esFavorito(idUsuario, idProducto)) {
			return ResponseEntity.badRequest().body("El producto ya está en favoritos");
		}

		favoritoService.darLike(idUsuario, idProducto);
		return ResponseEntity.ok("Producto agregado a favoritos");
	}

	@DeleteMapping
	public ResponseEntity<String> quitarFavorito(@RequestBody Map<String, Long> datos) {
		Long idUsuario = datos.get("idUsuario");
		Long idProducto = datos.get("idProducto");

		if (idUsuario == null || idProducto == null) {
			return ResponseEntity.badRequest().body("idUsuario e idProducto son requeridos");
		}

		favoritoService.quitarLike(idUsuario, idProducto);
		return ResponseEntity.ok("Producto eliminado de favoritos");
	}

	@GetMapping
	public ResponseEntity<List<Favorito>> listarTodosFavoritos() {
		List<Favorito> favoritos = favoritoService.getAll();
		return ResponseEntity.ok(favoritos);
	}

	@GetMapping("/verificar/{idUsuario}/{idProducto}")
	public ResponseEntity<Boolean> verificarFavorito(
			@PathVariable("idUsuario") Long idUsuario, 
			@PathVariable("idProducto") Long idProducto) {
		boolean esFavorito = favoritoService.esFavorito(idUsuario, idProducto);
		return ResponseEntity.ok(esFavorito);
	}

	@GetMapping("/usuario/{idUsuario}")
	public ResponseEntity<List<Favorito>> getFavoritosByUsuario(
			@PathVariable("idUsuario") Long idUsuario) {
		List<Favorito> favoritos = favoritoService.obtenerFavoritosUsuario(idUsuario);
		return ResponseEntity.ok(favoritos);
	}
}

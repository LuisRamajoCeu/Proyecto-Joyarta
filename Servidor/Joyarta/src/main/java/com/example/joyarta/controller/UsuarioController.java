package com.example.joyarta.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joyarta.model.Perfil;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Pedido;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.service.PerfilService;
import com.example.joyarta.service.ProductoService;
import com.example.joyarta.service.PedidoService;
import com.example.joyarta.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

	@Autowired private UsuarioService usuarioService;
	@Autowired private PerfilService perfilService;
    @Autowired private ProductoService productoService;
    @Autowired private PedidoService pedidoService;
	
	
	@PostMapping("/registro")
	public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario){
		if (usuarioService.existeEmail(usuario.getEmail())) {
			return ResponseEntity.status(409).body("El email ya está registrado");
		}
		Usuario creado = usuarioService.crearUsuario(usuario);
		if(creado != null) {
			return ResponseEntity.ok(creado);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id){
		Usuario usuario = usuarioService.getById(id);
		if(usuario != null) {
			return ResponseEntity.ok(usuario);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}/perfil")
	public ResponseEntity<Perfil> getPerfilById(@PathVariable Long id){
		Usuario usuario = usuarioService.getById(id);
		if(usuario != null && usuario.getPerfil() != null) {
			Perfil perfil = usuario.getPerfil();
			return ResponseEntity.ok(perfil);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}/perfil")
	public ResponseEntity<Perfil> editarPerfil(@PathVariable Long id, 
			@RequestBody Perfil perfil){
		Usuario usuario = usuarioService.getById(id);
		if(usuario != null && usuario.getPerfil() != null) {
			Long idPerfil = usuario.getPerfil().getId();
			Perfil actualizado = perfilService.update(idPerfil, perfil);
			return ResponseEntity.ok(actualizado);			
		}
		return ResponseEntity.notFound().build();
	}

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<Producto>> getProductosUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductosPorUsuario(id));
    }

    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<Pedido>> getPedidosUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(id));
    }
	
	@PostMapping("/login")
	public ResponseEntity<Usuario> login(@RequestBody Map<String, String> datos) {
	    String email = datos.get("email");
	    String password = datos.get("password");
	    Usuario usuario = usuarioService.login(email, password);
	    if (usuario != null) {
	        return ResponseEntity.ok(usuario);
	    }
	    return ResponseEntity.status(401).build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<?> cambiarPassword(@PathVariable Long id,
			@RequestBody Map<String, String> datos) {
		Usuario usuario = usuarioService.getById(id);
		if (usuario == null) {
			return ResponseEntity.notFound().build();
		}
		String actual = datos.get("actual");
		String nueva = datos.get("nueva");
		if (actual == null || nueva == null || nueva.length() < 6) {
			return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres");
		}
		if (!usuario.getPassword().equals(actual)) {
			return ResponseEntity.status(403).body("La contraseña actual no es correcta");
		}
		usuario.setPassword(nueva);
		usuarioService.crearUsuario(usuario);
		return ResponseEntity.ok().body("Contraseña actualizada correctamente");
	}
	
}

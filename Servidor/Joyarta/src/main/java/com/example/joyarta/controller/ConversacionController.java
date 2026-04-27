package com.example.joyarta.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joyarta.model.Conversacion;
import com.example.joyarta.model.Mensaje;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.service.ConversacionService;
import com.example.joyarta.service.MensajeService;
import com.example.joyarta.service.UsuarioService;

@RestController
@RequestMapping("/api/conversaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class ConversacionController {
	
	@Autowired
	private ConversacionService conversacionService;
	
	@Autowired
	private MensajeService mensajeService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/usuario/{idUsuario}")
	public ResponseEntity<List<Conversacion>> getConversacionesByUsuario(@PathVariable(value = "idUsuario") Long idUsuario) {
		List<Conversacion> conversaciones = conversacionService.getConversacionesByUsuario(idUsuario);
		return ResponseEntity.ok(conversaciones);
	}
	
	@PostMapping
	public ResponseEntity<Conversacion> crearConversacion(@RequestBody CrearConversacionRequest request) {
		Conversacion conversacion = conversacionService.crearConversacion(
			request.getIdUsuario1(), 
			request.getIdUsuario2(), 
			request.getIdProducto()
		);
		
		if (conversacion != null) {
			return ResponseEntity.ok(conversacion);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/{id}/mensajes")
	public ResponseEntity<List<Mensaje>> getMensajes(@PathVariable(value = "id") Long id) {
		List<Mensaje> mensajes = mensajeService.getMensajesByConversacion(id);
		return ResponseEntity.ok(mensajes);
	}
	
	@PostMapping("/{id}/mensajes")
	public ResponseEntity<Mensaje> enviarMensaje(
			@PathVariable(value = "id") Long idConversacion,
			@RequestBody EnviarMensajeRequest request) {
		
		Conversacion conversacion = conversacionService.getById(idConversacion);
		if (conversacion == null) {
			return ResponseEntity.notFound().build();
		}
		
		Usuario usuario = usuarioService.getById(request.getIdUsuario());
		if (usuario == null) {
			return ResponseEntity.badRequest().build();
		}
		
		Mensaje mensaje = new Mensaje();
		mensaje.setContenido(request.getContenido());
		mensaje.setFechaEnvio(LocalDateTime.now());
		mensaje.setConversacion(conversacion);
		mensaje.setUsuario(usuario);
		
		Mensaje guardado = mensajeService.crearMensaje(mensaje);
		return ResponseEntity.ok(guardado);
	}
	
	static class CrearConversacionRequest {
		private Long idUsuario1;
		private Long idUsuario2;
		private Long idProducto;
		
		public Long getIdUsuario1() { 
            return idUsuario1; 
        }
		public void setIdUsuario1(Long idUsuario1) { 
            this.idUsuario1 = idUsuario1; 
        }
		public Long getIdUsuario2() { 
            return idUsuario2; 
        }
		public void setIdUsuario2(Long idUsuario2) { 
            this.idUsuario2 = idUsuario2; 
        }
		public Long getIdProducto() { 
            return idProducto; 
        }
		public void setIdProducto(Long idProducto) { 
            this.idProducto = idProducto; 
        }
	}
	
	static class EnviarMensajeRequest {
		private Long idUsuario;
		private String contenido;
		
		public Long getIdUsuario() { 
            return idUsuario; 
        }
		public void setIdUsuario(Long idUsuario) { 
            this.idUsuario = idUsuario; 
        }
		public String getContenido() { 
            return contenido; 
        }
		public void setContenido(String contenido) { 
            this.contenido = contenido; 
        }
	}
}

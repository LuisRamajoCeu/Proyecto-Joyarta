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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joyarta.model.Propuesta;
import com.example.joyarta.model.Solicitud;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.service.PropuestaService;
import com.example.joyarta.service.SolicitudService;
import com.example.joyarta.service.UsuarioService;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:4200")
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;

	@Autowired
	private PropuestaService propuestaService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<Solicitud>> getAll() {
		return ResponseEntity.ok(solicitudService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Solicitud> getById(@PathVariable Long id) {
		Solicitud solicitud = solicitudService.getById(id);
		if (solicitud != null) {
			return ResponseEntity.ok(solicitud);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Map<String, Object> datos) {
		try {
			Long clienteId = Long.valueOf(datos.get("clienteId").toString());
			Usuario cliente = usuarioService.getById(clienteId);
			if (cliente == null) {
				return ResponseEntity.badRequest().body("Cliente no encontrado");
			}

			Solicitud solicitud = new Solicitud();
			solicitud.setTitulo((String) datos.get("titulo"));
			solicitud.setDescripcion((String) datos.get("descripcion"));
			solicitud.setImagenUrl((String) datos.get("imagenUrl"));
			if (datos.get("presupuestoEstimado") != null) {
				solicitud.setPresupuestoEstimado(Double.valueOf(datos.get("presupuestoEstimado").toString()));
			}
			solicitud.setCliente(cliente);

			Solicitud creada = solicitudService.crear(solicitud);
			return ResponseEntity.ok(creada);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Error al crear solicitud: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		solicitudService.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/usuario/{id}")
	public ResponseEntity<List<Solicitud>> getByUsuario(@PathVariable Long id) {
		return ResponseEntity.ok(solicitudService.getByClienteId(id));
	}

	@GetMapping("/{id}/propuestas")
	public ResponseEntity<List<Propuesta>> getPropuestas(@PathVariable Long id) {
		return ResponseEntity.ok(propuestaService.getBySolicitudId(id));
	}

	@PostMapping("/{id}/propuestas")
	public ResponseEntity<?> crearPropuesta(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
		try {
			Solicitud solicitud = solicitudService.getById(id);
			if (solicitud == null) {
				return ResponseEntity.badRequest().body("Solicitud no encontrada");
			}

			Long artesanoId = Long.valueOf(datos.get("artesanoId").toString());
			Usuario artesano = usuarioService.getById(artesanoId);
			if (artesano == null) {
				return ResponseEntity.badRequest().body("Artesano no encontrado");
			}

			Propuesta propuesta = new Propuesta();
			propuesta.setPresupuesto(Double.valueOf(datos.get("presupuesto").toString()));
			propuesta.setTiempoEstimado((String) datos.get("tiempoEstimado"));
			propuesta.setMensaje((String) datos.get("mensaje"));
			propuesta.setSolicitud(solicitud);
			propuesta.setArtesano(artesano);

			Propuesta creada = propuestaService.crear(propuesta);
			return ResponseEntity.ok(creada);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al crear propuesta: " + e.getMessage());
		}
	}

	@PutMapping("/propuestas/{id}/aceptar")
	public ResponseEntity<Propuesta> aceptarPropuesta(@PathVariable Long id) {
		Propuesta propuesta = propuestaService.aceptar(id);
		if (propuesta != null) {
			return ResponseEntity.ok(propuesta);
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/propuestas/{id}/rechazar")
	public ResponseEntity<Propuesta> rechazarPropuesta(@PathVariable Long id) {
		Propuesta propuesta = propuestaService.rechazar(id);
		if (propuesta != null) {
			return ResponseEntity.ok(propuesta);
		}
		return ResponseEntity.notFound().build();
	}
}

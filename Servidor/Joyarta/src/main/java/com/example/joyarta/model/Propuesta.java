package com.example.joyarta.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Propuesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Double presupuesto;

	@Column
	private String tiempoEstimado;

	@Column(length = 2000)
	private String mensaje;

	@Column
	private String estado = "PENDIENTE";

	@Column
	private LocalDate fechaCreacion = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "id_solicitud")
	private Solicitud solicitud;

	@ManyToOne
	@JoinColumn(name = "id_artesano")
	private Usuario artesano;

	public Propuesta() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Double getPresupuesto() { return presupuesto; }
	public void setPresupuesto(Double presupuesto) { this.presupuesto = presupuesto; }

	public String getTiempoEstimado() { return tiempoEstimado; }
	public void setTiempoEstimado(String tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }

	public String getMensaje() { return mensaje; }
	public void setMensaje(String mensaje) { this.mensaje = mensaje; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public LocalDate getFechaCreacion() { return fechaCreacion; }
	public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

	public Solicitud getSolicitud() { return solicitud; }
	public void setSolicitud(Solicitud solicitud) { this.solicitud = solicitud; }

	public Usuario getArtesano() { return artesano; }
	public void setArtesano(Usuario artesano) { this.artesano = artesano; }
}

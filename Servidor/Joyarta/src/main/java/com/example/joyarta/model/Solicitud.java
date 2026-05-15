package com.example.joyarta.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Solicitud {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String titulo;

	@Column(length = 2000)
	private String descripcion;

	@Column(length = 500000)
	private String imagenUrl;

	@Column
	private Double presupuestoEstimado;

	@Column
	private String estado = "ABIERTA";

	@Column
	private LocalDate fechaCreacion = LocalDate.now();

	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Usuario cliente;

	@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Propuesta> propuestas;

	public Solicitud() {}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getImagenUrl() { return imagenUrl; }
	public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

	public Double getPresupuestoEstimado() { return presupuestoEstimado; }
	public void setPresupuestoEstimado(Double presupuestoEstimado) { this.presupuestoEstimado = presupuestoEstimado; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public LocalDate getFechaCreacion() { return fechaCreacion; }
	public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

	public Usuario getCliente() { return cliente; }
	public void setCliente(Usuario cliente) { this.cliente = cliente; }

	public List<Propuesta> getPropuestas() { return propuestas; }
	public void setPropuestas(List<Propuesta> propuestas) { this.propuestas = propuestas; }
}

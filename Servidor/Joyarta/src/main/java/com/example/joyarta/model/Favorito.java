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
public class Favorito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private LocalDate fechaAgregado;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "id_producto")
	private Producto producto;

	public Favorito() {
		super();
	}

	public Favorito(LocalDate fechaAgregado, Usuario usuario, Producto producto) {
		super();
		this.fechaAgregado = fechaAgregado;
		this.usuario = usuario;
		this.producto = producto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaAgregado() {
		return fechaAgregado;
	}

	public void setFechaAgregado(LocalDate fechaAgregado) {
		this.fechaAgregado = fechaAgregado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public String toString() {
		return "Favorito [id=" + id + ", fechaAgregado=" + fechaAgregado + ", usuario=" + usuario + ", producto="
				+ producto + "]";
	}
	
	
	
	
}

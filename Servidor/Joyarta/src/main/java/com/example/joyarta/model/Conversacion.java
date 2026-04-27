package com.example.joyarta.model;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Conversacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private LocalDate fechaCreacion;
	@Column
	private Boolean activo;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario1")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "listaProductos", "password", "listaPedidos", "mensajes", "listaFavoritos"})
	private Usuario usuario1;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario2")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "listaProductos", "password", "listaPedidos", "mensajes", "listaFavoritos"})
	private Usuario usuario2;
	
	@ManyToOne
	@JoinColumn(name = "id_producto")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario", "listaDetallePedidos", "listaFavoritos"})
	private Producto producto;
	
	@OneToMany(mappedBy = "conversacion", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Mensaje> mensajes;

	public Conversacion() {
		super();
	}

	public Conversacion(LocalDate fechaCreacion, Boolean activo, Set<Mensaje> mensajes) {
		super();
		this.fechaCreacion = fechaCreacion;
		this.activo = activo;
		this.mensajes = mensajes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
public Set<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(Set<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public Usuario getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}

	public Usuario getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public String toString() {
		return "Conversacion [id=" + id + ", fechaCreacion=" + fechaCreacion + ", activo=" + activo + "]";
	}
	
	
	
}


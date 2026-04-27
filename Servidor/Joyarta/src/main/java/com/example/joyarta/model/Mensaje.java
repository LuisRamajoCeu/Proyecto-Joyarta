package com.example.joyarta.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Mensaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String contenido;
	@Column
	private LocalDateTime fechaEnvio;
	
	@ManyToOne
	@JoinColumn(name = "id_conversacion")
	@JsonIgnore
	private Conversacion conversacion;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	@JsonIgnoreProperties({"listaProductos", "password", "listaPedidos", "mensajes", "listaFavoritos", "perfil"})
	private Usuario usuario;

	public Mensaje() {
		super();
	}

	public Mensaje(String contenido, LocalDateTime fechaEnvio, Conversacion conversacion, Usuario usuario) {
		super();
		this.contenido = contenido;
		this.fechaEnvio = fechaEnvio;
		this.conversacion = conversacion;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Conversacion getConversacion() {
		return conversacion;
	}

	public void setConversacion(Conversacion conversacion) {
		this.conversacion = conversacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Mensaje [id=" + id + ", contenido=" + contenido + ", fechaEnvio=" + fechaEnvio + "]";
	}
	
	
	
}


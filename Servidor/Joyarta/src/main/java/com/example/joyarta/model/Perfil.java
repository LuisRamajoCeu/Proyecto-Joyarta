package com.example.joyarta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Perfil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nombre;
	@Column
	private String biografia;
	@Column(columnDefinition = "LONGTEXT")
	private String avatarUrl;
	@Column 
	private String direccion;
	
	@OneToOne(mappedBy = "perfil")
	@JsonIgnore
	private Usuario usuario;

	public Perfil() {
		super();
	}

	public Perfil(String nombre, String biografia, String avatarUrl, String direccion, Usuario usuario) {
		super();
		this.nombre = nombre;
		this.biografia = biografia;
		this.avatarUrl = avatarUrl;
		this.direccion = direccion;
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Perfil [id=" + id + ", nombre=" + nombre + ", biografia=" + biografia + ", avatarUrl=" + avatarUrl
				+ ", direccion=" + direccion + ", usuario=" + usuario + "]";
	}
	
	
	
}

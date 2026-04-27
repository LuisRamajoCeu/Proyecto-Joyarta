package com.example.joyarta.model;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nombre;
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private String rol;
	@Column
	private LocalDate fechaRegistro = LocalDate.now();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_perfil")
	private Perfil perfil;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Producto> listaProductos;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Pedido> listaPedidos;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Mensaje> mensajes;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Favorito> listaFavoritos;
	
	public Usuario() {
		
	}

	public Usuario(String nombre, String email, String password, String rol, LocalDate fechaRegistro) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.rol = rol;
		this.fechaRegistro = fechaRegistro;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Set<Producto> getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(Set<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}
	public Set<Pedido> getListaPedidos() {
		return listaPedidos;
	}
	public void setListaPedidos(Set<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}
public Set<Mensaje> getMensajes() {
		return mensajes;
	}
	public void setMensajes(Set<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	public Set<Favorito> getListaFavoritos() {
		return listaFavoritos;
	}
	public void setListaFavoritos(Set<Favorito> listaFavoritos) {
		this.listaFavoritos = listaFavoritos;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", password=" + password + ", rol=" + rol + ", fechaRegistro="
				+ fechaRegistro + "]";
	}
	
	
	
}


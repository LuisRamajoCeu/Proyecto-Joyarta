package com.example.joyarta.model;

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

@Entity
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nombre;
	@Column
	private String descripcion;
	@Column
	private Double precio;
	@Column
	private Integer stock;
	@Column(columnDefinition = "LONGTEXT")
	private String imagenUrl;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "listaProductos", "password", "listaPedidos", "mensajes", "listaFavoritos"})
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<DetallePedido> listaDetallePedidos;
	
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Favorito> listaFavoritos;

	public Producto() {
		super();
	}

	public Producto(String nombre, String descripcion, Double precio, Integer stock, String imagenUrl, Usuario usuario,
			Categoria categoria, Set<DetallePedido> listaDetallePedidos, Set<Favorito> listaFavoritos) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.imagenUrl = imagenUrl;
		this.usuario = usuario;
		this.categoria = categoria;
		this.listaDetallePedidos = listaDetallePedidos;
		this.listaFavoritos = listaFavoritos;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Set<DetallePedido> getListaDetallePedidos() {
		return listaDetallePedidos;
	}

	public void setListaDetallePedidos(Set<DetallePedido> listaDetallePedidos) {
		this.listaDetallePedidos = listaDetallePedidos;
	}

	public Set<Favorito> getListaFavoritos() {
		return listaFavoritos;
	}

	public void setListaFavoritos(Set<Favorito> listaFavoritos) {
		this.listaFavoritos = listaFavoritos;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio
				+ ", stock=" + stock + ", imagenUrl=" + imagenUrl + "]";
	}
	
	
	
	
}


package com.example.joyarta.model;

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
public class DetallePedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private Integer cantidad;
	@Column
	private Double precioUnitario;
	
	@ManyToOne
	@JoinColumn(name = "id_pedido")
	@JsonIgnore
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name = "id_producto")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "listaDetallePedidos", "listaFavoritos", "usuario", "categoria"})
	private Producto producto;

	public DetallePedido() {
		super();
	}

	public DetallePedido(Integer cantidad, Double precioUnitario, Pedido pedido, Producto producto) {
		super();
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.pedido = pedido;
		this.producto = producto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public String toString() {
		return "DetallePedido [id=" + id + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + "]";
	}
	
	
	
	
}

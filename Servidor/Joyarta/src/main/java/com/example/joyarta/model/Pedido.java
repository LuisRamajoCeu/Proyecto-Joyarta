package com.example.joyarta.model;

import java.time.LocalDate;
import java.util.Set;

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
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private LocalDate fecha;
	@Column
	private Double total;
	@Column
	private String estadoPago;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "listaPedidos", "mensajes", "listaProductos", "listaFavoritos"})
	private Usuario usuario;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private Set<DetallePedido> listaDetallePedidos;

	public Pedido() {
		super();
	}

	public Pedido(LocalDate fecha, Double total, String estadoPago, Usuario usuario,
			Set<DetallePedido> listaDetallePedidos) {
		super();
		this.fecha = fecha;
		this.total = total;
		this.estadoPago = estadoPago;
		this.usuario = usuario;
		this.listaDetallePedidos = listaDetallePedidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getEstadoPago() {
		return estadoPago;
	}

	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Set<DetallePedido> getListaDetallePedidos() {
		return listaDetallePedidos;
	}

	public void setListaDetallePedidos(Set<DetallePedido> listaDetallePedidos) {
		this.listaDetallePedidos = listaDetallePedidos;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", total=" + total + ", estadoPago=" + estadoPago + "]";
	}
	
	
	
	
	
}


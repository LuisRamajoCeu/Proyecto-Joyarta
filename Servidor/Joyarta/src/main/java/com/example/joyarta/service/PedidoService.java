package com.example.joyarta.service;

import java.util.List;

import com.example.joyarta.model.Pedido;

public interface PedidoService {

	Pedido crearPedido(Pedido pedido);

	List<Pedido> getAll();

	Pedido getById(Long id);

	Pedido update(Long id, Pedido actualizar);

	void delete(Long id);

    List<Pedido> obtenerPedidosPorUsuario(Long usuarioId);

}

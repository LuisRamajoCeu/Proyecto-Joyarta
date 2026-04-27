package com.example.joyarta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.joyarta.model.Pedido;
import com.example.joyarta.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService{

	@Autowired
	private PedidoRepository pedidoRepo;
	
	@Override
	public Pedido crearPedido(Pedido pedido) {
		return pedidoRepo.save(pedido);
	}
	
	@Override
	public List<Pedido> getAll() {
		return pedidoRepo.findAll();
	}

	@Override
	public Pedido getById(Long id) {
		return pedidoRepo.findById(id).orElse(null);
	}

	@Override
	public Pedido update(Long id, Pedido actualizar) {
		Pedido existente = pedidoRepo.findById(id).orElse(null);
		if (existente != null) {
			existente.setEstadoPago(actualizar.getEstadoPago());
			return pedidoRepo.save(existente);
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		pedidoRepo.deleteById(id);
	}
	
    @Override
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepo.findByUsuarioId(usuarioId);
    }

}

package com.example.joyarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
    java.util.List<Pedido> findByUsuarioId(Long usuarioId);
}

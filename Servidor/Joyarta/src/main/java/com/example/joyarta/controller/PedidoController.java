package com.example.joyarta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.joyarta.model.DetallePedido;
import com.example.joyarta.model.Pedido;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.service.PedidoService;
import com.example.joyarta.service.ProductoService;
import com.example.joyarta.service.UsuarioService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Pedido pedido = pedidoService.getById(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> datos) {
        String nuevoEstado = datos.get("estadoPago");
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Se requiere el campo 'estadoPago'");
        }
        
        Pedido pedido = pedidoService.getById(id);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        
        pedido.setEstadoPago(nuevoEstado);
        Pedido actualizado = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(actualizado);
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> datos) {
        try {
            Map<String, Object> usuarioMap = (Map<String, Object>) datos.get("usuario");
            if (usuarioMap == null || usuarioMap.get("id") == null) {
                return ResponseEntity.badRequest().body("Usuario no proporcionado");
            }
            Long usuarioId = Long.valueOf(usuarioMap.get("id").toString());
            Usuario usuario = usuarioService.getById(usuarioId);
            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setFecha(LocalDate.now());
            pedido.setEstadoPago("PENDIENTE");

            List<Map<String, Object>> detallesRaw = (List<Map<String, Object>>) datos.get("listaDetallePedidos");
            List<String> errores = new ArrayList<>();
            double total = 0.0;
            java.util.Set<DetallePedido> detalles = new java.util.HashSet<>();

            if (detallesRaw != null) {
                for (Map<String, Object> detalleMap : detallesRaw) {
                    Map<String, Object> productoMap = (Map<String, Object>) detalleMap.get("producto");
                    if (productoMap == null || productoMap.get("id") == null) {
                        errores.add("Detalle sin producto válido");
                        continue;
                    }

                    Long productoId = Long.valueOf(productoMap.get("id").toString());
                    Producto producto = productoService.getById(productoId);

                    if (producto == null) {
                        errores.add("Producto no encontrado ID: " + productoId);
                        continue;
                    }

                    Integer cantidad = 1;
                    if (detalleMap.get("cantidad") != null) {
                        cantidad = Integer.valueOf(detalleMap.get("cantidad").toString());
                    }
                    if (cantidad <= 0) cantidad = 1;

                    if (producto.getStock() < cantidad) {
                        errores.add("Stock insuficiente para: " + producto.getNombre());
                        continue;
                    }

                    producto.setStock(producto.getStock() - cantidad);
                    productoService.update(producto.getId(), producto);

                    DetallePedido detalle = new DetallePedido();
                    detalle.setProducto(producto);
                    detalle.setCantidad(cantidad);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalle.setPedido(pedido);
                    detalles.add(detalle);

                    total += producto.getPrecio() * cantidad;
                }
            }

            if (!errores.isEmpty()) {
                return ResponseEntity.badRequest().body(errores);
            }

            pedido.setListaDetallePedidos(detalles);
            pedido.setTotal(total);
            Pedido savedPedido = pedidoService.crearPedido(pedido);
            return ResponseEntity.ok(savedPedido);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al procesar pedido: " + e.getMessage());
        }
    }
}

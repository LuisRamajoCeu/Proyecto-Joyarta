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

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        Usuario usuario = usuarioService.getById(pedido.getUsuario().getId());
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDate.now());
        pedido.setEstadoPago("PENDIENTE");

        List<String> errores = new ArrayList<>();
        double total = 0.0;
        
        if (pedido.getListaDetallePedidos() != null) {
            for (DetallePedido detalle : pedido.getListaDetallePedidos()) {
                Producto producto = productoService.getById(detalle.getProducto().getId());
                
                if (producto == null) {
                    errores.add("Producto no encontrado ID: " + detalle.getProducto().getId());
                    continue;
                }

                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                	detalle.setCantidad(1);
                }

                if (producto.getStock() < detalle.getCantidad()) {
                    errores.add("Stock insuficiente para: " + producto.getNombre());
                    continue;
                }

                producto.setStock(producto.getStock() - detalle.getCantidad());
                productoService.crearProducto(producto);

                detalle.setProducto(producto);
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setPedido(pedido);
                
                total += producto.getPrecio() * detalle.getCantidad();
            }
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }

        pedido.setTotal(total);
        Pedido savedPedido = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(savedPedido);
    }
}

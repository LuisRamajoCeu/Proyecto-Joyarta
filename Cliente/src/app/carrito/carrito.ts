import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CarritoService, CartItem } from '../services/carrito-service';
import { Router } from '@angular/router';
import { lastValueFrom, Observable } from 'rxjs';
import { UsuarioService } from '../services/usuario-service';

@Component({
  selector: 'app-carrito',
  imports: [CommonModule],
  templateUrl: './carrito.html',
  styleUrl: './carrito.css'
})
export class Carrito {
  items$: Observable<CartItem[]>;

  mostrarModal = false;
  pedidoExito = false;
  pedidoError = false;
  mensajeModal = '';
  submensajeModal = '';
  totalPedido = 0;
  numeroPedido = 0;
  cantidadProductos = 0;
  procesando = false;

  constructor(
    private carritoService: CarritoService,
    public router: Router,
    private usuarioService: UsuarioService
  ) {
    this.items$ = this.carritoService.getCart();
  }

  calcularTotal(items: CartItem[]): number {
    return items.reduce((sum, item) => sum + (item.producto.precio * item.cantidad), 0);
  }



  eliminar(id: number) {
    if (id !== undefined) {
      this.carritoService.removeFromCart(id);
    }
  }

  actualizarCantidad(id: number, cantidad: number) {
    if (id !== undefined && cantidad > 0) {
      this.carritoService.updateQuantity(id, cantidad);
    }
  }

  async finalizarPedido() {
    if (this.carritoService.itemCount() === 0) return;

    if (!this.usuarioService.estaLogueado()) {
      this.pedidoError = true;
      this.pedidoExito = false;
      this.mensajeModal = 'Sesión no iniciada';
      this.submensajeModal = 'Necesitas iniciar sesión para realizar un pedido.';
      this.mostrarModal = true;
      return;
    }

    this.procesando = true;

    try {
      this.totalPedido = this.carritoService.calculateTotal();
      this.cantidadProductos = this.carritoService.itemCount();
      const pedido = await lastValueFrom(this.carritoService.finalizarPedido());
      this.numeroPedido = pedido?.id || Math.floor(Math.random() * 9000) + 1000;

      this.pedidoExito = true;
      this.pedidoError = false;
      this.mensajeModal = '¡Pedido realizado con éxito!';
      this.submensajeModal = 'Tu pedido ha sido procesado correctamente. Recibirás una confirmación pronto.';
      this.mostrarModal = true;
      this.carritoService.clearCart();
    } catch (error: any) {
      console.error('Error al realizar pedido:', error);
      console.error('Respuesta del servidor:', error?.error);
      this.pedidoExito = false;
      this.pedidoError = true;
      if (error && error.message === "Compra de producto propio no permitida") {
        this.mensajeModal = 'Acción no permitida';
        this.submensajeModal = 'No puedes adquirir tus propios productos como artesano.';
      } else {
        this.mensajeModal = 'Error al procesar el pedido';
        const serverMsg = error?.error;
        if (Array.isArray(serverMsg)) {
          this.submensajeModal = serverMsg.join(', ');
        } else if (typeof serverMsg === 'string') {
          this.submensajeModal = serverMsg;
        } else {
          this.submensajeModal = 'Ha ocurrido un problema. Prueba a vaciar el carrito y añadir los productos de nuevo.';
        }
      }
      this.mostrarModal = true;
    } finally {
      this.procesando = false;
    }
  }

  cerrarModal() {
    this.mostrarModal = false;
    if (this.pedidoExito) {
      this.router.navigate(['/home']);
    } else if (this.pedidoError && !this.usuarioService.estaLogueado()) {
      this.router.navigate(['/login']);
    }
  }

  irACatalogo() {
    this.mostrarModal = false;
    this.router.navigate(['/catalogo']);
  }
}

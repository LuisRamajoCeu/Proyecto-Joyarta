import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductoModel } from '../models/productoModel';
import { HttpClient } from '@angular/common/http';
import { UsuarioService } from './usuario-service';

export interface CartItem {
  producto: ProductoModel;
  cantidad: number;
}

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private cartItems = new BehaviorSubject<CartItem[]>([]);
  private apiUrl = 'http://localhost:8080/api/pedidos';

  constructor(private http: HttpClient, private usuarioService: UsuarioService) {
    const savedCart = localStorage.getItem('carrito');
    if (savedCart) {
      this.cartItems.next(JSON.parse(savedCart));
    }
  }

  getCart(): Observable<CartItem[]> {
    return this.cartItems.asObservable();
  }

  private saveCart(items: CartItem[]) {
    localStorage.setItem('carrito', JSON.stringify(items));
    this.cartItems.next(items);
  }

  addToCart(producto: ProductoModel, cantidad: number = 1): void {
    const currentItems = this.cartItems.value;
    const existingItem = currentItems.find(item => item.producto.id === producto.id);

    if (existingItem) {
      existingItem.cantidad += cantidad;
      this.saveCart([...currentItems]);
    } else {
      this.saveCart([...currentItems, { producto, cantidad }]);
    }
  }

  removeFromCart(productoId: number): void {
    const currentItems = this.cartItems.value.filter(item => item.producto.id !== productoId);
    this.saveCart(currentItems);
  }

  updateQuantity(productoId: number, cantidad: number): void {
    const currentItems = this.cartItems.value;
    const item = currentItems.find(i => i.producto.id === productoId);
    if (item) {
      item.cantidad = cantidad;
      if (item.cantidad <= 0) {
        this.removeFromCart(productoId);
      } else {
        this.saveCart([...currentItems]);
      }
    }
  }

  clearCart(): void {
    this.saveCart([]);
    localStorage.removeItem('carrito');
  }

  calculateTotal(): number {
    return this.cartItems.value.reduce((total, item) => total + (item.producto.precio * item.cantidad), 0);
  }

  itemCount(): number {
     return this.cartItems.value.reduce((count, item) => count + item.cantidad, 0);
  }

  finalizarPedido(): Observable<any> {
    const usuario = this.usuarioService.getUsuario();
    if (!usuario || !usuario.id) {
        throw new Error("Usuario no autenticado");
    }

    const comprandoPropio = this.cartItems.value.some(item => item.producto && item.producto.usuario && item.producto.usuario.id === usuario.id);
    if (comprandoPropio) {
        throw new Error("Compra de producto propio no permitida");
    }

    const pedido = {
      usuario: { id: usuario.id },
      listaDetallePedidos: this.cartItems.value
        .filter(item => item.producto && item.producto.id)
        .map(item => ({
        producto: { id: item.producto.id },
        cantidad: item.cantidad || 1
      }))
    };

    return this.http.post(this.apiUrl, pedido);
  }
}

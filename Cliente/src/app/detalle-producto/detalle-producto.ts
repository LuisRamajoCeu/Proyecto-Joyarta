import { CommonModule, AsyncPipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { ProductoModel } from '../models/productoModel';
import { Router } from '@angular/router';
import { TiendaService } from '../services/tienda-service';
import { Observable, lastValueFrom, firstValueFrom } from 'rxjs';
import { FavoritoService } from '../services/favorito-service';
import { UsuarioService } from '../services/usuario-service';
import { ChatService } from '../services/chat-service';

import { CarritoService } from '../services/carrito-service';

@Component({
  selector: 'app-detalle-producto',
  imports: [AsyncPipe, CommonModule],
  templateUrl: './detalle-producto.html',
  styleUrl: './detalle-producto.css',
})
export class DetalleProducto implements OnInit {
  producto$!: Observable<ProductoModel>;
  esFavorito = false;
  
  @Input()
  set id(idProducto: string) {
    this.idProducto = Number(idProducto);
    this.producto$ = this.tiendaService.getProductoById(this.idProducto);
    this.verificarFavorito();
  }

  idProducto!: number;

  constructor(
    private tiendaService: TiendaService,
    private router: Router,
    private favoritoService: FavoritoService,
    private usuarioService: UsuarioService,
    private chatService: ChatService,
    private carritoService: CarritoService
  ) {}

  ngOnInit() {
  }

  async verificarFavorito() {
    const usuario = this.usuarioService.getUsuario();
    if (usuario && usuario.id && this.idProducto) {
      try {
        this.esFavorito = await lastValueFrom(this.favoritoService.esFavorito(usuario.id, this.idProducto));
      } catch (error) {
        this.esFavorito = false;
      }
    }
  }

  async toggleFavorito() {
    const usuario = this.usuarioService.getUsuario();
    if (!usuario || !usuario.id) {
      alert('Debes iniciar sesión para agregar favoritos');
      return;
    }

    try {
      if (this.esFavorito) {
        await lastValueFrom(this.favoritoService.quitarFavorito(usuario.id, this.idProducto));
        this.esFavorito = false;
        console.log('Producto quitado de favoritos');
      } else {
        await lastValueFrom(this.favoritoService.agregarFavorito(usuario.id, this.idProducto));
        this.esFavorito = true;
        console.log('Producto agregado a favoritos');
      }
    } catch (err) {
      console.error('Error al actualizar favorito:', err);
    }
  }

  async contactarVendedor() {
    const usuario = this.usuarioService.getUsuario();
    if (!usuario || !usuario.id) {
      alert('Debes iniciar sesión para contactar al vendedor');
      this.router.navigate(['/login']);
      return;
    }

    try {
      const producto = await firstValueFrom(this.producto$);

      if (producto && producto.usuario && producto.usuario.id && producto.id) {
        const conversacion = await lastValueFrom(
          this.chatService.crearConversacion(usuario.id!, producto.usuario.id!, producto.id!)
        );
        console.log('Conversación creada/abierta:', conversacion);
        this.router.navigate(['/chat']);
      }
    } catch (error) {
       console.error('Error al iniciar conversación:', error);
       alert('Error al iniciar conversación. Intenta de nuevo.');
    }
  }

  btnCarritoAdded = false;
  mostrarModalError = false;
  mensajeModalError = '';
  submensajeModalError = '';

  cerrarModalError() {
    this.mostrarModalError = false;
  }

  async agregarAlCarrito() {
    try {
        const producto = await firstValueFrom(this.producto$);
        if (producto) {
            const usuario = this.usuarioService.getUsuario();
            if (usuario && producto.usuario && usuario.id === producto.usuario.id) {
                this.mensajeModalError = 'Acción no permitida';
                this.submensajeModalError = 'No puedes adquirir tus propios productos como artesano.';
                this.mostrarModalError = true;
                return;
            }
            this.carritoService.addToCart(producto);
            this.btnCarritoAdded = true;
            setTimeout(() => this.btnCarritoAdded = false, 1000);
        }
    } catch (error) {
        console.error('Error al obtener producto para carrito:', error);
    }
  }
}

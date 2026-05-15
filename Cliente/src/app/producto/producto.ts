import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { ProductoModel } from '../models/productoModel';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FavoritoService } from '../services/favorito-service';
import { UsuarioService } from '../services/usuario-service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-producto',
  imports: [CommonModule, RouterLink],
  templateUrl: './producto.html',
  styleUrl: './producto.css',
})
export class Producto implements OnInit {
  @Input() producto!: ProductoModel;
  esFavorito = false;
  @Output() favoritoCambiado = new EventEmitter<boolean>();

  constructor(
    private favoritoService: FavoritoService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.verificarFavorito();
  }

  async verificarFavorito() {
    const usuario = this.usuarioService.getUsuario();
    if (usuario && usuario.id && this.producto) {
      try {
        this.esFavorito = await lastValueFrom(this.favoritoService.esFavorito(usuario.id, this.producto.id));
      } catch (error) {
        this.esFavorito = false;
      }
    }
  }

  async toggleFavorito(event: Event) {
    event.stopPropagation();
    event.preventDefault();

    const usuario = this.usuarioService.getUsuario();
    if (!usuario) {
      alert('Debes iniciar sesión para agregar favoritos');
      return;
    }

    if (!usuario.id) return;

    try {
      if (this.esFavorito) {
        await lastValueFrom(this.favoritoService.quitarFavorito(usuario.id, this.producto.id));
        this.esFavorito = false;
        console.log('Producto quitado de favoritos');
        this.favoritoCambiado.emit(false);
      } else {
        await lastValueFrom(this.favoritoService.agregarFavorito(usuario.id, this.producto.id));
        this.esFavorito = true;
        console.log('Producto agregado a favoritos');
        this.favoritoCambiado.emit(true);
      }
    } catch (err) {
      console.error('Error al actualizar favorito:', err);
    }
  }
}

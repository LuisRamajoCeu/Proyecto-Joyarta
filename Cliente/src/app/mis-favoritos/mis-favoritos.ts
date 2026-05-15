import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FavoritoService } from '../services/favorito-service';
import { UsuarioService } from '../services/usuario-service';
import { Favorito } from '../models/favoritoModel';
import { Producto } from '../producto/producto';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-mis-favoritos',
  imports: [CommonModule, RouterLink, Producto],
  templateUrl: './mis-favoritos.html',
  styleUrl: './mis-favoritos.css',
})
export class MisFavoritos implements OnInit {
  favoritos: Favorito[] = [];
  cargando = true;

  constructor(
    private favoritoService: FavoritoService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.cargarFavoritos();
  }

  async cargarFavoritos() {
    this.cargando = true;
    try {
      const favoritos = await lastValueFrom(this.favoritoService.listarFavoritos());
      const usuario = this.usuarioService.getUsuario();
      if (usuario && usuario.id) {
        this.favoritos = favoritos.filter(f => f.usuario.id === usuario.id);
      }
    } catch (err) {
      console.error('Error al cargar favoritos:', err);
    } finally {
      this.cargando = false;
    }
  }

  onFavoritoCambiado(esFavorito: boolean, idFavorito: number) {
    if (!esFavorito) {
      this.favoritos = this.favoritos.filter(f => f.id !== idFavorito);
    }
  }
}

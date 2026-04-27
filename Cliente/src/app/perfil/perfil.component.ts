import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../services/usuario-service';
import { ProductoModel } from '../models/productoModel';
import { Perfil } from '../models/usuarioModel';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  usuario: any;
  perfil: any;
  productos: ProductoModel[] = [];
  pedidos: any[] = [];
  activeTab: 'productos' | 'pedidos' | 'editar' | 'seguridad' = 'productos';

  editNombre = '';
  editBiografia = '';
  editDireccion = '';
  editAvatarUrl = '';
  guardando = false;
  mensajeExito = '';
  mensajeError = '';

  passActual = '';
  passNueva = '';
  passConfirmar = '';
  guardandoPass = false;
  mensajeExitoPass = '';
  mensajeErrorPass = '';

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  async ngOnInit() {
    this.usuario = this.usuarioService.getUsuario();
    if (!this.usuario) {
      this.router.navigate(['/login']);
      return;
    }

    await this.loadPerfil();
    await this.loadProductos();
    await this.loadPedidos();
  }

  async loadPerfil() {
    try {
      this.perfil = await lastValueFrom(this.usuarioService.getPerfil(this.usuario.id));
      this.syncEditFields();
    } catch (err) {
      console.error('Error loading perfil', err);
    }
  }

  syncEditFields() {
    this.editNombre = this.perfil?.nombre || this.usuario?.nombre || '';
    this.editBiografia = this.perfil?.biografia || '';
    this.editDireccion = this.perfil?.direccion || '';
    this.editAvatarUrl = this.perfil?.avatarUrl || '';
  }

  async loadProductos() {
    try {
      this.productos = await lastValueFrom(this.usuarioService.getProductosUsuario(this.usuario.id));
    } catch (err) {
      console.error('Error loading productos', err);
    }
  }

  async loadPedidos() {
    try {
      this.pedidos = await lastValueFrom(this.usuarioService.getPedidosUsuario(this.usuario.id));
    } catch (err) {
      console.error('Error loading pedidos', err);
    }
  }

  async guardarPerfil() {
    this.guardando = true;
    this.mensajeExito = '';
    this.mensajeError = '';

    const perfilActualizado: Perfil = {
      nombre: this.editNombre,
      biografia: this.editBiografia,
      direccion: this.editDireccion,
      avatarUrl: this.editAvatarUrl
    };

    try {
      this.perfil = await lastValueFrom(
        this.usuarioService.editarPerfil(this.usuario.id, perfilActualizado)
      );
      this.mensajeExito = '¡Perfil actualizado correctamente!';
      
      this.usuario.nombre = this.editNombre;
      this.usuario.perfil = this.perfil;
      this.usuarioService.setUsuario(this.usuario);

      setTimeout(() => this.mensajeExito = '', 3000);
    } catch (err) {
      console.error('Error al guardar perfil', err);
      this.mensajeError = 'Error al guardar los cambios. Inténtalo de nuevo.';
      setTimeout(() => this.mensajeError = '', 3000);
    } finally {
      this.guardando = false;
    }
  }

  async cambiarPassword() {
    this.mensajeExitoPass = '';
    this.mensajeErrorPass = '';

    if (!this.passActual || !this.passNueva || !this.passConfirmar) {
      this.mensajeErrorPass = 'Rellena todos los campos.';
      return;
    }
    if (this.passNueva.length < 6) {
      this.mensajeErrorPass = 'La nueva contraseña debe tener al menos 6 caracteres.';
      return;
    }
    if (this.passNueva !== this.passConfirmar) {
      this.mensajeErrorPass = 'Las contraseñas no coinciden.';
      return;
    }

    this.guardandoPass = true;
    try {
      await lastValueFrom(
        this.usuarioService.cambiarPassword(this.usuario.id, this.passActual, this.passNueva)
      );
      this.mensajeExitoPass = '¡Contraseña actualizada correctamente!';
      this.passActual = '';
      this.passNueva = '';
      this.passConfirmar = '';
      setTimeout(() => this.mensajeExitoPass = '', 3000);
    } catch (err: any) {
      if (err.status === 403) {
        this.mensajeErrorPass = 'La contraseña actual no es correcta.';
      } else {
        this.mensajeErrorPass = 'Error al cambiar la contraseña.';
      }
      setTimeout(() => this.mensajeErrorPass = '', 4000);
    } finally {
      this.guardandoPass = false;
    }
  }

  onTabChange(tab: 'productos' | 'pedidos' | 'editar' | 'seguridad') {
    this.activeTab = tab;
    if (tab === 'editar') {
      this.syncEditFields();
    }
  }
}

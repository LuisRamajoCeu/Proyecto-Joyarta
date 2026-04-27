import { Component } from '@angular/core';
import { UsuarioService } from '../services/usuario-service';
import { Router, RouterLink } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Usuario } from '../models/usuarioModel';

@Component({
  selector: 'app-registro',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  nuevoUsuario: Usuario = {
    email: '',
    password: '',
    perfil: {
      nombre: '',
      biografia: '',
      direccion: '',
      avatarUrl: ''
    }
  };

  mostrarOpcionalesTxt = true;
  mostrarOpcionales = false;
  registrando = false;
  mostrarExito = false;
  errorMsg = '';

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  toggleOpcionales() {
    this.mostrarOpcionales = !this.mostrarOpcionales;
    this.mostrarOpcionalesTxt = false;
  }

  async onRegistro() {
    this.registrando = true;
    this.errorMsg = '';

    try {
      const usuarioCreado = await lastValueFrom(
        this.usuarioService.registrar(this.nuevoUsuario)
      );
      this.mostrarExito = true;
    } catch (error: any) {
      console.error(error);
      if (error.status === 409) {
        this.errorMsg = 'Este email ya está registrado. Prueba con otro.';
      } else {
        this.errorMsg = 'Error: No se pudo registrar. Verifica los datos.';
      }
    } finally {
      this.registrando = false;
    }
  }

  irALogin() {
    this.mostrarExito = false;
    this.router.navigate(['/login']);
  }
}

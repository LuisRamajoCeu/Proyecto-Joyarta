import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SolicitudService } from '../services/solicitud-service';
import { UsuarioService } from '../services/usuario-service';
import { Solicitud } from '../models/solicitudModel';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './solicitudes.html',
  styleUrl: './solicitudes.css',
})
export class Solicitudes implements OnInit {
  solicitudes: Solicitud[] = [];
  cargando = true;

  mostrarModalCrear = false;
  nuevaSolicitud = {
    titulo: '',
    descripcion: '',
    imagenUrl: '',
    presupuestoEstimado: null as number | null
  };
  creando = false;
  mensajeExito = '';
  mensajeError = '';

  constructor(
    private solicitudService: SolicitudService,
    public usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    this.cargarSolicitudes();
  }

  async cargarSolicitudes() {
    this.cargando = true;
    try {
      const usuario = this.usuarioService.getUsuario();
      if (usuario && usuario.rol === 'CLIENTE') {
        this.solicitudes = await lastValueFrom(this.solicitudService.getMisSolicitudes(usuario.id!));
      } else {
        this.solicitudes = await lastValueFrom(this.solicitudService.getAll());
      }
    } catch (err) {
      console.error('Error al cargar solicitudes:', err);
    } finally {
      this.cargando = false;
    }
  }

  esCliente(): boolean {
    const u = this.usuarioService.getUsuario();
    return u?.rol === 'CLIENTE';
  }

  esArtesano(): boolean {
    const u = this.usuarioService.getUsuario();
    return u?.rol === 'ARTESANO';
  }

  abrirModalCrear() {
    this.nuevaSolicitud = { titulo: '', descripcion: '', imagenUrl: '', presupuestoEstimado: null };
    this.mensajeExito = '';
    this.mensajeError = '';
    this.mostrarModalCrear = true;
  }

  cerrarModalCrear() {
    this.mostrarModalCrear = false;
  }

  onImagenSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.nuevaSolicitud.imagenUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  async crearSolicitud() {
    if (!this.nuevaSolicitud.titulo) {
      this.mensajeError = 'El título es obligatorio.';
      return;
    }

    this.creando = true;
    this.mensajeExito = '';
    this.mensajeError = '';

    try {
      const usuario = this.usuarioService.getUsuario();
      await lastValueFrom(this.solicitudService.crear({
        clienteId: usuario!.id,
        titulo: this.nuevaSolicitud.titulo,
        descripcion: this.nuevaSolicitud.descripcion,
        imagenUrl: this.nuevaSolicitud.imagenUrl,
        presupuestoEstimado: this.nuevaSolicitud.presupuestoEstimado
      }));
      this.mensajeExito = '¡Solicitud creada correctamente!';
      await this.cargarSolicitudes();
      setTimeout(() => this.cerrarModalCrear(), 1500);
    } catch (err) {
      console.error('Error al crear solicitud:', err);
      this.mensajeError = 'Error al crear la solicitud.';
    } finally {
      this.creando = false;
    }
  }

  getEstadoEmoji(estado: string): string {
    switch (estado) {
      case 'ABIERTA': return '🟢';
      case 'EN_PROCESO': return '🔵';
      case 'COMPLETADA': return '✅';
      case 'CANCELADA': return '🔴';
      default: return '⚪';
    }
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'ABIERTA': return 'Abierta';
      case 'EN_PROCESO': return 'En proceso';
      case 'COMPLETADA': return 'Completada';
      case 'CANCELADA': return 'Cancelada';
      default: return estado;
    }
  }
}

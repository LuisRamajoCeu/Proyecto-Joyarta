import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SolicitudService } from '../services/solicitud-service';
import { UsuarioService } from '../services/usuario-service';
import { Solicitud, Propuesta } from '../models/solicitudModel';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-detalle-solicitud',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './detalle-solicitud.html',
  styleUrl: './detalle-solicitud.css',
})
export class DetalleSolicitud implements OnInit {
  solicitud: Solicitud | null = null;
  propuestas: Propuesta[] = [];
  cargando = true;
  imagenAmpliada: string | null = null;

  nuevaPropuesta = {
    presupuesto: null as number | null,
    tiempoEstimado: '',
    mensaje: ''
  };
  enviandoPropuesta = false;
  mensajeExitoPropuesta = '';
  mensajeErrorPropuesta = '';

  constructor(
    private route: ActivatedRoute,
    private solicitudService: SolicitudService,
    public usuarioService: UsuarioService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarDatos(id);
    }
  }

  async cargarDatos(id: number) {
    this.cargando = true;
    try {
      this.solicitud = await lastValueFrom(this.solicitudService.getById(id));
      this.propuestas = await lastValueFrom(this.solicitudService.getPropuestas(id));
    } catch (err) {
      console.error('Error:', err);
    } finally {
      this.cargando = false;
    }
  }

  esPropietario(): boolean {
    const u = this.usuarioService.getUsuario();
    return u?.id === this.solicitud?.cliente?.id;
  }

  esArtesano(): boolean {
    const u = this.usuarioService.getUsuario();
    return u?.rol === 'ARTESANO';
  }

  get miPropuesta(): Propuesta | null {
    const u = this.usuarioService.getUsuario();
    return this.propuestas.find(p => p.artesano?.id === u?.id) || null;
  }

  get propuestasVisibles(): Propuesta[] {
    if (this.esPropietario()) {
      return this.propuestas;
    }
    const mi = this.miPropuesta;
    return mi ? [mi] : [];
  }

  puedeEnviarPropuesta(): boolean {
    if (!this.esArtesano() || this.solicitud?.estado !== 'ABIERTA') return false;
    return !this.miPropuesta;
  }

  async enviarPropuesta() {
    if (!this.nuevaPropuesta.presupuesto || !this.nuevaPropuesta.tiempoEstimado) {
      this.mensajeErrorPropuesta = 'El presupuesto y tiempo estimado son obligatorios.';
      return;
    }

    this.enviandoPropuesta = true;
    this.mensajeExitoPropuesta = '';
    this.mensajeErrorPropuesta = '';

    try {
      const usuario = this.usuarioService.getUsuario();
      await lastValueFrom(this.solicitudService.enviarPropuesta(this.solicitud!.id!, {
        artesanoId: usuario!.id,
        presupuesto: this.nuevaPropuesta.presupuesto,
        tiempoEstimado: this.nuevaPropuesta.tiempoEstimado,
        mensaje: this.nuevaPropuesta.mensaje
      }));
      this.mensajeExitoPropuesta = '¡Propuesta enviada correctamente!';
      this.nuevaPropuesta = { presupuesto: null, tiempoEstimado: '', mensaje: '' };
      await this.cargarDatos(this.solicitud!.id!);
    } catch (err) {
      console.error('Error:', err);
      this.mensajeErrorPropuesta = 'Error al enviar la propuesta.';
    } finally {
      this.enviandoPropuesta = false;
    }
  }

  async aceptarPropuesta(propuestaId: number) {
    try {
      await lastValueFrom(this.solicitudService.aceptarPropuesta(propuestaId));
      await this.cargarDatos(this.solicitud!.id!);
    } catch (err) {
      console.error('Error al aceptar:', err);
    }
  }

  async rechazarPropuesta(propuestaId: number) {
    try {
      await lastValueFrom(this.solicitudService.rechazarPropuesta(propuestaId));
      await this.cargarDatos(this.solicitud!.id!);
    } catch (err) {
      console.error('Error al rechazar:', err);
    }
  }

  getEstadoPropuesta(estado: string): string {
    switch (estado) {
      case 'PENDIENTE': return '⏳ Pendiente';
      case 'ACEPTADA': return '✅ Aceptada';
      case 'RECHAZADA': return '❌ Rechazada';
      default: return estado;
    }
  }

  abrirImagen(url: string) {
    this.imagenAmpliada = url;
  }

  cerrarImagen() {
    this.imagenAmpliada = null;
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../services/chat-service';
import { UsuarioService } from '../services/usuario-service';
import { Conversacion } from '../models/conversacionModel';
import { Mensaje as MensajeModel } from '../models/mensajeModel';
import { lastValueFrom } from 'rxjs';
import { Mensaje } from '../mensaje/mensaje';
import { ConversacionItem } from '../conversacion-item/conversacion-item';
import { CarritoService } from '../services/carrito-service';
import { ProductoModel } from '../models/productoModel';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule, Mensaje, ConversacionItem],
  templateUrl: './chat.html',
  styleUrl: './chat.css',
})
export class Chat implements OnInit, OnDestroy {
  conversaciones: Conversacion[] = [];
  conversacionSeleccionada: Conversacion | null = null;
  mensajes: MensajeModel[] = [];
  nuevoMensaje: string = '';
  idUsuarioActual: number = 0;

  private pollingInterval: any;
  mostrarToast: boolean = false;

  constructor(
    private chatService: ChatService,
    private usuarioService: UsuarioService,
    private carritoService: CarritoService
  ) { }

  ngOnInit(): void {
    const usuario = this.usuarioService.getUsuario();
    if (usuario && usuario.id) {
      this.idUsuarioActual = usuario.id;
      this.cargarConversaciones();

      this.pollingInterval = setInterval(() => {
        if (this.conversacionSeleccionada) {
          this.cargarMensajes(this.conversacionSeleccionada.id, false);
        }
        this.cargarConversaciones();
      }, 3000);
    }
  }

  ngOnDestroy(): void {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
    }
  }

  async cargarConversaciones(): Promise<void> {
    try {
      this.conversaciones = await lastValueFrom(this.chatService.getConversaciones(this.idUsuarioActual));
    } catch (error) {
      console.error('Error al cargar conversaciones:', error);
    }
  }

  seleccionarConversacion(conversacion: Conversacion): void {
    this.conversacionSeleccionada = conversacion;
    this.cargarMensajes(conversacion.id, true);
  }

  async cargarMensajes(idConversacion: number, forceScroll: boolean = false): Promise<void> {
    try {
      const container = document.querySelector('.mensajes-container');

      const estabaAbajo = container
        ? Math.abs((container.scrollHeight - container.scrollTop) - container.clientHeight) < 20
        : false;

      const nuevosMensajes = await lastValueFrom(this.chatService.getMensajes(idConversacion));

      if (nuevosMensajes.length !== this.mensajes.length) {
        this.mensajes = nuevosMensajes;

        if (forceScroll || estabaAbajo) {
          setTimeout(() => this.scrollToBottom(), 50);
        }
      }
    } catch (error) {
      console.error('Error al cargar mensajes:', error);
    }
  }

  async enviarMensaje(): Promise<void> {
    if (!this.nuevoMensaje.trim() || !this.conversacionSeleccionada) {
      return;
    }

    try {
      const mensaje = await lastValueFrom(this.chatService.enviarMensaje(
        this.conversacionSeleccionada.id,
        this.idUsuarioActual,
        this.nuevoMensaje
      ));

      this.mensajes.push(mensaje);
      this.nuevoMensaje = '';
      setTimeout(() => this.scrollToBottom(), 50);
    } catch (error) {
      console.error('Error al enviar mensaje:', error);
    }
  }

  getOtroUsuario(conversacion: Conversacion): any {
    return conversacion.usuario1.id === this.idUsuarioActual
      ? conversacion.usuario2
      : conversacion.usuario1;
  }

  esMiMensaje(mensaje: MensajeModel): boolean {
    return mensaje.usuario.id === this.idUsuarioActual;
  }

  private scrollToBottom(): void {
    const container = document.querySelector('.mensajes-container');
    if (container) {
      container.scrollTo({ top: container.scrollHeight, behavior: 'smooth' });
    }
  }

  mostrarModalError = false;
  mensajeModalError = '';
  submensajeModalError = '';

  cerrarModalError() {
    this.mostrarModalError = false;
  }

  agregarAlCarrito() {
    if (this.conversacionSeleccionada && this.conversacionSeleccionada.producto) {
      const prod = this.conversacionSeleccionada.producto as any;
      if (prod.usuario && prod.usuario.id === this.idUsuarioActual) {
        this.mensajeModalError = 'Acción no permitida';
        this.submensajeModalError = 'No puedes adquirir tus propios productos como artesano.';
        this.mostrarModalError = true;
        return;
      }
      this.carritoService.addToCart(prod as ProductoModel);
      this.mostrarAvisoToast();
    }
  }

  mostrarAvisoToast() {
    this.mostrarToast = true;
    setTimeout(() => {
      this.mostrarToast = false;
    }, 3000);
  }
}

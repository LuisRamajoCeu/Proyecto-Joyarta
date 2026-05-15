import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Conversacion } from '../models/conversacionModel';

@Component({
  selector: 'app-conversacion-item',
  imports: [CommonModule],
  templateUrl: './conversacion-item.html',
  styleUrl: './conversacion-item.css',
})
export class ConversacionItem {
  @Input() conversacion!: Conversacion;
  @Input() idUsuarioActual!: number;
  @Input() seleccionada: boolean = false;
  @Output() alSeleccionar = new EventEmitter<Conversacion>();

  seleccionar(): void {
    this.alSeleccionar.emit(this.conversacion);
  }

  get otroUsuario(): any {
      return this.conversacion.usuario1.id === this.idUsuarioActual 
        ? this.conversacion.usuario2 
        : this.conversacion.usuario1;
  }
}

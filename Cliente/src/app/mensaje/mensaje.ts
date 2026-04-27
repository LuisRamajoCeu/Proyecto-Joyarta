import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Mensaje as MensajeModel } from '../models/mensajeModel';

@Component({
  selector: 'app-mensaje',
  imports: [CommonModule, DatePipe],
  templateUrl: './mensaje.html',
  styleUrl: './mensaje.css',
})
export class Mensaje {
  @Input() mensaje!: MensajeModel;
  @Input() idUsuarioActual!: number;

  get esMio(): boolean {
    return this.mensaje?.usuario?.id === this.idUsuarioActual;
  }
}

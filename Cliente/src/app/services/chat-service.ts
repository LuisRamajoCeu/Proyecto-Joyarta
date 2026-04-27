import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Conversacion } from '../models/conversacionModel';
import { Mensaje } from '../models/mensajeModel';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/conversaciones';

  constructor(private http: HttpClient) {}

  getConversaciones(idUsuario: number): Observable<Conversacion[]> {
    return this.http.get<Conversacion[]>(`${this.apiUrl}/usuario/${idUsuario}`);
  }

  crearConversacion(idUsuario1: number, idUsuario2: number, idProducto?: number): Observable<Conversacion> {
    return this.http.post<Conversacion>(this.apiUrl, {
      idUsuario1,
      idUsuario2,
      idProducto
    });
  }

  getMensajes(idConversacion: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/${idConversacion}/mensajes`);
  }

  enviarMensaje(idConversacion: number, idUsuario: number, contenido: string): Observable<Mensaje> {
    return this.http.post<Mensaje>(`${this.apiUrl}/${idConversacion}/mensajes`, {
      idUsuario,
      contenido
    });
  }
}

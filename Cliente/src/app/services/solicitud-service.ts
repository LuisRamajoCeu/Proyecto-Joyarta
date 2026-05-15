import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Solicitud, Propuesta } from '../models/solicitudModel';

@Injectable({
  providedIn: 'root',
})
export class SolicitudService {
  private apiUrl = 'http://localhost:8080/api/solicitudes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(this.apiUrl);
  }

  getById(id: number): Observable<Solicitud> {
    return this.http.get<Solicitud>(`${this.apiUrl}/${id}`);
  }

  crear(datos: any): Observable<Solicitud> {
    return this.http.post<Solicitud>(this.apiUrl, datos);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getMisSolicitudes(userId: number): Observable<Solicitud[]> {
    return this.http.get<Solicitud[]>(`${this.apiUrl}/usuario/${userId}`);
  }

  getPropuestas(solicitudId: number): Observable<Propuesta[]> {
    return this.http.get<Propuesta[]>(`${this.apiUrl}/${solicitudId}/propuestas`);
  }

  enviarPropuesta(solicitudId: number, datos: any): Observable<Propuesta> {
    return this.http.post<Propuesta>(`${this.apiUrl}/${solicitudId}/propuestas`, datos);
  }

  aceptarPropuesta(propuestaId: number): Observable<Propuesta> {
    return this.http.put<Propuesta>(`${this.apiUrl}/propuestas/${propuestaId}/aceptar`, {});
  }

  rechazarPropuesta(propuestaId: number): Observable<Propuesta> {
    return this.http.put<Propuesta>(`${this.apiUrl}/propuestas/${propuestaId}/rechazar`, {});
  }
}

import { Injectable } from '@angular/core';
import { Perfil, Usuario } from '../models/usuarioModel';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductoModel } from '../models/productoModel';

@Injectable({
  providedIn: 'root',
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';
  private usuarioLogueado: Usuario | null = null;
  constructor(private http: HttpClient){
    const guardado = localStorage.getItem('usuario_joyarta');
    if (guardado){
      this.usuarioLogueado = JSON.parse(guardado);
    } 
  }
  login(email: string, password: string): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/login`, { email, password });  
  }
  registrar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/registro`, usuario);
  }
  setUsuario(usuario: Usuario) {
    this.usuarioLogueado = usuario;
    localStorage.setItem('usuario_joyarta', JSON.stringify(usuario));
  }
  getUsuario(): Usuario | null {
    return this.usuarioLogueado;
  }
  estaLogueado(): boolean {
    return this.usuarioLogueado != null;
  }
  logout(){
    this.usuarioLogueado = null;
    localStorage.removeItem('usuario_joyarta');
  }
  getPerfil(idUsuario: number): Observable<Perfil> {
    return this.http.get<Perfil>(`${this.apiUrl}/${idUsuario}/perfil`);
  }
  editarPerfil(idUsuario: number, perfil: Perfil): Observable<Perfil> {
    return this.http.put<Perfil>(`${this.apiUrl}/${idUsuario}/perfil`, perfil);
  }

  getProductosUsuario(id: number): Observable<ProductoModel[]> {
    return this.http.get<ProductoModel[]>(`${this.apiUrl}/${id}/productos`);
  }

  getPedidosUsuario(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}/pedidos`);
  }

  cambiarPassword(id: number, actual: string, nueva: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/password`, { actual, nueva }, { responseType: 'text' });
  }
}

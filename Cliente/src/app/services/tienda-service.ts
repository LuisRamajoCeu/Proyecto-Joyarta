import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProductoModel, Categoria } from '../models/productoModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TiendaService {
  private apiUrl = 'http://localhost:8080/api/tienda';
  constructor(private http: HttpClient) { }
  getProductos(): Observable<ProductoModel[]> {
    return this.http.get<ProductoModel[]>(`${this.apiUrl}/productos`);
  }
  getProductoById(id: number): Observable<ProductoModel> {
    return this.http.get<ProductoModel>(`${this.apiUrl}/productos/${id}`);
  }
  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.apiUrl}/categorias`);
  }
  
  crearProducto(producto: any): Observable<ProductoModel> {
    return this.http.post<ProductoModel>(`${this.apiUrl}/productos`, producto);
  }

  buscarProductos(nombre?: string, categoria?: string, precioMin?: number, precioMax?: number): Observable<ProductoModel[]> {
    let params = new HttpParams();
    if (nombre) params = params.set('nombre', nombre);
    if (categoria) params = params.set('categoria', categoria);
    if (precioMin !== undefined && precioMin !== null) params = params.set('precioMin', precioMin.toString());
    if (precioMax !== undefined && precioMax !== null) params = params.set('precioMax', precioMax.toString());
    
    return this.http.get<ProductoModel[]>(`${this.apiUrl}/productos`, { params });
  }

  editarProducto(id: number, producto: any): Observable<ProductoModel> {
    return this.http.put<ProductoModel>(`${this.apiUrl}/productos/${id}`, producto);
  }

  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/productos/${id}`);
  }
}

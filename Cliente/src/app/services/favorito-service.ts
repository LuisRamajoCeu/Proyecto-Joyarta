import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Favorito } from '../models/favoritoModel';

@Injectable({
  providedIn: 'root'
})
export class FavoritoService {
    private apiUrl = 'http://localhost:8080/api/favoritos';

    constructor(private http: HttpClient) {}

    agregarFavorito(idUsuario: number, idProducto: number): Observable<string> {
        return this.http.post<string>(this.apiUrl, { idUsuario, idProducto }, { responseType: 'text' as 'json' });
    }

    quitarFavorito(idUsuario: number, idProducto: number): Observable<string> {
        return this.http.request<string>('delete', this.apiUrl, { 
            body: { idUsuario, idProducto },
            responseType: 'text' as 'json'
        });
    }
    
    listarFavoritos(): Observable<Favorito[]> {
        return this.http.get<Favorito[]>(this.apiUrl);
    }
    
    esFavorito(idUsuario: number, idProducto: number): Observable<boolean> {
        return this.http.get<boolean>(`${this.apiUrl}/verificar/${idUsuario}/${idProducto}`);
    }
}

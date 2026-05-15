import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProductoModel } from '../models/productoModel';
import { TiendaService } from '../services/tienda-service';
import { AsyncPipe, CommonModule } from '@angular/common';
import { Producto } from '../producto/producto';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [AsyncPipe, CommonModule, Producto, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  novedades$: Observable<ProductoModel[]>;
  
  constructor(private tiendaService: TiendaService) {
    this.novedades$ = this.tiendaService.getProductos().pipe(
      map(productos => productos
        .sort((a, b) => b.id - a.id)
        .slice(0, 9)
      )
    );
  }
}

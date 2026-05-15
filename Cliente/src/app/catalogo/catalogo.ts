import { Component, OnInit } from '@angular/core';
import { TiendaService } from '../services/tienda-service';
import { ProductoModel, Categoria } from '../models/productoModel';
import { Observable } from 'rxjs';
import { AsyncPipe, CommonModule } from '@angular/common';
import { Producto } from '../producto/producto';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-catalogo',
  imports: [AsyncPipe, CommonModule, Producto, FormsModule],
  templateUrl: './catalogo.html',
  styleUrl: './catalogo.css',
})
export class Catalogo implements OnInit {
  todosProductos: ProductoModel[] = [];
  productosPagina: ProductoModel[] = [];
  categorias$: Observable<Categoria[]>;
  
  filtroNombre: string = '';
  filtroCategoria: string = '';
  filtroPrecioMin: number | null = null;
  filtroPrecioMax: number | null = null;

  paginaActual = 1;
  productosPorPagina = 20;
  totalPaginas = 1;
  
  constructor(private tiendaService: TiendaService) {
    this.categorias$ = this.tiendaService.getCategorias();
  }
  
  ngOnInit(): void {
    this.aplicarFiltros();
  }
  
  aplicarFiltros(): void {
    this.paginaActual = 1;
    this.tiendaService.buscarProductos(
      this.filtroNombre || undefined,
      this.filtroCategoria || undefined,
      this.filtroPrecioMin ?? undefined,
      this.filtroPrecioMax ?? undefined
    ).subscribe(productos => {
      this.todosProductos = productos;
      this.totalPaginas = Math.max(1, Math.ceil(productos.length / this.productosPorPagina));
      this.actualizarPagina();
    });
  }
  
  actualizarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.productosPorPagina;
    const fin = inicio + this.productosPorPagina;
    this.productosPagina = this.todosProductos.slice(inicio, fin);
  }

  irAPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.actualizarPagina();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  get paginas(): number[] {
    return Array.from({ length: this.totalPaginas }, (_, i) => i + 1);
  }

  limpiarFiltros(): void {
    this.filtroNombre = '';
    this.filtroCategoria = '';
    this.filtroPrecioMin = null;
    this.filtroPrecioMax = null;
    this.aplicarFiltros();
  }
}

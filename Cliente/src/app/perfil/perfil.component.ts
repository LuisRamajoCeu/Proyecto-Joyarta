import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../services/usuario-service';
import { TiendaService } from '../services/tienda-service';
import { ProductoModel, Categoria } from '../models/productoModel';
import { Perfil } from '../models/usuarioModel';
import { Router, RouterModule } from '@angular/router';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  usuario: any;
  perfil: any;
  productos: ProductoModel[] = [];
  pedidos: any[] = [];
  activeTab: 'productos' | 'pedidos' | 'editar' | 'seguridad' = 'productos';

  editNombre = '';
  editBiografia = '';
  editDireccion = '';
  editAvatarUrl = '';
  guardando = false;
  mensajeExito = '';
  mensajeError = '';

  passActual = '';
  passNueva = '';
  passConfirmar = '';
  guardandoPass = false;
  mensajeExitoPass = '';
  mensajeErrorPass = '';

  // ---- Subir Producto ----
  mostrarModalProducto = false;
  categorias: Categoria[] = [];
  nuevoProducto = {
    nombre: '',
    descripcion: '',
    precio: null as number | null,
    stock: null as number | null,
    imagenUrl: '',
    categoriaId: null as number | null
  };
  guardandoProducto = false;
  mensajeExitoProducto = '';
  mensajeErrorProducto = '';

  constructor(
    private usuarioService: UsuarioService,
    private tiendaService: TiendaService,
    private router: Router
  ) {}

  async ngOnInit() {
    this.usuario = this.usuarioService.getUsuario();
    if (!this.usuario) {
      this.router.navigate(['/login']);
      return;
    }

    await this.loadPerfil();
    await this.loadProductos();
    await this.loadPedidos();
    await this.loadCategorias();
  }

  async loadPerfil() {
    try {
      this.perfil = await lastValueFrom(this.usuarioService.getPerfil(this.usuario.id));
      this.syncEditFields();
    } catch (err) {
      console.error('Error loading perfil', err);
    }
  }

  syncEditFields() {
    this.editNombre = this.perfil?.nombre || this.usuario?.nombre || '';
    this.editBiografia = this.perfil?.biografia || '';
    this.editDireccion = this.perfil?.direccion || '';
    this.editAvatarUrl = this.perfil?.avatarUrl || '';
  }

  async loadProductos() {
    try {
      this.productos = await lastValueFrom(this.usuarioService.getProductosUsuario(this.usuario.id));
    } catch (err) {
      console.error('Error loading productos', err);
    }
  }

  async loadPedidos() {
    try {
      this.pedidos = await lastValueFrom(this.usuarioService.getPedidosUsuario(this.usuario.id));
    } catch (err) {
      console.error('Error loading pedidos', err);
    }
  }

  async loadCategorias() {
    try {
      this.categorias = await lastValueFrom(this.tiendaService.getCategorias());
    } catch (err) {
      console.error('Error loading categorias', err);
    }
  }

  async guardarPerfil() {
    this.guardando = true;
    this.mensajeExito = '';
    this.mensajeError = '';

    const perfilActualizado: Perfil = {
      nombre: this.editNombre,
      biografia: this.editBiografia,
      direccion: this.editDireccion,
      avatarUrl: this.editAvatarUrl
    };

    try {
      this.perfil = await lastValueFrom(
        this.usuarioService.editarPerfil(this.usuario.id, perfilActualizado)
      );
      this.mensajeExito = '¡Perfil actualizado correctamente!';
      
      this.usuario.nombre = this.editNombre;
      this.usuario.perfil = this.perfil;
      this.usuarioService.setUsuario(this.usuario);

      setTimeout(() => this.mensajeExito = '', 3000);
    } catch (err) {
      console.error('Error al guardar perfil', err);
      this.mensajeError = 'Error al guardar los cambios. Inténtalo de nuevo.';
      setTimeout(() => this.mensajeError = '', 3000);
    } finally {
      this.guardando = false;
    }
  }

  async cambiarPassword() {
    this.mensajeExitoPass = '';
    this.mensajeErrorPass = '';

    if (!this.passActual || !this.passNueva || !this.passConfirmar) {
      this.mensajeErrorPass = 'Rellena todos los campos.';
      return;
    }
    if (this.passNueva.length < 6) {
      this.mensajeErrorPass = 'La nueva contraseña debe tener al menos 6 caracteres.';
      return;
    }
    if (this.passNueva !== this.passConfirmar) {
      this.mensajeErrorPass = 'Las contraseñas no coinciden.';
      return;
    }

    this.guardandoPass = true;
    try {
      await lastValueFrom(
        this.usuarioService.cambiarPassword(this.usuario.id, this.passActual, this.passNueva)
      );
      this.mensajeExitoPass = '¡Contraseña actualizada correctamente!';
      this.passActual = '';
      this.passNueva = '';
      this.passConfirmar = '';
      setTimeout(() => this.mensajeExitoPass = '', 3000);
    } catch (err: any) {
      if (err.status === 403) {
        this.mensajeErrorPass = 'La contraseña actual no es correcta.';
      } else {
        this.mensajeErrorPass = 'Error al cambiar la contraseña.';
      }
      setTimeout(() => this.mensajeErrorPass = '', 4000);
    } finally {
      this.guardandoPass = false;
    }
  }

  onTabChange(tab: 'productos' | 'pedidos' | 'editar' | 'seguridad') {
    this.activeTab = tab;
    if (tab === 'editar') {
      this.syncEditFields();
    }
  }

  getInitials(nombre: string | undefined): string {
    if (!nombre) return '?';
    const words = nombre.trim().split(' ');
    if (words.length >= 2) {
      return (words[0][0] + words[1][0]).toUpperCase();
    }
    return nombre.substring(0, 2).toUpperCase();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.editAvatarUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  eliminarFoto() {
    this.editAvatarUrl = '';
  }

  // ---- Métodos para subir producto ----

  abrirModalProducto() {
    this.nuevoProducto = {
      nombre: '',
      descripcion: '',
      precio: null,
      stock: null,
      imagenUrl: '',
      categoriaId: null
    };
    this.mensajeExitoProducto = '';
    this.mensajeErrorProducto = '';
    this.mostrarModalProducto = true;
  }

  cerrarModalProducto() {
    this.mostrarModalProducto = false;
  }

  onProductoImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.nuevoProducto.imagenUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  async subirProducto() {
    this.mensajeExitoProducto = '';
    this.mensajeErrorProducto = '';

    if (!this.nuevoProducto.nombre || !this.nuevoProducto.precio || !this.nuevoProducto.stock) {
      this.mensajeErrorProducto = 'Rellena al menos el nombre, precio y stock.';
      return;
    }

    this.guardandoProducto = true;

    const productoPayload: any = {
      nombre: this.nuevoProducto.nombre,
      descripcion: this.nuevoProducto.descripcion,
      precio: this.nuevoProducto.precio,
      stock: this.nuevoProducto.stock,
      imagenUrl: this.nuevoProducto.imagenUrl,
      usuario: { id: this.usuario.id },
      categoria: this.nuevoProducto.categoriaId ? { id: this.nuevoProducto.categoriaId } : null
    };

    try {
      await lastValueFrom(this.tiendaService.crearProducto(productoPayload));
      this.mensajeExitoProducto = '¡Producto subido correctamente!';
      await this.loadProductos();
      setTimeout(() => {
        this.cerrarModalProducto();
      }, 1500);
    } catch (err) {
      console.error('Error al subir producto', err);
      this.mensajeErrorProducto = 'Error al subir el producto. Inténtalo de nuevo.';
    } finally {
      this.guardandoProducto = false;
    }
  }
}

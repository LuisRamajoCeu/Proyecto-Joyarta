import { Component, HostListener } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { UsuarioService } from './services/usuario-service';
import { CarritoService } from './services/carrito-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'Joyarta';
  navScrolled = false;

  constructor(public usuarioService: UsuarioService, public carritoService: CarritoService, private router: Router) {}

  @HostListener('window:scroll')
  onScroll() {
    this.navScrolled = window.scrollY > 30;
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/home']);
  }
}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { UsuarioService } from '../services/usuario-service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email: string = '';
  password: string = '';
  errorMsg: string = '';
  shakeCard: boolean = false;

  constructor(private usuarioService: UsuarioService, private router: Router) {}
  async onLogin() {
    try {
      const usuarioRecibido = await lastValueFrom(
        this.usuarioService.login(this.email, this.password)
      );
      console.log('Login exitoso:', usuarioRecibido);
      this.usuarioService.setUsuario(usuarioRecibido);
      this.router.navigate(['/home']);
    } catch (error) {
      console.error(error);
      this.errorMsg = 'Usuario o contraseña incorrectos.';
      this.shakeCard = true;
      setTimeout(() => this.shakeCard = false, 500);
    }
  }
}


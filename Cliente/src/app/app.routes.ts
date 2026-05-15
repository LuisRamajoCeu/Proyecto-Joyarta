import { Routes } from '@angular/router';
import { Chat } from './chat/chat';

import { Login } from './login/login';
import { DetalleProducto } from './detalle-producto/detalle-producto';
import { Catalogo } from './catalogo/catalogo';
import { Home } from './home/home';
import { Registro } from './registro/registro';
import { MisFavoritos } from './mis-favoritos/mis-favoritos';
import { Carrito } from './carrito/carrito';
import { PerfilComponent } from './perfil/perfil';
import { NotFound } from './not-found/not-found';
import { Solicitudes } from './solicitudes/solicitudes';
import { DetalleSolicitud } from './detalle-solicitud/detalle-solicitud';

export const routes: Routes = [
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: 'home', component: Home },
    { path: 'catalogo', component: Catalogo },
    { path: 'productos/:id', component: DetalleProducto },
    { path: 'login', component: Login },
    { path: 'chat', component: Chat },
    { path: 'registro', component: Registro },
    { path: 'favoritos', component: MisFavoritos },
    { path: 'carrito', component: Carrito },
    { path: 'perfil', component: PerfilComponent },
    { path: 'solicitudes', component: Solicitudes },
    { path: 'solicitudes/:id', component: DetalleSolicitud },
    { path: '**', component: NotFound }
];

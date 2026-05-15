import { Usuario } from './usuarioModel';

export interface Solicitud {
    id?: number;
    titulo: string;
    descripcion?: string;
    imagenUrl?: string;
    presupuestoEstimado?: number;
    estado?: string;
    fechaCreacion?: string;
    cliente?: Usuario;
}

export interface Propuesta {
    id?: number;
    presupuesto: number;
    tiempoEstimado: string;
    mensaje?: string;
    estado?: string;
    fechaCreacion?: string;
    solicitud?: Solicitud;
    artesano?: Usuario;
}

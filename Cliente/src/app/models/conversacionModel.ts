export interface Conversacion {
  id: number;
  fechaCreacion: string;
  activo: boolean;
  usuario1: {
    id: number;
    nombre: string;
    email: string;
  };
  usuario2: {
    id: number;
    nombre: string;
    email: string;
  };
  producto?: {
    id: number;
    nombre: string;
    imagenUrl: string;
    precio: number;
  };
}

export interface Mensaje {
  id: number;
  contenido: string;
  fechaEnvio: string;
  usuario: {
    id: number;
    nombre: string;
    email: string;
  };
}

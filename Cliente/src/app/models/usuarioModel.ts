export interface Perfil {
    id?: number;
    nombre: string;
    biografia?: string;
    avatarUrl?: string;
    direccion?: string;
    usuario?: Usuario;
}

export interface Usuario {
    id?: number;
    nombre?: string;
    email: string;
    password?: string;
    rol?: string;
    fechaRegistro?: string;
    perfil?: Perfil;
}
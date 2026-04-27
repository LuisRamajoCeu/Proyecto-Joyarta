import { Usuario } from "./usuarioModel";

export interface Categoria {
    id: number,
    nombre: string,
    descripcion: string,
    productos: ProductoModel[]
}

export interface ProductoModel {
    id: number,
    nombre: string,
    descripcion: string,
    precio: number,
    stock: number,
    imagenUrl: string,
    esFavorito?: boolean,
    usuario: Usuario,
    categoria: Categoria;
}
import { ProductoModel } from "./productoModel";
import { Usuario } from "./usuarioModel";

export interface Favorito {
    id: number;
    fechaAgregado: string;
    usuario: Usuario;
    producto: ProductoModel;
}

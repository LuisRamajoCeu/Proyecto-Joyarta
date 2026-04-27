import { ProductoModel } from "./productoModel";
import { Usuario } from "./usuarioModel";

export interface DetallePedido {
    id: number,
    cantidad: number,
    precioUnitario: number,
    pedido: Pedido,
    producto: ProductoModel
}

export interface Pedido {
    id: number,
    fecha: string,
    total: number,
    estadoPago: string,
    usuario: Usuario,
    listaDetallePedidos: DetallePedido[]
}
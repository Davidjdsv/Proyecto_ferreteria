package RegistroVentas;

public class RegistroVentas {
    int id_venta;
    int id_orden_compra;
    int id_producto;
    int cantidad;
    float sub_total;

    public RegistroVentas(int id_venta, int id_orden_compra, int id_producto, int cantidad, float sub_total) {
        this.id_venta = id_venta;
        this.id_orden_compra = id_orden_compra;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.sub_total = sub_total;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_orden_compra() {
        return id_orden_compra;
    }

    public void setId_orden_compra(int id_orden_compra) {
        this.id_orden_compra = id_orden_compra;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getSub_total() {
        return sub_total;
    }

    public void setSub_total(float sub_total) {
        this.sub_total = sub_total;
    }
}

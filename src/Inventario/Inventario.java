package Inventario;

public class Inventario {
    private int id_producto;
    private String nombre_producto;
    private String categoria;
    private double cantidad_stock;
    private int precio_producto;
    private int id_proveedor_asociado;

    public Inventario(int id_producto, String nombre_producto, String categoria,
                      double cantidad_stock, int precio_producto, int id_proveedor_asociado) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.categoria = categoria;
        this.cantidad_stock = cantidad_stock;
        this.precio_producto = precio_producto;
        this.id_proveedor_asociado = id_proveedor_asociado;
    }

    // Getters and Setters
    public int getId_inventario() {
        return id_producto;
    }

    public void setId_inventario(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre_producto;
    }

    public void setNombre(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return cantidad_stock;
    }

    public void setPrecio(double cantidad_stock) {
        this.cantidad_stock = cantidad_stock;
    }

    public int getCantidad_disponible() {
        return precio_producto;
    }

    public void setCantidad_disponible(int precio_producto) {
        this.precio_producto = precio_producto;
    }

    public int getId_proveedor() {
        return id_proveedor_asociado;
    }

    public void setId_proveedor(int id_proveedor_asociado) {
        this.id_proveedor_asociado = id_proveedor_asociado;
    }
}
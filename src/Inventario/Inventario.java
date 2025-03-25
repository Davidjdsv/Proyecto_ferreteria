package Inventario;

public class Inventario {
    private int id_inventario;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad_disponible;
    private int id_proveedor;

    public Inventario(int id_inventario, String nombre, String categoria,
                      double precio, int cantidad_disponible, int id_proveedor) {
        this.id_inventario = id_inventario;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad_disponible = cantidad_disponible;
        this.id_proveedor = id_proveedor;
    }

    // Getters and Setters
    public int getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(int id_inventario) {
        this.id_inventario = id_inventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad_disponible() {
        return cantidad_disponible;
    }

    public void setCantidad_disponible(int cantidad_disponible) {
        this.cantidad_disponible = cantidad_disponible;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }
}
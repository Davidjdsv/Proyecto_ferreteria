package Proveedores;

public class Proveedores {
    int id_proveedor;
    String nombre;
    String contacto;
    String categoria_producto;

    public Proveedores(int id_proveedor, String contacto, String nombre, String categoria_producto) {
        this.id_proveedor = id_proveedor;
        this.contacto = contacto;
        this.nombre = nombre;
        this.categoria_producto = categoria_producto;
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCategoria_producto() {
        return categoria_producto;
    }

    public void setCategoria_producto(String categoria_producto) {
        this.categoria_producto = categoria_producto;
    }
}

package Proveedores;

/**
 * Representa un proveedor en el sistema de gestión de pedidos de la ferretería.
 * Contiene información sobre el proveedor, como su ID, nombre, contacto y categoría de producto.
 *
 * @author Davidjdsv
 */
public class Proveedores {
    private int id_proveedor;
    private String nombre;
    private String contacto;
    private String categoria_producto;

    /**
     * Constructor de la clase Proveedores.
     *
     * @param id_proveedor Identificador único del proveedor en la base de datos.
     * @param nombre Nombre del proveedor.
     * @param contacto Información de contacto del proveedor.
     * @param categoria_producto Categoría de productos que suministra el proveedor.
     */
    public Proveedores(int id_proveedor, String nombre, String contacto, String categoria_producto) {
        this.id_proveedor = id_proveedor;
        this.nombre = nombre;
        this.contacto = contacto;
        this.categoria_producto = categoria_producto;
    }

    /**
     * Obtiene el ID del proveedor.
     *
     * @return ID del proveedor.
     */
    public int getId_proveedor() {
        return id_proveedor;
    }

    /**
     * Establece el ID del proveedor.
     *
     * @param id_proveedor Nuevo ID del proveedor.
     */
    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    /**
     * Obtiene el nombre del proveedor.
     *
     * @return Nombre del proveedor.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del proveedor.
     *
     * @param nombre Nuevo nombre del proveedor.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el contacto del proveedor.
     *
     * @return Contacto del proveedor.
     */
    public String getContacto() {
        return contacto;
    }

    /**
     * Establece el contacto del proveedor.
     *
     * @param contacto Nueva información de contacto del proveedor.
     */
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    /**
     * Obtiene la categoría de productos que suministra el proveedor.
     *
     * @return Categoría de productos del proveedor.
     */
    public String getCategoria_producto() {
        return categoria_producto;
    }

    /**
     * Establece la categoría de productos que suministra el proveedor.
     *
     * @param categoria_producto Nueva categoría de productos del proveedor.
     */
    public void setCategoria_producto(String categoria_producto) {
        this.categoria_producto = categoria_producto;
    }
}

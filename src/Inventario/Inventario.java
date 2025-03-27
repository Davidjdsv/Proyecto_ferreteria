package Inventario;

/**
 * Representa un producto en el inventario con sus atributos básicos.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class Inventario {
    /** Identificador único del producto */
    private int id_producto;

    /** Nombre del producto */
    private String nombre_producto;

    /** Categoría del producto */
    private String categoria;

    /** Cantidad en stock del producto */
    private int cantidad_stock;

    /** Precio del producto */
    private int precio_producto;

    /** Identificador del proveedor asociado (puede ser nulo) */
    private Integer id_proveedor_asociado;

    /**
     * Constructor para crear un nuevo producto de inventario.
     *
     * @param id_producto Identificador del producto
     * @param nombre_producto Nombre del producto
     * @param categoria Categoría del producto
     * @param cantidad_stock Cantidad en stock
     * @param precio_producto Precio del producto
     * @param id_proveedor_asociado Identificador del proveedor (puede ser nulo)
     */
    public Inventario(int id_producto, String nombre_producto, String categoria,
                      int cantidad_stock, int precio_producto, Integer id_proveedor_asociado) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.categoria = categoria;
        this.cantidad_stock = cantidad_stock;
        this.precio_producto = precio_producto;
        this.id_proveedor_asociado = id_proveedor_asociado;
    }

    /**
     * Obtiene el ID del producto.
     *
     * @return Identificador del producto
     */
    public int getId_producto() {
        return id_producto;
    }

    /**
     * Establece el ID del producto.
     *
     * @param id_producto Nuevo identificador del producto
     */
    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return Nombre del producto
     */
    public String getNombre_producto() {
        return nombre_producto;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre_producto Nuevo nombre del producto
     */
    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return Categoría del producto
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param categoria Nueva categoría del producto
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la cantidad en stock del producto.
     *
     * @return Cantidad en stock
     */
    public int getCantidad_stock() {
        return cantidad_stock;
    }

    /**
     * Establece la cantidad en stock del producto.
     *
     * @param cantidad_stock Nueva cantidad en stock
     */
    public void setCantidad_stock(int cantidad_stock) {
        this.cantidad_stock = cantidad_stock;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return Precio del producto
     */
    public int getPrecio_producto() {
        return precio_producto;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio_producto Nuevo precio del producto
     */
    public void setPrecio_producto(int precio_producto) {
        this.precio_producto = precio_producto;
    }

    /**
     * Obtiene el ID del proveedor asociado.
     *
     * @return Identificador del proveedor (puede ser nulo)
     */
    public Integer getId_proveedor_asociado() {
        return id_proveedor_asociado;
    }

    /**
     * Establece el ID del proveedor asociado.
     *
     * @param id_proveedor_asociado Nuevo identificador del proveedor (puede ser nulo)
     */
    public void setId_proveedor_asociado(Integer id_proveedor_asociado) {
        this.id_proveedor_asociado = id_proveedor_asociado;
    }
}
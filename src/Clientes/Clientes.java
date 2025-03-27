package Clientes;

/**
 * Clase Clientes que representa a un cliente con sus atributos básicos.
 */
public class Clientes {
    private int id_cliente;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;

    /**
     * Constructor de la clase Clientes.
     * @param id_cliente Identificador único del cliente.
     * @param nombre Nombre del cliente.
     * @param telefono Número de teléfono del cliente.
     * @param direccion Dirección del cliente.
     * @param correo Correo electrónico del cliente.
     */
    public Clientes(int id_cliente, String nombre, String telefono, String direccion, String correo) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    /**
     * Obtiene el ID del cliente.
     * @return ID del cliente.
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * Establece el ID del cliente.
     * @param id_cliente Nuevo ID del cliente.
     */
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    /**
     * Obtiene el nombre del cliente.
     * @return Nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     * @param nombre Nuevo nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     * @return Número de teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     * @param telefono Nuevo número de teléfono del cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del cliente.
     * @return Dirección del cliente.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del cliente.
     * @param direccion Nueva dirección del cliente.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     * @return Correo electrónico del cliente.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del cliente.
     * @param correo Nuevo correo electrónico del cliente.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
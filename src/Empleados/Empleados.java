package Empleados;

/**
 * Representa un empleado con sus atributos básicos.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class Empleados
{
    /** Identificador único del empleado */
    int id_empleado;

    /** Nombre del empleado */
    String nombre;

    /** Cargo del empleado */
    String cargo;

    /** Salario del empleado */
    double salario;

    /**
     * Constructor para crear un nuevo empleado.
     *
     * @param id_empleado Identificador único del empleado
     * @param nombre Nombre del empleado
     * @param cargo Cargo del empleado
     * @param salario Salario del empleado
     */
    public Empleados(int id_empleado, String nombre, String cargo, double salario)
    {
        this.id_empleado = id_empleado;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
    }

    /**
     * Obtiene el ID del empleado.
     *
     * @return Identificador del empleado
     */
    public int getId_empleado() {
        return id_empleado;
    }

    /**
     * Establece el ID del empleado.
     *
     * @param id_empleado Nuevo identificador del empleado
     */
    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    /**
     * Obtiene el nombre del empleado.
     *
     * @return Nombre del empleado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del empleado.
     *
     * @param nombre Nuevo nombre del empleado
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el cargo del empleado.
     *
     * @return Cargo del empleado
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Establece el cargo del empleado.
     *
     * @param cargo Nuevo cargo del empleado
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Obtiene el salario del empleado.
     *
     * @return Salario del empleado
     */
    public double getSalario() {
        return salario;
    }

    /**
     * Establece el salario del empleado.
     *
     * @param salario Nuevo salario del empleado
     */
    public void setSalario(double salario) {
        this.salario = salario;
    }
}
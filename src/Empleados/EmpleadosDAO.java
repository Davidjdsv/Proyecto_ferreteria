package Empleados;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase de Acceso a Datos (DAO) para operaciones CRUD de Empleados.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class EmpleadosDAO
{
    /** Conexión a la base de datos */
    private ConexionDB conexionDB = new ConexionDB();

    /**
     * Agrega un nuevo empleado a la base de datos.
     *
     * @param empleados Objeto Empleados a ser agregado
     */
    public void agregar(Empleados empleados)
    {
        Connection con = conexionDB.getConnection();

        String query = "INSERT INTO empleados (nombre, cargo, salario) VALUES (?,?,?)";

        try
        {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, empleados.getNombre());
            pst.setString(2, empleados.getCargo());
            pst.setDouble(3, empleados.getSalario());

            int resultado = pst.executeUpdate();

            if (resultado > 0)
            {
                JOptionPane.showMessageDialog(null, "Empleado agregado correctamente");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al agregar empleado");
            }

            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un empleado de la base de datos por su ID.
     *
     * @param id_empleado Identificador del empleado a eliminar
     */
    public void eliminar(int id_empleado)
    {
        Connection con = conexionDB.getConnection();

        String query = "DELETE FROM empleados WHERE id_empleado=?";

        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_empleado);

            int resultado = pst.executeUpdate();

            if (resultado > 0)
            {
                JOptionPane.showMessageDialog(null, "Empleado eliminado correctamente");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al eliminar empleado");
            }

            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la información de un empleado en la base de datos.
     *
     * @param empleados Objeto Empleados con la información actualizada
     */
    public void actualizar(Empleados empleados)
    {
        Connection con = conexionDB.getConnection();
        String query = "UPDATE empleados SET nombre = ?, cargo = ?, salario = ? WHERE id_empleado = ?";

        try
        {
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, empleados.getNombre());
            pst.setString(2, empleados.getCargo());
            pst.setDouble(3, empleados.getSalario());
            pst.setInt(4, empleados.getId_empleado());

            int resultado = pst.executeUpdate();

            if (resultado > 0)
            {
                JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Error al modificar empleado");
            }

            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
package Empleados;

import Conexion.ConexionBD;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmpleadosDAO
{
    private ConexionBD conexionBD = new ConexionBD();

    public void agregar(Empleados empleados)
    {
        Connection con = conexionBD.getConnection();

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

    public void eliminar(int id_empleado)
    {
        Connection con = conexionBD.getConnection();

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

    public void actualizar(Empleados empleados)
    {
        Connection con = conexionBD.getConnection();
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
package Clientes;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientesDAO {

    private ConexionDB conexionDB = new ConexionDB();

    public void agregar(Clientes clientes) {
        Connection con = conexionDB.getConnection(); // Corregido

        String query = "INSERT INTO clientes (nombre, telefono, direccion, correo) VALUES (?, ?, ?, ?)";

        try
        {
            PreparedStatement pst = con.prepareStatement(query); // Corregido

            pst.setString(1, clientes.getNombre());
            pst.setString(2, clientes.getTelefono());
            pst.setString(3, clientes.getDireccion()); // Corregido (antes faltaba)
            pst.setString(4, clientes.getCorreo());

            int resultado = pst.executeUpdate();
            if (resultado > 0)
            {
                JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no agregado");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void eliminar(int id_cliente)
    {
        Connection con = conexionDB.getConnection();

        String query = "DELETE FROM clientes WHERE id_cliente = ?";

        try
        {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1,id_cliente);

            int resultado = pst.executeUpdate();

            if(resultado>0)
            {
                JOptionPane.showMessageDialog(null,"Cliente eliminado exitosamente");
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Cliente no eliminado");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


    }

}

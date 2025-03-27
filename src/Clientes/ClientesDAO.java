package Clientes;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase ClientesDAO que maneja las operaciones de base de datos relacionadas con la tabla "clientes".
 */
public class ClientesDAO {

    private ConexionDB conexionDB = new ConexionDB();

    /**
     * Agrega un nuevo cliente a la base de datos.
     * @param clientes Objeto de tipo Clientes con la información del nuevo cliente.
     */
    public void agregar(Clientes clientes) {
        Connection con = conexionDB.getConnection();
        String query = "INSERT INTO clientes (nombre, telefono, direccion, correo) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, clientes.getNombre());
            pst.setString(2, clientes.getTelefono());
            pst.setString(3, clientes.getDireccion());
            pst.setString(4, clientes.getCorreo());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no agregado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un cliente de la base de datos dado su ID.
     * @param id_cliente ID del cliente a eliminar.
     */
    public void eliminar(int id_cliente) {
        Connection con = conexionDB.getConnection();
        String query = "DELETE FROM clientes WHERE id_cliente = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_cliente);

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no eliminado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     * @param clientes Objeto de tipo Clientes con los nuevos datos del cliente.
     */
    public void actualizar(Clientes clientes) {
        Connection con = conexionDB.getConnection();
        String query = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ?, correo = ? WHERE id_cliente = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, clientes.getNombre());
            pst.setString(2, clientes.getTelefono());
            pst.setString(3, clientes.getDireccion());
            pst.setString(4, clientes.getCorreo());
            pst.setInt(5, clientes.getId_cliente());

            int resultado = pst.executeUpdate();
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no actualizado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

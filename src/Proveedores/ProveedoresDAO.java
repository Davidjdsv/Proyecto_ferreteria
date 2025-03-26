package Proveedores;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase ProveedoresDAO que maneja las operaciones CRUD (Agregar, Eliminar y Actualizar)
 * para la tabla "proveedores" en la base de datos.
 * Se conecta a la base de datos utilizando la clase {@link ConexionDB}.
 *
 * @author Davidjdsv
 */
public class ProveedoresDAO {
    private ConexionDB conexionDB = new ConexionDB();

    /**
     * Agrega un nuevo proveedor a la base de datos.
     *
     * @param proveedores Objeto de la clase {@link Proveedores} que contiene la información del proveedor a agregar.
     */
    public void agregar(Proveedores proveedores) {
        Connection con = conexionDB.getConnection();
        String query = "INSERT INTO proveedores (nombre, contacto, categoria_producto) VALUES (?, ?, ?)";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, proveedores.getNombre());
            pst.setString(2, proveedores.getContacto());
            pst.setString(3, proveedores.getCategoria_producto());

            int resultado = pst.executeUpdate();
            String mensaje = resultado > 0 ? "Proveedor ingresado con éxito" : "Ups! Ocurrió un error al agregar al proveedor...";
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un proveedor de la base de datos basado en su ID.
     *
     * @param id_proveedor Identificador único del proveedor a eliminar.
     */
    public void eliminar(int id_proveedor) {
        Connection con = conexionDB.getConnection();
        String query = "DELETE FROM proveedores WHERE id_proveedor = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_proveedor);

            int resultado = pst.executeUpdate();
            String mensaje = resultado > 0 ? "Proveedor eliminado con éxito!" : "Ups! Ocurrió un problema al eliminar el proveedor...";
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de un proveedor en la base de datos.
     *
     * @param proveedores Objeto de la clase {@link Proveedores} con los datos actualizados del proveedor.
     */
    public void actualizar(Proveedores proveedores) {
        Connection con = conexionDB.getConnection();
        String query = "UPDATE proveedores SET nombre = ?, contacto = ?, categoria_producto = ? WHERE id_proveedor = ?";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, proveedores.getNombre());
            pst.setString(2, proveedores.getContacto());
            pst.setString(3, proveedores.getCategoria_producto());
            pst.setInt(4, proveedores.getId_proveedor());

            int resultado = pst.executeUpdate();
            String mensaje = resultado > 0 ? "Proveedor actualizado con éxito!" : "Ups! Ocurrió un error al actualizar al proveedor";
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

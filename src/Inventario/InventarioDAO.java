package Inventario;

import Conexion.ConexionDB;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase de Acceso a Datos (DAO) para operaciones de gestión de inventario.
 * Maneja la inserción, actualización y eliminación de productos.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class InventarioDAO {
    /** Conexión a la base de datos */
    private ConexionDB conexionDB = new ConexionDB();

    /**
     * Agrega un nuevo producto al inventario.
     * Valida la existencia del proveedor antes de insertar.
     *
     * @param inventario Objeto Inventario a insertar
     */
    public void agregar(Inventario inventario) {
        Connection con = conexionDB.getConnection();

        try {
            // Verificar si el proveedor existe (si se proporciona)
            if (inventario.getId_proveedor_asociado() != null &&
                    !validarProveedor(inventario.getId_proveedor_asociado())) {
                JOptionPane.showMessageDialog(null,
                        "Error: El proveedor con ID " + inventario.getId_proveedor_asociado() + " no existe",
                        "Error de Proveedor",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Consulta para insertar nuevo producto
            String query = "INSERT INTO inventario_productos (nombre_producto, categoria, cantidad_stock, precio_producto, id_proveedor_asociado) VALUES (?,?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(query);

            // Establecer parámetros
            pst.setString(1, inventario.getNombre_producto());
            pst.setString(2, inventario.getCategoria());
            pst.setInt(3, inventario.getCantidad_stock());
            pst.setInt(4, inventario.getPrecio_producto());

            // Manejar el caso en que el proveedor puede ser null
            if (inventario.getId_proveedor_asociado() != null) {
                pst.setInt(5, inventario.getId_proveedor_asociado());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }

            // Ejecutar inserción
            int resultado = pst.executeUpdate();

            // Mostrar mensaje de resultado
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto agregado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar producto");
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina un producto del inventario por su ID.
     *
     * @param id_producto ID del producto a eliminar
     */
    public void eliminar(int id_producto) {
        Connection con = conexionDB.getConnection();

        try {
            // Consulta para eliminar producto
            String query = "DELETE FROM inventario_productos WHERE id_producto = ?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_producto);

            // Ejecutar eliminación
            int resultado = pst.executeUpdate();

            // Mostrar mensaje de resultado
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado");
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la información de un producto existente.
     * Valida la existencia del proveedor antes de actualizar.
     *
     * @param inventario Objeto Inventario con la información actualizada
     */
    public void actualizar(Inventario inventario) {
        Connection con = conexionDB.getConnection();

        try {
            // Verificar si el proveedor existe (si se proporciona)
            if (inventario.getId_proveedor_asociado() != null &&
                    !validarProveedor(inventario.getId_proveedor_asociado())) {
                JOptionPane.showMessageDialog(null,
                        "Error: El proveedor con ID " + inventario.getId_proveedor_asociado() + " no existe",
                        "Error de Proveedor",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Consulta para actualizar producto
            String query = "UPDATE inventario_productos SET nombre_producto = ?, categoria = ?, cantidad_stock = ?, precio_producto = ?, id_proveedor_asociado = ? WHERE id_producto = ?";

            PreparedStatement pst = con.prepareStatement(query);

            // Establecer parámetros
            pst.setString(1, inventario.getNombre_producto());
            pst.setString(2, inventario.getCategoria());
            pst.setInt(3, inventario.getCantidad_stock());
            pst.setInt(4, inventario.getPrecio_producto());

            // Manejar el caso en que el proveedor puede ser null
            if (inventario.getId_proveedor_asociado() != null) {
                pst.setInt(5, inventario.getId_proveedor_asociado());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }

            pst.setInt(6, inventario.getId_producto());

            // Ejecutar actualización
            int resultado = pst.executeUpdate();

            // Mostrar mensaje de resultado
            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al modificar producto");
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Valida la existencia de un proveedor en la base de datos.
     *
     * @param id_proveedor ID del proveedor a validar
     * @return true si el proveedor existe, false en caso contrario
     */
    private boolean validarProveedor(int id_proveedor) {
        Connection con = conexionDB.getConnection();

        try {
            // Consulta para contar proveedores con el ID especificado
            String query = "SELECT COUNT(*) FROM proveedores WHERE id_proveedor = ?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id_proveedor);

            ResultSet rs = pst.executeQuery();

            // Verificar si existe el proveedor
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al validar proveedor: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
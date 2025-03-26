package Conexion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConexionDB: Para hacer la conexión a la base de datos con MySQL
 *@author Jhoan david Sinisterra
 */
public class ConexionDB {
    /**
     * Retorno del valor de la variable con
     * @return Un objeto conexión de la base de datos si la conexión es exitosa, de lo contrario, devuelve null
     */
    public Connection getConnection(){
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_ferreteria", "root", "");
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error en la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }

    public static void main(String[] args) {
        ConexionDB conexionDB = new ConexionDB();
        conexionDB.getConnection();
    }
}

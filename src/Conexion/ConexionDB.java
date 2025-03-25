package Conexion;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
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

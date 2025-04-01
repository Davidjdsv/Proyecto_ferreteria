package RegistroVentas;

import Conexion.ConexionDB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroVentasDAO {
    private ConexionDB conexionDB = new ConexionDB();

public void agregar (RegistroVentas registroVentas){
    Connection con =  conexionDB.getConnection();
    String query = "INSERT IN TO registroVentas (id_orden_compra,id_producto,cantidad,sub_total) VALUES ?,?,?,?";

    try
    {
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1,registroVentas.getId_orden_compra());
        pst.setInt(2,registroVentas.getId_producto());
        pst.setInt(3,registroVentas.getCantidad());
        pst.setFloat(4,registroVentas.getSub_total());

        int resultado = pst.executeUpdate();
        if (resultado > 0) {
            JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente");
        } else {
            JOptionPane.showMessageDialog(null, "Cliente no agregado");
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

    }
}




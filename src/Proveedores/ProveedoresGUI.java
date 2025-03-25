package Proveedores;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProveedoresGUI {
    private JTable table1;
    private JTextField id_proveedorTextField;
    private JTextField contactoTextField;
    private JTextField nombreTextField;
    private JTextField categoria_productoTextField;
    private JButton agregarButton;
    private JButton EliminarButton;
    private JButton actualizarButton;
    private JPanel main;
    int filas = 0;

    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();

    //TODO: Botón agregar
    public ProveedoresGUI() {
        id_proveedorTextField.setEnabled(false);
        obtenerDatos();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoria_productoTextField.getText();

                Proveedores proveedores = new Proveedores(0, nombre, contacto, categoria_producto);
                proveedoresDAO.agregar(proveedores);
                obtenerDatos();
                limpiar();
            }
        });

        //TODO: Botón de actualizar
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoria_productoTextField.getText();
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());

                Proveedores proveedores = new Proveedores(id_proveedor, nombre, contacto, categoria_producto);
                proveedoresDAO.actualizar(proveedores);
                obtenerDatos();
                limpiar();
            }
        });


        EliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());

                proveedoresDAO.eliminar(id_proveedor);
                obtenerDatos();
                limpiar();
            }
        });


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = table1.getSelectedRow();

                if(selectFila >= 0){
                    id_proveedorTextField.setText((String) table1.getValueAt(selectFila, 0));
                    nombreTextField.setText((String) table1.getValueAt(selectFila, 1));
                    contactoTextField.setText((String) table1.getValueAt(selectFila, 2));
                    categoria_productoTextField.setText((String) table1.getValueAt(selectFila, 3));

                    filas = selectFila;
                }
            }
        });
    }

    public void limpiar(){
        id_proveedorTextField.setText("");
        nombreTextField.setText("");
        contactoTextField.setText("");
        categoria_productoTextField.setText("");
    }

    ConexionDB conexionDB = new ConexionDB();

    public void obtenerDatos(){
        DefaultTableModel dtm = new DefaultTableModel();

        dtm.addColumn("Id_proveedor");
        dtm.addColumn("Nombre");
        dtm.addColumn("Contacto");
        dtm.addColumn("Categoría_producto");

        table1.setModel(dtm);
        String[] dato = new String[4];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM proveedores";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                dato[0] = rs.getString(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getString(4);
                dtm.addRow(dato);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestión de proveedores");
        frame.setContentPane(new ProveedoresGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 700);
        frame.setResizable(false);
    }

}

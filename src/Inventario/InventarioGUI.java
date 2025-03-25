package Inventario;

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

public class InventarioGUI {
    private JPanel main;
    private JTable table1;
    private JTextField id;
    private JTextField nombre;
    private JTextField categoria;
    private JTextField precio;
    private JTextField cantidad_disponible;
    private JTextField id_proveedor;
    private JButton agregarButton;
    private JButton actualizarButton;
    private JButton eliminarButton;

    InventarioDAO inventarioDAO = new InventarioDAO();

    public InventarioGUI() {
        obtener_datos();
        id.setEnabled(false);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombre.getText();
                String categoriaProducto = categoria.getText();
                double precioProducto = Double.parseDouble(precio.getText());
                int cantidadDisponible = Integer.parseInt(cantidad_disponible.getText());
                int idProveedor = Integer.parseInt(id_proveedor.getText());

                Inventario inventario = new Inventario(0, nombreProducto, categoriaProducto, precioProducto, cantidadDisponible, idProveedor);
                inventarioDAO.agregar(inventario);
                obtener_datos();
                clear();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombre.getText();
                String categoriaProducto = categoria.getText();
                double precioProducto = Double.parseDouble(precio.getText());
                int cantidadDisponible = Integer.parseInt(cantidad_disponible.getText());
                int idProveedor = Integer.parseInt(id_proveedor.getText());
                int idProducto = Integer.parseInt(id.getText());

                Inventario inventario = new Inventario(idProducto, nombreProducto, categoriaProducto, precioProducto, cantidadDisponible, idProveedor);
                inventarioDAO.actualizar(inventario);
                obtener_datos();
                clear();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProducto = Integer.parseInt(id.getText());
                inventarioDAO.eliminar(idProducto);
                obtener_datos();
                clear();
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = table1.getSelectedRow();

                if (selectFila >= 0) {
                    id.setText(table1.getValueAt(selectFila, 0).toString());
                    nombre.setText(table1.getValueAt(selectFila, 1).toString());
                    categoria.setText(table1.getValueAt(selectFila, 2).toString());
                    precio.setText(table1.getValueAt(selectFila, 3).toString());
                    cantidad_disponible.setText(table1.getValueAt(selectFila, 4).toString());
                    id_proveedor.setText(table1.getValueAt(selectFila, 5).toString());
                }
            }
        });
    }

    public void clear() {
        id.setText("");
        nombre.setText("");
        categoria.setText("");
        precio.setText("");
        cantidad_disponible.setText("");
        id_proveedor.setText("");
    }

    ConexionDB conexionDB = new ConexionDB();

    public void obtener_datos() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("id");
        model.addColumn("nombre");
        model.addColumn("categoria");
        model.addColumn("precio");
        model.addColumn("cantidad_disponible");
        model.addColumn("id_proveedor");

        table1.setModel(model);
        Object[] dato = new Object[6];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM inventario";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dato[0] = rs.getInt(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getDouble(4);
                dato[4] = rs.getInt(5);
                dato[5] = rs.getInt(6);
                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestión de Inventario");
        frame.setContentPane(new InventarioGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
        frame.setResizable(false);
    }
}
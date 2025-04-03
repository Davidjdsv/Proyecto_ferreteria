package RegistroVentas;

import Clientes.Clientes;
import Conexion.ConexionDB;
import MenuPrincipal.MainMenu;

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

public class RegistroVentasGUI {
    private JPanel main;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton actualizarButton;
    private JButton volverAlMenuButton;

    RegistroVentasDAO RegistroventasDAO = new RegistroVentasDAO();
    ConexionDB ConexionDB = new ConexionDB();

    int filas = 0;

    public RegistroVentasGUI() {
        mostrar();
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_orden_compra = Integer.parseInt(textField2.getText());
                    int id_producto = Integer.parseInt(textField3.getText());
                    int cantidad = Integer.parseInt(textField4.getText());
                    float sub_total = Float.parseFloat(textField5.getText());

                    RegistroVentas Registroventas = new RegistroVentas(0, id_orden_compra, id_producto, cantidad, sub_total);
                    RegistroventasDAO.agregar(Registroventas);
                    mostrar();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_venta = Integer.parseInt(textField1.getText());
                    RegistroventasDAO.eliminar(id_venta);
                    mostrar();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_orden_compra = Integer.parseInt(textField2.getText());
                    int id_producto = Integer.parseInt(textField3.getText());
                    int cantidad = Integer.parseInt(textField4.getText());
                    float sub_total = Float.parseFloat(textField5.getText());
                    int id_venta = Integer.parseInt(textField1.getText());

                    RegistroVentas Registroventas = new RegistroVentas(id_venta, id_orden_compra, id_producto, cantidad, sub_total);
                    RegistroventasDAO.actualizar(Registroventas);
                    mostrar();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = table1.getSelectedRow();

                if (selectFila >= 0) {
                    textField1.setText(table1.getValueAt(selectFila, 0).toString());
                    textField2.setText(table1.getValueAt(selectFila, 1).toString());
                    textField3.setText(table1.getValueAt(selectFila, 2).toString());
                    textField4.setText(table1.getValueAt(selectFila, 3).toString());
                    textField5.setText(table1.getValueAt(selectFila, 4).toString());

                    filas = selectFila;
                }
            }
        });

        volverAlMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(volverAlMenuButton);
                if (jFrame != null) {
                    jFrame.dispose();
                }
                MainMenu mainMenu = new MainMenu();
                mainMenu.main(null);
            }
        });
    }

    public void mostrar() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id_venta");
        model.addColumn("id_orden_compra");
        model.addColumn("id_producto");
        model.addColumn("cantidad");
        model.addColumn("sub_total");

        table1.setModel(model);
        Connection con = ConexionDB.getConnection();

        try {
            Statement stat = con.createStatement();
            String query = "SELECT * FROM RegistroVentas";
            ResultSet fb = stat.executeQuery(query);

            while (fb.next()) {
                model.addRow(new Object[]{fb.getInt(1), fb.getInt(2), fb.getInt(3), fb.getInt(4), fb.getFloat(5)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RegistroVentas");
        frame.setContentPane(new RegistroVentasGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
}

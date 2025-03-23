package Empleados;

import Conexion.ConexionDB;
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

public class EmpleadosGUI {
    private JPanel main;
    private JTable table1;
    private JTextField ID;
    private JTextField Nombre;
    private JTextField Salario;
    private JButton agregarButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JComboBox comboBox1;
    int filas = 0;

    EmpleadosDAO empleadosDAO = new EmpleadosDAO();

    public EmpleadosGUI() {
        obtener_datos();
        ID.setEnabled(false);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = Nombre.getText();
                String cargo = (String) comboBox1.getSelectedItem();
                double salario = Double.parseDouble(Salario.getText());

                Empleados empleados = new Empleados(0, nombre, cargo, salario);
                empleadosDAO.agregar(empleados);
                obtener_datos();
                clear();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = Nombre.getText();
                String cargo = (String) comboBox1.getSelectedItem();
                double salario = Double.parseDouble(Salario.getText());
                int id = Integer.parseInt(ID.getText());

                Empleados empleados = new Empleados(id, nombre, cargo, salario);
                empleadosDAO.actualizar(empleados);
                obtener_datos();
                clear();
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(ID.getText());
                empleadosDAO.eliminar(id);
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
                    ID.setText(table1.getValueAt(selectFila, 0).toString());
                    Nombre.setText(table1.getValueAt(selectFila, 1).toString());
                    comboBox1.setSelectedItem(table1.getValueAt(selectFila, 2));
                    Salario.setText(table1.getValueAt(selectFila, 3).toString());

                    filas = selectFila;
                }
            }
        });
    }

    public void clear() {
        ID.setText("");
        Nombre.setText("");
        comboBox1.setSelectedItem("");
        Salario.setText("");
    }

    ConexionDB conexionDB = new ConexionDB();

    public void obtener_datos() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("id");
        model.addColumn("nombre");
        model.addColumn("cargo");
        model.addColumn("salario");

        table1.setModel(model);
        Object[] dato = new Object[4];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM empleados";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dato[0] = rs.getInt(1);
                dato[1] = rs.getString(2);
                dato[2] = rs.getString(3);
                dato[3] = rs.getDouble(4);
                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestión de Empleados");
        frame.setContentPane(new EmpleadosGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 700);
        frame.setResizable(false);
    }
}
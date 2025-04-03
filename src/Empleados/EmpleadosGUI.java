package Empleados;

import Conexion.ConexionDB;
import MenuPrincipal.MainMenu;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

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

/**
 * Interfaz gráfica de usuario para la gestión de empleados.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class EmpleadosGUI {
    /** Panel principal de la interfaz */
    private JPanel main;

    /** Tabla para mostrar los empleados */
    private JTable table1;

    /** Campo de texto para el ID del empleado */
    private JTextField ID;

    /** Campo de texto para el nombre del empleado */
    private JTextField Nombre;

    /** Campo de texto para el salario del empleado */
    private JTextField Salario;

    /** Botón para agregar un nuevo empleado */
    private JButton agregarButton;

    /** Botón para actualizar la información de un empleado */
    private JButton actualizarButton;

    /** Botón para eliminar un empleado */
    private JButton eliminarButton;

    /** Combo box para seleccionar el cargo del empleado */
    private JComboBox comboBox1;
    private JButton volverButton;

    /** Variable para rastrear la fila seleccionada */
    int filas = 0;

    /** Objeto para realizar operaciones de acceso a datos */
    EmpleadosDAO empleadosDAO = new EmpleadosDAO();

    /**
     * Constructor de la interfaz gráfica de empleados.
     * Inicializa los componentes y configura los listeners.
     */
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

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(volverButton);
                jFrame.dispose();
                MainMenu.main(null);
            }
        });
    }

    /**
     * Limpia los campos de entrada de la interfaz.
     */
    public void clear() {
        ID.setText("");
        Nombre.setText("");
        comboBox1.setSelectedItem("");
        Salario.setText("");
    }

    /** Conexión a la base de datos */
    ConexionDB conexionDB = new ConexionDB();

    /**
     * Obtiene y muestra los datos de empleados en la tabla.
     */
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

    /**
     * Método principal para iniciar la aplicación de gestión de empleados.
     *
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión de Empleados");
        frame.setContentPane(new EmpleadosGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
}
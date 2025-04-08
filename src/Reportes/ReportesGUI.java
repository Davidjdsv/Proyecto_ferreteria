package Reportes;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportesGUI extends JFrame {
    private JPanel main;
    private JComboBox<String> tipoReporteComboBox;
    private JTextField fechaTextField;
    private JComboBox<String> empleadoComboBox;
    private JTextArea descripcionTextArea;
    private JButton generarReporteButton;
    private JButton limpiarButton;
    private JTable reportesTable;
    private JButton regresarButton;
    private JButton salirButton;
    private JButton exportarPDFButton;
    private JSpinner parametroSpinner;
    private JLabel parametroLabel;

    private DefaultTableModel tableModel;
    private Connection conexion;
    private ReportesDAO reportesImpl;

    public JPanel getMainPanel() {
        return main;
    }

    // Constructor sin argumentos que obtiene la conexión internamente
    public ReportesGUI() {
        this(ConexionDB.getConnection());

        if (this.conexion == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer conexión con la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ReportesGUI(Connection conexion) {
        // Asignar correctamente el parámetro de conexión al campo de la clase
        this.conexion = conexion;

        setContentPane(main);
        setTitle("Sistema de Reportes - Ferretería");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configurar fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaTextField.setText(dateFormat.format(new Date()));

        // Inicializar componentes adicionales
        inicializarComponentes();

        // Cargar empleados desde la base de datos
        cargarEmpleados();

        // Configurar tabla de reportes
        configurarTabla();

        // Inicializar implementación de reportes
        reportesImpl = new ReportesDAO(conexion, reportesTable, tableModel);

        // Configurar botones
        configurarBotones();

        setVisible(true);
    }

    private void inicializarComponentes() {
        // Configurar tipos de reportes disponibles
        if (tipoReporteComboBox.getItemCount() == 0) {
            tipoReporteComboBox.addItem("Seleccione un tipo de reporte");
            tipoReporteComboBox.addItem("Ventas Diarias");
            tipoReporteComboBox.addItem("Ventas Semanales");
            tipoReporteComboBox.addItem("Ventas Mensuales");
            tipoReporteComboBox.addItem("Productos Más Vendidos");
            tipoReporteComboBox.addItem("Clientes con Más Compras");
            tipoReporteComboBox.addItem("Stock Bajo");
            tipoReporteComboBox.addItem("Reporte Personalizado");
        }

        // Configurar el spinner para parámetros
        parametroSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        parametroLabel = new JLabel("Límite de registros:");

        // Añadir estos componentes al panel, aunque no están definidos en el código original
        // Tendrías que añadirlos a tu diseño de GUI

        // Configurar listener para el cambio de tipo de reporte
        tipoReporteComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoSeleccionado = (String) tipoReporteComboBox.getSelectedItem();
                actualizarParametrosPorTipoReporte(tipoSeleccionado);
            }
        });
    }

    private void actualizarParametrosPorTipoReporte(String tipoReporte) {
        if (parametroLabel == null || parametroSpinner == null) {
            return; // Evitar NullPointerException si no están inicializados
        }

        if (tipoReporte == null || tipoReporte.equals("Seleccione un tipo de reporte")) {
            parametroLabel.setVisible(false);
            parametroSpinner.setVisible(false);
            return;
        }

        switch (tipoReporte) {
            case "Productos Más Vendidos":
            case "Clientes con Más Compras":
                parametroLabel.setText("Límite de registros:");
                parametroSpinner.setValue(5);
                parametroLabel.setVisible(true);
                parametroSpinner.setVisible(true);
                break;
            case "Stock Bajo":
                parametroLabel.setText("Umbral de stock:");
                parametroSpinner.setValue(10);
                parametroLabel.setVisible(true);
                parametroSpinner.setVisible(true);
                break;
            default:
                parametroLabel.setVisible(false);
                parametroSpinner.setVisible(false);
                break;
        }
    }

    public String nombreEmpleadoCapturado;

    private void cargarEmpleados() {
        try {
            // Comprobar si la conexión es nula antes de usarla
            if (conexion == null) {
                System.out.println("La conexión es nula en cargarEmpleados()");
                conexion = ConexionDB.getConnection();
                if (conexion == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo establecer conexión con la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            PreparedStatement stmt = conexion.prepareStatement("SELECT id_empleado, nombre FROM empleados ORDER BY nombre");
            ResultSet rs = stmt.executeQuery();

            empleadoComboBox.addItem("Seleccione un empleado");
            

            while (rs.next()) {
                empleadoComboBox.addItem(rs.getInt("id_empleado") + " - " + rs.getString("nombre"));
            }
            // capturar nombre seleccionar combox
            String nombreEmpleadoCapturado = empleadoComboBox.getSelectedItem().toString();

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarTabla() {
        tableModel = new DefaultTableModel();
        // Columnas iniciales, se actualizarán según el tipo de reporte
        tableModel.addColumn("ID");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Fecha");
        tableModel.addColumn("Empleado ID");
        tableModel.addColumn("Descripción");

        reportesTable.setModel(tableModel);
    }

    private void cargarReportes() {
        tableModel.setRowCount(0);

        try {
            // Comprobar si la conexión es nula antes de usarla
            if (conexion == null) {
                System.out.println("La conexión es nula en cargarReportes()");
                conexion = ConexionDB.getConnection();
                if (conexion == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo establecer conexión con la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            PreparedStatement stmt = conexion.prepareStatement(
                    "SELECT * FROM reportes_generados ORDER BY fecha_compra DESC");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getInt("id_reporte");
                row[1] = rs.getString("tipo_reporte");
                row[2] = rs.getString("fecha_compra");
                row[3] = rs.getInt("id_empleado");

                String descripcion = rs.getString("descripcion");
                if (descripcion != null && descripcion.length() > 30) {
                    descripcion = descripcion.substring(0, 30) + "...";
                }
                row[4] = descripcion;

                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reportes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBotones() {
        generarReporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();

                if (tipoReporte.equals("Seleccione un tipo de reporte")) {
                    JOptionPane.showMessageDialog(ReportesGUI.this,
                            "Por favor, seleccione un tipo de reporte",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                switch (tipoReporte) {
                    case "Ventas Diarias":
                        reportesImpl.generarReporteVentasPorPeriodo("diario");
                        break;
                    case "Ventas Semanales":
                        reportesImpl.generarReporteVentasPorPeriodo("semanal");
                        break;
                    case "Ventas Mensuales":
                        reportesImpl.generarReporteVentasPorPeriodo("mensual");
                        break;
                    case "Productos Más Vendidos":
                        int limite = (Integer) parametroSpinner.getValue();
                        reportesImpl.generarReporteProductosMasVendidos(limite);
                        break;
                    case "Clientes con Más Compras":
                        limite = (Integer) parametroSpinner.getValue();
                        reportesImpl.generarReporteClientesConMasCompras(limite);
                        break;
                    case "Stock Bajo":
                        int umbral = (Integer) parametroSpinner.getValue();
                        reportesImpl.generarReporteStockBajo(umbral);
                        break;
                    case "Reporte Personalizado":
                        generarReporte(); // Usa el método original de generación de reportes
                        break;
                    default:
                        JOptionPane.showMessageDialog(ReportesGUI.this,
                                "Tipo de reporte no implementado",
                                "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Registrar que se generó un reporte
                guardarRegistroReporte(tipoReporte);
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        exportarPDFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();
                if (tipoReporte.equals("Seleccione un tipo de reporte")) {
                    JOptionPane.showMessageDialog(ReportesGUI.this,
                            "Por favor, primero genere un reporte para exportar",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                reportesImpl.exportarReporteActualAPDF(tipoReporte);
            }
        });

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Aquí puedes abrir la ventana anterior si es necesario
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void guardarRegistroReporte(String tipoReporte) {
        try {
            if (conexion == null) {
                conexion = ConexionDB.getConnection();
                if (conexion == null) {
                    return;
                }
            }

            String empleadoSeleccionado = (String) empleadoComboBox.getSelectedItem();
            if (empleadoSeleccionado == null || empleadoSeleccionado.equals("Seleccione un empleado")) {
                empleadoSeleccionado = "1 - Sistema"; // Valor por defecto
            }


            int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);
            String descripcion = "Reporte generado: " + tipoReporte + " por el empleado: " + nombreEmpleadoCapturado;

            System.out.println("Empleado seleccionado: " + nombreEmpleadoCapturado);

            if (!descripcionTextArea.getText().trim().isEmpty()) {
                descripcion += ". Generado por  " + descripcionTextArea.getText();
            }

            PreparedStatement stmt = conexion.prepareStatement(
                    "INSERT INTO reportes_generados (tipo_reporte, id_empleado, descripcion) VALUES (?, ?, ?)");
            stmt.setString(1, tipoReporte);
            stmt.setInt(2, idEmpleado);
            stmt.setString(3, descripcion);

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al guardar registro de reporte: " + e.getMessage());
        }
    }

    private void generarReporte() {
        String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();
        String empleadoSeleccionado = (String) empleadoComboBox.getSelectedItem();
        String descripcion = descripcionTextArea.getText();

        if (empleadoSeleccionado == null || empleadoSeleccionado.equals("Seleccione un empleado")) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un empleado",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese una descripción",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extraer ID de empleado
        int idEmpleado = Integer.parseInt(empleadoSeleccionado.split(" - ")[0]);

        try {
            // Comprobar si la conexión es nula antes de usarla
            if (conexion == null) {
                System.out.println("La conexión es nula en generarReporte()");
                conexion = ConexionDB.getConnection();
                if (conexion == null) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo establecer conexión con la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            PreparedStatement stmt = conexion.prepareStatement(
                    "INSERT INTO reportes_generados (tipo_reporte, id_empleado, descripcion) VALUES (?, ?, ?)");
            stmt.setString(1, tipoReporte);
            stmt.setInt(2, idEmpleado);
            stmt.setString(3, descripcion);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Reporte generado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarReportes();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo generar el reporte",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        tipoReporteComboBox.setSelectedIndex(0);
        empleadoComboBox.setSelectedIndex(0);
        descripcionTextArea.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaTextField.setText(dateFormat.format(new Date()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Connection conexion = ConexionDB.getConnection();
            if (conexion != null) {
                new ReportesGUI(conexion);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
/**
 * Paquete que contiene las clases relacionadas con la generación y gestión de reportes.
 */
package Reportes;

import Conexion.ConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Interfaz gráfica para la gestión de reportes del sistema de ferretería.
 * Permite generar diferentes tipos de reportes, visualizarlos en una tabla
 * y exportarlos a formato PDF.
 *
 * Esta clase utiliza la biblioteca Swing para la interfaz gráfica y JDBC para
 * la conexión a la base de datos.
 *
 * @author Cristian Restrepo
 * @version 1.1
 */
public class ReportesGUI extends JFrame {
    /** Panel principal de la interfaz */
    private JPanel main;
    /** Selector de tipo de reporte */
    private JComboBox<String> tipoReporteComboBox;
    /** Campo de texto para mostrar la fecha actual */
    private JTextField fechaTextField;
    /** Selector de empleado que genera el reporte */
    private JComboBox<String> empleadoComboBox;
    /** Área de texto para la descripción del reporte */
    private JTextArea descripcionTextArea;
    /** Botón para generar el reporte seleccionado */
    private JButton generarReporteButton;
    /** Botón para limpiar el formulario */
    private JButton limpiarButton;
    /** Tabla para mostrar los reportes generados */
    private JTable reportesTable;
    /** Botón para volver a la pantalla anterior */
    private JButton regresarButton;
    /** Botón para salir de la aplicación */
    private JButton salirButton;
    /** Botón para exportar el reporte actual a PDF */
    private JButton exportarPDFButton;
    /** Selector numérico para parámetros específicos de reportes */
    private JSpinner parametroSpinner;
    /** Etiqueta descriptiva para el spinner de parámetros */
    private JLabel parametroLabel;
    private static ReportesGUI instancia;


    /** Modelo de tabla para los reportes */
    private DefaultTableModel tableModel;
    /** Conexión a la base de datos */
    private Connection conexion;
    /** Implementación de la lógica de reportes */
    private ReportesDAO reportesImpl;

    /** Mapa para almacenar ID y nombre completo de empleados */
    private Map<Integer, String> empleadosMap = new HashMap<>();
    /** Nombre del empleado actualmente seleccionado */
    private String nombreEmpleadoSeleccionado = "";

    /**
     * Obtiene el panel principal de la interfaz.
     * @return Panel principal de la interfaz gráfica
     */
    public JPanel getMainPanel() {
        return main;
    }

    // Modifica el método getInstancia
    public static ReportesGUI getInstancia() {
        if (instancia == null) {
            instancia = new ReportesGUI();
            instancia.setSize(800, 600);
            instancia.setLocationRelativeTo(null);
        }
        return instancia;
    }

    /**
     * Constructor sin argumentos que obtiene la conexión internamente.
     * Utiliza la clase ConexionDB para establecer la conexión con la base de datos.
     */
    public ReportesGUI() {
        this(ConexionDB.getConnection());

        if (this.conexion == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo establecer conexión con la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Constructor que recibe una conexión a la base de datos.
     * @param conexion Conexión a la base de datos
     */
    public ReportesGUI(Connection conexion) {
        // Configurar la ventana principal
        this.conexion = conexion;

         if (instancia != null && instancia != this) {
        instancia.dispose();
    }
        setContentPane(main);
        setTitle("Sistema de Reportes - Ferretería Future Soft");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configurar fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaTextField.setText(dateFormat.format(new Date()));
        fechaTextField.setEditable(false);

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

        // Configurar la detección del cambio de selección en el combobox de empleados
        empleadoComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarNombreEmpleadoSeleccionado();
                actualizarDescripcionAutomatica();
            }
        });

        setVisible(true);
    }

    /**
     * Inicializa los componentes de la interfaz gráfica si no están ya creados.
     */
    private void initComponents() {
        main = new JPanel(new BorderLayout());

        // Panel superior con configuraciones de reporte
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Etiqueta y combo para tipo de reporte
        JLabel tipoReporteLabel = new JLabel("Tipo de Reporte:");
        tipoReporteComboBox = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(tipoReporteLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        topPanel.add(tipoReporteComboBox, gbc);

        // Etiqueta y campo para fecha
        JLabel fechaLabel = new JLabel("Fecha:");
        fechaTextField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        topPanel.add(fechaLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        topPanel.add(fechaTextField, gbc);

        // Etiqueta y combo para empleado
        JLabel empleadoLabel = new JLabel("Empleado:");
        empleadoComboBox = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        topPanel.add(empleadoLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;

        // Etiqueta y spinner para parámetro
        parametroLabel = new JLabel("Límite/Umbral:");
        parametroSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        topPanel.add(parametroLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1;
        topPanel.add(parametroSpinner, gbc);

        // Etiqueta y área para descripción
        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionTextArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descripcionTextArea);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        topPanel.add(descripcionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        topPanel.add(scrollPane, gbc);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        generarReporteButton = new JButton("Generar Reporte");
        limpiarButton = new JButton("Limpiar");
        exportarPDFButton = new JButton("Exportar a PDF");

        buttonPanel.add(generarReporteButton);
        buttonPanel.add(limpiarButton);
        buttonPanel.add(exportarPDFButton);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        topPanel.add(buttonPanel, gbc);

        // Panel inferior con botones de navegación
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        regresarButton = new JButton("Regresar");
        salirButton = new JButton("Salir");

        bottomPanel.add(regresarButton);
        bottomPanel.add(salirButton);

        // Tabla de reportes
        reportesTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(reportesTable);

        // Agregar componentes al panel principal
        main.add(topPanel, BorderLayout.NORTH);
        main.add(tableScrollPane, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Inicializa y configura los componentes adicionales de la interfaz.
     */
    private void inicializarComponentes() {
        // Configurar opciones para el tipo de reporte
        tipoReporteComboBox.addItem("Seleccione un tipo de reporte");
        tipoReporteComboBox.addItem("Ventas Diarias");
        tipoReporteComboBox.addItem("Ventas Semanales");
        tipoReporteComboBox.addItem("Ventas Mensuales");
        tipoReporteComboBox.addItem("Productos Más Vendidos");
        tipoReporteComboBox.addItem("Clientes con Más Compras");
        tipoReporteComboBox.addItem("Productos con Stock Bajo");

        // Configurar JSpinner para los parámetros
        parametroSpinner.setValue(10);

        // Ocultar inicialmente el spinner y su etiqueta
        parametroLabel.setVisible(false);
        parametroSpinner.setVisible(false);

        // Agregar listener para el combo de tipo de reporte
        tipoReporteComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();

                // Configurar visibilidad y etiqueta del spinner según el tipo de reporte
                switch (tipoReporte) {
                    case "Productos Más Vendidos":
                        parametroLabel.setText("Límite de productos:");
                        parametroLabel.setVisible(true);
                        parametroSpinner.setVisible(true);
                        parametroSpinner.setValue(10);
                        break;
                    case "Clientes con Más Compras":
                        parametroLabel.setText("Límite de clientes:");
                        parametroLabel.setVisible(true);
                        parametroSpinner.setVisible(true);
                        parametroSpinner.setValue(10);
                        break;
                    case "Productos con Stock Bajo":
                        parametroLabel.setText("Umbral de stock:");
                        parametroLabel.setVisible(true);
                        parametroSpinner.setVisible(true);
                        parametroSpinner.setValue(5);
                        break;
                    default:
                        parametroLabel.setVisible(false);
                        parametroSpinner.setVisible(false);
                        break;
                }

                // Actualizar la descripción automática
                actualizarDescripcionAutomatica();
            }
        });
    }

    /**
     * Carga la lista de empleados desde la base de datos.
     */
    private void cargarEmpleados() {
        empleadosMap.clear();
        empleadoComboBox.removeAllItems();
        empleadoComboBox.addItem("Seleccione un empleado");

        try {
            String sql = "SELECT * FROM empleados ORDER BY nombre";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idEmpleado = rs.getInt("id_empleado");
                String nombreCompleto = rs.getString("nombre");
                empleadosMap.put(idEmpleado, nombreCompleto);
                empleadoComboBox.addItem(idEmpleado + " - " + nombreCompleto);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar empleados: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura el modelo de la tabla para mostrar reportes.
     */
    private void configurarTabla() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        reportesTable.setModel(tableModel);
        reportesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        reportesTable.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Actualiza la variable que almacena el nombre del empleado seleccionado.
     */
    private void actualizarNombreEmpleadoSeleccionado() {
        String seleccion = (String) empleadoComboBox.getSelectedItem();
        if (seleccion != null && !seleccion.equals("Seleccione un empleado")) {
            String[] partes = seleccion.split(" - ");
            int idEmpleado = Integer.parseInt(partes[0]);
            nombreEmpleadoSeleccionado = empleadosMap.get(idEmpleado);
        } else {
            nombreEmpleadoSeleccionado = "";
        }
    }

    /**
     * Actualiza automáticamente la descripción del reporte según el tipo seleccionado.
     */
    private void actualizarDescripcionAutomatica() {
        String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();
        if (tipoReporte == null || tipoReporte.equals("Seleccione un tipo de reporte")) {
            descripcionTextArea.setText("");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(new Date());

        StringBuilder descripcion = new StringBuilder();
        descripcion.append("Reporte de ").append(tipoReporte)
                .append(" generado el ").append(fecha);

        if (!nombreEmpleadoSeleccionado.isEmpty()) {
            descripcion.append(" por ").append(nombreEmpleadoSeleccionado);
        }

        switch (tipoReporte) {
            case "Ventas Diarias":
                descripcion.append("\nDetalle de ventas diarias de los últimos 30 días.");
                break;
            case "Ventas Semanales":
                descripcion.append("\nDetalle de ventas semanales de las últimas 12 semanas.");
                break;
            case "Ventas Mensuales":
                descripcion.append("\nDetalle de ventas mensuales de los últimos 12 meses.");
                break;
            case "Productos Más Vendidos":
                int limite = (Integer) parametroSpinner.getValue();
                descripcion.append("\nLos ").append(limite).append(" productos más vendidos.");
                break;
            case "Clientes con Más Compras":
                limite = (Integer) parametroSpinner.getValue();
                descripcion.append("\nLos ").append(limite).append(" clientes con mayor cantidad de compras.");
                break;
            case "Productos con Stock Bajo":
                int umbral = (Integer) parametroSpinner.getValue();
                descripcion.append("\nProductos con stock menor o igual a ").append(umbral).append(" unidades.");
                break;
        }

        descripcionTextArea.setText(descripcion.toString());
    }

    /**
     * Configura los listeners para los botones de la interfaz.
     */
    private void configurarBotones() {
        // Botón para generar reporte
        generarReporteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        // Botón para limpiar formulario
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        // Botón para exportar a PDF
        exportarPDFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarAPDF();
            }
        });

        // Botón para regresar
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cerrar esta ventana
                // Aquí podría ir código para abrir la ventana anterior si es necesario
            }
        });

        // Botón para salir
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "¿Está seguro que desea salir del sistema?",
                        "Confirmar salida", JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Genera el reporte según el tipo seleccionado.
     */
    private void generarReporte() {
        String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();

        if (tipoReporte == null || tipoReporte.equals("Seleccione un tipo de reporte")) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un tipo de reporte.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
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
                case "Productos con Stock Bajo":
                    int umbral = (Integer) parametroSpinner.getValue();
                    reportesImpl.generarReporteStockBajo(umbral);
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                            "Tipo de reporte no implementado: " + tipoReporte,
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // Mostrar mensaje de éxito si hay datos
            if (reportesTable.getRowCount() > 0) {
                JOptionPane.showMessageDialog(this,
                        "Reporte generado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron datos para el reporte solicitado.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar reporte: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Limpia el formulario y restablece los valores por defecto.
     */
    private void limpiarFormulario() {
        tipoReporteComboBox.setSelectedIndex(0);
        empleadoComboBox.setSelectedIndex(0);
        descripcionTextArea.setText("");
        parametroSpinner.setValue(10);
        parametroLabel.setVisible(false);
        parametroSpinner.setVisible(false);

        // Limpiar tabla
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Actualizar fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaTextField.setText(dateFormat.format(new Date()));
    }

    /**
     * Exporta el reporte actual a formato PDF.
     */
    private void exportarAPDF() {
        if (reportesTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay datos para exportar. Genere un reporte primero.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoReporte = (String) tipoReporteComboBox.getSelectedItem();
        reportesImpl.exportarReporteActualAPDF(tipoReporte, nombreEmpleadoSeleccionado);
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ReportesGUI.getInstancia().setVisible(true);
        });
    }
}

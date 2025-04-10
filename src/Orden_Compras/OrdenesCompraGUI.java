package Orden_Compras;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.File;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;

/**
 * Clase OrdenesCompraGUI que representa la interfaz gráfica para gestionar órdenes de compra.
 * Esta clase permite visualizar, filtrar y gestionar órdenes de compra relacionadas con clientes y empleados.
 * Incluye funcionalidades para cambiar el estado de las órdenes y generar facturas en PDF.
 * @author Cristian Restrepo
 * @version 1.4
 */
public class OrdenesCompraGUI {
    /** Tabla para mostrar los registros de órdenes de compra */
    private JTable table1;
    /** Campo para mostrar el ID de la orden de compra */
    private JTextField idOrdenCompra;
    /** Campo para ingresar la cantidad de productos */
    private JTextField cantidadTextField;
    /** Campo para mostrar el total de la compra */
    private JTextField total;
    /** Campo para mostrar la fecha de compra */
    private JTextField fechaCompra;
    /** Panel principal de la interfaz */
    private JPanel mainPanel;
    /** Campo de texto para cliente */
    private JTextField ClienteTextField;
    /** Campo de texto para empleado */
    private JTextField EmpleadoTextField;
    /** Campo de texto para producto */
    private JTextField ProductoTextField;
    /** ComboBox para seleccionar el estado de la orden */
    /** ComboBox para filtrar por tipo (todos, por empleado, por cliente) */
    private JComboBox<String> configurarFiltroComboBox;
    /** Botón para actualizar el estado de una orden */
    private JButton actualizarEstadoButton;
    /** Botón para generar una factura PDF */
    private JButton generarFacturaButton;
    /** Panel para los botones de acción */
    private JPanel buttonPanel;
    /** Panel para los campos de filtrado */
    private JPanel filtroPanel;
    /** Campo de texto para el filtro de búsqueda */
    private JTextField filtroTextField;
    /** Botón para buscar según filtro */
    private JButton buscarButton;
    /** Botón para limpiar filtros */
    private JButton limpiarFiltrosButton;
    private JTextField estadoTextField;

    /**
     * Mapas para almacenar los IDs de clientes, empleados y productos.
     * Estos mapas permiten relacionar los elementos mostrados en los campos con sus IDs en la base de datos.
     */
    private Map<String, Integer> clientesMap = new HashMap<>();
    private Map<String, Integer> empleadosMap = new HashMap<>();
    private Map<String, Integer> productosMap = new HashMap<>();
    /** Mapa para almacenar los precios de los productos según su ID */
    private Map<Integer, Double> preciosProductos = new HashMap<>();

    /** Conexión a la base de datos */
    private Connection conn;
    /** Modelo de tabla para mostrar los datos */
    private DefaultTableModel model;

    /**
     * Obtiene el panel principal de la interfaz.
     * @return JPanel principal que contiene todos los componentes de la interfaz
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Constructor de la clase OrdenesCompraGUI.
     * Inicializa la interfaz y configura todos los componentes y listeners.
     */
    public OrdenesCompraGUI() {
        // Configurar la tabla
        configurarTabla();

        // Configurar el ComboBox de filtro
        configurarFiltroComboBox();
        
     

        // Configurar botones de acción
        configurarBotones();

        // Conexión a la base de datos
        establecerConexion();

        // Cargar datos iniciales
        cargarClientes();
        cargarEmpleados();
        cargarProductos();
        cargarOrdenes();

        // Configurar fecha actual como no editable
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaCompra.setText(dateFormat.format(new Date()));
        fechaCompra.setEditable(false);

        // Listener para cargar datos en los campos cuando se selecciona una fila
        configurarListenerTabla();

        // Listener para el ComboBox de filtro
        configurarListenerFiltro();

        // Listener para el campo de ID de orden
        configurarListenerIdOrden();

        // Limpiar campos inicialmente
        limpiarCampos();
    }

    private void configurarListenerFiltro() {
        configurarFiltroComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filtroSeleccionado = (String) configurarFiltroComboBox.getSelectedItem();
                if (filtroSeleccionado != null && !filtroSeleccionado.equals("Todas las órdenes")) {
                    filtroTextField.setEnabled(true);
                } else {
                    filtroTextField.setEnabled(false);
                    filtroTextField.setText("");
                }
            }
        });
    }

    private void configurarListenerTabla() {
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() != -1) {
                cargarDatosEnCampos(table1.getSelectedRow());
            }
        });
    }

    private void cargarEmpleados() {
        try {
            String sql = "SELECT id_empleado, nombre FROM empleados";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idEmpleado = rs.getInt("id_empleado");
                String nombreEmpleado = rs.getString("nombre");

                empleadosMap.put(nombreEmpleado, idEmpleado);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar empleados: " + e.getMessage());
        }
    }

    private void establecerConexion() {
        try {
            String url = "jdbc:mysql://localhost:3306/proyecto_ferreteria";
            String user = "root";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private void cargarClientes() {
        try {
            String sql = "SELECT id_cliente, nombre FROM clientes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idCliente = rs.getInt("id_cliente");
                String nombreCliente = rs.getString("nombre");

                clientesMap.put(nombreCliente, idCliente);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void configurarFiltroComboBox() {
        configurarFiltroComboBox.addItem("Todas las órdenes");
        configurarFiltroComboBox.addItem("Filtrar por cliente");
        configurarFiltroComboBox.addItem("Filtrar por empleado");
        configurarFiltroComboBox.addItem("Filtrar por producto");
    }
    private void configurarTabla() {
        model = new DefaultTableModel(new String[]{
                "ID Orden", "Cliente", "Empleado", "Producto", "Cantidad", "Total", "Estado", "Fecha"
        }, 0);
        table1.setModel(model);
    }
    

    private void cargarProductos() {
        try {
            String sql = "SELECT id_producto, nombre_producto, precio_producto FROM inventario_productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                String nombreProducto = rs.getString("nombre_producto");
                double precioProducto = rs.getDouble("precio_producto");

                productosMap.put(nombreProducto, idProducto);
                preciosProductos.put(idProducto, precioProducto);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }
    /**
     * Configura el listener para el campo de ID de orden.
     * Cuando el usuario ingresa un ID y presiona Enter, busca la orden automáticamente.
     */
    private void configurarListenerIdOrden() {
        idOrdenCompra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarOrdenPorId();
                }
            }
        });
    }

    /**
     * Busca una orden por su ID y carga los datos en los campos correspondientes.
     */
    private void buscarOrdenPorId() {
        String idOrdenTexto = idOrdenCompra.getText().trim();
        if (idOrdenTexto.isEmpty()) {
            return;
        }

        try {
            int idOrden = Integer.parseInt(idOrdenTexto);

            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE oc.id_orden_compra = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idOrden);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Cargar datos en los campos
                ClienteTextField.setText(rs.getString("cliente"));
                EmpleadoTextField.setText(rs.getString("empleado"));
                ProductoTextField.setText(rs.getString("nombre_producto"));

                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1;
                cantidadTextField.setText(String.valueOf(cantidad));

                total.setText(String.valueOf(rs.getDouble("total")));

                String estado = rs.getString("estado_orden");
                estadoTextField.setText(estado);


                fechaCompra.setText(rs.getString("fecha_compra"));

                // Resaltar la fila correspondiente en la tabla
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(model.getValueAt(i, 0).toString()) == idOrden) {
                        table1.setRowSelectionInterval(i, i);
                        table1.scrollRectToVisible(table1.getCellRect(i, 0, true));
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "No se encontró una orden con el ID: " + idOrden,
                        "Orden no encontrada", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }

            rs.close();
            pstmt.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese un número válido para el ID.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al buscar la orden: " + e.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura los botones de acción en la interfaz.
     * Crea los botones para actualizar estado y generar factura, y configura sus listeners.
     */
    private void configurarBotones() {
        actualizarEstadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoActualizarEstado();
            }
        });

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idOrdenCompra.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione una orden para generar factura");
                    return;
                }
                generarFacturaPDF();
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltro();
            }
        });

        limpiarFiltrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configurarFiltroComboBox.setSelectedIndex(0);
                filtroTextField.setText("");
                cargarOrdenes();
                limpiarCampos();
            }
        });
    }

    private void cargarOrdenes() {
        model.setRowCount(0); // Limpiar la tabla

        try {
            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "ORDER BY oc.id_orden_compra DESC";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int idOrden = rs.getInt("id_orden_compra");
                String cliente = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                String producto = rs.getString("nombre_producto");
                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1; // Si no hay registro en ventas
                double totalOrden = rs.getDouble("total");
                String estado = rs.getString("estado_orden");
                String fecha = rs.getString("fecha_compra");

                model.addRow(new Object[]{idOrden, cliente, empleado, producto, cantidad, totalOrden, estado, fecha});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar órdenes: " + e.getMessage());
        }
    }

    /**
     * Aplica el filtro seleccionado al listado de órdenes.
     */
    private void aplicarFiltro() {
        String textoBusqueda = filtroTextField.getText().trim();
        int opcionFiltro = configurarFiltroComboBox.getSelectedIndex();

        if (textoBusqueda.isEmpty() && opcionFiltro != 0) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un texto para filtrar");
            return;
        }

        switch (opcionFiltro) {
            case 0: // Todas las órdenes
                cargarOrdenes();
                break;
            case 1: // Por cliente
                filtrarPorCliente(textoBusqueda);
                break;
            case 2: // Por empleado
                filtrarPorEmpleado(textoBusqueda);
                break;
            case 3: // Por producto
                filtrarPorProducto(textoBusqueda);
                break;
        }
    }

    /**
     * Filtra las órdenes por cliente.
     * @param cliente Nombre del cliente para filtrar
     */
    private void filtrarPorCliente(String cliente) {
        model.setRowCount(0); // Limpiar tabla

        try {
            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE c.nombre LIKE ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + cliente + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int idOrden = rs.getInt("id_orden_compra");
                String clienteNombre = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                String producto = rs.getString("nombre_producto");
                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1; // Si no hay registro en ventas
                double totalOrden = rs.getDouble("total");
                String estado = rs.getString("estado_orden");
                String fecha = rs.getString("fecha_compra");

                model.addRow(new Object[]{
                        idOrden, clienteNombre, empleado, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar por cliente: " + e.getMessage());
        }
    }

    /**
     * Filtra las órdenes por empleado.
     * @param empleado Nombre del empleado para filtrar
     */
    // Completar el método filtrarPorEmpleado
    private void filtrarPorEmpleado(String empleado) {
        model.setRowCount(0); // Limpiar tabla

        try {
            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE e.nombre LIKE ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + empleado + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int idOrden = rs.getInt("id_orden_compra");
                String clienteNombre = rs.getString("cliente");
                String empleadoNombre = rs.getString("empleado");
                String producto = rs.getString("nombre_producto");
                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1; // Si no hay registro en ventas
                double totalOrden = rs.getDouble("total");
                String estado = rs.getString("estado_orden");
                String fecha = rs.getString("fecha_compra");

                model.addRow(new Object[]{
                        idOrden, clienteNombre, empleadoNombre, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar por empleado: " + e.getMessage());
        }
    }

    /**
     * Filtra las órdenes por producto.
     * @param producto Nombre del producto para filtrar
     */
    private void filtrarPorProducto(String producto) {
        model.setRowCount(0); // Limpiar tabla

        try {
            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE p.nombre_producto LIKE ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + producto + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int idOrden = rs.getInt("id_orden_compra");
                String clienteNombre = rs.getString("cliente");
                String empleado = rs.getString("empleado");
                String productoNombre = rs.getString("nombre_producto");
                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1; // Si no hay registro en ventas
                double totalOrden = rs.getDouble("total");
                String estado = rs.getString("estado_orden");
                String fecha = rs.getString("fecha_compra");

                model.addRow(new Object[]{
                        idOrden, clienteNombre, empleado, productoNombre, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar por producto: " + e.getMessage());
        }
    }

    /**
     * Carga los datos de una orden seleccionada en los campos de la interfaz.
     * @param row Índice de la fila seleccionada en la tabla
     */
    private void cargarDatosEnCampos(int row) {
        idOrdenCompra.setText(table1.getValueAt(row, 0).toString());
        ClienteTextField.setText(table1.getValueAt(row, 1).toString());
        EmpleadoTextField.setText(table1.getValueAt(row, 2).toString());
        ProductoTextField.setText(table1.getValueAt(row, 3).toString());
        cantidadTextField.setText(table1.getValueAt(row, 4).toString());
        total.setText(table1.getValueAt(row, 5).toString());

        // Asignar el estado directamente al estadoTextField
        estadoTextField.setText(table1.getValueAt(row, 6).toString());

        fechaCompra.setText(table1.getValueAt(row, 7).toString());
    }

    /**
     * Limpia todos los campos de la interfaz y restablece los valores por defecto.
     */
    private void limpiarCampos() {
        idOrdenCompra.setText("");
        ClienteTextField.setText("");
        EmpleadoTextField.setText("");
        ProductoTextField.setText("");
        cantidadTextField.setText("");
        total.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaCompra.setText(dateFormat.format(new Date()));
        estadoTextField.getSelectedText();
        table1.clearSelection();
    }

    /**
     * Actualiza el estado de una orden en la base de datos.
     * @param idOrden ID de la orden a actualizar
     * @param nuevoEstado Nuevo estado para la orden
     */
    private void actualizarEstadoOrden(int idOrden, String nuevoEstado) {
        try {
            String sql = "UPDATE ordenes_compra SET estado_orden = ? WHERE id_orden_compra = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idOrden);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // La actualización fue exitosa
                cargarOrdenes(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el estado de la orden.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar estado: " + e.getMessage());
        }
    }

    /**
     * Crea y muestra un diálogo modal para actualizar el estado de una orden.
     */
    /**
     * Muestra un diálogo modal para actualizar el estado de una orden de compra seleccionada.
     * Verifica si hay una orden seleccionada, muestra el estado actual y permite elegir un nuevo estado
     * desde un JComboBox. Al confirmar, se actualiza el estado de la orden en la base de datos.
     */
    private void mostrarDialogoActualizarEstado() {
        // Verifica que haya una orden seleccionada
        if (idOrdenCompra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una orden para actualizar su estado");
            return;
        }

        // Crear el diálogo modal
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(mainPanel), "Actualizar Estado de Orden", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(mainPanel);

        JPanel contentPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Agregar campo de ID de orden
        contentPanel.add(new JLabel("Orden ID:"));
        JTextField idOrdenField = new JTextField(idOrdenCompra.getText());
        idOrdenField.setEditable(false);
        contentPanel.add(idOrdenField);

        // Agregar campo de estado actual (desde estadoTextField)
        contentPanel.add(new JLabel("Estado Actual:"));
        JTextField estadoActualField = new JTextField(estadoTextField.getText()); // ← se usa .getText() correctamente
        estadoActualField.setEditable(false);
        contentPanel.add(estadoActualField);

        // Agregar combo para nuevo estado
        contentPanel.add(new JLabel("Nuevo Estado:"));
        JComboBox<String> nuevoEstadoComboBox = new JComboBox<>();
        nuevoEstadoComboBox.addItem("pendiente");
        nuevoEstadoComboBox.addItem("pagada");
        nuevoEstadoComboBox.addItem("enviada");

        // Selecciona el estado actual por defecto
        nuevoEstadoComboBox.setSelectedItem(estadoTextField.getText());
        contentPanel.add(nuevoEstadoComboBox);

        dialog.add(contentPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");

        // Listener para el botón Guardar
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoEstado = nuevoEstadoComboBox.getSelectedItem().toString();
                int idOrden = Integer.parseInt(idOrdenField.getText());

                actualizarEstadoOrden(idOrden, nuevoEstado);
                dialog.dispose();
            }
        });

        // Listener para el botón Cancelar
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }


    /**
     * Genera una factura en formato PDF para la orden seleccionada.
     * Incluye detalles del cliente, producto, empleado y valores de la compra.
     */
    private void generarFacturaPDF() {
        try {
            // Obtener la ruta para guardar el PDF
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Factura PDF");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int userSelection = fileChooser.showSaveDialog(mainPanel);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                return; // El usuario canceló la operación
            }

            // Obtener la ruta seleccionada
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            int idOrden = Integer.parseInt(idOrdenCompra.getText());
            String fileName = path + File.separator + "Factura_" + idOrden + ".pdf";

            // Crear el documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Obtener datos detallados de la orden desde la base de datos
            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, c.direccion, c.telefono, " +
                    "e.nombre as empleado, p.nombre_producto, p.precio_producto, rv.cantidad, " +
                    "oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE oc.id_orden_compra = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idOrden);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Datos para la factura
                String cliente = rs.getString("cliente");
                String direccion = rs.getString("direccion");
                String telefono = rs.getString("telefono");
                String empleado = rs.getString("empleado");
                String producto = rs.getString("nombre_producto");
                double precioUnitario = rs.getDouble("precio_producto");
                int cantidad = rs.getInt("cantidad");
                if (cantidad == 0) cantidad = 1; // Si no hay registro en ventas
                double totalOrden = rs.getDouble("total");
                String estado = rs.getString("estado_orden");
                String fecha = rs.getString("fecha_compra");

                // Encabezado de la factura
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Font smallFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

                // Título
                Paragraph title = new Paragraph("FACTURA DE COMPRA", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" "));

                // Información de la empresa
                Paragraph companyInfo = new Paragraph("FERRETERÍA FUTURE SOFT", subtitleFont);
                companyInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(companyInfo);

                Paragraph companyAddress = new Paragraph("Dirección: Sede Sagrado ", normalFont);
                companyAddress.setAlignment(Element.ALIGN_CENTER);
                document.add(companyAddress);

                Paragraph companyPhone = new Paragraph("Teléfono: 3172847523", normalFont);
                companyPhone.setAlignment(Element.ALIGN_CENTER);
                document.add(companyPhone);

                document.add(new Paragraph(" "));
                document.add(new Paragraph("FACTURA N°: " + idOrden, subtitleFont));
                document.add(new Paragraph("Fecha: " + fecha, normalFont));
                document.add(new Paragraph("Estado: " + estado, normalFont));
                document.add(new Paragraph(" "));

                // Información del cliente
                document.add(new Paragraph("DATOS DEL CLIENTE", subtitleFont));
                document.add(new Paragraph("Nombre: " + cliente, normalFont));
                document.add(new Paragraph("Dirección: " + direccion, normalFont));
                document.add(new Paragraph("Teléfono: " + telefono, normalFont));
                document.add(new Paragraph(" "));

                // Información del vendedor
                document.add(new Paragraph("ATENDIDO POR", subtitleFont));
                document.add(new Paragraph("Empleado: " + empleado, normalFont));
                document.add(new Paragraph(" "));

                // Detalles de la compra
                document.add(new Paragraph("DETALLES DE LA COMPRA", subtitleFont));

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Encabezados de la tabla
                PdfPCell cell1 = new PdfPCell(new Paragraph("Producto", normalFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph("Precio Unitario", normalFont));
                PdfPCell cell3 = new PdfPCell(new Paragraph("Cantidad", normalFont));
                PdfPCell cell4 = new PdfPCell(new Paragraph("Subtotal", normalFont));

                cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);

                // Fila con datos del producto
                table.addCell(producto);
                table.addCell(String.format("$%.2f", precioUnitario));
                table.addCell(String.valueOf(cantidad));
                table.addCell(String.format("$%.2f", precioUnitario * cantidad));

                document.add(table);

                // Total
                PdfPTable totalTable = new PdfPTable(2);
                totalTable.setWidthPercentage(50);
                totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

                totalTable.addCell(new PdfPCell(new Paragraph("TOTAL:", subtitleFont)));
                totalTable.addCell(new PdfPCell(new Paragraph(String.format("$%.2f", totalOrden), subtitleFont)));

                document.add(totalTable);

                // Pie de página
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                Paragraph thanks = new Paragraph("¡Gracias por su compra en future soft !", normalFont);
                thanks.setAlignment(Element.ALIGN_CENTER);
                document.add(thanks);

                Paragraph footer = new Paragraph("Esta factura sirve como comprobante de pago", smallFont);
                footer.setAlignment(Element.ALIGN_CENTER);
                document.add(footer);

                JOptionPane.showMessageDialog(null, "Factura generada exitosamente en: " + fileName);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontraron datos para la orden seleccionada.");
            }

            document.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar la factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * Crea una instancia de la clase y muestra la ventana principal.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Establecer look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Gestión de Órdenes de Compra");
                frame.setContentPane(new OrdenesCompraGUI().getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 700);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}


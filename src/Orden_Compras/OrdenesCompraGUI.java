package Orden_Compras;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase OrdenesCompraGUI que representa la interfaz gráfica para gestionar órdenes de compra.
 * Esta clase permite visualizar, filtrar y gestionar órdenes de compra relacionadas con clientes y empleados.
 * @author Cristian Restrepo
 * @version 1.1
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
    /** ComboBox para seleccionar clientes */
    private JComboBox<String> clienteComboBox;
    /** ComboBox para seleccionar empleados */
    private JComboBox<String> empleadoComboBox;
    /** ComboBox para seleccionar productos */
    private JComboBox<String> productoComboBox;
    /** ComboBox para seleccionar el estado de la orden */
    private JComboBox<String> estadoComboBox;
    /** ComboBox para filtrar por tipo (todos, por empleado, por cliente) */
    private JComboBox<String> configurarFiltroComboBox;

    /**
     * Mapas para almacenar los IDs de clientes, empleados y productos.
     * Estos mapas permiten relacionar los elementos mostrados en los ComboBox con sus IDs en la base de datos.
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

        // Listener para el ComboBox de cliente
        clienteComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && configurarFiltroComboBox.getSelectedIndex() == 2) {
                    cargarOrdenesPorCliente();
                }
            }
        });

        // Listener para el ComboBox de empleado
        empleadoComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && configurarFiltroComboBox.getSelectedIndex() == 1) {
                    cargarOrdenesPorEmpleado();
                }
            }
        });

        // Listener para cargar datos en los campos cuando se selecciona una fila
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    cargarDatosEnCampos(selectedRow);
                }
            }
        });

        // Listener para el ComboBox de filtro
        configurarFiltroComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (configurarFiltroComboBox.getSelectedIndex()) {
                        case 0: // Todas las órdenes
                            cargarOrdenes();
                            break;
                        case 1: // Por empleado
                            if (empleadoComboBox.getSelectedIndex() != -1) {
                                cargarOrdenesPorEmpleado();
                            }
                            break;
                        case 2: // Por cliente
                            if (clienteComboBox.getSelectedIndex() != -1) {
                                cargarOrdenesPorCliente();
                            }
                            break;
                        case 3: // Por producto
                            if (productoComboBox.getSelectedIndex() != -1) {
                                cargarOrdenesPorProducto();
                            }
                            break;
                    }
                }
            }
        });
    }

    /**
     * Configura el ComboBox de filtro con las opciones disponibles.
     */

    private void configurarFiltroComboBox() {
        // El componente ya está definido, solo añadir elementos
        configurarFiltroComboBox.addItem("Todas las órdenes");
        configurarFiltroComboBox.addItem("Filtrar por empleado");
        configurarFiltroComboBox.addItem("Filtrar por cliente");
        configurarFiltroComboBox.addItem("Filtrar por producto");
    }

    /**
     * Configura la tabla que muestra las órdenes de compra.
     * Define las columnas y establece el modelo de datos.
     */
    private void configurarTabla() {
        model = new DefaultTableModel();
        model.addColumn("ID Orden");
        model.addColumn("Cliente");
        model.addColumn("Empleado");
        model.addColumn("Producto");
        model.addColumn("Cantidad");
        model.addColumn("Total");
        model.addColumn("Estado");
        model.addColumn("Fecha");
        table1.setModel(model);
    }

    /**
     * Establece la conexión con la base de datos.
     * Utiliza los parámetros de conexión predefinidos.
     */
    private void establecerConexion() {
        try {
            String url = "jdbc:mysql://localhost:3306/proyecto_ferreteria";
            String user = "root";
            String password = "";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de clientes desde la base de datos.
     * Actualiza el ComboBox de clientes y el mapa de IDs.
     */
    private void cargarClientes() {
        clienteComboBox.removeAllItems();
        clientesMap.clear();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_cliente, nombre FROM clientes");

            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String item = id + " - " + nombre;
                clienteComboBox.addItem(item);
                clientesMap.put(item, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar clientes: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de empleados desde la base de datos.
     * Actualiza el ComboBox de empleados y el mapa de IDs.
     */
    private void cargarEmpleados() {
        empleadoComboBox.removeAllItems();
        empleadosMap.clear();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_empleado, nombre FROM empleados");

            while (rs.next()) {
                int id = rs.getInt("id_empleado");
                String nombre = rs.getString("nombre");
                String item = id + " - " + nombre;
                empleadoComboBox.addItem(item);
                empleadosMap.put(item, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar empleados: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de productos desde la base de datos.
     * Actualiza el ComboBox de productos y los mapas de IDs y precios.
     * Solo muestra productos con stock disponible.
     */
    private void cargarProductos() {
        productoComboBox.removeAllItems();
        productosMap.clear();
        preciosProductos.clear();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_producto, nombre_producto, precio_producto FROM inventario_productos WHERE cantidad_stock > 0");

            while (rs.next()) {
                int id = rs.getInt("id_producto");
                String nombre = rs.getString("nombre_producto");
                double precio = rs.getDouble("precio_producto");
                String item = id + " - " + nombre + " ($" + precio + ")";
                productoComboBox.addItem(item);
                productosMap.put(item, id);
                preciosProductos.put(id, precio);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }

    /**
     * Carga todas las órdenes de compra en la tabla.
     */
    private void cargarOrdenes() {
        model.setRowCount(0); // Limpiar tabla

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

                model.addRow(new Object[]{
                        idOrden, cliente, empleado, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar órdenes: " + e.getMessage());
        }
    }

    /**
     * Carga las órdenes de compra filtradas por empleado seleccionado.
     */
    private void cargarOrdenesPorEmpleado() {
        model.setRowCount(0); // Limpiar tabla

        try {
            String selectedEmpleado = empleadoComboBox.getSelectedItem().toString();
            int idEmpleado = empleadosMap.get(selectedEmpleado);

            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE oc.id_empleado = ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEmpleado);
            ResultSet rs = pstmt.executeQuery();

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

                model.addRow(new Object[]{
                        idOrden, cliente, empleado, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar órdenes por empleado: " + e.getMessage());
        }
    }

    /**
     * Carga las órdenes de compra filtradas por cliente seleccionado.
     */
    private void cargarOrdenesPorCliente() {
        model.setRowCount(0); // Limpiar tabla

        try {
            String selectedCliente = clienteComboBox.getSelectedItem().toString();
            int idCliente = clientesMap.get(selectedCliente);

            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE oc.id_cliente = ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

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

                model.addRow(new Object[]{
                        idOrden, cliente, empleado, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar órdenes por cliente: " + e.getMessage());
        }
    }

    /**
     * Carga las órdenes de compra filtradas por producto seleccionado.
     */
    private void cargarOrdenesPorProducto() {
        model.setRowCount(0); // Limpiar tabla

        try {
            String selectedProducto = productoComboBox.getSelectedItem().toString();
            int idProducto = productosMap.get(selectedProducto);

            String sql = "SELECT oc.id_orden_compra, c.nombre as cliente, e.nombre as empleado, " +
                    "p.nombre_producto, rv.cantidad, oc.total, oc.estado_orden, oc.fecha_compra " +
                    "FROM ordenes_compra oc " +
                    "JOIN clientes c ON oc.id_cliente = c.id_cliente " +
                    "JOIN empleados e ON oc.id_empleado = e.id_empleado " +
                    "JOIN inventario_productos p ON oc.id_producto = p.id_producto " +
                    "LEFT JOIN registro_ventas rv ON oc.id_orden_compra = rv.id_orden_compra " +
                    "WHERE oc.id_producto = ? " +
                    "ORDER BY oc.id_orden_compra DESC";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

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

                model.addRow(new Object[]{
                        idOrden, cliente, empleado, producto, cantidad, totalOrden, estado, fecha
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar órdenes por producto: " + e.getMessage());
        }
    }

    /**
     * Verifica si hay suficiente stock disponible para un producto.
     * @param idProducto ID del producto a verificar
     * @param cantidad Cantidad solicitada
     * @return true si hay suficiente stock, false en caso contrario
     */
    private boolean verificarStock(int idProducto, int cantidad) {
        try {
            String sql = "SELECT cantidad_stock FROM inventario_productos WHERE id_producto = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProducto);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int stockDisponible = rs.getInt("cantidad_stock");
                return stockDisponible >= cantidad;
            }

            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar stock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el stock de un producto después de una compra.
     * @param idProducto ID del producto
     * @param cantidad Cantidad vendida que se restará del stock
     */
    private void actualizarStock(int idProducto, int cantidad) {
        try {
            String sql = "UPDATE inventario_productos SET cantidad_stock = cantidad_stock - ? WHERE id_producto = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar stock: " + e.getMessage());
        }
    }

    /**
     * Carga los datos de una orden seleccionada en los campos de la interfaz.
     * @param row Índice de la fila seleccionada en la tabla
     */
    private void cargarDatosEnCampos(int row) {
        idOrdenCompra.setText(table1.getValueAt(row, 0).toString());

        // Buscar y seleccionar el cliente correcto en el combobox
        String clienteTabla = table1.getValueAt(row, 1).toString();
        for (int i = 0; i < clienteComboBox.getItemCount(); i++) {
            String clienteCombo = clienteComboBox.getItemAt(i);
            if (clienteCombo.contains(clienteTabla)) {
                clienteComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Buscar y seleccionar el empleado correcto en el combobox
        String empleadoTabla = table1.getValueAt(row, 2).toString();
        for (int i = 0; i < empleadoComboBox.getItemCount(); i++) {
            String empleadoCombo = empleadoComboBox.getItemAt(i);
            if (empleadoCombo.contains(empleadoTabla)) {
                empleadoComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Buscar y seleccionar el producto correcto en el combobox
        String productoTabla = table1.getValueAt(row, 3).toString();
        for (int i = 0; i < productoComboBox.getItemCount(); i++) {
            String productoCombo = productoComboBox.getItemAt(i);
            if (productoCombo.contains(productoTabla)) {
                productoComboBox.setSelectedIndex(i);
                break;
            }
        }

        cantidadTextField.setText(table1.getValueAt(row, 4).toString());
        total.setText(table1.getValueAt(row, 5).toString());

        String estadoTabla = table1.getValueAt(row, 6).toString();
        for (int i = 0; i < estadoComboBox.getItemCount(); i++) {
            if (estadoComboBox.getItemAt(i).equals(estadoTabla)) {
                estadoComboBox.setSelectedIndex(i);
                break;
            }
        }

        fechaCompra.setText(table1.getValueAt(row, 7).toString());
    }

    /**
     * Limpia todos los campos de la interfaz y restablece los valores por defecto.
     */
    private void limpiarCampos() {
        idOrdenCompra.setText("");
        cantidadTextField.setText("");
        total.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fechaCompra.setText(dateFormat.format(new Date()));

        if (clienteComboBox.getItemCount() > 0) clienteComboBox.setSelectedIndex(0);
        if (empleadoComboBox.getItemCount() > 0) empleadoComboBox.setSelectedIndex(0);
        if (productoComboBox.getItemCount() > 0) productoComboBox.setSelectedIndex(0);
        estadoComboBox.setSelectedIndex(0);

        table1.clearSelection();
    }

    /**
     * Método principal para iniciar la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Establecer look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión de Órdenes de Compra");
        frame.setContentPane(new OrdenesCompraGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
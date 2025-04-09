package VentasGUI;

import Conexion.ConexionDB;
import Orden_Compras.OrdenesCompraGUI;
import Inventario.InventarioDAO;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que implementa la interfaz gráfica de usuario para la gestión de ventas.
 * Permite realizar operaciones como buscar productos, agregarlos a una venta,
 * calcular subtotales, gestionar clientes y empleados, y registrar ventas en la base de datos.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class VentasGUI extends JFrame {
    /** Campo de texto para buscar productos */
    private JTextField buscador;
    /** Botón para seleccionar productos de la tabla */
    private JButton clickParaSeleccionarButton;
    /** Tabla que muestra los productos disponibles */
    private JTable datosproducto;
    /** Panel para mostrar clientes disponibles */
    private JPanel Clientesdisponible;
    /** Campo de texto auxiliar 2 */
    private JTextField textField2;
    /** Campo de texto auxiliar 3 */
    private JTextField textField3;
    /** Campo de texto auxiliar 4 */
    private JTextField textField4;
    /** Control para seleccionar cantidades */
    private JSpinner spinner1;
    /** Panel para mostrar precio y cantidad */
    private JPanel preciocantidad;
    /** Campo de texto para ingresar cantidad de venta */
    private JTextField cant_venta;
    /** Botón para agregar producto a la venta */
    private JButton agregarProductoButton;
    /** Panel principal de la interfaz */
    private JPanel main;
    /** Campo de texto para mostrar subtotal */
    private JTextField subtotalf;
    /** Botón para eliminar producto de la venta */
    private JButton eliminarButton;
    /** Tabla que muestra los productos agregados a la venta */
    private JTable productosventa;
    /** Campo de texto auxiliar 8 para total */
    private JTextField textField8;
    /** Botón para finalizar la venta */
    private JButton cobrarButton;
    /** Panel de desplazamiento para buscar productos */
    private JScrollPane buscarproducto;
    /** Panel de desplazamiento para datos de venta */
    private JScrollPane datoventa;
    /** Campo de texto auxiliar 1 */
    private JTextField textField1;
    /** Panel para mostrar el producto elegido */
    private JPanel producto_elegido;
    /** Campo de texto para buscar cliente */
    private JTextField buscarcliente;
    /** Campo de texto para mostrar cédula del cliente */
    private JTextField Ccedula;
    /** Panel para información del cliente */
    private JPanel infoc;
    /** Campo de texto para mostrar fecha actual */
    private JTextField calendario;
    /** Campo de texto para mostrar nombre del empleado */
    private JTextField nombreE;
    /** Campo de texto para id del empleado */
    private JTextField idempleado;
    /** Selector para estado de la orden */
    private JComboBox<String> estado1;
    /** Campo de texto para id del cliente */
    private JTextField idcliente;
    /** Botón para volver a la pantalla anterior */
    private JButton volverButton;

    /** Contador de filas */
    int filas = 0;
    /** Total monetario */
    double totalm = 0;
    /** Total con IVA */
    double totalconiva = 0;
    /** Porcentaje de IVA */
    double IVA = 0.19;

    /** Objeto para acceder a operaciones de inventario */
    InventarioDAO inventarioDAO = new InventarioDAO();
    /** Objeto para acceder a operaciones de órdenes de compra */
    OrdenesCompraGUI detalleOrdenDAO = new OrdenesCompraGUI();
    /** Cadena para búsqueda de cliente */
    private String buscar_cliente;

    /**
     * Obtiene el panel principal de la interfaz
     *
     * @return El panel principal
     */
    public JPanel getMainPanel() {
        return main;
    }

    /**
     * Constructor de la clase VentasGUI
     * Inicializa los componentes y configura los oyentes de eventos
     */
    public VentasGUI() {
        initComponents();
        setupEventListeners();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica
     * Configura la fecha actual, obtiene los datos de productos,
     * y establece propiedades básicas de la ventana
     */
    private void initComponents() {
        // Configuración de la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(new Date());

        obtener_datos_producto();

        setContentPane(main);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1006, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Gestión de Ventas");

        // Llenar el ComboBox de estados
        if (estado1 != null) {
            estado1.removeAllItems();
            estado1.addItem("pendiente");
            estado1.addItem("pagada");
            estado1.addItem("enviada");
        }

        // Establecer la fecha actual
        if (calendario != null) {
            calendario.setText(fechaActual);
            calendario.setEditable(false);
        }

        // Inicializar modelo de tabla para productos de venta
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Producto");
        model.addColumn("Nombre");
        model.addColumn("Cantidad Venta");
        model.addColumn("Precio Unit.");
        model.addColumn("SubTotal");
        productosventa.setModel(model);
    }

    /**
     * Configura los oyentes de eventos para los componentes de la interfaz
     * Maneja eventos de botones, campos de texto y tablas
     */
    private void setupEventListeners() {
        // Listener para el botón de seleccionar producto
        clickParaSeleccionarButton.addActionListener(e -> agregar_datos_p2());

        // Listener para el botón de agregar producto
        agregarProductoButton.addActionListener(e -> {
            if (validarCamposProducto()) {
                agregar_datos_p();
                psubtotal();
                clear();
            }
        });

        // Listener para el botón eliminar
        eliminarButton.addActionListener(e -> {
            int selectedRow = productosventa.getSelectedRow();

            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que deseas eliminar este producto?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DefaultTableModel model = (DefaultTableModel) productosventa.getModel();

                    // Obtener el subtotal del producto eliminado
                    String subtotalStr = (String) model.getValueAt(selectedRow, 4);
                    double subtotalEliminado = Double.parseDouble(subtotalStr);

                    // Calcular el total con IVA del producto eliminado
                    double subtotalConIva = subtotalEliminado * (1 + IVA);

                    // Revertir el cambio en el inventario
                    int idProducto = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
                    int cantidad = Integer.parseInt((String) model.getValueAt(selectedRow, 2));
                    restaurarInventario(idProducto, cantidad);

                    model.removeRow(selectedRow);

                    // Actualizar el total
                    if (totalm - subtotalConIva >= 0) {
                        totalm -= subtotalConIva;
                    } else {
                        totalm = 0;
                    }

                    textField8.setText(String.valueOf(totalm));
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado ningún producto para eliminar.");
            }
        });

        // Listener para selección de producto en la tabla
        datosproducto.addMouseListener(new MouseAdapter() {
            /**
             * Maneja el evento de clic en la tabla de productos
             * Actualiza los campos de texto con la información del producto seleccionado
             *
             * @param e El evento de ratón
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = datosproducto.getSelectedRow();

                if (selectFila >= 0) {
                    textField2.setText((String) datosproducto.getValueAt(selectFila, 0));
                    textField3.setText((String) datosproducto.getValueAt(selectFila, 1));
                    textField4.setText((String) datosproducto.getValueAt(selectFila, 4));  // Ahora en índice 4
                    textField1.setText((String) datosproducto.getValueAt(selectFila, 3));  // Ahora en índice 3

                    filas = selectFila;
                }
            }
        });

        // Listener para el campo de cantidad de venta
        cant_venta.addActionListener(e -> {
            if (!textField4.getText().isEmpty() && !cant_venta.getText().isEmpty()) {
                try {
                    double precio = Double.parseDouble(textField4.getText());
                    int cantidad = Integer.parseInt(cant_venta.getText());

                    // Validar que la cantidad no exceda el inventario disponible
                    int cantidadDisponible = Integer.parseInt(textField1.getText());
                    if (cantidad > cantidadDisponible) {
                        JOptionPane.showMessageDialog(null,
                                "No hay suficiente stock disponible. Stock actual: " + cantidadDisponible,
                                "Error de inventario", JOptionPane.ERROR_MESSAGE);
                        cant_venta.setText("");
                        subtotalf.setText("");
                        return;
                    }

                    double totalr = precio * cantidad;
                    subtotalf.setText(String.valueOf(totalr));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese valores numéricos válidos",
                            "Error de formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener para el campo de subtotal
        subtotalf.addActionListener(e -> psubtotal());

        // Listener para el campo ID del empleado
        idempleado.addActionListener(e -> {
            if (!idempleado.getText().isEmpty()) {
                buscar_empleado();
            }
        });

        // Listener para el campo ID del cliente
        idcliente.addActionListener(e -> {
            if (!idcliente.getText().isEmpty()) {
                buscar_cliente();
            }
        });

        // Listener para buscar productos
        buscador.addKeyListener(new KeyAdapter() {
            /**
             * Maneja el evento de liberación de tecla en el campo de búsqueda
             * Actualiza la tabla de productos según el texto ingresado
             *
             * @param e El evento de teclado
             */
            @Override
            public void keyReleased(KeyEvent e) {
                obtener_datos_producto();
            }
        });

        // Listener para el botón cobrar
        cobrarButton.addActionListener(e -> {
            if (validarCamposCobro()) {
                try {
                    // Verificar que hay productos en la tabla
                    if (productosventa.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null,
                                "No hay productos agregados a la venta",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Obtener datos
                    int id_cliente = Integer.parseInt(idcliente.getText());
                    int id_empleado = Integer.parseInt(idempleado.getText());
                    double total = Double.parseDouble(textField8.getText());
                    String estado = (String) estado1.getSelectedItem();

                    // Registrar orden de compra
                    Connection con = conexionBD.getConnection();
                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    int id_orden = 0;

                    try {
                        // Primero crear la orden de compra
                        String queryOrden = "INSERT INTO ordenes_compra (id_cliente, id_empleado, id_producto, total, estado_orden) VALUES (?, ?, ?, ?, ?)";
                        stmt = con.prepareStatement(queryOrden, Statement.RETURN_GENERATED_KEYS);

                        stmt.setInt(1, id_cliente);
                        stmt.setInt(2, id_empleado);

                        // Obtener el primer producto de la tabla para el id_producto en la orden
                        int id_producto = Integer.parseInt((String) productosventa.getValueAt(0, 0));
                        stmt.setInt(3, id_producto);
                        stmt.setDouble(4, total);
                        stmt.setString(5, estado);

                        stmt.executeUpdate();

                        // Obtener el ID generado para la orden
                        rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            id_orden = rs.getInt(1);
                        }

                        // Registrar cada producto en el registro de ventas
                        for (int i = 0; i < productosventa.getRowCount(); i++) {
                            id_producto = Integer.parseInt((String) productosventa.getValueAt(i, 0));
                            int cantidad = Integer.parseInt((String) productosventa.getValueAt(i, 2));
                            double precio_producto = Double.parseDouble((String) productosventa.getValueAt(i, 3));
                            double subtotal = Double.parseDouble((String) productosventa.getValueAt(i, 4));

                            String queryVenta = "INSERT INTO registro_ventas (id_orden_compra, id_producto, cantidad, precio_producto, sub_total, id_cliente, id_empleado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement stmtVenta = con.prepareStatement(queryVenta);
                            stmtVenta.setInt(1, id_orden);
                            stmtVenta.setInt(2, id_producto);
                            stmtVenta.setInt(3, cantidad);
                            stmtVenta.setDouble(4, precio_producto);
                            stmtVenta.setDouble(5, subtotal);
                            stmtVenta.setInt(6, id_cliente);
                            stmtVenta.setInt(7, id_empleado);

                            stmtVenta.executeUpdate();
                            stmtVenta.close();
                        }

                        JOptionPane.showMessageDialog(null,
                                "Venta registrada con éxito. Número de orden: " + id_orden,
                                "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);

                        limpiarFormulario();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Error al registrar la venta: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        try {
                            if (rs != null) rs.close();
                            if (stmt != null) stmt.close();
                            if (con != null) con.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Por favor ingrese valores numéricos válidos",
                            "Error de formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener para el botón volver si existe
        if (volverButton != null) {
            volverButton.addActionListener(e -> {
                dispose();
                // Si tienes un menú principal para volver
                // MainMenu mainMenu = new MainMenu();
                // mainMenu.setVisible(true);
            });
        }
    }

    /**
     * Valida los campos antes de agregar un producto a la venta
     * Verifica que se haya seleccionado un producto, que la cantidad sea válida
     * y que haya suficiente stock disponible
     *
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCamposProducto() {
        if (textField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cant_venta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int cantidad = Integer.parseInt(cant_venta.getText());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Verificar stock disponible
            int cantidadDisponible = Integer.parseInt(textField1.getText());
            if (cantidad > cantidadDisponible) {
                JOptionPane.showMessageDialog(null,
                        "No hay suficiente stock disponible. Stock actual: " + cantidadDisponible,
                        "Error de inventario", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Valida los campos antes de finalizar la venta
     * Verifica que se haya ingresado un cliente y un empleado, y que haya productos en la venta
     *
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCamposCobro() {
        if (idcliente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el ID del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (idempleado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el ID del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (productosventa.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Agregue al menos un producto a la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Limpia todos los campos del formulario y reinicia el total
     * Se utiliza después de completar una venta
     */
    private void limpiarFormulario() {
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField1.setText("");
        cant_venta.setText("");
        subtotalf.setText("");
        textField8.setText("");
        buscarcliente.setText("");
        Ccedula.setText("");
        nombreE.setText("");
        idempleado.setText("");
        idcliente.setText("");

        DefaultTableModel model = (DefaultTableModel) productosventa.getModel();
        model.setRowCount(0);

        totalm = 0;
        totalconiva = 0;
        obtener_datos_producto();
    }

    /**
     * Limpia los campos relacionados con el producto después de agregarlo a la venta
     */
    public void clear() {
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField1.setText("");
        cant_venta.setText("");
        subtotalf.setText("");
    }

    /** Objeto para conexión a la base de datos */
    ConexionDB conexionBD = new ConexionDB();

    /**
     * Obtiene los datos de productos de la base de datos y los muestra en la tabla
     * Permite filtrar por nombre de producto
     */
    public void obtener_datos_producto() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID Producto");
        model.addColumn("Nombre");
        model.addColumn("Categoría");  // Añadir columna para categoría
        model.addColumn("Cantidad Disponible");
        model.addColumn("Precio");

        datosproducto.setModel(model);
        String[] dato = new String[5];  // Aumentar tamaño del array a 5 para incluir categoría
        Connection con = ConexionDB.getConnection();

        try {
            // Modificar la consulta para incluir la columna categoría
            String query = "SELECT id_producto, nombre_producto, categoria, cantidad_stock, precio_producto FROM inventario_productos";

            String nombreProductoSeleccionado = buscador.getText();
            if (!nombreProductoSeleccionado.isEmpty()) {
                query = "SELECT id_producto, nombre_producto, categoria, cantidad_stock, precio_producto FROM inventario_productos WHERE nombre_producto LIKE ?";
            }
            PreparedStatement pstmt = con.prepareStatement(query);

            if (!nombreProductoSeleccionado.isEmpty()) {
                pstmt.setString(1, "%" + nombreProductoSeleccionado + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            model.setRowCount(0);

            while (rs.next()) {
                dato[0] = rs.getString("id_producto");
                dato[1] = rs.getString("nombre_producto");
                dato[2] = rs.getString("categoria");  // Añadir categoría
                dato[3] = rs.getString("cantidad_stock");
                dato[4] = rs.getString("precio_producto");

                model.addRow(dato);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar los productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Agrega un producto a la tabla de venta
     * Si el producto ya existe, actualiza la cantidad y el subtotal
     * Si es nuevo, agrega una nueva fila
     */
    public void agregar_datos_p() {
        DefaultTableModel model = (DefaultTableModel) productosventa.getModel();

        String[] dato = new String[5]; // Ahora 5 elementos para incluir el precio unitario
        dato[0] = textField2.getText(); // ID Producto
        dato[1] = textField3.getText(); // Nombre
        dato[2] = cant_venta.getText(); // Cantidad
        dato[3] = textField4.getText(); // Precio unitario (NUEVO)
        dato[4] = subtotalf.getText(); // Subtotal

        // Verificar si ya existe este producto en la tabla
        boolean productoExistente = false;
        int filaExistente = -1;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(dato[0])) {
                productoExistente = true;
                filaExistente = i;
                break;
            }
        }

        if (productoExistente) {
            // Actualizar cantidad y subtotal
            int cantidadActual = Integer.parseInt((String) model.getValueAt(filaExistente, 2));
            int nuevaCantidad = cantidadActual + Integer.parseInt(dato[2]);

            // Verificar stock disponible
            int cantidadDisponible = Integer.parseInt(textField1.getText());
            if (nuevaCantidad > cantidadDisponible) {
                JOptionPane.showMessageDialog(null,
                        "No hay suficiente stock disponible. Stock actual: " + cantidadDisponible,
                        "Error de inventario", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precioUnitario = Double.parseDouble(textField4.getText());
            double nuevoSubtotal = nuevaCantidad * precioUnitario;

            model.setValueAt(String.valueOf(nuevaCantidad), filaExistente, 2);
            model.setValueAt(String.valueOf(nuevoSubtotal), filaExistente, 4);
        } else {
            // Agregar nueva fila
            model.addRow(dato);
        }

        // Actualizar el inventario (reducir la cantidad disponible)
        actualizarInventario(Integer.parseInt(dato[0]), Integer.parseInt(dato[2]));
    }

    /**
     * Actualiza el inventario después de agregar un producto a la venta
     * Reduce la cantidad disponible del producto
     *
     * @param idProducto El ID del producto
     * @param cantidadVendida La cantidad vendida
     */
    public void actualizarInventario(int idProducto, int cantidadVendida) {
        Connection con = conexionBD.getConnection();
        PreparedStatement stmt = null;

        try {
            // Adaptado a la estructura de la base de datos
            String query = "UPDATE inventario_productos SET cantidad_stock = cantidad_stock - ? WHERE id_producto = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, cantidadVendida);
            stmt.setInt(2, idProducto);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inventario actualizado exitosamente.");
            } else {
                System.out.println("Error al actualizar el inventario.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar el inventario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Restaura el inventario cuando se elimina un producto de la venta
     * Aumenta la cantidad disponible del producto
     *
     * @param idProducto El ID del producto
     * @param cantidad La cantidad a restaurar
     */
    public void restaurarInventario(int idProducto, int cantidad) {
        Connection con = conexionBD.getConnection();
        PreparedStatement stmt = null;

        try {
            String query = "UPDATE inventario_productos SET cantidad_stock = cantidad_stock + ? WHERE id_producto = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Inventario restaurado exitosamente.");
            } else {
                System.out.println("Error al restaurar el inventario.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al restaurar el inventario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Agrega el producto seleccionado en la tabla a los campos de texto
     * Se utiliza cuando se hace clic en el botón de seleccionar producto
     */
    public void agregar_datos_p2() {
        if (datosproducto.getRowCount() > 0) {
            int selectFila = datosproducto.getSelectedRow();

            if (selectFila >= 0) {
                textField2.setText((String) datosproducto.getValueAt(selectFila, 0));  // ID Producto
                textField3.setText((String) datosproducto.getValueAt(selectFila, 1));  // Nombre
                textField1.setText((String) datosproducto.getValueAt(selectFila, 3));  // Cantidad Disponible (ahora en índice 3)
                textField4.setText((String) datosproducto.getValueAt(selectFila, 4));  // Precio (ahora en índice 4)
            } else {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un producto de la tabla",
                        "Selección", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Busca un cliente por su ID en la base de datos y muestra su información
     */
    public void buscar_cliente() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = conexionBD.getConnection();
            String idClienteStr = idcliente.getText();

            // Adaptado a la estructura de la base de datos
            String query = "SELECT nombre, telefono FROM clientes WHERE id_cliente = ?";

            stmt = con.prepareStatement(query);
            stmt.setString(1, idClienteStr);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");

                Ccedula.setText(nombre);
                buscarcliente.setText(telefono);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                Ccedula.setText("");
                buscarcliente.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al buscar cliente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Busca un empleado por su ID en la base de datos y muestra su nombre
     */
    /**
     * Busca un empleado en la base de datos utilizando el ID proporcionado en el campo idempleado.
     * Si encuentra el empleado, muestra su nombre en el campo nombreE.
     * Si no lo encuentra, muestra un mensaje de error y limpia el campo nombreE.
     *
     * El método establece una conexión con la base de datos, prepara y ejecuta una consulta SQL
     * para obtener el nombre del empleado basándose en su ID, y maneja posibles excepciones.
     *
     * @throws SQLException Si ocurre un error durante la conexión o consulta a la base de datos
     */
    public void buscar_empleado() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = conexionBD.getConnection();
            String idEmpleadoStr = idempleado.getText();

            // Adaptado a la estructura de la base de datos
            String query = "SELECT nombre FROM empleados WHERE id_empleado = ?";

            stmt = con.prepareStatement(query);
            stmt.setString(1, idEmpleadoStr);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                nombreE.setText(nombre);
            } else {
                JOptionPane.showMessageDialog(null, "Empleado no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                nombreE.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al buscar empleado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calcula el subtotal de una venta incluyendo el IVA.
     * Utiliza los valores ingresados en los campos textField4 (precio) y cant_venta (cantidad).
     * Calcula el total multiplicando precio por cantidad, calcula el IVA correspondiente,
     * y actualiza los campos subtotalf y textField8 con los resultados.
     *
     * El método verifica que los campos necesarios no estén vacíos antes de realizar los cálculos.
     * También actualiza la variable totalm sumándole el total con IVA de la venta actual.
     *
     * @throws NumberFormatException Si los valores ingresados no pueden convertirse a números
     */
    public void psubtotal() {
        if (!textField4.getText().isEmpty() && !cant_venta.getText().isEmpty()) {
            try {
                double precio = Double.parseDouble(textField4.getText());
                int cantidad = Integer.parseInt(cant_venta.getText());

                double totalr = precio * cantidad;
                double ivatotal = totalr * IVA;
                totalconiva = totalr + ivatotal;

                subtotalf.setText(String.valueOf(totalr));
                totalm += totalconiva;
                textField8.setText(String.valueOf(totalm));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Por favor ingrese valores numéricos válidos",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * Establece el tema visual FlatArcDarkIJTheme para la interfaz gráfica y
     * inicia la aplicación creando una instancia de VentasGUI.
     *
     * @param args Argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VentasGUI formVenta = new VentasGUI();
            formVenta.setVisible(true);
        });
    }
}


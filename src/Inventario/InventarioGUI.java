package Inventario;

import Conexion.ConexionDB;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Interfaz gráfica de usuario para la gestión de inventario.
 * Permite agregar, actualizar y eliminar productos.
 *
 * @author Cristian Restrepo
 * @version 1.0
 */
public class InventarioGUI {
    /** Panel principal de la interfaz */
    public JPanel main; // Make this public for direct access

    /** Tabla para mostrar los productos */
    private JTable table1;

    /** Campo de texto para el ID del producto */
    private JTextField id;

    /** Campo de texto para el nombre del producto */
    private JTextField nombre;

    /** Campo de texto para la categoría del producto */
    private JTextField categoria;

    /** Campo de texto para el precio del producto */
    private JTextField precio;

    /** Campo de texto para la cantidad en stock */
    private JTextField cantidad_stock;

    /** Campo de texto para el ID del proveedor */
    private JTextField id_proveedor;

    /** Botón para agregar un nuevo producto */
    private JButton agregarButton;

    /** Botón para actualizar un producto existente */
    private JButton actualizarButton;

    /** Botón para eliminar un producto */
    private JButton eliminarButton;
    private JButton volverButton;
    private JComboBox comboBox1;
    private JComboBox proveedorAsociadoComboBox;

    /** Objeto para realizar operaciones de acceso a datos */
    InventarioDAO inventarioDAO = new InventarioDAO();

    /**
     * Constructor de la interfaz gráfica de inventario.
     * Inicializa los componentes y configura los listeners.
     */
    public InventarioGUI() {
        obtener_datos();
        id.setEnabled(false);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombre.getText();
                String categoriaProducto = comboBox1.getSelectedItem().toString();
                int precioProducto = Integer.parseInt(precio.getText());
                int cantidadStock = Integer.parseInt(cantidad_stock.getText());
                String seleccionado = proveedorAsociadoComboBox.getSelectedItem().toString();
                Integer idProveedor = null;
                if (!seleccionado.isEmpty() && seleccionado.contains(" - ")) {
                    // Se asume que el formato es "id - nombre"
                    String[] partes = seleccionado.split(" - ");
                    idProveedor = Integer.parseInt(partes[0]);
                }
                Inventario inventario = new Inventario(0, nombreProducto, categoriaProducto, cantidadStock, precioProducto, idProveedor);
                inventarioDAO.agregar(inventario);
                obtener_datos();
                clear();
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombre.getText();
                String categoriaProducto = comboBox1.getSelectedItem().toString();
                int precioProducto = Integer.parseInt(precio.getText());
                int cantidadStock = Integer.parseInt(cantidad_stock.getText());
                int idProducto = Integer.parseInt(id.getText());
                String seleccionado = proveedorAsociadoComboBox.getSelectedItem().toString();
                Integer idProveedor = null;
                if (!seleccionado.isEmpty() && seleccionado.contains(" - ")) {
                    // Se asume que el formato es "id - nombre"
                    String[] partes = seleccionado.split(" - ");
                    idProveedor = Integer.parseInt(partes[0]);
                }

                Inventario inventario = new Inventario(idProducto, nombreProducto, categoriaProducto, cantidadStock, precioProducto, idProveedor);
                inventarioDAO.actualizar(inventario);
                obtener_datos();
                clear();
            }
        });

        // Llamamos al método para cargar proveedores y asignamos el modelo al combobox
        DefaultComboBoxModel<String> proveedorModel = inventarioDAO.cargarProveedores();
        proveedorAsociadoComboBox.setModel(proveedorModel);

        // Si necesitás detectar cambios en el combobox para extraer el id,
        // podés agregar un ActionListener:
        proveedorAsociadoComboBox.addActionListener(e -> {
            String seleccionado = (String) proveedorAsociadoComboBox.getSelectedItem();

            // Por ejemplo, si el formato es "id - nombre", podés extraer el id así:
            if(seleccionado != null && seleccionado.contains(" - ")) {
                String[] parts = seleccionado.split(" - ");
                int idProveedor = Integer.parseInt(parts[0]);
                System.out.println("Proveedor seleccionado: " + idProveedor);
                // Guardá o procesá el idProveedor según necesites
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
                    comboBox1.setSelectedItem(table1.getValueAt(selectFila, 2).toString());
                    cantidad_stock.setText(table1.getValueAt(selectFila, 3).toString());
                    precio.setText(table1.getValueAt(selectFila, 4).toString());

                    // Handle potential null value for provider ID
                    Object proveedorValue = table1.getValueAt(selectFila, 5);
                    id_proveedor.setText(proveedorValue == null ? "" : proveedorValue.toString());
                }
            }
        });

        // Add functionality to volverButton (if needed) to return to main menu
        if (volverButton != null) {
            volverButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This will be handled by MenuPrueba
                    Container parent = main.getParent();
                    if (parent != null && parent.getLayout() instanceof CardLayout) {
                        CardLayout cl = (CardLayout) parent.getLayout();
                        cl.show(parent, "welcome");
                    }
                }
            });
        }
    }

    /**
     * Limpia todos los campos de entrada de la interfaz.
     */
    public void clear() {
        id.setText("");
        nombre.setText("");
        comboBox1.setSelectedIndex(0);
        precio.setText("");
        cantidad_stock.setText("");
        id_proveedor.setText("");
    }

    /** Conexión a la base de datos */
    ConexionDB conexionDB = new ConexionDB();

    /**
     * Obtiene y muestra los datos de productos en la tabla.
     */
    public void obtener_datos() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("id_producto");
        model.addColumn("nombre_producto");
        model.addColumn("categoria");
        model.addColumn("cantidad_stock");
        model.addColumn("precio_producto");
        model.addColumn("id_proveedor_asociado");

        table1.setModel(model);
        Object[] dato = new Object[6];
        Connection con = conexionDB.getConnection();

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM inventario_productos";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dato[0] = rs.getInt("id_producto");
                dato[1] = rs.getString("nombre_producto");
                dato[2] = rs.getString("categoria");
                dato[3] = rs.getInt("cantidad_stock");
                dato[4] = rs.getInt("precio_producto");
                dato[5] = rs.getObject("id_proveedor_asociado");
                model.addRow(dato);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main panel of this GUI.
     * This method is used by MenuPrueba to get the panel to display.
     *
     * @return The main panel of this GUI
     */
    public JPanel getMainPanel() {
        return main; // Return the actual main panel instead of null
    }

    /**
     * Método principal para iniciar la aplicación de gestión de inventario.
     *
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Gestión de Inventario");
        frame.setContentPane(new InventarioGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1006,550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
}
package PruebaMenu;

import Clientes.ClientesGUI;
import Empleados.EmpleadosGUI;
import Inventario.InventarioGUI;
import Orden_Compras.OrdenesCompraGUI;
import Proveedores.ProveedoresGUI;
import Reportes.ReportesGUI;
import Sockets.ServidorGUI;
import VentasGUI.VentasGUI;
import Conexion.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

/**
 * Clase principal para gestionar el menú de la aplicación.
 * Proporciona navegación entre diferentes módulos y una interfaz gráfica basada en Swing.
 */
public class MenuPrueba {
    // Componentes principales de la interfaz gráfica
    public JPanel mainPanel; // Panel principal que contiene todos los elementos
    public JPanel menuPanel; // Panel lateral del menú
    public JPanel contentPanel; // Panel para mostrar el contenido de los módulos
    public JPanel welcomePanel; // Panel de bienvenida
    public JLabel lblTitulo; // Etiqueta para el título
    public JButton btnInventario; // Botón para acceder al módulo de inventario
    public JButton btnClientes; // Botón para acceder al módulo de clientes
    public JButton btnEmpleados; // Botón para acceder al módulo de empleados
    public JButton btnProveedores; // Botón para acceder al módulo de proveedores
    public JButton btnOrdenesCompra; // Botón para acceder al módulo de órdenes de compra
    public JButton btnVender; // Botón para acceder al módulo de ventas
    public JButton btnReportes; // Botón para acceder al módulo de reportes
    public JButton btnToggleMenu; // Botón para mostrar/ocultar el menú lateral
    public JLabel lblWelcomeImage; // Etiqueta para mostrar una imagen de bienvenida
    private JButton chatButton; // Botón para acceder al módulo de chat
    private JButton salirButton;

    private boolean menuVisible = true; // Estado de visibilidad del menú lateral
    private Map<String, JPanel> panelCache = new HashMap<>(); // Caché de paneles cargados
    private Map<String, Object> instanceCache = new HashMap<>(); // Caché de instancias de módulos

    /**
     * Constructor de la clase MenuPrueba.
     * Configura el diseño inicial, listeners de botones y carga la imagen de bienvenida.
     */
    public MenuPrueba() {
        // Configurar el contentPanel con CardLayout para gestionar diferentes vistas
        contentPanel.setLayout(new CardLayout());
        contentPanel.add(welcomePanel, "welcome");

        // Mostrar/Ocultar menú lateral
        btnToggleMenu.addActionListener(e -> {
            menuVisible = !menuVisible;
            menuPanel.setVisible(menuVisible);
            btnToggleMenu.setText(menuVisible ? "☰ Ocultar Menú" : "☰ Mostrar Menú");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        // Cargar imagen en lblWelcomeImage
        try {
            // Obtener el tamaño del JLabel
            int lblWidth = lblWelcomeImage.getWidth();
            int lblHeight = lblWelcomeImage.getHeight();
            // Cargar la imagen desde el archivo
            ImageIcon imagenFondo = new ImageIcon("Resources/Img/fondo_ferreteria.jpg");
            // Redimensionar la imagen si es necesario
            Image img = imagenFondo.getImage();
            // Ajustar al tamaño que necesites (puedes ajustar estos valores)
            Image imgRedimensionada = img.getScaledInstance(750, 450, Image.SCALE_SMOOTH);
            ImageIcon iconoRedimensionado = new ImageIcon(imgRedimensionada);

            lblWelcomeImage.setIcon(iconoRedimensionado);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }

        // Configurar ActionListeners para cada botón
        setupButtonActions();
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(mainPanel,
                        "¿Estás seguro de que deseas salir?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Configura los ActionListeners para los botones del menú.
     */
    private void setupButtonActions() {
        btnInventario.addActionListener(e -> cargarPanel("Inventario", InventarioGUI.class));
        btnClientes.addActionListener(e -> cargarPanel("Clientes", ClientesGUI.class));
        btnEmpleados.addActionListener(e -> cargarPanel("Empleados", EmpleadosGUI.class));
        btnProveedores.addActionListener(e -> cargarPanel("Proveedores", ProveedoresGUI.class));
        btnOrdenesCompra.addActionListener(e -> cargarPanel("OrdenesCompra", OrdenesCompraGUI.class));
        btnVender.addActionListener(e -> cargarPanel("Ventas", VentasGUI.class));
        btnReportes.addActionListener(e -> cargarPanel("Reportes", ReportesGUI.class));
        chatButton.addActionListener(e -> cargarPanel("Chat", ServidorGUI.class));
    }

    /**
     * Carga un panel específico en el contentPanel.
     * Si el panel ya está en caché, lo muestra directamente.
     * Si no, lo crea dinámicamente y lo agrega al caché.
     *
     * @param nombre   Nombre del panel a cargar.
     * @param guiClass Clase del módulo que se desea cargar.
     */
    private void cargarPanel(String nombre, Class<?> guiClass) {
        try {
            System.out.println("Cargando panel: " + nombre);

            if (panelCache.containsKey(nombre)) {
                System.out.println("Panel encontrado en caché");
                mostrarPanel(nombre);
                return;
            }

            Object guiInstance = null;

            // Manejo especial para ReportesGUI que requiere una conexión
            if (guiClass == ReportesGUI.class) {
                Connection conexion = ConexionDB.getConnection();
                if (conexion != null) {
                    guiInstance = new ReportesGUI(conexion);
                } else {
                    JOptionPane.showMessageDialog(mainPanel,
                            "No se pudo establecer conexión con la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                // Instanciación predeterminada para otras clases
                guiInstance = guiClass.getDeclaredConstructor().newInstance();
            }

            System.out.println("Instancia creada: " + guiInstance);

            instanceCache.put(nombre, guiInstance);

            JPanel panelPrincipal = null;

            // Intentar método getMainPanel()
            try {
                Method getMainPanelMethod = guiClass.getMethod("getMainPanel");
                Object result = getMainPanelMethod.invoke(guiInstance);
                if (result instanceof JPanel) {
                    panelPrincipal = (JPanel) result;
                    System.out.println("Panel encontrado mediante método getMainPanel()");
                }
            } catch (NoSuchMethodException e) {
                System.out.println("Método getMainPanel no encontrado: " + e.getMessage());
            }

            // Buscar campo 'main'
            if (panelPrincipal == null) {
                try {
                    Field mainField = guiClass.getField("main");
                    Object fieldValue = mainField.get(guiInstance);
                    if (fieldValue instanceof JPanel) {
                        panelPrincipal = (JPanel) fieldValue;
                        System.out.println("Panel encontrado en campo 'main'");
                    }
                } catch (NoSuchFieldException e) {
                    System.out.println("Campo 'main' no encontrado: " + e.getMessage());
                }
            }

            // Buscar campo 'mainPanel'
            if (panelPrincipal == null) {
                try {
                    Field mainPanelField = guiClass.getField("mainPanel");
                    Object fieldValue = mainPanelField.get(guiInstance);
                    if (fieldValue instanceof JPanel) {
                        panelPrincipal = (JPanel) fieldValue;
                        System.out.println("Panel encontrado en campo 'mainPanel'");
                    }
                } catch (NoSuchFieldException e) {
                    System.out.println("Campo 'mainPanel' no encontrado: " + e.getMessage());
                }
            }

            // Buscar cualquier JPanel en los campos públicos
            if (panelPrincipal == null) {
                for (Field field : guiClass.getFields()) {
                    if (JPanel.class.isAssignableFrom(field.getType())) {
                        Object fieldValue = field.get(guiInstance);
                        if (fieldValue != null) {
                            panelPrincipal = (JPanel) fieldValue;
                            System.out.println("Panel encontrado en campo: " + field.getName());
                            break;
                        }
                    }
                }
            }

            // Panel de error si no se encontró ninguno
            if (panelPrincipal == null) {
                System.out.println("No se encontró ningún panel, creando panel de error");
                panelPrincipal = new JPanel(new BorderLayout());
                JLabel errorLabel = new JLabel("No se pudo cargar el panel " + nombre, SwingConstants.CENTER);
                errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
                panelPrincipal.add(errorLabel, BorderLayout.CENTER);
            }

            // Agregar al contentPanel y a caché
            contentPanel.add(panelPrincipal, nombre);
            panelCache.put(nombre, panelPrincipal);

            mostrarPanel(nombre);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,
                    "Error al cargar el módulo " + nombre + ": " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            mostrarPanel("welcome");
        }
    }

    /**
     * Verifica si un panel contiene un botón de "volver" o similar.
     *
     * @param panel Panel a verificar.
     * @return true si contiene un botón de "volver", false en caso contrario.
     */
    private boolean hasBackButton(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                String buttonText = button.getText().toLowerCase();
                if (buttonText.contains("volver") || buttonText.contains("regresar") ||
                        buttonText.contains("atrás") || buttonText.contains("menu")) {
                    return true;
                }
            } else if (component instanceof JPanel) {
                if (hasBackButton((JPanel) component)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Muestra un panel específico en el contentPanel.
     *
     * @param nombre Nombre del panel a mostrar.
     */
    private void mostrarPanel(String nombre) {
        System.out.println("Mostrando panel: " + nombre);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, nombre);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Método principal para iniciar la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        try {
            // Configurar el tema visual de la aplicación
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar la ventana principal
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menú - Ferretería");
            MenuPrueba app = new MenuPrueba();
            frame.setContentPane(app.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }
}
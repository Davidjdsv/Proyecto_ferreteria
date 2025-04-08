package PruebaMenu;

import Clientes.ClientesGUI;
import Empleados.EmpleadosGUI;
import Inventario.InventarioGUI;
import Orden_Compras.OrdenesCompraGUI;
import Proveedores.ProveedoresGUI;
import Reportes.ReportesGUI;
import VentasGUI.VentasGUI;
import Sockets.ClienteGUI;
import Conexion.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;


public class MenuPrueba {
    public JPanel mainPanel;
    public JPanel menuPanel;
    public JPanel contentPanel;
    public JPanel welcomePanel;
    public JLabel lblTitulo;
    public JButton btnInventario;
    public JButton btnClientes;
    public JButton btnEmpleados;
    public JButton btnProveedores;
    public JButton btnOrdenesCompra;
    public JButton btnVender;
    public JButton btnReportes;
    public JButton btnToggleMenu;
    public JLabel lblWelcomeImage;
    private JButton chatButton;

    private boolean menuVisible = true;
    private Map<String, JPanel> panelCache = new HashMap<>();
    private Map<String, Object> instanceCache = new HashMap<>();

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
            Image imgRedimensionada = img.getScaledInstance(1000, 450, Image.SCALE_SMOOTH);
            ImageIcon iconoRedimensionado = new ImageIcon(imgRedimensionada);

            lblWelcomeImage.setIcon(iconoRedimensionado);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }

        // Configurar ActionListeners para cada botón
        setupButtonActions();
    }

    private void setupButtonActions() {
        btnInventario.addActionListener(e -> cargarPanel("Inventario", InventarioGUI.class));
        btnClientes.addActionListener(e -> cargarPanel("Clientes", ClientesGUI.class));
        btnEmpleados.addActionListener(e -> cargarPanel("Empleados", EmpleadosGUI.class));
        btnProveedores.addActionListener(e -> cargarPanel("Proveedores", ProveedoresGUI.class));
        btnOrdenesCompra.addActionListener(e -> cargarPanel("OrdenesCompra", OrdenesCompraGUI.class));
        btnVender.addActionListener(e -> cargarPanel("Ventas", VentasGUI.class));
        btnReportes.addActionListener(e -> cargarPanel("Reportes", ReportesGUI.class));
        chatButton.addActionListener(e -> cargarPanel("Chat", ClienteGUI.class));
    }

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

    private void mostrarPanel(String nombre) {
        System.out.println("Mostrando panel: " + nombre);
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, nombre);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Menú - Ferretería");
            MenuPrueba app = new MenuPrueba();

            frame.setContentPane(app.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setMinimumSize(new Dimension(900, 700));
            frame.setPreferredSize(new Dimension(1100, 750));
            frame.pack();
            frame.setVisible(true);
        });
    }
}
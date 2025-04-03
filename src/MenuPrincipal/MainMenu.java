package MenuPrincipal;

import Clientes.ClientesGUI;
import Empleados.EmpleadosGUI;
import Inventario.InventarioGUI;
import Proveedores.ProveedoresGUI;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal que implementa el menú principal de la aplicación.
 * Proporciona acceso a los diferentes módulos del sistema a través de una interfaz
 * gráfica con tema oscuro usando FlatLaf.
 *
 * @author David jdsv
 * @version 1.0
 */
public class MainMenu {
    private JPanel main;
    private JButton clientesMenu;
    private JButton empleadosMenu;
    private JButton inventariosMenu;
    private JButton ordenesCompraMenu;
    private JButton proveedoresMenu;
    private JPanel jPanelBackground;

    /**
     * Constructor que inicializa y configura los componentes del menú principal.
     * Aplica estilos a los botones y configura los listeners para la navegación
     * entre módulos.
     */
    public MainMenu() {
        // Mejoramos la apariencia de los botones con efectos
        setupButton(clientesMenu, "Clientes");
        setupButton(empleadosMenu, "Empleados");
        setupButton(inventariosMenu, "Inventario");
        setupButton(ordenesCompraMenu, "Órdenes de Compra");
        setupButton(proveedoresMenu, "Proveedores");

        // Configuramos los event listeners de forma más eficiente
        clientesMenu.addActionListener(e -> navigateTo(clientesMenu, ClientesGUI.class));
        empleadosMenu.addActionListener(e -> navigateTo(empleadosMenu, EmpleadosGUI.class));
        inventariosMenu.addActionListener(e -> navigateTo(inventariosMenu, InventarioGUI.class));
        proveedoresMenu.addActionListener(e -> navigateTo(proveedoresMenu, ProveedoresGUI.class));

        // Mantenemos el fondo personalizado
        jPanelBackground = new JPanelBackground("/Img/hero-java-epic.webp");
    }

    /**
     * Configura la apariencia y estilo de un botón del menú.
     * Aplica propiedades de FlatLaf para mejorar la experiencia visual.
     *
     * @param button El botón a configurar
     * @param text El texto que se mostrará en el botón
     */
    private void setupButton(JButton button, String text) {
        // Aplicamos propiedades de estilo FlatLaf
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_SQUARE);
        button.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        button.setText(text);

        // Mejoramos márgenes y tamaño
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));

        // Añadimos efecto hover personalizado
        button.putClientProperty("JButton.buttonType", "borderless");
        button.putClientProperty("JButton.paintShadow", true);
    }

    /**
     * Navega desde la ventana actual hacia otro módulo del sistema.
     * Cierra la ventana actual y abre la ventana del módulo seleccionado.
     *
     * @param sourceButton El botón que inició la navegación
     * @param destinationClass La clase del módulo destino
     * @param <T> El tipo de la clase destino
     */
    private <T> void navigateTo(JButton sourceButton, Class<T> destinationClass) {
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(sourceButton);
        currentFrame.dispose();

        try {
            // Invocamos el método main de la clase destino usando reflexión
            destinationClass.getMethod("main", String[].class).invoke(null, (Object) null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al abrir la ventana: " + e.getMessage(),
                    "Error de navegación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal que inicia la aplicación.
     * Configura el tema visual FlatLaf y crea la ventana principal.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Aplicamos el tema FlatLaf una sola vez antes de crear la ventana
        try {
            // Opcional: añadir propiedades globales para personalizar más la UI
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);

            FlatArcDarkIJTheme.setup();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al aplicar el tema visual: " + e.getMessage(),
                    "Error de UI", JOptionPane.WARNING_MESSAGE);
        }

        // Creamos y mostramos la ventana principal
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Gestión");
            frame.setContentPane(new MainMenu().main);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
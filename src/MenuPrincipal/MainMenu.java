package MenuPrincipal;

import Clientes.ClientesGUI;
import Empleados.EmpleadosGUI;
import Inventario.InventarioGUI;
import Orden_Compras.OrdenesCompraGUI;
import Proveedores.ProveedoresGUI;
import VentasGUI.VentasGUI;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainMenu {
    private JPanel main;
    private JButton clientesMenu;
    private JButton empleadosMenu;
    private JButton inventariosMenu;
    private JButton ordenesCompraMenu;
    private JButton proveedoresMenu;
    private JButton reportesButton;
    private JButton venderButton;

    public MainMenu() {
        clientesMenu.addActionListener(e -> ClientesGUI.main(null));
        inventariosMenu.addActionListener(e -> InventarioGUI.main(null));
        empleadosMenu.addActionListener(e -> EmpleadosGUI.main(null));
        proveedoresMenu.addActionListener(e -> ProveedoresGUI.main(null));
        ordenesCompraMenu.addActionListener(e -> OrdenesCompraGUI.main(null));
        venderButton.addActionListener(e -> VentasGUI.main(null));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Menú Principal");
        FondoPanel fondoPanel = new FondoPanel();
        MainMenu menuApp = new MainMenu();

        JPanel mainPanel = menuApp.main;
        mainPanel.setOpaque(false);
        fondoPanel.setLayout(new BorderLayout());
        fondoPanel.add(mainPanel, BorderLayout.CENTER);

        frame.setContentPane(fondoPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }

    static class FondoPanel extends JPanel {
        private Image imagenFondo;

        public FondoPanel() {
            URL imagenURL = getClass().getClassLoader().getResource("imagenes/1.png");
            if (imagenURL != null) {
                this.imagenFondo = new ImageIcon(imagenURL).getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (imagenFondo != null) {
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            } else {
                GradientPaint gp = new GradientPaint(0, 0, new Color(51, 153, 255), getWidth(), getHeight(), new Color(0, 51, 102));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}

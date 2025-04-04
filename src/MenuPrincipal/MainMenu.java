package MenuPrincipal;

import Clientes.ClientesGUI;
import Empleados.EmpleadosGUI;
import Inventario.InventarioGUI;
import Orden_Compras.OrdenesCompraGUI;
import Proveedores.ProveedoresGUI;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JPanel main;
    private JButton clientesMenu;
    private JButton empleadosMenu;
    private JButton inventariosMenu;
    private JButton ordenesCompraMenu;
    private JButton proveedoresMenu;
    private JPanel jPanelBackground;

    public MainMenu(){

        clientesMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(clientesMenu);
                jFrame.dispose();
                ClientesGUI.main(null);
            }
        });

        inventariosMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(inventariosMenu);
                jFrame.dispose();
                InventarioGUI.main(null);
            }
        });

        empleadosMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(empleadosMenu);
                jFrame.dispose();
                EmpleadosGUI.main(null);
            }
        });

        proveedoresMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(proveedoresMenu);
                jFrame.dispose();
                ProveedoresGUI.main(null);
            }
        });



        //jPanelBackground = new JPanelBackground("/Img/hero-java-epic.webp");
        ordenesCompraMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(ordenesCompraMenu);
                jFrame.dispose();
                OrdenesCompraGUI.main(null);
            }
        });
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Menú principal");
        frame.setContentPane(new MainMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
}

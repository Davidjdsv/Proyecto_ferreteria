package MenuPrincipal;

import Clientes.ClientesGUI;
import Inventario.InventarioGUI;

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

    public MainMenu(){

        clientesMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(clientesMenu);
                jFrame.dispose();
                ClientesGUI clientesGUI = new ClientesGUI();
                clientesGUI.main(null);
            }
        });



    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Menú principal");
        frame.setContentPane(new MainMenu().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
}

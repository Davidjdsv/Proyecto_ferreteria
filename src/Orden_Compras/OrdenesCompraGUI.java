package Orden_Compras;

import MenuPrincipal.MainMenu;
import Proveedores.ProveedoresGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrdenesCompraGUI {
    private JTable table1;
    private JTextField idOrdenCompra;
    private JTextField idCliente;
    private JTextField idEmpleado;
    private JTextField idProducto;
    private JTextField total;
    private JTextField fechaCompra;
    private JButton agregarCompraButton;
    private JButton actualizarCompraButton;
    private JButton volverButton;
    private JPanel main;

    public OrdenesCompraGUI(){

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(volverButton);
                jFrame.dispose();
                MainMenu.main(null);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ordenes de compra");
        frame.setContentPane(new OrdenesCompraGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
    }
}

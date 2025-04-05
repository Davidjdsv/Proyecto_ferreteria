package Orden_Compras;

import javax.swing.*;

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
    private JPanel main;

    public OrdenesCompraGUI(){
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ordenes de compra");
        frame.setContentPane(new OrdenesCompraGUI().main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1006,550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
}

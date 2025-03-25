package Proveedores;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProveedoresGUI {
    private JTable table1;
    private JTextField id_proveedorTextField;
    private JTextField contactoTextField;
    private JTextField nombreTextField;
    private JTextField categoria_productoTextField;
    private JButton agregarButton;
    private JButton EliminarButton;
    private JButton actualizarButton;

    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();

    //TODO: Botón agregar
    public ProveedoresGUI() {
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoria_productoTextField.getText();

                Proveedores proveedores = new Proveedores(0, nombre, contacto, categoria_producto);
                proveedoresDAO.agregar(proveedores);
            }
        });
    }

}

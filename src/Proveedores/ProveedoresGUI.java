package Proveedores;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProveedoresGUI {
    private JTable table1;
    private JTextField id_proveedorTextField;
    private JTextField contactoTextField;
    private JTextField nombreTextField;
    private JTextField categoria_productoTextField;
    private JButton agregarButton;
    private JButton EliminarButton;
    private JButton actualizarButton;
    int filas = 0;

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

        //TODO: Botón de actualizar
        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTextField.getText();
                String contacto = contactoTextField.getText();
                String categoria_producto = categoria_productoTextField.getText();
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());

                Proveedores proveedores = new Proveedores(id_proveedor, nombre, contacto, categoria_producto);
                proveedoresDAO.actualizar(proveedores);
            }
        });


        EliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_proveedor = Integer.parseInt(id_proveedorTextField.getText());

                proveedoresDAO.eliminar(id_proveedor);
            }
        });


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectFila = table1.getSelectedRow();

                if(fila > 0){
                    nombreTextField.setText((String) table1.getValueAt(selectFila, 0));
                    contactoTextField.setText((String) table1.getValueAt(selectFila, 1));
                    categoria_productoTextField.setText((String) table1.getValueAt(selectFila, 2));

                    filas = selectFila;
                }
            }
        });
    }



}

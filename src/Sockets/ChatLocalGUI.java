package Sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatLocalGUI {
    private JPanel main;
    private JTextArea textArea1;
    private JButton enviarMensajeButton;
    private JTextField textField1;

    public ChatLocalGUI(){
        textArea1.setEditable(false);
        enviarMensajeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
    }

    public void enviarMensaje(){
        String mensaje = textField1.getText();
        if(mensaje == null || mensaje.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese un mensaje antes de enviarlo.");
        } else {
            textArea1.append("Yo: " + mensaje + "\n");
            textField1.setText("");
        }
    }

    /**
     * Método para simular la recepción de un mensaje.
     * @param mensajeRecibido El mensaje que se ha recibido.
     */
    public void recibirMensaje(String mensajeRecibido) {
        textArea1.append("Otro: " + mensajeRecibido + "\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Local");
        ChatLocalGUI chatLocalGUI = new ChatLocalGUI();
        frame.setContentPane(chatLocalGUI.main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(600, 200);
        frame.setResizable(false);
    }
}

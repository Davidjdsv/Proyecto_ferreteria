package Sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteGUI {
    public JPanel mainPanel;
    private JTextField textField1;
    private JButton enviarButton;
    private JButton conectarButton;
    private JTextArea textArea1;

    // Variables para la comunicación con el servidor
    private PrintWriter out;
    private Socket socket;

    public ClienteGUI(){
            textArea1.setEditable(false);

            // Listener para el botón de conectar
            conectarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Solicitar dirección IP del servidor
                    String serverAddress = JOptionPane.showInputDialog("Ingrese la IP del servidor (localhost si es local):");
                    if (serverAddress == null || serverAddress.isEmpty()) {
                        serverAddress = "localhost";  // Valor por defecto
                    }
                    String finalServerAddress = serverAddress;
                    // Conectar al servidor en un hilo separado para poder varios enviar mensajes
                    new Thread(() -> conectarAlServidor(finalServerAddress)).start();
                }
            });

            // Listener para el botón de enviar mensaje
            enviarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    enviarMensaje();
                }
            });

            // Listener para enviar mensaje al presionar Enter
            textField1.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        enviarMensaje();
                    }
                }
            });
    }

    public void enviarMensaje(){
        String mensaje = textField1.getText();
        if (mensaje != null && !mensaje.isEmpty()) {
            if (out != null) {
                out.println(mensaje); // Enviar el mensaje al servidor
                actualizarTextArea("Cliente: " + mensaje + "\n");
                textField1.setText(""); // Limpiar el campo de texto
            } else {
                actualizarTextArea("Error: No estás conectado al servidor.\n");
            }
        }
    }

    public void recibirMensajes(BufferedReader in) {
        try {
            String receivedMessage;
            while ((receivedMessage = in.readLine()) != null) {
                if (receivedMessage.equalsIgnoreCase("salir")) {
                    actualizarTextArea("Servidor ha cerrado la conexión.\n");
                    break;
                }
                actualizarTextArea("Servidor dice: " + receivedMessage + "\n");
            }
        } catch (IOException e) {
            actualizarTextArea("Error al recibir mensajes: " + e.getMessage() + "\n");
        }
    }

    public void conectarAlServidor(String serverAddress) {
        try {
            // Instanciar, Crear socket y conectar al servidor al puerto 12345
            socket = new Socket(serverAddress, 12345);
            actualizarTextArea("Conectado al servidor.\n");
            actualizarTextArea("Bienvenido a nuestro chat apreciado cliente \n, ¿En qué le podemos servir?");

            // Configurar flujos de entrada/salida
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // Auto-flush activado

            // Iniciar hilo para recibir mensajes
            new Thread(() -> recibirMensajes(in)).start();

        } catch (IOException e) {
            actualizarTextArea("Error al conectar al servidor: " + e.getMessage() + "\n");
        }
    }

    private void actualizarTextArea(String mensaje) {
        SwingUtilities.invokeLater(() -> textArea1.append(mensaje));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cliente chat");
        ClienteGUI clienteGUI = new ClienteGUI();
        frame.setContentPane(clienteGUI.mainPanel);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Comentado por alguna razón
        frame.pack();
        frame.setSize(600, 400);
        frame.setVisible(true);
    }


}

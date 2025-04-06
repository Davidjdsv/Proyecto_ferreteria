package Sockets;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Clase Cliente que permite la comunicación con un servidor a través de sockets.
 * Utiliza una interfaz gráfica simple (JOptionPane) para enviar y recibir mensajes.
 *
 * Este cliente se conecta al servidor mediante una dirección IP y un puerto fijo (12345).
 * Una vez establecida la conexión, permite intercambiar mensajes de texto hasta que una de las partes escriba "salir".
 *
 * @author Jhoan David Sinisterra
 */
public class ChatCliente {

    /**
     * Método principal que ejecuta el cliente de chat.
     * Se conecta a un servidor especificado por IP (por defecto, localhost).
     * Utiliza BufferedReader y PrintWriter para el flujo de entrada/salida de datos por el socket.
     *
     */
    public static void main(String[] args) {
        /**
         * Solicita al usuario la dirección IP del servidor.
         * Si no se ingresa ninguna IP, se utiliza "localhost" por defecto.
         */
        String serverAddres = JOptionPane.showInputDialog(null, "Ingrese la dirección IP del servidor (localhost)");
        if (serverAddres == null || serverAddres.isEmpty()) serverAddres = "localhost";

        try (Socket socket = new Socket(serverAddres, 12345)) {
            // Se establecen los flujos de entrada y salida de datos con el servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Lectura de mensajes del servidor (Mensajes que llegan)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Envío de mensajes al servidor

            String sendMessage, recievedMessage;

            // Bucle de comunicación entre cliente y servidor
            do {
                // Solicita al usuario que escriba un mensaje
                sendMessage = JOptionPane.showInputDialog("Cliente, Escribe tu mensaje: ");

                // Si se escribe "salir" o se cancela, se notifica al servidor y se termina el ciclo
                if (sendMessage == null || sendMessage.equalsIgnoreCase("salir")) {
                    out.println("salir");
                    break;
                }

                // Envía el mensaje al servidor
                out.println(sendMessage);
                out.flush();

                // Espera y recibe la respuesta del servidor
                recievedMessage = in.readLine();

                // Si el servidor termina la conexión
                if (recievedMessage == null || recievedMessage.equalsIgnoreCase("salir")) {
                    JOptionPane.showMessageDialog(null, "El servidor ha cerrado su conexión...");
                    break;
                }

                // Muestra la respuesta del servidor
                JOptionPane.showMessageDialog(null, "El servidor dice: " + recievedMessage);
            } while (true);

        } catch (IOException e) {
            // Manejo de excepciones en caso de error en la conexión
            JOptionPane.showMessageDialog(null, "Error en el cliente " + e.getMessage());
        }
    }
}

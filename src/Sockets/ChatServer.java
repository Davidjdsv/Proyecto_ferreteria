package Sockets;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase ChatServer que actúa como servidor para una aplicación de chat sencilla.
 * Permite la comunicación bidireccional con un cliente usando sockets.
 *
 * Utiliza una interfaz gráfica básica (JOptionPane) para mostrar y solicitar mensajes.
 * El servidor escucha en el puerto 12345 por una conexión entrante.
 *
 * El chat se mantiene activo hasta que una de las partes escriba "salir".
 *
 * @author Jhoan David Sinisterra
 */
public class ChatServer {

    /**
     * Método principal que ejecuta el servidor de chat.
     * Espera una conexión entrante, luego inicia un ciclo de envío y recepción de mensajes.
     * Se cierra la conexión si alguna de las partes escribe "salir" o se produce un error.
     *
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            // Mensaje de estado al iniciar el servidor
            JOptionPane.showMessageDialog(null, "Servidor iniciado, esperando conexión...");

            // Acepta la conexión del cliente
            Socket clientSocket = serverSocket.accept();
            JOptionPane.showMessageDialog(null, "Cliente conectado al chat");

            // Se establecen los flujos de entrada y salida de datos con el cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Lectura de mensajes del cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Envío de mensajes al cliente

            String sendMessage, recievedMessage;

            // Bucle principal de comunicación
            do {
                // Lee el mensaje enviado por el cliente
                recievedMessage = in.readLine();
                if (recievedMessage == null || recievedMessage.equalsIgnoreCase("salir")) {
                    JOptionPane.showMessageDialog(null, "El cliente ha abandonado el chat...");
                    break;
                }

                // Muestra el mensaje del cliente
                JOptionPane.showMessageDialog(null, "El cliente dice: " + recievedMessage);

                // Solicita al servidor que escriba una respuesta
                sendMessage = JOptionPane.showInputDialog(null, "Servidor: Escribe tu mensaje: ");

                // Si se escribe "salir", se notifica al cliente y se termina el ciclo
                if (sendMessage == null || sendMessage.equalsIgnoreCase("salir")) {
                    out.println("salir");
                    break;
                }

                // Envía el mensaje al cliente
                out.println(sendMessage);
                out.flush();

            } while (true);

            // Cierre de conexiones
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            // Manejo de errores durante la ejecución del servidor
            JOptionPane.showMessageDialog(null, "Error en el servidor " + e.getMessage());
        }
    }
}

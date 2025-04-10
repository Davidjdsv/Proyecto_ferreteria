package LoginCliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Conexion.ConexionDB;

/**
 * Clase que implementa la interfaz de login para clientes
 * Permite a los clientes acceder al sistema usando su correo y teléfono como credenciales
 */
public class LoginClienteGUI {
    // Componentes de la interfaz gráfica
    public JPanel mainPanel;
    private JTextField txtCorreo;
    private JPasswordField txtTelefono;
    private JButton btnIngresar;
    private JButton btnCancelar;
    private JLabel lblTitulo;
    private JLabel lblCorreo;
    private JLabel lblTelefono;
    private JLabel lblMensaje;
    private JFrame loginFrame;

    /**
     * Constructor de la clase LoginClienteGUI
     */
    public LoginClienteGUI() {
        // Inicializar componentes
        initComponents();

        // Configurar eventos de botones
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar ventana de login
                if (loginFrame != null) {
                    loginFrame.dispose();
                }
            }
        });
    }

    /**
     * Inicializa y configura los componentes de la interfaz gráfica
     */
    private void initComponents() {
        // Crear panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(45, 45, 48)); // Color oscuro para match con tema principal

        // Panel para el formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(45, 45, 48));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        lblTitulo = new JLabel("Login de Cliente", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        formPanel.add(lblTitulo, gbc);

        // Label Correo
        lblCorreo = new JLabel("Correo Electrónico:");
        lblCorreo.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        lblCorreo.setForeground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(lblCorreo, gbc);

        // Campo Correo
        txtCorreo = new JTextField(20);
        txtCorreo.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(txtCorreo, gbc);

        // Label Teléfono
        lblTelefono = new JLabel("Teléfono (Contraseña):");
        lblTelefono.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        lblTelefono.setForeground(new Color(240, 240, 240));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblTelefono, gbc);

        // Campo Teléfono (contraseña)
        txtTelefono = new JPasswordField(20);
        txtTelefono.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(txtTelefono, gbc);

        // Label para mensajes (errores o éxito)
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        lblMensaje.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(lblMensaje, gbc);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(45, 45, 48));

        // Botón Ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(0, 122, 204));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        buttonPanel.add(btnIngresar);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(108, 108, 108));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Agregar todo al panel principal
        mainPanel.add(formPanel, BorderLayout.CENTER);
    }

    // ====== INICIO DE CÓDIGO MODIFICADO ======
    /**
     * Método que se llama cuando el login es exitoso
     * Este método puede ser sobrescrito por subclases
     * @param idCliente ID del cliente que ha hecho login
     * @param nombre Nombre del cliente
     */
    protected void onLoginExitoso(int idCliente, String nombre) {
        // Método que puede ser sobrescrito por subclases
    }

    /**
     * Método para cerrar la ventana desde fuera
     */
    public void cerrarVentana() {
        if (loginFrame != null) {
            loginFrame.dispose();
        }
    }
    // ====== FIN DE CÓDIGO MODIFICADO ======

    /**
     * Válida las credenciales del cliente contra la base de datos
     */
    private void validarLogin() {
        String correo = txtCorreo.getText().trim();
        String telefono = new String(txtTelefono.getPassword()).trim();

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || telefono.isEmpty()) {
            lblMensaje.setText("Por favor complete todos los campos");
            return;
        }

        // Conectar a la base de datos y validar credenciales
        Connection conexion = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexion = ConexionDB.getConnection();
            if (conexion == null) {
                lblMensaje.setText("Error de conexión a la base de datos");
                return;
            }

            // Consulta para validar cliente por correo y teléfono
            String query = "SELECT id_cliente, nombre FROM clientes WHERE correo = ? AND telefono = ?";
            pstmt = conexion.prepareStatement(query);
            pstmt.setString(1, correo);
            pstmt.setString(2, telefono);

            rs = pstmt.executeQuery();

            // ====== INICIO DE CÓDIGO MODIFICADO ======
            if (rs.next()) {
                // Login exitoso - obtener datos del cliente
                int idCliente = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");

                // Mostrar mensaje de bienvenida
                lblMensaje.setForeground(new Color(0, 150, 0));
                lblMensaje.setText("¡Bienvenido " + nombre + "!");

                // Mostrar mensaje
                JOptionPane.showMessageDialog(mainPanel,
                        "Login exitoso. Bienvenido" + nombre,
                        "Acceso concedido",
                        JOptionPane.INFORMATION_MESSAGE);

                // Llamar al método que puede ser sobrescrito por subclases
                onLoginExitoso(idCliente, nombre);

            } else {
                lblMensaje.setForeground(Color.RED);
                lblMensaje.setText("Correo o teléfono incorrectos");
            }
            // ====== FIN DE CÓDIGO MODIFICADO ======

        } catch (SQLException e) {
            lblMensaje.setText("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para mostrar la ventana de login
     */
    public void mostrar() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginFrame = new JFrame("Login Cliente - Ferretería");
                loginFrame.setContentPane(mainPanel);
                loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                loginFrame.setSize(450, 300);
                loginFrame.setLocationRelativeTo(null);
                loginFrame.setVisible(true);
            }
        });
    }

    /**
     * Método para obtener el panel principal
     * @return Panel principal de la interfaz
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Método principal para pruebas
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        try {
            // Configurar Look & Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginClienteGUI loginGUI = new LoginClienteGUI();
                loginGUI.mostrar();
            }
        });
    }
}
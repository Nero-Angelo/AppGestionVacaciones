package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import controllers.AuthController;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import models.User;

/**
 * Vista de autenticación para el sistema.
 * <p>
 * Proporciona: - Campos para usuario y contraseña - Validación básica de
 * credenciales - Redirección a AdminView o EmployeeView según rol - Manejo de
 * errores de autenticación
 * </p>
 *
 * @author Nelo Angelo
 */
public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final transient AuthController authController;

    // Creación una nueva vista de login
    public LoginView() {
        authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema de Vacaciones - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Iniciar sesión");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Username
        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 8, 8, 8)));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setHorizontalAlignment(JTextField.CENTER);

        //Password
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 8, 8, 8)));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setHorizontalAlignment(JTextField.CENTER);

        //LoginButton
        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        loginButton.addActionListener(this::performLogin);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginButton);

        add(panel);
    }

    // Intenta autenticar al usuario con las credenciales proporcionadas.
    private void performLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = authController.authenticate(username, password);

        if (user != null) {
            dispose();
            if (user.isAdmin()) {
                new AdminView(user).setVisible(true);
            } else {
                new EmployeeView(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos",
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}

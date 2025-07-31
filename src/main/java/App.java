import models.Database;
import views.LoginView;

import javax.swing.*;

/**
 ** Clase principal que inicia la aplicación del Sistema de Gestión de Vacaciones.
 * <p>
 * Responsabilidades:
 * - Configurar el Look and Feel del sistema operativo
 * - Inicializar la base de datos
 * - Mostrar la vista de login
 * - Manejar errores críticos de inicialización
 * </p>
 * @author Nelo Angelo
 */

public class App {
    
    /**
     * Punto de entrada principal de la aplicación.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */

    public static void main(String[] args) {
        // Configurar el Look and Feel para que sea consistente en todos los sistemas
        setLookAndFeel();

        // Inicializar la base de datos (crea tablas si no existen)
        initializeDatabase();

        // Mostrar la ventana de login en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

    /**
     * Establece el Look and Feel Nimbus para una apariencia moderna.
     */
    static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Fallback si Nimbus no está disponible
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el LookAndFeel Nimbus: " + e.getMessage());
        }
    }

    /**
     * Inicializa la base de datos y crea las tablas necesarias.
     */
    static void initializeDatabase() {
        try {
            Database.initialize();
            System.out.println("Base de datos inicializada correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Error crítico al inicializar la base de datos:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1); // Salir si no se puede inicializar la BD
        }
    }
}
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import views.LoginView;
import org.junit.jupiter.api.function.Executable;


/**
 *
 * @author Nelo Angelo
 */
class AppTest {
    
    public AppTest() {
    }
    
    @Test
    void testLookAndFeelDoesNotThrow() {
        Executable task = () -> App.setLookAndFeel();
        assertDoesNotThrow(task, "El método setLookAndFeel lanzó una excepción inesperada");
    }

    @Test
    void testDatabaseInitialization() {
        Executable dbInit = () -> App.initializeDatabase();
        assertDoesNotThrow(dbInit, "Error al inicializar la base de datos");
    }

    @Test
    void testLoginViewCreation() {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            assertNotNull(loginView, "LoginView no debe ser nulo");
            assertEquals("Sistema de Vacaciones - Login", loginView.getTitle());
        });
    }
}

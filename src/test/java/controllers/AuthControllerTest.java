package controllers;

import controllers.AuthController;
import models.Database;
import models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

/**
 *
 * @author Nelo Angelo
 */
public class AuthControllerTest {

    private final AuthController controller = new AuthController();
    private User testUser;

    @BeforeAll
    void setup() throws Exception {
        // Elimina usuarios previos para garantizar entorno limpio
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE username LIKE 'junit_%'");
        }
    }

    @Test
    @Order(1)
    void testCreateUser() {
        boolean created = controller.createUser("junit_user", "clave123", true);
        assertTrue(created);
    }

    @Test
    @Order(2)
    void testDuplicateUserFails() {
        boolean created = controller.createUser("junit_user", "clave123", true);
        assertFalse(created);
    }

    @Test
    @Order(3)
    void testAuthenticateSuccess() {
        User user = controller.authenticate("junit_user", "clave123");
        assertNotNull(user);
        assertEquals("junit_user", user.getUsername());
        testUser = user; // Guardamos para pruebas siguientes
    }

    @Test
    @Order(4)
    void testAuthenticateFailWithWrongPassword() {
        User user = controller.authenticate("junit_user", "claveIncorrecta");
        assertNull(user);
    }

    @Test
    @Order(5)
    void testGetAllUsers() {
        List<User> users = controller.getAllUsers();
        assertTrue(users.stream().anyMatch(u -> "junit_user".equals(u.getUsername())));
    }

    @Test
    @Order(6)
    void testUpdateUsername() {
        testUser.setUsername("junit_user_renombrado");
        boolean updated = controller.updateUser(testUser);
        assertTrue(updated);

        User updatedUser = controller.authenticate("junit_user_renombrado", "clave123");
        assertNotNull(updatedUser);
    }

    @Test
    @Order(7)
    void testUpdatePasswordAndLogin() {
        boolean updated = controller.updateUserPassword(testUser.getId(), "claveNueva456");
        assertTrue(updated);

        User oldLogin = controller.authenticate("junit_user_renombrado", "clave123");
        assertNull(oldLogin);

        User newLogin = controller.authenticate("junit_user_renombrado", "claveNueva456");
        assertNotNull(newLogin);
    }

    @Test
    @Order(8)
    void testGetUserById() {
        User userById = controller.getUserById(testUser.getId());
        assertNotNull(userById);
        assertEquals(testUser.getId(), userById.getId());
    }

    @Test
    @Order(9)
    void testDeleteUser() {
        boolean deleted = controller.deleteUser(testUser.getId());
        assertTrue(deleted);

        User login = controller.authenticate("junit_user_renombrado", "claveNueva456");
        assertNull(login);
    }
}

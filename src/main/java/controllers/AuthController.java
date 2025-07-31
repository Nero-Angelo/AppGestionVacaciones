package controllers;

import models.User;
import models.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;


/**
 * * Controlador para operaciones de autenticación y gestión de usuarios.
 * <p>
 * Proporciona funcionalidades para:
 * - Autenticación de usuarios con BCrypt
 * - CRUD de usuarios
 * - Cambio de contraseñas
 * - Verificación de permisos
 * </p>
 * @author Nelo Angelo
 */

/**
 * Controlador para operaciones de autenticación y gestión de usuarios
 */
public class AuthController {
    
    /**
     * Autentica un usuario con nombre de usuario y contraseña
     *  * - Contraseñas hasheadas con BCrypt
     * - Migración automática de contraseñas en texto plano a BCrypt
     * - Validación de credenciales
     * </p>
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Objeto User si las credenciales son válidas, null en caso contrario
     */
    public User authenticate(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password");
                
                // Caso 1: La contraseña está hasheada con BCrypt
                if (isValidBcryptHash(storedHash)) {
                    if (BCrypt.checkpw(password, storedHash)) {
                        return createUserFromResultSet(rs);
                    }
                } 
                // Caso 2: Contraseña en texto plano (migración automática)
                else if (storedHash.equals(password)) {
                    return migratePasswordAndGetUser(conn, rs, password);
                }
                // Caso 3: Contraseña en otro formato no reconocido
                else {
                    System.err.println("Formato de contraseña no reconocido para usuario: " + username);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error durante autenticación: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Crea un nuevo usuario en el sistema con contraseña hasheada.
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param isAdmin Indica si el usuario tendrá privilegios de administrador
     * @return true si se creó correctamente, false si el usuario ya existe
     * @throws IllegalArgumentException si username o password son inválidos
     */
    public boolean createUser(String username, String password, boolean isAdmin) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false;
        }
        
        if (userExists(username)) {
            return false;
        }
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, is_admin) VALUES (?, ?, ?)";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username.trim());
            pstmt.setString(2, hashedPassword);
            pstmt.setBoolean(3, isAdmin);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los usuarios registrados
     * @return Lista de usuarios ordenados por nombre de usuario
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, is_admin FROM users ORDER BY username";
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    "", // No devolver la contraseña por seguridad
                    rs.getBoolean("is_admin")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Elimina un usuario por ID
     * @param userId ID del usuario a eliminar
     * @return true si se eliminó correctamente, false si hubo error
     */
    public boolean deleteUser(int userId) {
        if (isLastAdmin(userId)) {
            System.err.println("No se puede eliminar el último administrador");
            return false;
        }
        
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza los datos de un usuario (sin cambiar contraseña)
     * @param user Objeto User con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean updateUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }
        
        if (usernameExists(user.getUsername(), user.getId())) {
            return false;
        }
        
        if (isLastAdmin(user.getId()) && !user.isAdmin()) {
            return false;
        }
        
        String sql = "UPDATE users SET username = ?, is_admin = ? WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername().trim());
            pstmt.setBoolean(2, user.isAdmin());
            pstmt.setInt(3, user.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza la contraseña de un usuario
     * @param userId ID del usuario
     * @param newPassword Nueva contraseña
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean updateUserPassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene un usuario por su ID
     * @param userId ID del usuario
     * @return Objeto User o null si no se encontró
     */
    public User getUserById(int userId) {
        String sql = "SELECT id, username, is_admin FROM users WHERE id = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    "", // No devolver la contraseña
                    rs.getBoolean("is_admin")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        }
        return null;
    }

    // ==================== MÉTODOS PRIVADOS DE APOYO ====================

    boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de usuario: " + e.getMessage());
            return false;
        }
    }
    
    private boolean usernameExists(String username, int excludeUserId) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND id != ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim());
            pstmt.setInt(2, excludeUserId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error al verificar nombre de usuario: " + e.getMessage());
            return false;
        }
    }
    
    boolean isLastAdmin(int userId) {
        // Primero verificar si el usuario es admin
        String checkAdminSql = "SELECT is_admin FROM users WHERE id = ?";
        boolean isAdmin = false;
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(checkAdminSql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                isAdmin = rs.getBoolean("is_admin");
            }
            
            if (!isAdmin) {
                return false;
            }
            
            // Contar cuántos admins hay
            String countAdminsSql = "SELECT COUNT(*) FROM users WHERE is_admin = TRUE";
            try (Statement stmt = conn.createStatement();
                 ResultSet countRs = stmt.executeQuery(countAdminsSql)) {
                
                if (countRs.next()) {
                    int adminCount = countRs.getInt(1);
                    return adminCount <= 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar último admin: " + e.getMessage());
        }
        return false;
    }
    
    private boolean isValidBcryptHash(String hash) {
        return hash != null && 
               (hash.startsWith("$2a$") || 
                hash.startsWith("$2b$") || 
                hash.startsWith("$2y$"));
    }
    
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getBoolean("is_admin")
        );
    }
    
    private User migratePasswordAndGetUser(Connection conn, ResultSet rs, String password) throws SQLException {
        int userId = rs.getInt("id");
        String newHash = BCrypt.hashpw(password, BCrypt.gensalt());
        
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setString(1, newHash);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
        
        return new User(
            userId,
            rs.getString("username"),
            newHash,
            rs.getBoolean("is_admin")
        );
    }
}
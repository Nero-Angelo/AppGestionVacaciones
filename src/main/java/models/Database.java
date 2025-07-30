package models;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

/**
 *
 * * Maneja la conexión e inicialización de la base de datos SQLite.
 * <p>
 * Responsabilidades:
 * - Configuración de la conexión JDBC
 * - Creación de tablas (empleados y usuarios)
 * - Migración de datos
 * - Validación de estructura
 * </p>
 * @author Nelo Angelo
 */

public class Database {
    
    /**
     * URL de conexión a la base de datos SQLite.
     * <p>
     * La base de datos se almacena en un archivo 'vacation_system.db'
     * en el directorio de ejecución de la aplicación.
     * </p>
     */
    private static final String URL = "jdbc:sqlite:vacation_system.db";

    /**
     * Establece conexión con la base de datos
     * * @return Conexión activa a la base de datos
     * @throws SQLException si ocurre un error al establecer la conexión
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Inicializa la base de datos y crea las tablas necesarias
     * y el usuario admin por defecto si no existen.
     * @throws RuntimeException si ocurre un error crítico durante la inicialización
     */
    public static void initialize() {
        try (Connection conn = connect()) {
            // Crear tablas si no existen
            createTables(conn);
            
            // Verificar y crear usuario admin si no existe
            createDefaultAdmin(conn);
            
            System.out.println("Base de datos inicializada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            throw new RuntimeException("Error crítico al inicializar la base de datos", e);
        }
    }

    /**
     * Crea las tablas necesarias en la base de datos
     */
    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Tabla de usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "is_admin BOOLEAN NOT NULL DEFAULT FALSE)");

            // Tabla de empleados
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "mothers_last_name TEXT," +
                "hire_date TEXT NOT NULL," +
                "birth_date TEXT NOT NULL," +
                "nss TEXT UNIQUE NOT NULL," +
                "curp TEXT UNIQUE NOT NULL," +
                "department TEXT NOT NULL," +
                "monthly_salary REAL)");
        }
    }

    /**
     * Crea el usuario administrador por defecto si no existe
     */
    private static void createDefaultAdmin(Connection conn) throws SQLException {
        // Verificar si ya existe un admin
        String checkSql = "SELECT COUNT(*) FROM users WHERE is_admin = TRUE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.getInt(1) == 0) {
                // Crear usuario admin con contraseña hasheada
                String username = "admin";
                String password = "Admin";
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                
                String insertSql = "INSERT INTO users (username, password, is_admin) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, hashedPassword);
                    pstmt.setBoolean(3, true);
                    pstmt.executeUpdate();
                    
                    System.out.println("Usuario admin creado con contraseña hasheada.");
                }
            }
        }
    }

    /**
     * Método para migrar todas las contraseñas a BCrypt (usar una sola vez)
     */
    public static void migratePasswordsToBCrypt() {
        String selectSql = "SELECT id, password FROM users WHERE password NOT LIKE '$2%'";
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {
            
            conn.setAutoCommit(false); // Iniciar transacción
            
            while (rs.next()) {
                String plainPassword = rs.getString("password");
                int userId = rs.getInt("id");
                
                if (plainPassword != null && !plainPassword.isEmpty()) {
                    String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                        pstmt.setString(1, hashedPassword);
                        pstmt.setInt(2, userId);
                        pstmt.executeUpdate();
                    }
                }
            }
            
            conn.commit(); // Confirmar transacción
            System.out.println("Migración de contraseñas completada exitosamente.");
            
        } catch (SQLException e) {
            System.err.println("Error en migración de contraseñas: " + e.getMessage());
        }
    }

    /**
     * Verifica si la base de datos está correctamente configurada
     */
    public static boolean isDatabaseValid() {
        String checkUsersSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='users'";
        String checkEmployeesSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='employees'";
        
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            
            // Verificar existencia de tablas
            boolean hasUsers = stmt.executeQuery(checkUsersSql).next();
            boolean hasEmployees = stmt.executeQuery(checkEmployeesSql).next();
            
            if (!hasUsers || !hasEmployees) {
                return false;
            }
            
            // Verificar que exista al menos un admin
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE is_admin = TRUE");
            return rs.getInt(1) > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
}
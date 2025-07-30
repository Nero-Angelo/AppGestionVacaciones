package controllers;

import models.Employee;
import models.Database;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para operaciones CRUD de empleados.
 * <p>
 * Gestiona todas las operaciones relacionadas con empleados:
 * - Creación, lectura, actualización y eliminación
 * - Validación de NSS y CURP únicos
 * - Conversión entre objetos Employee y registros de base de datos
 * </p>
 * @author Nelo Angelo
 */

public class EmployeeController {
    // SQL para inserción de nuevos empleados
    private static final String INSERT_SQL = "INSERT INTO employees " +
            "(first_name, last_name, mothers_last_name, hire_date, birth_date, nss, curp, department, monthly_salary) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_SQL = "UPDATE employees SET " +
            "first_name = ?, last_name = ?, mothers_last_name = ?, hire_date = ?, " +
            "birth_date = ?, nss = ?, curp = ?, department = ?, monthly_salary = ? " +
            "WHERE id = ?";
    
    private static final String DELETE_SQL = "DELETE FROM employees WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM employees ORDER BY last_name, first_name";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM employees WHERE id = ?";
    private static final String SELECT_BY_NSS_SQL = "SELECT * FROM employees WHERE nss = ?";
    private static final String SELECT_BY_CURP_SQL = "SELECT * FROM employees WHERE curp = ?";

    /**
     * Agrega un nuevo empleado a la base de datos
     * @param employee El empleado a agregar
     * @return true si se agregó correctamente, false si hubo error
     * * @throws IllegalArgumentException Si el empleado es nulo o tiene datos inválidos
     * @throws RuntimeException Si ocurre un error de base de datos
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }

        // Validar datos únicos
        if (nssExists(employee.getNss())) {
            throw new IllegalArgumentException("El NSS ya está registrado");
        }
        
        if (curpExists(employee.getCurp())) {
            throw new IllegalArgumentException("La CURP ya está registrada");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setEmployeeParameters(pstmt, employee);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar empleado: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al agregar empleado", e);
        }
        return false;
    }

    /**
     * Actualiza un empleado existente
     * @param employee El empleado con los datos actualizados
     * @return true si se actualizó correctamente, false si hubo error
     */
    public boolean updateEmployee(Employee employee) {
        if (employee == null || employee.getId() <= 0) {
            throw new IllegalArgumentException("Empleado inválido para actualización");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setEmployeeParameters(pstmt, employee);
            pstmt.setInt(10, employee.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar empleado: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al actualizar empleado", e);
        }
    }

    /**
     * Elimina un empleado por ID
     * @param id El ID del empleado a eliminar
     * @return true si se eliminó correctamente, false si hubo error
     */
    public boolean deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de empleado inválido");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al eliminar empleado", e);
        }
    }

    /**
     * Obtiene todos los empleados ordenados por apellido y nombre
     * @return Lista de todos los empleados
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al obtener empleados", e);
        }
        return employees;
    }

    /**
     * Busca un empleado por ID
     * @param id El ID del empleado a buscar
     * @return El empleado encontrado o null si no existe
     */
    public Employee getEmployeeById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de empleado inválido");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por ID: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al buscar empleado", e);
        }
    }

    /**
     * Verifica si un NSS ya existe en la base de datos
     * @param nss El NSS a verificar
     * @return true si ya existe, false si no existe
     * @throws IllegalArgumentException Si el NSS está vacío o es nulo
     */
    public boolean nssExists(String nss) {
        if (nss == null || nss.trim().isEmpty()) {
            throw new IllegalArgumentException("NSS no puede estar vacío");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NSS_SQL)) {
            
            pstmt.setString(1, nss.trim());
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar NSS: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al verificar NSS", e);
        }
    }

    /**
     * Verifica si una CURP ya existe en la base de datos
     * @param curp La CURP a verificar
     * @return true si ya existe, false si no existe
     * * @throws IllegalArgumentException Si el CURP está vacío o es nulo
     */
    public boolean curpExists(String curp) {
        if (curp == null || curp.trim().isEmpty()) {
            throw new IllegalArgumentException("CURP no puede estar vacía");
        }

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_CURP_SQL)) {
            
            pstmt.setString(1, curp.trim());
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar CURP: " + e.getMessage());
            throw new RuntimeException("Error de base de datos al verificar CURP", e);
        }
    }

    // ---- Métodos auxiliares ----
    
    private void setEmployeeParameters(PreparedStatement pstmt, Employee employee) throws SQLException {
        pstmt.setString(1, employee.getFirstName());
        pstmt.setString(2, employee.getLastName());
        pstmt.setString(3, employee.getMothersLastName());
        pstmt.setString(4, employee.getHireDate().toString());
        pstmt.setString(5, employee.getBirthDate().toString());
        pstmt.setString(6, employee.getNss());
        pstmt.setString(7, employee.getCurp());
        pstmt.setString(8, employee.getDepartment());
        pstmt.setDouble(9, employee.getMonthlySalary());
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("mothers_last_name"),
            LocalDate.parse(rs.getString("hire_date")),
            LocalDate.parse(rs.getString("birth_date")),
            rs.getString("nss"),
            rs.getString("curp"),
            rs.getString("department"),
            rs.getDouble("monthly_salary")
        );
    }
}
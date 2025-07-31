package controllers;

import models.Employee;
import models.Database;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


/**
 *
 * @author Nelo Angelo
 */
public class EmployeeControllerTest {

    private final EmployeeController controller = new EmployeeController();
    private Employee empleado;

    @BeforeAll
    void limpiarBase() throws Exception {
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM employees WHERE nss LIKE 'TESTNSS_%' OR curp LIKE 'TESTCURP_%'");
        }
    }

    @Test
    @Order(1)
    void testAgregarEmpleado() {
        empleado = new Employee(
            0,
            "Ana", "Ramírez", "Lozano",
            LocalDate.of(2023, 6, 1),
            LocalDate.of(1990, 4, 15),
            "TESTNSS_001",
            "TESTCURP_ABC123",
            "Finanzas",
            28000
        );

        boolean agregado = controller.addEmployee(empleado);
        assertTrue(agregado);
        assertTrue(empleado.getId() > 0);
    }

    @Test
    @Order(2)
    void testObtenerEmpleadoPorId() {
        Employee encontrado = controller.getEmployeeById(empleado.getId());
        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getFirstName());
        assertEquals("TESTNSS_001", encontrado.getNss());
    }

    @Test
    @Order(3)
    void testValidarExistenciaDeNSS() {
        assertTrue(controller.nssExists("TESTNSS_001"));
    }

    @Test
    @Order(4)
    void testValidarExistenciaDeCURP() {
        assertTrue(controller.curpExists("TESTCURP_ABC123"));
    }

    @Test
    @Order(5)
    void testActualizarEmpleado() {
        empleado.setMonthlySalary(30000);
        assertTrue(controller.updateEmployee(empleado));
    }

    @Test
    @Order(6)
    void testObtenerTodosLosEmpleados() {
        List<Employee> lista = controller.getAllEmployees();
        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(e -> "TESTNSS_001".equals(e.getNss())));
    }

    @Test
    @Order(7)
    void testEliminarEmpleado() {
        assertTrue(controller.deleteEmployee(empleado.getId()));
    }

    @Test
    @Order(8)
    void testBuscarEmpleadoYaEliminado() {
        assertNull(controller.getEmployeeById(empleado.getId()));
    }

    @Test
    @Order(9)
    void testAgregarEmpleadoConNSSDuplicadoFalla() {
        Employee primero = new Employee(0, "Luis", "Pérez", "Santos",
            LocalDate.of(2022, 2, 1), LocalDate.of(1985, 10, 10),
            "TESTNSS_002", "TESTCURP_UNICO_001", "TI", 25000);
        assertTrue(controller.addEmployee(primero));

        Employee duplicado = new Employee(0, "Carlos", "Vega", "Martínez",
            LocalDate.of(2023, 5, 1), LocalDate.of(1993, 6, 8),
            "TESTNSS_002", "TESTCURP_UNICO_002", "TI", 26000);
        assertThrows(IllegalArgumentException.class, () -> controller.addEmployee(duplicado));
    }

    @Test
    @Order(10)
    void testAgregarEmpleadoConCURPDuplicadaFalla() {
        Employee duplicado = new Employee(0, "Mario", "Hernández", "Nava",
            LocalDate.of(2024, 1, 1), LocalDate.of(1988, 6, 21),
            "TESTNSS_003", "TESTCURP_UNICO_001", "Legal", 32000);
        assertThrows(IllegalArgumentException.class, () -> controller.addEmployee(duplicado));
    }
}

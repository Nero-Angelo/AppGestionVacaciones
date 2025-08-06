package controllers;

import java.time.LocalDate;
import models.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Nelo Angelo
 */
public class VacationCalculatorTest {
    
    private Employee createTestEmployee(int yearsOfService, double monthlySalary) {
        LocalDate hireDate = LocalDate.now().minusYears(yearsOfService);
        LocalDate birthDate = LocalDate.now().minusYears(30); // Empleado de 30 años
        return new Employee(
            "Juan", 
            "Perez", 
            "Gomez", 
            hireDate, 
            birthDate, 
            "12345678901", 
            "PEGJ800101HDFRNN01", 
            "TI", 
            monthlySalary
        );
    }

    @Test
    public void testCalculateVacationDaysWithNullDate() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> VacationCalculator.calculateVacationDays(null)
        );
        assertEquals("La fecha de ingreso no puede ser nula.", exception.getMessage());
    }

    @Test
    public void testCalculateVacationDaysForDifferentYears() {
        LocalDate today = LocalDate.now();
        
        // Casos de prueba según la ley mexicana
        assertEquals(0, VacationCalculator.calculateVacationDays(today.minusMonths(11))); // Menos de 1 año
        assertEquals(12, VacationCalculator.calculateVacationDays(today.minusYears(1))); // 1 año
        assertEquals(14, VacationCalculator.calculateVacationDays(today.minusYears(2))); // 2 años
        assertEquals(16, VacationCalculator.calculateVacationDays(today.minusYears(3))); // 3 años
        assertEquals(18, VacationCalculator.calculateVacationDays(today.minusYears(4))); // 4 años
        assertEquals(20, VacationCalculator.calculateVacationDays(today.minusYears(5))); // 5 años
        assertEquals(22, VacationCalculator.calculateVacationDays(today.minusYears(10))); // 10 años
        assertEquals(24, VacationCalculator.calculateVacationDays(today.minusYears(12))); // 12 años
        assertEquals(26, VacationCalculator.calculateVacationDays(today.minusYears(16))); // 16 años
        assertEquals(28, VacationCalculator.calculateVacationDays(today.minusYears(22))); // 22 años
        assertEquals(30, VacationCalculator.calculateVacationDays(today.minusYears(28))); // 28 años
        assertEquals(32, VacationCalculator.calculateVacationDays(today.minusYears(35))); // 35 años (máximo)
    }

    @Test
    public void testCalculateWithNullEmployee() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> VacationCalculator.calculate(null, 25.0)
        );
        assertEquals("El empleado no puede ser nulo.", exception.getMessage());
    }

    @Test
    public void testCalculateWithInvalidSalary() {
        Employee employee = createTestEmployee(5, 0);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> VacationCalculator.calculate(employee, 25.0)
        );
        assertEquals("El salario mensual debe ser mayor a cero", exception.getMessage());
    }

    @Test
    public void testCalculateWithInvalidPercentage() {
        Employee employee = createTestEmployee(5, 10000);
        
        assertThrows(
            IllegalArgumentException.class, 
            () -> VacationCalculator.calculate(employee, 24.9)
        );
        
        assertThrows(
            IllegalArgumentException.class, 
            () -> VacationCalculator.calculate(employee, 100.1)
        );
    }

    @Test
    public void testCalculateWithValidInputs() {
        Employee employee = createTestEmployee(5, 15000); // 5 años de servicio, $15,000 mensuales
        double percentage = 25.0;
        
        VacationCalculator.VacationCalculationResult result = 
            VacationCalculator.calculate(employee, percentage);
        
        // Verificaciones básicas
        assertEquals(employee.getFullName(), result.getEmployeeName());
        assertEquals(employee.getHireDate(), result.getHireDate());
        assertEquals(employee.getNss(), result.getNss());
        assertEquals(employee.getDepartment(), result.getDepartment());
        assertEquals(5, result.getYearsWorked());
        
        // Verificaciones de cálculo
        assertEquals(20, result.getVacationDays()); // 5 años = 20 días
        assertEquals(15000 / 30.0, result.getDailySalary(), 0.001); // Salario diario
        assertEquals((15000 / 30.0) * 20, result.getVacationAmount(), 0.001); // Monto vacaciones
        assertEquals(((15000 / 30.0) * 20) * 0.25, result.getVacationPremium(), 0.001); // Prima vacacional
        assertEquals(((15000 / 30.0) * 20) * 1.25, result.getTotal(), 0.001); // Total
        assertEquals(25.0, result.getVacationPercentage(), 0.001); // Porcentaje
    }

    @Test
    public void testCalculateWithDifferentPercentages() {
        Employee employee = createTestEmployee(3, 12000); // 3 años de servicio, $12,000 mensuales
        
        // 25% de prima
        VacationCalculator.VacationCalculationResult result25 = 
            VacationCalculator.calculate(employee, 25.0);
        assertEquals(16, result25.getVacationDays());
        assertEquals(400.0, result25.getDailySalary(), 0.001);
        assertEquals(6400.0, result25.getVacationAmount(), 0.001);
        assertEquals(1600.0, result25.getVacationPremium(), 0.001);
        assertEquals(8000.0, result25.getTotal(), 0.001);
        
        // 50% de prima
        VacationCalculator.VacationCalculationResult result50 = 
            VacationCalculator.calculate(employee, 50.0);
        assertEquals(3200.0, result50.getVacationPremium(), 0.001);
        assertEquals(9600.0, result50.getTotal(), 0.001);
        
        // 100% de prima
        VacationCalculator.VacationCalculationResult result100 = 
            VacationCalculator.calculate(employee, 100.0);
        assertEquals(6400.0, result100.getVacationPremium(), 0.001);
        assertEquals(12800.0, result100.getTotal(), 0.001);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package controllers;

import java.time.LocalDate;
import models.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Nelo Angelo
 */
public class VacationCalculatorTest {
    
    public VacationCalculatorTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of calculateVacationDays method, of class VacationCalculator.
     */
    @Test
    public void testCalculateVacationDays() {
        System.out.println("calculateVacationDays");
        LocalDate hireDate = null;
        int expResult = 0;
        int result = VacationCalculator.calculateVacationDays(hireDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculate method, of class VacationCalculator.
     */
    @Test
    public void testCalculate() {
        System.out.println("calculate");
        Employee employee = null;
        double vacationPercentage = 0.0;
        VacationCalculator.VacationCalculationResult expResult = null;
        VacationCalculator.VacationCalculationResult result = VacationCalculator.calculate(employee, vacationPercentage);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

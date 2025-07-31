/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package models;

import java.time.LocalDate;
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
public class EmployeeTest {
    
    public EmployeeTest() {
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
     * Test of getId method, of class Employee.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Employee instance = null;
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class Employee.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        Employee instance = null;
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFirstName method, of class Employee.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        Employee instance = null;
        String expResult = "";
        String result = instance.getFirstName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFirstName method, of class Employee.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "";
        Employee instance = null;
        instance.setFirstName(firstName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastName method, of class Employee.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");
        Employee instance = null;
        String expResult = "";
        String result = instance.getLastName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLastName method, of class Employee.
     */
    @Test
    public void testSetLastName() {
        System.out.println("setLastName");
        String lastName = "";
        Employee instance = null;
        instance.setLastName(lastName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMothersLastName method, of class Employee.
     */
    @Test
    public void testGetMothersLastName() {
        System.out.println("getMothersLastName");
        Employee instance = null;
        String expResult = "";
        String result = instance.getMothersLastName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMothersLastName method, of class Employee.
     */
    @Test
    public void testSetMothersLastName() {
        System.out.println("setMothersLastName");
        String mothersLastName = "";
        Employee instance = null;
        instance.setMothersLastName(mothersLastName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHireDate method, of class Employee.
     */
    @Test
    public void testGetHireDate() {
        System.out.println("getHireDate");
        Employee instance = null;
        LocalDate expResult = null;
        LocalDate result = instance.getHireDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHireDate method, of class Employee.
     */
    @Test
    public void testSetHireDate() {
        System.out.println("setHireDate");
        LocalDate hireDate = null;
        Employee instance = null;
        instance.setHireDate(hireDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBirthDate method, of class Employee.
     */
    @Test
    public void testGetBirthDate() {
        System.out.println("getBirthDate");
        Employee instance = null;
        LocalDate expResult = null;
        LocalDate result = instance.getBirthDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBirthDate method, of class Employee.
     */
    @Test
    public void testSetBirthDate() {
        System.out.println("setBirthDate");
        LocalDate birthDate = null;
        Employee instance = null;
        instance.setBirthDate(birthDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNss method, of class Employee.
     */
    @Test
    public void testGetNss() {
        System.out.println("getNss");
        Employee instance = null;
        String expResult = "";
        String result = instance.getNss();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNss method, of class Employee.
     */
    @Test
    public void testSetNss() {
        System.out.println("setNss");
        String nss = "";
        Employee instance = null;
        instance.setNss(nss);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurp method, of class Employee.
     */
    @Test
    public void testGetCurp() {
        System.out.println("getCurp");
        Employee instance = null;
        String expResult = "";
        String result = instance.getCurp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCurp method, of class Employee.
     */
    @Test
    public void testSetCurp() {
        System.out.println("setCurp");
        String curp = "";
        Employee instance = null;
        instance.setCurp(curp);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepartment method, of class Employee.
     */
    @Test
    public void testGetDepartment() {
        System.out.println("getDepartment");
        Employee instance = null;
        String expResult = "";
        String result = instance.getDepartment();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDepartment method, of class Employee.
     */
    @Test
    public void testSetDepartment() {
        System.out.println("setDepartment");
        String department = "";
        Employee instance = null;
        instance.setDepartment(department);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMonthlySalary method, of class Employee.
     */
    @Test
    public void testGetMonthlySalary() {
        System.out.println("getMonthlySalary");
        Employee instance = null;
        double expResult = 0.0;
        double result = instance.getMonthlySalary();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMonthlySalary method, of class Employee.
     */
    @Test
    public void testSetMonthlySalary() {
        System.out.println("setMonthlySalary");
        double monthlySalary = 0.0;
        Employee instance = null;
        instance.setMonthlySalary(monthlySalary);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFullName method, of class Employee.
     */
    @Test
    public void testGetFullName() {
        System.out.println("getFullName");
        Employee instance = null;
        String expResult = "";
        String result = instance.getFullName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getYearsWorked method, of class Employee.
     */
    @Test
    public void testGetYearsWorked() {
        System.out.println("getYearsWorked");
        Employee instance = null;
        int expResult = 0;
        int result = instance.getYearsWorked();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Employee.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Employee instance = null;
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Employee.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Employee instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Employee.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Employee instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

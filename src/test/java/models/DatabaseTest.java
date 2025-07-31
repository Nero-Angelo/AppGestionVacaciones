/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package models;

import java.sql.Connection;
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
public class DatabaseTest {
    
    public DatabaseTest() {
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
     * Test of connect method, of class Database.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        Connection expResult = null;
        Connection result = Database.connect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class Database.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        Database.initialize();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of migratePasswordsToBCrypt method, of class Database.
     */
    @Test
    public void testMigratePasswordsToBCrypt() {
        System.out.println("migratePasswordsToBCrypt");
        Database.migratePasswordsToBCrypt();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDatabaseValid method, of class Database.
     */
    @Test
    public void testIsDatabaseValid() {
        System.out.println("isDatabaseValid");
        boolean expResult = false;
        boolean result = Database.isDatabaseValid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

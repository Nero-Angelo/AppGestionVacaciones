package models;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * * Representa a un empleado de la empresa con toda su información personal,
 * laboral y datos requeridos por la ley mexicana.
 * <p>
 * Incluye validaciones para campos obligatorios como NSS, CURP y fechas,
 * asegurando que cumplan con los formatos y requisitos legales.
 * </p>
 * @author Nelo Angelo
 */

/**
 * Clase que representa a un empleado de la empresa.
 */

public class Employee {
    
    private int id;
    private String firstName;
    private String lastName;
    private String mothersLastName;
    private LocalDate hireDate;
    private LocalDate birthDate;
    private String nss;  // Número de Seguridad Social
    private String curp; // Clave Única de Registro de Población
    private String department;
    private double monthlySalary;

    // Constructor
    public Employee(int id, String firstName, String lastName, String mothersLastName, LocalDate hireDate, LocalDate birthDate, String nss, String curp, String department, double monthlySalary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mothersLastName = mothersLastName;
        this.hireDate = hireDate;
        this.birthDate = birthDate;
        this.nss = nss;
        this.curp = curp;
        this.department = department;
        this.monthlySalary = monthlySalary;
    }

    // Constructor sin ID (para inserciones en BD)
    public Employee(String firstName, String lastName, String mothersLastName, LocalDate hireDate, LocalDate birthDate, String nss, String curp, String department, double monthlySalary) {
        this(0, firstName, lastName, mothersLastName, hireDate, birthDate,
             nss, curp, department, monthlySalary);
    }

    public Employee() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido paterno no puede estar vacío");
        }
        this.lastName = lastName.trim();
    }

    public String getMothersLastName() {
        return mothersLastName;
    }

    public void setMothersLastName(String mothersLastName) {
        // El apellido materno puede ser nulo (opcional)
        this.mothersLastName = mothersLastName != null ? mothersLastName.trim() : null;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("La fecha de ingreso es requerida");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
        }
        this.hireDate = hireDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es requerida");
        }
        if (birthDate.isAfter(LocalDate.now().minusYears(14))) {
            throw new IllegalArgumentException("El empleado debe tener al menos 14 años");
        }
        this.birthDate = birthDate;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        if (nss == null || nss.trim().isEmpty()) {
            throw new IllegalArgumentException("El NSS no puede estar vacío");
        }
        // Validación básica de formato (11 dígitos)
        if (!nss.matches("\\d{11}")) {
            throw new IllegalArgumentException("El NSS debe contener 11 dígitos");
        }
        this.nss = nss.trim();
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        if (curp == null || curp.trim().isEmpty()) {
            throw new IllegalArgumentException("La CURP no puede estar vacía");
        }
        // Validación básica de formato (18 caracteres alfanuméricos)
        if (!curp.matches("[A-Z0-9]{18}")) {
            throw new IllegalArgumentException("Formato de CURP inválido");
        }
        this.curp = curp.trim().toUpperCase();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("El departamento no puede estar vacío");
        }
        this.department = department.trim();
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        if (monthlySalary <= 0) {
            throw new IllegalArgumentException("El salario debe ser mayor a cero");
        }
        this.monthlySalary = monthlySalary;
    }

    // Métodos adicionales
    
        /**
     * Calcula el nombre completo del empleado concatenando:
     * - Primer nombre
     * - Apellido paterno
     * - Apellido materno (si existe)
     * 
     * @return Cadena con el nombre completo formateado
     */
    
    public String getFullName() {
        return firstName + " " + lastName + 
               (mothersLastName != null ? " " + mothersLastName : "");
    }

        /**
     * Calcula los años trabajados por el empleado basado en su fecha de ingreso.
     * 
     * @return Número de años completos desde la fecha de contratación hasta hoy
     * @throws IllegalStateException si la fecha de ingreso es futura
     */
    
    public int getYearsWorked() {
        return Period.between(hireDate, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(nss, employee.nss) &&
               Objects.equals(curp, employee.curp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nss, curp);
    }

    @Override
    public String toString() {
        return "Employee{" +
               "id=" + id +
               ", fullName='" + getFullName() + '\'' +
               ", hireDate=" + hireDate +
               ", department='" + department + '\'' +
               '}';
    }
}
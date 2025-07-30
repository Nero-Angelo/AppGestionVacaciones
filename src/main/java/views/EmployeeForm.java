package views;

import controllers.EmployeeController;
import models.Employee;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Formulario para captura y edición de información de empleados.
 * <p>
 * Proporciona:
 * - Campos para todos los datos requeridos de un empleado
 * - Validación en tiempo real de formatos
 * - Soporte para modo creación y edición
 * - Conversión de formatos de fecha (dd/MM/yyyy)
 * </p>
 * @author Nelo Angelo
 */

public class EmployeeForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private final transient Employee employee;
    private final transient EmployeeController controller;
    private final boolean isEditMode;

    // Componentes del formulario
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField mothersLastNameField;
    private JTextField hireDateField;
    private JTextField birthDateField;
    private JTextField nssField;
    private JTextField curpField;
    private JComboBox<String> departmentCombo;
    private JTextField salaryField;
    private JButton saveButton;

    public EmployeeForm(Employee employee) {
        this.employee = employee;
        this.isEditMode = (employee != null);
        this.controller = new EmployeeController();
        
        initializeUI();
        setTitle(isEditMode ? "Editar Empleado" : "Nuevo Empleado");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        addFormField(mainPanel, gbc, 0, "Nombre:", firstNameField = new JTextField(20));
        addFormField(mainPanel, gbc, 1, "Apellido Paterno:", lastNameField = new JTextField(20));
        addFormField(mainPanel, gbc, 2, "Apellido Materno:", mothersLastNameField = new JTextField(20));
        addFormField(mainPanel, gbc, 3, "Fecha de Ingreso (dd/mm/aaaa):", hireDateField = new JTextField(20));
        addFormField(mainPanel, gbc, 4, "Fecha de Nacimiento (dd/mm/aaaa):", birthDateField = new JTextField(20));
        addFormField(mainPanel, gbc, 5, "NSS (11 dígitos):", nssField = new JTextField(20));
        addFormField(mainPanel, gbc, 6, "CURP (18 caracteres):", curpField = new JTextField(20));

        // Combo departamento
        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(new JLabel("Departamento:"), gbc);
        
        gbc.gridx = 1;
        String[] departments = {"Administración", "TI", "Glosa", "Clasificación", "RH", "Contabilidad", "Tráfico", "Fianzas", "Ejecutivo de Cuenta", "Tramitación"};
        departmentCombo = new JComboBox<>(departments);
        mainPanel.add(departmentCombo, gbc);

        // Campo salario
        addFormField(mainPanel, gbc, 8, "Salario Mensual:", salaryField = new JTextField(20));

        // Botón Guardar
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        saveButton = new JButton(isEditMode ? "Actualizar" : "Guardar");
        saveButton.addActionListener(this::saveEmployee);
        mainPanel.add(saveButton, gbc);

        // Cargar datos si está en modo edición
        if (isEditMode) {
            loadEmployeeData();
        }

        add(mainPanel);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadEmployeeData() {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        mothersLastNameField.setText(employee.getMothersLastName());
        hireDateField.setText(employee.getHireDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        birthDateField.setText(employee.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        nssField.setText(employee.getNss());
        curpField.setText(employee.getCurp());
        departmentCombo.setSelectedItem(employee.getDepartment());
        salaryField.setText(String.format("%.2f", employee.getMonthlySalary()));
    }

    private void saveEmployee(ActionEvent e) {
        try {
            Employee emp;
            if (isEditMode) {
                emp = employee;
            } else {
                // Crear nuevo empleado con valores temporales que serán reemplazados
                emp = new Employee(
                    "", // firstName
                    "", // lastName
                    null, // mothersLastName
                    LocalDate.now(), // hireDate (temporal)
                    LocalDate.now().minusYears(25), // birthDate (temporal)
                    "", // nss
                    "", // curp
                    "Ventas", // department (default)
                    0.0 // salary (temporal)
                );
            }
            
            // Validar y asignar valores
            emp.setFirstName(validateTextField(firstNameField, "Nombre"));
            emp.setLastName(validateTextField(lastNameField, "Apellido Paterno"));
            emp.setMothersLastName(mothersLastNameField.getText().trim()); // Opcional
            
            emp.setHireDate(parseDate(hireDateField.getText(), "Fecha de Ingreso"));
            emp.setBirthDate(parseDate(birthDateField.getText(), "Fecha de Nacimiento"));
            
            validateAge(emp.getBirthDate());
            
            emp.setNss(validateNSS(nssField.getText()));
            emp.setCurp(validateCURP(curpField.getText()));
            
            emp.setDepartment((String) departmentCombo.getSelectedItem());
            emp.setMonthlySalary(validateSalary(salaryField.getText()));
            
            // Guardar en BD
            boolean success = isEditMode ? 
                controller.updateEmployee(emp) : 
                controller.addEmployee(emp);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Empleado " + (isEditMode ? "actualizado" : "registrado") + " correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Cerrar formulario
            } else {
                throw new Exception("Error al guardar en la base de datos");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos de validación ---
    
    private String validateTextField(JTextField field, String fieldName) throws Exception {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new Exception(fieldName + " es requerido");
        }
        return value;
    }
    
    private LocalDate parseDate(String dateStr, String fieldName) throws Exception {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new Exception(fieldName + " debe tener formato dd/mm/aaaa");
        }
    }
    
    private void validateAge(LocalDate birthDate) throws Exception {
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new Exception("El empleado debe ser mayor de 18 años");
        }
    }
    
    private String validateNSS(String nss) throws Exception {
        String cleaned = nss.replaceAll("[^0-9]", "");
        if (cleaned.length() != 11) {
            throw new Exception("El NSS debe contener 11 dígitos");
        }
        return cleaned;
    }
    
    private String validateCURP(String curp) throws Exception {
        String cleaned = curp.trim().toUpperCase();
        if (!cleaned.matches("[A-Z0-9]{18}")) {
            throw new Exception("La CURP debe tener 18 caracteres alfanuméricos");
        }
        return cleaned;
    }
    
    private double validateSalary(String salaryStr) throws Exception {
        try {
            double salary = Double.parseDouble(salaryStr.replace(",", ""));
            if (salary <= 0) {
                throw new Exception("El salario debe ser mayor a cero");
            }
            return salary;
        } catch (NumberFormatException e) {
            throw new Exception("Salario debe ser un número válido");
        }
    }
}
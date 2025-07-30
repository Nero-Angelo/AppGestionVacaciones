package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import models.Employee;
import controllers.VacationCalculator;

/**
 * Vista especializada para cálculo detallado de vacaciones.
 * <p>
 * Muestra:
 * - Información completa del empleado
 * - Configuración de porcentaje de prima
 * - Resultados detallados del cálculo
 * - Desglose de montos
 * </p>
 * @author Nelo Angelo
 */

public class VacationCalculatorView extends JFrame {

    private static final long serialVersionUID = 1L;
    private final transient Employee employee;
    
    //Crea una nueva calculadora de vacaciones.
    public VacationCalculatorView(Employee employee) {
        this.employee = employee;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Calculadora de Vacaciones - " + employee.getFullName());
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Mostrar información del empleado
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Empleado:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getFullName()), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Fecha de Ingreso:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getHireDate().toString()), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("NSS:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getNss()), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Departamento:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(employee.getDepartment()), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Salario Mensual:"), gbc);
        
        gbc.gridx = 1;
        panel.add(new JLabel(String.format("$%,.2f", employee.getMonthlySalary())), gbc);
        
        // Porcentaje de prima vacacional
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Porcentaje de Prima Vacacional (%):"), gbc);
        
        gbc.gridx = 1;
        JSpinner percentageSpinner = new JSpinner(new SpinnerNumberModel(25.0, 25.0, 100.0, 1.0));
        panel.add(percentageSpinner, gbc);
        
        // Botón de cálculo
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton calculateButton = new JButton("Calcular Vacaciones");
        calculateButton.addActionListener(e -> calculateVacations(percentageSpinner));
        panel.add(calculateButton, gbc);
        
        add(panel);
    }
    
    // Ejecuta el cálculo y muestra los resultados.
    private void calculateVacations(JSpinner percentageSpinner) {
        try {
            // Obtener el valor como Number y convertirlo a double
            double percentage = ((Number)percentageSpinner.getValue()).doubleValue();
            
            VacationCalculator.VacationCalculationResult result = 
                VacationCalculator.calculate(employee, percentage);
            
            // Mostrar resultados en un diálogo
            String message = String.format(
                "<html><body style='font-family: Arial;'>" +
                "<h2 style='color: #2c3e50;'>Resultados del Cálculo</h2>" +
                "<table>" +
                "<tr><td><b>Empleado:</b></td><td>%s</td></tr>" +
                "<tr><td><b>Fecha de Ingreso:</b></td><td>%s</td></tr>" +
                "<tr><td><b>Tiempo Laborado:</b></td><td>%d años</td></tr>" +
                "<tr><td><b>NSS:</b></td><td>%s</td></tr>" +
                "<tr><td><b>Departamento:</b></td><td>%s</td></tr>" +
                "<tr><td><b>Salario Diario:</b></td><td>$%,.2f</td></tr>" +
                "<tr><td><b>Días de Vacaciones:</b></td><td>%d días</td></tr>" +
                "<tr><td><b>Porcentaje Prima Vacacional:</b></td><td>%.0f%%</td></tr>" +
                "<tr><td><b>Monto por Vacaciones:</b></td><td>$%,.2f</td></tr>" +
                "<tr><td><b>Prima Vacacional:</b></td><td>$%,.2f</td></tr>" +
                "<tr><td><b>Total a Percibir:</b></td><td>$%,.2f</td></tr>" +
                "</table></body></html>",
                result.getEmployeeName(),
                result.getHireDate(),
                result.getYearsWorked(),
                //result.getDaysWorked(),
                result.getNss(),
                result.getDepartment(),
                result.getDailySalary(),
                result.getVacationDays(),
                result.getVacationPercentage(),
                result.getVacationAmount(),
                result.getVacationPremium(),
                result.getTotal()
            );
            
            JOptionPane.showMessageDialog(this, message, "Resultados del Cálculo", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al calcular las vacaciones: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
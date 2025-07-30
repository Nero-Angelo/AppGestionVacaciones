package views;

import controllers.EmployeeController;
import controllers.VacationCalculator;
import models.Employee;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Vista principal para usuarios con rol de empleado.
 * <p>
 * Permite: - Visualización de lista de empleados - Cálculo de días de
 * vacaciones - Configuración de porcentaje de prima vacacional - Visualización
 * de resultados detallados
 * </p>
 *
 * @author Nelo Angelo
 */
public class EmployeeView extends JFrame {

    private static final long serialVersionUID = 1L;
    private final transient User currentUser;
    private final transient EmployeeController employeeController;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JSpinner percentageSpinner;

    public EmployeeView(User user) {
        this.currentUser = user;
        this.employeeController = new EmployeeController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema de Vacaciones - Empleado");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Barra de estado
        JLabel statusBar = new JLabel("Usuario: " + currentUser.getUsername() + " | Rol: Empleado");
        mainPanel.add(statusBar, BorderLayout.NORTH);

        // Configurar modelo de tabla
        String[] columnNames = {"Nombre", "Departamento", "Fecha Ingreso", "Salario Mensual"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Double.class : String.class;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de controles
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Selector de porcentaje (usando Double para evitar problemas de casteo)
        percentageSpinner = new JSpinner(new SpinnerNumberModel(25.0, 25.0, 100.0, 1.0));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(percentageSpinner, "#'%'");
        percentageSpinner.setEditor(editor);

        JButton calculateButton = new JButton("Calcular Vacaciones");
        calculateButton.addActionListener(this::calculateVacation);

        controlPanel.add(new JLabel("Prima Vacacional:"));
        controlPanel.add(percentageSpinner);
        controlPanel.add(calculateButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Cargar datos
        loadEmployeeData();

        add(mainPanel);
    }

    private void loadEmployeeData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<Employee> employees = employeeController.getAllEmployees();
                    tableModel.setRowCount(0); // Limpiar tabla

                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    for (Employee emp : employees) {
                        Object[] row = {
                            emp.getFullName(),
                            emp.getDepartment(),
                            emp.getHireDate().format(dateFormat),
                            emp.getMonthlySalary() // Se formatea en getColumnClass
                        };
                        tableModel.addRow(row);
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(EmployeeView.this,
                                    "Error al cargar empleados: " + e.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
        };
        worker.execute();
    }

    /**
     * Calcula las vacaciones para el empleado seleccionado.
     *
     * @param e Evento de acción (no utilizado)
     * @throws IllegalStateException Si no hay empleado seleccionado
     */
    private void calculateVacation(ActionEvent e) {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado de la tabla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Obtener datos de la fila seleccionada
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            String department = (String) tableModel.getValueAt(selectedRow, 1);
            String hireDateStr = (String) tableModel.getValueAt(selectedRow, 2);
            double salary = (Double) tableModel.getValueAt(selectedRow, 3);

            // Crear empleado temporal para el cálculo
            Employee tempEmployee = new Employee(
                    name.split(" ")[0], // nombre
                    name.split(" ").length > 1 ? name.split(" ")[1] : "", // apellido
                    null, // apellido materno (opcional)
                    LocalDate.parse(hireDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalDate.now().minusYears(25), // fecha nacimiento estimada
                    "00000000000", // NSS temporal
                    "TEMP00000000000000", // CURP temporal
                    department,
                    salary
            );

            // Obtener porcentaje
            double percentage = ((Number) percentageSpinner.getValue()).doubleValue();

            // Calcular vacaciones
            VacationCalculator.VacationCalculationResult result
                    = VacationCalculator.calculate(tempEmployee, percentage);

            // Mostrar resultados
            showCalculationResult(result);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al calcular vacaciones: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showCalculationResult(VacationCalculator.VacationCalculationResult result) {
        String message = String.format("""
            <html>
            <body style='font-family: Arial; width: 350px'>
                <h2 style='color: #2c3e50; margin-bottom: 10px;'>Resultados para %s</h2>
                <table cellspacing='5' style='width: 100%%'>
                    <tr><td><b>Departamento:</b></td><td>%s</td></tr>
                    <tr><td><b>Antigüedad:</b></td><td>%d años</td></tr>
                    <tr><td><b>Días de vacaciones:</b></td><td>%d días</td></tr>
                    <tr><td><b>Salario diario:</b></td><td>$%,.2f</td></tr>
                    <tr><td><b>Prima (%d%%):</b></td><td>$%,.2f</td></tr>
                    <tr><td><b>Total a recibir:</b></td><td style='font-weight: bold; color: #27ae60;'>$%,.2f</td></tr>
                </table>
            </body>
            </html>""",
                result.getEmployeeName(),
                result.getDepartment(),
                result.getYearsWorked(),
                result.getVacationDays(),
                result.getDailySalary(),
                (int) result.getVacationPercentage(),
                result.getVacationPremium(),
                result.getTotal()
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Resultados del Cálculo",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

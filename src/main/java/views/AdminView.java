package views;

import controllers.AuthController;
import controllers.EmployeeController;
import models.Employee;
import models.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Vista principal para administradores del sistema.
 * <p>
 * Proporciona funcionalidades completas de administración:
 * - Gestión CRUD de empleados
 * - Gestión CRUD de usuarios
 * - Cálculo de vacaciones
 * - Cambio de contraseñas
 * </p>
 * @author Nelo Angelo
 */

public class AdminView extends JFrame {

    private static final long serialVersionUID = 1L;
    private final transient User currentUser;
    private JTable employeeTable;
    private JTable userTable;
    private DefaultTableModel employeeTableModel;
    private DefaultTableModel userTableModel;
    private final transient EmployeeController employeeController;
    private final transient AuthController authController;
    private JTabbedPane tabbedPane;

    // Crea una nueva vista de administrador.
    public AdminView(User user) {
        this.currentUser = user;
        this.employeeController = new EmployeeController();
        this.authController = new AuthController();
        initializeUI();
        loadEmployees();
        loadUsers();
    }

    private void initializeUI() {
        setTitle("Sistema de Vacaciones - Administrador");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar menú superior
        setupMenuBar();

        // Panel principal con pestañas
        tabbedPane = new JTabbedPane();

        // Pestaña de Empleados
        JPanel employeePanel = createEmployeePanel();
        tabbedPane.addTab("Empleados", employeePanel);

        // Pestaña de Usuarios
        JPanel userPanel = createUserPanel();
        tabbedPane.addTab("Usuarios", userPanel);

        // Barra de estado
        JLabel statusBar = new JLabel("Usuario: " + currentUser.getUsername() + " | Rol: Administrador");

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Empleados
        JMenu employeesMenu = new JMenu("Empleados");
        JMenuItem addEmployeeItem = new JMenuItem("Agregar Empleado");
        JMenuItem editEmployeeItem = new JMenuItem("Editar Empleado");
        JMenuItem deleteEmployeeItem = new JMenuItem("Eliminar Empleado");
        JMenuItem calculateVacationItem = new JMenuItem("Calcular Vacaciones");

        addEmployeeItem.addActionListener(e -> openEmployeeForm(null));
        editEmployeeItem.addActionListener(e -> editSelectedEmployee());
        deleteEmployeeItem.addActionListener(e -> deleteSelectedEmployee());
        calculateVacationItem.addActionListener(e -> calculateVacationForSelected());

        employeesMenu.add(addEmployeeItem);
        employeesMenu.add(editEmployeeItem);
        employeesMenu.add(deleteEmployeeItem);
        employeesMenu.addSeparator();
        employeesMenu.add(calculateVacationItem);

        // Menú Usuarios
        JMenu usersMenu = new JMenu("Usuarios");
        JMenuItem addUserItem = new JMenuItem("Agregar Usuario");
        JMenuItem refreshUsersItem = new JMenuItem("Actualizar Lista");

        addUserItem.addActionListener(e -> showAddUserDialog());
        refreshUsersItem.addActionListener(e -> loadUsers());

        usersMenu.add(addUserItem);
        usersMenu.add(refreshUsersItem);

        // Menú Salir
        JMenu exitMenu = new JMenu("Salir");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        JMenuItem exitItem = new JMenuItem("Salir del Sistema");

        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));

        exitMenu.add(logoutItem);
        exitMenu.add(exitItem);

        menuBar.add(employeesMenu);
        menuBar.add(usersMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);
    }

    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Configurar tabla de empleados
        String[] employeeColumns = {"ID", "Nombre", "Fecha Ingreso", "NSS", "Departamento", "Salario"};
        employeeTableModel = new DefaultTableModel(employeeColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Double.class;
                }
                return String.class;
            }
        };

        employeeTable = new JTable(employeeTableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setAutoCreateRowSorter(true);

        // Configurar renderer para el salario
        employeeTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    setText(String.format("$%,.2f", value));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para empleados
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton calculateButton = new JButton("Calcular Vacaciones");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> openEmployeeForm(null));
        editButton.addActionListener(e -> editSelectedEmployee());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        calculateButton.addActionListener(e -> calculateVacationForSelected());
        refreshButton.addActionListener(e -> loadEmployees());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Configurar tabla de usuarios
        String[] userColumns = {"ID", "Usuario", "Rol", "Acciones"};
        userTableModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna de acciones es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        userTable = new JTable(userTableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setAutoCreateRowSorter(true);

        // Configurar renderer para el rol
        userTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText((Boolean) value ? "Administrador" : "Usuario");
                return this;
            }
        });

        // Configurar columna de acciones
        userTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        userTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones para usuarios
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Usuario");
        JButton editButton = new JButton("Editar Usuario");
        JButton deleteButton = new JButton("Eliminar Usuario");
        JButton refreshButton = new JButton("Actualizar");
        JButton changePassButton = new JButton("Cambiar Contraseña");

        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> editSelectedUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsers());
        changePassButton.addActionListener(e -> changePasswordForSelectedUser());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(changePassButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadEmployees() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<Employee> employees = employeeController.getAllEmployees();
                    employeeTableModel.setRowCount(0);

                    for (Employee emp : employees) {
                        Object[] row = {
                            emp.getId(),
                            emp.getFullName(),
                            emp.getHireDate(),
                            emp.getNss(),
                            emp.getDepartment(),
                            emp.getMonthlySalary()
                        };
                        employeeTableModel.addRow(row);
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(AdminView.this,
                                    "Error al cargar empleados: " + e.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
        };
        worker.execute();
    }

    private void loadUsers() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<User> users = authController.getAllUsers();
                    userTableModel.setRowCount(0);

                    for (User user : users) {
                        Object[] row = {
                            user.getId(),
                            user.getUsername(),
                            user.isAdmin(),
                            "Editar/Eliminar"
                        };
                        userTableModel.addRow(row);
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(AdminView.this,
                                    "Error al cargar usuarios: " + e.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
        };
        worker.execute();
    }

    private void openEmployeeForm(Employee employee) {
        EmployeeForm form = new EmployeeForm(employee);
        form.setVisible(true);
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadEmployees();
            }
        });
    }

    private void editSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int employeeId = (int) employeeTable.getValueAt(selectedRow, 0);
            Employee employee = employeeController.getEmployeeById(employeeId);
            if (employee != null) {
                openEmployeeForm(employee);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int employeeId = (int) employeeTable.getValueAt(selectedRow, 0);
            String employeeName = (String) employeeTable.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar al empleado " + employeeName + "?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = employeeController.deleteEmployee(employeeId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Empleado eliminado correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployees();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el empleado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void calculateVacationForSelected() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            int employeeId = (int) employeeTable.getValueAt(selectedRow, 0);
            Employee employee = employeeController.getEmployeeById(employeeId);

            if (employee != null) {
                VacationCalculatorView calculator = new VacationCalculatorView(employee);
                calculator.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un empleado para calcular vacaciones.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddUserDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JCheckBox adminCheckbox = new JCheckBox("Es administrador");

        panel.add(new JLabel("Usuario:"));
        panel.add(usernameField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirmar Contraseña:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel(""));
        panel.add(adminCheckbox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Agregar Usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
            boolean isAdmin = adminCheckbox.isSelected();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Usuario y contraseña son requeridos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Las contraseñas no coinciden.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = authController.createUser(username, password, isAdmin);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Usuario creado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al crear el usuario. ¿El nombre ya existe?",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            String username = (String) userTable.getValueAt(selectedRow, 1);
            boolean isAdmin = (Boolean) userTable.getValueAt(selectedRow, 2);

            // No permitir editar al usuario actual
            if (userId == currentUser.getId()) {
                JOptionPane.showMessageDialog(this,
                        "No puede editar su propio usuario aquí. Use el menú de perfil.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField usernameField = new JTextField(username);
            JCheckBox adminCheckbox = new JCheckBox("Es administrador", isAdmin);

            panel.add(new JLabel("Usuario:"));
            panel.add(usernameField);
            panel.add(new JLabel("Rol:"));
            panel.add(adminCheckbox);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuario",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newUsername = usernameField.getText().trim();
                boolean newIsAdmin = adminCheckbox.isSelected();

                if (newUsername.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El nombre de usuario no puede estar vacío.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User(userId, newUsername, "", newIsAdmin);
                boolean success = authController.updateUser(user);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Usuario actualizado exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar el usuario.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            String username = (String) userTable.getValueAt(selectedRow, 1);

            // No permitir eliminar al usuario actual
            if (userId == currentUser.getId()) {
                JOptionPane.showMessageDialog(this,
                        "No puede eliminarse a sí mismo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar al usuario '" + username + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = authController.deleteUser(userId);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Usuario eliminado exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el usuario. ¿Es el último administrador?",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void changePasswordForSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            String username = (String) userTable.getValueAt(selectedRow, 1);

            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            JPasswordField passwordField = new JPasswordField();
            JPasswordField confirmPasswordField = new JPasswordField();

            panel.add(new JLabel("Usuario:"));
            panel.add(new JLabel(username));
            panel.add(new JLabel("Nueva Contraseña:"));
            panel.add(passwordField);
            panel.add(new JLabel("Confirmar Contraseña:"));
            panel.add(confirmPasswordField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Cambiar Contraseña",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "La contraseña no puede estar vacía.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this,
                            "Las contraseñas no coinciden.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = authController.updateUserPassword(userId, password);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Contraseña actualizada exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar la contraseña.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un usuario para cambiar la contraseña.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void logout() {
        this.dispose();
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
    }

    // Clases internas para los botones de acción en la tabla
    class ButtonRenderer extends JButton implements TableCellRenderer {

        private static final long serialVersionUID = 1L;

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private static final long serialVersionUID = 1L;

        private String label;
        private int editingRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            editingRow = row;

            JButton button = new JButton(label);
            button.addActionListener(e -> {
                int modelRow = table.convertRowIndexToModel(editingRow);
                int userId = (int) table.getModel().getValueAt(modelRow, 0);
                String username = (String) table.getModel().getValueAt(modelRow, 1);

                // Mostrar menú de opciones
                JPopupMenu popup = new JPopupMenu();
                JMenuItem editItem = new JMenuItem("Editar");
                JMenuItem deleteItem = new JMenuItem("Eliminar");
                JMenuItem changePassItem = new JMenuItem("Cambiar Contraseña");

                editItem.addActionListener(ev -> {
                    stopCellEditing();
                    editSelectedUser();
                });

                deleteItem.addActionListener(ev -> {
                    stopCellEditing();
                    deleteSelectedUser();
                });

                changePassItem.addActionListener(ev -> {
                    stopCellEditing();
                    changePasswordForSelectedUser();
                });

                popup.add(editItem);
                popup.add(deleteItem);
                popup.add(changePassItem);

                popup.show(button, 0, button.getHeight());
            });

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }
}

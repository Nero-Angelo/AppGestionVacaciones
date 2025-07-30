package controllers;

import models.Employee;
import java.time.LocalDate;
import java.time.Period;

/**
 * Calculadora de días de vacaciones y prima vacacional según la Ley Federal del
 * Trabajo de México.
 * <p>
 * Realiza los cálculos basados en: - Antigüedad del empleado - Salario diario -
 * Porcentaje de prima vacacional (25%-100%)
 * </p>
 *
 * @author Nelo Angelo
 */
public class VacationCalculator {

    /**
     * Calcula los días de vacaciones según los años de antigüedad del empleado.
     * Tabla de referencia según la ley: - 1 año: 12 días - 2 años: 14 días -
     * 3-4 años: +2 días por año - 5-9 años: 20 días - 10-14 años: 22 días - ...
     * hasta máximo 32 días
     * </p>
     *
     * @param hireDate Fecha de ingreso del empleado.
     * @return Días de vacaciones correspondientes.
     * @throws IllegalArgumentException si hireDate es nula o futura
     */
    public static int calculateVacationDays(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula.");
        }

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(hireDate, currentDate);
        int yearsWorked = period.getYears();

        // Tabla de días de vacaciones según antigüedad (Ley Federal del Trabajo, México)
        if (yearsWorked < 1) {
            return 0; // No aplica si no ha cumplido un año
        } else if (yearsWorked == 1) {
            return 12;
        } else if (yearsWorked == 2) {
            return 14;
        } else if (yearsWorked == 3) {
            return 16;
        } else if (yearsWorked == 4) {
            return 18;
        } else if (yearsWorked == 5) {
            return 20;
        } else if (yearsWorked <= 10) {
            return 22;
        } else if (yearsWorked <= 15) {
            return 24;
        } else if (yearsWorked <= 20) {
            return 26;
        } else if (yearsWorked <= 25) {
            return 28;
        } else if (yearsWorked <= 30) {
            return 30;
        } else {
            return 32; // Máximo según la ley
        }
    }

    /**
     * Calcula todos los componentes relacionados con las vacaciones y prima
     * vacacional.
     *
     * @param employee El empleado para el cálculo.
     * @param vacationPercentage Porcentaje de prima vacacional (25% - 100%).
     * @return Un objeto VacationCalculationResult con todos los resultados.
     */
    public static VacationCalculationResult calculate(Employee employee, double vacationPercentage) {
        if (employee == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo.");
        }

        if (employee.getMonthlySalary() <= 0) {
            throw new IllegalArgumentException("El salario mensual debe ser mayor a cero");
        }

        if (vacationPercentage < 25 || vacationPercentage > 100) {
            throw new IllegalArgumentException("El porcentaje de prima vacacional debe estar entre 25% y 100%.");
        }

        VacationCalculationResult result = new VacationCalculationResult();

        // Datos básicos del empleado
        result.setEmployeeName(employee.getFullName());
        result.setHireDate(employee.getHireDate());
        result.setNss(employee.getNss());
        result.setDepartment(employee.getDepartment());

        // Cálculo de tiempo laborado
        Period workPeriod = Period.between(employee.getHireDate(), LocalDate.now());
        result.setYearsWorked(workPeriod.getYears());
        //result.setDaysWorked(workPeriod.getDays());

        // Cálculo de días de vacaciones
        int vacationDays = calculateVacationDays(employee.getHireDate());
        result.setVacationDays(vacationDays);

        // Cálculo del salario diario
        double dailySalary = employee.getMonthlySalary() / 30.0;
        result.setDailySalary(dailySalary);

        // Cálculo del monto por vacaciones (Salario diario × Días de vacaciones)
        double vacationAmount = dailySalary * vacationDays;
        result.setVacationAmount(vacationAmount);

        // Cálculo de la prima vacacional (Monto vacaciones × % prima)
        double vacationPremium = vacationAmount * (vacationPercentage / 100.0);
        result.setVacationPremium(vacationPremium);
        result.setVacationPercentage(vacationPercentage);

        // Total a recibir (Monto vacaciones + Prima vacacional)
        double totalPayment = vacationAmount + vacationPremium;
        result.setTotal(totalPayment);

        return result;
    }

    /**
     * Clase interna para almacenar los resultados del cálculo.
     */
    public static class VacationCalculationResult {

        private String employeeName;
        private LocalDate hireDate;
        private String nss;
        private String department;
        private double dailySalary;
        private int vacationDays;
        private double vacationAmount;
        private double vacationPremium;
        private double vacationPercentage;
        private double total;
        private int yearsWorked;
        private int daysWorked;

        // Getters y Setters
        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public LocalDate getHireDate() {
            return hireDate;
        }

        public void setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
        }

        public String getNss() {
            return nss;
        }

        public void setNss(String nss) {
            this.nss = nss;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public double getDailySalary() {
            return dailySalary;
        }

        public void setDailySalary(double dailySalary) {
            this.dailySalary = dailySalary;
        }

        public int getVacationDays() {
            return vacationDays;
        }

        public void setVacationDays(int vacationDays) {
            this.vacationDays = vacationDays;
        }

        public double getVacationAmount() {
            return vacationAmount;
        }

        public void setVacationAmount(double vacationAmount) {
            this.vacationAmount = vacationAmount;
        }

        public double getVacationPremium() {
            return vacationPremium;
        }

        public void setVacationPremium(double vacationPremium) {
            this.vacationPremium = vacationPremium;
        }

        public double getVacationPercentage() {
            return vacationPercentage;
        }

        public void setVacationPercentage(double vacationPercentage) {
            this.vacationPercentage = vacationPercentage;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public int getYearsWorked() {
            return yearsWorked;
        }

        public void setYearsWorked(int yearsWorked) {
            this.yearsWorked = yearsWorked;
        }

        /*public int getDaysWorked() {
            return daysWorked;
        }
        
        public void setDaysWorked(int daysWorked) {
            this.daysWorked = daysWorked;
        }*/
    }
}

package se.moise.model;

import java.sql.Date;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int roleId;
    private String roleTitle; // Nytt fält för rollnamn
    private String gender;
    private double salary;
    private Date hireDate;

    // Fullständig konstruktor med hireDate och rollTitle
    public Employee(int employeeId, String firstName, String lastName, String email, String password,
                    int roleId, String roleTitle, String gender, double salary, Date hireDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        this.password = password;
        this.roleId = roleId;
        this.roleTitle = roleTitle;
        setGender(gender);
        setSalary(salary);
        this.hireDate = hireDate;
    }

    // Konstruktor utan hireDate men med rollTitle
    public Employee(int employeeId, String firstName, String lastName, String email, String password,
                    int roleId, String roleTitle, String gender, double salary) {
        this(employeeId, firstName, lastName, email, password, roleId, roleTitle, gender, salary, new Date(System.currentTimeMillis()));
    }

    // Konstruktor utan rollTitle (för kompatibilitet)
    public Employee(int employeeId, String firstName, String lastName, String email, String password,
                    int roleId, String gender, double salary, Date hireDate) {
        this(employeeId, firstName, lastName, email, password, roleId, null, gender, salary, hireDate);
    }

    // Konstruktor utan employeeId och hireDate
    public Employee(String firstName, String lastName, String email, String password, int roleId, String gender, double salary) {
        this(0, firstName, lastName, email, password, roleId, null, gender, salary, new Date(System.currentTimeMillis()));
    }

    public Employee() {

    }

    public Employee(int i, String moise, String khonde, String mail, String number, int i1, String man, double v) {
    }

    // Getters och setters
    public int getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getRoleId() { return roleId; }
    public String getRoleTitle() { return roleTitle; }
    public String getGender() { return gender; }
    public double getSalary() { return salary; }
    public Date getHireDate() { return hireDate; }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Förnamn kan inte vara tomt.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Efternamn kan inte vara tomt.");
        }
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Ogiltig e-postadress.");
        }
        this.email = email;
    }

    public void setGender(String gender) {
        if (gender == null || (!gender.equalsIgnoreCase("Man") && !gender.equalsIgnoreCase("Kvinna") && !gender.equalsIgnoreCase("Annat"))) {
            throw new IllegalArgumentException("Ogiltigt kön. Tillåtna värden: Man, Kvinna, Annat.");
        }
        this.gender = gender;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Lön kan inte vara negativ.");
        }
        this.salary = salary;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    @Override
    public String toString() {
        return String.format("%-5d %-15s %-15s %-30s %-10d %-15s %-10s %-10.2f %-10s",
                employeeId, firstName, lastName, email, roleId, roleTitle, gender, salary, hireDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return employeeId == employee.employeeId &&
                email.equals(employee.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}

package se.moise.databases;

import se.moise.model.WorkRole;
import se.moise.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private Connection connection;

    // ================================
    //    ANSLUTNING TILL DATABASEN
    // ================================
    public void connect() throws SQLException {
        String url = "jdbc:hsqldb:hsql://localhost:9002/jdbcclab";
        String user = "SA";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✅ Ansluten till databasen!");
    }

    public void disconnect() {
        try {
            if (connection != null) connection.close();
            System.out.println("✅ Databasanslutningen har stängts.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid stängning av databasanslutningen: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //           ARBETSROLLER
    // ================================
    public void createWorkRole(WorkRole role) {
        String sql = "INSERT INTO Work_role (title, description, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, role.getTitle());
            pstmt.setString(2, role.getDescription());
            pstmt.setDouble(3, role.getSalary());
            pstmt.executeUpdate();
            System.out.println("✅ Arbetsroll tillagd.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid skapande av arbetsroll: " + e.getMessage());
        }
    }

    public List<WorkRole> getAllWorkRoles() {
        List<WorkRole> roles = new ArrayList<>();
        String sql = "SELECT role_id, title, description, salary FROM Work_role";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roles.add(new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid hämtning av arbetsroller: " + e.getMessage());
        }
        return roles;
    }

    public void updateWorkRole(int roleId, String title, String description, double salary) {
        String sql = "UPDATE Work_role SET title = ?, description = ?, salary = ? WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, roleId);
            pstmt.executeUpdate();
            System.out.println("✅ Arbetsroll uppdaterad.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid uppdatering av arbetsroll: " + e.getMessage());
        }
    }

    public void deleteWorkRole(int roleId) {
        String sql = "DELETE FROM Work_role WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roleId);
            pstmt.executeUpdate();
            System.out.println("✅ Arbetsroll raderad.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid radering av arbetsroll: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //           MEDARBETARE
    // ================================
    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (first_name, last_name, email, password, role_id, gender, salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPassword());
            pstmt.setInt(5, employee.getRoleId());
            pstmt.setString(6, employee.getGender());
            pstmt.setDouble(7, employee.getSalary());
            pstmt.executeUpdate();
            System.out.println("✅ Medarbetare tillagd.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid skapande av medarbetare: " + e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, first_name, last_name, email, role_id, gender, salary, hire_date FROM Employee";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        null,                                                    // Lösenord returneras inte
                        rs.getInt("role_id"),
                        null,                                                     // Rolltitel hämtas inte här
                        rs.getString("gender"),
                        rs.getDouble("salary"),
                        rs.getDate("hire_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid hämtning av medarbetare: " + e.getMessage());
        }
        return employees;
    }

    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
            System.out.println("✅ Medarbetare raderad.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid radering av medarbetare: " + e.getMessage());
        }
        return false;
    }

    public Employee getEmployeeByEmailWithRole(String email) {
        String sql = """
            SELECT e.employee_id, e.first_name, e.last_name, e.email, e.gender, 
                   e.salary, e.hire_date, w.title AS role_title, e.role_id
            FROM Employee e
            JOIN Work_role w ON e.role_id = w.role_id
            WHERE e.email = ?
        """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            null,                                                       // Lösenord visas inte
                            rs.getInt("role_id"),
                            rs.getString("role_title"),
                            rs.getString("gender"),
                            rs.getDouble("salary"),
                            rs.getDate("hire_date")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid hämtning av medarbetarprofil: " + e.getMessage());
        }
        return null;
    }

    public void updateEmployeeProfile(String email, String firstName, String lastName, double salary) {
        String sql = "UPDATE Employee SET first_name = ?, last_name = ?, salary = ? WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setDouble(3, salary);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            System.out.println("✅ Profil uppdaterad.");
        } catch (SQLException e) {
            System.err.println("❌ Fel vid uppdatering av profil: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //          STATISTIK
    // ================================
    public Map<String, Integer> getEmployeeCountPerRole() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = """
            SELECT w.title, COUNT(e.employee_id) AS count
            FROM Work_role w
            LEFT JOIN Employee e ON w.role_id = e.role_id
            GROUP BY w.title
        """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("title"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid hämtning av statistik: " + e.getMessage());
        }
        return stats;
    }

    public Map<String, Double> getAverageSalaryPerRole() {
        Map<String, Double> stats = new HashMap<>();
        String sql = """
            SELECT w.title, AVG(e.salary) AS average_salary
            FROM Work_role w
            LEFT JOIN Employee e ON w.role_id = e.role_id
            GROUP BY w.title
        """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("title"), rs.getDouble("average_salary"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid hämtning av genomsnittslön: " + e.getMessage());
        }
        return stats;
    }

    public boolean employeeLogin(String email, String password) {
        String sql = "SELECT * FROM Employee WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email.trim());
            pstmt.setString(2, password.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ Inloggning lyckades för: " + email);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid inloggning: " + e.getMessage());
        }
        System.out.println("❌ Inloggning misslyckades för: " + email);
        return false;
    }

    public void updateEmployee(int employeeId, String newFirstName, String newLastName, double newSalary) {
    }
}

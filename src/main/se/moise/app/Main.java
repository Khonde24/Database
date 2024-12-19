package se.moise.app;

import se.moise.databases.DatabaseManager;
import se.moise.model.Employee;
import se.moise.model.WorkRole;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

public class Main {
    // ================================
    //         GLOBALA VARIABLER
    // ================================
    private static boolean loggedIn = false;                            // Indikerar om användaren är inloggad
    private static String currentEmployeeName = "";                     // Namn på den inloggade användaren

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager dbManager = new DatabaseManager();

        try {
            dbManager.connect();                                                                // Anslut till databasen
            boolean running = true;

//----------------------------------------------------------------------------------------------------------------------

            // ================================
            //          HUVUDLOOP
            // ================================
            while (running) {
                if (!loggedIn) {
                    // ================================
                    //          HUVUDMENY
                    // ================================
                    printHeader("       HUVUDMENY");
                    System.out.println("1. Hantera Arbetsroller (Admin)");
                    System.out.println("2. Hantera Medarbetare (Admin)");
                    System.out.println("3. Logga in som medarbetare");
                    System.out.println("4. Visa statistik och rapporter");
                    System.out.println("5. Avsluta");
                    System.out.println("-------------------------");
                    System.out.print("Välj ett alternativ (1-5): ");

                    int choice = getIntInput(scanner);
                    switch (choice) {
                        case 1 -> handleWorkRoles(scanner, dbManager);                     // Hantering av arbetsroller
                        case 2 -> handleEmployees(scanner, dbManager);                     // Hantering av medarbetare
                        case 3 ->
                                handleEmployeeLogin(scanner, dbManager);                   // Inloggning för medarbetare
                        case 4 -> showStatisticsAndReports(scanner, dbManager);            // Visa statistik
                        case 5 -> {
                            System.out.println("✅ Programmet avslutas...");
                            running = false;                                                // Avsluta huvudloopen
                        }
                        default -> System.out.println("❌ Ogiltigt val! Försök igen.");
                    }
                } else {

//----------------------------------------------------------------------------------------------------------------------

                    // ================================
                    //         MEDARBETARMENY
                    // ================================
                    printHeader("       MEDARBETARMENY");
                    System.out.println("1. Visa din profil");
                    System.out.println("2. Uppdatera din profil");
                    System.out.println("3. Logga ut");
                    System.out.println("-------------------------");
                    System.out.print("Välj ett alternativ (1-3): ");

                    int empChoice = getIntInput(scanner);
                    switch (empChoice) {
                        case 1 -> showEmployeeProfile(dbManager, currentEmployeeName);               // Visa profil
                        case 2 -> updateEmployeeProfile(scanner, dbManager, currentEmployeeName);    // Uppdatera profil
                        case 3 -> {
                            loggedIn = false;                                                       // Logga ut användaren
                            currentEmployeeName = "";
                            System.out.println("✅ Du har loggat ut.");
                        }
                        default -> System.out.println("❌ Ogiltigt val! Försök igen.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Fel vid anslutning till databasen: " + e.getMessage());
        } finally {
            dbManager.disconnect();                                                             // Koppla från databasen
            scanner.close();                                                                    // Stäng scannern
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //    INLOGGNING FÖR MEDARBETARE
    // ================================
    private static void handleEmployeeLogin(Scanner scanner, DatabaseManager dbManager) {
        printHeader("LOGGA IN SOM MEDARBETARE");

        System.out.print("Ange din email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Ange ditt lösenord: ");
        String password = scanner.nextLine().trim();

        if (dbManager.employeeLogin(email, password)) {
            loggedIn = true;                                                    // Flagga för att användaren är inloggad
            currentEmployeeName = email;                                        // Spara inloggad användares email
            System.out.println("✅ Inloggning lyckades! Välkommen, " + email + "!");
        } else {
            System.out.println("❌ Inloggning misslyckades. Kontrollera dina uppgifter.");
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //      HANTERA MEDARBETARE
    // ================================
    private static void handleEmployees(Scanner scanner, DatabaseManager dbManager) {
        boolean running = true;                                                 // Kontroll för att hålla menyn igång

        while (running) {
            printHeader("HANTERA MEDARBETARE");
            System.out.println("1. Lägg till ny medarbetare");
            System.out.println("2. Visa alla medarbetare");
            System.out.println("3. Uppdatera medarbetare");
            System.out.println("4. Radera medarbetare");
            System.out.println("5. Tillbaka till huvudmeny");
            System.out.println("-------------------------");
            System.out.print("Välj ett alternativ (1-5): ");

            int choice = getIntInput(scanner);
            switch (choice) {
                case 1 -> {                                                                 // Lägg till ny medarbetare
                    System.out.print("Ange förnamn: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Ange efternamn: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Ange email: ");
                    String email = scanner.nextLine();

                    System.out.print("Ange lösenord: ");
                    String password = scanner.nextLine();

                    System.out.print("Ange role_id: ");
                    int roleId = getIntInput(scanner);

                    System.out.print("Ange kön: ");
                    String gender = scanner.nextLine();

                    System.out.print("Ange lön: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    dbManager.createEmployee(new Employee(firstName, lastName, email, password, roleId, gender, salary));
                    System.out.println("✅ Medarbetare tillagd.");
                }
                case 2 -> {                                                                     // Visa alla medarbetare
                    List<Employee> employees = dbManager.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("⚠ Inga medarbetare tillgängliga.");
                    } else {
                        System.out.println("\n==================== ALLA MEDARBETARE ====================");
                        System.out.printf("%-5s | %-15s | %-15s | %-8s | %-8s | %-10s | %-30s%n",
                                "ID", "Förnamn", "Efternamn", "Roll-ID", "Kön", "Lön", "Email");
                        System.out.println("---------------------------------------------------------------------------------------------------");
                        employees.forEach(emp -> System.out.printf(
                                "%-5d | %-15s | %-15s | %-8d | %-8s | %-10.2f | %-30s%n",
                                emp.getEmployeeId(), emp.getFirstName(), emp.getLastName(), emp.getRoleId(),
                                emp.getGender(), emp.getSalary(), emp.getEmail()
                        ));
                        System.out.println("===================================================================================================");
                    }
                }
                case 3 -> {                                                                     // Uppdatera medarbetare
                    System.out.print("Ange ID för medarbetaren som ska uppdateras: ");
                    int employeeId = getIntInput(scanner);

                    System.out.print("Nytt förnamn: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Nytt efternamn: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Ny lön: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    dbManager.updateEmployee(employeeId, firstName, lastName, salary);
                    System.out.println("✅ Medarbetare uppdaterad.");
                }
                case 4 -> {                                                                        // Radera medarbetare
                    System.out.print("Ange ID för medarbetaren som ska raderas: ");
                    int employeeId = getIntInput(scanner);

                    if (dbManager.deleteEmployee(employeeId)) {
                        System.out.println("✅ Medarbetare med ID " + employeeId + " har raderats.");
                    } else {
                        System.out.println("❌ Ingen medarbetare hittades med ID: " + employeeId);
                    }
                }
                case 5 -> {
                    System.out.println("✅ Tillbaka till huvudmeny...");
                    running = false;                                   // Avslutar loopen och återvänder till huvudmenyn
                }
                default -> System.out.println("❌ Ogiltigt val! Försök igen.");
            }
        }
    }


//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //      HANTERA ARBETSROLLER
    // ================================
    private static void handleWorkRoles(Scanner scanner, DatabaseManager dbManager) {

        boolean running = true;                                                    // Kontroll för att hålla menyn igång

        while (running) {
            printHeader("HANTERA ARBETSROLLER");
            System.out.println("1. Skapa ny arbetsroll");
            System.out.println("2. Visa alla arbetsroller");
            System.out.println("3. Uppdatera arbetsroll");
            System.out.println("4. Radera arbetsroll");
            System.out.println("5. Tillbaka till huvudmeny");
            System.out.println("-------------------------");
            System.out.print("Välj ett alternativ (1-5): ");

            int choice = getIntInput(scanner);
            switch (choice) {
                case 1 -> {
                    System.out.print("Ange titel: ");
                    String title = scanner.nextLine();
                    System.out.print("Ange beskrivning: ");
                    String description = scanner.nextLine();
                    System.out.print("Ange lön: ");
                    double salary = Double.parseDouble(scanner.nextLine());
                    dbManager.createWorkRole(new WorkRole(0, title, description, salary));
                    System.out.println("✅ Arbetsroll skapad.");
                }
                case 2 -> {
                    List<WorkRole> roles = dbManager.getAllWorkRoles();
                    if (roles.isEmpty()) {
                        System.out.println("⚠ Inga arbetsroller tillgängliga.");
                    } else {
                        System.out.println("\n==================== ALLA ARBETSROLLER ====================");
                        System.out.printf("%-5s | %-20s | %-40s | %-10s%n", "ID", "Titel", "Beskrivning", "Lön");
                        System.out.println("------------------------------------------------------------------------------------------");
                        roles.forEach(role -> System.out.printf(
                                "%-5d | %-20s | %-40s | %-10.2f SEK%n",
                                role.getRoleId(), role.getTitle(), role.getDescription(), role.getSalary()
                        ));
                        System.out.println("==========================================================================================");
                    }
                }
                case 3 -> {
                    System.out.print("Ange ID för arbetsrollen: ");
                    int roleId = getIntInput(scanner);
                    System.out.print("Ny titel: ");
                    String title = scanner.nextLine();
                    System.out.print("Ny beskrivning: ");
                    String description = scanner.nextLine();
                    System.out.print("Ny lön: ");
                    double salary = Double.parseDouble(scanner.nextLine());
                    dbManager.updateWorkRole(roleId, title, description, salary);
                    System.out.println("✅ Arbetsroll uppdaterad.");
                }
                case 4 -> {
                    System.out.print("Ange ID för arbetsrollen som ska raderas: ");
                    int roleId = getIntInput(scanner);
                    dbManager.deleteWorkRole(roleId);
                    System.out.println("✅ Arbetsroll raderad.");
                }
                case 5 -> {
                    System.out.println("✅ Tillbaka till huvudmeny...");
                    running = false;                                   // Avbryter loopen och återvänder till huvudmenyn
                }
                default -> System.out.println("❌ Ogiltigt val! Försök igen.");
            }
        }
    }


//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //      STATISTIK OCH RAPPORTER
    // ================================
    private static void showStatisticsAndReports(Scanner scanner, DatabaseManager dbManager) {
        printHeader("STATISTIK OCH RAPPORTER");
        System.out.println("1. Visa antal medarbetare per arbetsroll");
        System.out.println("2. Visa genomsnittslön per arbetsroll");
        System.out.println("-------------------------");
        System.out.print("Välj ett alternativ (1-2): ");

        int choice = getIntInput(scanner);
        switch (choice) {
            case 1 -> {                                                         // Visa antal medarbetare per arbetsroll
                var stats = dbManager.getEmployeeCountPerRole();
                stats.forEach((role, count) -> System.out.printf("Roll: %s, Antal: %d%n", role, count));
            }
            case 2 -> {                                                          // Visa genomsnittslön per arbetsroll
                var salaries = dbManager.getAverageSalaryPerRole();
                salaries.forEach((role, avgSalary) -> System.out.printf("Roll: %s, Genomsnittslön: %.2f SEK%n", role, avgSalary));
            }
            default -> System.out.println("❌ Ogiltigt val! Försök igen.");
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //      VISA MEDARBETARPROFIL
    // ================================
    private static void showEmployeeProfile(DatabaseManager dbManager, String email) {
        Employee emp = dbManager.getEmployeeByEmailWithRole(email);
        if (emp != null) {
            LocalDate hireDate = emp.getHireDate().toLocalDate();
            LocalDate today = LocalDate.now();
            Period employmentDuration = Period.between(hireDate, today);

            System.out.println("\n====== DIN PROFIL ======");
            System.out.printf("Namn: %s %s%n", emp.getFirstName(), emp.getLastName());
            System.out.printf("Roll: %s (ID: %d)%n", emp.getRoleTitle(), emp.getRoleId());
            System.out.printf("Kön: %s%n", emp.getGender());
            System.out.printf("Lön: %.2f SEK%n", emp.getSalary());
            System.out.printf("Anställningsdatum: %s%n", hireDate);
            System.out.printf("Anställningstid: %d år och %d månader%n", employmentDuration.getYears(), employmentDuration.getMonths());
            System.out.printf("Email: %s%n", emp.getEmail());
            System.out.println("========================\n");
        } else {
            System.out.println("⚠ Medarbetaren hittades inte.");
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //    UPPDATERA MEDARBETARPROFIL
    // ================================
    private static void updateEmployeeProfile(Scanner scanner, DatabaseManager dbManager, String email) {
        Employee emp = dbManager.getEmployeeByEmailWithRole(email);
        if (emp != null) {
            System.out.print("Ange nytt förnamn (nuvarande: " + emp.getFirstName() + "): ");
            String firstName = scanner.nextLine();
            System.out.print("Ange nytt efternamn (nuvarande: " + emp.getLastName() + "): ");
            String lastName = scanner.nextLine();
            System.out.print("Ange ny lön (nuvarande: " + emp.getSalary() + "): ");
            double salary = Double.parseDouble(scanner.nextLine());

            dbManager.updateEmployeeProfile(email, firstName, lastName, salary);
            System.out.println("✅ Profil uppdaterad.");
        } else {
            System.out.println("⚠ Medarbetaren hittades inte.");
        }
    }

//----------------------------------------------------------------------------------------------------------------------

    // ================================
    //          HJÄLPMETODER
    // ================================
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("❌ Ange ett heltal: ");
            }
        }
    }

    private static void printHeader(String header) {
        System.out.println("\n========================");
        System.out.println(header);
        System.out.println("========================");
    }
}

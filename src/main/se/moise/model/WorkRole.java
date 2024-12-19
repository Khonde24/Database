package se.moise.model;

/**
 * Modellklass för WorkRole.
 * Representerar en arbetsroll med egenskaper som titel, beskrivning, lön och skapelsedatum.
 */
public class WorkRole {
    private int roleId;
    private String title;
    private String description;
    private double salary;

    // Konstruktorn med rätt tilldelning
    public WorkRole(int roleId, String title, String description, double salary) {
        this.roleId = roleId; // Fixad tilldelning
        this.title = title;
        this.description = description;
        this.salary = salary;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %.2f", roleId, title, description, salary);
    }
}

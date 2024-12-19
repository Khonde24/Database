package se.moise.databases;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility-klass för att hantera databasanslutningar.
 * Läser inställningar från database.properties-filen.
 */
public class Utility {
    // ============================================
    //          PRIVAT FÄLT FÖR ANSLUTNING
    // ============================================
    private Connection connection;

    // ============================================
    //               HÄMTA ANSLUTNINGEN
    // ============================================
    /**
     * Skapar och returnerar en databasanslutning.
     * Läser konfiguration från database.properties.
     *
     * @return Connection - databasanslutningen
     * @throws SQLException - om anslutningen misslyckas
     */
    public Connection getConnection() throws SQLException {
        // Skapa en Properties-objekt för att lagra konfiguration
        Properties props = new Properties();

        // Läs database.properties-filen
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new SQLException("❌ Kunde inte hitta database.properties-filen.");
            }
            props.load(input); // Ladda egenskaper från filen
        } catch (Exception e) {
            throw new SQLException("❌ Fel vid läsning av database.properties: " + e.getMessage(), e);
        }

        // Hämta inställningar från properties-filen
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        // Validera att inställningarna finns
        if (url == null || user == null || password == null) {
            throw new SQLException("❌ Database properties är ofullständiga. Kontrollera db.url, db.user och db.password.");
        }

                // Skapa databasanslutning
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Ansluten till databasen!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Kunde inte skapa databasanslutning: " + e.getMessage());
            throw e;
        }

        return connection; // Returnera anslutningen
    }

//----------------------------------------------------------------------------------------------------------------------

    // ============================================
    //    TESTMETOD FÖR ATT VALIDERAD ANSLUTNING
    // ============================================
    public static void main(String[] args) {
        Utility utility = new Utility();
        try {
            Connection conn = utility.getConnection();
            if (conn != null) {
                System.out.println("✅ Testanslutning lyckades!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Testanslutning misslyckades: " + e.getMessage());
        }
    }
}

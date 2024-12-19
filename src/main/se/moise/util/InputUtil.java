package se.moise.util;

import java.util.Scanner;

/**
 * Utility-klass för att hantera användarinmatning.
 * Innehåller metoder för att läsa strängar, heltal, decimaltal och ja/nej-svar.
 */
public class InputUtil {
    // ============================================
    // GLOBALA VARIABLER
    // ============================================
    public static final Scanner scanner = new Scanner(System.in);

    // ============================================
    // METOD: LÄS STRÄNG MED LÄNGDKONTROLL
    // ============================================

    /**
     * Läser en giltig sträng från användaren med både minsta och maximala längd.
     *
     * @param prompt    Meddelande som visas för användaren.
     * @param minLength Minsta tillåtna längd på inmatningen.
     * @return En giltig sträng som uppfyller längdkraven.
     */
    public static String readString(String prompt, int minLength) {
        String input; // Inmatningssträng
        while (true) {
            System.out.print(prompt); // Visa prompt
            input = scanner.nextLine(); // Läs input från användaren

            int maxLength = 0;
            if (input.length() >= minLength && input.length() <= maxLength) {
                return input; // Returnera om input är giltig
            } else {
                System.out.println("❌ Input måste vara mellan " + minLength + " och " + maxLength + " tecken.");
                System.out.println("-----------------------------------------");
            }
        }
    }

    // ============================================
    // METOD: LÄS HELTAL
    // ============================================

    /**
     * Läser ett heltal från användaren med felhantering.
     *
     * @param prompt Meddelande som visas för användaren.
     * @return Ett giltigt heltal.
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt); // Visa prompt
                return Integer.parseInt(scanner.nextLine()); // Försök läsa ett heltal
            } catch (NumberFormatException e) {
                System.out.println("❌ Felaktig inmatning. Ange ett heltal.");
                System.out.println("-----------------------------------------");
            }
        }
    }

    // ============================================
    // METOD: LÄS DECIMALTAL
    // ============================================

    /**
     * Läser ett decimaltal från användaren med felhantering.
     *
     * @param prompt Meddelande som visas för användaren.
     * @return Ett giltigt decimaltal.
     */
    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt); // Visa prompt
                return Double.parseDouble(scanner.nextLine()); // Försök läsa ett decimaltal
            } catch (NumberFormatException e) {
                System.out.println("❌ Felaktig inmatning. Ange ett decimaltal.");
                System.out.println("-----------------------------------------");
            }
        }
    }

    // ============================================
    // METOD: LÄS JA/NEJ-SVAR
    // ============================================

    /**
     * Ställer en ja/nej-fråga till användaren.
     *
     * @param prompt Meddelande som visas för användaren.
     * @return true om användaren svarar "ja", false om användaren svarar "nej".
     */
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (ja/nej): "); // Visa prompt och be om ja/nej-svar
            String input = scanner.nextLine().trim().toLowerCase(); // Läs input och normalisera
            if (input.equals("ja")) {
                return true; // Returnera true för "ja"
            } else if (input.equals("nej")) {
                return false; // Returnera false för "nej"
            } else {
                System.out.println("❌ Ogiltigt val. Skriv 'ja' eller 'nej'.");
                System.out.println("-----------------------------------------");
            }
        }
    }

    // ============================================
    // METOD: STÄNG SCANNERN (FRIVILLIG)
    // ============================================

    /**
     * Stänger scannern för att frigöra resurser.
     * Bör anropas när programmet avslutas.
     */
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close(); // Stäng scannern
            System.out.println("✅ Scanner stängd.");
            System.out.println("-----------------------------------------");
        }
    }
}

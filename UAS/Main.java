package UAS;

public class Main {
    public static void main(String[] args) {
        // Set system properties untuk mendukung GUI di berbagai platform
        System.setProperty("java.awt.headless", "false");
        try {
            // Jalankan aplikasi
            RentalPSAPP.main(args);
        } catch (Exception e) {
            System.err.println("Error saat menjalankan aplikasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
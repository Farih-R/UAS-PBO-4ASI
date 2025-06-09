package UAS;

public class Transaksi {
    private String nama;
    private String jenisPS;
    private int jam;
    private int biayaPS;
    private String menu;
    private int biayaMenu;
    private int totalBayar;
    private int diskon;

    public Transaksi(String nama, String jenisPS, int jam, int mie, int teh, int kopi, String diskonType) {
        this.nama = nama;
        this.jenisPS = jenisPS;
        this.jam = jam;

        // Hitung biaya PS
        int tarif = jenisPS.contains("PS3") ? 5000 : jenisPS.contains("PS4") ? 8000 : 12000;
        this.biayaPS = tarif * jam;

        // Hitung biaya menu
        this.biayaMenu = mie * 8000 + teh * 5000 + kopi * 6000;

        // Format menu string
        StringBuilder menuBuilder = new StringBuilder();
        if (mie > 0)
            menuBuilder.append("Mie x").append(mie).append(", ");
        if (teh > 0)
            menuBuilder.append("Es Teh x").append(teh).append(", ");
        if (kopi > 0)
            menuBuilder.append("Es Kopi x").append(kopi).append(", ");

        if (menuBuilder.length() > 0) {
            this.menu = menuBuilder.substring(0, menuBuilder.length() - 2);
        } else {
            this.menu = "-";
        }

        // Hitung diskon
        int total = this.biayaPS + this.biayaMenu;
        if (diskonType.contains("10%")) {
            this.diskon = total * 10 / 100;
        } else if (diskonType.contains("-5000")) {
            this.diskon = 5000;
        } else {
            this.diskon = 0;
        }

        this.totalBayar = total - this.diskon;
    }

    // Getters
    public String getNama() {
        return nama;
    }

    public String getJenisPS() {
        return jenisPS.split(" -")[0];
    }

    public String getJenisPSFull() {
        return jenisPS;
    }

    public int getJam() {
        return jam;
    }

    public int getBiayaPS() {
        return biayaPS;
    }

    public String getMenu() {
        return menu;
    }

    public int getBiayaMenu() {
        return biayaMenu;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public int getDiskon() {
        return diskon;
    }

    // Setters
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenisPS(String jenisPS) {
        this.jenisPS = jenisPS;
    }

    public void setJam(int jam) {
        this.jam = jam;
    }
}
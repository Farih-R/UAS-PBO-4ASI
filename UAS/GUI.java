package UAS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUI {

    public static class FormComponents {
        public JTextField tfNama;
        public JTextField tfJam;
        public JComboBox<String> cbPS;
        public JSpinner spMie;
        public JSpinner spTeh;
        public JSpinner spKopi;
        public JComboBox<String> cbDiskon;
        public JTextArea areaOutput;
        public JTextField tfIdHidden;
        public JTextField tfCari;

        public FormComponents() {
            initializeComponents();
        }

        private void initializeComponents() {
            tfNama = new JTextField();
            tfJam = new JTextField();

            String[] pilihanPS = { "PS3 - Rp 5000", "PS4 - Rp 8000", "PS5 - Rp 12000" };
            cbPS = new JComboBox<>(pilihanPS);

            spMie = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            spTeh = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
            spKopi = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

            String[] diskonOptions = { "Tanpa Diskon", "Diskon Pelajar (10%)", "Paket 2 Jam + Minuman -5000" };
            cbDiskon = new JComboBox<>(diskonOptions);

            areaOutput = new JTextArea();
            areaOutput.setEditable(false);
            areaOutput.setBorder(BorderFactory.createTitledBorder("Struk"));

            tfIdHidden = new JTextField();
            tfIdHidden.setVisible(false);

            tfCari = new JTextField();
        }

        public void clearForm() {
            tfNama.setText("");
            tfJam.setText("");
            cbPS.setSelectedIndex(0);
            spMie.setValue(0);
            spTeh.setValue(0);
            spKopi.setValue(0);
            cbDiskon.setSelectedIndex(0);
            areaOutput.setText("");
            tfIdHidden.setText("");
        }
    }

    public static class TableComponents {
        public DefaultTableModel tableModel;
        public JTable table;

        public TableComponents() {
            String[] columns = { "ID", "Nama", "PS", "Jam", "Biaya PS", "Menu", "Biaya Menu", "Total Bayar" };
            tableModel = new DefaultTableModel(columns, 0);
            table = new JTable(tableModel);
            table.setRowHeight(22); // Reduced from 25
        }
    }

    public static void setupLayout(JFrame frame, FormComponents form, TableComponents tableComp) {
        frame.setLayout(null);

        // Optimized constants for better spacing
        int leftMargin = 15;
        int topMargin = 10;
        int panelHeight = 55; // Reduced from 60
        int buttonHeight = 28; // Reduced from 30
        int fieldHeight = 24; // Reduced from 25

        int currentY = topMargin;

        // === ROW 1: Basic Information ===
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder("Form Rental PS"));
        basicInfoPanel.setLayout(null);
        basicInfoPanel.setBounds(leftMargin, currentY, 1050, panelHeight);

        // Nama Penyewa
        addLabelAndComponent(basicInfoPanel, "Nama Penyewa:", form.tfNama, 15, 22,
                100, 200, fieldHeight);

        // Jam Sewa
        addLabelAndComponent(basicInfoPanel, "Jam Sewa:", form.tfJam, 370, 22,
                60, 80, fieldHeight);

        // Jenis PS
        addLabelAndComponent(basicInfoPanel, "Jenis PS:", form.cbPS, 580, 22,
                55, 180, fieldHeight);

        frame.add(basicInfoPanel);
        currentY += panelHeight + 5; // Reduced gap

        // === ROW 2: Menu Selection ===
        JPanel menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createTitledBorder("Pilihan Menu"));
        menuPanel.setLayout(null);
        menuPanel.setBounds(leftMargin, currentY, 1050, panelHeight);

        addLabelAndComponent(menuPanel, "Mie Instan (Rp 8000):", form.spMie, 15, 22,
                120, 60, fieldHeight);

        addLabelAndComponent(menuPanel, "Es Teh (Rp 5000):", form.spTeh, 250, 22,
                100, 60, fieldHeight);

        addLabelAndComponent(menuPanel, "Es Kopi (Rp 6000):", form.spKopi, 465, 22,
                110, 60, fieldHeight);

        frame.add(menuPanel);
        currentY += panelHeight + 5;

        // === ROW 3: Discount and Action Buttons ===
        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(BorderFactory.createTitledBorder("Diskon & Cetak"));
        actionPanel.setLayout(null);
        actionPanel.setBounds(leftMargin, currentY, 1050, panelHeight);

        addLabelAndComponent(actionPanel, "Diskon:", form.cbDiskon, 15, 22,
                60, 250, fieldHeight);

        // Action buttons with proper spacing
        JButton btnSimpan = new JButton("Simpan + Struk");
        btnSimpan.setBounds(400, 22, 130, buttonHeight);
        actionPanel.add(btnSimpan);

        JButton btnPDF = new JButton("PDF");
        btnPDF.setBounds(540, 22, 70, buttonHeight);
        actionPanel.add(btnPDF);

        JButton btnPrint = new JButton("Cetak");
        btnPrint.setBounds(620, 22, 70, buttonHeight);
        actionPanel.add(btnPrint);

        frame.add(actionPanel);
        currentY += panelHeight + 5;

        // === Output Area (Reduced height) ===
        JScrollPane areaScroll = new JScrollPane(form.areaOutput);
        areaScroll.setBounds(leftMargin, currentY, 1050, 90); // Reduced from 120
        frame.add(areaScroll);
        currentY += 95; // Reduced gap

        // === Data Table (Reduced height) ===
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Data Transaksi"));
        tablePanel.setLayout(new BorderLayout());

        JScrollPane tableScroll = new JScrollPane(tableComp.table);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        tablePanel.setBounds(leftMargin, currentY, 1050, 140); // Reduced from 180
        frame.add(tablePanel);
        currentY += 145; // Reduced gap

        // === Search and Management Panel ===
        JPanel managementPanel = new JPanel();
        managementPanel.setBorder(BorderFactory.createTitledBorder("Manajemen Data"));
        managementPanel.setLayout(null);
        managementPanel.setBounds(leftMargin, currentY, 1050, panelHeight);

        addLabelAndComponent(managementPanel, "Cari (Nama/PS/ID):", form.tfCari, 15, 22,
                130, 200, fieldHeight);

        JButton btnCari = new JButton("Cari");
        btnCari.setBounds(360, 22, 80, buttonHeight);
        managementPanel.add(btnCari);

        JButton btnUbah = new JButton("Ubah");
        btnUbah.setBounds(450, 22, 80, buttonHeight);
        managementPanel.add(btnUbah);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(540, 22, 80, buttonHeight);
        managementPanel.add(btnHapus);

        frame.add(managementPanel);
        currentY += panelHeight + 10;

        // === Exit Button ===
        JButton btnKeluar = new JButton("Keluar");
        int btnWidth = 150;
        int frameWidth = 1100;
        int posX = (frameWidth / 2) - (btnWidth / 2);
        btnKeluar.setBounds(posX, currentY, btnWidth, buttonHeight);
        frame.add(btnKeluar);

        // Add hidden field
        frame.add(form.tfIdHidden);

        // Set optimized frame size
        frame.setMinimumSize(new Dimension(1100, currentY + 50));
        frame.setPreferredSize(new Dimension(1100, currentY + 50));
    }

    private static void addLabelAndComponent(Container parent, String labelText, JComponent component,
            int x, int y, int labelWidth, int componentWidth, int height) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, labelWidth, height);
        parent.add(label);

        component.setBounds(x + labelWidth + 10, y, componentWidth, height);
        parent.add(component);
    }

    // Overloaded method for backward compatibility
    private static void addLabelAndComponent(JFrame frame, String labelText, JComponent component,
            int x, int y, int labelWidth, int componentWidth) {
        addLabelAndComponent(frame, labelText, component, x, y, labelWidth, componentWidth, 25);
    }

    public static void applyTheme(JFrame frame) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf");
        }
    }
}
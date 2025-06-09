package UAS;

import javax.swing.*;
import java.awt.event.*;

public class RentalPSAPP {
    private JFrame frame;
    private GUI.FormComponents formComponents;
    private GUI.TableComponents tableComponents;
    private Handler eventHandlers;

    public RentalPSAPP() {
        initializeApplication();
    }

    private void initializeApplication() {
        // Authenticate user first
        if (!Login.authenticateUser()) {
            System.exit(0);
        }

        // Initialize components
        frame = new JFrame("Rental PS - Kasir");
        frame.setSize(1100, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        formComponents = new GUI.FormComponents();
        tableComponents = new GUI.TableComponents();
        eventHandlers = new Handler(formComponents, tableComponents);

        // Setup GUI
        GUI.setupLayout(frame, formComponents, tableComponents);
        GUI.applyTheme(frame);

        // Setup event handlers
        setupEventHandlers();

        // Load initial data
        DBConnection.loadDataToTable(tableComponents.tableModel);

        // Show frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupEventHandlers() {
        // Get buttons from frame
        JButton btnSimpan = findButton("Simpan + Struk");
        JButton btnPDF = findButton("PDF");
        JButton btnPrint = findButton("Cetak");
        JButton btnCari = findButton("Cari");
        JButton btnUbah = findButton("Ubah");
        JButton btnHapus = findButton("Hapus");
        JButton btnKeluar = findButton("Keluar");

        // Add event listeners
        if (btnSimpan != null)
            btnSimpan.addActionListener(eventHandlers::handleSaveTransaction);
        if (btnPDF != null)
            btnPDF.addActionListener(eventHandlers::handleGeneratePDF);
        if (btnPrint != null)
            btnPrint.addActionListener(eventHandlers::handlePrintReceipt);
        if (btnCari != null)
            btnCari.addActionListener(eventHandlers::handleSearch);
        if (btnUbah != null)
            btnUbah.addActionListener(eventHandlers::handleUpdate);
        if (btnHapus != null)
            btnHapus.addActionListener(eventHandlers::handleDelete);
        if (btnKeluar != null)
            btnKeluar.addActionListener(e -> handleExit());

        // Add table selection listener
        tableComponents.table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                eventHandlers.handleTableSelection();
            }
        });
    }

    private JButton findButton(String text) {
        return findButtonRecursive(frame, text);
    }

    private JButton findButtonRecursive(java.awt.Container container, String text) {
        for (java.awt.Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (text.equals(button.getText())) {
                    return button;
                }
            } else if (component instanceof java.awt.Container) {
                JButton result = findButtonRecursive((java.awt.Container) component, text);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private void handleExit() {
        if (Login.confirmExit(frame)) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RentalPSAPP();
        });
    }
}
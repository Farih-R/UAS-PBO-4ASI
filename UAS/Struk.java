package UAS;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class Struk {

    public static String generateReceiptText(Transaksi transaksi, int transaksiId) {
        try {
            String waktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            final int WIDTH = 35; // Lebar total struk

            StringBuilder receipt = new StringBuilder();

            // Detail transaksi dengan format baru
            receipt.append(createLine("-", WIDTH)).append("\n");
            receipt.append("STRUK RENTAL PS").append("\n");
            receipt.append("ID Transaksi: ").append(transaksiId).append("\n");
            receipt.append("Nama: ").append(transaksi.getNama()).append("\n");
            receipt.append("Jenis PS: ").append(transaksi.getJenisPSFull()).append("\n");
            receipt.append("Jam: ").append(transaksi.getJam()).append(" jam").append("\n");
            receipt.append("Biaya PS: Rp ").append(transaksi.getBiayaPS()).append("\n");

            // Menu jika ada
            if (!transaksi.getMenu().isEmpty() && !transaksi.getMenu().equals("-") && transaksi.getBiayaMenu() > 0) {
                receipt.append("Menu: ").append(transaksi.getMenu()).append("\n");
                receipt.append("Biaya Menu: Rp ").append(transaksi.getBiayaMenu()).append("\n");
            }

            // Diskon jika ada
            if (transaksi.getDiskon() > 0) {
                receipt.append("Diskon: Rp ").append(transaksi.getDiskon()).append("\n");
            }

            // Total
            receipt.append("Total Bayar: Rp ").append(transaksi.getTotalBayar()).append("\n");
            receipt.append("Waktu: ").append(waktu).append("\n");

            receipt.append(createLine("-", WIDTH)).append("\n");
            receipt.append(createLine("-", WIDTH)).append("\n");
            receipt.append("\n");

            // Footer
            receipt.append(centerText("Terima kasih atas kunjungan anda", WIDTH)).append("\n");

            return receipt.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating receipt: " + e.getMessage();
        }
    }

    // Helper method untuk membuat garis
    private static String createLine(String character, int width) {
        return character.repeat(width);
    }

    // Helper method untuk memusatkan text
    private static String centerText(String text, int width) {
        if (text.length() >= width)
            return text.substring(0, width);

        int padding = (width - text.length()) / 2;
        String leftPad = " ".repeat(Math.max(0, padding));
        String rightPad = " ".repeat(Math.max(0, width - text.length() - padding));
        return leftPad + text + rightPad;
    }

    // Helper method untuk format baris dengan alignment
    private static String formatLine(String label, String value, int width) {
        return formatLine(label, value, width, false);
    }

    private static String formatLine(String label, String value, int width, boolean bold) {
        String separator = " : ";
        int maxLabelWidth = 12; // Lebar maksimal untuk label

        // Potong label jika terlalu panjang
        if (label.length() > maxLabelWidth) {
            label = label.substring(0, maxLabelWidth);
        }

        // Hitung sisa ruang untuk value
        int availableSpace = width - label.length() - separator.length();

        // Potong value jika terlalu panjang
        if (value.length() > availableSpace) {
            value = value.substring(0, availableSpace);
        }

        // Format dengan padding
        String result = String.format("%-" + maxLabelWidth + "s%s%s",
                label, separator, value);

        // Pastikan tidak melebihi width
        if (result.length() > width) {
            result = result.substring(0, width);
        } else if (result.length() < width) {
            result = result + " ".repeat(width - result.length());
        }

        return result;
    }

    public static void showReceiptDialog(String receiptText) {
        try {
            JTextArea area = new JTextArea(receiptText);
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            area.setBackground(Color.WHITE);
            area.setForeground(Color.BLACK);
            area.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(450, 400));
            scroll.setBorder(BorderFactory.createTitledBorder("Struk Transaksi"));

            // Create custom dialog
            JDialog dialog = new JDialog((Frame) null, "Struk Transaksi", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scroll, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout());

            JButton printButton = new JButton("Cetak");
            printButton.addActionListener(e -> {
                try {
                    printReceipt(receiptText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error mencetak: " + ex.getMessage());
                }
            });

            JButton pdfButton = new JButton("Simpan PDF");
            pdfButton.addActionListener(e -> {
                try {
                    generatePDF(receiptText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error membuat PDF: " + ex.getMessage());
                }
            });

            JButton closeButton = new JButton("Tutup");
            closeButton.addActionListener(e -> dialog.dispose());

            buttonPanel.add(printButton);
            buttonPanel.add(pdfButton);
            buttonPanel.add(closeButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            dialog.setSize(500, 500);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error menampilkan struk: " + e.getMessage());
        }
    }

    public static void generatePDF(String receiptText) {
        try {
            // Create file chooser with default filename
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Struk PDF");

            // Set default filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            fileChooser.setSelectedFile(new File("Struk_" + timestamp + ".pdf"));

            // Show save dialog
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filename.toLowerCase().endsWith(".pdf")) {
                    filename += ".pdf";
                }

                // Create the file directory if it doesn't exist
                File file = new File(filename);
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }

                boolean pdfCreated = false;
                String finalFilename = filename;

                // Try using iText library first
                try {
                    createPDFWithiText(receiptText, filename);
                    pdfCreated = true;
                    System.out.println("PDF created with iText: " + filename);
                } catch (Exception e) {
                    System.out.println("iText tidak tersedia: " + e.getMessage());

                    // Fallback: Create as text file but inform user
                    finalFilename = filename.replace(".pdf", ".txt");
                    createTextFile(receiptText, finalFilename);
                    pdfCreated = true;
                    System.out.println("Text file created: " + finalFilename);
                }

                if (pdfCreated) {
                    // Show success message
                    String message = finalFilename.endsWith(".txt")
                            ? "Struk berhasil disimpan sebagai file teks!\n(Library PDF tidak tersedia)\nFile: "
                                    + finalFilename
                            : "Struk berhasil disimpan sebagai PDF!\nFile: " + finalFilename;

                    JOptionPane.showMessageDialog(null,
                            message,
                            "File Berhasil Dibuat",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Ask if user wants to open the file
                    int openFile = JOptionPane.showConfirmDialog(null,
                            "Apakah Anda ingin membuka file?",
                            "Buka File",
                            JOptionPane.YES_NO_OPTION);

                    if (openFile == JOptionPane.YES_OPTION) {
                        openFile(finalFilename);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat membuat file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk membuat PDF dengan iText (jika tersedia)
    private static void createPDFWithiText(String receiptText, String filename) throws Exception {
        try {
            // Check if iText classes are available
            Class.forName("com.itextpdf.text.Document");
            Class.forName("com.itextpdf.text.pdf.PdfWriter");

            // Try to use iText library - Use smaller page size for receipt
            com.itextpdf.text.Rectangle pageSize = new com.itextpdf.text.Rectangle(200, 400); // Custom size
            pageSize.setBackgroundColor(com.itextpdf.text.BaseColor.WHITE);

            com.itextpdf.text.Document document = new com.itextpdf.text.Document(pageSize, 10, 10, 10, 10); // Small
                                                                                                            // margins
            com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document,
                    new FileOutputStream(filename));
            document.open();

            // Create fonts
            com.itextpdf.text.pdf.BaseFont baseFont = com.itextpdf.text.pdf.BaseFont.createFont(
                    com.itextpdf.text.pdf.BaseFont.COURIER,
                    com.itextpdf.text.pdf.BaseFont.CP1252,
                    com.itextpdf.text.pdf.BaseFont.NOT_EMBEDDED);

            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontBody = new com.itextpdf.text.Font(baseFont, 8, com.itextpdf.text.Font.NORMAL);

            // Add content to PDF
            String[] lines = receiptText.split("\n");
            for (String line : lines) {
                com.itextpdf.text.Paragraph paragraph;

                if (line.contains("STRUK RENTAL PS") || line.contains("Kelompok 7")) {
                    paragraph = new com.itextpdf.text.Paragraph(line.trim(), fontTitle);
                    paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                } else if (line.contains("TOTAL BAYAR")) {
                    paragraph = new com.itextpdf.text.Paragraph(line.trim(), fontTitle);
                    paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                } else if (line.contains("Terima kasih")) {
                    paragraph = new com.itextpdf.text.Paragraph(line.trim(), fontBody);
                    paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                } else {
                    paragraph = new com.itextpdf.text.Paragraph(line.trim(), fontBody);
                    paragraph.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                }

                // Set spacing yang lebih ketat
                paragraph.setSpacingAfter(1f);
                paragraph.setSpacingBefore(0f);
                document.add(paragraph);
            }

            document.close();
            writer.close();

        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new Exception("iText library tidak ditemukan: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error creating PDF with iText: " + e.getMessage());
        }
    }

    // Alternative method: Create simple text file
    private static void createTextFile(String receiptText, String filename) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("STRUK TRANSAKSI RENTAL PS");
            writer.println("Format: Text File (PDF Library tidak tersedia)");
            writer.println();
            writer.print(receiptText);
            writer.println();
            writer.println("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

    // Helper method to open file
    private static void openFile(String filename) {
        try {
            File file = new File(filename);
            if (Desktop.isDesktopSupported() && file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(null,
                        "File berhasil dibuat tetapi tidak dapat dibuka secara otomatis.\nFile tersimpan di: "
                                + filename,
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "File berhasil dibuat tetapi tidak dapat dibuka secara otomatis.\nFile tersimpan di: " + filename +
                            "\nError: " + ex.getMessage(),
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void printReceipt(String receiptText) {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                        throws PrinterException {

                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Use monospaced font for better alignment
                    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
                    g2d.setFont(font);

                    FontMetrics fm = g2d.getFontMetrics();
                    int lineHeight = fm.getHeight();

                    String[] lines = receiptText.split("\n");
                    int y = lineHeight;

                    for (String line : lines) {
                        if (y > pageFormat.getImageableHeight()) {
                            break; // Don't print outside the printable area
                        }
                        g2d.drawString(line, 0, y);
                        y += lineHeight;
                    }

                    return PAGE_EXISTS;
                }
            });

            // Show print dialog
            if (printerJob.printDialog()) {
                printerJob.print();
                JOptionPane.showMessageDialog(null, "Struk berhasil dicetak!");
            }

        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null,
                    "Error saat mencetak: " + e.getMessage(),
                    "Error Print",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat mencetak: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
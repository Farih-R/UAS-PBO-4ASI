package UAS;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class Struk {

    public static String generateReceiptText(Transaksi transaction, int transactionId) {
        String waktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return "STRUK RENTAL PS\n" +
                "ID Transaksi: " + transactionId + "\n" +
                "Nama: " + transaction.getNama() + "\n" +
                "Jenis PS: " + transaction.getJenisPSFull() + "\n" +
                "Jam: " + transaction.getJam() + " jam\n" +
                "Biaya PS: Rp " + transaction.getBiayaPS() + "\n" +
                "Menu: " + transaction.getMenu() + "\n" +
                "Biaya Menu: Rp " + transaction.getBiayaMenu() + "\n" +
                "Diskon: Rp " + transaction.getDiskon() + "\n" +
                "Total Bayar: Rp " + transaction.getTotalBayar() + "\n" +
                "Waktu: " + waktu + "\n";
    }

    public static void showReceiptDialog(String receiptText) {
        JTextArea area = new JTextArea(receiptText);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scroll, "Struk Transaksi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void generatePDF(String receiptText) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Struk PDF");
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filename.toLowerCase().endsWith(".pdf")) {
                    filename += ".pdf";
                }

                Document doc = new Document(new Rectangle(226, 600));
                PdfWriter.getInstance(doc, new FileOutputStream(filename));
                doc.open();

                Font fontTitle = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
                Font fontBody = new Font(Font.FontFamily.COURIER, 8);

                // Judul
                Paragraph title = new Paragraph("STRUK TRANSAKSI RENTAL PS\n", fontTitle);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(5);
                doc.add(title);

                // Garis pembatas
                Paragraph separator = new Paragraph("------------------------------------------", fontBody);
                separator.setAlignment(Element.ALIGN_CENTER);
                separator.setSpacingAfter(5);
                doc.add(separator);

                // Isi struk baris demi baris
                String[] lines = receiptText.split("\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Paragraph p = new Paragraph(line, fontBody);
                        p.setAlignment(Element.ALIGN_LEFT);
                        doc.add(p);
                    }
                }

                // Garis bawah + ucapan
                doc.add(new Paragraph("------------------------------------------", fontBody));
                Paragraph thanks = new Paragraph("Terima kasih atas kunjungan Anda!", fontBody);
                thanks.setAlignment(Element.ALIGN_CENTER);
                thanks.setSpacingBefore(10);
                doc.add(thanks);

                doc.close();

                // Buka file PDF setelah selesai
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filename));
                }

                JOptionPane.showMessageDialog(null, "Struk berhasil disimpan!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void printReceipt(String receiptText) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable((graphics, pageFormat, pageIndex) -> {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                String[] lines = receiptText.split("\n");
                int yPos = 100;

                for (String line : lines) {
                    graphics.drawString(line, 50, yPos);
                    yPos += 15;
                }

                return Printable.PAGE_EXISTS;
            });

            if (job.printDialog()) {
                job.print();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
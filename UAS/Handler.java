package UAS;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Handler {
    private GUI.FormComponents formComponents;
    private GUI.TableComponents tableComponents;
    private String currentReceiptText = "";
    private Transaksi currentTransaction = null;

    public Handler(GUI.FormComponents formComponents, GUI.TableComponents tableComponents) {
        this.formComponents = formComponents;
        this.tableComponents = tableComponents;
    }

    public void handleSaveTransaction(ActionEvent e) {
        try {
            // Validate input
            if (formComponents.tfNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama penyewa harus diisi!");
                formComponents.tfNama.requestFocus();
                return;
            }

            if (formComponents.tfJam.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus diisi!");
                formComponents.tfJam.requestFocus();
                return;
            }

            // Validate jam is positive number
            int jam;
            try {
                jam = Integer.parseInt(formComponents.tfJam.getText().trim());
                if (jam <= 0) {
                    JOptionPane.showMessageDialog(null, "Jam sewa harus lebih dari 0!");
                    formComponents.tfJam.requestFocus();
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus berupa angka yang valid!");
                formComponents.tfJam.requestFocus();
                return;
            }

            // Get input values
            String nama = formComponents.tfNama.getText().trim();
            String jenisPS = formComponents.cbPS.getSelectedItem().toString();
            int mie = (int) formComponents.spMie.getValue();
            int teh = (int) formComponents.spTeh.getValue();
            int kopi = (int) formComponents.spKopi.getValue();
            String diskonType = formComponents.cbDiskon.getSelectedItem().toString();

            System.out.println("Debug - Input values:");
            System.out.println("Nama: " + nama);
            System.out.println("Jenis PS: " + jenisPS);
            System.out.println("Jam: " + jam);
            System.out.println("Menu - Mie: " + mie + ", Teh: " + teh + ", Kopi: " + kopi);
            System.out.println("Diskon: " + diskonType);

            // Create transaction model
            Transaksi transaction = new Transaksi(nama, jenisPS, jam, mie, teh, kopi, diskonType);
            currentTransaction = transaction;

            System.out.println("Debug - Transaction created:");
            System.out.println("Biaya PS: " + transaction.getBiayaPS());
            System.out.println("Menu: " + transaction.getMenu());
            System.out.println("Biaya Menu: " + transaction.getBiayaMenu());
            System.out.println("Total: " + transaction.getTotalBayar());

            // Save to database
            int transactionId = DBConnection.insertTransaction(
                    transaction.getNama(),
                    transaction.getJenisPS(),
                    transaction.getJam(),
                    transaction.getBiayaPS(),
                    transaction.getMenu(),
                    transaction.getBiayaMenu(),
                    transaction.getTotalBayar());

            System.out.println("Debug - Transaction ID: " + transactionId);

            if (transactionId > 0) {
                // Generate receipt text
                currentReceiptText = Struk.generateReceiptText(transaction, transactionId);

                // Display receipt in text area
                formComponents.areaOutput.setText(currentReceiptText);

                // Show receipt dialog
                SwingUtilities.invokeLater(() -> {
                    Struk.showReceiptDialog(currentReceiptText);
                });

                // Refresh table data
                DBConnection.loadDataToTable(tableComponents.tableModel);

                // Show success message
                JOptionPane.showMessageDialog(null,
                        "Transaksi berhasil disimpan!\nID Transaksi: " + transactionId,
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                formComponents.clearForm();

            } else {
                JOptionPane.showMessageDialog(null,
                        "Gagal menyimpan transaksi ke database!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleGeneratePDF(ActionEvent e) {
        if (currentReceiptText == null || currentReceiptText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Tidak ada struk untuk dibuat PDF.\nSilakan buat transaksi terlebih dahulu.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Struk.generatePDF(currentReceiptText);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error membuat PDF: " + ex.getMessage(),
                    "Error PDF",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handlePrintReceipt(ActionEvent e) {
        if (currentReceiptText == null || currentReceiptText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Tidak ada struk untuk dicetak.\nSilakan buat transaksi terlebih dahulu.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Struk.printReceipt(currentReceiptText);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error mencetak struk: " + ex.getMessage(),
                    "Error Print",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSearch(ActionEvent e) {
        String keyword = formComponents.tfCari.getText().trim();
        if (!keyword.isEmpty()) {
            DBConnection.searchData(keyword, tableComponents.tableModel);
            JOptionPane.showMessageDialog(null,
                    "Pencarian selesai untuk: \"" + keyword + "\"",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            DBConnection.loadDataToTable(tableComponents.tableModel);
            JOptionPane.showMessageDialog(null,
                    "Menampilkan semua data",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void handleUpdate(ActionEvent e) {
        try {
            if (formComponents.tfIdHidden.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Pilih data yang akan diubah dari tabel terlebih dahulu!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (formComponents.tfNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama penyewa harus diisi!");
                formComponents.tfNama.requestFocus();
                return;
            }

            if (formComponents.tfJam.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus diisi!");
                formComponents.tfJam.requestFocus();
                return;
            }

            int id = Integer.parseInt(formComponents.tfIdHidden.getText().trim());
            String nama = formComponents.tfNama.getText().trim();
            int jam = Integer.parseInt(formComponents.tfJam.getText().trim());

            if (jam <= 0) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus lebih dari 0!");
                formComponents.tfJam.requestFocus();
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Yakin ingin mengubah data ini?",
                    "Konfirmasi Update",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (DBConnection.updateTransaction(id, nama, jam)) {
                    DBConnection.loadDataToTable(tableComponents.tableModel);
                    formComponents.clearForm();
                    JOptionPane.showMessageDialog(null,
                            "Data berhasil diubah.",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Gagal mengubah data.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "ID dan Jam harus berupa angka yang valid!",
                    "Error Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleDelete(ActionEvent e) {
        try {
            if (formComponents.tfIdHidden.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Pilih data yang akan dihapus dari tabel terlebih dahulu!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Yakin ingin menghapus data ini?\nData yang sudah dihapus tidak dapat dikembalikan!",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(formComponents.tfIdHidden.getText().trim());

                if (DBConnection.deleteTransaction(id)) {
                    DBConnection.loadDataToTable(tableComponents.tableModel);
                    formComponents.clearForm();
                    JOptionPane.showMessageDialog(null,
                            "Data berhasil dihapus.",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Gagal menghapus data.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "ID harus berupa angka yang valid!",
                    "Error Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleTableSelection() {
        try {
            int selectedRow = tableComponents.table.getSelectedRow();
            if (selectedRow >= 0) {
                // Fill form with selected data
                formComponents.tfIdHidden.setText(tableComponents.table.getValueAt(selectedRow, 0).toString());
                formComponents.tfNama.setText(tableComponents.table.getValueAt(selectedRow, 1).toString());
                formComponents.tfJam.setText(tableComponents.table.getValueAt(selectedRow, 3).toString());

                // Set PS type based on stored data
                String psType = tableComponents.table.getValueAt(selectedRow, 2).toString();
                for (int i = 0; i < formComponents.cbPS.getItemCount(); i++) {
                    if (formComponents.cbPS.getItemAt(i).contains(psType)) {
                        formComponents.cbPS.setSelectedIndex(i);
                        break;
                    }
                }

                formComponents.areaOutput.setText("Data terpilih - ID: " + formComponents.tfIdHidden.getText() +
                        "\nSiap untuk diedit atau dihapus");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error saat memilih data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getter methods
    public String getCurrentReceiptText() {
        return currentReceiptText;
    }

    public Transaksi getCurrentTransaction() {
        return currentTransaction;
    }
}
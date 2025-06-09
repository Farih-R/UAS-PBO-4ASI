package UAS;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Handler {
    private GUI.FormComponents formComponents;
    private GUI.TableComponents tableComponents;
    private String currentReceiptText = "";

    public Handler(GUI.FormComponents formComponents,
            GUI.TableComponents tableComponents) {
        this.formComponents = formComponents;
        this.tableComponents = tableComponents;
    }

    public void handleSaveTransaction(ActionEvent e) {
        try {
            // Validate input
            if (formComponents.tfNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama penyewa harus diisi!");
                return;
            }

            if (formComponents.tfJam.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus diisi!");
                return;
            }

            // Get input values
            String nama = formComponents.tfNama.getText().trim();
            int jam = Integer.parseInt(formComponents.tfJam.getText());
            String jenisPS = formComponents.cbPS.getSelectedItem().toString();
            int mie = (int) formComponents.spMie.getValue();
            int teh = (int) formComponents.spTeh.getValue();
            int kopi = (int) formComponents.spKopi.getValue();
            String diskonType = formComponents.cbDiskon.getSelectedItem().toString();

            // Create transaction model
            Transaksi transaction = new Transaksi(nama, jenisPS, jam, mie, teh, kopi, diskonType);

            // Save to database
            int transactionId = DBConnection.insertTransaction(
                    transaction.getNama(),
                    transaction.getJenisPS(),
                    transaction.getJam(),
                    transaction.getBiayaPS(),
                    transaction.getMenu(),
                    transaction.getBiayaMenu(),
                    transaction.getTotalBayar());

            // Generate receipt
            currentReceiptText = Struk.generateReceiptText(transaction, transactionId);
            formComponents.areaOutput.setText(currentReceiptText);

            // Refresh table
            DBConnection.loadDataToTable(tableComponents.tableModel);

            // Clear form
            formComponents.clearForm();

            JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Jam sewa harus berupa angka!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void handleGeneratePDF(ActionEvent e) {
        if (currentReceiptText.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Tidak ada struk untuk dicetak. Silakan buat transaksi terlebih dahulu.");
            return;
        }
        Struk.generatePDF(currentReceiptText);
    }

    public void handlePrintReceipt(ActionEvent e) {
        if (currentReceiptText.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Tidak ada struk untuk dicetak. Silakan buat transaksi terlebih dahulu.");
            return;
        }
        Struk.printReceipt(currentReceiptText);
    }

    public void handleSearch(ActionEvent e) {
        String keyword = formComponents.tfCari.getText().trim();
        if (!keyword.isEmpty()) {
            DBConnection.searchData(keyword, tableComponents.tableModel);
        } else {
            DBConnection.loadDataToTable(tableComponents.tableModel);
        }
    }

    public void handleUpdate(ActionEvent e) {
        try {
            if (formComponents.tfIdHidden.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan diubah dari tabel!");
                return;
            }

            if (formComponents.tfNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nama penyewa harus diisi!");
                return;
            }

            if (formComponents.tfJam.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jam sewa harus diisi!");
                return;
            }

            int id = Integer.parseInt(formComponents.tfIdHidden.getText());
            String nama = formComponents.tfNama.getText().trim();
            int jam = Integer.parseInt(formComponents.tfJam.getText());

            if (DBConnection.updateTransaction(id, nama, jam)) {
                DBConnection.loadDataToTable(tableComponents.tableModel);
                formComponents.clearForm();
                JOptionPane.showMessageDialog(null, "Data berhasil diubah.");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal mengubah data.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID dan Jam harus berupa angka!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void handleDelete(ActionEvent e) {
        try {
            if (formComponents.tfIdHidden.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus dari tabel!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin ingin menghapus data ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(formComponents.tfIdHidden.getText());

                if (DBConnection.deleteTransaction(id)) {
                    DBConnection.loadDataToTable(tableComponents.tableModel);
                    formComponents.clearForm();
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus data.");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID harus berupa angka!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
            ex.printStackTrace();
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

                // Set PS type
                String psType = tableComponents.table.getValueAt(selectedRow, 2).toString();
                String biayaPS = tableComponents.table.getValueAt(selectedRow, 4).toString();
                String fullPSType = psType + " - Rp " + biayaPS;

                for (int i = 0; i < formComponents.cbPS.getItemCount(); i++) {
                    if (formComponents.cbPS.getItemAt(i).startsWith(psType)) {
                        formComponents.cbPS.setSelectedIndex(i);
                        break;
                    }
                }

                formComponents.areaOutput
                        .setText("Data siap diedit atau dihapus. ID: " + formComponents.tfIdHidden.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
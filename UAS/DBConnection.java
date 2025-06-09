package UAS;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_rentalps";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void loadDataToTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM riwayat_transaksi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("id"), rs.getString("nama"), rs.getString("jenis_ps"),
                        rs.getInt("jam"), rs.getInt("biaya_ps"), rs.getString("menu"),
                        rs.getInt("biaya_menu"), rs.getInt("total_bayar")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void searchData(String keyword, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM riwayat_transaksi WHERE nama LIKE ? OR jenis_ps LIKE ? OR id LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String query = "%" + keyword + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("id"), rs.getString("nama"), rs.getString("jenis_ps"),
                        rs.getInt("jam"), rs.getInt("biaya_ps"), rs.getString("menu"),
                        rs.getInt("biaya_menu"), rs.getInt("total_bayar")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insertTransaction(String nama, String jenisPS, int jam, int biayaPS,
            String menu, int biayaMenu, int totalBayar) {
        int lastId = 0;
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO riwayat_transaksi (nama, jenis_ps, jam, biaya_ps, menu, biaya_menu, total_bayar) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nama);
            ps.setString(2, jenisPS);
            ps.setInt(3, jam);
            ps.setInt(4, biayaPS);
            ps.setString(5, menu);
            ps.setInt(6, biayaMenu);
            ps.setInt(7, totalBayar);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastId;
    }

    public static boolean updateTransaction(int id, String nama, int jam) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE riwayat_transaksi SET nama=?, jam=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, jam);
            ps.setInt(3, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTransaction(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM riwayat_transaksi WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
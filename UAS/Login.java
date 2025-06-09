package UAS;

import javax.swing.*;
import java.awt.*;

public class Login {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static boolean authenticateUser() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal menerapkan Nimbus LookAndFeel: " + e.getMessage());
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(400, 300));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // padding luar

        // Spasi dari atas
        panel.add(Box.createVerticalStrut(20));

        // Label SISTEM PARKIR
        JLabel lblTitle = new JLabel("Rental PS - Kelompok 7", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 99, 255));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);

        // Spasi antara judul dan form
        panel.add(Box.createVerticalStrut(20));

        // Form login
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblUser = new JLabel("Username:");
        JTextField tfUser = new JTextField(15);
        JLabel lblPass = new JLabel("Password:");
        JPasswordField pfPass = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUser, gbc);
        gbc.gridx = 1;
        formPanel.add(tfUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblPass, gbc);
        gbc.gridx = 1;
        formPanel.add(pfPass, gbc);

        panel.add(formPanel);

        // Spasi sebelum tombol login
        panel.add(Box.createVerticalStrut(10));

        // Tombol login
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(33, 99, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(btnLogin);
        panel.add(btnPanel);

        // Tampilkan dialog
        JDialog dialog = new JDialog((Frame) null, "Login", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        final boolean[] loginSuccess = { false };

        btnLogin.addActionListener(e -> {
            String username = tfUser.getText().trim();
            String password = new String(pfPass.getPassword());

            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                loginSuccess[0] = true;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Username atau password salah.", "Login Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
        return loginSuccess[0];
    }

    public static boolean confirmExit(JFrame parentFrame) {
        int confirm = JOptionPane.showConfirmDialog(
                parentFrame,
                "Yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }
}

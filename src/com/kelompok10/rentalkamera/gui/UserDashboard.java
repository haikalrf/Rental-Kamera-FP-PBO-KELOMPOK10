package com.kelompok10.rentalkamera.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.kelompok10.rentalkamera.controller.CameraController;
import com.kelompok10.rentalkamera.model.Camera;
import com.kelompok10.rentalkamera.model.Person;

public class UserDashboard extends JFrame {

    private Person user;
    private CameraController cameraController = new CameraController();
    private JTable cameraTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, refreshButton, logoutButton;

    public UserDashboard(Person user) {
        this.user = user;
        initializeComponents();
        loadCameraData();
    }

    private void initializeComponents() {
        setTitle("Dashboard Pengguna - Selamat datang " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(52, 152, 219));

        JLabel welcomeLabel = new JLabel("Selamat datang, " + user.getUsername() + " (Pengguna)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);

        logoutButton = new JButton("Keluar");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(80, 30));
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(logoutButton);

        add(headerPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Cari Kamera"));

        JLabel searchLabel = new JLabel("Cari berdasarkan Merek/Tipe:");
        searchField = new JTextField(20);
        searchButton = new JButton("Cari");
        refreshButton = new JButton("Tampilkan Semua");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.CENTER);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Kamera Tersedia"));

        // Create table model
        String[] columnNames = {"ID", "Merek", "Tipe", "Harga (per hari)", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only for users
            }
        };

        cameraTable = new JTable(tableModel);
        cameraTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cameraTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        cameraTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        cameraTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        cameraTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        cameraTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        cameraTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(cameraTable);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rentButton = new JButton("Sewa Kamera Terpilih");
        rentButton.setBackground(new Color(46, 204, 113));
        rentButton.setForeground(Color.WHITE);
        rentButton.setPreferredSize(new Dimension(180, 35));

        buttonPanel.add(rentButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Combine search and table panels
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Siap");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Event Listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCameraData();
            }
        });

        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rentSelectedCamera();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Allow search on Enter key
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
    }

    private void loadCameraData() {
        tableModel.setRowCount(0); // Clear existing data
        List<Camera> cameras = cameraController.ambilSemuaKamera();

        for (Camera camera : cameras) {
            Object[] row = {
                    camera.getId(),
                    camera.getBrand(),
                    camera.getType(),
                    "Rp " + String.format("%.0f", camera.getHargaRental()),
                    camera.getStok()
            };
            tableModel.addRow(row);
        }
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan kata kunci pencarian!");
            return;
        }

        tableModel.setRowCount(0); // Clear existing data
        List<Camera> cameras = cameraController.searchCamera(keyword);

        if (cameras.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada kamera ditemukan untuk: " + keyword);
        } else {
            for (Camera camera : cameras) {
                Object[] row = {
                        camera.getId(),
                        camera.getBrand(),
                        camera.getType(),
                        "Rp " + String.format("%.0f", camera.getHargaRental()),
                        camera.getStok()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void rentSelectedCamera() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih kamera yang ingin disewa!");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String brand = (String) tableModel.getValueAt(selectedRow, 1);
        String type = (String) tableModel.getValueAt(selectedRow, 2);
        double harga = Double.parseDouble(((String) tableModel.getValueAt(selectedRow, 3)).replace("Rp ", "").replace(",", ""));
        int stock = (Integer) tableModel.getValueAt(selectedRow, 4);

        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "Maaf, kamera ini habis stok!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda ingin menyewa: " + brand + " " + type + "?",
                "Konfirmasi Penyewaan",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            int newStock = stock - 1;
            boolean updated = cameraController.ubahKamera(id, brand, type, harga, newStock);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Kamera berhasil disewa!");
                loadCameraData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyewa kamera!");
            }
        }
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}
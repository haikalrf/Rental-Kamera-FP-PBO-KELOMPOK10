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

public class AdminDashboard extends JFrame {

    private Person admin;
    private CameraController cameraController = new CameraController();
    private JTable cameraTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, refreshButton, addButton, editButton, deleteButton, logoutButton;

    public AdminDashboard(Person admin) {
        this.admin = admin;
        initializeComponents();
        loadCameraData();
    }

    private void initializeComponents() {
        setTitle("Dashboard Admin - Selamat datang " + admin.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(155, 89, 182));

        JLabel welcomeLabel = new JLabel("Selamat datang, " + admin.getUsername() + " (Administrator)");
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

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Manajemen Kamera"));

        // Create table model
        String[] columnNames = {"ID", "Merek", "Tipe", "Harga (per hari)", "Stok"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only, editing through forms
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
        scrollPane.setPreferredSize(new Dimension(800, 350));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Admin Action Buttons Panel
        JPanel adminButtonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Tambah Kamera");
        editButton = new JButton("Edit Pilihan");
        deleteButton = new JButton("Hapus Pilihan");

        // Style buttons
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setPreferredSize(new Dimension(120, 35));

        editButton.setBackground(new Color(52, 152, 219));
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(120, 35));

        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(120, 35));

        adminButtonPanel.add(addButton);
        adminButtonPanel.add(editButton);
        adminButtonPanel.add(deleteButton);

        tablePanel.add(adminButtonPanel, BorderLayout.SOUTH);

        // Combine search and table panels
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        JLabel statusLabel = new JLabel("Siap - Total Kamera: " + tableModel.getRowCount());
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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCamera();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedCamera();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCamera();
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

        // Update status
        Component[] components = ((JPanel) getContentPane().getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JPanel) {
            JPanel statusPanel = (JPanel) components[0];
            if (statusPanel.getComponentCount() > 0 && statusPanel.getComponent(0) instanceof JLabel) {
                JLabel statusLabel = (JLabel) statusPanel.getComponent(0);
                statusLabel.setText("Siap - Total Kamera: " + tableModel.getRowCount());
            }
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

    private void addCamera() {
        CameraFormDialog dialog = new CameraFormDialog(this, "Tambah Kamera Baru", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String brand = dialog.getBrand();
            String type = dialog.getCameraType();
            double price = dialog.getPrice();
            int stock = dialog.getStock();

            if (cameraController.tambahKamera(brand, type, price, stock)) {
                JOptionPane.showMessageDialog(this, "Kamera berhasil ditambahkan!");
                loadCameraData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan kamera!");
            }
        }
    }

    private void editSelectedCamera() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih kamera yang ingin diedit!");
            return;
        }

        // Get current camera data
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String brand = (String) tableModel.getValueAt(selectedRow, 1);
        String type = (String) tableModel.getValueAt(selectedRow, 2);
        String priceStr = (String) tableModel.getValueAt(selectedRow, 3);
        double price = Double.parseDouble(priceStr.replace("Rp ", "").replace(",", ""));
        int stock = (Integer) tableModel.getValueAt(selectedRow, 4);

        Camera currentCamera = new Camera(id, brand, type, price, stock);
        CameraFormDialog dialog = new CameraFormDialog(this, "Edit Kamera", currentCamera);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String newBrand = dialog.getBrand();
            String newType = dialog.getCameraType();
            double newPrice = dialog.getPrice();
            int newStock = dialog.getStock();

            if (cameraController.ubahKamera(id, newBrand, newType, newPrice, newStock)) {
                JOptionPane.showMessageDialog(this, "Kamera berhasil diperbarui!");
                loadCameraData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui kamera!");
            }
        }
    }

    private void deleteSelectedCamera() {
        int selectedRow = cameraTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih kamera yang ingin dihapus!");
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String brand = (String) tableModel.getValueAt(selectedRow, 1);
        String type = (String) tableModel.getValueAt(selectedRow, 2);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus: " + brand + " " + type + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            if (cameraController.hapusKamera(id)) {
                JOptionPane.showMessageDialog(this, "Kamera berhasil dihapus!");
                loadCameraData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kamera!");
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

    // Inner class for Camera Form Dialog
    private class CameraFormDialog extends JDialog {
        private JTextField brandField, typeField, priceField, stockField;
        private JButton saveButton, cancelButton;
        private boolean confirmed = false;

        public CameraFormDialog(JFrame parent, String title, Camera camera) {
            super(parent, title, true);
            initializeDialog(camera);
        }

        private void initializeDialog(Camera camera) {
            setSize(350, 250);
            setLocationRelativeTo(getParent());
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            // Brand
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            add(new JLabel("Merek:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            brandField = new JTextField(20);
            add(brandField, gbc);

            // Type
            gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
            add(new JLabel("Tipe:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            typeField = new JTextField(20);
            add(typeField, gbc);

            // Price
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
            add(new JLabel("Harga/hari:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            priceField = new JTextField(20);
            add(priceField, gbc);

            // Stock
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
            add(new JLabel("Stok:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
            stockField = new JTextField(20);
            add(stockField, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            saveButton = new JButton("Simpan");
            cancelButton = new JButton("Batal");

            saveButton.setBackground(new Color(46, 204, 113));
            saveButton.setForeground(Color.WHITE);
            cancelButton.setBackground(new Color(149, 165, 166));
            cancelButton.setForeground(Color.WHITE);

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
            add(buttonPanel, gbc);

            // Pre-fill data if editing
            if (camera != null) {
                brandField.setText(camera.getBrand());
                typeField.setText(camera.getType());
                priceField.setText(String.valueOf(camera.getHargaRental()));
                stockField.setText(String.valueOf(camera.getStok()));
            }

            // Event listeners
            saveButton.addActionListener(e -> saveCamera());
            cancelButton.addActionListener(e -> dispose());
        }

        private void saveCamera() {
            if (validateFields()) {
                confirmed = true;
                dispose();
            }
        }

        private boolean validateFields() {
            if (brandField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Merek tidak boleh kosong!");
                return false;
            }
            if (typeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tipe tidak boleh kosong!");
                return false;
            }
            try {
                double price = Double.parseDouble(priceField.getText().trim());
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0!");
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Format harga tidak valid!");
                return false;
            }
            try {
                int stock = Integer.parseInt(stockField.getText().trim());
                if (stock < 0) {
                    JOptionPane.showMessageDialog(this, "Stok tidak boleh negatif!");
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Format stok tidak valid!");
                return false;
            }
            return true;
        }

        public boolean isConfirmed() { return confirmed; }
        public String getBrand() { return brandField.getText().trim(); }
        public String getCameraType() { return typeField.getText().trim(); }
        public double getPrice() { return Double.parseDouble(priceField.getText().trim()); }
        public int getStock() { return Integer.parseInt(stockField.getText().trim()); }
    }
}

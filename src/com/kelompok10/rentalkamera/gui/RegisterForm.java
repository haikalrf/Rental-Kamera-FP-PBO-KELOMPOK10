package com.kelompok10.rentalkamera.gui;

import javax.swing.JButton;
//import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kelompok10.rentalkamera.controller.AuthController;

public class RegisterForm extends JFrame {

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    //private JComboBox<String> roleBox;
    private JButton registerButton, backButton;
    private AuthController authController = new AuthController();

    public RegisterForm() {
        this.setTitle("Register Form");
        this.setSize(320, 260);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        this.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(110, 20, 160, 25);
        this.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 60, 80, 25);
        this.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(110, 60, 160, 25);
        this.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 100, 80, 25);
        this.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(110, 100, 160, 25);
        this.add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(20, 180, 100, 25);
        this.add(registerButton);

        backButton = new JButton("Back");
        backButton.setBounds(170, 180, 100, 25);
        this.add(backButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
                    return;
                }

                if (authController.register(username, email, password, "user")) {
                    JOptionPane.showMessageDialog(null, "Registrasi berhasil!");
                    new LoginForm().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Email sudah digunakan!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });
    }

}

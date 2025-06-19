package com.kelompok10.rentalkamera.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.kelompok10.rentalkamera.controller.AuthController;
import com.kelompok10.rentalkamera.dao.UserDAO;
import com.kelompok10.rentalkamera.model.Person;

public class LoginForm extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private AuthController authController = new AuthController();
    private UserDAO userDAO = new UserDAO();

    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 20, 160, 25);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 160, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(20, 110, 100, 25);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(160, 110, 100, 25);
        add(registerButton);

        // Action login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Email dan password harus diisi!");
                    return;
                }

                if (authController.login(email, password)) {
                    Person user = userDAO.getByEmail(email);
                    JOptionPane.showMessageDialog(null, "Login berhasil sebagai: " + user.getRole());

                    // Redirect to dashboard according to role
                    if (user.getRole().equalsIgnoreCase("admin")) {
                        new AdminDashboard(user).setVisible(true);
                    } else {
                        new UserDashboard(user).setVisible(true);
                    }

                    dispose(); // tutup form login
                } else {
                    JOptionPane.showMessageDialog(null, "Email atau password salah!");
                }
            }
        });

        // Action register
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterForm().setVisible(true);
                dispose();
            }
        });
    }
    
}

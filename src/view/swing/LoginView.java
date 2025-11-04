package view.swing;

import javax.swing.*;

import controller.UserController;
import model.Session;
import model.User;

import java.awt.*;

public class LoginView extends JDialog {
    private boolean authenticated = false;
    private boolean registerRequested = false;

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginView() {
        setTitle("Grade Songs System - Login");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Cadastre-se");
        buttonsPanel.add(loginBtn);
        buttonsPanel.add(registerBtn);

        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton forgotBtn = new JButton("Esqueci a senha");
        forgotPanel.add(forgotBtn);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            User user = new User(0);
            user.setEmail(email);
            user.setPassword(password);

            UserController controller = new UserController();
            boolean auth = controller.loginCheckPassword(user);
            if (auth) {
                int id = controller.loginGetId(user);
                user.setId(id);
                Session.setLoggedUser(user);
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener(e -> {
            registerRequested = true;
            dispose();
        });

        forgotBtn.addActionListener(e -> {
            ForgotPasswordView forgot = new ForgotPasswordView(this);
            forgot.setVisible(true);
        });

        JPanel centerPanel = new JPanel(new BorderLayout(0, 4));
        centerPanel.add(form, BorderLayout.CENTER);
        centerPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(forgotPanel, BorderLayout.CENTER);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(325, 180);
        setLocationRelativeTo(null);
    }

    public boolean isRegisterRequested() {
        return registerRequested;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

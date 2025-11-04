package view.swing;

import javax.swing.*;
import controller.UserController;
import model.User;
import java.awt.*;

public class RegisterView extends JDialog {
    private boolean authenticated = false;

    private final JTextField usernameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JTextField questionField = new JTextField(20);
    private final JTextField answerField = new JTextField(20);

    public RegisterView() {
        setTitle("Grade Songs System - Cadastro");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        form.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        form.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        form.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Pergunta de segurança:"), gbc);
        gbc.gridx = 1;
        form.add(questionField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(new JLabel("Resposta de segurança:"), gbc);
        gbc.gridx = 1;
        form.add(answerField, gbc);

        JPanel buttons = new JPanel();
        JButton registerBtn = new JButton("Cadastrar");
        JButton loginBtn = new JButton("Fazer login");
        buttons.add(registerBtn);
        buttons.add(loginBtn);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String question = questionField.getText().trim();
            String answer = answerField.getText().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() ||
                question.isEmpty() || answer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User(0);
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setSecurityQuestion(question);
            user.setSecurityAnswer(answer);

            UserController controller = new UserController();
            boolean exists = controller.registerCheckPassword(user);
            if (!exists) {
                authenticated = true;
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email já existente no banco.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
        });

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

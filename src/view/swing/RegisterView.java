package view.swing;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JDialog {
    private boolean authenticated = false;
    private final JTextField usernameField = new JTextField(50);
    private final JTextField emailField = new JTextField(50);
    private final JPasswordField passwordField = new JPasswordField(20);

    public RegisterView() {
        setTitle("Grade Songs System - Swing");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Cadastrar");
        JButton cancelBtn = new JButton("Fazer login");
        buttons.add(loginBtn);
        buttons.add(cancelBtn);

        loginBtn.addActionListener(e -> {
            String name = usernameField.getText();
            String email = emailField.getText();
            String senha = new String(passwordField.getPassword());
            
        });

        cancelBtn.addActionListener(e -> {
        	 SwingUtilities.invokeLater(() -> {
                 LoginView login = new LoginView();
                 login.setVisible(true);

                 this.setVisible(false);
                 if (login.isAuthenticated()) {
                     MainView mainView = new MainView();
                     mainView.setVisible(true);
                     mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
                 } else {
                     System.exit(0);
                 }
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


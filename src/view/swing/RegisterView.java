package view.swing;

import javax.swing.*;

import controller.UserController;
import model.ModelException;
import model.User;
import model.auth.RegisterAuthenticator;

import java.awt.*;

public class RegisterView extends JDialog {
    private boolean authenticated = false;
    private final JTextField usernameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public RegisterView() {
        setTitle("Grade Songs System - Cadastro");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton registerBtn = new JButton("Cadastrar");
        JButton loginBtn = new JButton("Fazer login");
        buttons.add(registerBtn);
        buttons.add(loginBtn);

        registerBtn.addActionListener(e -> {
        	 String username = usernameField.getText();
        	 String email = emailField.getText();
             String password = new String(passwordField.getPassword());
             
             User user = new User(0);
             user.setName(username);
             user.setEmail(email);
             user.setPassword(password);
             
             UserController controller = new UserController();
             boolean auth = controller.registerCheckPassword(user);
			if (auth == false) {
			     authenticated = true;
			     dispose();
			 }
			else {
	            JOptionPane.showMessageDialog(this, "Email já existente no banco", "Erro", JOptionPane.ERROR_MESSAGE);
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


package view.swing;

import javax.swing.*;

import model.ModelException;
import model.Session;
import model.User;
import model.auth.LoginAuthenticator;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.UserDAO;

import java.awt.*;

public class LoginView extends JDialog {
    private boolean authenticated = false;
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginView() {
        setTitle("Grade Songs System - Login");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Cadastre-se");
        buttons.add(loginBtn);
        buttons.add(registerBtn);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            User user = new User(0);
            user.setEmail(email);
            user.setPassword(password);
            
            LoginAuthenticator la = new LoginAuthenticator();
            boolean auth = false;
				try {
					auth = la.autheticathor(user);
				} catch (ModelException e1) {
	                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
            if (auth) {
        		UserDAO userDAO = DAOFactory.createUserDAO();
				int id = 0;
				try {
					id = userDAO.findByEmailId(user.getEmail());
				} catch (ModelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private boolean registerRequested = false;
    public boolean isRegisterRequested() {
        return registerRequested;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
}


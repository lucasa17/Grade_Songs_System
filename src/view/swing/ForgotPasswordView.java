package view.swing;

import javax.swing.*;
import controller.UserController;
import model.ModelException;
import model.User;
import java.awt.*;

public class ForgotPasswordView extends JDialog {
    private final JTextField emailField = new JTextField(20);
    private final JTextField answerField = new JTextField(20);
    private final JPasswordField newPasswordField = new JPasswordField(20);
    private final JLabel questionLabel = new JLabel("Pergunta de segurança:");
    private final JButton getQuestionButton = new JButton("Pegar pergunta");
    private final JButton resetButton = new JButton("Trocar senha");

    private final UserController controller = new UserController();

    public ForgotPasswordView(Window parent) {
        super(parent, "Esqueci a Senha", ModalityType.APPLICATION_MODAL);
        setSize(450, 300);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(getQuestionButton, gbc);
        gbc.gridx = 1;
        add(questionLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Resposta:"), gbc);
        gbc.gridx = 1;
        add(answerField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Nova senha:"), gbc);
        gbc.gridx = 1;
        add(newPasswordField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(resetButton, gbc);

        getQuestionButton.addActionListener(e -> {
            String email = emailField.getText();
            User user = null;
            try {
                user = controller.findByEmail(email);
            } catch (ModelException e1) {
                JOptionPane.showMessageDialog(this, "Email não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            if (user != null && user.getSecurityQuestion() != null) {
                questionLabel.setText(user.getSecurityQuestion());
            } else {
                JOptionPane.showMessageDialog(this, "Email não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> {
            String email = emailField.getText();
            String answer = answerField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            boolean success = controller.resetPasswordWithSecurityAnswer(email, answer, newPassword);

            if (success) {
                JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao alterar senha. Verifique a resposta da pergunta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

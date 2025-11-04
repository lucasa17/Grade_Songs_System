package controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.ModelException;
import model.User;
import model.auth.LoginAuthenticator;
import model.auth.PasswordHash;
import model.auth.RegisterAuthenticator;
import model.data.DAOFactory;
import model.data.UserDAO;

public class UserController extends JDialog{
	UserDAO userDAO = DAOFactory.createUserDAO();

	public boolean loginCheckPassword(User user) {
		LoginAuthenticator la = new LoginAuthenticator();
        boolean auth = false;
			try {
				auth = la.autheticathor(user);
			} catch (ModelException e1) {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		return auth;
	}
	
	public int loginGetId(User user) {
		int id = 0;
		try {
			id = userDAO.findByEmailId(user.getEmail());
		} catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return id;
	}
	
	public boolean registerCheckPassword(User user) {
		RegisterAuthenticator ra = new RegisterAuthenticator();
		boolean auth = true;
		try {
			auth = ra.autheticathor(user);
		} catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Erro ao checar registros", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return auth;
	}
	
	public void deleteUser(User user) {
        try {
            userDAO.delete(user);
            JOptionPane.showMessageDialog(this, "Usuário excluído", "Erro", JOptionPane.OK_OPTION);
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir usuário", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	public String searchNameUser(int id) {
		UserDAO userDAO = DAOFactory.createUserDAO();
		User user = new User(0);
        try {
			user = userDAO.findById(id);
		} catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Problema ao buscar usúario.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
		}
    return user.getName();
	}
	
	public User findByEmail(String email) throws ModelException {
		return userDAO.findUserByEmail(email);
	}
	
	public boolean resetPasswordWithSecurityAnswer(String email, String answer, String newPassword) {
	    try {
	        User user = userDAO.findUserByEmail(email);
	        if (user == null) {
	            JOptionPane.showMessageDialog(this, "Email não encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        if (user.getSecurityAnswer() == null || !user.getSecurityAnswer().equalsIgnoreCase(answer.trim())) {
	            return false;
	        }
	        PasswordHash ph = new PasswordHash();
	        newPassword = ph.getHash(newPassword);
	        boolean success = userDAO.updatePassword(email, newPassword);
	        if (success) {
	            return true;
	        } else {
	            return false;
	        }

	    } catch (ModelException e) {
	        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	}

}

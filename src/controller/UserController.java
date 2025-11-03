package controller;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.ModelException;
import model.User;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import model.data.UserDAO;

public class UserController extends JDialog{
	
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
}

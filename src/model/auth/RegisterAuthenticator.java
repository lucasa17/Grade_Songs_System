package model.auth;

import model.ModelException;
import model.User;
import model.data.DAOFactory;
import model.data.UserDAO;

public class RegisterAuthenticator {
	public boolean autheticathor(User user) throws ModelException {
		PasswordHash ps = new PasswordHash();
		
		String hashPassword = ps.getHash(user.getPassword());
		user.setPassword(hashPassword);
		
		UserDAO userDAO = DAOFactory.createUserDAO();
		boolean condition = userDAO.findByEmail(user.getEmail());
		
		if(condition == false) {
			userDAO.save(user);
		}
		return condition;
	}
}

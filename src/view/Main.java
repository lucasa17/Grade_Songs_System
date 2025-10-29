package view;

import java.util.List;

import model.ModelException;
import model.User;
import model.data.DAOFactory;
import model.data.UserDAO;

public class Main {
	
   public static void main(String[] args) throws ModelException {
		UserDAO userDAO = DAOFactory.createUserDAO();
	
		List<User> users = userDAO.findAll();
		
		for(User user : users) {
			System.out.println("Usuário: "+ user.getName());	
		}
	}	
}

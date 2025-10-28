package view;

import model.ModelException;
import model.User;
import model.data.DAOFactory;
import model.data.UserDAO;

public class Main {
	
   public static void main(String[] args) throws ModelException {
		UserDAO userDAO = DAOFactory.createUserDAO();
	
		User user = userDAO.findByEmail("lucas@email.com");
		
		user.setName("Lucas Rosa");
		userDAO.update(user);
		
		System.out.println(user.getName());
		
		
		
   }
}

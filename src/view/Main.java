package view;

import model.ModelException;
import model.User;
import model.data.DAOFactory;
import model.data.UserDAO;

public class Main {
	
   public static void main(String[] args) throws ModelException {
		UserDAO userDAO = DAOFactory.createUserDAO();
		User user = new User(1);
		user.setName("Lucas");
		user.setEmail("asassss");
		user.setPassword("awofhwf");
		
		userDAO.save(user);
		user = userDAO.findById(1);
		
		user.setName("Lucas Rosa");
		userDAO.update(user);
		
		user = userDAO.findById(1);
		
		userDAO.delete(user);
		
		
   }
}

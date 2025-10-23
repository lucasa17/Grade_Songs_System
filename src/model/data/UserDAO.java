package model.data;

import model.ModelException;
import model.User;

public interface UserDAO {
	void save(User user) throws ModelException;
	void update(User user) throws ModelException;
	void delete(User user) throws ModelException;
    User findById(int id) throws ModelException;
}

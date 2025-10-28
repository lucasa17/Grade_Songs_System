package model.data;

import java.util.List;

import model.ModelException;
import model.User;

public interface UserDAO {
	void save(User user) throws ModelException;
	void update(User user) throws ModelException;
	void delete(User user) throws ModelException;
    User findById(int id) throws ModelException;
    User findByEmail(String email) throws ModelException;
    List<User> findAll() throws ModelException;
}

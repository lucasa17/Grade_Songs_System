package model.data;

import model.ModelException;
import model.User;

public interface UserDAO {
	void save(User user) throws ModelException;
	void update(User user) throws ModelException;
	void delete(User user) throws ModelException;
    User findById(int id) throws ModelException;
    int findByEmailId(String email) throws ModelException;
    boolean findByEmail(String email) throws ModelException;
    User findUserByEmail(String email) throws ModelException;
    boolean findByEmailPassword(String email, String password) throws ModelException;
    boolean updatePassword(String email, String newPassword) throws ModelException;
}

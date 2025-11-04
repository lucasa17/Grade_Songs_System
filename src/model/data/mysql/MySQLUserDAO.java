package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ModelException;
import model.User;
import model.data.DAOUtils;
import model.data.UserDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLUserDAO implements UserDAO {
	@Override
	public void save(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlIsert = " INSERT INTO "
					        + " user VALUES "
					        + " (DEFAULT, ?, ?, ?, ?, ?); ";
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getSecurityQuestion());
			preparedStatement.setString(5, user.getSecurityAnswer());
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir user do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		}
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
	}
	@Override
	public void update(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlUpdate = " UPDATE user "
					         + " set "
					         + " username = ?, "
					         + " email = ? "
					         + " WHERE id_user = ?; ";
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setInt(3, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar user do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		}
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}		
	}
	
	 public boolean updatePassword(String email, String newPassword) throws ModelException {
        String sql = "UPDATE user SET user_password = ? WHERE email = ?";
        try (Connection conn = MySQLConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new ModelException("Erro ao mudar senha: " + e.getMessage());
        }
    }
	 
	@Override
	public void delete(User user) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlUpdate = " DELETE FROM user WHERE id_user = ?; ";
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setInt(1, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao deletar user do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		}
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}	
	}
	@Override
	public User findById(int id) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		User user = null;
		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM user WHERE id_user = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				String name = rs.getString("username");
				String email = rs.getString("email");
				user = new User(id);
				user.setName(name);
				user.setEmail(email);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar user por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return user;
	}
	
	public int findByEmailId(String email) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM user WHERE email = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return rs.getInt("id_user");
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar user por email e password no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return -1;
	}
	
	public boolean findByEmail(String email) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM user WHERE email = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, email);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar user por email e password no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return false;
	}
	
	public User findUserByEmail(String email) throws ModelException {
	    String sql = "SELECT * FROM user WHERE email = ?";
	    try (Connection conn = MySQLConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, email);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            User user = new User(rs.getInt("id_user"));
	            user.setName(rs.getString("username"));
	            user.setEmail(rs.getString("email"));
	            user.setPassword(rs.getString("user_password"));
	            user.setSecurityQuestion(rs.getString("security_question"));
	            user.setSecurityAnswer(rs.getString("security_answer"));
	            return user;
	        }
	        return null;
	    } catch (SQLException e) {
	        throw new ModelException("Erro ao buscar user: " + e.getMessage());
	    }
	}

	@Override
	public boolean findByEmailPassword(String email, String password) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean found = false;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM user WHERE email = ? and user_password = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				found = true;
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar user por email e password no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return found;
	}
}


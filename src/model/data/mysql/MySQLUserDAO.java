package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
					        + " (DEFAULT, ?, ?, ?); ";
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
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


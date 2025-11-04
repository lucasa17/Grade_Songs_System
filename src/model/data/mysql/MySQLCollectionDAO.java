package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Collection;
import model.ModelException;
import model.Session;
import model.User;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLCollectionDAO implements CollectionDAO{

	@Override
	public void save(Collection collection) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

	        collection.setUser(Session.getLoggedUser());
			String sqlIsert = "INSERT INTO collection VALUES (DEFAULT, ?, ?)";
			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, collection.getName());
			preparedStatement.setInt(2, collection.getUser().getId());
			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Collection do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} 
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}	
	}

	@Override
	public void update(Collection collection) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = "UPDATE collection SET collection_name = ?, id_user_fk = ? WHERE id_collection = ?";

			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, collection.getName());
			preparedStatement.setInt(2, collection.getUser().getId());
			preparedStatement.setInt(3, collection.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Collection do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}		
	}

	@Override
	public void delete(Collection collection) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();  

			String sqlDelete = "delete from collection where id_collection = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, collection.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Collection do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}		
	}
	
	public Collection findById(int collectionId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Collection collection = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM collection WHERE id_collection = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, collectionId);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("collection_name");
				int userId = rs.getInt("id_user_fk");

				collection = new Collection(collectionId);
				collection.setName(name);
				
				User user = new User(userId);
				collection.setUser(user);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar Collection por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return collection;
	}
	
	@Override
	public Collection findByName(String collectionName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Collection collection = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM collection WHERE collection_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, collectionName);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int collectionId = rs.getInt("id_collection");
				collection = new Collection(collectionId);
				collection.setName(collectionName);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar artist por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return collection;
	}
	
	@Override
	public List<Collection> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Collection> collectionList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			statement = connection.createStatement();
			String sqlSeletc = " SELECT * FROM collection order by collection_name asc";
			
			rs = statement.executeQuery(sqlSeletc);

			setUpCollections(rs, collectionList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Collections do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}

		return collectionList;
	}
	
	private void setUpCollections(ResultSet rs, List<Collection> collectionList)
            throws SQLException, ModelException {
		
		while (rs.next()) {
			int collectionId = rs.getInt("id_collection"); 
			String collectionName = rs.getString("collection_name");
			int userId = rs.getInt("id_user_fk");

			Collection newCollection = new Collection(collectionId);
			newCollection.setName(collectionName);

			User collectionUser = DAOFactory.createUserDAO().findById(userId);
			newCollection.setUser(collectionUser);
			
			collectionList.add(newCollection);
		}
	}

	@Override
	public boolean searchByName(String collectionName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM collection WHERE collection_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, collectionName);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar collection por nome no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return false;
	}

}

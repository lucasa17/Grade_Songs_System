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

			String sqlIsert = " INSERT INTO "
					        + " collection VALUES "
					        + " (DEFAULT, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, collection.getName());
			preparedStatement.setInt(2, collection.getUser().getId());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Coleção do BD.", sqle);
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
			
			String sqlUpdate = " UPDATE collection "
					+ " SET "
					+ " collection_name = ?, "
					+ " id_user_fk = ?, "
					+ " WHERE id_collection = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, collection.getName());
			preparedStatement.setInt(2, collection.getUser().getId());
			preparedStatement.setInt(3, collection.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Coleção do BD.", sqle);
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

			String sqlDelete = "delete from collection where id = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, collection.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Coleção do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}		
	}

	@Override
	public List<Collection> findById(int userId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Collection> collectionList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSeletc = " SELECT * FROM collection WHERE id_user_fk = ?; ";
			preparedStatement = connection.prepareStatement(sqlSeletc);
			preparedStatement.setInt(1, userId);

			rs = preparedStatement.executeQuery();

			setUpCollections(rs, collectionList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Coleção do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return collectionList;
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
			String sqlSeletc = " SELECT * FROM posts order by post_date desc ; ";

			rs = statement.executeQuery(sqlSeletc);

			setUpCollections(rs, collectionList);
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar posts do BD.", sqle);
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

}

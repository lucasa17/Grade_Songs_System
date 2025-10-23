package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import model.Album;
import model.ModelException;
import model.data.AlbumDAO;
import model.data.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLAlbumDAO implements AlbumDAO{

	@Override
	public void save(Album album) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlIsert = " INSERT INTO "
					        + " album VALUES "
					        + " (DEFAULT, ?, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, album.getName());
			preparedStatement.setInt(2, album.getYear());
			preparedStatement.setInt(3, album.getArtist().getId());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Album do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} 
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}	
	}

	@Override
	public void update(Album album) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE album "
					+ " SET "
					+ " album_name = ?, "
					+ "album_year = ?"
					+ " id_artist_fk = ?, "
					+ " WHERE id_album = ?; ";
			
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
	public void delete(Album album) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Album> findAll() throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Album> findById(int id) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

}

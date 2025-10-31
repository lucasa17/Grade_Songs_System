package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Album;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.data.AlbumDAO;
import model.data.DAOFactory;
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
					        + " (DEFAULT, ?, ?, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, album.getName());
			preparedStatement.setInt(2, album.getYear());
			preparedStatement.setInt(3, album.getCollection().getId());
			preparedStatement.setInt(4, album.getArtist().getId());

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
					+ " album_year = ?"
					+ " id_artist_fk = ?, "
					+ " WHERE id_album = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, album.getName());
			preparedStatement.setInt(2, album.getYear());
			preparedStatement.setInt(3, album.getArtist().getId());
			preparedStatement.setInt(4, album.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Album do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}				
	}

	@Override
	public void delete(Album album) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();  

			String sqlDelete = "delete from album where id_album = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, album.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Album do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}				
	}

	@Override
	public Album findById(int albumId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Album album = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM album WHERE id_album = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, albumId);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("album_name");
				int year = rs.getInt("album_year");
				int collectionId = rs.getInt("id_collection_fk");
				int artistId = rs.getInt("id_artist_fk");

				album = new Album(albumId);
				album.setName(name);
				album.setYear(year);
				
				Collection collection = new Collection(collectionId);
				album.setCollection(collection);
				
				Artist artist = new Artist(artistId);
				album.setArtist(artist);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar Album por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return album;
	}
	
	@Override
	public List<Album> findAllById(int collectionId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Album> albumList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSeletc = " SELECT * FROM album WHERE id_collection_fk = ?; ";
			preparedStatement = connection.prepareStatement(sqlSeletc);
			preparedStatement.setInt(1, collectionId);

			rs = preparedStatement.executeQuery();

			setUpAlbums(rs, albumList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Albums do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return albumList;
	}
	
	public List<Album> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Album> albumList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			statement = connection.createStatement();
			String sqlSeletc = " SELECT * FROM album";

			rs = statement.executeQuery(sqlSeletc);

			setUpAlbums(rs, albumList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Albums do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}

		return albumList;
	}
	
	private void setUpAlbums(ResultSet rs, List<Album> albumList) throws ModelException, SQLException {
		while (rs.next()) {
			int albumId = rs.getInt("id_album"); 
			String albumName = rs.getString("album_name");
			int albumYear = rs.getInt("album_year"); 
			int artistId = rs.getInt("id_artist_fk");
			int collectionId = rs.getInt("id_collection_fk");

			Album newAlbum = new Album(albumId);
			newAlbum.setName(albumName);
			newAlbum.setYear(albumYear);

			Collection albumCollection = DAOFactory.createCollectionDAO().findById(collectionId);
			newAlbum.setCollection(albumCollection);
			
			Artist albumArtist = DAOFactory.createArtistDAO().findById(artistId);
			newAlbum.setArtist(albumArtist);
			
			albumList.add(newAlbum);
		}		
	}
}

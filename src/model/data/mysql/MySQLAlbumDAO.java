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
			
			String sqlUpdate = " UPDATE album SET album_name = ?, album_year = ?, id_artist_fk = ? WHERE id_album = ?; ";
			
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
			String sqlSeletc = " SELECT * FROM album order by album_name asc";

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

	@Override
	public Album findByName(String albumName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Album album = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM album WHERE album_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, albumName);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int albumId = rs.getInt("id_album");
				album = new Album(albumId);
				album.setName(albumName);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar album por name no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return album;
	}

	@Override
	public boolean searchByName(String albumName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM album WHERE album_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, albumName);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar album por nome no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return false;
	}
	
	@Override
	public List<Album> findAllOrdered(String column, boolean ascending) throws ModelException {
        String order = ascending ? "ASC" : "DESC";

        String validColumn;
        switch (column) {
            case " album_name": validColumn = "a.album_name"; break;
            case " artist_name": validColumn = "ar.artist_name"; break;
            case " album_year": validColumn = "a.album_year"; break;
            case " collection_name": validColumn = "c.collection_name"; break;
            default: validColumn = " a.album_name";
        }

        String sql = " SELECT a.*, ar.id_artist, ar.artist_name, c.id_collection, c.collection_name"
        		+ " FROM album a"
        		+ " JOIN artist ar ON a.id_artist_fk = ar.id_artist"
        		+ " JOIN collection c ON a.id_collection_fk = c.id_collection"
        		+ " ORDER BY " + validColumn + " " + order;

        try (Connection conn = MySQLConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Album> albums = new ArrayList<>();
            while (rs.next()) {
                albums.add(extractAlbum(rs));
            }
            return albums;

        } catch (SQLException e) {
            throw new ModelException("Erro ao ordenar álbuns: " + e.getMessage());
        }
    }

	@Override
	public List<Album> findFiltered(String name, String artist, String collection) throws ModelException {
	    String sql = "SELECT a.*, ar.id_artist, ar.artist_name, c.id_collection, c.collection_name"
	    		+ " FROM album a"
	    		+ " JOIN artist ar ON a.id_artist_fk = ar.id_artist"
	    		+ " JOIN collection c ON a.id_collection_fk = c.id_collection"
	    		+ " WHERE a.album_name LIKE ? AND ar.artist_name LIKE ? AND c.collection_name LIKE ?"
	    		+ " ORDER BY a.album_name ASC ";

	    try (Connection conn = MySQLConnectionFactory.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, "%" + name + "%");
	        stmt.setString(2, "%" + artist + "%");
	        stmt.setString(3, "%" + collection + "%");

	        ResultSet rs = stmt.executeQuery();
	        List<Album> albums = new ArrayList<>();

	        while (rs.next()) {
	            albums.add(extractAlbum(rs));
	        }

	        return albums;

	    } catch (SQLException e) {
	        throw new ModelException("Erro ao filtrar álbuns: " + e.getMessage());
	    }
	}

	@Override
	public List<Album> findTopRatedAlbums(int limit) throws ModelException {
	        List<Album> allAlbums = findAll(); 
	        allAlbums.sort((a1, a2) -> Double.compare(a2.getAverageRating(), a1.getAverageRating()));
	        return allAlbums.subList(0, Math.min(limit, allAlbums.size()));
	}
	 
    private Album extractAlbum(ResultSet rs) throws SQLException {
        Artist artist = new Artist(rs.getInt("id_artist"));
        artist.setName(rs.getString("artist_name"));

        Collection collection = new Collection(rs.getInt("id_collection"));
        collection.setName(rs.getString("collection_name"));

        Album album = new Album(rs.getInt("id_album"));
        album.setName(rs.getString("album_name"));
        album.setYear(rs.getInt("album_year"));
        album.setArtist(artist);
        album.setCollection(collection);

        return album;
    }

   
}

package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Artist;
import model.Collection;
import model.ModelException;
import model.User;
import model.data.ArtistDAO;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLArtistDAO implements ArtistDAO{

	@Override
	public void save(Artist artist) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlIsert = " INSERT INTO "
					        + " artist VALUES "
					        + " (DEFAULT, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, artist.getName());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Artist do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} 
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}			
	}

	@Override
	public void update(Artist artist) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE artist "
					+ " SET "
					+ " artist_name = ?, "
					+ " WHERE id_artist = ?; ";
		
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, artist.getName());
			preparedStatement.setInt(2, artist.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Artist do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}						
	}

	@Override
	public void delete(Artist artist) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();  

			String sqlDelete = "delete from artist where id_artist = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, artist.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Artist do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}					
	}

	@Override
	public Artist findById(int artistId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Artist artist = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM artist WHERE id_artist = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, artistId);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("artist_name");

				artist = new Artist(artistId);
				artist.setName(name);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar artist por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return artist;
	}
	
	@Override
	public Artist findByName(String artistName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Artist artist = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM artist WHERE artist_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, artistName);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int artistId = rs.getInt("id_artist");
				artist = new Artist(artistId);
				artist.setName(artistName);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar artist por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return artist;
	}

	@Override
	public List<Artist> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Artist> artistList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			statement = connection.createStatement();
			String sqlSeletc = " SELECT * FROM artist";
			
			rs = statement.executeQuery(sqlSeletc);

			setUpArtists(rs, artistList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Artist do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}

		return artistList;
	}
	
	private void setUpArtists(ResultSet rs, List<Artist> artistList)
            throws SQLException, ModelException {
		
		while (rs.next()) {
			int artistId = rs.getInt("id_artist"); 
			String artistName = rs.getString("artist_name");

			Artist newArtist = new Artist(artistId);
			newArtist.setName(artistName);
			
			artistList.add(newArtist);
		}
	}

	@Override
	public boolean searchByName(String artistName) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = MySQLConnectionFactory.getConnection();
			String sqlSelect = "SELECT * FROM artist WHERE artist_name = ?";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, artistName);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar artist por nome no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}
		return false;
	}
}

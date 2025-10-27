package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Album;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.data.ArtistDAO;
import model.data.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLArtistDAO implements ArtistDAO{

	@Override
	public void save(Artist artist) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Artist artist) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Artist artist) throws ModelException {
		// TODO Auto-generated method stub
		
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
			DAOUtils.sqlExceptionTreatement("Erro ao buscar user por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return artist;
	}

	@Override
	public List<Artist> findAllById(int albumId) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

}

package model.data.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Artist;
import model.Feature;
import model.ModelException;
import model.Song;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.FeatureDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLFeatureDAO implements FeatureDAO{

	@Override
	public void save(Feature feature) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlIsert = " INSERT INTO "
					        + " feature VALUES "
					        + " (DEFAULT, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setInt(1, feature.getSong().getId());
			preparedStatement.setInt(2, feature.getArtist().getId());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Feature do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} 
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}			
	}

	@Override
	public void update(Feature feature) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE feature "
					+ " SET "
					+ " id_song_fk = ?, "
					+ " id_artist_fk = ?, "
					+ " WHERE id_feature = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setInt(1, feature.getSong().getId());
			preparedStatement.setInt(2, feature.getArtist().getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Feature do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}					
	}

	@Override
	public void delete(Feature feature) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();  

			String sqlDelete = "delete from feature where id_feature = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, feature.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Feature do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}		
	}

	@Override
	public Feature findById(int featureId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Feature feature = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM feature WHERE id_feature = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, featureId);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int songId = rs.getInt("id_song_fk");
				int artistId = rs.getInt("id_artist_fk");

				feature = new Feature(featureId);
				
				Song song = new Song(songId);
				feature.setSong(song);
				
				Artist artist = new Artist(artistId);
				feature.setArtist(artist);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar Feature por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return feature;
	}

	@Override
	public List<Feature> findAllById(int songId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Feature> featureList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSeletc = " SELECT * FROM feature WHERE id_song_fk = ?; ";
			preparedStatement = connection.prepareStatement(sqlSeletc);
			preparedStatement.setInt(1, songId);

			rs = preparedStatement.executeQuery();

			setUpFeatures(rs, featureList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Features do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return featureList;
	}

	private void setUpFeatures(ResultSet rs, List<Feature> featureList) throws ModelException, SQLException {
		while (rs.next()) {
			int featureId = rs.getInt("id_feature"); 
			int songId = rs.getInt("id_song_fk");
			int artistId = rs.getInt("id_artist_fk");
			
			Feature newFeature = new Feature(featureId);
			
			Song FeatureSong = DAOFactory.createSongDAO().findById(songId);
			newFeature.setSong(FeatureSong);
			
			Artist featureArtist = DAOFactory.createArtistDAO().findById(artistId);
			newFeature.setArtist(featureArtist);
			
			featureList.add(newFeature);
		}		
	}

}

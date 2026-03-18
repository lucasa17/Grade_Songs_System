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
import model.ModelException;
import model.Song;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.SongDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLSongDAO implements SongDAO{

	@Override
	public void save(Song song) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlIsert = " INSERT INTO "
					        + " song VALUES "
					        + " (DEFAULT, ?, ?, ?, ?); ";

			preparedStatement = connection.prepareStatement(sqlIsert);
			preparedStatement.setString(1, song.getName());
			preparedStatement.setInt(2, song.getGrade());
			preparedStatement.setString(3, song.getFeatures());
			preparedStatement.setInt(4, song.getAlbum().getId());

			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao inserir Song do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} 
		finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}			
	}

	@Override
	public void update(Song song) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = MySQLConnectionFactory.getConnection();
			
			String sqlUpdate = " UPDATE song SET song_name = ?, grade = ?, feature_name = ?, id_album_fk = ? WHERE id_song = ?; ";
			
			preparedStatement = connection.prepareStatement(sqlUpdate);
			preparedStatement.setString(1, song.getName());
			preparedStatement.setInt(2, song.getGrade());
			preparedStatement.setString(3, song.getFeatures());
			preparedStatement.setInt(4, song.getAlbum().getId());
			preparedStatement.setInt(5, song.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao atualizar Song do BD.", sqle);
		} catch (ModelException me) {
			throw me;
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}						
	}

	@Override
	public void delete(Song song) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = MySQLConnectionFactory.getConnection();  

			String sqlDelete = "delete from song where id_song = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, song.getId());

			preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao excluir Song do BD.", sqle);
		} finally {
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}					
	}

	@Override
	public Song findById(int songId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Song song = null;

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSelect = "SELECT * FROM song WHERE id_song = ?;";
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setInt(1, songId);

			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String name = rs.getString("song_name");
				String featuresName = rs.getString("feature_name");
				int grade = rs.getInt("grade");
				int albumId = rs.getInt("id_album_fk");

				song = new Song(songId);
				song.setName(name);
				song.setFeatures(featuresName);
				song.setGrade(grade);
				
				Album album = new Album(albumId);
				song.setAlbum(album);
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao buscar Song por id no BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return song;
	}

	@Override
	public List<Song> findAllById(int albumId) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Song> songList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			String sqlSeletc = " SELECT * FROM song WHERE id_album_fk = ? order by song_name asc ";
			preparedStatement = connection.prepareStatement(sqlSeletc);
			preparedStatement.setInt(1, albumId);

			rs = preparedStatement.executeQuery();

			setUpSongs(rs, songList);

		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar Songs do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return songList;
	}

	@Override
	public List<Song> findAll() throws ModelException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<Song> songList = new ArrayList<>();

		try {
			connection = MySQLConnectionFactory.getConnection();

			statement = connection.createStatement();
			String sqlSeletc = "SELECT * FROM song order by song_name asc ; ";

			rs = statement.executeQuery(sqlSeletc);

			setUpSongs(rs, songList);
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao carregar songs do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(statement);
			DAOUtils.close(connection);
		}

		return songList;	
	}
	
	private void setUpSongs(ResultSet rs, List<Song> songList) throws ModelException, SQLException {
		while (rs.next()) {
			int songId = rs.getInt("id_song"); 
			String songName = rs.getString("song_name");
			String featuresName = rs.getString("feature_name");
			int songGrade = rs.getInt("grade"); 
			int albumId = rs.getInt("id_album_fk");
			
			Song newSong = new Song(songId);
			newSong.setName(songName);
			newSong.setFeatures(featuresName);
			newSong.setGrade(songGrade);
			
			Album SongAlbum = DAOFactory.createAlbumDAO().findById(albumId);
			newSong.setAlbum(SongAlbum);
			
			songList.add(newSong);
		}		
	}
	
	@Override
	public List<Song> findAllOrdered(String column, boolean ascending) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Song> songList = new ArrayList<>();

		String order = ascending ? "ASC" : "DESC";
		String validColumn;

		switch (column) {
			case "song_name": validColumn = "s.song_name"; break;
			case "artist_name": validColumn = "ar.artist_name"; break;
			case "album_name": validColumn = "a.album_name"; break;
			case "grade": validColumn = "s.grade"; break;
			case "feature_name": validColumn = "s.feature_name"; break;
			default: validColumn = "s.song_name"; break;
		}

		String sqlSelect =
			"SELECT s.*, a.id_album, a.album_name, a.album_year, " +
			"ar.id_artist, ar.artist_name " +
			"FROM song s " +
			"JOIN album a ON s.id_album_fk = a.id_album " +
			"JOIN artist ar ON a.id_artist_fk = ar.id_artist " +
			"ORDER BY " + validColumn + " " + order + ";";

		try {
			connection = MySQLConnectionFactory.getConnection();
			preparedStatement = connection.prepareStatement(sqlSelect);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				songList.add(extractSong(rs));
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao ordenar músicas do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return songList;
	}

	@Override
	public List<Song> findFiltered(String name, String artist, String album) throws ModelException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Song> songList = new ArrayList<>();

		String sqlSelect =
				  "SELECT s.*, a.id_album, a.album_name, a.album_year, " +
				  "ar.id_artist, ar.artist_name " +
				  "FROM song s " +
				  "JOIN album a ON s.id_album_fk = a.id_album " +
				  "JOIN artist ar ON a.id_artist_fk = ar.id_artist " +
				  "WHERE s.song_name LIKE ? " +
				  "AND ar.artist_name LIKE ? " +
				  "AND a.album_name LIKE ? " +
				  "ORDER BY s.song_name ASC;";


		try {
			connection = MySQLConnectionFactory.getConnection();
			preparedStatement = connection.prepareStatement(sqlSelect);
			preparedStatement.setString(1, "%" + name + "%");
			preparedStatement.setString(2, "%" + artist + "%");
			preparedStatement.setString(3, "%" + album + "%");

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				songList.add(extractSong(rs));
			}
		} catch (SQLException sqle) {
			DAOUtils.sqlExceptionTreatement("Erro ao filtrar músicas do BD.", sqle);
		} finally {
			DAOUtils.close(rs);
			DAOUtils.close(preparedStatement);
			DAOUtils.close(connection);
		}

		return songList;
	}

	private Song extractSong(ResultSet rs) throws SQLException {
	    int albumId = rs.getInt("id_album");
	    Album album = new Album(albumId);
	    album.setName(rs.getString("album_name"));
	    album.setYear(rs.getInt("album_year"));

	    int artistId = rs.getInt("id_artist");
	    String artistName = null;
	    try {
	        artistName = rs.getString("artist_name");
	    } catch (SQLException ignored) {
	    }

	    Artist artist = new Artist(artistId);
	    artist.setName(artistName);
	    album.setArtist(artist);

	    Song s = new Song(rs.getInt("id_song"));
	    s.setName(rs.getString("song_name"));
	    s.setGrade(rs.getInt("grade"));
	    s.setFeatures(rs.getString("feature_name"));
	    s.setAlbum(album);

	    return s;
	}

	
	@Override
	public List<Song> findTopRatedSongs(int limit) throws ModelException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    List<Song> songList = new ArrayList<>();

	    String sqlSelect =
	        "SELECT s.*, a.id_album, a.album_name, a.album_year, ar.id_artist, ar.artist_name " +
	        "FROM song s " +
	        "JOIN album a ON s.id_album_fk = a.id_album " +
	        "JOIN artist ar ON a.id_artist_fk = ar.id_artist " +
	        "ORDER BY s.grade DESC, s.song_name ASC " +
	        "LIMIT ?;";

	    try {
	        connection = MySQLConnectionFactory.getConnection();
	        preparedStatement = connection.prepareStatement(sqlSelect);
	        preparedStatement.setInt(1, limit);
	        rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            songList.add(extractSong(rs));
	        }
	    } catch (SQLException sqle) {
	        DAOUtils.sqlExceptionTreatement("Erro ao buscar músicas mais bem avaliadas.", sqle);
	    } finally {
	        DAOUtils.close(rs);
	        DAOUtils.close(preparedStatement);
	        DAOUtils.close(connection);
	    }

	    return songList;
	}



}

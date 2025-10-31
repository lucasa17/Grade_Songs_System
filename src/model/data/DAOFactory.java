package model.data;

import model.data.mysql.MySQLAlbumDAO;
import model.data.mysql.MySQLArtistDAO;
import model.data.mysql.MySQLCollectionDAO;
import model.data.mysql.MySQLFeatureDAO;
import model.data.mysql.MySQLSongDAO;
import model.data.mysql.MySQLUserDAO;

public final class DAOFactory {
	public static UserDAO createUserDAO() {
		return new MySQLUserDAO();
	}
	
	public static CollectionDAO createCollectionDAO() {
		return new MySQLCollectionDAO();
	}

	public static AlbumDAO createAlbumDAO() {
		return new MySQLAlbumDAO();
	}
	
	public static ArtistDAO createArtistDAO() {
		return new MySQLArtistDAO();
	}
	
	public static SongDAO createSongDAO() {
		return new MySQLSongDAO();
	}
	
	public static FeatureDAO createFeatureDAO() {
		return new MySQLFeatureDAO();
	}
}

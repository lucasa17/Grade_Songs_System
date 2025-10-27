package model.data;

import model.data.mysql.MySQLArtistDAO;
import model.data.mysql.MySQLCollectionDAO;
import model.data.mysql.MySQLUserDAO;

public final class DAOFactory {
	public static UserDAO createUserDAO() {
		return new MySQLUserDAO();
	}
	
	public static CollectionDAO createCollectionDAO() {
		return new MySQLCollectionDAO();
	}

	public static ArtistDAO createArtistDAO() {
		return new MySQLArtistDAO();
	}
}

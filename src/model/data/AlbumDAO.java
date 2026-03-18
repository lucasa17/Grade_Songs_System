package model.data;

import java.util.List;

import model.Album;
import model.ModelException;

public interface AlbumDAO {
	void save(Album album) throws ModelException;
	void update(Album album) throws ModelException;
	void delete(Album album) throws ModelException;
	Album findByName(String albumName) throws ModelException;
	boolean searchByName(String albumName) throws ModelException;
	Album findById(int albumId) throws ModelException;
	List<Album> findAllById(int collectionId) throws ModelException;
	List<Album> findAll() throws ModelException;
	List<Album> findAllOrdered(String column, boolean ascending) throws ModelException;
	List<Album> findFiltered(String name, String artist, String collection) throws ModelException;
	List<Album> findTopRatedAlbums(int limit) throws ModelException;
}

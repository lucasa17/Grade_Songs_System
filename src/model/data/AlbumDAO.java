package model.data;

import java.util.List;

import model.Album;
import model.Artist;
import model.ModelException;

public interface AlbumDAO {
	void save(Album album) throws ModelException;
	void update(Album album) throws ModelException;
	void delete(Album album) throws ModelException;
	Album findById(int albumId) throws ModelException;
	List<Album> findAllById(int collectionId) throws ModelException;
}

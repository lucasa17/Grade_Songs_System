package model.data;

import java.util.List;

import model.Album;
import model.ModelException;

public interface AlbumDAO {
	void save(Album album) throws ModelException;
	void update(Album album) throws ModelException;
	void delete(Album album) throws ModelException;
	List<Album> findAll() throws ModelException;
	Album findById(int id) throws ModelException;
}

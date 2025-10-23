package model.data;

import java.util.List;

import model.ModelException;
import model.Song;

public interface SongDAO {
	void save(Song song) throws ModelException;
	void update(Song song) throws ModelException;
	void delete(Song song) throws ModelException;
	List<Song> findAll() throws ModelException;
	List<Song> findById(int id) throws ModelException;
}

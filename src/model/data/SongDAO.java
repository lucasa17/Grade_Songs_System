package model.data;

import java.util.List;

import model.Artist;
import model.ModelException;
import model.Song;

public interface SongDAO {
	void save(Song song) throws ModelException;
	void update(Song song) throws ModelException;
	void delete(Song song) throws ModelException;
	Song findById(int songId) throws ModelException;
	List<Song> findAllById(int albumId) throws ModelException;
}

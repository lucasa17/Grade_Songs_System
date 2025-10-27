package model.data;

import java.util.List;

import model.Artist;
import model.ModelException;

public interface ArtistDAO {
	void save(Artist artist) throws ModelException;
	void update(Artist artist) throws ModelException;
	void delete(Artist artist) throws ModelException;
	Artist findById(int artistId) throws ModelException;
	List<Artist> findAllById(int albumId) throws ModelException;
}

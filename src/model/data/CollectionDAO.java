package model.data;

import java.util.List;

import model.Collection;
import model.ModelException;
import model.Song;

public interface CollectionDAO {
	void save(Collection collection) throws ModelException;
	void update(Collection collection) throws ModelException;
	void delete(Collection collection) throws ModelException;
	Collection findById(int collectionId) throws ModelException;
	Collection findByName(String collectionName) throws ModelException;
	List<Collection> findAll() throws ModelException;
}
	
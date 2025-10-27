package model.data;

import java.util.List;

import model.Collection;
import model.ModelException;

public interface CollectionDAO {
	void save(Collection collection) throws ModelException;
	void update(Collection collection) throws ModelException;
	void delete(Collection collection) throws ModelException;
	Collection findById(int collectionId) throws ModelException;
	List<Collection> findAllById(int userId) throws ModelException;
}
	
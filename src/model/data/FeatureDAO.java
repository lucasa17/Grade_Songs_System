package model.data;

import java.util.List;

import model.Feature;
import model.ModelException;

public interface FeatureDAO {
	void save(Feature feature) throws ModelException;
	void update(Feature feature) throws ModelException;
	void delete(Feature feature) throws ModelException;
	List<Feature> findAll() throws ModelException;
    Feature findById(int id) throws ModelException;
}

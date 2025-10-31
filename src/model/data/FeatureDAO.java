package model.data;

import java.util.List;

import model.Feature;
import model.ModelException;

public interface FeatureDAO {
	void save(Feature feature) throws ModelException;
	void update(Feature feature) throws ModelException;
	void delete(Feature feature) throws ModelException;
	Feature findByName(String featureName) throws ModelException;
	boolean searchByName(String featureName) throws ModelException;
	Feature findById(int featureId) throws ModelException;
	List<Feature> findAllById(int songId) throws ModelException;
	List<Feature> findAll() throws ModelException;

}

package model;

import java.util.List;

public class Song {
	private int id;
	private String name;
	private int grade;
	private List<Feature> features;
	
	public Song(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public int getId() {
		return id;
	}
	
}

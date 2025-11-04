package model;

public class Song {
	private int id;
	private String name;
	private int grade;
	private Album album;
	private String features;
	
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
	
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public int getId() {
		return id;
	}
	
	public void validate() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome da música não pode ser vazio.");
		}
		if (grade < 0) {
			setGrade(0);
		}
		if (grade > 100) {
			setGrade(100);
		}
		if (features == null || features.isBlank()) {
			features = " ";
		}
		if(album == null) {
			throw new IllegalArgumentException("O nome do album não pode ser vazio.");
		}
	}
	
	@Override
	public String toString() {
	    return this.getName();
	}
}

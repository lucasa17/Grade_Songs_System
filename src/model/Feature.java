package model;

public class Feature {
	private int id;
	private String name;
	private Song song;
	
	public Feature(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public int getId() {
		return id;
	}
}

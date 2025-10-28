package model;

public class Feature {
	private int id;
	private Artist artist;
	private Song song;
	
	public Feature(int id) {
		this.id = id;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
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

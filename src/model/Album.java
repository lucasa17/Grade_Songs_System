package model;

import java.util.List;

public class Album {
	private int id;
	private String name;
	private int year;
	private Artist artist;
	private List<Song> songs;
	
	public Album(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setUser(Artist artist) {
		this.artist = artist;
	}
	
	public int getId() {
		return id;
	}
}

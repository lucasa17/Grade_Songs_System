package model;

import java.util.List;

public class Album {
	private int id;
	private String name;
	private int year;
	private Collection collection;
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
	
	public Collection getCollection() {
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	public int getId() {
		return id;
	}
	
	public void validate() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome do album não pode ser vazio.");
		}
		if (year <=0) {
			throw new IllegalArgumentException("O ano não pode ser negativo");
		}
		if (collection == null) {
			throw new IllegalArgumentException("O nome da coelção não pode ser vazio.");
		}
		if (artist == null) {
			throw new IllegalArgumentException("O nome do artista não pode ser vazio.");
		}
	}
	
	@Override
	public String toString() {
	    return this.getName();
	}
}

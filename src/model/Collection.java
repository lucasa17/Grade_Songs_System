package model;

import java.util.List;

public class Collection {
	private int id;
	private String name;
	private User user;
	private List<Album> albums;
	
	public Collection(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	
}

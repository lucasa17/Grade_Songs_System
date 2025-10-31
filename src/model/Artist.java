package model;

public class Artist {
	private int id;
	private String name;
	
	public Artist(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
	    return this.getName(); // ou o campo que representa o nome da coleção
	}
}

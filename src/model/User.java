package model;

import java.util.List;

public class User {
	private int id;
	private String name;
	private String email;
	private String password;
	private List<Collection> collections;
	
	public User(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Collection> getCollections() {
		return collections;
	}

	public void setCollections(List<Collection> collections) {
		this.collections = collections;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}
	
	public void validate() {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("O nome do usuário não pode ser vazio.");
		}

		if (email == null || email.isBlank() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			throw new IllegalArgumentException("O email do usuário é inválido.");
		}	
	}
	
	@Override
	public String toString() {
		return name;
	}
}
package main.java.id;

public class StringId implements Id {
	private String id;
	StringId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}

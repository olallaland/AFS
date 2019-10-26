package main.java.id;

import java.io.Serializable;

public class StringId implements Id, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5682627581288256691L;
	private String id;
	public StringId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
}

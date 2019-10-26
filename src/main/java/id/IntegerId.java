package main.java.id;

import java.io.Serializable;

public class IntegerId implements Id, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5870339272621676893L;
	private int id;
	public IntegerId(int id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
}

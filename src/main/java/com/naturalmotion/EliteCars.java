package com.naturalmotion;

import java.util.List;

public class EliteCars {

	private static final EliteCars INSTANCE = new EliteCars();

	private List<String> eliteCars;

	private EliteCars() {
	}

	public static EliteCars getInstance() {
		return INSTANCE;
	}

	public List<String> getEliteCars() {
		return eliteCars;
	}

	public void setEliteCars(List<String> eliteCars) {
		this.eliteCars = eliteCars;
	}

}

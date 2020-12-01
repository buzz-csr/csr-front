package com.naturalmotion.token;

import java.util.regex.Pattern;

public class TokenUser {

	private Pattern pattern = Pattern.compile(":");

	private String user;

	private String dir;

	public TokenUser(String user, String dir) {
		this.user = user;
		this.dir = dir;
	}

	public TokenUser(String string) {
		String[] split = pattern.split(string);
		user = split[0];
		dir = split[1];
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	@Override
	public String toString() {
		return user + ":" + dir;
	}

}

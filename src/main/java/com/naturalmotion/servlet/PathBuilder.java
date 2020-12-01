package com.naturalmotion.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.naturalmotion.Configuration;
import com.naturalmotion.token.TokenManager;
import com.naturalmotion.token.TokenUser;

public class PathBuilder {

	private static final String SEPARATOR = "/";

	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String build(HttpServletRequest req) throws IOException {
		String token = req.getParameter("token");
		return buildFromToken(token);
	}

	public String buildFromToken(String token) throws IOException {
		TokenUser from = new TokenManager().from(token);
		return createPath(from);
	}

	private String createPath(TokenUser tokenUser) throws IOException {
		Configuration configuration = new Configuration();
		String path = configuration.getString("working.directory");
		return path + SEPARATOR + tokenUser.getUser() + SEPARATOR + createDirName(tokenUser);
	}

	private String createDirName(TokenUser tokenUser) {
		return formater.format(new Date(Long.valueOf(tokenUser.getDir())));
	}
}

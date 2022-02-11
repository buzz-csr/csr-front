package com.naturalmotion.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.webservice.configuration.ConfigSession;
import com.naturalmotion.webservice.configuration.Configuration;
import com.naturalmotion.webservice.configuration.json.Auth;

import csr.Checksum;

public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 6058178301572297415L;

	private Configuration configuration = new Configuration();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ConfigSession configSession = new ConfigSession(configuration);

		Map<String, String> result = new HashMap<>();

		Map<String, Auth> list = configSession.get();

		Checksum checksum = new Checksum();
		String computeHmac = null;
		for (String user : list.keySet()) {
			computeHmac = checksum.computeHmac(user);
			result.put(user, createUrl(computeHmac));
		}

		resp.getWriter().write(new ObjectMapper().writeValueAsString(result));
	}

	private String createUrl(String computeHmac) {
		return configuration.getString("server.mod.url") + "?userId=" + computeHmac;
	}

}

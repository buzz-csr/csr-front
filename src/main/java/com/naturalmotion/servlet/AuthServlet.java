package com.naturalmotion.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import csr.session.SessionService;

public class AuthServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -6496670268972988191L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		String type = req.getParameter("type");

		if ("license".equals(type)) {
			String auth = new SessionService().getLicenseKey(req.getParameter("playerId"));
			resp.getWriter().write("{ \"key\" : \"" + auth + "\"}");
		} else {
			String auth = new SessionService().getAuth(req.getParameter("playerId"));
			resp.getWriter().write("{ \"auth\" : \"" + auth + "\"}");
		}
	}

}

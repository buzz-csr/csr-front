package com.naturalmotion.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.csr_api.service.check.CheckCarsServiceImpl;
import com.naturalmotion.csr_api.service.check.CheckReport;
import com.naturalmotion.csr_api.service.io.NsbException;

public class CheckServlet extends HttpServlet {

	private static final long serialVersionUID = -5041082194915733049L;

	private final Logger log = LoggerFactory.getLogger(CheckServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			List<CheckReport> reports = new CheckCarsServiceImpl().check(new PathBuilder().build(req));

			ObjectMapper objectMapper = new ObjectMapper();
			resp.getWriter().write(objectMapper.writeValueAsString(reports));
		} catch (NsbException | IOException e) {
			log.error("Error checking cars", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			List<CheckReport> reports = new CheckCarsServiceImpl().correct(new PathBuilder().build(req));

			ObjectMapper objectMapper = new ObjectMapper();
			resp.getWriter().write(objectMapper.writeValueAsString(reports));
		} catch (NsbException | IOException e) {
			log.error("Error checking cars", e);
		}
	}

}

package com.naturalmotion.servlet;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.csr_api.service.car.CarServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;

public class DebanServlet extends HttpServlet {

	private static final long serialVersionUID = 6765684344918999835L;

	private final Logger log = LoggerFactory.getLogger(DebanServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String path = new PathBuilder().build(req);
			JsonObject spent = new ProfileUpdaterFileImpl(path).deban();
			JsonArray eliteCars = new CarServiceFileImpl(path).removeEliteLevel();

			JsonObjectBuilder result = Json.createObjectBuilder();
			result.add("eliteTokenSpent", spent);
			result.add("eliteCars", eliteCars);

			resp.getWriter().write(result.build().toString());
		} catch (IOException | NsbException e) {
			log.error("Error deban profile", e);
		}
	}

}

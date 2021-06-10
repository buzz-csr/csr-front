package com.naturalmotion.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.io.NsbReader;
import com.naturalmotion.csr_api.service.io.ScbReader;
import com.naturalmotion.token.TokenManager;
import com.naturalmotion.webservice.api.CrewResources;
import com.naturalmotion.webservice.configuration.Configuration;
import com.naturalmotion.webservice.service.auth.Authorization;
import com.naturalmotion.webservice.service.auth.AuthorizationFactory;

public class GetFileV2 extends HttpServlet {

	private static final long serialVersionUID = 5739502846788943953L;

	private final Logger log = LoggerFactory.getLogger(GetFileV2.class);

	private CrewResources crewResources = new CrewResources();

	private AuthorizationFactory authorizationFactory = new AuthorizationFactory();

	private Configuration configuration = new Configuration();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=UTF-8");

		String userId = req.getParameter("userId");
		String resultUser = new UserResolver(configuration).findUserFromId(userId);
		if (resultUser == null) {
			writeError(resp, "utilisateur inconnu");
		} else {
			try (PrintWriter writer = resp.getWriter();) {
				String token = new TokenManager().create(resultUser);
				String path = new PathBuilder().buildFromToken(token);

				Authorization auth = authorizationFactory.get(resultUser);
				JsonObject profile = crewResources.getFullProfile(auth);

				File editedDir = new File(path + "/Edited");
				editedDir.mkdirs();

				createFile(editedDir.getPath(), profile, "nsb");
				createFile(editedDir.getPath(), profile, "scb");

				String content = new NsbFormatter().getFileContent(path);

				writer.write("{ \"token\": \"" + token + "\", \"nsb\": " + content + "}");
			} catch (Exception e) {
				log.error("Error reading profile files", e);
			}
		}
	}

	private void createFile(String path, JsonObject profile, String fileName) throws IOException {
		String fullFileName = path + "/" + fileName + ".json";
		File file = new File(fullFileName);
		file.createNewFile();

		try (BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));) {
			JsonObject jsonObject = profile.getJsonObject(fileName);
			bwriter.write(jsonObject.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String error = null;
		String userId = req.getParameter("userId");

		String resultUser = new UserResolver(configuration).findUserFromId(userId);
		if (resultUser == null) {
			error = "utilisateur inconnu";
		} else {

			Authorization auth = authorizationFactory.get(resultUser);

			String path = new PathBuilder().build(req);
			NsbReader nsbReader = new NsbReader();
			ScbReader scbReader = new ScbReader();

			try {
				crewResources.updateProfile(auth, nsbReader.getNsbFile(path), scbReader.getScbFile(path));
			} catch (NsbException e) {
				log.error("Error updating profile", e);
				error = "Problème inconnu";
			}
		}

		resp.setContentType("application/json; charset=UTF-8");
		writeError(resp, error);

	}

	private void writeError(HttpServletResponse resp, String error) throws IOException {
		try (PrintWriter writer = resp.getWriter();) {
			if (error != null) {
				writer.write("{ \"error\": \"" + error + "\"}");
			} else {
				writer.write("{ \"status\": \"OK\"}");
			}
		}
	}

}

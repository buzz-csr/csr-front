package com.naturalmotion.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.json.Json;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.csr_api.api.EliteTokenParam;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;
import com.naturalmotion.token.TokenManager;

import csr.Extract;

@MultipartConfig
public class Upload extends HttpServlet {

	private static final String ORIGINAL = "Original";

	private static final long serialVersionUID = -1637365817304780292L;

	private final Logger log = LoggerFactory.getLogger(Upload.class);

	private static final String SEPARATOR = "/";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

		String tokens = req.getParameter("tokens");
		if (tokens != null) {
			uploadForDeban(req, tokens);
		} else {
			uploadNewFile(req, resp);
		}
	}

	public void uploadForDeban(HttpServletRequest req, String tokens) {
		try (JsonReader jsonReader = Json.createReader(new StringReader(tokens));) {
			List<EliteTokenParam> params = new EliteTokenParamFactory().build(req);
			new ProfileUpdaterFileImpl(new PathBuilder().build(req)).updateResourceAfterBan(params);
		} catch (NsbException | IOException e) {
			log.error("Error updating elite token after deban", e);
		}
	}

	public void uploadNewFile(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String user = req.getParameter("user");
			String token = new TokenManager().create(user);
			String path = new PathBuilder().buildFromToken(token);
			File actualDir = new File(path + SEPARATOR + ORIGINAL + SEPARATOR);
			actualDir.mkdirs();

			for (Part part : req.getParts()) {
				try (InputStream stream = part.getInputStream()) {
					byte[] buffer = new byte[stream.available()];
					if (stream.read(buffer) > 0) {
						File target = new File(actualDir.getPath() + SEPARATOR + getFileName(part));
						try (OutputStream outStream = new FileOutputStream(target)) {
							outStream.write(buffer);
						}
					}
				}
			}

			new Extract().unzipAll(path + SEPARATOR);
			resp.getWriter().write("{\"token\": \"" + token + "\"}");
		} catch (IOException | ServletException | InvalidKeyException | NoSuchAlgorithmException e) {
			log.error("Error uploading files", e);
		}
	}

	private String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename"))
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
		}
		return "Default.file";
	}

}

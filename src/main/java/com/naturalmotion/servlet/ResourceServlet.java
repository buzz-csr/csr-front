package com.naturalmotion.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.csr_api.api.ResourceType;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.updater.ProfileUpdater;
import com.naturalmotion.csr_api.service.updater.ProfileUpdaterFileImpl;
import com.naturalmotion.csr_api.service.updater.UpdaterException;

public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = -2698675847367966977L;

	private final Logger log = LoggerFactory.getLogger(ResourceServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		try {
			String path = new PathBuilder().build(req);

			String type = req.getParameter("type");
			if ("cash".equals(type)) {
				addCashOr(path, req, resp);
			} else if ("elite".equals(type)) {
				addEliteTokens(path, req, resp);
			} else {
				addKeys(path, req, resp);
			}
		} catch (IOException e) {
			log.error("Error adding resources", e);
		}
	}

	private void addEliteTokens(String path, HttpServletRequest req, HttpServletResponse resp) {
		try {
			new ProfileUpdaterFileImpl(path).updateEliteTokens(new EliteTokenParamFactory().build(req));

			resp.getWriter().write(new NsbFormatter().getFileContent(path));
		} catch (IOException | NsbException e) {
			log.error("Error updating elite tokens", e);
		}
	}

	public void addKeys(String path, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String bronze = req.getParameter("bronze");
			String gold = req.getParameter("gold");
			String silver = req.getParameter("silver");

			ProfileUpdater profileUpdater = new ProfileUpdaterFileImpl(path);
			profileUpdater.updateResource(ResourceType.BRONZE_KEY, new BigDecimal(bronze));
			profileUpdater.updateResource(ResourceType.GOLD_KEY, new BigDecimal(gold));
			profileUpdater.updateResource(ResourceType.SILVER_KEY, new BigDecimal(silver));

			resp.getWriter().write(new NsbFormatter().getFileContent(path));
		} catch (IOException | UpdaterException | NsbException e) {
			log.error("Error adding keys", e);
		}
	}

	public void addCashOr(String path, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String cash = req.getParameter("cash");
			String gold = req.getParameter("gold");

			ProfileUpdater profileUpdater = new ProfileUpdaterFileImpl(path);
			profileUpdater.updateResource(ResourceType.CASH, new BigDecimal(cash));
			profileUpdater.updateResource(ResourceType.GOLD, new BigDecimal(gold));
			resp.getWriter().write(new NsbFormatter().getFileContent(path));
		} catch (IOException | UpdaterException | NsbException e) {
			log.error("Error adding cash/or", e);
		}
	}

}

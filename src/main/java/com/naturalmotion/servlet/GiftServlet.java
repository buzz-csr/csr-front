package com.naturalmotion.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naturalmotion.Configuration;
import com.naturalmotion.csr_api.api.EliteToken;
import com.naturalmotion.csr_api.api.EliteTokenParam;
import com.naturalmotion.csr_api.api.FusionColor;
import com.naturalmotion.csr_api.api.FusionParam;
import com.naturalmotion.csr_api.service.car.CarException;
import com.naturalmotion.csr_api.service.gift.GiftService;
import com.naturalmotion.csr_api.service.gift.GiftServiceFileImpl;
import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.reader.ProfileReaderFileImpl;

public class GiftServlet extends HttpServlet {

	private static final long serialVersionUID = 6868517541785832236L;
	private static final String SEPARATOR = "/";

	private final Logger log = LoggerFactory.getLogger(CarServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String action = req.getParameter("action");
		if ("fuel".equals(action)) {
			addFuel(req);
		} else if ("fusions".equals(action)) {
			addFusions(req);
		} else if ("elite".equals(action)) {
			addEliteTokens(req);
		} else if ("restoration".equals(action)) {
			addRestorationTokens(req);
		} else if ("stage6".equals(action)) {
			addStage6(req);
		}
	}

	private void addStage6(HttpServletRequest req) {
		try {
			GiftService service = new GiftServiceFileImpl(new PathBuilder().build(req));
			service.addStage6(req.getParameter("carId"));
		} catch (IOException | NsbException e) {
			log.error("Error adding restoration tokens", e);
		}
	}

	private void addRestorationTokens(HttpServletRequest req) {
		try {
			GiftService service = new GiftServiceFileImpl(new PathBuilder().build(req));
			service.addRestorationToken(req.getParameter("carId"), new BigDecimal(req.getParameter("qty")));
		} catch (IOException | NsbException e) {
			log.error("Error adding restoration tokens", e);
		}
	}

	private void addEliteTokens(HttpServletRequest req) {
		try {
			String dir = req.getParameter("dir");
			String user = req.getParameter("user");
			Configuration configuration = new Configuration();
			String path = configuration.getString("working.directory");

			GiftService service = new GiftServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir);
			List<EliteTokenParam> tokens = new ArrayList<>();
			addEliteParam(tokens, req.getParameter("green"), EliteToken.GREEN);
			addEliteParam(tokens, req.getParameter("blue"), EliteToken.BLUE);
			addEliteParam(tokens, req.getParameter("red"), EliteToken.RED);
			addEliteParam(tokens, req.getParameter("yellow"), EliteToken.YELLOW);
			service.addEliteToken(tokens);
		} catch (NsbException | IOException e) {
			log.error("Error adding elite tokens", e);
		}
	}

	private void addFusionParam(List<FusionParam> params, String qty, FusionColor color) {
		if (StringUtils.isNotBlank(qty)) {
			params.add(new FusionParam(color, new BigDecimal(qty)));
		}
	}

	private void addEliteParam(List<EliteTokenParam> params, String qty, EliteToken token) {
		if (StringUtils.isNotBlank(qty) && new BigDecimal(qty).compareTo(BigDecimal.ZERO) > 0) {
			params.add(new EliteTokenParam(token, new BigDecimal(qty)));
		}
	}

	private void addFusions(HttpServletRequest req) {
		List<FusionParam> params = new ArrayList<>();
		addFusionParam(params, req.getParameter("green"), FusionColor.GREEN);
		addFusionParam(params, req.getParameter("blue"), FusionColor.BLUE);
		addFusionParam(params, req.getParameter("red"), FusionColor.RED);

		try {
			String dir = req.getParameter("dir");
			String user = req.getParameter("user");
			String brand = req.getParameter("brand");
			Configuration configuration = new Configuration();
			String path = configuration.getString("working.directory");

			GiftService service = new GiftServiceFileImpl(path + SEPARATOR + user + SEPARATOR + dir);
			List<String> brands = null;
			if ("-- TOUTES --".equals(brand)) {
				brands = new ProfileReaderFileImpl(path + SEPARATOR + user + SEPARATOR + dir).getBrands();
			} else {
				brands = Arrays.asList(brand);
			}
			service.addFusions(params, brands);
		} catch (IOException | NsbException e) {
			log.error("Error adding fusions", e);
		}
	}

	private void addFuel(HttpServletRequest req) {
		try {
			GiftService service = new GiftServiceFileImpl(new PathBuilder().build(req));
			BigDecimal qty = new BigDecimal(req.getParameter("qty"));
			service.addEssence(qty);
		} catch (IOException | CarException | NsbException e) {
			log.error("Error adding fuel gift", e);
		}
	}
}

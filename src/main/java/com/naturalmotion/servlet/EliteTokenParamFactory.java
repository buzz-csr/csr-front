package com.naturalmotion.servlet;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;

import com.naturalmotion.csr_api.api.EliteToken;
import com.naturalmotion.csr_api.api.EliteTokenParam;

public class EliteTokenParamFactory {

	public List<EliteTokenParam> build(HttpServletRequest request) {
		List<EliteTokenParam> params;
		try (JsonReader jsonReader = Json.createReader(new StringReader(request.getParameter("tokens")));) {
			JsonObject object = jsonReader.readObject();
			params = createEliteParams(object);
		}
		return params;
	}

	private List<EliteTokenParam> createEliteParams(JsonObject object) {
		List<EliteTokenParam> params = new ArrayList<>();
		BigDecimal green = getExpected(object, "Green");
		if (green.compareTo(BigDecimal.ZERO) >= 0) {
			params.add(new EliteTokenParam(EliteToken.GREEN, green));
		}
		BigDecimal blue = getExpected(object, "Blue");
		if (blue.compareTo(BigDecimal.ZERO) >= 0) {
			params.add(new EliteTokenParam(EliteToken.BLUE, blue));
		}
		BigDecimal red = getExpected(object, "Red");
		if (red.compareTo(BigDecimal.ZERO) >= 0) {
			params.add(new EliteTokenParam(EliteToken.RED, red));
		}
		BigDecimal yellow = getExpected(object, "Yellow");
		if (yellow.compareTo(BigDecimal.ZERO) >= 0) {
			params.add(new EliteTokenParam(EliteToken.YELLOW, yellow));
		}
		return params;
	}

	private BigDecimal getExpected(JsonObject object, String green) {
		BigDecimal bigDecimal = BigDecimal.valueOf(object.getInt(green));
		if (bigDecimal.signum() < 0) {
			bigDecimal = BigDecimal.ZERO;
		}
		return bigDecimal;
	}
}

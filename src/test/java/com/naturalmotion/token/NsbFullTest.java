package com.naturalmotion.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.naturalmotion.csr_api.service.io.NsbException;
import com.naturalmotion.csr_api.service.io.NsbReader;

public class NsbFullTest {

	private Map<String, Integer> minFusions = new HashMap<String, Integer>();

	@Before
	public void setup() {
		minFusions.put("TIER_1", 27);
		minFusions.put("TIER_2", 32);
		minFusions.put("TIER_3", 32);
		minFusions.put("TIER_4", 68);
		minFusions.put("TIER_5", 96);
	}

	@Test
	public void testFusions() throws NsbException {
		List<String> carNotFull = new ArrayList<>();
		NsbReader nsbReader = new NsbReader();
		JsonObject nsbFull = nsbReader.getNsbFull();
		JsonArray caow = nsbFull.getJsonArray("caow");
		for (int i = 0; i < caow.size(); i++) {
			int nbFusion = 0;
			boolean missingFsg = true;
			JsonObject car = caow.getJsonObject(i);
			JsonArray parts = car.getJsonArray("upst");
			System.out.println(car.getString("crdb"));
			for (int p = 0; p < parts.size(); p++) {
				JsonObject part = parts.getJsonObject(p);
				JsonArray partLevels = part.getJsonArray("lvls");
				for (int l = 0; l < partLevels.size(); l++) {
					JsonObject level = partLevels.getJsonObject(l);
					if (level.containsKey("fsg")) {
						JsonArray fusions = level.getJsonArray("fsg");
						for (int f = 0; f < fusions.size(); f++) {
							int fusion = fusions.getInt(f);
							if (fusion > 0) {
								nbFusion++;
							}
						}
					} else {
						missingFsg = false;
					}
				}
			}
			String tier = car.getString("ctie");
			if (!missingFsg && nbFusion < minFusions.get(tier)) {
				carNotFull.add(car.getString("crdb") + " should have at least " + minFusions.get(tier)
				        + " fusions but has only " + nbFusion);
			}
			// reset
			nbFusion = 0;
		}

		Assertions.assertThat(carNotFull).hasSize(0);
	}
}

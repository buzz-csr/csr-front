package com.naturalmotion.listener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naturalmotion.listener.json.AccountHistory;
import com.naturalmotion.webservice.api.Member;

public class HistoryUpdaterTest {

	private HistoryUpdater updater = new HistoryUpdater();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");

	@Test
	public void testUpdate() throws Exception {
		AccountHistory accountJson = new AccountHistory();
		updater.update(member(99), sdf.parse("10/04/2021 08:50"), accountJson);
		Assertions.assertThat(accountJson.getDayHistories()).hasSize(1);
		Assertions.assertThat(accountJson.getDayHistories().get("10/04/2021").getCumul())
				.isEqualByComparingTo(BigDecimal.ZERO);

		updater.update(member(200), sdf.parse("10/04/2021 10:50"), accountJson);
		Assertions.assertThat(accountJson.getDayHistories()).hasSize(1);
		Assertions.assertThat(accountJson.getDayHistories().get("10/04/2021").getCumul())
				.isEqualByComparingTo(new BigDecimal("101")); // 200-99

		updater.update(member(50), sdf.parse("11/04/2021 08:50"), accountJson);
		Assertions.assertThat(accountJson.getDayHistories()).hasSize(2);
		Assertions.assertThat(accountJson.getDayHistories().get("11/04/2021").getCumul())
				.isEqualByComparingTo(BigDecimal.ZERO);

		updater.update(member(60), sdf.parse("11/04/2021 10:50"), accountJson);
		Assertions.assertThat(accountJson.getDayHistories()).hasSize(2);
		Assertions.assertThat(accountJson.getDayHistories().get("11/04/2021").getCumul())
				.isEqualByComparingTo(new BigDecimal("10")); // 10

		System.out.println(new ObjectMapper().writeValueAsString(accountJson));

	}

	private Member member(int points) {
		Member member = new Member();
		member.setPoints(points);
		return member;
	}

}

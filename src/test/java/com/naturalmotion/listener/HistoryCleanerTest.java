package com.naturalmotion.listener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.naturalmotion.listener.json.AccountHistory;
import com.naturalmotion.listener.json.DayHistory;

public class HistoryCleanerTest {

	private SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void testClean() throws Exception {
		HistoryCleaner cleaner = new HistoryCleaner();

		AccountHistory accountJson = new AccountHistory();
		Map<String, DayHistory> dayHistories = new HashMap<String, DayHistory>();
		dayHistories.put("01/01/2020", new DayHistory()); // Too old -> should be removed
		dayHistories.put("08/05/2020", new DayHistory()); // OK
		dayHistories.put("09/05/2020", new DayHistory()); // OK
		accountJson.setDayHistories(dayHistories);
		AccountHistory clean = cleaner.clean(accountJson, dFormat.parse("10/05/2020"));
		Assertions.assertThat(clean.getDayHistories()).hasSize(2);
	}

	@Test
	public void testCleanEndOfYear1() throws Exception {
		HistoryCleaner cleaner = new HistoryCleaner();

		AccountHistory accountJson = new AccountHistory();
		Map<String, DayHistory> dayHistories = new HashMap<String, DayHistory>();
		dayHistories.put("01/12/2020", new DayHistory()); // Too old -> should be removed
		dayHistories.put("08/02/2021", new DayHistory()); // OK
		dayHistories.put("09/02/2021", new DayHistory()); // OK
		accountJson.setDayHistories(dayHistories);
		AccountHistory clean = cleaner.clean(accountJson, dFormat.parse("10/03/2021"));
		Assertions.assertThat(clean.getDayHistories()).hasSize(2);
	}

	@Test
	public void testCleanEndOfYear2() throws Exception {
		HistoryCleaner cleaner = new HistoryCleaner();

		AccountHistory accountJson = new AccountHistory();
		Map<String, DayHistory> dayHistories = new HashMap<String, DayHistory>();
		dayHistories.put("01/12/2020", new DayHistory()); // Too old -> should be removed
		dayHistories.put("30/12/2020", new DayHistory()); // OK
		dayHistories.put("31/12/2020", new DayHistory()); // OK
		accountJson.setDayHistories(dayHistories);
		AccountHistory clean = cleaner.clean(accountJson, dFormat.parse("28/02/2021"));
		Assertions.assertThat(clean.getDayHistories()).hasSize(2);
	}

}

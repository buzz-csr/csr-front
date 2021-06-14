package com.naturalmotion.listener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.naturalmotion.listener.json.AccountHistory;
import com.naturalmotion.listener.json.DayHistory;
import com.naturalmotion.webservice.api.Member;

public class HistoryUpdater {

	private SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");

	public void update(Member member, Date date, AccountHistory accountJson) {
		Map<String, DayHistory> dayHistories = accountJson.getDayHistories();
		if (dayHistories == null) {
			dayHistories = new HashMap<String, DayHistory>();
		}

		String formatDate = dFormat.format(date);
		DayHistory dayHistory = dayHistories.get(formatDate);
		if (dayHistory == null) {
			dayHistory = new DayHistory();
			dayHistory.setCumul(BigDecimal.ZERO);
			dayHistory.setSnapshot(new HashMap<>());
		}
		Map<Date, BigDecimal> snapshot = dayHistory.getSnapshot();
		snapshot.put(date, BigDecimal.valueOf(member.getPoints()));

		if (snapshot.size() > 1) {
			BigDecimal smaller = new BigDecimal("9000000");
			BigDecimal biggest = BigDecimal.ZERO;
			Set<Date> keySet = snapshot.keySet();
			for (Date key : keySet) {
				BigDecimal actual = snapshot.get(key);
				if (actual.compareTo(smaller) < 0) {
					smaller = actual;
				}
				if (actual.compareTo(biggest) > 0) {
					biggest = actual;
				}
			}
			dayHistory.setCumul(biggest.subtract(smaller));
		}

		dayHistories.put(formatDate, dayHistory);
		accountJson.setDayHistories(dayHistories);
	}

}

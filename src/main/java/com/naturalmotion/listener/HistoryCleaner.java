package com.naturalmotion.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.naturalmotion.listener.json.AccountHistory;
import com.naturalmotion.listener.json.DayHistory;

public class HistoryCleaner {

	private SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");

	public AccountHistory clean(AccountHistory accountJson, Date date) throws ParseException {
		AccountHistory cleanHistory = new AccountHistory();
		Map<String, DayHistory> cleanDayHistories = new HashMap<String, DayHistory>();
		cleanHistory.setDayHistories(cleanDayHistories);

		Map<String, DayHistory> dayHistories = accountJson.getDayHistories();
		Set<String> days = dayHistories.keySet();
		for (String day : days) {
			Date actualDay = dFormat.parse(day);
			Calendar actualCal = Calendar.getInstance();
			actualCal.setTime(actualDay);
			actualCal.add(Calendar.DAY_OF_YEAR, 70); // 5 saisons

			if (!actualCal.getTime().before(date)) {
				cleanDayHistories.put(day, dayHistories.get(day));
			}

		}
		return cleanHistory;
	}

}

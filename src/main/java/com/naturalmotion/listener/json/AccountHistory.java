package com.naturalmotion.listener.json;

import java.util.Map;

public class AccountHistory {

	private Map<String, DayHistory> dayHistories;

	public Map<String, DayHistory> getDayHistories() {
		return dayHistories;
	}

	public void setDayHistories(Map<String, DayHistory> dayHistories) {
		this.dayHistories = dayHistories;
	}

}

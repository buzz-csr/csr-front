package com.naturalmotion.listener.json;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class DayHistory {

	private BigDecimal cumul;

	private Map<Date, BigDecimal> snapshot;

	public BigDecimal getCumul() {
		return cumul;
	}

	public void setCumul(BigDecimal cumul) {
		this.cumul = cumul;
	}

	public Map<Date, BigDecimal> getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Map<Date, BigDecimal> snapshot) {
		this.snapshot = snapshot;
	}

}

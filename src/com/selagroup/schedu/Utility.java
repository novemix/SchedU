package com.selagroup.schedu;

import java.util.Calendar;

public class Utility {
	
	public static long intFromCalendar(Calendar iCalendar) {
		return iCalendar.getTimeInMillis();
	}
	
	public static Calendar calendarFromInt(long iTimeInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(iTimeInMillis);
		return calendar;
	}
}

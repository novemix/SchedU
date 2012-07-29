package com.selagroup.schedu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.selagroup.schedu.model.Instructor;
import com.selagroup.schedu.model.TimePlaceBlock;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Utility {
	
	public static final String[] sDays = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
	public static final int MILLIS_PER_SECOND = 1000;
	public static final int SECONDS_PER_MINUTE = 60;
	public static final int MINUTES_PER_HOUR = 60;
	public static final int HOURS_PER_DAY = 24;
	public static final int MILLIS_PER_MINUTE = MILLIS_PER_SECOND * SECONDS_PER_MINUTE;
	public static final int MILLIS_PER_HOUR = MILLIS_PER_MINUTE * MINUTES_PER_HOUR;
	public static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * HOURS_PER_DAY;
	
	public static long intFromCalendar(Calendar iCalendar) {
		return iCalendar.getTimeInMillis();
	}
	
	public static Calendar calendarFromInt(long iTimeInMillis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(iTimeInMillis);
		return calendar;
	}
	
	public static void populateInstructorHours(ScrollView i_sv, List<TimePlaceBlock> i_hours) {
		LinearLayout new_ll = new LinearLayout(i_sv.getContext());
		new_ll.setOrientation(LinearLayout.VERTICAL);
		
		if (i_hours != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
			for (TimePlaceBlock tpb : i_hours) {
				TextView newTextView = new TextView(new_ll.getContext());
				newTextView.setText(getDayString(tpb.getDayFlagArray()) + ": " + sdf.format(tpb.getStartTime().getTime()).toLowerCase() + " - " + sdf.format(tpb.getEndTime().getTime()).toLowerCase());
				new_ll.addView(newTextView);
			}
		}
		i_sv.removeAllViews();
		i_sv.addView(new_ll);
	}
	
	public static String getDayString(boolean[] arr) {
		String result = "";
		for (int i = 0 ; i < 7 ; i++) {
			if (arr[i]) result += ("".equals(result) ? "" : ", ") + sDays[i];
		}
		return result;
	}
}

package com.selagroup.schedu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.selagroup.schedu.activities.AllCoursesActivity;
import com.selagroup.schedu.activities.CalendarActivity;
import com.selagroup.schedu.activities.ScheduPreferences;
import com.selagroup.schedu.activities.TermActivity;
import com.selagroup.schedu.model.TimePlaceBlock;

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
				newTextView.setText(tpb.toDateTimeString());
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
	
	public static void buildOptionsMenu(Menu iMenu) {
		iMenu.add(R.string.menu_item_term_activity);
		iMenu.add(R.string.menu_item_all_courses_activity);
		iMenu.add(R.string.menu_item_calendar_activity);
		iMenu.add(R.string.menu_item_preferences);
	}
	
	public static boolean handleOptionsMenuSelection(Context iContext, MenuItem iItem) {
		Resources res = iContext.getResources();
		if (iItem.getTitle().equals(res.getString(R.string.menu_item_term_activity))) {
			iContext.startActivity(new Intent(iContext, TermActivity.class));
			return true;
		}
		if (iItem.getTitle().equals(res.getString(R.string.menu_item_all_courses_activity))) {
			iContext.startActivity(new Intent(iContext, AllCoursesActivity.class));
			return true;
		}
		if (iItem.getTitle().equals(res.getString(R.string.menu_item_calendar_activity))) {
			iContext.startActivity(new Intent(iContext, CalendarActivity.class));
		}
		if (iItem.getTitle().equals(res.getString(R.string.menu_item_preferences))) {
			iContext.startActivity(new Intent(iContext, ScheduPreferences.class));
			return true;
		}
		return false;
	}
}

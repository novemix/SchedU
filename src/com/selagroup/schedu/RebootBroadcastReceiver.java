package com.selagroup.schedu;

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.selagroup.schedu.database.DBHelper;
import com.selagroup.schedu.managers.CourseManager;
import com.selagroup.schedu.managers.ExamManager;
import com.selagroup.schedu.managers.InstructorManager;
import com.selagroup.schedu.managers.LocationManager;
import com.selagroup.schedu.managers.TermManager;
import com.selagroup.schedu.managers.TimePlaceBlockManager;
import com.selagroup.schedu.model.Term;

public class RebootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmSystem alarmSystem = new AlarmSystem(context);
		
		// Schedule alarms
		DBHelper dbHelper = new DBHelper(context);
		LocationManager locationManager = new LocationManager(dbHelper);
		TimePlaceBlockManager blockManager = new TimePlaceBlockManager(dbHelper, locationManager);
		TermManager termManager = new TermManager(dbHelper);
		CourseManager courseManager = new CourseManager(dbHelper, termManager, new InstructorManager(dbHelper, locationManager, blockManager), blockManager);
		ExamManager examManager = new ExamManager(dbHelper, blockManager, courseManager);
		
		List<Term> allTerms = termManager.getAll();
		Term term = Utility.getCurrentTerm(allTerms, Calendar.getInstance());
		alarmSystem.scheduleEventsForDay(courseManager.getAllForTerm(term.getID()),
				examManager.getAll(), Calendar.getInstance(), true);
	}
}

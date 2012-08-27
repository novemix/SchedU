package com.selagroup.schedu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.selagroup.schedu.activities.CourseActivity;
import com.selagroup.schedu.activities.CourseExamsActivity;
import com.selagroup.schedu.activities.ScheduPreferences;
import com.selagroup.schedu.model.Course;
import com.selagroup.schedu.model.Exam;
import com.selagroup.schedu.model.TimePlaceBlock;

public class AlarmSystem {
	private static final SimpleDateFormat CLASS_NOTIFICATION_FORMAT = new SimpleDateFormat("h:mma");
	private static final SimpleDateFormat EXAM_NOTIFICATION_FORMAT = new SimpleDateFormat("EEE, MMM d");

	public static final String ALARM_SET_NEXT_DAY_EVENTS = "com.selagroup.schedu.ALARM_SET_NEXT_DAY_EVENTS";
	public static final int ALARM_CODE_SET_NEXT_DAY_EVENTS = -1;

	public static final int SHOW_COURSE_BLOCK = -2;

	public static final String ALARM_SET_SILENT_MODE = "com.selagroup.schedu.ALARM_SET_SILENT_MODE";

	public static final String ALARM_SET_COURSE_REMINDER = "com.selagroup.schedu.ALARM_SET_COURSE_REMINDER";
	public static final int ALARM_CODE_SET_COURSE_REMINDER = -3;

	public static final String ALARM_SET_EXAM_REMINDER = "com.selagroup.schedu.ALARM_SET_EXAM_REMINDER";
	public static final int ALARM_CODE_SET_EXAM_REMINDER = -4;

	private static final int EXAM_ID_OFFSET = 10000; // Notification ID offset for exams so they don't conflict with courses

	private Context mContext;
	private NotificationManager notificationMgr;
	private AlarmManager mAlarmManager;

	private ArrayList<PendingIntent> mAllAlarmIntents = new ArrayList<PendingIntent>();

	private int mPrevRingerMode;
	private boolean mIgnoreNextRingerChange = false;

	private final BroadcastReceiver ringerModeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int mode = intent.getExtras().getInt("mode", AudioManager.RINGER_MODE_NORMAL);
			AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

			// Do not set phone to vibrate if it's on silent
			if (!(mode == AudioManager.RINGER_MODE_VIBRATE && mPrevRingerMode == AudioManager.RINGER_MODE_SILENT)) {
				mIgnoreNextRingerChange = true;
				audioMgr.setRingerMode(mode);
			}
		}
	};

	private final BroadcastReceiver nextDayReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			ArrayList<Course> courses = (ArrayList<Course>) bundle.getSerializable("courses");
			ArrayList<Exam> exams = (ArrayList<Exam>) bundle.getSerializable("exams");
			Calendar day = (Calendar) bundle.getSerializable("day");
			scheduleEventsForDay(courses, exams, day, true);
		}
	};

	private final BroadcastReceiver courseReminderReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			Course course = (Course) bundle.getSerializable("course");
			TimePlaceBlock block = (TimePlaceBlock) bundle.getSerializable("block");
			Calendar day = (Calendar) bundle.getSerializable("day");
			showCourseNotification(course, block, day);
		}
	};

	private final BroadcastReceiver examReminderReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			Exam exam = (Exam) bundle.getSerializable("exam");
			showExamNotification(exam);
		}
	};

	private final BroadcastReceiver userRingerModeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (!mIgnoreNextRingerChange) {
				mPrevRingerMode = intent.getExtras().getInt(AudioManager.EXTRA_RINGER_MODE);
			} else {
				mIgnoreNextRingerChange = false;
			}
		}
	};

	/**
	 * @param iContext Application context
	 */
	public AlarmSystem(Context iContext) {
		mContext = iContext.getApplicationContext();
		mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		notificationMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

		mContext.registerReceiver(ringerModeReceiver, new IntentFilter(ALARM_SET_SILENT_MODE));
		mContext.registerReceiver(nextDayReceiver, new IntentFilter(ALARM_SET_NEXT_DAY_EVENTS));
		mContext.registerReceiver(courseReminderReceiver, new IntentFilter(ALARM_SET_COURSE_REMINDER));
		mContext.registerReceiver(examReminderReceiver, new IntentFilter(ALARM_SET_EXAM_REMINDER));
		mContext.registerReceiver(userRingerModeReceiver, new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION));

		mPrevRingerMode = ((AudioManager) iContext.getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
	}

	/**
	 * Schedules class events for a given day: class notifications, silence/unsilence ringer, and exam notifications
	 * @param iCourses Courses for the current term
	 * @param iDay Day to schedule events for
	 */
	public void scheduleEventsForDay(ArrayList<Course> iCourses, ArrayList<Exam> iExams, Calendar iDay, boolean iNewNotifications) {
		clearAlarms();
		restoreRingerMode();

		// Get preferences
		Map<String, ?> allPreferences = PreferenceManager.getDefaultSharedPreferences(mContext).getAll();

		boolean silentMode = (Boolean) allPreferences.get(ScheduPreferences.PREF_KEY_SILENT);

		boolean courseReminders = (Boolean) allPreferences.get(ScheduPreferences.PREF_KEY_COURSE_REMIND);
		int courseReminderLeadTime = Integer.parseInt((String) allPreferences.get(ScheduPreferences.PREF_KEY_COURSE_REMIND_TIME));

		boolean examReminders = (Boolean) allPreferences.get(ScheduPreferences.PREF_KEY_EXAM_REMINDERS);
		int examReminderLeadTime = Integer.parseInt((String) allPreferences.get(ScheduPreferences.PREF_KEY_EXAM_LEAD_TIME));

		/*
		 * For each class block, schedule alarms for: Phone silence/unsilence for the next class, reminder before next class, exam reminders
		 */
		AudioManager audioMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mPrevRingerMode = audioMgr.getRingerMode();
		for (Course course : iCourses) {
			for (TimePlaceBlock block : course.getBlocksOnDay(iDay.get(Calendar.DAY_OF_WEEK) - 1)) {
				Calendar start = (Calendar) block.getStartTime();
				Calendar end = (Calendar) block.getEndTime();
				start.set(iDay.get(Calendar.YEAR), iDay.get(Calendar.MONTH), iDay.get(Calendar.DAY_OF_MONTH));
				end.set(iDay.get(Calendar.YEAR), iDay.get(Calendar.MONTH), iDay.get(Calendar.DAY_OF_MONTH));

				// If in silent mode, schedule the silence events
				if (silentMode) {
					if (end.after(Calendar.getInstance())) {
						scheduleSilentMode(AudioManager.RINGER_MODE_VIBRATE, start, block.getID());
						scheduleSilentMode(mPrevRingerMode, end, block.getID());
					}
				}

				// If course reminders are on, schedule notifications
				if (courseReminders && iNewNotifications) {
					Calendar courseReminderTime = (Calendar) start.clone();
					courseReminderTime.add(Calendar.MINUTE, -courseReminderLeadTime);
					if (start.after(Calendar.getInstance())) {
						scheduleCourseReminder(courseReminderTime, course, block, start);
					}
				}
			}
		}

		if (examReminders && iNewNotifications) {
			for (Exam exam : iExams) {
				Calendar examReminderTime = (Calendar) exam.getBlock().getStartTime().clone();
				examReminderTime.add(Calendar.MINUTE, -examReminderLeadTime * Utility.MINUTES_PER_DAY);
				if (exam.getBlock().getEndTime().after(Calendar.getInstance())) {
					scheduleExamReminder(examReminderTime, exam);
				}
			}
		}

		// Set alarm to reschedule all events for tomorrow
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);
		Intent intent = new Intent(ALARM_SET_NEXT_DAY_EVENTS);
		intent.putExtra("courses", iCourses);
		intent.putExtra("exams", iExams);
		intent.putExtra("day", iDay);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_CODE_SET_NEXT_DAY_EVENTS, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mAllAlarmIntents.add(pendingIntent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, tomorrow.getTimeInMillis(), pendingIntent);
	}

	/**
	 * Clear all previously set alarms
	 */
	public void clearAlarms() {
		for (PendingIntent intent : mAllAlarmIntents) {
			mAlarmManager.cancel(intent);
		}
		mAllAlarmIntents.clear();
	}

	/**
	 * Set up an alarm to silence/unsilence the phone at the specified time
	 * @param iSilence True to silence the phone, false to unsilence
	 * @param alarmTime Time to silence the phone
	 */
	public void scheduleSilentMode(final int iMode, Calendar iAlarmTime, int iBlockID) {
		Intent intent = new Intent(ALARM_SET_SILENT_MODE);
		intent.putExtra("mode", iMode);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (3 * (iBlockID + 4)) + iMode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mAllAlarmIntents.add(pendingIntent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, iAlarmTime.getTimeInMillis(), pendingIntent);
	}

	private void scheduleCourseReminder(Calendar iReminderTime, Course iCourse, TimePlaceBlock iBlock, Calendar iDay) {
		Intent intent = new Intent(ALARM_SET_COURSE_REMINDER);
		intent.putExtra("course", iCourse);
		intent.putExtra("block", iBlock);
		intent.putExtra("day", iDay);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_CODE_SET_COURSE_REMINDER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mAllAlarmIntents.add(pendingIntent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, iReminderTime.getTimeInMillis(), pendingIntent);
	}

	private void showCourseNotification(Course iCourse, TimePlaceBlock iBlock, Calendar iStart) {
		Notification notification = new Notification(R.drawable.schedu_icon, "SchedU", System.currentTimeMillis());
		notification.number = 1;
		notification.vibrate = new long[] { 200, 200, 200, 200 };
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Show course intent
		Intent showCourseIntent = new Intent(mContext, CourseActivity.class);
		showCourseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		showCourseIntent.putExtra("courseID", iCourse.getID());
		showCourseIntent.putExtra("blockID", iBlock.getID());
		showCourseIntent.putExtra("day", iStart);

		// Broadcast
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, iCourse.getID(), showCourseIntent, PendingIntent.FLAG_ONE_SHOT | Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.setLatestEventInfo(mContext, iCourse.getCode(), iCourse.getName() + ": " + iBlock.toTimeString(), contentIntent);

		notificationMgr.notify(iCourse.getID(), notification);
	}

	private void scheduleExamReminder(Calendar iReminderTime, Exam iExam) {
		Intent intent = new Intent(ALARM_SET_EXAM_REMINDER);
		intent.putExtra("exam", iExam);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_CODE_SET_EXAM_REMINDER, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		mAllAlarmIntents.add(pendingIntent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, iReminderTime.getTimeInMillis(), pendingIntent);
	}

	private void showExamNotification(Exam iExam) {
		Course course = iExam.getCourse();
		Notification notification = new Notification(R.drawable.schedu_icon, "SchedU", System.currentTimeMillis());
		notification.number = 1;
		notification.vibrate = new long[] { 200, 200, 200, 200 };
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Show the calendar
		Intent showExamIntent = new Intent(mContext, CourseExamsActivity.class);
		showExamIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		showExamIntent.putExtra("courseID", course.getID());

		// Broadcast
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, iExam.getID(), showExamIntent, PendingIntent.FLAG_ONE_SHOT | Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(mContext, "Exam in " + course.getCode(),
				EXAM_NOTIFICATION_FORMAT.format(iExam.getBlock().getStartTime().getTime()) + ": " +
				iExam.getBlock().toTimeString(), contentIntent);
		notificationMgr.notify(EXAM_ID_OFFSET + iExam.getID(), notification);
	}

	public void restoreRingerMode() {
		AudioManager audioMgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		if (audioMgr.getRingerMode() != mPrevRingerMode) {
			mIgnoreNextRingerChange = true;
			audioMgr.setRingerMode(mPrevRingerMode);
		}
	}
}

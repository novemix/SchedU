package com.selagroup.schedu;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.selagroup.schedu.activities.CourseActivity;

public class ScheduleAlarmSystem {
	public static final String ALARM_ACTION = "com.selagroup.schedu.ALARM_ACTION";
	public static final int ALARM_REQUEST_CODE = -1;
	public static final int SHOW_COURSE_BLOCK = -2;
	
	private Context mContext;
	private NotificationManager notificationMgr;
	private AlarmManager alarmMgr;
	
	public ScheduleAlarmSystem(Context iContext) {
		mContext = iContext;
		alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		notificationMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	/**
	 * Set up an alarm to go off at the specified time
	 * @param alarmTime
	 */
	public void scheduleAlarm(Calendar alarmTime) {
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				showNotification();
			}
		}, new IntentFilter(ALARM_ACTION));
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_REQUEST_CODE, new Intent(ALARM_ACTION), 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
	}
	
	private void showNotification() {
		Notification notification = new Notification(R.drawable.ic_launcher, "Notification", System.currentTimeMillis());
		notification.number = 1;
		notification.vibrate = new long[] { 200, 200, 200, 200 };
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Show course intent
		Intent showCourseIntent = new Intent(mContext, CourseActivity.class);
		showCourseIntent.putExtra("courseID", -1);
		showCourseIntent.putExtra("blockID", -1);
		showCourseIntent.putExtra("day", Calendar.getInstance());
		
		notification.contentIntent = PendingIntent.getActivity(mContext, SHOW_COURSE_BLOCK, showCourseIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		// notification.setLatestEventInfo(mContext, "Title", "Content text", contentIntent);
		
		notificationMgr.notify(1, notification);
	}
}

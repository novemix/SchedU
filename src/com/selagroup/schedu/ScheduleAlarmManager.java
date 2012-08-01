package com.selagroup.schedu;

import com.selagroup.schedu.model.Course;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ScheduleAlarmManager {
	public static final String ALARM_ACTION = "com.selagroup.schedu.ALARM_ACTION";
	public static final int ALARM_REQUEST_CODE = -1;
	private Context mContext;
	private NotificationManager notificationMgr;
	private AlarmManager alarmMgr;
	
	public ScheduleAlarmManager(Context iContext) {
		mContext = iContext;
		alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		notificationMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public void setupAlarms(Course iCourse) {
		
	}
	
	private void showNotification() {
		Notification notification = new Notification(R.drawable.ic_launcher, "Notification", System.currentTimeMillis());
		notification.number = 1;
		notification.vibrate = new long[] { 200, 100, 200, 100 };
		
		// notification.setLatestEventInfo(mContext, "Title", "Content text", contentIntent);
		
		notificationMgr.notify(1, notification);
	}
	
	private void configureAlarm() {
		mContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				showNotification();
			}
		}, new IntentFilter(ALARM_ACTION));
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_REQUEST_CODE, new Intent(ALARM_ACTION), 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pendingIntent);
	}
}

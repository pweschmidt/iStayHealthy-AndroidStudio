package com.pweschmidt.healthapps;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.database.Cursor;


public class AlertSetter extends BroadcastReceiver {
//	private static final String TAG = "AlertSetter";

	@Override
	public void onReceive(Context context, Intent _intent) {
		int standardInterval = 24 * 60 * 60 * 1000;//24 hour repeat interval
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		java.util.Date date = new java.util.Date();
		long now = date.getTime();
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.ALERTS_CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			String label = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTLABEL));
			int repeat = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN));
			long startTime = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSTARTTIME));
			int requestCode = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREQUESTCODE));
			Calendar alarmCalendar = Calendar.getInstance();
			alarmCalendar.setTimeInMillis(startTime);
			boolean isEveryDay = (0 < repeat);
			calendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));

			long time = calendar.getTimeInMillis();
			
			Intent alarmIntent = new Intent(context, AlarmReceiver.class);
			alarmIntent.putExtra("isVibrate", true);
			alarmIntent.putExtra("Label", label);
			alarmIntent.putExtra("isEveryDay", isEveryDay);
			alarmIntent.putExtra("requestCode", requestCode);
			PendingIntent pendingSingleIntent = PendingIntent.getBroadcast(context.getApplicationContext(), requestCode, alarmIntent, 0);
			
			if(isEveryDay){
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, standardInterval, pendingSingleIntent);								
			}
			else{
				long diff = startTime - now;
				if( 0 < diff){
					alarm.set(AlarmManager.RTC_WAKEUP, startTime, pendingSingleIntent);					
				}
			}

			cursor.moveToNext();
		}
		
		cursor.close();

	}

}

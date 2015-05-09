package com.pweschmidt.healthapps;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
//import android.util.Log;
import android.media.*;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;

public class AlarmReceiver extends BroadcastReceiver {
	public static final String ACTION_MEDICATION_ALARM = "com.pweschmidt.healthapps.ACTION_MEDICATION_ALARM";
//	private static final String TAG = "AlarmReceiver";
	private NotificationManager notificationManager;
	
	/**
	 * if it is a single notification event we delete the Alert from the database
	 */
	public void onReceive(Context context, Intent intent) {
//		Log.d(TAG, "onReceive");
		notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "iStayHealthy";
		CharSequence message ="";
		Bundle extras = intent.getExtras();
		boolean isVibrate = true;
		int requestCode = 0;
		long time = System.currentTimeMillis();
		if(null != extras){
			message = extras.getCharSequence("Label");
			isVibrate = extras.getBoolean("isVibrate");
			requestCode = extras.getInt("requestCode");
		}
        SharedPreferences prefs = context.getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
		boolean isConfirmMeds = prefs.getBoolean("isConfirmMeds", false);
//		Log.d(TAG,"isConfirmMeds is "+((isConfirmMeds) ? "true" : "false"));
		Intent alertIntent = new Intent(context, iStayHealthyTabActivity.class);
		alertIntent.putExtra("isVibrate", isVibrate);
		alertIntent.putExtra("isConfirmMeds", isConfirmMeds);
		PendingIntent notificationIntent = PendingIntent.getActivity(context, requestCode, alertIntent, 0);
		Notification notification = new Notification(R.drawable.icon, "iStayHealthy Notifier",time);

		Uri soundPath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		if(null == soundPath){
			soundPath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		}
		
		if(null == soundPath)
			notification.defaults |= Notification.DEFAULT_SOUND;
		else
			notification.sound = soundPath;
		
		if(isVibrate)notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.setLatestEventInfo(context, from, message, notificationIntent);
		notificationManager.notify(requestCode, notification);					
	}

}

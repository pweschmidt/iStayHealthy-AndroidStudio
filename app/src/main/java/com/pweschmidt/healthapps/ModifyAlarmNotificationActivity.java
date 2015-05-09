package com.pweschmidt.healthapps;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.text.format.DateFormat;
import android.view.*;
import android.widget.*;
import android.content.ContentValues;

import java.util.*;
import java.text.SimpleDateFormat;
//import android.util.*;

import com.pweschmidt.healthapps.datamodel.Alerts;

public class ModifyAlarmNotificationActivity extends Activity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
//	private static final String TAG = "ModifyAlarmNotificationActivity";
	private static final int TIME_DIALOG_ID = 11;
	private boolean isVibrate;
	private String label;
	private String sound;
	private TextView dateText;
	private TableRow dateRow;
	private boolean isEveryDay;
	private boolean isConfirmMeds;
	private long time;
	private long rowID;
	private Alerts alert;
	private final int standardInterval = 24 * 60 * 60 * 1000;//24 hour repeat interval
	private SimpleDateFormat simpleFormat;
	private int currentRequestCode;
	private Uri alertUri;

	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_alarms);
        rowID = -1;// this will change only if we can successfully load the alert
		simpleFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.EditAlert);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
		
        ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
		delete.setOnClickListener(this);
		
        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
				
		isVibrate = true;
		CheckBox checkBox = (CheckBox)findViewById(R.id.modifyVibrateButton);
		checkBox.setChecked(isVibrate);
		checkBox.setOnClickListener(this);
		
		isEveryDay = true;
		CheckBox repeatBox = (CheckBox)findViewById(R.id.modifyRepeatButton);
		repeatBox.setChecked(isEveryDay);
		repeatBox.setOnClickListener(this);
		
		CheckBox confirmBox = (CheckBox)findViewById(R.id.confirmCheckButton);
        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
		isConfirmMeds = prefs.getBoolean("isConfirmMeds", false);
		confirmBox.setChecked(isConfirmMeds);
		confirmBox.setOnClickListener(this);
		
	}	
	

	/**
	 * 
	 */
	public void onClick(View view){
		int resId = view.getId();
		switch(resId){
		case R.id.BackButton:
			setResult(RESULT_CANCELED, null);
			finish();
			break;
		case R.id.TrashButton:
    		AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    		String yes = getResources().getString(R.string.Yes);
    		String no = getResources().getString(R.string.No);
    		String message = getResources().getString(R.string.deleteentry);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   deleteAlertFromSQL();
    	   			   cancelNotifications();
    	        	   ModifyAlarmNotificationActivity.this.finish();
    	           }
    	       })
    	       .setNegativeButton(no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    		AlertDialog alert = builder.create();    	
    		alert.show();
			break;
		case R.id.SaveButton:
			updateAlertInSQL();
			cancelNotifications();//remove previous one
			setNewNotification();//set the new one
			setResult(RESULT_OK, null);
	        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isConfirmMeds", isConfirmMeds);
            editor.commit();
			finish();
			break;
		case R.id.setDateTimeRow:
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.modifyVibrateButton:
			if( ((CheckBox)view).isChecked() ){
				isVibrate = true;
			}
			else{
				isVibrate = false;
			}
			break;
		case R.id.modifyRepeatButton:
			if( ((CheckBox)view).isChecked() ){
				isEveryDay = true;
			}
			else{
				isEveryDay = false;
			}
			break;
		case R.id.confirmCheckButton:
			if( ((CheckBox)view).isChecked())
			{
				isConfirmMeds = true;
			}
			else
			{
				isConfirmMeds = false;
			}
			break;
		}
	}
	
    /**
     * entry point when returning to ResultsActivity
     */
    public void onResume(){
    	super.onResume();
//    	Log.d(TAG,"onResume");
    	Bundle extras = getIntent().getExtras();
    	if(null != extras){
    		rowID = extras.getLong("AlertsIndex");
    		alertUri = Uri.withAppendedPath(iStayHealthyContentProvider.ALERTS_CONTENT_URI, String.valueOf(rowID));
    		Cursor alertCursor = getContentResolver().query(alertUri, null, null, null, null);
    		if(0 < alertCursor.getCount() && alertCursor.moveToFirst())
    		{
    			alert = new Alerts();
    			alert.setAlerts(alertCursor);
    		}
//    		Log.d(TAG,"onCreate getting bundle extra with value rowID = "+rowID);
//    		results = databaseAdapter.getResults(rowID);
    		alertCursor.close();
    		
    		label = alert.getAlertLabel();
    		time = alert.getAlertStartTime();
    		currentRequestCode = alert.getRequestCode();
    		dateText.setText(simpleFormat.format(new Date(time)));
    		int repeat = alert.getAlertRepeatPattern();
    		if( 0 < repeat)
    			isEveryDay = true;
    		else
    			isEveryDay = false;
//    		Log.d(TAG,"onCreate - we found position index "+rowID);
    	}
    	else{
    		label = "";
//    		Log.d(TAG,"onCreate no extras found ");    		
    		dateText.setText(simpleFormat.format(new Date()));
    	}
     }

    /**
     * called when activity is paused - e.g. when we move to another one
     */
    public void onPause(){
    	super.onPause();
//    	Log.d(TAG,"onPause");
    }

    protected Dialog onCreateDialog(int id) {
    	if(TIME_DIALOG_ID != id){
    		return null;
    	}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		TimePickerDialog dialog = new TimePickerDialog(this, this, hour, minute, true);   	
        return dialog;
    }    
	
	/**
	 * implements TimePickerDialog.OnTimeSetListener
	 */
	public void onTimeSet(TimePicker view, int hour, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		time = calendar.getTimeInMillis();
		dateText.setText(simpleFormat.format(new Date(time)));
	}
	

	/**
	 * 
	 */
	public void updateAlertInSQL(){
		if(0 > rowID)
			return;
		label = (((EditText)findViewById(R.id.modifyAlarmLabel)).getText()).toString().trim();
		alert.setAlertLabel(label);
		alert.setAlertStartTime(time);
		alert.setAlertSoundFileName(sound);
		alert.setAlertText(label);
		alert.setRequestCode(currentRequestCode);
		int repeat = (isEveryDay) ? 1 : 0;
		alert.setAlertRepeatPattern(repeat);
		ContentValues content = alert.contentValuesForAlert();
		if(rowID != -1)
		{
    		getContentResolver().update(alertUri, content, null, null);
			
		}
	}

	/**
	 * 
	 */
	public void setNewNotification(){
		label = (((EditText)findViewById(R.id.modifyAlarmLabel)).getText()).toString().trim();
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("isVibrate", isVibrate);
		intent.putExtra("Label", label);
		intent.putExtra("isEveryDay", isEveryDay);
		intent.putExtra("requestCode", currentRequestCode);
		PendingIntent pendingSingleIntent = PendingIntent.getBroadcast(this.getApplicationContext(), currentRequestCode, intent, 0);		
		if(isEveryDay)
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, standardInterval, pendingSingleIntent);
		else
			alarm.set(AlarmManager.RTC_WAKEUP, time, pendingSingleIntent);
	}
	
	/**
	 * 
	 */
	private void deleteAlertFromSQL(){
		if(0 > rowID)
			return;
		getContentResolver().delete(alertUri, null, null);
	}
	
	/**
	 * 
	 */
	private void cancelNotifications(){
		label = (((EditText)findViewById(R.id.modifyAlarmLabel)).getText()).toString().trim();
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("isVibrate", isVibrate);
		intent.putExtra("Label", label);
		intent.putExtra("rowAdded", rowID);
		intent.putExtra("requestCode", currentRequestCode);
		intent.putExtra("isEveryDay", isEveryDay);
		PendingIntent pendingSingleIntent = PendingIntent.getBroadcast(this.getApplicationContext(), currentRequestCode, intent, 0);		
		alarm.cancel(pendingSingleIntent);
	}
	
}

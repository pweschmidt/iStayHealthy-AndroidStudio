package com.pweschmidt.healthapps;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.database.Cursor;

import java.util.*;
import java.text.SimpleDateFormat;

import com.pweschmidt.healthapps.datamodel.Alerts;


public class AddAlarmNotificationActivity extends Activity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
//	private static final String TAG = "AddAlarmNotificationActivity";
	private static final int TIME_DIALOG_ID = 1;
	private boolean isVibrate;
	private boolean isEveryDay;
	private boolean isConfirmMeds;
	private TextView dateText;
	private TableRow dateRow;
	private long time;
	private final int standardInterval = 24 * 60 * 60 * 1000;//24 hour repeat interval
	private SimpleDateFormat simpleFormat;
	private int currentHighestRequestCode;
	
	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
		setContentView(R.layout.add_alarm);
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        currentHighestRequestCode = 0;

		TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.AddAlert);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);

    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	delete.setVisibility(View.GONE);
        
        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
		simpleFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        dateText.setText(simpleFormat.format(new Date()));
				
		isVibrate = true;
		CheckBox checkBox = (CheckBox)findViewById(R.id.vibrateCheckButton);
		checkBox.setChecked(isVibrate);
		checkBox.setOnClickListener(this);

		isEveryDay = true;
		CheckBox repeatBox = (CheckBox)findViewById(R.id.repeatCheckButton);
		repeatBox.setChecked(isEveryDay);
		repeatBox.setOnClickListener(this);
		
		isConfirmMeds = false;
		CheckBox confirmBox = (CheckBox)findViewById(R.id.confirmCheckButton);
		confirmBox.setChecked(isConfirmMeds);
		confirmBox.setOnClickListener(this);
				
	}
	
    /**
     * entry point when returning to ResultsActivity
     */
    public void onResume(){
    	super.onResume();
//    	Log.d(TAG,"onResume");
		findCurrentHighestRequestCode();
     }

    private void findCurrentHighestRequestCode(){
		Cursor cursor = getContentResolver().query(iStayHealthyContentProvider.ALERTS_CONTENT_URI, null, null, null, null);
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			int code = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREQUESTCODE));
			if(currentHighestRequestCode < code)
				currentHighestRequestCode = code;
			cursor.moveToNext();
		}
		cursor.close();
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
	 * implements View.OnClickListener
	 */
	public void onClick(View view){
		int resId = view.getId();
		switch(resId){
		case R.id.BackButton:
			setResult(RESULT_CANCELED, null);
			finish();
			break;
		case R.id.SaveButton:
			saveToSQL();
			setNotification();
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
		case R.id.vibrateCheckButton:
			if( ((CheckBox)view).isChecked() )
			{
				isVibrate = true;
			}
			else{
				isVibrate = false;
			}
			break;
		case R.id.repeatCheckButton:
			if( ((CheckBox)view).isChecked() )
			{
				isEveryDay = true;
			}
			else
			{
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
	 * 
	 */
	private void saveToSQL(){
		String label = (((EditText)findViewById(R.id.alarmLabel)).getText()).toString().trim();
		int repeats = (isEveryDay) ? 1 : 0;
		Alerts alert = new Alerts();
		alert.setAlertRepeatPattern(repeats);
		alert.setAlertLabel(label);
		alert.setAlertText(label);
		alert.setAlertStartTime(time);
		alert.setRequestCode(currentHighestRequestCode + 1);
		getContentResolver().insert(iStayHealthyContentProvider.ALERTS_CONTENT_URI, alert.contentValuesForAlert());
	}
	

	/**
	 * 
	 */
	private void setNotification()
	{
		String label = (((EditText)findViewById(R.id.alarmLabel)).getText()).toString().trim();
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("isVibrate", isVibrate);
		intent.putExtra("Label", label);
		intent.putExtra("isEveryDay", isEveryDay);
		int newRequestCode = currentHighestRequestCode + 1;
		intent.putExtra("requestCode", newRequestCode);
		
		PendingIntent pendingSingleIntent = PendingIntent.getBroadcast(this.getApplicationContext(), newRequestCode, intent, 0);		
		if(isEveryDay){
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, standardInterval, pendingSingleIntent);				
		}
		else{
			alarm.set(AlarmManager.RTC_WAKEUP, time, pendingSingleIntent);						
		}
	}
	
}

package com.pweschmidt.healthapps.datamodel;
import android.content.ContentValues;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;
import android.database.Cursor;

public class Alerts {
	private String alertLabel;
	private String alertText;
	private int alertRepeatPattern;
	private long timeZoneOffset;
	private int hour;
	private int minute;
	private long alertStartTime;
	private String alertSoundFile;
	private String GUID;
	private String Tintabee;
	private int requestCode;
	
	public Alerts(){
		alertLabel = "iStayHealthy Alert";
		alertText = "Take your Meds";
		alertRepeatPattern = 1;
		alertStartTime = 0;
		alertSoundFile = "default";
		timeZoneOffset = 0;//GMT
		hour = 0;
		minute = 0;
		requestCode = 0;
		GUID = java.util.UUID.randomUUID().toString();
		Tintabee="";
	}
	public int getRequestCode(){return requestCode;}
	public void setRequestCode(int requestCode){this.requestCode = requestCode;}
	
	public String getTintabee(){return Tintabee;}
	public void setTintabee(String text){Tintabee = text;}
	public int getHour(){return hour;}
	public int getMinute(){return minute;}
	public long getTimezoneOffset(){return timeZoneOffset;}
	public void setHour(int _hour){hour = _hour;}
	public void setMinute(int _minute){minute = _minute;}
	public void setTimezoneOffset(long _timeZoneOffset){timeZoneOffset = _timeZoneOffset;}
	
	/**
	 * gets the label text for the Alert
	 * @return
	 */
	public String getAlertLabel(){return alertLabel;}
	/**
	 * gets the alert message text
	 * @return
	 */
	public String getAlertText(){return alertText;}
	/**
	 * alert repeat pattern is given in number of hours. 24 == every 24 hours, 12 == every 12 hours.
	 * other supported values are 8 and 6. Essentially, the alarm is repeated between 1 to 4x a day
	 * @return
	 */
	public int getAlertRepeatPattern(){return alertRepeatPattern;}
	/**
	 * alert start time. this is the time from which the alert is going to start. 
	 * @return
	 */
	public long getAlertStartTime(){return alertStartTime;}
	
	public String getAlertSoundFileName(){return alertSoundFile;}
	
	/**
	 * sets the label text for the alert
	 * @param alertLabel
	 */
	public void setAlertLabel(String alertLabel){this.alertLabel = alertLabel;}
	/**
	 * sets the message text for the alert
	 * @param alertText
	 */
	public void setAlertText(String alertText){this.alertText = alertText;}
	/**
	 * sets the repeat pattern for the alert. 24 means once every 24 hours, 12 every 12 hours. 
	 * other supported values are 8 and 6. Default repeat pattern is 24 (once a day)
	 * @param alertRepeatPattern
	 */
	public void setAlertRepeatPattern(int alertRepeatPattern){this.alertRepeatPattern = alertRepeatPattern;}
	/**
	 * sets the time from which the alert will be kicked off (depending on repeat patterns);
	 * @param alertStartTimeText
	 */
	public void setAlertStartTime(long alertStartTime){this.alertStartTime = alertStartTime;}
	
	public void setAlertSoundFileName(String alertSoundFile){this.alertSoundFile = alertSoundFile;}
	public void setGUID(String _GUID){GUID = _GUID;}
	public String getGUID(){return GUID;}

	public ContentValues contentValuesForAlert()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.ALERTLABEL, alertLabel);
		content.put(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN, alertRepeatPattern);
		content.put(iStayHealthyDatabaseSchema.ALERTSTARTTIME, alertStartTime);
		content.put(iStayHealthyDatabaseSchema.ALERTTEXT, alertText);
		content.put(iStayHealthyDatabaseSchema.ALERTSOUND, alertSoundFile);
		content.put(iStayHealthyDatabaseSchema.ALERTHOUR, hour);
		content.put(iStayHealthyDatabaseSchema.AlERTMINUTE, minute);
		content.put(iStayHealthyDatabaseSchema.ALERTTIMEZONEOFFSET, timeZoneOffset);
		content.put(iStayHealthyDatabaseSchema.ALERTREQUESTCODE, requestCode);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	public void setAlerts(Cursor cursor)
	{
		alertLabel			= 	cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTLABEL));
		alertRepeatPattern	= 	cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN));
		alertStartTime		=	cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSTARTTIME));
		alertText			=	cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTTEXT));
		alertSoundFile		=	cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSOUND));	
		hour				=	cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTHOUR));
		minute				=	cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.AlERTMINUTE));
		timeZoneOffset		=	cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTTIMEZONEOFFSET));
		requestCode			=	cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREQUESTCODE));
		GUID				=	cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
		
	}
}

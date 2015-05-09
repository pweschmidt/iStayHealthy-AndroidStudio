package com.pweschmidt.healthapps.datamodel;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import android.content.ContentValues;
import android.database.Cursor;

public class Wellness extends MedicalEvent
{
	private int sleepBarometer;
	private int moodBarometer;
	private int wellnessBarometer;

	public Wellness()
	{
		setTime((new java.util.Date()).getTime());
		sleepBarometer = 0;
		moodBarometer = 0;
		wellnessBarometer = 0;
	}
	
	
	public int getWellnessBarometer(){return wellnessBarometer;}
	public void setWellnessBarometer(int wellnessBarometer){this.wellnessBarometer = wellnessBarometer;}

	public int getMoodBarometer(){return moodBarometer;}
	public void setMoodBarometer(int moodBarometer){this.moodBarometer = moodBarometer;}

	public int getSleepBarometer(){return sleepBarometer;}
	public void setSleepBarometer(int sleepBarometer){this.sleepBarometer = sleepBarometer;}
	
	
	public ContentValues contentValuesForWellness()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.RECORDEDDATE, time);
		content.put(iStayHealthyDatabaseSchema.SLEEPBAROMETER, sleepBarometer);
		content.put(iStayHealthyDatabaseSchema.MOODBAROMETER, moodBarometer);
		content.put(iStayHealthyDatabaseSchema.WELLNESSBAROMETER, wellnessBarometer);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	public void setWellness(Cursor cursor)
	{
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.RECORDEDDATE));
		sleepBarometer = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SLEEPBAROMETER));
		moodBarometer = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MOODBAROMETER));
		wellnessBarometer = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WELLNESSBAROMETER));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));		
	}
}

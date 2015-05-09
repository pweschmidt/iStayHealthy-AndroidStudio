package com.pweschmidt.healthapps.datamodel;

import android.content.ContentValues;
import android.database.Cursor;

import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

public class MissedMedication extends MedicalEvent
{
	private String name;
	private String drug;
	private String missedReason;

	public MissedMedication(){
		setTime((new java.util.Date()).getTime());
		name = "";
		drug = "";
		missedReason = "";
	}

	/**
	 * gets the commercial name
	 * @return
	 */
	public String getName(){return name;}
	/**
	 * gets the drug contained in the HIV med
	 * @return
	 */
	public String getDrug(){return drug;}

	/**
	 * sets the commercial name of the drug
	 * @param name
	 */
	public void setName(String name){this.name = name;}
	/**
	 * sets the drug contained in the HIV med
	 * @param name
	 */
	public void setDrug(String drug){this.drug = drug;}
	
	public String getMissedReason(){return missedReason;}
	public void setMissedReason(String missedReason){this.missedReason = missedReason;}
	
	public ContentValues contentValuesForMedication()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.MISSEDREASON, missedReason);
		content.put(iStayHealthyDatabaseSchema.MISSEDDATE, getTime());
		content.put(iStayHealthyDatabaseSchema.NAME, name);
		content.put(iStayHealthyDatabaseSchema.DRUG, drug);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	
	public void setMedication(Cursor cursor)
	{
		missedReason = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MISSEDREASON));
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MISSEDDATE));
		name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
		drug = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
	}
	
}

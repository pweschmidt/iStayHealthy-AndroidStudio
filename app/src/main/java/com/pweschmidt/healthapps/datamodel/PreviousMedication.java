package com.pweschmidt.healthapps.datamodel;

import android.content.ContentValues;
import android.database.Cursor;

import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

public class PreviousMedication extends MedicalEvent
{
	
	private long endDate;
	private boolean isART;
	private String name;
	private String drug;
	private String reasonEnded;

	public PreviousMedication(){
		setTime((new java.util.Date()).getTime());
		isART = false;
		name = "";
		drug = "";
		reasonEnded = "";
	}
	

	public long getEndDate(){return endDate;}
	public void setEndDate(long endDate){this.endDate = endDate;}
	
	public boolean getIsART(){return isART;}
	public void setIsART(boolean isART){this.isART = isART;}
	
	public String getName(){return name;}
	public void setName(String name){this.name = name;}
	
	public String getDrug(){return drug;}
	public void setDrug(String drug){this.drug = drug;}
	
	public String getReasonEnded(){return reasonEnded;}
	public void setReasonEnded(String reasonEnded){this.reasonEnded = reasonEnded;}
	
	public void setGUID(String _GUID){GUID = _GUID;}
	public String getGUID(){return GUID;}
	
	public ContentValues contentValuesForMedication()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.STARTDATE, getTime());
		content.put(iStayHealthyDatabaseSchema.ENDDATE, endDate);
		content.put(iStayHealthyDatabaseSchema.NAME, name);
		content.put(iStayHealthyDatabaseSchema.DRUG, drug);
		if(isART){
			content.put(iStayHealthyDatabaseSchema.ISART, 1);
		}
		else
			content.put(iStayHealthyDatabaseSchema.ISART, 0);
		content.put(iStayHealthyDatabaseSchema.REASONENDED, reasonEnded);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	
	public void setMedication(Cursor cursor)
	{
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
		endDate = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ENDDATE));
		name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
		drug = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
		int intIsART = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ISART));
		if(0 < intIsART)
			isART = true;
		else
			isART = false;
		reasonEnded = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.REASONENDED));
	}
	
	
}

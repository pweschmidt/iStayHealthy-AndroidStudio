package com.pweschmidt.healthapps.datamodel;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import android.content.ContentValues;
import android.database.Cursor;

public class Procedures extends MedicalEvent
{
	private String Illness;
	private long EndDate;
	private String Name;
	private String Notes;
	private String CausedBy;

	public Procedures(){
		Illness = "";
		setTime((new java.util.Date()).getTime());
		EndDate = 0;
		Name = "";
		Notes = "";
		CausedBy = "";
	}

	public String getIllness(){return Illness;}
	public long getEndDate(){return EndDate;}
	public String getName(){return Name;}
	public String getNotes(){return Notes;}
	public String getCausedBy(){return CausedBy;}

	public void setIllness(String text){ Illness = text;}
	public void setEndDate(long value){ EndDate = value;}
	public void setName(String text){ Name = text;}
	public void setNotes(String text){ Notes = text;}
	public void setCausedBy(String text){ CausedBy = text;}

	public ContentValues contentValuesForProcedures()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.DATE, getTime());
		content.put(iStayHealthyDatabaseSchema.ENDDATE, EndDate);
		content.put(iStayHealthyDatabaseSchema.ILLNESS, Illness);
		content.put(iStayHealthyDatabaseSchema.NAME, Name);
		content.put(iStayHealthyDatabaseSchema.NOTES, Notes);
		content.put(iStayHealthyDatabaseSchema.CAUSEDBY, CausedBy);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	public void setProcedures(Cursor cursor)
	{
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DATE));
		EndDate = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ENDDATE));
		Illness = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ILLNESS));
		Name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
		CausedBy = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CAUSEDBY));
		Notes = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NOTES));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));		
	}

}

package com.pweschmidt.healthapps.datamodel;

import android.content.ContentValues;
import android.database.Cursor;

import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

public class SideEffects extends MedicalEvent
{
	public static final int MINOR = 0;
	public static final int MAJOR = 1;
	public static final int SERIOUS = 2;
	private String sideEffects;
	private String name;
	private String drug;
	private int seriousness;
	
	public SideEffects()
	{
		time = new java.util.Date().getTime();
		sideEffects = "";
		name = "";
		drug = "";
		seriousness = SideEffects.MINOR;
	}
	
	public int getSeriousness(){return seriousness;}
	public void setSeriousness(int seriousness){this.seriousness = seriousness;}
	
	
	public void setSideEffects(String effects){sideEffects = effects;}
	public String getSideEffects(){return sideEffects;}
	
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

	public ContentValues contentValuesForMedication()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.SIDEEFFECTS, sideEffects);
		content.put(iStayHealthyDatabaseSchema.SIDEEFFECTSDATE, time);
		content.put(iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS, seriousness);
		content.put(iStayHealthyDatabaseSchema.NAME, name);
		content.put(iStayHealthyDatabaseSchema.DRUG, drug);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	
	public void setMedication(Cursor cursor)
	{
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SIDEEFFECTSDATE));
		sideEffects = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SIDEEFFECTS));
		drug = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG));
		name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
		seriousness = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS));
	}

}

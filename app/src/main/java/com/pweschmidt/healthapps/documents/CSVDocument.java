package com.pweschmidt.healthapps.documents;

import com.pweschmidt.healthapps.*;
import android.database.Cursor;
//import android.util.Log;
import android.content.*;
import java.util.*;
import android.text.format.*;

public class CSVDocument 
{
//	private static final String TAG = "CSVDocument";
	private StringBuffer text;
	private String TAB = "\t";
	private String NEWLINE = "\n";
	private String NOTPRESENT = "n/a";
	private Context context;

	public CSVDocument(Context context)
	{
//		Log.d(TAG,"creating CSV document");
		this.context = context;
		text = new StringBuffer("iStayHealthy Android");
		text.append(NEWLINE);
		text.append(DateFormat.format("dd MMM yyyy", new Date()));
		text.append(NEWLINE);
		
		writefromSQL(context);
	}

	/**
	 * 
	 * @param context
	 * @throws SQLiteException
	 */
	private void writefromSQL(Context context)
	{
		writeResults();
		writeMedication();
		writeMissedMedication();
		writeSideEffects();
		writeOtherMedication();
	}
	

	/**
	 * 
	 * @param adapter
	 */
	private void writeResults()
	{
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.RESULTS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			text.append("No Results"+NEWLINE);
			cursor.close();
			return;
		}
		text.append("Date"+TAB+"CD4 Count"+TAB+"CD4 %"+TAB+"Viral Load"+TAB+"Viral Load HepC"+NEWLINE);
		while(cursor.isAfterLast() == false)
		{
			long date = cursor.getLong(cursor.getColumnIndex("ResultsDate"));
			String dateString = DateFormat.format("dd MMM yyyy", date).toString();
			int cd4 = cursor.getInt(cursor.getColumnIndex("CD4"));
	  	    float cd4Percent = cursor.getFloat(cursor.getColumnIndex("CD4Percent"));
	  	    int viralLoad = cursor.getInt(cursor.getColumnIndex("ViralLoad"));
	  	    int viralLoadHepC = cursor.getInt(cursor.getColumnIndex("HepCViralLoad"));
	  	    text.append(dateString+TAB);
	  	    if(0 < cd4)
	  	    {
	  	    	text.append(""+cd4+TAB);
	  	    }
	  	    else
	  	    {
	  	    	text.append(NOTPRESENT+TAB);
	  	    }
			
	  	    if(0 < cd4Percent)
	  	    {
	  	    	text.append(""+cd4Percent+"%"+TAB);
	  	    }
	  	    else
	  	    {
	  	    	text.append(NOTPRESENT+TAB);
	  	    }
	  	    if(0 < viralLoad)
	  	    {
	  	    	if(10 >= viralLoad)
	  	    	{
	  	    		text.append("undetectable"+TAB);
	  	    	}
	  	    	else
	  	    	{
	  	    		text.append(""+viralLoad+TAB);
	  	    	}
	  	    }
	  	    else
	  	    {
	  	    	text.append(NOTPRESENT+TAB);
	  	    }
	  	    
	  	    if(0 < viralLoadHepC)
	  	    {
	  	    	text.append(""+viralLoadHepC+TAB);
	  	    }
	  	    else
	  	    {
	  	    	text.append(NOTPRESENT+TAB);
	  	    }
	  	    text.append(NEWLINE);
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void writeMedication()
	{
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			text.append("No HIV Medication"+NEWLINE);
			cursor.close();
			return;
		}
		text.append("Start Date"+TAB+"Medication Name"+TAB+"Drugs"+NEWLINE);
		while(cursor.isAfterLast() == false)
		{
			long date = cursor.getLong(cursor.getColumnIndex("StartDate"));
			String dateString = DateFormat.format("dd MMM yyyy", date).toString();
			String name = cursor.getString(cursor.getColumnIndex("Name"));
			String drug = cursor.getString(cursor.getColumnIndex("Drug"));
	  	    text.append(dateString+TAB);
	  	    text.append(name+TAB);
	  	    text.append(drug+TAB);
	  	    text.append(NEWLINE);
			cursor.moveToNext();
		}
		cursor.close();
		
	}

	/**
	 * 
	 * @param adapter
	 */
	private void writeMissedMedication()
	{
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			text.append("No Missed HIV Medication"+NEWLINE);
			cursor.close();
			return;
		}
		text.append("Missed Date"+TAB+"Medication Name"+NEWLINE);
		while(cursor.isAfterLast() == false)
		{
			long date = cursor.getLong(cursor.getColumnIndex("MissedDate"));
			String dateString = DateFormat.format("dd MMM yyyy", date).toString();
			String name = cursor.getString(cursor.getColumnIndex("Name"));
	  	    text.append(dateString+TAB);
	  	    text.append(name+TAB);
	  	    text.append(NEWLINE);
			cursor.moveToNext();
		}
		cursor.close();
		
	}

	/**
	 * 
	 * @param adapter
	 */
	private void writeSideEffects()
	{
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			text.append("No Side Effects"+NEWLINE);
			cursor.close();
			return;
		}
		text.append("Side Effects Date"+TAB+"Medication Name"+TAB+"Side Effects"+NEWLINE);
		while(cursor.isAfterLast() == false)
		{
			long date = cursor.getLong(cursor.getColumnIndex("SideEffectsDate"));
			String dateString = DateFormat.format("dd MMM yyyy", date).toString();
			String name = cursor.getString(cursor.getColumnIndex("Name"));
			String effects = cursor.getString(cursor.getColumnIndex("SideEffects"));
	  	    text.append(dateString+TAB);
	  	    text.append(name+TAB);
	  	    text.append(effects+TAB);
	  	    text.append(NEWLINE);
			cursor.moveToNext();
		}
		cursor.close();
		
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void writeOtherMedication()
	{
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			text.append("No Other Medication"+NEWLINE);
			cursor.close();
			return;
		}
		text.append("Start Date"+TAB+"Medication Name"+TAB+"Dose in [mg]"+NEWLINE);
		while(cursor.isAfterLast() == false)
		{
			long date = cursor.getLong(cursor.getColumnIndex("StartDate"));
			String dateString = DateFormat.format("dd MMM yyyy", date).toString();
			String name = cursor.getString(cursor.getColumnIndex("Name"));
			int dose = cursor.getInt(cursor.getColumnIndex("Dose"));
	  	    text.append(dateString+TAB);
	  	    text.append(name+TAB);
	  	    text.append(""+dose+TAB);
	  	    text.append(NEWLINE);
			cursor.moveToNext();
		}
		cursor.close();
		
	}
	
	/**
	 * returns the data as comma-separated-value (CSV) string
	 */
	public String toString(){
//		Log.d(TAG,"csvString = "+text.toString());
		return text.toString();
	}
	
}

package com.pweschmidt.healthapps;

import java.util.Date;

import com.pweschmidt.healthapps.datamodel.*;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.content.ContentValues;

public class iStayHealthy extends Application 
{
	
	private static iStayHealthy applicationInstance;
	private boolean isPasswordEnabled;
	public static final String ISTAYHEALTHY_PREFS = "iStayHealthyPrefsFile";
	

	public void onConfigurationChanged(Configuration newConfiguration)
	{
		super.onConfigurationChanged(newConfiguration);
	}
	
	public void onCreate()
	{
		super.onCreate();
		applicationInstance = this;
		setUpMasterRecord();
        SharedPreferences prefs = getSharedPreferences(ISTAYHEALTHY_PREFS, 0);
        isPasswordEnabled = prefs.getBoolean("isPasswordEnabled", false);        
        String test = (prefs.getString("password", "")).trim();
        if(isPasswordEnabled && test.equals(""))
        {
            SharedPreferences.Editor editor = prefs.edit();
            isPasswordEnabled = false;
            editor.putBoolean("isPasswordEnabled", isPasswordEnabled);
            editor.commit();
        }
	}
	
	public void onLowMemory()
	{
		super.onLowMemory();
	}	
	
	public static iStayHealthy getInstance()
	{
		return applicationInstance;
	}
	
	public boolean requiresPassword(){return isPasswordEnabled;}
	
	private void setUpMasterRecord()
	{
		Cursor cursor = getApplicationContext().getContentResolver().query(iStayHealthyContentProvider.RECORDS_CONTENT_URI, null, null, null, null);		
		int records = cursor.getCount();
		cursor.close();
    	if(0 == records)
    	{
    		iStayHealthyRecord masterRecord = new iStayHealthyRecord();
    		masterRecord.setLastModified((new Date()).getTime());
    		ContentValues content = masterRecord.contentForMasterRecord();
    		getApplicationContext().getContentResolver().insert(iStayHealthyContentProvider.RECORDS_CONTENT_URI, content);
    	}
	}
		
	
}

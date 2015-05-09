package com.pweschmidt.healthapps;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class iStayHealthyDatabaseHelper extends SQLiteOpenHelper {

	private Context context;

	public iStayHealthyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(iStayHealthyDatabaseSchema.CREATEMAINTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEMEDICATIONTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEMISSEDMEDICATIONTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEOTHERMEDICATIONTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATERESULTSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEALERTSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATESIDEEFFECTSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEPROCEDURESTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATECONTACTSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEPREVIOUSMEDSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEWELLNESSTABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		if(iStayHealthyDatabaseSchema._MISSING_Table_DATABASE_VERSION > oldVersion){
			database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MEDICATIONTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.TINTABEEKEY);
			database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.TINTABEEKEY);
			database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.TINTABEEKEY);
			database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.TINTABEEKEY);
			database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.ALERTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.TINTABEEKEY);
			database.execSQL(iStayHealthyDatabaseSchema.CREATEPROCEDURESTABLE);
			database.execSQL(iStayHealthyDatabaseSchema.CREATECONTACTSTABLE);
			database.execSQL(iStayHealthyDatabaseSchema.CREATEMAINTABLE);
			upgradeFromVersion11(database);
		}
		else if(iStayHealthyDatabaseSchema._MISSING_Table_DATABASE_VERSION == oldVersion){
			//for those who installed version 2.0.0 the main table wasn't created.
			//bummer
			database.execSQL(iStayHealthyDatabaseSchema.CREATEMAINTABLE);				
		}
					
		if(iStayHealthyDatabaseSchema._V2_0_8_DATABASE_VERSION > oldVersion)
		{
			Cursor cursor = database.query(iStayHealthyDatabaseSchema.RESULTSTABLE, null, null, null, null, null, iStayHealthyDatabaseSchema.PLATELETCOUNT);
			boolean hasEntries = false;
			if(null != cursor)
			{
				if(1 <= cursor.getColumnCount())
					hasEntries = true;	
				else
					hasEntries = false;
			}
			else
				hasEntries = false;
			if(!hasEntries)
			{
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.PLATELETCOUNT);
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.HAEMOGLOBULIN);
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.WHITECELLCOUNT);
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.REDCELLCOUNT);		
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.MISSEDREASON);				
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS);
				database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.ALERTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.ALERTREQUESTCODE+" integer default 0");				
			}
			if(null != cursor)
				cursor.close();
			upgradeAlertsForVersion16(database);				
			
		}
		
		if(iStayHealthyDatabaseSchema.CURRENT_DATABASE_VERSION > oldVersion){
			upgradeToVersion17(database);
		}
		
	}
	
	/**
	 * For version 17 we have 2 new tables and a whole wagonload of new columns
	 * @param database
	 */
	private void upgradeToVersion17(SQLiteDatabase database)
	{
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MAINRECORDTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.ISDIABETIC);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MAINRECORDTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.ISSMOKER);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MAINRECORDTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.GENDER);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.MAINRECORDTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.DOB);
		
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.CHOLESTEROLRATIO);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.CARDIACRISKFACTOR);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.HEPBTITER);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.HEPCTITER);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAlanineTransaminase);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAspartateTransaminase);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAlkalinePhosphatase);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAlbumin);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAlanineTotalBilirubin);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverAlanineDirectBilirubin);
		database.execSQL("ALTER TABLE "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ADD COLUMN "+iStayHealthyDatabaseSchema.liverGammaGlutamylTranspeptidase);
		
		database.execSQL(iStayHealthyDatabaseSchema.CREATEPREVIOUSMEDSTABLE);
		database.execSQL(iStayHealthyDatabaseSchema.CREATEWELLNESSTABLE);		
	}
	
	/**
	 * in version 11 and below of the database, the results table has an error.
	 * the error is, that the Create String has 2 commas missing after Systole.
	 * Here is the template of what we have to do
	 * 1.) create a tmp table of what results table should look like
	 * 2.) copy the data that do work from existing results table into the new one using an
	 * SQLite insert into tableX (column1, column2...) select column1, column2.... from tableY
	 * statement
	 * 3.) drop the results table
	 * 4.) recreate the results table - this time the correct one
	 * 5.) copy the data from the tmp table into the newly created results table
	 * 6.) drop the tmp table
	 * @param _database
	 * iStayHealthyDatabaseSchema.
	 */
	private void upgradeFromVersion11(SQLiteDatabase database){
		String TMPTABLE = "TMPRESULTSTABLE";
		String CREATETMPTABLE = "create table "+TMPTABLE+
				" ("+iStayHealthyDatabaseSchema.KEY_ID+" integer primary key autoincrement, "+iStayHealthyDatabaseSchema.CD4+" integer,"+iStayHealthyDatabaseSchema.RESULTSDATE+" long,"
				+iStayHealthyDatabaseSchema.VIRALLOAD+" integer,"+iStayHealthyDatabaseSchema.CD4PERCENT+" real,"+iStayHealthyDatabaseSchema.HEPCVIRALLOAD+" integer,"+iStayHealthyDatabaseSchema.GLUCOSE+" real,"
				+iStayHealthyDatabaseSchema.TOTALCHOLESTEROL+" real,"+iStayHealthyDatabaseSchema.LDL+" real,"+iStayHealthyDatabaseSchema.HDL+" real,"+iStayHealthyDatabaseSchema.TRIGYLCERIDE+" real,"+iStayHealthyDatabaseSchema.HEARTRATE+" integer,"
				+iStayHealthyDatabaseSchema.SYSTOLE+" integer,"+iStayHealthyDatabaseSchema.DIASTOLE+" integer,"+iStayHealthyDatabaseSchema.OXYGENLEVEL+" real, "+iStayHealthyDatabaseSchema.WEIGHT+" real, "
				+iStayHealthyDatabaseSchema.TINTABEEKEY+" text, "+iStayHealthyDatabaseSchema.GUIDTEXT+" GUID);";
		//create tmp table
		database.execSQL(CREATETMPTABLE);
		
		//SQLite copy data from results to tmp table statement
		String COPYTOSTRING = "insert into "+TMPTABLE+" ("+
				iStayHealthyDatabaseSchema.CD4+", "+iStayHealthyDatabaseSchema.RESULTSDATE+", "+iStayHealthyDatabaseSchema.VIRALLOAD+", "+iStayHealthyDatabaseSchema.CD4PERCENT+", "+iStayHealthyDatabaseSchema.GUIDTEXT+" ) select "+
				iStayHealthyDatabaseSchema.CD4+", "+iStayHealthyDatabaseSchema.RESULTSDATE+", "+iStayHealthyDatabaseSchema.VIRALLOAD+", "+iStayHealthyDatabaseSchema.CD4PERCENT+", "+iStayHealthyDatabaseSchema.GUIDTEXT+
				" from "+iStayHealthyDatabaseSchema.RESULTSTABLE;

		//execute SQL copy statement
		database.execSQL(COPYTOSTRING);
		//drop results table since we copied the relevant data across into tmp table
		database.execSQL("drop table if exists "+iStayHealthyDatabaseSchema.RESULTSTABLE);
		//recreate results table
		database.execSQL(iStayHealthyDatabaseSchema.CREATERESULTSTABLE);

		//SQLite copy data from tmp to results table statement
		String COPYFROMSTRING = "insert into "+iStayHealthyDatabaseSchema.RESULTSTABLE+" ("
				+iStayHealthyDatabaseSchema.CD4+", "+iStayHealthyDatabaseSchema.RESULTSDATE+", "+iStayHealthyDatabaseSchema.VIRALLOAD+", "+iStayHealthyDatabaseSchema.CD4PERCENT+", "+iStayHealthyDatabaseSchema.GUIDTEXT+" ) select "
				+iStayHealthyDatabaseSchema.CD4+", "+iStayHealthyDatabaseSchema.RESULTSDATE+", "+iStayHealthyDatabaseSchema.VIRALLOAD+", "+iStayHealthyDatabaseSchema.CD4PERCENT+", "+iStayHealthyDatabaseSchema.GUIDTEXT+
				" from "+TMPTABLE;			
		//execute SQL copy statement
		database.execSQL(COPYFROMSTRING);
		//drop tmp table since we copied the relevant data across back into the results table
		database.execSQL("drop table if exists "+TMPTABLE);
	}
	
	/**
	 * the method runs through all registered alerts and resets them.
	 * In particular the request code for the PendingIntent needs to be unique.
	 * In the previous versions we had it set to 0, so that in case of multiple
	 * alerts, only one ever fired.
	 * @param database
	 */
	private void upgradeAlertsForVersion16(SQLiteDatabase database){
		Cursor cursor = database.query(iStayHealthyDatabaseSchema.ALERTSTABLE, null, null, null, null, null, iStayHealthyDatabaseSchema.ALERTLABEL);
		if(1 >= cursor.getColumnCount()){
			cursor.close();
			return;
		}
		int standardInterval = 24 * 60 * 60 * 1000;//24 hour repeat interval
		AlarmManager alarm = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		int requestCode = 0;
		Calendar calendar = Calendar.getInstance();
		java.util.Date date = new java.util.Date();
		long now = date.getTime();
		while(cursor.moveToNext()){
			long rowId = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.KEY_ID));
			String label = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTLABEL));
			int repeat = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN));
			long startTime = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSTARTTIME));
			Calendar alarmCalendar = Calendar.getInstance();
			alarmCalendar.setTimeInMillis(startTime);
			boolean isEveryDay = (0 < repeat);
			calendar.set(Calendar.HOUR_OF_DAY, alarmCalendar.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, alarmCalendar.get(Calendar.MINUTE));

			long time = calendar.getTimeInMillis();
			Intent intent = new Intent(context, AlarmReceiver.class);
			intent.putExtra("Label", label);
			intent.putExtra("isEveryDay", isEveryDay);
			
			PendingIntent pendingSingleIntent = PendingIntent.getBroadcast(context, 0, intent, 0);		
			alarm.cancel(pendingSingleIntent);
			
			
			Intent newIntent = new Intent(context, AlarmReceiver.class);
			newIntent.putExtra("Label", label);
			newIntent.putExtra("isVibrate", true);
			newIntent.putExtra("isEveryDay", isEveryDay);
			newIntent.putExtra("requestCode", requestCode);
			PendingIntent newPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), requestCode, newIntent, 0);
			
			if(isEveryDay){
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, standardInterval, newPendingIntent);								
			}
			else{
				long diff = startTime - now;
				if( 0 < diff){
					alarm.set(AlarmManager.RTC_WAKEUP, startTime, newPendingIntent);					
				}
			}
						
			ContentValues content = new ContentValues();
			content.put(iStayHealthyDatabaseSchema.ALERTLABEL, label);
			content.put(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN, repeat);
			content.put(iStayHealthyDatabaseSchema.ALERTSTARTTIME, startTime);
			content.put(iStayHealthyDatabaseSchema.ALERTREQUESTCODE, requestCode);
			content.put(iStayHealthyDatabaseSchema.ALERTTEXT, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTTEXT)));
			content.put(iStayHealthyDatabaseSchema.ALERTSOUND, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSOUND)));
			content.put(iStayHealthyDatabaseSchema.ALERTHOUR, cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTHOUR)));
			content.put(iStayHealthyDatabaseSchema.AlERTMINUTE, cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.AlERTMINUTE)));
			content.put(iStayHealthyDatabaseSchema.ALERTTIMEZONEOFFSET, cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTTIMEZONEOFFSET)));
			content.put(iStayHealthyDatabaseSchema.GUIDTEXT, java.util.UUID.randomUUID().toString());
			content.put(iStayHealthyDatabaseSchema.TINTABEEKEY, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.TINTABEEKEY)));
			database.updateWithOnConflict(iStayHealthyDatabaseSchema.ALERTSTABLE, content, iStayHealthyDatabaseSchema.KEY_ID + "=" + rowId, null, SQLiteDatabase.CONFLICT_IGNORE);				

			requestCode = requestCode + 1;
		}
		
		cursor.close();
	}

	
}

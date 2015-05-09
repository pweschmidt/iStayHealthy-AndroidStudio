package com.pweschmidt.healthapps;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
//import android.util.Log;

public class iStayHealthyContentProvider extends ContentProvider 
{
	
//	private static final String TAG = "iStayHealthyContentProvider";
	private iStayHealthyDatabaseHelper databaseHelper;
	
	private static final String AUTHORITY = "com.pweschmidt.healthapps.iStayHealthy.iStayHealthyContentProvider";
	private static final String RECORDS = "records";
	private static final String RESULTS = "results";
	private static final String REVERSEDRESULTS = "reversedresults";
	private static final String MEDS = "meds";
	private static final String MISSEDMEDS = "missedmeds";
	private static final String OTHERMEDS = "othermeds";
	private static final String PREVIOUSMEDS = "previousmeds";
	private static final String ALERTS = "alerts";
	private static final String CLINICS = "clinics";
	private static final String EFFECTS = "sideeffects";
	private static final String PROCEDURES = "procedures";
	private static final String WELLNESS = "wellness";
	
	public static final int ALLRESULT = 100;
	public static final int SINGLERESULT = 101;
	public static final int ALLMEDS = 200;
	public static final int SINGLEMEDS = 201;
	public static final int ALLMISSEDMEDS = 300;
	public static final int SINGLEMISSEDMEDS = 301;
	public static final int ALLOTHERMEDS = 400;
	public static final int SINGLEOTHERMEDS = 401;
	public static final int ALLPREVIOUSMEDS = 500;
	public static final int SINGLEPREVIOUSMEDS = 501;
	public static final int ALLALERTS = 600;
	public static final int SINGLEALERTS = 601;
	public static final int ALLCLINICS = 700;
	public static final int SINGLECLINICS = 701;
	public static final int ALLEFFECTS = 800;
	public static final int SINGLEEFFECTS = 801;
	public static final int ALLPROCEDURES = 900;
	public static final int SINGLEPROCEDURES = 901;
	public static final int ALLWELLNESS = 1000;
	public static final int SINGLEWELLNESS = 1001;
	public static final int ALLRECORDS = 1100;
	public static final int SINGLERECORDS = 1101;
	public static final int ALLREVERSEDRESULTS = 1200;
	public static final int SINGLEREVERSEDRESULTS = 1201;
	
	
	public static final Uri RECORDS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+RECORDS);
	public static final Uri RESULTS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+RESULTS);
	public static final Uri REVERSED_RESULTS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+REVERSEDRESULTS);
	public static final Uri MEDS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+MEDS);
	public static final Uri MISSEDMEDS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+MISSEDMEDS);
	public static final Uri OTHERMEDS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+OTHERMEDS);
	public static final Uri PREVIOUSMEDS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+PREVIOUSMEDS);
	public static final Uri ALERTS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+ALERTS);
	public static final Uri CLINICS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+CLINICS);
	public static final Uri EFFECTS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+EFFECTS);
	public static final Uri PROCEDURES_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+PROCEDURES);
	public static final Uri WELLNESS_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+WELLNESS);
	
	private static final UriMatcher uriMatcher;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, RECORDS, ALLRECORDS);
		uriMatcher.addURI(AUTHORITY, RECORDS + "/#", SINGLERECORDS);
		uriMatcher.addURI(AUTHORITY, RESULTS, ALLRESULT);
		uriMatcher.addURI(AUTHORITY, RESULTS + "/#", SINGLERESULT);
		uriMatcher.addURI(AUTHORITY, REVERSEDRESULTS, ALLREVERSEDRESULTS);
		uriMatcher.addURI(AUTHORITY, REVERSEDRESULTS + "/#", SINGLEREVERSEDRESULTS);
		uriMatcher.addURI(AUTHORITY, MEDS, ALLMEDS);
		uriMatcher.addURI(AUTHORITY, MEDS + "/#", SINGLEMEDS);
		uriMatcher.addURI(AUTHORITY, MISSEDMEDS, ALLMISSEDMEDS);
		uriMatcher.addURI(AUTHORITY, MISSEDMEDS + "/#", SINGLEMISSEDMEDS);
		uriMatcher.addURI(AUTHORITY, OTHERMEDS, ALLOTHERMEDS);
		uriMatcher.addURI(AUTHORITY, OTHERMEDS + "/#", SINGLEOTHERMEDS);
		uriMatcher.addURI(AUTHORITY, PREVIOUSMEDS, ALLPREVIOUSMEDS);
		uriMatcher.addURI(AUTHORITY, PREVIOUSMEDS + "/#", SINGLEPREVIOUSMEDS);
		uriMatcher.addURI(AUTHORITY, ALERTS, ALLALERTS);
		uriMatcher.addURI(AUTHORITY, ALERTS + "/#", SINGLEALERTS);
		uriMatcher.addURI(AUTHORITY, CLINICS, ALLCLINICS);
		uriMatcher.addURI(AUTHORITY, CLINICS + "/#", SINGLECLINICS);
		uriMatcher.addURI(AUTHORITY, EFFECTS, ALLEFFECTS);
		uriMatcher.addURI(AUTHORITY, EFFECTS + "/#", SINGLEEFFECTS);
		uriMatcher.addURI(AUTHORITY, PROCEDURES, ALLPROCEDURES);
		uriMatcher.addURI(AUTHORITY, PROCEDURES + "/#", SINGLEPROCEDURES);
		uriMatcher.addURI(AUTHORITY, WELLNESS, ALLWELLNESS);
		uriMatcher.addURI(AUTHORITY, WELLNESS + "/#", SINGLEWELLNESS);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{

		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int uriType = uriMatcher.match(uri);
		int rowsAffected = 0;
		String id = null;
		switch(uriType)
		{
		case ALLRECORDS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.MAINRECORDTABLE, selection, selectionArgs);
			break;
		case SINGLERECORDS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MAINRECORDTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MAINRECORDTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLRESULT:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.RESULTSTABLE, selection, selectionArgs);
			break;
		case SINGLERESULT:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.RESULTSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.RESULTSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLMEDS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.MEDICATIONTABLE, selection, selectionArgs);
			break;
		case SINGLEMEDS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MEDICATIONTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MEDICATIONTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLMISSEDMEDS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, selection, selectionArgs);
			break;
		case SINGLEMISSEDMEDS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLOTHERMEDS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, selection, selectionArgs);
			break;
		case SINGLEOTHERMEDS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLPREVIOUSMEDS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, selection, selectionArgs);
			break;
		case SINGLEPREVIOUSMEDS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLALERTS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.ALERTSTABLE, selection, selectionArgs);
			break;
		case SINGLEALERTS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.ALERTSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.ALERTSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLCLINICS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.CONTACTSTABLE, selection, selectionArgs);
			break;
		case SINGLECLINICS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.CONTACTSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.CONTACTSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLEFFECTS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, selection, selectionArgs);
			break;
		case SINGLEEFFECTS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLPROCEDURES:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.PROCEDURESTABLE, selection, selectionArgs);
			break;
		case SINGLEPROCEDURES:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.PROCEDURESTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.PROCEDURESTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLWELLNESS:
			rowsAffected = database.delete(iStayHealthyDatabaseSchema.WELLNESSTABLE, selection, selectionArgs);
			break;
		case SINGLEWELLNESS:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.WELLNESSTABLE, iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsAffected = database.delete(iStayHealthyDatabaseSchema.WELLNESSTABLE, selection + " and " + iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
//		Log.d(TAG,"inserting content values into the DB");
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int uriType = uriMatcher.match(uri);
		Uri insertedUri = null;
//		Log.d(TAG,"the uriType is "+uriType);
		long id = 0;
		switch(uriType)
		{
		case ALLRECORDS:
//			Log.d(TAG,"the uriType is ALLRECORDS");
			id = database.insert(iStayHealthyDatabaseSchema.MAINRECORDTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(RECORDS_CONTENT_URI, id);
			break;
		case ALLRESULT:
//			Log.d(TAG,"the uriType is ALLRESULT");
			id = database.insert(iStayHealthyDatabaseSchema.RESULTSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(RESULTS_CONTENT_URI, id);
			break;
		case ALLMEDS:
//			Log.d(TAG,"the uriType is ALLMEDS");
			id = database.insert(iStayHealthyDatabaseSchema.MEDICATIONTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(MEDS_CONTENT_URI, id);
			break;
		case ALLMISSEDMEDS:
//			Log.d(TAG,"the uriType is ALLMEDS");
			id = database.insert(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(MISSEDMEDS_CONTENT_URI, id);
			break;
		case ALLOTHERMEDS:
//			Log.d(TAG,"the uriType is ALLOTHERMEDS");
			id = database.insert(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(OTHERMEDS_CONTENT_URI, id);
			break;
		case ALLPREVIOUSMEDS:
//			Log.d(TAG,"the uriType is ALLPREVIOUSMEDS");
			id = database.insert(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(PREVIOUSMEDS_CONTENT_URI, id);
			break;
		case ALLALERTS:
//			Log.d(TAG,"the uriType is ALLALERTS");
			id = database.insert(iStayHealthyDatabaseSchema.ALERTSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(ALERTS_CONTENT_URI, id);
			break;
		case ALLCLINICS:
//			Log.d(TAG,"the uriType is ALLCLINICS");
			id = database.insert(iStayHealthyDatabaseSchema.CONTACTSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(CLINICS_CONTENT_URI, id);
			break;
		case ALLEFFECTS:
//			Log.d(TAG,"the uriType is ALLEFFECTS");
			id = database.insert(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(EFFECTS_CONTENT_URI, id);
			break;
		case ALLPROCEDURES:
//			Log.d(TAG,"the uriType is ALLPROCEDURES");
			id = database.insert(iStayHealthyDatabaseSchema.PROCEDURESTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(PROCEDURES_CONTENT_URI, id);
			break;
		case ALLWELLNESS:
//			Log.d(TAG,"the uriType is ALLWELLNESS");
			id = database.insert(iStayHealthyDatabaseSchema.WELLNESSTABLE, null, values);
			insertedUri = ContentUris.withAppendedId(WELLNESS_CONTENT_URI, id);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return insertedUri;
	}

	@Override
	public boolean onCreate() 
	{
	    databaseHelper = new iStayHealthyDatabaseHelper(getContext(),iStayHealthyDatabaseSchema.DATABASE_NAME, null,iStayHealthyDatabaseSchema.CURRENT_DATABASE_VERSION);		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) 
	{
//		Log.d(TAG,"iStayHealthyContentProvider::query method");
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		int uriType = uriMatcher.match(uri);
//		Log.d(TAG,"iStayHealthyContentProvider::query method and the uri type is "+uriType);
		switch(uriType)
		{
		case ALLRECORDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MAINRECORDTABLE);
			break;
		case SINGLERECORDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MAINRECORDTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLRESULT:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.RESULTSTABLE);
			sortOrder = iStayHealthyDatabaseSchema.RESULTSDATE;
			break;
		case SINGLERESULT:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.RESULTSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLREVERSEDRESULTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.RESULTSTABLE);
			sortOrder = iStayHealthyDatabaseSchema.RESULTSDATE + " DESC";
			break;
		case SINGLEREVERSEDRESULTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.RESULTSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MEDICATIONTABLE);
			sortOrder = iStayHealthyDatabaseSchema.STARTDATE;
			break;
		case SINGLEMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MEDICATIONTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLMISSEDMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE);
			sortOrder = iStayHealthyDatabaseSchema.MISSEDDATE;
			break;
		case SINGLEMISSEDMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLOTHERMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE);
			sortOrder = iStayHealthyDatabaseSchema.STARTDATE;
			break;
		case SINGLEOTHERMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLPREVIOUSMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE);
			sortOrder = iStayHealthyDatabaseSchema.ENDDATE + " DESC";
			break;
		case SINGLEPREVIOUSMEDS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLALERTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.ALERTSTABLE);
			break;
		case SINGLEALERTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.ALERTSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLCLINICS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.CONTACTSTABLE);
			break;
		case SINGLECLINICS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.CONTACTSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLEFFECTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE);
			sortOrder = iStayHealthyDatabaseSchema.SIDEEFFECTSDATE + " DESC";			
			break;
		case SINGLEEFFECTS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLPROCEDURES:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.PROCEDURESTABLE);
			sortOrder = iStayHealthyDatabaseSchema.DATE + " DESC";
			break;
		case SINGLEPROCEDURES:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.PROCEDURESTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		case ALLWELLNESS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.WELLNESSTABLE);
			break;
		case SINGLEWELLNESS:
			queryBuilder.setTables(iStayHealthyDatabaseSchema.WELLNESSTABLE);
			queryBuilder.appendWhere(iStayHealthyDatabaseSchema.KEY_ID + " = "+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) 
	{
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		int uriType = uriMatcher.match(uri);
		int rowsUpdated = 0;
		String id = uri.getLastPathSegment();
		switch(uriType)
		{
		case ALLRECORDS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.MAINRECORDTABLE, 
					values, selection, selectionArgs);
			break;
		case SINGLERECORDS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MAINRECORDTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MAINRECORDTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLRESULT:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.RESULTSTABLE, values, selection, selectionArgs);
			break;
		case SINGLERESULT:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.RESULTSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.RESULTSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLMEDS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.MEDICATIONTABLE, values, selection, selectionArgs);
			break;
		case SINGLEMEDS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MEDICATIONTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MEDICATIONTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLMISSEDMEDS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, values, selection, selectionArgs);
			break;
		case SINGLEMISSEDMEDS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.MISSEDMEDICATIONTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLOTHERMEDS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, values, selection, selectionArgs);
			break;
		case SINGLEOTHERMEDS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.OTHERMEDICATIONTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLPREVIOUSMEDS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, values, selection, selectionArgs);
			break;
		case SINGLEPREVIOUSMEDS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.PREVIOUSMEDSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLALERTS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.ALERTSTABLE, values, selection, selectionArgs);
			break;
		case SINGLEALERTS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.ALERTSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.ALERTSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLCLINICS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.CONTACTSTABLE, values, selection, selectionArgs);
			break;
		case SINGLECLINICS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.CONTACTSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.CONTACTSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLEFFECTS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, values, selection, selectionArgs);
			break;
		case SINGLEEFFECTS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.SIDEEFFECTSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLPROCEDURES:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.PROCEDURESTABLE, values, selection, selectionArgs);
			break;
		case SINGLEPROCEDURES:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.PROCEDURESTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.PROCEDURESTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		case ALLWELLNESS:
			rowsUpdated = database.update(iStayHealthyDatabaseSchema.WELLNESSTABLE, values, selection, selectionArgs);
			break;
		case SINGLEWELLNESS:
			if(TextUtils.isEmpty(selection))
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.WELLNESSTABLE, values, 
						iStayHealthyDatabaseSchema.KEY_ID + "=" + id, null);
			}
			else
			{
				rowsUpdated = database.update(iStayHealthyDatabaseSchema.WELLNESSTABLE, 
						values, selection +" and "+iStayHealthyDatabaseSchema.KEY_ID + "=" + id, selectionArgs);				
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}

package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.text.format.DateFormat;
//import android.util.Log;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
//import android.text.*;
import android.net.Uri;

import com.pweschmidt.healthapps.datamodel.Medication;
import com.pweschmidt.healthapps.datamodel.PreviousMedication;

public class ModifyHIVDrugsActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "ModifyHIVDrugsActivity";
    private long rowID;
    private boolean hasEnded;
    private String name;
//    private String drug;
    private Date date;
	private Date endDate;
	private static final int CHANGE_DIALOG_ID = 0;
	private static final int END_DIALOG_ID = 1;
	private int whichDialog;
	private TextView startDateView;
	private TextView endDateView;
	private Medication hivMedication;
	private long previousStartTime;
	private Uri medUri;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
//    	Log.d(TAG,"onCreate");
    	setContentView(R.layout.modify_hiv_drugs);

//    	databaseAdapter = ((iStayHealthy)getApplicationContext()).getDatabase();
        name = "";
//        drug = "";
        hasEnded = false;
        date = new Date();
        endDate = new Date();
        whichDialog = CHANGE_DIALOG_ID;
        rowID = -1;

        ImageButton cancel = (ImageButton)findViewById(R.id.BackButton);
    	cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED, null);
				finish();//do nothing
			}
		});
    	
    	ImageButton save = (ImageButton)findViewById(R.id.SaveButton);
    	save.setOnClickListener(this);
    	
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	
    	TableRow startRow = (TableRow)findViewById(R.id.changeTreatmentDateRow);
    	startRow.setOnClickListener(this);
    	TableRow endRow = (TableRow)findViewById(R.id.endTreatmentDateRow);
    	endRow.setOnClickListener(this);
    	
    	startDateView = (TextView)findViewById(R.id.treatmentStartText);
    	endDateView = (TextView)findViewById(R.id.treatmentEndText);
    	endDateView.setText("---");
    	
    	Bundle extras = getIntent().getExtras();
    	if(null != extras)
    	{
    		rowID = extras.getLong("HIVMedicationIndex");
    		medUri = Uri.withAppendedPath(iStayHealthyContentProvider.MEDS_CONTENT_URI, String.valueOf(rowID));
    		Cursor medCursor = getContentResolver().query(medUri, null, null, null, null);
    		if(0 < medCursor.getCount() && medCursor.moveToFirst())
    		{
    			hivMedication = new Medication();
    			hivMedication.setMedication(medCursor);
    		}
//    		Log.d(TAG,"onCreate getting bundle extra with value rowID = "+rowID);
//    		results = databaseAdapter.getResults(rowID);
    		medCursor.close();
//    		Log.d(TAG,"onCreate - we found position index "+rowID);
        	TextView titleView = (TextView)findViewById(R.id.TitleMainTitle);
//        	hivMedication = databaseAdapter.getMedication(rowID);
        	name = hivMedication.getName();
//        	drug = hivMedication.getDrug();
        	previousStartTime = hivMedication.getTime();
        	date = new Date(previousStartTime);
    		startDateView.setText(DateFormat.format("dd MMM yyyy", date));
        	titleView.setText(name);
    	}
    	else
    	{
//    		Log.d(TAG,"onCreate no extras found ");    		
    	}
    	    	
	}

	/**
	 * 
	 */
    public void onClick(View view)
    {
    	int resId = view.getId();
    	switch(resId)
    	{
    	case R.id.changeTreatmentDateRow:
			whichDialog = CHANGE_DIALOG_ID;
			showDialog(CHANGE_DIALOG_ID);
    		break;
    	case R.id.endTreatmentDateRow:
			whichDialog = END_DIALOG_ID;
			showDialog(END_DIALOG_ID);
    		break;
    		
    	case R.id.SaveButton:
    		if( 0 <= rowID && null != hivMedication )
    		{
    			if(hasEnded)
    			{
	       			getContentResolver().delete(medUri, null, null);
    				PreviousMedication previous = new PreviousMedication();
    				previous.setDrug(hivMedication.getDrug());
    				previous.setName(hivMedication.getName());
    				previous.setTime(date.getTime());
    				previous.setIsART(true);
    				previous.setEndDate(endDate.getTime());
    				ContentValues previousContent = previous.contentValuesForMedication();
    				getContentResolver().insert(iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, previousContent);
    			}
    			else    				
    			{
        			long time = date.getTime();
        			if(time != previousStartTime)
        			{
            			hivMedication.setTime(date.getTime());
                		ContentValues content = hivMedication.contentValuesForMedication();
                		getContentResolver().update(medUri, content, null, null);
//            			databaseAdapter.updateMedication(rowID, hivMedication);    				
        			}    				
    			}
    		}
			setResult(RESULT_OK, null);
			finish();			
    		break;
    	case R.id.TrashButton:
    		AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    		String yes = getResources().getString(R.string.Yes);
    		String no = getResources().getString(R.string.No);
    		String message = getResources().getString(R.string.deleteentry);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	       		if(-1 != rowID /*&& null != databaseAdapter */){   		
    	       			getContentResolver().delete(medUri, null, null);
//    	    			databaseAdapter.removeMedication(rowID);
    	        		setResult(RESULT_OK, null);
    	    		}
    	       		ModifyHIVDrugsActivity.this.finish();
    	           }
    	       })
    	       .setNegativeButton(no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    		AlertDialog alert = builder.create();    	
    		alert.show();
    		break;    		
    	}
    	
    }
    	
	/**
	 * 
	 */
	protected Dialog onCreateDialog(int id) 
	{
		if(CHANGE_DIALOG_ID != id && END_DIALOG_ID != id)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		switch(id)
		{
		case CHANGE_DIALOG_ID:
			calendar.setTime(date);
			break;
		case END_DIALOG_ID:
			calendar.setTime(endDate);
			break;
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  year, month, day);
		dialog.setCancelable(false);
		return dialog;
	}
	
	/**
	 * 
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		switch(whichDialog)
		{
		case CHANGE_DIALOG_ID:
			date = new Date(year - 1900, monthOfYear, dayOfMonth);
			startDateView.setText(DateFormat.format("dd MMM yyyy", date));
			break;
		case END_DIALOG_ID:
			endDate = new Date(year - 1900, monthOfYear, dayOfMonth);
			hasEnded = true;
	    	endDateView.setText(DateFormat.format("dd MMM yyyy", endDate));
			break;
		}
		
	}	
        
}

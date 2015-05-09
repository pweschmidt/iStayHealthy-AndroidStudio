package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
//import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableRow;
//import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.content.ContentValues;

import com.pweschmidt.healthapps.datamodel.PreviousMedication;

public class EditPreviousActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final int BANNER_ACTIVITY_REQUEST_CODE = 100;
	private static final int DATE_DIALOG_ID = 0;
	private PreviousMedication previous;
	private TableRow startDateRow;
	private TableRow endDateRow;
	private long rowID;
	private Uri previousUri;
	private boolean isStartDate;
	private Date startDate;
	private Date endDate;
	private TextView startView;
	private TextView endView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_previous);
        rowID = -1;
        isStartDate = true;
		previous = new PreviousMedication();
        startDateRow = (TableRow)findViewById(R.id.startDateRow);
        startDateRow.setOnClickListener(this);
        
        endDateRow = (TableRow)findViewById(R.id.endDateRow);
        endDateRow.setOnClickListener(this);
		
        ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
        delete.setOnClickListener(this);
		
        ImageButton save = (ImageButton)findViewById(R.id.SaveButton);
        save.setOnClickListener(this);

        ImageButton banner = (ImageButton)findViewById(R.id.CancelButton);
        banner.setOnClickListener(this);
        
        Bundle extras = getIntent().getExtras();    	
    	if(null != extras)
    	{
    		rowID = extras.getLong("PreviousIndex");    	
    		previousUri = Uri.withAppendedPath(iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, String.valueOf(rowID));
    		Cursor cursor = getContentResolver().query(previousUri, null, null, null, null);
    		if(0 < cursor.getCount() && cursor.moveToFirst())
    		{
    			previous.setMedication(cursor);
    		}
    		cursor.close();   	
    		startDate = new Date(previous.getTime());
    		endDate = new Date(previous.getEndDate());
    		startView = (TextView)findViewById(R.id.selectedDateView);
    		startView.setText(DateFormat.format("dd MMM yyyy", startDate));
    		endView = (TextView)findViewById(R.id.selectedEndDateView);
    		endView.setText(DateFormat.format("dd MMM yyyy", endDate));    		
    	}
        
	}
	
	
	public void onClick(View view)
	{
		int resId = view.getId();
		switch(resId)
		{
		case R.id.SaveButton:
			if(null != startDate)
				previous.setTime(startDate.getTime());
			if(null != endDate)
				previous.setEndDate(endDate.getTime());
			ContentValues content = previous.contentValuesForMedication();
			if(rowID != -1)
			{
	    		getContentResolver().update(previousUri, content, null, null);									
			}
			finish();
			break;
		case R.id.startDateRow:
			isStartDate = true;
    		showDialog(DATE_DIALOG_ID);			
			break;
		case R.id.endDateRow:
			isStartDate = false;
    		showDialog(DATE_DIALOG_ID);			
			break;
		case R.id.CancelButton:
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
    	        	   if(rowID != -1){    	        		   
       	       				getContentResolver().delete(previousUri, null, null);
        	        		setResult(RESULT_OK, null);
    	        	   }
    	        	   EditPreviousActivity.this.finish();
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
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		if(isStartDate)
		{
			startDate = new Date(year - 1900, monthOfYear, dayOfMonth);
			startView.setText(DateFormat.format("dd MMM yyyy", startDate));
		}
		else
		{
			endDate = new Date(year - 1900, monthOfYear, dayOfMonth);
			endView.setText(DateFormat.format("dd MMM yyyy", endDate));			
		}
	}	

	protected Dialog onCreateDialog(int id) {
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar calendar = Calendar.getInstance();
		if(isStartDate)
		{
			calendar.setTime(startDate);					
		}
		else
		{
			calendar.setTime(endDate);								
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  year, month, day);
		dialog.setCancelable(false);
		return dialog;
	}
	
	
}

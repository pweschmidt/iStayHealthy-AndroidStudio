package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TableRow;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.text.format.DateFormat;
import android.view.View;
import android.database.Cursor;

import com.pweschmidt.healthapps.datamodel.OtherMedication;

public class ModifyOtherMedsActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "ModifyOtherMedsActivity";
	private Date startDate;
	private TextView dateText;
	private TableRow dateRow;
	private static final int DATE_DIALOG_ID = 0;
    private long rowID;
    private OtherMedication otherMedication;
	private Uri medUri;

	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");
        setContentView(R.layout.modify_othermeds);
        rowID = -1;
        otherMedication = null;
        startDate = new Date();
        
        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
		
    	Bundle extras = getIntent().getExtras();
    	if(null != extras)
    	{
    		rowID = extras.getLong("OtherMedsIndex");
    		medUri = Uri.withAppendedPath(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, String.valueOf(rowID));
    		Cursor medCursor = getContentResolver().query(medUri, null, null, null, null);
    		if(0 < medCursor.getCount() && medCursor.moveToFirst())
    		{
    			otherMedication = new OtherMedication();
    			otherMedication.setMedication(medCursor);
    		}
//    		Log.d(TAG,"onCreate getting bundle extra with value rowID = "+rowID);
//    		results = databaseAdapter.getResults(rowID);
    		medCursor.close();
    		
//    		Log.d(TAG,"onCreate - we found position index "+rowID);
    	}
    	
		
		
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.EditOtherMeds);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
		
        ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
		delete.setOnClickListener(this);
		
		EditText name = (EditText)findViewById(R.id.modifyOtherMedsEditMedName);
		EditText dose = (EditText)findViewById(R.id.modifyOtherMedsEditDose);

		if( null != otherMedication )
		{
			name.setText(new String(otherMedication.getName()));
			dose.setText(new String(Integer.toString(otherMedication.getDose())));	
			long time = otherMedication.getTime();
			startDate = new Date(time);
			dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
		}
		
	}
	
	protected Dialog onCreateDialog(int id) 
	{
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  year, month, day);
		dialog.setCancelable(false);
		
		return dialog;
	}  
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
//		Log.d(TAG,"onDateSet");
		startDate = new Date(year - 1900, monthOfYear, dayOfMonth);
		dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
	}	

	
	/**
	 * 
	 * @param view
	 */
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
    	case R.id.setDateTimeRow:
    		showDialog(DATE_DIALOG_ID);
    		break;    	
		case R.id.BackButton:
			setResult(RESULT_CANCELED, null);
			finish();//do nothing				
			break;
		case R.id.SaveButton:
			if( 0 <= rowID && null != otherMedication )
			{
	    		String name = (((EditText)findViewById(R.id.modifyOtherMedsEditMedName)).getText()).toString().trim();
	    		otherMedication.setName(name);
	    		String dose = (((EditText)findViewById(R.id.modifyOtherMedsEditDose)).getText()).toString().trim();
	    		otherMedication.setDose(Integer.parseInt(dose));
	    		otherMedication.setTime(startDate.getTime());
	    		getContentResolver().update(medUri, otherMedication.contentValuesForMedication(), null, null);
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
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	       		if(-1 != rowID )
    	       		{   	
    	       			getContentResolver().delete(medUri, null, null);
    	        		setResult(RESULT_OK, null);
    	    		}
    	       		ModifyOtherMedsActivity.this.finish();
    	           }
    	       })
    	       .setNegativeButton(no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	                dialog.cancel();
    	           }
    	       });
    		AlertDialog alert = builder.create();    	
    		alert.show();
			break;
		}		
	}
	
}

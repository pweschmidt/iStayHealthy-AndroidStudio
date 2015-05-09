package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.*;
import android.widget.*;
import android.net.Uri;
import android.database.Cursor;

import com.pweschmidt.healthapps.datamodel.Procedures;

public class ModifyProceduresActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener 
{
	private Date procedureDate;
	private static final int DATE_DIALOG_ID = 0;
	private TextView dateText;
	private TableRow dateRow;
    private long rowId;
    private Procedures procedures;
    private Uri procUri;

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyprocedures);
        rowId = -1;
    	Bundle extras = getIntent().getExtras();
    	
        procedureDate = new Date();
        
        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", procedureDate));

        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.ModifyProcedure);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
    	
        ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	
    	EditText changeIllness = (EditText)findViewById(R.id.ChangeIllness);
    	EditText changeProcedure = (EditText)findViewById(R.id.ChangeProcedure);
    	
    	if(null != extras)
    	{
    		rowId = extras.getLong("ProceduresIndex");
    		procUri = Uri.withAppendedPath(iStayHealthyContentProvider.PROCEDURES_CONTENT_URI, String.valueOf(rowId));
    		Cursor procCursor = getContentResolver().query(procUri, null, null, null, null);
    		if(procCursor.getCount() != 0 && procCursor.moveToFirst())
    		{
    			procedures = new Procedures();
    			procedures.setProcedures(procCursor);
        		changeIllness.setText(procedures.getIllness());
        		changeProcedure.setText(procedures.getName());
        		long time = procedures.getTime();
        		procedureDate = new Date(time);
            	dateText.setText(DateFormat.format("dd MMM yyyy", procedureDate));
    		}
    		procCursor.close();
    	}
    	
    }
	/**
	 * 
	 */
	protected Dialog onCreateDialog(int id) 
	{
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(procedureDate);
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
		procedureDate = new Date(year - 1900, monthOfYear, dayOfMonth);
    	dateText.setText(DateFormat.format("dd MMM yyyy", procedureDate));
	}	
    
    /**
     * 
     */
    public void onClick(View view)
    {
    	int resId = view.getId();
    	switch(resId)
    	{
    	case R.id.setDateTimeRow:
    		showDialog(DATE_DIALOG_ID);
    		break;    	
    	case R.id.BackButton:
    		setResult(RESULT_CANCELED, null);
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
    	       		if(-1 != rowId )
    	       		{ 
    	       			getContentResolver().delete(procUri, null, null);
    	        		setResult(RESULT_OK, null);
    	    		}
    	       		ModifyProceduresActivity.this.finish();
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
    	case R.id.SaveButton:
    		if(0 <= rowId && null != procedures )
    		{
    			String illness = (((EditText)findViewById(R.id.ChangeIllness)).getText()).toString().trim();
    			String procedure = (((EditText)findViewById(R.id.ChangeProcedure)).getText()).toString().trim();
        		procedures.setIllness(illness);
        		procedures.setName(procedure);
        		procedures.setTime(procedureDate.getTime());
        		getContentResolver().update(procUri, procedures.contentValuesForProcedures(), null, null);
    		}
    		setResult(RESULT_OK, null);
    		finish();
    		break;
    	}
    }
    
    /**
     * called when activity is paused - e.g. when we move to another one
     */
    public void onPause(){
    	super.onPause();
    }
    

}

package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TableRow;
//import android.util.Log;
import com.pweschmidt.healthapps.datamodel.OtherMedication;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.text.format.DateFormat;

public class AddOtherMedsActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "AddOtherMedsActivity";
	private Date startDate;
	private TextView dateText;
	private TableRow dateRow;
	private static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");
        setContentView(R.layout.add_othermeds);
        startDate = new Date();
        
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.AddOtherMeds);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
        
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	delete.setVisibility(View.GONE);

        
        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);
//        dateRow.setFocusable(true);
        
        
        dateText = (TextView)findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
//        setDateButton.setText(DateFormat.format("dd MMM yyyy", startDate));
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
    	case R.id.BackButton:
			setResult(RESULT_CANCELED, null);
			finish();//do nothing				
    		break;
    	case R.id.SaveButton:
    		OtherMedication otherMeds = new OtherMedication();
    		otherMeds.setTime(startDate.getTime());
    		String name = (((EditText)findViewById(R.id.EditMedName)).getText()).toString().trim();
    		if(!name.equals(""))
    		{
    			otherMeds.setName(name);
    		}
    		String doseText = (((EditText)findViewById(R.id.EditDose)).getText()).toString().trim();
    		if(!doseText.equals(""))
    		{
    			otherMeds.setDose(Integer.parseInt(doseText));
    		}
    		getContentResolver().insert(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, otherMeds.contentValuesForMedication());
    		
//			Log.d(TAG,"new otherMeds table added at row "+rowIndexAdded);
			setResult(RESULT_OK, null);
			finish();
			break;
    	case R.id.setDateTimeRow:
    		showDialog(DATE_DIALOG_ID);
    		break;
    	}
    }
    
	protected Dialog onCreateDialog(int id) 
	{
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  cyear, cmonth, cday);
		dialog.setCancelable(false);
		
		return dialog;
	}    
    
	/**
	 * 
	 */
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
//		Log.d(TAG,"onDateSet");
		startDate = new Date(year - 1900, monthOfYear, dayOfMonth);
        dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
	}	

}

package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.text.format.DateFormat;
import android.app.Dialog;
import android.app.DatePickerDialog;
import com.pweschmidt.healthapps.datamodel.Procedures;

public class AddProceduresActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
	private Date procedureDate;
	private static final int DATE_DIALOG_ID = 0;
	private TextView dateText;
	private TableRow dateRow;
	
	/**
	 * 
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprocedures);
        procedureDate = new Date();
        
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.AddProcedure);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
        
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	delete.setVisibility(View.GONE);

        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", procedureDate));
    }

    
	/**
	 * 
	 */
	protected Dialog onCreateDialog(int id) {
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
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		procedureDate = new Date(year - 1900, monthOfYear, dayOfMonth);
    	dateText.setText(DateFormat.format("dd MMM yyyy", procedureDate));
	}	
	/**
	 * 
	 * @return
	 */
	private boolean containsData(){
		String illness = (((EditText)findViewById(R.id.EditIllness)).getText()).toString().trim();
		String procedure = (((EditText)findViewById(R.id.EditProcedure)).getText()).toString().trim();
		if(illness.equals("") && procedure.equals("")  )
			return false;
		return true;
	}
    
    /**
     * 
     */
    public void onClick(View view){
    	int resId = view.getId();
    	switch(resId){
    	case R.id.BackButton:
    		setResult(RESULT_CANCELED, null);
    		finish();
    		break;
    	case R.id.SaveButton:
    		if(containsData()){
	    		Procedures procs = new Procedures();
	    		procs.setTime(procedureDate.getTime());
	    		String illness = (((EditText)findViewById(R.id.EditIllness)).getText()).toString().trim();
	    		String procedure = (((EditText)findViewById(R.id.EditProcedure)).getText()).toString().trim();
	    		procs.setIllness(illness);
	    		procs.setName(procedure);
	    		getContentResolver().insert(iStayHealthyContentProvider.PROCEDURES_CONTENT_URI, procs.contentValuesForProcedures());
    		}
    		setResult(RESULT_OK, null);
    		finish();
    		break;
    	case R.id.setDateTimeRow:
    		showDialog(DATE_DIALOG_ID);
    		break;
    	}
    }
    
    
}

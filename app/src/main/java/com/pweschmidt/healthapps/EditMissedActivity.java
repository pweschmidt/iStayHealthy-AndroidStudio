package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
//import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContentValues;

import com.pweschmidt.healthapps.datamodel.MissedMedication;
import android.widget.*;
import android.database.Cursor;
import android.content.Context;
import android.view.LayoutInflater;

public class EditMissedActivity extends Activity  
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
	private Date missedDate;
	private TableRow missedDateRow;
	private TextView missedDateTextView;
//	private static final int BANNER_ACTIVITY_REQUEST_CODE = 100;
	private static final int DATE_DIALOG_ID = 0;
    private long rowID;
	private Uri missedUri;
	private boolean isInEditMode;
	private MissedMedication missed;
    private ListView missedReasonListView;
    private String selectedReason;
	private String selectedName;
	private String selectedDrug;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_missed);
        rowID = -1;
        isInEditMode = false;
        selectedReason = null;
		missed = new MissedMedication();
    	Bundle extras = getIntent().getExtras();
    	
    	ImageButton save = (ImageButton)findViewById(R.id.SaveButton);
    	save.setOnClickListener(this);
    	
    	ImageButton banner = (ImageButton)findViewById(R.id.CancelButton);
    	banner.setOnClickListener(this);
    	
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	
    	missedDateRow = (TableRow)findViewById(R.id.dateHeaderTableRow);
    	missedDateRow.setOnClickListener(this);
        
    	
        String reasonsStrings[] = getResources().getStringArray(R.array.AllMissedReason);

        missedReasonListView = (ListView)findViewById(R.id.missedReasonListView);        
        missedReasonListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ReasonsAdapter adapter = new ReasonsAdapter(this, R.layout.missedreason_item, reasonsStrings);
    	int missedReasonIndex = -1;
    	if(null != extras)
    	{
    		isInEditMode = extras.getBoolean("isInEditMode");
    		if(isInEditMode)
    		{
    			delete.setVisibility(View.VISIBLE);
        		rowID = extras.getLong("MissedIndex");    	
        		missedUri = Uri.withAppendedPath(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, String.valueOf(rowID));
        		Cursor cursor = getContentResolver().query(missedUri, null, null, null, null);
        		if(0 < cursor.getCount() && cursor.moveToFirst())
        		{
        			missed.setMedication(cursor);
        		}
        		cursor.close();
        		selectedReason = missed.getMissedReason();
        		for(int i = 0 ; i < reasonsStrings.length ; i++)
        		{
        			if(selectedReason.startsWith(reasonsStrings[i]))
        			{
        				missedReasonIndex = i;
        				break;
        			}
        		}
        		selectedName = missed.getName();
        		selectedDrug = missed.getDrug();
        		long missedTime = missed.getTime();
        		missedDate = new Date(missedTime);
    		}
    		else
    		{
    			delete.setVisibility(View.GONE);
    			selectedName = extras.getString("Name");
    			selectedDrug = extras.getString("Drug");
    			missedDate = new Date();
    		}
    		    		
    	}
    	adapter.setMissedReasonIndex(missedReasonIndex);
    	missedReasonListView.setAdapter(adapter);
    	missedDateTextView = (TextView)findViewById(R.id.selectedDateView);    	
    	missedDateTextView.setText(DateFormat.format("dd MMM yyyy", missedDate));
    	
	}
	
	protected Dialog onCreateDialog(int id) {
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(missedDate);		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  year, month, day);
		dialog.setCancelable(false);
		return dialog;
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		missedDate = new Date(year - 1900, monthOfYear, dayOfMonth);
		missedDateTextView.setText(DateFormat.format("dd MMM yyyy", missedDate));
	}	
		
	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.dateHeaderTableRow:
    		showDialog(DATE_DIALOG_ID);			
			break;
		case R.id.SaveButton:
			if(null != selectedReason)
				missed.setMissedReason(selectedReason);
			missed.setTime(missedDate.getTime());
			if(null != selectedName)
				missed.setName(selectedName);
			if(null != selectedDrug)
				missed.setDrug(selectedDrug);
			ContentValues content = missed.contentValuesForMedication();
			if(isInEditMode)
			{
				if(rowID != -1)
				{
		    		getContentResolver().update(missedUri, content, null, null);					
				}				
			}
			else
			{
				getContentResolver().insert(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, content);				
			}			
    		setResult(RESULT_OK, null);
    		finish();            	
			break;
		case R.id.CancelButton:
    		setResult(RESULT_CANCELED, null);
    		finish();            	
			break;
		case R.id.TrashButton:
    		AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    		String yes = getResources().getString(R.string.Yes);
    		String no = getResources().getString(R.string.No);
    		String message = getResources().getString(R.string.DeleteMissedMeds);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   if(rowID != -1 && isInEditMode){    	        		   
       	       				getContentResolver().delete(missedUri, null, null);
        	        		setResult(RESULT_OK, null);
    	        	   }
    	        	   EditMissedActivity.this.finish();
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

	private class ReasonsAdapter extends ArrayAdapter<String>
	{
	    Context context; 
	    int layoutResourceId;    
	    String data[] = null;
	    boolean[] boxesChecked = null;
	    
	    private class ViewHolder
	    {
	    	TextView reasonTextView;
	    	CheckBox reasonCheckBox;
	    }

	    public ReasonsAdapter(Context context, int layoutResourceId, String[] data) 
	    {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	        boxesChecked = new boolean[data.length];
	        for(int i = 0 ; i < boxesChecked.length ; i++)
	        {
	        	boxesChecked[i] = false;
	        }
	    }
	    
	    public void setMissedReasonIndex(int index)
	    {
	    	if(0 <= index && index < boxesChecked.length)
	    	{
	    		boxesChecked[index] = true;
	    	}
	    }
	    
	    
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	    	View row = convertView;
	    	final ViewHolder holder;
	    	if(null == row)
	    	{
	    		holder = new ViewHolder();
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            TextView reasonText = (TextView)row.findViewById(R.id.missedReasonView);
	            CheckBox box = (CheckBox)row.findViewById(R.id.missedCheckButton);
		    	holder.reasonTextView = reasonText;
		    	holder.reasonCheckBox = box;	    		
	    	}
	    	else
	    	{
	    		holder = (ViewHolder)row.getTag();
	    	}
	    	
	    	row.setTag(holder);
	    	holder.reasonTextView.setText(data[position]);
	    	holder.reasonCheckBox.setId(position);
            holder.reasonCheckBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					for(int i = 0 ; i < data.length ; i++)
					{
						if(v.getId() == i)
						{
							boxesChecked[i] = true;
						}
						else
						{
							boxesChecked[i] = false;
						}
					}
					notifyDataSetChanged();
				}
			});
            
            if(boxesChecked[position])
            {
            	holder.reasonCheckBox.setChecked(true);
            	selectedReason = (String) holder.reasonTextView.getText();
            }
            else
            	holder.reasonCheckBox.setChecked(false);
            
            
	    	return row;
	    }
	 		
	}
	
}

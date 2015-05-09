package com.pweschmidt.healthapps;

import java.util.Calendar;
import java.util.Date;

import com.pweschmidt.healthapps.datamodel.SideEffects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RadioButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
//import android.util.Log;

public class EditEffectsActivity extends Activity 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "EditEffectsActivity";
	public static final int LINK_ACTIVITY_REQUEST_CODE = 100;
	public static final int EFFECTS_ACTIVITY_REQUEST_CODE = 199;
	private Date effectsDate;
	private TableRow effectsDateRow;
	private TextView effectsDateTextView;
//	private static final int BANNER_ACTIVITY_REQUEST_CODE = 100;
	private static final int DATE_DIALOG_ID = 0;
    private long rowID;
	private Uri effectsUri;
	private boolean isInEditMode;
	private String selectedDrug;
	private String selectedName;
	private String selectedEffect;
    private SideEffects sideEffects;
    private int selectedSeriousness;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_effects);
		selectedEffect = null;
        rowID = -1;
        isInEditMode = false;
        selectedSeriousness = SideEffects.MINOR;
		sideEffects = new SideEffects();
    	Bundle extras = getIntent().getExtras();
    	
    	ImageButton save = (ImageButton)findViewById(R.id.SaveButton);
    	save.setOnClickListener(this);
    	
    	ImageButton back = (ImageButton)findViewById(R.id.CancelButton);
    	back.setOnClickListener(this);
    	
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);

    	Button selector = (Button)findViewById(R.id.effectsSelector);
    	selector.setOnClickListener(this);
    		
    	effectsDateRow = (TableRow)findViewById(R.id.dateHeaderTableRow);
    	effectsDateRow.setOnClickListener(this);
    	
    	TableRow infoRow = (TableRow)findViewById(R.id.glossaryRow);
    	infoRow.setOnClickListener(this);
    	
    	
    	if(null != extras)
    	{
    		isInEditMode = extras.getBoolean("isInEditMode");
    		if(isInEditMode)
    		{
//    			Log.d(TAG,"is In edit mode and the delete button should be visible");
    			delete.setVisibility(View.VISIBLE);
        		rowID = extras.getLong("EffectsIndex");    	
        		effectsUri = Uri.withAppendedPath(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, String.valueOf(rowID));
        		Cursor cursor = getContentResolver().query(effectsUri, null, null, null, null);
        		if(0 < cursor.getCount() && cursor.moveToFirst())
        		{
        			sideEffects.setMedication(cursor);
        		}
        		cursor.close();
        		selectedEffect = sideEffects.getSideEffects();
        		selectedSeriousness = sideEffects.getSeriousness();
        		selectedDrug = sideEffects.getDrug();
        		selectedName = sideEffects.getName();
        		switch(selectedSeriousness)
        		{
        		case SideEffects.SERIOUS:
        			RadioButton serious = (RadioButton)findViewById(R.id.seriousChoice);
        			serious.setChecked(true);
        			break;
        		case SideEffects.MAJOR:
        			RadioButton major = (RadioButton)findViewById(R.id.majorChoice);
        			major.setChecked(true);
        			break;
        		case SideEffects.MINOR:
        	    	RadioButton minor = (RadioButton)findViewById(R.id.minorChoice);
        	    	minor.setChecked(true);
        			break;
        		}
        		long time = sideEffects.getTime();
        		effectsDate = new Date(time);
    		}
    		else
    		{
//    			Log.d(TAG,"is NOT in edit mode and the delete button should NOT be visible");
    			delete.setVisibility(View.GONE);
    			selectedDrug = extras.getString("Drug");
    			selectedName = extras.getString("Name");
    			effectsDate = new Date();
    	    	RadioButton minor = (RadioButton)findViewById(R.id.minorChoice);
    	    	minor.setChecked(true);
    		}
    		    		
    	}
    	effectsDateTextView = (TextView)findViewById(R.id.selectedDateView);    	
    	effectsDateTextView.setText(DateFormat.format("dd MMM yyyy", effectsDate));
	}
	
	
	
	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.glossaryRow:
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.medslist);
            bannerIntent.putExtra("url", url);    		
			startActivityForResult(bannerIntent,LINK_ACTIVITY_REQUEST_CODE);			
			break;
		case R.id.effectsSelector:
			Intent selectorIntent = new Intent(this, SelectEffects.class);
			startActivityForResult(selectorIntent, EFFECTS_ACTIVITY_REQUEST_CODE);
			break;
		case R.id.dateHeaderTableRow:
    		showDialog(DATE_DIALOG_ID);			
			break;
		case R.id.SaveButton:
	    	EditText effectsEditor = (EditText)findViewById(R.id.enterSideEffect);
	    	selectedEffect = effectsEditor.getText().toString().trim();
			if(null != selectedEffect)
				sideEffects.setSideEffects(selectedEffect);
			sideEffects.setTime(effectsDate.getTime());
			sideEffects.setSeriousness(selectedSeriousness);
			if(null != selectedDrug)
				sideEffects.setDrug(selectedDrug);
			if(null != selectedName)
				sideEffects.setName(selectedName);
			ContentValues content = sideEffects.contentValuesForMedication();
			if(isInEditMode)
			{
				if(rowID != -1)
				{
		    		getContentResolver().update(effectsUri, content, null, null);					
				}				
			}
			else
			{
				getContentResolver().insert(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, content);				
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
    		String message = getResources().getString(R.string.DeleteSideEffects);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   if(rowID != -1 && isInEditMode){    	        		   
       	       				getContentResolver().delete(effectsUri, null, null);
        	        		setResult(RESULT_OK, null);
    	        	   }
    	        	   EditEffectsActivity.this.finish();
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
	
	
	public void onRadioButtonClicked(View view)
	{
		boolean isChecked = ((RadioButton)view).isChecked();
		int resID = view.getId();
		switch(resID)
		{
		case R.id.seriousChoice:
			if(isChecked)
				selectedSeriousness = SideEffects.SERIOUS;
			break;
		case R.id.majorChoice:
			if(isChecked)
				selectedSeriousness = SideEffects.MAJOR;
			break;
		case R.id.minorChoice:
			if(isChecked)
				selectedSeriousness = SideEffects.MINOR;
			break;
		}
	}
	
	
	protected Dialog onCreateDialog(int id) {
		if(DATE_DIALOG_ID != id)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(effectsDate);		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(this,  this,  year, month, day);
		dialog.setCancelable(false);
		return dialog;
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		effectsDate = new Date(year - 1900, monthOfYear, dayOfMonth);
		effectsDateTextView.setText(DateFormat.format("dd MMM yyyy", effectsDate));
	}	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(EFFECTS_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
		{
			if(null != data)
			{
				if(data.hasExtra("selectedEffect"))
				{
			    	EditText effectsEditor = (EditText)findViewById(R.id.enterSideEffect);
					selectedEffect = data.getExtras().getString("selectedEffect");
					effectsEditor.setText(selectedEffect);
				}
			}
		}
	}
	

}

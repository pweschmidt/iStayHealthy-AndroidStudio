package com.pweschmidt.healthapps;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.pweschmidt.healthapps.datamodel.Medication;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

//import android.util.Log;

//import android.content.DialogInterface;

public class AddHIVDrugsActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "AddResultActivity";
	public static final int LINK_ACTIVITY_REQUEST_CODE = 100;
	private TableLayout medTable;
	private StringMap medHashMap;
	private Vector<String> selectedList;
	private Date startDate;
	private TextView dateText;
	private TableRow dateRow;
	private static final int DATE_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");
        setContentView(R.layout.add_hiv_drugs);
        medTable = (TableLayout)findViewById(R.id.medTable);
        medHashMap = new StringMap();
        selectedList = new Vector<String>();
        startDate = new Date();
      
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.AddHIVDrugs);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
        
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	delete.setVisibility(View.GONE);
    	
    	TableRow infoRow = (TableRow)findViewById(R.id.glossaryRow);
    	infoRow.setOnClickListener(this);

        dateRow = (TableRow)findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);

        dateText = (TextView)findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
        
        
        String[] combiList = getResources().getStringArray(R.array.CombiMeds);
        String combiTitle = getResources().getString(R.string.combinationtablets);
        Vector<String> combiMeds = new Vector<String>(Arrays.asList(combiList));
        setUpTitle(combiTitle);
        fillMedicationRows(combiMeds, FileMaps.combiMeds);
        
        String[] nnRTIList = getResources().getStringArray(R.array.NNRTI);
        String NNRTITitle = getResources().getString(R.string.NNRTIDrugs);
        Vector<String> nnRTIMeds = new Vector<String>(Arrays.asList(nnRTIList));
        setUpTitle(NNRTITitle);
        fillMedicationRows(nnRTIMeds, FileMaps.nnRTI);
        
        String[] nRTIList = getResources().getStringArray(R.array.NRTI);
        String NRTITitle = getResources().getString(R.string.NRTIDrugs);
        Vector<String> nRTIMeds = new Vector<String>(Arrays.asList(nRTIList));
        setUpTitle(NRTITitle);
        fillMedicationRows(nRTIMeds, FileMaps.nRTI);
        
        String[] piList = getResources().getStringArray(R.array.ProteaseInhibitors);
        String PITitle = getResources().getString(R.string.PIDrugs);
        Vector<String> pIMeds = new Vector<String>(Arrays.asList(piList));
        setUpTitle(PITitle);
        fillMedicationRows(pIMeds, FileMaps.proteaseInhibitors);
        
        String[] entryList = getResources().getStringArray(R.array.EntryInhibitors);
        String entryTitle = getResources().getString(R.string.EntryDrugs);
        Vector<String> entryMeds = new Vector<String>(Arrays.asList(entryList));
        setUpTitle(entryTitle);
        fillMedicationRows(entryMeds, FileMaps.entryInhibitors);
        
        String[] integraseList = getResources().getStringArray(R.array.IntegraseInhibitors);
        String integraseTitle = getResources().getString(R.string.IntegraseDrugs);
        Vector<String> integraseMeds = new Vector<String>(Arrays.asList(integraseList));
        setUpTitle(integraseTitle);
        fillMedicationRows(integraseMeds, FileMaps.integraseInhibitors);
        
    	String[] boosterList = getResources().getStringArray(R.array.Boosters);
        String boosterTitle = getResources().getString(R.string.Booster);
        Vector<String> boosterMeds = new Vector<String>(Arrays.asList(boosterList));
        setUpTitle(boosterTitle);
        fillMedicationRows(boosterMeds, FileMaps.boosters);

        String[] otherList = getResources().getStringArray(R.array.OtherInhibitors);
        String otherTitle = getResources().getString(R.string.OtherInhibitors);
        Vector<String> otherMeds = new Vector<String>(Arrays.asList(otherList));
        setUpTitle(otherTitle);
        fillMedicationRows(otherMeds, FileMaps.otherInhibitors);
	}

	/**
	 * 
	 */
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
		startDate = new Date(year - 1900, monthOfYear, dayOfMonth);
        dateText.setText(DateFormat.format("dd MMM yyyy", startDate));
	}	
	

	/**
	 * 
	 */
	public void onClick(View view)
	{
		int resID = view.getId();
		if(R.id.BackButton == resID)
		{
			setResult(RESULT_CANCELED, null);
			finish();//do nothing							
		}
		else if(R.id.SaveButton == resID)
		{
			Iterator <String> iterator = selectedList.iterator();
			while(iterator.hasNext())
			{
				String medString = (String)iterator.next();
				String[] medStrings = medString.split(":");
				Medication medication = new Medication();
				medication.setName(medStrings[1]);
				medication.setDrug(medStrings[2]);
				medication.setMedicationForm(medStrings[3]);
				medication.setTime(startDate.getTime());
	    		getContentResolver().insert(iStayHealthyContentProvider.MEDS_CONTENT_URI, medication.contentValuesForMedication());
//    			Log.d(TAG,"new medication table added at row "+rowIndexAdded);
			}			
			setResult(RESULT_OK, null);
			finish();
		}
		else if(R.id.glossaryRow == resID)
		{
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.medslist);
            bannerIntent.putExtra("url", url);    		
			startActivityForResult(bannerIntent,LINK_ACTIVITY_REQUEST_CODE);			
		}
		else if(R.id.setDateTimeRow == resID)
		{
			showDialog(DATE_DIALOG_ID);			
		}
		else
		{
			CheckBox checkBox = (CheckBox)view;
			String key = medHashMap.getStringForCheckBox(checkBox);
			if( null != key )
			{
				if(checkBox.isChecked())
				{
					selectedList.add(key);
				}
				else
				{
					if(selectedList.contains(key))
						selectedList.remove(key);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param title
	 */
	private void setUpTitle(String title)
	{
        TableRow combiRowTitle = new TableRow(this);
        combiRowTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        TextView combiTitleView = new TextView(this);
    	
    	combiTitleView.setText(title);
    	combiTitleView.setTextColor(Color.DKGRAY);
    	combiTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
    	combiTitleView.setTypeface(Typeface.SERIF, Typeface.BOLD);
    	combiTitleView.setGravity(Gravity.CENTER);
    	combiTitleView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	LayoutParams params = new LayoutParams();
    	params.span = 3;
    	combiRowTitle.addView(combiTitleView,params);
    	medTable.addView(combiRowTitle, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));	
	}
	
	
	private void fillMedicationRows(Vector <String> medVector, HashMap<String, Integer>imageMap)
	{
		Iterator <String>iterator = medVector.iterator();
    	while(iterator.hasNext())
    	{
    		String key = (String)iterator.next();
    		String[] strings = key.split(":");
    		String imageName = strings[0];
    		int resId = R.drawable.combi;
    		if(imageMap.containsKey(imageName))
    			resId = imageMap.get(imageName).intValue();
    		String commercialName = strings[1];
    		String drugs = strings[2];
    		String form = strings[3];
    		TableRow tableRow = new TableRow(this);
    		tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    		if(0 != resId)
    		{
	    		ImageView imageView = new ImageView(this);
	    		imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    		imageView.setImageResource(resId);
	    		imageView.setMaxHeight(55);
	    		imageView.setMaxWidth(55);
	    		tableRow.addView(imageView);
    		}    		
    		TableLayout textLayout = new TableLayout(this);
    		textLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		
    		TableRow commercialRow = new TableRow(this);
    		commercialRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    		TextView commercialLabel = new TextView(this);
    		commercialLabel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		commercialLabel.setText(commercialName);
    		commercialLabel.setTextColor(Color.DKGRAY);
    		commercialLabel.setTextSize(12);
    		commercialRow.addView(commercialLabel);
    		
    		TableRow drugRow = new TableRow(this);
    		drugRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    		TextView drugLabel = new TextView(this);
    		drugLabel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		drugLabel.setText(drugs);
    		drugLabel.setTextColor(Color.LTGRAY);
    		drugLabel.setTextSize(10);
    		drugRow.addView(drugLabel);

    		TableRow formRow = new TableRow(this);
    		formRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));    		

    		TextView formLabel = new TextView(this);
    		formLabel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		formLabel.setText(form);
    		formLabel.setTextColor(Color.RED);
    		formLabel.setTextSize(8);
    		formRow.addView(formLabel);
    		
    		textLayout.addView(commercialRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		textLayout.addView(drugRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		textLayout.addView(formRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		tableRow.addView(textLayout);
    		
    		CheckBox checkBox = new CheckBox(this);
    		checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    		checkBox.setChecked(false);
    		checkBox.setOnClickListener(this);
    		tableRow.addView(checkBox);
    		medHashMap.put(key, checkBox);
        	medTable.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	}
	}
	

	
    /**
     * entry point when returning to ResultsActivity
     */
    public void onResume(){
    	super.onResume();
//    	Log.d(TAG,"onResume");
    }

    /**
     * called when activity is paused - e.g. when we move to another one
     */
    public void onPause(){
    	super.onPause();
//    	Log.d(TAG,"onPause");
    }
}

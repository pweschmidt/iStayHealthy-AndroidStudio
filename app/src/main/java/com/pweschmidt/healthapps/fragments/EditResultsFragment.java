package com.pweschmidt.healthapps.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.datamodel.Results;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;

import java.util.Calendar;
import java.util.Date;

//import android.support.v4.app.FragmentTransaction;
//import android.content.Intent;
//import android.util.*;

public class EditResultsFragment extends Fragment 
	implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{
//	private static final String TAG = "EditResultsFragment";
	private Date resultsDate;
	private EditText cd4Text;
	private EditText cd4PercentText;
	private EditText vlText;
	private EditText hepCText;
	private TextView dateText;
	private EditText sugarText;
	private EditText hdlText;
	private EditText ldlText;
	private EditText cholesterolText;
	private EditText heartRateText;
	private EditText systoleText;
	private EditText diastoleText;
	private EditText weightText;
	private EditText riskText;
	private TableRow dateRow;
	private boolean isUndetectable;
	private boolean isHepCUndetectable;
	private TableRow cd4Row;
	private TableRow cd4PercentRow;
	private TableRow vlRow;
	private TableRow vlUndetectableRow;
	private TableRow sugarRow ;
	private TableRow hdlRow ;
	private TableRow ldlRow ;
	private TableRow pressureRow ;
	private TableRow heartRateRow ;
	private TableRow hepCRow ;
	private TableRow hepCCheckRow ;
	private TableRow cholesterolRow;
	private TableRow riskRow;
	private TableRow weightRow;
	private CheckBox checkBox;
	private CheckBox hepCCheckBox;
	private Uri resultUri;
    private long rowID;
    private boolean isInEditMode;
    private Results results;
    private TextView titleText;
    private ImageButton delete;
	private static final int HIVVIEW = 0x0;
	private static final int BLOODSVIEW = 0x1;
	private static final int OTHERVIEW = 0x2;
	
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);	
	}
	
	public void onActivityCreated(Bundle savedInstance)
	{
		super.onActivityCreated(savedInstance);
        resultsDate = new Date();
        rowID = -1;
        isInEditMode = false;
        results = null;
    	isUndetectable = false;
    	isHepCUndetectable = true;
    	Bundle extras = getActivity().getIntent().getExtras();
    	if(null != extras)
    	{
    		rowID = extras.getLong("ResultsIndex");
    		if(rowID >= 0)
    		{
        		resultUri = Uri.withAppendedPath(iStayHealthyContentProvider.RESULTS_CONTENT_URI, String.valueOf(rowID));
        		Cursor resultsCursor = getActivity().getContentResolver().query(resultUri, null, null, null, null);
        		if(0 < resultsCursor.getCount() && resultsCursor.moveToFirst())
        		{
        			results = new Results();
        			results.setResult(resultsCursor);
        		}
        		resultsCursor.close();    			
        		if(null != results)
        		{
        			isInEditMode = true;
        		}
    		}
    	}
    	View view = getView();
    	setUpRowsAndTextFields(view);
    	String title = getResources().getString(R.string.AddResults);
		Uri data = getActivity().getIntent().getData();

		if (null != data)
		{
			title = getResources().getString(R.string.ImportResults);
			results = new Results(data);
			prefillTextFields();
		}
    	if(isInEditMode)
    	{
    		
        	title = getResources().getString(R.string.EditResults);
    		prefillTextFields();
        	delete.setVisibility(View.VISIBLE);    		
    	}
    	else
    	{
        	delete.setVisibility(View.GONE);    		
    	}
    	titleText.setText(title);
    	setVisibility(HIVVIEW, true);
    	setVisibility(BLOODSVIEW, false);
    	setVisibility(OTHERVIEW, false);    		
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.editresult_fragment, container, false);
		
        ImageButton cancelButton = (ImageButton)view.findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
        
        ImageButton saveButton = (ImageButton)view.findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
        
    	delete = (ImageButton)view.findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	
    	RadioButton hivButton = (RadioButton)view.findViewById(R.id.HIVButton);
    	hivButton.setOnClickListener(this);
    	hivButton.setChecked(true);
    	
    	RadioButton bloodButton = (RadioButton)view.findViewById(R.id.BloodButton);
    	bloodButton.setOnClickListener(this);
    	
    	RadioButton otherButton = (RadioButton)view.findViewById(R.id.OtherButton);
    	otherButton.setOnClickListener(this);
    	
    	checkBox = (CheckBox)view.findViewById(R.id.isUndetectableCheckButton);
    	checkBox.setOnClickListener(this);
    	
    	hepCCheckBox = (CheckBox)view.findViewById(R.id.isHepCUndetectableCheckButton);
    	hepCCheckBox.setOnClickListener(this);


    	return view;
    }	
    
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
	{
		resultsDate = new Date(year - 1900, monthOfYear, dayOfMonth);
    	dateText.setText(DateFormat.format("dd MMM yyyy", resultsDate));
	}	
    
    
    public void onClick(View view)
    {
    	int resID = view.getId();
		FragmentActivity activity = getActivity();
    	switch(resID){
    	case R.id.BackButton:
    		activity.setResult(FragmentActivity.RESULT_CANCELED, null);
    		activity.finish();//do nothing
    		break;
    	case R.id.setDateTimeRow:
//    		Log.d(TAG,"date time row selected");
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(resultsDate);
    		
    		int year = calendar.get(Calendar.YEAR);
    		int month = calendar.get(Calendar.MONTH);
    		int day = calendar.get(Calendar.DAY_OF_MONTH);
    		DatePickerDialog dialog = new DatePickerDialog(getActivity(),  this,  year, month, day);
    		dialog.setCancelable(false);
    		dialog.show();
    		break;
    	case R.id.SaveButton:
    		if(!containsData())
    		{
//    			Log.d(TAG,"Saving data - but we have NO entries/changes. Therefore cancel");
    			activity.setResult(FragmentActivity.RESULT_CANCELED, null);
    			activity.finish();
    		}
    		else{
    			if(!isInEditMode)
    				results = new Results();  
	    		String test = cd4Text.getText().toString().trim();
	    		if(!test.equals(""))
	    			results.setCD4Count(Integer.parseInt(test));
	
	    		test = cd4PercentText.getText().toString().trim();
	    		if(!test.equals(""))
	    			results.setCD4Percent(Float.parseFloat(test));
	    		test = vlText.getText().toString().trim();
	    		if(!test.equals("")){   
	    			String undetectable = getResources().getString(R.string.undetectable);

	    			if(test.equalsIgnoreCase(undetectable) || undetectable.contains(test)){
	    				results.setViralLoad(0);
	    			}
	    			else
	    				results.setViralLoad(Integer.parseInt(test));
	    		}
	    		else
	    		{
	    			if(!checkBox.isChecked())
	    			{
	    				results.setViralLoad(-1);
	    			}
	    		}
	    		results.setTime(resultsDate.getTime());

	    		String sugar = sugarText.getText().toString().trim();
	    		if(!sugar.equals(""))
	    			results.setGlucose(Float.parseFloat(sugar));
	    		String hdl = hdlText.getText().toString().trim();
	    		if(!hdl.equals(""))
	    			results.setHDL(Float.parseFloat(hdl));
	    		String ldl = ldlText.getText().toString().trim();
	    		if(!ldl.equals(""))
	    			results.setLDL(Float.parseFloat(ldl));
	    		String systole = systoleText.getText().toString().trim();
	    		if(!systole.equals("") && !systole.equals("0"))
	    			results.setSystole(Integer.parseInt(systole));
	    		String diastole = diastoleText.getText().toString().trim();
	    		if(!diastole.equals("") && !diastole.equals("0"))
	    			results.setDiastole(Integer.parseInt(diastole));
	    		String rate = heartRateText.getText().toString().trim();
	    		if(!rate.equals(""))
	    			results.setHeartRate(Integer.parseInt(rate));
	    		String weight = weightText.getText().toString().trim();
	    		if(!weight.equals(""))
	    			results.setWeight(Float.parseFloat(weight));
	    		String cholesterol = cholesterolText.getText().toString().trim();
	    		if(!cholesterol.equals(""))
	    			results.setTotalCholesterol(Float.parseFloat(cholesterol));
	    		
	    		String risk = riskText.getText().toString().trim();
	    		if(!risk.equals(""))
	    			results.setCardiacRiskFactor(Float.parseFloat(risk));
	    		
	    		String hepc = hepCText.getText().toString().trim();
	    		if(!hepc.equals("")){
	    			String undetectable = getResources().getString(R.string.undetectable);
	    			if(hepc.equalsIgnoreCase(undetectable) || undetectable.contains(test)){
	    				results.setHepCViralLoad(0);
	    			}
	    			else
	    				results.setHepCViralLoad(Integer.parseInt(hepc));	    			
	    		}
	    		else
	    		{
	    			if(!hepCCheckBox.isChecked())
	    			{
	    				results.setHepCViralLoad(-1);	    				    				
	    			}
	    		}
	    		if(isInEditMode)
	    		{
//	    			Log.d(TAG,"Saving data - updating data for row "+rowID);
		    		activity.getContentResolver().update(resultUri, results.contentValuesForResult(), null, null);
	    		}
	    		else
	    		{
		    		activity.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, results.contentValuesForResult());	    			    			    			
	    		}
//    			Log.d(TAG,"Saving data - set activity result to RESULT_OK, which is "+FragmentActivity.RESULT_OK);
	    		activity.setResult(FragmentActivity.RESULT_OK, null);
	    		activity.finish();
    		}
    		break;
    	case R.id.isHepCUndetectableCheckButton:
    		CheckBox hepVLBox = (CheckBox)view;
    		if(hepVLBox.isChecked()){
				String undetectable = getResources().getString(R.string.undetectable);
				hepCText.setText(undetectable);
				hepCText.setEnabled(false);    			
    		}
    		else
    		{
				String undetectable = getResources().getString(R.string.undetectable);
				String text = hepCText.getText().toString().trim();
				if(text.startsWith(undetectable))
					hepCText.setText("");
    			hepCText.setEnabled(true);    			
    		}
    		break;
    	case R.id.isUndetectableCheckButton:
    		CheckBox vlBox = (CheckBox)view;
    		if(vlBox.isChecked()){
				String undetectable = getResources().getString(R.string.undetectable);
				vlText.setText(undetectable);
				vlText.setEnabled(false);    			
    		}
    		else
    		{
				String undetectable = getResources().getString(R.string.undetectable);
				String text = vlText.getText().toString().trim();
				if(text.startsWith(undetectable))
					vlText.setText("");
    			vlText.setEnabled(true);    			
    		}
    		break; 
    	case R.id.TrashButton:
    		AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
    		String yes = getResources().getString(R.string.Yes);
    		String no = getResources().getString(R.string.No);
    		String message = getResources().getString(R.string.deleteentry);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	       		if(-1 != rowID)
    	       		{  
//    	       			Log.d(TAG,"***** deleting the entry and sending back RESULT_OK");
    	       			getActivity().getContentResolver().delete(resultUri, null, null);
    	        		getActivity().setResult(FragmentActivity.RESULT_OK, null);
    	    		}
    	       		getActivity().finish();
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
    	case R.id.HIVButton:
        	setVisibility(HIVVIEW, true);
        	setVisibility(BLOODSVIEW, false);
        	setVisibility(OTHERVIEW, false);    		
    		break;
    	case R.id.BloodButton:
        	setVisibility(HIVVIEW, false);
        	setVisibility(BLOODSVIEW, true);
        	setVisibility(OTHERVIEW, false);    		
    		break;
    	case R.id.OtherButton:
        	setVisibility(HIVVIEW, false);
        	setVisibility(BLOODSVIEW, false);
        	setVisibility(OTHERVIEW, true);    		
    		break;
    	}    	
    	
    }   
    
	private boolean containsData(){
		String cd4 = cd4Text.getText().toString().trim();
		String cd4Percent = cd4PercentText.getText().toString().trim();
		String vl = vlText.getText().toString().trim();
		String sugar = sugarText.getText().toString().trim();
		String hdl = hdlText.getText().toString().trim();
		String ldl = ldlText.getText().toString().trim();
		String systole = systoleText.getText().toString().trim();
		String diastole = diastoleText.getText().toString().trim();
		String rate = heartRateText.getText().toString().trim();
		String hepc = hepCText.getText().toString().trim();
		String weight = weightText.getText().toString().trim();
		String cholesterol = cholesterolText.getText().toString().trim();
		String risk = riskText.getText().toString().trim();

		
		if(cd4.equals("") && cd4Percent.equals("") && vl.equals("") && sugar.equals("")
				&& hdl.equals("") && ldl.equals("") && systole.equals("0") && diastole.equals("0") 
				&& rate.equals("") && hepc.equals("") && risk.equals("") && cholesterol.equals("") && weight.equals(""))
			return false;
		return true;
	}
	
	private void setVisibility(int type, boolean isOn)
	{
		int visibilityIndex = (isOn) ? View.VISIBLE : View.GONE;
		switch(type)
		{
		case HIVVIEW:
			cd4Row.setVisibility(visibilityIndex);
			cd4PercentRow.setVisibility(visibilityIndex);
			vlRow.setVisibility(visibilityIndex);
			vlUndetectableRow.setVisibility(visibilityIndex);
			break;
		case BLOODSVIEW:
			sugarRow.setVisibility(visibilityIndex);
			hdlRow.setVisibility(visibilityIndex);
			ldlRow.setVisibility(visibilityIndex);
			cholesterolRow.setVisibility(visibilityIndex);
			hepCRow.setVisibility(visibilityIndex);
			hepCCheckRow.setVisibility(visibilityIndex);
			break;
		case OTHERVIEW:
			pressureRow.setVisibility(visibilityIndex);
			riskRow.setVisibility(visibilityIndex);
			weightRow.setVisibility(visibilityIndex);
			heartRateRow.setVisibility(visibilityIndex);
			break;
		}
	}
    
	private void setUpRowsAndTextFields(View view)
	{
		
        dateRow = (TableRow)view.findViewById(R.id.setDateTimeRow);
        dateRow.setOnClickListener(this);
    	cd4Row = (TableRow)view.findViewById(R.id.CD4Row);
    	cd4PercentRow = (TableRow)view.findViewById(R.id.CD4PercentRow);
    	vlRow = (TableRow)view.findViewById(R.id.ViralLoadRow);
    	vlUndetectableRow = (TableRow)view.findViewById(R.id.UndetectableVLRow);
    	sugarRow = (TableRow)view.findViewById(R.id.BloodSugarRow);    	
    	hdlRow = (TableRow)view.findViewById(R.id.HDLRow);
    	ldlRow = (TableRow)view.findViewById(R.id.LDLRow);
    	pressureRow = (TableRow)view.findViewById(R.id.BloodPressureRow);
    	heartRateRow = (TableRow)view.findViewById(R.id.HeartRateRow);
    	hepCRow = (TableRow)view.findViewById(R.id.HepCRow);
    	hepCCheckRow = (TableRow)view.findViewById(R.id.HepCCheckRow);
    	riskRow = (TableRow)view.findViewById(R.id.CardiacRiskRow);
    	cholesterolRow = (TableRow)view.findViewById(R.id.CholesterolRow);
    	weightRow = (TableRow)view.findViewById(R.id.WeightRow);
        
        dateText = (TextView)view.findViewById(R.id.dateTimeText);
        dateText.setText(DateFormat.format("dd MMM yyyy", resultsDate));
    	vlText = (EditText)view.findViewById(R.id.HIVViralLoad);   
    	hepCText = (EditText)view.findViewById(R.id.HepCViralLoad);
		cd4Text = (EditText)view.findViewById(R.id.EditCD4Count);
		cd4PercentText = (EditText)view.findViewById(R.id.EditCD4Percent);
		vlText = (EditText)view.findViewById(R.id.HIVViralLoad);
		sugarText = (EditText)view.findViewById(R.id.BloodSugar);
		hdlText = (EditText)view.findViewById(R.id.HDL);
		ldlText = (EditText)view.findViewById(R.id.LDL);
		cholesterolText = (EditText)view.findViewById(R.id.Cholesterol);
		systoleText = (EditText)view.findViewById(R.id.Systole);
		diastoleText = (EditText)view.findViewById(R.id.Diastole);
		heartRateText = (EditText)view.findViewById(R.id.HeartRate);
		riskText = (EditText)view.findViewById(R.id.CardiacRisk);
		hepCText = (EditText)view.findViewById(R.id.HepCViralLoad);
		weightText = (EditText)view.findViewById(R.id.Weight);
	}
	
	private void prefillTextFields()
	{
    	if(null != results)
    	{
    		if(results.getCD4Count() > 0)
    			cd4Text.setText(new String(Integer.toString(results.getCD4Count())));
    		if(results.getCD4Percent() >0)
    			cd4PercentText.setText(new String(Float.toString(results.getCD4Percent())));
    		if(results.getGlucose() >0)
    			sugarText.setText(new String(Float.toString(results.getGlucose())));
    		if(results.getSystole() >0 && results.getDiastole() > 0)
    		{
    			systoleText.setText(new String(Integer.toString(results.getSystole())));
    			diastoleText.setText(new String(Integer.toString(results.getDiastole())));
    		}
    		if(results.getHeartRate() >0)
    			heartRateText.setText(Integer.toString(results.getHeartRate()));
    		
    		if(results.getHDL() >0)
    			hdlText.setText(Float.toString(results.getHDL()));
    		if(results.getLDL() >0)
    			ldlText.setText(Float.toString(results.getLDL()));
    		
    		if(results.getCardiacRiskFactor() >0)
    			riskText.setText(Float.toString(results.getCardiacRiskFactor()));
    	
    		if(results.getTotalCholesterol() >0)
    			cholesterolText.setText(Float.toString(results.getTotalCholesterol()));
    		if(results.getWeight() > 0)
    			weightText.setText(Float.toString(results.getWeight()));
    		int viralLoadValue = results.getViralLoad();
    		if( 10 >= viralLoadValue)
    		{
    			if(0 <= viralLoadValue)
    			{
        			vlText.setText(getResources().getString(R.string.undetectable));
        			vlText.setEnabled(false);
        			checkBox.setChecked(true);    				
    			}
    			else
    			{
    				vlText.setText("");
    				vlText.setEnabled(true);
    				checkBox.setChecked(false);
    			}
    		}
    		else
    		{
    			vlText.setText(new String(Integer.toString(results.getViralLoad())));
    			vlText.setEnabled(true);
    			checkBox.setChecked(false);
    		}
    		int hepCViralLoadValue = results.getHepCViralLoad();
    		if(10 >= hepCViralLoadValue)
    		{
    			if(0 <= hepCViralLoadValue)
    			{
        			hepCText.setText(getResources().getString(R.string.undetectable));
        			hepCText.setEnabled(false);
        			hepCCheckBox.setChecked(true);    				
    			}
    			else
    			{
    				hepCText.setText("");
        			hepCText.setEnabled(true);
    				hepCCheckBox.setChecked(false);
    			}
    		}
    		else
    		{
    			hepCText.setText(new String(Integer.toString(results.getViralLoad())));
    			hepCText.setEnabled(true);
    			hepCCheckBox.setChecked(false);
    		}
    		long resultDateAsLong = results.getTime();
    		resultsDate = new Date(resultDateAsLong);
            dateText.setText(DateFormat.format("dd MMM yyyy", resultsDate));
    	}
		
	}
	
	
}

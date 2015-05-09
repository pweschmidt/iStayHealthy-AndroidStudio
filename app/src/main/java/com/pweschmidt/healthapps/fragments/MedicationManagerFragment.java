package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.EffectsListActivity;
import com.pweschmidt.healthapps.MissedListActivity;
import com.pweschmidt.healthapps.PreviousListActivity;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

public class MedicationManagerFragment extends Fragment 
	implements View.OnClickListener
{
	protected static final int EFFECTS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MISSEDMEDS_ACTIVITY_REQUEST_CODE = 110;
	protected static final int PREVIOUSMEDS_ACTIVITY_REQUEST_CODE = 120;

	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	
    	TableRow effectsRow = (TableRow)getActivity().findViewById(R.id.sideEffectsRow);
    	effectsRow.setOnClickListener(this);
    	
    	TableRow missedRow = (TableRow)getActivity().findViewById(R.id.missedMedsRow);
    	missedRow.setOnClickListener(this);
    	
    	TableRow previousRow = (TableRow)getActivity().findViewById(R.id.previousMedsRow);
    	previousRow.setOnClickListener(this);
    	
    }	
	
	public void onResume()
	{
		super.onResume();
    	TextView effectsText = (TextView)getActivity().findViewById(R.id.numberOfSideEffects);
    	TextView missedText = (TextView)getActivity().findViewById(R.id.numberOfMissedDates);
    	TextView previousText = (TextView)getActivity().findViewById(R.id.numberOfPreviousMeds);
    	int effects = getEffectsCount();
    	effectsText.setText(""+effects);
    	int missed = getMissedCount();
    	missedText.setText(""+missed);
    	int previous = getPreviousCount();
    	previousText.setText(""+previous);
	}
	
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.med_manager_fragment, container, false);
    	return view;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(Activity.RESULT_OK == resultCode)
    	{
    		switch(requestCode)
    		{
    		case EFFECTS_ACTIVITY_REQUEST_CODE:
    	    	TextView effectsText = (TextView)getActivity().findViewById(R.id.numberOfSideEffects);
    	    	int effects = getEffectsCount();
    	    	effectsText.setText(""+effects);
    			break;
    		case MISSEDMEDS_ACTIVITY_REQUEST_CODE:
    	    	TextView missedText = (TextView)getActivity().findViewById(R.id.numberOfMissedDates);
    	    	int missed = getMissedCount();
    	    	missedText.setText(""+missed);
    			break;
    		case PREVIOUSMEDS_ACTIVITY_REQUEST_CODE:
    	    	TextView previousText = (TextView)getActivity().findViewById(R.id.numberOfPreviousMeds);
    	    	int previous = getPreviousCount();
    	    	previousText.setText(""+previous);
    			break;
    		}
    	}
    }    
    
    public void onClick(View view)
    {
    	int resId = view.getId();
    	switch(resId)
    	{
    	case R.id.sideEffectsRow:
    		Intent effectsIntent = new Intent(getActivity(), EffectsListActivity.class);
    		startActivityForResult(effectsIntent, EFFECTS_ACTIVITY_REQUEST_CODE);
    		break;
    	case R.id.missedMedsRow:
    		Intent missedIntent = new Intent(getActivity(), MissedListActivity.class);
    		startActivityForResult(missedIntent, MISSEDMEDS_ACTIVITY_REQUEST_CODE);
    		break;
    	case R.id.previousMedsRow:
    		Intent previousIntent = new Intent(getActivity(), PreviousListActivity.class);
    		startActivityForResult(previousIntent, PREVIOUSMEDS_ACTIVITY_REQUEST_CODE);
    		break;
    	}
    }
    
    
    private int getEffectsCount()
    {
    	int count = 0;
    	Cursor cursor = getActivity().getContentResolver().query(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, null, null, null, null);
    	count = cursor.getCount();
    	cursor.close();
    	return count;
    }
    
    private int getMissedCount()
    {
    	int count = 0;
    	Cursor cursor = getActivity().getContentResolver().query(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, null, null, null, null);
    	count = cursor.getCount();
    	cursor.close();
    	return count;
    }
    
    private int getPreviousCount()
    {
    	int count = 0;
    	Cursor cursor = getActivity().getContentResolver().query(iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, null, null, null, null);
    	count = cursor.getCount();
    	cursor.close();
    	return count;
    }
}

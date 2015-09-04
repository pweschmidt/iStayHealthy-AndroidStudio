package com.pweschmidt.healthapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pweschmidt.healthapps.fragments.AllResultsFragment;
import com.pweschmidt.healthapps.fragments.BloodResultFragment;
import com.pweschmidt.healthapps.fragments.HIVResultsListFragment;
import com.pweschmidt.healthapps.fragments.OtherResultFragment;
import com.pweschmidt.healthapps.fragments.ResultsLoader;
//import android.util.*;
public class ResultsListActivity extends FragmentActivity 
	implements View.OnClickListener
{
//	private static final String TAG = "ResultsListActivity";
	public static final int RESULTS_ACTIVITY_REQUEST_CODE = 100;
	public static final int MODIFYRESULTS_ACTIVITY_REQUEST_CODE = 110;
	public static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;
	private static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
	private static final int HIV = 0;
	private static final int BLOOD = 1;
	private static final int OTHER = 2;
	private static final int ALL = 3;
	private int whichFragment;
	private ResultsLoader currentFragmentLoader;
	private Uri importData;
	private boolean isImported;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultslist);
		isImported = false;
    	ImageButton addButton = (ImageButton)findViewById(R.id.TitleActionButton);
    	addButton.setImageResource(R.drawable.addselector);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.Results);
    	titleText.setText(title);
    	    	
    	ImageButton pozButton = (ImageButton)findViewById(R.id.PozButton);
    	pozButton.setOnClickListener(this);
    	
    	RadioButton allButton = (RadioButton)findViewById(R.id.AllButton);
    	allButton.setOnClickListener(this);
    	allButton.setChecked(true);
    	
    	RadioButton hivButton = (RadioButton)findViewById(R.id.HIVButton);
    	hivButton.setOnClickListener(this);
    	
    	RadioButton bloodButton = (RadioButton)findViewById(R.id.BloodButton);
    	bloodButton.setOnClickListener(this);
    	
    	RadioButton otherButton = (RadioButton)findViewById(R.id.OtherButton);
    	otherButton.setOnClickListener(this);
    	whichFragment = ALL;
    	if(null != findViewById(R.id.resultsFragmentContainer))
    	{
    		if(null != savedInstanceState)
    			return;
    		AllResultsFragment fragment = new AllResultsFragment();
    		currentFragmentLoader = fragment;
    		fragment.setArguments(getIntent().getExtras());
    		getSupportFragmentManager().beginTransaction().add(R.id.resultsFragmentContainer, fragment).commit();
    	}
	}

	public void onResume()
	{
		super.onResume();
		importData = getIntent().getData();
		if(null != importData && !isImported)
		{
			Intent addResult = new Intent(ResultsListActivity.this, EditResultsActivity.class);
			addResult.setData(importData);
			startActivityForResult(addResult, RESULTS_ACTIVITY_REQUEST_CODE);
			isImported = true;
		}

//		currentFragmentLoader.reloadResults();
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//		Log.d(TAG,"Being called: onActivityResult. The request code is "+requestCode+" and the result code is "+resultCode);
    	if(	RESULTS_ACTIVITY_REQUEST_CODE == requestCode || MODIFYRESULTS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
//    		Log.d(TAG,"Being called: onActivityResult");
    		if(FragmentActivity.RESULT_OK == resultCode && null != currentFragmentLoader)
    		{
//        		Log.d(TAG,"Being called: onActivityResult - reloading the cursors for fragments");
    			currentFragmentLoader.reloadResults();
    		}
    	}
    }
	
	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.TitleActionButton:
    		Intent addResult = new Intent(this, EditResultsActivity.class);
    		startActivityForResult(addResult, RESULTS_ACTIVITY_REQUEST_CODE);    		
			break;
		case R.id.PozButton:
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.bannerURL);
            bannerIntent.putExtra("url", url);    		
			startActivityForResult(bannerIntent,POZ_BANNER_ACTIVITY_REQUEST_CODE);
			break;
		case R.id.AllButton:
			if(whichFragment != ALL)
			{
	    		AllResultsFragment fragment = new AllResultsFragment();
	    		currentFragmentLoader = fragment;
	    		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    		transaction.replace(R.id.resultsFragmentContainer, fragment);
	    		transaction.addToBackStack(null);
	    		transaction.commit();
				whichFragment = ALL;				
			}
			break;
		case R.id.HIVButton:
			if(whichFragment != HIV)
			{
	    		HIVResultsListFragment fragment = new HIVResultsListFragment();
	    		currentFragmentLoader = fragment;
	    		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    		transaction.replace(R.id.resultsFragmentContainer, fragment);
	    		transaction.addToBackStack(null);
	    		transaction.commit();
				whichFragment = HIV;
			}
			break;
		case R.id.BloodButton:
			if(whichFragment != BLOOD)
			{
	    		BloodResultFragment fragment = new BloodResultFragment();
	    		currentFragmentLoader = fragment;
	    		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    		transaction.replace(R.id.resultsFragmentContainer, fragment);
	    		transaction.addToBackStack(null);
	    		transaction.commit();
				whichFragment = BLOOD;
			}
			break;
		case R.id.OtherButton:
			if(whichFragment != OTHER)
			{
	    		OtherResultFragment fragment = new OtherResultFragment();
	    		currentFragmentLoader = fragment;
	    		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    		transaction.replace(R.id.resultsFragmentContainer, fragment);
	    		transaction.addToBackStack(null);
	    		transaction.commit();
				whichFragment = OTHER;
			}
			break;
		}
	}
}

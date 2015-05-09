package com.pweschmidt.healthapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.pweschmidt.healthapps.fragments.MedicationListFragment;
//import com.pweschmidt.healthapps.fragments.MedicationManagerFragment;

public class MedicationListActivity extends FragmentActivity 
	implements View.OnClickListener
{
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
	protected static final int MED_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYMEDS_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;
	private MedicationListFragment medListFragment;
//	private MedicationManagerFragment medMgrFragment;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medlist);
    	ImageButton addButton = (ImageButton)findViewById(R.id.TitleActionButton);
    	addButton.setImageResource(R.drawable.addselector);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getApplicationContext().getResources().getString(R.string.HIVDrugs);
    	titleText.setText(title);
    	    	
    	ImageButton pozButton = (ImageButton)findViewById(R.id.PozButton);
    	pozButton.setOnClickListener(this);
    	
    	FragmentManager manager = getSupportFragmentManager();
    	medListFragment = (MedicationListFragment)manager.findFragmentById(R.id.currentmeds_fragment);
//    	medMgrFragment = (MedicationManagerFragment)manager.findFragmentById(R.id.med_manager_fragment);
    	
	}

	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.TitleActionButton:
    		Intent addResult = new Intent(this, AddHIVDrugsActivity.class);
    		startActivityForResult(addResult, MED_ACTIVITY_REQUEST_CODE);    		
			break;
		case R.id.PozButton:
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.bannerURL);
            bannerIntent.putExtra("url", url);    		
			startActivity(bannerIntent);
			break;
		}
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(MED_ACTIVITY_REQUEST_CODE == requestCode || 
    			MODIFYMEDS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode && medListFragment != null)
    		{
    			medListFragment.receivedResult(requestCode, resultCode);
    		}
    	}
    }
	
	
}

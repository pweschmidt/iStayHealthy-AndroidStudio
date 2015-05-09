package com.pweschmidt.healthapps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class GeneralMedActivity extends Activity implements View.OnClickListener{
	protected static final int GENERALMEDS_ACTIVITY_REQUEST_CODE = 900;

	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generalmed);
        
        ImageButton banner = (ImageButton)findViewById(R.id.generalMedsBanner);
        banner.setOnClickListener(this);
        
        TableRow otherMedsRow = (TableRow)findViewById(R.id.otherMedRow);
        otherMedsRow.setOnClickListener(this);
        
        TableRow clinicRow = (TableRow)findViewById(R.id.clinicsRow);
        clinicRow.setOnClickListener(this);
        
        TableRow illnessRow = (TableRow)findViewById(R.id.illnessRow);
        illnessRow.setOnClickListener(this);
        
        
	}	
	
	/**
	 * 
	 */
	public void onClick(View view){
		int resId = view.getId();
		switch(resId){
		case R.id.generalMedsBanner:
			Intent bannerIntent = new Intent(this, WebViewActivity.class);
	        String url = getResources().getString(R.string.bannerURL);
	        bannerIntent.putExtra("url", url);
			startActivityForResult(bannerIntent,GENERALMEDS_ACTIVITY_REQUEST_CODE);			
			break;
		case R.id.otherMedRow:
			Intent otherMedIntent = new Intent(this, OtherMedsListActivity.class);
			startActivity(otherMedIntent);
			break;
		case R.id.clinicsRow:
			Intent clinicIntent = new Intent(this, ContactsListActivity.class);		
			startActivity(clinicIntent);
			break;
		case R.id.illnessRow:
			Intent illnessIntent = new Intent(this, ProceduresListActivity.class);
			startActivity(illnessIntent);
			break;
		}
	}
}

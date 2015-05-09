package com.pweschmidt.healthapps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;
//import android.util.*;

import com.pweschmidt.healthapps.documents.*;

public class ToolsActivity extends Activity implements View.OnClickListener{
//	private static final String TAG = "ToolsActivity";
	private static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
	public static final int LINK_ACTIVITY_REQUEST_CODE = 100;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools);
        ImageButton back = (ImageButton)findViewById(R.id.TitleActionButton);
        back.setImageResource(R.drawable.backselector);
        back.setOnClickListener(this);

    	TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.Extras);
    	titleText.setText(title);
        
    	ImageButton pozButton = (ImageButton)findViewById(R.id.PozButton);
    	pozButton.setOnClickListener(this);
        
        TableRow dropboxRow = (TableRow)findViewById(R.id.dropboxRow);
        dropboxRow.setOnClickListener(this);
        
        TableRow androidRow = (TableRow)findViewById(R.id.androidRow);
        androidRow.setOnClickListener(this);
        androidRow.setVisibility(View.GONE);
        
        TableRow emailRow = (TableRow)findViewById(R.id.emailResultsRow);
        emailRow.setOnClickListener(this);
        
        TableRow feedbackRow = (TableRow)findViewById(R.id.feedbackRow);
        feedbackRow.setOnClickListener(this);
        
        TableRow passwordRow = (TableRow)findViewById(R.id.passwordRow);
        passwordRow.setOnClickListener(this);
        
        TableRow glossaryRow = (TableRow)findViewById(R.id.glossaryRow);
        glossaryRow.setOnClickListener(this);
        
    }

    /**
     * 
     */
    public void onClick(View view){
    	int resId = view.getId();
    	switch(resId){
    	case R.id.TitleActionButton:
    		finish();
    		break;
    	case R.id.emailResultsRow:
    		CSVDocument doc = new CSVDocument(this);
    		String text = doc.toString();
    		Intent emailIntent = new Intent(Intent.ACTION_SEND);
    		emailIntent.setType("plain/text");
    		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "iStayHealthy Data");
    		emailIntent.putExtra(Intent.EXTRA_TEXT, text);
    		startActivity(Intent.createChooser(emailIntent, "Send email..."));
    		break;
    	case R.id.feedbackRow:
    		Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
    		feedbackIntent.setType("plain/text");
    		feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "iStayHealthy Feedback");
    		feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"istayhealthy.app@gmail.com"});    		
    		startActivity(Intent.createChooser(feedbackIntent, "Send email..."));
    		break;
    	case R.id.glossaryRow:
    		Intent infoIntent = new Intent(this, WebViewActivity.class);
            String infoUrl = getResources().getString(R.string.officialinfo);
            infoIntent.putExtra("url", infoUrl);    		
			startActivityForResult(infoIntent,LINK_ACTIVITY_REQUEST_CODE);			
    		break;
    	case R.id.dropboxRow:
    		Intent dropboxIntent = new Intent(this, DropboxActivity.class);
    		startActivity(dropboxIntent);
    		break;
    	case R.id.androidRow:
    		Intent backupIntent = new Intent(this, AndroidBackupRestoreActivity.class);
    		startActivity(backupIntent);
    		break;
    	case R.id.passwordRow:
    		Intent passwordIntent = new Intent(this, SetPasswordActivity.class);
    		startActivity(passwordIntent);
    		break;
		case R.id.PozButton:
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.bannerURL);
            bannerIntent.putExtra("url", url);    		
			startActivityForResult(bannerIntent,POZ_BANNER_ACTIVITY_REQUEST_CODE);
			break;
    		
    	}    	
    }
        
}

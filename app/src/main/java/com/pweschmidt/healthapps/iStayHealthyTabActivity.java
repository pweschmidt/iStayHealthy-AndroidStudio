package com.pweschmidt.healthapps;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.pweschmidt.healthapps.datamodel.Medication;

//import android.os.Environment;
//import android.content.res.*;
//import android.util.Log;
public class iStayHealthyTabActivity extends TabActivity 
	implements TabHost.OnTabChangeListener
{
	public static final int RESULTS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int LOGIN_ACTIVITY_REQUEST_CODE = 1000;
//	private static final String TAG = "iStayHealthyTabActivity";
    static final Object[] sDataLock = new Object[0];
    private static final int DIALOG_MEDICATION = 10;
	protected static final int MISSED_ACTIVITY_REQUEST_CODE = 100;
    private TabHost tabHost;
	private Medication[] medications;
	private boolean confirmedIsChecked;
	private static iStayHealthyTabActivity thisActivity;
//	private ImportThread thread;
    private static ProgressDialog progressDialog;
    private static Handler handler;
    private boolean isTablet;
	private Uri importData;
	private boolean isImported;
    private Intent resultsIntent;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        handler = new Handler();
        isTablet = false;
		isImported = false;
        String screen = getResources().getString(R.string.screenType);
        if(null != screen && screen.startsWith("tablet"))
        {
        	isTablet = true;
        }
        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
        int whichTab = prefs.getInt("whichTab", 0);
    	confirmedIsChecked = true;
        iStayHealthy application = (iStayHealthy)getApplicationContext();
        boolean isPasswordEnabled = application.requiresPassword();
        if(isPasswordEnabled)
        {
        	Intent loginIntent = new Intent(this, LoginActivity.class);
        	startActivityForResult(loginIntent, LOGIN_ACTIVITY_REQUEST_CODE);
        }
        
        setContentView(R.layout.main);
//        Log.d(TAG, "onCreate");
        tabHost = getTabHost();  // The activity TabHost
        
        //Status/Chart activity
//        Intent statusIntent = new Intent().setClass(this, StatusActivity.class);
        Intent statusIntent = new Intent().setClass(this, DashboardActivity.class);
        setNewTab(this, tabHost, "charts", R.string.Charts, R.drawable.charts, statusIntent);
        
        //Results activity
        resultsIntent = new Intent().setClass(this, ResultsListActivity.class);
        importData = getIntent().getData();
        setNewTab(this, tabHost, "results", R.string.Results, R.drawable.list, resultsIntent);
        if (null != importData)
        {
            resultsIntent.setData(importData);
        }

        //Medication activity
        Intent medIntent = new Intent().setClass(this, MedicationListActivity.class);
        setNewTab(this, tabHost, "hivdrugs", R.string.HIVDrugs, R.drawable.combi, medIntent);
        
        //Alert activity
        Intent alertIntent = new Intent().setClass(this, AlertListActivity.class);
        setNewTab(this, tabHost, "alerts", R.string.Alerts, R.drawable.clock, alertIntent);

        //Alert activity
        Intent otherIntent = new Intent().setClass(this, GeneralMedActivity.class);
        setNewTab(this, tabHost, "general", R.string.general, R.drawable.cross, otherIntent);
        
        tabHost.setOnTabChangedListener(this);

        if(!isTablet)
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        tabHost.setCurrentTab(whichTab);

    }
    
    private void setNewTab(Context context, TabHost tabHost, String tag, int title, int icon, Intent content)
    {
    	TabSpec tabSpec = tabHost.newTabSpec(tag);
    	View tabView = getTabIndicator(tabHost.getContext(), title, icon);
    	tabSpec.setIndicator(tabView);
    	tabSpec.setContent(content);
    	tabHost.addTab(tabSpec);
    }    
    
    private View getTabIndicator(Context context, int title, int drawable)
    {
    	View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
    	TextView textView = (TextView)view.findViewById(R.id.textView);
    	textView.setText(title);
    	ImageView iconView = (ImageView)view.findViewById(R.id.tabImageView);
    	iconView.setBackgroundResource(drawable);
    	return view;
    }
    
    /**
     * 
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//    	Log.d(TAG,"Return from Login Activity");
    	switch(requestCode)
    	{
    	case LOGIN_ACTIVITY_REQUEST_CODE:
    		if(RESULT_CANCELED == resultCode)
    		{
//    	    	Log.d(TAG,"we finish the activity");
    			finish();
    		}
    		break;
    	}
    }



    /**
     * 
     */
    public void onTabChanged(String tabID)
    {
        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();
    	if(tabID.equalsIgnoreCase("charts"))
    	{
    		editor.putInt("whichTab", 0);
    	}
    	if(tabID.equalsIgnoreCase("results"))
    	{
    		editor.putInt("whichTab", 1);
    	}
    	if(tabID.equalsIgnoreCase("hivdrugs"))
    	{
    		editor.putInt("whichTab", 2);
    	}
    	if(tabID.equalsIgnoreCase("alerts"))
    	{
    		editor.putInt("whichTab", 3);
    	}
    	if(tabID.equalsIgnoreCase("general"))
    	{
    		editor.putInt("whichTab", 4);
    	}
		editor.commit();
    }
    

    public void onResume()
    {
    	super.onResume();
    	Bundle extras = getIntent().getExtras();
    	if(null != extras)
    	{
    		boolean isConfirmMeds = extras.getBoolean("isConfirmMeds");
    		if(isConfirmMeds && confirmedIsChecked)
    		{
//    			Log.d(TAG,"We are in isConfirmMeds");
    	    	Cursor medCursor = getContentResolver().query(iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
    			if(0 < medCursor.getCount() && medCursor.moveToFirst())
    			{
    				medications = new Medication[medCursor.getCount()];
    				int index = 0;
    				while(!medCursor.isAfterLast())
    				{
    					Medication med = new Medication();
    					med.setMedication(medCursor);
    					medications[index] = med;				
    					index++;
    					medCursor.moveToNext();
    				}			
        			showDialog(DIALOG_MEDICATION);
    			}  
    			confirmedIsChecked = false;
    	    	medCursor.close();
    		}
    	}
        importData = getIntent().getData();
        if(null != importData && !isImported)
        {
            resultsIntent.setData(importData);
			tabHost.setCurrentTab(1);
			isImported = true;
        }
    	NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancelAll();    	
    }
    
    protected Dialog onCreateDialog(int dialogId)
    {
    	switch(dialogId)
    	{
    	case DIALOG_MEDICATION:
    		if(null != medications)
    		{
        		if(0 < medications.length)
        		{
            		Builder builder = new AlertDialog.Builder(this);
            		builder.setMessage(R.string.confirmmeds);
            		builder.setCancelable(false);
            		builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {				
        				public void onClick(DialogInterface dialog, int which) {
        					confirmedIsChecked = false;    					
        				}
        			});
            		builder.setNegativeButton(R.string.No, new NoOnClickListener(this));
            		AlertDialog medDialog = builder.create();
            		medDialog.show();    			
        		}    			
    		}
    		break;    	
    	}
    	return super.onCreateDialog(DIALOG_MEDICATION);
    }
    
//	public Object onRetainNonConfigurationInstance()
//	{
//		return thread;
//	}
    
    protected void onDestroy()
    {
    	if(null != progressDialog && progressDialog.isShowing())
    	{
    		progressDialog.dismiss();
    		progressDialog = null;
    	}
    	super.onDestroy();
    }
    
    
    private final class NoOnClickListener implements DialogInterface.OnClickListener
    {
    	private Context context;
    	public NoOnClickListener(Context context)
    	{
    		this.context = context;    		
    	}
    	
		public void onClick(DialogInterface dialog, int which) 
		{
			confirmedIsChecked = false; 
			if(null != medications)
			{
				if(0 < medications.length)
				{
					Intent addMissed = new Intent(context, EditMissedActivity.class);
		    		addMissed.putExtra("isInEditMode", false);			
					addMissed.putExtra("Name", medications[0].getName());
					addMissed.putExtra("Drug", medications[0].getDrug());
		    		startActivityForResult(addMissed, MISSED_ACTIVITY_REQUEST_CODE);    													
				}
			}
			
		}    	
    }
}
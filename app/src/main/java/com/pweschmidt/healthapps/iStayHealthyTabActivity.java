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
import com.pweschmidt.healthapps.datamodel.Results;
import com.pweschmidt.healthapps.documents.XMLErrorHandler;
import com.pweschmidt.healthapps.documents.XMLParser;

import org.xml.sax.SAXParseException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.StringTokenizer;

//import android.os.Environment;
//import android.content.res.*;
//import android.util.Log;
public class iStayHealthyTabActivity extends TabActivity 
	implements TabHost.OnTabChangeListener
{
	protected static final int LOGIN_ACTIVITY_REQUEST_CODE = 1000;
//	private static final String TAG = "iStayHealthyTabActivity";
    static final Object[] sDataLock = new Object[0];
    private static final int DIALOG_MEDICATION = 10;
	protected static final int MISSED_ACTIVITY_REQUEST_CODE = 100;
    private TabHost tabHost;
	private Medication[] medications;
	private boolean confirmedIsChecked;
	private static iStayHealthyTabActivity thisActivity;
	private ImportThread thread;
    private static ProgressDialog progressDialog;
    private static Handler handler;
    private boolean isTablet;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        handler = new Handler();
        isTablet = false;
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
        Intent resultsIntent = new Intent().setClass(this, ResultsListActivity.class);
        setNewTab(this, tabHost, "results", R.string.Results, R.drawable.list, resultsIntent);

        //Medication activity
        Intent medIntent = new Intent().setClass(this, MedicationListActivity.class);
        setNewTab(this, tabHost, "hivdrugs", R.string.HIVDrugs, R.drawable.combi, medIntent);
        
        //Alert activity
        Intent alertIntent = new Intent().setClass(this, AlertListActivity.class);
        setNewTab(this, tabHost, "alerts", R.string.Alerts, R.drawable.clock, alertIntent);

        //Alert activity
        Intent otherIntent = new Intent().setClass(this, GeneralMedActivity.class);
        setNewTab(this, tabHost, "general", R.string.general, R.drawable.cross, otherIntent);
        
        //Landscape Charts Activity. This is only activated when in landscape mode
        // but for this we never show the tabbar
//        Intent landscapeIntent = new Intent().setClass(this, DashboardLandscapeActivity.class);
//        setNewTab(this, tabHost, "", R.string.Charts, R.drawable.charts, landscapeIntent);
//        TabHost.TabSpec landscapeSpec = tabHost.newTabSpec("").setIndicator(null,
//        		null).setContent(landscapeIntent);
//        tabHost.addTab(landscapeSpec);
//        tabHost.getTabWidget().getChildAt(5).setVisibility(View.GONE);
        tabHost.setOnTabChangedListener(this);        

        if(!isTablet)
        {
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        /*
        Configuration config = getResources().getConfiguration();  
        if(isTablet)
        {
        	showTabbars();
            tabHost.setCurrentTab(whichTab);        	        	
        }
        else
        {
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) 
            {
                hideTabbars();
                tabHost.setCurrentTab(0);        		
            }
            else
            {
            	showTabbars();
                tabHost.setCurrentTab(whichTab);        	
            }        	
        }
        */
        tabHost.setCurrentTab(whichTab);        	
        thread = (ImportThread) getLastNonConfigurationInstance();
		if (thread != null && thread.isAlive()) 
		{
	    	progressDialog = ProgressDialog.show(this, "", "Importing...");
		}
        
        
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
    
    /*
    private void hideTabbars()
    {
        tabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
        tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
        tabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
        tabHost.getTabWidget().getChildAt(3).setVisibility(View.GONE);
        tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);
    }
    
    private void showTabbars()
    {
        tabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
        tabHost.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
        tabHost.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);
        tabHost.getTabWidget().getChildAt(3).setVisibility(View.VISIBLE);
        tabHost.getTabWidget().getChildAt(4).setVisibility(View.VISIBLE);    	
    }
     */
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
    
   /* 
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        if(isTablet)
        	return;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
        	hideTabbars();
        	tabHost.setCurrentTab(0);
        	overridePendingTransition(R.anim.transition, R.anim.phaseout);
        }
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
            int whichTab = prefs.getInt("whichTab", 0);
        	showTabbars();
            tabHost.setCurrentTab(whichTab);        	        	
        	overridePendingTransition(R.anim.transition, R.anim.phaseout);
        }
    } 
    */

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
        Uri data = getIntent().getData();
        if(null != data)
        {
        	if(null != thread && thread.isAlive())
        	{
        		thread.stop();
        		thread = null;
        	}
    	    progressDialog = ProgressDialog.show(this, "", "Importing...");
        	thread = new ImportThread(getIntent().getData());
        	thread.start();
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
    
	public Object onRetainNonConfigurationInstance() 
	{
		return thread;
	}
    
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
    
    static public class ImportRunnable implements Runnable
    {
    	public void run()
    	{
    		progressDialog.dismiss();
    	}
    }
    
    
    static public class ImportThread extends Thread
    {
    	private Uri uri;
    	
    	public ImportThread(Uri uri)
    	{
    		super();
    		this.uri = uri;    		
    	}
    	
    	public void run()
    	{
    		if(null == uri)
    			return;
    		
    		String scheme = uri.getScheme();
    		String host = uri.getHost();
			String path = uri.getPath();
    		if(null != scheme && null != host)
    		{
        		if(scheme.equalsIgnoreCase("http") && host.equalsIgnoreCase("www.istayhealthy.uk.com") && path.contains("results"))
        		{
        			String parameters = uri.getSchemeSpecificPart();
        			StringTokenizer tokenizer = new StringTokenizer(parameters, ";");
        			String query = null;
        			if(2 == tokenizer.countTokens())
        			{
        				tokenizer.nextToken();
        				query = tokenizer.nextToken();
        			}
        			if(null != query)
        			{
        				tokenizer = new StringTokenizer(query,"&");
        			}
					else
					{
						tokenizer = new StringTokenizer(path,"&");
					}
        			boolean hasData = false;
        			Results results = new Results();
        			results.setTime((new Date()).getTime());
        			while(tokenizer.hasMoreTokens())
        			{
        				String parameterSet = tokenizer.nextToken();
        				StringTokenizer params = new StringTokenizer(parameterSet,"=");
        				if(2 == params.countTokens())
        				{
        					String key = params.nextToken();
        					String value = params.nextToken();
        					if(key.equalsIgnoreCase("cd4"))
        					{
        						results.setCD4Count(Integer.parseInt(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("cd4Percent"))
        					{
        						results.setCD4Percent(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("viralload"))
        					{
        						results.setViralLoad(Integer.parseInt(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("risk"))
        					{
        						results.setCardiacRiskFactor(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("cholesterol"))
        					{
        						results.setTotalCholesterol(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("hdl"))
        					{
        						results.setHDL(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("ldl"))
        					{
        						results.setLDL(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("glucose"))
        					{
        						results.setGlucose(Float.parseFloat(value));
        						hasData = true;
        					}
        					else if(key.equalsIgnoreCase("hepCViralLoad"))
        					{
        						results.setHepCViralLoad(Integer.parseInt(value));
        						hasData = true;
        					}
        				}
        			}
        			if(hasData)
        			{
        				thisActivity.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, results.contentValuesForResult());        				
        			}
        		} 
        		else //could be file/email attachment
        		{
//        			Log.d(TAG,"it could be that we have an email attachment at path "+uri.getPath());
        			try
        			{
        				InputStream input = thisActivity.getContentResolver().openInputStream(uri);
        				if(null != input)
        				{
//        					Log.d(TAG,"read in the data from the attachment");
        					ByteArrayOutputStream out = new ByteArrayOutputStream();
        					byte[] buffer = new byte[4];
        					while(input.read(buffer) > 0)
        					{
        						out.write(buffer);
        					}
        					byte[] xmlData = out.toByteArray();
//        					Log.d(TAG,"We read in "+xmlData.length+ " bytes");
        					if(0 < xmlData.length)
        					{

                                XMLErrorHandler errorHandler = new XMLErrorHandler();
            					XMLParser parser = new XMLParser(xmlData, thisActivity);
                                try {
                                    parser.parse(errorHandler);
                                }
                                catch (SAXParseException se)
                                {
                                    input.close();
                                    out.close();
                                }
        					}
        					input.close();
        					out.close();
        					
        				}
        				
        			}catch(Exception e){};
        		}
    		}
        	handler.post(new ImportRunnable());
    	}
    	
    }
    
    
}
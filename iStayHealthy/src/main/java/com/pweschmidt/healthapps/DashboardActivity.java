package com.pweschmidt.healthapps;

import android.content.Intent;
//import android.content.res.Configuration;
import android.database.Cursor;
import android.database.ContentObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.*;
import com.pweschmidt.healthapps.graphs.*;
import android.content.pm.ActivityInfo;
//import android.util.Log;

public class DashboardActivity extends FragmentActivity 
	implements View.OnClickListener, PageControllerListener, LoaderManager.LoaderCallbacks<Cursor>
{
//	private static final String TAG = "DashboardActivity";
	private ViewPager pager;
	private DashboardPageControllerLayout pageController;
	private DashboardPagerAdapter pagerAdapter;
	public static final int NUMBEROFSUPPORTEDGRAPHS = 0x7;
	public static final int RESULTSLOADER = 0x01;
	public static final int MEDLOADER = 0x02;
	public static final int MISSEDLOADER = 0x03;
	public static final int PREVLOADER = 0x04;
//	private boolean isOnCreate;
	private DashboardActivity thisActivity;
	private ResultsObserver resultsObserver;
	private ResultsObserver medObserver;
	private ResultsObserver missedObserver;
	private TableRow headerRow;
	private boolean isSmallPhone;
	private boolean isPhone;
//	private View separatorView;

	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		thisActivity = this;
//		isOnCreate = true;
		setContentView(R.layout.dashboard);
        String screen = getResources().getString(R.string.screenType);
        isSmallPhone = false;
        isPhone = true;
        if(null != screen)
        {
        	if(screen.startsWith("smallphone"))
        	{
        		isSmallPhone = true;
        	}
        	else if(screen.startsWith("tablet"))
        	{
        		isPhone = false;
        	}
        }
 		
		ImageButton toolButton = (ImageButton)findViewById(R.id.TitleActionButton);
		toolButton.setImageResource(R.drawable.toolselector);
		toolButton.setOnClickListener(this);
		
		TextView titleView = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getApplicationContext().getResources().getString(R.string.Charts);
    	titleView.setText(title);
    	
    	ImageButton poz = (ImageButton)findViewById(R.id.PozButton);
    	poz.setOnClickListener(this);
    	
    	headerRow = (TableRow)findViewById(R.id.headerTableRow);
    	headerRow.setVisibility(View.VISIBLE);
    	
//    	separatorView = (View)findViewById(R.id.separatorView);
//    	separatorView.setVisibility(View.VISIBLE);

    	resultsObserver = new ResultsObserver(RESULTSLOADER);
    	medObserver = new ResultsObserver(MEDLOADER);
    	missedObserver = new ResultsObserver(MISSEDLOADER);
    	getApplicationContext().getContentResolver().registerContentObserver(iStayHealthyContentProvider.RESULTS_CONTENT_URI, true, resultsObserver);
    	getApplicationContext().getContentResolver().registerContentObserver(iStayHealthyContentProvider.MEDS_CONTENT_URI, true, medObserver);
    	getApplicationContext().getContentResolver().registerContentObserver(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, true, missedObserver);
    	initViewPager();


    	getSupportLoaderManager().initLoader(RESULTSLOADER,null,this);
		getSupportLoaderManager().initLoader(MEDLOADER,null,this);
		getSupportLoaderManager().initLoader(MISSEDLOADER,null,this);
		
		if(isPhone)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		if(isSmallPhone && null != pageController)
		{
			pageController.setVisibility(View.GONE);
		}
		/*
        Configuration config = getResources().getConfiguration(); 
        if(isSmallPhone)
        {
        	headerRow.setVisibility(View.GONE);
    		pageController.setVisibility(View.GONE);
        	pagerAdapter.hideHeaders();        	
        }
        else if(!isTablet)
        {
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) 
            {
            	headerRow.setVisibility(View.GONE);
        		pageController.setVisibility(View.GONE);
            	pagerAdapter.hideHeaders();
            }
            else
            {
            	headerRow.setVisibility(View.VISIBLE);        	
        		pageController.setVisibility(View.VISIBLE);
            	pagerAdapter.showHeaders();
            }        	
        }
        */
        
		
	}
	
	/*
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        if(isTablet || isSmallPhone)
        	return;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
        	headerRow.setVisibility(View.GONE);
    		pageController.setVisibility(View.GONE);
        	pagerAdapter.hideHeaders();
        }
        else
        {
        	headerRow.setVisibility(View.VISIBLE);        	
    		pageController.setVisibility(View.VISIBLE);
        	pagerAdapter.showHeaders();
        }
    }	
	*/
	
	public void onResume()
	{
		super.onResume();
		/*
		if(isOnCreate)
		{
			isOnCreate = false;
		}
		else
		{
			pagerAdapter.clearAllData();
			getSupportLoaderManager().restartLoader(RESULTSLOADER,null,this);
			getSupportLoaderManager().restartLoader(MEDLOADER,null,this);
			getSupportLoaderManager().restartLoader(MISSEDLOADER,null,this);			
		}
		*/
	}
	
	public void onClick(View view)
	{
    	int resID = view.getId();
    	switch(resID)
    	{
    	case R.id.TitleActionButton:
    		Intent extrasIntent = new Intent(this, ToolsActivity.class);
    		startActivity(extrasIntent);
    		break;
    	case R.id.PozButton:
    		Intent bannerIntent = new Intent(this, WebViewActivity.class);
            String url = getResources().getString(R.string.bannerURL);
            bannerIntent.putExtra("url", url); 
            startActivity(bannerIntent);
    		break;
    	}
		
	}
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = null;
    	switch(id)
    	{
    	case RESULTSLOADER:
//    		Log.d(TAG,"creating loader for results");
        	loader = new CursorLoader(this, iStayHealthyContentProvider.RESULTS_CONTENT_URI, null, null, null, null);
    		break;
    	case MEDLOADER:
//    		Log.d(TAG,"creating loader for meds");
        	loader = new CursorLoader(this, iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
    		break;
    	case MISSEDLOADER:
//    		Log.d(TAG,"creating loader for missed");
        	loader = new CursorLoader(this, iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, null, null, null, null);
    		break;
    	case PREVLOADER:
//    		Log.d(TAG,"creating loader for missed");
    		loader = new CursorLoader(this, iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, null, null, null, null);
    		break;
    	default:
//    		Log.d(TAG,"don't know what to load - so load results");
        	loader = new CursorLoader(this, iStayHealthyContentProvider.RESULTS_CONTENT_URI, null, null, null, null);
    		break;
    	}
    	return loader;
    }
    
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
//    	Log.d(TAG,"**** onLoadFinished ****");
    	int whichLoader = loader.getId();
    	switch(whichLoader)
    	{
    	case RESULTSLOADER:
			pagerAdapter.clearResults();
    		pagerAdapter.setResultsFromCursor(cursor);
    		break;
    	case MEDLOADER:
			pagerAdapter.clearMedication();
    		pagerAdapter.setMedicationsFromCursor(cursor);
    		break;
    	case MISSEDLOADER:
			pagerAdapter.clearMissedMedication();
    		pagerAdapter.setMissedMedicationsFromCursor(cursor);
    		break;
    	case PREVLOADER:
    		pagerAdapter.setPreviousMedicationsFromCursor(cursor);
    		break;
    	}
    }
    
    public void onLoaderReset(Loader<Cursor> loader)
    {
//    	Log.d(TAG,"**** onLoaderReset ****");
    	pagerAdapter.clearAllData();
    }
	
    
	public void turnPage(int toPage)
	{
		pager.setCurrentItem(toPage, true);
	}
	
	/**
	 * TODO we have test pages only at the moment
	 */
	private void initViewPager()
	{
		pageController = (DashboardPageControllerLayout)findViewById(R.id.pageControlView);
		pageController.addPageControllerListener(this);
		pageController.setPageNumber(NUMBEROFSUPPORTEDGRAPHS);
		pageController.setPage(0);
		pageController.setVisibility(View.VISIBLE);
		
		pagerAdapter = new DashboardPagerAdapter(getApplicationContext());

		
		pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setClipChildren(true);
		pager.setPageMargin(10);
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int page) 
			{
				if(null != pageController)
					pageController.setPage(page);
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			public void onPageScrollStateChanged(int arg0) {}
		});
		
	}
	
	private class ResultsObserver extends ContentObserver
	{
		private int which;
		public ResultsObserver(int which)
		{
			super(null);
			this.which = which;
		}
		
		public void onChange(boolean selfChange)
		{
			super.onChange(selfChange);
			switch(which)
			{
			case RESULTSLOADER:
				thisActivity.runOnUiThread(new Runnable(){
					public void run()
					{
						getSupportLoaderManager().restartLoader(RESULTSLOADER,null,thisActivity);						
					}
				});
				
				break;
			case MEDLOADER:
				thisActivity.runOnUiThread(new Runnable(){
					public void run()
					{
						getSupportLoaderManager().restartLoader(MEDLOADER,null,thisActivity);						
					}
				});
				break;
			case MISSEDLOADER:
				thisActivity.runOnUiThread(new Runnable(){
					public void run()
					{
						getSupportLoaderManager().restartLoader(MISSEDLOADER,null,thisActivity);						
					}
				});
				break;
			case PREVLOADER:
				break;
			}
		}
		
	}
	
	
}

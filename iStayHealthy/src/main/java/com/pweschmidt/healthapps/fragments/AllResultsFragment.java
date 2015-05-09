package com.pweschmidt.healthapps.fragments;

import java.text.NumberFormat;
import java.util.Locale;

import com.pweschmidt.healthapps.EditResultsActivity;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

//import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.pweschmidt.healthapps.ResultsListActivity;
//import android.util.*;

public class AllResultsFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>, ResultsLoader
{
//	private static final String TAG = "AllResultsFragment";
	private static final int RESULTS_LIST_LOADER = 0x01;
	protected static final int MODIFYRESULTS_ACTIVITY_REQUEST_CODE = 110;
	private ResultsCursorAdapter adapter;

	   public void onActivityCreated(Bundle savedInstanceState)
	    {
	    	super.onActivityCreated(savedInstanceState);
	    	adapter = new ResultsCursorAdapter(
	                getActivity().getApplicationContext(), 
	                R.layout.result_list_item, 
	                null, 
	                new String[] {iStayHealthyDatabaseSchema.CD4,iStayHealthyDatabaseSchema.CD4PERCENT,iStayHealthyDatabaseSchema.VIRALLOAD,iStayHealthyDatabaseSchema.RESULTSDATE}, 
	                new int[] {R.id.CD4Count,R.id.CD4Percent,R.id.ViralLoad,R.id.resultsDate});
	    	getLoaderManager().initLoader(RESULTS_LIST_LOADER, null, this);
	        setListAdapter(adapter);    	
	    }
	   
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	    {
	    	return inflater.inflate(R.layout.dynamic_result_fragment, container, false);
	    }    
	    	
	    public void reloadResults()
	    {
//	    	Log.d(TAG,"reloading results for AllResultsFragment");
			getLoaderManager().restartLoader(RESULTS_LIST_LOADER, null, this);    		    	
	    }

	    /*
	    public void setArguments(Bundle args)
	    {
	    	if(null == args)
	    		return;
	    	int resultsCode = args.getInt("AddResultsCode");
	    	int activityCode = args.getInt("ActivityResultCode");
	    	if(resultsCode == ResultsListActivity.RESULTS_ACTIVITY_REQUEST_CODE && activityCode == FragmentActivity.RESULT_OK)
	    	{
				getLoaderManager().restartLoader(RESULTS_LIST_LOADER, null, this);    		
	    	}
	    }
	    */
	    public void onActivityResult(int requestCode, int resultCode, Intent data)
	    {
	    	super.onActivityResult(requestCode, resultCode, data);
//	    	Log.d(TAG,"onActivityResult:: called with requestCode="+requestCode);
	    	if(ResultsListActivity.MODIFYRESULTS_ACTIVITY_REQUEST_CODE == requestCode || 
	    			ResultsListActivity.RESULTS_ACTIVITY_REQUEST_CODE == requestCode)
	    	{
	    		if(FragmentActivity.RESULT_OK == resultCode)
	    		{
//	    	    	Log.d(TAG,"onActivityResult::reloading results for AllResultsFragment");
	    			getLoaderManager().restartLoader(RESULTS_LIST_LOADER, null, this);
	    		}
	    	}
	    }
	
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent modifyResults = new Intent(getActivity(), EditResultsActivity.class);
		modifyResults.putExtra("ResultsIndex", id);
//    	Log.d(TAG,"onListItemClick - select row "+id+" and start activity with code"+ResultsListActivity.MODIFYRESULTS_ACTIVITY_REQUEST_CODE);
    	getActivity().startActivityForResult(modifyResults, ResultsListActivity.MODIFYRESULTS_ACTIVITY_REQUEST_CODE);
	}
	
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.REVERSED_RESULTS_CONTENT_URI, null, null, null, null);
    	return loader;
    }
    
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
    	adapter.swapCursor(cursor);
    }
    
    public void onLoaderReset(Loader<Cursor> loader)
    {
    	adapter.swapCursor(null);    	
    }
	
	
    private class ResultsCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;

    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public ResultsCursorAdapter(Context context, int layout, Cursor cursor,
    			String[] from, int[] to){
    		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		this._context = context;
     	}
    	
    	/**
    	 * callback when going to ListView position
    	 */
       	public View getView(int pos, View inView, ViewGroup parent) {
  	       View view = inView;
  	       if (null == view) {
  	            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  	            view = inflater.inflate(R.layout.result_list_item, null);
  	       }
  	       Cursor cursor = adapter.getCursor();
  	       cursor.moveToPosition(pos);
  	       int cd4Count = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CD4));
  	       float cd4Percent = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CD4PERCENT));
  	       int viralLoad = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.VIRALLOAD));
  	       
  	       String cd4String = getString(R.string.notapplicable);
  	       String cd4PercentString = getString(R.string.notapplicable);
  	       String vlString = getString(R.string.notapplicable);
  	       
  	     	       
  	       long dateValue = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.RESULTSDATE));
  	       
  	       TextView dateView = (TextView)view.findViewById(R.id.resultsDate);
  	       dateView.setText(DateFormat.format("dd MMM yyyy", dateValue));
  	       
  	       TextView cd4Title = (TextView)view.findViewById(R.id.CD4Title);
  	       cd4Title.setText(R.string.CD4);
  	       
  	       TextView cd4ResultView = (TextView)view.findViewById(R.id.CD4Count);
  	       if(0 < cd4Count){
  	    	   cd4String = Integer.toString(cd4Count);
  	    	   if(200>= cd4Count)cd4ResultView.setTextColor(getResources().getColor(R.color.darkred));
  	    	   if(200 < cd4Count && 350 >= cd4Count )cd4ResultView.setTextColor(getResources().getColor(R.color.darkyellow));
  	    	   if(350 < cd4Count)cd4ResultView.setTextColor(getResources().getColor(R.color.darkgreen));
  	       }
  	       else{
  	    	 cd4ResultView.setTextColor(Color.DKGRAY);
  	       }
  	       cd4ResultView.setText(cd4String);

  	       NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
  	       numberFormat.setMaximumFractionDigits(1);
  	       numberFormat.setGroupingUsed(false);
  	       TextView cd4PercentTitle = (TextView)view.findViewById(R.id.CD4PercentTitle);
  	       cd4PercentTitle.setText(R.string.CD4Percent);

  	       TextView cd4PercentView = (TextView)view.findViewById(R.id.CD4Percent);
  	       cd4PercentView.setText(cd4PercentString);
  	       if(0 < cd4Percent){
  	    	   cd4PercentString = numberFormat.format(cd4Percent);
  	    	   if(15 > cd4Percent)cd4PercentView.setTextColor(getResources().getColor(R.color.darkred));
  	    	   if(15 <= cd4Percent && 21 > cd4Percent)cd4PercentView.setTextColor(getResources().getColor(R.color.darkyellow));
  	    	   if(21 <= cd4Percent)cd4PercentView.setTextColor(getResources().getColor(R.color.darkgreen));
  	       }
  	       else{
  	    	 cd4PercentView.setTextColor(Color.DKGRAY);
  	       }
  	       cd4PercentView.setText(cd4PercentString);
  	       
  	       TextView vlTitle = (TextView)view.findViewById(R.id.ViralLoadTitle);
  	       vlTitle.setText(R.string.ViralLoad);
  	       
  	       TextView viralLoadView = (TextView)view.findViewById(R.id.ViralLoad);
  	       viralLoadView.setText(vlString);
  	       if(0 <= viralLoad){
  	    	   if(10 >= viralLoad){
  	    		   vlString = getString(R.string.undetectable);
  	    		   viralLoadView.setTextColor(getResources().getColor(R.color.darkgreen));
  	    	   }
  	    	   else
  	    		   vlString = Integer.toString(viralLoad);
  	    	   if(10 < viralLoad && 200 >= viralLoad)viralLoadView.setTextColor(getResources().getColor(R.color.darkyellow));
  	    	   if(200 < viralLoad)viralLoadView.setTextColor(getResources().getColor(R.color.darkred)); 	    	   
  	       }
  	       else{
  	    	   viralLoadView.setTextColor(Color.DKGRAY);
  	       }
  	       
  	       viralLoadView.setText(vlString);
  	       return (view);
       	}
    }

}

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

public class BloodResultFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>, ResultsLoader
{
//	private static final String TAG = "BloodResultFragment";
	private static final int RESULTS_LIST_LOADER = 0x01;
	protected static final int MODIFYRESULTS_ACTIVITY_REQUEST_CODE = 110;
	private BloodResultsCursorAdapter adapter;
	
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	adapter = new BloodResultsCursorAdapter(
                getActivity().getApplicationContext(), 
                R.layout.result_list_item, 
                null, 
                new String[] {iStayHealthyDatabaseSchema.TOTALCHOLESTEROL,iStayHealthyDatabaseSchema.LDL,iStayHealthyDatabaseSchema.GLUCOSE,iStayHealthyDatabaseSchema.RESULTSDATE}, 
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
		getLoaderManager().restartLoader(RESULTS_LIST_LOADER, null, this);    		    	
    }
    
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
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(ResultsListActivity.MODIFYRESULTS_ACTIVITY_REQUEST_CODE == requestCode || 
    			ResultsListActivity.RESULTS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(FragmentActivity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(RESULTS_LIST_LOADER, null, this);
    		}
    	}
    }
	
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent modifyResults = new Intent(getActivity(), EditResultsActivity.class);
		modifyResults.putExtra("ResultsIndex", id);
		getActivity().startActivityForResult(modifyResults, ResultsListActivity.MODIFYRESULTS_ACTIVITY_REQUEST_CODE);
	}
	
	
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	String selector = iStayHealthyDatabaseSchema.GLUCOSE + " > 0 OR " + iStayHealthyDatabaseSchema.TOTALCHOLESTEROL + " > 0 OR "
    			+ iStayHealthyDatabaseSchema.HDL + " > 0 OR " + iStayHealthyDatabaseSchema.LDL + " > 0";
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.REVERSED_RESULTS_CONTENT_URI, null, selector, null, null);
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
	
    private class BloodResultsCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;

    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public BloodResultsCursorAdapter(Context context, int layout, Cursor cursor,
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
  	       float cholesterol = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.TOTALCHOLESTEROL));
  	       float ldl = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.LDL));
  	       float sugar = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GLUCOSE));
  	       
  	       String cholesterolString = getString(R.string.notapplicable);
  	       String ldlString = getString(R.string.notapplicable);
  	       String sugarString = getString(R.string.notapplicable);
  	       
  	     	       
  	       long dateValue = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.RESULTSDATE));
  	       
  	       TextView dateView = (TextView)view.findViewById(R.id.resultsDate);
  	       dateView.setText(DateFormat.format("dd MMM yyyy", dateValue));
  	       
  	       TextView cd4Title = (TextView)view.findViewById(R.id.CD4Title);
  	       cd4Title.setText(R.string.cholesterol);
  	       
  	       NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
  	       numberFormat.setMaximumFractionDigits(2);
  	       numberFormat.setGroupingUsed(false);
  	       TextView cd4ResultView = (TextView)view.findViewById(R.id.CD4Count);
  	       if(0 < cholesterol)
  	       {
  	    	 cholesterolString = numberFormat.format(cholesterol);
  	       }
	       cd4ResultView.setTextColor(Color.DKGRAY);
  	       cd4ResultView.setText(cholesterolString);

  	       TextView cd4PercentTitle = (TextView)view.findViewById(R.id.CD4PercentTitle);
  	       cd4PercentTitle.setText(R.string.LDL);

  	       TextView cd4PercentView = (TextView)view.findViewById(R.id.CD4Percent);
  	       cd4PercentView.setText(ldlString);
  	       if(0 < ldl)
  	       {
  	    	 ldlString = numberFormat.format(ldl);
  	       }
  	       cd4PercentView.setTextColor(Color.DKGRAY);
  	       cd4PercentView.setText(ldlString);
  	       
  	       TextView vlTitle = (TextView)view.findViewById(R.id.ViralLoadTitle);
  	       vlTitle.setText(R.string.sugar);
  	       
  	       TextView viralLoadView = (TextView)view.findViewById(R.id.ViralLoad);
  	       if(0 < sugar)
  	       {
  	    	 sugarString = numberFormat.format(sugar);
  	       }
  	       
	       viralLoadView.setTextColor(Color.DKGRAY);
  	       viralLoadView.setText(sugarString);
  	       return (view);
       	}
    }
	
}

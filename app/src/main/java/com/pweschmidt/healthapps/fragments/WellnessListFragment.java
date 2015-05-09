package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class WellnessListFragment extends ListFragment 
	implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "ResultsListFragment";
//	private static final int WELL_LIST_LOADER = 0x01;
	protected static final int WELL_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYWELL_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    }	

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.results_fragment, container, false);
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.TitleActionButton);
    	addButton.setImageResource(R.drawable.nextitem);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.Results);
    	titleText.setText(title);
    	
    	ImageButton pozButton = (ImageButton)view.findViewById(R.id.PozButton);
    	pozButton.setOnClickListener(this);
    	return view;
    }
    
    
	public void onClick(View view)
	{
		
	}
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.WELLNESS_CONTENT_URI, null, null, null, null);
    	return loader;
    }
    
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
//    	adapter.swapCursor(cursor);
    }
    
    public void onLoaderReset(Loader<Cursor> loader)
    {
//    	adapter.swapCursor(null);    	
    }

	public void onListItemClick(ListView list, View view, int position, long id) 
	{
	}  
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    }	

}

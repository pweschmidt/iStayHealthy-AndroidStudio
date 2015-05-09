package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.EditPreviousActivity;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class PreviousListFragment extends ListFragment 
	implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
	
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "PreviousListFragment";
	private static final int PREVIOUS_LIST_LOADER = 0x01;
	protected static final int MODIFYPREVIOUS_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;
	protected PreviousCursorAdapter adapter;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        adapter = new PreviousCursorAdapter(
                getActivity().getApplicationContext(), 
                R.layout.previous_list_item, 
                null, 
                new String[] {"Name","StartDate","EndDate"}, 
                new int[] {R.id.previousMed,R.id.previousStartDateText,R.id.previousEndDateText});
    	getLoaderManager().initLoader(PREVIOUS_LIST_LOADER, null, this);
        setListAdapter(adapter);    	
    }
	
	
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.previous_fragment, container, false);
    	/*
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.TitleActionButton);
    	addButton.setImageResource(R.drawable.addbutton);
    	addButton.setOnClickListener(this);
    	*/
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.PreviousMeds);
    	titleText.setText(title);
    	
    	ImageButton backButton = (ImageButton)view.findViewById(R.id.BackButton);
    	backButton.setOnClickListener(this);
    	return view;
    }

	public void onClick(View view)
	{
		int resID = view.getId();
		if(resID == R.id.BackButton)
		{
			getActivity().finish();
		}
	}
	
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent editPrevious = new Intent(getActivity(), EditPreviousActivity.class);
		editPrevious.putExtra("PreviousIndex", id);
		startActivityForResult(editPrevious, MODIFYPREVIOUS_ACTIVITY_REQUEST_CODE);
	}
	

    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, null, null, null, null);
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
	
	
    private class PreviousCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;

    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public PreviousCursorAdapter(Context context, int layout, Cursor cursor,
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
  	            view = inflater.inflate(R.layout.previous_list_item, null);
  	       }
  	       Cursor cursor = adapter.getCursor();
  	       cursor.moveToPosition(pos);
 	       long startDate = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
 	       long endDate = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ENDDATE));
 	       String name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
 	       
  	       TextView startDateView = (TextView)view.findViewById(R.id.previousStartDateText);
  	       startDateView.setText(DateFormat.format("dd MMM yyyy ", startDate));
  	       startDateView.setTextColor(Color.LTGRAY);
 	       
  	       TextView endDateView = (TextView)view.findViewById(R.id.previousEndDateText);
  	       endDateView.setText(DateFormat.format("dd MMM yyyy ", endDate));
  	       endDateView.setTextColor(Color.LTGRAY);
  	       
  	       TextView drugsView = (TextView)view.findViewById(R.id.previousMed);
  	       drugsView.setText(name);
  	       
 	       
  	       return (view);
       	}
    }	
	
}

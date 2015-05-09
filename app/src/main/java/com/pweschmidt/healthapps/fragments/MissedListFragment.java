package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;
import com.pweschmidt.healthapps.EditMissedActivity;
import com.pweschmidt.healthapps.datamodel.Medication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class MissedListFragment extends ListFragment 
	implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "MissedListFragment";
	private static final int MISSED_LIST_LOADER = 0x01;
	protected static final int MISSED_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYMISSED_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;
	protected MissedCursorAdapter adapter;
	private Medication[] medications;
	private String[] medNames;
	private Medication selectedMed;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        adapter = new MissedCursorAdapter(
                getActivity().getApplicationContext(), 
                R.layout.missedmed_list_item, 
                null, 
                new String[] {"Name","MissedDate"}, 
                new int[] {R.id.missedMedName,R.id.missedMedDate});
    	getLoaderManager().initLoader(MISSED_LIST_LOADER, null, this);
        setListAdapter(adapter);    	
    }
	
	
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.missed_fragment, container, false);
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.AddButton);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.MissedMedications);
    	titleText.setText(title);
    	
    	Cursor medCursor = getActivity().getContentResolver().query(iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
		if(0 < medCursor.getCount() && medCursor.moveToFirst())
		{
			medications = new Medication[medCursor.getCount()];
			medNames = new String[medCursor.getCount()];
			int index = 0;
			while(!medCursor.isAfterLast())
			{
				Medication med = new Medication();
				med.setMedication(medCursor);
				medications[index] = med;				
				medNames[index] = med.getName();
				index++;
				medCursor.moveToNext();
			}			
		}  
		
		selectedMed = null;
    	medCursor.close();
    	
    	ImageButton backButton = (ImageButton)view.findViewById(R.id.BackButton);
    	backButton.setOnClickListener(this);
    	return view;
    }
	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.AddButton:
    		final Intent addMissed = new Intent(getActivity(), EditMissedActivity.class);
    		addMissed.putExtra("isInEditMode", false);
    		if(null != medications)
    		{
        		if(0 < medications.length)
        		{
        			if(1 == medications.length)
        			{
        				addMissed.putExtra("Name", medications[0].getName());
        				addMissed.putExtra("Drug", medications[0].getDrug());
        	    		startActivityForResult(addMissed, MISSED_ACTIVITY_REQUEST_CODE);    				
        			}
        			else
        			{
        			    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        			    builder.setTitle(R.string.HIVDrugs);
        			    builder.setSingleChoiceItems(medNames, 0, new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) 
    						{
    							selectedMed = medications[which];							
    						}
    					});
        			    
        			    builder.setPositiveButton(R.string.Select, new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							if(null != selectedMed)
    							{
    			    				addMissed.putExtra("Name", selectedMed.getName());
    			    				addMissed.putExtra("Drug", selectedMed.getDrug());								
    							}
    							else
    							{
    			    				addMissed.putExtra("Name", medications[0].getName());
    			    				addMissed.putExtra("Drug", medications[0].getDrug());								
    							}
    		            		startActivityForResult(addMissed, MISSED_ACTIVITY_REQUEST_CODE);    										
    						}
    					});
        			    builder.setCancelable(true);
        			    builder.show();   				
        				
        			}
        		}    			
    		}
			break;		
		case R.id.BackButton:
			getActivity().finish();
			break;
		}
	}
	
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent editMissed = new Intent(getActivity(), EditMissedActivity.class);
		editMissed.putExtra("isInEditMode", true);
		editMissed.putExtra("MissedIndex", id);
		startActivityForResult(editMissed, MISSED_ACTIVITY_REQUEST_CODE);
	}
	
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(MISSED_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(MISSED_LIST_LOADER, null, this);
    		}
    	}
    }
	
	
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, null, null, null, null);
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
	
	
    private class MissedCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;

    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public MissedCursorAdapter(Context context, int layout, Cursor cursor,
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
  	            view = inflater.inflate(R.layout.missedmed_list_item, null);
  	       }
  	       Cursor cursor = adapter.getCursor();
  	       cursor.moveToPosition(pos);
 	       String name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
 	       long dateValue = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MISSEDDATE));
  	       TextView missedView = (TextView)view.findViewById(R.id.missedMedName);
  	       missedView.setText(name);
  	       
  	       TextView missedDateView = (TextView)view.findViewById(R.id.missedMedDate);
  	       missedDateView.setText(DateFormat.format("dd MMM yyyy", dateValue));
  	       return (view);
       	}
    }

}

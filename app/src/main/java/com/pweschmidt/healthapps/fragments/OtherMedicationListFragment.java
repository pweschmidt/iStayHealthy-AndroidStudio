package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.AddOtherMedsActivity;
import com.pweschmidt.healthapps.ModifyOtherMedsActivity;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import android.app.Activity;
import android.content.Context;
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

public class OtherMedicationListFragment extends ListFragment 
 implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "ResultsListFragment";
	private static final int OTHERMEDS_LIST_LOADER = 0x01;
	protected static final int OTHERMEDS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFY_OTHERMEDS_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;

	private OtherMedicationsAdapter adapter;
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        adapter = new OtherMedicationsAdapter(
                getActivity().getApplicationContext(), 
                R.layout.othermed_list_item, 
                null, 
                new String[] {"StartDate","Name","Dose"}, 
                new int[] {R.id.otherMedStartDate,R.id.otherMedName,R.id.otherMedDosage});
    	getLoaderManager().initLoader(OTHERMEDS_LIST_LOADER, null, this);
        setListAdapter(adapter);    	
    }	

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.list_fragment_layout, container, false);
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.AddButton);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.OtherMeds);
    	titleText.setText(title);
    	
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
    		Intent addResult = new Intent(getActivity(), AddOtherMedsActivity.class);
    		startActivityForResult(addResult, OTHERMEDS_ACTIVITY_REQUEST_CODE);    		
    		break;
    	case R.id.BackButton:
    		getActivity().finish();
    		break;
    	}
		
	}
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, null, null, null, null);
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

	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		super.onListItemClick(list, view, position, id);
		Intent modifyIntent = new Intent(getActivity(), ModifyOtherMedsActivity.class);
		modifyIntent.putExtra("OtherMedsIndex", id);
		startActivityForResult(modifyIntent, MODIFY_OTHERMEDS_ACTIVITY_REQUEST_CODE);
	}  
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(OTHERMEDS_ACTIVITY_REQUEST_CODE == requestCode || 
    			MODIFY_OTHERMEDS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(OTHERMEDS_LIST_LOADER, null, this);
    		}
    	}
    }	

    private class OtherMedicationsAdapter extends SimpleCursorAdapter{
    	private Context _context;
    	
    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public OtherMedicationsAdapter(Context context, int layout, Cursor cursor,
    			String[] from, int[] to){
    		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		this._context = context;
     	}
    	
    	/**
    	 * 
    	 */
       	public View getView(int pos, View inView, ViewGroup parent) {
   	       View view = inView;
   	       if (null == view) {
   	            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   	            view = inflater.inflate(R.layout.othermed_list_item, null);
   	       }  
   	       Cursor cursor = adapter.getCursor();
   	       cursor.moveToPosition(pos);
   	       String medName = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
   	       int dose = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DOSE));
   	       String doseText = ""+dose+" [mg]";
   	       long dateValue = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
   	       
   	       
   	       TextView dateView = (TextView)view.findViewById(R.id.otherMedStartDate);
  	       dateView.setText(DateFormat.format("dd MMM yyyy", dateValue));
   	       
  	       TextView nameView = (TextView)view.findViewById(R.id.otherMedName);
  	       nameView.setText(medName);
  	       
  	       TextView dosage = (TextView)view.findViewById(R.id.otherMedDosage);
  	       dosage.setText(doseText);
   	       return (view);
       	}    	    	
    }    
    
}

package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.ModifyClinicActivity;
import com.pweschmidt.healthapps.AddClinicActivity;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsListFragment extends ListFragment 
	implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "ResultsListFragment";
	private static final int CONTACTS_LIST_LOADER = 0x01;
	protected static final int CONTACTS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYCONTACTS_ACTIVITY_REQUEST_CODE = 110;
	protected static final int BANNERVIEW_ACTIVITY_REQUEST_CODE = 120;
	private ClinicsListAdapter adapter;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        adapter = new ClinicsListAdapter(
                getActivity().getApplicationContext(), 
                R.layout.clinic_list_item, 
                null, 
                new String[] {"ClinicName"}, 
                new int[] {R.id.clinicName});
    	getLoaderManager().initLoader(CONTACTS_LIST_LOADER, null, this);
        setListAdapter(adapter);    	
    }	

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.list_fragment_layout, container, false);
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.AddButton);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.Clinics);
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
    		Intent addResult = new Intent(getActivity(), AddClinicActivity.class);
    		startActivityForResult(addResult, CONTACTS_ACTIVITY_REQUEST_CODE);    		
    		break;
    	case R.id.BackButton:
    		getActivity().finish();
    		break;
    	}
		
	}
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.CLINICS_CONTENT_URI, null, null, null, null);
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
		Intent modifyIntent = new Intent(getActivity(), ModifyClinicActivity.class);
		modifyIntent.putExtra("ContactsIndex", id);
		startActivityForResult(modifyIntent, MODIFYCONTACTS_ACTIVITY_REQUEST_CODE);		
	}  
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(MODIFYCONTACTS_ACTIVITY_REQUEST_CODE == requestCode || 
    			CONTACTS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(CONTACTS_LIST_LOADER, null, this);
    		}
    	}
    }	

	private class ClinicsListAdapter extends SimpleCursorAdapter{
    	private Context _context;
    	
    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public ClinicsListAdapter(Context context, int layout, Cursor cursor,
    			String[] from, int[] to){
    		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		this._context = context;
     	}
       	public View getView(int pos, View inView, ViewGroup parent) {
    	       View view = inView;
    	       if (null == view) {
    	            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	            view = inflater.inflate(R.layout.clinic_list_item, null);
    	       }  
    	       Cursor cursor = adapter.getCursor();
    	       cursor.moveToPosition(pos);
    	       String clinicName = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICNAME));
    	       if(clinicName.equals(""))
    	    	   clinicName = "No Name entered";
    	       TextView textView = (TextView)view.findViewById(R.id.clinicName);
    	       textView.setText(clinicName);    	       
    	       return (view);
       	}    	
    	
		
	}
    
}

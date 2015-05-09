package com.pweschmidt.healthapps.fragments;

import com.pweschmidt.healthapps.FileMaps;
import com.pweschmidt.healthapps.ModifyHIVDrugsActivity;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MedicationListFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>
{
	private static final int MEDS_LIST_LOADER = 0x01;
	protected static final int ADDMEDS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYMEDS_ACTIVITY_REQUEST_CODE = 110;
	protected MedicationImageCursorAdapter adapter;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	getLoaderManager().initLoader(MEDS_LIST_LOADER, null, this);
        adapter = new MedicationImageCursorAdapter(
        		getActivity().getApplicationContext(), 
                R.layout.med_list_item, 
                null, 
                new String[] {"StartDate","Name","Drug","MedicationForm"}, 
                new int[] {R.id.startDate,R.id.medName,R.id.drugContent,R.id.drugForms});
        setListAdapter(adapter);    	
    }	
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.currentmeds_fragment, container, false);
    	return view;
    }    
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(ADDMEDS_ACTIVITY_REQUEST_CODE == requestCode || 
    			MODIFYMEDS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(MEDS_LIST_LOADER, null, this);
    		}
    	}
    }

	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent modifyDrugs = new Intent(getActivity(), ModifyHIVDrugsActivity.class);
		modifyDrugs.putExtra("HIVMedicationIndex", id);
		startActivityForResult(modifyDrugs, MODIFYMEDS_ACTIVITY_REQUEST_CODE);
	}
    
    
    
    public void receivedResult(int requestCode, int resultCode)
    {
    	if(ADDMEDS_ACTIVITY_REQUEST_CODE == requestCode || 
    			MODIFYMEDS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(MEDS_LIST_LOADER, null, this);
    		}
    	}    	
    }
    
    
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
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
	
	
    private class MedicationImageCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;
    	
    	public MedicationImageCursorAdapter(Context context, int layout, Cursor cursor,
    			String[] from, int[] to) {
    		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    		this._context = context;
    	}
    	public View getView(int pos, View inView, ViewGroup parent) {
 	       View view = inView;
 	       if (null == view) {
 	            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 	            view = inflater.inflate(R.layout.med_list_item, null);
 	       }
 	       Cursor cursor = adapter.getCursor();
 	       cursor.moveToPosition(pos);
 	       String name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
 	       String drug = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG));
 	       String form = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MEDICATIONFORM));
 	       long dateValue = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
  	       String lowerCaseName = name.toLowerCase();
  	       String[] strings = lowerCaseName.split("/");
  	       int resId = R.drawable.combi;
  	       if(FileMaps.allMedImages.containsKey(strings[0]))
  	    	   resId = FileMaps.allMedImages.get(strings[0]).intValue();
 	       ImageView imageView = (ImageView)view.findViewById(R.id.medListImage);
 	       if( 0 != resId){
// 	    	   Log.d(TAG,"MedicationImageCursorAdapter::getView resId = "+resId);
 	    	   imageView.setImageResource(resId);
 	    	   imageView.setMaxHeight(55);
 	    	   imageView.setMaxWidth(55);
 	       }
 	       else{
// 	    	   Log.d(TAG,"MedicationImageCursorAdapter::getView resId is NULL"); 	    	   
 	       }
 	       TextView startDateView = (TextView)view.findViewById(R.id.startDate);
 	       startDateView.setText(DateFormat.format("dd MMM yyyy", dateValue));
 	       
 	       TextView nameView = (TextView)view.findViewById(R.id.medName);
 	       nameView.setText(name);
 	       
 	       TextView drugView = (TextView)view.findViewById(R.id.drugContent);
 	       drugView.setText(drug);
 	       
 	       TextView formView = (TextView)view.findViewById(R.id.drugForms);
 	       formView.setText(form);
 	       
 	       return(view);
    	}
    	
    	
    }	
}

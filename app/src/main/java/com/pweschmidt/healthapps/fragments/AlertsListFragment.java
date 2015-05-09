package com.pweschmidt.healthapps.fragments;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.pweschmidt.healthapps.AddAlarmNotificationActivity;
import com.pweschmidt.healthapps.ModifyAlarmNotificationActivity;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.WebViewActivity;
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

public class AlertsListFragment extends ListFragment 
	implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{

	protected static final int POZ_BANNER_ACTIVITY_REQUEST_CODE = 500;
//	private static final String TAG = "AlertsListFragment";
	private static final int ALERTS_LIST_LOADER = 0x01;
	protected static final int ALERTS_ACTIVITY_REQUEST_CODE = 100;
	protected static final int MODIFYALERTS_ACTIVITY_REQUEST_CODE = 110;
	private AlertsCursorAdapter adapter;

    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        adapter = new AlertsCursorAdapter(
                getActivity().getApplicationContext(), 
                R.layout.alert_list_item, 
                null, 
                new String[] {"AlertStartTime","AlertLabel"}, 
                new int[] {R.id.alertTimeView,R.id.alertLabelView});
    	getLoaderManager().initLoader(ALERTS_LIST_LOADER, null, this);
        setListAdapter(adapter);    	
    }
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View view = inflater.inflate(R.layout.alerts_fragment, container, false);
    	ImageButton addButton = (ImageButton)view.findViewById(R.id.TitleActionButton);
    	addButton.setImageResource(R.drawable.addselector);
    	addButton.setOnClickListener(this);
    	
    	TextView titleText = (TextView)view.findViewById(R.id.TitleMainTitle);
    	String title = getActivity().getApplicationContext().getResources().getString(R.string.Alerts);
    	titleText.setText(title);
    	    	
    	ImageButton pozButton = (ImageButton)view.findViewById(R.id.PozButton);
    	pozButton.setOnClickListener(this);
    	return view;
    }
	
    public Loader<Cursor>onCreateLoader(int id, Bundle args)
    {
    	CursorLoader loader = new CursorLoader(getActivity(), iStayHealthyContentProvider.ALERTS_CONTENT_URI, null, null, null, null);
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
    
    
	public void onClick(View view)
	{
    	int resID = view.getId();
    	switch(resID)
    	{
    	case R.id.TitleActionButton:
    		Intent addResult = new Intent(getActivity(), AddAlarmNotificationActivity.class);
    		startActivityForResult(addResult, ALERTS_ACTIVITY_REQUEST_CODE);    		
    		break;
    	case R.id.PozButton:
    		Intent bannerIntent = new Intent(getActivity(), WebViewActivity.class);
            String url = getResources().getString(R.string.bannerURL);
            bannerIntent.putExtra("url", url);    		
			startActivityForResult(bannerIntent,POZ_BANNER_ACTIVITY_REQUEST_CODE);
    		break;
    	}
		
	}
	
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		Intent modifyResults = new Intent(getActivity(), ModifyAlarmNotificationActivity.class);
		modifyResults.putExtra("AlertsIndex", id);
		startActivityForResult(modifyResults, MODIFYALERTS_ACTIVITY_REQUEST_CODE);
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(ALERTS_ACTIVITY_REQUEST_CODE == requestCode || 
    			MODIFYALERTS_ACTIVITY_REQUEST_CODE == requestCode)
    	{
    		if(Activity.RESULT_OK == resultCode)
    		{
    			getLoaderManager().restartLoader(ALERTS_LIST_LOADER, null, this);
    		}
    	}
    }
	
	
    private class AlertsCursorAdapter extends SimpleCursorAdapter{
    	private Context _context;

    	/**
    	 * 
    	 * @param context
    	 * @param layout
    	 * @param cursor
    	 * @param from
    	 * @param to
    	 */
    	public AlertsCursorAdapter(Context context, int layout, Cursor cursor,
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
  	            view = inflater.inflate(R.layout.alert_list_item, null);
  	       }
  	       Cursor cursor = adapter.getCursor();
  	       cursor.moveToPosition(pos);
  	       long time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTSTARTTIME));
  	       SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
  	       TextView timeView = (TextView)view.findViewById(R.id.alertTimeView);
  	       timeView.setText(simpleFormat.format(new java.util.Date(time)));
  	       
  	       String label = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTLABEL));
  	       TextView labelView = (TextView)view.findViewById(R.id.alertLabelView);
  	       labelView.setText(label);
  	       
  	       int repeat = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ALERTREPEATPATTERN));
  	       String repeatText = (0 < repeat) ? getResources().getString(R.string.recurring) :
  	    	   getResources().getString(R.string.unique);
  	       TextView repeatLabel = (TextView)view.findViewById(R.id.repeatTimeView);
  	       repeatLabel.setText(repeatText);
  	       return (view);
       	}
    }
	
}

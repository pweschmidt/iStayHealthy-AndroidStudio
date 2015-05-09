package com.pweschmidt.healthapps;

import com.pweschmidt.healthapps.datamodel.Contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.net.*;
import android.database.Cursor;
//import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//import android.util.*;

public class ModifyClinicActivity extends Activity implements View.OnClickListener 
{
	protected static final int WEBVIEW_ACTIVITY_REQUEST_CODE = 1120;
	
	private long rowId;
    private Contacts contacts;
	private Uri contactsUri;

	/**
	 * 
	 */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifyclinic);
        rowId = -1;    	
        
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.ModifyClinic);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);
        
        ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
        delete.setOnClickListener(this);
        
        Button call = (Button)findViewById(R.id.CallClinicButton);
        call.setOnClickListener(this);
        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
        {
        	call.setVisibility(View.GONE);
        }
        else
        {
        	call.setVisibility(View.VISIBLE);
        }
        
		//TODO: enable for later releases
//        Button emergency = (Button)findViewById(R.id.CallEmergencyButton);
//        emergency.setVisibility(View.GONE);
//        emergency.setOnClickListener(this);
        
        Button email = (Button)findViewById(R.id.EmailClinicButton);
        email.setOnClickListener(this);
        
        Button web = (Button)findViewById(R.id.ClinicWebButton);
        web.setOnClickListener(this);
        
        /*
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
			.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		*/
		//TODO: enable for later releases
//		EditText emText = (EditText)findViewById(R.id.ChangeEmergencyPhone);
//		emText.setVisibility(View.GONE);
        
    	Bundle extras = getIntent().getExtras();
    	if(null != extras)
    	{
    		rowId = extras.getLong("ContactsIndex");
    		contactsUri = Uri.withAppendedPath(iStayHealthyContentProvider.CLINICS_CONTENT_URI, String.valueOf(rowId));
    		Cursor contactsCursor = getContentResolver().query(contactsUri, null, null, null, null);
    		if(0 < contactsCursor.getCount() && contactsCursor.moveToFirst())
    		{
    			contacts = new Contacts();
    			contacts.setContacts(contactsCursor);
    		}
    		contactsCursor.close();
    		((EditText)findViewById(R.id.ChangeClinic)).setText(contacts.getClinicName());
    		((EditText)findViewById(R.id.ChangeClinicID)).setText(contacts.getClinicID());
    		((EditText)findViewById(R.id.ChangeClinicEmail)).setText(contacts.getClinicEmailAddress());
    		((EditText)findViewById(R.id.ChangeClinicWeb)).setText(contacts.getClinicWebSite());
    		((EditText)findViewById(R.id.ChangeClinicPhone)).setText(contacts.getClinicContactNumber());
    		((EditText)findViewById(R.id.ChangeConsultantName)).setText(contacts.getConsultantName());
//    		((EditText)findViewById(R.id.ChangeEmergencyPhone)).setText(contacts.getEmergencyContactNumber());
    	}
        
        
    }
    
    /**
     * 
     */
    public void onClick(View view)
    {
    	int resId = view.getId();
    	switch(resId)
    	{
    	case R.id.BackButton:
    		setResult(RESULT_CANCELED, null);
    		finish();
    		break;
    	case R.id.SaveButton:
    		if(-1 != rowId && null != contacts)
    		{   			
    			String clinic = (((EditText)findViewById(R.id.ChangeClinic)).getText()).toString().trim();
    			String clinicId = (((EditText)findViewById(R.id.ChangeClinicID)).getText()).toString().trim();
    			String email = (((EditText)findViewById(R.id.ChangeClinicEmail)).getText()).toString().trim();
    			String web = (((EditText)findViewById(R.id.ChangeClinicWeb)).getText()).toString().trim();
    			String phone = (((EditText)findViewById(R.id.ChangeClinicPhone)).getText()).toString().trim();
    			String consultant = (((EditText)findViewById(R.id.ChangeConsultantName)).getText()).toString().trim();
//    			String emergency = (((EditText)findViewById(R.id.ChangeEmergencyPhone)).getText()).toString().trim();    			
    			contacts.setClinicName(clinic);
    			contacts.setClinicID(clinicId);
    			contacts.setClinicEmailAddress(email);
    			contacts.setClinicWebSite(web);
    			contacts.setClinicContactNumber(phone);
    			contacts.setConsultantName(consultant);
//    			contacts.setEmergencyContactNumber(emergency);
    			getContentResolver().update(contactsUri, contacts.contentValuesForContacts(), null, null);
        		setResult(RESULT_OK, null);
    		}
    		setResult(RESULT_OK, null);
    		finish();
    		break;
    	case R.id.TrashButton:
    		AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
    		String yes = getResources().getString(R.string.Yes);
    		String no = getResources().getString(R.string.No);
    		String message = getResources().getString(R.string.deleteentry);
    		
    		builder.setMessage(message)
    	       .setCancelable(false)
    	       .setPositiveButton(yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	       		if(-1 != rowId && null != contacts )
    	       		{   
    	       			getContentResolver().delete(contactsUri, null, null);
    	    		}
    	       		ModifyClinicActivity.this.finish();
    	           }
    	       })
    	       .setNegativeButton(no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    		AlertDialog alert = builder.create();    	
    		alert.show();
    		
    		break;
    	case R.id.CallClinicButton:
			String phone = (((EditText)findViewById(R.id.ChangeClinicPhone)).getText()).toString().trim();
			if(!phone.equals("")){
				Intent callIntent = new Intent(Intent.ACTION_CALL);		
				String phoneNumber = new String("tel:"+phone);
				callIntent.setData(Uri.parse(phoneNumber));
				startActivity(callIntent);    						
			}
			break;
			/*
    	case R.id.CallEmergencyButton:
			String emergency = (((EditText)findViewById(R.id.ChangeEmergencyPhone)).getText()).toString().trim();    			
			if(!emergency.equals("")){
				Intent callIntent = new Intent(Intent.ACTION_CALL);		
				String phoneNumber = new String("tel:"+emergency);
				callIntent.setData(Uri.parse(phoneNumber));
				startActivity(callIntent);    						
			}
    		break;
    		*/
    	case R.id.EmailClinicButton:
			String email = (((EditText)findViewById(R.id.ChangeClinicEmail)).getText()).toString().trim();
			if(!email.equals("") && email.contains("@")){
	    		Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
	    		feedbackIntent.setType("plain/text");
	    		feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from iStayHealthy");
	    		feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});    		
	    		startActivity(Intent.createChooser(feedbackIntent, "Send email..."));				
			}
			else{
		        Toast error = Toast.makeText(this, "No valid email address", Toast.LENGTH_LONG);
		        error.show();
			}
    		break;
    	case R.id.ClinicWebButton:
			String web = (((EditText)findViewById(R.id.ChangeClinicWeb)).getText()).toString().trim();
			if(!web.equals("")){
				Intent bannerIntent = new Intent(this, WebViewActivity.class);				
		        bannerIntent.putExtra("url", web);
				startActivityForResult(bannerIntent,WEBVIEW_ACTIVITY_REQUEST_CODE);
			}
			else{
		        Toast error = Toast.makeText(this, "empty web string", Toast.LENGTH_LONG);
		        error.show();
			}
    		break;
    	}
    }
    
    /**
     * called when activity is paused - e.g. when we move to another one
     */
    public void onPause(){
    	super.onPause();
    }

    
	//monitor phone call activities
    /*
	private class PhoneCallListener extends PhoneStateListener {
 
		private boolean isPhoneCalling = false;
 
		String LOG_TAG = "LOGGING 123";
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
 
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");
 
				isPhoneCalling = true;
			}
 
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
 
				if (isPhoneCalling) {
 
					Log.i(LOG_TAG, "restart app");
 
					// restart app
					Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
 
					isPhoneCalling = false;
				}
 
			}
		}
	}
    */
    
}

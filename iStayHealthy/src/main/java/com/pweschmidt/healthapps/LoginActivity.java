package com.pweschmidt.healthapps;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.Intent;
//import android.content.pm.*;
public class LoginActivity extends Activity implements View.OnClickListener
{
	public static final int PASSWORD_REQUEST_CODE = 110;

	//	private final static String TAG = "LoginActivity";
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        Button goButton = (Button)findViewById(R.id.goButton);
        final TextView wrongPasswordText = (TextView)findViewById(R.id.wrongPasswordText);
        wrongPasswordText.setVisibility(View.GONE);
        goButton.setOnClickListener(this);
        
        Button forgot = (Button)findViewById(R.id.forgotPasswordButton);
        forgot.setOnClickListener(this);        
    }
	
	public void onClick(View view)
	{
		int resId = view.getId();
		if(R.id.goButton == resId)
		{
			EditText passwordField = (EditText)findViewById(R.id.passwordTextView);
			String password = (passwordField.getText()).toString().trim();
	        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
	        String test = (prefs.getString("password", "")).trim();
	        if(test.equals(""))
	        {
	            SharedPreferences.Editor editor = prefs.edit();
	            editor.putBoolean("isPasswordEnabled", false);
	            editor.commit();
	        	setResult(RESULT_OK);
	        	finish();
	        }
	        else
	        {
		        if(password.equals(test))
		        {
		            TextView wrongPasswordText = (TextView)findViewById(R.id.wrongPasswordText);
		            wrongPasswordText.setVisibility(View.GONE);
		        	setResult(RESULT_OK);
		        	finish();
		        }
		        else if(password.equalsIgnoreCase("D309C434-B512-455D-987F-DDED12425B55"))
		        {
		        	Toast toast = Toast.makeText(this, "Please reset your password", Toast.LENGTH_SHORT);
		        	toast.show();
		        	Intent tools = new Intent(this, SetPasswordActivity.class);
		        	tools.putExtra("resetpassword", true);
		        	startActivityForResult(tools, PASSWORD_REQUEST_CODE);
		        }
		        else
		        {
		            TextView wrongPasswordText = (TextView)findViewById(R.id.wrongPasswordText);
		            wrongPasswordText.setVisibility(View.VISIBLE);	        	
		            passwordField.setText("");
		        }	        	
	        }
	        
		}
		else if(R.id.forgotPasswordButton == resId)
		{
    		Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
    		feedbackIntent.setType("plain/text");
    		feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "I forgot my password");
    		feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"istayhealthy.app@gmail.com"});    		
    		startActivity(Intent.createChooser(feedbackIntent, "Send email..."));
		}
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//    	Log.d(TAG,"Return from Login Activity");
    	switch(requestCode)
    	{
    	case PASSWORD_REQUEST_CODE:
        	setResult(RESULT_OK);
        	finish();
    		break;
    	}
    }
	
	
	
	/**
	 * we don't allow the back button to be pressed from the Login Activity
	 */
	public void onBackPressed()
	{
//		Log.d(TAG,"we pressed the BACK key");
    	setResult(RESULT_CANCELED);		
    	finish();
	}
}

package com.pweschmidt.healthapps;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
//import android.util.Log;
//import android.text.*;

public class SetPasswordActivity extends Activity implements View.OnClickListener{
//	private static final String TAG = "SetPasswordActivity";
	private boolean isPasswordEnabled;
	
	/**
	 * 
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpassword);
        CheckBox box = (CheckBox)findViewById(R.id.PasswordCheckbox);
        box.setOnClickListener(this);
        
        ImageButton cancel = (ImageButton)findViewById(R.id.DoneButton);
        cancel.setOnClickListener(this);
        
//        ImageView firstTick = (ImageView)findViewById(R.id.firstCheckImage);
//        ImageView secondTick = (ImageView)findViewById(R.id.secondCheckImage);
        
        SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
        isPasswordEnabled = prefs.getBoolean("isPasswordEnabled", false);
        String password = prefs.getString("password", "");
		TableRow firstRow = (TableRow)findViewById(R.id.firstPasswordRow);
		TableRow secondRow = (TableRow)findViewById(R.id.secondPasswordRow);
		Button setButton = (Button)findViewById(R.id.setPasswordButton);
		setButton.setOnClickListener(this);
		if(isPasswordEnabled){
//			Log.d(TAG,"password is enabled");
			box.setChecked(true);
			firstRow.setVisibility(View.VISIBLE);
			secondRow.setVisibility(View.VISIBLE);
			setButton.setVisibility(View.VISIBLE);
	    	EditText firstEntry = (EditText)findViewById(R.id.FirstPasswordEntry);
//	    	firstEntry.addTextChangedListener(new CustomTextWatcher(firstEntry, firstTick));
	    	firstEntry.setText(password);
	    	EditText secondEntry = (EditText)findViewById(R.id.SecondPasswordEntry);
//	    	secondEntry.addTextChangedListener(new CustomTextWatcher(secondEntry, secondTick));
	    	secondEntry.setText(password);
		}
		else{
//			Log.d(TAG,"password is disabled");
			box.setChecked(false);
			firstRow.setVisibility(View.GONE);
			secondRow.setVisibility(View.GONE);
			setButton.setVisibility(View.GONE);
		}
        
        
    }	

    /**
     * 
     */
    public void onClick(View view){
    	int resId = view.getId();
    	switch(resId){
    	case R.id.DoneButton:
            setResult(Activity.RESULT_OK, null);                
    		finish();
    		break;
    	case R.id.PasswordCheckbox:
    		CheckBox box = (CheckBox)view;
    		TableRow firstRow = (TableRow)findViewById(R.id.firstPasswordRow);
    		TableRow secondRow = (TableRow)findViewById(R.id.secondPasswordRow);
    		Button setButton = (Button)findViewById(R.id.setPasswordButton);
    		if(box.isChecked()){
//    			Log.d(TAG,"check box is checked");
    			isPasswordEnabled = true;
    			firstRow.setVisibility(View.VISIBLE);
    			secondRow.setVisibility(View.VISIBLE);
    			setButton.setVisibility(View.VISIBLE);
    	        Toast toast = Toast.makeText(this, R.string.passwordenabled, Toast.LENGTH_LONG);
    	        toast.show();
    		}
    		else{
//    			Log.d(TAG,"check box is Unchecked");
    			isPasswordEnabled = false;
    			firstRow.setVisibility(View.GONE);
    			secondRow.setVisibility(View.GONE);
    			setButton.setVisibility(View.GONE);
    	        Toast toast = Toast.makeText(this, R.string.passworddisabled, Toast.LENGTH_LONG);
    	        toast.show();
    		}
            SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isPasswordEnabled", isPasswordEnabled);
            editor.commit();
    		break;
    	case R.id.setPasswordButton:
    		boolean isCorrect = wrapUp();
    		if(isCorrect){
                Toast msg = Toast.makeText(this, "Password Saved", Toast.LENGTH_LONG);
                msg.show();    			
                setResult(Activity.RESULT_OK, null);                
                finish();
    		}
    		break;
    	}
    }	
    
    /**
     * 
     */
    private boolean wrapUp(){
    	if(!isPasswordEnabled){
//    		Log.d(TAG,"wrapUp password is NOT enabled");
    		return false;
    	}
    	EditText firstEntry = (EditText)findViewById(R.id.FirstPasswordEntry);
    	EditText secondEntry = (EditText)findViewById(R.id.SecondPasswordEntry);
		String first = (firstEntry.getText()).toString().trim();
		String second = (secondEntry.getText()).toString().trim();
    	TextView feedbackView = (TextView)findViewById(R.id.passwordFeedback);
    	if(!first.equals(second)){
    		feedbackView.setText(R.string.IncorrectPassword);
    		firstEntry.setText("");
    		secondEntry.setText("");
    	}
    	else if(4 > first.length()){
    		feedbackView.setText(R.string.TooShortPassword);
    		firstEntry.setText("");
    		secondEntry.setText("");    		
    	}
    	
    	if(isPasswordEnabled && first.equals(second) && 4 <= first.length()){
//    		Log.d(TAG,"wrapUp password is enabled and confirmed");
            SharedPreferences prefs = getSharedPreferences(iStayHealthy.ISTAYHEALTHY_PREFS, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isPasswordEnabled", true);
            editor.putString("password", first);
            editor.commit();
            return true;
    	}
    	return false;
    }
    
 
    
}

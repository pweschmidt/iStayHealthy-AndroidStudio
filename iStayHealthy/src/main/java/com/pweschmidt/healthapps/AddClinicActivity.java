package com.pweschmidt.healthapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.pweschmidt.healthapps.datamodel.Contacts;

public class AddClinicActivity extends Activity implements View.OnClickListener
{

	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addclinic);
        
        ImageButton cancelButton = (ImageButton)findViewById(R.id.BackButton);
        cancelButton.setOnClickListener(this);

        TextView titleText = (TextView)findViewById(R.id.TitleMainTitle);
    	String title = getResources().getString(R.string.AddClinic);
    	titleText.setText(title);
        
        ImageButton saveButton = (ImageButton)findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(this);

        
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setOnClickListener(this);
    	delete.setVisibility(View.GONE);
        //TODO: enable for later releases
//        EditText emergency = (EditText)findViewById(R.id.EditEmergencyPhone);
//        emergency.setVisibility(View.GONE);
    }
	
	/**
	 * 
	 */
    public void onClick(View view)
    {
    	int resId = view.getId();
    	switch(resId){
    	case R.id.BackButton:
    		setResult(RESULT_CANCELED, null);
    		finish();
    		break;
    	case R.id.SaveButton:
    		if(containsData()){
    			String clinic = (((EditText)findViewById(R.id.EditClinic)).getText()).toString().trim();
    			String clinicId = (((EditText)findViewById(R.id.EditClinicID)).getText()).toString().trim();
    			String email = (((EditText)findViewById(R.id.EditClinicEmail)).getText()).toString().trim();
    			String web = (((EditText)findViewById(R.id.EditClinicWeb)).getText()).toString().trim();
    			String phone = (((EditText)findViewById(R.id.EditClinicPhone)).getText()).toString().trim();
    			String consultant = (((EditText)findViewById(R.id.EditConsultantName)).getText()).toString().trim();
//    			String emergency = (((EditText)findViewById(R.id.EditEmergencyPhone)).getText()).toString().trim();    			
	    		Contacts contacts = new Contacts();
    			contacts.setClinicName(clinic);
    			contacts.setClinicID(clinicId);
    			contacts.setClinicEmailAddress(email);
    			contacts.setClinicWebSite(web);
    			contacts.setClinicContactNumber(phone);
    			contacts.setConsultantName(consultant);
//    			contacts.setEmergencyContactNumber(emergency);
    			getContentResolver().insert(iStayHealthyContentProvider.CLINICS_CONTENT_URI, contacts.contentValuesForContacts());
    		}
    		setResult(RESULT_OK, null);
    		finish();
    		break;
    	}
    }
    
    /**
     * 
     * @return
     */
    private boolean containsData()
    {
		String clinic = (((EditText)findViewById(R.id.EditClinic)).getText()).toString().trim();
		String clinicId = (((EditText)findViewById(R.id.EditClinicID)).getText()).toString().trim();
		String email = (((EditText)findViewById(R.id.EditClinicEmail)).getText()).toString().trim();
		String web = (((EditText)findViewById(R.id.EditClinicWeb)).getText()).toString().trim();
		String phone = (((EditText)findViewById(R.id.EditClinicPhone)).getText()).toString().trim();
		String consultant = (((EditText)findViewById(R.id.EditConsultantName)).getText()).toString().trim();
//		String emergency = (((EditText)findViewById(R.id.EditEmergencyPhone)).getText()).toString().trim();
		if(clinic.equals("") && clinicId.equals("") && email.equals("") && web.equals("") &&
				phone.equals("") && consultant.equals(""))
			return false;
    	return true;
    }
    
}

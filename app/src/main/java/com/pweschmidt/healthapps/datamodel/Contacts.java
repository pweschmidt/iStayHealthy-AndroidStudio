package com.pweschmidt.healthapps.datamodel;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import android.content.ContentValues;
import android.database.Cursor;

public class Contacts {
	private String InsuranceID;
	private String InsuranceName;
	private String AppointmentContactNumber;
	private String ClinicCountry;
	private String EmergencyContactNumber;
	private String GUID;
	private String ClinicName;
	private String ClinicCity;
	private String ClinicPostcode;
	private String ClinicID;
	private String ConsultantName;
	private String InsuranceAuthorisationCode;
	private String EmergencyContactNumber2;
	private String ClinicNurseName;
	private String InsuranceContactNumber;
	private String ResultsContactNumber;
	private String ContactName;
	private String ClinicContactNumber;
	private String ClinicStreet;
	private String ClinicEmailAddress;
	private String ClinicWebSite;
	private String InsuranceWebSite;
	private String Tintabee;

	public Contacts(){
		InsuranceID = "";
		InsuranceName = "";
		AppointmentContactNumber = "";
		ClinicCountry = "";
		EmergencyContactNumber = "";
		ClinicName = "";
		ClinicCity = "";
		ClinicPostcode = "";
		ClinicID = "";
		ConsultantName = "";
		InsuranceAuthorisationCode = "";
		EmergencyContactNumber2 = "";
		ClinicNurseName = "";
		InsuranceContactNumber = "";
		ResultsContactNumber = "";
		ContactName = "";
		ClinicContactNumber = "";
		ClinicStreet = "";
		ClinicEmailAddress = "";
		ClinicWebSite = "";
		InsuranceWebSite = "";		
		GUID = java.util.UUID.randomUUID().toString();
		Tintabee="";
	}
	public String getInsuranceID(){return InsuranceID;}
	public String getInsuranceName(){return InsuranceName;}
	public String getAppointmentContactNumber(){return AppointmentContactNumber;}
	public String getClinicCountry(){return ClinicCountry;}
	public String getEmergencyContactNumber(){return EmergencyContactNumber;}
	public String getGUID(){return GUID;}
	public String getClinicName(){return ClinicName;}
	public String getClinicCity(){return ClinicCity;}
	public String getClinicPostcode(){return ClinicPostcode;}
	public String getClinicID(){return ClinicID;}
	public String getConsultantName(){return ConsultantName;}
	public String getInsuranceAuthorisationCode(){return InsuranceAuthorisationCode;}
	public String getEmergencyContactNumber2(){return EmergencyContactNumber2;}
	public String getClinicNurseName(){return ClinicNurseName;}
	public String getInsuranceContactNumber(){return InsuranceContactNumber;}
	public String getResultsContactNumber(){return ResultsContactNumber;}
	public String getContactName(){return ContactName;}
	public String getClinicContactNumber(){return ClinicContactNumber;}
	public String getClinicStreet(){return ClinicStreet;}
	public String getClinicEmailAddress(){return ClinicEmailAddress;}
	public String getClinicWebSite(){return ClinicWebSite;}
	public String getInsuranceWebSite(){return InsuranceWebSite;}

	
	public void setInsuranceID(String text){ InsuranceID = text;}
	public void setInsuranceName(String text){ InsuranceName = text;}
	public void setAppointmentContactNumber(String text){ AppointmentContactNumber = text;}
	public void setClinicCountry(String text){ ClinicCountry = text;}
	public void setEmergencyContactNumber(String text){ EmergencyContactNumber = text;}
	public void setGUID(String text){ GUID = text;}
	public void setClinicName(String text){ ClinicName = text;}
	public void setClinicCity(String text){ ClinicCity = text;}
	public void setClinicPostcode(String text){ ClinicPostcode = text;}
	public void setClinicID(String text){ ClinicID = text;}
	public void setConsultantName(String text){ ConsultantName = text;}
	public void setInsuranceAuthorisationCode(String text){ InsuranceAuthorisationCode = text;}
	public void setEmergencyContactNumber2(String text){ EmergencyContactNumber2 = text;}
	public void setClinicNurseName(String text){ ClinicNurseName = text;}
	public void setInsuranceContactNumber(String text){ InsuranceContactNumber = text;}
	public void setResultsContactNumber(String text){ ResultsContactNumber = text;}
	public void setContactName(String text){ ContactName = text;}
	public void setClinicContactNumber(String text){ ClinicContactNumber = text;}
	public void setClinicStreet(String text){ ClinicStreet = text;}
	public void setClinicEmailAddress(String text){ ClinicEmailAddress = text;}
	public void setClinicWebSite(String text){ ClinicWebSite = text;}
	public void setInsuranceWebSite(String text){ InsuranceWebSite = text;}

	public String getTintabee(){return Tintabee;}
	public void setTintabee(String text){Tintabee = text;}

	public ContentValues contentValuesForContacts()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.CLINICNAME, ClinicName);
		content.put(iStayHealthyDatabaseSchema.CLINICID, ClinicID);
		content.put(iStayHealthyDatabaseSchema.CONSULTANTNAME, ConsultantName);
		content.put(iStayHealthyDatabaseSchema.CLINICNURSENAME, ClinicNurseName);
		content.put(iStayHealthyDatabaseSchema.CONTACTNAME, ContactName);
		content.put(iStayHealthyDatabaseSchema.CLINICEMAILADDRESS, ClinicEmailAddress);
		content.put(iStayHealthyDatabaseSchema.CLINICWEBSITE, ClinicWebSite);
		content.put(iStayHealthyDatabaseSchema.CLINICSTREET, ClinicStreet);
		content.put(iStayHealthyDatabaseSchema.CLINICCITY, ClinicCity);
		content.put(iStayHealthyDatabaseSchema.CLINICPOSTCODE, ClinicPostcode);
		content.put(iStayHealthyDatabaseSchema.CLINICCOUNTRY, ClinicCountry);
		content.put(iStayHealthyDatabaseSchema.CLINICCONTACTNUMBER, ClinicContactNumber);
		content.put(iStayHealthyDatabaseSchema.EMERGENCYCONTACTNUMBER, EmergencyContactNumber);
		content.put(iStayHealthyDatabaseSchema.EMERGENCYCONTACTNUMBER2, EmergencyContactNumber2);
		content.put(iStayHealthyDatabaseSchema.RESULTSCONTACTNUMBER, ResultsContactNumber);
		content.put(iStayHealthyDatabaseSchema.APPOINTMENTCONTACTNUMBER, AppointmentContactNumber);
		content.put(iStayHealthyDatabaseSchema.INSURANCEID, InsuranceID);
		content.put(iStayHealthyDatabaseSchema.INSURANCENAME, InsuranceName);
		content.put(iStayHealthyDatabaseSchema.INSURANCEAUTHORISATIONCODE, InsuranceAuthorisationCode);
		content.put(iStayHealthyDatabaseSchema.INSURANCECONTACTNUMBER, InsuranceContactNumber);
		content.put(iStayHealthyDatabaseSchema.INSURANCEWEBSITE, InsuranceWebSite);		
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	public void setContacts(Cursor cursor)
	{
		ClinicName = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICNAME));
		ClinicID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICID));
		ConsultantName = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CONSULTANTNAME));
		ClinicEmailAddress = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICEMAILADDRESS));
		ClinicWebSite = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICWEBSITE));
		ClinicContactNumber = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CLINICCONTACTNUMBER));
		EmergencyContactNumber = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.EMERGENCYCONTACTNUMBER));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
		
	}
	
}

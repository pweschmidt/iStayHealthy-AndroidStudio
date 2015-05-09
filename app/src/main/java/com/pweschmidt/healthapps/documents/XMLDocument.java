package com.pweschmidt.healthapps.documents;

import com.pweschmidt.healthapps.*;
import com.pweschmidt.healthapps.datamodel.SideEffects;

import android.database.Cursor;
import android.content.*;
import java.util.*;

//import android.text.format.*;
import java.text.SimpleDateFormat;

//import android.util.Log;

public class XMLDocument 
{
//	private final static String TAG = "XMLDocument";
//	private String APOSTROPHE = "\"";
//	private String TAB = "\t";
//	private String NEWLINE = "\n";
//	private String NOTPRESENT = "n/a";
//	private final DateFormat dateformat = new DateFormat();
	private String PREAMBLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private XMLElement root;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
	private Context context;
	public XMLDocument(Context context)
	{
		this.context = context;
		root = new XMLElement("iStayHealthyRecord");
		root.addAttribute("UID", java.util.UUID.randomUUID().toString());
		root.addAttribute("dbVersion", ""+iStayHealthyDatabaseSchema.CURRENT_DATABASE_VERSION);
		root.addAttribute("fromDevice", "Android");
		root.addAttribute("fromDate", (new Date()).toString());

		createResults();
		createMedication();
		createMissedMedication();
		createSideEffects();
		createOtherMedication();
		createContacts();
		createIllness();	
		createPreviousMedication();
//		createWellness();
	}
	
	
	/**
	 * 
	 * @param adapter
	 */
	private void createResults()
	{
//		Log.d(TAG,"createResults");
		int level = 1;
		XMLElement results = new XMLElement("Results", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.RESULTS_CONTENT_URI, null, null, null, null);
//		Log.d(TAG,"createResults after getAllResultsInChronologicalOrder");
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		int count = 0;
//		Log.d(TAG,"createResults before while loop");
		while(cursor.isAfterLast() == false)
		{
//			Log.d(TAG,"createResults in while loop count = "+count);
			XMLElement result = new XMLElement("Result", level);
			long date = cursor.getLong(cursor.getColumnIndex("ResultsDate"));
			result.addAttribute("ResultsDate",dateFormat.format(new Date(date)));	
			if(0 < cursor.getInt(cursor.getColumnIndex("CD4")))
				result.addAttribute("CD4", ""+ cursor.getInt(cursor.getColumnIndex("CD4")));
			if(0.0  < cursor.getFloat(cursor.getColumnIndex("CD4Percent")))
				result.addAttribute("CD4Percent",""+cursor.getFloat(cursor.getColumnIndex("CD4Percent")));
			
			int vl = cursor.getInt(cursor.getColumnIndex("ViralLoad"));
			if( 0 < vl )
			{
				if ( 40 >= vl )
					result.addAttribute("ViralLoad", "undetectable");
				else
					result.addAttribute("ViralLoad",""+vl);				
			}
			int hepCvl = cursor.getInt(cursor.getColumnIndex("HepCViralLoad"));
			if( 0 < hepCvl )
			{
				if ( 40 >= hepCvl )
					result.addAttribute("HepCViralLoad", "undetectable");
				else
					result.addAttribute("HepCViralLoad",""+hepCvl);				
			}
			result.addAttribute("Glucose", ""+cursor.getFloat(cursor.getColumnIndex("Glucose")));
			result.addAttribute("LDL", ""+cursor.getFloat(cursor.getColumnIndex("LDL")));
			result.addAttribute("HDL", ""+cursor.getFloat(cursor.getColumnIndex("HDL")));
			result.addAttribute("Systole", ""+cursor.getInt(cursor.getColumnIndex("Systole")));
			result.addAttribute("Diastole", ""+cursor.getInt(cursor.getColumnIndex("Diastole")));
			result.addAttribute("HeartRate", ""+cursor.getInt(cursor.getColumnIndex("HeartRate")));
			result.addAttribute(""+iStayHealthyDatabaseSchema.WEIGHT, ""+cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WEIGHT)));
			result.addAttribute(""+iStayHealthyDatabaseSchema.PLATELETCOUNT, ""+cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.PLATELETCOUNT)));
			result.addAttribute(""+iStayHealthyDatabaseSchema.WHITECELLCOUNT, ""+cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WHITECELLCOUNT)));
			result.addAttribute(""+iStayHealthyDatabaseSchema.REDCELLCOUNT, ""+cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.REDCELLCOUNT)));
			result.addAttribute(""+iStayHealthyDatabaseSchema.HAEMOGLOBULIN, ""+cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HAEMOGLOBULIN)));
			result.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			results.addChild(result);
			count = count + 1;
			cursor.moveToNext();
		}		
		root.addChild(results);
//		Log.d(TAG,"createResults add result to parent element");
		cursor.close();
//		Log.d(TAG,"createResults close cursor");
	}

	/**
	 * 
	 * @param adapter
	 */
	private void createMedication()
	{
//		Log.d(TAG,"createMedication");
		int level = 1;
		XMLElement meds = new XMLElement("Medications", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.MEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement medication = new XMLElement("Medication",level);
			long date = cursor.getLong(cursor.getColumnIndex("StartDate"));
			medication.addAttribute("StartDate",dateFormat.format(new Date(date)));		
			medication.addAttribute("Name", cursor.getString(cursor.getColumnIndex("Name")));
			medication.addAttribute("Drug", cursor.getString(cursor.getColumnIndex("Drug")));
			medication.addAttribute("MedicationForm", cursor.getString(cursor.getColumnIndex("MedicationForm")));
			medication.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			meds.addChild(medication);
			cursor.moveToNext();
		}		
		level++;
		root.addChild(meds);
		cursor.close();
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void createMissedMedication()
	{
//		Log.d(TAG,"createMissedMedication");
		int level = 1;
		XMLElement missed = new XMLElement("MissedMedications", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement missedMed = new XMLElement("MissedMedication",level);
			long date = cursor.getLong(cursor.getColumnIndex("MissedDate"));
			missedMed.addAttribute("MissedDate",dateFormat.format(new Date(date)));		
			missedMed.addAttribute("Name", cursor.getString(cursor.getColumnIndex("Name")));
			missedMed.addAttribute("Drug", cursor.getString(cursor.getColumnIndex("Drug")));
			missedMed.addAttribute(""+iStayHealthyDatabaseSchema.MISSEDREASON, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MISSEDREASON)));
			missedMed.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			missed.addChild(missedMed);
			cursor.moveToNext();
		}
		root.addChild(missed);	
		cursor.close();
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void createSideEffects()
	{
//		Log.d(TAG,"createSideEffects");
		int level = 1;
		XMLElement effects = new XMLElement("HIVSideEffects", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement effect = new XMLElement("SideEffects",level);
			long date = cursor.getLong(cursor.getColumnIndex("SideEffectsDate"));
			effect.addAttribute("SideEffectsDate",dateFormat.format(new Date(date)));		
			effect.addAttribute("SideEffects", cursor.getString(cursor.getColumnIndex("SideEffects")));
			effect.addAttribute("Name", cursor.getString(cursor.getColumnIndex("Name")));
			effect.addAttribute("Drug", cursor.getString(cursor.getColumnIndex("Drug")));
			effect.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			String seriousness = "";
			int index = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS));
			if(SideEffects.MINOR == index)
				seriousness = "minor";
			else if (SideEffects.MAJOR == index)
				seriousness = "major";
			else
				seriousness = "serious";
						
			effect.addAttribute(""+iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS, seriousness);
			effects.addChild(effect);
			cursor.moveToNext();
		}

		root.addChild(effects);			
		cursor.close();
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void createOtherMedication()
	{
//		Log.d(TAG,"createOtherMedication");
		int level = 1;
		XMLElement other = new XMLElement("OtherMedications", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement otherMedication = new XMLElement("OtherMedication",level);
			otherMedication.addAttribute("Dose", ""+ cursor.getInt(cursor.getColumnIndex("Dose")));
			long date = cursor.getLong(cursor.getColumnIndex("StartDate"));
			otherMedication.addAttribute("StartDate",dateFormat.format(new Date(date)));		
			otherMedication.addAttribute("Name", cursor.getString(cursor.getColumnIndex("Name")));
			otherMedication.addAttribute("Drug", cursor.getString(cursor.getColumnIndex("Drug")));
			otherMedication.addAttribute("MedicationForm", cursor.getString(cursor.getColumnIndex("MedicationForm")));
			otherMedication.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			other.addChild(otherMedication);
			cursor.moveToNext();
		}
		root.addChild(other);
		cursor.close();
	}
	
	
	/**
	 * 
	 * @param adapter
	 */
	private void createContacts()
	{
//		Log.d(TAG,"createContacts");
		int level = 1;
		XMLElement contacts = new XMLElement("ClinicalContacts", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.CLINICS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement contact = new XMLElement("Contacts",level);
			contact.addAttribute("ClinicName", cursor.getString(cursor.getColumnIndex("ClinicName")));
			contact.addAttribute("ClinicID", cursor.getString(cursor.getColumnIndex("ClinicID")));
			contact.addAttribute("ConsultantName", cursor.getString(cursor.getColumnIndex("ConsultantName")));
			contact.addAttribute("ClinicNurseName", cursor.getString(cursor.getColumnIndex("ClinicNurseName")));
			contact.addAttribute("ContactName", cursor.getString(cursor.getColumnIndex("ContactName")));
			contact.addAttribute("ClinicEmailAddress", cursor.getString(cursor.getColumnIndex("ClinicEmailAddress")));
			contact.addAttribute("ClinicWebSite", cursor.getString(cursor.getColumnIndex("ClinicWebSite")));
			contact.addAttribute("ClinicStreet", cursor.getString(cursor.getColumnIndex("ClinicStreet")));
			contact.addAttribute("ClinicCity", cursor.getString(cursor.getColumnIndex("ClinicCity")));
			contact.addAttribute("ClinicPostcode", cursor.getString(cursor.getColumnIndex("ClinicPostcode")));
			contact.addAttribute("ClinicCountry", cursor.getString(cursor.getColumnIndex("ClinicCountry")));
			contact.addAttribute("ClinicContactNumber", cursor.getString(cursor.getColumnIndex("ClinicContactNumber")));
			contact.addAttribute("EmergencyContactNumber2", cursor.getString(cursor.getColumnIndex("EmergencyContactNumber2")));
			contact.addAttribute("EmergencyContactNumber", cursor.getString(cursor.getColumnIndex("EmergencyContactNumber")));
			contact.addAttribute("ResultsContactNumber", cursor.getString(cursor.getColumnIndex("ResultsContactNumber")));
			contact.addAttribute("AppointmentContactNumber", cursor.getString(cursor.getColumnIndex("AppointmentContactNumber")));
			contact.addAttribute("InsuranceID", cursor.getString(cursor.getColumnIndex("InsuranceID")));
			contact.addAttribute("InsuranceName", cursor.getString(cursor.getColumnIndex("InsuranceName")));
			contact.addAttribute("InsuranceAuthorisationCode", cursor.getString(cursor.getColumnIndex("InsuranceAuthorisationCode")));
			contact.addAttribute("InsuranceContactNumber", cursor.getString(cursor.getColumnIndex("InsuranceContactNumber")));
			contact.addAttribute("InsuranceWebSite", cursor.getString(cursor.getColumnIndex("InsuranceWebSite")));			
			contact.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			contacts.addChild(contact);
			cursor.moveToNext();			
		}		
		
		root.addChild(contacts);
		cursor.close();
	}
	
	/**
	 * 
	 * @param adapter
	 */
	private void createIllness()
	{
//		Log.d(TAG,"createIllness");
		int level = 1;
		XMLElement procs = new XMLElement("IllnessesAndProcedures", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.PROCEDURES_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement proc = new XMLElement("Procedures",level);
			long date = cursor.getLong(cursor.getColumnIndex("Date"));
			proc.addAttribute("Date",dateFormat.format(new Date(date)));		
			proc.addAttribute("Name", cursor.getString(cursor.getColumnIndex("Name")));
			proc.addAttribute("Illness", cursor.getString(cursor.getColumnIndex("Illness")));
			proc.addAttribute("CausedBy", cursor.getString(cursor.getColumnIndex("CausedBy")));
			proc.addAttribute("Notes", cursor.getString(cursor.getColumnIndex("Notes")));
			proc.addAttribute("GUID", cursor.getString(cursor.getColumnIndex("GUID")));
			procs.addChild(proc);
			cursor.moveToNext();			
		}		
		
		root.addChild(procs);											
		cursor.close();
	}
	
	private void createPreviousMedication()
	{
		int level = 1;
		XMLElement previousTable = new XMLElement("PreviousMedications", level);
		Cursor cursor = context.getContentResolver().query(iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, null, null, null, null);
		if(0 == cursor.getCount() || !cursor.moveToFirst())
		{
			cursor.close();
			return;
		}
		level++;
		while(cursor.isAfterLast() == false)
		{
			XMLElement previousMed = new XMLElement("PreviousMedication", level);
			long start = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
			long end = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ENDDATE));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.STARTDATE, dateFormat.format(new Date(start)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.ENDDATE, dateFormat.format(new Date(end)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.NAME, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.DRUG, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.REASONENDED, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.REASONENDED)));
			int isART = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ISART));
			if(0 < isART)
				previousMed.addAttribute(iStayHealthyDatabaseSchema.ISART, "true");
			else
				previousMed.addAttribute(iStayHealthyDatabaseSchema.ISART, "false");
			previousMed.addAttribute(iStayHealthyDatabaseSchema.GUIDTEXT, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT)));
			previousTable.addChild(previousMed);
			cursor.moveToNext();
		}
		root.addChild(previousTable);
		cursor.close();
	}
	
	/**
	 * 
	private void createWellness(){
		int level = 1;
		XMLElement previousTable = new XMLElement("Wellnesses", level);
		Cursor cursor = adapter.getAllWellness();
		if(0 == cursor.getCount()){
			cursor.close();
			return;
		}
		cursor.moveToFirst();
		level++;
		while(cursor.isAfterLast() == false){
			XMLElement previousMed = new XMLElement("Wellness", level);
			long start = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.RECORDEDDATE));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.STARTDATE, dateFormat.format(new Date(start)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.SLEEPBAROMETER, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SLEEPBAROMETER)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.MOODBAROMETER, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MOODBAROMETER)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.WELLNESSBAROMETER, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WELLNESSBAROMETER)));
			previousMed.addAttribute(iStayHealthyDatabaseSchema.GUIDTEXT, cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT)));
			previousTable.addChild(previousMed);
			cursor.moveToNext();
		}
		root.addChild(previousTable);
		cursor.close();
		
	}
	 */
	

	/**
	 * 
	 * @return
	 */
	public String toXMLString(){
//		Log.d(TAG,"toXMLString");
		StringBuffer xmlBuffer = new StringBuffer(PREAMBLE);
		xmlBuffer.append(root.toXMLString());
		return xmlBuffer.toString();
	}
}

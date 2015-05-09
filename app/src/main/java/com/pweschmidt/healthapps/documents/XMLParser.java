package com.pweschmidt.healthapps.documents;

import com.pweschmidt.healthapps.datamodel.*;
import java.net.URL;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.pweschmidt.healthapps.*;
import android.content.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class XMLParser 
{
//	private static final String TAG = "XMLParser";
	public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
	private Context context;
	private byte[] xmlData;
	private URL url;
	private boolean isTintabee;
	Document doc;
	
	/**
	 * 
	 * @param url
	 * @param context
	 */
	public XMLParser(URL url, Context context)
	{
		this.context = context;
		this.url = url;
		xmlData = null;
		isTintabee = false;
	}
	
	/**
	 * 
	 * @param xmlBytes
	 * @param context
	 */
	public XMLParser(byte[] xmlBytes, Context context)
	{
		this.url = null;
//		Log.d(TAG,"We are about to read in the bytes "+xmlBytes.length);
		this.xmlData = xmlBytes;
		this.context = context;
		isTintabee = false;
	}
	
	/**
	 * 
	 * @param xmlBytes
	 * @param context
	 * @param checkIfTintabee
	 */
	public XMLParser(byte[] xmlBytes, Context context, boolean checkIfTintabee)
	{
		this.url = null;
		this.xmlData = xmlBytes;
		this.context = context;
		isTintabee = checkIfTintabee;
	}
	
	
	public void parse()
	{
		try
		{
//			Log.d(TAG,"We are about to parse xml data");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			if( null != url )
				doc = db.parse(new InputSource(url.openStream()));
			else{
				doc = db.parse(new ByteArrayInputStream(xmlData));				
			}
			doc.getDocumentElement().normalize();
			createResults();
			createMedication();
			createMissedMedication();
			createOtherMedication();
			createSideEffects();
			createContacts();
			createProcedures();	
			createPreviousMedication();
			createWellness();
		}
		catch(Exception e){}
	}
	
	
	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createResults()throws Exception
	{
		if(null == doc){
			return;
		}
		NodeList results = getNodes(doc,"Result");
//		Log.d(TAG,"parse: we have "+results.getLength()+" results");
		for(int i = 0 ; i < results.getLength(); ++i){
//			Log.d(TAG,"Result "+i);
			Results result = new Results();
			Element element = (Element)results.item(i);
			String guid = getAttributeValue(element, "GUID");
			if(null != guid){
				result.setGUID(guid);
			}
			String resultsDate = getAttributeValue(element, "ResultsDate");
			if(!resultsDate.equals("")){
				Date date = dateFormat.parse(resultsDate);
//				Log.d(TAG,"Date = "+resultsDate);
				result.setTime(date.getTime());										
			}
			else
			{
				result.setTime((new Date()).getTime());
			}
			String cd4 = getAttributeValue(element, "CD4");
			if(!cd4.equals("")){
//				Log.d(TAG,"CD4 count = "+cd4);
				result.setCD4Count(Integer.parseInt(cd4));
			}
			String cd4Percent = getAttributeValue(element, "CD4Percent");
			if(!cd4Percent.equals("")){
//				Log.d(TAG,"CD4 percent = "+cd4Percent);
				result.setCD4Percent(Float.parseFloat(cd4Percent));
			}
			String vl = getAttributeValue(element, "ViralLoad");
			if(!vl.equals("")){
//				Log.d(TAG,"Viral Load = "+vl);
				if(vl.equalsIgnoreCase("undetectable"))
					result.setViralLoad(10);
				else if(vl.startsWith("undetectable"))
				{
//					Log.d(TAG,"Viral Load = "+vl+" and we recognised it as undetectable");
					result.setViralLoad(10);
				}
				else{
					if(isTintabee){
						float value = Float.parseFloat(vl);
						result.setViralLoad((int)value);
					}
					else{
						result.setViralLoad(Integer.parseInt(vl));						
					}
				}
			}
//			Log.d(TAG,"after vl");
			String hepCVl = getAttributeValue(element, "HepCViralLoad");
			if(!hepCVl.equals("")){
				if(hepCVl.equalsIgnoreCase("undetectable"))
					result.setHepCViralLoad(10);
				else{
					if(isTintabee){
						float value = Float.parseFloat(hepCVl);
						result.setHepCViralLoad((int)value);
					}
					else{
						result.setHepCViralLoad(Integer.parseInt(hepCVl));										
					}
				}
			}
			String glucose = getAttributeValue(element, "Glucose");
			if(!glucose.equals("")){
//				Log.d(TAG,"Glucose = "+glucose);
				result.setGlucose(Float.parseFloat(glucose));
			}
			String ldl = getAttributeValue(element, "LDL");
			if(!ldl.equals("")){
//				Log.d(TAG,"LDL  = "+ldl);
				result.setLDL(Float.parseFloat(ldl));
			}
			String hdl = getAttributeValue(element, "HDL");
			if(!hdl.equals("")){
//				Log.d(TAG,"HDL  = "+hdl);
				result.setHDL(Float.parseFloat(hdl));
			}
			String systole = getAttributeValue(element, "Systole");
			if(!systole.equals("")){
				result.setSystole(Integer.parseInt(systole));
//				Log.d(TAG,"Systole = "+systole);
			}
			String diastole = getAttributeValue(element, "Diastole");
			if(!diastole.equals("")){
				result.setDiastole(Integer.parseInt(diastole));
//				Log.d(TAG,"Diastole = "+diastole);
			}
			String rate = getAttributeValue(element, "HeartRate");
			if(!rate.equals("")){
				result.setHeartRate(Integer.parseInt(rate));
//				Log.d(TAG,"HeartRate = "+rate);
			}
//			Log.d(TAG,"after heartRate");
			
			String weight = getAttributeValue(element, iStayHealthyDatabaseSchema.WEIGHT);
			if(!weight.equals("")){
				result.setWeight(Float.parseFloat(weight));
			}

			String haemo = getAttributeValue(element, iStayHealthyDatabaseSchema.HAEMOGLOBULIN);
			if(!haemo.equals(""))
			{
				if(haemo.contains("."))
				{
					float value = Float.parseFloat(haemo);
					result.setHaemoglobulin((int)value);
				}
				else
				{
					result.setHaemoglobulin(Integer.parseInt(haemo));					
				}
			}
			String platelets = getAttributeValue(element, iStayHealthyDatabaseSchema.PLATELETCOUNT);
			if(!platelets.equals(""))
			{
				if(platelets.contains("."))
				{
					float value = Float.parseFloat(platelets);
					result.setPlateletCount((int)value);
				}
				else
				{
					result.setPlateletCount(Integer.parseInt(platelets));					
				}
			}
			String redCells = getAttributeValue(element, iStayHealthyDatabaseSchema.REDCELLCOUNT);
			if(!redCells.equals(""))
			{
				if(redCells.contains("."))
				{
					float value = Float.parseFloat(redCells);
					result.setRedCellCount((int)value);
				}
				else
				{
					result.setRedCellCount(Integer.parseInt(redCells));					
				}
			}
//			Log.d(TAG,"after redcellcount");
			String whitecells = getAttributeValue(element, iStayHealthyDatabaseSchema.WHITECELLCOUNT);
			if(!whitecells.equals(""))
			{
				if(whitecells.contains("."))
				{
					float value = Float.parseFloat(whitecells);
					result.setWhiteCellCount((int)value);
				}
				else
				{
					result.setWhiteCellCount(Integer.parseInt(whitecells));					
				}
			}	
//			Log.d(TAG,"WRITING RESULT TO SQL DATABASE");
			ContentValues values = result.contentValuesForResult();
			context.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, values);
//			android.net.Uri uri = context.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, values);
//			Log.d(TAG,"in the XML parsing we added the row to the URI = "+uri.getPath());
		}
	}
	
	
	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList meds = getNodes(doc,"Medication");
		for(int i = 0 ; i < meds.getLength(); ++i){
			Element element = (Element)meds.item(i);
			Medication med = new Medication();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				med.setGUID(guid);
			}
			String startDate = getAttributeValue(element, "StartDate");
			if(!startDate.equals("")){
				Date date = dateFormat.parse(startDate);
				med.setTime(date.getTime());					
			}
			String name = getAttributeValue(element, "Name");
			if(!name.equals("")){
				med.setName(name);
			}
			String drug = getAttributeValue(element, "Drug");
			if(!drug.equals("")){
				med.setDrug(drug);
			}
			String form = getAttributeValue(element, "MedicationForm");
			if(!form.equals("")){
				med.setMedicationForm(form);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.MEDS_CONTENT_URI, med.contentValuesForMedication());
		}
	}
	
	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createMissedMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList misseds = getNodes(doc, "MissedMedication");
		for(int i = 0 ; i < misseds.getLength(); ++i){
			Element element = (Element)misseds.item(i);
			MissedMedication missed = new MissedMedication();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				missed.setGUID(guid);
			}
			String missedDate = getAttributeValue(element, "MissedDate");
			if(!missedDate.equals("")){
				Date date = dateFormat.parse(missedDate);
				missed.setTime(date.getTime());
			}
			String name = getAttributeValue(element, "Name");
			if(!name.equals("")){
				missed.setName(name);
			}
			String drug = getAttributeValue(element, "Drug");
			if(!drug.equals("")){
				missed.setDrug(drug);
			}
			
			String reason = getAttributeValue(element, iStayHealthyDatabaseSchema.MISSEDREASON);
			if(!reason.equals(""))
				missed.setMissedReason(reason);
			
			context.getContentResolver().insert(iStayHealthyContentProvider.MISSEDMEDS_CONTENT_URI, missed.contentValuesForMedication());
		}
	}

	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createOtherMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList otherMeds = getNodes(doc,"OtherMedication");
		for(int i = 0 ; i < otherMeds.getLength(); ++i){
			Element element = (Element)otherMeds.item(i);
			OtherMedication other = new OtherMedication();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				other.setGUID(guid);
			}
			String startDate = getAttributeValue(element, "StartDate");
			if(!startDate.equals("")){
				Date date = dateFormat.parse(startDate);
				other.setTime(date.getTime());
			}
			String name = getAttributeValue(element, "Name");
			if(!name.equals("")){
				other.setName(name);
			}
			String drug = getAttributeValue(element, "Drug");
			if(!drug.equals("")){
				other.setDrug(drug);
			}
			
			String dose = getAttributeValue(element, "Dose");
			if(!dose.equals(""))
			{
				other.setDose(Integer.parseInt(dose));
			}
			
			String unit = getAttributeValue(element, "Unit");
					
			
			String form = getAttributeValue(element, "MedicationForm");
			if(!form.equals("")){
				other.setMedicationForm(form);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, other.contentValuesForMedication());
		}
	}
	
	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createSideEffects()throws Exception{
		if(null == doc){
			return;
		}
		NodeList effects = getNodes(doc, "SideEffects");
		for(int i = 0 ; i < effects.getLength(); ++i){
			Element element = (Element)effects.item(i);
			SideEffects effect = new SideEffects();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				effect.setGUID(guid);
			}
			String effectsDate = getAttributeValue(element, "SideEffectsDate");
			if(!effectsDate.equals("")){
				Date date = dateFormat.parse(effectsDate);
				effect.setTime(date.getTime());
			}
			String name = getAttributeValue(element, "Name");
			if(!name.equals("")){
				effect.setName(name);
			}
			String drug = getAttributeValue(element, "Drug");
			if(!drug.equals("")){
				effect.setDrug(drug);
			}
			String effectsText = getAttributeValue(element, "SideEffects");
			if(!effectsText.equals("")){
				effect.setSideEffects(effectsText);
			}
			
			String seriousness = getAttributeValue( element, iStayHealthyDatabaseSchema.SIDEEFFECTSERIOUSNESS);
			if(!seriousness.equals("")){
				if(seriousness.equals("minor"))
					effect.setSeriousness(SideEffects.MINOR);
				else if(seriousness.equals("major"))
					effect.setSeriousness(SideEffects.MAJOR);
				else if(seriousness.equals("serious"))
					effect.setSeriousness(SideEffects.SERIOUS);
				else
					effect.setSeriousness(SideEffects.MINOR);					
			}
			
			context.getContentResolver().insert(iStayHealthyContentProvider.EFFECTS_CONTENT_URI, effect.contentValuesForMedication());
		}
	}
	
	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createContacts()throws Exception{
		if(null == doc){
			return;
		}
		NodeList contacts = getNodes(doc, "Contacts");
		for(int i = 0 ; i < contacts.getLength(); ++i){
			Element element = (Element)contacts.item(i);
			Contacts contact = new Contacts();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				contact.setGUID(guid);
			}
			String key = getAttributeValue(element,"key");
			if(!key.equals("")){
				contact.setTintabee(key);
			}
			String clinicName = getAttributeValue(element,"ClinicName");
			if(!clinicName.equals("")){
				contact.setClinicName(clinicName);
			}
			String clinicId = getAttributeValue(element,"ClinicID");
			if(!clinicId.equals("")){
				contact.setClinicID(clinicId);
			}
			String consultantName = getAttributeValue(element,"ConsultantName");
			if(!consultantName.equals("")){
				contact.setConsultantName(consultantName);
			}
			String clinicNurseName = getAttributeValue(element,"ClinicNurseName");
			if(!clinicNurseName.equals("")){
				contact.setClinicNurseName(clinicNurseName);
			}
			String contactName = getAttributeValue(element,"ContactName");
			if(!contactName.equals("")){
				contact.setContactName(contactName);
			}
			String email = getAttributeValue(element,"ClinicEmailAddress");
			if(!email.equals("")){
				contact.setClinicEmailAddress(email);
			}
			String web = getAttributeValue(element,"ClinicWebSite");		
			if(!web.equals("")){
				contact.setClinicWebSite(web);
			}
			String street = getAttributeValue(element,"ClinicStreet");
			if(!street.equals("")){
				contact.setClinicStreet(street);
			}
			String city = getAttributeValue(element,"ClinicCity");
			if(!city.equals("")){
				contact.setClinicCity(city);
			}
			String zip = getAttributeValue(element,"ClinicPostcode");
			if(!zip.equals("")){
				contact.setClinicPostcode(zip);
			}
			String country = getAttributeValue(element,"ClinicCountry");
			if(!country.equals("")){
				contact.setClinicCountry(country);
			}
			String number = getAttributeValue(element,"ClinicContactNumber");
			if(!number.equals("")){
				contact.setClinicContactNumber(number);
			}
			String emergency2 = getAttributeValue(element,"EmergencyContactNumber2");
			if(!emergency2.equals("")){
				contact.setEmergencyContactNumber2(emergency2);
			}
			String emergency = getAttributeValue(element,"EmergencyContactNumber");
			if(!emergency.equals("")){
				contact.setEmergencyContactNumber(emergency);
			}
			String number2 = getAttributeValue(element,"ResultsContactNumber");
			if(!number2.equals("")){
				contact.setResultsContactNumber(number2);
			}
			String number3 = getAttributeValue(element,"AppointmentContactNumber");
			if(!number3.equals("")){
				contact.setAppointmentContactNumber(number3);
			}
			String insuranceID = getAttributeValue(element,"InsuranceID");
			if(!insuranceID.equals("")){
				contact.setInsuranceID(insuranceID);
			}
			String insurance = getAttributeValue(element,"InsuranceName");
			if(!insurance.equals("")){
				contact.setInsuranceName(insurance);
			}
			String code = getAttributeValue(element,"InsuranceAuthorisationCode");
			if(!code.equals("")){
				contact.setInsuranceAuthorisationCode(code);
			}
			String insuranceNumber = getAttributeValue(element,"InsuranceContactNumber");
			if(!insuranceNumber.equals("")){
				contact.setInsuranceContactNumber(insuranceNumber);
			}
			String insuranceWeb = getAttributeValue(element,"InsuranceWebSite");
			if(!insuranceWeb.equals("")){
				contact.setInsuranceWebSite(insuranceWeb);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.CLINICS_CONTENT_URI, contact.contentValuesForContacts());
		}
	}

	/**
	 * 
	 * @param adapter
	 * @param doc
	 */
	private void createProcedures()throws Exception{
		if(null == doc){
			return;
		}
		NodeList procedures = getNodes(doc, "Procedures");
		for(int i = 0 ; i < procedures.getLength(); ++i){
			Element element = (Element)procedures.item(i);
			Procedures procs = new Procedures();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				procs.setGUID(guid);
			}
			String procdate = getAttributeValue(element, "Date");
			if(!procdate.equals(""))
			{
				Date date = dateFormat.parse(procdate);
				procs.setTime(date.getTime());
			}
			String name = getAttributeValue(element, "Name");
			if(!name.equals(""))
			{
				procs.setName(name);
			}
			String illness = getAttributeValue(element, "Illness");
			if(!illness.equals("")){
				procs.setIllness(illness);
			}
			String caused = getAttributeValue(element, "CausedBy");
			if(!caused.equals("")){
				procs.setCausedBy(caused);
			}			
			String notes = getAttributeValue(element, "Notes");
			if(!notes.equals("")){
				procs.setNotes(notes);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.PROCEDURES_CONTENT_URI, procs.contentValuesForProcedures());
		}
	}
	
	private void createPreviousMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList procedures = getNodes(doc, "PreviousMedications");
		for(int i = 0 ; i < procedures.getLength(); ++i){
			Element element = (Element)procedures.item(i);
			PreviousMedication previous = new PreviousMedication();
			String guid = getAttributeValue(element, "GUID");
			if(!guid.equals("")){
				previous.setGUID(guid);
			}
						
			String start = getAttributeValue(element, iStayHealthyDatabaseSchema.STARTDATE);
			if(!start.equals("")){
				Date date = dateFormat.parse(start);
				previous.setTime(date.getTime());					
			}
			
			String end = getAttributeValue(element, iStayHealthyDatabaseSchema.ENDDATE);
			if(!end.equals("")){
				Date date = dateFormat.parse(end);
				previous.setEndDate(date.getTime());					
			}
			
			
			String name = getAttributeValue(element, "Name");
			if(!name.equals("")){
				previous.setName(name);
			}
			String drug = getAttributeValue(element, iStayHealthyDatabaseSchema.DRUG);
			if(!drug.equals("")){
				previous.setDrug(drug);
			}
			
			String reason = getAttributeValue( element, iStayHealthyDatabaseSchema.REASONENDED);
			if(!reason.equals("")){
				previous.setReasonEnded(reason);
			}
			
			context.getContentResolver().insert(iStayHealthyContentProvider.PREVIOUSMEDS_CONTENT_URI, previous.contentValuesForMedication());
		}
		
	}
	
	private void createWellness()throws Exception{
		
	}
	
	/**
	 * for Tintabee elements we need to take care of lowercase
	 * @param name
	 * @return
	 */
	private NodeList getNodes(Document doc, String name){
		NodeList nodes = doc.getElementsByTagName(name);
		if(nodes.getLength() == 0){
			nodes =  doc.getElementsByTagName(convertToLowerString(name));			
		}
		if(nodes.getLength() == 0){
			nodes = doc.getElementsByTagName(name.toLowerCase());
		}
		return nodes;
	}
	
	/**
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	private String getAttributeValue(Element element, String name){
//		Log.d(TAG,"getting Attribute Values for "+element.getNodeName()+" with attribute "+name);
		String value = "";		
		if(element.hasAttribute(name)){
//			Log.d(TAG,"we have an attribute with name "+name);
			value = element.getAttribute(name);
			if(null == value)value = "";
			return value;
		}
		String alternativeName = TintabeeXMLMap.xmlMap.get(name);
//		Log.d(TAG,"looking up in the TintabeeXMLMap table and the name is "+alternativeName);
		if(null != alternativeName){
			if(element.hasAttribute(alternativeName)){
//				Log.d(TAG,"we have an alternative attribute with name "+alternativeName);
				value = element.getAttribute(alternativeName);
				if(null == value)value = "";
				return value;				
			}
		}
		value = "";
		return value;
	}
	
	/**
	 * replaces the first letter to lowercase to satisfy Tintabee XML format
	 * @param name
	 * @return
	 */
	private String convertToLowerString(String name){
		StringBuilder builder = new StringBuilder(name);
		builder.replace(0, 1, name.toLowerCase());
		return builder.toString();
	}
	
}

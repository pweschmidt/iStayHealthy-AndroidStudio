package com.pweschmidt.healthapps.documents;

import android.content.ContentValues;
import android.content.Context;

import com.pweschmidt.healthapps.datamodel.Contacts;
import com.pweschmidt.healthapps.datamodel.Medication;
import com.pweschmidt.healthapps.datamodel.MissedMedication;
import com.pweschmidt.healthapps.datamodel.OtherMedication;
import com.pweschmidt.healthapps.datamodel.PreviousMedication;
import com.pweschmidt.healthapps.datamodel.Procedures;
import com.pweschmidt.healthapps.datamodel.Results;
import com.pweschmidt.healthapps.datamodel.SideEffects;
import com.pweschmidt.healthapps.iStayHealthyContentProvider;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser 
{
//	private static final String TAG = "XMLParser";
	public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
	private Context context;
	private byte[] xmlData;
	private URL url;
	private boolean isTintabee;
	Document doc;
	
//	public XMLParser(URL url, Context context)
//	{
//		this.context = context;
//		this.url = url;
//		xmlData = null;
//		isTintabee = false;
//	}
	
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
	
//	public XMLParser(byte[] xmlBytes, Context context, boolean checkIfTintabee)
//	{
//		this.url = null;
//		this.xmlData = xmlBytes;
//		this.context = context;
//		isTintabee = checkIfTintabee;
//	}
	
	
	public void parse(XMLErrorHandler errorHandler)throws SAXParseException
	{
		try
		{
//			Log.d(TAG,"We are about to parse xml data of content "+xmlData.length);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(errorHandler);
                doc = db.parse(new ByteArrayInputStream(xmlData));
//            if( null != url )
//				doc = db.parse(new InputSource(url.openStream()));
//			else{
//			}
//                Log.d(TAG,"We are AFTER parsing the data set");
                doc.getDocumentElement().normalize();
//            Log.d(TAG,"We are AFTER normalising the data set");
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
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(null != guid){
				result.setGUID(guid);
			}
			String resultsDate = getAttributeValue(element, iStayHealthyDatabaseSchema.RESULTSDATE);
			if(!resultsDate.equals("")){
				long date = getDateFromDateString(resultsDate);
//				Log.d(TAG,"Date = "+resultsDate);
				result.setTime(date);
			}
			else
			{
				result.setTime((new Date()).getTime());
			}
			String cd4 = getAttributeValue(element, iStayHealthyDatabaseSchema.CD4);
			if(!cd4.equals("")){
//				Log.d(TAG,"CD4 count = "+cd4);
                int iCD4;
                iCD4 = getIntegerFromString(cd4);
                result.setCD4Count(iCD4);
			}
			String cd4Percent = getAttributeValue(element, iStayHealthyDatabaseSchema.CD4PERCENT);
			if(!cd4Percent.equals("")){
//				Log.d(TAG,"CD4 percent = "+cd4Percent);
                float fCD4;
                fCD4 = getFloatFromString(cd4Percent);
				result.setCD4Percent(fCD4);
			}
			String vl = getAttributeValue(element, iStayHealthyDatabaseSchema.VIRALLOAD);
			if(!vl.equals("")){
                int iVL;
                iVL = getIntegerFromString(vl);
                result.setViralLoad(iVL);
//                Log.d(TAG,"Viral Load = "+vl+ " integer value = "+iVL);
//				if(vl.equalsIgnoreCase("undetectable"))
//					result.setViralLoad(10);
//				else if(vl.startsWith("undetectable"))
//				{
//					Log.d(TAG,"Viral Load = "+vl+" and we recognised it as undetectable");
//					result.setViralLoad(10);
//				}
//				else{
//                  int iVL;
//                    iVL = getIntegerFromString(vl);
//                    result.setViralLoad(iVL);
//                }
			}
//			Log.d(TAG,"after vl");
			String hepCVl = getAttributeValue(element, iStayHealthyDatabaseSchema.HEPCVIRALLOAD);
			if(!hepCVl.equals(""))
            {
                int iVL;
                iVL = getIntegerFromString(hepCVl);
                result.setHepCViralLoad(iVL);
			}
			String glucose = getAttributeValue(element, iStayHealthyDatabaseSchema.GLUCOSE);
			if(!glucose.equals("")){
//				Log.d(TAG,"Glucose = "+glucose);
                float fGlucose;
                fGlucose = getFloatFromString(glucose);
				result.setGlucose(fGlucose);
			}
			String ldl = getAttributeValue(element, iStayHealthyDatabaseSchema.LDL);
			if(!ldl.equals("")){
//				Log.d(TAG,"LDL  = "+ldl);
                float fLDL;
                fLDL = getFloatFromString(ldl);
				result.setLDL(fLDL);
			}
			String hdl = getAttributeValue(element, iStayHealthyDatabaseSchema.HDL);
			if(!hdl.equals("")){
//				Log.d(TAG,"HDL  = "+hdl);
                float fHDL;
                fHDL = getFloatFromString(hdl);
				result.setHDL(fHDL);
			}
			String systole = getAttributeValue(element, iStayHealthyDatabaseSchema.SYSTOLE);
			if(!systole.equals("")){
                int iSystole;
                iSystole = getIntegerFromString(systole);
				result.setSystole(iSystole);
//				Log.d(TAG,"Systole = "+systole);
			}
			String diastole = getAttributeValue(element, iStayHealthyDatabaseSchema.DIASTOLE);
			if(!diastole.equals("")){
                int iDiastole;
                iDiastole = getIntegerFromString(diastole);
				result.setDiastole(iDiastole);
//				Log.d(TAG,"Diastole = "+diastole);
			}
			String rate = getAttributeValue(element, iStayHealthyDatabaseSchema.HEARTRATE);
			if(!rate.equals("")){
                int iHR;
                iHR = getIntegerFromString(rate);
				result.setHeartRate(iHR);
//				Log.d(TAG,"HeartRate = "+rate);
			}
//			Log.d(TAG,"after heartRate");
			
			String weight = getAttributeValue(element, iStayHealthyDatabaseSchema.WEIGHT);
			if(!weight.equals("")){
                float fW;
                fW = getFloatFromString(weight);
				result.setWeight(fW);
			}

			String haemo = getAttributeValue(element, iStayHealthyDatabaseSchema.HAEMOGLOBULIN);
			if(!haemo.equals(""))
			{
                int iHaemo;
                iHaemo = getIntegerFromString(haemo);
                result.setHaemoglobulin(iHaemo);
			}
			String platelets = getAttributeValue(element, iStayHealthyDatabaseSchema.PLATELETCOUNT);
			if(!platelets.equals(""))
			{
                int fP = getIntegerFromString(platelets);
                result.setPlateletCount(fP);
			}
			String redCells = getAttributeValue(element, iStayHealthyDatabaseSchema.REDCELLCOUNT);
			if(!redCells.equals(""))
			{
                int rC = getIntegerFromString(redCells);
                result.setRedCellCount(rC);
			}
//			Log.d(TAG,"after redcellcount");
			String whitecells = getAttributeValue(element, iStayHealthyDatabaseSchema.WHITECELLCOUNT);
			if(!whitecells.equals(""))
			{
                int wC = getIntegerFromString(whitecells);
                result.setWhiteCellCount(wC);
			}
//			Log.d(TAG,"WRITING RESULT TO SQL DATABASE");
			ContentValues values = result.contentValuesForResult();
//			context.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, values);
			android.net.Uri uri = context.getContentResolver().insert(iStayHealthyContentProvider.RESULTS_CONTENT_URI, values);
//			Log.d(TAG,"in the XML parsing we added the row to the URI = "+uri.getPath());
		}
	}
	
	
	/**
	 * 
	 */
	private void createMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList meds = getNodes(doc,"Medication");
//        Log.d(TAG,"parse: we have "+meds.getLength()+" meds");
		for(int i = 0 ; i < meds.getLength(); ++i){
			Element element = (Element)meds.item(i);
			Medication med = new Medication();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				med.setGUID(guid);
			}
			String startDate = getAttributeValue(element, iStayHealthyDatabaseSchema.STARTDATE);
			if(!startDate.equals("")){
				long date = getDateFromDateString(startDate);
				med.setTime(date);
			}
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
			if(!name.equals("")){
//                Log.d(TAG,"name ="+name);
				med.setName(name);
			}
			String drug = getAttributeValue(element, iStayHealthyDatabaseSchema.DRUG);
			if(!drug.equals("")){
//                Log.d(TAG,"drug ="+drug);
				med.setDrug(drug);
			}
			String form = getAttributeValue(element, iStayHealthyDatabaseSchema.MEDICATIONFORM);
			if(!form.equals("")){
				med.setMedicationForm(form);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.MEDS_CONTENT_URI, med.contentValuesForMedication());
		}
	}
	
	/**
	 * 
	 */
	private void createMissedMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList misseds = getNodes(doc, "MissedMedication");
//        Log.d(TAG,"parse: we have "+misseds.getLength()+" missed");
		for(int i = 0 ; i < misseds.getLength(); ++i){
			Element element = (Element)misseds.item(i);
			MissedMedication missed = new MissedMedication();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				missed.setGUID(guid);
			}
			String missedDate = getAttributeValue(element, iStayHealthyDatabaseSchema.MISSEDDATE);
			if(!missedDate.equals("")){
				long date = getDateFromDateString(missedDate);
				missed.setTime(date);
			}
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
			if(!name.equals("")){
				missed.setName(name);
			}
			String drug = getAttributeValue(element, iStayHealthyDatabaseSchema.DRUG);
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
	 */
	private void createOtherMedication()throws Exception{
		if(null == doc){
			return;
		}
		NodeList otherMeds = getNodes(doc,"OtherMedication");
		for(int i = 0 ; i < otherMeds.getLength(); ++i){
			Element element = (Element)otherMeds.item(i);
			OtherMedication other = new OtherMedication();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				other.setGUID(guid);
			}
			String startDate = getAttributeValue(element, iStayHealthyDatabaseSchema.STARTDATE);
			if(!startDate.equals("")){
				long date = getDateFromDateString(startDate);
				other.setTime(date);
			}
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
			if(!name.equals("")){
//                Log.d(TAG,"name ="+name);
				other.setName(name);
			}
			String drug = getAttributeValue(element, iStayHealthyDatabaseSchema.DRUG);
			if(!drug.equals("")){
 //               Log.d(TAG,"drug ="+drug);
				other.setDrug(drug);
			}
			
			String dose = getAttributeValue(element, iStayHealthyDatabaseSchema.DOSE);
			if(!dose.equals(""))
			{
				other.setDose(Integer.parseInt(dose));
			}
			
			String unit = getAttributeValue(element, "Unit");
					
			
			String form = getAttributeValue(element, iStayHealthyDatabaseSchema.MEDICATIONFORM);
			if(!form.equals("")){
				other.setMedicationForm(form);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.OTHERMEDS_CONTENT_URI, other.contentValuesForMedication());
		}
	}
	
	/**
	 * 
	 */
	private void createSideEffects()throws Exception{
		if(null == doc){
			return;
		}
		NodeList effects = getNodes(doc, "SideEffects");
		for(int i = 0 ; i < effects.getLength(); ++i){
			Element element = (Element)effects.item(i);
			SideEffects effect = new SideEffects();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				effect.setGUID(guid);
			}
			String effectsDate = getAttributeValue(element, iStayHealthyDatabaseSchema.SIDEEFFECTSDATE);
			if(!effectsDate.equals("")){
				long date = getDateFromDateString(effectsDate);
				effect.setTime(date);
			}
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
			if(!name.equals("")){
				effect.setName(name);
			}
			String drug = getAttributeValue(element, iStayHealthyDatabaseSchema.DRUG);
			if(!drug.equals("")){
				effect.setDrug(drug);
			}
			String effectsText = getAttributeValue(element, iStayHealthyDatabaseSchema.SIDEEFFECTS);
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
	 */
	private void createContacts()throws Exception{
		if(null == doc){
			return;
		}
		NodeList contacts = getNodes(doc, "Contacts");
		for(int i = 0 ; i < contacts.getLength(); ++i){
			Element element = (Element)contacts.item(i);
			Contacts contact = new Contacts();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				contact.setGUID(guid);
			}
			String clinicName = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICNAME);
			if(!clinicName.equals("")){
				contact.setClinicName(clinicName);
			}
			String clinicId = getAttributeValue(element, iStayHealthyDatabaseSchema.CLINICID);
			if(!clinicId.equals("")){
				contact.setClinicID(clinicId);
			}
			String consultantName = getAttributeValue(element,iStayHealthyDatabaseSchema.CONSULTANTNAME);
			if(!consultantName.equals("")){
				contact.setConsultantName(consultantName);
			}
			String clinicNurseName = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICNURSENAME);
			if(!clinicNurseName.equals("")){
				contact.setClinicNurseName(clinicNurseName);
			}
			String contactName = getAttributeValue(element,iStayHealthyDatabaseSchema.CONTACTNAME);
			if(!contactName.equals("")){
				contact.setContactName(contactName);
			}
			String email = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICEMAILADDRESS);
			if(!email.equals("")){
				contact.setClinicEmailAddress(email);
			}
			String web = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICWEBSITE);
			if(!web.equals("")){
				contact.setClinicWebSite(web);
			}
			String street = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICSTREET);
			if(!street.equals("")){
				contact.setClinicStreet(street);
			}
			String city = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICCITY);
			if(!city.equals("")){
				contact.setClinicCity(city);
			}
			String zip = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICPOSTCODE);
			if(!zip.equals("")){
				contact.setClinicPostcode(zip);
			}
			String country = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICCOUNTRY);
			if(!country.equals("")){
				contact.setClinicCountry(country);
			}
			String number = getAttributeValue(element,iStayHealthyDatabaseSchema.CLINICCONTACTNUMBER);
			if(!number.equals("")){
				contact.setClinicContactNumber(number);
			}
			String emergency2 = getAttributeValue(element,iStayHealthyDatabaseSchema.EMERGENCYCONTACTNUMBER2);
			if(!emergency2.equals("")){
				contact.setEmergencyContactNumber2(emergency2);
			}
			String emergency = getAttributeValue(element,iStayHealthyDatabaseSchema.EMERGENCYCONTACTNUMBER);
			if(!emergency.equals("")){
				contact.setEmergencyContactNumber(emergency);
			}
			String number2 = getAttributeValue(element,iStayHealthyDatabaseSchema.RESULTSCONTACTNUMBER);
			if(!number2.equals("")){
				contact.setResultsContactNumber(number2);
			}
			String number3 = getAttributeValue(element,iStayHealthyDatabaseSchema.APPOINTMENTCONTACTNUMBER);
			if(!number3.equals("")){
				contact.setAppointmentContactNumber(number3);
			}
			String insuranceID = getAttributeValue(element,iStayHealthyDatabaseSchema.INSURANCEID);
			if(!insuranceID.equals("")){
				contact.setInsuranceID(insuranceID);
			}
			String insurance = getAttributeValue(element,iStayHealthyDatabaseSchema.INSURANCENAME);
			if(!insurance.equals("")){
				contact.setInsuranceName(insurance);
			}
			String code = getAttributeValue(element,iStayHealthyDatabaseSchema.INSURANCEAUTHORISATIONCODE);
			if(!code.equals("")){
				contact.setInsuranceAuthorisationCode(code);
			}
			String insuranceNumber = getAttributeValue(element,iStayHealthyDatabaseSchema.INSURANCECONTACTNUMBER);
			if(!insuranceNumber.equals("")){
				contact.setInsuranceContactNumber(insuranceNumber);
			}
			String insuranceWeb = getAttributeValue(element,iStayHealthyDatabaseSchema.INSURANCEWEBSITE);
			if(!insuranceWeb.equals("")){
				contact.setInsuranceWebSite(insuranceWeb);
			}
			context.getContentResolver().insert(iStayHealthyContentProvider.CLINICS_CONTENT_URI, contact.contentValuesForContacts());
		}
	}

	/**
	 * 
	 */
	private void createProcedures()throws Exception{
		if(null == doc){
			return;
		}
		NodeList procedures = getNodes(doc, "Procedures");
		for(int i = 0 ; i < procedures.getLength(); ++i){
			Element element = (Element)procedures.item(i);
			Procedures procs = new Procedures();
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				procs.setGUID(guid);
			}
			String procdate = getAttributeValue(element, iStayHealthyDatabaseSchema.DATE);
			if(!procdate.equals(""))
			{
				long date = getDateFromDateString(procdate);
				procs.setTime(date);
			}
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
			if(!name.equals(""))
			{
				procs.setName(name);
			}
			String illness = getAttributeValue(element, iStayHealthyDatabaseSchema.ILLNESS);
			if(!illness.equals("")){
				procs.setIllness(illness);
			}
			String caused = getAttributeValue(element, iStayHealthyDatabaseSchema.CAUSEDBY);
			if(!caused.equals("")){
				procs.setCausedBy(caused);
			}			
			String notes = getAttributeValue(element, iStayHealthyDatabaseSchema.NOTES);
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
			String guid = getAttributeValue(element, iStayHealthyDatabaseSchema.GUIDTEXT);
			if(!guid.equals("")){
				previous.setGUID(guid);
			}
						
			String start = getAttributeValue(element, iStayHealthyDatabaseSchema.STARTDATE);
			if(!start.equals("")){
				long date = getDateFromDateString(start);
				previous.setTime(date);
			}
			
			String end = getAttributeValue(element, iStayHealthyDatabaseSchema.ENDDATE);
			if(!end.equals("")){
				long date = getDateFromDateString(end);
				previous.setEndDate(date);
			}
			
			
			String name = getAttributeValue(element, iStayHealthyDatabaseSchema.NAME);
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

    private int getIntegerFromString(String value)
    {
        if (value.contains("undetectable"))
        {
            return 10;
        }
        String regex = "[0-9]+";
        boolean matchesNumber = value.matches(regex);
        int iValue = 0;
        if (matchesNumber)
        {
            try
            {
                iValue = Integer.parseInt(value);
            }
            catch (NumberFormatException ne)
            {
                return 0;
            }
        }
        else
        {
            float fValue = getFloatFromString(value);
            iValue = (int)fValue;
        }
        return iValue;
    }

    private float getFloatFromString(String value)
    {
        String regex = "[-+]?([0-9]*\\.[0-9]+|[0-9]+)";
        boolean matchesNumber = value.matches(regex);
        float valueFloat = 0;
        if (matchesNumber)
        {
            try
            {
                valueFloat = Float.parseFloat(value);
            }catch(NumberFormatException ne)
            {
                return 0;
            }

        }
        return valueFloat;

    }

    private long getDateFromDateString(String dateValue)
    {
        Date date = new Date();
        try
        {
            date = dateFormat.parse(dateValue);
        }
        catch(ParseException ie)
        {
            return new Date().getTime();
        }

        return date.getTime();
    }
}

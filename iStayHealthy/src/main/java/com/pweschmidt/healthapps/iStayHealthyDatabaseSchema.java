package com.pweschmidt.healthapps;

public class iStayHealthyDatabaseSchema {
	public static final String DATABASE_NAME = "iStayHealthyAndroid.sqlite";
	public static final int    _MISSING_Table_DATABASE_VERSION = 14;//version 2.0.0 - misses main table
	public static final int    _V2_0_1_DATABASE_VERSION = 15;//version 2.0.1
	public static final int	   _V2_0_8_DATABASE_VERSION = 16;//version up to 2.0.8
	public static final int    CURRENT_DATABASE_VERSION = 17;//version 3.0.0 ///NEW
    ///////////////////////////////////////////////////////////////////////////
    //                      Data Table Definitions		                     //
    ///////////////////////////////////////////////////////////////////////////
	public static final String MAINRECORDTABLE = "ISTAYHEALTHYRECORD";
//	private static final String MAINID = "MainID";
//	private static final String RECORD = "Record";
	public static final String MEDICATIONTABLE = "MEDICATION";
	public static final String MISSEDMEDICATIONTABLE = "MISSEDMEDICATION";	
	public static final String OTHERMEDICATIONTABLE = "OTHERMEDICATION";	
	public static final String SIDEEFFECTSTABLE = "SIDEEFFECTS";
	public static final String RESULTSTABLE = "Results";	
	public static final String ALERTSTABLE = "ALERTS";
	public static final String CHARTDATATABLE = "CHARTDATA";
	public static final String CONTACTSTABLE = "CONTACTS";
	public static final String PROCEDURESTABLE = "PROCEDURES";
	public static final String PREVIOUSMEDSTABLE = "PreviousMedication";//NEW
	public static final String WELLNESSTABLE = "Wellness";//NEW

    ///////////////////////////////////////////////////////////////////////////
    //                      Data Table Columns Definitions                   //
    ///////////////////////////////////////////////////////////////////////////
	public static final String KEY_ID = "_id";
	public static final String NAME = "Name";
	public static final String MEDICATIONFORM = "MedicationForm";
	public static final String DRUG = "Drug";
	public static final String ENDDATE = "EndDate";
	public static final String STARTDATE = "StartDate";
	public static final String DATE = "Date";
	public static final String DOSE = "Dose";
	public static final String MISSEDDATE = "MissedDate";
	public static final String MISSEDREASON = "MissedReason";///2.0.7
	public static final String IMAGE = "Image";
	public static final String CD4 = "CD4";
	public static final String RESULTSDATE = "ResultsDate";
	public static final String VIRALLOAD = "ViralLoad";
	public static final String CD4PERCENT = "CD4Percent";
	public static final String HEPCVIRALLOAD = "HepCViralLoad";
	public static final String GLUCOSE = "Glucose";
	public static final String TOTALCHOLESTEROL = "TotalCholesterol";
	public static final String LDL = "LDL";
	public static final String HDL = "HDL";
	public static final String TRIGYLCERIDE = "Triglyceride";
	public static final String HEARTRATE = "HeartRate";
	public static final String SYSTOLE = "Systole";
	public static final String DIASTOLE = "Diastole";
	public static final String WEIGHT = "Weight";
	public static final String OXYGENLEVEL = "OxygenLevel";
	public static final String PLATELETCOUNT = "PlateletCount";///2.0.7
	public static final String HAEMOGLOBULIN = "Haemoglobulin";///2.0.7
	public static final String WHITECELLCOUNT = "WhiteCellCount";///2.0.7
	public static final String REDCELLCOUNT = "RedCellCount";///2.0.7
	public static final String CHOLESTEROLRATIO = "cholesterolRatio";//2.1.0
	public static final String CARDIACRISKFACTOR = "cardiacRiskFactor";//2.1.0
	public static final String HEPBTITER = "hepBTiter";//2.1.0
	public static final String HEPCTITER = "hepCTiter";//2.1.0
	public static final String liverAlanineTransaminase = "liverAlanineTransaminase";//2.1.0
	public static final String liverAspartateTransaminase = "liverAspartateTransaminase";//2.1.0
	public static final String liverAlkalinePhosphatase = "liverAlkalinePhosphatase";//2.1.0
	public static final String liverAlbumin = "liverAlbumin";//2.1.0
	public static final String liverAlanineTotalBilirubin = "liverAlanineTotalBilirubin";//2.1.0
	public static final String liverAlanineDirectBilirubin = "liverAlanineDirectBilirubin";//2.1.0
	public static final String liverGammaGlutamylTranspeptidase = "liverGammaGlutamylTranspeptidase";//2.1.0
	
	
	public static final String ALERTSTARTTIME = "AlertStartTime";
	public static final String ALERTLABEL = "AlertLabel";
	public static final String ALERTSOUND = "AlertSound";
	public static final String ALERTREPEATPATTERN = "AlertRepeatPattern";
	public static final String ALERTTEXT = "AlertText";
	public static final String ALERTHOUR = "AlertHour";
	public static final String AlERTMINUTE = "AlertMinute";
	public static final String ALERTTIMEZONEOFFSET = "AlertTimezoneOffset";
	public static final String ALERTREQUESTCODE = "AlertRequestCode";///2.0.7
	
	public static final String GUIDTEXT = "GUID";
	public static final String TINTABEEKEY = "Tintabee";
	public static final String SIDEEFFECTS = "SideEffects";
	public static final String SIDEEFFECTSDATE = "SideEffectsDate";
	public static final String SIDEEFFECTSERIOUSNESS = "Seriousness";///2.0.7
	
	public static final String TIMESTAMP = "TimeStap";
	public static final String DATAFLAG = "DataFlag";
	public static final String DATAROWID = "DataRowID";
	public static final String LASTMODIFIED = "lastModified";
	public static final String PASSWORD = "password";
	public static final String ISPASSWORDENABLED = "isPasswordEnabled";
	
	public static final String ILLNESS = "Illness";
	public static final String CAUSEDBY = "CausedBy";
	public static final String NOTES = "Notes";

	public static final String CLINICNAME = "ClinicName";
	public static final String CLINICID = "ClinicID";	
	public static final String CONSULTANTNAME = "ConsultantName";
	public static final String CLINICNURSENAME = "ClinicNurseName";
	public static final String CONTACTNAME = "ContactName";
	public static final String CLINICEMAILADDRESS = "ClinicEmailAddress";
	public static final String CLINICWEBSITE = "ClinicWebSite";
	public static final String CLINICSTREET = "ClinicStreet";
	public static final String CLINICCITY = "ClinicCity";
	public static final String CLINICPOSTCODE = "ClinicPostcode";
	public static final String CLINICCOUNTRY = "ClinicCountry";
	public static final String CLINICCONTACTNUMBER = "ClinicContactNumber";
	public static final String EMERGENCYCONTACTNUMBER2 = "EmergencyContactNumber2";
	public static final String EMERGENCYCONTACTNUMBER = "EmergencyContactNumber";
	public static final String RESULTSCONTACTNUMBER = "ResultsContactNumber";
	public static final String APPOINTMENTCONTACTNUMBER = "AppointmentContactNumber";
	public static final String INSURANCEID = "InsuranceID";
	public static final String INSURANCENAME = "InsuranceName";
	public static final String INSURANCEAUTHORISATIONCODE = "InsuranceAuthorisationCode";
	public static final String INSURANCECONTACTNUMBER = "InsuranceContactNumber";
	public static final String INSURANCEWEBSITE = "InsuranceWebSite";

	public static final String ISART = "isART";//2.1.0
	public static final String RECORDEDDATE = "recordedDate";//2.1.0
	public static final String REASONENDED = "reasonEnded";//2.1.0
	public static final String SLEEPBAROMETER = "sleepBarometer";//2.1.0
	public static final String MOODBAROMETER = "moodBarometer";//2.1.0
	public static final String WELLNESSBAROMETER = "wellnessBarometer";//2.1.0
	
	public static final String ISDIABETIC = "isDiabetic";//2.1.0
	public static final String ISSMOKER = "isSmoker";//2.1.0
	public static final String GENDER = "gender";//2.1.0
	public static final String DOB = "yearOfBirth";//2.1.0
	
	
    ///////////////////////////////////////////////////////////////////////////
    //                      SQL Create Table Strings                         //
    ///////////////////////////////////////////////////////////////////////////
		
	
    ///////////////////////////////////////////////////////////////////////////
    //                      Contacts Table			                         //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATECONTACTSTABLE = "create table "+CONTACTSTABLE +
	" ("+KEY_ID+ " integer primary key autoincrement, "+CLINICNAME+" text, "+CLINICID+" text, "
	+CONSULTANTNAME+" text, "+CLINICNURSENAME+" text, "+CONTACTNAME+" text, "+CLINICEMAILADDRESS+" text, "
	+CLINICWEBSITE+" text, "+CLINICSTREET+" text, "+CLINICCITY+" text, "+CLINICPOSTCODE+" text, "
	+CLINICCOUNTRY+" text, "+CLINICCONTACTNUMBER+" text, "+EMERGENCYCONTACTNUMBER+" text, "
	+EMERGENCYCONTACTNUMBER2+" text, "+RESULTSCONTACTNUMBER+" text, "+APPOINTMENTCONTACTNUMBER+" text, " 
	+INSURANCEID+" text, "+INSURANCENAME+" text, "+INSURANCEAUTHORISATIONCODE+" text, " 
	+INSURANCECONTACTNUMBER+" text,"+INSURANCEWEBSITE+" text, "+TINTABEEKEY+" text, "+GUIDTEXT+" text)";	
	
    ///////////////////////////////////////////////////////////////////////////
    //                      Main Table		    	                         //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEMAINTABLE = "create table "+MAINRECORDTABLE +
	" ("+KEY_ID+ " integer primary key autoincrement, "+LASTMODIFIED+" long, "
	+ISPASSWORDENABLED+" integer, "+PASSWORD+" text, "
	+DOB+" long, "+ISDIABETIC+" integer, "+ISSMOKER+" integer, "+GENDER+" text, "
	+TINTABEEKEY+" text, "+GUIDTEXT+" text)";

    ///////////////////////////////////////////////////////////////////////////
    //                      PreviousMedication Table		                 //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEPREVIOUSMEDSTABLE = "create table "+PREVIOUSMEDSTABLE+
	" ("+KEY_ID+ " integer primary key autoincrement, "+STARTDATE+" long, "+ENDDATE+" long, "
	+ISART+" integer, "+REASONENDED+" text, "+NAME+" text, "+DRUG+" text, "+GUIDTEXT+" text)";

    ///////////////////////////////////////////////////////////////////////////
    //                      Wellness Table          		                 //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEWELLNESSTABLE = "create table "+WELLNESSTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+RECORDEDDATE+" long, "
	+SLEEPBAROMETER+" integer, "+MOODBAROMETER+" integer, "+WELLNESSBAROMETER+" integer, "
	+GUIDTEXT+" text)";


	///////////////////////////////////////////////////////////////////////////
    //                      Procedures Table		                         //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEPROCEDURESTABLE = "create table "+PROCEDURESTABLE +
	" ("+KEY_ID+ " integer primary key autoincrement, "+DATE+" long, "
			+ENDDATE+" long, "+ILLNESS+" text, "+NAME+" text, "+CAUSEDBY+" text, "
	+NOTES+" text, "+TINTABEEKEY+" text, "+GUIDTEXT+" text)";
		
    ///////////////////////////////////////////////////////////////////////////
    //                      Medication Table			                     //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEMEDICATIONTABLE = "create table "+MEDICATIONTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+DOSE+" integer, "+NAME+" text, "
	+STARTDATE+" long, "+ENDDATE+" long, "+MEDICATIONFORM+" text, "+DRUG+" text, "
	+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";


    ///////////////////////////////////////////////////////////////////////////
    //                      Missed Medication Table			                 //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEMISSEDMEDICATIONTABLE = "create table "+MISSEDMEDICATIONTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+DRUG+" text,"+MISSEDDATE+" long, "+NAME+" text, "
	+MISSEDREASON+" text, "+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";
	
    ///////////////////////////////////////////////////////////////////////////
    //                      Other Medication Table			                 //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEOTHERMEDICATIONTABLE = "create table "+OTHERMEDICATIONTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+DOSE+" integer, "+NAME+" text, "
	+STARTDATE+" long, "+ENDDATE+" long, "+MEDICATIONFORM+" text, "+DRUG+" text, "+IMAGE+" blob, "
	+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";
	
	
    ///////////////////////////////////////////////////////////////////////////
    //                      Results Table			                         //
    ///////////////////////////////////////////////////////////////////////////
	
	public static final String CREATERESULTSTABLE = "create table "+RESULTSTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+CD4+" integer,"+RESULTSDATE+" long, "
	+VIRALLOAD+" integer, "+CD4PERCENT+" real, "+HEPCVIRALLOAD+" integer, "+GLUCOSE+" real, "
	+TOTALCHOLESTEROL+" real, "+LDL+" real, "+HDL+" real, "+TRIGYLCERIDE+" real, "+HEARTRATE+" integer, "
	+SYSTOLE+" integer, "+DIASTOLE+" integer, "+OXYGENLEVEL+" real, "+WEIGHT+" real, "
	+PLATELETCOUNT+" integer, "+HAEMOGLOBULIN+" integer, "+WHITECELLCOUNT+" integer, "+REDCELLCOUNT+" integer, "
	+CHOLESTEROLRATIO+" real, "+CARDIACRISKFACTOR+" real, "+HEPBTITER+" integer, "+HEPCTITER+" integer, "
	+liverAlanineTransaminase+" real, "+liverAspartateTransaminase+" real, "+liverAlkalinePhosphatase+" real, "
	+liverAlbumin+" real, "+liverAlanineTotalBilirubin+" real, "+liverAlanineDirectBilirubin+" real, "
	+liverGammaGlutamylTranspeptidase+" real, "+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";

    ///////////////////////////////////////////////////////////////////////////
    //                      Alerts Table			                         //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATEALERTSTABLE = "create table "+ALERTSTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+ALERTSTARTTIME+" long, "+
	ALERTHOUR + " integer, " + AlERTMINUTE + " integer, " + ALERTTIMEZONEOFFSET + " long, " +
	ALERTLABEL+" text, "+ALERTSOUND+" text, "+ALERTREPEATPATTERN+" integer, "+
	ALERTTEXT+" text, "+ALERTREQUESTCODE+" integer, "+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";
	
    ///////////////////////////////////////////////////////////////////////////
    //                      Side Effects Table			                     //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATESIDEEFFECTSTABLE = "create table "+SIDEEFFECTSTABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+SIDEEFFECTSDATE+" long, "+
	SIDEEFFECTS+" text, "+DRUG+" text, "+NAME+" text, "+SIDEEFFECTSERIOUSNESS+" integer, "
	+TINTABEEKEY+" text, "+GUIDTEXT+" GUID);";

	
    ///////////////////////////////////////////////////////////////////////////
    //                      Charts Table	(unused)                         //
    ///////////////////////////////////////////////////////////////////////////
	public static final String CREATECHARTDATATABLE = "create table "+CHARTDATATABLE+
	" ("+KEY_ID+" integer primary key autoincrement, "+TIMESTAMP+" long, "+
	DATAFLAG+" integer, "+DATAROWID+" long, "+GUIDTEXT+" GUID);";

}

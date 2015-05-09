package com.pweschmidt.healthapps.datamodel;
import android.content.ContentValues;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;
import android.database.Cursor;

public class Medication extends MedicalEvent
{
	private int dose;
	private String name;
	private long endDate;
	private String medicationForm;
	private String drug;
	

	public Medication()
	{
		dose = 0;
		name = "";
		setTime((new java.util.Date()).getTime());
		endDate = 0;
		medicationForm = "";
		drug = "";
	}
	
	/**
	 * gets the dose in [mg]
	 * @return
	 */
	public int getDose(){return dose;}
	/**
	 * gets the commercial name of the medication
	 * @return
	 */
	public String getName(){return name;}
	/**
	 * gets the start date
	 * @return
	 */
	/**
	 * gets the end date after which the medication will no longer be taken
	 * @return
	 */
	public long getEndDate(){return endDate;}
	/**
	 * gets the medication form, e.g. tablet or capsule
	 * @return
	 */
	public String getMedicationForm(){return medicationForm;}
	/**
	 * gets the drug component(s) of the medication
	 * @return
	 */
	public String getDrug(){return drug;}

	/**
	 * set the dose in mg
	 * @param dose
	 */
	public void setDose(int dose){this.dose = dose;}
	/**
	 * set the commercial name under which the HIV medication is listed
	 * @param name
	 */
	public void setName(String name){this.name = name;}
	/**
	 * set the start date from which the HIV medication is to be taken
	 * @param startDate
	 */
	/**
	 * sets the end date after which this HIV medication will no longer be taken
	 * @param endDate
	 */
	public void setEndDate(long endDate){this.endDate = endDate;}
	/**
	 * sets the form of the medication, e.g. tablet, capsule, injection....
	 * @param medicationForm
	 */
	public void setMedicationForm(String medicationForm){this.medicationForm = medicationForm;}
	/**
	 * sets the drug(s) contained in the HIV medication
	 * @param drug
	 */
	public void setDrug(String drug){this.drug = drug;}

	public ContentValues contentValuesForMedication()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.DOSE, dose);
		content.put(iStayHealthyDatabaseSchema.STARTDATE, getTime());
		content.put(iStayHealthyDatabaseSchema.ENDDATE, endDate);
		content.put(iStayHealthyDatabaseSchema.NAME, name);
		content.put(iStayHealthyDatabaseSchema.MEDICATIONFORM, medicationForm);
		content.put(iStayHealthyDatabaseSchema.DRUG, drug);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, getGUID());
		return content;
	}
	
	
	public void setMedication(Cursor cursor)
	{
		dose = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DOSE));
		time = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.STARTDATE));
		endDate = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ENDDATE));
		name = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.NAME));
		medicationForm = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.MEDICATIONFORM));
		drug = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DRUG));
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));
	}
	
}

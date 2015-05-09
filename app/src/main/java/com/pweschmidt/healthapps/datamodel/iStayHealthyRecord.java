package com.pweschmidt.healthapps.datamodel;
import android.content.ContentValues;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;
import android.database.Cursor;

public class iStayHealthyRecord {
	private String Password;
	private long lastModified;
	private int isPasswordEnabled;
	private String Tintabee;
	private String GUID;
	private long yearOfBirth;
	private boolean isDiabetic;
	private boolean isSmoker;
	private String gender;

	public iStayHealthyRecord(){
		Password = "";
		lastModified = 0;
		isPasswordEnabled = 0;
		GUID = java.util.UUID.randomUUID().toString();		
		Tintabee="";
		gender = "";
		yearOfBirth = 0;
		isDiabetic = false;
		isSmoker = false;
	}
	
	public String getGender(){return gender;}
	public void setGender(String gender){this.gender = gender;}
	
	public long getYearOfBirth(){return yearOfBirth;}
	public void setYearOfBirth(long dob){this.yearOfBirth = dob;}
	
	public boolean getIsDiabetic(){return isDiabetic;}
	public void setIsDiabetic(boolean isDiabetic){this.isDiabetic = isDiabetic;}
	
	public boolean getIsSmoker(){return isSmoker;}
	public void setIsSmoker(boolean isSmoker){this.isSmoker = isSmoker;}
	
	public String getPassword(){return Password;}
	public long getLastModified(){return lastModified;}
	public int getIsPasswordEnabled(){return isPasswordEnabled;}
	
	public void setPassword(String text){ Password = text;}
	public void setLastModified(long value){lastModified = value;}
	public void setIsPasswordEnabled(int flag){isPasswordEnabled = flag;	}
	
	
	public String getGUID(){return GUID;}
	public void setGUID(String text){ GUID = text;}
	
	public String getTintabee(){return Tintabee;}
	public void setTintabee(String text){Tintabee = text;}
	
	public ContentValues contentForMasterRecord()
	{
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.LASTMODIFIED, lastModified);
		content.put(iStayHealthyDatabaseSchema.PASSWORD, Password);
		
		content.put(iStayHealthyDatabaseSchema.ISPASSWORDENABLED, isPasswordEnabled);
		if(isDiabetic)
			content.put(iStayHealthyDatabaseSchema.ISDIABETIC, 1);
		else
			content.put(iStayHealthyDatabaseSchema.ISDIABETIC, 0);
		if(isSmoker)
			content.put(iStayHealthyDatabaseSchema.ISSMOKER, 1);
		else
			content.put(iStayHealthyDatabaseSchema.ISSMOKER, 0);
		
		content.put(iStayHealthyDatabaseSchema.DOB, yearOfBirth);
		content.put(iStayHealthyDatabaseSchema.GENDER, gender);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
		return content;
	}
	
	public void setMasterRecord(Cursor cursor)
	{
		Password = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.PASSWORD));
		lastModified = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.LASTMODIFIED));
		isPasswordEnabled = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ISPASSWORDENABLED));
		int diabeticFlag = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ISDIABETIC));
		if( 0 < diabeticFlag)
			isDiabetic = true;
		else
			isDiabetic = false;
		
		int smokerFlag = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.ISSMOKER));
		if(0 < smokerFlag)
			isSmoker = true;
		else
			isSmoker = false;
		
			
		yearOfBirth = cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DOB));
		gender = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GENDER));
		
		
		GUID = cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT));		
	}
}

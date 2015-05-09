package com.pweschmidt.healthapps.datamodel;

public abstract class MedicalEvent 
{
	protected long time;
	protected String GUID;
	
	public void setTime(long time){this.time = time;}
	
	public long getTime(){return time;}
	
	public void setGUID(String GUID){this.GUID = GUID;}
	
	public String getGUID(){return java.util.UUID.randomUUID().toString();}	
}

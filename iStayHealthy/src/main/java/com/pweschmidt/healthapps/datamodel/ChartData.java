package com.pweschmidt.healthapps.datamodel;

public class ChartData {
	private String GUID;
	private long rowID;
	private int dataFlag;
	private long timeStamp;

	public static final int RESULT_FLAG = 0;
	public static final int MEDICATION_FLAG = 1;
	public static final int MISSED_FLAG = 2;
	
	public ChartData(){
		timeStamp = 0;
		rowID = -1;
		dataFlag = ChartData.RESULT_FLAG;
		GUID = java.util.UUID.randomUUID().toString();
	}
	
	public void setDataFlag(int flag){dataFlag = flag;}
	public int getDataFlag(){return dataFlag;}

	public void setTimeStamp(long time){timeStamp = time;}
	public long getTimeStamp(){return timeStamp;}
	
	public void setRow(long row){rowID = row;}
	public long getRow(){return rowID;}
	
	public void setGUID(String _GUID){GUID = _GUID;}
	public String getGUID(){return GUID;}

}

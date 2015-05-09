package com.pweschmidt.healthapps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import android.app.backup.*;
import android.os.ParcelFileDescriptor;
//import android.util.Log;
import android.database.*;
import com.pweschmidt.healthapps.datamodel.*;

import com.pweschmidt.healthapps.documents.*;


public class iStayHealthyBackupAgent extends BackupAgent 
{
//	private static final String TAG = "iStayHealthyBackupAgent";
	public static final String FILE_HELPER_KEY = "iStayHealthyDBFile";
	private File dataFile;
	public boolean dbExists;
    private static final int AGENT_VERSION = 1;
    private static final String APP_DATA_KEY = "iStayHealthyData";

	/**
	 * 
	 */
	public void onCreate()
	{
		dataFile = new File(getFilesDir(), iStayHealthyDatabaseSchema.DATABASE_NAME);
		if(dataFile.exists()){
//			Log.d(TAG,"database file found");
			dbExists = true;
		}
		else{
//			Log.d(TAG,"database file NOT found!!!");
			dbExists = false;
		}
	}

	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
			ParcelFileDescriptor newState) throws IOException 
	{
		boolean isBackup = (null == oldState);
		long currentDate = (new Date()).getTime();
		Cursor records = getApplicationContext().getContentResolver().query(iStayHealthyContentProvider.RECORDS_CONTENT_URI, null, null, null, null);
		if(records.getCount() > 0 && records.moveToFirst())
		{
			iStayHealthyRecord record = new iStayHealthyRecord();
			record.setMasterRecord(records);
			if(isBackup)
			{
				currentDate = record.getLastModified();
				isBackup = doBackup(oldState, currentDate);
			}
		}
		records.close();
		if(!isBackup)
		{
			return;
		}
		
		XMLDocument doc = new XMLDocument(this);
		String xml = doc.toXMLString();
		byte[] bytesToWrite = xml.getBytes();
		int numberOfBytes = bytesToWrite.length;
		data.writeEntityHeader(APP_DATA_KEY, numberOfBytes);
		data.writeEntityData(bytesToWrite, numberOfBytes);
		writeNewState(newState, currentDate);
	}

	
	@Override
	public void onRestore(BackupDataInput data, int appVersionCode,
			ParcelFileDescriptor newState) throws IOException 
	{
        long date = (new Date()).getTime();
        while (data.readNextHeader()) 
        {
            String key = data.getKey();
            int dataSize = data.getDataSize();

            if (APP_DATA_KEY.equals(key)) 
            {
                // It's our saved data, a flattened chunk of data all in
                // one buffer.  Use some handy structured I/O classes to
                // extract it.
                byte[] dataBuf = new byte[dataSize];
                data.readEntityData(dataBuf, 0, dataSize);
				XMLParser parser = new XMLParser(dataBuf,this);  
				parser.parse(); 
            } 
            else 
            {
                // Curious!  This entity is data under a key we do not
                // understand how to process.  Just skip it.
                data.skipEntityData();
            }
        }

        // The last thing to do is write the state blob that describes the
        // app's data as restored from backup.
        writeNewState(newState, date);

	}
	
	/**
	 * 
	 * @param oldState
	 * @param lastModified
	 * @return
	 * @throws IOException
	 */
	private boolean doBackup(ParcelFileDescriptor oldState, long lastModified)throws IOException
	{
		if(null == oldState)
		{
			return true;
		}
        FileInputStream instream = new FileInputStream(oldState.getFileDescriptor());
        DataInputStream in = new DataInputStream(instream);
        int version = in.readInt();
        if(AGENT_VERSION < version)
        {
        	return true;
        }
        
        long modDate = in.readLong();
        if(lastModified < modDate)
        	return true;
		
        in.close();
		return false;
	}
	
	/**
	 * 
	 * @param newState
	 * @param lastModified
	 * @throws IOException
	 */
	private void writeNewState(ParcelFileDescriptor newState, long lastModified)throws IOException
	{
        FileOutputStream outstream = new FileOutputStream(newState.getFileDescriptor());
        DataOutputStream out = new DataOutputStream(outstream);
		out.writeInt(AGENT_VERSION);
		out.writeLong(lastModified);        
        out.close();
	}

}

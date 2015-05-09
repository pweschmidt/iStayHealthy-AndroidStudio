package com.pweschmidt.healthapps;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.Button;

import java.io.*;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.RESTUtility;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.pweschmidt.healthapps.documents.*;

import org.xml.sax.SAXParseException;

import java.util.*;

public class DropboxActivity extends Activity implements View.OnClickListener
{
 //   private static final String TAG = "DropboxActivity";
    ///////////////////////////////////////////////////////////////////////////
    //                      Your Dropbox-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////
    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    final static private String APP_KEY = "sekt4gbt7526j0y";
    final static private String APP_SECRET = "drg5hompcf9vbd2";
    // You don't need to change these, leave them alone.
    final static private String ACCOUNT_PREFS_NAME = "prefs";
    final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
    final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    final static private String OAUTH2_TOKEN = "OAUTH2_TOKEN_DROPBOX";
    // If you'd like to change the access type to the full Dropbox instead of
    // an app folder, change this value.
    final static private AccessType ACCESS_TYPE = AccessType.DROPBOX;
    static DropboxAPI<com.dropbox.client2.android.AndroidAuthSession> dropBoxSession;
    ///////////////////////////////////////////////////////////////////////////
    //                      End Dropbox-specific settings.                   //
    ///////////////////////////////////////////////////////////////////////////
//    private static final String OLDBACKUPFILE = "/iStayHealthy/iStayHealthy.xml";
    private static final String BACKUPFILE = "/iStayHealthy/iStayHealthy.isth";
    private static final String UPLOADFILE = "/iStayHealthy/iStayHealthy.isth";//only for debugging
    private Button backupButton;
    private Button restoreButton;
    private Button connectButton;
    private Button disconnectButton;
    private static String errorMessage;
    private static ProgressDialog progressDialog;
    private UploadThread thread;
    private static Handler handler;
    public boolean isUpload;
    private static DropboxActivity thisActivity;
    private static Context thisContext;

    /**
     * 
     */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropboxbackup);

        // We create a new AuthSession so that we can use the Dropbox API.
        isUpload = true;
        thisActivity = this;
        
        thisContext = (Context)this;
        handler = new Handler();
        com.dropbox.client2.android.AndroidAuthSession session = buildSession();
        dropBoxSession = new DropboxAPI<com.dropbox.client2.android.AndroidAuthSession>(session);
        checkAppKeySetup();
        // Display the proper UI state if logged in or not
        
        ImageButton cancel = (ImageButton)findViewById(R.id.BackFromDropboxButton);
        cancel.setOnClickListener(this);
        
        backupButton = (Button)findViewById(R.id.BackupButton);
        backupButton.setOnClickListener(this);
        
        restoreButton = (Button)findViewById(R.id.RestoreButton);
        restoreButton.setOnClickListener(this);

        connectButton = (Button)findViewById(R.id.LinkDropbox);
        connectButton.setOnClickListener(this);


        disconnectButton = (Button)findViewById(R.id.UnlinkDropbox);
        disconnectButton.setOnClickListener(this);
        setLoggedIn(dropBoxSession.getSession().isLinked());
        thread = (UploadThread) getLastNonConfigurationInstance();
		if (thread != null && thread.isAlive()) 
		{
	    	String message = thread.getProgressMessage();
	    	progressDialog = ProgressDialog.show(this, "", message);
		}
       
    }	

    /**
     * 
     */
    public void onClick(View view)
    {
//    	Log.d(TAG,"in onClick");
    	int resId = view.getId();
    	switch(resId)
    	{
    	case R.id.BackFromDropboxButton:
    		finish();
    		break;
    	case R.id.BackupButton:
//    		Log.d(TAG,"in backup data");
    		isUpload = true;
    		backup();
    		break;
    	case R.id.RestoreButton:
    		isUpload = false;
    		restore();
    		break;
    	case R.id.LinkDropbox:
    		dropBoxSession.getSession().startOAuth2Authentication(DropboxActivity.this);
    		break;
    	case R.id.UnlinkDropbox:
    		logOut();
    		break;
    	}
    }
    
    /**
     * 
     */
    public void onResume()
    {
    	super.onResume();
        AndroidAuthSession session = dropBoxSession.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) 
        {
            try 
            {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                String token = session.getOAuth2AccessToken();
                storeOAuth2Token(token);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
//                Log.i(TAG, "Error authenticating", e);
            }
        }
    }

    
    /**
     * 
     */
	public Object onRetainNonConfigurationInstance() {
		return thread;
	}
    

	/**
	 * 
	 */
    protected void onDestroy()
    {
    	if(null != progressDialog && progressDialog.isShowing())
    	{
    		progressDialog.dismiss();
    		progressDialog = null;
    	}
    	super.onDestroy();
    }
    
    
    /**
     * 
     */
    public void backup()
    {
    	if(null != thread && thread.isAlive())
    	{
    		showToast("Wait until current process is finished");
    		return;
    	}
    	String upload = getResources().getString(R.string.upload);
    	progressDialog = ProgressDialog.show(this, "", upload);
    	thread = new UploadThread(true);
    	thread.start();
    }

    /**
     * 
     */
    public void restore()
    {
    	if(null != thread && thread.isAlive())
    	{
    		showToast("Wait until current process is finished");
    		return;
    	}
    	String download = getResources().getString(R.string.download);
    	progressDialog = ProgressDialog.show(this, "", download);
    	thread = new UploadThread(false);
    	thread.start();
    	
    }
    /**
     * 
     * @author peterschmidt
     *
     */
    static public class UploadThread extends Thread
    {
    	private boolean isUpload;
    	private String progressMessage;
    	/**
    	 * 
    	 * @param upLoadFlag
    	 */
    	public UploadThread(boolean upLoadFlag)
    	{
    		super();
    		isUpload = upLoadFlag;
    		if(isUpload)
    		{
    			progressMessage = thisActivity.getResources().getString(R.string.upload);
    		}
    		else
    		{
    			progressMessage = thisActivity.getResources().getString(R.string.download);    			
    		}
    	}
    	
    	public String getProgressMessage()
    	{
    		return progressMessage;
    	}
    	
    	/**
    	 * 
    	 */
    	public void run()
    	{
    		if(isUpload)
    		{
    			XMLDocument doc = new XMLDocument(thisContext.getApplicationContext());
    			String xml = doc.toXMLString();
        		uploadData(xml);
    		}
    		if(!isUpload)
    		{
    			ByteArrayOutputStream out = new ByteArrayOutputStream();
    			boolean isOk = downloadData(out);
    			if(isOk)
    			{
    				try
    				{
        				byte[] array = out.toByteArray();
                        XMLErrorHandler errorHandler = new XMLErrorHandler();
        				XMLParser parser = new XMLParser(array,thisContext.getApplicationContext());
                        try
                        {
                            parser.parse(errorHandler);
                        }catch(SAXParseException se)
                        {
                            String type = errorHandler.getType();
                            if (!type.equals("WARNING"))
                            {
                                String message = se.getLocalizedMessage();
                                showToast(message);
                                out.close();
                            }

                        }
        				out.close();
    				}catch(IOException ie){ie.printStackTrace();}
    			}
    		}
    		handler.post(new UploadRunnable());    			
    	}
    }

    /**
     * 
     * @author peterschmidt
     *
     */
    static public class UploadRunnable implements Runnable
    {
    	public void run()
    	{
    		showToast(errorMessage);
    		progressDialog.dismiss();
    	}
    }
    
    /**
     * 
     */
    private void logOut()
    {
        // Remove credentials from the session
    	dropBoxSession.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        setLoggedIn(false);
    }


    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) 
    {
    	if (loggedIn) 
    	{
        	connectButton.setVisibility(View.GONE);
        	disconnectButton.setVisibility(View.VISIBLE);
    	} 
    	else 
    	{
        	connectButton.setVisibility(View.VISIBLE);
        	disconnectButton.setVisibility(View.GONE);        	
    	}
    }

    /**
     * 
     */
    private void checkAppKeySetup() 
    {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") ||
                APP_SECRET.startsWith("CHANGE")) 
        {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) 
        {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);
            finish();
        }
    }

    /**
     * 
     * @param msg
     */
    static private void showToast(String msg) 
    {
        Toast error = Toast.makeText(thisActivity, msg, Toast.LENGTH_LONG);
        error.show();
    }

    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     *
     * @return Array of [access_key, access_secret], or null if none stored
     */
    private String[] getKeys() 
    {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) 
        {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } 
        else 
        {
        	return null;
        }
    }

    private String getOAuth2Token()
    {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String token = prefs.getString(OAUTH2_TOKEN, null);
        return token;
    }


    /**
     * Shows keeping the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeKeys(String key, String secret)
    {
        // Save the access key for later
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        if (null != key && null != secret)
        {
            edit.putString(ACCESS_KEY_NAME, key);
            edit.putString(ACCESS_SECRET_NAME, secret);
        }
        edit.commit();
    }

    private void storeOAuth2Token(String token)
    {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        if(null != token)
        {
            edit.putString(OAUTH2_TOKEN, token);
        }
        edit.commit();
    }

    /**
     * 
     */
    private void clearKeys() 
    {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 
     * @return
     */
    private AndroidAuthSession buildSession() 
    {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        String token = getOAuth2Token();
        if (null != token)
        {
            session = new AndroidAuthSession(appKeyPair, token);
        }
        else if (stored != null)
        {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
            session = new AndroidAuthSession(appKeyPair, accessToken);
//            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
        }
        else 
        {
            session = new AndroidAuthSession(appKeyPair);
//            session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }

        return session;
    }

    ///////////////////////////////////////////////////////////////////////////
    //                      download data from Dropbox                       //
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param out
     * @return
     */
    static private boolean downloadData(OutputStream out)
    {
//    	Log.d(TAG,"downloadData");
    	errorMessage = "completed download";
        boolean success = true;
        try 
        {
        	
        	List<Entry> fileList = dropBoxSession.search("/", "iStayHealthy", 1000, false);
        	if(fileList.isEmpty())
        	{
//            	Log.d(TAG,"no iStayHealthy folder in Dropbox");
        		errorMessage = "no iStayHealthy folder - creating iStayHealthy folder in root";
        		dropBoxSession.createFolder("/iStayHealthy");
        		return false;
        	}
        	List<Entry> backupFiles = dropBoxSession.search("/iStayHealthy", ".xml", 1000, false);
        	List<Entry> newBackupFiles = dropBoxSession.search("/iStayHealthy", ".isth", 1000, false);
        	if(backupFiles.isEmpty() && newBackupFiles.isEmpty())
        	{
//            	Log.d(TAG,"no iStayHealthy.xml  in /iStayHealthy folder in Dropbox");
        		errorMessage = "no iStayHealthy backupfile";
        		return false;        		
        	}
        	String backupPath = null;
        	long time = 0;
        	Entry lastOldEntry = null;
        	for( Entry entry: backupFiles)
        	{
        		String path = entry.path;
        		long oldTime = (RESTUtility.parseDate(entry.modified)).getTime();
        		if(path.startsWith("/iStayHealthy/iStayHealthy-Android"))
        		{
        			if(oldTime > time)
        			{
        				time = oldTime;
        				lastOldEntry = entry;
        			}
        		}
        	}
        	long newTime = 0;
        	Entry lastNewEntry = null;
        	for( Entry newEntry : newBackupFiles)
        	{
        		String path = newEntry.path;
        		long lastNewTime = (RESTUtility.parseDate(newEntry.modified)).getTime();
        		if(path.startsWith("/iStayHealthy/iStayHealthy"))
        		{
        			if(lastNewTime > newTime)
        			{
        				newTime = lastNewTime;
        				lastNewEntry = newEntry;
        			}
        		}        		
        	}        		
        	if(null != lastOldEntry)
        	{
        		Entry last = dropBoxSession.metadata(lastOldEntry.path, 1, null, false, null);
//        		Log.d(TAG,"we have found the last Android XML file at "+last.path+" from date "+last.modified);
        		if(null == lastNewEntry)
        		{
//        			Log.d(TAG,"we haven't found any new iStayHealthy.isth file");
        			Entry lastEntry = dropBoxSession.move(last.path, BACKUPFILE);
        			backupPath = lastEntry.path;
        		}
        		else
        		{
        			Entry other = dropBoxSession.metadata(lastNewEntry.path, 1, null, false, null);
        			long firstTime = (RESTUtility.parseDate(last.modified)).getTime();
        			long lastTime = (RESTUtility.parseDate(other.modified)).getTime();
        			if(firstTime > lastTime)
        			{
        				backupPath = last.path;
        			}
        			else
        			{
        				backupPath = other.path;
        			}
        		}
        	}
        	else if(null != lastNewEntry)
        	{
//        		Log.d(TAG,"we have not found an Android XML but a new isth file at "+lastNewEntry.path+" from date "+lastNewEntry.modified);
        		backupPath = lastNewEntry.path;
        	}
        	
        	if(null != backupPath)
        	{
//        		Log.d(TAG,"We are backing up from "+backupPath);
            	dropBoxSession.getFile(backupPath, null, out, null);   
            	
            	for(Entry oldXML : backupFiles)
            	{
            		dropBoxSession.delete(oldXML.path);
            	}
        	}
        	else
        	{
        		return false;
        	}
        	
        	
        	
//        	Entry existingFile = dropBoxSession.metadata(BACKUPFILE, 1, null, false, null);
//        	Entry exsitingOldFile = dropBoxSession.metadata(OLDBACKUPFILE, 1, null, false, null);
        	
//        	String path = existingFile.path;
        	
//        	Log.d(TAG,"file has "+existingFile.bytes+" bytes");
        	
        } catch (DropboxUnlinkedException e) {
            // The AuthSession wasn't properly authenticated or user unlinked.
        } catch (DropboxPartialFileException e) {
            // We canceled the operation
            errorMessage = "Download canceled";
        } catch (DropboxServerException e) {
            // Server-side exception.  These are examples of what could happen,
            // but we don't do anything special with them here.
            if (e.error == DropboxServerException._304_NOT_MODIFIED) {
                // won't happen since we don't pass in revision with metadata
            } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                success = false;
                // Unauthorized, so we should unlink them.  You may want to
                // automatically log the user out in this case.
            } else if (e.error == DropboxServerException._403_FORBIDDEN) {
                success = false;
                // Not allowed to access this
            } else if (e.error == DropboxServerException._404_NOT_FOUND) {
                success = false;
                // path not found (or if it was the thumbnail, can't be
                // thumbnailed)
            } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
                success = false;
                // too many entries to return
            } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
                success = false;
                // can't be thumbnailed
            } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                success = false;
                // user is over quota
            } else {
                success = false;
                // Something else
            }
            // This gets the Dropbox error, translated into the user's language
            errorMessage = e.body.userError;
            if (errorMessage == null) {
            	errorMessage = e.body.error;
            }
        } catch (DropboxIOException e) {
            // Happens all the time, probably want to retry automatically.
        	errorMessage = "Network error.  Try again.";
            success = false;
        } catch (DropboxParseException e) {
            // Probably due to Dropbox server restarting, should retry
        	errorMessage = "Dropbox error.  Try again.";
            success = false;
        } catch (DropboxException e) {
            // Unknown error
        	errorMessage = "Unknown error.  Try again.";
            success = false;
        }
        return success;
    }
    

    ///////////////////////////////////////////////////////////////////////////
    //                      upload data to Dropbox                           //
    ///////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param xml
     * @return
     */
    static private boolean uploadData(String xml)
    {
//    	Log.d(TAG,"uploadData");
    	errorMessage = "completed upload";
        boolean success = true;
        try 
        {
        	List<Entry> fileList = dropBoxSession.search("/", "iStayHealthy", 1000, false);
        	if(fileList.isEmpty())
        	{
//            	Log.d(TAG,"no iStayHealthy folder in Dropbox");
        		errorMessage = "no iStayHealthy folder - creating iStayHealthy folder in root";
        		dropBoxSession.createFolder("/iStayHealthy");
        	}
        	ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
        	List<Entry> isthFiles = dropBoxSession.search("/iStayHealthy", "iStayHealthy.isth", 1000, false);
        	if(isthFiles.isEmpty())
        	{
            	dropBoxSession.putFile(UPLOADFILE, in, xml.length(), null, null);    			        		
        	}
        	else
        	{
        		Entry newEntryFile = dropBoxSession.metadata(UPLOADFILE, 1, null, false, null);        		
        		String revision = newEntryFile.rev;
//    			Log.d(TAG,"we did  find the file with revision "+ revision+ " and will upload an updated version");
            	dropBoxSession.putFile(UPLOADFILE, in, xml.length(), revision, null);
        	}
        } catch (DropboxUnlinkedException e) {
            // The AuthSession wasn't properly authenticated or user unlinked.
        } catch (DropboxPartialFileException e) {
            // We canceled the operation
            errorMessage = "Download canceled";
        } catch (DropboxServerException e) {
            // Server-side exception.  These are examples of what could happen,
            // but we don't do anything special with them here.
            if (e.error == DropboxServerException._304_NOT_MODIFIED) {
                // won't happen since we don't pass in revision with metadata
            } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                errorMessage = "Unauthorized. Please disconnect from Dropbox and login again";
                success = false;
                // Unauthorized, so we should unlink them.  You may want to
                // automatically log the user out in this case.
            } else if (e.error == DropboxServerException._403_FORBIDDEN) {
                errorMessage = "Action is not allowed";
                success = false;
                // Not allowed to access this
            } else if (e.error == DropboxServerException._404_NOT_FOUND) {
                errorMessage = "File was not found.";
                success = false;
                // path not found (or if it was the thumbnail, can't be
                // thumbnailed)
            } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
                errorMessage = "Request was not accepted.";
                success = false;
                // too many entries to return
            } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
                errorMessage = "Uploaded file is of unsupported type.";
                success = false;
                // can't be thumbnailed
            } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                success = false;
                // user is over quota
            } else {
                // Something else
            }
            // This gets the Dropbox error, translated into the user's language
            errorMessage = e.body.userError;
            if (errorMessage == null) {
            	errorMessage = e.body.error;
            }
        } catch (DropboxIOException e) {
            // Happens all the time, probably want to retry automatically.
        	errorMessage = "Network error.  Try again.";
            success = false;
        } catch (DropboxParseException e) {
            // Probably due to Dropbox server restarting, should retry
        	errorMessage = "Dropbox error.  Try again.";
            success = false;
        } catch (DropboxException e) {
            // Unknown error
        	errorMessage = "Unknown error.  Try again.";
            success = false;
        }
    	return success;
    }
}

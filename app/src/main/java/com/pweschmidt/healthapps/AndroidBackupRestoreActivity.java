package com.pweschmidt.healthapps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.backup.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class AndroidBackupRestoreActivity extends Activity implements View.OnClickListener{
//    static final String TAG = "AndroidBackupRestoreActivity";
    static final Object[] sDataLock = new Object[0];
    BackupManager backupManager;
    private static AndroidBackupRestoreActivity thisActivity;
//    private static String errorMessage;
    private static ProgressDialog progressDialog;

    /**
     * 
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidbackup);
        backupManager = new BackupManager(this);
        Button back = (Button)findViewById(R.id.BackFromBackupButton);
        back.setOnClickListener(this);
        
        Button backup = (Button)findViewById(R.id.BackupAndroidButton);
        backup.setOnClickListener(this);
        
        Button restore = (Button)findViewById(R.id.RestoreAndroidButton);
        restore.setOnClickListener(this);
    }
    
    /**
     * 
     */
    public void onClick(View view){
    	int resId = view.getId();
    	switch(resId){
    	case R.id.BackFromBackupButton:
    		finish();
    		break;
    	case R.id.BackupAndroidButton:
    		backupManager.dataChanged();
    		break;
    	case R.id.RestoreAndroidButton:
    		backupManager.requestRestore(new iStayHealthyBackupObserver());
    		break;
    	}
    }

    /**
	 * 
	 */
    protected void onDestroy(){
    	if(null != progressDialog && progressDialog.isShowing()){
    		progressDialog.dismiss();
    		progressDialog = null;
    	}
    	super.onDestroy();
    }
    
    private class iStayHealthyBackupObserver extends RestoreObserver{
    	public void restoreStarting(int numPackages){
        	String download = getResources().getString(R.string.download);
        	progressDialog = ProgressDialog.show(thisActivity, "", download);	
    	}
    	
    	public void restoreFinished(int error){
        	if(null != progressDialog && progressDialog.isShowing()){
        		progressDialog.dismiss();
        		progressDialog = null;
        	}    		
    	}
    }
    
}

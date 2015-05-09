package com.pweschmidt.healthapps;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.*;
import android.app.ProgressDialog;
import android.graphics.*;
public class WebViewActivity extends Activity 
{
	private WebView bannerView;
	private String loading;
	private WebViewActivity thisActivity;
	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.bannerview);
	    thisActivity = this;
	    String url = "http://www.poz.com";

	    Bundle extras = getIntent().getExtras();
	    if(null != extras){
	    	url = extras.getString("url");
	    }
	    bannerView = (WebView)findViewById(R.id.bannerView);
	    bannerView.getSettings().setJavaScriptEnabled(true);
	    bannerView.getSettings().setPluginState(WebSettings.PluginState.ON);
	    bannerView.getSettings().setBuiltInZoomControls(true);
	    bannerView.setWebViewClient(new BannerWebViewClient());
	    bannerView.loadUrl(url);	    	
	}
	
	public void onResume(){
		super.onResume();
	}

	public void onPause(){
		super.onPause();
	}
	

	/**
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && bannerView.canGoBack()) {
	    	bannerView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}	
	/**
	 * 
	 * @author peterschmidt
	 *
	 */
	private class BannerWebViewClient extends WebViewClient
	{
		ProgressDialog dialog;
		
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			String loading = thisActivity.getResources().getString(R.string.loading);
			if(null != dialog)
			{
				if(dialog.isShowing())
				{
					dialog.dismiss();
				}
			}
			dialog = ProgressDialog.show(thisActivity, "", loading);
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url)
		{
			if(null != dialog)
			{
				if(dialog.isShowing())
					dialog.dismiss();
			}
			super.onPageFinished(view, url);
		}
		
	    public boolean shouldOverrideUrlLoading(WebView view, String url) 
	    {
	        view.loadUrl(url);
	        return true;
	    }		
	}
}

package com.pweschmidt.healthapps;
import android.widget.CheckBox;

public class StringCheckBoxPair {
	private CheckBox value;
	private String key;
	
	public StringCheckBoxPair(String key, CheckBox value){
		this.key = key;
		this.value = value;
	}

	public String getKey(){return key;}
	public CheckBox getValue(){return value;}
}

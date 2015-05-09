package com.pweschmidt.healthapps.documents;

import java.util.HashMap;

public class TintabeeXMLMap {
    public final static HashMap<String, String>xmlMap = new HashMap<String, String>();
    static{
    	xmlMap.put("ResultsDate", "resultsDate");
    	xmlMap.put("CD4", "cd4");
    	xmlMap.put("CD4Percent", "cd4Percent");
    	xmlMap.put("LDL", "ldlc");
    	xmlMap.put("HDL", "hdlc");
    	xmlMap.put("ViralLoad", "viralLoad");
    	xmlMap.put("HepCViralLoad", "hepCViralLoad");
    	xmlMap.put("GUID", "GUID");
    	xmlMap.put("Result", "result");
    }
}

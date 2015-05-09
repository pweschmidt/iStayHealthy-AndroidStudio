package com.pweschmidt.healthapps;

import java.util.*;

public class FileMaps {
    
    public final static HashMap<String, Integer>allMedImages = new HashMap<String, Integer>();
    static{
    	allMedImages.put("atripla", new Integer(R.drawable.atripla));
    	allMedImages.put("combivir", new Integer(R.drawable.combivir));
    	allMedImages.put("complera", new Integer(R.drawable.complera));
    	allMedImages.put("kaletra", new Integer(R.drawable.kaletra));
    	allMedImages.put("kivexa", new Integer(R.drawable.kivexa));
    	allMedImages.put("trizivir", new Integer(R.drawable.trizivir));
    	allMedImages.put("truvada", new Integer(R.drawable.truvada));
    	allMedImages.put("zidovudine", new Integer(R.drawable.lamivudine));
    	allMedImages.put("stribild", new Integer(R.drawable.stribild));
    	allMedImages.put("edurant", new Integer(R.drawable.edurant));
    	allMedImages.put("intelence", new Integer(R.drawable.intelence));
    	allMedImages.put("rescriptor", new Integer(R.drawable.rescriptor));
    	allMedImages.put("sustiva", new Integer(R.drawable.sustiva));
    	allMedImages.put("viramune", new Integer(R.drawable.viramune));    	
    	allMedImages.put("didanosine", new Integer(R.drawable.didanosine));    	
    	allMedImages.put("emtriva", new Integer(R.drawable.emtriva));    	
    	allMedImages.put("epivir", new Integer(R.drawable.epivir));    	
    	allMedImages.put("retrovir", new Integer(R.drawable.retrovir));    	
    	allMedImages.put("stavudine", new Integer(R.drawable.stavudine));    	
    	allMedImages.put("videx", new Integer(R.drawable.videx));    	
    	allMedImages.put("viread", new Integer(R.drawable.viread));    	
    	allMedImages.put("zerit",new Integer( R.drawable.zerit));    	
    	allMedImages.put("ziagen",new Integer( R.drawable.ziagen));    	
    	allMedImages.put("zidovudine", new Integer(R.drawable.zidovudine));    	    	
    	allMedImages.put("aptivus", new Integer(R.drawable.aptivus));
    	allMedImages.put("crixivan", new Integer(R.drawable.crixivan));
    	allMedImages.put("invirase", new Integer(R.drawable.invirase));
    	allMedImages.put("lexiva", new Integer(R.drawable.lexiva));
    	allMedImages.put("norvir", new Integer(R.drawable.norvir));
    	allMedImages.put("prezista", new Integer(R.drawable.prezista));
    	allMedImages.put("reyataz", new Integer(R.drawable.reyataz));
    	allMedImages.put("viracept", new Integer(R.drawable.viracept));
    	allMedImages.put("celsentri",new Integer(R.drawable.celsentri));
    	allMedImages.put("fuzeon",new Integer(R.drawable.fuzeon));    	
    	allMedImages.put("isentress", new Integer(R.drawable.isentress));
    	allMedImages.put("tivicay", new Integer(R.drawable.tivicay));
    }
    
    public final static HashMap<String, Integer> combiMeds = new HashMap<String, Integer>();
    static{
    	combiMeds.put("atripla", new Integer(R.drawable.atripla));
    	combiMeds.put("combivir", new Integer(R.drawable.combivir));
    	combiMeds.put("complera", new Integer(R.drawable.complera));
    	combiMeds.put("kaletra", new Integer(R.drawable.kaletra));
    	combiMeds.put("kivexa", new Integer(R.drawable.kivexa));
    	combiMeds.put("trizivir", new Integer(R.drawable.trizivir));
    	combiMeds.put("truvada", new Integer(R.drawable.truvada));
    	combiMeds.put("zidovudine", new Integer(R.drawable.lamivudine));
    	combiMeds.put("stribild", new Integer(R.drawable.stribild));
    }
    
    public final static HashMap<String, Integer> nnRTI = new HashMap<String, Integer>();
    static{
    	nnRTI.put("edurant", new Integer(R.drawable.edurant));
    	nnRTI.put("intelence", new Integer(R.drawable.intelence));
    	nnRTI.put("rescriptor", new Integer(R.drawable.rescriptor));
    	nnRTI.put("sustiva", new Integer(R.drawable.sustiva));
    	nnRTI.put("viramune", new Integer(R.drawable.viramune));    	
    }
    
    
    public final static HashMap<String, Integer> nRTI = new HashMap<String, Integer>();
    static{
    	nRTI.put("didanosine", new Integer(R.drawable.didanosine));    	
    	nRTI.put("emtriva", new Integer(R.drawable.emtriva));    	
    	nRTI.put("epivir", new Integer(R.drawable.epivir));    	
    	nRTI.put("retrovir", new Integer(R.drawable.retrovir));    	
    	nRTI.put("stavudine", new Integer(R.drawable.stavudine));    	
    	nRTI.put("videx", new Integer(R.drawable.videx));    	
    	nRTI.put("viread", new Integer(R.drawable.viread));    	
    	nRTI.put("zerit", new Integer(R.drawable.zerit));    	
    	nRTI.put("ziagen", new Integer(R.drawable.ziagen));    	
    	nRTI.put("zidovudine", new Integer(R.drawable.zidovudine));    	    	
    }
    
    public final static HashMap<String, Integer> proteaseInhibitors = new HashMap<String, Integer>();
    static{
    	proteaseInhibitors.put("aptivus", new Integer(R.drawable.aptivus));
    	proteaseInhibitors.put("crixivan", new Integer(R.drawable.crixivan));
    	proteaseInhibitors.put("invirase", new Integer(R.drawable.invirase));
    	proteaseInhibitors.put("lexiva", new Integer(R.drawable.lexiva));
    	proteaseInhibitors.put("norvir", new Integer(R.drawable.norvir));
    	proteaseInhibitors.put("prezista", new Integer(R.drawable.prezista));
    	proteaseInhibitors.put("reyataz", new Integer(R.drawable.reyataz));
    	proteaseInhibitors.put("viracept", new Integer(R.drawable.viracept));
    }

    public final static HashMap<String, Integer> entryInhibitors = new HashMap<String, Integer>();
    static{
    	entryInhibitors.put("celsentri",new Integer(R.drawable.celsentri));
    	entryInhibitors.put("fuzeon",new Integer(R.drawable.fuzeon));    	
    }

    public final static HashMap<String, Integer> integraseInhibitors = new HashMap<String, Integer>();
    static{
    	integraseInhibitors.put("isentress", new Integer(R.drawable.isentress));
    	integraseInhibitors.put("tivicay", new Integer(R.drawable.tivicay));
    }

    /**
     * 
     * @param name
     * @return
     */
    public static int getResourceIdForName(String name){
    	int resId = R.drawable.combi;
    	if(null != FileMaps.combiMeds.get(name))
    		return FileMaps.combiMeds.get(name).intValue();
    	
    	if(null != FileMaps.nnRTI.get(name))
    		return FileMaps.nnRTI.get(name).intValue();
    	
    	if(null != FileMaps.nRTI.get(name))
    		return FileMaps.nRTI.get(name).intValue();
    	
    	if(null != FileMaps.proteaseInhibitors.get(name))
    		return FileMaps.proteaseInhibitors.get(name).intValue();
    	
    	if(null != FileMaps.entryInhibitors.get(name))
    		return FileMaps.entryInhibitors.get(name).intValue();
    	
    	if(null != FileMaps.integraseInhibitors.get(name))
    		return FileMaps.integraseInhibitors.get(name).intValue();
    	
    	return resId;
    }
	
}

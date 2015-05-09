package com.pweschmidt.healthapps.datamodel;

import android.content.ContentValues;
import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;
import android.database.Cursor;

public class Results extends MedicalEvent
{
	private int cd4Count;
	private float cd4Percent;
	private int viralLoad;
	private int hepCViralLoad;
	private float glucose;
	private float totalCholesterol;
	private float LDL;
	private float HDL;
	private float triglyceride;
	private int heartRate;
	private int systole;
	private int diastole;
	private float oxygenLevel;
	private int plateletCount;
	private int whiteCellCount;
	private int redCellCount;
	private int haemoglobulin;
	private float cholesterolRatio;
	private float cardiacRiskFactor;
	private int hepBTiter;
	private int hepCTiter;
	private float liverAlanineTransaminase;
	private float liverAspartateTransaminase;
	private float liverAlkalinePhosphatase;
	private float liverAlbumin;
	private float liverAlanineTotalBilirubin;
	private float liverAlanineDirectBilirubin;
	private float liverGammaGlutamylTranspeptidase;
	private float weight;
	
	
	public Results(){
		setTime((new java.util.Date()).getTime());
		cd4Percent = (float)0.0;
		cd4Count = viralLoad = hepCViralLoad = 0;
		glucose = totalCholesterol = LDL = HDL = triglyceride = (float)0.0;
		heartRate = systole = diastole = 0;
		oxygenLevel = (float)0.0;
		plateletCount = whiteCellCount = redCellCount = haemoglobulin = 0;
		weight = cardiacRiskFactor = cholesterolRatio = (float)0.0;
		hepCTiter = hepBTiter = 0;
		liverAlanineTransaminase = (float)0.0;
		liverAspartateTransaminase = (float)0.0;
		liverAlkalinePhosphatase = (float)0.0;
		liverAlbumin = (float)0.0;
		liverAlanineTotalBilirubin = (float)0.0;
		liverAlanineDirectBilirubin = (float)0.0;
		liverGammaGlutamylTranspeptidase = (float)0.0;
	}

	
	public float getLiverGammaGlutamylTranspeptidase(){return liverGammaGlutamylTranspeptidase;}
	public void setLiverGammaGlutamylTranspeptidase(float liverGammaGlutamylTranspeptidase){this.liverGammaGlutamylTranspeptidase = liverGammaGlutamylTranspeptidase;}

	public float getLiverAlanineDirectBilirubin(){return liverAlanineDirectBilirubin;}
	public void setLiverAlanineDirectBilirubin(float liverAlanineDirectBilirubin){this.liverAlanineDirectBilirubin = liverAlanineDirectBilirubin;}

	public float getLiverAlanineTotalBilirubin(){return liverAlanineTotalBilirubin;}
	public void setLiverAlanineTotalBilirubin(float liverAlanineTotalBilirubin){this.liverAlanineTotalBilirubin = liverAlanineTotalBilirubin;}

	public float getLiverAlbumin(){return liverAlbumin;}
	public void setLiverAlbumin(float liverAlbumin){this.liverAlbumin = liverAlbumin;}

	public float getLiverAlkalinePhosphatase(){return liverAlkalinePhosphatase;}
	public void setLiverAlkalinePhosphatase(float liverAlkalinePhosphatase){this.liverAlkalinePhosphatase = liverAlkalinePhosphatase;}

	public float getLiverAspartateTransaminase(){return liverAspartateTransaminase;}
	public void setLiverAspartateTransaminase(float liverAspartateTransaminase){this.liverAspartateTransaminase = liverAspartateTransaminase;}

	public float getLiverAlanineTransaminase(){return liverAlanineTransaminase;}
	public void setLiverAlanineTransaminase(float liverAlanineTransaminase){this.liverAlanineTransaminase = liverAlanineTransaminase;}

	public float getWeight(){return weight;}
	public void setWeight(float weight){this.weight = weight;}
	
	public float getCardiacRiskFactor(){return cardiacRiskFactor;}
	public void setCardiacRiskFactor(float cardiacRiskFactor){
		this.cardiacRiskFactor = cardiacRiskFactor;}
	
	public float getCholesterolRatio(){return cholesterolRatio;}
	public void setCholesterolRatio(float cholesterolRatio){
		this.cholesterolRatio = cholesterolRatio;}
	
	public int getHepBTiter(){return hepBTiter;}
	public void setHepBTiter(int hepBTiter){this.hepBTiter = hepBTiter;}
	
	public int getHepCTiter(){return hepCTiter;}
	public void setHepCTiter(int hepCTiter){this.hepCTiter = hepCTiter;}
	/**
	 * 
	 * @return
	 */
	public int getCD4Count(){return cd4Count;}
	/**
	 * 
	 * @return
	 */
	public float getCD4Percent(){return cd4Percent;}
	/**
	 * 
	 * @return
	 */
	public int getViralLoad(){return viralLoad;}
	/**
	 * 
	 * @return
	 */
	public int getHepCViralLoad(){return hepCViralLoad;}
	/**
	 * 
	 * @return
	 */
	public float getGlucose(){return glucose;}
	/**
	 * 
	 * @return
	 */
	public float getTotalCholesterol(){return totalCholesterol;}
	/**
	 * 
	 * @return
	 */
	public float getLDL(){return LDL;}
	/**
	 * 
	 * @return
	 */
	public float getHDL(){return HDL;}
	/**
	 * 
	 * @return
	 */
	public float getTriglyceride(){return triglyceride;}
	/**
	 * 
	 * @return
	 */
	public int getHeartRate(){return heartRate;}
	/**
	 * 
	 * @return
	 */
	public int getSystole(){return systole;}
	/**
	 * 
	 * @return
	 */
	public int getDiastole(){return diastole;}
	/**
	 * 
	 * @return
	 */
	public float getOxygenLevel(){return oxygenLevel;}
	
	
	public int getPlateletCount(){return plateletCount;}
	
	public int getWhiteCellCount(){return whiteCellCount;}
	
	public int getRedCellCount(){return redCellCount;}
	
	public int getHaemoglobulin(){return haemoglobulin;}
	

	/**
	 * set CD4 count
	 * @param cd4Count
	 */
	public void setCD4Count(int cd4Count){this.cd4Count = cd4Count;}
	/**
	 * 
	 * @param cd4Percent
	 */
	public void setCD4Percent(float cd4Percent){this.cd4Percent = cd4Percent;}
	/**
	 * 
	 * @param viralLoad
	 */
	public void setViralLoad(int viralLoad){this.viralLoad = viralLoad;}
	/**
	 * 
	 * @param hepCViralLoad
	 */
	public void setHepCViralLoad(int hepCViralLoad){this.hepCViralLoad = hepCViralLoad;}
	/**
	 * 
	 * @param glucose
	 */
	public void setGlucose(float glucose){this.glucose = glucose;}
	/**
	 * 
	 * @param totalCholesterol
	 */
	public void setTotalCholesterol(float totalCholesterol){this.totalCholesterol = totalCholesterol;}
	/**
	 * 
	 * @param LDL
	 */
	public void setLDL(float LDL){this.LDL = LDL;}
	/**
	 * 
	 * @param HDL
	 */
	public void setHDL(float HDL){this.HDL = HDL;}
	/**
	 * 
	 * @param triglyceride
	 */
	public void setTriglyceride(float triglyceride){this.triglyceride = triglyceride;}
	/**
	 * 
	 * @param heartRate
	 */
	public void setHeartRate(int heartRate){this.heartRate = heartRate;}
	/**
	 * 
	 * @param systole
	 */
	public void setSystole(int systole){this.systole = systole;}
	/**
	 * 
	 * @param diastole
	 */
	public void setDiastole(int diastole){this.diastole = diastole;}
	/**
	 * 
	 * @param oxygenLevel
	 */
	public void setOxygenLevel(float oxygenLevel){this.oxygenLevel = oxygenLevel;}


	public void setPlateletCount(int plateletCount){this.plateletCount = plateletCount;}
	
	public void setWhiteCellCount(int whiteCellCount){this.whiteCellCount = whiteCellCount;}
	
	public void setRedCellCount(int redCellCount){this.redCellCount = redCellCount;}
	
	public void setHaemoglobulin(int haemoglobulin){this.haemoglobulin = haemoglobulin;}

    public ContentValues contentValuesForResult()
    {
		ContentValues content = new ContentValues();
		content.put(iStayHealthyDatabaseSchema.CD4, cd4Count);
		content.put(iStayHealthyDatabaseSchema.CD4PERCENT, cd4Percent);
		content.put(iStayHealthyDatabaseSchema.VIRALLOAD, viralLoad);
		content.put(iStayHealthyDatabaseSchema.HEPCVIRALLOAD, hepCViralLoad);
		content.put(iStayHealthyDatabaseSchema.GLUCOSE, glucose);
		content.put(iStayHealthyDatabaseSchema.HDL, HDL);
		content.put(iStayHealthyDatabaseSchema.LDL, LDL);
		content.put(iStayHealthyDatabaseSchema.TOTALCHOLESTEROL, totalCholesterol);
		content.put(iStayHealthyDatabaseSchema.DIASTOLE, diastole);
		content.put(iStayHealthyDatabaseSchema.SYSTOLE, systole);
		content.put(iStayHealthyDatabaseSchema.HEARTRATE, heartRate);
		content.put(iStayHealthyDatabaseSchema.PLATELETCOUNT, plateletCount);
		content.put(iStayHealthyDatabaseSchema.WHITECELLCOUNT, whiteCellCount);
		content.put(iStayHealthyDatabaseSchema.REDCELLCOUNT, redCellCount);
		content.put(iStayHealthyDatabaseSchema.HAEMOGLOBULIN, haemoglobulin);

		content.put(iStayHealthyDatabaseSchema.WEIGHT, weight);
		content.put(iStayHealthyDatabaseSchema.CHOLESTEROLRATIO, cholesterolRatio);
		content.put(iStayHealthyDatabaseSchema.CARDIACRISKFACTOR, cardiacRiskFactor);
		content.put(iStayHealthyDatabaseSchema.HEPBTITER, hepBTiter);
		content.put(iStayHealthyDatabaseSchema.HEPCTITER, hepCTiter);
	
		content.put(iStayHealthyDatabaseSchema.liverAlanineTransaminase, liverAlanineTransaminase);
		content.put(iStayHealthyDatabaseSchema.liverAspartateTransaminase, liverAspartateTransaminase);
		content.put(iStayHealthyDatabaseSchema.liverAlkalinePhosphatase, liverAlkalinePhosphatase);
		content.put(iStayHealthyDatabaseSchema.liverAlbumin, liverAlbumin);
		content.put(iStayHealthyDatabaseSchema.liverAlanineTotalBilirubin, liverAlanineTotalBilirubin);
		content.put(iStayHealthyDatabaseSchema.liverAlanineDirectBilirubin, liverAlanineDirectBilirubin);
		content.put(iStayHealthyDatabaseSchema.liverGammaGlutamylTranspeptidase, liverGammaGlutamylTranspeptidase);
		
		content.put(iStayHealthyDatabaseSchema.RESULTSDATE, time);
		content.put(iStayHealthyDatabaseSchema.GUIDTEXT, GUID);
    	return content;
    }
    
    public void setResult(Cursor cursor)
    {
		setCD4Count(cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CD4)));
		setCD4Percent(cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CD4PERCENT)));
		setViralLoad(cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.VIRALLOAD)));
		setHepCViralLoad(cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HEPCVIRALLOAD)));
		setTime(cursor.getLong(cursor.getColumnIndex(iStayHealthyDatabaseSchema.RESULTSDATE)));
		setGUID(cursor.getString(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GUIDTEXT)));
		float glucose_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.GLUCOSE));
		setGlucose(glucose_p);
		int systole_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.SYSTOLE));
		setSystole(systole_p);
		int diastole_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.DIASTOLE));
		setDiastole(diastole_p);
		float hdl_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HDL));
		setHDL(hdl_p);
		float ldl_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.LDL));
		setLDL(ldl_p);
		float cholesterol = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.TOTALCHOLESTEROL));
		setTotalCholesterol(cholesterol);
		int rate_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HEARTRATE));
		setHeartRate(rate_p);
		int platelet_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.PLATELETCOUNT));
		setPlateletCount(platelet_p);
		int whiteCell_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WHITECELLCOUNT));
		setWhiteCellCount(whiteCell_p);
		int redCell_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.REDCELLCOUNT));
		setRedCellCount(redCell_p);
		int haemo_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HAEMOGLOBULIN));
		setHaemoglobulin(haemo_p);		
		float weight_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.WEIGHT));
		setWeight(weight_p);
		float cholesterolRatio_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CHOLESTEROLRATIO));
		setCholesterolRatio(cholesterolRatio_p);
		
		float risk_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.CARDIACRISKFACTOR));
		setCardiacRiskFactor(risk_p);
		int hepBTiter_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HEPBTITER));
		setHepBTiter(hepBTiter_p);
		int hepCTiter_p = cursor.getInt(cursor.getColumnIndex(iStayHealthyDatabaseSchema.HEPCTITER));
		setHepCTiter(hepCTiter_p);
		
		float liverAlanineTransaminase_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAlanineTransaminase));
		setLiverAlanineTransaminase(liverAlanineTransaminase_p);
		float liverAspartateTransaminase_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAspartateTransaminase));
		setLiverAspartateTransaminase(liverAspartateTransaminase_p);
		float liverAlkalinePhosphatase_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAlkalinePhosphatase));
		setLiverAlkalinePhosphatase(liverAlkalinePhosphatase_p);
		float liverAlbumin_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAlbumin));
		setLiverAlbumin(liverAlbumin_p);
		float liverAlanineTotalBilirubin_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAlanineTotalBilirubin));
		setLiverAlanineTotalBilirubin(liverAlanineTotalBilirubin_p);
		float liverAlanineDirectBilirubin_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverAlanineDirectBilirubin));
		setLiverAlanineDirectBilirubin(liverAlanineDirectBilirubin_p);
		float liverGammaGlutamylTranspeptidase_p = cursor.getFloat(cursor.getColumnIndex(iStayHealthyDatabaseSchema.liverGammaGlutamylTranspeptidase));
		setLiverGammaGlutamylTranspeptidase(liverGammaGlutamylTranspeptidase_p);
    	
    }
	
}

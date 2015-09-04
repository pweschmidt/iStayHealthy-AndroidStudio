package com.pweschmidt.healthapps.datamodel;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.pweschmidt.healthapps.iStayHealthyDatabaseSchema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Date;

public class Results extends MedicalEvent implements Parcelable
{
	public final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMyy");
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

	public Results(Uri data) {
		String scheme = data.getScheme();
		String host = data.getHost();
		String path = data.getPath();
		if (null != scheme && null != host) {
			if (scheme.equalsIgnoreCase("http") && host.equalsIgnoreCase("www.istayhealthy.uk.com") && path.contains("results")) {
				String parameters = data.getSchemeSpecificPart();
				StringTokenizer tokenizer = new StringTokenizer(parameters, "?");
				String query = null;
				if (2 == tokenizer.countTokens()) {
					tokenizer.nextToken();
					query = tokenizer.nextToken();
				}
				if (null != query) {
					tokenizer = new StringTokenizer(query, "&");
				} else {
					tokenizer = new StringTokenizer(path, "&");
				}
				boolean hasData = false;
				while (tokenizer.hasMoreTokens()) {
					String parameterSet = tokenizer.nextToken();
					StringTokenizer params = new StringTokenizer(parameterSet, "=");
					if (2 == params.countTokens()) {
						String key = params.nextToken();
						String value = params.nextToken();
						if (key.equalsIgnoreCase("cd4")) {
							cd4Count = Integer.parseInt(value);
						} else if (key.equalsIgnoreCase("cd4Percent")) {
							cd4Percent = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("viralload")) {
							if (value.equalsIgnoreCase("undetecable") || value.startsWith("undetectable")) {
								viralLoad = 0;
							} else {
								viralLoad = Integer.parseInt(value);
							}
						} else if (key.equalsIgnoreCase("risk")) {
							cardiacRiskFactor = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("cholesterol")) {
							totalCholesterol = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("hdl")) {
							HDL = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("ldl")) {
							LDL = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("glucose")) {
							glucose = Float.parseFloat(value);
						} else if (key.equalsIgnoreCase("hepCViralLoad")) {
							if (value.equalsIgnoreCase("undetectable") || value.startsWith("undetectable")) {
								hepCViralLoad = 0;
							} else {
								hepCViralLoad = Integer.parseInt(value);
							}
						}
						else if (key.equalsIgnoreCase("resultsDate"))
						{
							Date date = new Date();
							try
							{
								date = dateFormat.parse(value);
								time = date.getTime();
							}
							catch(ParseException ie)
							{
								time = date.getTime();
							}

						}
					}
				}
			}

		}
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

	public Results(Parcel resultsParcel)
	{
		ArrayList<String> data = new ArrayList<String>();
		resultsParcel.readStringList(data);
		this.time = Long.parseLong(data.get(0));
		this.GUID = data.get(1);
		this.cd4Count = Integer.parseInt(data.get(2));
		this.cd4Percent = Float.parseFloat(data.get(3));
		this.viralLoad = Integer.parseInt(data.get(4));
		this.hepCViralLoad = Integer.parseInt(data.get(5));
		this.glucose = Float.parseFloat(data.get(6));
		this.totalCholesterol = Float.parseFloat(data.get(7));
		this.LDL = Float.parseFloat(data.get(8));
		this.HDL = Float.parseFloat(data.get(9));
		this.triglyceride = Float.parseFloat(data.get(10));
		this.heartRate = Integer.parseInt(data.get(11));
		this.systole = Integer.parseInt(data.get(12));
		this.diastole = Integer.parseInt(data.get(13));
		this.oxygenLevel = Float.parseFloat(data.get(14));
		this.plateletCount = Integer.parseInt(data.get(15));
		this.whiteCellCount = Integer.parseInt(data.get(16));
		this.redCellCount = Integer.parseInt(data.get(17));
		this.haemoglobulin = Integer.parseInt(data.get(18));
		this.cholesterolRatio = Float.parseFloat(data.get(19));
		this.cardiacRiskFactor = Float.parseFloat(data.get(20));
		this.hepBTiter = Integer.parseInt(data.get(21));
		this.hepCTiter = Integer.parseInt(data.get(22));
		this.liverAlanineTransaminase = Float.parseFloat(data.get(23));
		this.liverAspartateTransaminase = Float.parseFloat(data.get(24));
		this.liverAlkalinePhosphatase = Float.parseFloat(data.get(25));
		this.liverAlbumin = Float.parseFloat(data.get(26));
		this.liverAlanineTotalBilirubin = Float.parseFloat(data.get(27));
		this.liverAlanineDirectBilirubin = Float.parseFloat(data.get(28));
		this.liverGammaGlutamylTranspeptidase = Float.parseFloat(data.get(29));
		this.weight = Float.parseFloat(data.get(30));

	}

	public void writeToParcel(Parcel parcel, int flags)
	{
		ArrayList<String> data = new ArrayList<String>();
		data.add(0, ""+time);
		data.add(1,GUID);
		data.add(2, ""+cd4Count);
		data.add(3,""+cd4Percent);
		data.add(4, ""+viralLoad);
		data.add(5,""+hepCViralLoad);
		data.add(6, ""+glucose);
		data.add(7,""+totalCholesterol);
		data.add(8, ""+LDL);
		data.add(9,""+HDL);
		data.add(10, ""+triglyceride);
		data.add(11,""+heartRate);
		data.add(12, ""+systole);
		data.add(13,""+diastole);
		data.add(14, ""+time);
		data.add(15,""+oxygenLevel);
		data.add(16, ""+plateletCount);
		data.add(17,""+whiteCellCount);
		data.add(18, ""+redCellCount);
		data.add(19,""+haemoglobulin);
		data.add(20, ""+cholesterolRatio);
		data.add(21,""+cardiacRiskFactor);
		data.add(22, ""+hepBTiter);
		data.add(23,""+liverAlanineTransaminase);
		data.add(24, ""+liverAspartateTransaminase);
		data.add(25,""+liverAlkalinePhosphatase);
		data.add(26, ""+liverAlbumin);
		data.add(27,""+liverAlanineTotalBilirubin);
		data.add(28, ""+liverAlanineDirectBilirubin);
		data.add(29,""+liverGammaGlutamylTranspeptidase);
		data.add(30, ""+weight);
		parcel.writeStringList(data);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Results createFromParcel(Parcel parcel) {
			return new Results(parcel);
		}

		public Results[] newArray(int size) {
			return new Results[size];
		}
	};

	public int describeContents(){return 0;}
}

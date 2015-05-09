package com.pweschmidt.healthapps.graphs;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.datamodel.*;
import java.util.*;

public class ResultsFilter 
{
	public int firstFilter;
	public int secondFilter;	
	public boolean isSingleFilter;
	public int firstColourIndex;
	public int secondColourIndex;
	public boolean showMeds;
	
	public ArrayList<Results>getFilteredResults(ArrayList<Results>results, int resId)
	{
		ArrayList<Results>events = new ArrayList<Results>();
		if(null == results)
			return events;
		if(0 == results.size())
			return events;
		Iterator<Results>iterator = results.iterator();
		while(iterator.hasNext())
		{
			Results result = iterator.next();
			float value = ResultsFilter.getResultValue(result, resId);
			if(0 < value)
			{
				events.add(result);
			}
		}		
		return events;
	}
	
	public static float getResultValue(Results result, int resId)
	{
		float resultValue = -1;
		switch(resId)
		{
		case R.string.CD4Count:
			resultValue = result.getCD4Count();
			break;
		case R.string.CD4Percent:
			resultValue =  result.getCD4Percent();
			break;
		case R.string.ViralLoad:
			resultValue =  result.getViralLoad();
			break;
		case R.string.HepCViralLoad:
			resultValue =  result.getHepCViralLoad();
			break;
		case R.string.HDL:
			resultValue =  result.getHDL();
			break;
		case R.string.LDL:
			resultValue =  result.getLDL();
			break;
		case R.string.sugar:
			resultValue =  result.getGlucose();
			break;
		case R.string.weight:
			resultValue =  result.getWeight();
			break;
		case R.string.systole:
			resultValue =  result.getSystole();			
			break;
		case R.string.diastole:
			resultValue =  result.getDiastole();
			break;
		case R.string.cholesterol:
			resultValue =  result.getTotalCholesterol();
			break;
		case R.string.cholesterolRatio:
			resultValue =  result.getCholesterolRatio();
			break;
		case R.string.cardiacRisk:
			resultValue =  result.getCardiacRiskFactor();
			break;
		case R.string.whitecells:
			resultValue =  result.getWhiteCellCount();
			break;
		case R.string.redcells:
			resultValue =  result.getRedCellCount();
			break;
		case R.string.haemo:
			resultValue =  result.getHaemoglobulin();
			break;
		case R.string.platelet:
			resultValue =  result.getPlateletCount();
			break;
		}
		return resultValue;
	}
}

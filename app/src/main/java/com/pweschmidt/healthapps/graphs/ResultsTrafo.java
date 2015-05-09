package com.pweschmidt.healthapps.graphs;

import com.pweschmidt.healthapps.R;
//import android.util.Log;
public class ResultsTrafo 
{
	public static float MAXVIRALLOAD = 10000000;
	
	/**
	 * starting point on the x axis. when we have less than 10 points we start from the
	 * centre of the chart rather than from the left
	 * @param width
	 * @param count
	 * @return
	 */
	public static float xStart(float width, int count){
		float x = width/(float)count;
		if( 10 > count ){
			x = width/(float)(count + 1);
		}
		if( x < ChartsPageView.MARGINLEFT )
			x = ChartsPageView.MARGINLEFT;
		if( x > width - ChartsPageView.MARGINRIGHT)
			x = width - ChartsPageView.MARGINRIGHT;
		return x;
	}
	
	public static float getLogYOffsetValue(int ticks, float value, float chartHeight)
	{
		float yOffset = 0;
		
		if(0 == value)
			value = 1;
		
		if(value > ResultsTrafo.MAXVIRALLOAD)
			value = ResultsTrafo.MAXVIRALLOAD;
		
		double dMaxY = Math.floor(chartHeight / (ticks + 1));
		dMaxY = dMaxY * ticks;
		

		double dValue = Math.log10(value);
		double dMax = Math.log10(ResultsTrafo.MAXVIRALLOAD);
		double dOffset = dValue * (dMaxY / dMax);
		yOffset = chartHeight + ChartsPageView.MARGINTOP - (float)dOffset;
		/*
		float correctedValue = value - range.realMin;
		if(correctedValue <= 0)
			correctedValue = 1;
		
		float correctedMaxValue = range.realMax - range.realMin;
		if(correctedMaxValue <= 0)
			correctedMaxValue = 1;

		double dCorrected = Math.log10(correctedValue);
		double dCorrectedMax = Math.log10(correctedMaxValue);
		double dYOffset = dCorrected * ( chartHeight / dCorrectedMax );
		yOffset = chartHeight + ChartsPageView.MARGINTOP - (float)dYOffset;
		if(0 > yOffset)yOffset = 0;
		*/
		return yOffset;
	}
	
	public static float getLinearYOffsetValue(int ticks, float value, ValueRange range, float chartHeight)
	{
		float correctedValue = value - range.realMin;
		if(0 >= correctedValue)
			return chartHeight;
		
		float correctedMaxValue = range.realMax - range.realMin;
		double dMaxY = Math.floor(chartHeight / ticks);
		dMaxY = dMaxY * ticks;
		
		
		float yOffset = correctedValue * ( (float)dMaxY / correctedMaxValue );
		yOffset = chartHeight + ChartsPageView.MARGINTOP - yOffset;
		if(0 > yOffset)yOffset = 0;
		return yOffset;
	}
	
	public static ValueRange getValueRangeForLogValues(int ticks, float min, float max)
	{
		ValueRange range = new ValueRange();
		float realMin = min;
		float realMax = max;
		float divisor = 1000000;
		if(max < 1000000)
			divisor = 100000;
		else if(max < 100000)
			divisor = 10000;
		else if(max < 10000)
			divisor = 1000;
		else if(max < 1000)
			divisor = 100;
		else 
			divisor = 10;
	
		double scale = max / divisor;
		scale = Math.ceil(scale);
		realMax = (float)scale * divisor;
		
		if(min > 0)
		{
			double dMin = min / scale;
			dMin = Math.floor(dMin);
			realMin = (float)(dMin * scale);
		}
				
		range.realMax = realMax;
		range.realMin = realMin;
		range.realTickDistance = (realMax - realMin) / ticks;
		return range;
	}
	
	
	public static ValueRange getValueRange(int ticks, float min, float max)
	{
		ValueRange range = new ValueRange();
		
		float realMin = min;
		float realMax = max;
		
		float midMax = max/2;
		if(midMax < realMin)
			realMin = midMax;
		
		if(10 < max)
		{
			double scale = realMax / 10;
			scale = Math.ceil(scale);
			realMax = (float)scale * 10;
			
			double dMin = realMin / scale;
			dMin = Math.floor(dMin);
			realMin = (float)(dMin * scale);
		}
		else
		{
			double dMax = Math.ceil(realMax);
			if(dMax % 2 > 0.0)
				dMax += 1.0;
			if(dMax > 10.0)dMax = 10.0;
			realMax = (float)dMax;
			
			double dMin = Math.floor(realMin);
			if(dMin % 2 > 0.0)
				dMin -= 1.0;
			if(dMin < 0.0)dMin = 0.0;
			realMin = (float)dMin;			
			
		}
		
		float diff = realMax - realMin;
		if(10 < diff)
		{
			int divisor = (int)(diff / ticks);
			realMax = realMin + (divisor * ticks);
		}
		
		range.realMax = realMax;
		range.realMin = realMin;
		range.realTickDistance = (realMax - realMin) / ticks;
		return range;
	}
	
	public static float maxRangeForType(int graphType)
	{
		float maxRange = 0;
		switch(graphType)
		{
		case R.string.CD4Count:
			maxRange = 2000;
			break;
		case R.string.CD4Percent:
			maxRange = 100;			
			break;
		case R.string.ViralLoad:
			maxRange = 10000000;
			break;
		case R.string.HepCViralLoad:
			maxRange = 10000000;
			break;
		case R.string.HDL:
			maxRange = 100;			
			break;
		case R.string.LDL:
			maxRange = 100;			
			break;
		case R.string.sugar:
			maxRange = 100;			
			break;
		case R.string.weight:
			maxRange = 500;
			break;
		case R.string.systole:
			maxRange = 500;
			break;
		case R.string.diastole:
			maxRange = 500;
			break;
		case R.string.cholesterol:
			break;
		case R.string.cholesterolRatio:
			maxRange = 100;			
			break;
		case R.string.cardiacRisk:
			maxRange = 100;			
			break;
		case R.string.whitecells:
			maxRange = 100;			
			break;
		case R.string.haemo:
			maxRange = 100;			
			break;
		case R.string.redcells:
			maxRange = 1000;
			break;
		case R.string.platelet:		
			maxRange = 1000;
			break;
		}
		return maxRange;
	}
	
}

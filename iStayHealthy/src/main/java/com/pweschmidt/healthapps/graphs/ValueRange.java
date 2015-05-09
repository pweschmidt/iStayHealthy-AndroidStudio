package com.pweschmidt.healthapps.graphs;

public class ValueRange 
{
	public float realMax;
	public float realMin;
	public float realTickDistance;
	public static final int MINSTART = 99999999;
	public static final int MAXSTART = 0;
	
	/**
	 * add 20% to the top value and round to nearest multiple of 10
	 * @param max
	 * @return
	 */
	public static float maxRange(float max)
	{
		double newMax = Math.floor(max * 1.2);
		if(newMax > ValueRange.MINSTART)
			newMax = ValueRange.MINSTART;
		return (float)newMax;
	}
	
	/**
	 * remove 20% from min and round to nearest multiple of 10
	 * @param min
	 * @return
	 */
	public static float minRange(float min)
	{
		if(ValueRange.MAXSTART > min)
			return ValueRange.MAXSTART;

		double newMin = Math.floor(min * 0.8);
		if(newMin <= ValueRange.MAXSTART)
			newMin = ValueRange.MAXSTART;
		
		return (float)newMin;
	}
	
}

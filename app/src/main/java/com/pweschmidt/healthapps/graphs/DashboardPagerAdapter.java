package com.pweschmidt.healthapps.graphs;

import android.support.v4.view.PagerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import com.pweschmidt.healthapps.DashboardActivity;
import com.pweschmidt.healthapps.R;

import android.content.Context;
import android.database.Cursor;
//import android.view.ViewGroup;
import android.support.v4.view.ViewPager;
import android.view.View;
//import android.util.Log;
import com.pweschmidt.healthapps.datamodel.*;

public class DashboardPagerAdapter extends PagerAdapter
{
	public static final int DASHBOARDCD4VL = 0x0;
	public static final int DASHBOARDCD4PERCENTVL = 0x1;
	public static final int DASHBOARDPRESSURE = 0x6;
	public static final int DASHBOARDRISK = 0x3;
	public static final int DASHBOARDCHOLESTEROL = 0x2;
	public static final int DASHBOARDWEIGHT = 0x5;
	public static final int DASHBOARDSUGAR = 0x4;
//	private static final String TAG = "DashboardPagerAdapter";
	private Context context;
	private List<DashboardCustomPageViewLayout> dashboardPages;
//	private int numberOfGraphs;
	
	
	public DashboardPagerAdapter(Context _context)
	{
		context = _context;
		dashboardPages = new ArrayList<DashboardCustomPageViewLayout>();
		for(int i = 0 ; i < DashboardActivity.NUMBEROFSUPPORTEDGRAPHS; i++)
		{
			DashboardCustomPageViewLayout page = new DashboardCustomPageViewLayout(context);
			page.setFilters(getFilter(i, context));
			dashboardPages.add(page);						
		}
				
	}	
	
	public static ResultsFilter getFilter(int index, Context context)
	{
		ResultsFilter filter = new ResultsFilter();
		switch(index)
		{
		case DASHBOARDCD4VL:
			filter.firstFilter = R.string.CD4Count;
			filter.secondFilter = R.string.ViralLoad;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkyellow);
			filter.secondColourIndex = context.getResources().getColor(R.color.darkblue);
			filter.isSingleFilter = false;
			filter.showMeds = true;
			break;
		case DASHBOARDCD4PERCENTVL:
			filter.firstFilter = R.string.CD4Percent;
			filter.secondFilter = R.string.ViralLoad;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkyellow);
			filter.secondColourIndex = context.getResources().getColor(R.color.darkblue);
			filter.isSingleFilter = false;
			filter.showMeds = true;
			break;
		case DASHBOARDCHOLESTEROL:
			filter.firstFilter = R.string.cholesterol;
			filter.secondFilter = R.string.HDL;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkgreen);
			filter.secondColourIndex = context.getResources().getColor(R.color.darkred);
			filter.isSingleFilter = false;
			filter.showMeds = true;
			break;
		case DASHBOARDRISK:
			filter.firstFilter = R.string.cardiacRisk;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkblue);
			filter.isSingleFilter = true;
			filter.showMeds = true;
			break;
		case DASHBOARDSUGAR:
			filter.firstFilter = R.string.sugar;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkred);
			filter.isSingleFilter = true;
			filter.showMeds = true;
			break;
		case DASHBOARDWEIGHT:
			filter.firstFilter = R.string.weight;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkgreen);
			filter.isSingleFilter = true;
			filter.showMeds = true;
			break;
		case DASHBOARDPRESSURE:
			filter.firstFilter = R.string.systole;
			filter.firstColourIndex = context.getResources().getColor(R.color.darkgreen);
			filter.isSingleFilter = true;
			filter.showMeds = true;
			break;		
		}
		return filter;
	}
		
	
	public int getCount() 
	{
		return DashboardActivity.NUMBEROFSUPPORTEDGRAPHS;
	}

	public boolean isViewFromObject(View view, Object page) 
	{
		return (view == (View)page);
	}
	
	public void destroyItem(View container, int position, Object chart)
	{
		((ViewPager)container).removeView((DashboardCustomPageViewLayout)chart);
	}
	
	public Object instantiateItem(View container,int position )
	{
//		Log.d(TAG,"WE ARE IN instantiateItem for pageView = "+position);
		DashboardCustomPageViewLayout chart = (DashboardCustomPageViewLayout)dashboardPages.get(position);
		((ViewPager)container).addView(chart);
		return chart;
	}
	
	public void hideHeaders()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.hideHeader();
		}		
	}
	
	public void showHeaders()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.showHeader();
		}		
		
	}

	public void clearResults()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.clearResults();			
		}
		
	}
	
	public void clearMedication()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.clearMedication();			
		}		
	}
	
	public void clearMissedMedication()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.clearMissedMedication();			
		}		
	}
	
	public void clearAllData()
	{
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.reset();
		}				
	}
	
	
	
	
	public void setResultsFromCursor(Cursor cursor)
	{
		ArrayList<Results>results = new ArrayList<Results>();
		if(null == cursor)
			return;
		if(0 == cursor.getCount())
			return;
		if(!cursor.moveToFirst())
			return;
		while(!cursor.isAfterLast())
		{
			Results result = new Results();
			result.setResult(cursor);
			results.add(result);
			cursor.moveToNext();
		}	
		
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.updateResults(results);
		}
	}
	
	public void setMedicationsFromCursor(Cursor cursor)
	{
		ArrayList<Medication>meds = new ArrayList<Medication>();
		if(null == cursor)
			return;
		if(0 == cursor.getCount())
			return;
		if(!cursor.moveToFirst())
			return;
		while(!cursor.isAfterLast())
		{
			Medication med = new Medication();
			med.setMedication(cursor);
			meds.add(med);
			cursor.moveToNext();
		}		
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.updatedMedication(meds);
		}
	}
	
	public void setMissedMedicationsFromCursor(Cursor cursor)
	{
		ArrayList<MissedMedication>missed = new ArrayList<MissedMedication>();
		if(null == cursor)
			return;
		if(0 == cursor.getCount())
			return;
		if(!cursor.moveToFirst())
			return;
		while(!cursor.isAfterLast())
		{
			MissedMedication missedMed = new MissedMedication();
			missedMed.setMedication(cursor);
			missed.add(missedMed);
			cursor.moveToNext();
		}		
		Iterator<DashboardCustomPageViewLayout>iterator = dashboardPages.iterator();
		while(iterator.hasNext())
		{
			DashboardCustomPageViewLayout dashboard = iterator.next();
			dashboard.updateMissedMedication(missed);
		}
	}
	
	public void setPreviousMedicationsFromCursor(Cursor cursor)
	{
		ArrayList<PreviousMedication>previous = new ArrayList<PreviousMedication>();
		if(null == cursor)
			return;
		if(0 == cursor.getCount())
			return;
		if(!cursor.moveToFirst())
			return;
		while(!cursor.isAfterLast())
		{
			PreviousMedication previousMed = new PreviousMedication();
			previousMed.setMedication(cursor);
			previous.add(previousMed);
			cursor.moveToNext();
		}		
	}
	
}

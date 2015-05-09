package com.pweschmidt.healthapps.graphs;

import com.pweschmidt.healthapps.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.*;

import com.pweschmidt.healthapps.datamodel.*;

public class DashboardCustomPageViewLayout extends LinearLayout 
{
	private View dashboardView;
    private ChartsPageView chartView;
    private TableRow firstRow;
    private TableRow secondRow;
	private TextView title;
	private TextView value;
	private TextView change;
	private IndicatorView indicator;

	private TextView optionaltitle;
	private TextView optionalvalue;
	private TextView optionalchange;
	private IndicatorView optionalindicator;
	private ResultsFilter filter;
	private Context context;

    public DashboardCustomPageViewLayout(Context context)
	{
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dashboardView = inflater.inflate(R.layout.dashboard_custom_page_view, this);
		if(null != dashboardView)
		{
			chartView = (ChartsPageView)dashboardView.findViewById(R.id.chartView);
			firstRow = (TableRow)dashboardView.findViewById(R.id.firstHeaderRow);
			secondRow = (TableRow)dashboardView.findViewById(R.id.secondHeaderRow);			
			title = (TextView)dashboardView.findViewById(R.id.summaryTitle);
			value = (TextView)dashboardView.findViewById(R.id.summaryValue);
			change = (TextView)dashboardView.findViewById(R.id.summaryChange);
			indicator = (IndicatorView)dashboardView.findViewById(R.id.summaryIndicator);
			optionaltitle = (TextView)dashboardView.findViewById(R.id.optionalTitle);
			optionalvalue = (TextView)dashboardView.findViewById(R.id.optionalValue);
			optionalchange = (TextView)dashboardView.findViewById(R.id.optionalChange);
			optionalindicator = (IndicatorView)dashboardView.findViewById(R.id.optionalIndicator);
		}
	}
	
	public DashboardCustomPageViewLayout(Context context, AttributeSet set)
	{
		super(context, set);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dashboardView = inflater.inflate(R.layout.dashboard_custom_page_view, this);
		if(null != dashboardView)
		{
			chartView = (ChartsPageView)dashboardView.findViewById(R.id.chartView);
			firstRow = (TableRow)dashboardView.findViewById(R.id.firstHeaderRow);
			secondRow = (TableRow)dashboardView.findViewById(R.id.secondHeaderRow);			
			title = (TextView)dashboardView.findViewById(R.id.summaryTitle);
			value = (TextView)dashboardView.findViewById(R.id.summaryValue);
			change = (TextView)dashboardView.findViewById(R.id.summaryChange);
			indicator = (IndicatorView)dashboardView.findViewById(R.id.summaryIndicator);
			optionaltitle = (TextView)dashboardView.findViewById(R.id.optionalTitle);
			optionalvalue = (TextView)dashboardView.findViewById(R.id.optionalValue);
			optionalchange = (TextView)dashboardView.findViewById(R.id.optionalChange);
			optionalindicator = (IndicatorView)dashboardView.findViewById(R.id.optionalIndicator);
		}
	}
	
	public void setFilters(ResultsFilter filter)
	{
		if(null == filter)
			return;
		
		chartView.setResultsFilter(filter);
		this.filter = filter;
		title.setText(filter.firstFilter);
		if(filter.firstFilter == R.string.systole)
		{
			title.setText(R.string.pressure);
		}
		title.setTextColor(filter.firstColourIndex);
		value.setTextColor(filter.firstColourIndex);	
		if(!filter.isSingleFilter)
		{
			optionaltitle.setText(filter.secondFilter);
			optionaltitle.setTextColor(filter.secondColourIndex);
			optionalvalue.setTextColor(filter.secondColourIndex);			
		}		
	}
	
	public void hideHeader()
	{
		firstRow.setVisibility(View.GONE);
		secondRow.setVisibility(View.GONE);
		chartView.showTitles();
	}
	
	public void showHeader()
	{
		firstRow.setVisibility(View.VISIBLE);
		secondRow.setVisibility(View.VISIBLE);
		chartView.hideTitles();
	}
	
	public void updateResults(ArrayList<Results>results)
	{
		chartView.setResults(results);
		setResults(results);
	}
	
	public void updatedMedication(ArrayList<Medication>meds)
	{
		chartView.setMedication(meds);
	}
	
	public void updateMissedMedication(ArrayList<MissedMedication>missed)
	{
		chartView.setMissedMedication(missed);
	}
		
	public void reset()
	{
		if(null != chartView)
			chartView.clearTimeline();
	}
	
	public void clearResults()
	{
		if(null != chartView)
			chartView.clearResults();
	}
	
	public void clearMedication()
	{
		if(null != chartView)
			chartView.clearMeds();
	}
	public void clearMissedMedication()
	{
		if(null != chartView)
			chartView.clearMissed();
	}
	
	private void setResults(ArrayList<Results>results)
	{
		if(null == results || null == filter)
		{
			value.setText(R.string.NoResultsListed);
			if(!filter.isSingleFilter)
				optionalvalue.setText(R.string.NoResultsListed);
			return;
		}
		if(0 == results.size())
		{
			value.setText(R.string.NoResultsListed);
			if(!filter.isSingleFilter)
				optionalvalue.setText(R.string.NoResultsListed);
			return;
		}
		ArrayList<Results>first = filter.getFilteredResults(results, filter.firstFilter);
		if(0 == first.size())
		{
			value.setText(R.string.NoResultsListed);			
		}
		else
		{
			int lastIndex = first.size() - 1;
			int previousIndex = first.size() - 2;
			Results last = first.get(lastIndex);
			float value = ResultsFilter.getResultValue(last, filter.firstFilter);
			setValueText(value, filter.firstFilter, true, false);
			Results previous = null;
			if(0 <= previousIndex)
			{
				previous = first.get(previousIndex);
				float prevValue = ResultsFilter.getResultValue(previous, filter.firstFilter);
				float diff = value - prevValue;
				setValueText(diff, filter.firstFilter, true, true);
				setIndicator(diff, filter.firstFilter, false);
			}
		}
		
		
		if(!filter.isSingleFilter)
		{
			ArrayList<Results>second = filter.getFilteredResults(results, filter.secondFilter);
			if(0 == second.size())
			{
				optionalvalue.setText(R.string.NoResultsListed);
			}
			else
			{
				int lastIndex = second.size() - 1;
				int previousIndex = second.size() - 2;
				Results last = second.get(lastIndex);
				float value = ResultsFilter.getResultValue(last, filter.secondFilter);
				setValueText(value, filter.secondFilter, false, false);
				Results previous = null;
				if(0 <= previousIndex)
				{
					previous = second.get(previousIndex);
					float prevValue = ResultsFilter.getResultValue(previous, filter.secondFilter);
					float diff = value - prevValue;
					setValueText(diff, filter.secondFilter, false, true);
					setIndicator(diff, filter.secondFilter, true);
				}
				
			}
		}
		
		
	}
	
	
	private void setValueText(float resultValue, int stringResId, boolean isFirst, boolean isChange)
	{
		String valueText = "";
		switch(stringResId)
		{
		case R.string.CD4Count:
			valueText = new String(String.format("%d", (int)resultValue));
			break;
		case R.string.CD4Percent:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.ViralLoad:
			if(isChange)
			{
				valueText = new String(String.format("%d", (int)resultValue));				
			}
			else
			{
				if(10 < resultValue)
					valueText = new String(String.format("%d", (int)resultValue));
				else
				{
					valueText = context.getApplicationContext().getResources().getString(R.string.und);
				}				
			}
			break;
		case R.string.HepCViralLoad:
			if(isChange)
			{
				valueText = new String(String.format("%d", (int)resultValue));				
			}
			else
			{
				if(10 < resultValue)
					valueText = new String(String.format("%d", (int)resultValue));
				else
				{
					valueText = context.getApplicationContext().getResources().getString(R.string.und);
				}				
			}
			break;
		case R.string.HDL:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.LDL:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.sugar:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.weight:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.systole:
			valueText = new String(String.format("%3d", (int)resultValue));
			break;
		case R.string.diastole:
			valueText = new String(String.format("%3d", (int)resultValue));
			break;
		case R.string.cholesterol:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.cholesterolRatio:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.cardiacRisk:
			valueText = new String(String.format("%3.2f", resultValue));
			break;
		case R.string.whitecells:
			valueText = new String(String.format("%d", (int)resultValue));
			break;
		case R.string.redcells:
			valueText = new String(String.format("%d", (int)resultValue));
			break;
		case R.string.haemo:
			valueText = new String(String.format("%d", (int)resultValue));
			break;
		case R.string.platelet:
			valueText = new String(String.format("%d", (int)resultValue));
			break;		
		}
		
		if(isFirst)
		{
			/*
			*/
			if(isChange)
			{
				if(0 < resultValue)
				{
					String plusValue = new String("+"+valueText);
					change.setText(plusValue);
				}
				else
				{
					change.setText(valueText);					
				}
			}
			else
				value.setText(valueText);			
		}
		else
		{
			/*
			*/
			if(isChange)
			{
				if(0 < resultValue)
				{
					String plusValue = new String("+"+valueText);
					optionalchange.setText(plusValue);
				}
				else
				{
					optionalchange.setText(valueText);					
				}
			}
			else
				optionalvalue.setText(valueText);
		}
	}
	
	private void setIndicator(float change, int resId, boolean isSecondGraph)
	{
		boolean upIsBetter = true;
		switch(resId)
		{
		case R.string.ViralLoad:
			upIsBetter = false;
			break;
		case R.string.HepCViralLoad:
			upIsBetter = false;
			break;
		case R.string.LDL:
			upIsBetter = false;
			break;
		case R.string.sugar:
			upIsBetter = false;
			break;
		case R.string.weight:
			upIsBetter = false;
			break;
		case R.string.cholesterol:
			upIsBetter = false;
			break;
		case R.string.cardiacRisk:
			upIsBetter = false;
			break;
		}
		
		if(upIsBetter)
		{
			if(0 < change)
			{
				if(isSecondGraph)
					optionalindicator.setIndicator(new ResultsIndicator(ResultsIndicator.UP, ResultsIndicator.BETTER));					
				else
					indicator.setIndicator(new ResultsIndicator(ResultsIndicator.UP, ResultsIndicator.BETTER));										
			}
			else if(0 > change)
			{
				if(isSecondGraph)
					optionalindicator.setIndicator(new ResultsIndicator(ResultsIndicator.DOWN, ResultsIndicator.WORSE));										
				else
					indicator.setIndicator(new ResultsIndicator(ResultsIndicator.DOWN, ResultsIndicator.WORSE));										
					
			}			
		}
		else
		{
			if(0 < change)
			{
				if(isSecondGraph)
					optionalindicator.setIndicator(new ResultsIndicator(ResultsIndicator.UP, ResultsIndicator.WORSE));					
				else
					indicator.setIndicator(new ResultsIndicator(ResultsIndicator.UP, ResultsIndicator.WORSE));					
			}
			else if(0 > change)
			{
				if(isSecondGraph)
					optionalindicator.setIndicator(new ResultsIndicator(ResultsIndicator.DOWN, ResultsIndicator.BETTER));										
				else
					indicator.setIndicator(new ResultsIndicator(ResultsIndicator.DOWN, ResultsIndicator.BETTER));															
			}			
		}
		
	}
	
	
}

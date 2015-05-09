package com.pweschmidt.healthapps.graphs;

import android.view.View;
//import android.view.View.MeasureSpec;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
//import android.util.Log;
import android.util.AttributeSet;
//import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import android.text.format.*;
import com.pweschmidt.healthapps.R;
import com.pweschmidt.healthapps.datamodel.*;

public class ChartsPageView extends View 
{
//	private static final String TAG = "ChartsPageView";
//	private Context context;
	private int redColour;
//	private int greenColour;
//	private int amberColour;
//	private int blueColour;
	private int whichFrame;
	private Canvas canvas;
	private boolean isInTestingMode;
	public static final int MARGINLEFT = 29;
	public static final int MARGINRIGHT = 25;
	public static final int MARGINTOP = 25;
	public static final int MARGINBOTTOM = 25;
	public static final float INSET = 2;
	public static final float ROUNDNESS = 10;
	public static final float INSETOFFSET = (float)1.5;
	public static final float MAJORTICKLENGTH = 9;
	public static final float TICKWIDTH = (float)1.5;
	public static final float TEXTOFFSET = (float)3.5;
	public static final float TEXTSIZE = 12;
	public static final float TITLESIZE = 16;
	public static final float EXPTEXTSIZE = 10;
	
	public static final float SMALLTEXTSIZE = 9;
	public static final float SMALLEXPTEXTSIZE = 7;
	public static final float SMALLTITLESIZE = 12;
	
	public static final float STROKEWIDTH = 2;
	public static final float TEXTSTROKEWIDTH = 1;
	public static final int YAXISTICKS = 8;
	public static final int SMALLYAXISTICKS = 4;
			
	private int parentWidth;
	private int parentHeight;
	private Paint textPaint;
	private Paint exponentPaint;
	private Paint axisPaint;
	private Paint timelinePaint;
	private Paint titlePaint;
	private Paint medLinePaint;
	private Paint missedMedLinePaint;
	public int chartWidth;
	public int chartHeight;
	private Context context;
	private ValueRange firstValueRange;
	private ValueRange secondValueRange;
//	private float[] dashedPattern = {2,3};
	private float[] medDashedPattern = {6,3};
//	private float[] dateDashedPattern = {1,4,(float)2.5};
	private final DashPathEffect medDash = new DashPathEffect(medDashedPattern, 2);

	private ArrayList<Long>timeline;
	private HashMap<Long, MedicalEvent>firstGraph;
	private HashMap<Long, MedicalEvent>secondGraph;
	private ResultsFilter filter;
	private boolean useSmallSize;
	private boolean useTitles;
	
	public ChartsPageView(Context _context)
	{
		super(_context);
		context = _context;
		timeline = new ArrayList<Long>();
		firstGraph = new HashMap<Long,MedicalEvent>();
		secondGraph = new HashMap<Long, MedicalEvent>();
		isInTestingMode = false;
		useTitles = false;
		useSmallSize = false;
		String screen = context.getResources().getString(R.string.screenType);
		if(screen.startsWith("smallphone"))
			useSmallSize = true;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		firstValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		secondValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		setGraphCharacteristics();
		setUpAxisPaint();	
	}
	
	public ChartsPageView(Context _context, AttributeSet set)
	{
		super(_context, set);
		context = _context;
		isInTestingMode = false;
		timeline = new ArrayList<Long>();
		firstGraph = new HashMap<Long,MedicalEvent>();
		secondGraph = new HashMap<Long, MedicalEvent>();
		useTitles = false;
		useSmallSize = false;
		String screen = context.getResources().getString(R.string.screenType);
		if(screen.startsWith("smallphone"))
			useSmallSize = true;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		firstValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		secondValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		setGraphCharacteristics();
		setUpAxisPaint();	
	}

	public ChartsPageView(Context _context, AttributeSet set, int defaultStyle)
	{
		super(_context, set, defaultStyle);
		context = _context;
		isInTestingMode = false;
		timeline = new ArrayList<Long>();
		firstGraph = new HashMap<Long,MedicalEvent>();
		secondGraph = new HashMap<Long, MedicalEvent>();
		useTitles = false;
		useSmallSize = false;
		String screen = context.getResources().getString(R.string.screenType);
		if(screen.startsWith("smallphone"))
			useSmallSize = true;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		firstValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		secondValueRange = ResultsTrafo.getValueRange(ticks, 0, 100);
		setGraphCharacteristics();
		setUpAxisPaint();	
	}
	
	public void testFrame(int whichFrame)
	{
		this.whichFrame = whichFrame;
		isInTestingMode = true;
		setUpAxisPaint();
	}
	
	public void hideTitles()
	{
		useTitles = false;		
		invalidate();
	}
	
	public void showTitles()
	{
		useTitles = true;
		invalidate();
	}
	
	public void clearTimeline()
	{
		timeline.clear();
		firstGraph.clear();
		secondGraph.clear();
		invalidate();
	}
	
	public void clearResults()
	{
		if(0 == timeline.size())
			return;
		ArrayList<Long>tmpTimeline = new ArrayList<Long>();
		HashMap<Long, MedicalEvent>tmpGraph = new HashMap<Long, MedicalEvent>();
		Iterator<Long>iterator = timeline.iterator();
		while(iterator.hasNext())
		{
			Long time = iterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(null != event)
			{
				String name = event.getClass().getName();
				if(name.endsWith(Medication.class.getName()) || name.endsWith(MissedMedication.class.getName()))
				{
					tmpTimeline.add(time);
					tmpGraph.put(time, event);
				}				
			}
		}
		timeline.clear();
		firstGraph.clear();
		secondGraph.clear();
//		timeline = null;
//		firstGraph = null;
//		secondGraph = null;
		
		timeline = tmpTimeline;
		firstGraph = tmpGraph;
		secondGraph = new HashMap<Long, MedicalEvent>();
		invalidate();
	}
	
	public void clearMeds()
	{
		if(0 == timeline.size())
			return;
		Iterator<Long>iterator = timeline.iterator();
		ArrayList<Long>tmpTimeline = new ArrayList<Long>();
		HashMap<Long, MedicalEvent>tmpGraph = new HashMap<Long, MedicalEvent>();
		HashMap<Long, MedicalEvent>secondTmpGraph = new HashMap<Long, MedicalEvent>();
		while(iterator.hasNext())
		{
			Long time = iterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(null != event)
			{
				if(event.getClass().getName().endsWith(Results.class.getName()) || event.getClass().getName().endsWith(MissedMedication.class.getName()))
				{
					tmpTimeline.add(time);
					tmpGraph.put(time, event);
				}
			}
			if(!filter.isSingleFilter)
			{
				MedicalEvent event2 = secondGraph.get(time);
				if(null != event2)
				{
					if(event2.getClass().getName().endsWith(Results.class.getName()) || event2.getClass().getName().endsWith(MissedMedication.class.getName()))
					{
						if(!tmpTimeline.contains(time))
							tmpTimeline.add(time);
						secondTmpGraph.put(time, event2);
					}					
				}
				
			}
		}
		timeline.clear();
		firstGraph.clear();
		secondGraph.clear();
//		timeline = null;
//		firstGraph = null;
//		secondGraph = null;
		timeline = tmpTimeline;
		firstGraph = tmpGraph;
		if(!filter.isSingleFilter)
		{
			secondGraph = secondTmpGraph;
		}
		else
		{
			secondGraph = new HashMap<Long, MedicalEvent>();
		}
		invalidate();
	}
	
	public void clearMissed()
	{
		Iterator<Long>iterator = timeline.iterator();
		ArrayList<Long>tmpTimeline = new ArrayList<Long>();
		HashMap<Long, MedicalEvent>tmpGraph = new HashMap<Long, MedicalEvent>();
		HashMap<Long, MedicalEvent>secondTmpGraph = new HashMap<Long, MedicalEvent>();
		while(iterator.hasNext())
		{
			Long time = iterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(null != event)
			{
				if(event.getClass().getName().endsWith(Results.class.getName()) || event.getClass().getName().endsWith(Medication.class.getName()))
				{
					tmpTimeline.add(time);
					tmpGraph.put(time, event);
				}
			}
			if(!filter.isSingleFilter)
			{
				MedicalEvent event2 = secondGraph.get(time);
				if(null != event2)
				{
					if(event2.getClass().getName().endsWith(Results.class.getName()) || event2.getClass().getName().endsWith(Medication.class.getName()))
					{
						if(!tmpTimeline.contains(time))
							tmpTimeline.add(time);
						secondTmpGraph.put(time, event2);
					}					
				}
				
			}
		}
		timeline.clear();
		firstGraph.clear();
		secondGraph.clear();
	//	timeline = null;
//		firstGraph = null;
//		secondGraph = null;
		timeline = tmpTimeline;
		firstGraph = tmpGraph;
		if(!filter.isSingleFilter)
		{
			secondGraph = secondTmpGraph;
		}
		else
		{
			secondGraph = new HashMap<Long, MedicalEvent>();
		}
		invalidate();
	}
		
	
	
	
	public void setResultsFilter(ResultsFilter filter){this.filter = filter;}
		
	public void setResults(ArrayList<Results>results)
	{ 
		ArrayList<Results>first = filter.getFilteredResults(results, filter.firstFilter);
		ArrayList<Results>second = new ArrayList<Results>();
		if(!filter.isSingleFilter)
		{
			second = filter.getFilteredResults(results, filter.secondFilter);
		}
		Iterator<Results>firstIterator = first.iterator();
		float max = ValueRange.MAXSTART;
		float min = ValueRange.MINSTART;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		while(firstIterator.hasNext())
		{
			Results result = firstIterator.next();
			float value = ResultsFilter.getResultValue(result, filter.firstFilter);
			if(max < value)max = value;
			if(min > value)min = value;
			Long time = Long.valueOf(result.getTime());
			if(!firstGraph.containsValue(result))
			{
				timeline.add(time);
				firstGraph.put(time, result);
			}
		}
		if(max != ValueRange.MAXSTART && min != ValueRange.MINSTART)
		{
			max = ValueRange.maxRange(max);
			min = ValueRange.minRange(min);
			firstValueRange = ResultsTrafo.getValueRange(ticks, min, max);			
		}
//		Log.d(TAG,"max = "+max+" min = "+min);
		max = ValueRange.MAXSTART;
		min = ValueRange.MINSTART;
		Iterator<Results>secondIterator = second.iterator();
		while(secondIterator.hasNext())
		{
			Results result = secondIterator.next();
			float value = ResultsFilter.getResultValue(result, filter.secondFilter);
			if(max < value)max = value;
			if(min > value)min = value;
			Long time = Long.valueOf(result.getTime());
			if(!secondGraph.containsValue(result))
			{
				if(!timeline.contains(time))
					timeline.add(time);
				secondGraph.put(time, result);
			}
		}
//		Log.d(TAG,"the timeline has "+timeline.size()+" events");
		if(max != ValueRange.MAXSTART && min != ValueRange.MINSTART)
		{
			max = ValueRange.maxRange(max);
			min = ValueRange.minRange(min);
			secondValueRange = ResultsTrafo.getValueRange(ticks, min, max);			
		}
		Collections.sort(timeline);
		invalidate();		
	}
	
	
	public void setMedication(ArrayList<Medication>meds)
	{
		if(null == meds)
			return;
		if(0 == meds.size())
			return;
		Iterator<Medication>iterator = meds.iterator();
		while(iterator.hasNext())
		{
			Medication med = iterator.next();
			if(!firstGraph.containsValue(med))
			{
				Long time = Long.valueOf(med.getTime());
				timeline.add(time);
				firstGraph.put(time, med);
			}
		}
		Collections.sort(timeline);
		invalidate();
	}
	
	public void setMissedMedication(ArrayList<MissedMedication>missed)
	{
		if(null == missed)
			return;
		if(0 == missed.size())
			return;
		Iterator<MissedMedication>iterator = missed.iterator();
		while(iterator.hasNext())
		{
			MissedMedication med = iterator.next();
			if(!firstGraph.containsValue(med))
			{
				Long time = Long.valueOf(med.getTime());
				timeline.add(time);
				firstGraph.put(time, med);
			}
		}
		Collections.sort(timeline);
		invalidate();
	}
	
	
	
	private void drawYAxisLegends()
	{
		if(filter.isSingleFilter)
		{
			if(filter.firstFilter == R.string.ViralLoad || filter.firstFilter == R.string.HepCViralLoad)
				drawLogYAxisLegend(false);
			else
				drawYAxisLegend(false);
		}
		else
		{
			drawYAxisRight();
			drawYAxisLegend(false);
			if(filter.secondFilter == R.string.ViralLoad || filter.secondFilter == R.string.HepCViralLoad)
				drawLogYAxisLegend(true);
			else
				drawYAxisLegend(true);			
		}
	}
	
	private void drawFirstGraph()
	{
		if(0 == timeline.size() || 0 == firstGraph.size())
			return;
		Iterator<Long>timeIterator = timeline.iterator();
		float xStep = chartWidth / timeline.size();
		float xStart = ResultsTrafo.xStart(chartWidth, timeline.size());
		float x = xStart;
		float previousY = -1;
		float previousX = -1;
		float y = 0;
		timelinePaint.setColor(filter.firstColourIndex);
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		while(timeIterator.hasNext())
		{
			Long time = timeIterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(isTypeOfEvent(event, Results.class.getName()))
			{
				Results result = (Results)event;
				float value = ResultsFilter.getResultValue(result, filter.firstFilter);
				if(filter.firstFilter == R.string.ViralLoad || filter.firstFilter == R.string.HepCViralLoad)
				{
					y = ResultsTrafo.getLogYOffsetValue(ticks, value, chartHeight);					
				}
				else
				{
					y = ResultsTrafo.getLinearYOffsetValue(ticks, value, firstValueRange, chartHeight);					
				}
				if(timeline.size() < 50)
				{
					RectF point = new RectF(x-INSET, y-INSET, x+INSET, y+INSET);
					canvas.drawRect(point, timelinePaint);					
				}
				if(0 < previousY)
				{
					canvas.drawLine(previousX, previousY, x, y, timelinePaint);
				}					
				previousY = y;
				previousX = x;
			}
			x += xStep;
		}
		
	}
	
	private void drawSecondGraph()
	{
		if(0 == timeline.size() || 0 == secondGraph.size() || filter.isSingleFilter)
			return;		
		float xStep = chartWidth / timeline.size();
		float xStart = ResultsTrafo.xStart(chartWidth, timeline.size());
		float x = xStart;
		float previousY = -1;
		float previousX = -1;
		float y = 0;
		timelinePaint.setColor(filter.secondColourIndex);
		Iterator<Long>timeIterator = timeline.iterator();
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		while(timeIterator.hasNext())
		{
			Long time = timeIterator.next();
			MedicalEvent event = secondGraph.get(time);
			if(isTypeOfEvent(event, Results.class.getName()))
			{
				Results result = (Results)event;
				float value = ResultsFilter.getResultValue(result, filter.secondFilter);
				if(filter.secondFilter == R.string.ViralLoad || filter.secondFilter == R.string.HepCViralLoad)
				{
					y = ResultsTrafo.getLogYOffsetValue(ticks, value, chartHeight);	
					
				}
				else
				{
					y = ResultsTrafo.getLinearYOffsetValue(ticks, value, secondValueRange, chartHeight);					
				}
				if(timeline.size() < 50)
				{
					RectF point = new RectF(x-INSET, y-INSET, x+INSET, y+INSET);
					canvas.drawRect(point, timelinePaint);					
				}
				if(0 < previousY)
				{
					canvas.drawLine(previousX, previousY, x, y, timelinePaint);
				}					
				previousY = y;
				previousX = x;
			}
			x += xStep;
		}
	}
	
	private void drawMedGraph()
	{
		if(0 == timeline.size() || 0 == firstGraph.size())
			return;
		Iterator<Long>timeIterator = timeline.iterator();
		float xStep = chartWidth / timeline.size();
		float xStart = ResultsTrafo.xStart(chartWidth, timeline.size());
		float x = xStart;
		while(timeIterator.hasNext())
		{
			Long time = timeIterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(isTypeOfEvent(event, Medication.class.getName()))
			{
				long date = event.getTime();
				String dateString = DateFormat.format("MMM yy", new Date(date)).toString();	
				android.content.res.Resources resources = context.getResources();
				Bitmap bitmap;
				canvas.drawLine(x, MARGINTOP, x, MARGINTOP + chartHeight, medLinePaint);
				bitmap = BitmapFactory.decodeResource(resources, R.drawable.combismall);
				textPaint.setColor(Color.DKGRAY);
				canvas.drawText(dateString, x - MARGINLEFT+INSET, MARGINTOP + chartHeight + MARGINBOTTOM - 10, textPaint);
				float xIcon = x - MARGINLEFT/2;
				float yIcon = MARGINTOP + chartHeight - 30;
				canvas.drawBitmap(bitmap, xIcon, yIcon, null);			
				
			}
			x += xStep;
		}		
	}
	
	private void drawMissedGraph()
	{
		if(0 == timeline.size() || 0 == firstGraph.size())
			return;
		Iterator<Long>timeIterator = timeline.iterator();
		float xStep = chartWidth / timeline.size();
		float xStart = ResultsTrafo.xStart(chartWidth, timeline.size());
		float x = xStart;
		while(timeIterator.hasNext())
		{
			Long time = timeIterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(isTypeOfEvent(event, MissedMedication.class.getName()))
			{
				long date = event.getTime();
				String dateString = DateFormat.format("MMM yy", new Date(date)).toString();	
				android.content.res.Resources resources = context.getResources();
				Bitmap bitmap;
				canvas.drawLine(x, MARGINTOP, x, MARGINTOP + chartHeight, missedMedLinePaint);
				bitmap = BitmapFactory.decodeResource(resources, R.drawable.missedsmall);
				textPaint.setColor(Color.DKGRAY);
				canvas.drawText(dateString, x - MARGINLEFT+INSET, MARGINTOP + chartHeight + MARGINBOTTOM - 10, textPaint);
				float xIcon = x - MARGINLEFT/2;
				float yIcon = MARGINTOP + chartHeight - 30;
				canvas.drawBitmap(bitmap, xIcon, yIcon, null);			
				
			}
			x += xStep;
		}		
		
	}
	/*
	private void drawPreviousMedsGraph()
	{
		if(0 == timeline.size() || 0 == firstGraph.size())
			return;
		if(0 == timeline.size() || 0 == firstGraph.size())
			return;
		Iterator<Long>timeIterator = timeline.iterator();
		float xStep = chartWidth / timeline.size();
		float xStart = Trafo.xStart(chartWidth, timeline.size());
		float x = xStart;
		float y = 0;
		while(timeIterator.hasNext())
		{
			Long time = timeIterator.next();
			MedicalEvent event = firstGraph.get(time);
			if(isTypeOfEvent(event, PreviousMedication.class.getName()))
			{
				
			}
			x += xStep;
		}				
	}
	*/
	
	public void onDraw(Canvas _canvas)
	{
		super.onDraw(_canvas);
		if(null == _canvas)
			return;
		this.canvas = _canvas;
		if(isInTestingMode)
		{
			testPaintCanvas();
			return;
		}
		drawXAxis();
		drawYAxis();
		
		drawYAxisLegends();
				
		
		drawFirstGraph();
		drawSecondGraph();
		if(filter.showMeds)
		{
			drawMedGraph();
			drawMissedGraph();			
		}
		drawDates();
		if(useTitles)			
			drawTitle();
	}
	
	protected void onMeasure(int width, int height)
	{
		super.onMeasure(width, height);		
		parentWidth = MeasureSpec.getSize(width);
		parentHeight = MeasureSpec.getSize(height);
		chartWidth = parentWidth - MARGINLEFT - MARGINRIGHT ;
		chartHeight = parentHeight - MARGINTOP - MARGINBOTTOM;	
	}
	
	
	
	
	private void drawYAxisLegend(boolean isRight)
	{		
		ValueRange range = firstValueRange;
		if(isRight)
			range = secondValueRange;
		float yValue = range.realMin;
		float textX = 0;
		if(isRight)
			textX = MARGINLEFT + chartWidth + TEXTOFFSET;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		for(int tick = 1 ; tick < ticks ; ++tick)
		{
			yValue += range.realTickDistance;
			String label = null;
			if(10 > yValue)
			{
				label = new String(String.format("%1.1f",yValue));
			}
			else
			{
				label = new String(""+(int)yValue);				
			}
			float y = ResultsTrafo.getLinearYOffsetValue(ticks, yValue, range, chartHeight);
			float textY = y - TEXTOFFSET;
			canvas.drawText(label, textX, textY, textPaint);			
		}	
	}
	
	private void drawLogYAxisLegend(boolean isRight)
	{
		float yValue = 10;
		int ticks = (useSmallSize) ? SMALLYAXISTICKS : YAXISTICKS;
		for(int tick = 1 ; tick < ticks; ++tick)
		{
			float yOffset = ResultsTrafo.getLogYOffsetValue(ticks, yValue, chartHeight);
//			Log.d(TAG,"drawLogYAxisLegend yOffset="+yOffset+" yValue="+yValue);
			float textX = 0;
			if(isRight)
				textX = MARGINLEFT + chartWidth + TEXTOFFSET;
			float textY = yOffset - TEXTOFFSET;
			String label = null;
			if(1000 < yValue)
			{
				label = "10";				
				String exponent = new String(""+tick);
				canvas.drawText(exponent, textX+MAJORTICKLENGTH+TEXTOFFSET+INSET, textY-MAJORTICKLENGTH+INSET, exponentPaint);								
			}
			else
			{
				if(yValue == 10)
				{
					label = (context.getResources()).getString(R.string.und);					
				}
				else
				{
					label = new String(""+(int)yValue);					
				}
			}
			canvas.drawText(label, textX, textY, textPaint);				
				
			yValue *= 10;
		}
	}
	
	/**
	 */
	private void setGraphCharacteristics()
	{		
		timelinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		timelinePaint.setStrokeWidth(STROKEWIDTH);	

		medLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		medLinePaint.setColor(Color.LTGRAY);
		medLinePaint.setStrokeWidth(STROKEWIDTH);
		medLinePaint.setPathEffect(medDash);
		
		missedMedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		missedMedLinePaint.setColor(redColour);
		missedMedLinePaint.setStrokeWidth(STROKEWIDTH);
		missedMedLinePaint.setPathEffect(medDash);					

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			
		textPaint.setTextSize(TEXTSIZE);
		textPaint.setTypeface(Typeface.SERIF);
		textPaint.setStrokeWidth(TEXTSTROKEWIDTH);
		
		titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		titlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		titlePaint.setTypeface(Typeface.SERIF);
		titlePaint.setStrokeWidth(TEXTSTROKEWIDTH);
		titlePaint.setTextSize(SMALLTITLESIZE);
	
		exponentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		exponentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		exponentPaint.setTextSize(EXPTEXTSIZE);
		exponentPaint.setTypeface(Typeface.SERIF);
		exponentPaint.setStrokeWidth(TEXTSTROKEWIDTH);
		if(useSmallSize)
		{
			textPaint.setTextSize(SMALLTEXTSIZE);
			exponentPaint.setTextSize(SMALLEXPTEXTSIZE);			
//			titlePaint.setTextSize(SMALLTITLESIZE);
		}
		
		
	}
	
	private void drawTitle()
	{
		if(null == filter)
			return;
		titlePaint.setColor(filter.firstColourIndex);
		String title = context.getApplicationContext().getResources().getString(filter.firstFilter);
		canvas.drawText(title, INSET, MARGINTOP - INSET, titlePaint);
		
		if(!filter.isSingleFilter)
		{
			title = context.getApplicationContext().getResources().getString(filter.secondFilter);
			titlePaint.setColor(filter.secondColourIndex);			
			int length = title.length();
			float xOffset = MARGINLEFT + chartWidth - length*2 - MARGINRIGHT;
			canvas.drawText(title, xOffset, MARGINTOP - INSET, titlePaint);			
		}
	}
	
	private void drawDates()
	{
		if(null == filter)
			return;
		if(0 == timeline.size() || (0 == firstGraph.size() && 0 == secondGraph.size()))
			return;
		int lastEvent = timeline.size() - 1;
		float xStep = chartWidth / timeline.size();
		float xStart = ResultsTrafo.xStart(chartWidth, timeline.size());
		Long first = timeline.get(0);
		MedicalEvent startDateEvent = firstGraph.get(first);
		if(null == startDateEvent && !filter.isSingleFilter && 0 != secondGraph.size())
		{
			startDateEvent = secondGraph.get(first);
		}
		if(null == startDateEvent)
			return;
		if(isTypeOfEvent(startDateEvent, Results.class.getName()))
		{
			long time = startDateEvent.getTime();
			String dateString = DateFormat.format("MMM yy", new Date(time)).toString();	
			float x = xStart - MARGINLEFT+INSET;
			if(0 > x)x = 0;
			canvas.drawText(dateString, x, MARGINTOP + chartHeight + MARGINBOTTOM - 10, textPaint);
		}
		
		
		if(0 < lastEvent)
		{
			xStart = xStart + (timeline.size() - 1) * xStep;
			Long last = timeline.get(lastEvent);	
			MedicalEvent lastDateEvent = firstGraph.get(last);
			if(null == lastDateEvent && !filter.isSingleFilter && 0 != secondGraph.size())
			{
				lastDateEvent = secondGraph.get(last);
			}
			if(isTypeOfEvent(lastDateEvent, Results.class.getName()))
			{
				long time = lastDateEvent.getTime();				
				String dateString = DateFormat.format("MMM yy", new Date(time)).toString();	
				canvas.drawText(dateString, xStart - MARGINLEFT+INSET, MARGINTOP + chartHeight + MARGINBOTTOM - 10, textPaint);
			}
		}
		
	}
	
	private void setUpAxisPaint()
	{
		axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setColor(Color.BLACK);
		axisPaint.setStrokeWidth(STROKEWIDTH);
	}
	
	
	private void drawXAxis()
	{
		float startX = MARGINLEFT ;
		float startY = MARGINTOP + chartHeight;		
		float stopX = MARGINLEFT + chartWidth;
		float stopY = MARGINTOP + chartHeight;
		canvas.drawLine(startX, startY, stopX, stopY, axisPaint);		
	}
	
	private void drawYAxis()
	{
		float startX = MARGINLEFT;
		float startY = MARGINTOP;
		float stopX = MARGINLEFT;
		float stopY = MARGINTOP + chartHeight;
		canvas.drawLine(startX, startY, stopX, stopY, axisPaint);	
		setTickMarksOnYAxis();
	}
	
	
	private void setTickMarksOnYAxis()
	{
		float startX = MARGINLEFT - (MAJORTICKLENGTH/2);
		float stopX = MARGINLEFT + (MAJORTICKLENGTH/2);	
		int distance = chartHeight / YAXISTICKS;
		for(int tick = 1 ; tick < YAXISTICKS ; ++tick)
		{
			float yOffset = MARGINTOP + distance * tick;	
//			Log.d(TAG,"setTickMarksOnYAxis yOffset="+yOffset);
			canvas.drawLine(startX, yOffset, stopX, yOffset, axisPaint);
		}
	}
	
	private void drawYAxisRight()
	{
		float startX = MARGINLEFT + chartWidth;
		float startY = MARGINTOP;
		float stopX = MARGINLEFT + chartWidth;
		float stopY = MARGINTOP + chartHeight;
		canvas.drawLine(startX, startY, stopX, stopY, axisPaint);	
		setTickMarksOnYAxisRight();		
	}

	private void setTickMarksOnYAxisRight()
	{
		float startX = MARGINLEFT - (MAJORTICKLENGTH/2) + chartWidth;
		float stopX = MARGINLEFT + (MAJORTICKLENGTH/2) + chartWidth;	
		int distance = chartHeight / YAXISTICKS;
		for(int tick = 1 ; tick < YAXISTICKS ; ++tick)
		{
			float yOffset = MARGINTOP + distance * tick;	
			canvas.drawLine(startX, yOffset, stopX, yOffset, axisPaint);
		}		
	}
	
	private void testPaintCanvas()
	{
		switch(whichFrame)
		{
		case 0:
			canvas.drawColor(Color.LTGRAY);
			break;
		case 1:
			canvas.drawColor(Color.BLUE);
			break;
		case 2:
			canvas.drawColor(Color.RED);
			break;
		}	
		drawXAxis();
		drawYAxis();
	}
	
	
	private boolean isTypeOfEvent(MedicalEvent event, String name)
	{
		boolean isTypeOfEvent = false;
		if(null == event)
		{
			return isTypeOfEvent;
		}
		
		String eventClassName = event.getClass().getName();
		if(eventClassName.endsWith(name))
			isTypeOfEvent = true;
		return isTypeOfEvent;
	}
	
}

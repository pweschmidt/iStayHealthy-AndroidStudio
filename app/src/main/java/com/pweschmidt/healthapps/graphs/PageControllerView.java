package com.pweschmidt.healthapps.graphs;

import com.pweschmidt.healthapps.R;

import android.view.View;
//import android.view.View.MeasureSpec;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.graphics.*;

public class PageControllerView extends View 
{
	private int activePage;
	private int pages;
	private Paint circle;
	private float radius;
	private int parentWidth;
	private int parentHeight;
	private int selectedColour;
	
	public PageControllerView(Context context)
	{
		super(context);
		circle = new Paint(Paint.ANTI_ALIAS_FLAG);
		circle.setStrokeWidth(1);
		circle.setStyle(Paint.Style.FILL_AND_STROKE);
		selectedColour = (context.getResources()).getColor(R.color.selectedPageColour);
		activePage = 0;
		radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
	}

	public PageControllerView(Context context, AttributeSet set)
	{
		super(context, set);
		circle = new Paint(Paint.ANTI_ALIAS_FLAG);
		circle.setStrokeWidth(1);
		circle.setStyle(Paint.Style.FILL_AND_STROKE);
		selectedColour = (context.getResources()).getColor(R.color.selectedPageColour);
		activePage = 0;
		radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
	}
	
	public PageControllerView(Context context, AttributeSet set, int defaultStyle)
	{
		super(context, set, defaultStyle);
		circle = new Paint(Paint.ANTI_ALIAS_FLAG);
		circle.setStrokeWidth(1);
		circle.setStyle(Paint.Style.FILL_AND_STROKE);
		selectedColour = (context.getResources()).getColor(R.color.selectedPageColour);
		activePage = 0;
		radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
	}

	public void setPages(int number){pages = number;}
	public int getPages(){return pages;}	
	
	public void setActivePage(int page)
	{
		activePage = page;
		this.invalidate();
	}
	
	public int getActivePage(){return activePage;}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int midHeight = parentHeight / 2;
		int midWidth = parentWidth / 2;
		float distance = 20;
		int firstPoint = midWidth - ((pages/2) * (int)distance);
		int nextX = firstPoint;
		for(int i = 0 ; i < pages ; i++)
		{
			if(i == activePage)
			{
				circle.setColor(selectedColour);
			}
			else
			{
				circle.setColor(Color.LTGRAY);				
			}
			canvas.drawCircle(nextX, midHeight, radius, circle);
			nextX += distance;
		}		
	}
	
	protected void onMeasure(int width, int height)
	{
		super.onMeasure(width, height);
		parentWidth = MeasureSpec.getSize(width);
		parentHeight = MeasureSpec.getSize(height);
	}	
	
}

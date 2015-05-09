package com.pweschmidt.healthapps.graphs;

import com.pweschmidt.healthapps.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import java.util.*;
//import android.util.*;

public class DashboardPageControllerLayout extends LinearLayout 
	implements View.OnClickListener
{
	List<PageControllerListener> listeners = new ArrayList<PageControllerListener>();
//	private static final String TAG = "DashboardPageControllerLayout";

	private PageControllerView pageController;
	
	public DashboardPageControllerLayout(Context context)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pagecontroller, this);
		pageController = (PageControllerView)view.findViewById(R.id.pageControlIndicatorView);
		pageController.setOnClickListener(this);
	}

	public DashboardPageControllerLayout(Context context, AttributeSet set)
	{
		super(context, set);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pagecontroller, this);
		pageController = (PageControllerView)view.findViewById(R.id.pageControlIndicatorView);
		pageController.setOnClickListener(this);
	}
	
	public void addPageControllerListener(PageControllerListener listener)
	{
		listeners.add(listener);
	}
		
	private void turnPage(int toPage)
	{
		Iterator<PageControllerListener> iterator = listeners.iterator();
		
		while(iterator.hasNext())
		{
			PageControllerListener listener = (PageControllerListener)iterator.next();
			listener.turnPage(toPage);
		}
	}
	
	public void onClick(View view)
	{
		int resId = view.getId();
//		Log.d(TAG,"we clicked on the page controller view");
		if(resId == R.id.pageControlIndicatorView)
		{
			int currentPage = pageController.getActivePage();
			int pages = pageController.getPages();
			currentPage++;
			if(currentPage >= pages)currentPage=0;
//			Log.d(TAG,"we are about to turn the page to "+currentPage);
			turnPage(currentPage);
		}
	}
	
	public void testPages()
	{
		pageController.setPages(3);
		this.invalidate();
	}
	
	public void setPageNumber(int pages)
	{
		pageController.setPages(pages);
	}
	
	public void setPage(int pageIndex)
	{
		pageController.setActivePage(pageIndex);
	}
	
	
}

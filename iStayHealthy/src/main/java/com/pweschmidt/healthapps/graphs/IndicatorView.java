package com.pweschmidt.healthapps.graphs;


import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.pweschmidt.healthapps.R;

public class IndicatorView extends View 
{

	private Paint triangle;
	private ResultsIndicator indicator;
	private int redColour;
	private int greenColour;
	private Context context;
	private Path trianglePath;
	private float triangleOriginY;
	private float triangleOriginX;
	private float triangleMiddle;
	private float triangleFull;
	

	public IndicatorView(Context _context)
	{
		super(_context);
		context = _context;
		triangleOriginY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
		triangleOriginX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
		triangleMiddle = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
		triangleFull = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics());
	}

	public IndicatorView(Context _context, AttributeSet set)
	{
		super(_context, set);
		context = _context;
		triangleOriginY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
		triangleOriginX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
		triangleMiddle = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
		triangleFull = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics());
		
	}

	public IndicatorView(Context _context, AttributeSet set, int defaultStyle)
	{
		super(_context, set, defaultStyle);
		context = _context;
		triangleOriginY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
		triangleOriginX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
		triangleMiddle = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
		triangleFull = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics());
	}
	
	
	private void setColours(Context context)
	{
		redColour = (context.getResources()).getColor(R.color.darkred);
		greenColour = (context.getResources()).getColor(R.color.darkgreen);
	}
	
	public void setIndicator(ResultsIndicator ind)
	{
		indicator = ind;
		invalidate();
	}
	
	
	public ResultsIndicator getIndicator(){return indicator;}
	
	public void onDraw(Canvas canvas)
	{
		if(null == indicator)
		{
			indicator = new ResultsIndicator(ResultsIndicator.NOCHANGE, ResultsIndicator.NEUTRAL);
		}
		setColours(context);
		setUpIndicator();
		if(null != trianglePath)
			canvas.drawPath(trianglePath, triangle);
	}
	
	
	private void setUpIndicator()
	{
		triangle = new Paint(Paint.ANTI_ALIAS_FLAG);
		triangle.setStrokeWidth(1);
		triangle.setStyle(Paint.Style.FILL_AND_STROKE);
		
		int evaluation = indicator.getEvaluation();
		switch(evaluation)
		{
		case ResultsIndicator.BETTER:
			triangle.setColor(greenColour);
			break;
		case ResultsIndicator.WORSE:
			triangle.setColor(redColour);
			break;
		}
		
		int direction = indicator.getDirection();
		switch(direction)
		{
		case ResultsIndicator.UP:
			trianglePath = upTriangle();
			break;
		case ResultsIndicator.DOWN:
			trianglePath = downTriangle();
			break;
		}
		
	}
	
	private Path upTriangle()
	{
		Path path = new Path();
		PointF p1 = new PointF(triangleMiddle,triangleOriginY);
		PointF p2 = new PointF(triangleFull,triangleFull);
		PointF p3 = new PointF(triangleOriginX,triangleFull);
		
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		return path;
	}
	
	private Path downTriangle()
	{
		Path path = new Path();
		PointF p3 = new PointF(triangleMiddle,triangleFull);
		PointF p2 = new PointF(triangleFull,triangleOriginY);
		PointF p1 = new PointF(triangleOriginX,triangleOriginY);
		path.moveTo(p1.x, p1.y);
		path.lineTo(p2.x, p2.y);
		path.lineTo(p3.x, p3.y);
		return path;		
	}

}

package com.pweschmidt.healthapps.graphs;

public class ResultsIndicator 
{
	public static final int NEUTRAL = 0;
	public static final int UP = 1;
	public static final int DOWN = -1;

	public static final int NOCHANGE = 0;
	public static final int BETTER = 1;
	public static final int WORSE = -1;
	
	private int direction;
	private int evaluation;

	public ResultsIndicator(int direction, int evaluation)
	{
		this.direction = direction;
		this.evaluation = evaluation;
	}

	public void setDirection(int dir)
	{
		direction = dir;
	}
	
	public void setEvaluation(int eval)
	{
		evaluation = eval;
	}
	
	public int getDirection(){return direction;}
	public int getEvaluation(){return evaluation;}
	
	
}

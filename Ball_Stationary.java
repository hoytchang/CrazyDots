package com.TurboGames.crazydots;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball_Stationary {
	//location of ball
	private int x;
	private int y;
	
	//radius of ball
	private int r;
	
	//paint
	private Paint paint;
	
	//random number generator
	Random rand = new Random();

	//constructor
	public Ball_Stationary(int xSet, int ySet, int rad, int mycolor) {	
		x = xSet;
		y = ySet;
		r = rad;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(mycolor);
	}
	
	public void draw(Canvas c) {
		c.drawCircle( x, y, r, paint);
	}
	
	public void drawSquare(Canvas c) {
		c.drawRect(x-r, y-r, x+r, y+r, paint);
	}
	
	//set functions
	public void goTo(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}
	
	public void setR(int rad) {
		r = rad;
	}
	
	//get functions
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getR() {
		return r;
	}
}

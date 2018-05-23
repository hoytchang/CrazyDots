package com.TurboGames.crazydots;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Ball_Success {
	//location of ball
	private int x;
	private int y;
	
	//radius of ball
	private int r;
	
	//paint
	private Paint paint;
	
	//velocity of ball inflation
	private int vel = 5;
	
	//bounce or wrap
	boolean bounce;
	
	//moving or stationary
	boolean moving;
	
	//path for drawing a star
	Path path;
	Double angle;
	
	//constructor
	//public Ball(Context context, int xSet, int ySet, int rad, int mycolor, boolean b, boolean move) {
	public Ball_Success(int xSet, int ySet, int rad, int mycolor) {	
		x = xSet;
		y = ySet;
		r = rad;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(mycolor);
		
		path = new Path();
	}
	
	public void update(Canvas c) {
		//apply inflation
		r = r + vel;
	}
	
	public void draw(Canvas c) {
		c.drawCircle( x, y, r, paint);
	}
	
	public void drawStar(Canvas c) {
		//draw a 5-point star using path
		path.reset();
		
        // start path
        angle = -Math.PI/2 + 2*Math.PI/5;
        path.moveTo((float)(x + r*Math.cos(angle)), (float)(y + r*Math.sin(angle)));
        
        // go to the 5 points
        for(int i = 0; i < 5; i++) {
        	angle += 3*2*Math.PI/5;
        	path.lineTo((float)(x + r*Math.cos(angle)), (float)(y + r*Math.sin(angle)));
        }
        
        path.close();
        c.drawPath(path, paint);
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
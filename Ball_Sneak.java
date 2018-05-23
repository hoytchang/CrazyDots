package com.TurboGames.crazydots;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Ball_Sneak {
	//location of ball
	private int x;
	private int y;
	
	//radius of ball
	private int r;
	
	//paint
	private Paint paint;
	
	//velocity of ball
	private int vel_= 15;
	
	//life counter, goes from 20 down to 0
	public int life_counter;
	public int sneak_life;
	
	public boolean alive;
	
	public int start_station;
	public int end_station;
	public int vel_x;
	public int vel_y;
	
	//path for drawing a star
	Path path;
	Double angle;
	
	//constructor
	//public Ball(Context context, int xSet, int ySet, int rad, int mycolor, boolean b, boolean move) {
	public Ball_Sneak(int xSet, int ySet, int rad, int mycolor, int life) {	
		x = xSet;
		y = ySet;
		r = rad;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(mycolor);
		
		life_counter = life;
		sneak_life = life;
		alive = true;
		
		path = new Path();
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

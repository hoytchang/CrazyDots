package com.TurboGames.crazydots;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Ball_Shoot {
	//location of ball
	private int x;
	private int y;
	
	//radius of ball
	private int r;
	
	//paint
	private Paint paint;
	
	//velocity of ball
	private int vel_x = 0;
	private int vel_y = 0;
	private int max_vel = 20;
	private int min_vel = 1;
	
	//random number generator
	Random rand = new Random();
	
	//newly created
	public boolean newly_created;
	
	//path for drawing a star
	Path path;
	Double angle;
	
	//constructor
	//public Ball(Context context, int xSet, int ySet, int rad, int mycolor, boolean b, boolean move) {
	public Ball_Shoot(int xSet, int ySet, int rad, int mycolor) {	
		x = xSet;
		y = ySet;
		r = rad;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(mycolor);
		
		path = new Path();

		//nextInt(int n) returns a random integer between 0 (inclusive) and n (exclusive)
		vel_x = min_vel + rand.nextInt(max_vel - min_vel + 1);
		vel_y = min_vel + rand.nextInt(max_vel - min_vel + 1);
		if( rand.nextInt(2) == 1)
			vel_x = vel_x * (-1);
		if( rand.nextInt(2) == 1)
			vel_y = vel_y * (-1);
		
		newly_created = true;
	}
	
	public void update(Canvas c) {
		//apply velocity
		x = x + vel_x;
		y = y + vel_y;
		
		//return to center, when ball gets to edge of screen
		if (x > c.getWidth() || y > c.getHeight() || x < 0 || y < 0) {
			x = c.getWidth()/2;
			y = c.getHeight()/2;
			vel_x = min_vel + rand.nextInt(max_vel - min_vel + 1);
			vel_y = min_vel + rand.nextInt(max_vel - min_vel + 1);
			if( rand.nextInt(2) == 1)
				vel_x = vel_x * (-1);
			if( rand.nextInt(2) == 1)
				vel_y = vel_y * (-1);
		}
	}
	
	public void draw(Canvas c) {
		c.drawCircle( x, y, r, paint);
	}
	
	public void drawSquare(Canvas c) {
		c.drawRect(x-r, y-r, x+r, y+r, paint);
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

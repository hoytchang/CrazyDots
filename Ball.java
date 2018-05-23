package com.TurboGames.crazydots;

import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Ball {
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
	private int min_vel = 5;
	
	//random number generator
	Random rand = new Random();
	
	//bounce or wrap
	boolean bounce;
	
	//moving or stationary
	boolean moving;
	
	//path for drawing a star
	Path path;
	Double angle;
	
	//constructor
	public Ball(int xSet, int ySet, int rad, int mycolor, boolean b, boolean move) {	
		x = xSet;
		y = ySet;
		r = rad;
		
		bounce = b;
		
		moving = move;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(mycolor);
		
		path = new Path();

		if(moving) {
			//nextInt(int n) returns a random integer between 0 (inclusive) and n (exclusive)
			vel_x = min_vel + rand.nextInt(max_vel - min_vel + 1);
			vel_y = min_vel + rand.nextInt(max_vel - min_vel + 1);
			if( rand.nextInt(2) == 1)
				vel_x = vel_x * (-1);
			if( rand.nextInt(2) == 1)
				vel_y = vel_y * (-1);
		}
	}
	
	public void update(Canvas c) {
		//apply velocity
		x = x + vel_x;
		y = y + vel_y;
		
		//either bounce or wrap, when ball gets to edge of screen
		if(bounce) {
			if (x > c.getWidth()) {
				x = c.getWidth();
				vel_x = vel_x * (-1);
			}
			if (y > c.getHeight()) {
				y = c.getHeight();
				vel_y = vel_y * (-1);
			}
			if (x < 0) {
				x = 0;
				vel_x = vel_x * (-1);
			}
			if (y < 0) {
				y = 0;
				vel_y = vel_y * (-1);
			}
		}
		else{
			if (x > c.getWidth()) {
				x = 0;
			}
			if (y > c.getHeight()) {
				y = 0;
			}
			if (x < 0) {
				x = c.getWidth();
			}
			if (y < 0) {
				y = c.getHeight();
			}
		}
	}
	
	public void draw(Canvas c) {
		c.drawCircle( x, y, r, paint);
	}
	
	public void draw_crazy(Canvas c, int crazy_draw_counter) {
		int mycolor = 0;
		switch (crazy_draw_counter) {
		case 0:
			mycolor = Color.GREEN;
			break;
		case 1:
			mycolor = Color.BLUE;
			break;
		case 2:
			mycolor = Color.YELLOW;
			break;
		case 3:
			mycolor = Color.CYAN;
			break;
		case 4:
			mycolor = Color.MAGENTA;
			break;
		}
		paint.setColor(mycolor);
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
	
	public void drawBomb(Canvas c) {
		c.drawCircle( x,y,r,paint);
		c.drawRect( (float)(x-r/2), (float)(y-1.2*r), (float)(x+r/2), y, paint);
		//c.drawRect( (float)(x-r/10), (float)(y-1.5*r), (float)(x+r/10), y, paint);
		path.reset();
		path.moveTo( x, (float)(y-1.2*r) );
		RectF rectf = new RectF();
		rectf.set(x, (float)(y-2.2*r), (float)(x+r), (float)(y-1.2*r) );
		path.arcTo(rectf, -180, 90);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth( (float)(r*0.1) );
		c.drawPath(path, paint);
		paint.setStyle(Paint.Style.FILL);
	}
	
	public void drawHeart(Canvas c) {
		
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

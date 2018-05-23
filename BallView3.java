package com.TurboGames.crazydots;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.view.View;



public class BallView3 extends View {
	//handler to handle animation sequence timing
	private Handler h;
	
	//actors for the game
	//public Ball ball0; //decoy
	public Ball_Shoot ball_target; //red target ball
	public Ball_Shoot star;  //red star
	public Ball_Stationary stationary;
	public Ball_Success ball_success; //when you press the red ball
	public Ball_Success star_success; //when you press the red star
	public ArrayList<Ball_Shoot> shoot_balls = new ArrayList<Ball_Shoot>();
	
	//frame rate
	private final int FRAME_RATE = 30;
	
	//score
	int score;
	int hi_score = 0;
	Paint score_paint;
	
	//success = true if red dot is pressed, false otherwise
	boolean success = false;
	int success_counter = 0;
	
	//red star
	boolean star_success_bool = false;
	int star_counter = 0;
	public boolean star_alive = false;
	
	//life of target sneak ball
	//int target_sneak_life = 100;
	
	//cycle through colors
	int color_counter = 0;
	int mycolor = 0;
	
	//random stuff
	Random rand = new Random();
	Random rand2 = new Random();

	//constructor
	public BallView3(Context context) {
		super(context);
		h = new Handler();
		
		//set the ball positions
		//ball0 = new Ball(100, 100, 20, Color.RED,true,true);
		ball_target = new Ball_Shoot(100, 100, 40, Color.RED);
		shoot_balls.add(ball_target);
		stationary = new Ball_Stationary(0,0,50,Color.GRAY);
		ball_success = new Ball_Success(100, 100, 60, Color.RED);
		star =  new Ball_Shoot(100,100,50,Color.RED);
		shoot_balls.add(star);
		star_success = new Ball_Success(100, 100, 70, Color.RED);
		
		//set score paint color
		score_paint = new Paint();
		score_paint.setStyle(Paint.Style.FILL);
		score_paint.setAntiAlias(true);
		score_paint.setColor(Color.WHITE);
		score_paint.setTextSize(45);
		
		//reset score
		score = 0;
	}
	
	//inner class Runnable (part of game loop)
	private Runnable r = new Runnable() {
		//@Override
		public void run() {
			invalidate();
		}
	};
	
	public void addScore(){
		score = score + 100;
		success = true;
		create_new_ball();
	}
	
	public void addStarScore(){
		score = score + 2000;
		star_success_bool = true;
		create_new_ball();
	}
	
	public int getScore(){
		return score;
	}
	
	public void set_hi_score(int new_high_score) {
		hi_score = new_high_score;
	}
	
	public void minusScore() {
		//score = score - 10;
	}
	
	public void create_new_ball() {
		switch (color_counter) {
		case 0:
			mycolor = Color.GREEN;
			color_counter++;
			break;
		case 1:
			mycolor = Color.BLUE;
			color_counter++;
			break;
		case 2:
			mycolor = Color.YELLOW;
			color_counter++;
			break;
		case 3:
			mycolor = Color.CYAN;
			color_counter++;
			break;
		case 4:
			mycolor = Color.MAGENTA;
			color_counter = 0;
			break;
		}
		//nextInt(int n) returns a random integer between 0 (inclusive) and n (exclusive)
		//Random rand = new Random();
		int radius = 30 + rand.nextInt(15);
		shoot_balls.add(new Ball_Shoot(0, 0, radius, mycolor));
	}
	
	void shrink_balls(){
		for(int i=2; i<shoot_balls.size(); i++) 
			if( shoot_balls.get(i).getR() > 5)
				shoot_balls.get(i).setR( shoot_balls.get(i).getR() / 2);
	}
	
	protected void onDraw(Canvas c) {
		//black background
		c.drawColor(Color.BLACK);
		
		//make star come alive
		if(shoot_balls.size() == 10 || (shoot_balls.size() % 100 == 0 && score > 0) )
			star_alive = true;
		
		//update stationaries (really only need to do this once, not at every frame
		stationary.goTo(c.getWidth()/2, c.getHeight()/2);
		
		//update shoot_balls
		for(int i=0; i<shoot_balls.size(); i++) {
			if (shoot_balls.get(i).newly_created) {
				shoot_balls.get(i).goTo(c.getWidth()/2, c.getHeight()/2);
				shoot_balls.get(i).newly_created = false;
			}
			shoot_balls.get(i).update(c);
			//draw balls
			if(i==1) {
				if(star_alive)
					shoot_balls.get(i).drawStar(c);
			}
			else
				shoot_balls.get(i).draw(c);
		}
		
			
		//draw the stationaries
		stationary.draw(c);
		
		//draw the success ball
		if(success) {
			ball_success.goTo( ball_target.getX(), ball_target.getY() );
			ball_success.update(c);
			ball_success.draw(c);
			success_counter ++;
			score_paint.setTextAlign(Align.CENTER);
			c.drawText("+"+Integer.toString(100), ball_target.getX(),ball_target.getY(),score_paint);
			//reset stuff
			if (success_counter > 5) {
				success_counter = 0;
				ball_success.setR(60);
				success = false;
			}
		}
		//draw the star success
		if(star_success_bool) {
			star_success.goTo( star.getX(), star.getY() );
			star_success.update(c);
			star_success.drawStar(c);
			star_counter ++;
			score_paint.setTextAlign(Align.CENTER);
			c.drawText("+"+Integer.toString(2000), star.getX(),star.getY(),score_paint);
			//reset stuff
			star_alive = false;
			if (star_counter > 10) {
				star_counter = 0;
				star_success.setR(80);
				star_success_bool = false;
				shrink_balls();
			}
		}
		
		//draw the score
		score_paint.setTextAlign(Align.LEFT);
		c.drawText("High Score: ", 0, 0+45, score_paint);
		c.drawText("Score: ", 0, 0+90, score_paint);
		score_paint.setTextAlign(Align.RIGHT);
		c.drawText(Integer.toString(hi_score), c.getWidth(), 0+45, score_paint);
		c.drawText(Integer.toString(score), c.getWidth(), 0+90, score_paint);
		
		//frame rate
		h.postDelayed(r, FRAME_RATE);
	}
}

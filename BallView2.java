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



public class BallView2 extends View {
	//handler to handle animation sequence timing
	private Handler h;
	
	//actors for the game
	//public Ball ball0; //decoy
	public Ball_Sneak ball_target; //red target ball
	public Ball_Stationary[] stationaries;
	public Ball_Success ball_success;
	//public Ball_Sneak sneak;
	//public Ball_Sneak sneak2;
	//public Ball balls[];
	public ArrayList<Ball_Sneak> sneak_balls = new ArrayList<Ball_Sneak>();
	public Ball_Sneak star;  //red star
	public Ball_Success star_success; //when you press the red star
	
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
	int target_sneak_life = 50;  //100 is too slow paced
	
	//cycle through colors
	int color_counter = 0;
	int mycolor = 0;
	
	//random stuff
	Random rand = new Random();
	Random rand2 = new Random();

	//constructor
	public BallView2(Context context) {
		super(context);
		h = new Handler();
		
		//set the ball positions
		//ball0 = new Ball(100, 100, 20, Color.RED,true,true);
		ball_target = new Ball_Sneak(100, 100, 40, Color.RED,target_sneak_life);
		sneak_balls.add(ball_target);
		stationaries = new Ball_Stationary[9];
		for(int i = 0; i<9; i++) {
			stationaries[i] = new Ball_Stationary(0,0,50,Color.GRAY);
		}
		ball_success = new Ball_Success(100, 100, 60, Color.RED);
		//sneak = new Ball_Sneak(0,0,40,Color.BLUE,sneak_life);
		//sneak2 = new Ball_Sneak(0,0,40,Color.GREEN,sneak_life);
		star = new Ball_Sneak(100, 100, 50, Color.RED, target_sneak_life);
		sneak_balls.add(star);
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
		if(ball_target.alive) {
			score = score + 100;
			success = true;
			create_new_ball();
		}
	}
	
	public void addStarScore(){
		if(star.alive){ 
			score = score + 2000;
			star_success_bool = true;
			create_new_ball();
		}
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
		int rand_sneak_life = target_sneak_life - 10 + rand.nextInt(20);
		sneak_balls.add(new Ball_Sneak(0, 0, radius, mycolor,rand_sneak_life));
	}
	
	void shrink_balls(){
		for(int i=2; i<sneak_balls.size(); i++) 
			if( sneak_balls.get(i).getR() > 5)
				sneak_balls.get(i).setR( sneak_balls.get(i).getR() / 2);
	}
	
	protected void onDraw(Canvas c) {
		//black background
		c.drawColor(Color.BLACK);
		
		//make star come alive
		if(sneak_balls.size() == 10 || (sneak_balls.size() % 50 == 0 && score > 0) )
			star_alive = true;
		
		//update the balls
		//ball0.update(c);
		//if(!success)
		//	ball_target.update(c);

		//for(int i=0; i<balls.size(); i++)
		//	balls.get(i).update(c);
		
		//update stationaries (really only need to do this once, not at every frame
		int counter = 0;
		int station_spacing = 150;
		for(int i = -1; i<=1; i++) {
			for(int j = -1; j<=1; j++) {
				stationaries[counter].goTo(c.getWidth()/2+i*station_spacing, c.getHeight()/2+j*station_spacing);
				counter++;
			}
		}
		
		//update sneak ball
		/*
		 * Location of stationaries:
		 * 
		 * 0 3 6
		 * 1 4 7
		 * 2 5 8
		 * 
		 * sneak cycle:
		 * 1) sneak ball is moving towards destination, is alive, and life_counter is ticking down from 50
		 * 2) sneak ball has reached destination, is dead, and life_counter is ticking from 50 to 0
		 * 
		 */
		//update sneak_balls
		for(int i=0; i<sneak_balls.size(); i++) {
			if( sneak_balls.get(i).alive){
				//if sneak ball is newly alive, set it to starting position
				if(sneak_balls.get(i).life_counter == sneak_balls.get(i).sneak_life) {
					//nextInt(int n) returns a random integer between 0 (inclusive) and n (exclusive)
					sneak_balls.get(i).start_station = rand.nextInt(9);
					sneak_balls.get(i).end_station = rand2.nextInt(9);
					//compute velocity from start_station to end_station
					sneak_balls.get(i).vel_x = (int) (( stationaries[sneak_balls.get(i).end_station].getX() - stationaries[sneak_balls.get(i).start_station].getX() ) / 30.0);
					sneak_balls.get(i).vel_y = (int) (( stationaries[sneak_balls.get(i).end_station].getY() - stationaries[sneak_balls.get(i).start_station].getY() ) / 30.0);
					sneak_balls.get(i).goTo( stationaries[sneak_balls.get(i).start_station].getX(), stationaries[sneak_balls.get(i).start_station].getY());
				}
				
				//move sneak ball
				sneak_balls.get(i).goTo(sneak_balls.get(i).getX() + sneak_balls.get(i).vel_x, sneak_balls.get(i).getY() + sneak_balls.get(i).vel_y);
				
				//sneak has reached destination, and should switch to dead
				if( Math.abs(sneak_balls.get(i).getX()-stationaries[sneak_balls.get(i).end_station].getX()) < Math.abs(sneak_balls.get(i).vel_x)+5 && Math.abs(sneak_balls.get(i).getY()-stationaries[sneak_balls.get(i).end_station].getY()) < Math.abs(sneak_balls.get(i).vel_y)+5) { 
					sneak_balls.get(i).alive = false;
				}
				
				//tick down life counter
				sneak_balls.get(i).life_counter--;
			} else {
				//tick down life counter
				sneak_balls.get(i).life_counter--;
				
				//if life counter reaches 0, make sneak ball alive again
				if(sneak_balls.get(i).life_counter == 0) {
					//reset sneak
					sneak_balls.get(i).alive = true;
					sneak_balls.get(i).life_counter = sneak_balls.get(i).sneak_life;
				}
			}
			
			//draw the sneak balls
			if(sneak_balls.get(i).alive) {
				if(i==1) {
					if(star_alive)
						sneak_balls.get(i).drawStar(c);
				}
				else
					sneak_balls.get(i).draw(c);
			}
		}
		//c.drawText("start_station: "+Integer.toString(sneak2.start_station), c.getWidth(), c.getHeight() - 120, score_paint);
		//c.drawText("end_station: " + Integer.toString(sneak2.end_station)  , c.getWidth(), c.getHeight() - 80, score_paint);
		//c.drawText("vel x: "+Integer.toString(sneak2.vel_x), c.getWidth(), c.getHeight() - 40, score_paint);
		//c.drawText("vel y: " + Integer.toString(sneak2.vel_y)  , c.getWidth(), c.getHeight(), score_paint);
		
			
		//draw the stationaries
		for(int i = 0; i < stationaries.length; i++) {
			stationaries[i].draw(c);
			//c.drawText(Integer.toString(i), stationaries[i].getX(), stationaries[i].getY(), score_paint);
		}
		
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
				star_success.setR(70);
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

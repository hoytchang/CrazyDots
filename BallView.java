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

//this class acts as both the "model" and the "view"
//it has the Runnable and the onDraw() method
public class BallView extends View {
	//handler to handle animation sequence timing
	private Handler h;
	
	//actors for the game
	public Ball ball_target; //red target ball
	public Ball star;  //red star
	//public Ball ball_crazy; //crazy ball
	public Ball_Success ball_success; //when you press the red ball
	public Ball_Success star_success; //when you press the red star
	public ArrayList<Ball> balls = new ArrayList<Ball>();
	
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
	
	//crazy ball
	boolean crazy_success_bool = false;
	int crazy_draw_counter = 0;
	public boolean crazy_alive = false;
	int crazy_counter = 0;
	
	//life of sneak ball
	//int sneak_life = 100;
	
	//cycle through colors
	int color_counter = 0;
	int mycolor = 0;
	
	//constructor
	public BallView(Context context) {
		super(context);
		h = new Handler();
		
		//set the ball positions
		ball_target = new Ball(100, 100, 40, Color.RED,true,true);
		ball_success = new Ball_Success(100, 100, 60, Color.RED);
		star = new Ball(100, 100, 60, Color.RED, true, true);
		star_success = new Ball_Success(100, 100, 80, Color.RED);
		//ball_crazy = new Ball(100,100,40, Color.RED, true, true);
		
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
	
	public void addCrazy(){
		crazy_success_bool = true;
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
		Random rand = new Random();
		int radius = 30 + rand.nextInt(20);
		balls.add(new Ball( ball_target.getX(), ball_target.getY(), radius, mycolor,true,true));
	}
	
	void shrink_balls(){
		for(int i=0; i<balls.size(); i++) 
			if( balls.get(i).getR() > 5)
				balls.get(i).setR( balls.get(i).getR() / 2);
	}
	
	protected void onDraw(Canvas c) {
		//black background
		c.drawColor(Color.BLACK);
		
		//make star come alive
		if(balls.size() == 10 || (balls.size() % 100 == 0 && score > 0) )
			star_alive = true;
		
		//make crazy ball come alive
		if(balls.size() % 5 == 0 && score > 0)
			crazy_alive = true;
		
		//update the balls
		if(!success)
			ball_target.update(c);
		for(int i=0; i<balls.size(); i++)
			balls.get(i).update(c);
		if(!star_success_bool)
			star.update(c);
		//if(crazy_alive && !crazy_success_bool)
		//	ball_crazy.update(c);

		//draw the balls
		ball_target.draw(c);
		if(star_alive)
			star.drawStar(c);
		for(int i=0; i<balls.size(); i++)
			balls.get(i).draw(c);
		//if(crazy_alive) {
		//	if(crazy_draw_counter == 5)
		//		crazy_draw_counter = 0;
		//	ball_crazy.draw_crazy(c,crazy_draw_counter);
		//	crazy_draw_counter++;
		//}
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
		//draw the crazy success
		/*
		if(crazy_success_bool){
			ball_crazy.setR( ball_crazy.getR() + 5 );
			crazy_counter++;
			//reset stuff
			if(crazy_counter>10) {
				crazy_success_bool = false;
				crazy_counter = 0;
				crazy_alive = false;
				ball_crazy.setR(40);
				shrink_balls();
			}
		}
		*/
		
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

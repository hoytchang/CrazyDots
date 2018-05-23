package com.TurboGames.crazydots;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MainActivity extends Activity{ //ActionBarActivity {
	//for set content view
	TCanvas tCanvas;
	Paint text_paint = new Paint();
	
	//declare the listeners, listen for touches
	GestureDetector gestureDetector;
	
	//number of levels
	int Num_Levels = 4;
	/*
	 * Level 1: balls bounce randomly (done, has red star)
	 * Level 2: balls sneak between stationary balls (done, has red star)
	 * Level 3: balls shoot out randomly from the center (done, has red star)
	 * Level 4: balls pop up from bottom of screen and fall down with gravity (done, has red star)
	 * 
	 * Add red star: pressing the star adds 500 to your score, and shrinks the size of the distracting balls
	 * Add red heart: pressing the heart reduces the size of the distracting balls
	 * Add crazy ball (fluctuating colors): pressing the crazy ball activates crazy mode for 10 seconds, background gets trippy and you get 500 points per click
	 * Add black ball with "X" or skull & crossbones.  Touching it makes you lose.
	 * 
	 * Level : balls shoot from an upper stationary, and gets sucked into a lower stationary, in a parabola (?) or hyperbola (?)
	 * Level : balls shoot from the corner, hit 45 degree walls in 3 corners, and bounce in a rectangular pattern
	 * Level : one stationary in the middle, a circular array of stationaries that rotate
	 * Level : balls travel in a path 
	 * Level : balls get covered up by arrays of lines/bars
	 * Level : balls travel in a single path, there is a 45 degree wall in the middle that it bounces off
	 * Level : balls shoot out horizontally from a grey ball that moves up and down
	 *  
	 */
	
	//location of level buttons
	int[] x_loc = new int[Num_Levels];
	int[] y_loc = new int[Num_Levels];
	
	//go to level
	int go_to_level = -1;
	
	//highest unlocked level
	int unlocked_level = 1;
	
	//getting preferences
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		fullscreen(); // without this, bottom of canvas goes out of screen
		tCanvas = new TCanvas(this);
		setContentView(tCanvas);
		gestureDetector = new GestureDetector(this, gestureListener);
		prefs = this.getSharedPreferences("com.TurboGames.myapp.CrazyDots.myPrefsKey", Context.MODE_PRIVATE);
	}
	
	public void fullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	public class TCanvas extends View {
		//handler to handle animation sequence timing
		private Handler h;
		
		//frame rate
		private final int FRAME_RATE = 30;
		
		//success = true if a level dot is pressed, false otherwise
		boolean success = false;
		int success_counter = 0;
		public Ball_Success ball_success;
		public int success_x;
		public int success_y;
		
		//constructor
		public TCanvas(Context context) {
			super(context);
			h = new Handler();
			
			ball_success = new Ball_Success(100, 100, 60, Color.WHITE);
		}
		
		//inner class Runnable (part of game loop)
		private Runnable r = new Runnable() {
			//@Override
			public void run() {
				invalidate();
			}
		};
		
		public void onDraw(Canvas c) {
			//getting high scores from preferences
			int[] hi_scores = new int[Num_Levels];
			int required_score = 10000;
			String hi_score_key_string;
			for(int i = 0; i < Num_Levels; i++) {
				//retrieve hi score from preferences
				hi_score_key_string = "L" + Integer.toString(i+1) + "_hi_score_key";
				hi_scores[i] = prefs.getInt(hi_score_key_string, 0);
				
				//if score is high enough, unlock next level
				if( hi_scores[i] >= required_score)
					unlocked_level = i+2;
			}
			/*
			int L1_hi_score = prefs.getInt("L1_hi_score_key", 0); //0 is the default value
			int L2_hi_score = prefs.getInt("L2_hi_score_key", 0);
			int L3_hi_score = prefs.getInt("L3_hi_score_key", 0);
			if (L1_hi_score >= required_score)
				unlocked_level = 2;
			if (L2_hi_score >= 10000)
				unlocked_level = 3;
			*/
			
			//draw stuff
			int y_position;
			c.drawColor(Color.BLACK);

			//draw title and instructions
			 text_paint.setStyle(Paint.Style.FILL);
			 text_paint.setAntiAlias(true);
			 text_paint.setColor(Color.WHITE);
			 text_paint.setTextSize(60);
			 text_paint.setTextAlign(Align.CENTER);
			 
			 y_position = 80;
			 c.drawText("CRAZY DOTS", c.getWidth()/2, y_position, text_paint);
			 
			 y_position += 50;
			 text_paint.setTextSize(20);
			 c.drawText("How to play:",c.getWidth()/2,y_position,text_paint);
			 y_position += 20;
			 c.drawText("Touch the Red Dot to score points.",c.getWidth()/2,y_position,text_paint);
			 y_position += 20;
			 c.drawText("Score "+required_score+" to unlock the next level.",c.getWidth()/2,y_position,text_paint);
			 
			 //draw levels
			 text_paint.setTextSize(30);
			 text_paint.setTextAlign(Align.LEFT);
			 
			 for(int i = 0; i < Num_Levels; i++){
				 if(i > unlocked_level - 1)
					 text_paint.setColor(Color.GRAY);
				 y_position += 120;
				 x_loc[i] = c.getWidth()/2 - 120;
				 y_loc[i] = y_position;
				 c.drawCircle(x_loc[i], y_loc[i], 50, text_paint);
				 c.drawText("Level "+Integer.toString(i+1), c.getWidth()/2 - 45, y_position, text_paint);
				 c.drawText("High Score: "+hi_scores[i], c.getWidth()/2 - 45, y_position + 35, text_paint);
			 }
			 
			//draw the success ball, then go to selected activity
			if(success) {
				ball_success.goTo(x_loc[go_to_level], y_loc[go_to_level]);
				ball_success.update(c);
				ball_success.draw(c);
				success_counter ++;
				//reset stuff, then go to activity
				if (success_counter > 5) {
					success_counter = 0;
					ball_success.setR(60);
					success = false;
					go_to_activity();
				}
			}
			
			//frame rate
			h.postDelayed(r, FRAME_RATE);
		 }
	 }
	
	public boolean onTouchEvent(MotionEvent event){
		//get int representing the type of action
		int action = event.getAction();
		
		//if the user touched the screen or dragged along the screen
		if (action == MotionEvent.ACTION_DOWN ) { //|| action == MotionEvent.ACTION_MOVE){
			int x = (int)event.getX();
			int y = (int)event.getY();
			
			for(int i = 0; i < Num_Levels; i++) {
				//launch the level if that location was pressed
				if ( Math.abs( x - x_loc[i]) < 40 && Math.abs(y - y_loc[i]) < 40) {
					//if that level is unlocked
					if(i <= unlocked_level - 1) {
						//draw a circle at that location
						go_to_level = i;
						tCanvas.success = true;
					}
				}
			}
		}
		return gestureDetector.onTouchEvent(event);
	}
	
	public void go_to_activity () {
		Intent intent;
		switch (go_to_level) {
		case 0:
			intent = new Intent(this, Level_1_Activity.class);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(this, Level_2_Activity.class);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, Level_3_Activity.class);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(this, Level_4_Activity.class);
			startActivity(intent);
			break;
		}
	}
	
	//gestureListener for double taps
	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		//called when the listener touches the screen
		@Override
		public boolean onDoubleTap(MotionEvent e){
			//do some stuff
			return true;
		}
	};

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
}

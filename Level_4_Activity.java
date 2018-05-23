package com.TurboGames.crazydots;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;

public class Level_4_Activity extends Activity { //ActionBarActivity {
	//declare the view
		private BallView4 ballView;
		
		//declare the listeners, listen for touches
		GestureDetector gestureDetector;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Intent intent = getIntent();
			
			//getting preferences
			SharedPreferences prefs = this.getSharedPreferences("com.TurboGames.myapp.CrazyDots.myPrefsKey", Context.MODE_PRIVATE);
			int hi_score = prefs.getInt("L4_hi_score_key", 0); //0 is the default value
			
			fullscreen();
			
			ballView = new BallView4(this);
			ballView.set_hi_score(hi_score);
			setContentView(ballView);
			
			gestureDetector = new GestureDetector(this, gestureListener);
		}
		
		public void fullscreen() {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		public boolean onTouchEvent(MotionEvent event){
			//get int representing the type of action
			int action = event.getAction();
			
			//if the user touched the screen or dragged along the screen
			if (action == MotionEvent.ACTION_DOWN ) { //|| action == MotionEvent.ACTION_MOVE){
				int x = (int)event.getX();
				int y = (int)event.getY();
				int red_x = ballView.ball_target.getX();
				int red_y = ballView.ball_target.getY();
				int star_x = ballView.star.getX();
				int star_y = ballView.star.getY();
				
				//if user touched the red ball
				if( Math.abs(x-red_x) < 40 && Math.abs(y-red_y) < 40 ) {
					ballView.addScore();
					
					int score = ballView.getScore();
					SharedPreferences prefs = this.getSharedPreferences("com.TurboGames.myapp.CrazyDots.myPrefsKey", Context.MODE_PRIVATE);
					int hi_score = prefs.getInt("L4_hi_score_key", 0); //0 is the default value
					if (score > hi_score) {
						Editor editor = prefs.edit();
						editor.putInt("L4_hi_score_key", score);
						editor.commit();
						ballView.set_hi_score(score);
					}
					
				} 
				//if user touched the red star
				else if ( Math.abs(x-star_x) < 40 && Math.abs(y-star_y) < 40 && ballView.star_alive) {
					ballView.addStarScore();
					
					int score = ballView.getScore();
					SharedPreferences prefs = this.getSharedPreferences("com.TurboGames.myapp.CrazyDots.myPrefsKey", Context.MODE_PRIVATE);
					int hi_score = prefs.getInt("L4_hi_score_key", 0); //0 is the default value
					if (score > hi_score) {
						Editor editor = prefs.edit();
						editor.putInt("L4_hi_score_key", score);
						editor.commit();
						ballView.set_hi_score(score);
					}
				}
			}
			return gestureDetector.onTouchEvent(event);
		}
		
		//gestureListener for double taps
		SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
			//called when the listener touches the screen
			@Override
			public boolean onDoubleTap(MotionEvent e){
				//do some stuff to modify the balls:
				//ballView.ball1.goTo(xPos, yPos)
				return true;
			}
		};
	}
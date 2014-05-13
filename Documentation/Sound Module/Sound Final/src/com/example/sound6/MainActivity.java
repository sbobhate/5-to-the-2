package com.example.sound6;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {;
	
    WelcomeTrack welcome_sound;
    
	boolean wasPaused = false;
	boolean transition = false;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
		System.out.println("***STARTING ANOTHER RUN***");
            welcome_sound = new WelcomeTrack();
            welcome_sound.playWelcomeTrack();
        
    }
    
    public void goToSecondActivity (View v)
    {
    	transition = true;
    	Intent newActivity = new Intent(this, SecondActivity.class);
    	startActivity(newActivity);
    }
    
    @Override
    public void onPause () {
    	super.onPause();
    	wasPaused = true;
    	if (this.isFinishing() || transition) {
    		welcome_sound.prepare_to_quit();
    	}
    	welcome_sound.pause();
    }
    
    @Override
    public void onResume () {
    	super.onResume();
		//System.out.println("Made it to main resume");
    	if (wasPaused) {
    		welcome_sound.resume();
    		wasPaused = false;
    	}
    	if (transition)
    		finish();
    }
    
    @Override 	
    public void onDestroy() {
    	super.onDestroy();
    	welcome_sound.quit();
	    Log.i("Debug", "on Destroy");
		System.out.println("Quitting from main activity, welcomeRunning");
    }
}
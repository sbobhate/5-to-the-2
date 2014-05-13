package com.example.sound6;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.View.OnClickListener;

public class SecondActivity extends ActionBarActivity {

    SoundTrack fivebfive;
    WelcomeTrack welcome_sound = new WelcomeTrack();
	
	boolean wasPaused = false;
	boolean welcomeRunning = false;
	boolean soundRunning = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    	
        fivebfive = new SoundTrack();
        fivebfive.playSoundTrack(1);
        soundRunning = true;
    }
        
    public void speedup (View view)
    {
    	fivebfive.speedup(0.9375);
    	Log.w("Debug", "increased");
    }
    
    public void startOver (View view)
    {
    	//call this one after if you're in the middle of play
    	Log.w("Debug", "startOver");
    	if (welcomeRunning) {
    		welcome_sound.startAgain();
            fivebfive = new SoundTrack();
            fivebfive.playSoundTrack(1);
            welcomeRunning = false;
            soundRunning = true;
    	}
    	else if (soundRunning) {
    	    fivebfive.startAgain();
            fivebfive = new SoundTrack();
            fivebfive.playSoundTrack(1);
    	}
    }

    public void makeUrgent (View view)
    {
    	fivebfive.make_urgent();
    	Log.w("Debug", "urgent");
    }
    
    public void winner (View view)
    {
    	Log.w("Debug", "winner");
    	fivebfive.gameComplete(1);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;
    }
    
    public void loser (View view)
    {
    	Log.w("Debug", "loser");
    	fivebfive.gameComplete(2);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;
    }
    
    public void tie (View view)
    {
    	Log.w("Debug", "tie");
    	fivebfive.gameComplete(3);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;
    }
    
    public void replay (View view)
    {
    	//call this one after the end of a game
    	welcome_sound.continuing();
    	welcome_sound.quit();
    	welcomeRunning = false;
        fivebfive = new SoundTrack();
        fivebfive.playSoundTrack(1);
        soundRunning = true;
    }
    
    @Override
    public void onPause () {
    	super.onPause();
    	wasPaused = true;
    	if (this.isFinishing() && soundRunning) {
        	fivebfive.set_to_quit();
    	}
    	else if (this.isFinishing() && welcomeRunning) {
        	welcome_sound.prepare_to_quit();
    	}
    	if (soundRunning)
        	fivebfive.pause();
    	else if (welcomeRunning)
    		welcome_sound.pause();
    }
    
    @Override
    public void onResume () {
    	super.onResume();
    	if (wasPaused && soundRunning) {
    		fivebfive.resume();
    		wasPaused = false;
    	}
    	else if (wasPaused && welcomeRunning) {
    		welcome_sound.resume();
    		wasPaused = false;
    	}
    }
    
    @Override 	
    public void onDestroy() {
    	super.onDestroy();
    	
    	//This block may need to be uncommented
    	
/*    	if (soundRunning) {
    		System.out.println("Quitting from second activity, soundRunning");
        	fivebfive.quit();
    	}
    	else if (welcomeRunning) {
    		System.out.println("Quitting from second activity, welcomeRunning");
    		newGame.quit();
    	}*/
    }
    
}

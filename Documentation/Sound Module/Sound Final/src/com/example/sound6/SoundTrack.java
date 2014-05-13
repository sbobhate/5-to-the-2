package com.example.sound6;

/* Michael Reavey, EC327, Spring_2014*/

import java.util.concurrent.CyclicBarrier;

public class SoundTrack {

    private	Track bass_line;
	private Track melody_line;
	private SoundEffect pause_effect;
	private SoundEffect quit_effect;
	private SoundEffect win_sound;
	private CyclicBarrier cb;
	private boolean quitting = false;
    
	// No-arg constructor
    public SoundTrack() {}
  
    // Same as prepare_to_quit in WelcomeTrack class
    // Helps transition from onPause to onDestroy
    public void set_to_quit () {
    	quitting = true;
    }
	
    // Speed up sample by multiplying buffer size by a constant factor
    // If factor > 1, sample slows down
    // If factor < 1, the sample speeds up. If factor is close to zero, the accelearation is quicker
    // If factor <= 0, error
	public void speedup(double factor) {
		bass_line.speedup(factor);
		melody_line.speedup(factor);
	}
	
	// Loops the last half of each sample
	// For musical diversity.  Non-essential to functionality
	public void make_urgent() {
		bass_line.make_urgent();
		melody_line.make_urgent();
	}
	
	// 'p' indicates each Track will resume playing
    public void resume () {
    	bass_line.state = 'p';
        melody_line.state = 'p';
    }
    
    // 's' stops each Track
    // plays pause SoundEffect as long as onDestroy has not been initiated
	public void pause() {
		if (!quitting) {
		    pause_effect = new SoundEffect(5);
		    pause_effect.start();
		}
		bass_line.state = 's';
		melody_line.state = 's';
	}
	
	// When match is finished, plays the appropriate SoundEffect
	public void gameComplete(int who_won) {
        bass_line.keeprunning = false;
        melody_line.keeprunning = false;
        bass_line.isrunning = false;
        melody_line.isrunning = false;
        switch (who_won) {
            case 1: //win
                win_sound = new SoundEffect(7);
                win_sound.start();
                break;
            case 2: //lose
                win_sound = new SoundEffect(8);
                win_sound.start();
                break;
            case 3: //tie
                win_sound = new SoundEffect(9);
                win_sound.start();
                break;
        }
    }
	
	// Restart the SoundTrack if the game is restarted in the middle of play
	public void startAgain() {
        bass_line.keeprunning = false;
        melody_line.keeprunning = false;
        bass_line.isrunning = false;
        melody_line.isrunning = false;		
	    bass_line = null;
	    melody_line = null;
	}
	
	// Stops execution of all threads
	// Play quit SoundEffect
	public void quit() {   
        bass_line.keeprunning = false;
        melody_line.keeprunning = false;
        bass_line.isrunning = false;
        melody_line.isrunning = false;
        
        quit_effect = new SoundEffect(6);
        quit_effect.start();
	    
        bass_line = null;
	    melody_line = null;
	    pause_effect = null;
	    quit_effect = null;
	    win_sound = null;
    }
		
	// Play the main game SoundTrack
    public void playSoundTrack(int soundtrack_select) { 
    	switch (soundtrack_select) {
    	    case 1: //main theme
    	        cb = new CyclicBarrier(2);
    	        bass_line = new Track(cb, 2);
    	        melody_line = new Track(cb, 3);
                bass_line.start();
                melody_line.start(); 
                break;
    	    case 2: //currently blank, add additional SoundTracks here. 
                break;
            default:
            	break;
    	}       	
    }
}
package com.example.sound6;

/* Michael Reavey, EC327, Spring_2014*/

public class WelcomeTrack {

    private Track welcome;
    private SoundEffect pause_effect;
    private SoundEffect quit_effect;
	private boolean quitting = false;
	private boolean continuing = false;
    
	// No-arg constructor
    public WelcomeTrack() {}

    // Same as set_to_quit in SoundTrack class
    // Helps transition from onPause to onDestroy
    public void prepare_to_quit () {
    	quitting = true;
    }
    
    // 'p' indicates the Track will resume playing
    public void resume () {
        welcome.state = 'p';
    }
    
    // Set when WelcomeTrack is transitioning to a new SoundTrack
    public void continuing () {
    	continuing = true;
    }
	
    // 's' stops each Track
    // plays pause SoundEffect as long as onDestroy has not been initiated
	public void pause() {
		if (!quitting) {
		    pause_effect = new SoundEffect(5);
		    pause_effect.start();
		}
        welcome.state = 's';
	}

	// Restart the WelcomeTrack if restart is triggered
    public void startAgain() {
	    welcome.keeprunning = false;
        welcome.isrunning = false;
        welcome = null;
    }
    
    // Stops execution of all threads
    // Play quit SoundEffect
	public void quit() {   
	    welcome.keeprunning = false;
        welcome.isrunning = false;
        
        if (!continuing) {
            quit_effect = new SoundEffect(6);
            quit_effect.start();
            continuing = false;
    	    quit_effect = null;
        }
	    welcome = null;
    }
	
	// Play WelcomeTrack where it must start with silence to avoid conflicts with gameComplete sound
    public void playWelcomeTrack(int silence_toggle) { 
        welcome = new Track(4);
    	welcome.silent_start = true;
        welcome.start();
    }
	
    // Play generic WelcomeTrack
    public void playWelcomeTrack() { 
        welcome = new Track(4);
        welcome.start();
    } 
}

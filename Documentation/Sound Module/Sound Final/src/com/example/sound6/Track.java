package com.example.sound6;

/* Michael Reavey, EC327, Spring_2014*/

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.FloatMath;

import java.util.concurrent.CyclicBarrier;

public class Track extends Thread {

    private AudioTrack audioTrack;
	private CyclicBarrier cbar;
	private Sample sample = new Sample();
	
	private int which_sample;
	private int counter = 0;
	private int sound_index = 0;
	private int duration_loc = 1;
	private double freq = 440.0f;
	private double angle = 0.1f;
	private double twopi = 8.*Math.atan(1.);
	
	private int buffsize;
	private int samplerate;
	private int amp;
	private int halfway;
	private double [] tune;
	
	private boolean urgent = false;
	
    // Variables accessed from SoundTrack and WelcomeTrack
	protected boolean isrunning = true;
	protected boolean keeprunning = true;
	protected char state = 'p';
	protected boolean silent_start = false;
	
	// Start the track with silence
	// Called to avoid having a SoundEffect and a SoundTrack/ WelcomeTrack play simultaneously
	private void insertSilence () {
		sample.setBuffsize(3500);
		buffsize = sample.getBuffsize();
	    short samples[] = new short [buffsize];
		
	    for (int j = 0; j <  25; j++) {
		    for (int i=0; i < buffsize; i++) { 
			    samples[i] = 0;
		    }
		    
		    audioTrack.write(samples, 0, buffsize);
	    }
	    silent_start = false;
	}
	
    //constructor, with CyclicBarrier
	protected Track(CyclicBarrier c, int w) {
		cbar = c;
		which_sample = w;
	}
	
	//constructor, no CyclicBarrier
	protected Track(int w) {
		which_sample = w;
	}
	
	// Speed up the sound
	protected void speedup(double factor) {
		sample.speedup(factor);
	}
	
	// Loop the second half of the sound
	protected boolean make_urgent() {
		return urgent = true;
	}
	
	// Called on thread.start()
	public void run() {
		
		setPriority(Thread.MAX_PRIORITY);
		
		sample.setBuffsize(3500);
		buffsize = sample.getBuffsize();
		sample.setSampleRate(27000);
		samplerate = sample.getSampleRate();
		
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                samplerate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, buffsize, 
                AudioTrack.MODE_STREAM);		
		
		while (keeprunning)
		{
			switch (state) {
			case 'p':
				audioTrack.play();
				isrunning = true;
				while (isrunning) { 
					
					if(silent_start) {
						insertSilence();
					}
					
					buffsize = sample.getBuffsize(); //continue to get in case of update.
					tune = sample.getSampleType(which_sample); //determine pitch content, rests
					amp = sample.getAmplitude();
				
				    short samples[] = new short [buffsize];
					
		    	    freq = tune[sound_index];
				
		    	    if (which_sample == 3) { //sawtooth
					    for (int i=0; i < buffsize; i++) { 
						    samples[i] = (short)(amp*angle);
						    if (i < (buffsize/2))
							    angle = ((angle + (double)2*freq/samplerate)) % 2;			
						    else
							    angle = ((angle + ((double)2*freq/samplerate)-2)) % 2;
					    }
		    	    }
		    	    
		    	    else if (which_sample == 2 || which_sample == 4) { //manipulated sin wave
		    			for (int i=0; i < buffsize; i++) { 
		    				if (i < buffsize/2)
		    				    samples[i] = (short)((i/amp)*FloatMath.sin((float)angle));
		    			    else if (i > 1*buffsize/2)
		    				    samples[i] = (short)((amp-(i*32))*FloatMath.sin((float)angle));
		    				else
		    				    samples[i] = (short)((amp)*FloatMath.sin((float)angle));
		    			
		    				if (tune[sound_index] != 0)
		    					angle = ((angle + (double)twopi*freq/samplerate) % (2.0f * (double)Math.PI));
		    				else 	
		    					angle = 0;
		    			}
		        	}
		    	    		    	    
				    try { //cyclic barrier
					    cbar.await();
				    } catch (Exception exc) {
					    System.out.println(exc);
				      }	
		    	    
				    audioTrack.write(samples, 0, buffsize);
				    
				    counter++;	
				    	    
				    //this block advances the audio loop through the sample array
				    halfway = sample.getHalfwayPoint();
				    if (counter % tune[duration_loc] == 0) {
				    	sound_index+=2;
				    	duration_loc+=2;
				    	if (sound_index == tune.length)
				    		sound_index = 0;
				    	if (duration_loc == tune.length + 1) {
				    		if (urgent) {
				    			duration_loc = halfway+1;
				    			sound_index = halfway;
				    		    urgent = false;	
								System.out.println("urgent duration_loc: " + duration_loc);
				    		}
				    		else	
				    		    duration_loc = 1;
				    	}
				    	//buffsize = (int)(sample.buff_shrink_factor*buffsize); //duplicitous
				    	counter = 0; 
				    }
					break;
				}
			case 's':
				audioTrack.pause();
				break;
			}
	    }
	    audioTrack.stop(); //shut down the sound
	    audioTrack.release();
    }
}
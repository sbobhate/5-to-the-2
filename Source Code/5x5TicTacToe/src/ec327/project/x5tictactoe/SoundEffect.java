package ec327.project.x5tictactoe;

/*
 * @author Micheal Reavey
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.FloatMath;

public class SoundEffect extends Thread {

	private AudioTrack audioTrack;
    private Sample sample = new Sample();

    private int which_sample;
	private int counter = 0;
	private int sound_index = 0;
	private int duration_loc = 1;
	private double freq = 440.0f;
	private double angle = 0.1f;
	private double twopi = 8.*Math.atan(1.);

	private boolean isrunning = true;

    private int buffsize;
    private int samplerate;
    private int amp;
    private double [] tune;

    // Constructor
    protected SoundEffect(int w) {
		which_sample = w;
	}

	// Called on thread.start()
    @SuppressLint("FloatMath")
	public void run() {

		setPriority(Thread.MAX_PRIORITY);

		sample.setBuffsize(5000); //Get's amended later, dummy value for now
		buffsize = sample.getBuffsize();
		sample.setSampleRate(27000);
		samplerate = sample.getSampleRate();

		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                samplerate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, buffsize, 
                AudioTrack.MODE_STREAM);		

		audioTrack.play();
		while (isrunning)
		{			
					tune = sample.getSampleType(which_sample); //determine pitch content, rests

					amp = sample.getAmplitude();

				    short samples[] = new short [buffsize];

		    	    freq = tune[sound_index];

		    	    if (which_sample == 5) { //pause
		    	    	sample.setBuffsize(4000);
		    	    	buffsize = sample.getBuffsize();
					    for (int i=0; i < buffsize; i++) 
					    {
						    samples[i] = (short) (amp*(angle));
						    if (tune[sound_index] != 0) {
						        if (i < (buffsize/2)) //generate sample
							        angle = ((angle + (double)2*freq/samplerate)) % 2;	
						        else
							        angle = ((angle + ((double)2*freq/samplerate)-2)) % 2;
						    }
						    else //silence case
						    	angle = 0;
					    }
		    	    }
		    	    else { //quit and win,lose,tie
		    	    	if (which_sample == 6) {
		    	    	    sample.setBuffsize(3000);
		    	    	    buffsize = sample.getBuffsize();
		    	    	}
		    	    	else {
		    	    	    sample.setBuffsize(4000);
		    	    	    buffsize = sample.getBuffsize();
		    	    	}
					    for (int i=0; i < buffsize; i++) {
		    				if (i < buffsize/2)
		    				    samples[i] = (short)((i/amp)*FloatMath.sin((float)angle));
		    			    else
		    				    samples[i] = (short)((amp-(i*32))*FloatMath.sin((float)angle));
		    				if (tune[sound_index] != 0)
		    					angle = ((angle + (double)twopi*freq/samplerate) % (2.0f * (double)Math.PI));		
		    				else
		    					angle = 0;
					    }
		    		}
				    audioTrack.write(samples, 0, buffsize);

				    counter++;	

				    if (counter % tune[duration_loc] == 0) {				    
				    	sound_index+=2;
				    	duration_loc+=2;
				    	if (duration_loc == tune.length+1) {
				    	    isrunning = false;
				            audioTrack.stop(); //shut down the sound
				            audioTrack.release();
				    	}
				    }
		      }
		}	
}

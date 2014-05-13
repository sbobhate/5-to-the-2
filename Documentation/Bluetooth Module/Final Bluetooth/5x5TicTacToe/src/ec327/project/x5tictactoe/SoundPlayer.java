package ec327.project.x5tictactoe;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundPlayer implements Runnable {

	private Object mPauseLock;
	private boolean isPaused;
	private boolean isFinished;
	
	private int buffsize = 8000;
	//double buff_shrink_factor = 0.9375f;
	private double buff_shrink_factor = 1.0f;
	private short samples[] = new short[buffsize];
	private int amp = 10000;
	private double twopi = 8.*Math.atan(1.);
	private double freq = 440.f;
	private double angle = 0.25f;
	private double semitone = Math.pow(2.0f, 1/12.f);
	private double a4 = 440.f;
	double b4 = a4*(double)Math.pow(semitone, 2);
	double cs4 = a4*(double)Math.pow(semitone, 4);
	double d3 = a4*(double)Math.pow(semitone, -7);
	private double e5 = a4*(double)Math.pow(semitone, 19);
	//double [] tune = new double [] {cs4, 8, b4, 8, a4, 8, 0, 8}; //new double???
	private double [] tune = new double [] {0, 16, e5, 4, 0, 4, e5, 3, 0, 3, e5, 2, 0, 2, e5, 1, 0, 1};
	private int timing_location  = 1;
	private int samplerate = 22500;
	protected boolean isRunning = true;
	
	AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
            samplerate, AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT, buffsize, 
            AudioTrack.MODE_STREAM);
	
	public SoundPlayer ()
	{
		mPauseLock = new Object ();
		isPaused = false;
		isFinished = false;
	}
	
	private void playSound ()
	{
		// start audio
		audioTrack.play();
		int counter = 0;
		int index = 0;
		while(isRunning) {
    	    freq = tune[index];
			for (int i=0; i < buffsize; i++) { 
				samples[i] = (short)(amp*Math.sin((float)angle));
				//samples[i] = (short)(amp*angle);
				if (i < (buffsize/2))
					//angle = ((angle + (double)2*freq/samplerate)) % 2;
					angle = ((angle + (double)twopi*freq/samplerate) % (2.0f * (double)Math.PI));					
				else
					//angle = ((angle + ((double)2*freq/samplerate)-2)) % 2;
					angle = ((angle + (double)twopi*freq/samplerate) % (2.0f * (double)Math.PI));
			}
		    audioTrack.write(samples, 0, buffsize);
		    counter+=1;
	    	//System.out.println("Timing" + timing_location);
		    if (counter % tune[timing_location] == 0) {
		    	index+=2;
		    	timing_location+=2;
		    	if (index == tune.length)
		    		index = 0;
		    	if (timing_location == tune.length + 1) {
		    		timing_location = 1;
		    		isRunning = false;
		    	}

		    	buffsize = (int)(buff_shrink_factor*buffsize);
				//System.out.println("buffsize: " + buffsize);
		    	counter = 0;
		    	if (buffsize < 500) {
		    		isRunning = false;
		    	}
		    }
		}    
		audioTrack.stop();
		audioTrack.release();
		isFinished = true;
	}
	
	protected void stopSound ()
	{
		audioTrack.stop();
		audioTrack.release();
	}
	
	protected void pauseSound ()
	{
		synchronized (mPauseLock) {
			isPaused = true;
		}
	}
	
	protected void resumeSound()
	{
		synchronized (mPauseLock) {
			isPaused = false;
			mPauseLock.notifyAll();
		}
	}

	@Override
	public void run() {
		while (!isFinished)
		{
			playSound();
			synchronized (mPauseLock)
			{
				while (isPaused)
				{
					try{
						mPauseLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}
	
}

package ec327.project.x5tictactoe;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

/*
 * Game Timer Class
 * Private Members:
 	- The Progress Bar
 	- The time
 	- The boolean of whether the time has started
 * Protected Members:
 	- The constructor
 	- The on finish method that controls what happens on the end of a turn
 	- The on tick method that determines what happens on each tick
 	- The set progress bar method that gets a reference to the progress bar
 	- The start timer count down timer method that sets the boolean to true
 	- The get status method that returns whether the timer has started or not
 */

public class GameTimer extends CountDownTimer {
	
	private ProgressBar timer;
	private int time;
    private boolean timerHasStarted;
    
    Context context;
    
    protected GameTimer(Context c, long startTime, long Interval) 
	{
		super(startTime, Interval);
		context = c;
		timerHasStarted = false;
		Log.i("Debug", "Game Timer Created.");
	}
    
    @Override
    public void onFinish() 
    {
    	timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_green));
    	if (time == 0)
    	{
    		cancel();
	        timer.setProgress(0);
	        timerHasStarted = false;
	        Log.w("Debug", "Time Over.");
    	}
    	else
    	{
    		cancel();
    		Log.w("Debug", "Next Round.");
    		start();
    	}
    }

    @Override
    public void onTick(long millisUntilFinished) 
    {
    	time = (int) Math.ceil(millisUntilFinished);
    	timer.setProgress(time);
    	if (time <= 2500 && time > 1000)
    	{
    		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_yellow));
    	}
    	else if (time <= 1000 && time > 0)
    		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_red));
//    	Log.w("Debug", "" + time);
    }
    
    protected void setProgressBar (ProgressBar bar)
    {
    	timer = bar;
    }
	
    protected void startCountdownTimer() 
    {
    	timerHasStarted = true;
    	Log.i("Debug", "Game Timer Started.");
    }
    
    protected boolean getStatus ()
    {
    	return timerHasStarted;
    }
    
}

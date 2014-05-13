package ec327.project.x5tictactoe;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

/*
 * @author Shantanu Bobhate
 * EC327, Spring 2014
 * Boston University, Boston, MA
 * 
 * Copyright (C) 2014 Shantanu Bobhate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Game Timer Class
 	- Is a derived class that extends the CountDownTimer
 	- Handles all the timer related functions
 * 
 * Private Members:
 	- The maxTime
 	- The Progress Bar
 	- The time
 	- The boolean of whether the time is up
 *
 * Protected Members:
 	- The constructor
 	- The set progress bar method that gets a reference to the progress bar
 	- The start timer count down timer method that sets the boolean to false
 	- The end turn method that controls what happens at the end of a turn where time is not up
 	- The get status method that returns whether the time is over or not
  *
  * Public Members:
  	- The interface that communicates with Game Manager
  	- The on finish method that controls what happens when the time is over
 	- The on tick method that determines what happens on each tick
  *
 */

public class GameTimer extends CountDownTimer {
	
	static private int maxTime;
	private ProgressBar timer;
	private int time;
	private boolean timeIsUp = false;;
    protected TimerCommunicator communicator;
    
    Context context;
    
    protected GameTimer(Context c, long startTime, long Interval) 
	{
		super(startTime, Interval);
		context = c;
		maxTime = (int) startTime;
		timeIsUp = false;
		Log.i("Debug", "Game Timer Created.");
	}
    
    @Override
    public void onFinish() 
    {
		cancel();
		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_green));
        timer.setProgress(0);
        timeIsUp = true;
        communicator.timeIsUp();
        Log.w("Debug", "Time Over.");
}

    @Override
    public void onTick(long millisUntilFinished) 
    {
    	time = (int) Math.ceil(millisUntilFinished);
    	timer.setProgress(time);
    	if (time <= 1000 && time > 500)
    	{
    		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_yellow));
    	}
    	else if (time <= 500 && time > 0)
    		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_red));
    }
    
    protected void setProgressBar (ProgressBar bar)
    {
    	timer = bar;
    }
	
    protected void startCountdownTimer() 
    {
    	start();
    	timeIsUp = false;
    	Log.i("Debug", "Game Timer Started.");
    }
    
    protected void endTurn ()
    {
    	if (time > 0)
    	{
    		cancel();
    		timer.setProgress(maxTime);
    		timer.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_timer_green));
    		time = maxTime;
    		start();
    	}
    }
    
    protected boolean timeUp ()
    {
    	return timeIsUp;
    }
    
    public interface TimerCommunicator
    {
    	public void timeIsUp();
    }
    
}

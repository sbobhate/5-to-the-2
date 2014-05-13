package ec327.project.x5tictactoe;

/*
 * Game Activity
 * Objects:
 	- A Board board1
 	- An Automated Player
 	- A Game Timers
 * Current Problems:
 	- Making the game stop when the time runs out
 	- Introducing the sound factor
 * Improvements:
 	- Making the timer decrease smoother
 	- UI elements
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Game extends Activity {

	private Board board1;
	private int moveCounter;
	private boolean[] changed = new boolean [25];
	private GridView gridview;
	private CustomDialog dialog;
	private int mode;
	private AutomatedPlayer player;
	private ProgressBar timer;
	private int time = 5000;
	private GameTimer gTimer;
	private long startTime = 5000;		// 5 seconds
    private long interval = 1;		// 1 second
    private Vibrator thunder;
    private wifiConnection wific;    //add by Ted
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		Log.w("Debug", "New Game Created.");
		
		setup();
	}
	
	@Override
	protected void onPause ()
	{
		gTimer.onFinish();
		Log.w("Debug", "Game Paused");
		super.onPause();
	}
	
	@Override
	protected void onResume ()
	{
		Log.w("Debug", "Game Resumed");
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		gTimer.cancel();
		Log.w("Debug", "Game Destroyed.");
		super.onDestroy();
	}

	// Setup Function:
	private void setup ()
	{
		Intent oldActivity = getIntent ();
		mode = oldActivity.getIntExtra("mode", 0);
		
		Log.i("Debug", "Setting Up Game.");
		
		board1 = new Board ();
        board1.clear();
        moveCounter = 0;
        
        for (int i = 0; i < changed.length; i++)
        {
        	changed[i] = false;
        }
         
        TextView text = (TextView) findViewById(R.id.title);
        Typeface face=Typeface.createFromAsset(getAssets(), "tr2n.ttf"); 
        text.setTypeface(face);
        
        dialog = new CustomDialog (1);
        
        thunder = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        
        timer = (ProgressBar) findViewById(R.id.timer);
		timer.setMax(time);
		timer.setProgress(time);
		
        gridview = (GridView) findViewById(R.id.board);
        gridview.setAdapter(new ImageAdapter(this));

        if (mode == 1)
        {
        	Log.i("Debug", "Classic One Player Game Started.");
        }
        else if (mode == 2)
        {
        	gTimer = new GameTimer (this, startTime, interval);
    		gTimer.setProgressBar(timer);
    		gTimer.startCountdownTimer();
        	gTimer.start();
        	Log.i("Debug", "Timed One Player Game Started.");
        }
        else if (mode == 3)
        {
        	wific = new wifiConnection();
        	while(!wific.isStarted());
			//Toast.makeText(Game.this, "wifi Game Started.", 100).show();
        	Log.i("Debug", "wifi Game Started.");
        }
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Code to start game goes here
            	if (mode == 1)
            	{
        			player = new AutomatedPlayer (board1);
        			playOnePlayer (v, position);
            	}
            	else if (mode == 2)
            	{
        			player = new AutomatedPlayer (board1);
        			playOnePlayer (v, position);
            	}
            	else if (mode == 3)
            	{
            		Log.i("Debug", "pressed");
        			localwifigame (v, position);
            	}
            }
        });
        
	}
	
	public void playOnePlayer (View v, int position)
	{
		char boardStatus = board1.getBoardStatus();
    	if (boardStatus == 'c')
    	{
    		if (moveCounter % 2 == 0)
        	{
        		if (changed[position] == false)
        		{
        			board1.writeToBoard(position, 'x');
        			((ImageView) v).setImageResource(R.drawable.x);
        			changed[position] = true;
        			if (gTimer != null)
        				gTimer.onFinish();
        			if (board1.getBoardStatus() != 'c')
        			{
        				dialog.show(getFragmentManager(), "Conclusion Dialog");
        				thunder.vibrate(500);
        				if (gTimer != null)
	    					gTimer.cancel();
        			}
        			else
        			{
        				player.makeDefensiveMove();
        				int index = player.getIndex();
		        		ImageView imageview = (ImageView) gridview.getChildAt(index);
		        		imageview.setImageResource(R.drawable.o);
		        		changed[index] = true;
		        		moveCounter += 2;
		    			if (board1.getBoardStatus() != 'c')
		    			{
		    				dialog.show(getFragmentManager(), "Conclusion Dialog");
		    				thunder.vibrate(500);
		    				if (gTimer != null)
		    					gTimer.cancel();
		    			}
        			}
        		}
        	}
    	}
	}
	
	/* To Shantanu:
	 * I replace the original local game option with wifi game in this demo
	 * since I am going to change the UI here.
	 * You might create a mode 4 that call the wifi game option in the complete version.
	 * In the wifi option, I abandon "host" and "join" action. All game in wifi mode
	 * will try to scan the router and join existing game first, and then create a game
	 * if no game exists under current local network.
	 */
	
	
	/* the original local game
	public void playTwoPlayer (View v, int position)
	{
		char boardStatus = board1.getBoardStatus();
    	if (boardStatus == 'c')
    	{
        	if (moveCounter % 2 == 0)
        	{
        		if (changed[position] == false)
        		{
        			board1.writeToBoard(position, 'x');
        			((ImageView) v).setImageResource(R.drawable.x);
        			changed[position] = true;
        			moveCounter ++;
        			if (board1.getBoardStatus() != 'c')
        			{
        				dialog.show(getFragmentManager(), "Conclusion Dialog");
        				thunder.vibrate(500);
        			}
        		}
        		
        	}
        	else
        	{
        		if (changed[position] == false)
        		{
        			board1.writeToBoard(position, 'o');
        			((ImageView) v).setImageResource(R.drawable.o);
        			changed[position] = true;
        			moveCounter ++;
        			if (board1.getBoardStatus() != 'c')
        			{
        				dialog.show(getFragmentManager(), "Conclusion Dialog");
        				thunder.vibrate(500);
        			}
        		}
        	}
    	}
    	else
    	{
    		
    	}
	}
	*/
	
	/*
	 * Here is the wifi game mode
	 */
	public void localwifigame (View v, int position){
		char boardStatus = board1.getBoardStatus();
		if(wific.isStarted()){

			if (wific.isHost()){  // is host
				if (boardStatus == 'c')
		    	{	
					if (moveCounter % 2 == 0){
						if (changed[position] == false)
		        		{
		        			board1.writeToBoard(position, 'x');
		        			((ImageView) v).setImageResource(R.drawable.x);
		        			changed[position] = true;

		        			final String boardstr = new String(board1.getBoard());
	
		        		    wific.SetSendingMsg(boardstr);

		        		    moveCounter ++;
		        		    			
		        		    
		        		    
		        			if (board1.getBoardStatus() != 'c')
		        			{
		        				dialog.show(getFragmentManager(), "Conclusion Dialog");
		        				thunder.vibrate(500);
		        			}
		        			String msg = new String(board1.getBoard());
		        			String received = new String(board1.getBoard());
		        			while (msg.equals(received) || received.length() != 25){

				        				received = wific.GetReceivedMsg();

	    		    		}
		        			moveCounter ++;
		        			char temp[] = received.toCharArray();
		        			
		        			board1.updateboard(temp);
		        			updatePosition(temp);

		        		}
					}
						
		    	}
			} else { //  is client
				// update board first
    			String msg = new String(board1.getBoard());
    			String received = new String(board1.getBoard());
    			while (msg.equals(received) || received.length() != 25){
    				received = wific.GetReceivedMsg();
    			}
    			moveCounter ++;
    			char temp[] = received.toCharArray();
    			
    			board1.updateboard(temp);
    			updatePosition(temp);
    			if (moveCounter % 2 == 1){
    				if (changed[position] == false){
            			board1.writeToBoard(position, 'o');
            			((ImageView) v).setImageResource(R.drawable.o);
            			changed[position] = true;
            			
            			final String boardstr = new String(board1.getBoard());
            			
                		wific.SetSendingMsg(boardstr);
                			
            			moveCounter ++;

            			if (board1.getBoardStatus() != 'c')
            			{
            				dialog.show(getFragmentManager(), "Conclusion Dialog");
            				thunder.vibrate(500);
            			}
            		}	
    			}
	        }		
		}
	}		
	
		
    	


	public void updatePosition(char [] temp){
		for (int i = 0; i < 25; i++){
			changed[i] = (temp[i] != '#');
		}
	}
	
	public void startOver (View view)
	{
		Log.i("Debug", "Starting Over.");
		board1.clear();
		int size = gridview.getChildCount();
		for (int i = 0; i < size; i++) {
			ImageView imageView = (ImageView) gridview.getChildAt(i);
			imageView.setImageResource(R.drawable.dark_box);
			changed[i] = false;
	    }
		moveCounter = 0;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.two_player, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

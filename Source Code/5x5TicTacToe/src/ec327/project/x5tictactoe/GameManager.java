package ec327.project.x5tictactoe;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import ec327.project.x5tictactoe.GameTimer.TimerCommunicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
 * Game Manager Class
 	- Handles all the game related functions
 *
 * Classes it communicates with:
 	- Board
 	- Automated Player
 	- Game Timer
 	- Custom Dialog
 	- Sound Track
 	- Welcome Track
 *
 * game_type catalog:
 	- 1: Classic Single Player
 	- 2: Timed Single Player
 	- 3: Local Two Player
 	- 4: Bluetooth Two Player
 *
 */

@SuppressWarnings("unused")
public class GameManager {

	private int moveCounter;
	private int xWinCounter;
	private int oWinCounter;
	private int totalGamesWon;
	private int points;
	
	private GridView gridview;
	private TextView xScoreBoard;
	private TextView oScoreBoard;
	
	private Board board1;
	private boolean[] changed = new boolean [25];
	private int game_type;
	private AutomatedPlayer player;
	private ProgressBar timer;
	private int maxTime = 2000;
	private GameTimer gTimer;
	private long startTime = maxTime;
    private long interval = 1;
    private CustomDialog dialog;
    private Vibrator thunder;
    
    private static final String DEFAULT = "N/A";
    private static final long DEFAULT_LONG = 0;
    
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    
    private SoundTrack fivebfive;
    private WelcomeTrack welcome_sound = new WelcomeTrack();
    private boolean wasPaused = false;
    private boolean welcomeRunning = false;
    private boolean soundRunning = false;
    private boolean mute; // Mute
    
    private char symbol;
    
	protected GameManager (Context c, GoogleApiClient client)
	{
		context = c;
		mGoogleApiClient = client;
		setup();
	}
	
	private void setup ()
	{
		Intent oldActivity = ((Activity) context).getIntent ();
		game_type = oldActivity.getIntExtra("game_type", 0);
		// Mute
		mute = oldActivity.getBooleanExtra("mute_state", false);
		
		board1 = new Board ();
        board1.clear();
        moveCounter = 0;
        xWinCounter = 0;
        oWinCounter = 0;
        
        loadGameData();
        
        for (int i = 0; i < changed.length; i++)
        {
        	changed[i] = false;
        }
         
        xScoreBoard = (TextView) ((Activity) context).findViewById(R.id.x_score_box);
        oScoreBoard = (TextView) ((Activity) context).findViewById(R.id.o_score_box);
        
        TextView restart = (TextView) ((Activity) context).findViewById(R.id.restart);
        TextView text = (TextView) ((Activity) context).findViewById(R.id.title);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "tr2n.ttf"); 
        text.setTypeface(face);
        restart.setTypeface(face);
        xScoreBoard.setTypeface(face);
        oScoreBoard.setTypeface(face);
        
        fivebfive = new SoundTrack();
        fivebfive.playSoundTrack(1);
        soundRunning = true;
        
        thunder = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
        gridview = (GridView) ((Activity) context).findViewById(R.id.board);
        gridview.setAdapter(new ImageAdapter(context));
        
        timer = (ProgressBar) ((Activity) context).findViewById(R.id.timer);
        
        if (game_type == 1)
        {	
        	player = new AutomatedPlayer (board1);
        	timer.setIndeterminate(true);
        	// Log.i("Debug", "Classic One Player Game Started.");
        }
        else if (game_type == 2)
        {
        	player = new AutomatedPlayer (board1);
    		timer.setMax(maxTime);
    		timer.setProgress(maxTime);
        	gTimer = new GameTimer (context, startTime, interval);
    		gTimer.setProgressBar(timer);
    		gTimer.communicator = new TimerCommunicator () {
				@Override
				public void timeIsUp() {
					loserSound();
					showDialog(5);
					thunder.vibrate(500);
    				pushAccomplishments();
    				cancelTimer();
				}
    		};
    		gTimer.startCountdownTimer();
        	// Log.i("Debug", "Timed One Player Game Started.");
        }
        else if (game_type == 3)
        {
        	timer.setIndeterminate(true);
        	// Log.i("Debug", "Local Two Player Game Started.");
        }
        else if (game_type == 4)
        {
        	((GlobalVariables) ((Activity)context).getApplication()).getConnectionService().setHandler(
    				handler);
        	int s = ((GlobalVariables) ((Activity)context).getApplication()).getSymbol();
        	if (s == 2) symbol = 'x';
        	else if (s == 3) symbol = 'o';
        	// Log.i("Debug", "Bluetooth Two Player Game Started.");
        }
        else
        	Toast.makeText(context, "Incorrect Game Mode", Toast.LENGTH_SHORT).show();
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	if (game_type == 1)
        			playOnePlayer (v, position);
            	else if (game_type == 2)
        			playOnePlayer (v, position);
            	else if (game_type == 3)
        			playTwoPlayer (v, position);
            	else if (game_type == 4)
            	{
            		playBtGame(v, position);
            	}
            }
        });
	}
	
	public void playOnePlayer (View v, int position)
	{
		int number_of_moves = 0;
		char boardStatus = board1.getBoardStatus();
		if (boardStatus == 'c')
		{
			if (moveCounter % 2 == 0)
	    	{
	    		if (changed[position] == false)
	    		{
	    			if (gTimer == null || !gTimer.timeUp())
	    			{
	    				number_of_moves++;
		    			board1.writeToBoard(position, 'x');
		    			((ImageView) v).setImageResource(R.drawable.x);
		    			changed[position] = true;
		    			if (board1.getAdvancedBoardStatus(position) == 'x')
		    				makeWarningSound();
		    			if (gTimer != null)
		    				gTimer.endTurn();
		    			if (board1.getBoardStatus() != 'c')
		    			{
		    				if (board1.getBoardStatus() == 'x')
		    				{
		    					if (game_type == 2 && number_of_moves < 10)
		    					{
		    						Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.champ));
		    						increaseTotalPoints(20);
		    					}
			    				if (mGoogleApiClient.isConnected())
			    				{
			    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.first_win));
			    					Games.Achievements.increment(mGoogleApiClient, context.getString(R.string.getting_good), 1);
			    				}
			    				increaseTotalGamesWon();
			    				increaseTotalPoints(10);
			    				pushAccomplishments();
			    				increaseXWinCounter();
			    				showDialog(3);
			    				winnerSound();
			    				long[] pattern = {0, 100, 100, 300};
			    				thunder.vibrate(pattern, -1);
		    				}
		    				else if (board1.getBoardStatus() == 't')
		    				{
		    					showDialog(8);
		    					tieSound();
			    				thunder.vibrate(500);
			    				pushAccomplishments();
		    				}
		    				cancelTimer();
		    			}
		    			else
		    			{
		    				player.makeDefensiveMove();
		    				int index = player.getIndex();
			        		ImageView imageview = (ImageView) gridview.getChildAt(index);
			        		imageview.setImageResource(R.drawable.o);
			        		changed[index] = true;
			        		if (board1.getAdvancedBoardStatus(position) == 'o')
			    				makeWarningSound();
			        		moveCounter += 2;
			    			if (board1.getBoardStatus() != 'c' && board1.getBoardStatus() == 'o')
			    			{
			    				showDialog(4);
			    				loserSound();
			    				long[] pattern = {0, 300, 100, 100};
			    				thunder.vibrate(pattern, -1);
			    				increaseOWinCounter();
			    				pushAccomplishments();
			    				cancelTimer();
			    			}
			    			else if (board1.getBoardStatus() == 't')
			    			{
			    				showDialog(8);
			    				tieSound();
			    				thunder.vibrate(500);
			    				pushAccomplishments();
			    				cancelTimer();
			    			}
			    			speedUpSound();
		    			}
	    			}
	    		}
	    	}
		}
	}

	private void playTwoPlayer (View v, int position)
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
	    			if (board1.getAdvancedBoardStatus(position) == 'x')
	    				makeWarningSound();
	    			moveCounter ++;
	    			if (board1.getBoardStatus() != 'c')
	    			{
	    				if (board1.getBoardStatus() == 'x')
	    				{
		    				increaseXWinCounter();
		    				winnerSound();
		    				showDialog(6);
		    				thunder.vibrate(500);
	    				}
	    				else if (board1.getBoardStatus() == 't')
	    				{
	    					showDialog(8);
	    					tieSound();
	    					thunder.vibrate(500);
	    				}
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
	    			if (board1.getAdvancedBoardStatus(position) == 'o')
	    				makeWarningSound();
	    			moveCounter ++;
	    			if (board1.getBoardStatus() != 'c')
	    			{
	    				if (board1.getBoardStatus() == 'x')
	    				{
		    				increaseOWinCounter();
		    				showDialog(7);
		    				winnerSound();
		    				thunder.vibrate(500);
	    				}
	    				else if (board1.getBoardStatus() == 't')
	    				{
	    					showDialog(8);
	    					thunder.vibrate(500);
	    					tieSound();
	    				}
	    			}
	    		}
	    	}
		}
		else
		{
			showDialog(8);
		}
	}

	private void playBtGame(View v, int position){
		char boardStatus = board1.getBoardStatus();
		if (boardStatus == 'c'){
			if (changed[position] == false) {
				if (symbol == 'x' && moveCounter % 2 == 0){
					// create message
					MessageContainer m = new MessageContainer();
					m.setMessage(((GlobalVariables) ((Activity)context).getApplication()).getSymbol());
					m.setCoords(position);

					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ObjectOutputStream oos;
						oos = new ObjectOutputStream(baos);
						oos.writeObject(m);
						((GlobalVariables) ((Activity)context).getApplication()).getConnectionService()
								.write(baos.toByteArray());
						
						board1.writeToBoard(position, 'x');
						((ImageView) v).setImageResource(R.drawable.x);
						changed[position] = true;
						moveCounter++;

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (board1.getBoardStatus() != 'c')
	    			{
	    				if (board1.getBoardStatus() == 'x')
	    				{
		    				increaseXWinCounter();
		    				winnerSound();
		    				showDialog(3);
		    				thunder.vibrate(500);
		    				if (mGoogleApiClient.isConnected())
		    				{
		    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
		    					Games.Achievements.increment(mGoogleApiClient, context.getString(R.string.making_a_name), 1);
		    				}
	    				}
	    				else if (board1.getBoardStatus() == 't')
	    				{
	    					if (mGoogleApiClient.isConnected())
		    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
	    					showDialog(8);
	    					tieSound();
	    					thunder.vibrate(500);
	    				}
	    			}
				} else if (symbol == 'o' && moveCounter % 2 == 1){
					// create message
					MessageContainer m = new MessageContainer();
					m.setMessage(((GlobalVariables) ((Activity)context).getApplication()).getSymbol());
					m.setCoords(position);

					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ObjectOutputStream oos;
						oos = new ObjectOutputStream(baos);
						oos.writeObject(m);
						((GlobalVariables) ((Activity)context).getApplication()).getConnectionService()
								.write(baos.toByteArray());
						
						board1.writeToBoard(position, 'o');
						((ImageView) v).setImageResource(R.drawable.o);
						changed[position] = true;
						moveCounter++;

					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (board1.getBoardStatus() != 'c')
	    			{
	    				if (board1.getBoardStatus() == 'o')
	    				{
	    					if (mGoogleApiClient.isConnected())
		    				{
		    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
		    					Games.Achievements.increment(mGoogleApiClient, context.getString(R.string.making_a_name), 1);
		    				}
		    				increaseOWinCounter();
		    				winnerSound();
		    				showDialog(3);
		    				thunder.vibrate(500);
	    				}
	    				else if (board1.getBoardStatus() == 't')
	    				{
	    					if (mGoogleApiClient.isConnected())
		    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
	    					showDialog(8);
	    					tieSound();
	    					thunder.vibrate(500);
	    				}
	    			}
				}	
			}
		}else
		{
			showDialog(8);
		}
	}
	
	protected void replay ()
	{
		// Log.i("Debug", "Starting Over.");
		board1.clear();
		int size = gridview.getChildCount();
		for (int i = 0; i < size; i++) {
			ImageView imageView = (ImageView) gridview.getChildAt(i);
			imageView.setImageResource(R.drawable.dark_box);
			changed[i] = false;
	    }
		moveCounter = 0;
		cancelTimer();
		RestartTimer();
		replaySound();
		if (game_type == 4)
		{
			MessageContainer m = new MessageContainer();
			m.setMessage(8);
			m.setCoords(0);
	
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(baos);
				oos.writeObject(m);
				((GlobalVariables) ((Activity)context).getApplication()).getConnectionService()
						.write(baos.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void replayBt ()
	{
		board1.clear();
		int size = gridview.getChildCount();
		for (int i = 0; i < size; i++) {
			ImageView imageView = (ImageView) gridview.getChildAt(i);
			imageView.setImageResource(R.drawable.dark_box);
			changed[i] = false;
	    }
		moveCounter = 0;
		cancelTimer();
		RestartTimer();
		replaySound();
	}
	
	protected void startOver ()
	{
		// Log.i("Debug", "Starting Over.");
		board1.clear();
		int size = gridview.getChildCount();
		for (int i = 0; i < size; i++) {
			ImageView imageView = (ImageView) gridview.getChildAt(i);
			imageView.setImageResource(R.drawable.dark_box);
			changed[i] = false;
	    }
		moveCounter = 0;
		xWinCounter = 0;
		oWinCounter = 0;
		updateWinCounters();
		cancelTimer();
		RestartTimer();
		startOverSound();
	}
	
	private void showDialog (int id)
	{
		dialog = new CustomDialog (id, mute);
		String [] tags = {"You Win ", "You Lose ", "Out Of Time ", "X Wins ", "O Wins ", "Tie "};
		dialog.show(((Activity) context).getFragmentManager(), tags[id - 3] + "Dialog");
	}

	private void pushAccomplishments ()
	{
		if (mGoogleApiClient.isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.first_game));
    		if (game_type == 2)
				Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.the_challenger));
    		Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.total_games_won), getTotalGamesWon());
    		Games.Leaderboards.submitScore(mGoogleApiClient, context.getString(R.string.points), (long) getTotalPoints());
    		Games.Achievements.increment(mGoogleApiClient, context.getString(R.string.addicted), 1);
        }
	}
	
	private void increaseXWinCounter ()
	{
		xWinCounter++;
		updateWinCounters();
	}
	
	private void increaseOWinCounter ()
	{
		oWinCounter++;
		updateWinCounters();
	}
	
	private void updateWinCounters ()
	{
		xScoreBoard.setText("" + xWinCounter);
		oScoreBoard.setText("" + oWinCounter);
	}
	
	private void increaseTotalGamesWon ()
	{
		totalGamesWon ++;
	}
	
	private void increaseTotalPoints (int amount)
	{
		points += amount;
	}
	
	private String getUserName ()
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
		String name = sharedPreferences.getString("username", DEFAULT);
		if (name.equals(DEFAULT))
		{
			return "Player 1";
		}
		else
		{
			return name;
		}
	}
	
	private void loadGameData ()
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
		long number1 = sharedPreferences.getLong(getUserName() + "1", DEFAULT_LONG);
		long number2 = sharedPreferences.getLong(getUserName() + "2", DEFAULT_LONG);
		if (number1 == (DEFAULT_LONG))
		{
			totalGamesWon = 0;
		}
		else
		{
			totalGamesWon = (int) number1;
		}
		if (number2 == (DEFAULT_LONG))
		{
			points = 0;
		}
		else
		{
			points = (int) number2;
		}
	}
	
	protected void saveGameData ()
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(getUserName() + "1", totalGamesWon);
		editor.putLong(getUserName() + "2", points);
		editor.commit();
	}
	
	private int getTotalGamesWon ()
	{
		return totalGamesWon;
	}
	
	private double getTotalPoints ()
	{
		return points;
	}
	
	protected void cancelTimer ()
	{
		if (gTimer != null)
			gTimer.cancel();
	}
	
	private void RestartTimer ()
	{
		if (gTimer != null)
			gTimer.startCountdownTimer();
	}
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Log.i("Debug", "Incoming Message");

			switch (msg.what) {

			case BTmenu.MESSAGE_READ:

				try {

					byte[] readBuf = (byte[]) msg.obj;
					// int paramInt = msg.arg1;

					ByteArrayInputStream bais = new ByteArrayInputStream(
							readBuf);
					ObjectInputStream ois;
					ois = new ObjectInputStream(bais);

					MessageContainer readedMessage = (MessageContainer) ois
							.readObject();
					// Log.i("Debug","Message NUM: " + readedMessage.getMessage());

					switch (readedMessage.getMessage()) {

					case MessageContainer.MESSAGE_SYMBOL_O:
						int position = readedMessage.getCoords();
						board1.writeToBoard(position, 'o');
						ImageView imageView = (ImageView) gridview.getChildAt(position);
						imageView.setImageResource(R.drawable.o);
						changed[position] = true;
						moveCounter++;
						
						if (board1.getBoardStatus() != 'c')
		    			{
		    				if (board1.getBoardStatus() == 'o')
		    				{
		    					if (mGoogleApiClient.isConnected())
			    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
			    				increaseOWinCounter();
			    				loserSound();
			    				dialog = new CustomDialog (10, mute);
		    					dialog.show(((Activity) context).getFragmentManager(), "Dialog Lose Bt");
		    					thunder.vibrate(500);
		    				}
		    				else if (board1.getBoardStatus() == 't')
		    				{
		    					if (mGoogleApiClient.isConnected())
			    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
			    				tieSound();
		    					showDialog(8);
		    					thunder.vibrate(500);
		    				}
		    			} 
						break;

					case MessageContainer.MESSAGE_SYMBOL_X:
						int position1 = readedMessage.getCoords();
						board1.writeToBoard(position1, 'x');
						ImageView imageView1 = (ImageView) gridview.getChildAt(position1);
						imageView1.setImageResource(R.drawable.x);
						changed[position1] = true;
						moveCounter++;
						
						if (board1.getBoardStatus() != 'c')
		    			{
		    				if (board1.getBoardStatus() == 'x')
		    				{
		    					if (mGoogleApiClient.isConnected())
			    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
			    				increaseXWinCounter();
			    				loserSound();
			    				dialog = new CustomDialog (10, mute);
		    					dialog.show(((Activity) context).getFragmentManager(), "Dialog Lose Bt");
			    				thunder.vibrate(500);
		    				}
		    				else if (board1.getBoardStatus() == 't')
		    				{
		    					if (mGoogleApiClient.isConnected())
			    					Games.Achievements.unlock(mGoogleApiClient, context.getString(R.string.head_to_head));
		    					tieSound();
		    					showDialog(8);
		    					thunder.vibrate(500);
		    				}
		    			}
						break;

					case MessageContainer.START_OVER:
						
						if (dialog != null)
						{
							dialog.dismiss();
						}
						replayBt();

						break;

					case MessageContainer.MESSAGE_EXIT:

						// Log.i("Debug", "Stop connection and accept new ones");
						((GlobalVariables) ((Activity)context).getApplication())
								.getConnectionService().stop();
						((GlobalVariables) ((Activity)context).getApplication())
								.getConnectionService().start();
						((Activity)context).finish();

						break;

					case MessageContainer.MESSAGE_GAME_OVER:

						if (readedMessage.getCoords() == -1) {
							Toast.makeText(((Activity)context).getApplicationContext(),
									"Your opponent has quit the game",
									Toast.LENGTH_LONG).show();
							((Activity)context).finish();
						}

						break;

					default:

						break;
					}

				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			default:

				break;

			}

		}

	};
	
	public void onBackPressed() {
		
		if (game_type == 4){
			try {
				MessageContainer m = new MessageContainer(
						MessageContainer.MESSAGE_GAME_OVER, -1);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(baos);
				oos.writeObject(m);
				((GlobalVariables) ((Activity)context).getApplication()).getConnectionService().write(
						baos.toByteArray());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	///// SOUND CONTROL /////
	
	private void speedUpSound ()
    {
    	fivebfive.speedup(0.9375);
    }
	
	private void startOverSound ()
    {
    	//call this one after if you're in the middle of play
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
	
    private void makeWarningSound ()
    {
    	fivebfive.make_urgent();
    }
    
    private void winnerSound ()
    {
    	fivebfive.gameComplete(1);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;

    }
    
    public void loserSound ()
    {
    	fivebfive.gameComplete(2);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;
    }
    
    public void tieSound ()
    {
    	fivebfive.gameComplete(3);
    	soundRunning = false;
    	welcome_sound.playWelcomeTrack(1);
    	welcomeRunning = true;
    }
    
    private void replaySound ()
    {
    	welcome_sound.continuing();
    	welcome_sound.quit();
    	welcomeRunning = false;
        fivebfive = new SoundTrack();
        fivebfive.playSoundTrack(1);
        soundRunning = true;
    }
    
    protected void onPauseSound ()
    {
    	wasPaused = true;
    	if (((Activity) context).isFinishing() && soundRunning) {
        	fivebfive.set_to_quit();
    	}
    	else if (((Activity) context).isFinishing() && welcomeRunning) {
        	welcome_sound.prepare_to_quit();
    	}
    	if (soundRunning)
        	fivebfive.pause();
    	else if (welcomeRunning)
    		welcome_sound.pause();
    }
    
    protected void onResumeSound ()
    {
    	if (wasPaused && soundRunning) {
    		fivebfive.resume();
    		wasPaused = false;
    	}
    	else if (wasPaused && welcomeRunning) {
    		welcome_sound.resume();
    		wasPaused = false;
    	}
    }
    
    protected void onDestroySound ()
    {
    }
    
    protected boolean getMuteState ()
    {
    	return mute;
    }
    
    protected void setMuteState (boolean mute_state)
    {
    	mute = mute_state;
    }
	
}



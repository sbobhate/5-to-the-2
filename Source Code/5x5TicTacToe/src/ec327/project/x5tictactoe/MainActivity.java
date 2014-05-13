package ec327.project.x5tictactoe;

import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameActivity;

import ec327.project.x5tictactoe.CustomDialog.DialogCommunicator;

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
 * Main Activity
  	- The main menu of the game
  	- Contains the sign-in/out methods associated with the google plus API
  	- Acts as the controller to initiate games
 *
 */

public class MainActivity extends BaseGameActivity implements View.OnClickListener, DialogCommunicator {
	
    private static final String DEFAULT = "N/A";
    private String displayName;
    final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
    
    private WelcomeTrack welcome_sound;
    private boolean wasPaused = false;
	private boolean transition = false;
	private boolean mute;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null)
        	mute = false;
        else
        {
        	mute = savedInstanceState.getBoolean("mute_state");
        	Toast.makeText(this, "on saved exists", Toast.LENGTH_SHORT).show();
        }
        
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.single_player_button).setOnClickListener(this);
        findViewById(R.id.two_player_button).setOnClickListener(this);
        
        Log.i("Debug", "App started");
        
        setTypeface ();
        
        welcome_sound = new WelcomeTrack();
        welcome_sound.playWelcomeTrack();
        
        if (mute)
        	welcome_sound.pause();
        
    }
    
    @Override
    protected void onPause ()
    {
    	if (!mute)
    	{
    		wasPaused = true;
    		if (this.isFinishing() || transition) {
        		welcome_sound.prepare_to_quit();
        	}
    		welcome_sound.pause();
    	}
    	
    	super.onPause();
    	// Log.w("Debug", "App Paused");
    }
    
    @Override
    protected void onResume ()
    {
    	if (wasPaused) {
    		welcome_sound.resume();
    		wasPaused = false;
    	}
    	super.onResume();
    	// Log.w("Debug", "App Resumed");
    }
    
    @Override
    protected void onDestroy ()
    {
    	welcome_sound.quit();
    	super.onDestroy();
    	// Log.w("Debug", "App Stopped");
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
    	outState.putBoolean("mute_state", mute);
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState)
    {
    	mute = savedInstanceState.getBoolean("mute_state");
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
	public void onSignInFailed () {
		showSignInBar ();
		// Log.e("Debug", "Sign In Failed");
	}

	@Override
	public void onSignInSucceeded () {
		getUsername ();
		hideSignInBar ();
		// Log.i("Debug", "Sign In Succeeded");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
        	case R.id.sign_in_button:
        		beginUserInitiatedSignIn();
                break;
        	case R.id.single_player_button:
				startOnePlayer();
				transition = true;
				break;
        	case R.id.two_player_button:
        		startTwoPlayer();
        		transition = true;
        		break;
		}
	}
	
	private void setTypeface ()
	{
		TextView text = (TextView) findViewById(R.id.title);
        Typeface face=Typeface.createFromAsset(getAssets(), "tr2n.ttf"); 
        text.setTypeface(face);
	}
	
	private void startOnePlayer ()
    {
    	Log.i("Debug", "Starting One Player Game.");
    	transition = true;
    	CustomDialog onePlayerChoiceDialog = new CustomDialog (1, mute);
    	onePlayerChoiceDialog.show(getFragmentManager(), "One Player Choices");
    }
	
	private void startTwoPlayer ()
    {
    	Log.i("Debug", "Starting Two Player Game.");
    	transition = true;
    	CustomDialog twoPlayerChoiceDialog = new CustomDialog (2, mute);
    	twoPlayerChoiceDialog.show(getFragmentManager(), "Two Player Choices");
    }
	
	private void showSettings()
	{
		CustomDialog settings = new CustomDialog (9, mute);
		settings.show(getFragmentManager(), "Settings");
	}
	
	private void showSignInBar () {
        findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
    }
	
	private void hideSignInBar() {
        findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
    }
	
	private void getUsername ()
	{
		Player currentPlayer = Games.Players.getCurrentPlayer(getApiClient());
        if (currentPlayer == null) {
            displayName = "Player 1";
        } else {
            displayName = currentPlayer.getDisplayName();
            saveDisplayName ();
        }
	}
	
	private void saveDisplayName ()
	{
		SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String earlierName = sharedPreferences.getString("username", DEFAULT);
		if (earlierName == DEFAULT)
		{
			editor.putString("username", displayName);
		}
		else 
		{
			displayName = earlierName;
		}
		editor.commit();
	}
	
    public void onShowAchievementsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
    }

    public void onShowLeaderboardsRequested() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showSettings();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

	@Override
	public void newGame() {
	}

	@Override
	public void showAchievements() {
		onShowAchievementsRequested();
	}

	@Override
	public void showLeaderboards() {
		onShowLeaderboardsRequested();
	}

	@Override
	public void flipMusicState(boolean mute_state) {
		if (!mute_state) {
			welcome_sound.resume();
    		mute = mute_state;
    	}
		else
		{
	    	welcome_sound.pause();
	    	mute = mute_state;
		}
	}

	@Override
	public void signOutOfGoogle() {
		signOut();
		showSignInBar ();
	}
    
}

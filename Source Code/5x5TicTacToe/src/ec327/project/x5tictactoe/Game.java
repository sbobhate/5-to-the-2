package ec327.project.x5tictactoe;

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
 * Game Activity
 	- Contains a Game Manager object that handles all the game related function
 *
 */

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import ec327.project.x5tictactoe.CustomDialog.DialogCommunicator;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

@SuppressWarnings("unused")
public class Game extends BaseGameActivity implements DialogCommunicator {
    
	GameManager mGameManager;
	private final int RC_UNUSED = 5001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		// Log.i("Debug", "New Game Created.");
		
		mGameManager = new GameManager(this, getApiClient());
	}
	
	@Override
	protected void onPause ()
	{
		mGameManager.cancelTimer();
		mGameManager.onPauseSound();
		// Log.w("Debug", "Game Paused");
		super.onPause();
	}
	
	@Override
	protected void onResume ()
	{
		mGameManager.onResumeSound();
		// Log.w("Debug", "Game Resumed");
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		mGameManager.onDestroySound();
		mGameManager.saveGameData();
		mGameManager.cancelTimer();
		// Log.w("Debug", "Game Destroyed.");
		super.onDestroy();
	}
	
    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
    	outState.putBoolean("mute_state", mGameManager.getMuteState());
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState)
    {
    	mGameManager.setMuteState(savedInstanceState.getBoolean("mute_state"));
    	super.onRestoreInstanceState(savedInstanceState);
    }
	
	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}
	
	public void startOver (View v)
	{
		mGameManager.startOver();
	}
	
	@Override
	public void newGame() {
		mGameManager.replay();
	}

	@Override
	public void showAchievements() {
		if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.achievements_not_available));
        }
	}

	@Override
	public void showLeaderboards() {
		if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            showAlert(getString(R.string.leaderboards_not_available));
        }
	}

	@Override
	public void flipMusicState(boolean mute_state) {
		if (!mute_state) {
			mGameManager.onResumeSound();
			mGameManager.setMuteState(mute_state);
    	}
		else
		{
			mGameManager.onPauseSound();
			mGameManager.setMuteState(mute_state);
		}
	}

	@Override
	public void signOutOfGoogle() {
		signOut();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showSettings();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
	
	private void showSettings()
	{
		CustomDialog settings = new CustomDialog (9, mGameManager.getMuteState());
		settings.show(getFragmentManager(), "Settings");
	}
	
	@Override
	public void onBackPressed() {
		mGameManager.onBackPressed();
		super.onBackPressed();
	}

	
}

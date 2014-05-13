package ec327.project.x5tictactoe;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements View.OnClickListener {
	
	private static boolean DEBUG_ENABLED = true;
    private static final String TAG = "TrivialQuest";
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	enableDebugLog(DEBUG_ENABLED, TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        
        Log.i("Debug", "App started");
        
        TextView text = (TextView) findViewById(R.id.title);
        Typeface face=Typeface.createFromAsset(getAssets(), "tr2n.ttf"); 
        text.setTypeface(face);
    }
    
    @Override
    protected void onPause ()
    {
    	super.onPause();
    }
    
    @Override
    protected void onResume ()
    {
    	super.onResume();
    }
    
    @Override
    protected void onDestroy ()
    {
    	super.onDestroy();
    	Log.w("Debug", "App Stopped");
    }
    
    public void startOnePlayer (View view)
    {
    	Log.i("Debug", "Starting One Player Game.");
    	CustomDialog singlePlayerChoiceDialog = new CustomDialog (1);
    	singlePlayerChoiceDialog.show(getFragmentManager(), "Single Player Choices");
    }
    
    public void startTwoPlayer (View view)
    {
    	Log.i("Debug", "Starting Two Player Game.");
    	Intent newActivity = new Intent (this, Game.class);
    	newActivity.putExtra("mode", 3);
    	startActivity (newActivity);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
	    Log.w("Debug", "Exiting...");
        return true;
    }

    @Override
	public void onSignInFailed () {
		showSignInBar ();
	}

	@Override
	public void onSignInSucceeded () {
		showSignOutBar ();
	}
    
	private void showSignInBar () {
        findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
    }
	
	private void showSignOutBar() {
        findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
        	case R.id.sign_in_button:
        		beginUserInitiatedSignIn();
                break;
        	case R.id.sign_out_button:
        		signOut ();
        		showSignInBar ();
        		break;
        	case R.id.single_player_button:
				showAlert(getString(R.string.victory), getString(R.string.you_won));
				if (getApiClient().isConnected()) {
	                // unlock the "Trivial Victory" achievement.
	                Games.Achievements.unlock(getApiClient(), getString(R.string.play_first_game));
	            }
		}
	}
	
}

package ec327.project.x5tictactoe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
 * Custom Dialog Class
 	- Creates a custom dialog based on the argument provided
 	- Contains an interface that allows the dialog to communicate with the activity to show 
 	  leaderboards, showing achievements and changing the mute state
 * 
 * State Catalog:
 	- 1: One Player Choice
 	- 2: Two Player Choice
 	- 3: You Win Dialog
 	- 4: You Lose Dialog
 	- 5: Out Of Time Dialog
 	- 6: X Wins Dialog
 	- 7: O Wins Dialog
 	- 8: Tie Dialog
 	- 9: Settings Menu
 	- 10: Bluetooth Mode, You Lose Dialog, No Button
 *
 */

@SuppressLint("ValidFragment")
public class CustomDialog extends DialogFragment implements View.OnClickListener {
	
	private int state;
	private boolean mute;
	
	DialogCommunicator dialogCommunicator;
	
	ImageView newGameButton, volume;
	
	@SuppressLint("ValidFragment")
	public CustomDialog (int i, boolean mute_state)
	{
		state = i;
		mute = mute_state;
	}
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		Dialog dialog = new Dialog (getActivity (), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = null;
		if (state == 1 || state == 2)
		{
			view = inflater.inflate(R.layout.dialog_choice_single_player, null);
			String[] values = {};
			if (state == 1)
				values = new String[] {"Classic", "Race Against Time"};
			else if (state == 2)
				values = new String[] {"Local", "Bluetooth"};
			ListView list = (ListView) view.findViewById(R.id.list);
			CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.list_item, values);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position == 0)
					{
						if (state == 1)
						{
							Intent newActivity = new Intent (getActivity(), Game.class);
							newActivity.putExtra("game_type", 1);
							newActivity.putExtra("mute_state", mute);
							startActivity (newActivity);
						}
						else if (state == 2)
						{
							Intent newActivity = new Intent (getActivity(), Game.class);
					    	newActivity.putExtra("game_type", 3);
					    	newActivity.putExtra("mute_state", mute);
					    	startActivity (newActivity);
						}
					}
					else if (position == 1)
					{
						if (state == 1)
						{
							Intent newActivity = new Intent (getActivity(), Game.class);
					    	newActivity.putExtra("game_type", 2);
					    	newActivity.putExtra("mute_state", mute);
					    	startActivity (newActivity);
						}
				    	else if (state == 2)
						{
							Intent newActivity = new Intent (getActivity(), BTmenu.class);
							newActivity.putExtra("mute_state", mute);
					    	startActivity (newActivity);
						}
					}
				}
				
			});
		}
		else if (state == 3 || state == 4 || state == 5 || state == 6 || state == 7 || state == 8)
		{
			view = inflater.inflate(R.layout.dialog_game_conclusion, null);
			newGameButton = (ImageView) view.findViewById(R.id.newgame_button);
			newGameButton.setOnClickListener(this);
			dialogCommunicator = (DialogCommunicator) getActivity();
			ImageView conclusionImage = (ImageView) view.findViewById(R.id.conclusion_image);
			if (state == 3)
				conclusionImage.setImageResource(R.drawable.dialog_youwin);
			else if (state == 4)
				conclusionImage.setImageResource(R.drawable.dialog_youlose);
			else if (state == 5)
				conclusionImage.setImageResource(R.drawable.dialog_outoftime);
			else if (state == 6)
				conclusionImage.setImageResource(R.drawable.dialog_xwins);
			else if (state == 7)
				conclusionImage.setImageResource(R.drawable.dialog_owins);
			else if (state == 8)
				conclusionImage.setImageResource(R.drawable.dialog_tie);
			else
				Toast.makeText(getActivity(), "Error with Conclusion Dialog.", Toast.LENGTH_SHORT).show();
		}
		else if (state == 9)
		{
			view = inflater.inflate(R.layout.dialog_settings, null);
			dialogCommunicator = (DialogCommunicator) getActivity();
			TextView showAchievements = (TextView) view.findViewById(R.id.show_achievements);
			TextView showLeaderboards = (TextView) view.findViewById(R.id.show_leaderboards);
			TextView musicText = (TextView) view.findViewById(R.id.text);
			TextView license = (TextView) view.findViewById(R.id.license);
			TextView signOut = (TextView) view.findViewById(R.id.sign_out_button);
			Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "tr2n.ttf"); 
			showAchievements.setTypeface(face);
			showLeaderboards.setTypeface(face);
			musicText.setTypeface(face);
			license.setTypeface(face);
			signOut.setTypeface(face);
			showAchievements.setOnClickListener(this);
			showLeaderboards.setOnClickListener(this);
			musicText.setOnClickListener(this);
			license.setOnClickListener(this);
			signOut.setOnClickListener(this);
			
			RelativeLayout musicSettingView = (RelativeLayout) view.findViewById(R.id.music_setting);
			volume = (ImageView) musicSettingView.findViewById(R.id.volume);
			if (mute)
				volume.setImageResource(R.drawable.ic_action_volume_muted);
			else
				volume.setImageResource(R.drawable.ic_action_volume_on);
		}
		else if (state == 10)
		{
			view = inflater.inflate(R.layout.dialog_you_lose_bt, null);
			ImageView conclusionImage = (ImageView) view.findViewById(R.id.conclusion_image);
			conclusionImage.setImageResource(R.drawable.dialog_youlose);
		}
		else
			Toast.makeText(getActivity(), "Wrong Dialog State", Toast.LENGTH_SHORT).show();
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		Log.i("Debug", "Custom Dialog Created With State " + state);
		return dialog;
	}
	
	interface DialogCommunicator
	{
		public void newGame ();
		public void showAchievements();
		public void showLeaderboards();
		public void flipMusicState (boolean mute_state);
		public void signOutOfGoogle ();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.newgame_button:
				dialogCommunicator.newGame();	
				dismiss();
				break;
			case R.id.show_achievements:
				dialogCommunicator.showAchievements();
				break;
			case R.id.show_leaderboards:
				dialogCommunicator.showLeaderboards();
				break;
			case R.id.text:
				if (!mute)
				{
					volume.setImageResource(R.drawable.ic_action_volume_muted);
					mute = true;
				}
				else
				{
					volume.setImageResource(R.drawable.ic_action_volume_on);
					mute = false;
				}
				dialogCommunicator.flipMusicState(mute);
				break;
			case R.id.license:
				Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse("http://www.apache.org/licenses/LICENSE-2.0"));
				startActivity(browserIntent);
				break;
			case R.id.sign_out_button:
				dialogCommunicator.signOutOfGoogle();
				break;
		}
	}
	
}

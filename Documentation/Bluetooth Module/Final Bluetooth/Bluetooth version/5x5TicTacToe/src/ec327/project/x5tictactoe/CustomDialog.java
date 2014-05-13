package ec327.project.x5tictactoe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class CustomDialog extends DialogFragment {
	
	int state;
	
	@SuppressLint("ValidFragment")
	public CustomDialog (int i)
	{
		state = i;
	}
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		Dialog dialog = new Dialog (getActivity (), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = null;
		if (state == 1)
		{
			view = inflater.inflate(R.layout.dialog_choice_single_player, null);
			String[] values = new String[] {"Classic", "Race Against Time"};
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
			ListView list = (ListView) view.findViewById(R.id.list);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (position == 0)
					{
						Intent newActivity = new Intent (getActivity(), Game.class);
				    	newActivity.putExtra("mode", 1);
				    	startActivity (newActivity);
					}
					else if (position == 1)
					{
						Intent newActivity = new Intent (getActivity(), Game.class);
				    	newActivity.putExtra("mode", 2);
				    	startActivity (newActivity);
					}
				}
				
			});
		}
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		Log.i("Debug", "Conclusion Dialog Created.");
		return dialog;
	}
	
}

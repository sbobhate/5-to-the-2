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
 * Custom List Adapter
 	- Used by the game choice dialog 
 * 
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<Object> {

	private int layout_resource;
	private Context mContext;
	private String[] items;
	
	public CustomListAdapter(Context context, int resource,
			String[] values) {
		super(context, resource, values);
		layout_resource = resource;
		mContext = context;
		items = values;
	}

	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	     View v = convertView;
	     if (v == null) {
	         LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         v = vi.inflate(layout_resource, null);
	     }
	     TextView list_text = (TextView) v.findViewById(R.id.list_text);
	     list_text.setText(items[position]);
	     Typeface face=Typeface.createFromAsset(mContext.getAssets(), "tr2n.ttf");
	     list_text.setTypeface(face);
	     return v;
     }
	
}

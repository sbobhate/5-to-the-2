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
 * Image Adapter
 	- Set the initial images for the board gridview
 * 
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	
	public ImageAdapter (Context c)
	{
		mContext = c;
	}
	
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

	private Integer[] mThumbIds = {
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box, R.drawable.dark_box,
            R.drawable.dark_box
    };
	
	public int getElement(int pos) {
		return mThumbIds[pos];
	}
	
}

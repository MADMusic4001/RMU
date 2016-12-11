/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.character;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Character;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 11/27/2016.
 */
public class SelectCharacterAdapter extends ArrayAdapter<Character> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.award_xp_list_row;
	private LayoutInflater layoutInflater;
	private SelectCharacterAdapter.CharacterAdapterCallbacks callbacks;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	public SelectCharacterAdapter(Context context, CharacterAdapterCallbacks callbacks) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((CheckBox) rowView.findViewById(R.id.select_check_box),
									(TextView) rowView.findViewById(R.id.character_name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		Character character = getItem(position);
		holder.character = character;
		if(character != null ) {
			holder.nameView.setText(character.getFullName());
			holder.selectCheckBox.setChecked(callbacks.isSelected(character));
		}

		return rowView;
	}

	private class ViewHolder {
		private Character character;
		private CheckBox selectCheckBox;
		private TextView nameView;

		public ViewHolder(CheckBox selectCheckBox, TextView nameView) {
			this.selectCheckBox = selectCheckBox;
			this.nameView = nameView;

			initSelectCheckBox();
		}

		private void initSelectCheckBox() {
			selectCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					callbacks.setSelected(character, selectCheckBox.isChecked());
				}
			});
		}
	}

	public interface CharacterAdapterCallbacks {
		boolean isSelected(Character character);
		void setSelected(Character character, boolean selected);
	}
}

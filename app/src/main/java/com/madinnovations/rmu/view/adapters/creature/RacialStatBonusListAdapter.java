/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.creature;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.creature.RacialStatBonus;

/**
 * Populates a ListView with {@link RacialStatBonus} information
 */
public class RacialStatBonusListAdapter extends ArrayAdapter<RacialStatBonus> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.creature_variety_racial_stat_bonuses_list_row;
	private LayoutInflater layoutInflater;
	private SetRacialStatBonus setRacialStatBonusHandler;

	/**
	 * Creates a new RacialStatBonusListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public RacialStatBonusListAdapter(Context context, SetRacialStatBonus setRacialStatBonusHandler) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setRacialStatBonusHandler = setRacialStatBonusHandler;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView,@NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.stat_code_view),
									(EditText) rowView.findViewById(R.id.stat_bonus_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		RacialStatBonus racialStatBonus = getItem(position);
		if(racialStatBonus != null) {
			holder.currentInstance = racialStatBonus;
			holder.statNameView.setText(String.format(getContext().getString(R.string.code_name_format_string),
					racialStatBonus.getStat().getAbbreviation(), racialStatBonus.getStat().getName()));
			holder.bonusEdit.setText(String.valueOf(racialStatBonus.getBonus()));
		}

		return rowView;
	}

	private class ViewHolder {
		private RacialStatBonus currentInstance;
		private TextView statNameView;
		private EditText bonusEdit;

		ViewHolder(TextView statNameView, EditText bonusEdit) {
			this.statNameView = statNameView;
			this.bonusEdit = bonusEdit;
			initBonusEdit();
		}

		private void initBonusEdit() {
			bonusEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.length() == 0) {
						bonusEdit.setError(getContext().getString(R.string.validation_creature_variety_bonus_required));
					}
				}
			});
			bonusEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(bonusEdit.length() > 0) {
							try {
								short newBonus = Short.valueOf(bonusEdit.getText().toString());
								if (newBonus != currentInstance.getBonus()) {
									currentInstance.setBonus(newBonus);
									setRacialStatBonusHandler.setRacialStatBonus(currentInstance);
								}
								bonusEdit.setError(null);
							}
							catch (NumberFormatException ex) {
								bonusEdit.setError(getContext().getString(R.string.validation_creature_variety_bonus_required));
							}
						}
					}
				}
			});
		}
	}

	public interface SetRacialStatBonus {
		/**
		 * Sets a racial stat bonus
		 *
		 * @param racialStatBonus  a racial stat bonus instance
		 */
		void setRacialStatBonus(RacialStatBonus racialStatBonus);
	}
}

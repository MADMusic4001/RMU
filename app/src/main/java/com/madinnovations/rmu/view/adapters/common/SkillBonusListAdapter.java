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
package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.SkillBonus;

/**
 * Populates a ListView with {@link SkillBonus} information
 */
public class SkillBonusListAdapter extends ArrayAdapter<SkillBonus> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.creature_variety_skill_bonuses_list_row;
	private ListView listView = null;
	private LayoutInflater layoutInflater;
	private SetSkillBonus setSkillBonusHandler;

	/**
	 * Creates a new SkillBonusListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public SkillBonusListAdapter(@NonNull Context context, @NonNull SetSkillBonus setSkillBonusHandler,
								 @NonNull ListView listView) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setSkillBonusHandler = setSkillBonusHandler;
		this.listView = listView;
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.skill_name_view),
									(EditText) rowView.findViewById(R.id.skill_bonuses_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		SkillBonus skillBonus = getItem(position);
		holder.skillBonus = skillBonus;
		if(skillBonus != null) {
			if (skillBonus.getSkill() != null) {
				holder.skillNameView.setText(skillBonus.getSkill().getName());
			}
			else if (skillBonus.getSpecialization() != null) {
				holder.skillNameView.setText(skillBonus.getSpecialization().getName());
			}
			holder.bonusEdit.setText(String.valueOf(skillBonus.getBonus()));
		}
		return rowView;
	}

	private class ViewHolder {
		private SkillBonus skillBonus;
		private TextView   skillNameView;
		private EditText   bonusEdit;

		ViewHolder(TextView skillNameView, EditText bonusEdit) {
			this.skillNameView = skillNameView;
			initSkillNameView();
			this.bonusEdit = bonusEdit;
			initBonusEdit();
		}

		private void initSkillNameView() {
			skillNameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listView != null) {
						int position = getPosition(skillBonus);
						listView.setItemChecked(position, !listView.isItemChecked(position));
					}
				}
			});

			skillNameView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return listView.performLongClick();
				}
			});
		}

		private void initBonusEdit() {
			bonusEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listView != null) {
						int position = getPosition(skillBonus);
						listView.setItemChecked(position, !listView.isItemChecked(position));
					}
				}
				});

			bonusEdit.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					return listView.performLongClick();
			}
			});
			bonusEdit.setOnDragListener(new View.OnDragListener() {
				@Override
				public boolean onDrag(View v, DragEvent event) {
					return true;
				}
			});
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
					else {
						try {
							bonusEdit.setError(null);
						}
						catch (NumberFormatException ex) {
							bonusEdit.setError(getContext().getString(R.string.validation_creature_variety_bonus_required));
						}
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
								if (newBonus != skillBonus.getBonus()) {
									skillBonus.setBonus(newBonus);
									setSkillBonusHandler.setSkillBonus(skillBonus);
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

	public interface SetSkillBonus {
		/**
		 * Sets a skill bonus
		 *
		 * @param skillBonus  a SkillBonus instance
		 */
		void setSkillBonus(SkillBonus skillBonus);
	}
}

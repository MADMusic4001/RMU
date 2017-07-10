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
package com.madinnovations.rmu.view.adapters.combat;

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
import com.madinnovations.rmu.data.entities.combat.AttackBonus;

/**
 * Populates a ListView with {@link AttackBonus} information
 */
public class AttackBonusListAdapter extends ArrayAdapter<AttackBonus> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.creature_variety_attack_bonuses_list_row;
	private ListView listView = null;
	private LayoutInflater layoutInflater;
	private SetAttackBonus setAttackBonusHandler;

	/**
	 * Creates a new AttackBonusListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public AttackBonusListAdapter(@NonNull Context context, @NonNull SetAttackBonus setAttackBonusHandler,
								  @NonNull ListView listView) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setAttackBonusHandler = setAttackBonusHandler;
		this.listView = listView;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.attack_name_view),
									(EditText) rowView.findViewById(R.id.attack_bonuses_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		AttackBonus attackBonus = getItem(position);
		holder.currentInstance = attackBonus;
		if(attackBonus != null) {
			holder.attackNameView.setText(attackBonus.getAttack().getName());
			holder.bonusEdit.setText(String.valueOf(attackBonus.getBonus()));
		}

		return rowView;
	}

	private class ViewHolder {
		private AttackBonus currentInstance;
		private TextView attackNameView;
		private EditText bonusEdit;

		ViewHolder(TextView attackNameView, EditText bonusEdit) {
			this.attackNameView = attackNameView;
			initAttackNameView();
			this.bonusEdit = bonusEdit;
			initBonusEdit();
		}

		private void initAttackNameView() {
			attackNameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = getPosition(currentInstance);
					listView.setItemChecked(position, !listView.isItemChecked(position));
				}
			});

			attackNameView.setOnLongClickListener(new View.OnLongClickListener() {
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
						int position = getPosition(currentInstance);
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
								if (newBonus != currentInstance.getBonus()) {
									currentInstance.setBonus(newBonus);
									setAttackBonusHandler.setAttackBonus(currentInstance);
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

	public interface SetAttackBonus {
		/**
		 * Sets a attack bonus
		 *
		 * @param attackBonus  a AttackBonus instance
		 */
		void setAttackBonus(AttackBonus attackBonus);
	}
}

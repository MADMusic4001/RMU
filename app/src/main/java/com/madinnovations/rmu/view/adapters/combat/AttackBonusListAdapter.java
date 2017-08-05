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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
									(CheckBox) rowView.findViewById(R.id.primary_checkbox));
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
			holder.primaryCheckbox.setChecked(attackBonus.isPrimary());
		}

		return rowView;
	}

	private class ViewHolder {
		private AttackBonus currentInstance;
		private TextView    attackNameView;
		private CheckBox    primaryCheckbox;

		ViewHolder(TextView attackNameView, CheckBox primaryCheckbox) {
			this.attackNameView = attackNameView;
			initAttackNameView();
			this.primaryCheckbox = primaryCheckbox;
			initPrimaryCheckbox();
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

		private void initPrimaryCheckbox() {
			primaryCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(currentInstance.isPrimary() != isChecked) {
						currentInstance.setPrimary(isChecked);
						setAttackBonusHandler.setAttackBonus(currentInstance);
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

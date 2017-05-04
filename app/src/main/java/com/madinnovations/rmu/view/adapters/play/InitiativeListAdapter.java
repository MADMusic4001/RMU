/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.play;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.CreatureAttack;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.InitiativeListItem;

/**
 * Populates a ListView with {@link InitiativeListItem} information
 */
public class InitiativeListAdapter extends ArrayAdapter<InitiativeListItem> {
	private static final String TAG = "InitiativeListAdapter";
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_initiative_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new InitiativeListAdapter instance

	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public InitiativeListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		InitiativeListAdapter.ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new InitiativeListAdapter.ViewHolder((TextView) rowView.findViewById(R.id.combatant_name_view),
														  (EditText) rowView.findViewById(R.id.base_initiative_edit),
														  (TextView) rowView.findViewById(R.id.quickness_bonus_view),
														  (TextView) rowView.findViewById(R.id.other_penalties_view),
														  (TextView) rowView.findViewById(R.id.total_initiative_view),
														  (TextView) rowView.findViewById(R.id.offensive_bonus_view),
														  (EditText) rowView.findViewById(R.id.parry_edit),
														  (TextView) rowView.findViewById(R.id.defensive_bonus_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (InitiativeListAdapter.ViewHolder) convertView.getTag();
		}

		InitiativeListItem initiativeListItem = getItem(position);
		holder.initiativeListItem = initiativeListItem;
		if(initiativeListItem != null) {
			Short shortValue;
			if(initiativeListItem.getCharacter() != null) {
				holder.nameView.setText(initiativeListItem.getCharacter().getKnownAs());
				holder.initiativeRollEdit.setEnabled(true);
				shortValue = initiativeListItem.getCharacter().getTotalStatBonus(Statistic.QUICKNESS);
				holder.quicknessBonusView.setText(String.valueOf(shortValue));
				holder.otherPenaltiesView.setText(String.valueOf(initiativeListItem.getCharacter().getInitiativePenalty()));
				holder.offensiveBonusView.setText(String.valueOf(initiativeListItem.getCharacter().getOffensiveBonus()));
			}
			else if(initiativeListItem.getCreature() != null) {
				Creature creature = initiativeListItem.getCreature();
				holder.nameView.setText(creature.getCreatureVariety().getName());
				holder.initiativeRollEdit.setEnabled(false);
				shortValue = creature.getCreatureVariety().getRacialStatBonuses()
						.get(Statistic.QUICKNESS);
				if(shortValue != null) {
					holder.quicknessBonusView.setText(String.valueOf(shortValue));
				}
				holder.otherPenaltiesView.setText(String.valueOf(creature.getInitiativePenalty()));
				CreatureAttack nextAttack = creature.getCreatureVariety().getNextAttack(creature.getLastAttack(),
																						creature.getNumCreatures());
				if(nextAttack != null) {
					short bonus = creature.getCreatureVariety().getOffensiveBonus(nextAttack.getBaseAttack());
					holder.offensiveBonusView.setText(String.valueOf(bonus));
				}
				else {
					holder.offensiveBonusView.setText(null);
				}
			}
			holder.initiativeRollEdit.setText(String.valueOf(initiativeListItem.getCombatRoundInfo().getInitiativeRoll()));

			holder.baseInitiativeView.setText(String.valueOf(initiativeListItem.getCombatRoundInfo().getBaseInitiative()));
//			holder.parryEdit.setText(String.valueOf(initiativeListItem.getCombatRoundInfo().getParry()));
//			holder.defensiveBonusView.setText(String.valueOf(initiativeListItem.getCombatRoundInfo().getDefensiveBonus()));
		}
		return rowView;
	}

	private class ViewHolder {
		public InitiativeListItem initiativeListItem;
		public TextView           nameView;
		public EditText           initiativeRollEdit;
		public TextView           quicknessBonusView;
		public TextView           otherPenaltiesView;
		public TextView           baseInitiativeView;
		public TextView           offensiveBonusView;
		public EditText           parryEdit;
		public TextView           defensiveBonusView;

		public ViewHolder(TextView nameView, EditText initiativeRollEdit, TextView quicknessBonusView,
						  TextView otherPenaltiesView, TextView baseInitiativeView, TextView offensiveBonusView,
						  EditText parryEdit, TextView defensiveBonusView) {
			this.nameView = nameView;
			this.initiativeRollEdit = initiativeRollEdit;
			this.quicknessBonusView = quicknessBonusView;
			this.otherPenaltiesView = otherPenaltiesView;
			this.baseInitiativeView = baseInitiativeView;
			this.offensiveBonusView = offensiveBonusView;
			this.parryEdit = parryEdit;
			this.defensiveBonusView = defensiveBonusView;
			initInitiativeRollEdit();
		}

		private void initInitiativeRollEdit() {
			initiativeRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						if(initiativeRollEdit.getText().length() > 0) {
							short newValue = Short.valueOf(initiativeRollEdit.getText().toString());
							short oldValue = initiativeListItem.getCombatRoundInfo().getInitiativeRoll();

							if (newValue != oldValue) {
								short newTotal = (short)(initiativeListItem.getCombatRoundInfo().getBaseInitiative() - oldValue
										+ newValue);
								initiativeListItem.getCombatRoundInfo().setInitiativeRoll(newValue);
								initiativeListItem.getCombatRoundInfo().setBaseInitiative(newTotal);
								baseInitiativeView.setText(String.valueOf(newTotal));
							}
						}
					}
				}
			});
		}
	}
}

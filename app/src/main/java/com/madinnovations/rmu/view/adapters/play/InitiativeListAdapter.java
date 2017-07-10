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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackResult;
import com.madinnovations.rmu.data.entities.combat.CreatureAttack;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.data.entities.play.InitiativeListItem;

/**
 * Populates a ListView with {@link InitiativeListItem} information
 */
public class InitiativeListAdapter extends ArrayAdapter<InitiativeListItem> {
	@SuppressWarnings("unused")
	private static final String TAG                = "InitiativeListAdapter";
	private static final int    LAYOUT_RESOURCE_ID = R.layout.initiative_list_row;
	private LayoutInflater layoutInflater;
	private EncounterSetup encounterSetup;

	/**
	 * Creates a new InitiativeListAdapter instance

	 * @param context the view {@link Context} the adapter will be attached to
	 * @param encounterSetup  the EncounterSetup instance for this encounter
	 */
	public InitiativeListAdapter(Context context, @NonNull EncounterSetup encounterSetup) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.encounterSetup = encounterSetup;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		InitiativeListAdapter.ViewHolder holder;

		InitiativeListItem initiativeListItem = getItem(position);

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new InitiativeListAdapter.ViewHolder(initiativeListItem,
														  (TextView) rowView.findViewById(R.id.combatant_name_view),
														  (EditText) rowView.findViewById(R.id.base_initiative_edit),
														  (TextView) rowView.findViewById(R.id.quickness_bonus_view),
														  (TextView) rowView.findViewById(R.id.other_penalties_view),
														  (TextView) rowView.findViewById(R.id.total_initiative_view),
														  (Spinner)  rowView.findViewById(R.id.attacks_spinner),
														  (Spinner)  rowView.findViewById(R.id.opponents_spinner),
														  (TextView) rowView.findViewById(R.id.offensive_bonus_view),
														  (EditText) rowView.findViewById(R.id.parry_edit),
														  (TextView) rowView.findViewById(R.id.defensive_bonus_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (InitiativeListAdapter.ViewHolder) convertView.getTag();
		}

		holder.initiativeListItem = initiativeListItem;
		holder.copyItemToView();

		return rowView;
	}

	private class ViewHolder {
		public InitiativeListItem   initiativeListItem;
		public TextView             nameView;
		public EditText             initiativeRollEdit;
		public TextView             quicknessBonusView;
		public TextView             otherPenaltiesView;
		public TextView             baseInitiativeView;
		public Spinner              attacksSpinner;
		public Spinner              opponentsSpinner;
		public TextView             offensiveBonusView;
		public EditText             parryEdit;
		public ArrayAdapter<Object> attacksSpinnerAdapter;
		public ArrayAdapter<Being>  opponentsSpinnerAdapter;
		public TextView             defensiveBonusView;

		public ViewHolder(InitiativeListItem initiativeListItem, TextView nameView, EditText initiativeRollEdit,
						  TextView quicknessBonusView, TextView otherPenaltiesView, TextView baseInitiativeView,
						  Spinner attacksSpinner, Spinner opponentsSpinner, TextView offensiveBonusView, EditText parryEdit,
						  TextView defensiveBonusView) {
			this.initiativeListItem = initiativeListItem;
			this.nameView = nameView;
			this.initiativeRollEdit = initiativeRollEdit;
			this.quicknessBonusView = quicknessBonusView;
			this.otherPenaltiesView = otherPenaltiesView;
			this.baseInitiativeView = baseInitiativeView;
			this.attacksSpinner      = attacksSpinner;
			this.opponentsSpinner   = opponentsSpinner;
			this.offensiveBonusView = offensiveBonusView;
			this.parryEdit          = parryEdit;
			this.defensiveBonusView = defensiveBonusView;
			initAttacksSpinner();
			initOpponentsSpinner();
			initInitiativeRollEdit();
		}

		private void initInitiativeRollEdit() {
			initiativeRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						if(initiativeRollEdit.getText().length() > 0) {
							short newValue = Short.valueOf(initiativeRollEdit.getText().toString());
							short oldValue = initiativeListItem.getEncounterRoundInfo().getInitiativeRoll();

							if (newValue != oldValue) {
								short newTotal = (short)(initiativeListItem.getEncounterRoundInfo().getBaseInitiative() - oldValue
										+ newValue);
								initiativeListItem.getEncounterRoundInfo().setInitiativeRoll(newValue);
								initiativeListItem.getEncounterRoundInfo().setBaseInitiative(newTotal);
								baseInitiativeView.setText(String.valueOf(newTotal));
							}
						}
					}
				}
			});
		}

		private void initAttacksSpinner() {
			attacksSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			attacksSpinner.setAdapter(attacksSpinnerAdapter);
			copyItemToAttackSpinner(initiativeListItem.getEncounterRoundInfo());

			if(initiativeListItem.getCharacter() != null) {
				Character character = initiativeListItem.getCharacter();
				if(character.getMainHandItem() instanceof Weapon) {
					attacksSpinnerAdapter.add(((WeaponTemplate)character.getMainHandItem().getItemTemplate()).getAttack());
				}
			}
		}

		private void initOpponentsSpinner() {
			opponentsSpinnerAdapter = new ArrayAdapter<Being>(getContext(), R.layout.single_field_row);
			opponentsSpinner.setAdapter(opponentsSpinnerAdapter);

			opponentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					Being newBeing = opponentsSpinnerAdapter.getItem(position);
					if(initiativeListItem.getCharacter() != null) {
						Character character = initiativeListItem.getCharacter();
						EncounterRoundInfo characterEncounterRoundInfo = encounterSetup.getCharacterCombatInfo().get(character);
						EncounterRoundInfo opponentInfo = encounterSetup.getEnemyCombatInfo().get(newBeing);
						short offensiveBonus = characterEncounterRoundInfo.getOffensiveBonus(
								encounterSetup.getEnemyCombatInfo().get(newBeing), character.getWeapon(), null);
						offensiveBonusView.setText((String.valueOf(offensiveBonus)));
						parryEdit.setText("0");
						defensiveBonusView.setText(String.valueOf(
								characterEncounterRoundInfo.getDefensiveBonus(opponentInfo)));
					}
					else {

					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void copyItemToView() {
			EncounterRoundInfo encounterRoundInfo = initiativeListItem.getEncounterRoundInfo();
			Being combatant = encounterRoundInfo.getCombatant();
			if(combatant instanceof Character) {
				nameView.setText(((Character)combatant).getKnownAs());
				opponentsSpinnerAdapter.clear();
				opponentsSpinnerAdapter.addAll(encounterSetup.getEnemyCombatInfo().keySet());
				opponentsSpinnerAdapter.notifyDataSetChanged();
			}
			else {
				nameView.setText(combatant.toString());
				opponentsSpinnerAdapter.clear();
				opponentsSpinnerAdapter.addAll(encounterSetup.getCharacterCombatInfo().keySet());
				opponentsSpinnerAdapter.notifyDataSetChanged();
			}
			initiativeRollEdit.setEnabled(true);
			short quicknessBonus = combatant.getTotalStatBonus(Statistic.QUICKNESS);
			quicknessBonusView.setText(String.valueOf(quicknessBonus));
			short otherPenalties = combatant.getInitiativeModifications();
			otherPenaltiesView.setText(String.valueOf(otherPenalties));
			encounterRoundInfo.setBaseInitiative((short)(encounterRoundInfo.getInitiativeRoll() + otherPenalties));
			baseInitiativeView.setText(String.valueOf(encounterRoundInfo.getBaseInitiative()));
			EncounterRoundInfo opponentEncounterRoundInfo = getSelectedOpponentEncounterRoundInfo(encounterRoundInfo);
			copyItemToAttackSpinner(encounterRoundInfo);
			Object attackObject = attacksSpinner.getSelectedItem();
			Weapon weapon = null;
			Attack attack = null;
			if(attackObject instanceof Weapon) {
				weapon = (Weapon)attackObject;
			}
			else {
				attack = (Attack)attackObject;
			}
			short offensiveBonus = encounterRoundInfo.getOffensiveBonus(opponentEncounterRoundInfo, weapon, attack);
			offensiveBonusView.setText(String.valueOf(offensiveBonus));
			parryEdit.setText(String.valueOf(encounterRoundInfo.getParry()));
			defensiveBonusView.setText(String.valueOf(encounterRoundInfo.getDefensiveBonus(opponentEncounterRoundInfo)));
		}

		private EncounterRoundInfo getSelectedOpponentEncounterRoundInfo(EncounterRoundInfo encounterRoundInfo) {
			Being opponent;
			if(encounterRoundInfo.getSelectedOpponent() != null) {
				opponent = encounterRoundInfo.getSelectedOpponent();
				int position = opponentsSpinnerAdapter.getPosition(encounterRoundInfo.getSelectedOpponent());
				if(position == -1) {
					opponentsSpinnerAdapter.add(opponent);
					opponentsSpinnerAdapter.notifyDataSetChanged();
					position = opponentsSpinnerAdapter.getPosition(encounterRoundInfo.getSelectedOpponent());
				}
				opponentsSpinner.setSelection(position);
			}
			else {
				opponent = (Being)opponentsSpinner.getSelectedItem();
				if(opponent == null && opponentsSpinnerAdapter.getCount() > 0) {
					opponent = (Being) opponentsSpinnerAdapter.getItem(0);
				}
			}
			EncounterRoundInfo opponentEncounterRoundInfo;
			if(opponent instanceof Character) {
				opponentEncounterRoundInfo = encounterSetup.getCharacterCombatInfo().get(opponent);
			}
			else {
				opponentEncounterRoundInfo = encounterSetup.getEnemyCombatInfo().get((Creature)opponent);
			}
			return opponentEncounterRoundInfo;
		}

		private void copyItemToAttackSpinner(EncounterRoundInfo encounterRoundInfo) {
			if(attacksSpinnerAdapter == null) {
				initAttacksSpinner();
			}

			attacksSpinnerAdapter.clear();
			if(encounterRoundInfo.getCombatant() instanceof Character) {
				Character character = (Character)encounterRoundInfo.getCombatant();
				if(character.getMainHandItem() instanceof Weapon) {
					attacksSpinnerAdapter.add(character.getMainHandItem());
				}
				if(character.getOffhandItem() instanceof Weapon) {
					attacksSpinnerAdapter.add(character.getOffhandItem());
				}
			}
			else {
				Creature creature = (Creature)encounterRoundInfo.getCombatant();
				if(creature.getMainHandItem() instanceof Weapon) {
					attacksSpinnerAdapter.add(creature.getMainHandItem());
				}
				if(creature.getOffhandItem() instanceof Weapon) {
					attacksSpinnerAdapter.add(creature.getOffhandItem());
				}
				for(CreatureAttack creatureAttack : creature.getCreatureVariety().getAttackList()) {
					if(encounterRoundInfo.getAttackResult() != null) {
						AttackResult attackResult = encounterRoundInfo.getAttackResult();
						if(!creatureAttack.getBaseAttack().equals(attackResult.getAttack())) {
							if(creatureAttack.isCriticalFollowUp() && attackResult.getCriticalResult() != null &&
									creatureAttack.isSameRoundFollowUp()) {
								attacksSpinnerAdapter.add(creatureAttack.getBaseAttack());
							}
							else if(!creatureAttack.isCriticalFollowUp()) {
								attacksSpinnerAdapter.add(creatureAttack.getBaseAttack());
							}
						}
						else {
							attacksSpinnerAdapter.add(creatureAttack.getBaseAttack());
						}
					}
					else {
						if(!creatureAttack.isCriticalFollowUp() && creatureAttack.getBaseAttack() != null) {
							Log.d(TAG, "copyItemToAttackSpinner: adding creatureAttack.getBaseAttack()4: " +
									creatureAttack.getBaseAttack());
							attacksSpinnerAdapter.add(creatureAttack.getBaseAttack());
						}
					}
				}
			}
			attacksSpinnerAdapter.notifyDataSetChanged();
		}
	}
}

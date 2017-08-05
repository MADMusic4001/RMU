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
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;

import rx.Subscriber;

/**
 * Populates a ListView with {@link EncounterRoundInfo} information
 */
public class InitiativeListAdapter extends ArrayAdapter<EncounterRoundInfo> {
	@SuppressWarnings("unused")
	private static final String TAG                = "InitiativeListAdapter";
	private static final int    LAYOUT_RESOURCE_ID = R.layout.initiative_list_row;
	private SpecializationRxHandler specializationRxHandler;
	private LayoutInflater          layoutInflater;
	private EncounterSetup          encounterSetup;

	/**
	 * Creates a new InitiativeListAdapter instance

	 * @param context the view {@link Context} the adapter will be attached to
	 * @param encounterSetup  the EncounterSetup instance for this encounter
	 */
	public InitiativeListAdapter(Context context, @NonNull EncounterSetup encounterSetup,
								 SpecializationRxHandler specializationRxHandler) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.specializationRxHandler = specializationRxHandler;
		this.encounterSetup = encounterSetup;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		InitiativeListAdapter.ViewHolder holder;

		EncounterRoundInfo encounterRoundInfo = getItem(position);

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new InitiativeListAdapter.ViewHolder(encounterRoundInfo,
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

		holder.encounterRoundInfo = encounterRoundInfo;
		holder.copyItemToView();

		return rowView;
	}

	private class ViewHolder {
		EncounterRoundInfo   encounterRoundInfo;
		TextView             nameView;
		EditText             initiativeRollEdit;
		TextView             quicknessBonusView;
		TextView             otherPenaltiesView;
		TextView             baseInitiativeView;
		Spinner              attacksSpinner;
		Spinner              opponentsSpinner;
		TextView             offensiveBonusView;
		EditText             parryEdit;
		ArrayAdapter<Object> attacksSpinnerAdapter;
		ArrayAdapter<Being>  opponentsSpinnerAdapter;
		TextView             defensiveBonusView;

		public ViewHolder(EncounterRoundInfo encounterRoundInfo, TextView nameView, EditText initiativeRollEdit,
						  TextView quicknessBonusView, TextView otherPenaltiesView, TextView baseInitiativeView,
						  Spinner attacksSpinner, Spinner opponentsSpinner, TextView offensiveBonusView, EditText parryEdit,
						  TextView defensiveBonusView) {
			this.encounterRoundInfo = encounterRoundInfo;
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
							short oldValue = encounterRoundInfo.getInitiativeRoll();

							if (newValue != oldValue) {
								short newTotal = (short)(encounterRoundInfo.getBaseInitiative() - oldValue
										+ newValue);
								encounterRoundInfo.setInitiativeRoll(newValue);
								encounterRoundInfo.setBaseInitiative(newTotal);
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
			copyItemToAttackSpinner(encounterRoundInfo);

			attacksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					encounterRoundInfo.setSelectedAttack(attacksSpinnerAdapter.getItem(position));
					offensiveBonusView.setText(String.valueOf(getOffensiveBonus()));
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initOpponentsSpinner() {
			opponentsSpinnerAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			opponentsSpinner.setAdapter(opponentsSpinnerAdapter);

			opponentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					Being opponent = (Being)opponentsSpinner.getSelectedItem();
					//noinspection SuspiciousMethodCalls
					EncounterRoundInfo opponentInfo = encounterSetup.getEnemyCombatInfo().get(opponent);
					if(opponentInfo == null) {
						//noinspection SuspiciousMethodCalls
						opponentInfo = encounterSetup.getCharacterCombatInfo().get(opponent);
					}
					offensiveBonusView.setText(String.valueOf(getOffensiveBonus()));
					parryEdit.setText("0");
					defensiveBonusView.setText(String.valueOf(encounterRoundInfo.getDefensiveBonus(opponentInfo)));
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void copyItemToView() {
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
			initiativeRollEdit.setText(String.valueOf(encounterRoundInfo.getInitiativeRoll()));
			quicknessBonusView.setText(String.valueOf(combatant.getTotalStatBonus(Statistic.QUICKNESS)));
			otherPenaltiesView.setText(String.valueOf(combatant.getInitiativeModifications()));
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
			short offensiveBonus = encounterRoundInfo.getOffensiveBonus(opponentEncounterRoundInfo, weapon, attack, null);
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
				if(opponent == null && opponentsSpinnerAdapter != null && opponentsSpinnerAdapter.getCount() > 0) {
					opponent = opponentsSpinnerAdapter.getItem(0);
				}
			}
			EncounterRoundInfo opponentEncounterRoundInfo;
			if(opponent instanceof Character) {
				opponentEncounterRoundInfo = encounterSetup.getCharacterCombatInfo().get(opponent);
			}
			else {
				//noinspection SuspiciousMethodCalls
				opponentEncounterRoundInfo = encounterSetup.getEnemyCombatInfo().get(opponent);
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
				if(attacksSpinnerAdapter.getCount() == 0) {
					addStrikes();
					addSweeps();
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
				EncounterRoundInfo opponentEncounterRoundInfo = getSelectedOpponentEncounterRoundInfo(encounterRoundInfo);
				Attack nextCreatureAttack = creature.getNextAttack(encounterRoundInfo.getAttackResult(), opponentEncounterRoundInfo,
						false);
				if(nextCreatureAttack != null) {
					attacksSpinnerAdapter.add(nextCreatureAttack);
				}
			}
			attacksSpinnerAdapter.notifyDataSetChanged();
			if(encounterRoundInfo.getSelectedAttack() != null) {
				attacksSpinner.setSelection(attacksSpinnerAdapter.getPosition(encounterRoundInfo.getSelectedAttack()));
			}
			else {
				encounterRoundInfo.setSelectedAttack(attacksSpinner.getSelectedItem());
			}
		}

		private void addStrikes() {
			specializationRxHandler.getByName("Strikes").subscribe(new Subscriber<Specialization>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "onError: Exception caught getting Strikes specialization", e);
				}
				@Override
				public void onNext(Specialization specialization) {
					if(attacksSpinnerAdapter.getPosition(specialization) == -1) {
						attacksSpinnerAdapter.add(specialization);
						attacksSpinnerAdapter.notifyDataSetChanged();
					}
				}
			});
		}

		private void addSweeps() {
			specializationRxHandler.getByName("Sweeps & Throws").subscribe(new Subscriber<Specialization>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "onError: Exception caught getting Sweeps & Throws specialization", e);
				}
				@Override
				public void onNext(Specialization specialization) {
					if(attacksSpinnerAdapter.getPosition(specialization) == -1) {
						attacksSpinnerAdapter.add(specialization);
						attacksSpinnerAdapter.notifyDataSetChanged();
					}
				}
			});
		}

		private short getOffensiveBonus() {
			Being opponent = (Being)opponentsSpinner.getSelectedItem();
			//noinspection SuspiciousMethodCalls
			EncounterRoundInfo opponentInfo = encounterSetup.getEnemyCombatInfo().get(opponent);
			if(opponentInfo == null) {
				//noinspection SuspiciousMethodCalls
				opponentInfo = encounterSetup.getCharacterCombatInfo().get(opponent);
			}
			Weapon weapon = null;
			Attack attack = null;
			Specialization specialization = null;
			Object selection = attacksSpinner.getSelectedItem();
			if(selection instanceof Specialization) {
				specialization = (Specialization)selection;
			}
			if(selection instanceof Weapon) {
				weapon = (Weapon)selection;
			}
			if(selection instanceof  Attack) {
				attack = (Attack)selection;
			}

			return encounterRoundInfo.getOffensiveBonus(opponentInfo, weapon, attack, specialization);
		}
	}
}

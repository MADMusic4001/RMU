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
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.Resistance;
import com.madinnovations.rmu.data.entities.common.Effect;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Stat;

import static android.view.View.GONE;

/**
 * Fragment class for handling Effect UI. Used by Talents
 */
public class EffectFragment extends Fragment {
	private LinearLayout                 effectsList;
	private Effect     currentEffect     = Effect.BONUS;
	private short      effectValue       = 0;
	private Resistance currentResistance = Resistance.CHANNELING;
	private short      resistBonus       = 0;
	private Skill      skill;
	private short      skillBonus = 0;
	private Stat       stat;
	private short      statBonus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null) {
			effectsList = (LinearLayout)savedInstanceState.get("layout");
		}
	}

	private void addEffectRow(LayoutInflater inflater, final int index) {
		View effectRow = inflater.inflate(R.layout.talent_parameter_row, effectsList, false);
		initEffectSpinner(effectRow, index);
		initEffectEdit(effectRow, index);
		setResistancesVisibility(effectRow, GONE);
		setSkillsVisibility(effectRow, GONE);
		setStatsVisibility(effectRow, GONE);
		effectsList.addView(effectRow);
	}

	private void initEffectSpinner(View parent, final int index) {
		final Spinner spinner = (Spinner)parent.findViewById(R.id.parameter_spinner);
		final ArrayAdapter<Effect> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		adapter.clear();
		adapter.addAll(Effect.values());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Effect effect = adapter.getItem(position);
				if(effect != null) {
					if(!effect.equals(currentEffect)) {
						switch (effect) {
							case MAGICAL_RR:
								final Spinner resistanceSpinner = (Spinner)parent.findViewById(R.id.resistance_spinner);
								resistanceSpinner.setVisibility(View.VISIBLE);
								final ArrayAdapter<Resistance> resistanceAdapter = new ArrayAdapter<>(
										getActivity(), R.layout.spinner_row);
								resistanceSpinner.setAdapter(resistanceAdapter);
								resistanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
									@Override
									public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
										Resistance resistance = resistanceAdapter.getItem(
												resistanceSpinner.getSelectedItemPosition());
										if(resistance != EffectFragment.this.currentResistance) {
											currentResistance = resistance;
											saveItem();
										}
									}
									@Override
									public void onNothingSelected(AdapterView<?> parent) {}
								});
								enableResistances(effectsList, index);
								setValuesVisibility(effectsList, View.GONE);
								setSkillsVisibility(effectsList, View.GONE);
								setStatsVisibility(effectsList, View.GONE);
								break;
							case STAT_BONUS:
								enableStats(effectsList, index);
								break;
							case SKILL_BONUS:
								enableSkills(effectsList, index);
								break;
						}
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initEffectEdit(View parent, final int index) {
		final EditText editText = (EditText)parent.findViewById(R.id.value_edit);
		editText.setText(String.valueOf(currentEffect));

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != effectValue) {
						effectValue = newShort;
						saveItem();
					}
				}
			}
		});
	}

	private void enableResistances(View parent, final int index) {
		setValuesVisibility(parent, GONE);
		setSkillsVisibility(parent, GONE);
		setStatsVisibility(parent, GONE);

		parent.findViewById(R.id.resistance_label).setVisibility(View.VISIBLE);
		final Spinner spinner = (Spinner)parent.findViewById(R.id.resistance_spinner);
		spinner.setVisibility(View.VISIBLE);
		final ArrayAdapter<Resistance> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		adapter.clear();
		adapter.addAll(Resistance.values());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(currentResistance));
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Resistance resistance = adapter.getItem(position);
				if(resistance != null) {
					if(!resistance.equals(currentResistance)) {
						currentResistance = resistance;
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		parent.findViewById(R.id.resistance_value_label).setVisibility(View.VISIBLE);
		final EditText editText = (EditText)parent.findViewById(R.id.resistance_value_edit);
		editText.setVisibility(View.VISIBLE);
		editText.setText(String.valueOf(resistBonus));
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != resistBonus) {
						resistBonus = newShort;
						saveItem();
					}
				}
			}
		});
	}

	private void enableSkills(View parent, int index) {
		setValuesVisibility(parent, GONE);
		setResistancesVisibility(parent, GONE);
		setStatsVisibility(parent, GONE);
	}

	private void enableStats(View parent, int index) {
		setValuesVisibility(parent, GONE);
		setResistancesVisibility(parent, GONE);
		setSkillsVisibility(parent, GONE);
	}

	private void setValuesVisibility(View parent, int visibility) {
		parent.findViewById(R.id.value_label).setVisibility(visibility);
		parent.findViewById(R.id.value_edit).setVisibility(visibility);
	}

	private void setSkillsVisibility(View parent, int visibility) {
		parent.findViewById(R.id.skill_label).setVisibility(visibility);
		parent.findViewById(R.id.skill_spinner).setVisibility(visibility);
		parent.findViewById(R.id.skill_bonus_label).setVisibility(visibility);
		parent.findViewById(R.id.skill_bonus_edit).setVisibility(visibility);
	}

	private void setResistancesVisibility(View parent, int visibility) {
		parent.findViewById(R.id.resistance_label).setVisibility(visibility);
		parent.findViewById(R.id.resistance_spinner).setVisibility(visibility);
		parent.findViewById(R.id.resistance_value_label).setVisibility(visibility);
		parent.findViewById(R.id.resistance_value_edit).setVisibility(visibility);
	}

	private void setStatsVisibility(View parent, int visibility) {
		parent.findViewById(R.id.stat_label).setVisibility(visibility);
		parent.findViewById(R.id.stat_spinner).setVisibility(visibility);
		parent.findViewById(R.id.stat_bonus_label).setVisibility(visibility);
		parent.findViewById(R.id.stat_bonus_edit).setVisibility(visibility);
	}

	private void geAffectedResistance() {

	}

	private void saveItem() {

	}

	public void setEffectsList(LinearLayout effectsList) {
		this.effectsList = effectsList;
	}
}

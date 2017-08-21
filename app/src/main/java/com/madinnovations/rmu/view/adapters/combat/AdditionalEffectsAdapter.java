/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.view.adapters.combat;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;
import com.madinnovations.rmu.data.entities.combat.CriticalSeverity;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.Effect;
import com.madinnovations.rmu.data.entities.combat.TargetType;
import com.madinnovations.rmu.data.entities.common.Dice;
import com.madinnovations.rmu.data.entities.common.TimeUnit;

/**
 * Adapter for displaying additional critical results
 */
@SuppressWarnings("unused")
public class AdditionalEffectsAdapter extends ArrayAdapter<AdditionalEffect> {
	private static final String TAG = "AdditionalCriticalResul";
	private static final int LAYOUT_RESOURCE_ID = R.layout.additional_effects_row;
	private AdditionalEffect additionalEffect;
	private LayoutInflater   layoutInflater;

	public AdditionalEffectsAdapter(@NonNull Context context,
									@LayoutRes int resource) {
		super(context, resource);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((Spinner) rowView.findViewById(R.id.applies_to_spinner),
									(LinearLayout) rowView.findViewById(R.id.effect_layout),
									(Spinner) rowView.findViewById(R.id.effect_spinner),
									(TextInputLayout) rowView.findViewById(R.id.value1_edit_label),
									(EditText) rowView.findViewById(R.id.value1_edit),
									(TextInputLayout) rowView.findViewById(R.id.value2_edit_label),
									(EditText) rowView.findViewById(R.id.value2_edit),
									(TextInputLayout) rowView.findViewById(R.id.value3_edit_label),
									(EditText) rowView.findViewById(R.id.value3_edit),
									(TextInputLayout) rowView.findViewById(R.id.value4_edit_label),
									(EditText) rowView.findViewById(R.id.value4_edit),
									(LinearLayout) rowView.findViewById(R.id.units_layout),
									(Spinner) rowView.findViewById(R.id.units_spinner),
									(LinearLayout) rowView.findViewById(R.id.dice_layout),
									(Spinner) rowView.findViewById(R.id.dice_spinner),
									(LinearLayout) rowView.findViewById(R.id.critical_severity_layout),
									(Spinner) rowView.findViewById(R.id.critical_severity_spinner),
									(LinearLayout) rowView.findViewById(R.id.critical_type_layout),
									(Spinner) rowView.findViewById(R.id.critical_type_spinner));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		AdditionalEffect additionalEffect = getItem(position);
		holder.currentInstance = additionalEffect;

		if(additionalEffect != null) {
			holder.copyItemToViews();
		}
		return rowView;
	}

	private class ViewHolder {
		AdditionalEffect               currentInstance;
		Spinner                        targetSpinner;
		ArrayAdapter<TargetType>       targetTypeArrayAdapter;
		LinearLayout                   effectLayout;
		Spinner                        effectSpinner;
		ArrayAdapter<Effect>           effectArrayAdapter;
		TextInputLayout                value1Layout;
		EditText                       value1Edit;
		TextInputLayout                value2Layout;
		EditText                       value2Edit;
		TextInputLayout                value3Layout;
		EditText                       value3Edit;
		TextInputLayout                value4Layout;
		EditText                       value4Edit;
		LinearLayout                   unitsLayout;
		Spinner                        unitsSpinner;
		ArrayAdapter<TimeUnit>         unitTypeArrayAdapter;
		LinearLayout                   diceLayout;
		Spinner                        diceSpinner;
		ArrayAdapter<Dice>             diceTypeArrayAdapter;
		LinearLayout                   criticalSeverityLayout;
		Spinner                        criticalSeveritySpinner;
		ArrayAdapter<CriticalSeverity> criticalSeverityArrayAdapter;
		LinearLayout                   criticalTypeLayout;
		Spinner                        criticalTypeSpinner;
		ArrayAdapter<CriticalType>     criticalTypeArrayAdapter;

		public ViewHolder(Spinner targetSpinner, LinearLayout effectLayout, Spinner effectSpinner, TextInputLayout value1Layout,
						  EditText value1Edit, TextInputLayout value2Layout, EditText value2Edit, TextInputLayout value3Layout,
						  EditText value3Edit, TextInputLayout value4Layout, EditText value4Edit, LinearLayout unitsLayout,
						  Spinner unitsSpinner, LinearLayout diceLayout, Spinner diceSpinner, LinearLayout criticalSeverityLayout,
						  Spinner criticalSeveritySpinner, LinearLayout criticalTypeLayout, Spinner criticalTypeSpinner) {
			this.targetSpinner = targetSpinner;
			this.effectLayout = effectLayout;
			this.effectSpinner = effectSpinner;
			this.value1Layout = value1Layout;
			this.value1Edit = value1Edit;
			this.value2Layout = value2Layout;
			this.value2Edit = value2Edit;
			this.value3Layout = value3Layout;
			this.value3Edit = value3Edit;
			this.value4Layout = value4Layout;
			this.value4Edit = value4Edit;
			this.unitsLayout = unitsLayout;
			this.unitsSpinner = unitsSpinner;
			this.diceLayout = diceLayout;
			this.diceSpinner = diceSpinner;
			this.criticalSeverityLayout = criticalSeverityLayout;
			this.criticalSeveritySpinner = criticalSeveritySpinner;
			this.criticalTypeLayout = criticalTypeLayout;
			this.criticalTypeSpinner = criticalTypeSpinner;

			initTargetSpinner();
			initEffectSpinner();
			initUnitSpinner();
			initDiceSpinner();
			initCriticalSeveritySpinner();
			initCriticalTypeSpinner();
		}

		private void initTargetSpinner() {
			targetTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			targetSpinner.setAdapter(targetTypeArrayAdapter);
			targetTypeArrayAdapter.clear();
			targetTypeArrayAdapter.addAll(TargetType.values());
			targetTypeArrayAdapter.notifyDataSetChanged();
		}

		private void initEffectSpinner() {
			effectArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			effectSpinner.setAdapter(effectArrayAdapter);
			effectArrayAdapter.clear();
			effectArrayAdapter.addAll(Effect.values());
			effectArrayAdapter.notifyDataSetChanged();

			effectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					selectEffect();
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initUnitSpinner() {
			unitTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			unitsSpinner.setAdapter(unitTypeArrayAdapter);
			unitTypeArrayAdapter.clear();
			unitTypeArrayAdapter.addAll(TimeUnit.values());
			unitTypeArrayAdapter.notifyDataSetChanged();
		}

		private void initDiceSpinner() {
			diceTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			diceSpinner.setAdapter(diceTypeArrayAdapter);
			diceTypeArrayAdapter.clear();
			diceTypeArrayAdapter.addAll(Dice.values());
			diceTypeArrayAdapter.notifyDataSetChanged();
		}

		private void initCriticalSeveritySpinner() {
			criticalSeverityArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			criticalSeveritySpinner.setAdapter(criticalSeverityArrayAdapter);
			criticalSeverityArrayAdapter.clear();
			criticalSeverityArrayAdapter.addAll(CriticalSeverity.values());
			criticalSeverityArrayAdapter.notifyDataSetChanged();
		}

		private void initCriticalTypeSpinner() {
			criticalTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			criticalTypeSpinner.setAdapter(criticalTypeArrayAdapter);
			criticalTypeArrayAdapter.clear();
			criticalTypeArrayAdapter.addAll(CriticalType.values());
			criticalTypeArrayAdapter.notifyDataSetChanged();
		}

		private void copyItemToViews() {
			if(currentInstance.getTargetType() != null) {
				targetSpinner.setSelection(targetTypeArrayAdapter.getPosition(currentInstance.getTargetType()));
			}
			if(currentInstance.getEffect() != null) {
				effectSpinner.setSelection(effectArrayAdapter.getPosition(currentInstance.getEffect()));
				selectEffect();
			}
			if(currentInstance.getValue1() != null) {
				value1Edit.setText(currentInstance.getValue1().toString());
			}
			if(currentInstance.getValue2() != null) {
				value1Edit.setText(currentInstance.getValue2().toString());
			}
			if(currentInstance.getValue3() != null) {
				value1Edit.setText(currentInstance.getValue3().toString());
			}
			if(currentInstance.getValue4() != null) {
				value1Edit.setText(currentInstance.getValue4().toString());
			}
		}

		private void selectEffect() {
			Effect effect = (Effect)effectSpinner.getSelectedItem();
			if(effect != null) {
				switch (effect) {
					case ATTACK:
						break;
					case BLEED:
						hideAll();
						setSingleIntegerInput(R.string.hint_bleeding);
						break;
					case BREAKAGE:
						hideAll();
						setSingleIntegerInput(R.string.hint_breakage);
						break;
					case DAZE:
						hideAll();
						setSingleIntegerInput(R.string.hint_dazed);
						break;
					case FATIGUE:
						hideAll();
						setSingleIntegerInput(R.string.hint_fatigue);
						break;
					case GRAPPLE:
						hideAll();
						setSingleIntegerInput(R.string.hint_grappled);
						break;
					case CRITICAL:
						hideAll();
						criticalSeverityLayout.setVisibility(View.VISIBLE);
						criticalTypeLayout.setVisibility(View.VISIBLE);
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof CriticalSeverity) {
							criticalSeveritySpinner.setSelection(criticalSeverityArrayAdapter.getPosition(
									(CriticalSeverity)currentInstance.getValue1()));
						}
						else {
							currentInstance.setValue1(null);
						}
						if(currentInstance.getValue2() != null && currentInstance.getValue2() instanceof CriticalType) {
							criticalTypeSpinner.setSelection(criticalTypeArrayAdapter.getPosition(
									(CriticalType) currentInstance.getValue2()));
						}
						else {
							currentInstance.setValue2(null);
						}
						break;
					case DEATH:
						hideAll();
						value1Layout.setVisibility(View.VISIBLE);
						value1Layout.setHint(getContext().getString(R.string.hint_death));
						value1Edit.setHint(R.string.hint_death);
						unitsLayout.setVisibility(View.VISIBLE);
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
							value1Edit.setText(currentInstance.getValue1().toString());
						}
						else {
							currentInstance.setValue1(null);
						}
						if(currentInstance.getValue2() != null && currentInstance.getValue2() instanceof TimeUnit) {
							unitsSpinner.setSelection(unitTypeArrayAdapter.getPosition((TimeUnit)currentInstance.getValue2()));
						}
						else {
							currentInstance.setValue2(null);
						}
						break;
					case DEFENSIVE_BONUS:
						hideAll();
						setSingleIntegerInput(R.string.hint_defensive_bonus);
						break;
					case DROP_ITEM:
						hideAll();
						value1Layout.setVisibility(View.VISIBLE);
						value1Layout.setHint(getContext().getString(R.string.hint_distance));
						value1Edit.setHint(R.string.hint_distance);
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
							value1Edit.setText(String.valueOf(currentInstance.getValue1()));
						}
						else {
							currentInstance.setValue1(null);
							value1Edit.setText(null);
							setDiceInput();
						}
						break;
					case HIT_POINTS:
						hideAll();
						value1Layout.setVisibility(View.VISIBLE);
						value1Layout.setHint(getContext().getString(R.string.hint_hits));
						value1Edit.setHint(R.string.hint_hits);
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
							value1Edit.setText(String.valueOf(currentInstance.getValue1()));
						}
						else {
							currentInstance.setValue1(null);
							value1Edit.setText(null);
							setDiceInput();
						}
						break;
					case INITIATIVE_BONUS:
						hideAll();
						setSingleIntegerInput(R.string.hint_initiative);
						break;
					case INJURY:
						hideAll();
						setSingleIntegerInput(R.string.hint_injury);
						break;
					case KNOCK_BACK:
						hideAll();
						break;
					case MORALE:
						hideAll();
						break;
					case OFFENSIVE_BONUS:
						hideAll();
						setSingleIntegerInput(R.string.hint_offensive_bonus);
						break;
					case PRONE:
						hideAll();
						setSingleIntegerInput(R.string.hint_prone);
						break;
					case SKILL_BONUS:
						hideAll();
						break;
					case STAGGER:
						hideAll();
						setSingleIntegerInput(R.string.hint_staggered);
						break;
					case STUN:
						hideAll();
						setSingleIntegerInput(R.string.hint_stunned);
						break;
					case STUN_NO_PARRY:
						hideAll();
						setSingleIntegerInput(R.string.hint_stunned_no_parry);
						break;
					case UNCONSCIOUS:
						hideAll();
						break;
				}
			}
		}

		private void hideAll() {
			value1Layout.setVisibility(View.GONE);
			value2Layout.setVisibility(View.GONE);
			value3Layout.setVisibility(View.GONE);
			value4Layout.setVisibility(View.GONE);
			unitsLayout.setVisibility(View.GONE);
			diceLayout.setVisibility(View.GONE);
			criticalSeverityLayout.setVisibility(View.GONE);
			criticalTypeLayout.setVisibility(View.GONE);
		}

		private void setSingleIntegerInput(@StringRes int hintId) {
			value1Layout.setVisibility(View.VISIBLE);
			value1Layout.setHint(getContext().getString(hintId));
			value1Edit.setHint(hintId);
			if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
				value1Edit.setText(currentInstance.getValue1().toString());
			}
			else {
				currentInstance.setValue1(null);
			}
		}

		private void setDiceInput() {
			value2Layout.setVisibility(View.VISIBLE);
			value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
			value2Edit.setHint(R.string.hint_num_dice);
			if(currentInstance.getValue2() != null && currentInstance.getValue2() instanceof Integer) {
				value2Edit.setText(String.valueOf(currentInstance.getValue2()));
			}
			else {
				currentInstance.setValue2(null);
				value2Edit.setText(null);
			}
			diceLayout.setVisibility(View.VISIBLE);
			if(currentInstance.getValue3() != null && currentInstance.getValue3() instanceof Dice) {
				diceSpinner.setSelection(diceTypeArrayAdapter.getPosition((Dice)currentInstance.getValue3()));
			}
			else {
				currentInstance.setValue3(null);
			}
		}
	}
}

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
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;
import com.madinnovations.rmu.data.entities.combat.CriticalSeverity;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.Effect;
import com.madinnovations.rmu.data.entities.combat.RelativeTo;
import com.madinnovations.rmu.data.entities.combat.TargetType;
import com.madinnovations.rmu.data.entities.common.Dice;
import com.madinnovations.rmu.data.entities.common.TimeUnit;
import com.madinnovations.rmu.view.utils.EditTextUtils;

/**
 * Adapter for displaying additional critical results
 */
@SuppressWarnings("unused")
public class AdditionalEffectsAdapter extends ArrayAdapter<AdditionalEffect> {
	private static final String TAG = "AdditionalEffectsAdapte";
	private static final int LAYOUT_RESOURCE_ID = R.layout.additional_effects_row;
	private AdditionalEffect additionalEffect;
	private LayoutInflater   layoutInflater;
	private AdditionalEffectCallbacks additionalEffectCallbacks;

	public AdditionalEffectsAdapter(@NonNull Context context, @LayoutRes int resource,
									AdditionalEffectCallbacks additionalEffectCallbacks) {
		super(context, resource);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.additionalEffectCallbacks = additionalEffectCallbacks;
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
									(Spinner) rowView.findViewById(R.id.critical_type_spinner),
									(LinearLayout) rowView.findViewById(R.id.relative_to_layout),
									(Spinner) rowView.findViewById(R.id.relative_to_spinner),
									(ImageButton) rowView.findViewById(R.id.remove_effect_button));
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

	private class ViewHolder implements EditTextUtils.ValuesCallback {
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
		LinearLayout                   relativeToLayout;
		Spinner                        relativeToSpinner;
		ArrayAdapter<RelativeTo>       relativeToArrayAdapter;
		ImageButton                    removeEffectButton;

		public ViewHolder(Spinner targetSpinner, LinearLayout effectLayout, Spinner effectSpinner, TextInputLayout value1Layout,
						  EditText value1Edit, TextInputLayout value2Layout, EditText value2Edit, TextInputLayout value3Layout,
						  EditText value3Edit, TextInputLayout value4Layout, EditText value4Edit, LinearLayout unitsLayout,
						  Spinner unitsSpinner, LinearLayout diceLayout, Spinner diceSpinner, LinearLayout criticalSeverityLayout,
						  Spinner criticalSeveritySpinner, LinearLayout criticalTypeLayout, Spinner criticalTypeSpinner,
						  LinearLayout relativeToLayout, Spinner relativeToSpinner, ImageButton removeEffectButton) {
			this.targetSpinner = targetSpinner;
			this.effectLayout = effectLayout;
			this.effectSpinner = effectSpinner;
			this.value1Layout = value1Layout;
			this.value1Edit = value1Edit;
			EditTextUtils.initEdit(value1Edit, getContext(), this, R.id.value1_edit, 0);
			this.value2Layout = value2Layout;
			this.value2Edit = value2Edit;
			EditTextUtils.initEdit(value2Edit, getContext(), this, R.id.value2_edit, 0);
			this.value3Layout = value3Layout;
			this.value3Edit = value3Edit;
			EditTextUtils.initEdit(value3Edit, getContext(), this, R.id.value3_edit, 0);
			this.value4Layout = value4Layout;
			this.value4Edit = value4Edit;
			EditTextUtils.initEdit(value4Edit, getContext(), this, R.id.value4_edit, 0);
			this.unitsLayout = unitsLayout;
			this.unitsSpinner = unitsSpinner;
			this.diceLayout = diceLayout;
			this.diceSpinner = diceSpinner;
			this.criticalSeverityLayout = criticalSeverityLayout;
			this.criticalSeveritySpinner = criticalSeveritySpinner;
			this.criticalTypeLayout = criticalTypeLayout;
			this.criticalTypeSpinner = criticalTypeSpinner;
			this.relativeToLayout = relativeToLayout;
			this.relativeToSpinner = relativeToSpinner;
			this.removeEffectButton = removeEffectButton;

			initTargetSpinner();
			initEffectSpinner();
			initUnitSpinner();
			initDiceSpinner();
			initDice2Spinner();
			initCriticalSeveritySpinner();
			initCriticalTypeSpinner();
			initRemoveEffectButton();
		}

		@Override
		public String getValueForEditText(int editTextId) {
			String result = null;

			switch (editTextId) {
				case R.id.value1_edit:
					if(currentInstance.getValue1() != null) {
						result = String.valueOf(currentInstance.getValue1());
					}
					break;
				case R.id.value2_edit:
					if(currentInstance.getValue2() != null) {
						result = String.valueOf(currentInstance.getValue2());
					}
					break;
				case R.id.value3_edit:
					if(currentInstance.getValue3() != null) {
						result = String.valueOf(currentInstance.getValue3());
					}
					break;
				case R.id.value4_edit:
					if(currentInstance.getValue4() != null) {
						result = String.valueOf(currentInstance.getValue4());
					}
					break;
			}
			return result;
		}

		@Override
		public void setValueFromEditText(int editTextId, String newString) {
			switch (editTextId) {
				case R.id.value1_edit:
					if(newString.isEmpty()) {
						currentInstance.setValue1(null);
					}
					else {
						currentInstance.setValue1(Integer.valueOf(newString));
					}
					if(currentInstance.getEffect().equals(Effect.DROP_ITEM) ||
							currentInstance.getEffect().equals(Effect.MOVE)) {
						hideAll();
						setDiceInput();
					}
					break;
				case R.id.value2_edit:
					if(newString.isEmpty()) {
						currentInstance.setValue2(null);
					}
					else {
						currentInstance.setValue2(Integer.valueOf(newString));
					}
					if(currentInstance.getEffect().equals(Effect.DROP_ITEM) ||
							currentInstance.getEffect().equals(Effect.MOVE)) {
						hideAll();
						if(currentInstance.getValue2() == null) {
							currentInstance.setValue1(null);
						}
						setDiceInput();
					}
					break;
				case R.id.value3_edit:
					if(newString.isEmpty()) {
						currentInstance.setValue3(null);
					}
					else {
						currentInstance.setValue3(Integer.valueOf(newString));
					}
					if(currentInstance.getEffect().equals(Effect.DROP_ITEM) ||
							currentInstance.getEffect().equals(Effect.MOVE)) {
						hideAll();
						setDiceInput();
					}
					break;
				case R.id.value4_edit:
					if(newString.isEmpty()) {
						currentInstance.setValue4(null);
					}
					else {
						currentInstance.setValue4(Integer.valueOf(newString));
					}
					if(currentInstance.getEffect().equals(Effect.DROP_ITEM) ||
							currentInstance.getEffect().equals(Effect.MOVE)) {
						hideAll();
						if(currentInstance.getValue4() == null) {
							currentInstance.setValue3(null);
						}
						setDiceInput();
					}
					break;
			}
			additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
		}

		@Override
		public boolean performLongClick(int editTextId) {
			return false;
		}

		private void initTargetSpinner() {
			targetTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			targetSpinner.setAdapter(targetTypeArrayAdapter);
			targetTypeArrayAdapter.clear();
			targetTypeArrayAdapter.addAll(TargetType.values());
			targetTypeArrayAdapter.notifyDataSetChanged();

			targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					TargetType targetType = (TargetType)targetSpinner.getItemAtPosition(position);
					if(!targetType.equals(currentInstance.getTargetType())) {
						currentInstance.setTargetType(targetType);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
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
					additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
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

			unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					TimeUnit timeUnit = (TimeUnit)unitsSpinner.getItemAtPosition(position);
					if(currentInstance.getValue2() == null || !(currentInstance.getValue2() instanceof TimeUnit) ||
							!timeUnit.equals(currentInstance.getValue2())) {
						currentInstance.setValue2(timeUnit);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initDiceSpinner() {
			diceTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			diceSpinner.setAdapter(diceTypeArrayAdapter);
			diceTypeArrayAdapter.clear();
			diceTypeArrayAdapter.addAll(Dice.values());
			diceTypeArrayAdapter.notifyDataSetChanged();

			diceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					Dice dice = (Dice)diceSpinner.getItemAtPosition(position);
					if(currentInstance.getValue1() == null || !(currentInstance.getValue1() instanceof Dice) ||
							!dice.equals(currentInstance.getValue1())) {
						currentInstance.setValue1(dice);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initDice2Spinner() {
			relativeToArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			relativeToSpinner.setAdapter(relativeToArrayAdapter);
			relativeToArrayAdapter.clear();
			relativeToArrayAdapter.addAll(RelativeTo.values());
			relativeToArrayAdapter.notifyDataSetChanged();

			relativeToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					RelativeTo relativeTo = (RelativeTo) relativeToSpinner.getItemAtPosition(position);
					if(currentInstance.getValue3() == null || !(currentInstance.getValue3() instanceof RelativeTo) ||
							!relativeTo.equals(currentInstance.getValue3())) {
						currentInstance.setValue3(relativeTo);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initCriticalSeveritySpinner() {
			criticalSeverityArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			criticalSeveritySpinner.setAdapter(criticalSeverityArrayAdapter);
			criticalSeverityArrayAdapter.clear();
			criticalSeverityArrayAdapter.addAll(CriticalSeverity.values());
			criticalSeverityArrayAdapter.notifyDataSetChanged();

			criticalSeveritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					CriticalSeverity criticalSeverity = (CriticalSeverity)criticalSeveritySpinner.getItemAtPosition(position);
					if(currentInstance.getValue1() == null || !(currentInstance.getValue1() instanceof CriticalSeverity) ||
							!criticalSeverity.equals(currentInstance.getValue1())) {
						currentInstance.setValue1(criticalSeverity);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initCriticalTypeSpinner() {
			criticalTypeArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.single_field_row);
			criticalTypeSpinner.setAdapter(criticalTypeArrayAdapter);
			criticalTypeArrayAdapter.clear();
			criticalTypeArrayAdapter.addAll(CriticalType.values());
			criticalTypeArrayAdapter.notifyDataSetChanged();

			criticalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					CriticalType criticalType = (CriticalType)criticalTypeSpinner.getItemAtPosition(position);
					if(currentInstance.getValue2() == null || !(currentInstance.getValue2() instanceof CriticalType) ||
							!criticalType.equals(currentInstance.getValue2())) {
						currentInstance.setValue2(criticalType);
						additionalEffectCallbacks.updateAdditionalEffect(currentInstance);
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		}

		private void initRemoveEffectButton() {
			removeEffectButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					additionalEffectCallbacks.removeAdditionalEffect(currentInstance);
				}
			});
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
			else {
				value1Edit.setText(null);
			}
			if(currentInstance.getValue2() != null) {
				value2Edit.setText(currentInstance.getValue2().toString());
			}
			else {
				value2Edit.setText(null);
			}
			if(currentInstance.getValue3() != null) {
				value3Edit.setText(currentInstance.getValue3().toString());
			}
			else {
				value3Edit.setText(null);
			}
			if(currentInstance.getValue4() != null) {
				value4Edit.setText(currentInstance.getValue4().toString());
			}
			else {
				value4Edit.setText(null);
			}
		}

		private void selectEffect() {
			Effect effect = (Effect)effectSpinner.getSelectedItem();
			if(effect != null) {
				currentInstance.setEffect(effect);
				switch (effect) {
					case ATTACK:
						hideAll();
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
					case UNCONSCIOUS:
						hideAll();
						value1Layout.setVisibility(View.VISIBLE);
						if(effect.equals(Effect.DEATH)) {
							value1Layout.setHint(getContext().getString(R.string.hint_death));
						}
						else {
							value1Layout.setHint(getContext().getString(R.string.hint_unconscious));
						}
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
							unitsSpinner.setSelection(unitTypeArrayAdapter.getPosition(TimeUnit.ROUNDS));
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
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
							value1Layout.setVisibility(View.VISIBLE);
							value1Layout.setHint(getContext().getString(R.string.hint_hits));
							value1Edit.setText(String.valueOf(currentInstance.getValue1()));
						}
						else if (currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Dice) {
							value2Layout.setVisibility(View.VISIBLE);
							value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
							value2Edit.setText(String.valueOf(currentInstance.getValue2()));
							diceLayout.setVisibility(View.VISIBLE);
							diceSpinner.setSelection(diceTypeArrayAdapter.getPosition((Dice)currentInstance.getValue1()));
						}
						else {
							value1Layout.setVisibility(View.VISIBLE);
							value1Layout.setHint(getContext().getString(R.string.hint_hits));
							value1Edit.setText(null);
							value2Layout.setVisibility(View.VISIBLE);
							value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
							value2Edit.setText(null);
							diceLayout.setVisibility(View.VISIBLE);
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
						setSingleIntegerInput(R.string.hint_morale);
						break;
					case MOVE:
						hideAll();
						value1Layout.setVisibility(View.VISIBLE);
						value1Layout.setHint(getContext().getString(R.string.hint_distance));
						if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
							value1Edit.setText(String.valueOf(currentInstance.getValue1()));
						}
						else {
							currentInstance.setValue1(null);
							value1Edit.setText(null);
							setDiceInput();
						}
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
			relativeToLayout.setVisibility(View.GONE);
		}

		private void setSingleIntegerInput(@StringRes int hintId) {
			value1Layout.setVisibility(View.VISIBLE);
			value1Layout.setHint(getContext().getString(hintId));
			if(currentInstance.getValue1() != null && currentInstance.getValue1() instanceof Integer) {
				value1Edit.setText(currentInstance.getValue1().toString());
			}
			else {
				value1Edit.setText(null);
				currentInstance.setValue1(null);
			}
		}

		private void setDiceInput() {
			if(currentInstance.getValue1() != null) {
				if(currentInstance.getValue1() instanceof Integer) {
					value1Layout.setVisibility(View.VISIBLE);
					value1Layout.setHint(getContext().getString(R.string.hint_distance));
					value1Edit.setText(String.valueOf(currentInstance.getValue1()));
				}
				else if(currentInstance.getValue1() instanceof Dice) {
					value2Layout.setVisibility(View.VISIBLE);
					value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
					diceLayout.setVisibility(View.VISIBLE);
					diceSpinner.setSelection(diceTypeArrayAdapter.getPosition((Dice)currentInstance.getValue1()));
					if(currentInstance.getValue2() != null) {
						value2Edit.setText(String.valueOf(currentInstance.getValue2()));
					}
				}
			}
			else if(currentInstance.getValue2() != null) {
				value2Layout.setVisibility(View.VISIBLE);
				value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
				diceLayout.setVisibility(View.VISIBLE);
				Dice dice = (Dice)diceSpinner.getSelectedItem();
				currentInstance.setValue1(dice);
			}
			else {
				value1Layout.setVisibility(View.VISIBLE);
				value1Layout.setHint(getContext().getString(R.string.hint_distance));
				value1Edit.setText(null);
				value2Layout.setVisibility(View.VISIBLE);
				value2Layout.setHint(getContext().getString(R.string.hint_num_dice));
				value2Edit.setText(null);
				diceLayout.setVisibility(View.VISIBLE);
			}

			if(currentInstance.getValue1() == null || !(currentInstance.getValue1() instanceof Integer) ||
					!currentInstance.getValue1().equals(0)) {
				if (currentInstance.getValue3() != null) {
					if (currentInstance.getValue3() instanceof Integer) {
						value3Layout.setVisibility(View.VISIBLE);
						value3Layout.setHint(getContext().getString(R.string.hint_direction));
						value3Edit.setText(String.valueOf(currentInstance.getValue3()));
					}
					else if (currentInstance.getValue3() instanceof RelativeTo) {
						value4Layout.setVisibility(View.VISIBLE);
						value4Layout.setHint(getContext().getString(R.string.hint_relative_degrees));
						relativeToLayout.setVisibility(View.VISIBLE);
						relativeToSpinner.setSelection(relativeToArrayAdapter.getPosition(
								(RelativeTo) currentInstance.getValue3()));
						if (currentInstance.getValue4() != null) {
							value4Edit.setText(String.valueOf(currentInstance.getValue4()));
						}
					}
				}
				else if (currentInstance.getValue4() != null) {
					value4Layout.setVisibility(View.VISIBLE);
					value4Layout.setHint(getContext().getString(R.string.hint_relative_degrees));
					relativeToLayout.setVisibility(View.VISIBLE);
					RelativeTo relativeTo = (RelativeTo) relativeToSpinner.getSelectedItem();
					currentInstance.setValue3(relativeTo);
				}
				else {
					value3Layout.setVisibility(View.VISIBLE);
					value3Layout.setHint(getContext().getString(R.string.hint_direction));
					value3Edit.setText(null);
					value4Layout.setVisibility(View.VISIBLE);
					value4Layout.setHint(getContext().getString(R.string.hint_relative_degrees));
					value4Edit.setText(null);
//					relativeToLayout.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	public interface AdditionalEffectCallbacks {
		/**
		 * Request to add a new AdditionalEffect instance to its parent.
		 *
		 * @param additionalEffect  the new AdditionalEffect instance
		 */
		void addAdditionalEffect(AdditionalEffect additionalEffect);

		/**
		 * Notification that an AdditionalEffect was changed.
		 *
		 * @param additionalEffect  the AdditionalEffect instance that was changed
		 */
		void updateAdditionalEffect(AdditionalEffect additionalEffect);

		/**
		 * Request to remove an AdditionalEffect instance from its parent.
		 *
		 * @param additionalEffect  the AdditionalEffect instance to remove
		 */
		void removeAdditionalEffect(AdditionalEffect additionalEffect);
	}
}

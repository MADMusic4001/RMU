/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.utils.ReactiveUtils;
import com.madinnovations.rmu.data.entities.DatabaseObject;
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.common.UnitType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Talent>, CheckBoxUtils.ValuesCallback,
		EditTextUtils.ValuesCallback {
	private static final String TAG = "TalentsFragment";
	@Inject
	protected AttackRxHandler                          attackRxHandler;
	@Inject
	protected CriticalTypeRxHandler                    criticalTypeRxHandler;
	@Inject
	protected SkillRxHandler                           skillRxHandler;
	@Inject
	protected SpecializationRxHandler                  specializationRxHandler;
	@Inject
	protected SpellRxHandler                           spellRxHandler;
	@Inject
	protected SpellListRxHandler                       spellListRxHandler;
	@Inject
	protected TalentRxHandler                          talentRxHandler;
	@Inject
	protected TalentCategoryRxHandler                  talentCategoryRxHandler;
	@Inject
	protected ReactiveUtils                            reactiveUtils;
	private ArrayAdapter<TalentCategory>               filterSpinnerAdapter;
	private Spinner                                    filterSpinner;
	private LayoutInflater                             layoutInflater;
	private ArrayAdapter<TalentCategory>               categorySpinnerAdapter;
	private ArrayAdapter<Action>                       actionSpinnerAdapter;
	private TwoFieldListAdapter<Talent>                listAdapter;
	private ListView                                   listView;
	private Spinner                                    categorySpinner;
	private EditText                                   nameEdit;
	private EditText                                   descriptionEdit;
	private EditText                                   initialCostEdit;
	private EditText                                   costPerTierEdit ;
	private EditText                                   minTiersEdit;
	private EditText                                   maxTiersEdit;
	private CheckBox                                   creatureOnlyCheckbox;
	private CheckBox                                   flawCheckbox;
	private CheckBox                                   situationalCheckbox;
	private Spinner                                    actionSpinner;
	private LinearLayout                               parametersList;
	private Talent                                     currentInstance           = new Talent();
	private boolean                                    isNew                     = true;
	private Map<Parameter, Collection<DatabaseObject>> parameterCollectionsCache = new HashMap<>();
	private Map<View, Integer>                         indexMap                  = new HashMap<>();
	private Object                                     choice;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		this.layoutInflater = inflater;
		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

		choice = new Object() {
			@Override
			public String toString() {
				return getActivity().getString(R.string.choice_label);
			}
		};

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_talent_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_talent_description);

		initFilterSpinner(layout);
		initCategorySpinner(layout);
		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, R.string.validation_talent_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
												 R.string.validation_talent_description_required);
		initialCostEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.initial_cost_edit,
												 R.string.validation_talent_initial_cost_required);
		costPerTierEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.cost_per_tier_edit,
												 R.string.validation_talent_cost_per_tier_required);
		minTiersEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.min_tiers_edit,
											  R.string.validation_talent_min_tiers_required);
		maxTiersEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.max_tiers_edit,
											  R.string.validation_talent_max_tiers_required);
		creatureOnlyCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.creature_only_check_box);
		flawCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.flaw_check_box);
		situationalCheckbox = CheckBoxUtils.initCheckBox(layout, this, R.id.situational_check_box);
		initActionSpinner(layout);
		initAddParametersButton(layout, inflater);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			saveItem();
		}
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.talents_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Talent();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			listAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.talent_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Talent talent;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_talent:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Talent();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_talent:
				talent = (Talent)listView.getItemAtPosition(info.position);
				if(talent != null) {
					deleteItem(talent);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Talent talent) {
		return talent.getName();
	}

	@Override
	public CharSequence getField2Value(Talent talent) {
		return talent.getDescription();
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch (checkBoxId) {
			case R.id.creature_only_check_box:
				result = currentInstance.isCreatureOnly();
				break;
			case R.id.flaw_check_box:
				result = currentInstance.isFlaw();
				break;
			case R.id.situational_check_box:
				result = currentInstance.isSituational();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch (checkBoxId) {
			case R.id.creature_only_check_box:
				currentInstance.setCreatureOnly(newBoolean);
				saveItem();
				break;
			case R.id.flaw_check_box:
				currentInstance.setFlaw(newBoolean);
				saveItem();
				break;
			case R.id.situational_check_box:
				currentInstance.setSituational(newBoolean);
				saveItem();
				break;
		}
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
			case R.id.notes_edit:
				result = currentInstance.getDescription();
				break;
			case R.id.initial_cost_edit:
				result = String.valueOf(currentInstance.getDpCost());
				break;
			case R.id.cost_per_tier_edit:
				result = String.valueOf(currentInstance.getDpCostPerTier());
				break;
			case R.id.min_tiers_edit:
				result = String.valueOf(currentInstance.getMinTier());
				break;
			case R.id.max_tiers_edit:
				result = String.valueOf(currentInstance.getMaxTier());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				currentInstance.setName(newString);
				saveItem();
				break;
			case R.id.notes_edit:
				currentInstance.setDescription(newString);
				saveItem();
				break;
			case R.id.initial_cost_edit:
				currentInstance.setDpCost(Short.valueOf(newString));
				saveItem();
				break;
			case R.id.cost_per_tier_edit:
				currentInstance.setDpCostPerTier(Short.valueOf(newString));
				saveItem();
				break;
			case R.id.min_tiers_edit:
				currentInstance.setMinTier(Short.valueOf(newString));
				saveItem();
				break;
			case R.id.max_tiers_edit:
				currentInstance.setMaxTier(Short.valueOf(newString));
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}
	// </editor-fold>

	// <editor-fold desc="copy/save/delete methods">
	@SuppressWarnings("unchecked")
	private boolean copyViewsToItem() {
		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}
		boolean changed = false;
		short newShort;
		Action newAction;

		if(categorySpinner.getSelectedItemPosition() != -1) {
			TalentCategory newCategory = categorySpinnerAdapter.getItem(categorySpinner.getSelectedItemPosition());
			if ((newCategory == null && currentInstance.getCategory() != null) ||
					(newCategory != null && !newCategory.equals(currentInstance.getCategory()))) {
				currentInstance.setCategory(newCategory);
				changed = true;
			}
		}

		String newValue = nameEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getName() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getName()))) {
			currentInstance.setName(newValue);
			changed = true;
		}

		newValue = descriptionEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getDescription() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newValue);
			changed = true;
		}

		if(initialCostEdit.getText().length() > 0) {
			newShort = Short.valueOf(initialCostEdit.getText().toString());
			if(newShort != currentInstance.getDpCost()) {
				currentInstance.setDpCost(newShort);
				changed = true;
			}
		}

		if(costPerTierEdit.getText().length() > 0) {
			newShort = Short.valueOf(costPerTierEdit.getText().toString());
			if(newShort != currentInstance.getDpCostPerTier()) {
				currentInstance.setDpCostPerTier(newShort);
				changed = true;
			}
		}

		if(minTiersEdit.getText().length() > 0) {
			newShort = Short.valueOf(minTiersEdit.getText().toString());
			if(newShort != currentInstance.getMinTier()) {
				currentInstance.setMinTier(newShort);
				changed = true;
			}
		}

		if(maxTiersEdit.getText().length() > 0) {
			newShort = Short.valueOf(maxTiersEdit.getText().toString());
			if(newShort != currentInstance.getMaxTier()) {
				currentInstance.setMaxTier(newShort);
				changed = true;
			}
		}

		if(creatureOnlyCheckbox.isChecked() != currentInstance.isCreatureOnly()) {
			currentInstance.setCreatureOnly(creatureOnlyCheckbox.isChecked());
			changed = true;
		}

		if(flawCheckbox.isChecked() != currentInstance.isFlaw()) {
			currentInstance.setFlaw(flawCheckbox.isChecked());
			changed = true;
		}

		if(situationalCheckbox.isChecked() != currentInstance.isSituational()) {
			currentInstance.setSituational(situationalCheckbox.isChecked());
			changed = true;
		}

		newAction = actionSpinnerAdapter.getItem(actionSpinner.getSelectedItemPosition());
		if(newAction != null && !newAction.equals(currentInstance.getAction())) {
			currentInstance.setAction(newAction);
			changed = true;
		}

		changed |= copyParameterRows();

		return changed;
	}

	@SuppressWarnings("ConstantConditions")
	private boolean copyParameterRows() {
		boolean changed = false;

		for(Map.Entry<View, Integer> entry : indexMap.entrySet()) {
			Spinner spinner = (Spinner) entry.getKey().findViewById(R.id.parameter_spinner);
			Spinner valuesSpinner = (Spinner) entry.getKey().findViewById(R.id.value_spinner);
			EditText initialValueEdit = (EditText)entry.getKey().findViewById(R.id.initial_value_edit);
			EditText valuePer1Edit = (EditText)entry.getKey().findViewById(R.id.value_per1_edit);
			Spinner valuePer1Spinner = (Spinner) entry.getKey().findViewById(R.id.value_per1_spinner);
			EditText valuePer2Edit = (EditText)entry.getKey().findViewById(R.id.value_per2_edit);
			Spinner valuePer2Spinner = (Spinner) entry.getKey().findViewById(R.id.value_per2_spinner);
			EditText valuePer3Edit = (EditText)entry.getKey().findViewById(R.id.value_per3_edit);
			Spinner valuePer3Spinner = (Spinner) entry.getKey().findViewById(R.id.value_per3_spinner);
			Parameter parameter = (Parameter) spinner.getSelectedItem();
			TalentParameterRow parameterRow = currentInstance.getTalentParameterRows()[entry.getValue()];
			if (parameter.getHandler() != null || parameter.getEnumValues() != null) {
				Object object = valuesSpinner.getSelectedItem();
				if (object != null) {
					if (parameterRow.getParameter().getHandler() != null && object instanceof DatabaseObject) {
						int objectId = ((DatabaseObject) object).getId();
						if (objectId == -1) {
							if (parameterRow.getInitialValue() != null) {
								parameterRow.setInitialValue(null);
								changed = true;
							}
						}
						else if (parameterRow.getInitialValue() == null || objectId != parameterRow.getInitialValue()) {
							parameterRow.setInitialValue(objectId);
							changed = true;
						}
					}
					else if (parameterRow.getParameter().getEnumValues() != null && (object instanceof Enum<?>
							|| object.equals(choice))) {
						if (object.equals(choice)) {
							if (parameterRow.getEnumName() != null) {
								parameterRow.setEnumName(null);
								changed = true;
							}
						}
						else {
							@SuppressWarnings("ConstantConditions")
							String enumName = ((Enum<?>) object).name();
							if (!enumName.equals(parameterRow.getEnumName())) {
								parameterRow.setEnumName(enumName);
								changed = true;
							}
						}
					}
					if (changed) {
						saveItem();
					}
				}
			}
			else {
				Integer nullableInteger = null;
				if(initialValueEdit.getText().length() > 0) {
					nullableInteger = Integer.valueOf(initialValueEdit.getText().toString());
				}
				if(nullableInteger == null && parameterRow.getInitialValue() != null) {
					parameterRow.setInitialValue(null);
					changed = true;
				}
				if(nullableInteger != null && !nullableInteger.equals(parameterRow.getInitialValue())) {
					parameterRow.setInitialValue(nullableInteger);
					changed = true;
				}

				for(int i = parameterRow.getPerValues().length - 1; i >= 0; i--) {
					EditText valueEdit = null;
					Spinner valueSpinner = null;
					switch (i) {
						case 0:
							valueEdit = valuePer1Edit;
							valueSpinner = valuePer1Spinner;
							break;
						case 1:
							valueEdit = valuePer2Edit;
							valueSpinner = valuePer2Spinner;
							break;
						case 2:
							valueEdit = valuePer3Edit;
							valueSpinner = valuePer3Spinner;
							break;
					}
					if(valueEdit.getText().length() > 0) {
						nullableInteger = Integer.valueOf(valueEdit.getText().toString());
					}
					if(nullableInteger == null && parameterRow.getPerValues()[i] != null) {
						if(i == 0) {
							parameterRow.setPerValues(new Integer[0]);
							parameterRow.setUnitTypes(new UnitType[0]);
						}
						else {
							parameterRow.setPerValues(Arrays.copyOfRange(parameterRow.getPerValues(), 0, i - 1));
							parameterRow.setUnitTypes(Arrays.copyOfRange(parameterRow.getUnitTypes(), 0, i - 1));
						}
						changed = true;
					}
					UnitType selectedUnitType = (UnitType)valueSpinner.getSelectedItem();
					if(nullableInteger != null && selectedUnitType != null) {
						if (parameterRow.getPerValues().length > i && parameterRow.getUnitTypes().length > i
								&& (!nullableInteger.equals(parameterRow.getPerValues()[i])
								|| !selectedUnitType.equals(parameterRow.getUnitTypes()[i]))) {
							parameterRow.getPerValues()[i] = nullableInteger;
							parameterRow.getUnitTypes()[i] = (UnitType)valueSpinner.getSelectedItem();
							changed = true;
						}
					}
				}
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		categorySpinner.setSelection(categorySpinnerAdapter.getPosition(currentInstance.getCategory()));
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		initialCostEdit.setText(String.valueOf(currentInstance.getDpCost()));
		costPerTierEdit.setText(String.valueOf(currentInstance.getDpCostPerTier()));
		minTiersEdit.setText(String.valueOf(currentInstance.getMinTier()));
		maxTiersEdit.setText(String.valueOf(currentInstance.getMaxTier()));
		creatureOnlyCheckbox.setChecked(currentInstance.isCreatureOnly());
		flawCheckbox.setChecked(currentInstance.isFlaw());
		situationalCheckbox.setChecked(currentInstance.isSituational());
		actionSpinner.setSelection(actionSpinnerAdapter.getPosition(currentInstance.getAction()));

		parametersList.removeAllViews();
		indexMap.clear();
		for(int i = 0; i < currentInstance.getTalentParameterRows().length; i++) {
			addParameterRow(layoutInflater, i);
		}

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			talentRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Talent>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving new Talent: " + currentInstance, e);
							Toast.makeText(getActivity(), R.string.toast_talent_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Talent savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_talent_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Talent item) {
		talentRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_talent_delete_failed, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Boolean success) {
						if(success) {
							int position = listAdapter.getPosition(item);
							if(position == listAdapter.getCount() -1) {
								position--;
							}
							listAdapter.remove(item);
							listAdapter.notifyDataSetChanged();
							if(position >= 0) {
								listView.setSelection(position);
								listView.setItemChecked(position, true);
								currentInstance = listAdapter.getItem(position);
							}
							else {
								currentInstance = new Talent();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_talent_category_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	// </editor-fold>

	// <editor-fold desc="init talents_fragment layout views">
	private void initFilterSpinner(View layout) {
		filterSpinner = (Spinner)layout.findViewById(R.id.talent_category_filter_spinner);
		filterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		filterSpinner.setAdapter(filterSpinnerAdapter);

		final TalentCategory allCategories = new TalentCategory();
		allCategories.setName(getString(R.string.label_all_talent_categories));
		filterSpinnerAdapter.add(allCategories);
		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all specialization Skill instances", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						filterSpinnerAdapter.addAll(talentCategories);
						filterSpinnerAdapter.notifyDataSetChanged();
						filterSpinner.setSelection(filterSpinnerAdapter.getPosition(allCategories));
					}
				});

		filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredTalents(filterSpinnerAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredTalents(null);
			}
		});
	}

	private void initCategorySpinner(View layout) {
		categorySpinner = (Spinner)layout.findViewById(R.id.category_spinner);
		categorySpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		categorySpinner.setAdapter(categorySpinnerAdapter);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> items) {
						categorySpinnerAdapter.clear();
						categorySpinnerAdapter.addAll(items);
						categorySpinnerAdapter.notifyDataSetChanged();
					}
				});

		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCategory() == null || categorySpinnerAdapter.getPosition(currentInstance.getCategory()) != position) {
					currentInstance.setCategory(categorySpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getCategory() != null) {
					currentInstance.setCategory(null);
					saveItem();
				}
			}
		});
	}

	private void initActionSpinner(View layout) {
		actionSpinner = (Spinner)layout.findViewById(R.id.action_spinner);
		actionSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		actionSpinnerAdapter.addAll(Action.values());
		actionSpinnerAdapter.notifyDataSetChanged();
		actionSpinner.setAdapter(actionSpinnerAdapter);

		actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Action action = actionSpinnerAdapter.getItem(position);
				if(action != null && !action.equals(currentInstance.getAction())) {
					currentInstance.setAction(action);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initAddParametersButton(final View layout, final LayoutInflater inflater) {
		parametersList = (LinearLayout)layout.findViewById(R.id.parameters_list);
		Button button = (Button)layout.findViewById(R.id.add_parameter_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int length = currentInstance.getTalentParameterRows().length;
				TalentParameterRow[] rows = new TalentParameterRow[length + 1];
				if(length > 0) {
					System.arraycopy(currentInstance.getTalentParameterRows(), 0, rows, 0, length);
				}
				rows[length] = new TalentParameterRow();
				rows[length].setParameter(Parameter.BONUS);
				currentInstance.setTalentParameterRows(rows);
				addParameterRow(inflater, length);
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		loadFilteredTalents(null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Talent) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Talent();
					isNew = true;
				}
				copyItemToViews();
			}
		});

		registerForContextMenu(listView);
	}

	private void addParameterRow(LayoutInflater inflater, final int index) {
		View parameterRowView = inflater.inflate(R.layout.talent_parameter_row, parametersList, false);
		indexMap.put(parameterRowView, index);
		initRemoveParameterButton(parameterRowView);
		initParameterSpinner(parameterRowView);
		initInitialValueEdit(parameterRowView, currentInstance.getTalentParameterRows()[index]);
		initValuePerEdits(parameterRowView);
		initValueSpinner(parameterRowView, currentInstance.getTalentParameterRows()[index]);
		setParameterRow(parameterRowView, currentInstance.getTalentParameterRows()[index]);

		parametersList.addView(parameterRowView);
	}
	// </editor-fold>

	// <editor-fold desc="talent_parameter_row layout views initialization and handling">
	private void initParameterSpinner(final View layout) {
		Spinner spinner = (Spinner)layout.findViewById(R.id.parameter_spinner);
		final ArrayAdapter<Parameter> adapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		adapter.addAll(Parameter.values());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(currentInstance.getTalentParameterRows()[indexMap.get(layout)].getParameter()));

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Parameter parameter = adapter.getItem(position);
				if(parameter != null) {
					TalentParameterRow row = currentInstance.getTalentParameterRows()[indexMap.get(layout)];
					if(!parameter.equals(row.getParameter())) {
						row.setParameter(parameter);
						setParameterRow(layout, row);
 						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initInitialValueEdit(final View layout, final TalentParameterRow row) {
		final EditText editText = (EditText)layout.findViewById(R.id.initial_value_edit);
		Integer value = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
		if(value != null) {
			editText.setText(String.valueOf(value));
		}
		else {
			editText.setText(null);
		}

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					Integer newInteger = Integer.valueOf(editText.getText().toString());
					if (!newInteger.equals(row.getInitialValue())) {
						row.setInitialValue(newInteger);
						saveItem();
					}
				}
				else if(row.getInitialValue() != null) {
					currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(null);
					saveItem();
				}
			}
		});
	}

	private void initValuePerEdits(final View layout) {
		EditText valuePerEdit = null;
		EditText nextValuePerEdit = null;
		Spinner  valuePerSpinner = null;
		Spinner  nextValuePerSpinner = null;
		ArrayAdapter<UnitType> spinnerAdapter;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[indexMap.get(layout)];

		for(int i = 0; i < 3; i++) {
			spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
			spinnerAdapter.addAll(UnitType.values());

			switch (i) {
				case 0:
					valuePerEdit = (EditText) layout.findViewById(R.id.value_per1_edit);
					valuePerSpinner = (Spinner) layout.findViewById(R.id.value_per1_spinner);
					nextValuePerEdit = (EditText) layout.findViewById(R.id.value_per2_edit);
					nextValuePerSpinner = (Spinner) layout.findViewById(R.id.value_per2_spinner);
					break;
				case 1:
					valuePerEdit = nextValuePerEdit;
					valuePerSpinner = nextValuePerSpinner;
					nextValuePerEdit = (EditText) layout.findViewById(R.id.value_per3_edit);
					nextValuePerSpinner = (Spinner) layout.findViewById(R.id.value_per3_spinner);
					break;
				case 2:
					valuePerEdit = nextValuePerEdit;
					valuePerSpinner = nextValuePerSpinner;
					nextValuePerEdit = null;
					nextValuePerSpinner = null;
					break;
			}
			if (valuePerSpinner != null) {
				valuePerSpinner.setAdapter(spinnerAdapter);
			}
			Integer value = null;
			UnitType unitType = null;
			if(row.getPerValues().length > i && row.getUnitTypes().length > i) {
				value = row.getPerValues()[i];
				unitType = row.getUnitTypes()[i];
			}
			if (valuePerEdit != null && valuePerSpinner != null && value != null && unitType != null) {
				valuePerEdit.setText(String.valueOf(value));
				valuePerSpinner.setSelection(spinnerAdapter.getPosition(unitType));
			}
			else if (valuePerEdit != null && valuePerSpinner != null) {
				valuePerEdit.setText(null);
				valuePerSpinner.setVisibility(GONE);
			}

			initValuePerEdit(row, i, valuePerEdit, valuePerSpinner, spinnerAdapter, nextValuePerEdit);

			initValuePerSpinner(row, i, valuePerSpinner);
		}
	}

	private void initValuePerEdit(final TalentParameterRow row, final int index, final EditText valuePerEdit,
								  final Spinner valuePerSpinner, ArrayAdapter<UnitType> spinnerAdapter,
								  final EditText nextValuePerEdit) {
		Integer value;
		UnitType unitType;
		if(row.getPerValues().length > index && row.getUnitTypes().length > index) {
			value = row.getPerValues()[index];
			unitType = row.getUnitTypes()[index];
			if (value != null && unitType != null) {
				valuePerEdit.setText(String.valueOf(value));
				valuePerSpinner.setSelection(spinnerAdapter.getPosition(unitType));
			}
			else {
				valuePerEdit.setText(null);
				valuePerSpinner.setVisibility(GONE);
			}
		}

		valuePerEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(valuePerEdit.getText().length() > 0) {
					valuePerSpinner.setVisibility(VISIBLE);
					if(nextValuePerEdit != null) {
						nextValuePerEdit.setVisibility(VISIBLE);
					}
				}
				else {
					if(nextValuePerEdit != null) {
						nextValuePerEdit.setVisibility(GONE);
					}
					valuePerSpinner.setVisibility(GONE);
				}
			}
		});

		valuePerEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (valuePerEdit.getText().length() > 0) {
					valuePerSpinner.setVisibility(View.VISIBLE);
					if(nextValuePerEdit != null) {
						nextValuePerEdit.setVisibility(View.VISIBLE);
					}
					Integer newInteger = Integer.valueOf(valuePerEdit.getText().toString());
					if(row.getPerValues().length > index && !newInteger.equals(row.getPerValues()[index])) {
						row.getPerValues()[index] = newInteger;
						saveItem();
					}
					else if(row.getPerValues().length <= index) {
						Integer[] valuesPer = new Integer[index + 1];
						System.arraycopy(row.getPerValues(), 0, valuesPer, 0, row.getPerValues().length);
						valuesPer[index] = newInteger;
						row.setPerValues(valuesPer);

						UnitType[] unitTypes = new UnitType[index + 1];
						System.arraycopy(row.getUnitTypes(), 0, unitTypes, 0, row.getUnitTypes().length);
						unitTypes[index] = (UnitType)valuePerSpinner.getSelectedItem();
						row.setUnitTypes(unitTypes);

						saveItem();
					}
				}
				else {
					valuePerSpinner.setVisibility(View.GONE);
					if(nextValuePerEdit != null) {
						nextValuePerEdit.setVisibility(GONE);
					}
					if(index == row.getPerValues().length - 1) {
						Integer[] valuesPer = new Integer[index];
						if(index > 0) {
							System.arraycopy(row.getPerValues(), 0, valuesPer, 0, row.getPerValues().length - 1);
						}
						row.setPerValues(valuesPer);
						saveItem();
					}
					else if (nextValuePerEdit != null && nextValuePerEdit.getVisibility() == VISIBLE){
						valuePerEdit.setError(getString(R.string.validation_per_value_required));
					}
				}
			}
		});
	}

	private void initValuePerSpinner(final TalentParameterRow row, final int index, final Spinner valuePerSpinner) {
		valuePerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(row.getUnitTypes().length > index && !row.getUnitTypes()[index].equals(valuePerSpinner.getSelectedItem())) {
					row.getUnitTypes()[index] = (UnitType)valuePerSpinner.getSelectedItem();
					saveItem();
				}
				else if(row.getUnitTypes().length == index) {
					UnitType[] unitTypes = new UnitType[index + 1];
					System.arraycopy(row.getUnitTypes(), 0, unitTypes, 0, row.getUnitTypes().length);
					unitTypes[index] = (UnitType)valuePerSpinner.getSelectedItem();
					row.setUnitTypes(unitTypes);
					Log.d(TAG, "onItemSelected: saving " + currentInstance.print());
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initRemoveParameterButton(final View layout) {
		final ImageButton removeParameterButton = (ImageButton)layout.findViewById(R.id.remove_parameter_button);

		removeParameterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parametersList.removeView(layout);
				int index = indexMap.get(layout);
				int position = index + 1;
				int newLength = currentInstance.getTalentParameterRows().length -1;
				TalentParameterRow[] newRows = new TalentParameterRow[newLength];
				if(indexMap.size() == 1) {
					indexMap.clear();
				}
				else {
					if (index > 0) {
						System.arraycopy(currentInstance.getTalentParameterRows(), 0, newRows, 0, newLength);
					}
					if (index < newLength) {
						System.arraycopy(currentInstance.getTalentParameterRows(), position, newRows, index, newLength - index);
					}
					indexMap.remove(layout);
				}
				currentInstance.setTalentParameterRows(newRows);
				saveItem();
			}
		});
	}

	private void initValueSpinner(final View layout, final TalentParameterRow parameterRow) {
		final Spinner valuesSpinner = (Spinner)layout.findViewById(R.id.value_spinner);
		final ArrayAdapter<Object> valuesAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		valuesSpinner.setAdapter(valuesAdapter);

		setParameterRow(layout, parameterRow);

		valuesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				boolean changed = false;
				Object object = valuesAdapter.getItem(position);
				if(object != null) {
					if(parameterRow.getParameter().getHandler() != null && object instanceof DatabaseObject) {
						int objectId = ((DatabaseObject)object).getId();
						if(objectId == -1) {
							if(parameterRow.getInitialValue() != null) {
								parameterRow.setInitialValue(null);
								changed = true;
							}
						}
						else if(parameterRow.getInitialValue() == null || objectId != parameterRow.getInitialValue()) {
							parameterRow.setInitialValue(objectId);
							changed = true;
						}
					}
					else if(parameterRow.getParameter().getEnumValues() != null && (object instanceof Enum<?>
							|| object.equals(choice))) {
						if(object.equals(choice)) {
							if(parameterRow.getEnumName() != null) {
								parameterRow.setEnumName(null);
								changed = true;
							}
						}
						else {
							@SuppressWarnings("ConstantConditions")
							String enumName = ((Enum<?>) object).name();
							if(!enumName.equals(parameterRow.getEnumName())) {
								parameterRow.setEnumName(enumName);
								changed = true;
							}
						}
					}
					if(changed) {
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void loadFilteredTalents(final TalentCategory filter) {
		Observable<Collection<Talent>> observable;

		if(filter == null || filter.getId() == -1) {
			observable = talentRxHandler.getAll();
		}
		else {
			observable = talentRxHandler.getTalentsForTalentCategory(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {
						int position = listAdapter.getPosition(currentInstance);
						if (position == -1 && listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							position = 0;
						}
						if (position >= 0) {
							listView.setSelection(position);
							listView.setItemChecked(position, true);
							listAdapter.notifyDataSetChanged();
						}
						copyItemToViews();
					}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting Talent instances", e);
						Toast.makeText(TalentsFragment.this.getActivity(),
									   R.string.toast_talents_load_failed,
									   Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						listAdapter.clear();
						listAdapter.addAll(talents);
						listAdapter.notifyDataSetChanged();
						if(filter == null) {
							String toastString;
							toastString = String.format(getString(R.string.toast_talents_loaded), talents.size());
							Toast.makeText(TalentsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	private void setParameterRow(View layout, final TalentParameterRow talentParameterRow) {
		final TextView valuesSpinnerLabel = (TextView) layout.findViewById(R.id.value_spinner_label);
		final Spinner valuesSpinner = (Spinner)layout.findViewById(R.id.value_spinner);
		final ArrayAdapter<Object> valuesAdapter = (ArrayAdapter<Object>)valuesSpinner.getAdapter();
		final EditText initialValueEdit = (EditText)layout.findViewById(R.id.initial_value_edit);
		final EditText valuePer1Edit = (EditText)layout.findViewById(R.id.value_per1_edit);
		final Spinner valuePer1Spinner = (Spinner)layout.findViewById(R.id.value_per1_spinner);
		final EditText valuePer2Edit = (EditText)layout.findViewById(R.id.value_per2_edit);
		final Spinner valuePer2Spinner = (Spinner)layout.findViewById(R.id.value_per2_spinner);
		final EditText valuePer3Edit = (EditText)layout.findViewById(R.id.value_per3_edit);
		final Spinner valuePer3Spinner = (Spinner)layout.findViewById(R.id.value_per3_spinner);

		if(talentParameterRow.getParameter().getHandler() != null) {
			final Collection<DatabaseObject> spinnerValues = parameterCollectionsCache.get(talentParameterRow.getParameter());
			if(spinnerValues == null) {
				reactiveUtils.getGetAllObservable(talentParameterRow.getParameter().getHandler())
						.subscribe(new Subscriber<Object>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting parameter values.", e);
							}
							@Override
							public void onNext(Object results) {
								if(talentParameterRow.getParameter().getHandler()
										== ReactiveUtils.Handler.SPECIALIZATION_RX_HANDLER) {
									Collection<DatabaseObject> specializationResults = new ArrayList<>();
									for(Skill skill : (Collection<Skill>)results) {
										for(Specialization specialization : skill.getSpecializations()) {
											specializationResults.add(specialization);
										}
									}
									parameterCollectionsCache.put(talentParameterRow.getParameter(), specializationResults);
								}
								else {
									parameterCollectionsCache.put(talentParameterRow.getParameter(),
																  (Collection<DatabaseObject>) results);
								}
								valuesAdapter.clear();
								valuesAdapter.add(choice);
								valuesAdapter.addAll(parameterCollectionsCache.get(talentParameterRow.getParameter()));
								String className = null;
								for (DatabaseObject databaseObject : parameterCollectionsCache.get(
										talentParameterRow.getParameter())) {
									if(className == null) {
										className = databaseObject.getClass().getSimpleName() + ":";
									}
									if(talentParameterRow.getInitialValue() == null) {
										valuesSpinner.setSelection(valuesAdapter.getPosition(choice));
									}
									else if (databaseObject.getId() == talentParameterRow.getInitialValue()) {
										valuesSpinner.setSelection(valuesAdapter.getPosition(databaseObject));
										break;
									}
								}
								valuesSpinnerLabel.setText(className);
							}
						});
			}
			else {
				valuesAdapter.clear();
				valuesAdapter.add(choice);
				String className = null;
				DatabaseObject databaseObject = null;
				for(DatabaseObject databaseObject1 : spinnerValues) {
					databaseObject = databaseObject1;
				}
				if(databaseObject != null) {
					className = databaseObject.getClass().getSimpleName() + ":";
				}
				valuesSpinnerLabel.setText(className);
				valuesAdapter.addAll(spinnerValues);
			}
			valuesSpinnerLabel.setVisibility(View.VISIBLE);
			valuesSpinner.setVisibility(View.VISIBLE);
			initialValueEdit.setVisibility(GONE);
			valuePer1Edit.setVisibility(GONE);
			valuePer1Spinner.setVisibility(GONE);
			valuePer2Edit.setVisibility(GONE);
			valuePer2Spinner.setVisibility(GONE);
			valuePer3Edit.setVisibility(GONE);
			valuePer3Spinner.setVisibility(GONE);
		}
		else if(talentParameterRow.getParameter().getEnumValues() != null) {
			valuesAdapter.clear();
			valuesAdapter.add(choice);
			valuesAdapter.addAll((Object[])talentParameterRow.getParameter().getEnumValues());
			valuesAdapter.notifyDataSetChanged();
			String enumName = null;
			if(indexMap.get(layout) != null) {
				enumName = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getEnumName();
			}
			if(enumName != null) {
				for (Enum enumObject : talentParameterRow.getParameter().getEnumValues()) {
					if (enumObject.name().equals(enumName)) {
						valuesSpinner.setSelection(valuesAdapter.getPosition(enumObject));
						break;
					}
				}
			}
			else {
				valuesSpinner.setSelection(0);
			}
			String className = talentParameterRow.getParameter().getEnumValues()[0].getDeclaringClass().getSimpleName() + ":";
			valuesSpinnerLabel.setText(className);
			valuesSpinnerLabel.setVisibility(View.VISIBLE);
			valuesSpinner.setVisibility(View.VISIBLE);
			initialValueEdit.setVisibility(GONE);
			valuePer1Edit.setVisibility(GONE);
			valuePer1Spinner.setVisibility(GONE);
			valuePer2Edit.setVisibility(GONE);
			valuePer2Spinner.setVisibility(GONE);
			valuePer3Edit.setVisibility(GONE);
			valuePer3Spinner.setVisibility(GONE);
		}
		else {
			if(talentParameterRow.getInitialValue() != null) {
				initialValueEdit.setText(String.valueOf(talentParameterRow.getInitialValue()));
			}
			else {
				initialValueEdit.setText(null);
			}

			valuePer1Edit.setVisibility(View.VISIBLE);
			if(talentParameterRow.getPerValues().length > 0 && talentParameterRow.getUnitTypes().length > 0
					&& talentParameterRow.getPerValues()[0] != null) {
				valuePer1Edit.setText(String.valueOf(talentParameterRow.getPerValues()[0]));
				valuePer1Spinner.setVisibility(View.VISIBLE);
				if(talentParameterRow.getUnitTypes()[0] != null) {
					valuePer1Spinner.setSelection(((ArrayAdapter<UnitType>) valuePer1Spinner.getAdapter()).getPosition(
							talentParameterRow.getUnitTypes()[0]));
				}
			}
			else {
				valuePer1Edit.setText(null);
				valuePer1Spinner.setVisibility(GONE);
			}

			if(talentParameterRow.getPerValues().length > 1 && talentParameterRow.getUnitTypes().length > 1
					&& talentParameterRow.getPerValues()[1] != null) {
				valuePer2Edit.setVisibility(View.VISIBLE);
				valuePer2Edit.setText(String.valueOf(talentParameterRow.getPerValues()[1]));
				valuePer2Spinner.setVisibility(View.VISIBLE);
				valuePer2Spinner.setSelection(((ArrayAdapter<UnitType>)valuePer1Spinner.getAdapter()).getPosition(
						talentParameterRow.getUnitTypes()[1]));
			}
			else {
				valuePer2Edit.setText(null);
				valuePer2Spinner.setVisibility(GONE);
			}

			if(talentParameterRow.getPerValues().length > 2 && talentParameterRow.getUnitTypes().length > 2
					&& talentParameterRow.getPerValues()[2] != null) {
				valuePer3Edit.setVisibility(View.VISIBLE);
				valuePer3Edit.setText(String.valueOf(talentParameterRow.getPerValues()[2]));
				valuePer3Spinner.setVisibility(View.VISIBLE);
				valuePer3Spinner.setSelection(((ArrayAdapter<UnitType>)valuePer1Spinner.getAdapter()).getPosition(
						talentParameterRow.getUnitTypes()[2]));
			}
			else {
				valuePer3Edit.setText(null);
				valuePer3Spinner.setVisibility(GONE);
			}

			valuesSpinnerLabel.setVisibility(GONE);
			valuesSpinner.setVisibility(GONE);
			initialValueEdit.setVisibility(View.VISIBLE);
		}
	}
	// </editor-fold>

}

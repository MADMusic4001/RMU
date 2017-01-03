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
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

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

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_talent_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_talent_description));

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

	// <editor-fold desc="copy/save/deletemethods">
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

		if(situationalCheckbox.isChecked() != currentInstance.isSituational()) {
			currentInstance.setSituational(situationalCheckbox.isChecked());
			changed = true;
		}

		newAction = actionSpinnerAdapter.getItem(actionSpinner.getSelectedItemPosition());
		if(newAction != null && !newAction.equals(currentInstance.getAction())) {
			currentInstance.setAction(newAction);
			changed = true;
		}

		for(Map.Entry<View, Integer> entry : indexMap.entrySet()) {
			Spinner spinner = (Spinner) entry.getKey().findViewById(R.id.parameter_spinner);
			Spinner valuesSpinner = (Spinner) entry.getKey().findViewById(R.id.value_spinner);
			EditText initialValueEdit = (EditText)entry.getKey().findViewById(R.id.initial_value_edit);
			EditText valuePerEdit = (EditText)entry.getKey().findViewById(R.id.value_per_edit);
			CheckBox perTierCheckbox = (CheckBox)entry.getKey().findViewById(R.id.per_tier_check_box);
			CheckBox perLevelCheckbox = (CheckBox)entry.getKey().findViewById(R.id.per_level_check_box);
			CheckBox perRoundCheckbox = (CheckBox)entry.getKey().findViewById(R.id.per_round_check_box);
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

				if(valuePerEdit.getText().length() > 0) {
					nullableInteger = Integer.valueOf(valuePerEdit.getText().toString());
				}
				if(nullableInteger == null && parameterRow.getValuePer() != null) {
					parameterRow.setValuePer(null);
					changed = true;
				}
				if(nullableInteger != null && !nullableInteger.equals(parameterRow.getValuePer())) {
					parameterRow.setValuePer(nullableInteger);
					changed = true;
				}

				boolean isChecked = perTierCheckbox.isChecked();
				if(parameterRow.isPerTier() != isChecked) {
					parameterRow.setPerTier(isChecked);
					changed = true;
				}

				isChecked = perLevelCheckbox.isChecked();
				if(parameterRow.isPerLevel() != isChecked) {
					parameterRow.setPerLevel(isChecked);
					changed = true;
				}

				isChecked = perRoundCheckbox.isChecked();
				if(parameterRow.isPerRound() != isChecked) {
					parameterRow.setPerRound(isChecked);
					changed = true;
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
							Toast.makeText(getActivity(), getString(R.string.toast_talent_save_failed), Toast.LENGTH_SHORT).show();
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
								Toast.makeText(getActivity(), getString(R.string.toast_talent_saved), Toast.LENGTH_SHORT).show();
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
						String toastString = getString(R.string.toast_talent_delete_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getActivity(), getString(R.string.toast_talent_category_deleted), Toast.LENGTH_SHORT).show();
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
		initInitialValueEdit(parameterRowView);
		initValuePerEdit(parameterRowView);
		initValueCheckBoxes(parameterRowView);
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

	private void initInitialValueEdit(final View layout) {
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
					if(indexMap.get(layout) < currentInstance.getTalentParameterRows().length) {
						if (!newInteger.equals(currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue())) {
							currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(newInteger);
							saveItem();
						}
					}
				}
				else {
					if(currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue() != null) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(null);
						saveItem();
					}
				}
			}
		});
	}

	private void initValuePerEdit(final View layout) {
		final EditText valuePerEdit = (EditText)layout.findViewById(R.id.value_per_edit);
		Integer value = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getValuePer();
		if(value != null) {
			valuePerEdit.setText(String.valueOf(value));
		}
		else {
			valuePerEdit.setText(null);
		}

		valuePerEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(valuePerEdit.getText().length() > 0) {
					Integer newInteger = Integer.valueOf(valuePerEdit.getText().toString());
					if(indexMap.get(layout) < currentInstance.getTalentParameterRows().length) {
						if (!newInteger.equals(currentInstance.getTalentParameterRows()[indexMap.get(layout)].getValuePer())) {
							currentInstance.getTalentParameterRows()[indexMap.get(layout)].setValuePer(newInteger);
							saveItem();
						}
					}
				}
				else {
					if(currentInstance.getTalentParameterRows()[indexMap.get(layout)].getValuePer() != null) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setValuePer(null);
						saveItem();
					}
				}
			}
		});
	}

	private void initValueCheckBoxes(final View layout) {
		final CheckBox perTierCheckBox = (CheckBox)layout.findViewById(R.id.per_tier_check_box);
		perTierCheckBox.setChecked(currentInstance.getTalentParameterRows()[indexMap.get(layout)].isPerTier());
		perTierCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = indexMap.get(layout);
				boolean newBoolean = perTierCheckBox.isChecked();
				if(newBoolean != currentInstance.getTalentParameterRows()[index].isPerTier()) {
					currentInstance.getTalentParameterRows()[index].setPerTier(newBoolean);
					saveItem();
				}
			}
		});

		final CheckBox perLevelCheckBox = (CheckBox)layout.findViewById(R.id.per_level_check_box);
		perLevelCheckBox.setChecked(currentInstance.getTalentParameterRows()[indexMap.get(layout)].isPerLevel());
		perLevelCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = indexMap.get(layout);
				boolean newBoolean = perLevelCheckBox.isChecked();
				if(newBoolean != currentInstance.getTalentParameterRows()[index].isPerLevel()) {
					currentInstance.getTalentParameterRows()[index].setPerLevel(newBoolean);
					saveItem();
				}
			}
		});

		final CheckBox perRoundCheckBox = (CheckBox)layout.findViewById(R.id.per_round_check_box);
		perRoundCheckBox.setChecked(currentInstance.getTalentParameterRows()[indexMap.get(layout)].isPerRound());
		perRoundCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = indexMap.get(layout);
				boolean newBoolean = perRoundCheckBox.isChecked();
				if(newBoolean != currentInstance.getTalentParameterRows()[index].isPerRound()) {
					currentInstance.getTalentParameterRows()[index].setPerRound(newBoolean);
					saveItem();
				}
			}
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
						System.arraycopy(currentInstance.getTalentParameterRows(), 0, newRows, 0, index);
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
									   getString(R.string.toast_talents_load_failed),
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
		final EditText valuePerEdit = (EditText)layout.findViewById(R.id.value_per_edit);
		final CheckBox perTierCheckbox = (CheckBox)layout.findViewById(R.id.per_tier_check_box);
		final CheckBox perLevelCheckbox = (CheckBox)layout.findViewById(R.id.per_level_check_box);
		final CheckBox perRoundCheckbox = (CheckBox)layout.findViewById(R.id.per_round_check_box);

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
								parameterCollectionsCache.put(talentParameterRow.getParameter(),
															  (Collection<DatabaseObject>) results);
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
			valuePerEdit.setVisibility(GONE);
			perTierCheckbox.setVisibility(GONE);
			perLevelCheckbox.setVisibility(GONE);
			perRoundCheckbox.setVisibility(GONE);
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
			valuePerEdit.setVisibility(GONE);
			perTierCheckbox.setVisibility(GONE);
			perLevelCheckbox.setVisibility(GONE);
			perRoundCheckbox.setVisibility(GONE);
		}
		else {
			if(talentParameterRow.getInitialValue() != null) {
				initialValueEdit.setText(String.valueOf(talentParameterRow.getInitialValue()));
			}
			else {
				initialValueEdit.setText(null);
			}
			if(talentParameterRow.getValuePer() != null) {
				valuePerEdit.setText(String.valueOf(talentParameterRow.getValuePer()));
			}
			else {
				valuePerEdit.setText(null);
			}
			perTierCheckbox.setChecked(talentParameterRow.isPerTier());
			perLevelCheckbox.setChecked(talentParameterRow.isPerLevel());
			perRoundCheckbox.setChecked(talentParameterRow.isPerRound());
			valuesSpinnerLabel.setVisibility(GONE);
			valuesSpinner.setVisibility(GONE);
			initialValueEdit.setVisibility(View.VISIBLE);
			valuePerEdit.setVisibility(View.VISIBLE);
			perTierCheckbox.setVisibility(View.VISIBLE);
			perLevelCheckbox.setVisibility(View.VISIBLE);
			perRoundCheckbox.setVisibility(View.VISIBLE);
		}
	}
	// </editor-fold>

}

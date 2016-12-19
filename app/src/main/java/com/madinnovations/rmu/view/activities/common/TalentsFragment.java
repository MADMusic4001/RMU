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
import android.util.SparseArray;
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
import com.madinnovations.rmu.data.entities.combat.Action;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.Resistance;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Sense;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentParameterRow;
import com.madinnovations.rmu.data.entities.spells.Spell;
import com.madinnovations.rmu.data.entities.spells.SpellList;
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
import static android.view.View.VISIBLE;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Talent>, CheckBoxUtils.ValuesCallback,
		EditTextUtils.ValuesCallback {
	private static final String TAG = "TalentsFragment";
	@Inject
	AttackRxHandler                      attackRxHandler;
	@Inject
	CriticalTypeRxHandler                criticalTypeRxHandler;
	@Inject
	SkillRxHandler                       skillRxHandler;
	@Inject
	SpecializationRxHandler              specializationRxHandler;
	@Inject
	SpellRxHandler                       spellRxHandler;
	@Inject
	SpellListRxHandler                   spellListRxHandler;
	@Inject
	TalentRxHandler                      talentRxHandler;
	@Inject
	TalentCategoryRxHandler              talentCategoryRxHandler;
	private ArrayAdapter<TalentCategory> filterSpinnerAdapter;
	private Spinner                      filterSpinner;
	private LayoutInflater               layoutInflater;
	private ArrayAdapter<TalentCategory> categorySpinnerAdapter;
	private ArrayAdapter<Action>         actionSpinnerAdapter;
	private TwoFieldListAdapter<Talent>  listAdapter;
	private ListView                     listView;
	private Spinner                      categorySpinner;
	private EditText                     nameEdit;
	private EditText                     descriptionEdit;
	private EditText                     initialCostEdit;
	private EditText                     costPerTierEdit ;
	private EditText                     minTiersEdit;
	private EditText                     maxTiersEdit;
	private CheckBox                     flawCheckbox;
	private CheckBox                     situationalCheckbox;
	private Spinner                      actionSpinner;
	private LinearLayout                 parametersList;
	private Talent                       currentInstance           = new Talent();
	private boolean                      isNew                     = true;
	private SparseArray<Attack>         attackSparseArray         = null;
	private SparseArray<Skill>          skillSparseArray          = null;
	private SparseArray<Specialization> specializationSparseArray = null;
	private SparseArray<Spell>          spellSparseArray          = null;
	private SparseArray<SpellList>      spellListSparseArray      = null;
	private SparseArray<CriticalType>   criticalTypeSparseArray   = null;
	private Map<View, Integer> indexMap             = new HashMap<>();
	private Attack             choiceAttack         = null;
	@SuppressWarnings("unused")
	private Resistance         choiceResistance     = null;
	private Skill              choiceSkill          = null;
	private Specialization     choiceSpecialization = null;
	private Spell              choiceSpell          = null;
	private SpellList          choiceSpellList      = null;
	private CriticalType       choiceCriticalType   = null;
	@SuppressWarnings("unused")
	private Statistic          choiceStat           = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		this.layoutInflater = inflater;
		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

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
			Spinner spinner = (Spinner)entry.getKey().findViewById(R.id.parameter_spinner);
			Parameter parameter = (Parameter)spinner.getSelectedItem();
			if(parameter != null) {
				switch (parameter) {
					case ATTACK:
						changed |= copyAttackViewsToItem(entry.getKey(), entry.getValue());
						break;
					case ELEMENTAL_RR:
					case FEAR_RR:
					case FOLLOWER_FEAR_RR:
					case MAGICAL_RR:
					case PHYSICAL_RR:
						changed |= copyResistanceViewsToItem(entry.getKey(), entry.getValue(), parameter);
						break;
					case SENSE:
						changed |= copySenseViewsToItem(entry.getKey(), entry.getValue());
						break;
					case SKILL:
						changed |= copySkillViewsToItem(entry.getKey(), entry.getValue());
						break;
					case SPECIALIZATION:
						changed |= copySpecializationsViewsToItem(entry.getKey(), entry.getValue());
						break;
					case SPELL:
						changed |= copySpellsViewsToItem(entry.getKey(), entry.getValue());
						break;
					case SPELL_LIST:
						changed |= copySpellListsViewsToItem(entry.getKey(), entry.getValue());
						break;
					case STAT:
						changed |= copyStatsViewsToItem(entry.getKey(), entry.getValue());
						break;
					case CONDITION:
						changed |= copyNoValuesViewsToItem(entry.getValue(), parameter);
						break;
					case CRITICAL_TYPE:
						changed |= copyCriticalTypeViewsToItem(entry.getKey(), entry.getValue());
						break;
					case CRITICAL_SEVERITY:
						changed |= copyCriticalSeverityViewsToItem(entry.getKey(), entry.getValue());
						break;
					default:
						changed |= copyValuesViewsToItem(entry.getKey(), entry.getValue(), parameter);
						break;
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

	private void initFilterSpinner(View layout) {
		filterSpinner = (Spinner)layout.findViewById(R.id.talent_category_filter_spinner);
		filterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
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
						Log.e("SpecializationsFragment", "Exception caught getting all specialization Skill instances", e);
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
		categorySpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
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
		actionSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		actionSpinnerAdapter.clear();
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
		initAttacksSpinner(parameterRowView);
		initResistancesSpinner(parameterRowView);
		initSenseSpinner(parameterRowView);
		initSkillsSpinner(parameterRowView);
		initSpecializationsSpinner(parameterRowView);
		initSpellsSpinner(parameterRowView);
		initSpellListsSpinner(parameterRowView);
		initCriticalTypesSpinner(parameterRowView);
		initCriticalSeveritiesSpinner(parameterRowView);
		initInitialAdjustmentEdit(parameterRowView);
		initAdjustmentPerEdit(parameterRowView);
		initStatsSpinner(parameterRowView);

		Parameter parameter = currentInstance.getTalentParameterRows()[index].getParameter();
		switch (parameter) {
			case ATTACK:
				setAttacksVisibility(parameterRowView, VISIBLE);
				break;
			case ELEMENTAL_RR:
			case FEAR_RR:
			case FOLLOWER_FEAR_RR:
			case MAGICAL_RR:
			case PHYSICAL_RR:
				setResistancesVisibility(parameterRowView, VISIBLE);
				break;
			case SKILL:
				setSkillsVisibility(parameterRowView, VISIBLE);
				break;
			case SPECIALIZATION:
				setSpecializationsVisibility(parameterRowView, VISIBLE);
				break;
			case SPELL:
				setSpellsVisibility(parameterRowView, VISIBLE);
				break;
			case SPELL_LIST:
				setSpellListsVisibility(parameterRowView, VISIBLE);
				break;
			case STAT:
				setStatsVisibility(parameterRowView, VISIBLE);
				break;
			case CRITICAL_TYPE:
				setCriticalTypeVisibility(parameterRowView, VISIBLE);
				break;
			case CRITICAL_SEVERITY:
				setCriticalSeverityVisibility(parameterRowView, VISIBLE);
				break;
			case CONDITION:
				break;
			default:
				setValuesVisibility(parameterRowView, VISIBLE);
				break;
		}

		parametersList.addView(parameterRowView);
	}

	private void initParameterSpinner(final View layout) {
		Spinner spinner = (Spinner)layout.findViewById(R.id.parameter_spinner);
		final ArrayAdapter<Parameter> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		adapter.clear();
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
						switch (parameter) {
							case ATTACK:
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setAttacksVisibility(layout, VISIBLE);
								break;
							case CRITICAL_TYPE:
								setAttacksVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, VISIBLE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								break;
							case CRITICAL_SEVERITY:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, VISIBLE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								break;
							case ELEMENTAL_RR:
								setResistanceSpinnerAdapter(layout, Resistance.getElementalResistances());
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setResistancesVisibility(layout, VISIBLE);
								break;
							case FEAR_RR:
							case FOLLOWER_FEAR_RR:
								setResistanceSpinnerAdapter(layout, Resistance.getFearResistances());
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setResistancesVisibility(layout, VISIBLE);
								break;
							case MAGICAL_RR:
								setResistanceSpinnerAdapter(layout, Resistance.getMagicalResistances());
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setResistancesVisibility(layout, VISIBLE);
								break;
							case PHYSICAL_RR:
								setResistanceSpinnerAdapter(layout, Resistance.getPhysicalResistances());
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setResistancesVisibility(layout, VISIBLE);
								break;
							case SENSE:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSenseVisibility(layout, VISIBLE);
								break;
							case SKILL:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSkillsVisibility(layout, VISIBLE);
								break;
							case SPECIALIZATION:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSpecializationsVisibility(layout, VISIBLE);
								break;
							case SPELL:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSpellsVisibility(layout, VISIBLE);
								break;
							case SPELL_LIST:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setSpellListsVisibility(layout, VISIBLE);
								break;
							case STAT:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								setStatsVisibility(layout, VISIBLE);
								break;
							case CONDITION:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, GONE);
								break;
							default:
								setAttacksVisibility(layout, GONE);
								setCriticalSeverityVisibility(layout, GONE);
								setCriticalTypeVisibility(layout, GONE);
								setResistancesVisibility(layout, GONE);
								setSenseVisibility(layout, GONE);
								setSkillsVisibility(layout, GONE);
								setSpecializationsVisibility(layout, GONE);
								setSpellsVisibility(layout, GONE);
								setSpellListsVisibility(layout, GONE);
								setStatsVisibility(layout, GONE);
								setValuesVisibility(layout, VISIBLE);
								break;
						}
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
		final EditText editText = (EditText)layout.findViewById(R.id.value_per_edit);
		Integer value = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getValuePer();
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

	private void initAttacksSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.attack_spinner);
		final ArrayAdapter<Attack> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(attackSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < attackSparseArray.size(); i++) {
				adapter.add(attackSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				Attack currentAttack = new Attack(index);
				spinner.setSelection(adapter.getPosition(currentAttack));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceAttack = new Attack();
			choiceAttack.setName(getString(R.string.choice_label));
			attackRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Attack>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught getting all Attack instances", e);
						}
						@Override
						public void onNext(Collection<Attack> attacks) {
							attackSparseArray = new SparseArray<>(attacks.size() + 1);
							attackSparseArray.put(choiceAttack.getId(), choiceAttack);
							for(Attack attack : attacks) {
								attackSparseArray.put(attack.getId(), attack);
							}
							adapter.clear();
							adapter.addAll(attacks);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								Attack currentAttack = new Attack(index);
								spinner.setSelection(adapter.getPosition(currentAttack));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Attack attack = adapter.getItem(position);
				if(attack != null) {
					Attack currentAttack = null;
					Integer index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
					if(index != null) {
						currentAttack = attackSparseArray.get(index);
					}
					if(!attack.equals(currentAttack)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(attack.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initResistancesSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.resistance_spinner);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Resistance resistance = ((ArrayAdapter<Resistance>)spinner.getAdapter()).getItem(position);
				if(resistance != null) {
					int index = indexMap.get(layout);
					Resistance currentResistance = Resistance.valueOf(
							currentInstance.getTalentParameterRows()[index].getEnumName());
					if(!resistance.equals(currentResistance)) {
						currentInstance.getTalentParameterRows()[index].setEnumName(resistance.name());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void setResistanceSpinnerAdapter(View layout, Resistance[] resistances) {
		Spinner spinner = (Spinner)layout.findViewById(R.id.resistance_spinner);
		ArrayAdapter<Resistance> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		adapter.clear();
		adapter.addAll(resistances);
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(Resistance.valueOf(
				currentInstance.getTalentParameterRows()[indexMap.get(layout)].getEnumName())));
	}

	private void initSenseSpinner(final View layout) {
		final Spinner spinner = (Spinner) layout.findViewById(R.id.sense_spinner);
		final ArrayAdapter<Sense> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		adapter.clear();
		adapter.addAll(Sense.values());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		TalentParameterRow talentParameterRow = currentInstance.getTalentParameterRows()[indexMap.get(layout)];
		if (Parameter.SENSE.equals(talentParameterRow.getParameter())) {
			Sense currentSense = null;
			if(talentParameterRow.getInitialValue() != null) {
				currentSense = adapter.getItem(talentParameterRow.getInitialValue());
			}
			if(currentSense != null) {
				spinner.setSelection(adapter.getPosition(currentSense));
			}
			else {
				spinner.setSelection(0);
			}
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Sense sense = adapter.getItem(position);
				if(sense != null) {
					Sense currentSense = null;
					if(indexMap.get(layout) != null&& currentInstance.getTalentParameterRows()[indexMap.get(layout)] != null
							&& currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue() != null) {
						currentSense = adapter.getItem(currentInstance.getTalentParameterRows()
																	 [indexMap.get(layout)].getInitialValue());
					}
					if(!sense.equals(currentSense)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setEnumName(sense.name());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSkillsSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.skill_spinner);
		final ArrayAdapter<Skill> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(skillSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < skillSparseArray.size(); i++) {
				adapter.add(skillSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				spinner.setSelection(adapter.getPosition(skillSparseArray.get(index)));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceSkill = new Skill();
			choiceSkill.setName(getString(R.string.choice_label));
			skillRxHandler.getNonSpecializationSkills()
					.subscribe(new Subscriber<Collection<Skill>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught loading all non-specialization Skill instances", e);
						}
						@Override
						public void onNext(Collection<Skill> skills) {
							skillSparseArray = new SparseArray<>(skills.size() + 1);
							skillSparseArray.put(choiceSkill.getId(), choiceSkill);
							for(Skill skill : skills) {
								skillSparseArray.put(skill.getId(), skill);
							}
							adapter.clear();
							adapter.addAll(skills);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								spinner.setSelection(adapter.getPosition(skillSparseArray.get(index)));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Skill skill= adapter.getItem(position);
				if(skill != null) {
					Integer skillId = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
					Skill currentSkill = null;
					if(skillId != null) {
						currentSkill = skillSparseArray.get(skillId);
					}
					if(!skill.equals(currentSkill)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(skill.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSpecializationsSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.specialization_spinner);
		final ArrayAdapter<Specialization> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(specializationSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < specializationSparseArray.size(); i++) {
				adapter.add(specializationSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				spinner.setSelection(adapter.getPosition(specializationSparseArray.get(index)));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceSpecialization = new Specialization();
			choiceSpecialization.setName(getString(R.string.choice_label));
			specializationRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Specialization>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught getting all Specialization intances", e);
						}
						@Override
						public void onNext(Collection<Specialization> specializations) {
							specializationSparseArray = new SparseArray<>(specializations.size() + 1);
							specializationSparseArray.put(choiceSpecialization.getId(), choiceSpecialization);
							for(Specialization specialization : specializations) {
								specializationSparseArray.put(specialization.getId(), specialization);
							}
							adapter.clear();
							adapter.addAll(specializations);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								spinner.setSelection(adapter.getPosition(specializationSparseArray.get(index)));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Specialization specialization= adapter.getItem(position);
				if(specialization != null) {
					Specialization currentSpecialization = specializationSparseArray.get(
							currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue());
					if(!specialization.equals(currentSpecialization)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(specialization.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSpellsSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.spell_spinner);
		final ArrayAdapter<Spell> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(spellSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < spellSparseArray.size(); i++) {
				adapter.add(spellSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				spinner.setSelection(adapter.getPosition(spellSparseArray.get(index)));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceSpell = new Spell();
			choiceSpell.setName(getString(R.string.choice_label));
			spellRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Spell>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception getting all Spell instances", e);
						}
						@Override
						public void onNext(Collection<Spell> spells) {
							spellSparseArray = new SparseArray<>(spells.size() + 1);
							spellSparseArray.put(choiceSpell.getId(), choiceSpell);
							for(Spell spell : spells) {
								spellSparseArray.put(spell.getId(), spell);
							}
							adapter.clear();
							adapter.addAll(spells);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								spinner.setSelection(adapter.getPosition(spellSparseArray.get(index)));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Spell spell = adapter.getItem(position);
				if(spell != null) {
					Spell currentSpell = spellSparseArray.get(
							currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue());
					if(!spell.equals(currentSpell)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(spell.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSpellListsSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.spell_list_spinner);
		final ArrayAdapter<SpellList> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(spellListSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < spellListSparseArray.size(); i++) {
				adapter.add(spellListSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				spinner.setSelection(adapter.getPosition(spellListSparseArray.get(index)));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceSpellList = new SpellList();
			choiceSpellList.setName(getString(R.string.choice_label));
			spellListRxHandler.getAll()
					.subscribe(new Subscriber<Collection<SpellList>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception getting all Spell List instances", e);
						}
						@Override
						public void onNext(Collection<SpellList> spellLists) {
							spellListSparseArray = new SparseArray<>(spellLists.size() + 1);
							spellListSparseArray.put(choiceSpell.getId(), choiceSpellList);
							for(SpellList spellList : spellLists) {
								spellListSparseArray.put(spellList.getId(), spellList);
							}
							adapter.clear();
							adapter.addAll(spellLists);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								spinner.setSelection(adapter.getPosition(spellListSparseArray.get(index)));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SpellList spellList = adapter.getItem(position);
				if(spellList != null) {
					SpellList currentSpellList = null;
					Integer index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
					if(index != null) {
						currentSpellList = spellListSparseArray.get(index);
					}
					if(!spellList.equals(currentSpellList)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(spellList.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initCriticalTypesSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.critical_type_spinner);
		final ArrayAdapter<CriticalType> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		if(criticalTypeSparseArray != null) {
			adapter.clear();
			for(int i = 0; i < criticalTypeSparseArray.size(); i++) {
				adapter.add(criticalTypeSparseArray.valueAt(i));
			}
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			Integer index = null;
			if(indexMap.get(layout) != null) {
				index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
			}
			if(index != null) {
				spinner.setSelection(adapter.getPosition(criticalTypeSparseArray.get(index)));
			}
			else {
				spinner.setSelection(0);
			}
		}
		else {
			choiceCriticalType = new CriticalType();
			choiceCriticalType.setName(getString(R.string.choice_label));
			criticalTypeRxHandler.getAll()
					.subscribe(new Subscriber<Collection<CriticalType>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception getting all Spell List instances", e);
						}
						@Override
						public void onNext(Collection<CriticalType> criticalTypes) {
							criticalTypeSparseArray = new SparseArray<>(criticalTypes.size() + 1);
							criticalTypeSparseArray.put(choiceSpell.getId(), choiceCriticalType);
							for(CriticalType criticalType : criticalTypes) {
								criticalTypeSparseArray.put(criticalType.getId(), criticalType);
							}
							adapter.clear();
							adapter.addAll(criticalTypes);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							Integer index = null;
							if(indexMap.get(layout) != null) {
								index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
							}
							if(index != null) {
								spinner.setSelection(adapter.getPosition(criticalTypeSparseArray.get(index)));
							}
							else {
								spinner.setSelection(0);
							}
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				CriticalType criticalType = adapter.getItem(position);
				if(criticalType != null) {
					Integer index = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getInitialValue();
					CriticalType currentCriticalType = null;
					if(index != null) {
						currentCriticalType = criticalTypeSparseArray.get(index);
					}
					if(!criticalType.equals(currentCriticalType)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setInitialValue(criticalType.getId());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initCriticalSeveritiesSpinner(final View layout) {
		final Spinner spinner = (Spinner)layout.findViewById(R.id.critical_severity_spinner);
		final ArrayAdapter<Character> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		adapter.clear();
		for(char c = 'A'; c <= 'J'; c++) {
			adapter.add(c);
		}
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		Character severity = null;
		if(indexMap.get(layout) != null && currentInstance.getTalentParameterRows()[indexMap.get(layout)].getEnumName() != null) {
			severity = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getEnumName().charAt(0);
		}
		if(severity != null) {
			spinner.setSelection(adapter.getPosition(severity));
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Character severity = adapter.getItem(position);
				if(severity != null) {
					String severityString = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getEnumName();
					Character currentSeverity = null;
					if(severityString != null) {
						currentSeverity = severityString.charAt(0);
					}
					if(!severity.equals(currentSeverity)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)]
								.setEnumName(severity.toString());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initInitialAdjustmentEdit(final View layout) {
		final EditText editText = (EditText)layout.findViewById(R.id.initial_adjustment_edit);
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
						if (!newInteger.equals(currentInstance.getTalentParameterRows()[indexMap.get(layout)]
													   .getInitialValue())) {
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

	private void initAdjustmentPerEdit(final View layout) {
		final EditText editText = (EditText)layout.findViewById(R.id.adjustment_per_tier_edit);
		Integer value = currentInstance.getTalentParameterRows()[indexMap.get(layout)].getValuePer();
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

	private void initStatsSpinner(final View layout) {
		final Spinner spinner = (Spinner) layout.findViewById(R.id.stat_spinner);
		final ArrayAdapter<Statistic> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);

		adapter.clear();
		adapter.addAll(Statistic.getAllStats());
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		TalentParameterRow talentParameterRow = currentInstance.getTalentParameterRows()[indexMap.get(layout)];
		if (Parameter.STAT.equals(talentParameterRow.getParameter())) {
			Statistic currentStat = adapter.getItem(talentParameterRow.getInitialValue());
			if(currentStat != null) {
				spinner.setSelection(adapter.getPosition(currentStat));
			}
			else {
				spinner.setSelection(0);
			}
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Statistic statistic = adapter.getItem(position);
				if(statistic != null) {
					Statistic currentStat = adapter.getItem(currentInstance.getTalentParameterRows()[indexMap.get(layout)]
																	.getInitialValue());
					if(!statistic.equals(currentStat)) {
						currentInstance.getTalentParameterRows()[indexMap.get(layout)].setEnumName(statistic.name());
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private boolean copyNoValuesViewsToItem(int index, Parameter parameter) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != parameter) {
			row.setParameter(parameter);
			changed = true;
		}

		if(row.getInitialValue() != null) {
			row.setInitialValue(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}

	private boolean copyValuesViewsToItem(final View layout, int index, Parameter parameter) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != parameter) {
			row.setParameter(parameter);
			changed = true;
		}
		String value = ((EditText)layout.findViewById(R.id.initial_value_edit)).getText().toString();
		if(value.length() > 0) {
			int intValue = Integer.valueOf(value);
			if(row.getInitialValue() == null || row.getInitialValue() != intValue) {
				row.setInitialValue(intValue);
				changed = true;
			}
		}
		else if(row.getInitialValue() != null) {
			row.setInitialValue(null);
			changed = true;
		}

		value = ((EditText)layout.findViewById(R.id.value_per_edit)).getText().toString();
		if(value.length() > 0) {
			int intValue = Integer.valueOf(value);
			if(row.getValuePer() == null || row.getValuePer() != intValue) {
				row.setValuePer(intValue);
				changed = true;
			}
		}
		else if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		boolean booleanValue = ((CheckBox)layout.findViewById(R.id.per_level_check_box)).isChecked();
		if(row.isPerLevel() != booleanValue) {
			row.setPerLevel(booleanValue);
			changed = true;
		}

		booleanValue = ((CheckBox)layout.findViewById(R.id.per_tier_check_box)).isChecked();
		if(row.isPerTier() != booleanValue) {
			row.setPerTier(booleanValue);
			changed = true;
		}

		booleanValue = ((CheckBox)layout.findViewById(R.id.per_round_check_box)).isChecked();
		if(row.isPerRound() != booleanValue) {
			row.setPerRound(booleanValue);
			changed = true;
		}

		return changed;
	}
	private void setValuesVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.initial_value_label).setVisibility(visibility);
		layout.findViewById(R.id.initial_value_edit).setVisibility(visibility);
		layout.findViewById(R.id.value_per_label).setVisibility(visibility);
		layout.findViewById(R.id.value_per_edit).setVisibility(visibility);
		layout.findViewById(R.id.per_level_check_box).setVisibility(visibility);
		layout.findViewById(R.id.per_tier_check_box).setVisibility(visibility);
		layout.findViewById(R.id.per_round_check_box).setVisibility(visibility);
	}

	private boolean copyAttackViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.ATTACK) {
			row.setParameter(Parameter.ATTACK);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.attack_spinner);
		int id = -1;
		if(spinner.getSelectedItem() != null) {
			id = ((Attack) spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null || id != row.getInitialValue()) {
			row.setInitialValue(id);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setAttacksVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.attack_label).setVisibility(visibility);
		layout.findViewById(R.id.attack_spinner).setVisibility(visibility);
	}

	private boolean copyResistanceViewsToItem(final View layout, int index, Parameter parameter) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != parameter) {
			row.setParameter(parameter);
			changed = true;
		}
		Spinner spinner = (Spinner)layout.findViewById(R.id.resistance_spinner);
		String newEnumName = null;
		if(spinner.getSelectedItem() != null) {
			newEnumName = ((Resistance) spinner.getSelectedItem()).name();
		}
		if(row.getEnumName() == null || !row.getEnumName().equals(newEnumName)) {
			row.setEnumName(newEnumName);
			changed = true;
		}

		if(row.getInitialValue() != null) {
			row.setInitialValue(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setResistancesVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.resistance_label).setVisibility(visibility);
		layout.findViewById(R.id.resistance_spinner).setVisibility(visibility);
	}

	private boolean copySenseViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.SENSE) {
			row.setParameter(Parameter.SENSE);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.sense_spinner);
		String newInitialValue = null;
		if(spinner.getSelectedItem() != null) {
			newInitialValue = ((Sense)spinner.getSelectedItem()).name();
		}
		if(row.getEnumName() == null || row.getEnumName().equals(newInitialValue)) {
			row.setEnumName(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setSenseVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.sense_label).setVisibility(visibility);
		layout.findViewById(R.id.sense_spinner).setVisibility(visibility);
	}

	private boolean copySkillViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.SKILL) {
			row.setParameter(Parameter.SKILL);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.skill_spinner);
		int newInitialValue = -1;
		if(spinner.getSelectedItem() != null) {
			newInitialValue = ((Skill)spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null || newInitialValue != row.getInitialValue()) {
			row.setInitialValue(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setSkillsVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.skill_label).setVisibility(visibility);
		layout.findViewById(R.id.skill_spinner).setVisibility(visibility);
	}

	private boolean copySpecializationsViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.SPECIALIZATION) {
			row.setParameter(Parameter.SPECIALIZATION);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.specialization_spinner);
		int newInitialValue = -1;
		if (spinner.getSelectedItem() != null) {
			newInitialValue = ((Specialization) spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null ||  newInitialValue != row.getInitialValue()) {
			row.setInitialValue(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setSpecializationsVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.specialization_label).setVisibility(visibility);
		layout.findViewById(R.id.specialization_spinner).setVisibility(visibility);
	}

	private boolean copySpellsViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.SPELL) {
			row.setParameter(Parameter.SPELL);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.spell_spinner);
		int newInitialValue = -1;
		if(spinner.getSelectedItem() != null) {
			newInitialValue = ((Spell) spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null || newInitialValue != row.getInitialValue()) {
			row.setInitialValue(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setSpellsVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.spell_label).setVisibility(visibility);
		layout.findViewById(R.id.spell_spinner).setVisibility(visibility);
	}

	private boolean copySpellListsViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.SPELL_LIST) {
			row.setParameter(Parameter.SPELL_LIST);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.spell_list_spinner);
		int newInitialValue = -1;
		if(spinner.getSelectedItem() != null) {
			newInitialValue = ((SpellList) spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null || newInitialValue != row.getInitialValue()) {
			row.setInitialValue(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setSpellListsVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.spell_list_label).setVisibility(visibility);
		layout.findViewById(R.id.spell_list_spinner).setVisibility(visibility);
	}

	private boolean copyStatsViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.STAT) {
			row.setParameter(Parameter.STAT);
			changed = true;
		}

		if(row.getInitialValue() != null) {
			row.setInitialValue(null);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.stat_spinner);
		String newEnumName = null;
		if(spinner.getSelectedItem() != null) {
			newEnumName = ((Statistic) spinner.getSelectedItem()).name();
		}
		if((newEnumName == null && row.getEnumName() != null)
				|| (newEnumName != null && !newEnumName.equals(row.getEnumName()))) {
			row.setEnumName(newEnumName);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setStatsVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.stat_label).setVisibility(visibility);
		layout.findViewById(R.id.stat_spinner).setVisibility(visibility);
	}

	private boolean copyCriticalTypeViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.CRITICAL_TYPE) {
			row.setParameter(Parameter.CRITICAL_TYPE);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.critical_type_spinner);
		int newInitialValue = -1;
		if(spinner.getSelectedItem() != null) {
			newInitialValue = ((CriticalType) spinner.getSelectedItem()).getId();
		}
		if(row.getInitialValue() == null || newInitialValue != row.getInitialValue()) {
			row.setInitialValue(newInitialValue);
			changed = true;
		}

		if(row.getEnumName() != null) {
			row.setEnumName(null);
			changed = true;
		}

		if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setCriticalTypeVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.critical_type_label).setVisibility(visibility);
		layout.findViewById(R.id.critical_type_spinner).setVisibility(visibility);
	}

	private boolean copyCriticalSeverityViewsToItem(final View layout, int index) {
		boolean changed = false;
		TalentParameterRow row = currentInstance.getTalentParameterRows()[index];
		if(row.getParameter() != Parameter.CRITICAL_SEVERITY) {
			row.setParameter(Parameter.CRITICAL_SEVERITY);
			changed = true;
		}

		Spinner spinner = (Spinner)layout.findViewById(R.id.critical_severity_spinner);
		Character severity = null;
		if(spinner.getSelectedItem() != null) {
			severity = (Character)spinner.getSelectedItem();
		}
		Character oldSeverity = null;
		if(row.getEnumName() != null && !row.getEnumName().isEmpty()) {
			oldSeverity = row.getEnumName().charAt(0);
		}
		if((severity != null && !severity.equals(oldSeverity) || (severity == null && oldSeverity != null))) {
			if(severity != null) {
				row.setEnumName(severity.toString());
			}
			else {
				row.setEnumName(null);
			}
		}

		String value = ((EditText)layout.findViewById(R.id.initial_adjustment_edit)).getText().toString();
		if(value.length() > 0) {
			int intValue = Integer.valueOf(value);
			if(row.getInitialValue() == null || row.getInitialValue() != intValue) {
				row.setInitialValue(intValue);
				changed = true;
			}
		}
		else if(row.getInitialValue() != null) {
			row.setInitialValue(null);
			changed = true;
		}

		value = ((EditText)layout.findViewById(R.id.adjustment_per_tier_edit)).getText().toString();
		if(value.length() > 0) {
			int intValue = Integer.valueOf(value);
			if(row.getValuePer() == null || row.getValuePer() != intValue) {
				row.setValuePer(intValue);
				changed = true;
			}
		}
		else if(row.getValuePer() != null) {
			row.setValuePer(null);
			changed = true;
		}

		if(row.isPerLevel()) {
			row.setPerLevel(false);
			changed = true;
		}

		if(row.isPerRound()) {
			row.setPerRound(false);
			changed = true;
		}

		if(row.isPerTier()) {
			row.setPerTier(false);
			changed = true;
		}

		return changed;
	}
	private void setCriticalSeverityVisibility(final View layout, int visibility) {
		layout.findViewById(R.id.critical_severity_spinner).setVisibility(visibility);
		layout.findViewById(R.id.critical_severity_label).setVisibility(visibility);
		layout.findViewById(R.id.initial_adjustment_label).setVisibility(visibility);
		layout.findViewById(R.id.initial_adjustment_edit).setVisibility(visibility);
		layout.findViewById(R.id.adjustment_per_label).setVisibility(visibility);
		layout.findViewById(R.id.adjustment_per_tier_edit).setVisibility(visibility);
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
}

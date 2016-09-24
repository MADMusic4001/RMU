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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.data.entities.combat.Resistance;
import com.madinnovations.rmu.data.entities.common.Effect;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.data.entities.common.TalentEffectRow;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Talent>, CheckBoxUtils.ValuesCallback,
		EditTextUtils.ValuesCallback {
	private static final String LOG_TAG = "TalentsFragment";
	@Inject
	SkillRxHandler                       skillRxHandler;
	@Inject
	SpecializationRxHandler              specializationRxHandler;
	@Inject
	StatRxHandler                        statRxHandler;
	@Inject
	TalentRxHandler                      talentRxHandler;
	@Inject
	TalentCategoryRxHandler              talentCategoryRxHandler;
	private ArrayAdapter<TalentCategory> categorySpinnerAdapter;
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
	private EditText                     actionPointsEdit;
	private LinearLayout                 effectsList;
	private Talent                       currentInstance   = new Talent();
	private boolean                      isNew             = true;
	private Collection<Skill>            skills            = null;
	private Collection<Specialization>   specializations   = null;
	private Collection<Stat>             stats             = null;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_talent_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_talent_description));

		initCategorySpinner(layout);
		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, R.string.validation_talent_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.description_edit,
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
		actionPointsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.action_points_edit,
												  R.string.validation_talent_action_points_required);
		initAddEffectsButton(layout, inflater);
//		initParametersListView(layout);
//		initSelectedParametersListView(layout);
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
//			case R.id.context_delete_parameter:
//				ParameterValue parameterValue = (ParameterValue) selectedParametersList.getItemAtPosition(info.position);
//				if(parameterValue != null) {
//					selectedParametersListAdapter.remove(parameterValue);
//					selectedParametersListAdapter.notifyDataSetChanged();
//					saveItem();
//					return true;
//				}
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
			case R.id.description_edit:
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
			case R.id.action_points_edit:
				result = String.valueOf(currentInstance.getActionPoints());
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
			case R.id.description_edit:
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
			case R.id.action_points_edit:
				currentInstance.setActionPoints(Short.valueOf(newString));
				saveItem();
				break;
		}
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		short newShort;

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

		if(actionPointsEdit.getText().length() > 0) {
			newShort = Short.valueOf(actionPointsEdit.getText().toString());
			if(newShort != currentInstance.getActionPoints()) {
				currentInstance.setActionPoints(newShort);
				changed = true;
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
		actionPointsEdit.setText(String.valueOf(currentInstance.getActionPoints()));
//		selectedParametersListAdapter.clear();
//		selectedParametersListAdapter.addAll(currentInstance.getParameterValues());
//		selectedParametersListAdapter.notifyDataSetChanged();

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
							Log.e(LOG_TAG, "Exception saving new Talent: " + currentInstance, e);
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
						Log.e(LOG_TAG, "Exception when deleting: " + item, e);
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
						Log.e(LOG_TAG, "Exception caught getting all TalentCategory instances", e);
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

//	private void initAffectedSkillSpinner(View layout) {
//		affectedSkillSpinner = (Spinner)layout.findViewById(R.id.affected_skill_spinner);
//		affectedSkillSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
//		affectedSkillSpinner.setAdapter(affectedSkillSpinnerAdapter);
//
//		skillRxHandler.getAll()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Collection<Skill>>() {
//					@Override
//					public void onCompleted() {}
//					@Override
//					public void onError(Throwable e) {
//						Log.e(LOG_TAG, "Exception caught getting all TalentCategory instances", e);
//					}
//					@Override
//					public void onNext(Collection<Skill> items) {
//						affectedSkillSpinnerAdapter.clear();
//						affectedSkillSpinnerAdapter.addAll(items);
//						affectedSkillSpinnerAdapter.notifyDataSetChanged();
//					}
//				});
//
//		affectedSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				if(currentInstance.getCategory() == null || affectedSkillSpinnerAdapter.getPosition(currentInstance.getAffectedSkill()) != position) {
//					currentInstance.setAffectedSkill(affectedSkillSpinnerAdapter.getItem(position));
//					saveItem();
//				}
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				if(currentInstance.getAffectedSkill() != null) {
//					currentInstance.setAffectedSkill(null);
//					saveItem();
//				}
//			}
//		});
//	}

	private void initAddEffectsButton(final View layout, final LayoutInflater inflater) {
		effectsList = (LinearLayout)layout.findViewById(R.id.effects_list);
		Button button = (Button)layout.findViewById(R.id.add_effect_button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EffectFragment effectFragment = new EffectFragment();
				effectFragment.setEffectsList(effectsList);
				FragmentManager fragmentManager;
				fragmentManager = getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.add(R.id.effects_list, effectFragment);
				transaction.commit();

				int length = currentInstance.getTalentEffectRows().length;
				TalentEffectRow[] rows = new TalentEffectRow[length + 1];
				if(length > 0) {
					System.arraycopy(currentInstance.getTalentEffectRows(), 0, rows, 0, length);
				}
				rows[length] = new TalentEffectRow();
				rows[length].setEffect(Effect.ACTION_POINTS);
				currentInstance.setTalentEffectRows(rows);
				addEffectRow(inflater, length);
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		talentRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {
						if(listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							listAdapter.notifyDataSetChanged();
							copyItemToViews();
						}
					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Talent instances", e);
						Toast.makeText(TalentsFragment.this.getActivity(),
								getString(R.string.toast_talents_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						listAdapter.clear();
						listAdapter.addAll(talents);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_talents_loaded), talents.size());
						Toast.makeText(TalentsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

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

	private void addEffectRow(LayoutInflater inflater, final int index) {
		View effectRow = inflater.inflate(R.layout.talent_effect_row, effectsList, false);
		initEffectSpinner(effectRow, index);
		initEffectEdit(effectRow, index);
		setResistancesVisibility(effectRow, GONE);
		setSkillsVisibility(effectRow, GONE);
		setStatsVisibility(effectRow, GONE);
		effectsList.addView(effectRow);
	}

	private void initEffectSpinner(View parent, final int index) {
		Spinner spinner = (Spinner)parent.findViewById(R.id.effect_spinner);
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
					TalentEffectRow row = currentInstance.getTalentEffectRows()[index];
					if(!effect.equals(row.getEffect())) {
						row.setEffect(effect);
						switch (effect) {
							case ELEMENTAL_RR:
							case FEAR_RR:
							case FOLLOWER_FEAR_RR:
							case MAGICAL_RR:
							case PHYSICAL_RR:
								enableResistances(effectsList, index);
								break;
							case SKILL_BONUS:
								enableSkills(effectsList, index);
								break;
							case SPECIALIZATION_BONUS:
								enableSpecializations(effectsList, index);
							case STAT_BONUS:
								enableStats(effectsList, index);
								break;
							default:
								setResistancesVisibility(effectsList, GONE);
								setSkillsVisibility(effectsList, GONE);
								setSpecializationsVisibility(effectsList, GONE);
								setStatsVisibility(effectsList, GONE);
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
		editText.setText(String.valueOf(currentInstance.getTalentEffectRows()[index].getBonus()));

		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != currentInstance.getTalentEffectRows()[index].getBonus()) {
						currentInstance.getTalentEffectRows()[index].setBonus(newShort);
						saveItem();
					}
				}
			}
		});
	}

	private void enableResistances(View parent, final int index) {
		setValuesVisibility(parent, GONE);
		setSkillsVisibility(parent, GONE);
		setSpecializationsVisibility(parent, GONE);
		setStatsVisibility(parent, GONE);

		parent.findViewById(R.id.resistance_label).setVisibility(View.VISIBLE);
		final Spinner spinner = (Spinner)parent.findViewById(R.id.resistance_spinner);
		spinner.setVisibility(View.VISIBLE);
		final ArrayAdapter<Resistance> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		adapter.clear();
		if(Effect.ELEMENTAL_RR.equals(currentInstance.getTalentEffectRows()[index].getEffect())) {
			adapter.addAll(Resistance.getElementalResistances());
		}
		else if(Effect.FEAR_RR.equals(currentInstance.getTalentEffectRows()[index].getEffect())) {
			adapter.addAll(Resistance.getFearResistances());
		}
		else if(Effect.FOLLOWER_FEAR_RR.equals(currentInstance.getTalentEffectRows()[index].getEffect())) {
			adapter.addAll(Resistance.getFearResistances());
		}
		else if(Effect.MAGICAL_RR.equals(currentInstance.getTalentEffectRows()[index].getEffect())) {
			adapter.addAll(Resistance.getMagicalResistances());
		}
		else if(Effect.PHYSICAL_RR.equals(currentInstance.getTalentEffectRows()[index].getEffect())) {
			adapter.addAll(Resistance.gePhysicalResistances());
		}
		adapter.notifyDataSetChanged();
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(currentInstance.getTalentEffectRows()[index].getAffectedResistance()));
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Resistance resistance = adapter.getItem(position);
				if(resistance != null) {
					if(!resistance.equals(currentInstance.getTalentEffectRows()[index].getAffectedResistance())) {
						currentInstance.getTalentEffectRows()[index].setAffectedResistance(resistance);
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
		editText.setText(String.valueOf(currentInstance.getTalentEffectRows()[index].getBonus()));
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != currentInstance.getTalentEffectRows()[index].getBonus()) {
						currentInstance.getTalentEffectRows()[index].setBonus(newShort);
						saveItem();
					}
				}
			}
		});
	}

	private void enableSkills(View parent, final int index) {
		setValuesVisibility(parent, GONE);
		setResistancesVisibility(parent, GONE);
		setSpecializationsVisibility(parent, GONE);
		setStatsVisibility(parent, GONE);

		parent.findViewById(R.id.skill_label).setVisibility(View.VISIBLE);
		final Spinner spinner = (Spinner)parent.findViewById(R.id.skill_spinner);
		spinner.setVisibility(View.VISIBLE);
		final ArrayAdapter<Skill> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		if(skills != null) {
			adapter.clear();
			adapter.addAll(skills);
			adapter.notifyDataSetChanged();
			spinner.setSelection(adapter.getPosition(currentInstance.getTalentEffectRows()[index].getAffectedSkill()));
		}
		else {
			skillRxHandler.getNonSpecializationSkills()
					.subscribe(new Subscriber<Collection<Skill>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception caught loading all non-specialization Skill instances", e);
						}
						@Override
						public void onNext(Collection<Skill> skillCollection) {
							skills = skillCollection;
							adapter.clear();
							adapter.addAll(skills);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							spinner.setSelection(adapter.getPosition(currentInstance.getTalentEffectRows()[index].getAffectedSkill()));
						}
					});
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Skill skill= adapter.getItem(position);
				if(skill != null) {
					if(!skill.equals(currentInstance.getTalentEffectRows()[index].getAffectedSkill())) {
						currentInstance.getTalentEffectRows()[index].setAffectedSkill(skill);
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		parent.findViewById(R.id.skill_bonus_label).setVisibility(View.VISIBLE);
		final EditText editText = (EditText)parent.findViewById(R.id.skill_bonus_edit);
		editText.setVisibility(View.VISIBLE);
		editText.setText(String.valueOf(currentInstance.getTalentEffectRows()[index].getBonus()));
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != currentInstance.getTalentEffectRows()[index].getBonus()) {
						currentInstance.getTalentEffectRows()[index].setBonus(newShort);
						saveItem();
					}
				}
			}
		});
	}

	private void enableSpecializations(View parent, final int index) {
		setValuesVisibility(parent, GONE);
		setResistancesVisibility(parent, GONE);
		setSkillsVisibility(parent, GONE);
		setStatsVisibility(parent, GONE);

		parent.findViewById(R.id.specialization_label).setVisibility(View.VISIBLE);
		final Spinner spinner = (Spinner)parent.findViewById(R.id.resistance_spinner);
		spinner.setVisibility(View.VISIBLE);
		final ArrayAdapter<Specialization> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		if(specializations != null) {
			adapter.clear();
			adapter.addAll(specializations);
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			spinner.setSelection(adapter.getPosition(currentInstance.getTalentEffectRows()[index].getAffectedSpecialization()));
		}
		else {
			specializationRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Specialization>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception caught getting all Specialization intances", e);
						}
						@Override
						public void onNext(Collection<Specialization> specializationCollection) {
							specializations = specializationCollection;
							adapter.clear();
							adapter.addAll(specializations);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							spinner.setSelection(adapter.getPosition(
									currentInstance.getTalentEffectRows()[index].getAffectedSpecialization()));
						}
					});
		}
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Specialization specialization= adapter.getItem(position);
				if(specialization != null) {
					if(!specialization.equals(currentInstance.getTalentEffectRows()[index].getAffectedSpecialization())) {
						currentInstance.getTalentEffectRows()[index].setAffectedSpecialization(specialization);
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		parent.findViewById(R.id.specialization_bonus_label).setVisibility(View.VISIBLE);
		final EditText editText = (EditText)parent.findViewById(R.id.specialization_bonus_edit);
		editText.setVisibility(View.VISIBLE);
		editText.setText(String.valueOf(currentInstance.getTalentEffectRows()[index].getBonus()));
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != currentInstance.getTalentEffectRows()[index].getBonus()) {
						currentInstance.getTalentEffectRows()[index].setBonus(newShort);
						saveItem();
					}
				}
			}
		});
	}

	private void enableStats(View parent, final int index) {
		setValuesVisibility(parent, GONE);
		setResistancesVisibility(parent, GONE);
		setSkillsVisibility(parent, GONE);
		setSpecializationsVisibility(parent, GONE);

		parent.findViewById(R.id.stat_label).setVisibility(View.VISIBLE);
		final Spinner spinner = (Spinner)parent.findViewById(R.id.stat_spinner);
		spinner.setVisibility(View.VISIBLE);
		final ArrayAdapter<Stat> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		if(stats != null) {
			adapter.clear();
			adapter.addAll(stats);
			adapter.notifyDataSetChanged();
			spinner.setAdapter(adapter);
			spinner.setSelection(adapter.getPosition(currentInstance.getTalentEffectRows()[index].getAffectedStat()));
		}
		else {
			statRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Stat>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception getting all Stat instances", e);
						}
						@Override
						public void onNext(Collection<Stat> statsCollection) {
							stats = statsCollection;
							adapter.clear();
							adapter.addAll(stats);
							adapter.notifyDataSetChanged();
							spinner.setAdapter(adapter);
							spinner.setSelection(adapter.getPosition(
									currentInstance.getTalentEffectRows()[index].getAffectedStat()));
						}
					});
		}
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Stat stat = adapter.getItem(position);
				if(stat != null) {
					if(!stat.equals(currentInstance.getTalentEffectRows()[index].getAffectedStat())) {
						currentInstance.getTalentEffectRows()[index].setAffectedStat(stat);
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		parent.findViewById(R.id.stat_bonus_label).setVisibility(View.VISIBLE);
		final EditText editText = (EditText)parent.findViewById(R.id.stat_bonus_edit);
		editText.setVisibility(View.VISIBLE);
		editText.setText(String.valueOf(currentInstance.getTalentEffectRows()[index].getBonus()));
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(newShort != currentInstance.getTalentEffectRows()[index].getBonus()) {
						currentInstance.getTalentEffectRows()[index].setBonus(newShort);
						saveItem();
					}
				}
			}
		});
	}

	private void setValuesVisibility(View parent, int visibility) {
		parent.findViewById(R.id.value_label).setVisibility(visibility);
		parent.findViewById(R.id.value_edit).setVisibility(visibility);
	}

	private void setResistancesVisibility(View parent, int visibility) {
		parent.findViewById(R.id.resistance_label).setVisibility(visibility);
		parent.findViewById(R.id.resistance_spinner).setVisibility(visibility);
		parent.findViewById(R.id.resistance_value_label).setVisibility(visibility);
		parent.findViewById(R.id.resistance_value_edit).setVisibility(visibility);
	}

	private void setSkillsVisibility(View parent, int visibility) {
		parent.findViewById(R.id.skill_label).setVisibility(visibility);
		parent.findViewById(R.id.skill_spinner).setVisibility(visibility);
		parent.findViewById(R.id.skill_bonus_label).setVisibility(visibility);
		parent.findViewById(R.id.skill_bonus_edit).setVisibility(visibility);
	}

	private void setSpecializationsVisibility(View parent, int visibility) {
		parent.findViewById(R.id.specialization_label).setVisibility(visibility);
		parent.findViewById(R.id.specialization_spinner).setVisibility(visibility);
		parent.findViewById(R.id.specialization_bonus_label).setVisibility(visibility);
		parent.findViewById(R.id.specialization_bonus_edit).setVisibility(visibility);
	}

	private void setStatsVisibility(View parent, int visibility) {
		parent.findViewById(R.id.stat_label).setVisibility(visibility);
		parent.findViewById(R.id.stat_spinner).setVisibility(visibility);
		parent.findViewById(R.id.stat_bonus_label).setVisibility(visibility);
		parent.findViewById(R.id.stat_bonus_edit).setVisibility(visibility);
	}
}

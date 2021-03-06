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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for skills.
 */
public class SkillsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Skill>, EditTextUtils.ValuesCallback,
		CheckBoxUtils.ValuesCallback {
	private static final String TAG = "SkillsFragment";
	@Inject
	protected SkillRxHandler              skillRxHandler;
	@Inject
	protected SkillCategoryRxHandler      skillCategoryRxHandler;
	private   ArrayAdapter<SkillCategory> skillCategoryFilterSpinnerAdapter;
	private   ArrayAdapter<SkillCategory> skillCategorySpinnerAdapter;
	protected ArrayAdapter<Statistic>     stat1SpinnerAdapter;
	protected ArrayAdapter<Statistic>     stat2SpinnerAdapter;
	protected ArrayAdapter<Statistic>     stat3SpinnerAdapter;
	private   TwoFieldListAdapter<Skill>  listAdapter;
	private   Spinner                     skillCategoryFilterSpinner;
	private   ListView                    listView;
	private   EditText                    nameEdit;
	private   EditText                    descriptionEdit;
	private   Spinner                     skillCategorySpinner;
	private   CheckBox                    useCategoryStatsCheckBox;
	private   CheckBox                    requiresSpecializationCheckBox;
	private   CheckBox                    requiresConcentrationCheckBox;
	private   CheckBox                    isLoreCheckBox;
	private   Spinner                     stat1Spinner;
	private   Spinner                     stat2Spinner;
	private   Spinner                     stat3Spinner;
	private Skill currentInstance = new Skill();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.skills_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(R.string.label_skill_name);
		((TextView)layout.findViewById(R.id.header_field2)).setText(R.string.label_skill_description);

		initSkillCategoryFilterSpinner(layout);
		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit, R.string.validation_skill_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
				R.string.validation_skill_description_required);
		initSkillCategorySpinner(layout);
		initUseCategoryStatsCheckBox(layout);
		requiresSpecializationCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.requires_specialization_check_box);
		requiresConcentrationCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.requires_concentration_check_box);
		isLoreCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.is_lore_check_box);
		initStat1Spinner(layout);
		initStat2Spinner(layout);
		initStat3Spinner(layout);
		loadStatSpinners();
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
		inflater.inflate(R.menu.skills_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_skill) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Skill();
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
		getActivity().getMenuInflater().inflate(R.menu.skill_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Skill skill;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_skill:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Skill();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_skill:
				skill = (Skill)listView.getItemAtPosition(info.position);
				if(skill != null) {
					deleteItem(skill);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		if(currentInstance != null) {
			switch (editTextId) {
				case R.id.name_edit:
					result = currentInstance.getName();
					break;
				case R.id.notes_edit:
					result = currentInstance.getDescription();
					break;
			}
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		if(currentInstance != null) {
			switch (editTextId) {
				case R.id.name_edit:
					currentInstance.setName(newString);
					saveItem();
					break;
				case R.id.notes_edit:
					currentInstance.setDescription(newString);
					saveItem();
					break;
			}
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		if(currentInstance != null) {
			switch (checkBoxId) {
				case R.id.requires_specialization_check_box:
					result = currentInstance.isRequiresSpecialization();
					break;
				case R.id.requires_concentration_check_box:
					result = currentInstance.isRequiresConcentration();
					break;
				case R.id.is_lore_check_box:
					result = currentInstance.isLore();
					break;
			}
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		if(currentInstance != null) {
			switch (checkBoxId) {
				case R.id.requires_specialization_check_box:
					currentInstance.setRequiresSpecialization(newBoolean);
					saveItem();
					break;
				case R.id.requires_concentration_check_box:
					currentInstance.setRequiresConcentration(newBoolean);
					saveItem();
					break;
				case R.id.is_lore_check_box:
					currentInstance.setLore(newBoolean);
					saveItem();
					break;
			}
		}
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		Statistic newStat;
		boolean newBoolean;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
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

		if(skillCategorySpinner.getSelectedItemPosition() != -1) {
			SkillCategory skillCategory = skillCategorySpinnerAdapter.getItem(skillCategorySpinner.getSelectedItemPosition());
			if(skillCategory != null && !skillCategory.equals(currentInstance.getCategory())) {
				currentInstance.setCategory(skillCategory);
				changed = true;
			}
		}

		newBoolean = requiresSpecializationCheckBox.isChecked();
		if(newBoolean != currentInstance.isRequiresSpecialization()) {
			currentInstance.setRequiresSpecialization(newBoolean);
			changed = true;
		}

		newBoolean = requiresConcentrationCheckBox.isChecked();
		if(newBoolean != currentInstance.isRequiresConcentration()) {
			currentInstance.setRequiresConcentration(newBoolean);
			changed = true;
		}

		newBoolean = isLoreCheckBox.isChecked();
		if(newBoolean != currentInstance.isLore()) {
			currentInstance.setLore(newBoolean);
			changed = true;
		}

		newBoolean = useCategoryStatsCheckBox.isChecked();
		if(newBoolean != currentInstance.isUseCategoryStats()) {
			currentInstance.setUseCategoryStats(newBoolean);
		}
		if(newBoolean) {
			if(currentInstance.getStats() != null && !currentInstance.getStats().isEmpty()) {
				currentInstance.setStats(null);
				changed = true;
			}
		}
		else {
			List<Statistic> stats = currentInstance.getStats();
			if(stats == null) {
				stats = new ArrayList<>(3);
				changed = true;
			}
			newStat = stat1SpinnerAdapter.getItem(stat1Spinner.getSelectedItemPosition());
			if(stats.size() < 1) {
				stats.add(newStat);
				changed = true;
			}
			else if(!stats.get(0).equals(newStat)) {
				stats.set(0, newStat);
				changed = true;
			}
			newStat = stat2SpinnerAdapter.getItem(stat2Spinner.getSelectedItemPosition());
			if(stats.size() < 2) {
				stats.add(newStat);
				changed = true;
			}
			else if(!stats.get(1).equals(newStat)) {
				stats.set(1, newStat);
				changed = true;
			}
			newStat = stat3SpinnerAdapter.getItem(stat3Spinner.getSelectedItemPosition());
			if(stats.size() < 3) {
				stats.add(newStat);
				changed = true;
			}
			else if(!stats.get(2).equals(newStat)) {
				stats.set(2, newStat);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		if(currentInstance.getCategory() == null) {
			if(skillCategorySpinner.getSelectedItemPosition() != -1) {
				currentInstance.setCategory(skillCategorySpinnerAdapter.getItem(skillCategorySpinner.getSelectedItemPosition()));
			}
		}
		else {
			skillCategorySpinner.setSelection(skillCategorySpinnerAdapter.getPosition(currentInstance.getCategory()));
		}

		requiresSpecializationCheckBox.setChecked(currentInstance.isRequiresSpecialization());
		requiresConcentrationCheckBox.setChecked(currentInstance.isRequiresConcentration());
		isLoreCheckBox.setChecked(currentInstance.isLore());

		useCategoryStatsCheckBox.setChecked(currentInstance.isUseCategoryStats());
		if(currentInstance.isUseCategoryStats()) {
			stat1Spinner.setVisibility(View.GONE);
			stat2Spinner.setVisibility(View.GONE);
			stat3Spinner.setVisibility(View.GONE);
		}
		else {
			stat1Spinner.setVisibility(View.VISIBLE);
			stat2Spinner.setVisibility(View.VISIBLE);
			stat3Spinner.setVisibility(View.VISIBLE);
			copyStatsToSpinners();
		}

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final Skill item) {
		skillRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), R.string.toast_skill_delete_failed, Toast.LENGTH_SHORT).show();
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
								currentInstance = new Skill();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), R.string.toast_skill_deleted, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			skillRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Skill>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving Skill", e);
							Toast.makeText(getActivity(), R.string.toast_skill_save_failed, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Skill savedSkill) {
							if (wasNew) {
								listAdapter.add(savedSkill);
								if(savedSkill == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedSkill));
									listView.setItemChecked(listAdapter.getPosition(savedSkill), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_skill_saved, Toast.LENGTH_SHORT).show();

								int position = listAdapter.getPosition(currentInstance);
								SkillCategory skillCategory = skillCategoryFilterSpinnerAdapter.getItem
										(skillCategoryFilterSpinner.getSelectedItemPosition());
								if(skillCategory != null) {
									if (position >= 0) {
										if (!skillCategory.getName().equals(savedSkill.getCategory().getName())
												&& skillCategory.getId() != -1) {
											listAdapter.remove(savedSkill);
											listAdapter.notifyDataSetChanged();
										}
										else {
											LinearLayout v = (LinearLayout) listView.getChildAt(
													position - listView.getFirstVisiblePosition());
											if (v != null) {
												TextView textView = (TextView) v.findViewById(R.id.row_field1);
												textView.setText(currentInstance.getName());
												textView = (TextView) v.findViewById(R.id.row_field2);
												textView.setText(currentInstance.getDescription());
											}
										}
									}
									else if (skillCategory.equals(savedSkill.getCategory()) && skillCategory.getId() != -1) {
										listAdapter.add(savedSkill);
										listAdapter.notifyDataSetChanged();
									}
								}
							}
						}
					});
		}
	}

	private void initSkillCategoryFilterSpinner(View layout) {
		skillCategoryFilterSpinner = (Spinner)layout.findViewById(R.id.skill_category_filter_spinner);
		skillCategoryFilterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		skillCategoryFilterSpinner.setAdapter(skillCategoryFilterSpinnerAdapter);

		final SkillCategory allSkillCategories = new SkillCategory();
		allSkillCategories.setName(getString(R.string.label_all_skill_categories));
		skillCategoryFilterSpinnerAdapter.add(allSkillCategories);
		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> skillCategories) {
						skillCategoryFilterSpinnerAdapter.addAll(skillCategories);
						skillCategoryFilterSpinnerAdapter.notifyDataSetChanged();
						skillCategoryFilterSpinner.setSelection(
								skillCategoryFilterSpinnerAdapter.getPosition(allSkillCategories));
					}
				});

		skillCategoryFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredSkills(skillCategoryFilterSpinnerAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredSkills(null);
			}
		});
	}

	private void initSkillCategorySpinner(View layout) {
		skillCategorySpinner = (Spinner)layout.findViewById(R.id.skill_category_spinner);
		skillCategorySpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		skillCategorySpinner.setAdapter(skillCategorySpinnerAdapter);

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> skillCategories) {
						skillCategorySpinnerAdapter.addAll(skillCategories);
						skillCategorySpinnerAdapter.notifyDataSetChanged();
						if(currentInstance.getCategory() != null) {
							skillCategorySpinner.setSelection(
									skillCategorySpinnerAdapter.getPosition(currentInstance.getCategory()));
						}
					}
				});

		skillCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				if(currentInstance.getCategory() == null ||
						skillCategorySpinnerAdapter.getPosition(currentInstance.getCategory()) != position) {
					currentInstance.setCategory(skillCategorySpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getCategory() != null) {
					currentInstance.setCategory(null);
				}
			}
		});
	}

	private void initUseCategoryStatsCheckBox(View layout) {
		useCategoryStatsCheckBox = (CheckBox)layout.findViewById(R.id.use_cat_stats_check_box);
		useCategoryStatsCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(useCategoryStatsCheckBox.isChecked() != currentInstance.isUseCategoryStats()) {
					if (useCategoryStatsCheckBox.isChecked()) {
						stat1Spinner.setVisibility(View.GONE);
						stat2Spinner.setVisibility(View.GONE);
						stat3Spinner.setVisibility(View.GONE);
					} else {
						stat1Spinner.setVisibility(View.VISIBLE);
						stat2Spinner.setVisibility(View.VISIBLE);
						stat3Spinner.setVisibility(View.VISIBLE);
						copyStatsToSpinners();
					}
					currentInstance.setUseCategoryStats(useCategoryStatsCheckBox.isChecked());
					saveItem();
				}
			}
		});
	}

//	private void initRequiresSpecializationCheckBox(View layout) {
//		requiresSpecializationCheckBox = (CheckBox)layout.findViewById(R.id.requires_specialization_check_box);
//		requiresSpecializationCheckBox.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(requiresSpecializationCheckBox.isChecked() != currentInstance.isRequiresSpecialization()) {
//					currentInstance.setRequiresSpecialization(requiresSpecializationCheckBox.isChecked());
//					saveItem();
//				}
//			}
//		});
//	}

	private void initStat1Spinner(View layout) {
		stat1Spinner = (Spinner)layout.findViewById(R.id.stat1_spinner);
		stat1SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		stat1Spinner.setAdapter(stat1SpinnerAdapter);

		stat1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Statistic> stats = currentInstance.getStats();
				if(stats == null || stats.isEmpty() || stat1SpinnerAdapter.getPosition(stats.get(0)) != position) {
					if(stats == null) {
						stats = new ArrayList<>(3);
					}
					if(stats.isEmpty()) {
						stats.add(stat1SpinnerAdapter.getItem(position));
					}
					else {
						stats.set(0, stat1SpinnerAdapter.getItem(position));
					}
					currentInstance.setStats(stats);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStats() != null && !currentInstance.getStats().isEmpty()) {
					currentInstance.getStats().remove(0);
					saveItem();
				}
			}
		});
	}

	private void initStat2Spinner(View layout) {
		stat2Spinner = (Spinner)layout.findViewById(R.id.stat2_spinner);
		stat2SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		stat2Spinner.setAdapter(stat2SpinnerAdapter);

		stat2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Statistic> stats = currentInstance.getStats();
				if(stats != null && (stats.size() == 1 ||
						(stats.size() > 1 && stat2SpinnerAdapter.getPosition(stats.get(1)) != position))) {
					if(stats.size() == 1) {
						stats.add(stat2SpinnerAdapter.getItem(position));
					}
					else {
						stats.set(1, stat2SpinnerAdapter.getItem(position));
					}
					currentInstance.setStats(stats);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStats() != null && currentInstance.getStats().size() > 1) {
					currentInstance.getStats().remove(1);
					saveItem();
				}
			}
		});
	}

	private void initStat3Spinner(View layout) {
		stat3Spinner = (Spinner)layout.findViewById(R.id.stat3_spinner);
		stat3SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		stat3Spinner.setAdapter(stat3SpinnerAdapter);

		stat3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Statistic> stats = currentInstance.getStats();
				if(stats != null && (stats.size() == 2 || (stats.size() == 3 &&
						stat3SpinnerAdapter.getPosition(stats.get(2)) != position))) {
					if(stats.size() == 2) {
						stats.add(stat3SpinnerAdapter.getItem(position));
					}
					else {
						stats.set(2, stat3SpinnerAdapter.getItem(position));
					}
					currentInstance.setStats(stats);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStats() != null && currentInstance.getStats().size() == 3) {
					currentInstance.getStats().remove(2);
					saveItem();
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		loadFilteredSkills(null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Skill) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Skill();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void loadStatSpinners() {
//		statRxHandler.getAll()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Collection<Stat>>() {
//					@Override
//					public void onCompleted() {}
//					@Override
//					public void onError(Throwable e) {
//						Log.e(TAG, "Exception caught getting all Stat instances", e);
//					}
//					@Override
//					public void onNext(Collection<Stat> items) {
//						stat1SpinnerAdapter.clear();
//						stat1SpinnerAdapter.addAll(items);
//						stat1SpinnerAdapter.notifyDataSetChanged();
//
//						stat2SpinnerAdapter.clear();
//						stat2SpinnerAdapter.addAll(items);
//						stat2SpinnerAdapter.notifyDataSetChanged();
//
//						stat3SpinnerAdapter.clear();
//						stat3SpinnerAdapter.addAll(items);
//						stat3SpinnerAdapter.notifyDataSetChanged();
//					}
//				});
		stat1SpinnerAdapter.clear();
		stat1SpinnerAdapter.addAll(Statistic.values());
		stat1SpinnerAdapter.notifyDataSetChanged();

		stat2SpinnerAdapter.clear();
		stat2SpinnerAdapter.addAll(Statistic.values());
		stat2SpinnerAdapter.notifyDataSetChanged();

		stat3SpinnerAdapter.clear();
		stat3SpinnerAdapter.addAll(Statistic.values());
		stat3SpinnerAdapter.notifyDataSetChanged();
	}

	private void copyStatsToSpinners() {
		List<Statistic> stats = currentInstance.getStats();
		if(stats == null) {
			stats = new ArrayList<>(3);
		}
		setStatSpinnerValue(stats, stat1Spinner, stat1SpinnerAdapter, 0);
		setStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
		setStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
		currentInstance.setStats(stats);
	}

	private void setStatSpinnerValue(List<Statistic> stats, Spinner spinner, ArrayAdapter<Statistic> adapter, int statIndex) {
		int position;

		if(stats.size() <= statIndex) {
			stats.add(null);
		}

		if (stats.get(statIndex) != null) {
			spinner.setSelection(adapter.getPosition(stats.get(statIndex)));
		} else {
			position = spinner.getSelectedItemPosition();
			if (position != -1) {
				stats.set(statIndex, adapter.getItem(position));
			}
			else {
				stats.set(statIndex, adapter.getItem(0));
				spinner.setSelection(0);
			}
		}
	}

	private void loadFilteredSkills(final SkillCategory filter) {
		Observable<Collection<Skill>> observable;

		if(filter == null || filter.getId() == -1) {
			observable = skillRxHandler.getAll();
		}
		else {
			observable = skillRxHandler.getSkillsForCategory(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Skill>>() {
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
						Log.e(TAG, "Exception caught getting all Skill instances", e);
						Toast.makeText(SkillsFragment.this.getActivity(),
								R.string.toast_skills_load_failed,
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						listAdapter.clear();
						listAdapter.addAll(skills);
						listAdapter.notifyDataSetChanged();
						if(filter == null) {
							String toastString;
							toastString = String.format(getString(R.string.toast_skills_loaded), skills.size());
							Toast.makeText(SkillsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	public CharSequence getField1Value(Skill skill) {
		return skill.getName();
	}

	@Override
	public CharSequence getField2Value(Skill skill) {
		return skill.getDescription();
	}
}

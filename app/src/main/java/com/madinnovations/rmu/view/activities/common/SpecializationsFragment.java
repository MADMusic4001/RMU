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
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.SpecializationListAdapter;
import com.madinnovations.rmu.view.adapters.common.StatSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for specializations.
 */
public class SpecializationsFragment extends Fragment {
	@Inject
	protected SpecializationRxHandler specializationRxHandler;
	@Inject
	protected SkillRxHandler              skillRxHandler;
	@Inject
	protected StatRxHandler               statRxHandler;
	@Inject
	protected SpecializationListAdapter   listAdapter;
	@Inject
	protected SkillSpinnerAdapter         skillFilterSpinnerAdapter;
	@Inject
	protected SkillSpinnerAdapter         skillSpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter          stat1SpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter          stat2SpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter          stat3SpinnerAdapter;
	private   Spinner                     skillFilterSpinner;
	private   ListView                    listView;
	private   EditText                    nameEdit;
	private   EditText                    descriptionEdit;
	private   Spinner                     skillSpinner;
	private   CheckBox                    useSkillStatsCheckBox;
	private   Spinner                     stat1Spinner;
	private   Spinner                     stat2Spinner;
	private   Spinner                     stat3Spinner;
	private Specialization currentInstance = new Specialization();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.specializations_fragment, container, false);

		initSkillFilterSpinner(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);
		initSkillSpinner(layout);
		initUseSkillStatsCheckBox(layout);
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
		inflater.inflate(R.menu.specializations_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_specialization) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Specialization();
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
		getActivity().getMenuInflater().inflate(R.menu.specialization_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Specialization specialization;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_specialization:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Specialization();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_specialization:
				specialization = (Specialization)listView.getItemAtPosition(info.position);
				if(specialization != null) {
					deleteItem(specialization);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		Stat newStat;
		boolean newBoolean;

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

		if(skillSpinner.getSelectedItemPosition() != -1) {
			Skill skill = skillSpinnerAdapter.getItem(skillSpinner.getSelectedItemPosition());
			if(!skill.equals(currentInstance.getSkill())) {
				currentInstance.setSkill(skill);
				changed = true;
			}
		}

		newBoolean = useSkillStatsCheckBox.isChecked();
		if(newBoolean != currentInstance.isUseSkillStats()) {
			currentInstance.setUseSkillStats(newBoolean);
		}
		if(newBoolean) {
			if(currentInstance.getStats() != null && !currentInstance.getStats().isEmpty()) {
				currentInstance.setStats(null);
				changed = true;
			}
		}
		else {
			List<Stat> stats = currentInstance.getStats();
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
		if(currentInstance.getSkill() == null) {
			if(skillSpinner.getSelectedItemPosition() != -1) {
				currentInstance.setSkill(skillSpinnerAdapter.getItem(skillSpinner.getSelectedItemPosition()));
			}
		}
		else {
			skillSpinner.setSelection(skillSpinnerAdapter.getPosition(currentInstance.getSkill()));
		}

		useSkillStatsCheckBox.setChecked(currentInstance.isUseSkillStats());
		if(currentInstance.isUseSkillStats()) {
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

	private void deleteItem(final Specialization item) {
		specializationRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpecializationFragment", "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), getString(R.string.toast_specialization_delete_failed), Toast.LENGTH_SHORT).show();
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
								currentInstance = new Specialization();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_specialization_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			specializationRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Specialization>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("SpecializationsFragment", "Exception saving Specialization", e);
							String toastString = getString(R.string.toast_specialization_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Specialization savedSpecialization) {
							if (wasNew) {
								listAdapter.add(savedSpecialization);
								if(savedSpecialization == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedSpecialization));
									listView.setItemChecked(listAdapter.getPosition(savedSpecialization), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								String toastString;
								toastString = getString(R.string.toast_specialization_saved);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

								int position = listAdapter.getPosition(currentInstance);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.name_view);
									if (textView != null) {
										textView.setText(currentInstance.getName());
									}
									textView = (TextView) v.findViewById(R.id.description_view);
									if (textView != null) {
										textView.setText(currentInstance.getDescription());
									}
								}
							}
						}
					});
		}
	}

	private void initSkillFilterSpinner(View layout) {
		skillFilterSpinner = (Spinner)layout.findViewById(R.id.skill_filter_spinner);
		skillFilterSpinner.setAdapter(skillFilterSpinnerAdapter);

		final Skill allSkills = new Skill();
		allSkills.setName(getString(R.string.label_all_skills));
		skillFilterSpinnerAdapter.add(allSkills);
		skillRxHandler.getSpecializationSkills()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpecializationsFragment", "Exception caught getting all specialization Skill instances", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						skillFilterSpinnerAdapter.addAll(skills);
						skillFilterSpinnerAdapter.notifyDataSetChanged();
						skillFilterSpinner.setSelection(skillFilterSpinnerAdapter.getPosition(allSkills));
					}
				});

		skillFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredSpecializations(skillFilterSpinnerAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredSpecializations(null);
			}
		});
	}

	private void initNameEdit(View layout) {
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (currentInstance != null && !newName.equals(currentInstance.getName())) {
						currentInstance.setName(newName);
						saveItem();
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (currentInstance != null && !newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initSkillSpinner(View layout) {
		skillSpinner = (Spinner)layout.findViewById(R.id.skill_spinner);
		skillSpinner.setAdapter(skillSpinnerAdapter);

		skillRxHandler.getSpecializationSkills()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpecializationsFragment", "Exception caught getting all specialization Skill instances", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						skillSpinnerAdapter.addAll(skills);
						skillSpinnerAdapter.notifyDataSetChanged();
						if(currentInstance.getSkill() != null) {
							skillSpinner.setSelection(skillSpinnerAdapter.getPosition(currentInstance.getSkill()));
						}
					}
				});

		skillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				if(currentInstance.getSkill() == null ||
						skillSpinnerAdapter.getPosition(currentInstance.getSkill()) != position) {
					currentInstance.setSkill(skillSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSkill() != null) {
					currentInstance.setSkill(null);
				}
			}
		});
	}

	private void initUseSkillStatsCheckBox(View layout) {
		useSkillStatsCheckBox = (CheckBox)layout.findViewById(R.id.use_skill_stats_check_box);
		useSkillStatsCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(useSkillStatsCheckBox.isChecked() != currentInstance.isUseSkillStats()) {
					if (useSkillStatsCheckBox.isChecked()) {
						stat1Spinner.setVisibility(View.GONE);
						stat2Spinner.setVisibility(View.GONE);
						stat3Spinner.setVisibility(View.GONE);
					} else {
						stat1Spinner.setVisibility(View.VISIBLE);
						stat2Spinner.setVisibility(View.VISIBLE);
						stat3Spinner.setVisibility(View.VISIBLE);
						copyStatsToSpinners();
					}
					currentInstance.setUseSkillStats(useSkillStatsCheckBox.isChecked());
					saveItem();
				}
			}
		});
	}

	private void initStat1Spinner(View layout) {
		stat1Spinner = (Spinner)layout.findViewById(R.id.stat1_spinner);
		stat1Spinner.setAdapter(stat1SpinnerAdapter);

		stat1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Stat> stats = currentInstance.getStats();
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
		stat2Spinner.setAdapter(stat2SpinnerAdapter);

		stat2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Stat> stats = currentInstance.getStats();
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
		stat3Spinner.setAdapter(stat3SpinnerAdapter);

		stat3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Stat> stats = currentInstance.getStats();
				if(stats != null && (stats.size() == 2 || (stats.size() == 3 && stat3SpinnerAdapter.getPosition(stats.get(2)) != position))) {
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

		listView.setAdapter(listAdapter);

		loadFilteredSpecializations(null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Specialization) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Specialization();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void loadStatSpinners() {
		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SkillCategoriesFrag", "Exception caught getting all Stat instances", e);
					}
					@Override
					public void onNext(Collection<Stat> items) {
						stat1SpinnerAdapter.clear();
						stat1SpinnerAdapter.addAll(items);
						stat1SpinnerAdapter.notifyDataSetChanged();

						stat2SpinnerAdapter.clear();
						stat2SpinnerAdapter.addAll(items);
						stat2SpinnerAdapter.notifyDataSetChanged();

						stat3SpinnerAdapter.clear();
						stat3SpinnerAdapter.addAll(items);
						stat3SpinnerAdapter.notifyDataSetChanged();
					}
				});

	}

	private void copyStatsToSpinners() {
		List<Stat> stats = currentInstance.getStats();
		if(stats == null) {
			stats = new ArrayList<>(3);
		}
		setStatSpinnerValue(stats, stat1Spinner, stat1SpinnerAdapter, 0);
		setStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
		setStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
		currentInstance.setStats(stats);
	}

	private void setStatSpinnerValue(List<Stat> stats, Spinner spinner, StatSpinnerAdapter adapter, int statIndex) {
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

	private void loadFilteredSpecializations(final Skill filter) {
		Observable<Collection<Specialization>> observable;

		if(filter == null || filter.getId() == -1) {
			observable = specializationRxHandler.getAll();
		}
		else {
			observable = specializationRxHandler.getSpecializationsForSkill(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Specialization>>() {
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
						Log.e("SpecializationsFragment", "Exception caught getting all Specialization instances", e);
						Toast.makeText(SpecializationsFragment.this.getActivity(),
								getString(R.string.toast_specializations_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Specialization> specializations) {
						listAdapter.clear();
						listAdapter.addAll(specializations);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_specializations_loaded), specializations.size());
						Toast.makeText(SpecializationsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});
	}
}

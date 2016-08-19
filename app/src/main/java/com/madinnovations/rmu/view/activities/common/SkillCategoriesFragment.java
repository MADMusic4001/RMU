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
import android.view.MotionEvent;
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
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillCategoryListAdapter;
import com.madinnovations.rmu.view.adapters.common.StatSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for skill categories.
 */
public class SkillCategoriesFragment extends Fragment {
	@Inject
	protected SkillCategoryRxHandler skillCategoryRxHandler;
	@Inject
	protected StatRxHandler statRxHandler;
	@Inject
	protected SkillCategoryListAdapter listAdapter;
	@Inject
	protected StatSpinnerAdapter stat1SpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter stat2SpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter stat3SpinnerAdapter;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private CheckBox noStatsCheckBox;
	private CheckBox realmStatsCheckBox;
	private Spinner stat1Spinner;
	private Spinner stat2Spinner;
	private Spinner stat3Spinner;
	private SkillCategory currentInstance = new SkillCategory();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.skill_categories_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initNoStatsCheckBox(layout);
		initRealmStatsCheckBox(layout);
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
		inflater.inflate(R.menu.skill_categories_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_skill_category) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new SkillCategory();
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
		getActivity().getMenuInflater().inflate(R.menu.skill_category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final SkillCategory skillCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_skill_category:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new SkillCategory();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_skill_category:
				skillCategory = (SkillCategory) listView.getItemAtPosition(info.position);
				if(skillCategory != null) {
					deleteItem(skillCategory);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		Stat newStat;
		int position;
		List<Stat> stats;

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getName() != null) ||
				(newString != null && !newString.equals(currentInstance.getName()))) {
			currentInstance.setName(newString);
			changed = true;
		}

		newString = descriptionEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getDescription() != null) ||
				(newString != null && !newString.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newString);
			changed = true;
		}

		// If checkbox does not match value in entity then update value in entity
		boolean newNoStats = noStatsCheckBox.isChecked();
		if(currentInstance.isNoStats() != newNoStats) {
			currentInstance.setNoStats(newNoStats);
			changed = true;
		}
		// If checkbox is checked then set stats list to null
		if(newNoStats) {
			// If current instance says to use realm stats
			if(currentInstance.isRealmStats()) {
				currentInstance.setRealmStats(false);
				changed = true;
			}
			if(currentInstance.getStats() != null) {
				currentInstance.setStats(null);
				changed = true;
			}
			realmStatsCheckBox.setVisibility(View.GONE);
			stat1Spinner.setVisibility(View.GONE);
			stat2Spinner.setVisibility(View.GONE);
			stat3Spinner.setVisibility(View.GONE);
		}
		else {
			boolean newRealmStats = realmStatsCheckBox.isChecked();
			if(currentInstance.isRealmStats() != newRealmStats) {
				currentInstance.setRealmStats(newRealmStats);
				changed = true;
			}
			stats = currentInstance.getStats();
			if(newRealmStats) {
				if(stats != null && stats.size() != 1) {
					while(stats.size() > 1 ) {
						stats.remove(1);
						changed = true;
					}
				}
				else if(stats == null) {
					stats = new ArrayList<>(1);
				}
				position = stat1Spinner.getSelectedItemPosition();
				if(position != -1) {
					newStat = stat1SpinnerAdapter.getItem(position);
					if(stats.isEmpty() || !newStat.equals(stats.get(0))) {
						stats.add(0, newStat);
						changed = true;
					}
				}
				else {
					if(!stats.isEmpty()) {
						changed = true;
					}
					stats = null;
				}
				currentInstance.setStats(stats);
			}
			else {
				int currentIndex = 0;
				if(stats == null) {
					stats = new ArrayList<>(3);
				}

				newStat = null;
				position = stat1Spinner.getSelectedItemPosition();
				if (position >= 0) {
					newStat = stat1SpinnerAdapter.getItem(position);
				}
				changed |= setStat(stats, newStat, currentIndex++);

				newStat = null;
				position = stat2Spinner.getSelectedItemPosition();
				if (position >= 0) {
					newStat = stat2SpinnerAdapter.getItem(position);
				}
				changed |= setStat(stats, newStat, currentIndex++);

				newStat = null;
				position = stat3Spinner.getSelectedItemPosition();
				if (position >= 0) {
					newStat = stat3SpinnerAdapter.getItem(position);
				}
				changed |= setStat(stats, newStat, currentIndex);

				if(stats.size() == 0) {
					stats = null;
				}
				currentInstance.setStats(stats);
			}
		}

		return changed;
	}

	private boolean setStat(List<Stat> stats, Stat newStat, int currentIndex) {
		boolean changed = false;

		if (newStat != null) {
			if (stats.size() <= currentIndex) {
				stats.add(currentIndex, newStat);
				changed = true;
			}
			else if(!newStat.equals(stats.get(currentIndex))) {
				stats.set(currentIndex, newStat);
				changed = true;
			}
		}
		else {
			stats.add(currentIndex, null);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		noStatsCheckBox.setChecked(currentInstance.isNoStats());
		if(currentInstance.isNoStats()) {
			realmStatsCheckBox.setVisibility(View.GONE);
			stat1Spinner.setVisibility(View.GONE);
			stat2Spinner.setVisibility(View.GONE);
			stat3Spinner.setVisibility(View.GONE);
		}
		else {
			realmStatsCheckBox.setVisibility(View.VISIBLE);
			realmStatsCheckBox.setChecked(currentInstance.isRealmStats());
			List<Stat> stats = currentInstance.getStats() != null ? currentInstance.getStats() : new ArrayList<Stat>(1);
			stat1Spinner.setVisibility(View.VISIBLE);
			setStatSpinnerValue(stats, stat1Spinner, stat1SpinnerAdapter, 0);
			if(currentInstance.isRealmStats()) {
				stat2Spinner.setVisibility(View.GONE);
				stat3Spinner.setVisibility(View.GONE);
			}
			else {
				stat2Spinner.setVisibility(View.VISIBLE);
				setStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
				stat3Spinner.setVisibility(View.VISIBLE);
				setStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
			}
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
			skillCategoryRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<SkillCategory>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("SkillCategoriesFrag", "Exception saving new SkillCategory: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_skill_category_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(SkillCategory savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_skill_category_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.name_view);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.description_view);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final SkillCategory item) {
		skillCategoryRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SkillCategoriesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_skill_category_delete_failed);
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
								currentInstance = new SkillCategory();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_skill_category_deleted), Toast.LENGTH_SHORT).show();
						}
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
					nameEdit.setError(getString(R.string.validation_skill_category_name_required));
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
					descriptionEdit.setError(getString(R.string.validation_skill_category_description_required));
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

	private void initNoStatsCheckBox(View layout) {
		noStatsCheckBox = (CheckBox)layout.findViewById(R.id.no_stats_check_box);
		noStatsCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(noStatsCheckBox.isChecked() != currentInstance.isNoStats()) {
					if (noStatsCheckBox.isChecked()) {
						realmStatsCheckBox.setVisibility(View.GONE);
						currentInstance.setRealmStats(false);
						stat1Spinner.setVisibility(View.GONE);
						stat2Spinner.setVisibility(View.GONE);
						stat3Spinner.setVisibility(View.GONE);
						currentInstance.setStats(null);
					} else {
						realmStatsCheckBox.setVisibility(View.VISIBLE);
						realmStatsCheckBox.setChecked(currentInstance.isRealmStats());
						stat1Spinner.setVisibility(View.VISIBLE);
						List<Stat> stats = currentInstance.getStats() != null ? currentInstance.getStats() : new ArrayList<Stat>(1);
						setStatSpinnerValue(stats, stat1Spinner, stat1SpinnerAdapter, 0);
						if (realmStatsCheckBox.isChecked()) {
							stat2Spinner.setVisibility(View.GONE);
							stat3Spinner.setVisibility(View.GONE);
						}
						else {
							((ArrayList)stats).ensureCapacity(3);
							stat2Spinner.setVisibility(View.VISIBLE);
							setStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
							stat3Spinner.setVisibility(View.VISIBLE);
							setStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
						}
						currentInstance.setStats(stats);
					}
					currentInstance.setNoStats(noStatsCheckBox.isChecked());
					saveItem();
				}
			}
		});
	}

	private void initRealmStatsCheckBox(View layout) {
		realmStatsCheckBox = (CheckBox)layout.findViewById(R.id.realm_stats_check_box);
		realmStatsCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(realmStatsCheckBox.isChecked() != currentInstance.isRealmStats()) {
					if(realmStatsCheckBox.isChecked()) {
						stat2Spinner.setVisibility(View.GONE);
						stat3Spinner.setVisibility(View.GONE);
						List<Stat> stats = currentInstance.getStats();
						while(stats != null && stats.size() > 1) {
							stats.remove(1);
						}
					}
					else {
						List<Stat> stats = currentInstance.getStats() != null ? currentInstance.getStats() : new ArrayList<Stat>(3);
						((ArrayList)stats).ensureCapacity(3);
						stat2Spinner.setVisibility(View.VISIBLE);
						setStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
						stat3Spinner.setVisibility(View.VISIBLE);
						setStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
						currentInstance.setStats(stats);
					}
					currentInstance.setRealmStats(realmStatsCheckBox.isChecked());
					saveItem();
				}
			}
		});

	}

	private void initStat1Spinner(View layout) {
		stat1Spinner = (Spinner)layout.findViewById(R.id.stat1_spinner);
		stat1Spinner.setAdapter(stat1SpinnerAdapter);

		stat1Spinner.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_UP && !view.hasFocus()) {
					view.performClick();
				}
				return false;
			}
		});
		stat1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				List<Stat> stats = currentInstance.getStats();
				if(stats == null || stats.isEmpty() || stat1SpinnerAdapter.getPosition(stats.get(0)) != position) {
					copySpinnersToStats();
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
				copySpinnersToStats();
				saveItem();
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
				copySpinnersToStats();
				saveItem();
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

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
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
						Log.e("SkillCategoriesFrag", "Exception caught getting all SkillCategory instances", e);
						Toast.makeText(SkillCategoriesFragment.this.getActivity(),
								getString(R.string.toast_skill_categories_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<SkillCategory> skillCategories) {
						listAdapter.clear();
						listAdapter.addAll(skillCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_skill_categories_loaded), skillCategories.size());
						Toast.makeText(SkillCategoriesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (SkillCategory) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new SkillCategory();
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

	private boolean copySpinnersToStats() {
		boolean changed;

		List<Stat> stats = currentInstance.getStats();
		if(stats == null) {
			stats = new ArrayList<>(3);
		}
		changed = getStatSpinnerValue(stats, stat1Spinner, stat1SpinnerAdapter, 0);
		changed |= getStatSpinnerValue(stats, stat2Spinner, stat2SpinnerAdapter, 1);
		changed |= getStatSpinnerValue(stats, stat3Spinner, stat3SpinnerAdapter, 2);
		if(changed) {
			currentInstance.setStats(stats);
		}

		return changed;
	}

	private boolean getStatSpinnerValue(List<Stat> stats, Spinner spinner, StatSpinnerAdapter adapter, int statIndex) {
		boolean changed = false;
		int position;
		Stat newStat = null;

		if(spinner.getVisibility() != View.VISIBLE) {
			if(stats.size() >= statIndex + 1) {
				stats.remove(statIndex);
				changed = true;
			}
		}
		else {
			position = spinner.getSelectedItemPosition();
			if (position >= 0) {
				newStat = adapter.getItem(position);
			}
			if (stats.size() >= statIndex + 1 && stats.get(statIndex) != null) {
				if (!stats.get(statIndex).equals(newStat) && newStat != null) {
					stats.set(statIndex, newStat);
					changed = true;
				}
			} else if (stats.size() < statIndex + 1 || stats.get(statIndex) == null) {
				if (position >= 0) {
					if (stats.size() < statIndex + 1) {
						stats.add(newStat);
					} else {
						stats.set(statIndex, newStat);
					}
					changed = true;
				} else if (adapter.getCount() > 0) {
					newStat = adapter.getItem(0);
					if (stats.size() >= statIndex + 1) {
						stats.set(statIndex, newStat);
					} else {
						stats.add(newStat);
					}
					changed = true;
				}
			}
		}
		return changed;
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
			else if(adapter.getCount() > 0) {
				stats.set(statIndex, adapter.getItem(0));
				spinner.setSelection(0);
			}
		}
	}
}

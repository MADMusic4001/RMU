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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.utils.CheckBoxUtils;
import com.madinnovations.rmu.view.utils.EditTextUtils;
import com.madinnovations.rmu.view.utils.SpinnerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for skill categories.
 */
public class SkillCategoriesFragment extends Fragment implements TwoFieldListAdapter.GetValues<SkillCategory>,
		EditTextUtils.ValuesCallback, CheckBoxUtils.ValuesCallback, SpinnerUtils.ValuesCallback {
	@Inject
	protected SkillCategoryRxHandler skillCategoryRxHandler;
	private TwoFieldListAdapter<SkillCategory> listAdapter = null;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private CheckBox tradesAndCraftsCheckBox;
	private CheckBox combatCheckBox;
	private CheckBox noStatsCheckBox;
	private CheckBox realmStatsCheckBox;
	private Spinner  stat1Spinner;
	private Spinner  stat2Spinner;
	private Spinner  stat3Spinner;
	private SkillCategory currentInstance = new SkillCategory();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.skill_categories_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_skill_category_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_skill_category_description));

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
										  R.string.validation_skill_category_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
												 R.string.validation_skill_category_description_required);
		tradesAndCraftsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.trades_and_crafts_check_box);
		combatCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.combat_check_box);
		noStatsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.no_stats_check_box);
		realmStatsCheckBox = CheckBoxUtils.initCheckBox(layout, this, R.id.realm_stats_check_box);
		stat1Spinner = new SpinnerUtils<Statistic>().initSpinner(layout, getActivity(),
																 Arrays.asList(Statistic.getAllStats()),
																 this,
																 R.id.stat1_spinner, null);
		stat2Spinner = new SpinnerUtils<Statistic>().initSpinner(layout, getActivity(),
																 Arrays.asList(Statistic.getAllStats()),
																 this,
																 R.id.stat2_spinner, null);
		stat3Spinner = new SpinnerUtils<Statistic>().initSpinner(layout, getActivity(),
																 Arrays.asList(Statistic.getAllStats()),
																 this,
																 R.id.stat3_spinner, null);
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

	@Override
	public boolean getValueForCheckBox(@IdRes int checkBoxId) {
		boolean result = false;

		switch (checkBoxId) {
			case R.id.combat_check_box:
				result = currentInstance.isCombat();
				break;
			case R.id.trades_and_crafts_check_box:
				result = currentInstance.isCraftAndTrade();
				break;
			case R.id.no_stats_check_box:
				result = currentInstance.isNoStats();
				break;
			case R.id.realm_stats_check_box:
				result = currentInstance.isRealmStats();
				break;
		}

		return result;
	}

	@Override
	public void setValueFromCheckBox(@IdRes int checkBoxId, boolean newBoolean) {
		switch (checkBoxId) {
			case R.id.combat_check_box:
				currentInstance.setCombat(newBoolean);
				saveItem();
				break;
			case R.id.trades_and_crafts_check_box:
				currentInstance.setCraftAndTrade(newBoolean);
				saveItem();
				break;
			case R.id.no_stats_check_box:
				currentInstance.setNoStats(newBoolean);
				int visibility = newBoolean ? View.GONE : View.VISIBLE;
				stat1Spinner.setVisibility(visibility);
				stat2Spinner.setVisibility(visibility);
				stat3Spinner.setVisibility(visibility);
				saveItem();
				break;
			case R.id.realm_stats_check_box:
				currentInstance.setRealmStats(newBoolean);
				visibility = newBoolean ? View.GONE : View.VISIBLE;
				stat2Spinner.setVisibility(visibility);
				stat3Spinner.setVisibility(visibility);
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
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		switch (editTextId) {
			case R.id.name_edit:
				currentInstance.setName(newString);
				break;
			case R.id.notes_edit:
				currentInstance.setDescription(newString);
				break;
		}
	}

	@Override
	public Object getValueForSpinner(@IdRes int spinnerId) {
		Statistic result = null;

		switch (spinnerId) {
			case R.id.stat1_spinner:
				result = currentInstance.getStats().size() > 0 ? currentInstance.getStats().get(0) : null;
				break;
			case R.id.stat2_spinner:
				result = currentInstance.getStats().size() > 1 ? currentInstance.getStats().get(1) : null;
				break;
			case R.id.stat3_spinner:
				result = currentInstance.getStats().size() > 2 ? currentInstance.getStats().get(2) : null;
				break;
		}

		return result;
	}

	@Override
	public void setValueFromSpinner(@IdRes int spinnerId, Object newItem) {
		switch (spinnerId) {
			case R.id.stat1_spinner:
				if(currentInstance.getStats().size() == 0) {
					currentInstance.getStats().add((Statistic)newItem);
				}
				else {
					currentInstance.getStats().set(0, (Statistic)newItem);
				}
				break;
			case R.id.stat2_spinner:
				while(currentInstance.getStats().size() < 2) {
					currentInstance.getStats().add(null);
				}
				currentInstance.getStats().set(1, (Statistic)newItem);
				break;
			case R.id.stat3_spinner:
				while(currentInstance.getStats().size() < 3) {
					currentInstance.getStats().add(null);
				}
				currentInstance.getStats().set(2, (Statistic)newItem);
				break;
		}
	}

	@SuppressWarnings("unchecked")
	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		Statistic newStat;
		int position;
		List<Statistic> stats;
		ArrayAdapter<Statistic> stat1SpinnerAdapter = (ArrayAdapter<Statistic>)stat1Spinner.getAdapter();
		ArrayAdapter<Statistic> stat2SpinnerAdapter = (ArrayAdapter<Statistic>)stat2Spinner.getAdapter();
		ArrayAdapter<Statistic> stat3SpinnerAdapter = (ArrayAdapter<Statistic>)stat3Spinner.getAdapter();

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

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

		boolean newCombat = combatCheckBox.isChecked();
		if(currentInstance.isCombat() != newCombat) {
			currentInstance.setCombat(newCombat);
			changed = true;
		}

		boolean newTradesAndCrafts = tradesAndCraftsCheckBox.isChecked();
		if(currentInstance.isCraftAndTrade() != newTradesAndCrafts) {
			currentInstance.setCraftAndTrade(newTradesAndCrafts);
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
					if(stats.isEmpty() || (newStat != null && !newStat.equals(stats.get(0)))) {
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
				if (position >= 0 && position < stat1SpinnerAdapter.getCount()) {
					newStat = stat1SpinnerAdapter.getItem(position);
				}
				changed |= setStat(stats, newStat, currentIndex++);

				newStat = null;
				position = stat2Spinner.getSelectedItemPosition();
				if (position >= 0 && position < stat2SpinnerAdapter.getCount()) {
					newStat = stat2SpinnerAdapter.getItem(position);
				}
				changed |= setStat(stats, newStat, currentIndex++);

				newStat = null;
				position = stat3Spinner.getSelectedItemPosition();
				if (position >= 0 && position < stat3SpinnerAdapter.getCount()) {
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

	private boolean setStat(List<Statistic> stats, Statistic newStat, int currentIndex) {
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

	@SuppressWarnings("unchecked")
	private void copyItemToViews() {
		ArrayAdapter<Statistic> stat1SpinnerAdapter = (ArrayAdapter<Statistic>)stat1Spinner.getAdapter();
		ArrayAdapter<Statistic> stat2SpinnerAdapter = (ArrayAdapter<Statistic>)stat2Spinner.getAdapter();
		ArrayAdapter<Statistic> stat3SpinnerAdapter = (ArrayAdapter<Statistic>)stat3Spinner.getAdapter();

		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		combatCheckBox.setChecked(currentInstance.isCombat());
		tradesAndCraftsCheckBox.setChecked(currentInstance.isCraftAndTrade());
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
			List<Statistic> stats = currentInstance.getStats() != null ? currentInstance.getStats() : new ArrayList<Statistic>(1);
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

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
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

	private void setStatSpinnerValue(List<Statistic> stats, Spinner spinner, ArrayAdapter<Statistic> adapter, int statIndex) {
		int position;

		while(stats.size() <= statIndex) {
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

	@Override
	public CharSequence getField1Value(SkillCategory skillCategory) {
		return skillCategory.getName();
	}

	@Override
	public CharSequence getField2Value(SkillCategory skillCategory) {
		return skillCategory.getDescription();
	}
}

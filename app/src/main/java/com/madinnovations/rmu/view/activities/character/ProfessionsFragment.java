/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.activities.character;

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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;
import com.madinnovations.rmu.data.entities.character.SkillCostGroup;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.character.ProfessionCategoryCostListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class ProfessionsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Profession>,
		ProfessionCategoryCostListAdapter.ProfessionCostsCallbacks, EditTextUtils.ValuesCallback {
	private static final String TAG = "ProfessionsFragment";
	@Inject
	protected ProfessionRxHandler               professionRxHandler;
	@Inject
	protected SkillRxHandler                    skillRxHandler;
	@Inject
	protected SkillCategoryRxHandler            skillCategoryRxHandler;
	private   ArrayAdapter<String>              realm1SpinnerAdapter;
	private   ArrayAdapter<String>              realm2SpinnerAdapter;
	private   ProfessionCategoryCostListAdapter categoryCostListAdapter;
	private   TwoFieldListAdapter<Profession>   listAdapter;
	private   ListView                          listView;
	private   EditText                          nameEdit;
	private   EditText                          descriptionEdit;
	private   Spinner                           realm1Spinner;
	private   Spinner                           realm2Spinner;
	private   String                            noRealm = null;
	private Collection<SkillCategory> skillCategories = new ArrayList<>();
	private Collection<Skill> skills = new ArrayList<>();
	private Profession currentInstance = new Profession();
	private boolean isNew = true;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		noRealm = getString(R.string.label_no_realm);
		View layout = inflater.inflate(R.layout.professions_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_profession_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_profession_description));

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
										  R.string.validation_profession_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
												 R.string.validation_profession_description_required);
		initRealm1Spinner(layout);
		initRealm2Spinner(layout);
		initCategoryCostListView(layout);
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
		inflater.inflate(R.menu.professions_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_profession) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Profession();
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
		getActivity().getMenuInflater().inflate(R.menu.profession_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Profession profession;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_profession:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Profession();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_profession:
				profession = (Profession)listView.getItemAtPosition(info.position);
				if(profession != null) {
					deleteItem(profession);
					return true;
				}
				break;
			case R.id.context_copy_profession:
				if(copyViewsToItem()) {
					saveItem();
				}
				profession = (Profession)listView.getItemAtPosition(info.position);
				if(profession != null) {
					currentInstance = new Profession(profession);
					currentInstance.setName(profession.getName() + getString(R.string.copy_string));
					copyItemToViews();
					isNew = true;
					listView.clearChoices();
					listAdapter.notifyDataSetChanged();
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Profession profession) {
		return profession.getName();
	}

	@Override
	public CharSequence getField2Value(Profession profession) {
		return profession.getDescription();
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
				saveItem();
				break;
			case R.id.notes_edit:
				currentInstance.setDescription(newString);
				saveItem();
				break;
		}
	}

	@Override
	public boolean performLongClick(@IdRes int editTextId) {
		return false;
	}

	@Override
	public Profession getProfessionInstance() {
		return currentInstance;
	}

	@Override
	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			professionRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Profession>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception saving Profession", e);
							String toastString = getString(R.string.toast_profession_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Profession savedProfession) {
							if (wasNew) {
								listAdapter.add(savedProfession);
								if(savedProfession == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedProfession));
									listView.setItemChecked(listAdapter.getPosition(savedProfession), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								Toast.makeText(getActivity(), R.string.toast_profession_saved, Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(currentInstance);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(currentInstance.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(currentInstance.getDescription());
								}
							}
						}
					});
		}
	}
	// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;

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

		Map<SkillCategory, DevelopmentCostGroup> newSkillCategoryCosts = new HashMap<>();
		Map<Skill, DevelopmentCostGroup> newSkillCosts = new HashMap<>();
		Map<SkillCategory, List<DevelopmentCostGroup>> newAssignableSkillCosts = new HashMap<>();
		for(int i = 0; i < categoryCostListAdapter.getGroupCount(); i++) {
			ProfessionSkillCategoryCost categoryCost = (ProfessionSkillCategoryCost)categoryCostListAdapter.getGroup(i);
			if(!categoryCost.isAssignable() && categoryCost.getCostGroup() != null) {
				newSkillCategoryCosts.put(categoryCost.getSkillCategory(), categoryCost.getCostGroup());
			}
			for(int j = 0; j < categoryCostListAdapter.getChildrenCount(i); j++) {
				SkillCostGroup skillCostGroup = (SkillCostGroup) categoryCostListAdapter.getChild(i, j);
				if(categoryCost.isAssignable()) {
					List<DevelopmentCostGroup> assignableCosts = newAssignableSkillCosts.get(categoryCost.getSkillCategory());
					if(assignableCosts == null) {
						assignableCosts = new ArrayList<>(categoryCostListAdapter.getChildrenCount(i));
						newAssignableSkillCosts.put(categoryCost.getSkillCategory(), assignableCosts);
					}
					assignableCosts.add(categoryCost.getAssignableCostGroups().get(j));
				}
				else if(skillCostGroup.getCostGroup() != null) {
					newSkillCosts.put(skillCostGroup.getSkill(), skillCostGroup.getCostGroup());
				}
			}
		}

		boolean mapChange = false;
		if(newSkillCategoryCosts.size() != currentInstance.getSkillCategoryCosts().size()) {
			mapChange = true;
		}
		else {
			for(Map.Entry<SkillCategory, DevelopmentCostGroup> entry : newSkillCategoryCosts.entrySet()) {
				mapChange |= !entry.getValue().equals(currentInstance.getSkillCategoryCosts().get(entry.getKey()));
			}
		}
		if(mapChange) {
			currentInstance.setSkillCategoryCosts(newSkillCategoryCosts);
			changed = true;
		}

		mapChange = false;
		if(newSkillCosts.size() != currentInstance.getSkillCosts().size()) {
			mapChange = true;
		}
		else {
			for(Map.Entry<Skill, DevelopmentCostGroup> entry : newSkillCosts.entrySet()) {
				mapChange |= !entry.getValue().equals(currentInstance.getSkillCosts().get(entry.getKey()));
			}
		}
		if(mapChange) {
			currentInstance.setSkillCosts(newSkillCosts);
			changed = true;
		}

		mapChange = false;
		if(newAssignableSkillCosts.size() != currentInstance.getAssignableSkillCostsMap().size()) {
			mapChange = true;
		}
		else {
			for(Map.Entry<SkillCategory, List<DevelopmentCostGroup>> entry : newAssignableSkillCosts.entrySet()) {
				mapChange |= !entry.getValue().equals(currentInstance.getAssignableSkillCostsMap().get(entry.getKey()));
			}
		}
		if(mapChange) {
			currentInstance.setAssignableSkillCostsMap(newAssignableSkillCosts);
			changed = true;
		}

		String newRealmName = realm1SpinnerAdapter.getItem(realm1Spinner.getSelectedItemPosition());
		if(newRealmName != null) {
			Realm newRealm = Realm.getRealmWithName(newRealmName);
			if (newRealm == null && currentInstance.getRealm1() != null) {
				currentInstance.setRealm1(null);
				changed = true;
			}
			else if(newRealm != null && !newRealm.equals(currentInstance.getRealm1())) {
				currentInstance.setRealm1(newRealm);
				changed = true;
			}
		}

		newRealmName = realm2SpinnerAdapter.getItem(realm2Spinner.getSelectedItemPosition());
		if(newRealmName != null) {
			Realm newRealm = Realm.getRealmWithName(newRealmName);
			if (newRealm == null && currentInstance.getRealm2() != null) {
				currentInstance.setRealm2(null);
				changed = true;
			}
			else if(newRealm != null && !newRealm.equals(currentInstance.getRealm2())) {
				currentInstance.setRealm2(newRealm);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		Realm currentRealm;
		String realmName;

		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		currentRealm = currentInstance.getRealm1();
		if(currentRealm == null) {
			realmName = noRealm;
		}
		else {
			realmName = currentRealm.toString();
		}
		realm1Spinner.setSelection(realm1SpinnerAdapter.getPosition(realmName));
		currentRealm = currentInstance.getRealm2();
		if(currentRealm == null) {
			realmName = noRealm;
		}
		else {
			realmName = currentRealm.toString();
		}
		realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(realmName));

		categoryCostListAdapter.clear();
		categoryCostListAdapter.addAll(createCostList());
		categoryCostListAdapter.notifyDataSetChanged();

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
	private void deleteItem(final Profession item) {
		professionRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception when deleting: ", e);
					Toast.makeText(getActivity(), R.string.toast_profession_delete_failed, Toast.LENGTH_SHORT).show();
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
							currentInstance = new Profession();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), R.string.toast_profession_deleted, Toast.LENGTH_SHORT).show();
					}
				}
			});
	}
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
	private void initRealm1Spinner(View layout) {
		realm1Spinner = (Spinner)layout.findViewById(R.id.realm1_spinner);
		realm1SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		realm1Spinner.setAdapter(realm1SpinnerAdapter);
		realm1SpinnerAdapter.add(noRealm);
		for(Realm realm : Realm.values()) {
			realm1SpinnerAdapter.add(realm.toString());
		}
		realm1SpinnerAdapter.notifyDataSetChanged();

		realm1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newRealmName = realm1SpinnerAdapter.getItem(position);
				if(newRealmName != null) {
					Realm newRealm = Realm.getRealmWithName(newRealmName);
					if (newRealm == null && currentInstance.getRealm1() != null) {
						currentInstance.setRealm1(null);
						saveItem();
					}
					else if (newRealm != null && !newRealm.equals(currentInstance.getRealm1())) {
						currentInstance.setRealm1(newRealm);
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void initRealm2Spinner(View layout) {
		realm2Spinner = (Spinner)layout.findViewById(R.id.realm2_spinner);
		realm2SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		realm2Spinner.setAdapter(realm2SpinnerAdapter);
		realm2SpinnerAdapter.add(noRealm);
		for(Realm realm : Realm.values()) {
			realm2SpinnerAdapter.add(realm.toString());
		}
		realm2SpinnerAdapter.notifyDataSetChanged();

		realm2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String newRealmName = realm2SpinnerAdapter.getItem(position);
				if(newRealmName != null) {
					Realm newRealm = Realm.getRealmWithName(newRealmName);
					if (newRealm == null && currentInstance.getRealm2() != null) {
						currentInstance.setRealm2(null);
						saveItem();
					}
					else if (newRealm != null && !newRealm.equals(currentInstance.getRealm2())) {
						currentInstance.setRealm2(newRealm);
						saveItem();
					}
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void initCategoryCostListView(View layout) {
		ExpandableListView categoryCostListView = (ExpandableListView) layout.findViewById(R.id.costs_list);
		categoryCostListAdapter = new ProfessionCategoryCostListAdapter(getActivity(), this, categoryCostListView);
		categoryCostListView.setAdapter(categoryCostListAdapter);
		categoryCostListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		categoryCostListAdapter.addAll(createCostList());
		categoryCostListAdapter.notifyDataSetChanged();
		skillCategoryRxHandler.getAll()
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught loading all SkillCategory instances.", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> skillCategories) {
						ProfessionsFragment.this.skillCategories = skillCategories;
					}
				});
		skillRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught getting all Skill instances.", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						ProfessionsFragment.this.skills = skills;
					}
				});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		professionRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Profession>>() {
				@Override
				public void onCompleted() {
					if(listAdapter.getCount() > 0) {
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						listAdapter.notifyDataSetChanged();
					}
					copyItemToViews();
				}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception caught getting all Profession instances", e);
					Toast.makeText(ProfessionsFragment.this.getActivity(),
							R.string.toast_professions_load_failed,
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Profession> professions) {
					listAdapter.clear();
					listAdapter.addAll(professions);
					listAdapter.notifyDataSetChanged();
					if(professions.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_professions_loaded), professions.size());
					Toast.makeText(ProfessionsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Profession) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Profession();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
	// </editor-fold>

	// <editor-fold desc="Private methods">
	private Map<SkillCategory, DevelopmentCostGroup> getSkillCategoryCosts() {
		Map<SkillCategory, DevelopmentCostGroup> oldSkillCategoryCosts = currentInstance.getSkillCategoryCosts();
		Map<SkillCategory, DevelopmentCostGroup> newSkillCategoryCosts = new LinkedHashMap<>(skillCategories.size());
		if(oldSkillCategoryCosts == null) {
			oldSkillCategoryCosts = new LinkedHashMap<>(0);
		}
		for(SkillCategory skillCategory : skillCategories) {
			if(!oldSkillCategoryCosts.containsKey(skillCategory)) {
				newSkillCategoryCosts.put(skillCategory, null);
			}
			else {
				newSkillCategoryCosts.put(skillCategory, oldSkillCategoryCosts.get(skillCategory));
			}
		}
		return newSkillCategoryCosts;
	}

	private Map<Skill, DevelopmentCostGroup> getSkillCosts() {
		Map<Skill, DevelopmentCostGroup> skillCosts = new LinkedHashMap<>(currentInstance.getSkillCosts());
		for(Skill skill : skills) {
			if(!skillCosts.containsKey(skill)) {
				skillCosts.put(skill, null);
			}
		}

		return skillCosts;
	}

	@SuppressWarnings("unchecked")
	private List<ProfessionSkillCategoryCost> createCostList() {
		List<ProfessionSkillCategoryCost> costList = new ArrayList<>(skillCategories.size());
		Map<SkillCategory, DevelopmentCostGroup> skillCategoryCosts = getSkillCategoryCosts();
		Map<Skill, DevelopmentCostGroup> skillCosts = getSkillCosts();

		for(Map.Entry<SkillCategory, DevelopmentCostGroup> entry : skillCategoryCosts.entrySet()) {
			List<SkillCostGroup> skillCostGroupList = new ArrayList<>();
			for(Map.Entry<Skill, DevelopmentCostGroup> skillCostEntry : skillCosts.entrySet()) {
				if(skillCostEntry.getKey().getCategory().equals(entry.getKey())) {
					SkillCostGroup professionSkillCost = new SkillCostGroup(
							skillCostEntry.getKey(),
							skillCostEntry.getValue());
					skillCostGroupList.add(professionSkillCost);
				}
			}
			Collections.sort(skillCostGroupList);
			List<DevelopmentCostGroup> assignableCostList = currentInstance.getAssignableSkillCostsMap().get(entry.getKey());
			if(assignableCostList != null) {
				for(int i = assignableCostList.size() - 1; i >=0; i--) {
					if(assignableCostList.get(i) == null) {
						assignableCostList.set(i, DevelopmentCostGroup.NONE);
					}
				}
				if (assignableCostList.size() > skillCostGroupList.size()) {
					assignableCostList = new ArrayList<>();
					currentInstance.getAssignableSkillCostsMap().put(entry.getKey(), assignableCostList);
				}
				else {
					Collections.sort(assignableCostList);
				}
			}
			else {
				assignableCostList = new ArrayList<>();
			}
			ProfessionSkillCategoryCost professionSkillCategoryCost = new ProfessionSkillCategoryCost(
					entry.getKey(),
					skillCostGroupList,
					entry.getValue(),
					!assignableCostList.isEmpty(),
					assignableCostList,
					currentInstance.getProfessionalSkillCategories().contains(entry.getKey()));
			costList.add(professionSkillCategoryCost);
		}
		return costList;
	}
	// </editor-fold>
}

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
package com.madinnovations.rmu.view.activities.creature;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureArchetypeRxHandler;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature archetypes.
 */
public class CreatureArchetypesMainPageFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureArchetype> {
	@Inject
	protected CreatureArchetypeRxHandler             creatureArchetypeRxHandler;
	@Inject
	protected SkillCategoryRxHandler                 skillCategoryRxHandler;
	private   CreatureArchetypesFragment             creatureArchetypesFragment;
	private   ArrayAdapter<Statistic>                stat1SpinnerAdapter;
	private   ArrayAdapter<Statistic>                stat2SpinnerAdapter;
	private   ArrayAdapter<SkillCategory>            primarySkillCategoriesListAdapter;
	private   ArrayAdapter<SkillCategory>            secondarySkillCategoriesListAdapter;
	private   ArrayAdapter<SkillCategory>            tertiarySkillCategoriesListAdapter;
	private   EditText                               nameEdit;
	private EditText                     descriptionEdit;
	private CheckBox                     stat1IsRealmCheckBox;
	private Spinner                      stat1Spinner;
	private CheckBox                     stat2IsRealmCheckBox;
	private Spinner                      stat2Spinner;
	private ListView                     primarySkillCategoriesList;
	private ListView                     secondarySkillCategoriesList;
	private ListView                     tertiarySkillCategoriesList;
	private EditText                     spellsEdit;
	private EditText                     rolesEdit;
	private List<SkillCategory> newSkillCategoriesList = new ArrayList<>();
	private Statistic           realmStat              = Statistic.NON_REALM;

	/**
	 * Creates a new CreatureArchetypesMainPageFragment instance.
	 *
	 * @param creatureArchetypesFragment  the CreatureArchetypesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureArchetypesMainPageFragment newInstance(CreatureArchetypesFragment creatureArchetypesFragment) {
		CreatureArchetypesMainPageFragment fragment = new CreatureArchetypesMainPageFragment();
		fragment.creatureArchetypesFragment = creatureArchetypesFragment;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_archetypes_main_page, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initStat1IsRealmCheckBox(layout);
		initStat1Spinner(layout);
		initStat2IsRealmCheckBox(layout);
		initStat2Spinner(layout);
		initPrimarySkillCategoriesList(layout);
		initSecondarySkillCategoriesList(layout);
		initTertiarySkillCategoriesList(layout);
		initSpellsEdit(layout);
		initRolesEdit(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			creatureArchetypesFragment.saveItem();
		}
		super.onPause();
	}

	public boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		Statistic newStat;
		int position;

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && creatureArchetypesFragment.getCurrentInstance().getName() != null) ||
				(newString != null && !newString.equals(creatureArchetypesFragment.getCurrentInstance().getName()))) {
			creatureArchetypesFragment.getCurrentInstance().setName(newString);
			changed = true;
		}

		newString = descriptionEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && creatureArchetypesFragment.getCurrentInstance().getDescription() != null) ||
				(newString != null && !newString.equals(creatureArchetypesFragment.getCurrentInstance().getDescription()))) {
			creatureArchetypesFragment.getCurrentInstance().setDescription(newString);
			changed = true;
		}

		if(creatureArchetypesFragment.getCurrentInstance().isRealmStat1() != stat1IsRealmCheckBox.isChecked()) {
			creatureArchetypesFragment.getCurrentInstance().setRealmStat1(stat1IsRealmCheckBox.isChecked());
			changed = true;
		}

		position = stat1Spinner.getSelectedItemPosition();
		if(position != -1) {
			newStat = stat1SpinnerAdapter.getItem(position);
			if(newStat != null && newStat.equals(realmStat) && creatureArchetypesFragment.getCurrentInstance().getStat1() != null) {
				creatureArchetypesFragment.getCurrentInstance().setStat1(null);
				changed = true;
			}
			else if(newStat != null && !newStat.equals(realmStat) && !newStat.equals(creatureArchetypesFragment.getCurrentInstance().getStat1())) {
				creatureArchetypesFragment.getCurrentInstance().setStat1(newStat);
				changed = true;
			}
		}

		if(creatureArchetypesFragment.getCurrentInstance().isRealmStat2() != stat2IsRealmCheckBox.isChecked()) {
			creatureArchetypesFragment.getCurrentInstance().setRealmStat2(stat2IsRealmCheckBox.isChecked());
			changed = true;
		}

		position = stat2Spinner.getSelectedItemPosition();
		if(position != -1) {
			newStat = stat2SpinnerAdapter.getItem(position);
			if(newStat != null && newStat.equals(realmStat) && creatureArchetypesFragment.getCurrentInstance().getStat2() != null) {
				creatureArchetypesFragment.getCurrentInstance().setStat2(null);
				changed = true;
			}
			else if(newStat != null && !newStat.equals(realmStat) && !newStat.equals(creatureArchetypesFragment.getCurrentInstance().getStat2())) {
				creatureArchetypesFragment.getCurrentInstance().setStat2(newStat);
				changed = true;
			}
		}

		if(updateSkillCategoriesList(primarySkillCategoriesList, primarySkillCategoriesListAdapter, creatureArchetypesFragment.getCurrentInstance().getPrimarySkills())) {
			creatureArchetypesFragment.getCurrentInstance().setPrimarySkills(newSkillCategoriesList);
			changed = true;
		}

		if(updateSkillCategoriesList(secondarySkillCategoriesList, secondarySkillCategoriesListAdapter,
				creatureArchetypesFragment.getCurrentInstance().getSecondarySkills())) {
			creatureArchetypesFragment.getCurrentInstance().setSecondarySkills(newSkillCategoriesList);
			changed = true;
		}

		if(updateSkillCategoriesList(tertiarySkillCategoriesList, tertiarySkillCategoriesListAdapter,
				creatureArchetypesFragment.getCurrentInstance().getTertiarySkills())) {
			creatureArchetypesFragment.getCurrentInstance().setTertiarySkills(newSkillCategoriesList);
			changed = true;
		}

		newString = spellsEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && creatureArchetypesFragment.getCurrentInstance().getSpells() != null) ||
				(newString != null && !newString.equals(creatureArchetypesFragment.getCurrentInstance().getSpells()))) {
			creatureArchetypesFragment.getCurrentInstance().setSpells(newString);
			changed = true;
		}

		newString = rolesEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && creatureArchetypesFragment.getCurrentInstance().getRoles() != null) ||
				(newString != null && !newString.equals(creatureArchetypesFragment.getCurrentInstance().getRoles()))) {
			creatureArchetypesFragment.getCurrentInstance().setRoles(newString);
			changed = true;
		}

		return changed;
	}

	private boolean updateSkillCategoriesList(ListView sourceListView, ArrayAdapter<SkillCategory> sourceAdapter,
											  List<SkillCategory> currentList) {
		boolean changed = false;
		SparseBooleanArray checkedSkillCategories;
		List<SkillCategory> selectedSkillCategories = new ArrayList<>();

		checkedSkillCategories = sourceListView.getCheckedItemPositions();
		selectedSkillCategories.clear();
		if(checkedSkillCategories != null) {
			for (int i = 0; i < checkedSkillCategories.size(); i++) {
				if (checkedSkillCategories.valueAt(i) && checkedSkillCategories.keyAt(i) != -1) {
					selectedSkillCategories.add(sourceAdapter.getItem(checkedSkillCategories.keyAt(i)));
				}
			}
			newSkillCategoriesList.clear();
			for (SkillCategory category : currentList) {
				if (selectedSkillCategories.contains(category)) {
					newSkillCategoriesList.add(category);
					selectedSkillCategories.remove(category);
				} else {
					changed = true;
				}
			}
			if (selectedSkillCategories.size() > 0) {
				newSkillCategoriesList.addAll(selectedSkillCategories);
				changed = true;
			}
		}
		else if(!currentList.isEmpty()) {
			newSkillCategoriesList.clear();
			changed = true;
		}

		return changed;
	}

	public void copyItemToViews() {
		nameEdit.setText(creatureArchetypesFragment.getCurrentInstance().getName());
		descriptionEdit.setText(creatureArchetypesFragment.getCurrentInstance().getDescription());
		stat1IsRealmCheckBox.setChecked(creatureArchetypesFragment.getCurrentInstance().isRealmStat1());
		if(creatureArchetypesFragment.getCurrentInstance().getStat1() == null) {
			stat1Spinner.setSelection(stat1SpinnerAdapter.getPosition(realmStat));
		}
		else {
			stat1Spinner.setSelection(stat1SpinnerAdapter.getPosition(creatureArchetypesFragment.getCurrentInstance().getStat1()));
		}
		stat2IsRealmCheckBox.setChecked(creatureArchetypesFragment.getCurrentInstance().isRealmStat2());
		if(creatureArchetypesFragment.getCurrentInstance().getStat2() == null) {
			stat2Spinner.setSelection(stat2SpinnerAdapter.getPosition(realmStat));
		}
		else {
			stat2Spinner.setSelection(stat1SpinnerAdapter.getPosition(creatureArchetypesFragment.getCurrentInstance().getStat2()));
		}
		primarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : creatureArchetypesFragment.getCurrentInstance().getPrimarySkills()) {
			primarySkillCategoriesList.setItemChecked(primarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		primarySkillCategoriesListAdapter.notifyDataSetChanged();
		secondarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : creatureArchetypesFragment.getCurrentInstance().getSecondarySkills()) {
			secondarySkillCategoriesList.setItemChecked(secondarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		secondarySkillCategoriesListAdapter.notifyDataSetChanged();
		tertiarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : creatureArchetypesFragment.getCurrentInstance().getTertiarySkills()) {
			tertiarySkillCategoriesList.setItemChecked(tertiarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		tertiarySkillCategoriesListAdapter.notifyDataSetChanged();
		spellsEdit.setText(creatureArchetypesFragment.getCurrentInstance().getSpells());
		rolesEdit.setText(creatureArchetypesFragment.getCurrentInstance().getRoles());

		if(creatureArchetypesFragment.getCurrentInstance().getName() != null && !creatureArchetypesFragment.getCurrentInstance().getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(creatureArchetypesFragment.getCurrentInstance().getDescription() != null && !creatureArchetypesFragment.getCurrentInstance().getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
		if(creatureArchetypesFragment.getCurrentInstance().getSpells() != null && !creatureArchetypesFragment.getCurrentInstance().getSpells().isEmpty()) {
			spellsEdit.setError(null);
		}
		if(creatureArchetypesFragment.getCurrentInstance().getRoles() != null && !creatureArchetypesFragment.getCurrentInstance().getRoles().isEmpty()) {
			rolesEdit.setError(null);
		}
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
					nameEdit.setError(getString(R.string.validation_creature_archetype_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(creatureArchetypesFragment.getCurrentInstance().getName())) {
						creatureArchetypesFragment.getCurrentInstance().setName(newName);
						creatureArchetypesFragment.saveItem();
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.notes_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_creature_archetype_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (!newDescription.equals(creatureArchetypesFragment.getCurrentInstance().getDescription())) {
						creatureArchetypesFragment.getCurrentInstance().setDescription(newDescription);
						creatureArchetypesFragment.saveItem();
					}
				}
			}
		});
	}

	private void initStat1IsRealmCheckBox(View layout) {
		stat1IsRealmCheckBox = (CheckBox)layout.findViewById(R.id.stat1_is_realm_check_box);

		stat1IsRealmCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				creatureArchetypesFragment.getCurrentInstance().setRealmStat1(stat1IsRealmCheckBox.isChecked());
				stat1Spinner.setEnabled(!stat1IsRealmCheckBox.isChecked());
				if(stat1IsRealmCheckBox.isChecked()) {
					creatureArchetypesFragment.getCurrentInstance().setStat1(null);
				}
				creatureArchetypesFragment.saveItem();
			}
		});
	}

	private void initStat1Spinner(View layout) {
		stat1Spinner = (Spinner)layout.findViewById(R.id.stat1_spinner);
		stat1SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		stat1Spinner.setAdapter(stat1SpinnerAdapter);

//		statRxHandler.getAll()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Collection<Stat>>() {
//					@Override
//					public void onCompleted() {}
//					@Override
//					public void onError(Throwable e) {
//						Log.e("CreatureArchetypesFrag", "Exception caught getting all Stat instances", e);
//					}
//					@Override
//					public void onNext(Collection<Stat> items) {
//						stat1SpinnerAdapter.clear();
//						stat1SpinnerAdapter.add(realmStat);
//						stat1SpinnerAdapter.addAll(items);
//						stat1SpinnerAdapter.notifyDataSetChanged();
//					}
//				});
		stat1SpinnerAdapter.clear();
		stat1SpinnerAdapter.addAll(Statistic.values());
		stat1SpinnerAdapter.notifyDataSetChanged();
		stat1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Statistic newStat = stat1SpinnerAdapter.getItem(position);
				if(newStat != null && newStat.equals(realmStat) && creatureArchetypesFragment.getCurrentInstance().getStat1() != null) {
					creatureArchetypesFragment.getCurrentInstance().setStat1(null);
					creatureArchetypesFragment.saveItem();
				}
				else if(newStat != null && !newStat.equals(realmStat) && !newStat.equals(creatureArchetypesFragment.getCurrentInstance().getStat1())) {
					creatureArchetypesFragment.getCurrentInstance().setStat1(newStat);
					creatureArchetypesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(creatureArchetypesFragment.getCurrentInstance().getStat1() != null) {
					creatureArchetypesFragment.getCurrentInstance().setStat1(null);
					creatureArchetypesFragment.saveItem();
				}
			}
		});
	}

	private void initStat2IsRealmCheckBox(View layout) {
		stat2IsRealmCheckBox = (CheckBox)layout.findViewById(R.id.stat2_is_realm_check_box);

		stat2IsRealmCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				creatureArchetypesFragment.getCurrentInstance().setRealmStat2(stat2IsRealmCheckBox.isChecked());
				stat2Spinner.setEnabled(!stat2IsRealmCheckBox.isChecked());
				if(stat2IsRealmCheckBox.isChecked()) {
					creatureArchetypesFragment.getCurrentInstance().setStat2(null);
				}
				creatureArchetypesFragment.saveItem();
			}
		});
	}

	private void initStat2Spinner(View layout) {
		stat2Spinner = (Spinner)layout.findViewById(R.id.stat2_spinner);
		stat2SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		stat2Spinner.setAdapter(stat2SpinnerAdapter);

//		statRxHandler.getAll()
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new Subscriber<Collection<Stat>>() {
//					@Override
//					public void onCompleted() {}
//					@Override
//					public void onError(Throwable e) {
//						Log.e("CreatureArchetypesFrag", "Exception caught getting all Stat instances", e);
//					}
//					@Override
//					public void onNext(Collection<Stat> items) {
//						stat2SpinnerAdapter.clear();
//						stat2SpinnerAdapter.add(realmStat);
//						stat2SpinnerAdapter.addAll(items);
//						stat2SpinnerAdapter.notifyDataSetChanged();
//					}
//				});
		stat2SpinnerAdapter.clear();
		stat2SpinnerAdapter.addAll(Statistic.values());
		stat2SpinnerAdapter.notifyDataSetChanged();
		stat2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Statistic newStat = stat2SpinnerAdapter.getItem(position);
				if(newStat != null && newStat.equals(realmStat) && creatureArchetypesFragment.getCurrentInstance().getStat2() != null) {
					creatureArchetypesFragment.getCurrentInstance().setStat2(null);
					creatureArchetypesFragment.saveItem();
				}
				else if(newStat != null && !newStat.equals(realmStat) && !newStat.equals(creatureArchetypesFragment.getCurrentInstance().getStat2())) {
					creatureArchetypesFragment.getCurrentInstance().setStat2(newStat);
					creatureArchetypesFragment.saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(creatureArchetypesFragment.getCurrentInstance().getStat2() != null) {
					creatureArchetypesFragment.getCurrentInstance().setStat2(null);
					creatureArchetypesFragment.saveItem();
				}
			}
		});
	}

	private void initPrimarySkillCategoriesList(View layout) {
		primarySkillCategoriesList = (ListView) layout.findViewById(R.id.primary_skills_list);
		primarySkillCategoriesListAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		primarySkillCategoriesList.setAdapter(primarySkillCategoriesListAdapter);

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> items) {
						primarySkillCategoriesListAdapter.clear();
						primarySkillCategoriesListAdapter.addAll(items);
						primarySkillCategoriesListAdapter.notifyDataSetChanged();
					}
				});
		primarySkillCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				SkillCategory skillCategory = primarySkillCategoriesListAdapter.getItem(position);
				boolean checked = primarySkillCategoriesList.isItemChecked(position);
				if(creatureArchetypesFragment.getCurrentInstance().getPrimarySkills().contains(skillCategory) && !checked) {
					creatureArchetypesFragment.getCurrentInstance().getPrimarySkills().remove(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
				else if(!creatureArchetypesFragment.getCurrentInstance().getPrimarySkills().contains(skillCategory) && checked) {
					creatureArchetypesFragment.getCurrentInstance().getPrimarySkills().add(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
			}
		});
	}

	private void initSecondarySkillCategoriesList(View layout) {
		secondarySkillCategoriesList = (ListView) layout.findViewById(R.id.secondary_skills_list);
		secondarySkillCategoriesListAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		secondarySkillCategoriesList.setAdapter(secondarySkillCategoriesListAdapter);

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> items) {
						secondarySkillCategoriesListAdapter.clear();
						secondarySkillCategoriesListAdapter.addAll(items);
						secondarySkillCategoriesListAdapter.notifyDataSetChanged();
					}
				});
		secondarySkillCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				SkillCategory skillCategory = secondarySkillCategoriesListAdapter.getItem(position);
				boolean checked = secondarySkillCategoriesList.isItemChecked(position);
				if(creatureArchetypesFragment.getCurrentInstance().getSecondarySkills().contains(skillCategory) && !checked) {
					creatureArchetypesFragment.getCurrentInstance().getSecondarySkills().remove(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
				else if(!creatureArchetypesFragment.getCurrentInstance().getSecondarySkills().contains(skillCategory) && checked) {
					creatureArchetypesFragment.getCurrentInstance().getSecondarySkills().add(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
			}
		});
	}

	private void initTertiarySkillCategoriesList(View layout) {
		tertiarySkillCategoriesList = (ListView) layout.findViewById(R.id.tertiary_skills_list);
		tertiarySkillCategoriesListAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		tertiarySkillCategoriesList.setAdapter(tertiarySkillCategoriesListAdapter);

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> items) {
						tertiarySkillCategoriesListAdapter.clear();
						tertiarySkillCategoriesListAdapter.addAll(items);
						tertiarySkillCategoriesListAdapter.notifyDataSetChanged();
					}
				});
		tertiarySkillCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				SkillCategory skillCategory = tertiarySkillCategoriesListAdapter.getItem(position);
				boolean checked = tertiarySkillCategoriesList.isItemChecked(position);
				if(creatureArchetypesFragment.getCurrentInstance().getTertiarySkills().contains(skillCategory) && !checked) {
					creatureArchetypesFragment.getCurrentInstance().getTertiarySkills().remove(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
				else if(!creatureArchetypesFragment.getCurrentInstance().getTertiarySkills().contains(skillCategory) && checked) {
					creatureArchetypesFragment.getCurrentInstance().getTertiarySkills().add(skillCategory);
					creatureArchetypesFragment.saveItem();
				}
			}
		});
	}

	private void initSpellsEdit(View layout) {
		spellsEdit = (EditText)layout.findViewById(R.id.spells_edit);
		spellsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && spellsEdit != null) {
					spellsEdit.setError(getString(R.string.validation_spells_required));
				}
			}
		});
		spellsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newSpells = spellsEdit.getText().toString();
					if (!newSpells.equals(creatureArchetypesFragment.getCurrentInstance().getSpells())) {
						creatureArchetypesFragment.getCurrentInstance().setSpells(newSpells);
						creatureArchetypesFragment.saveItem();
					}
				}
			}
		});
	}

	private void initRolesEdit(View layout) {
		rolesEdit = (EditText)layout.findViewById(R.id.roles_edit);
		rolesEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && rolesEdit != null) {
					rolesEdit.setError(getString(R.string.validation_roles_required));
				}
			}
		});
		rolesEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newRoles = rolesEdit.getText().toString();
					if (!newRoles.equals(creatureArchetypesFragment.getCurrentInstance().getRoles())) {
						creatureArchetypesFragment.getCurrentInstance().setRoles(newRoles);
						creatureArchetypesFragment.saveItem();
					}
				}
			}
		});
	}

	@Override
	public CharSequence getField1Value(CreatureArchetype creatureArchetype) {
		return creatureArchetype.getName();
	}

	@Override
	public CharSequence getField2Value(CreatureArchetype creatureArchetype) {
		return creatureArchetype.getDescription();
	}
}

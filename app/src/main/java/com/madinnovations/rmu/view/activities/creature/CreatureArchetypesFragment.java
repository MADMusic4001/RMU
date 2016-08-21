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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureArchetypeRxHandler;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.StatSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.ArchetypeSkillCategoryListAdapter;
import com.madinnovations.rmu.view.adapters.creature.CreatureArchetypeListAdapter;
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
public class CreatureArchetypesFragment extends Fragment {
	@Inject
	protected CreatureArchetypeRxHandler creatureArchetypeRxHandler;
	@Inject
	protected StatRxHandler statRxHandler;
	@Inject
	protected SkillCategoryRxHandler skillCategoryRxHandler;
	@Inject
	protected CreatureArchetypeListAdapter listAdapter;
	@Inject
	protected StatSpinnerAdapter stat1SpinnerAdapter;
	@Inject
	protected StatSpinnerAdapter stat2SpinnerAdapter;
	@Inject
	protected ArchetypeSkillCategoryListAdapter primarySkillCategoriesListAdapter;
	@Inject
	protected ArchetypeSkillCategoryListAdapter secondarySkillCategoriesListAdapter;
	@Inject
	protected ArchetypeSkillCategoryListAdapter tertiarySkillCategoriesListAdapter;
	private   ListView                     listView;
	private   EditText                     nameEdit;
	private   EditText                     descriptionEdit;
	private Spinner                        stat1Spinner;
	private Spinner                        stat2Spinner;
	private ListView                       primarySkillCategoriesList;
	private ListView                       secondarySkillCategoriesList;
	private ListView                       tertiarySkillCategoriesList;
	private   EditText                     spellsEdit;
	private   EditText                     rolesEdit;
	private CreatureArchetype currentInstance = new CreatureArchetype();
	private boolean          isNew            = true;
	private List<SkillCategory> newSkillCategoriesList = new ArrayList<>();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_archetypes_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_archetype_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_archetype_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initStat1Spinner(layout);
		initStat2Spinner(layout);
		initPrimarySkillCategoriesList(layout);
		initSecondarySkillCategoriesList(layout);
		initTertiarySkillCategoriesList(layout);
		initSpellsEdit(layout);
		initRolesEdit(layout);
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
		inflater.inflate(R.menu.creature_archetypes_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_archetype) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CreatureArchetype();
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
		getActivity().getMenuInflater().inflate(R.menu.creature_archetype_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureArchetype creatureArchetype;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_archetype:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CreatureArchetype();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature_archetype:
				creatureArchetype = (CreatureArchetype)listView.getItemAtPosition(info.position);
				if(creatureArchetype != null) {
					deleteItem(creatureArchetype);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		Stat newStat;
		int position;

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

		position = stat1Spinner.getSelectedItemPosition();
		if(position != -1) {
			newStat = stat1SpinnerAdapter.getItem(position);
			if(!newStat.equals(currentInstance.getStat1())) {
				currentInstance.setStat1(newStat);
				changed = true;
			}
		}

		position = stat2Spinner.getSelectedItemPosition();
		if(position != -1) {
			newStat = stat2SpinnerAdapter.getItem(position);
			if(!newStat.equals(currentInstance.getStat2())) {
				currentInstance.setStat2(newStat);
				changed = true;
			}
		}

		if(updateSkillCategoriesList(primarySkillCategoriesList, primarySkillCategoriesListAdapter, currentInstance.getPrimarySkills())) {
			currentInstance.setPrimarySkills(newSkillCategoriesList);
			changed = true;
		}

		if(updateSkillCategoriesList(secondarySkillCategoriesList, secondarySkillCategoriesListAdapter,
				currentInstance.getSecondarySkills())) {
			currentInstance.setSecondarySkills(newSkillCategoriesList);
			changed = true;
		}

		if(updateSkillCategoriesList(tertiarySkillCategoriesList, tertiarySkillCategoriesListAdapter,
				currentInstance.getTertiarySkills())) {
			currentInstance.setTertiarySkills(newSkillCategoriesList);
			changed = true;
		}

		newString = spellsEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getSpells() != null) ||
				(newString != null && !newString.equals(currentInstance.getSpells()))) {
			currentInstance.setSpells(newString);
			changed = true;
		}

		newString = rolesEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getRoles() != null) ||
				(newString != null && !newString.equals(currentInstance.getRoles()))) {
			currentInstance.setRoles(newString);
			changed = true;
		}

		return changed;
	}

	private boolean updateSkillCategoriesList(ListView sourceListView, ArchetypeSkillCategoryListAdapter sourceAdapter,
											  List<SkillCategory> currentList) {
		boolean changed = false;
		SparseBooleanArray checkedSkillCategories;
		List<SkillCategory> selectedSkillCategories = new ArrayList<>();

		checkedSkillCategories = sourceListView.getCheckedItemPositions();
		selectedSkillCategories.clear();
		if(checkedSkillCategories != null) {
			for (int i = 0; i < checkedSkillCategories.size(); i++) {
				if (checkedSkillCategories.valueAt(i)) {
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

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		stat1Spinner.setSelection(stat1SpinnerAdapter.getPosition(currentInstance.getStat1()));
		stat2Spinner.setSelection(stat1SpinnerAdapter.getPosition(currentInstance.getStat2()));
		primarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : currentInstance.getPrimarySkills()) {
			primarySkillCategoriesList.setItemChecked(primarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		primarySkillCategoriesListAdapter.notifyDataSetChanged();
		secondarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : currentInstance.getSecondarySkills()) {
			secondarySkillCategoriesList.setItemChecked(secondarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		secondarySkillCategoriesListAdapter.notifyDataSetChanged();
		tertiarySkillCategoriesList.clearChoices();
		for(SkillCategory skillCategory : currentInstance.getTertiarySkills()) {
			tertiarySkillCategoriesList.setItemChecked(tertiarySkillCategoriesListAdapter.getPosition(skillCategory), true);
		}
		tertiarySkillCategoriesListAdapter.notifyDataSetChanged();
		spellsEdit.setText(currentInstance.getSpells());
		rolesEdit.setText(currentInstance.getRoles());

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
		if(currentInstance.getSpells() != null && !currentInstance.getSpells().isEmpty()) {
			spellsEdit.setError(null);
		}
		if(currentInstance.getRoles() != null && !currentInstance.getRoles().isEmpty()) {
			rolesEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureArchetypeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CreatureArchetype>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CreatureArchetypesFrag", "Exception saving new CreatureArchetype: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CreatureArchetype savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.header_field1);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.header_field2);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final CreatureArchetype item) {
		creatureArchetypeRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_creature_archetype_delete_failed);
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
								currentInstance = new CreatureArchetype();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_deleted), Toast.LENGTH_SHORT).show();
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
					nameEdit.setError(getString(R.string.validation_creature_archetype_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(currentInstance.getName())) {
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
					descriptionEdit.setError(getString(R.string.validation_creature_archetype_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (!newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initStat1Spinner(View layout) {
		stat1Spinner = (Spinner)layout.findViewById(R.id.stat1_spinner);
		stat1Spinner.setAdapter(stat1SpinnerAdapter);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception caught getting all Stat instances", e);
					}
					@Override
					public void onNext(Collection<Stat> items) {
						stat1SpinnerAdapter.clear();
						stat1SpinnerAdapter.addAll(items);
						stat1SpinnerAdapter.notifyDataSetChanged();
					}
				});

		stat1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getStat1() == null || stat1SpinnerAdapter.getPosition(currentInstance.getStat1()) != position) {
					currentInstance.setStat1(stat1SpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStat1() != null) {
					currentInstance.setStat1(null);
					saveItem();
				}
			}
		});
	}

	private void initStat2Spinner(View layout) {
		stat2Spinner = (Spinner)layout.findViewById(R.id.stat2_spinner);
		stat2Spinner.setAdapter(stat2SpinnerAdapter);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception caught getting all Stat instances", e);
					}
					@Override
					public void onNext(Collection<Stat> items) {
						stat2SpinnerAdapter.clear();
						stat2SpinnerAdapter.addAll(items);
						stat2SpinnerAdapter.notifyDataSetChanged();
					}
				});

		stat2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getStat2() == null || stat2SpinnerAdapter.getPosition(currentInstance.getStat2()) != position) {
					currentInstance.setStat2(stat2SpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStat2() != null) {
					currentInstance.setStat2(null);
					saveItem();
				}
			}
		});
	}

	private void initPrimarySkillCategoriesList(View layout) {
		primarySkillCategoriesList = (ListView) layout.findViewById(R.id.primary_skills_list);

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
				if(currentInstance.getPrimarySkills().contains(skillCategory) && !checked) {
					currentInstance.getPrimarySkills().remove(skillCategory);
					saveItem();
				}
				else if(!currentInstance.getPrimarySkills().contains(skillCategory) && checked) {
					currentInstance.getPrimarySkills().add(skillCategory);
					saveItem();
				}
			}
		});
	}

	private void initSecondarySkillCategoriesList(View layout) {
		secondarySkillCategoriesList = (ListView) layout.findViewById(R.id.secondary_skills_list);

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
				if(currentInstance.getSecondarySkills().contains(skillCategory) && !checked) {
					currentInstance.getSecondarySkills().remove(skillCategory);
					saveItem();
				}
				else if(!currentInstance.getSecondarySkills().contains(skillCategory) && checked) {
					currentInstance.getSecondarySkills().add(skillCategory);
					saveItem();
				}
			}
		});
	}

	private void initTertiarySkillCategoriesList(View layout) {
		tertiarySkillCategoriesList = (ListView) layout.findViewById(R.id.tertiary_skills_list);

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
				if(currentInstance.getTertiarySkills().contains(skillCategory) && !checked) {
					currentInstance.getTertiarySkills().remove(skillCategory);
					saveItem();
				}
				else if(!currentInstance.getTertiarySkills().contains(skillCategory) && checked) {
					currentInstance.getTertiarySkills().add(skillCategory);
					saveItem();
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
					if (!newSpells.equals(currentInstance.getSpells())) {
						currentInstance.setSpells(newSpells);
						saveItem();
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
					if (!newRoles.equals(currentInstance.getRoles())) {
						currentInstance.setRoles(newRoles);
						saveItem();
					}
				}
			}
		});
	}

 	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		creatureArchetypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureArchetype>>() {
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
						Log.e("CreatureArchetypesFrag",
								"Exception caught getting all CreatureArchetype instances in onCreateView", e);
						Toast.makeText(CreatureArchetypesFragment.this.getActivity(),
								getString(R.string.toast_creature_archetypes_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CreatureArchetype> creatureArchetypes) {
						listAdapter.clear();
						listAdapter.addAll(creatureArchetypes);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_archetypes_loaded), creatureArchetypes.size());
						Toast.makeText(CreatureArchetypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CreatureArchetype) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CreatureArchetype();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}

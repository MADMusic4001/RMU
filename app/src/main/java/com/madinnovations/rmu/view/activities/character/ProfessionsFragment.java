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
package com.madinnovations.rmu.view.activities.character;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.character.ProfessionCategoryCostListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class ProfessionsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Profession> {
	@Inject
	protected ProfessionRxHandler professionRxHandler;
	@Inject
	protected SkillCategoryRxHandler skillCategoryRxHandler;
	@Inject
	protected ProfessionCategoryCostListAdapter categoryCostListAdapter;
	private TwoFieldListAdapter<Profession> listAdapter;
	private ListView                  listView;
	private EditText                  nameEdit;
	private EditText                  descriptionEdit;
	private Collection<SkillCategory> skillCategories = null;
	private Profession currentInstance = new Profession();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.professions_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_profession_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_profession_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
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
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		String value = nameEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getName() != null) ||
				(value != null && !value.equals(currentInstance.getName()))) {
			currentInstance.setName(value);
			changed = true;
		}

		value = descriptionEdit.getText().toString();
		if(value.isEmpty()) {
			value = null;
		}
		if((value == null && currentInstance.getDescription() != null) ||
				(value != null && !value.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(value);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		addMissingCosts();
		categoryCostListAdapter.clear();
		if(currentInstance.getProfessionSkillCategoryCosts() != null) {
			categoryCostListAdapter.addAll(currentInstance.getProfessionSkillCategoryCosts());
		}
		categoryCostListAdapter.notifyDataSetChanged();

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final Profession item) {
		professionRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("ProfessionFragment", "Exception when deleting: ", e);
					Toast.makeText(getActivity(), getString(R.string.toast_profession_delete_failed), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getActivity(), getString(R.string.toast_profession_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void saveItem() {
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
						Log.e("ProfessionsFragment", "Exception saving Profession", e);
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
							String toastString;
							toastString = getString(R.string.toast_profession_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

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
					nameEdit.setError(getString(R.string.validation_profession_name_required));
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
					descriptionEdit.setError(getString(R.string.validation_profession_description_required));
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

	private void initCategoryCostListView(View layout) {
		ListView categoryCostListView = (ListView) layout.findViewById(R.id.category_costs_list);
		categoryCostListView.setAdapter(categoryCostListAdapter);

		if(currentInstance.getProfessionSkillCategoryCosts() == null || currentInstance.getProfessionSkillCategoryCosts().isEmpty()) {
			skillCategoryRxHandler.getAll()
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Collection<SkillCategory>>() {
						@Override
						public void onCompleted() {
							categoryCostListAdapter.clear();
							categoryCostListAdapter.addAll(currentInstance.getProfessionSkillCategoryCosts());
							categoryCostListAdapter.notifyDataSetChanged();
						}
						@Override
						public void onError(Throwable e) {
							Log.e("ProfessionsFragment", "Failed to load SkillCategory instances", e);
						}
						@Override
						public void onNext(Collection<SkillCategory> skillCategories) {
							ProfessionsFragment.this.skillCategories = skillCategories;
							addMissingCosts();
						}
					});
		}
	}

	private void addMissingCosts() {
		List<ProfessionSkillCategoryCost> skillCategoryCosts = currentInstance.getProfessionSkillCategoryCosts();
		if(skillCategoryCosts == null) {
			skillCategoryCosts = new ArrayList<>();
			currentInstance.setProfessionSkillCategoryCosts(skillCategoryCosts);
		}
		if(skillCategories != null) {
			for (SkillCategory skillCategory : skillCategories) {
				boolean newCost = true;
				for (ProfessionSkillCategoryCost pscc : currentInstance.getProfessionSkillCategoryCosts()) {
					if (skillCategory.equals(pscc.getSkillCategory())) {
						newCost = false;
						break;
					}
				}
				if (newCost) {
					skillCategoryCosts.add(new ProfessionSkillCategoryCost(-1, currentInstance, skillCategory, new SkillCost()));
				}
			}
		}
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
						copyItemToViews();
					}
				}
				@Override
				public void onError(Throwable e) {
					Log.e("ProfessionsFragment", "Exception caught getting all Profession instances", e);
					Toast.makeText(ProfessionsFragment.this.getActivity(),
							getString(R.string.toast_professions_load_failed),
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

	@Override
	public CharSequence getField1Value(Profession profession) {
		return profession.getName();
	}

	@Override
	public CharSequence getField2Value(Profession profession) {
		return profession.getDescription();
	}
}

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
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillCategorySpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for skills.
 */
public class SkillsFragment extends Fragment {
	@Inject
	protected SkillRxHandler skillRxHandler;
	@Inject
	protected SkillCategoryRxHandler skillCategoryRxHandler;
	@Inject
	protected SkillListAdapter listAdapter;
	@Inject
	protected SkillCategorySpinnerAdapter skillCategorySpinnerAdapter;
	private ListView listView;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private Spinner skillCategorySpinner;
	private Skill currentInstance = new Skill();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.skills_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initSkillCategorySpinner(layout);
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

	private boolean copyViewsToItem() {
		boolean changed = false;

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
			if(!skillCategory.equals(currentInstance.getCategory())) {
				currentInstance.setCategory(skillCategory);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		skillCategorySpinner.setSelection(skillCategorySpinnerAdapter.getPosition(currentInstance.getCategory()));

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
						Log.e("SkillFragment", "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), getString(R.string.toast_skill_delete_failed), Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getActivity(), getString(R.string.toast_skill_deleted), Toast.LENGTH_SHORT).show();
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
							Log.e("SkillsFragment", "Exception saving Skill", e);
							String toastString = getString(R.string.toast_skill_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
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
								String toastString;
								toastString = getString(R.string.toast_skill_saved);
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

	private void initSkillCategorySpinner(View layout) {
		skillCategorySpinner = (Spinner)layout.findViewById(R.id.skill_category_spinner);
		skillCategorySpinner.setAdapter(skillCategorySpinnerAdapter);

		skillCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SkillCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SkillsFragment", "Exception caught getting all SkillCategory instances", e);
					}
					@Override
					public void onNext(Collection<SkillCategory> skillCategories) {
						skillCategorySpinnerAdapter.addAll(skillCategories);
						skillCategorySpinnerAdapter.notifyDataSetChanged();
						if(currentInstance.getCategory() != null) {
							skillCategorySpinner.setSelection(skillCategorySpinnerAdapter.getPosition(currentInstance.getCategory()));
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

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SkillsFragment", "Exception caught getting all CriticalCode instances", e);
						Toast.makeText(SkillsFragment.this.getActivity(),
								getString(R.string.toast_skills_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						listAdapter.clear();
						listAdapter.addAll(skills);
						listAdapter.notifyDataSetChanged();
						if(skills.size() > 0) {
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							copyItemToViews();
						}
						String toastString;
						toastString = String.format(getString(R.string.toast_skills_loaded), skills.size());
						Toast.makeText(SkillsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

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
}

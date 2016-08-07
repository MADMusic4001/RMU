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
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillCategoryListAdapter;
import com.madinnovations.rmu.view.adapters.common.StatSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

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
		initStat1Spinner(layout);
		initStat2Spinner(layout);
		initStat3Spinner(layout);
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
			copyItemToControls();
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
				copyItemToControls();
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
			currentInstance.setName(value);
			changed = true;
		}

		Stat newStat = null;
		if(stat1Spinner.getSelectedItemPosition() >= 0) {
			newStat = stat1SpinnerAdapter.getItem(stat1Spinner.getSelectedItemPosition());
		}
		if((newStat == null && currentInstance.getStat1() != null) ||
				(newStat != null && !newStat.equals(currentInstance.getStat1()))) {
			currentInstance.setStat1(newStat);
			changed = true;
		}

		newStat = null;
		if(stat2Spinner.getSelectedItemPosition() >= 0) {
			newStat = stat2SpinnerAdapter.getItem(stat2Spinner.getSelectedItemPosition());
		}
		if((newStat == null && currentInstance.getStat1() != null) ||
				(newStat != null && !newStat.equals(currentInstance.getStat2()))) {
			currentInstance.setStat2(newStat);
			changed = true;
		}

		newStat = null;
		if(stat3Spinner.getSelectedItemPosition() >= 0) {
			newStat = stat3SpinnerAdapter.getItem(stat3Spinner.getSelectedItemPosition());
		}
		if((newStat == null && currentInstance.getStat1() != null) ||
				(newStat != null && !newStat.equals(currentInstance.getStat3()))) {
			currentInstance.setStat3(newStat);
			changed = true;
		}

		return changed;
	}

	private void copyItemToControls() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		stat1Spinner.setSelection(stat1SpinnerAdapter.getPosition(currentInstance.getStat1()));
		stat2Spinner.setSelection(stat2SpinnerAdapter.getPosition(currentInstance.getStat2()));
		stat3Spinner.setSelection(stat3SpinnerAdapter.getPosition(currentInstance.getStat3()));

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
							copyItemToControls();
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
						Log.e("SkillCategoriesFrag", "Exception caught getting all Stat instances", e);
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
						Log.e("SkillCategoriesFrag", "Exception caught getting all Stat instances", e);
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

	private void initStat3Spinner(View layout) {
		stat3Spinner = (Spinner)layout.findViewById(R.id.stat3_spinner);
		stat3Spinner.setAdapter(stat3SpinnerAdapter);

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
						stat3SpinnerAdapter.clear();
						stat3SpinnerAdapter.addAll(items);
						stat3SpinnerAdapter.notifyDataSetChanged();
					}
				});

		stat3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getStat3() == null || stat3SpinnerAdapter.getPosition(currentInstance.getStat3()) != position) {
					currentInstance.setStat3(stat3SpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getStat3() != null) {
					currentInstance.setStat3(null);
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
					public void onCompleted() {}
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
				copyItemToControls();
			}
		});
		registerForContextMenu(listView);
	}
}

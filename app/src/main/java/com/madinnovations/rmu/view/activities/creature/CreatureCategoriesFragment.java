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
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureCategoryRxHandler;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.creature.CreatureCategoryListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature categories.
 */
public class CreatureCategoriesFragment extends Fragment {
	@Inject
	protected CreatureCategoryRxHandler   creatureCategoryRxHandler;
	@Inject
	protected CreatureCategoryListAdapter listAdapter;
	private   ListView                    listView;
	private   EditText                    nameEdit;
	private   EditText                    descriptionEdit;
	private CreatureCategory selectedInstance = null;
	private boolean          dirty            = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.name_descriptions_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		creatureCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureCategory>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("CreatureCategoriesFrag",
							  "Exception caught getting all CreatureCategory instances in onCreateView", e);
						Toast.makeText(CreatureCategoriesFragment.this.getActivity(),
									   getString(R.string.toast_creature_categories_load_failed),
									   Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<CreatureCategory> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_categories_loaded), creatureCategories.size());
						Toast.makeText(CreatureCategoriesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.creature_categories_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_category) {
			newCreatureCategory();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.creature_category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureCategory creatureCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_category:
				newCreatureCategory();
				return true;
			case R.id.context_delete_creature_category:
				creatureCategory = (CreatureCategory)listView.getItemAtPosition(info.position);
				if(creatureCategory != null) {
					creatureCategoryRxHandler.deleteById(creatureCategory.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("CreatureCategoriesFrag", "Exception when deleting: " + creatureCategory, e);
									String toastString = getString(R.string.toast_creature_category_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(creatureCategory);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_creature_category_deleted);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
								}
							});
					return true;
				}
				else {
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
	}

	private void newCreatureCategory() {
		CreatureCategory creatureCategory = new CreatureCategory();
		creatureCategory.setName(getString(R.string.default_creature_category_name));
		creatureCategory.setDescription(getString(R.string.default_creature_category_description));
		creatureCategoryRxHandler.save(creatureCategory)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<CreatureCategory>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("CreatureCategoriesFrag", "Exception saving new CreatureCategory in onOptionsItemSelected", e);
					}

					@Override
					public void onNext(CreatureCategory savedCreatureCategory) {
						listAdapter.add(savedCreatureCategory);
						nameEdit.setText(savedCreatureCategory.getName());
						descriptionEdit.setText(savedCreatureCategory.getDescription());
						selectedInstance = savedCreatureCategory;
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
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getName())) {
					dirty = true;
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (selectedInstance != null && !newName.equals(selectedInstance.getName())) {
						selectedInstance.setName(newName);
						creatureCategoryRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<CreatureCategory>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("CreatureCategoriesFrag", "Save failed for: " + selectedInstance, e);
										String toastString = getString(R.string.toast_creature_category_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(CreatureCategory savedCreatureCategory) {
										onSaved(savedCreatureCategory);
									}
								});
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
				dirty = true;
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_description_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getDescription())) {
					dirty = true;
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (selectedInstance != null && !newDescription.equals(selectedInstance.getDescription())) {
						selectedInstance.setDescription(newDescription);
						creatureCategoryRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<CreatureCategory>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("CreatureCategoriesFrag",
											  "Exception saving new CreatureCategory in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_creature_category_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(CreatureCategory savedCreatureCategory) {
										onSaved(savedCreatureCategory);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(CreatureCategory creatureCategory) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_creature_category_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(creatureCategory);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(creatureCategory.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(creatureCategory.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && selectedInstance != null) {
					selectedInstance.setName(nameEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					creatureCategoryRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<CreatureCategory>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("CreatureCategoriesFrag", "Exception saving new CreatureCategory in initListView", e);
									String toastString = getString(R.string.toast_creature_category_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(CreatureCategory savedCreatureCategory) {
									onSaved(savedCreatureCategory);
								}
							});
				}

				selectedInstance = (CreatureCategory) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}

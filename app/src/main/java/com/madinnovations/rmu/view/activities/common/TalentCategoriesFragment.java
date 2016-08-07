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
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.TalentCategoryListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for talent categories.
 */
public class TalentCategoriesFragment extends Fragment {
	@Inject
	protected TalentCategoryRxHandler   talentCategoryRxHandler;
	@Inject
	protected TalentCategoryListAdapter listAdapter;
	private   ListView                  listView;
	private   EditText                  nameEdit;
	private   EditText                  descriptionEdit;
	private TalentCategory currentInstance = new TalentCategory();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talent_categories_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
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
		inflater.inflate(R.menu.talent_categories_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent_category) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new TalentCategory();
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
		getActivity().getMenuInflater().inflate(R.menu.talent_category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final TalentCategory talentCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_talent_category:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new TalentCategory();
				isNew = true;
				copyItemToControls();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_talent_category:
				talentCategory = (TalentCategory) listView.getItemAtPosition(info.position);
				if(talentCategory != null) {
					deleteItem(talentCategory);
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

	private void copyItemToControls() {
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());

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
			talentCategoryRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<TalentCategory>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("TalentCategoriesFrag", "Exception saving new TalentCategory: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_talent_category_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(TalentCategory savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_talent_category_saved), Toast.LENGTH_SHORT).show();
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

	private void deleteItem(@NonNull final TalentCategory item) {
		talentCategoryRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentCategoriesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_talent_category_delete_failed);
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
								currentInstance = new TalentCategory();
								isNew = true;
							}
							copyItemToControls();
							Toast.makeText(getActivity(), getString(R.string.toast_talent_category_deleted), Toast.LENGTH_SHORT).show();
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

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("TalentCategoryFragment", "Exception caught getting all TalentCategory instances", e);
						Toast.makeText(TalentCategoriesFragment.this.getActivity(),
								getString(R.string.toast_talent_categories_load_failed),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						listAdapter.clear();
						listAdapter.addAll(talentCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_talent_categories_loaded), talentCategories.size());
						Toast.makeText(TalentCategoriesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (TalentCategory) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new TalentCategory();
					isNew = true;
				}
				copyItemToControls();
			}
		});
		registerForContextMenu(listView);
	}
}

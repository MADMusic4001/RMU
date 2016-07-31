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
public class TalentCategoryFragment extends Fragment {
	@Inject
	protected TalentCategoryRxHandler   talentCategoryRxHandler;
	@Inject
	protected TalentCategoryListAdapter listAdapter;
	private   ListView                  listView;
	private   EditText                  nameEdit;
	private   EditText                  descriptionEdit;
	private TalentCategory selectedInstance = null;
	private boolean dirty = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.name_descriptions_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("TalentCategoryFragment", "Exception caught getting all TalentCategory instances in onCreateView", e);
						Toast.makeText(TalentCategoryFragment.this.getActivity(),
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
						Toast.makeText(TalentCategoryFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.e("TalentCategoryFragment", "Creating options menu");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.talent_categories_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent_category) {
			TalentCategory talentCategory = new TalentCategory();
			talentCategory.setName(getString(R.string.default_talent_category_name));
			talentCategory.setDescription(getString(R.string.default_talent_category_description));
			talentCategoryRxHandler.save(talentCategory)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<TalentCategory>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("TalentCategoryFragment", "Exception saving new TalentCategory in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(TalentCategory savedTalentCategory) {
							listAdapter.add(savedTalentCategory);
							nameEdit.setText(savedTalentCategory.getName());
							descriptionEdit.setText(savedTalentCategory.getDescription());
							selectedInstance = savedTalentCategory;
						}
					});
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
			case R.id.context_delete_talent_category:
				talentCategory = (TalentCategory)listView.getItemAtPosition(info.position);
				if(talentCategory != null) {
					talentCategoryRxHandler.deleteById(talentCategory.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("TalentCategoryFragment", "Exception when deleting: " + talentCategory, e);
									String toastString = getString(R.string.toast_talent_category_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(talentCategory);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_talent_category_deleted);
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
						talentCategoryRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<TalentCategory>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("TalentCategoryFragment", "Save failed for: " + selectedInstance, e);
									String toastString = getString(R.string.toast_talent_category_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(TalentCategory savedTalentCategory) {
									onSaved(savedTalentCategory);
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
						talentCategoryRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<TalentCategory>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("TalentCategoryFragment", "Exception saving new TalentCategory in initDescriptionEdit", e);
									String toastString = getString(R.string.toast_talent_category_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(TalentCategory savedTalentCategory) {
									onSaved(savedTalentCategory);
								}
							});
					}
				}
			}
		});
	}

	private void onSaved(TalentCategory talentCategory) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_talent_category_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(talentCategory);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(talentCategory.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(talentCategory.getDescription());
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
					talentCategoryRxHandler.save(selectedInstance)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<TalentCategory>() {
							@Override
							public void onCompleted() {
							}
							@Override
							public void onError(Throwable e) {
								Log.e("TalentCategoryFragment", "Exception saving new TalentCategory in initListView", e);
								String toastString = getString(R.string.toast_talent_category_save_failed);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onNext(TalentCategory savedTalentCategory) {
								onSaved(savedTalentCategory);
							}
						});
				}

				selectedInstance = (TalentCategory) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}

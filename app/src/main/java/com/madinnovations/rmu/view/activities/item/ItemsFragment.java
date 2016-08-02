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
package com.madinnovations.rmu.view.activities.item;

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
import com.madinnovations.rmu.controller.rxhandler.item.ItemRxHandler;
import com.madinnovations.rmu.data.entities.object.Item;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.item.ItemListAdapter;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature categories.
 */
public class ItemsFragment extends Fragment {
	@Inject
	protected ItemRxHandler   itemRxHandler;
	@Inject
	protected ItemListAdapter listAdapter;
	private   ListView        listView;
	private   EditText        nameEdit;
	private   EditText        descriptionEdit;
	private   EditText        weightEdit;
	private Item selectedInstance = null;
	private boolean          dirty            = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newItemFragmentComponent(new ItemFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.items_fragment, container, false);

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initWeightEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		itemRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Item>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("ItemsFragment",
							  "Exception caught getting all Item instances in onCreateView", e);
						Toast.makeText(ItemsFragment.this.getActivity(),
									   getString(R.string.toast_items_load_failed),
									   Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<Item> creatureCategories) {
						listAdapter.clear();
						listAdapter.addAll(creatureCategories);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_items_loaded), creatureCategories.size());
						Toast.makeText(ItemsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.items_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_item) {
			newItem();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem) {
		final Item item;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();

		switch (menuItem.getItemId()) {
			case R.id.context_new_item:
				newItem();
				return true;
			case R.id.context_delete_item:
				item = (Item)listView.getItemAtPosition(info.position);
				if(item != null) {
					itemRxHandler.deleteById(item.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("ItemsFragment", "Exception when deleting: " + item, e);
									String toastString = getString(R.string.toast_item_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(item);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_item_deleted);
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
				return super.onContextItemSelected(menuItem);
		}
	}

	private void newItem() {
		Item item = new Item();
		item.setName(getString(R.string.default_item_name));
		item.setDescription(getString(R.string.default_item_description));
		item.setWeight(getActivity().getResources().getInteger(R.integer.default_item_weight));
		itemRxHandler.save(item)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Item>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {
					Log.e("ItemsFragment", "Exception saving new Item in onOptionsItemSelected", e);
				}

				@Override
				public void onNext(Item savedItem) {
					listAdapter.add(savedItem);
					nameEdit.setText(savedItem.getName());
					descriptionEdit.setText(savedItem.getDescription());
					weightEdit.setText(String.valueOf(savedItem.getWeight()));
					selectedInstance = savedItem;
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
						itemRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Item>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("ItemsFragment", "Save failed for: " + selectedInstance, e);
										String toastString = getString(R.string.toast_item_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Item savedItem) {
										onSaved(savedItem);
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
						itemRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Item>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("ItemsFragment",
											  "Exception saving new Item in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_item_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Item savedItem) {
										onSaved(savedItem);
									}
								});
					}
				}
			}
		});
	}

	private void initWeightEdit(View layout) {
		weightEdit = (EditText)layout.findViewById(R.id.weight_edit);
		weightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				dirty = true;
				if (editable.length() == 0 && weightEdit != null) {
					weightEdit.setError(getString(R.string.validation_weight_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(String.valueOf(selectedInstance.getWeight()))) {
					dirty = true;
				}
			}
		});
		weightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					Integer newWeight = null;
					if(weightEdit.getText().length() > 0) {
						newWeight = Integer.valueOf(weightEdit.getText().toString());
					}
					if (selectedInstance != null && newWeight != null &&
							!String.valueOf(selectedInstance.getWeight()).equals(newWeight)) {
						selectedInstance.setWeight(newWeight);
						itemRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<Item>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("ItemsFragment",
											  "Exception saving new Item in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_item_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(Item savedItem) {
										onSaved(savedItem);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(Item item) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_item_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(item);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(item.getName());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(item.getDescription());
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
					selectedInstance.setWeight(Integer.valueOf(weightEdit.getText().toString()));
					itemRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Item>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("ItemsFragment", "Exception saving new Item in initListView", e);
									String toastString = getString(R.string.toast_item_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(Item savedItem) {
									onSaved(savedItem);
								}
							});
				}

				selectedInstance = (Item) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					nameEdit.setText(selectedInstance.getName());
					descriptionEdit.setText(selectedInstance.getDescription());
					weightEdit.setText(String.valueOf(selectedInstance.getWeight()));
				}
			}
		});
		registerForContextMenu(listView);
	}
}

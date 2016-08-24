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
package com.madinnovations.rmu.view.activities.spell;

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
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListTypeRxHandler;
import com.madinnovations.rmu.data.entities.spells.SpellListType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.SpellFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class SpellListTypesFragment extends Fragment implements TwoFieldListAdapter.GetValues<SpellListType> {
	@Inject
	protected SpellListTypeRxHandler spellListTypeRxHandler;
	private TwoFieldListAdapter<SpellListType> listAdapter;
	private ListView              listView;
	private EditText              nameEdit;
	private EditText              descriptionEdit;
	private SpellListType currentInstance = new SpellListType();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newSpellFragmentComponent(new SpellFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.spell_list_types_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_spell_list_type_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field2).getLayoutParams()).weight = 4;
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_spell_list_type_description));

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
		inflater.inflate(R.menu.spell_list_types_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_spell_list_type) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new SpellListType();
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
		getActivity().getMenuInflater().inflate(R.menu.spell_list_type_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final SpellListType spellListType;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_spell_list_type:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new SpellListType();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_spell_list_type:
				spellListType = (SpellListType)listView.getItemAtPosition(info.position);
				if(spellListType != null) {
					deleteItem(spellListType);
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
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final SpellListType item) {
		spellListTypeRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("SpellListTypeFragment", "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_spell_list_type_delete_failed), Toast.LENGTH_SHORT).show();
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
							currentInstance = new SpellListType();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_spell_list_type_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			spellListTypeRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<SpellListType>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpellListTypesFragment", "Exception saving SpellListType", e);
						String toastString = getString(R.string.toast_spell_list_type_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(SpellListType savedSpellListType) {
						if (wasNew) {
							listAdapter.add(savedSpellListType);
							if(savedSpellListType == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedSpellListType));
								listView.setItemChecked(listAdapter.getPosition(savedSpellListType), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_spell_list_type_saved);
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
					nameEdit.setError(getString(R.string.validation_spell_list_type_name_required));
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
					descriptionEdit.setError(getString(R.string.validation_spell_list_type_description_required));
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
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 4, this);
		listView.setAdapter(listAdapter);

		spellListTypeRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<SpellListType>>() {
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
					Log.e("SpellListTypesFragment", "Exception caught getting all SpellListType instances", e);
					Toast.makeText(SpellListTypesFragment.this.getActivity(),
							getString(R.string.toast_spell_list_types_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<SpellListType> spellListTypes) {
					listAdapter.clear();
					listAdapter.addAll(spellListTypes);
					listAdapter.notifyDataSetChanged();
					if(spellListTypes.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_spell_list_types_loaded), spellListTypes.size());
					Toast.makeText(SpellListTypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (SpellListType) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new SpellListType();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(SpellListType spellListType) {
		return spellListType.getName();
	}

	@Override
	public CharSequence getField2Value(SpellListType spellListType) {
		return spellListType.getDescription();
	}
}

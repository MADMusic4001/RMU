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
import com.madinnovations.rmu.controller.rxhandler.spell.SpellTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellTypeRxHandler;
import com.madinnovations.rmu.data.entities.spells.SpellType;
import com.madinnovations.rmu.data.entities.spells.SpellType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.ThreeFieldListAdapter;
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
public class SpellTypesFragment extends Fragment implements ThreeFieldListAdapter.GetValues<SpellType> {
	@Inject
	protected SpellTypeRxHandler               spellTypeRxHandler;
	private   ThreeFieldListAdapter<SpellType> listAdapter;
	private   ListView                            listView;
	private   EditText                            nameEdit;
	private   EditText                            codeEdit;
	private   EditText                            descriptionEdit;
	private SpellType currentInstance = new SpellType();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newSpellFragmentComponent(new SpellFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.spell_types_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_spell_type_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_spell_type_code));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field3).getLayoutParams()).weight = 4;
		((TextView)layout.findViewById(R.id.header_field3)).setText(getString(R.string.label_spell_type_description));

		initNameEdit(layout);
		initCodeEdit(layout);
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
		inflater.inflate(R.menu.spell_types_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_spell_type) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new SpellType();
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
		getActivity().getMenuInflater().inflate(R.menu.spell_type_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final SpellType spellType;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_spell_type:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new SpellType();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_spell_type:
				spellType = (SpellType)listView.getItemAtPosition(info.position);
				if(spellType != null) {
					deleteItem(spellType);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(SpellType spellType) {
		return spellType.getName();
	}

	@Override
	public CharSequence getField2Value(SpellType spellType) {
		return spellType.getCode().toString();
	}

	@Override
	public CharSequence getField3Value(SpellType spellType) {
		return spellType.getDescription();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String stringValue;

		stringValue = nameEdit.getText().toString();
		if(stringValue.isEmpty()) {
			stringValue = null;
		}
		if((stringValue == null && currentInstance.getName() != null) ||
				(stringValue != null && !stringValue.equals(currentInstance.getName()))) {
			currentInstance.setName(stringValue);
			changed = true;
		}

		stringValue = codeEdit.getText().toString();
		if(stringValue.isEmpty()) {
			stringValue = null;
		}
		if((stringValue == null && currentInstance.getCode() != null) ||
				(stringValue != null && stringValue.charAt(0) != currentInstance.getCode())) {
			if(stringValue != null) {
				currentInstance.setCode(stringValue.charAt(0));
			}
			else {
				currentInstance.setCode(null);
			}
			changed = true;
		}

		stringValue = descriptionEdit.getText().toString();
		if(stringValue.isEmpty()) {
			stringValue = null;
		}
		if((stringValue == null && currentInstance.getDescription() != null) ||
				(stringValue != null && !stringValue.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(stringValue);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		if(currentInstance.getCode() != null) {
			codeEdit.setText(currentInstance.getCode().toString());
		}
		else {
			codeEdit.setText(null);
		}
		descriptionEdit.setText(currentInstance.getDescription());

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getCode() != null) {
			descriptionEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final SpellType item) {
		spellTypeRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpellTypeFragment", "Exception when deleting: " + item, e);
						Toast.makeText(getActivity(), getString(R.string.toast_spell_type_delete_failed), Toast.LENGTH_SHORT).show();
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
								currentInstance = new SpellType();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_spell_type_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			spellTypeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<SpellType>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("SpellTypesFragment", "Exception saving SpellType", e);
							String toastString = getString(R.string.toast_spell_type_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(SpellType savedSpellType) {
							if (wasNew) {
								listAdapter.add(savedSpellType);
								if(savedSpellType == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedSpellType));
									listView.setItemChecked(listAdapter.getPosition(savedSpellType), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								String toastString;
								toastString = getString(R.string.toast_spell_type_saved);
								Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

								int position = listAdapter.getPosition(currentInstance);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(currentInstance.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(currentInstance.getCode().toString());
									textView = (TextView) v.findViewById(R.id.row_field3);
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
				if (editable.length() == 0) {
					nameEdit.setError(getString(R.string.validation_spell_type_name_required));
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

	private void initCodeEdit(View layout) {
		codeEdit = (EditText)layout.findViewById(R.id.code_edit);
		codeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					codeEdit.setError(getString(R.string.validation_spell_type_code_required));
				}
			}
		});
		codeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(codeEdit.getText().length() > 0) {
						char newCode = codeEdit.getText().toString().charAt(0);
						if (currentInstance.getCode() == null || newCode != (currentInstance.getCode())) {
							currentInstance.setCode(newCode);
							saveItem();
						}
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
					descriptionEdit.setError(getString(R.string.validation_spell_type_description_required));
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

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new ThreeFieldListAdapter<>(this.getActivity(), 1, 1, 4, this);
		listView.setAdapter(listAdapter);

		spellTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<SpellType>>() {
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
						Log.e("SpellTypesFragment", "Exception caught getting all SpellType instances", e);
						Toast.makeText(SpellTypesFragment.this.getActivity(),
									   getString(R.string.toast_spell_types_load_failed),
									   Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<SpellType> spellTypes) {
						listAdapter.clear();
						listAdapter.addAll(spellTypes);
						listAdapter.notifyDataSetChanged();
						if(spellTypes.size() > 0) {
							listView.setSelection(0);
							listView.setItemChecked(0, true);
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							copyItemToViews();
						}
						String toastString;
						toastString = String.format(getString(R.string.toast_spell_types_loaded), spellTypes.size());
						Toast.makeText(SpellTypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (SpellType) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new SpellType();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListTypeRxHandler;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.data.entities.spells.SpellList;
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
public class SpellListsFragment extends Fragment implements TwoFieldListAdapter.GetValues<SpellList> {
	private static final String LOG_TAG = "SpellListsFragment";
	@Inject
	protected ProfessionRxHandler            professionRxHandler;
	@Inject
	protected RealmRxHandler                 realmRxHandler;
	@Inject
	protected SpellListTypeRxHandler         spellListTypeRxHandler;
	@Inject
	protected SpellListRxHandler             spellTypeRxHandler;
	private   ArrayAdapter<Profession>       professionSpinnerAdapter;
	private   ArrayAdapter<Realm>            realm1SpinnerAdapter;
	private   ArrayAdapter<Realm>            realm2SpinnerAdapter;
	private   ArrayAdapter<SpellListType>    spellListTypeSpinnerAdapter;
	private   TwoFieldListAdapter<SpellList> listAdapter;
	private   ListView                       listView;
	private   EditText                       nameEdit;
	private   EditText                       notesEdit;
	private   Spinner                        realm1Spinner;
	private   Spinner                        realm2Spinner;
	private   Spinner                        professionSpinner;
	private   Spinner                        spellListTypeSpinner;
	private   SpellList currentInstance = new SpellList();
	private   boolean isNew = true;
	private   Collection<Realm>  realms = null;
	private   Realm noRealm = new Realm();
	private   Profession noProfession = new Profession();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newSpellFragmentComponent(new SpellFragmentModule(this)).injectInto(this);

		noRealm.setName(getString(R.string.label_no_realm));
		noProfession.setName(getString(R.string.label_no_profession));

		View layout = inflater.inflate(R.layout.spell_lists_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_spell_list_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_spell_list_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initRealm1Spinner(layout);
		initRealm2Spinner(layout);
		initProfessionSpinner(layout);
		initSpellListTypeSpinner(layout);
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
		inflater.inflate(R.menu.spell_lists_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_spell_list) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new SpellList();
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
		getActivity().getMenuInflater().inflate(R.menu.spell_list_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final SpellList spellType;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_spell_list:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new SpellList();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_spell_list:
				spellType = (SpellList)listView.getItemAtPosition(info.position);
				if(spellType != null) {
					deleteItem(spellType);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(SpellList spellList) {
		return spellList.getName();
	}

	@Override
	public CharSequence getField2Value(SpellList spellList) {
		return spellList.getNotes();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		int position;
		String stringValue;
		Realm realm;
		Profession profession;
		SpellListType spellListType;

		stringValue = nameEdit.getText().toString();
		if(stringValue.isEmpty()) {
			stringValue = null;
		}
		if((stringValue == null && currentInstance.getName() != null) ||
				(stringValue != null && !stringValue.equals(currentInstance.getName()))) {
			currentInstance.setName(stringValue);
			changed = true;
		}

		stringValue = notesEdit.getText().toString();
		if(stringValue.isEmpty()) {
			stringValue = null;
		}
		if((stringValue == null && currentInstance.getNotes() != null) ||
				(stringValue != null && !stringValue.equals(currentInstance.getNotes()))) {
			currentInstance.setNotes(stringValue);
			changed = true;
		}

		position = realm1Spinner.getSelectedItemPosition();
		if(position != -1) {
			realm = realm1SpinnerAdapter.getItem(position);
			if (realm != null && !realm.equals(currentInstance.getRealm())) {
				currentInstance.setRealm(realm);
				changed = true;
			}
		}

		position = realm2Spinner.getSelectedItemPosition();
		if(position != -1) {
			realm = realm2SpinnerAdapter.getItem(position);
			if(realm != null) {
				if (realm.getId() == -1 && currentInstance.getRealm2() != null) {
					currentInstance.setRealm2(null);
					changed = true;
				} else if (realm.getId() != -1 && !realm.equals(currentInstance.getRealm2())) {
					currentInstance.setRealm2(realm);
					changed = true;
				}
			}
		}

		position = professionSpinner.getSelectedItemPosition();
		if(position != -1) {
			profession = professionSpinnerAdapter.getItem(position);
			if(profession != null) {
				if (profession.getId() == -1 && currentInstance.getProfession() != null) {
					currentInstance.setProfession(null);
					changed = true;
				} else if (profession.getId() != -1 && !profession.equals(currentInstance.getProfession())) {
					currentInstance.setProfession(profession);
					changed = true;
				}
			}
		}

		position = spellListTypeSpinner.getSelectedItemPosition();
		if(position != -1) {
			spellListType = spellListTypeSpinnerAdapter.getItem(position);
			if (spellListType != null && !spellListType.equals(currentInstance.getSpellListType())) {
				currentInstance.setSpellListType(spellListType);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		notesEdit.setText(currentInstance.getNotes());
		realm1Spinner.setSelection(realm1SpinnerAdapter.getPosition(currentInstance.getRealm()));
		if(currentInstance.getRealm2() == null) {
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(noRealm));
		}
		else {
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(currentInstance.getRealm2()));
		}
		if(currentInstance.getProfession() == null) {
			professionSpinner.setSelection(professionSpinnerAdapter.getPosition(noProfession));
		}
		else {
			professionSpinner.setSelection(professionSpinnerAdapter.getPosition(currentInstance.getProfession()));
		}
		spellListTypeSpinner.setSelection(spellListTypeSpinnerAdapter.getPosition(currentInstance.getSpellListType()));

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getNotes() != null && !currentInstance.getNotes().isEmpty()) {
			notesEdit.setError(null);
		}
	}

	private void deleteItem(final SpellList item) {
		spellTypeRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(LOG_TAG, "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_spell_list_delete_failed), Toast.LENGTH_SHORT).show();
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
							currentInstance = new SpellList();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_spell_list_deleted), Toast.LENGTH_SHORT).show();
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
				.subscribe(new Subscriber<SpellList>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception saving SpellList", e);
						String toastString = getString(R.string.toast_spell_list_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(SpellList savedSpellList) {
						if (wasNew) {
							listAdapter.add(savedSpellList);
							if(savedSpellList == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedSpellList));
								listView.setItemChecked(listAdapter.getPosition(savedSpellList), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_spell_list_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(currentInstance.getName());
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getNotes());
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
					nameEdit.setError(getString(R.string.validation_spell_list_name_required));
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
		notesEdit = (EditText)layout.findViewById(R.id.notes_edit);
		notesEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && notesEdit != null) {
					notesEdit.setError(getString(R.string.validation_spell_list_description_required));
				}
			}
		});
		notesEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = notesEdit.getText().toString();
					if (currentInstance != null && !newDescription.equals(currentInstance.getNotes())) {
						currentInstance.setNotes(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initRealm1Spinner(View layout) {
		realm1Spinner = (Spinner)layout.findViewById(R.id.realm1_spinner);
		realm1SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		realm1Spinner.setAdapter(realm1SpinnerAdapter);

		if(realms != null) {
			realm1SpinnerAdapter.clear();
			realm1SpinnerAdapter.addAll(realms);
			realm1SpinnerAdapter.notifyDataSetChanged();
		}
		else {
			realmRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Realm>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception caught getting all Realm instances", e);
						}
						@Override
						public void onNext(Collection<Realm> realms) {
							SpellListsFragment.this.realms = realms;
							realm1SpinnerAdapter.clear();
							realm1SpinnerAdapter.addAll(realms);
							realm1SpinnerAdapter.notifyDataSetChanged();
						}
					});
		}

		realm1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Realm newRealm = realm1SpinnerAdapter.getItem(position);
				if(newRealm != null && !newRealm.equals(currentInstance.getRealm())) {
					currentInstance.setRealm(newRealm);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initRealm2Spinner(View layout) {
		realm2Spinner = (Spinner)layout.findViewById(R.id.realm2_spinner);
		realm2SpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		realm2Spinner.setAdapter(realm2SpinnerAdapter);

		if(realms != null) {
			realm2SpinnerAdapter.clear();
			realm2SpinnerAdapter.add(noRealm);
			realm2SpinnerAdapter.addAll(realms);
			realm2SpinnerAdapter.notifyDataSetChanged();
		}
		else {
			realmRxHandler.getAll()
					.subscribe(new Subscriber<Collection<Realm>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(LOG_TAG, "Exception caught getting all Realm instances", e);
						}
						@Override
						public void onNext(Collection<Realm> realms) {
							SpellListsFragment.this.realms = realms;
							realm2SpinnerAdapter.clear();
							realm2SpinnerAdapter.add(noRealm);
							realm2SpinnerAdapter.addAll(realms);
							realm2SpinnerAdapter.notifyDataSetChanged();
						}
					});
		}

		realm2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Realm newRealm = realm2SpinnerAdapter.getItem(position);
				if(newRealm != null && !newRealm.equals(currentInstance.getRealm2())) {
					currentInstance.setRealm2(newRealm);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initProfessionSpinner(View layout) {
		professionSpinner = (Spinner)layout.findViewById(R.id.profession_spinner);
		professionSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		professionSpinner.setAdapter(professionSpinnerAdapter);

		professionRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Profession>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Profession instances", e);
					}
					@Override
					public void onNext(Collection<Profession> professions) {
						professionSpinnerAdapter.clear();
						professionSpinnerAdapter.add(noProfession);
						professionSpinnerAdapter.addAll(professions);
						professionSpinnerAdapter.notifyDataSetChanged();
					}
				});

		professionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Profession newProfession = professionSpinnerAdapter.getItem(position);
				if(newProfession != null && !newProfession.equals(currentInstance.getProfession())) {
					currentInstance.setProfession(newProfession);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSpellListTypeSpinner(View layout) {
		spellListTypeSpinner = (Spinner)layout.findViewById(R.id.spell_list_type_spinner);
		spellListTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		spellListTypeSpinner.setAdapter(spellListTypeSpinnerAdapter);

		spellListTypeRxHandler.getAll()
				.subscribe(new Subscriber<Collection<SpellListType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Profession instances", e);
					}
					@Override
					public void onNext(Collection<SpellListType> spellListTypes) {
						spellListTypeSpinnerAdapter.clear();
						spellListTypeSpinnerAdapter.addAll(spellListTypes);
						spellListTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		spellListTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SpellListType newSpellListType = spellListTypeSpinnerAdapter.getItem(position);
				if(newSpellListType != null && !newSpellListType.equals(currentInstance.getSpellListType())) {
					currentInstance.setSpellListType(newSpellListType);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		spellTypeRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<SpellList>>() {
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
					Log.e(LOG_TAG, "Exception caught getting all SpellList instances", e);
					Toast.makeText(SpellListsFragment.this.getActivity(),
							getString(R.string.toast_spell_lists_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<SpellList> spellTypes) {
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
					toastString = String.format(getString(R.string.toast_spell_lists_loaded), spellTypes.size());
					Toast.makeText(SpellListsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (SpellList) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new SpellList();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}

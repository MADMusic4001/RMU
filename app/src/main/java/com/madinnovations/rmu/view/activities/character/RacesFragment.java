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

import android.app.ActionBar;
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
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class RacesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Race> {
	private static final String LOG_TAG = "RacesFragment";
	@Inject
	protected RaceRxHandler                raceRxHandler;
	@Inject
	protected RealmRxHandler               realmRxHandler;
	private   TwoFieldListAdapter<Race> listAdapter;
	private   ListView                     listView;
	private   EditText                     nameEdit;
	private   EditText                     descriptionEdit;
	private   EditText                     devPointsEdit;
	private   TextView[]                   rrTextViews;
	private   EditText[]                   rrEditViews;
	private   Collection<Realm> realms          = null;
	private   Race              currentInstance = new Race();
	private   boolean           isNew           = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_race_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_race_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initRRMods(layout);
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
		inflater.inflate(R.menu.races_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_race) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Race();
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
		getActivity().getMenuInflater().inflate(R.menu.race_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Race race;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_race:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Race();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_race:
				race = (Race)listView.getItemAtPosition(info.position);
				if(race != null) {
					deleteItem(race);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Race race) {
		return race.getName();
	}

	@Override
	public CharSequence getField2Value(Race race) {
		return race.getDescription();
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		short newShort;

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getName() != null) ||
				(newString != null && !newString.equals(currentInstance.getName()))) {
			currentInstance.setName(newString);
			changed = true;
		}

		newString = descriptionEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getDescription() != null) ||
				(newString != null && !newString.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newString);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}

		descriptionEdit.setText(currentInstance.getDescription());
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void deleteItem(final Race item) {
		raceRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("RaceFragment", "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_race_delete_failed), Toast.LENGTH_SHORT).show();
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
							currentInstance = new Race();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_race_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			raceRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Race>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("RacesFragment", "Exception saving Race", e);
						String toastString = getString(R.string.toast_race_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Race savedRace) {
						if (wasNew) {
							listAdapter.add(savedRace);
							if(savedRace == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedRace));
								listView.setItemChecked(listAdapter.getPosition(savedRace), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_race_saved);
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
				if (editable.length() == 0) {
					nameEdit.setError(getString(R.string.validation_race_name_required));
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
				if (editable.length() == 0) {
					descriptionEdit.setError(getString(R.string.validation_race_description_required));
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

	private void initRRMods(View layout) {
		final LinearLayout rrModLabels = (LinearLayout)layout.findViewById(R.id.rr_mod_labels_row);
		final LinearLayout rrModEdits = (LinearLayout)layout.findViewById(R.id.rr_mod_edits_row);

		realmRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Realm>>() {
					@Override
					public void onCompleted() {

					}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Realm instances", e);
					}
					@Override
					public void onNext(Collection<Realm> realms) {
						rrTextViews = new TextView[realms.size()];
						rrEditViews = new EditText[realms.size()];
						int index = 0;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT,
																					   1f);
						TextView textView;
						EditText editText;
						for(Realm realm : realms) {
							textView = new TextView(getActivity());
							textView.setLayoutParams(params);
							textView.setText(realm.getName());
							rrModLabels.addView(textView);
							rrTextViews[index] = textView;
							editText = new EditText(getActivity());
							editText.setHint(getString(R.string.hint_race_rr_mod));
							editText.setLayoutParams(params);
							rrModEdits.addView(editText);
							rrEditViews[index] = editText;
							index++;
						}
						textView = new TextView(getActivity());
						textView.setLayoutParams(params);
						textView.setText(getString(R.string.label_physical_rr));
						rrModLabels.addView(textView);
						editText = new EditText(getActivity());
						editText.setLayoutParams(params);
						editText.setHint(getString(R.string.hint_race_physical_rr));
						rrModEdits.addView(editText);
					}
				});
	}
	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		raceRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Race>>() {
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
					Log.e("RacesFragment", "Exception caught getting all Race instances", e);
					Toast.makeText(RacesFragment.this.getActivity(),
							getString(R.string.toast_races_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Race> races) {
					listAdapter.clear();
					listAdapter.addAll(races);
					listAdapter.notifyDataSetChanged();
					if(races.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_races_loaded), races.size());
					Toast.makeText(RacesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Race) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Race();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}

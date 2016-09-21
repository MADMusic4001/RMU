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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	protected RaceRxHandler       raceRxHandler;
	@Inject
	protected RealmRxHandler      realmRxHandler;
	@Inject
	protected SizeRxHandler       sizeRxHandler;
	@Inject
	protected StatRxHandler                statRxHandler;
	private   ArrayAdapter<Size>           sizeSpinnerAdapter;
	private   TwoFieldListAdapter<Race>    listAdapter;
	private   ListView                     listView;
	private   EditText                     nameEdit;
	private   EditText                     descriptionEdit;
	private   EditText                     devPointsEdit;
	private   EditText                     enduranceModEdit;
	private   EditText                     baseHitsEdit;
	private   EditText                     recoveryMultEdit;
	private   Spinner                      sizeSpinner;
	private   EditText                     strideModEdit;
	private   EditText                     averageHeightEdit;
	private   EditText                     averageWeightEdit;
	private   EditText                     poundsPerInchEdit;
	private   Map<Stat, EditText>          statEditViews;
	private   Map<Realm, EditText>         rrEditViews;
	private   EditText                     physicalRREdit;
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
		initDevPointsEdit(layout);
		initEnduranceModEdit(layout);
		initBaseHitsEdit(layout);
		initRecoveryMultEdit(layout);
		initSizeSpinner(layout);
		initStrideMod(layout);
		initAverageHeightEdit(layout);
		initAverageWeightEdit(layout);
		initPoundsPerInchEdit(layout);
		initStatMods(layout);
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
		Short oldShort;
		float newFloat;
		Size newSize;

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

		if(devPointsEdit.getText().length() > 0) {
			newShort = Short.valueOf(devPointsEdit.getText().toString());
			if(newShort != currentInstance.getBonusDevelopmentPoints()) {
				currentInstance.setBonusDevelopmentPoints(newShort);
				changed = true;
			}
		}

		if(enduranceModEdit.getText().length() > 0) {
			newShort = Short.valueOf(enduranceModEdit.getText().toString());
			if(newShort != currentInstance.getEnduranceModifier()) {
				currentInstance.setEnduranceModifier(newShort);
				changed = true;
			}
		}

		if(baseHitsEdit.getText().length() > 0) {
			newShort = Short.valueOf(baseHitsEdit.getText().toString());
			if(newShort != currentInstance.getBaseHits()) {
				currentInstance.setBaseHits(newShort);
				changed = true;
			}
		}

		if(recoveryMultEdit.getText().length() > 0) {
			newFloat = Float.valueOf(recoveryMultEdit.getText().toString());
			if(newFloat != currentInstance.getRecoveryMultiplier()) {
				currentInstance.setRecoveryMultiplier(newFloat);
				changed = true;
			}
		}

		if(sizeSpinner.getSelectedItemPosition() >= 0) {
			newSize = sizeSpinnerAdapter.getItem(sizeSpinner.getSelectedItemPosition());
			if (newSize != null && !newSize.equals(currentInstance.getSize())) {
				currentInstance.setSize(newSize);
				changed = true;
			}
		}

		if(strideModEdit.getText().length() > 0) {
			newShort = Short.valueOf(strideModEdit.getText().toString());
			if(newShort != currentInstance.getStrideModifier()) {
				currentInstance.setStrideModifier(newShort);
				changed = true;
			}
		}

		if(averageHeightEdit.getText().length() > 0) {
			newShort = Short.valueOf(averageHeightEdit.getText().toString());
			if(newShort != currentInstance.getAverageHeight()) {
				currentInstance.setAverageHeight(newShort);
				changed = true;
			}
		}

		if(averageWeightEdit.getText().length() > 0) {
			newShort = Short.valueOf(averageWeightEdit.getText().toString());
			if(newShort != currentInstance.getAverageWeight()) {
				currentInstance.setAverageWeight(newShort);
				changed = true;
			}
		}

		if(poundsPerInchEdit.getText().length() > 0) {
			newShort = Short.valueOf(poundsPerInchEdit.getText().toString());
			if(newShort != currentInstance.getPoundsPerInch()) {
				currentInstance.setPoundsPerInch(newShort);
				changed = true;
			}
		}

		for(Map.Entry<Stat, EditText> entry : statEditViews.entrySet()) {
			EditText editText = entry.getValue();
			oldShort = currentInstance.getStatModifiers().get(entry.getKey());
			if(editText.getText().length() > 0) {
				newShort = Short.valueOf(editText.getText().toString());
				if(oldShort == null || newShort != oldShort) {
					currentInstance.getStatModifiers().put(entry.getKey(), newShort);
					changed = true;
				}
			}
			else if(oldShort != null) {
				currentInstance.getStatModifiers().remove(entry.getKey());
				changed= true;
			}
		}

		for(Map.Entry<Realm, EditText> entry : rrEditViews.entrySet()) {
			EditText editText = entry.getValue();
			oldShort = currentInstance.getRealmResistancesModifiers().get(entry.getKey());
			if(editText.getText().length() > 0) {
				newShort = Short.valueOf(editText.getText().toString());
				if(oldShort == null || newShort != oldShort) {
					currentInstance.getRealmResistancesModifiers().put(entry.getKey(), newShort);
					changed = true;
				}
			}
			else if(oldShort != null) {
				currentInstance.getRealmResistancesModifiers().remove(entry.getKey());
				changed= true;
			}
		}

		if(physicalRREdit.getText().length() > 0) {
			newShort = Short.valueOf(physicalRREdit.getText().toString());
			if(newShort != currentInstance.getPhysicalResistanceModifier()) {
				currentInstance.setPhysicalResistanceModifier(newShort);
				changed = true;
			}
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

		devPointsEdit.setText(String.valueOf(currentInstance.getBonusDevelopmentPoints()));
		devPointsEdit.setError(null);

		enduranceModEdit.setText(String.valueOf(currentInstance.getEnduranceModifier()));
		enduranceModEdit.setError(null);

		baseHitsEdit.setText(String.valueOf(currentInstance.getBaseHits()));
		baseHitsEdit.setError(null);

		recoveryMultEdit.setText(String.valueOf(currentInstance.getRecoveryMultiplier()));
		recoveryMultEdit.setError(null);

		if(currentInstance.getSize() != null) {
			sizeSpinner.setSelection(sizeSpinnerAdapter.getPosition(currentInstance.getSize()));
		}

		strideModEdit.setText(String.valueOf(currentInstance.getStrideModifier()));
		strideModEdit.setError(null);

		averageHeightEdit.setText(String.valueOf(currentInstance.getAverageHeight()));
		averageHeightEdit.setError(null);

		averageWeightEdit.setText(String.valueOf(currentInstance.getAverageWeight()));
		averageWeightEdit.setError(null);

		poundsPerInchEdit.setText(String.valueOf(currentInstance.getPoundsPerInch()));
		poundsPerInchEdit.setError(null);

		for(Map.Entry<Stat, EditText> entry : statEditViews.entrySet()) {
			if(currentInstance.getStatModifiers().get(entry.getKey()) == null) {
				entry.getValue().setText(null);
			}
			else {
				entry.getValue().setText(String.valueOf(currentInstance.getStatModifiers().get(entry.getKey())));
			}
		}

		for(Map.Entry<Realm, EditText> entry : rrEditViews.entrySet()) {
			if(currentInstance.getRealmResistancesModifiers().get(entry.getKey()) == null) {
				entry.getValue().setText(null);
			}
			else {
				entry.getValue().setText(String.valueOf(currentInstance.getRealmResistancesModifiers().get(entry.getKey())));
			}
		}

		physicalRREdit.setText(String.valueOf(currentInstance.getPhysicalResistanceModifier()));
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

	private void initDevPointsEdit(View layout) {
		devPointsEdit = (EditText)layout.findViewById(R.id.dp_edit);
		devPointsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					devPointsEdit.setError(getString(R.string.validation_race_dev_points_required));
				}
			}
		});
		devPointsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(devPointsEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(devPointsEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getBonusDevelopmentPoints()) {
							currentInstance.setBonusDevelopmentPoints(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initEnduranceModEdit(View layout) {
		enduranceModEdit = (EditText)layout.findViewById(R.id.endurance_mod_edit);
		enduranceModEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					enduranceModEdit.setError(getString(R.string.validation_race_endurance_mod_required));
				}
			}
		});
		enduranceModEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(enduranceModEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(enduranceModEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getEnduranceModifier()) {
							currentInstance.setEnduranceModifier(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBaseHitsEdit(View layout) {
		baseHitsEdit = (EditText)layout.findViewById(R.id.base_hits_edit);
		baseHitsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					baseHitsEdit.setError(getString(R.string.validation_race_base_hits_required));
				}
			}
		});
		baseHitsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(baseHitsEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(baseHitsEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getBaseHits()) {
							currentInstance.setBaseHits(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initRecoveryMultEdit(View layout) {
		recoveryMultEdit = (EditText)layout.findViewById(R.id.recovery_mult_edit);
		recoveryMultEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					recoveryMultEdit.setError(getString(R.string.validation_race_recovery_mult_required));
				}
			}
		});
		recoveryMultEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(recoveryMultEdit.getText().length() > 0) {
						final float newFloat = Float.valueOf(recoveryMultEdit.getText().toString());
						if (currentInstance != null && newFloat != currentInstance.getRecoveryMultiplier()) {
							currentInstance.setRecoveryMultiplier(newFloat);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initSizeSpinner(View layout) {
		sizeSpinner = (Spinner)layout.findViewById(R.id.size_spinner);
		sizeSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		sizeSpinner.setAdapter(sizeSpinnerAdapter);

		sizeRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Size>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Size instances", e);
					}
					@Override
					public void onNext(Collection<Size> sizes) {
						sizeSpinnerAdapter.clear();
						sizeSpinnerAdapter.addAll(sizes);
						sizeSpinnerAdapter.notifyDataSetChanged();
					}
				});
		sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Size newSize = sizeSpinnerAdapter.getItem(position);
				if(newSize != null && !newSize.equals(currentInstance.getSize())) {
					currentInstance.setSize(newSize);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void initStrideMod(View layout) {
		strideModEdit = (EditText)layout.findViewById(R.id.stride_mod_edit);
		strideModEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					strideModEdit.setError(getString(R.string.validation_race_stride_mod_required));
				}
			}
		});
		strideModEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(strideModEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(strideModEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getStrideModifier()) {
							currentInstance.setStrideModifier(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initAverageHeightEdit(View layout) {
		averageHeightEdit = (EditText)layout.findViewById(R.id.average_height_edit);
		averageHeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					averageHeightEdit.setError(getString(R.string.validation_race_avg_height_required));
				}
			}
		});
		averageHeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(averageHeightEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(averageHeightEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getAverageHeight()) {
							currentInstance.setAverageHeight(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initAverageWeightEdit(View layout) {
		averageWeightEdit = (EditText)layout.findViewById(R.id.average_weight_edit);
		averageWeightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					averageWeightEdit.setError(getString(R.string.validation_race_avg_weight_required));
				}
			}
		});
		averageWeightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(averageWeightEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(averageWeightEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getAverageWeight()) {
							currentInstance.setAverageWeight(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initPoundsPerInchEdit(View layout) {
		poundsPerInchEdit = (EditText)layout.findViewById(R.id.pounds_per_inch_edit);
		poundsPerInchEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					poundsPerInchEdit.setError(getString(R.string.validation_race_pounds_per_inch_required));
				}
			}
		});
		poundsPerInchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(poundsPerInchEdit.getText().length() > 0) {
						final short newShort = Short.valueOf(poundsPerInchEdit.getText().toString());
						if (currentInstance != null && newShort != currentInstance.getPoundsPerInch()) {
							currentInstance.setPoundsPerInch(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initStatMods(View layout) {
		final LinearLayout statModLabels1 = (LinearLayout)layout.findViewById(R.id.stat_mod_labels_row1);
		final LinearLayout statModLabels2 = (LinearLayout)layout.findViewById(R.id.stat_mod_labels_row2);
		final LinearLayout statModEdits1 = (LinearLayout)layout.findViewById(R.id.stat_mod_edits_row1);
		final LinearLayout statModEdits2 = (LinearLayout)layout.findViewById(R.id.stat_mod_edits_row2);

		statRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all Stat instances", e);
					}
					@Override
					public void onNext(Collection<Stat> stats) {
						statEditViews = new HashMap<>(stats.size());
						int i = 0;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT,
								1f);
						int numStats = stats.size();
						for(Stat stat : stats) {
							if(numStats <= 6  || i < numStats/2) {
								initStatViews(stat, params, statModLabels1, statModEdits1);
							}
							else {
								initStatViews(stat, params, statModLabels2, statModEdits2);
							}
							i++;
						}
					}
				});
	}

	private void initStatViews(final Stat stat, LinearLayout.LayoutParams params, LinearLayout labelsRow, LinearLayout editsRow) {
		TextView textView = new TextView(getActivity());
		textView.setLayoutParams(params);
		textView.setText(stat.getName());

		final EditText editText = new EditText(getActivity());
		editText.setHint(getString(R.string.hint_race_stat_mod));
		editText.setLayoutParams(params);
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(currentInstance.getStatModifiers().get(stat) == null ||
							newShort != currentInstance.getStatModifiers().get(stat)) {
						currentInstance.getStatModifiers().put(stat, newShort);
						saveItem();
					}
				}
				else if(currentInstance.getStatModifiers().get(stat) != null) {
					currentInstance.getStatModifiers().remove(stat);
					saveItem();
				}
			}
		});
		statEditViews.put(stat, editText);
		labelsRow.addView(textView);
		editsRow.addView(editText);
	}

	private void initRRMods(View layout) {
		final LinearLayout rrModLabels = (LinearLayout)layout.findViewById(R.id.rr_mod_labels_row);
		final LinearLayout rrModEdits = (LinearLayout)layout.findViewById(R.id.rr_mod_edits_row);

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
						rrEditViews = new HashMap<>(realms.size());
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
																						 ActionBar.LayoutParams.WRAP_CONTENT,
																						 1f);
						TextView textView;
						for(Realm realm : realms) {
							initRealmViews(realm, params, rrModLabels, rrModEdits);
						}
						textView = new TextView(getActivity());
						textView.setLayoutParams(params);
						textView.setText(getString(R.string.label_physical_rr));
						rrModLabels.addView(textView);
						physicalRREdit = new EditText(getActivity());
						physicalRREdit.setLayoutParams(params);
						physicalRREdit.setHint(getString(R.string.hint_race_physical_rr));
						rrModEdits.addView(physicalRREdit);
						physicalRREdit.addTextChangedListener(new TextWatcher() {
							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {}
							@Override
							public void afterTextChanged(Editable s) {
								if (s.length() == 0) {
									physicalRREdit.setError(getString(R.string.validation_race_phyiscal_rr_required));
								}
							}
						});
						physicalRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if(physicalRREdit.getText().length() > 0) {
									short newShort = Short.valueOf(physicalRREdit.getText().toString());
									if(newShort != currentInstance.getPhysicalResistanceModifier()) {
										currentInstance.setPhysicalResistanceModifier(newShort);
										saveItem();
									}
								}
							}
						});
					}
				});
	}

	private void initRealmViews(final Realm realm, LinearLayout.LayoutParams params, LinearLayout labelsRow,
								LinearLayout editsRow) {
		TextView textView = new TextView(getActivity());
		textView.setLayoutParams(params);
		textView.setText(realm.getName());

		final EditText editText = new EditText(getActivity());
		editText.setHint(getString(R.string.hint_race_rr_mod));
		editText.setLayoutParams(params);
		editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText.getText().length() > 0) {
					short newShort = Short.valueOf(editText.getText().toString());
					if(currentInstance.getRealmResistancesModifiers().get(realm) == null ||
							newShort != currentInstance.getRealmResistancesModifiers().get(realm)) {
						currentInstance.getRealmResistancesModifiers().put(realm, newShort);
						saveItem();
					}
				}
				else if(currentInstance.getRealmResistancesModifiers().get(realm) != null) {
					currentInstance.getRealmResistancesModifiers().remove(realm);
					saveItem();
				}
			}
		});
		rrEditViews.put(realm, editText);
		labelsRow.addView(textView);
		editsRow.addView(editText);
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

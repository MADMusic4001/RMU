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
import android.support.annotation.IdRes;
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
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.utils.EditTextUtils;

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
public class RacesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Race>, EditTextUtils.ValuesCallback {
	private static final String TAG = "RacesFragment";
	@Inject
	protected RaceRxHandler       raceRxHandler;
	@Inject
	protected RealmRxHandler      realmRxHandler;
	@Inject
	protected SizeRxHandler       sizeRxHandler;
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
	private   Map<Statistic, EditText>     statEditViews;
	private   Map<Realm, EditText>         rrEditViews;
	private   EditText                     physicalRREdit;
	private   Race              currentInstance = new Race();
	private   boolean           isNew           = true;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_race_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_race_description));

		nameEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.name_edit,
										  R.string.validation_race_name_required);
		descriptionEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.notes_edit,
												 R.string.validation_race_description_required);
		devPointsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.dp_edit,
											   R.string.validation_race_dev_points_required);
		enduranceModEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.endurance_mod_edit,
												  R.string.validation_race_endurance_mod_required);
		baseHitsEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.base_hits_edit,
											  R.string.validation_race_base_hits_required);
		recoveryMultEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.recovery_mult_edit,
												  R.string.validation_race_recovery_mult_required);
		strideModEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.stride_mod_edit,
											   R.string.validation_race_stride_mod_required);
		averageHeightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.average_height_edit,
												   R.string.validation_race_avg_height_required);
		averageWeightEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.average_weight_edit,
												   R.string.validation_race_avg_weight_required);
		poundsPerInchEdit = EditTextUtils.initEdit(layout, getActivity(), this, R.id.pounds_per_inch_edit,
												   R.string.validation_race_pounds_per_inch_required);
		initStatMods(layout);
		initRRMods(layout);
		initSizeSpinner(layout);
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

	@Override
	public String getValueForEditText(@IdRes int editTextId) {
		String result = null;

		switch (editTextId) {
			case R.id.name_edit:
				result = currentInstance.getName();
				break;
			case R.id.notes_edit:
				result = currentInstance.getDescription();
				break;
			case R.id.dp_edit:
				result = String.valueOf(currentInstance.getBonusDevelopmentPoints());
				break;
			case R.id.endurance_mod_edit:
				result = String.valueOf(currentInstance.getEnduranceModifier());
				break;
			case R.id.base_hits_edit:
				result = String.valueOf(currentInstance.getBaseHits());
				break;
			case R.id.recovery_mult_edit:
				result = String.valueOf(currentInstance.getRecoveryMultiplier());
				break;
			case R.id.stride_mod_edit:
				result = String.valueOf(currentInstance.getStrideModifier());
				break;
			case R.id.average_height_edit:
				result = String.valueOf(currentInstance.getAverageHeight());
				break;
			case R.id.average_weight_edit:
				result = String.valueOf(currentInstance.getAverageWeight());
				break;
			case R.id.pounds_per_inch_edit:
				result = String.valueOf(currentInstance.getPoundsPerInch());
				break;
		}

		return result;
	}

	@Override
	public void setValueFromEditText(@IdRes int editTextId, String newString) {
		try {
			switch (editTextId) {
				case R.id.name_edit:
					currentInstance.setName(newString);
					break;
				case R.id.notes_edit:
					currentInstance.setDescription(newString);
					break;
				case R.id.dp_edit:
					currentInstance.setBonusDevelopmentPoints(Short.valueOf(newString));
					break;
				case R.id.endurance_mod_edit:
					currentInstance.setEnduranceModifier(Short.valueOf(newString));
					break;
				case R.id.base_hits_edit:
					currentInstance.setBaseHits(Short.valueOf(newString));
					break;
				case R.id.recovery_mult_edit:
					currentInstance.setRecoveryMultiplier(Float.valueOf(newString));
					break;
				case R.id.stride_mod_edit:
					currentInstance.setStrideModifier(Short.valueOf(newString));
					break;
				case R.id.average_height_edit:
					currentInstance.setAverageHeight(Short.valueOf(newString));
					break;
				case R.id.average_weight_edit:
					currentInstance.setAverageWeight(Short.valueOf(newString));
					break;
				case R.id.pounds_per_inch_edit:
					currentInstance.setPoundsPerInch(Short.valueOf(newString));
					break;
			}
		}
		catch (NumberFormatException ignored) {}
	}
	// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;
		String newString;
		short newShort;
		Short oldShort;
		float newFloat;
		Size newSize;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

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

		for(Map.Entry<Statistic, EditText> entry : statEditViews.entrySet()) {
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

		for(Map.Entry<Statistic, EditText> entry : statEditViews.entrySet()) {
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
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
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
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
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
						Log.e(TAG, "Exception caught getting all Size instances", e);
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

	private void initStatMods(View layout) {
		statEditViews = new HashMap<>(10);

		initStatViews(layout, R.id.agility_label, R.id.agility_edit, Statistic.AGILITY);
		initStatViews(layout, R.id.constitution_label, R.id.constitution_edit, Statistic.CONSTITUTION);
		initStatViews(layout, R.id.empathy_label, R.id.empathy_edit, Statistic.EMPATHY);
		initStatViews(layout, R.id.intuition_label, R.id.intuition_edit, Statistic.INTUITION);
		initStatViews(layout, R.id.memory_label, R.id.memory_edit, Statistic.MEMORY);
		initStatViews(layout, R.id.presence_label, R.id.presence_edit, Statistic.PRESENCE);
		initStatViews(layout, R.id.quickness_label, R.id.quickness_edit, Statistic.QUICKNESS);
		initStatViews(layout, R.id.reasoning_label, R.id.reasoning_edit, Statistic.REASONING);
		initStatViews(layout, R.id.self_discipline_label, R.id.self_discipline_edit, Statistic.SELF_DISCIPLINE);
		initStatViews(layout, R.id.strength_label, R.id.strength_edit, Statistic.STRENGTH);
	}

	private void initStatViews(View layout, @IdRes int labelId, @IdRes int editID, final Statistic stat) {
		TextView textView = (TextView)layout.findViewById(labelId);
		textView.setText(stat.getName());

		final EditText editText = (EditText)layout.findViewById(editID);
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
						Log.e(TAG, "Exception caught getting all Realm instances", e);
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
	// </editor-fold>
}

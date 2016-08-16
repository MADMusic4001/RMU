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
package com.madinnovations.rmu.view.activities.combat;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalTypeRxHandler;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.BodyPartSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalResultListAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalTypeSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for critical results.
 */
public class CriticalResultsFragment extends Fragment {
	@Inject
	protected CriticalResultRxHandler    criticalResultRxHandler;
	@Inject
	protected CriticalTypeRxHandler      criticalTypeRxHandler;
	@Inject
	protected BodyPartRxHandler          bodyPartRxHandler;
	@Inject
	protected CriticalResultListAdapter  listAdapter;
	@Inject
	protected CriticalTypeSpinnerAdapter criticalTypeFilterSpinnerAdapter;
	@Inject
	protected CriticalTypeSpinnerAdapter criticalTypeSpinnerAdapter;
	@Inject
	protected BodyPartSpinnerAdapter     bodyPartSpinnerAdapter;
	private Spinner  criticalTypeFilterSpinner;
	private ListView listView;
	private Spinner  criticalTypeSpinner;
	private EditText descriptionEdit;
	private EditText minRollEdit;
	private EditText maxRollEdit;
	private EditText severityCodeEdit;
	private Spinner  bodyPartSpinner;
	private EditText hitsEdit;
	private EditText bleedingEdit;
	private EditText fatigueEdit;
	private EditText breakageEdit;
	private EditText injuryEdit;
	private EditText dazedEdit;
	private EditText stunnedEdit;
	private EditText noParryEdit;
	private CheckBox staggeredCheckBox;
	private EditText knockBackEdit;
	private CheckBox proneCheckBox;
	private EditText grappledEdit;
	private CriticalResult currentInstance = new CriticalResult();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.critical_results_fragment, container, false);

		initCriticalTypeFilterSpinner(layout);
		initCriticalTypeSpinner(layout);
		initDescriptionEdit(layout);
		initMinRollEdit(layout);
		initMaxRollEdit(layout);
		initSeverityCodeEdit(layout);
		initBodyPartSpinner(layout);
		initHitsEdit(layout);
		initBleedingEdit(layout);
		initFatigueEdit(layout);
		initBreakageEdit(layout);
		initInjuryEdit(layout);
		initDazedEdit(layout);
		initStunnedEdit(layout);
		initNoParryEdit(layout);
		initStaggeredCheckBox(layout);
		initKnockBackEdit(layout);
		initProneCheckBox(layout);
		initGrappledEdit(layout);
		initListView(layout);

		copyItemToViews();
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
		inflater.inflate(R.menu.critical_results_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_critical_result) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CriticalResult();
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
		getActivity().getMenuInflater().inflate(R.menu.critical_result_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CriticalResult criticalResult;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_critical_result:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CriticalResult();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_critical_result:
				criticalResult = (CriticalResult)listView.getItemAtPosition(info.position);
				if(criticalResult != null) {
					deleteItem(criticalResult);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		int position;
		CriticalType newCriticalType = null;
		String newString;
		char newChar;
		short newShort;
		Short newBreakage = null;
		BodyPart newBodyPart = null;

		position = criticalTypeSpinner.getSelectedItemPosition();
		if(position >= 0) {
			newCriticalType = criticalTypeSpinnerAdapter.getItem(position);
		}
		if((newCriticalType == null && currentInstance.getCriticalType() != null) ||
				(newCriticalType != null && !newCriticalType.equals(currentInstance.getCriticalType()))) {
			currentInstance.setCriticalType(newCriticalType);
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

		if(severityCodeEdit.length() > 0) {
			newChar = severityCodeEdit.getText().charAt(0);
			if(newChar !=  currentInstance.getSeverityCode()) {
				currentInstance.setSeverityCode(newChar);
				changed = true;
			}
		}

		if(minRollEdit.length() > 0) {
			newShort = Short.valueOf(minRollEdit.getText().toString());
			if(newShort !=  currentInstance.getMinRoll()) {
				currentInstance.setMinRoll(newShort);
				changed = true;
			}
		}

		if(maxRollEdit.length() > 0) {
			newShort = Short.valueOf(maxRollEdit.getText().toString());
			if(newShort !=  currentInstance.getMaxRoll()) {
				currentInstance.setMaxRoll(newShort);
				changed = true;
			}
		}

		if(bodyPartSpinner.getSelectedItemPosition() >= 0) {
			newBodyPart = bodyPartSpinnerAdapter.getItem(bodyPartSpinner.getSelectedItemPosition());
		}
		if((newBodyPart == null && currentInstance.getBodyPart() != null) ||
				(newBodyPart!= null && !newBodyPart.equals(currentInstance.getBodyPart()))) {
			currentInstance.setBodyPart(newBodyPart);
			changed = true;
		}

		if(hitsEdit.length() > 0) {
			newShort = Short.valueOf(hitsEdit.getText().toString());
			if(newShort !=  currentInstance.getHits()) {
				currentInstance.setHits(newShort);
				changed = true;
			}
		}

		if(bleedingEdit.length() > 0) {
			newShort = Short.valueOf(bleedingEdit.getText().toString());
			if(newShort !=  currentInstance.getBleeding()) {
				currentInstance.setBleeding(newShort);
				changed = true;
			}
		}

		if(fatigueEdit.length() > 0) {
			newShort = Short.valueOf(fatigueEdit.getText().toString());
			if(newShort !=  currentInstance.getFatigue()) {
				currentInstance.setFatigue(newShort);
				changed = true;
			}
		}

		if(breakageEdit.length() > 0) {
			newBreakage = Short.valueOf(breakageEdit.getText().toString());
		}
		if(newBreakage ==  null && currentInstance.getBreakage() != null ||
				newBreakage != null && !newBreakage.equals(currentInstance.getBreakage())) {
			currentInstance.setBreakage(newBreakage);
			changed = true;
		}

		if(injuryEdit.length() > 0) {
			newShort = Short.valueOf(injuryEdit.getText().toString());
			if(newShort !=  currentInstance.getInjury()) {
				currentInstance.setInjury(newShort);
				changed = true;
			}
		}

		if(dazedEdit.length() > 0) {
			newShort = Short.valueOf(dazedEdit.getText().toString());
			if(newShort !=  currentInstance.getDazed()) {
				currentInstance.setDazed(newShort);
				changed = true;
			}
		}

		if(stunnedEdit.length() > 0) {
			newShort = Short.valueOf(stunnedEdit.getText().toString());
			if(newShort !=  currentInstance.getStunned()) {
				currentInstance.setStunned(newShort);
				changed = true;
			}
		}

		if(noParryEdit.length() > 0) {
			newShort = Short.valueOf(noParryEdit.getText().toString());
			if(newShort !=  currentInstance.getNoParry()) {
				currentInstance.setNoParry(newShort);
				changed = true;
			}
		}

		if(staggeredCheckBox.isChecked() != currentInstance.isStaggered()) {
			currentInstance.setStaggered(staggeredCheckBox.isChecked());
			changed = true;
		}

		if(knockBackEdit.length() > 0) {
			newShort = Short.valueOf(knockBackEdit.getText().toString());
			if(newShort !=  currentInstance.getKnockBack()) {
				currentInstance.setKnockBack(newShort);
				changed = true;
			}
		}

		if(proneCheckBox.isChecked() != currentInstance.isProne()) {
			currentInstance.setProne(proneCheckBox.isChecked());
			changed = true;
		}

		if(grappledEdit.length() > 0) {
			newShort = Short.valueOf(grappledEdit.getText().toString());
			if(newShort !=  currentInstance.getGrappled()) {
				currentInstance.setGrappled(newShort);
				changed = true;
			}
		}

		return changed;
	}

	private void copyItemToViews() {
		criticalTypeSpinner.setSelection(criticalTypeSpinnerAdapter.getPosition(currentInstance.getCriticalType()));
		descriptionEdit.setText(currentInstance.getDescription());
		minRollEdit.setText(String.valueOf(currentInstance.getMinRoll()));
		maxRollEdit.setText(String.valueOf(currentInstance.getMaxRoll()));
		severityCodeEdit.setText(String.valueOf(currentInstance.getSeverityCode()));
		bodyPartSpinner.setSelection(bodyPartSpinnerAdapter.getPosition(currentInstance.getBodyPart()));
		hitsEdit.setText(String.valueOf(currentInstance.getHits()));
		bleedingEdit.setText(String.valueOf(currentInstance.getBleeding()));
		fatigueEdit.setText(String.valueOf(currentInstance.getFatigue()));
		if(currentInstance.getBreakage() != null) {
			breakageEdit.setText(String.valueOf(currentInstance.getBreakage()));
		}
		else {
			breakageEdit.setText("");
		}
		injuryEdit.setText(String.valueOf(currentInstance.getInjury()));
		dazedEdit.setText(String.valueOf(currentInstance.getDazed()));
		stunnedEdit.setText(String.valueOf(currentInstance.getStunned()));
		noParryEdit.setText(String.valueOf(currentInstance.getNoParry()));
		staggeredCheckBox.setChecked(currentInstance.isStaggered());
		knockBackEdit.setText(String.valueOf(currentInstance.getKnockBack()));
		proneCheckBox.setChecked(currentInstance.isProne());
		grappledEdit.setText(String.valueOf(currentInstance.getGrappled()));
		if(currentInstance.getDescription() != null) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			criticalResultRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<CriticalResult>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultsFragment", "Exception saving new CriticalResult", e);
					}
					@Override
					public void onNext(CriticalResult savedCriticalResult) {
						if (wasNew) {
							listAdapter.add(savedCriticalResult);
							if(savedCriticalResult == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedCriticalResult));
								listView.setItemChecked(listAdapter.getPosition(savedCriticalResult), true);
							}
						}
						if(getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_critical_result_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(savedCriticalResult);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.range_view);
								textView.setText(String.format(getString(R.string.min_max_roll_value),
										savedCriticalResult.getMinRoll(), savedCriticalResult.getMaxRoll()));
								textView = (TextView) v.findViewById(R.id.severity_code_view);
								textView.setText(String.valueOf(currentInstance.getSeverityCode()));
								textView = (TextView) v.findViewById(R.id.description_view);
								textView.setText(savedCriticalResult.getDescription());
							}
						}
					}
				});
		}
	}

	private void deleteItem(@NonNull final CriticalResult item) {
		criticalResultRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_critical_result_delete_failed);
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
								currentInstance = new CriticalResult();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_critical_result_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initCriticalTypeFilterSpinner(View layout) {
		criticalTypeFilterSpinner = (Spinner)layout.findViewById(R.id.critical_type_filter_spinner);
		criticalTypeFilterSpinner.setAdapter(criticalTypeFilterSpinnerAdapter);

		final CriticalType allSkills = new CriticalType();
		allSkills.setName(getString(R.string.label_all_critical_types));
		criticalTypeFilterSpinnerAdapter.add(allSkills);
		criticalTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<CriticalType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SpecializationsFragment", "Exception caught getting all specialization Skill instances", e);
					}
					@Override
					public void onNext(Collection<CriticalType> criticalTypes) {
						criticalTypeFilterSpinnerAdapter.addAll(criticalTypes);
						criticalTypeFilterSpinnerAdapter.notifyDataSetChanged();
						criticalTypeFilterSpinner.setSelection(criticalTypeFilterSpinnerAdapter.getPosition(allSkills));
					}
				});

		criticalTypeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				loadFilteredCriticalResults(criticalTypeFilterSpinnerAdapter.getItem(position));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				loadFilteredCriticalResults(null);
			}
		});
	}

	private void initCriticalTypeSpinner(View layout) {
		criticalTypeSpinner = (Spinner) layout.findViewById(R.id.critical_type_spinner);

		criticalTypeSpinner.setAdapter(criticalTypeSpinnerAdapter);

		criticalTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<CriticalType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultsFragment", "Exception caught getting all CriticalType instances in initCriticalTypeSpinner", e);
					}
					@Override
					public void onNext(Collection<CriticalType> criticalTypessResults) {
						criticalTypeSpinnerAdapter.clear();
						criticalTypeSpinnerAdapter.addAll(criticalTypessResults);
						criticalTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		criticalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCriticalType() == null ||
						criticalTypeSpinnerAdapter.getPosition(currentInstance.getCriticalType()) != position) {
					currentInstance.setCriticalType(criticalTypeSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getCriticalType() != null) {
					currentInstance.setCriticalType(null);
					saveItem();
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
					descriptionEdit.setError(getString(R.string.validation_critical_result_description_required));
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

	private void initMinRollEdit(View layout) {
		minRollEdit = (EditText)layout.findViewById(R.id.min_roll_edit);
		minRollEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && minRollEdit != null) {
					minRollEdit.setError(getString(R.string.validation_min_roll_required));
				}
				else {
					int newValue = Integer.valueOf(editable.toString());
					if(newValue > currentInstance.getMaxRoll()) {
						minRollEdit.setError(getString(R.string.validation_min_roll_gt_max_roll));
					}
					else {
						minRollEdit.setError(null);
						maxRollEdit.setError(null);
					}
				}
			}
		});
		minRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(minRollEdit.getText().length() > 0) {
						short newMinRoll = Short.valueOf(minRollEdit.getText().toString());
						if (newMinRoll != currentInstance.getMinRoll()) {
							currentInstance.setMinRoll(newMinRoll);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initMaxRollEdit(View layout) {
		maxRollEdit = (EditText)layout.findViewById(R.id.max_roll_edit);
		maxRollEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && maxRollEdit != null) {
					maxRollEdit.setError(getString(R.string.validation_max_roll_required));
				}
				else {
					short newValue = Short.valueOf(editable.toString());
					if(newValue <= currentInstance.getMinRoll()) {
						maxRollEdit.setError(getString(R.string.validation_max_roll_lt_min_roll));
					}
					else {
						new GridView.LayoutParams(85, 85);
						minRollEdit.setError(null);
						maxRollEdit.setError(null);
					}
				}
			}
		});
		maxRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(maxRollEdit.getText().length() > 0) {
						short newMaxRoll = Short.valueOf(maxRollEdit.getText().toString());
						if (newMaxRoll != currentInstance.getMaxRoll()) {
							currentInstance.setMaxRoll(newMaxRoll);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initSeverityCodeEdit(View layout) {
		severityCodeEdit = (EditText)layout.findViewById(R.id.severity_code_edit);
		severityCodeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					severityCodeEdit.setError(getString(R.string.validation_severity_code_required));
				}
			}
		});
		severityCodeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(severityCodeEdit.getText().length() > 0) {
						final char newValue = severityCodeEdit.getText().charAt(0);
						if (newValue != currentInstance.getSeverityCode()) {
							currentInstance.setSeverityCode(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBodyPartSpinner(View layout) {
		bodyPartSpinner = (Spinner) layout.findViewById(R.id.body_part_spinner);

		bodyPartSpinner.setAdapter(bodyPartSpinnerAdapter);

		bodyPartRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<BodyPart>>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalResultsFragment", "Exception caught getting all BodyPart instances in initBodyPartSpinner", e);
				}
				@Override
				public void onNext(Collection<BodyPart> bodyPartsResults) {
					bodyPartSpinnerAdapter.clear();
					bodyPartSpinnerAdapter.addAll(bodyPartsResults);
					bodyPartSpinnerAdapter.notifyDataSetChanged();
				}
			});

		bodyPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getBodyPart() == null || bodyPartSpinnerAdapter.getPosition(currentInstance.getBodyPart()) != position) {
					currentInstance.setBodyPart(bodyPartSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getBodyPart() != null) {
					currentInstance.setBodyPart(null);
					saveItem();
				}
			}
		});
	}

	private void initHitsEdit(View layout) {
		hitsEdit = (EditText)layout.findViewById(R.id.hits_edit);
		hitsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && hitsEdit != null) {
					hitsEdit.setError(getString(R.string.validation_hits_required));
				}
			}
		});
		hitsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(hitsEdit.getText().length() > 0) {
						short newHits = Short.valueOf(hitsEdit.getText().toString());
						if (newHits != currentInstance.getHits()) {
							currentInstance.setHits(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBleedingEdit(View layout) {
		bleedingEdit = (EditText)layout.findViewById(R.id.bleeding_edit);
		bleedingEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && bleedingEdit != null) {
					bleedingEdit.setError(getString(R.string.validation_bleeding_required));
				}
			}
		});
		bleedingEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(bleedingEdit.getText().length() > 0) {
						short newBleeding = Short.valueOf(bleedingEdit.getText().toString());
						if (newBleeding != currentInstance.getBleeding()) {
							currentInstance.setBleeding(newBleeding);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initFatigueEdit(View layout) {
		fatigueEdit = (EditText)layout.findViewById(R.id.fatigue_edit);
		fatigueEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && fatigueEdit != null) {
					fatigueEdit.setError(getString(R.string.validation_fatigue_required));
				}
			}
		});
		fatigueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(fatigueEdit.getText().length() > 0) {
						short newValue = Short.valueOf(fatigueEdit.getText().toString());
						if (newValue != currentInstance.getFatigue()) {
							currentInstance.setFatigue(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBreakageEdit(View layout) {
		breakageEdit = (EditText)layout.findViewById(R.id.breakage_edit);
		breakageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					Short newValue = null;
					if(breakageEdit.getText().length() > 0) {
						newValue = Short.valueOf(breakageEdit.getText().toString());
					}
					if (newValue == null && currentInstance.getBreakage() != null ||
							newValue != null && !newValue.equals(currentInstance.getBreakage())) {
						currentInstance.setBreakage(newValue);
						saveItem();
					}
				}
			}
		});
	}

	private void initInjuryEdit(View layout) {
		injuryEdit = (EditText)layout.findViewById(R.id.injury_edit);
		injuryEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && injuryEdit != null) {
					injuryEdit.setError(getString(R.string.validation_injury_required));
				}
			}
		});
		injuryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(injuryEdit.getText().length() > 0) {
						short newValue = Short.valueOf(injuryEdit.getText().toString());
						if (newValue != currentInstance.getInjury()) {
							currentInstance.setInjury(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initDazedEdit(View layout) {
		dazedEdit = (EditText)layout.findViewById(R.id.dazed_edit);
		dazedEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && dazedEdit != null) {
					dazedEdit.setError(getString(R.string.validation_dazed_required));
				}
			}
		});
		dazedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(dazedEdit.getText().length() > 0) {
						short newValue = Short.valueOf(dazedEdit.getText().toString());
						if (newValue != currentInstance.getDazed()) {
							currentInstance.setDazed(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initStunnedEdit(View layout) {
		stunnedEdit = (EditText)layout.findViewById(R.id.stunned_edit);
		stunnedEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && stunnedEdit != null) {
					stunnedEdit.setError(getString(R.string.validation_stunned_required));
				}
			}
		});
		stunnedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(stunnedEdit.getText().length() > 0) {
						short newValue = Short.valueOf(stunnedEdit.getText().toString());
						if (newValue != currentInstance.getStunned()) {
							currentInstance.setStunned(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initNoParryEdit(View layout) {
		noParryEdit = (EditText)layout.findViewById(R.id.stunned_no_parry_edit);
		noParryEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && noParryEdit != null) {
					noParryEdit.setError(getString(R.string.validation_no_parry_required));
				}
			}
		});
		noParryEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(noParryEdit.getText().length() > 0) {
						short newValue = Short.valueOf(noParryEdit.getText().toString());
						if (newValue != currentInstance.getNoParry()) {
							currentInstance.setNoParry(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initStaggeredCheckBox(View layout) {
		staggeredCheckBox = (CheckBox) layout.findViewById(R.id.staggered_checkbox);
		staggeredCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setStaggered(staggeredCheckBox.isChecked());
				saveItem();
			}
		});
	}

	private void initKnockBackEdit(View layout) {
		knockBackEdit = (EditText)layout.findViewById(R.id.knock_back_edit);
		knockBackEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && knockBackEdit != null) {
					knockBackEdit.setError(getString(R.string.validation_knock_back_required));
				}
			}
		});
		knockBackEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(knockBackEdit.getText().length() > 0) {
						short newValue = Short.valueOf(knockBackEdit.getText().toString());
						if (newValue != currentInstance.getKnockBack()) {
							currentInstance.setKnockBack(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initProneCheckBox(View layout) {
		proneCheckBox = (CheckBox) layout.findViewById(R.id.prone_checkbox);
		proneCheckBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setProne(proneCheckBox.isChecked());
				saveItem();
			}
		});
	}

	private void initGrappledEdit(View layout) {
		grappledEdit = (EditText)layout.findViewById(R.id.grappled_edit);
		grappledEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && grappledEdit != null) {
					grappledEdit.setError(getString(R.string.validation_grappled_required));
				}
			}
		});
		grappledEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(grappledEdit.getText().length() > 0) {
						short newValue = Short.valueOf(grappledEdit.getText().toString());
						if (newValue != currentInstance.getGrappled()) {
							currentInstance.setGrappled(newValue);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);
		loadFilteredCriticalResults(null);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CriticalResult) listView.getItemAtPosition(position);
				if(currentInstance == null) {
					currentInstance = new CriticalResult();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	private void loadFilteredCriticalResults(final CriticalType filter) {
		Observable<Collection<CriticalResult>> observable;

		if(filter == null || filter.getId() == -1) {
			observable = criticalResultRxHandler.getAll();
		}
		else {
			observable = criticalResultRxHandler.getCriticalResultsForCriticalType(filter);
		}
		observable.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CriticalResult>>() {
					@Override
					public void onCompleted() {
						int position = listAdapter.getPosition(currentInstance);
						if (position == -1 && listAdapter.getCount() > 0) {
							currentInstance = listAdapter.getItem(0);
							isNew = false;
							position = 0;
						}
						if (position >= 0) {
							listView.setSelection(position);
							listView.setItemChecked(position, true);
							listAdapter.notifyDataSetChanged();
						}
						copyItemToViews();
					}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultsFragment", "Exception caught getting all CriticalResult instances", e);
						Toast.makeText(CriticalResultsFragment.this.getActivity(),
								getString(R.string.toast_critical_results_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CriticalResult> specializations) {
						listAdapter.clear();
						listAdapter.addAll(specializations);
						listAdapter.notifyDataSetChanged();
						if(filter == null) {
							String toastString;
							toastString = String.format(getString(R.string.toast_critical_results_loaded), specializations.size());
							Toast.makeText(CriticalResultsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}

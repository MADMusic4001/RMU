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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.data.entities.combat.BodyPart;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.BodyPartSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalResultListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for critical results.
 */
public class CriticalResultsFragment extends Fragment {
	@Inject
	protected CriticalResultRxHandler criticalResultRxHandler;
	@Inject
	protected BodyPartRxHandler bodyPartRxHandler;
	@Inject
	protected CriticalResultListAdapter listAdapter;
	@Inject
	protected BodyPartSpinnerAdapter spinnerAdapter;
	private ListView listView;
	private EditText descriptionEdit;
	private EditText minRollEdit;
	private EditText maxRollEdit;
	private EditText severityCodeEdit;
	private Spinner bodyPartSpinner;
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

		copyItemToControls();
		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		super.onPause();
		saveItem();
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
			currentInstance = new CriticalResult();
			isNew = true;
			copyItemToControls();
			listView.clearChoices();
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
				currentInstance = new CriticalResult();
				isNew = true;
				copyItemToControls();
				listView.clearChoices();
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

	private void copyItemToControls() {
		descriptionEdit.setText(currentInstance.getDescription());
		minRollEdit.setText(String.valueOf(currentInstance.getMinRoll()));
		maxRollEdit.setText(String.valueOf(currentInstance.getMaxRoll()));
		severityCodeEdit.setText(String.valueOf(currentInstance.getSeverityCode()));
		bodyPartSpinner.setSelection(spinnerAdapter.getPosition(currentInstance.getBodyPart()));
		hitsEdit.setText(String.valueOf(currentInstance.getHits()));
		bleedingEdit.setText(String.valueOf(currentInstance.getBleeding()));
		fatigueEdit.setText(String.valueOf(currentInstance.getFatigue()));
		breakageEdit.setText(String.valueOf(currentInstance.getBreakage()));
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
			criticalResultRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<CriticalResult>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultsFragment", "Exception saving new CriticalResult", e);
					}

					@Override
					public void onNext(CriticalResult savedCriticalResult) {
						if (isNew) {
							listAdapter.add(savedCriticalResult);
							listView.setSelection(listAdapter.getPosition(savedCriticalResult));
							listView.setItemChecked(listAdapter.getPosition(savedCriticalResult), true);
							isNew = false;
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
							}
							copyItemToControls();
							Toast.makeText(getActivity(), getString(R.string.toast_critical_result_deleted), Toast.LENGTH_SHORT).show();
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
				}
			}
		});
		minRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(minRollEdit.getText().length() > 0) {
						int newMinRoll = Integer.valueOf(minRollEdit.getText().toString());
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
					int newValue = Integer.valueOf(editable.toString());
					if(newValue <= currentInstance.getMinRoll()) {
						maxRollEdit.setError(getString(R.string.validation_max_roll_lt_min_roll));
					}
				}
			}
		});
		maxRollEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(maxRollEdit.getText().length() > 0) {
						int newMaxRoll = Integer.valueOf(maxRollEdit.getText().toString());
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

		bodyPartSpinner.setAdapter(spinnerAdapter);

		bodyPartRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<BodyPart>>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalResultsFragment", "Exception caught getting all BodyPart instances in initBodyPartSpinner", e);
					Toast.makeText(CriticalResultsFragment.this.getActivity(),
							getString(R.string.toast_body_parts_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<BodyPart> bodyPartsResults) {
					spinnerAdapter.clear();
					spinnerAdapter.addAll(bodyPartsResults);
					spinnerAdapter.notifyDataSetChanged();
				}
			});

		bodyPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getBodyPart() == null || spinnerAdapter.getPosition(currentInstance.getBodyPart()) != position) {
					currentInstance.setBodyPart(spinnerAdapter.getItem(position));
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
		breakageEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && breakageEdit != null) {
					breakageEdit.setError(getString(R.string.validation_breakage_required));
				}
			}
		});
		breakageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(breakageEdit.getText().length() > 0) {
						short newValue = Short.valueOf(breakageEdit.getText().toString());
						if (newValue != currentInstance.getBreakage()) {
							currentInstance.setBreakage(newValue);
							saveItem();
						}
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

		criticalResultRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<CriticalResult>>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalResultsFragment", "Exception caught getting all CriticalCode instances in onCreateView", e);
					Toast.makeText(CriticalResultsFragment.this.getActivity(),
							getString(R.string.toast_critical_results_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<CriticalResult> criticalResults) {
					listAdapter.clear();
					listAdapter.addAll(criticalResults);
					listAdapter.notifyDataSetChanged();
					if(criticalResults.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToControls();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_critical_results_loaded), criticalResults.size());
					Toast.makeText(CriticalResultsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentInstance = (CriticalResult) listView.getItemAtPosition(position);
				if(currentInstance == null) {
					currentInstance = new CriticalResult();
					isNew = true;
				}
				copyItemToControls();
			}
		});
		registerForContextMenu(listView);
	}
}

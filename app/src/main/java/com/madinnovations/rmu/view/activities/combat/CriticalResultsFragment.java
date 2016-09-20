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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	protected BodyPartRxHandler          bodyPartRxHandler;
	@Inject
	protected CriticalResultRxHandler    criticalResultRxHandler;
	@Inject
	protected CriticalTypeRxHandler      criticalTypeRxHandler;
	@Inject
	protected CriticalResultListAdapter  listAdapter;
	private   ArrayAdapter<CriticalType> criticalTypeFilterSpinnerAdapter;
	private   ArrayAdapter<Character>    criticalSeverityFilterSpinnerAdapter;
	private Spinner  criticalTypeFilterSpinner;
	private Spinner  criticalSeverityFilterSpinner;
	private ListView listView;
	private CriticalType criticalTypeFilter = null;
	private char criticalSeverityFilter = 'A';
	private Collection<BodyPart> bodyParts = null;
	private Collection<CriticalResult> tempResults = null;
	private CriticalResult currentInstance = new CriticalResult();
	private boolean isNew = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.critical_results_fragment, container, false);

		initCriticalTypeFilterSpinner(layout);
		initCriticalSeverityFilterSpinner(layout);
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		int position;
		CriticalType newCriticalType = null;
		String newString;
		char newChar;
		short newShort;

//		position = criticalTypeFilterSpinner.getSelectedItemPosition();
//		if(position >= 0) {
//			newCriticalType = criticalTypeFilterSpinnerAdapter.getItem(position);
//		}
//		if((newCriticalType == null && currentInstance.getCriticalType() != null) ||
//				(newCriticalType != null && !newCriticalType.equals(currentInstance.getCriticalType()))) {
//			currentInstance.setCriticalType(newCriticalType);
//			changed = true;
//		}

//		newString = descriptionEdit.getText().toString();
//		if(newString.isEmpty()) {
//			newString = null;
//		}
//		if((newString == null && currentInstance.getResultText() != null) ||
//				(newString != null && !newString.equals(currentInstance.getResultText()))) {
//			currentInstance.setResultText(newString);
//			changed = true;
//		}
//
//		if(severityCodeEdit.length() > 0) {
//			newChar = severityCodeEdit.getText().charAt(0);
//			if(newChar !=  currentInstance.getSeverityCode()) {
//				currentInstance.setSeverityCode(newChar);
//				changed = true;
//			}
//		}
//
//		if(minRollEdit.length() > 0) {
//			newShort = Short.valueOf(minRollEdit.getText().toString());
//			if(newShort !=  currentInstance.getMinRoll()) {
//				currentInstance.setMinRoll(newShort);
//				changed = true;
//			}
//		}
//
//		if(maxRollEdit.length() > 0) {
//			newShort = Short.valueOf(maxRollEdit.getText().toString());
//			if(newShort !=  currentInstance.getMaxRoll()) {
//				currentInstance.setMaxRoll(newShort);
//				changed = true;
//			}
//		}
//
//		if(bodyPartSpinner.getSelectedItemPosition() >= 0) {
//			newBodyPart = bodyPartSpinnerAdapter.getItem(bodyPartSpinner.getSelectedItemPosition());
//		}
//		if((newBodyPart == null && currentInstance.getBodyPart() != null) ||
//				(newBodyPart!= null && !newBodyPart.equals(currentInstance.getBodyPart()))) {
//			currentInstance.setBodyPart(newBodyPart);
//			changed = true;
//		}
//
//		if(hitsEdit.length() > 0) {
//			newShort = Short.valueOf(hitsEdit.getText().toString());
//			if(newShort !=  currentInstance.getHits()) {
//				currentInstance.setHits(newShort);
//				changed = true;
//			}
//		}
//
//		if(bleedingEdit.length() > 0) {
//			newShort = Short.valueOf(bleedingEdit.getText().toString());
//			if(newShort !=  currentInstance.getBleeding()) {
//				currentInstance.setBleeding(newShort);
//				changed = true;
//			}
//		}
//
//		if(fatigueEdit.length() > 0) {
//			newShort = Short.valueOf(fatigueEdit.getText().toString());
//			if(newShort !=  currentInstance.getFatigue()) {
//				currentInstance.setFatigue(newShort);
//				changed = true;
//			}
//		}
//
//		if(breakageEdit.length() > 0) {
//			newBreakage = Short.valueOf(breakageEdit.getText().toString());
//		}
//		if(newBreakage ==  null && currentInstance.getBreakage() != null ||
//				newBreakage != null && !newBreakage.equals(currentInstance.getBreakage())) {
//			currentInstance.setBreakage(newBreakage);
//			changed = true;
//		}
//
//		if(injuryEdit.length() > 0) {
//			newShort = Short.valueOf(injuryEdit.getText().toString());
//			if(newShort !=  currentInstance.getInjury()) {
//				currentInstance.setInjury(newShort);
//				changed = true;
//			}
//		}
//
//		if(dazedEdit.length() > 0) {
//			newShort = Short.valueOf(dazedEdit.getText().toString());
//			if(newShort !=  currentInstance.getDazed()) {
//				currentInstance.setDazed(newShort);
//				changed = true;
//			}
//		}
//
//		if(stunnedEdit.length() > 0) {
//			newShort = Short.valueOf(stunnedEdit.getText().toString());
//			if(newShort !=  currentInstance.getStunned()) {
//				currentInstance.setStunned(newShort);
//				changed = true;
//			}
//		}
//
//		if(noParryEdit.length() > 0) {
//			newShort = Short.valueOf(noParryEdit.getText().toString());
//			if(newShort !=  currentInstance.getNoParry()) {
//				currentInstance.setNoParry(newShort);
//				changed = true;
//			}
//		}
//
//		if(staggeredCheckBox.isChecked() != currentInstance.isStaggered()) {
//			currentInstance.setStaggered(staggeredCheckBox.isChecked());
//			changed = true;
//		}
//
//		if(knockBackEdit.length() > 0) {
//			newShort = Short.valueOf(knockBackEdit.getText().toString());
//			if(newShort !=  currentInstance.getKnockBack()) {
//				currentInstance.setKnockBack(newShort);
//				changed = true;
//			}
//		}
//
//		if(proneCheckBox.isChecked() != currentInstance.isProne()) {
//			currentInstance.setProne(proneCheckBox.isChecked());
//			changed = true;
//		}
//
//		if(grappledEdit.length() > 0) {
//			newShort = Short.valueOf(grappledEdit.getText().toString());
//			if(newShort !=  currentInstance.getGrappled()) {
//				currentInstance.setGrappled(newShort);
//				changed = true;
//			}
//		}
//
		return changed;
	}

	private void copyItemToViews() {
//		descriptionEdit.setText(currentInstance.getResultText());
//		minRollEdit.setText(String.valueOf(currentInstance.getMinRoll()));
//		maxRollEdit.setText(String.valueOf(currentInstance.getMaxRoll()));
//		severityCodeEdit.setText(String.valueOf(currentInstance.getSeverityCode()));
//		bodyPartSpinner.setSelection(bodyPartSpinnerAdapter.getPosition(currentInstance.getBodyPart()));
//		hitsEdit.setText(String.valueOf(currentInstance.getHits()));
//		bleedingEdit.setText(String.valueOf(currentInstance.getBleeding()));
//		fatigueEdit.setText(String.valueOf(currentInstance.getFatigue()));
//		if(currentInstance.getBreakage() != null) {
//			breakageEdit.setText(String.valueOf(currentInstance.getBreakage()));
//		}
//		else {
//			breakageEdit.setText("");
//		}
//		injuryEdit.setText(String.valueOf(currentInstance.getInjury()));
//		dazedEdit.setText(String.valueOf(currentInstance.getDazed()));
//		stunnedEdit.setText(String.valueOf(currentInstance.getStunned()));
//		noParryEdit.setText(String.valueOf(currentInstance.getNoParry()));
//		staggeredCheckBox.setChecked(currentInstance.isStaggered());
//		knockBackEdit.setText(String.valueOf(currentInstance.getKnockBack()));
//		proneCheckBox.setChecked(currentInstance.isProne());
//		grappledEdit.setText(String.valueOf(currentInstance.getGrappled()));
//		if(currentInstance.getResultText() != null) {
//			descriptionEdit.setError(null);
//		}
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
								TextView textView = (TextView) v.findViewById(R.id.header_field1);
								textView.setText(String.format(getString(R.string.min_max_roll_value),
										savedCriticalResult.getMinRoll(), savedCriticalResult.getMaxRoll()));
								textView = (TextView) v.findViewById(R.id.header_field2);
								textView.setText(String.valueOf(currentInstance.getSeverityCode()));
								textView = (TextView) v.findViewById(R.id.header_field3);
								textView.setText(savedCriticalResult.getResultText());
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
		criticalTypeFilterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		criticalTypeFilterSpinner.setAdapter(criticalTypeFilterSpinnerAdapter);

		criticalTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<CriticalType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CriticalResultsFragment", "Exception caught getting all CriticalType instances", e);
					}
					@Override
					public void onNext(Collection<CriticalType> criticalTypes) {
						criticalTypeFilterSpinnerAdapter.addAll(criticalTypes);
						criticalTypeFilterSpinnerAdapter.notifyDataSetChanged();
						criticalTypeFilter = currentInstance.getCriticalType();
						criticalTypeFilterSpinner.setSelection(criticalTypeFilterSpinnerAdapter.getPosition(criticalTypeFilter));
					}
				});

		criticalTypeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				criticalTypeFilter = criticalTypeFilterSpinnerAdapter.getItem(position);
				loadCriticalResults();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initCriticalSeverityFilterSpinner(View layout) {
		criticalSeverityFilterSpinner = (Spinner)layout.findViewById(R.id.critical_severity_filter_spinner);
		criticalSeverityFilterSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_row);
		criticalSeverityFilterSpinner.setAdapter(criticalSeverityFilterSpinnerAdapter);

		criticalSeverityFilterSpinnerAdapter.add('A');
		criticalSeverityFilterSpinnerAdapter.add('B');
		criticalSeverityFilterSpinnerAdapter.add('C');
		criticalSeverityFilterSpinnerAdapter.add('D');
		criticalSeverityFilterSpinnerAdapter.add('E');
		criticalSeverityFilterSpinnerAdapter.notifyDataSetChanged();
		criticalSeverityFilter = currentInstance.getSeverityCode();
		criticalSeverityFilterSpinner.setSelection(criticalSeverityFilterSpinnerAdapter.getPosition(criticalSeverityFilter));

		criticalSeverityFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("ConstantConditions")
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				criticalSeverityFilter = criticalSeverityFilterSpinnerAdapter.getItem(position);
				loadCriticalResults();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);
		loadCriticalResults();

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

	private void loadCriticalResults() {
		if(criticalTypeFilter == null || criticalTypeFilter.getId() == -1) {
			listAdapter.clear();
			listAdapter.notifyDataSetChanged();
		}
		else {
			criticalResultRxHandler.getCriticalResultTableRows(criticalTypeFilter, criticalSeverityFilter)
					.observeOn(AndroidSchedulers.mainThread())
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
						public void onNext(final Collection<CriticalResult> criticalResultRows) {
							if (bodyParts == null) {
								bodyPartRxHandler.getAll()
										.observeOn(AndroidSchedulers.mainThread())
										.subscribeOn(Schedulers.io())
										.subscribe(new Subscriber<Collection<BodyPart>>() {
											@Override
											public void onCompleted() {}
											@Override
											public void onError(Throwable e) {
												Log.e("RMU", "Exception caught getting all body parts.", e);
											}
											@Override
											public void onNext(Collection<BodyPart> bodyParts) {
												CriticalResultsFragment.this.bodyParts = bodyParts;
												listAdapter.clear();
												listAdapter.addAll(CriticalResult.generateMissingCriticalResultRows(criticalResultRows, criticalTypeFilter,
														criticalSeverityFilter, bodyParts));
												listAdapter.notifyDataSetChanged();
											}
										});
							}
							else {
								listAdapter.clear();
								listAdapter.addAll(CriticalResult.generateMissingCriticalResultRows(criticalResultRows, criticalTypeFilter,
										criticalSeverityFilter, bodyParts));
								listAdapter.notifyDataSetChanged();
							}
						}
					});
		}
	}
}

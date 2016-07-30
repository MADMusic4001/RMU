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
import android.content.Context;
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
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalCodeRxHandler;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.combat.CriticalCodeListAdapter;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for critical codes.
 */
public class CriticalCodesFragment extends Fragment {
	@Inject
	protected CriticalCodeRxHandler   criticalCodeRxHandler;
	@Inject
	protected CriticalCodeListAdapter listAdapter;
	private   ListView                listView;
	private   EditText                codeEdit;
	private   EditText                descriptionEdit;
	private CriticalCode selectedInstance = null;
	private boolean  dirty            = false;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCombatFragmentComponent(new CombatFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.critical_codes_fragment, container, false);

		initCodeEdit(layout);
		initDescriptionEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		criticalCodeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CriticalCode>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("CriticalCodesFragment", "Exception caught getting all CriticalCode instances in onCreateView", e);
						Toast.makeText(CriticalCodesFragment.this.getActivity(),
									   getString(R.string.toast_critical_codes_load_failed),
									   Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<CriticalCode> criticalCodes) {
						listAdapter.clear();
						listAdapter.addAll(criticalCodes);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_critical_codes_loaded), criticalCodes.size());
						Toast.makeText(CriticalCodesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.critical_codes_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_critical_code) {
			CriticalCode criticalCode = new CriticalCode();
			criticalCode.setCode(getString(R.string.default_critical_code_code));
			criticalCode.setDescription(getString(R.string.default_critical_code_description));
			criticalCodeRxHandler.save(criticalCode)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CriticalCode>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("CriticalCodesFragment", "Exception saving new CriticalCode in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(CriticalCode savedCriticalCode) {
							listAdapter.add(savedCriticalCode);
							codeEdit.setText(savedCriticalCode.getCode());
							descriptionEdit.setText(savedCriticalCode.getDescription());
							selectedInstance = savedCriticalCode;
						}
					});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.critical_code_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CriticalCode criticalCode;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_delete_critical_code:
				criticalCode = (CriticalCode)listView.getItemAtPosition(info.position);
				if(criticalCode != null) {
					criticalCodeRxHandler.deleteById(criticalCode.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("CriticalCodeFragment", "Exception thrown when deleting: " + criticalCode, e);
									String toastString = getString(R.string.toast_critical_code_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(criticalCode);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_critical_code_deleted);
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
				return super.onContextItemSelected(item);
		}
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
				if (editable.length() == 0 && codeEdit != null) {
					codeEdit.setError(getString(R.string.validation_code_required));
				}
				else if (selectedInstance != null && !editable.toString().equals(selectedInstance.getCode())) {
					dirty = true;
				}
			}
		});
		codeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newCode = codeEdit.getText().toString();
					if (selectedInstance != null && !newCode.equals(selectedInstance.getCode())) {
						selectedInstance.setCode(newCode);
						criticalCodeRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<CriticalCode>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("CriticalCodeFragment", "Save failed for: " + selectedInstance, e);
										String toastString = getString(R.string.toast_critical_code_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(CriticalCode savedCriticalCode) {
										onSaved(savedCriticalCode);
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
						criticalCodeRxHandler.save(selectedInstance)
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe(new Subscriber<CriticalCode>() {
									@Override
									public void onCompleted() {
									}
									@Override
									public void onError(Throwable e) {
										Log.e("CriticalCodesFragment",
											  "Exception caught saving new CriticalCode in initDescriptionEdit", e);
										String toastString = getString(R.string.toast_critical_code_save_failed);
										Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
									}
									@Override
									public void onNext(CriticalCode savedCriticalCode) {
										onSaved(savedCriticalCode);
									}
								});
					}
				}
			}
		});
	}

	private void onSaved(CriticalCode criticalCode) {
		if(getActivity() == null) {
			return;
		}

		String toastString;
		toastString = getString(R.string.toast_critical_code_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(criticalCode);
		// Add 1 for the header row
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition() + 1);
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.name_view);
			if (textView != null) {
				textView.setText(criticalCode.getCode());
			}
			textView = (TextView) v.findViewById(R.id.description_view);
			if (textView != null) {
				textView.setText(criticalCode.getDescription());
			}
		}
	}

	private void initListView(View layout) {
		View headerView;

		listView = (ListView) layout.findViewById(R.id.list_view);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.code_description_header, listView, false);
		listView.addHeaderView(headerView);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && selectedInstance != null) {
					selectedInstance.setCode(codeEdit.getText().toString());
					selectedInstance.setDescription(descriptionEdit.getText().toString());
					criticalCodeRxHandler.save(selectedInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<CriticalCode>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("CriticalCodesFragment",
										  "Exception caught saving new CriticalCode in initListView", e);
									String toastString = getString(R.string.toast_critical_code_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(CriticalCode savedCriticalCode) {
									onSaved(savedCriticalCode);
								}
							});
				}

				selectedInstance = (CriticalCode) listView.getItemAtPosition(position);
				if (selectedInstance != null) {
					codeEdit.setText(selectedInstance.getCode());
					descriptionEdit.setText(selectedInstance.getDescription());
				}
			}
		});
		registerForContextMenu(listView);
	}
}

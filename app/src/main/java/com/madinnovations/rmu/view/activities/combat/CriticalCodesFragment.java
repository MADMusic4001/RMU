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
	private CriticalCode currentInstance = new CriticalCode();
	private boolean isNew = true;

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
		inflater.inflate(R.menu.critical_codes_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_critical_code) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CriticalCode();
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
		getActivity().getMenuInflater().inflate(R.menu.critical_code_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CriticalCode criticalCode;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_critical_code:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CriticalCode();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_critical_code:
				criticalCode = (CriticalCode)listView.getItemAtPosition(info.position);
				if(criticalCode != null) {
					deleteItem(criticalCode);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		String newValue = codeEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getCode() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getCode()))) {
			currentInstance.setCode(newValue);
			changed = true;
		}

		newValue = descriptionEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getDescription() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newValue);
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		codeEdit.setText(currentInstance.getCode());
		descriptionEdit.setText(currentInstance.getDescription());
		if(currentInstance.getCode() != null && !currentInstance.getCode().isEmpty()) {
			codeEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			criticalCodeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Subscriber<CriticalCode>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CriticalCodeFragment", "Save failed for: " + currentInstance, e);
							String toastString = getString(R.string.toast_critical_code_save_failed);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CriticalCode savedCriticalCode) {
							if (wasNew) {
								listAdapter.add(savedCriticalCode);
								if(savedCriticalCode == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedCriticalCode));
									listView.setItemChecked(listAdapter.getPosition(savedCriticalCode), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if (getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_critical_code_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedCriticalCode);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.name_view);
									if (textView != null) {
										textView.setText(savedCriticalCode.getCode());
									}
									textView = (TextView) v.findViewById(R.id.description_view);
									if (textView != null) {
										textView.setText(savedCriticalCode.getDescription());
									}
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final CriticalCode item) {
		criticalCodeRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalCodeFragment", "Exception thrown when deleting: " + item, e);
					String toastString = getString(R.string.toast_critical_code_delete_failed);
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
							currentInstance = new CriticalCode();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_critical_code_deleted), Toast.LENGTH_SHORT).show();
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
				if (editable.length() == 0 && codeEdit != null) {
					codeEdit.setError(getString(R.string.validation_code_required));
				}
			}
		});
		codeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newCode = codeEdit.getText().toString();
					if (currentInstance != null && !newCode.equals(currentInstance.getCode())) {
						currentInstance.setCode(newCode);
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
					descriptionEdit.setError(getString(R.string.validation_description_required));
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

		listView.setAdapter(listAdapter);

		criticalCodeRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<CriticalCode>>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e("CriticalCodesFragment", "Exception caught getting all CriticalCode instances", e);
					Toast.makeText(CriticalCodesFragment.this.getActivity(),
							getString(R.string.toast_critical_codes_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<CriticalCode> criticalCodes) {
					listAdapter.clear();
					listAdapter.addAll(criticalCodes);
					listAdapter.notifyDataSetChanged();
					if(criticalCodes.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						copyItemToViews();
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_critical_codes_loaded), criticalCodes.size());
					Toast.makeText(CriticalCodesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CriticalCode) listView.getItemAtPosition(position);
				if (currentInstance == null) {
					currentInstance = new CriticalCode();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
}

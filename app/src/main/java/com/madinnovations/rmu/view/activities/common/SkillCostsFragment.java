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
package com.madinnovations.rmu.view.activities.common;

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
import com.madinnovations.rmu.controller.rxhandler.common.SkillCostRxHandler;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillCostListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for skill costs.
 */
public class SkillCostsFragment extends Fragment {
	@Inject
	protected SkillCostRxHandler   skillCostRxHandler;
	@Inject
	protected SkillCostListAdapter listAdapter;
	private   ListView             listView;
	private   EditText             initialCostEdit;
	private   EditText             additionalCostEdit;
	private SkillCost    currentInstance = new SkillCost();
	private boolean isNew           = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.skill_costs_fragment, container, false);

		initInitialCostEdit(layout);
		initAdditionalCostEdit(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.skill_costs_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_skill_cost) {
			currentInstance = new SkillCost();
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
		getActivity().getMenuInflater().inflate(R.menu.skill_cost_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final SkillCost skillCost;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_skill_cost:
				currentInstance = new SkillCost();
				isNew = true;
				copyItemToControls();
				listView.clearChoices();
				return true;
			case R.id.context_delete_skill_cost:
				skillCost = (SkillCost) listView.getItemAtPosition(info.position);
				if(skillCost != null) {
					deleteItem(skillCost);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	private void copyItemToControls() {
		initialCostEdit.setText(String.valueOf(currentInstance.getInitialCost()));
		additionalCostEdit.setText(String.valueOf(currentInstance.getAdditionalCost()));

		initialCostEdit.setError(null);
		additionalCostEdit.setError(null);
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			skillCostRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<SkillCost>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("SkillCostsFragment", "Exception saving new SkillCost: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_skill_cost_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(SkillCost savedItem) {
							if (isNew) {
								listAdapter.add(savedItem);
								listView.setSelection(listAdapter.getPosition(savedItem));
								listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								isNew = false;
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_skill_cost_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.initial_cost_view);
									textView.setText(String.valueOf(savedItem.getInitialCost()));
									textView = (TextView) v.findViewById(R.id.additional_cost_view);
									textView.setText(String.valueOf(savedItem.getAdditionalCost()));
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final SkillCost item) {
		skillCostRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("SkillCostsFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_skill_cost_delete_failed);
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
								currentInstance = new SkillCost();
								isNew = true;
							}
							copyItemToControls();
							Toast.makeText(getActivity(), getString(R.string.toast_skill_cost_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initInitialCostEdit(View layout) {
		initialCostEdit = (EditText)layout.findViewById(R.id.initial_cost_edit);
		initialCostEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && initialCostEdit != null) {
					initialCostEdit.setError(getString(R.string.validation_initial_cost_required));
				}
			}
		});
		initialCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newInitialCost;
					if(initialCostEdit.getText() != null && initialCostEdit.getText().length() > 0) {
						newInitialCost = Integer.valueOf(initialCostEdit.getText().toString());
					}
					else {
						newInitialCost = null;
					}
					if (currentInstance != null && ( newInitialCost != null && currentInstance.getInitialCost() != newInitialCost)) {
						currentInstance.setInitialCost(newInitialCost);
						saveItem();
					}
				}
			}
		});
	}

	private void initAdditionalCostEdit(View layout) {
		additionalCostEdit = (EditText)layout.findViewById(R.id.additional_cost_edit);
		additionalCostEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && additionalCostEdit != null) {
					additionalCostEdit.setError(getString(R.string.validation_additional_cost_required));
				}
			}
		});
		additionalCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final Integer newAdditionalCost;
					if(additionalCostEdit.getText() != null && additionalCostEdit.getText().length() > 0) {
						newAdditionalCost = Integer.valueOf(additionalCostEdit.getText().toString());
					}
					else {
						newAdditionalCost = null;
					}
					if (currentInstance != null && (
							newAdditionalCost != null && currentInstance.getAdditionalCost() != newAdditionalCost)) {
						currentInstance.setAdditionalCost(newAdditionalCost);
						saveItem();
					}
				}
			}
		});
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		skillCostRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<SkillCost>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("SkillCostsFragment", "Exception caught getting all SkillCost instances", e);
						Toast.makeText(SkillCostsFragment.this.getActivity(), getString(R.string.toast_skill_costs_load_failed),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNext(Collection<SkillCost> skillCosts) {
						listAdapter.clear();
						listAdapter.addAll(skillCosts);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_skill_costs_loaded), skillCosts.size());
						Toast.makeText(SkillCostsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentInstance = (SkillCost) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new SkillCost();
					isNew = true;
				}
				copyItemToControls();
			}
		});
		registerForContextMenu(listView);
	}
}

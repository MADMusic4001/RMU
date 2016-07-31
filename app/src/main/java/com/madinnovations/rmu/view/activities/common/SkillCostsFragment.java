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
import android.content.res.Resources;
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
	private SkillCost    currentInstance = null;
	private boolean dirty           = false;

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

		skillCostRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<SkillCost>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("SkillCostsFragment", "Exception caught getting all SkillCost instances in onCreateView", e);
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
			SkillCost skillCost = new SkillCost();
			Resources resources = getActivity().getResources();
			skillCost.setInitialCost(resources.getInteger(R.integer.default_skill_cost_initial_cost));
			skillCost.setAdditionalCost(resources.getInteger(R.integer.default_skill_cost_additional_cost));
			skillCostRxHandler.save(skillCost)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<SkillCost>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {
							Log.e("SkillCostsFragment", "Exception saving new SkillCost in onOptionsItemSelected", e);
						}

						@Override
						public void onNext(SkillCost savedSkillCost) {
							listAdapter.add(savedSkillCost);
							initialCostEdit.setText(String.valueOf(savedSkillCost.getInitialCost()));
							additionalCostEdit.setText(String.valueOf(savedSkillCost.getAdditionalCost()));
							currentInstance = savedSkillCost;
						}
					});
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
			case R.id.context_delete_skill_cost:
				skillCost = (SkillCost)listView.getItemAtPosition(info.position);
				if(skillCost != null) {
					skillCostRxHandler.deleteById(skillCost.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Boolean>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									Log.e("SkillCostFragment", "Exception when deleting: " + skillCost, e);
									String toastString = getString(R.string.toast_skill_cost_delete_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Boolean success) {
									String toastString;

									if(success) {
										listAdapter.remove(skillCost);
										listAdapter.notifyDataSetChanged();
										toastString = getString(R.string.toast_skill_cost_deleted);
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
				else if (currentInstance != null && ((editable.length() == 0) ||
						(editable.length() > 0 &&
								!Integer.valueOf(editable.toString()).equals(currentInstance.getInitialCost())))) {
					dirty = true;
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
						dirty = true;
						currentInstance.setInitialCost(newInitialCost);
						Log.d("SkillCostsFragment", "Saving skillCost");
						save();
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
				else if (currentInstance != null && ((editable.length() == 0) ||
						(editable.length() > 0 &&
								!Integer.valueOf(editable.toString()).equals(currentInstance.getInitialCost())))) {
					dirty = true;
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
						dirty = true;
						currentInstance.setAdditionalCost(newAdditionalCost);
						Log.d("SkillCostsFragment", "Saving skillCost");
						save();
					}
				}
			}
		});
	}

	private void save() {
		skillCostRxHandler.save(currentInstance)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<SkillCost>() {
				@Override
				public void onCompleted() {
				}

				@Override
				public void onError(Throwable e) {
					Log.e("SkillCostsFragment", "Exception saving SkillCost", e);
					String toastString = getString(R.string.toast_skill_cost_save_failed);
					Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onNext(SkillCost savedSkillCost) {
					onSaved(savedSkillCost);
				}
			});
	}

	private void onSaved(SkillCost skillCost) {
		if(getActivity() == null) {
			return;
		}
		String toastString;
		toastString = getString(R.string.toast_skill_cost_saved);
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

		int position = listAdapter.getPosition(skillCost);
		LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
		if (v != null) {
			TextView textView = (TextView) v.findViewById(R.id.initial_cost_view);
			if (textView != null) {
				textView.setText(String.valueOf(skillCost.getInitialCost()));
			}
			textView = (TextView) v.findViewById(R.id.additional_cost_view);
			if (textView != null) {
				textView.setText(String.valueOf(skillCost.getAdditionalCost()));
			}
		}
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(dirty && currentInstance != null) {
					if(initialCostEdit.getText().length() > 0) {
						currentInstance.setInitialCost(Integer.valueOf(initialCostEdit.getText().toString()));
					}
					if(additionalCostEdit.getText().length() > 0) {
						currentInstance.setAdditionalCost(Integer.valueOf(additionalCostEdit.getText().toString()));
					}
					skillCostRxHandler.save(currentInstance)
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<SkillCost>() {
								@Override
								public void onCompleted() {
								}
								@Override
								public void onError(Throwable e) {
									Log.e("SkillCostsFragment", "Exception saving new SkillCost in initListView", e);
									String toastString = getString(R.string.toast_skill_cost_save_failed);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
								@Override
								public void onNext(SkillCost savedSkillCost) {
									onSaved(savedSkillCost);
								}
							});
					dirty = false;
				}

				currentInstance = (SkillCost) listView.getItemAtPosition(position);
				if (currentInstance != null) {
					initialCostEdit.setText(String.valueOf(currentInstance.getInitialCost()));
					additionalCostEdit.setText(String.valueOf(currentInstance.getAdditionalCost()));
				}
			}
		});
		registerForContextMenu(listView);
	}
}

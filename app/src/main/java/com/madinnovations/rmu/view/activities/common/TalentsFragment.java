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
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.DragEvent;
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
import com.madinnovations.rmu.controller.rxhandler.common.ParameterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.ParameterValue;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.common.DragParameterListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentCategorySpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentParameterListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment implements TwoFieldListAdapter.GetValues<Talent> {
	private static final String DRAG_ADD_PARAMETER = "add-parameter";
	private static final String DRAG_REMOVE_PARAMETER = "remove-parameter";
	@Inject
	protected TalentRxHandler talentRxHandler;
	@Inject
	TalentCategoryRxHandler talentCategoryRxHandler;
	@Inject
	SkillRxHandler skillRxHandler;
	@Inject
	ParameterRxHandler parameterRxHandler;
	@Inject
	protected TalentCategorySpinnerAdapter categorySpinnerAdapter;
	@Inject
	protected SkillSpinnerAdapter affectedSkillSpinnerAdapter;
	@Inject
	protected DragParameterListAdapter parametersListAdapter;
	@Inject
	protected TalentParameterListAdapter selectedParametersListAdapter;
	private TwoFieldListAdapter<Talent> listAdapter;
	private ListView listView;
	private Spinner  categorySpinner;
	private EditText nameEdit;
	private EditText descriptionEdit;
	private Spinner  affectedSkillSpinner;
	private EditText initialCostEdit;
	private EditText costPerTierEdit;
	private EditText bonusPerTierEdit;
	private CheckBox flawCheckbox;
	private CheckBox situationalCheckbox;
	private EditText actionPointsEdit;
	private ListView selectedParametersList;
	private Talent currentInstance = new Talent();
	private boolean          isNew            = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_talent_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_talent_description));

		initCategorySpinner(layout);
		initNameEdit(layout);
		initDescriptionEdit(layout);
		initAffectedSkillSpinner(layout);
		initInitialCostEdit(layout);
		initCostPerTierEdit(layout);
		initBonusPerTierEdit(layout);
		initFlawCheckbox(layout);
		initSituationalCheckbox(layout);
		initActionPointsEdit(layout);
		initParametersListView(layout);
		initSelectedParametersListView(layout);
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
		inflater.inflate(R.menu.talents_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Talent();
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
		getActivity().getMenuInflater().inflate(R.menu.talent_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Talent talent;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_talent:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Talent();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_talent:
				talent = (Talent)listView.getItemAtPosition(info.position);
				if(talent != null) {
					deleteItem(talent);
					return true;
				}
			case R.id.context_delete_parameter:
				ParameterValue parameterValue = (ParameterValue) selectedParametersList.getItemAtPosition(info.position);
				if(parameterValue != null) {
					selectedParametersListAdapter.remove(parameterValue);
					selectedParametersListAdapter.notifyDataSetChanged();
					saveItem();
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;
		short newShort;

		if(categorySpinner.getSelectedItemPosition() != -1) {
			TalentCategory newCategory = categorySpinnerAdapter.getItem(categorySpinner.getSelectedItemPosition());
			if ((newCategory == null && currentInstance.getCategory() != null) ||
					(newCategory != null && !newCategory.equals(currentInstance.getCategory()))) {
				currentInstance.setCategory(newCategory);
				changed = true;
			}
		}

		String newValue = nameEdit.getText().toString();
		if(newValue.isEmpty()) {
			newValue = null;
		}
		if((newValue == null && currentInstance.getName() != null) ||
				(newValue != null && !newValue.equals(currentInstance.getName()))) {
			currentInstance.setName(newValue);
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

		if(affectedSkillSpinner.getSelectedItemPosition() != -1) {
			Skill newSkill = affectedSkillSpinnerAdapter.getItem(affectedSkillSpinner.getSelectedItemPosition());
			if ((newSkill == null && currentInstance.getAffectedSkill() != null) ||
					(newSkill != null && !newSkill.equals(currentInstance.getAffectedSkill()))) {
				currentInstance.setAffectedSkill(newSkill);
				changed = true;
			}
		}

		if(initialCostEdit.getText().length() > 0) {
			newShort = Short.valueOf(initialCostEdit.getText().toString());
			if(newShort != currentInstance.getDpCost()) {
				currentInstance.setDpCost(newShort);
				changed = true;
			}
		}

		if(costPerTierEdit.getText().length() > 0) {
			newShort = Short.valueOf(costPerTierEdit.getText().toString());
			if(newShort != currentInstance.getDpCostPerTier()) {
				currentInstance.setDpCostPerTier(newShort);
				changed = true;
			}
		}

		if(bonusPerTierEdit.getText().length() > 0) {
			newShort = Short.valueOf(bonusPerTierEdit.getText().toString());
			if(newShort != currentInstance.getBonusPerTier()) {
				currentInstance.setBonusPerTier(newShort);
				changed = true;
			}
		}

		if(situationalCheckbox.isChecked() != currentInstance.isSituational()) {
			currentInstance.setSituational(situationalCheckbox.isChecked());
			changed = true;
		}

		if(actionPointsEdit.getText().length() > 0) {
			newShort = Short.valueOf(actionPointsEdit.getText().toString());
			if(newShort != currentInstance.getActionPoints()) {
				currentInstance.setActionPoints(newShort);
				changed = true;
			}
		}

		boolean updateParameters = false;
		if(selectedParametersListAdapter.getCount() != currentInstance.getParameterValues().size()) {
			updateParameters = true;
		}
		else {
			for(ParameterValue parameterValue : currentInstance.getParameterValues()) {
				if(selectedParametersListAdapter.getPosition(parameterValue) == -1) {
					updateParameters = true;
					break;
				}
			}
		}
		if(updateParameters) {
			currentInstance.getParameterValues().clear();
			for(int i = 0; i < selectedParametersListAdapter.getCount(); i++) {
				currentInstance.getParameterValues().add(selectedParametersListAdapter.getItem(i));
			}
			changed = true;
		}

		return changed;
	}

	private void copyItemToViews() {
		categorySpinner.setSelection(categorySpinnerAdapter.getPosition(currentInstance.getCategory()));
		nameEdit.setText(currentInstance.getName());
		descriptionEdit.setText(currentInstance.getDescription());
		affectedSkillSpinner.setSelection(affectedSkillSpinnerAdapter.getPosition(currentInstance.getAffectedSkill()));
		initialCostEdit.setText(String.valueOf(currentInstance.getDpCost()));
		costPerTierEdit.setText(String.valueOf(currentInstance.getDpCostPerTier()));
		bonusPerTierEdit.setText(String.valueOf(currentInstance.getBonusPerTier()));
		flawCheckbox.setChecked(currentInstance.isFlaw());
		situationalCheckbox.setChecked(currentInstance.isSituational());
		actionPointsEdit.setText(String.valueOf(currentInstance.getActionPoints()));
		selectedParametersListAdapter.clear();
		selectedParametersListAdapter.addAll(currentInstance.getParameterValues());
		selectedParametersListAdapter.notifyDataSetChanged();

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			talentRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<Talent>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("TalentsFragment", "Exception saving new Talent: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_talent_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(Talent savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_talent_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final Talent item) {
		talentRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_talent_delete_failed);
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
								currentInstance = new Talent();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_talent_category_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initCategorySpinner(View layout) {
		categorySpinner = (Spinner)layout.findViewById(R.id.category_spinner);
		categorySpinner.setAdapter(categorySpinnerAdapter);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<TalentCategory> items) {
						categorySpinnerAdapter.clear();
						categorySpinnerAdapter.addAll(items);
						categorySpinnerAdapter.notifyDataSetChanged();
					}
				});

		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCategory() == null || categorySpinnerAdapter.getPosition(currentInstance.getCategory()) != position) {
					currentInstance.setCategory(categorySpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getCategory() != null) {
					currentInstance.setCategory(null);
					saveItem();
				}
			}
		});
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
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_talent_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(currentInstance.getName())) {
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
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_talent_description_required));
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

	private void initAffectedSkillSpinner(View layout) {
		affectedSkillSpinner = (Spinner)layout.findViewById(R.id.affected_skill_spinner);
		affectedSkillSpinner.setAdapter(affectedSkillSpinnerAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all TalentCategory instances", e);
					}
					@Override
					public void onNext(Collection<Skill> items) {
						affectedSkillSpinnerAdapter.clear();
						affectedSkillSpinnerAdapter.addAll(items);
						affectedSkillSpinnerAdapter.notifyDataSetChanged();
					}
				});

		affectedSkillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getCategory() == null || affectedSkillSpinnerAdapter.getPosition(currentInstance.getAffectedSkill()) != position) {
					currentInstance.setAffectedSkill(affectedSkillSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getAffectedSkill() != null) {
					currentInstance.setAffectedSkill(null);
					saveItem();
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
					initialCostEdit.setError(getString(R.string.validation_talent_initial_cost_required));
				}
			}
		});
		initialCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(initialCostEdit.getText().length() > 0) {
						short newHits = Short.valueOf(initialCostEdit.getText().toString());
						if (newHits != currentInstance.getDpCost()) {
							currentInstance.setDpCost(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initCostPerTierEdit(View layout) {
		costPerTierEdit = (EditText)layout.findViewById(R.id.cost_per_tier_edit);
		costPerTierEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && costPerTierEdit != null) {
					costPerTierEdit.setError(getString(R.string.validation_talent_cost_per_tier_required));
				}
			}
		});
		costPerTierEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(costPerTierEdit.getText().length() > 0) {
						short newHits = Short.valueOf(costPerTierEdit.getText().toString());
						if (newHits != currentInstance.getDpCostPerTier()) {
							currentInstance.setDpCostPerTier(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBonusPerTierEdit(View layout) {
		bonusPerTierEdit = (EditText)layout.findViewById(R.id.bonus_per_tier_edit);
		bonusPerTierEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && bonusPerTierEdit != null) {
					bonusPerTierEdit.setError(getString(R.string.validation_talent_bonus_per_tier_required));
				}
			}
		});
		bonusPerTierEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(bonusPerTierEdit.getText().length() > 0) {
						short newHits = Short.valueOf(bonusPerTierEdit.getText().toString());
						if (newHits != currentInstance.getBonusPerTier()) {
							currentInstance.setBonusPerTier(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initFlawCheckbox(View layout) {
		flawCheckbox = (CheckBox) layout.findViewById(R.id.flaw_check_box);
		flawCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setFlaw(situationalCheckbox.isChecked());
				saveItem();
			}
		});
	}

	private void initSituationalCheckbox(View layout) {
		situationalCheckbox = (CheckBox) layout.findViewById(R.id.situational_check_box);
		situationalCheckbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				currentInstance.setSituational(situationalCheckbox.isChecked());
				saveItem();
			}
		});
	}

	private void initActionPointsEdit(View layout) {
		actionPointsEdit = (EditText)layout.findViewById(R.id.action_points_edit);
		actionPointsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && actionPointsEdit != null) {
					actionPointsEdit.setError(getString(R.string.validation_talent_action_points_required));
				}
			}
		});
		actionPointsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(actionPointsEdit.getText().length() > 0) {
						short newHits = Short.valueOf(actionPointsEdit.getText().toString());
						if (newHits != currentInstance.getActionPoints()) {
							currentInstance.setActionPoints(newHits);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initParametersListView(View layout) {
		ListView parametersListView = (ListView) layout.findViewById(R.id.parameters_list);

		parametersListView.setAdapter(parametersListAdapter);

		parametersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				String positionString = String.valueOf(position);
				ClipData.Item clipDataItem = new ClipData.Item(positionString);
				ClipData dragData = new ClipData(DRAG_ADD_PARAMETER, new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
						clipDataItem);

				View.DragShadowBuilder myShadow = new MyDragShadowBuilder(view);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
				}
				return false;
			}
		});
		parameterRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Parameter>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("TalentsFragment", "Exception caught getting all Parameter instances in initParametersListView", e);
					}
					@Override
					public void onNext(Collection<Parameter> items) {
						parametersListAdapter.clear();
						parametersListAdapter.addAll(items);
						parametersListAdapter.notifyDataSetChanged();
					}
				});
	}

	private void initSelectedParametersListView(View layout) {
		selectedParametersList = (ListView) layout.findViewById(R.id.selected_parameters_list);
		selectedParametersList.setAdapter(selectedParametersListAdapter);
		selectedParametersList.setOnDragListener(new MyDragEventListener());
		selectedParametersListAdapter.clear();
		selectedParametersListAdapter.addAll(currentInstance.getParameterValues());
		selectedParametersListAdapter.notifyDataSetChanged();
		registerForContextMenu(selectedParametersList);
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		talentRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Talent>>() {
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
						Log.e("TalentsFragment", "Exception caught getting all Talent instances", e);
						Toast.makeText(TalentsFragment.this.getActivity(),
								getString(R.string.toast_talents_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						listAdapter.clear();
						listAdapter.addAll(talents);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_talents_loaded), talents.size());
						Toast.makeText(TalentsFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Talent) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Talent();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	protected class MyDragEventListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = selectedParametersList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription().getLabel().equals(DRAG_ADD_PARAMETER)) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					v.setBackground(hoverShape);
					v.invalidate();
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					v.setBackground(targetShape);
					v.invalidate();
					break;
				case DragEvent.ACTION_DROP:
					for(int i = 0; i < event.getClipData().getItemCount(); i++) {
						ClipData.Item item = event.getClipData().getItemAt(i);
						int position = Integer.valueOf(item.getText().toString());
						Parameter parameter = parametersListAdapter.getItem(position);
						ParameterValue parameterValue = new ParameterValue(parameter, null);
						if (selectedParametersListAdapter.getPosition(parameterValue) == -1) {
							selectedParametersListAdapter.add(parameterValue);
						}
					}
					selectedParametersListAdapter.notifyDataSetChanged();
					v.setBackground(normalShape);
					v.invalidate();
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackground(normalShape);
					v.invalidate();
					event.getResult();
					break;
			}

			return true;
		}
	}

	private static class MyDragShadowBuilder extends View.DragShadowBuilder {
		private static Drawable shadow;

		public MyDragShadowBuilder(View v) {
			super(v);
			shadow = new ColorDrawable(Color.LTGRAY);
		}

		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			int width, height;

			width = getView().getWidth() * 3/4;
			height = getView().getHeight() * 3/4;
			shadow.setBounds(0, 0, width, height);
			size.set(width, height);
			touch.set(width / 2, height / 2);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			shadow.draw(canvas);
		}
	}

	@Override
	public CharSequence getField1Value(Talent talent) {
		return talent.getName();
	}

	@Override
	public CharSequence getField2Value(Talent talent) {
		return talent.getDescription();
	}
}

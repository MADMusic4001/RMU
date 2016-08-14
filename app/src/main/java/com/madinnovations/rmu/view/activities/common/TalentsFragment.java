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
import com.madinnovations.rmu.view.adapters.common.DragParameterListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentCategorySpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentListAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentParameterListAdapter;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for talents.
 */
public class TalentsFragment extends Fragment {
	@Inject
	protected TalentRxHandler talentRxHandler;
	@Inject
	TalentCategoryRxHandler talentCategoryRxHandler;
	@Inject
	SkillRxHandler skillRxHandler;
	@Inject
	ParameterRxHandler parameterRxHandler;
	@Inject
	protected TalentListAdapter listAdapter;
	@Inject
	protected TalentCategorySpinnerAdapter categorySpinnerAdapter;
	@Inject
	protected SkillSpinnerAdapter affectedSkillSpinnerAdapter;
	@Inject
	protected DragParameterListAdapter parametersListAdapter;
	@Inject
	protected TalentParameterListAdapter selectedParametersListAdapter;
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
	private ListView parametersListview;
	private ListView selectedParametersListview;
	private List<ParameterValue> parameterValueList = new ArrayList<>();
	private Talent currentInstance = new Talent();
	private boolean          isNew            = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCommonFragmentComponent(new CommonFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talents_fragment, container, false);

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
		initParametersListview(layout);
		initSelectedParametersListview(layout);
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
				ParameterValue parameterValue = (ParameterValue)selectedParametersListview.getItemAtPosition(info.position);
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
									TextView textView = (TextView) v.findViewById(R.id.name_view);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.description_view);
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
					actionPointsEdit.setError(getString(R.string.validation_hits_required));
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

	private void initParametersListview(View layout) {
		parametersListview = (ListView) layout.findViewById(R.id.parameters_list);

		parametersListview.setAdapter(parametersListAdapter);

		parametersListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				String positionString = String.valueOf(position);
				ClipData.Item clipDataItem = new ClipData.Item(positionString);
				ClipData dragData = new ClipData(positionString, new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},
						clipDataItem);

				View.DragShadowBuilder myShadow = new MyDragShadowBuilder(view);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
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
						Log.e("TalentsFragment", "Exception caught getting all Parameter instances in initParametersListview", e);
					}
					@Override
					public void onNext(Collection<Parameter> items) {
						parametersListAdapter.clear();
						parametersListAdapter.addAll(items);
						parametersListAdapter.notifyDataSetChanged();
					}
				});
	}

//	private void initAddParameterButton(View layout) {
//		Button addParameterButton = (Button) layout.findViewById(R.id.add_parameter_button);
//
//		addParameterButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				boolean change = false;
//				SparseBooleanArray checkedList = parametersListview.getCheckedItemPositions();
//				for(int i = 0; i < checkedList.size(); i++) {
//					if(checkedList.valueAt(i)) {
//						if(selectedParametersListAdapter.getPosition(parametersListAdapter.getItem(checkedList.keyAt(i))) == -1) {
//							Parameter parameter = parametersListAdapter.getItem(checkedList.keyAt(i));
//							selectedParametersListAdapter.add(parameter);
//							currentInstance.getParameters().add(parameter);
//							change = true;
//						}
//					}
//				}
//				parametersListview.clearChoices();
//				parametersListAdapter.notifyDataSetChanged();
//				selectedParametersListAdapter.notifyDataSetChanged();
//				if(change) {
//					saveItem();
//				}
//			}
//		});
//	}
//
//	private void initRemoveParameterButton(View layout) {
//		Button removeParameterButton = (Button) layout.findViewById(R.id.remove_parameter_button);
//
//		removeParameterButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				boolean change = false;
//				SparseBooleanArray checkedList = selectedParametersListview.getCheckedItemPositions();
//				for(int i = checkedList.size() - 1 ; i >=0 ; i--) {
//					if(checkedList.valueAt(i)) {
//						Parameter parameter = selectedParametersListAdapter.getItem(checkedList.keyAt(i));
//						selectedParametersListAdapter.remove(parameter);
//						currentInstance.getParameters().remove(parameter);
//						change = true;
//					}
//				}
//				selectedParametersListview.clearChoices();
//				selectedParametersListAdapter.notifyDataSetChanged();
//				if(change) {
//					saveItem();
//				}
//			}
//		});
//	}

	private void initSelectedParametersListview(View layout) {
		selectedParametersListview = (ListView) layout.findViewById(R.id.selected_parameters_list);
		selectedParametersListview.setAdapter(selectedParametersListAdapter);
		selectedParametersListview.setOnDragListener(new MyDragEventListener());
		selectedParametersListAdapter.clear();
		selectedParametersListAdapter.addAll(currentInstance.getParameterValues());
		selectedParametersListAdapter.notifyDataSetChanged();
		registerForContextMenu(selectedParametersListview);
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);

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

		// Clicking a row in the listView will send the user to the edit world activity
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
		private Drawable originalDrawable;
		private int originalBackgroundColor;

		// This is the method that the system calls when it dispatches a drag event to the
		// listener.
		public boolean onDrag(View v, DragEvent event) {

			// Defines a variable to store the action type for the incoming event
			final int action = event.getAction();

			// Handles each of the expected events
			switch(action) {

				case DragEvent.ACTION_DRAG_STARTED:

					// Determines if this View can accept the dragged data
					if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

						// As an example of what your application might do,
						// applies a blue color tint to the View to indicate that it can accept
						// data.
						originalDrawable = v.getBackground();
						v.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background,
								null));

						// Invalidate the view to force a redraw in the new tint
						v.invalidate();

						// returns true to indicate that the View can accept the dragged data.
						return true;

					}

					// Returns false. During the current drag and drop operation, this View will
					// not receive events again until ACTION_DRAG_ENDED is sent.
					return false;

				case DragEvent.ACTION_DRAG_ENTERED:

					// Applies a green tint to the View. Return true; the return value is ignored.

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						v.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null));
					}
					else {
						v.setBackgroundColor(Color.GREEN);
					}

					// Invalidate the view to force a redraw in the new tint
					v.invalidate();

					return true;

				case DragEvent.ACTION_DRAG_LOCATION:

					// Ignore the event
					return true;

				case DragEvent.ACTION_DRAG_EXITED:

					// Re-sets the color tint to blue. Returns true; the return value is ignored.
					v.setBackground(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background,
							null));

					// Invalidate the view to force a redraw in the new tint
					v.invalidate();

					return true;

				case DragEvent.ACTION_DROP:

					// Gets the item containing the dragged data
					ClipData.Item item = event.getClipData().getItemAt(0);

					// Gets the text data from the item.
					CharSequence dragData = item.getText();
					int position = Integer.valueOf(item.getText().toString());
					Parameter parameter = parametersListAdapter.getItem(position);

					Log.e("TalentsFragment", "parameter = " + parameter);
					Toast.makeText(getActivity(), "Dragged data is " + parameter.getName(), Toast.LENGTH_LONG).show();
					selectedParametersListAdapter.add(new ParameterValue(parameter, null));
					selectedParametersListAdapter.notifyDataSetChanged();
					// Displays a message containing the dragged data.

					// Turns off any color tints
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						v.setBackground(originalDrawable);
					}
					else {
						v.setBackgroundColor(originalBackgroundColor);
					}

					// Invalidates the view to force a redraw
					v.invalidate();

					// Returns true. DragEvent.getResult() will return true.
					return true;

				case DragEvent.ACTION_DRAG_ENDED:

					// Turns off any color tinting
					v.setBackgroundColor(originalBackgroundColor);

					// Invalidates the view to force a redraw
					v.invalidate();

					// Does a getResult(), and displays what happened.
					if (event.getResult()) {
						Toast.makeText(getActivity(), "The drop was handled.", Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(getActivity(), "The drop didn't work.", Toast.LENGTH_LONG).show();

					}

					// returns true; the value is ignored.
					return true;

				// An unknown action type was received.
				default:
					Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
					break;
			}

			return false;
		}
	}

	private static class MyDragShadowBuilder extends View.DragShadowBuilder {

		// The drag shadow image, defined as a drawable thing
		private static Drawable shadow;

		// Defines the constructor for myDragShadowBuilder
		public MyDragShadowBuilder(View v) {

			// Stores the View parameter passed to myDragShadowBuilder.
			super(v);

			// Creates a draggable image that will fill the Canvas provided by the system.
			shadow = new ColorDrawable(Color.LTGRAY);
		}

		// Defines a callback that sends the drag shadow dimensions and touch point back to the
		// system.
		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			// Defines local variables
			int width, height;

			// Sets the width of the shadow to half the width of the original View
			width = getView().getWidth() / 2;

			// Sets the height of the shadow to half the height of the original View
			height = getView().getHeight() / 2;

			// The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
			// Canvas that the system will provide. As a result, the drag shadow will fill the
			// Canvas.
			shadow.setBounds(0, 0, width, height);

			// Sets the size parameter's width and height values. These get back to the system
			// through the size parameter.
			size.set(width, height);

			// Sets the touch point's position to be in the middle of the drag shadow
			touch.set(width / 2, height / 2);
		}

		// Defines a callback that draws the drag shadow in a Canvas that the system constructs
		// from the dimensions passed in onProvideShadowMetrics().
		@Override
		public void onDrawShadow(Canvas canvas) {

			// Draws the ColorDrawable in the Canvas passed in from the system.
			shadow.draw(canvas);
		}
	}
}

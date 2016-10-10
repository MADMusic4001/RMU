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
package com.madinnovations.rmu.view.adapters.character;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;
import com.madinnovations.rmu.data.entities.character.SkillCostEntry;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link ProfessionSkillCategoryCost} information
 */
public class ProfessionCategoryCostListAdapter extends BaseExpandableListAdapter {
	private static final int GROUP_LAYOUT_RESOURCE_ID = R.layout.list_profession_category_costs_group_row;
	private static final int CHILD_LAYOUT_RESOURCE_ID = R.layout.list_profession_category_costs_child_row;
	private Context context;
	private ExpandableListView listView;
	private LayoutInflater layoutInflater;
	private List<ProfessionSkillCategoryCost> listData = new ArrayList<>();
	private ProfessionCostsCallbacks callbackImpl;

	public void clear() {
		listData.clear();
	}

	public void add(ProfessionSkillCategoryCost professionSkillCategoryCost) {
		listData.add(professionSkillCategoryCost);
	}

	public void addAll(Collection<ProfessionSkillCategoryCost> professionSkillCategoryCosts) {
		listData.addAll(professionSkillCategoryCosts);
	}

	/**
	 * Creates a new ProfessionCategoryCostListAdapter instance.
	 *
	 * @param context  a {@link Context} instance to use to get a LayoutInflater
	 * @param callbackImpl  an implementation of the {@link ProfessionCostsCallbacks} interface
	 * @param listView  the {@link ExpandableListView} instance this adapter will be attached to
	 */
	@Inject
	public ProfessionCategoryCostListAdapter(@NonNull Context context,
											 @NonNull ProfessionCostsCallbacks callbackImpl,
											 @NonNull ExpandableListView listView) {
		this.context = context;
		this.callbackImpl = callbackImpl;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listView = listView;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return listData.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listData.get(groupPosition).getSkillCosts().get(childPosition);
	}

	@Override
	public int getGroupCount() {
		return listData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ProfessionSkillCategoryCost skillCategoryCost = listData.get(groupPosition);
		if(skillCategoryCost.getSkillCosts() == null) {
			return 0;
		}
		return skillCategoryCost.getSkillCosts().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View rowView;
		GroupViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(GROUP_LAYOUT_RESOURCE_ID, parent, false);
			holder = new GroupViewHolder((TextView) rowView.findViewById(R.id.name_view),
									(EditText)rowView.findViewById(R.id.initial_cost_edit),
									(EditText)rowView.findViewById(R.id.additional_cost_edit),
									(CheckBox)rowView.findViewById(R.id.assignable_check_box));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (GroupViewHolder) convertView.getTag();
		}

		ProfessionSkillCategoryCost skillCategoryCost = (ProfessionSkillCategoryCost)getGroup(groupPosition);
		holder.skillCategoryCost = skillCategoryCost;
		holder.nameView.setText(skillCategoryCost.getSkillCategory().getName());
		if(skillCategoryCost.getSkillCategoryCost() == null) {
			holder.initialCostEdit.setText(null);
			holder.additionalCostEdit.setText(null);
		}
		else {
			if(skillCategoryCost.getSkillCategoryCost().getFirstCost() == null) {
				holder.initialCostEdit.setText(null);
			}
			else {
				holder.initialCostEdit.setText(String.valueOf(skillCategoryCost.getSkillCategoryCost().getFirstCost()));
			}
			if(skillCategoryCost.getSkillCategoryCost().getAdditionalCost() == null) {
				holder.additionalCostEdit.setText("");
			}
			else {
				holder.additionalCostEdit.setText(String.valueOf(skillCategoryCost.getSkillCategoryCost().getAdditionalCost()));
			}
		}
		holder.assignableCheckBox.setChecked(skillCategoryCost.isAssignable());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View rowView;
		ChildViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(CHILD_LAYOUT_RESOURCE_ID, parent, false);
			holder = new ChildViewHolder((TextView) rowView.findViewById(R.id.name_view),
										 (EditText)rowView.findViewById(R.id.initial_cost_edit),
										 (EditText)rowView.findViewById(R.id.additional_cost_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ChildViewHolder) convertView.getTag();
		}

		SkillCostEntry skillCost = (SkillCostEntry)getChild(groupPosition, childPosition);
		holder.skillCost = skillCost;
		holder.nameView.setText(skillCost.getSkill().getName());
		if(skillCost.getSkillCost() == null) {
			holder.initialCostEdit.setText(null);
			holder.additionalCostEdit.setText(null);
		}
		else {
			if(skillCost.getSkillCost().getFirstCost() == null) {
				holder.initialCostEdit.setText(null);
			}
			else {
				holder.initialCostEdit.setText(String.valueOf(skillCost.getSkillCost().getFirstCost()));
			}
			if(skillCost.getSkillCost().getAdditionalCost() == null) {
				holder.additionalCostEdit.setText(null);
			}
			else {
				holder.additionalCostEdit.setText(String.valueOf(skillCost.getSkillCost().getAdditionalCost()));
			}
		}
		return rowView;
	}

	private class GroupViewHolder {
		private ProfessionSkillCategoryCost skillCategoryCost;
		private TextView nameView;
		private EditText initialCostEdit;
		private EditText additionalCostEdit;
		private CheckBox assignableCheckBox;

		GroupViewHolder(TextView nameView, EditText initialCostEdit, EditText additionalCostEdit, CheckBox assignableCheckBox) {
			this.nameView = nameView;
			initNameView();
			this.initialCostEdit = initialCostEdit;
			initInitialCostEdit();
			this.additionalCostEdit = additionalCostEdit;
			initAdditionalCostEdit();
			this.assignableCheckBox = assignableCheckBox;
			initAssignableCheckBox();
		}

		private void initNameView() {
			nameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = listData.indexOf(skillCategoryCost);
					if(listView.isGroupExpanded(position)) {
						listView.collapseGroup(position);
					}
					else {
						listView.expandGroup(position);
					}
				}
			});
		}

		private void initInitialCostEdit() {
			initialCostEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.length() > 0) {
						short newValue = Short.valueOf(editable.toString());
						int otherValue;
						if(additionalCostEdit.length() > 0) {
							otherValue = Integer.valueOf(additionalCostEdit.getText().toString());
							if(otherValue < newValue) {
								initialCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
							}
							else {
								initialCostEdit.setError(null);
								additionalCostEdit.setError(null);
							}
						}
					}
				}
			});
			initialCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof GroupViewHolder && initialCostEdit == view) {
							boolean changed = false;
							Profession profession = callbackImpl.getProfessionInstance();
							SkillCategory skillCategory = skillCategoryCost.getSkillCategory();
							SkillCost currentCategorySkillCost = profession.getSkillCategoryCosts().get(skillCategory);
							if (initialCostEdit.length() > 0) {
								if(currentCategorySkillCost == null) {
									currentCategorySkillCost = new SkillCost();
									profession.getSkillCategoryCosts().put(skillCategory, currentCategorySkillCost);
									changed = true;
								}
								Short newCost = Short.valueOf(initialCostEdit.getText().toString());
								skillCategoryCost.getSkillCategoryCost().setFirstCost(newCost);
								if(!newCost.equals(currentCategorySkillCost.getFirstCost())) {
									currentCategorySkillCost.setFirstCost(newCost);
									changed = true;
								}
							}
							else {
								if(currentCategorySkillCost != null && currentCategorySkillCost.getFirstCost() != null) {
									changed = true;
									currentCategorySkillCost.setFirstCost(null);
									skillCategoryCost.getSkillCategoryCost().setFirstCost(null);
								}
							}
							if(changed) {
								callbackImpl.saveItem();
							}
						}
					}
				}
			});
		}

		private void initAdditionalCostEdit() {
			additionalCostEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.length() > 0) {
						Short newValue = Short.valueOf(editable.toString());
						Short otherValue;
						if(initialCostEdit.length() > 0) {
							otherValue = Short.valueOf(initialCostEdit.getText().toString());
							if(newValue < otherValue) {
								additionalCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
							}
							else {
								additionalCostEdit.setError(null);
								initialCostEdit.setError(null);
							}
						}
					}
				}
			});
			additionalCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof GroupViewHolder && additionalCostEdit == view) {
							boolean changed = false;
							Profession profession = callbackImpl.getProfessionInstance();
							SkillCategory skillCategory = skillCategoryCost.getSkillCategory();
							SkillCost currentCategorySkillCost = profession.getSkillCategoryCosts().get(skillCategory);
							if (additionalCostEdit.length() > 0) {
								if(currentCategorySkillCost == null) {
									currentCategorySkillCost = new SkillCost();
									profession.getSkillCategoryCosts().put(skillCategory, currentCategorySkillCost);
									changed = true;
								}
								Short newCost = Short.valueOf(additionalCostEdit.getText().toString());
								skillCategoryCost.getSkillCategoryCost().setAdditionalCost(newCost);
								if(!newCost.equals(currentCategorySkillCost.getFirstCost())) {
									currentCategorySkillCost.setAdditionalCost(newCost);
									changed = true;
								}
							}
							else {
								if(currentCategorySkillCost != null && currentCategorySkillCost.getFirstCost() != null) {
									changed = true;
									currentCategorySkillCost.setAdditionalCost(null);
									skillCategoryCost.getSkillCategoryCost().setAdditionalCost(null);
								}
							}
							if(changed) {
								callbackImpl.saveItem();
							}
						}
					}
				}
			});
		}

		private void initAssignableCheckBox() {
			assignableCheckBox.setOnCheckedChangeListener(
					new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							skillCategoryCost.setAssignable(isChecked);
							if(isChecked) {
								List<SkillCost> assignableCosts;
								if(callbackImpl.getProfessionInstance().getAssignableSkillCosts()
										.get(skillCategoryCost.getSkillCategory()) == null) {
									assignableCosts = new ArrayList<>(skillCategoryCost.getSkillCosts().size());
									callbackImpl.getProfessionInstance().getAssignableSkillCosts()
											.put(skillCategoryCost.getSkillCategory(), assignableCosts);
								}
								else {
									assignableCosts = callbackImpl.getProfessionInstance().getAssignableSkillCosts()
											.get(skillCategoryCost.getSkillCategory());
								}
								for(SkillCostEntry professionSkillCost : skillCategoryCost.getSkillCosts()) {
									assignableCosts.add(professionSkillCost.getSkillCost());
									skillCategoryCost.getAssignableSkillCosts().add(professionSkillCost.getSkillCost());
								}
							}
							else {
								callbackImpl.getProfessionInstance().getAssignableSkillCosts()
										.remove(skillCategoryCost.getSkillCategory());
								skillCategoryCost.getAssignableSkillCosts().clear();
							}
							callbackImpl.saveItem();
						}
					}
			);
		}
	}

	private class ChildViewHolder {
		private SkillCostEntry skillCost;
		private TextView       nameView;
		private EditText       initialCostEdit;
		private EditText       additionalCostEdit;

		ChildViewHolder(TextView nameView, EditText initialCostEdit, EditText additionalCostEdit) {
			this.nameView = nameView;
			this.initialCostEdit = initialCostEdit;
			initInitialCostEdit();
			this.additionalCostEdit = additionalCostEdit;
			initAdditionalCostEdit();
		}

		private void initInitialCostEdit() {
			initialCostEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.length() > 0) {
						Short newValue = Short.valueOf(editable.toString());
						Short otherValue;
						if(additionalCostEdit.length() > 0) {
							otherValue = Short.valueOf(additionalCostEdit.getText().toString());
							if(otherValue < newValue) {
								initialCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
							}
							else {
								initialCostEdit.setError(null);
								additionalCostEdit.setError(null);
							}
						}
					}
				}
			});
			initialCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof ChildViewHolder && initialCostEdit == view) {
							if (initialCostEdit.length() > 0) {
								boolean changed = false;
								Short newCost = Short.valueOf(initialCostEdit.getText().toString());
								if(!newCost.equals(skillCost.getSkillCost().getFirstCost())) {
									skillCost.getSkillCost().setFirstCost(newCost);
									SkillCost cost = callbackImpl.getProfessionInstance().getSkillCosts()
											.get(skillCost.getSkill());
									if(cost == null) {
										cost = new SkillCost();
										callbackImpl.getProfessionInstance().getSkillCosts()
												.put(skillCost.getSkill(), cost);
									}
									cost.setFirstCost(newCost);
									changed = true;
								}
								if(changed) {
									callbackImpl.saveItem();
								}
							}
						}
					}
				}
			});
		}

		private void initAdditionalCostEdit() {
			additionalCostEdit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.length() > 0) {
						Short newValue = Short.valueOf(editable.toString());
						Short otherValue;
							if(initialCostEdit.length() > 0) {
								otherValue = Short.valueOf(initialCostEdit.getText().toString());
								if(newValue < otherValue) {
									additionalCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
								}
								else {
									additionalCostEdit.setError(null);
									initialCostEdit.setError(null);
								}
							}
						}
					}
			});
			additionalCostEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof ChildViewHolder && additionalCostEdit == view) {
							if (additionalCostEdit.length() > 0) {
								boolean changed = false;
								Short newCost = Short.valueOf(additionalCostEdit.getText().toString());
								if(!newCost.equals(skillCost.getSkillCost().getAdditionalCost())) {
									skillCost.getSkillCost().setAdditionalCost(newCost);
									SkillCost cost = callbackImpl.getProfessionInstance().getSkillCosts()
											.get(skillCost.getSkill());
									if(cost == null) {
										cost = new SkillCost();
										callbackImpl.getProfessionInstance().getSkillCosts()
												.put(skillCost.getSkill(), cost);
									}
									cost.setAdditionalCost(newCost);
									changed = true;
								}
								if(changed) {
									callbackImpl.saveItem();
								}
							}
						}
					}
				}
			});
		}
	}

	public interface ProfessionCostsCallbacks {
		/**
		 * Gets a profession instance to work with
		 * 
		 * @return a Profession instance
		 */
		Profession getProfessionInstance();

		/**
		 * Saves the current Profession instance
		 */
		void saveItem();
	}
}

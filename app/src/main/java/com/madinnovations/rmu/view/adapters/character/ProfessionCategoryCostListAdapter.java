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

		ProfessionSkillCategoryCost skillCategoryCost = (ProfessionSkillCategoryCost)getGroup(groupPosition);
		holder.skillCategoryCost = skillCategoryCost;
		SkillCostEntry skillCost = (SkillCostEntry)getChild(groupPosition, childPosition);
		holder.skillCostEntry = skillCost;
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
								additionalCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
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
							if(copyCostToItem()) {
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
							if(copyCostToItem()) {
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
								List<SkillCost> assignableCosts = callbackImpl.getProfessionInstance().getAssignableSkillCosts()
										.get(skillCategoryCost.getSkillCategory());
								if(assignableCosts == null) {
									assignableCosts = new ArrayList<>(skillCategoryCost.getSkillCosts().size());
									callbackImpl.getProfessionInstance().getAssignableSkillCosts()
											.put(skillCategoryCost.getSkillCategory(), assignableCosts);
								}
								for(int i = 0; i < skillCategoryCost.getSkillCosts().size(); i++) {
									SkillCostEntry skillCostEntry = skillCategoryCost.getSkillCosts().get(i);
									assignableCosts.add(i, skillCostEntry.getSkillCost());
									skillCategoryCost.getAssignableSkillCosts().add(i, skillCostEntry.getSkillCost());
									callbackImpl.getProfessionInstance().getSkillCosts().remove(skillCostEntry.getSkill());
								}
							}
							else {
								for(int i = 0; i < skillCategoryCost.getAssignableSkillCosts().size(); i++) {
									if(skillCategoryCost.getAssignableSkillCosts().get(i) != null &&
											skillCategoryCost.getSkillCosts().size() > i) {
										callbackImpl.getProfessionInstance().getSkillCosts().put(
												skillCategoryCost.getSkillCosts().get(i).getSkill(),
												skillCategoryCost.getAssignableSkillCosts().get(i));
									}
								}
								callbackImpl.getProfessionInstance().getAssignableSkillCosts()
										.remove(skillCategoryCost.getSkillCategory());
								skillCategoryCost.getAssignableSkillCosts().clear();
							}
							callbackImpl.saveItem();
						}
					}
			);
		}

		private boolean copyCostToItem() {
			boolean changed = false;
			Short firstCost = null;
			Short secondCost = null;
			SkillCost skillCost = callbackImpl.getProfessionInstance().getSkillCategoryCosts().get(
					skillCategoryCost.getSkillCategory());

			if(initialCostEdit.getText().length() > 0) {
				firstCost = Short.valueOf(initialCostEdit.getText().toString());
			}
			if(additionalCostEdit.getText().length() > 0) {
				secondCost = Short.valueOf(additionalCostEdit.getText().toString());
			}

			if(firstCost == null && secondCost == null && skillCost != null) {
				changed = true;
				skillCost = null;
				callbackImpl.getProfessionInstance().getSkillCategoryCosts().remove(skillCategoryCost.getSkillCategory());
			}
			else if(firstCost != null && secondCost != null) {
				if(firstCost >= secondCost) {
					additionalCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
				}
				else {
					if (skillCost == null) {
						skillCost = new SkillCost(firstCost, secondCost);
						callbackImpl.getProfessionInstance().getSkillCategoryCosts().put(
								skillCategoryCost.getSkillCategory(), skillCost);
						changed = true;
					} else if (!firstCost.equals(skillCost.getFirstCost()) || !secondCost.equals(skillCost.getAdditionalCost())) {
						skillCost.setFirstCost(firstCost);
						skillCost.setAdditionalCost(secondCost);
						changed = true;
					}
				}
			}
			else {
				if(skillCost == null) {
					skillCost = new SkillCost(firstCost, secondCost);
					changed = true;
				}
				else {
					if(firstCost == null && skillCost.getFirstCost() != null) {
						changed = true;
						skillCost.setFirstCost(null);
					}
					else if(firstCost != null && !firstCost.equals(skillCost.getFirstCost())) {
						changed = true;
						skillCost.setFirstCost(firstCost);
					}
					if(secondCost == null && skillCost.getAdditionalCost() != null) {
						changed =true;
						skillCost.setAdditionalCost(null);
					}
					else if(secondCost != null && !secondCost.equals(skillCost.getAdditionalCost())) {
						changed = true;
						skillCost.setAdditionalCost(secondCost);
					}
				}
			}

			if(changed) {
				skillCategoryCost.setSkillCategoryCost(skillCost);
			}
			return changed;
		}
	}

	private class ChildViewHolder {
		private ProfessionSkillCategoryCost skillCategoryCost;
		private SkillCostEntry skillCostEntry;
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
							if(copyCostToItem()) {
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
						if(((View)view.getParent()).getTag() instanceof ChildViewHolder && additionalCostEdit == view) {
							if(copyCostToItem()) {
								callbackImpl.saveItem();
							}
						}
					}
				}
			});
		}

		private boolean copyCostToItem() {
			boolean changed = false;
			Short firstCost = null;
			Short secondCost = null;
			SkillCost skillCost;
			int index = 0;
			if(skillCategoryCost.isAssignable()) {
				index = skillCategoryCost.getSkillCosts().indexOf(skillCostEntry);
				skillCost = callbackImpl.getProfessionInstance().getAssignableSkillCosts().get(
						skillCategoryCost.getSkillCategory()).get(index);
			}
			else {
				skillCost = callbackImpl.getProfessionInstance().getSkillCosts().get(
							skillCostEntry.getSkill());
			}

			if(initialCostEdit.getText().length() > 0) {
				firstCost = Short.valueOf(initialCostEdit.getText().toString());
			}
			if(additionalCostEdit.getText().length() > 0) {
				secondCost = Short.valueOf(additionalCostEdit.getText().toString());
			}

			if(firstCost == null && secondCost == null && skillCost != null) {
				changed = true;
				skillCost = null;
				if(skillCategoryCost.isAssignable()) {
					callbackImpl.getProfessionInstance().getAssignableSkillCosts().get(
							skillCategoryCost.getSkillCategory()).add(index, null);
				}
				else {
					callbackImpl.getProfessionInstance().getSkillCosts().remove(skillCostEntry.getSkill());
				}
			}
			else if(firstCost != null && secondCost != null) {
				if(firstCost >= secondCost) {
					additionalCostEdit.setError(context.getString(R.string.validation_initial_cost_gt_additional_cost));
				}
				else {
					if (skillCost == null) {
						skillCost = new SkillCost(firstCost, secondCost);
						if(skillCategoryCost.isAssignable()) {
							callbackImpl.getProfessionInstance().getAssignableSkillCosts().get(
									skillCategoryCost.getSkillCategory()).add(index, skillCost);
						}
						else {
							callbackImpl.getProfessionInstance().getSkillCosts().put(skillCostEntry.getSkill(), skillCost);
						}
						changed = true;
					} else if (!firstCost.equals(skillCost.getFirstCost()) || !secondCost.equals(skillCost.getAdditionalCost())) {
						skillCost.setFirstCost(firstCost);
						skillCost.setAdditionalCost(secondCost);
						changed = true;
					}
				}
			}
			else {
				if(skillCost == null) {
					skillCost = new SkillCost(firstCost, secondCost);
					changed = true;
				}
				else {
					if(firstCost == null && skillCost.getFirstCost() != null) {
						changed = true;
						skillCost.setFirstCost(null);
					}
					else if(firstCost != null && !firstCost.equals(skillCost.getFirstCost())) {
						changed = true;
						skillCost.setFirstCost(firstCost);
					}
					if(secondCost == null && skillCost.getAdditionalCost() != null) {
						changed =true;
						skillCost.setAdditionalCost(null);
					}
					else if(secondCost != null && !secondCost.equals(skillCost.getAdditionalCost())) {
						changed = true;
						skillCost.setAdditionalCost(secondCost);
					}
				}
			}

			if(changed) {
				if(skillCategoryCost.isAssignable()) {
					callbackImpl.getProfessionInstance().getAssignableSkillCosts().get(
							skillCategoryCost.getSkillCategory()).add(index, skillCost);
				}
				else {
					callbackImpl.getProfessionInstance().getSkillCosts().put(skillCostEntry.getSkill(), skillCost);
				}
				skillCostEntry.setSkillCost(skillCost);
			}
			return changed;
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

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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Profession;
import com.madinnovations.rmu.data.entities.character.ProfessionSkillCategoryCost;
import com.madinnovations.rmu.data.entities.character.SkillCostGroup;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.SkillCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	private ArrayAdapter<DevelopmentCostGroup> dpCostsSpinnerAdapter;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

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
		dpCostsSpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_row);
		dpCostsSpinnerAdapter.addAll(DevelopmentCostGroup.values());
	}

	@Override
	public Object getGroup(int groupPosition) {
		return listData.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listData.get(groupPosition).getSkillCostGroups().get(childPosition);
	}

	@Override
	public int getGroupCount() {
		return listData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int result = 0;

		ProfessionSkillCategoryCost skillCategoryCost = listData.get(groupPosition);
		if(skillCategoryCost.getSkillCostGroups() != null) {
			result = skillCategoryCost.getSkillCostGroups().size();
		}

		return result;
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
									(Spinner) rowView.findViewById(R.id.dp_costs_spinner),
									(CheckBox)rowView.findViewById(R.id.assignable_check_box));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (GroupViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(context, colors[groupPosition % colors.length]));
		ProfessionSkillCategoryCost skillCategoryCost = (ProfessionSkillCategoryCost)getGroup(groupPosition);
		holder.position = groupPosition;
		holder.skillCategoryCost = skillCategoryCost;
		holder.nameView.setText(skillCategoryCost.getSkillCategory().getName());
		if(skillCategoryCost.getCostGroup() == null) {
			holder.skillCostsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(DevelopmentCostGroup.NONE));
		}
		else {
			holder.skillCostsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(skillCategoryCost.getCostGroup()));
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
										 (Spinner) rowView.findViewById(R.id.child_cost_spinner));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ChildViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(context, colors[(groupPosition + childPosition + 1) %
				colors.length]));
		holder.groupPosition = groupPosition;
		holder.position = childPosition;
		ProfessionSkillCategoryCost skillCategoryCost = (ProfessionSkillCategoryCost)getGroup(groupPosition);
		holder.skillCategoryCost = skillCategoryCost;
		SkillCostGroup skillCostGroup = (SkillCostGroup)getChild(groupPosition, childPosition);
		holder.skillCostGroup = skillCostGroup;
		if(skillCategoryCost.isAssignable()) {
			holder.nameView.setText(String.format(context.getString(R.string.assignable_skill_name), childPosition + 1));
		 	if(skillCategoryCost.getAssignableCostGroups().size() > childPosition) {
				DevelopmentCostGroup dpCostGroup = skillCategoryCost.getAssignableCostGroups().get(childPosition);
				if (dpCostGroup == null) {
					holder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(DevelopmentCostGroup.NONE));
				}
				else {
					holder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(dpCostGroup));
				}
			}
			else {
				holder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(DevelopmentCostGroup.NONE));
			}
		}
		else {
			holder.nameView.setText(skillCostGroup.getSkill().getName());
			if(skillCostGroup.getCostGroup() == null) {
				holder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(DevelopmentCostGroup.NONE));
			}
			else {
				holder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(skillCostGroup.getCostGroup()));
			}
		}

		return rowView;
	}

	public void clear() {
		listData.clear();
	}

	public void add(ProfessionSkillCategoryCost professionSkillCategoryCost) {
		listData.add(professionSkillCategoryCost);
	}

	public void addAll(Collection<ProfessionSkillCategoryCost> professionSkillCategoryCosts) {
		listData.addAll(professionSkillCategoryCosts);
	}

	private ChildViewHolder findChildViewHolder(View startView, int groupPosition, int childPosition) {
		View parentView;
		ChildViewHolder childViewHolder = null;

		do {
			parentView = (View) startView.getParent();
		} while (parentView != null && !(parentView instanceof ViewGroup));

		if(parentView != null && parentView instanceof ListView) {
			ListView listView = (ListView)parentView;
			for(int i = 0; i < listView.getChildCount(); i++) {
				View child = listView.getChildAt(i);
				Object tag = child.getTag();
				if(tag instanceof ChildViewHolder) {
					ChildViewHolder tempChildViewHolder = (ChildViewHolder)tag;
					if(tempChildViewHolder.groupPosition == groupPosition && tempChildViewHolder.position == childPosition) {
						childViewHolder = tempChildViewHolder;
						break;
					}
				}
			}
		}

		return childViewHolder;
	}

	private class GroupViewHolder {
		private int position;
		private ProfessionSkillCategoryCost skillCategoryCost;
		private TextView nameView;
		private Spinner  skillCostsSpinner;
		private CheckBox assignableCheckBox;

		GroupViewHolder(TextView nameView, Spinner skillCostsSpinner, CheckBox assignableCheckBox) {
			this.nameView = nameView;
			initNameView();
			this.skillCostsSpinner = skillCostsSpinner;
			initSkillCostsSpinner();
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

		private void initSkillCostsSpinner() {
			skillCostsSpinner.setAdapter(dpCostsSpinnerAdapter);
			skillCostsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(copyCostToItem()) {
						callbackImpl.saveItem();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					if(copyCostToItem()) {
						callbackImpl.saveItem();
					}
				}
			});
			skillCostsSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof GroupViewHolder && skillCostsSpinner == view) {
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
								List<DevelopmentCostGroup> assignableCosts;
								boolean isNew = false;
								if (skillCategoryCost.getAssignableCostGroups() == null ||
										skillCategoryCost.getAssignableCostGroups().isEmpty()) {
									assignableCosts = callbackImpl.getProfessionInstance()
											.getAssignableSkillCostsMap()
											.get(skillCategoryCost.getSkillCategory());
								}
								else {
									assignableCosts = skillCategoryCost.getAssignableCostGroups();
								}
								if(assignableCosts == null) {
									assignableCosts = new ArrayList<>(skillCategoryCost.getSkillCostGroups().size());
									callbackImpl.getProfessionInstance().getAssignableSkillCostsMap()
											.put(skillCategoryCost.getSkillCategory(), assignableCosts);
									isNew = true;
								}
								for(int i = 0; i < skillCategoryCost.getSkillCostGroups().size(); i++) {
									SkillCostGroup skillCostGroup = skillCategoryCost.getSkillCostGroups().get(i);
									if(isNew) {
										assignableCosts.add(i, skillCostGroup.getCostGroup());
										skillCategoryCost.getAssignableCostGroups().add(i, skillCostGroup.getCostGroup());
									}
									callbackImpl.getProfessionInstance().getSkillCosts().remove(skillCostGroup.getSkill());
								}
								for(int i = 0; i < skillCategoryCost.getAssignableCostGroups().size(); i++) {
									DevelopmentCostGroup dpCostGroup = skillCategoryCost.getAssignableCostGroups().get(i);
									updateSkillRow(position, i, String.format(context.getString(R.string.assignable_skill_name), i + 1), dpCostGroup);
								}
							}
							else {
								for(int i = 0; i < skillCategoryCost.getSkillCostGroups().size(); i++) {
								SkillCostGroup skillCostGroup = skillCategoryCost.getSkillCostGroups().get(i);
									if(skillCostGroup.getCostGroup() != null) {
										callbackImpl.getProfessionInstance().getSkillCosts().put(
												skillCostGroup.getSkill(), skillCostGroup.getCostGroup());
										updateSkillRow(position, i, skillCostGroup.getSkill().getName(),
													   skillCostGroup.getCostGroup());
									}
								}
								callbackImpl.getProfessionInstance().getAssignableSkillCostsMap()
										.remove(skillCategoryCost.getSkillCategory());
							}
							callbackImpl.saveItem();
						}
					}
			);
		}

		private void updateSkillRow(int groupPosition, int childPosition, String name, DevelopmentCostGroup dpCostGroup) {
			ChildViewHolder childViewHolder = ProfessionCategoryCostListAdapter.this.findChildViewHolder(
					nameView, groupPosition, childPosition);
			if(childViewHolder != null) {
				childViewHolder.nameView.setText(name);
				if(dpCostGroup == null) {
					childViewHolder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(DevelopmentCostGroup.NONE));
				}
				else {
					childViewHolder.costsSpinner.setSelection(dpCostsSpinnerAdapter.getPosition(dpCostGroup));
				}
				childViewHolder.nameView.invalidate();
				childViewHolder.costsSpinner.invalidate();
			}
		}

		private boolean copyCostToItem() {
			boolean changed = false;
			Map<SkillCategory, DevelopmentCostGroup> categoryCostsMap =
					callbackImpl.getProfessionInstance().getSkillCategoryCosts();
			DevelopmentCostGroup oldCostGroup = categoryCostsMap.get(skillCategoryCost.getSkillCategory());
			DevelopmentCostGroup newCostGroup = null;
			if(skillCostsSpinner.getSelectedItem() != null) {
				newCostGroup = (DevelopmentCostGroup) skillCostsSpinner.getSelectedItem();
			}
			if((newCostGroup == null || newCostGroup == DevelopmentCostGroup.NONE) &&
					!(oldCostGroup == null || oldCostGroup == DevelopmentCostGroup.NONE)) {
				categoryCostsMap.put(skillCategoryCost.getSkillCategory(), null);
				changed = true;
			}
			else if (newCostGroup != null && !newCostGroup.equals(oldCostGroup)) {
				categoryCostsMap.put(skillCategoryCost.getSkillCategory(), newCostGroup);
				changed = true;
			}

			if(changed) {
				skillCategoryCost.setCostGroup(newCostGroup);
			}
			return changed;
		}
	}

	private class ChildViewHolder {
		private ProfessionSkillCategoryCost skillCategoryCost;
		private int                         groupPosition;
		private int                         position;
		private SkillCostGroup              skillCostGroup;
		private TextView                    nameView;
		private Spinner                     costsSpinner;

		ChildViewHolder(TextView nameView, Spinner costsSpinner) {
			this.nameView = nameView;
			this.costsSpinner = costsSpinner;
			initSpinner();
		}

		private void initSpinner() {
			costsSpinner.setAdapter(dpCostsSpinnerAdapter);
			costsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					if(copyCostToItem()) {
						callbackImpl.saveItem();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					if(copyCostToItem()) {
						callbackImpl.saveItem();
					}
				}
			});
			costsSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof ChildViewHolder && costsSpinner == view) {
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
			DevelopmentCostGroup oldCostGroup = null;
			DevelopmentCostGroup newCostGroup = null;
			Map<SkillCategory, List<DevelopmentCostGroup>> costGroupsMap = callbackImpl.getProfessionInstance()
					.getAssignableSkillCostsMap();
			List<DevelopmentCostGroup> skillCosts = costGroupsMap.get(skillCategoryCost.getSkillCategory());

			if(skillCategoryCost.isAssignable()) {
				if(skillCosts == null) {
					skillCosts = new ArrayList<>();
					costGroupsMap.put(skillCategoryCost.getSkillCategory(), skillCosts);
				}
				if(position < skillCosts.size()) {
					oldCostGroup = skillCosts.get(position);
				}
			}
			else {
				oldCostGroup = callbackImpl.getProfessionInstance().getSkillCosts().get(skillCostGroup.getSkill());
			}

			if(costsSpinner.getSelectedItem() != null) {
				newCostGroup = (DevelopmentCostGroup) costsSpinner.getSelectedItem();
			}
			if((newCostGroup == null || newCostGroup == DevelopmentCostGroup.NONE) &&
					!(oldCostGroup == null || oldCostGroup == DevelopmentCostGroup.NONE)) {
				oldCostGroup = null;
				changed = true;
			}
			else if (newCostGroup != null && !newCostGroup.equals(oldCostGroup)) {
				oldCostGroup = newCostGroup;
				changed = true;
			}

			if(changed) {
				if(skillCategoryCost.isAssignable()) {
					if(skillCosts.size() > position) {
						skillCosts.remove(position);
						skillCosts.add(position, oldCostGroup);
					}
					else {
						while(skillCosts.size() < position) {
							skillCosts.add(DevelopmentCostGroup.NONE);
						}
						skillCosts.add(oldCostGroup);
					}
					if(skillCosts.size() <= skillCategoryCost.getAssignableCostGroups().size()) {
						Collections.copy(skillCategoryCost.getAssignableCostGroups(), skillCosts);
					}
				}
				else {
					callbackImpl.getProfessionInstance().getSkillCosts().put(skillCostGroup.getSkill(), oldCostGroup);
				}
				if(oldCostGroup == null) {
					Log.d("RMU", "oldCostGroup == null");
				}
				skillCostGroup.setCostGroup(oldCostGroup);
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

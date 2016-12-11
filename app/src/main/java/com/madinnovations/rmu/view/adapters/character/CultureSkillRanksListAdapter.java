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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Culture;
import com.madinnovations.rmu.data.entities.character.CultureSkillCategoryRanks;
import com.madinnovations.rmu.data.entities.character.CultureSkillRank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link CultureSkillCategoryRanks} information
 */
public class CultureSkillRanksListAdapter extends BaseExpandableListAdapter {
	private static final int GROUP_LAYOUT_RESOURCE_ID = R.layout.list_culture_skill_ranks_group_row;
	private static final int CHILD_LAYOUT_RESOURCE_ID = R.layout.list_culture_skill_ranks_child_row;
	private ExpandableListView listView;
	private LayoutInflater layoutInflater;
	private List<CultureSkillCategoryRanks> listData = new ArrayList<>();
	private CultureRanksCallbacks callbackImpl;
	private Context context;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	public void clear() {
		listData.clear();
	}

	@SuppressWarnings("unchecked")
	public void add(CultureSkillCategoryRanks professionSkillCategoryCost) {
		listData.add(professionSkillCategoryCost);
		Collections.sort(listData);
	}

	@SuppressWarnings("unchecked")
	public void addAll(Collection<CultureSkillCategoryRanks> professionSkillCategoryCosts) {
		listData.addAll(professionSkillCategoryCosts);
		Collections.sort(listData);
	}

	/**
	 * Creates a new ProfessionCategoryCostListAdapter instance.
	 *
	 * @param context  a {@link Context} instance to use to get a LayoutInflater
	 * @param callbackImpl  an implementation of the {@link CultureRanksCallbacks} interface
	 * @param listView  the {@link ExpandableListView} instance this adapter will be attached to
	 */
	@Inject
	public CultureSkillRanksListAdapter(@NonNull Context context,
										@NonNull CultureRanksCallbacks callbackImpl,
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
		return listData.get(groupPosition).getSkillRanksList().get(childPosition);
	}

	@Override
	public int getGroupCount() {
		return listData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		CultureSkillCategoryRanks skillCategoryCost = listData.get(groupPosition);
		if(skillCategoryCost.getSkillRanksList() == null) {
			return 0;
		}
		return skillCategoryCost.getSkillRanksList().size();
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
			holder = new GroupViewHolder((TextView) rowView.findViewById(R.id.name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (GroupViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(context, colors[groupPosition % colors.length]));
		CultureSkillCategoryRanks skillCategoryRank = (CultureSkillCategoryRanks)getGroup(groupPosition);
		holder.skillCategoryRanks = skillCategoryRank;
		holder.nameView.setText(skillCategoryRank.getSkillCategory().getName());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View rowView;
		ChildViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(CHILD_LAYOUT_RESOURCE_ID, parent, false);
			holder = new ChildViewHolder((TextView) rowView.findViewById(R.id.name_view),
										 (EditText)rowView.findViewById(R.id.ranks_edit));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ChildViewHolder) convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(context, colors[(groupPosition + childPosition) % colors.length]));
		CultureSkillRank skillRank = (CultureSkillRank)getChild(groupPosition, childPosition);
		holder.skillRank = skillRank;
		holder.nameView.setText(skillRank.getSkill().getName());
		if(skillRank.getRanks() == null) {
			holder.ranksEdit.setText(null);
		}
		else {
			holder.ranksEdit.setText(String.valueOf(skillRank.getRanks()));
		}
		return rowView;
	}

	private class GroupViewHolder {
		private CultureSkillCategoryRanks skillCategoryRanks;
		private TextView                  nameView;

		GroupViewHolder(TextView nameView) {
			this.nameView = nameView;
			initNameView();
		}

		private void initNameView() {
			nameView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = listData.indexOf(skillCategoryRanks);
					if(listView.isGroupExpanded(position)) {
						listView.collapseGroup(position);
					}
					else {
						listView.expandGroup(position);
					}
				}
			});
		}
	}

	private class ChildViewHolder {
		private CultureSkillRank skillRank;
		private TextView         nameView;
		private EditText         ranksEdit;

		public ChildViewHolder(TextView nameView, EditText ranksEdit) {
			this.nameView = nameView;
			this.ranksEdit = ranksEdit;
			initRanksEdit();
		}

		private void initRanksEdit() {
			ranksEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(!hasFocus) {
						if(((View)view.getParent()).getTag() instanceof CultureSkillRanksListAdapter.ChildViewHolder
								&& ranksEdit == view) {
							if (ranksEdit.length() > 0) {
								boolean changed = false;
								Short newRanks = Short.valueOf(ranksEdit.getText().toString());
								if(!newRanks.equals(skillRank.getRanks())) {
									skillRank.setRanks(newRanks);
									Short rank = callbackImpl.getCultureInstance().getSkillRanks()
											.get(skillRank.getSkill());
									if(rank == null) {
										callbackImpl.getCultureInstance().getSkillRanks()
												.put(skillRank.getSkill(), newRanks);
									}
									changed = true;
								}
								if(changed) {
									callbackImpl.saveItem();
								}
							}
							else {
								if(skillRank.getRanks() != null) {
									skillRank.setRanks(null);
									callbackImpl.getCultureInstance().getSkillRanks().remove(skillRank.getSkill());
									callbackImpl.saveItem();
								}
							}
						}
					}
				}
			});
		}
	}

	public interface CultureRanksCallbacks {
		/**
		 * Gets a Culture instance to work with
		 * 
		 * @return a Culture instance
		 */
		Culture getCultureInstance();

		/**
		 * Saves the current Culture instance
		 */
		void saveItem();
	}
}

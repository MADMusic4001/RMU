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
package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentCategory;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 9/18/2016.
 */
public class ExpandableTalentsListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater   layoutInflater;
	private TalentCategory[] groupData;
	private Talent[][]       childData;

	/**
	 * Creates a new ExpandableTalentsListAdapter instance.
	 *
	 * @param context  an android context to use when loading view layouts, strings, etc.
	 * @param groupData  the group item data
	 * @param childData  the child item data
	 */
	public ExpandableTalentsListAdapter(Context context, TalentCategory[] groupData, Talent[][] childData) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groupData = groupData;
		this.childData = childData;
	}

	/**
	 * Gets the packed position of the given Talent instance.
	 *
	 * @param talent  the Talent instance whose position is needed
	 * @return the packed position of the given Talent instance or -1 if not found.
	 */
	public long getPosition(Talent talent) {
		if(talent == null) {
			return -1;
		}

		for(int i = 0; i < childData.length; i++) {
			for(int j = 0; j < childData[i].length; j++) {
				if(talent.equals(childData[i][j])) {
					return ExpandableListView.getPackedPositionForChild(i, j);
				}
			}
		}

		return -1;
	}

	/**
	 * Gets the Talent at the given position.
	 *
	 * @param position  the packed position value of the desired Talent instance
	 * @return the Talent instance at the given packed position.
	 */
	public Talent getItem(long position) {
		return (Talent)getChild(ExpandableListView.getPackedPositionGroup(position),
								ExpandableListView.getPackedPositionChild(position));
	}

	@Override
	public int getGroupCount() {
		int result = 0;
		if(groupData != null) {
			result = groupData.length;
		}

		return result;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int result = 0;

		if(childData != null && childData.length > groupPosition) {
			result = childData[groupPosition].length;
		}

		return result;
	}

	@Override
	public Object getGroup(int groupPosition) {
		TalentCategory result = null;

		if(groupData != null && groupPosition >= 0 && groupData.length > groupPosition) {
			result = groupData[groupPosition];
		}

		return result;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Talent result = null;

		if(childData != null && groupPosition >= 0 && childData.length > groupPosition &&
				childPosition >= 0 && childData[groupPosition].length > childPosition) {
			result = childData[groupPosition][childPosition];
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
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(R.layout.expandable_list_group_row, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		TalentCategory category = (TalentCategory)getGroup(groupPosition);
		holder.textView.setText(category.getName());
		return rowView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(R.layout.expandable_list_child_row, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		Talent talent = (Talent)getChild(groupPosition, childPosition);
		holder.textView.setText(talent.getName());
		return rowView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	// Setters
	public void setGroupData(TalentCategory[] groupData) {
		this.groupData = groupData;
	}
	public void setChildData(Talent[][] childData) {
		this.childData = childData;
	}

	private class ViewHolder {
		TextView textView;

		public ViewHolder(TextView textView) {
			this.textView = textView;
		}
	}
}

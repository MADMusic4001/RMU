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
package com.madinnovations.rmu.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;

import java.util.List;
import java.util.Map;

/**
 * Populates an expandable list view with menu items
 */
public class MainMenuListAdapter extends BaseExpandableListAdapter {
	private static final String LOG_TAG = "MainMenuListAdapter";
	private LayoutInflater layoutInflater;
	private ExpandableListView listView;
	private List<String> groupHeaders;
	private Map<String, List<String>> groupItems;
	private Drawable defaultBackground = null;

	/**
	 * Creates a new MainMenuListAdapter instance
	 *
	 * @param context  an android context
	 * @param groupHeaders  a list of group header strings
	 * @param groupItems  a map with the group header strings as the keys and a list of group item strings as the values
	 */
	public MainMenuListAdapter(Context context, List<String> groupHeaders, Map<String, List<String>> groupItems,
							   ExpandableListView listView) {
		this.groupHeaders = groupHeaders;
		this.groupItems = groupItems;
		this.listView = listView;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount() {
		return groupHeaders.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupItems.get(groupHeaders.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupHeaders.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int itemPosition) {
		return groupItems.get(groupHeaders.get(groupPosition)).get(itemPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int itemPosition) {
		return itemPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.main_menu_group_row, parent, false);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.group_header);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.main_menu_item_row, parent, false);
		}

		if(defaultBackground == null) {
			defaultBackground = convertView.getBackground();
		}
		int index = listView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
		Log.d(LOG_TAG, "Item " + index + " itemChecked = " + listView.isItemChecked(index));
		if(listView.isItemChecked(index)) {
			convertView.setBackgroundColor(Color.BLUE);
		}
		else {
//			if(defaultBackground != null) {
//				convertView.setBackground(defaultBackground);
//			}
//			else {
				convertView.setBackgroundColor(Color.DKGRAY);
//			}
		}
		TextView txtListChild = (TextView) convertView.findViewById(R.id.expandable_list_item);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}
}

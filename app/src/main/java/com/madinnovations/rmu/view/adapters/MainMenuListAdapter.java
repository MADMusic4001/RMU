package com.madinnovations.rmu.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;

import java.util.List;
import java.util.Map;

/**
 * Populates an expandable list view with menu items
 */
public class MainMenuListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<String> groupHeaders;
	private Map<String, List<String>> groupItems;

	/**
	 * Creates a new MainMenuListAdapter instance
	 *
	 * @param context  an android context
	 * @param groupHeaders  a list of group header strings
	 * @param groupItems  a map with the group header strings as the keys and a list of group item strings as the values
	 */
	public MainMenuListAdapter(Context context, List<String> groupHeaders, Map<String, List<String>> groupItems) {
		this.context = context;
		this.groupHeaders = groupHeaders;
		this.groupItems = groupItems;
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
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
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
			LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.expandable_list_item);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}
}

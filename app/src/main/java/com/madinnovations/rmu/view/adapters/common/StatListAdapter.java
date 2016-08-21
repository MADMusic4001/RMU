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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Stat;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link Stat} information
 */
public class StatListAdapter extends ArrayAdapter<Stat> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_1_to_1_to_6_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new StatListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public StatListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.row_field1),
									(TextView) rowView.findViewById(R.id.row_field2),
									(TextView) rowView.findViewById(R.id.row_field3));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		Stat stat = getItem(position);
		holder.abbreviationView.setText(stat.getAbbreviation());
		holder.nameView.setText(stat.getName());
//		Log.d("RMU", "width = " + holder.descriptionView.getWidth());
//		Log.d("RMU", "measuredWidth = " + holder.descriptionView.getMeasuredWidth());
//		Log.d("RMU", "maxWidth = " + holder.descriptionView.getMaxWidth());
//		CharSequence displayText = TextUtils.ellipsize(stat.getDescription(),holder.descriptionView.getPaint(),
//				holder.descriptionView.getWidth(), TextUtils.TruncateAt.END);
//		holder.descriptionView.setText(displayText);
		holder.descriptionView.setText(stat.getDescription());
		return rowView;
	}

	private class ViewHolder {
		private TextView abbreviationView;
		private TextView nameView;
		private TextView descriptionView;

		ViewHolder(TextView abbreviationView, TextView nameView, TextView descriptionView) {
			this.abbreviationView = abbreviationView;
			this.nameView = nameView;
			this.descriptionView = descriptionView;
		}
	}
}

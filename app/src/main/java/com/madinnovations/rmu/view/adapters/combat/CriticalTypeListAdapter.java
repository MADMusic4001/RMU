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
package com.madinnovations.rmu.view.adapters.combat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.CriticalType;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link CriticalType} information
 */
public class CriticalTypeListAdapter extends ArrayAdapter<CriticalType> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_1_to_5_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new CriticalTypeListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public CriticalTypeListAdapter(Context context) {
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
					(TextView) rowView.findViewById(R.id.row_field2));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CriticalType item = getItem(position);
		holder.codeView.setText(String.valueOf(item.getCode()));
		holder.nameView.setText(item.getName());
		return rowView;
	}

	private class ViewHolder {
		private TextView codeView;
		private TextView nameView;

		ViewHolder(TextView codeView, TextView nameView) {
			this.codeView = codeView;
			this.nameView = nameView;
		}
	}
}

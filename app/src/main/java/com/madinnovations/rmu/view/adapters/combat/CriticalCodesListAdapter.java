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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;

import javax.inject.Inject;

/**
 * Populates a Spinner with {@link CriticalCode} information
 */
public class CriticalCodesListAdapter extends ArrayAdapter<CriticalCode> {
	@SuppressWarnings("unused")
	private static final String TAG                = "CritCodesListAdapter";
	private static final int    LAYOUT_RESOURCE_ID = R.layout.single_field_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new CriticalCodesListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@SuppressWarnings("WeakerAccess")
	@Inject
	public CriticalCodesListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
		return getView(position, convertView, parent);
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CriticalCode criticalCode = getItem(position);
		if(criticalCode != null) {
			holder.codeView.setText(criticalCode.getCode());
		}
		return rowView;
	}

	private class ViewHolder {
		private TextView codeView;

		ViewHolder(TextView codeView) {
			this.codeView = codeView;
		}
	}
}

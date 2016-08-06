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
import com.madinnovations.rmu.data.entities.common.SkillCost;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link SkillCost} information
 */
public class SkillCostListAdapter extends ArrayAdapter<SkillCost> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.cost_cost_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new SkillCostListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public SkillCostListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.initial_cost_view),
									(TextView) rowView.findViewById(R.id.additional_cost_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		SkillCost skillCost = getItem(position);
 		holder.initialCostView.setText(String.valueOf(skillCost.getFirstCost()));
		holder.additionalCostView.setText(String.valueOf(skillCost.getAdditionalCost()));
		return rowView;
	}

	private class ViewHolder {
		private TextView initialCostView;
		private TextView additionalCostView;

		ViewHolder(TextView initialCostView, TextView additionalCostView) {
			this.initialCostView = initialCostView;
			this.additionalCostView = additionalCostView;
		}
	}
}

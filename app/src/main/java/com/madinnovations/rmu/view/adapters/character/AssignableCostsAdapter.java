/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.adapters.character;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.SkillCostGroup;

import java.util.Collection;

/**
 * Adapter for allowing user to assign costs to skills.
 */
public class AssignableCostsAdapter extends ArrayAdapter<SkillCostGroup> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_edit_skill_costs_row;
	private LayoutInflater layoutInflater;
	private ViewHolder[] viewHolders = new ViewHolder[0];

	/**
	 * Creates a new AssignableCostsAdapter instance
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public AssignableCostsAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void clear() {
		super.clear();
		viewHolders = new ViewHolder[0];
	}

	@Override
	public void addAll(@NonNull Collection<? extends SkillCostGroup> collection) {
		ViewHolder[] views = new ViewHolder[viewHolders.length + collection.size()];
		if(viewHolders.length > 0) {
			System.arraycopy(viewHolders, 0, views, 0, viewHolders.length);
		}
		viewHolders = views;
		super.addAll(collection);
	}

	@Override
	public void addAll(SkillCostGroup... items) {
		ViewHolder[] views = new ViewHolder[viewHolders.length + items.length];
		if(viewHolders.length > 0) {
			System.arraycopy(viewHolders, 0, views, 0, viewHolders.length);
		}
		viewHolders = views;
		super.addAll(items);
	}

	@Override
	public void add(SkillCostGroup object) {
		ViewHolder[] views = new ViewHolder[viewHolders.length + 1];
		if(viewHolders.length > 0) {
			System.arraycopy(viewHolders, 0, views, 0, viewHolders.length);
		}
		viewHolders = views;
		super.add(object);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView)rowView.findViewById(R.id.name_view),
									(LinearLayout)rowView.findViewById(R.id.costs_group),
									(TextView)rowView.findViewById(R.id.initial_cost_view),
									(TextView)rowView.findViewById(R.id.additional_cost_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		viewHolders[position] = holder;
		SkillCostGroup skillCostGroup = getItem(position);
		if(skillCostGroup != null && skillCostGroup.getSkill() != null) {
			holder.nameView.setText(skillCostGroup.getSkill().getName());
			holder.initialCostView.setText(String.valueOf(skillCostGroup.getCostGroup().getFirstCost()));
			holder.additionalCostView.setText(String.valueOf(skillCostGroup.getCostGroup().getAdditionalCost()));
		}

		return rowView;
	}

	private class ViewHolder {
		TextView nameView;
		LinearLayout costsGroup;
		TextView initialCostView;
		TextView additionalCostView;

		public ViewHolder(final TextView nameView, final LinearLayout costsGroup, final TextView initialCostView,
						  TextView additionalCostView) {
			this.nameView = nameView;
			this.costsGroup = costsGroup;
			this.initialCostView = initialCostView;
			this.additionalCostView = additionalCostView;
		}
	}
}

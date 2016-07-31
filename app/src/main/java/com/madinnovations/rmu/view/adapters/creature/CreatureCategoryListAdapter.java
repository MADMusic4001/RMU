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
package com.madinnovations.rmu.view.adapters.creature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.creature.CreatureCategory;

import javax.inject.Inject;

/**
 * Populates a ListView with {@link CreatureCategory} information
 */
public class CreatureCategoryListAdapter extends ArrayAdapter<CreatureCategory> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.name_description_row;
	private LayoutInflater layoutInflater;

	/**
	 * Creates a new CreatureCategoryListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	@Inject
	public CreatureCategoryListAdapter(Context context) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.name_view),
									(TextView) rowView.findViewById(R.id.description_view));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		CreatureCategory creatureCategory = getItem(position);
		holder.nameView.setText(creatureCategory.getName());
		holder.descriptionView.setText(creatureCategory.getDescription());
		return rowView;
	}

	private class ViewHolder {
		private TextView nameView;
		private TextView descriptionView;

		ViewHolder(TextView nameView, TextView descriptionView) {
			this.nameView = nameView;
			this.descriptionView = descriptionView;
		}
	}
}

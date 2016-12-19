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
package com.madinnovations.rmu.view.adapters.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.SkillRanks;

/**
 * Populates a ListView with {@link SkillRanks} information
 */
public class SkillRanksAdapter extends ArrayAdapter<SkillRanks> {
	private static final String TAG = "SkillRanksAdapter";
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_skill_ranks_row;
	private LayoutInflater layoutInflater;
	private SkillRanksAdapterCallbacks callbacks;
	private int[] colors = new int[]{
			R.color.list_even_row_background,
			R.color.list_odd_row_background};

	/**
	 * Creates a new SkillRanksAdapter instance.
	 *
	 * @param context  the android context to be used by this adapter
	 * @param callbacks  an instance that implements the SkillRanksAdapterCallbacks interface
	 */
	public SkillRanksAdapter(Context context, @NonNull SkillRanksAdapterCallbacks callbacks) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView)rowView.findViewById(R.id.name_view),
									(TextView)rowView.findViewById(R.id.ranks_view),
									(ImageButton) rowView.findViewById(R.id.increment_button),
									(ImageButton)rowView.findViewById(R.id.decrement_button),
									(EditText)rowView.findViewById(R.id.culture_ranks_view));
			rowView.setTag(holder);
			rowView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Log.d(TAG, "onLongClick: ");
					return false;
				}
			});
		}
		else {
			rowView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		rowView.setBackgroundColor(ContextCompat.getColor(getContext(), colors[position % colors.length]));
		SkillRanks skillRanks = getItem(position);
		holder.skillRanks = skillRanks;
		if(skillRanks != null) {
			DevelopmentCostGroup costGroup = callbacks.getSkillCost(skillRanks);
			if(costGroup != null) {
				String builder = skillRanks.toString() + " (" + costGroup.getFirstCost() + "/" + costGroup.getAdditionalCost()
						+ ")";
				holder.nameView.setText(builder);
			}
			holder.ranksView.setText(String.valueOf(callbacks.getRanks(skillRanks)));
			short cultureRanks = callbacks.getCultureRanks(skillRanks);
			if(cultureRanks > 0) {
				holder.cultureRanksView.setText(String.valueOf(cultureRanks));
			}
			else {
				holder.cultureRanksView.setText(null);
			}
		}

		return rowView;
	}

	public class ViewHolder {
		private SkillRanks skillRanks;
		private TextView nameView;
		private TextView ranksView;
		private ImageButton incrementButton;
		private ImageButton decrementButton;
		private EditText cultureRanksView;

		public ViewHolder(TextView nameView, TextView ranksView, ImageButton incrementButton, ImageButton decrementButton,
						  EditText cultureRanksView) {
			this.nameView = nameView;
			this.ranksView = ranksView;
			this.incrementButton = incrementButton;
			this.decrementButton = decrementButton;
			this.cultureRanksView = cultureRanksView;

			initIncrementButton();
			initDecrementButton();
		}

		private void initIncrementButton() {
			incrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					short ranks = callbacks.purchaseRank(skillRanks);
					ranksView.setText(String.valueOf(ranks));
				}
			});
		}

		private void initDecrementButton() {
			decrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					short ranks = callbacks.sellRank(skillRanks);
					ranksView.setText(String.valueOf(ranks));
				}
			});
		}

		// Getters
		public SkillRanks getSkillRanks() {
			return skillRanks;
		}
	}

	public interface SkillRanksAdapterCallbacks {
		short purchaseRank(SkillRanks skillRanks);
		short sellRank(SkillRanks skillRanks);
		DevelopmentCostGroup getSkillCost(SkillRanks skillRanks);
		short getRanks(SkillRanks skillRanks);
		short getCultureRanks(SkillRanks skillRanks);
	}
}

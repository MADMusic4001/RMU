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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillRanks;
import com.madinnovations.rmu.data.entities.common.Specialization;

/**
 * Populates a ListView with {@link SkillRanks} information
 */
public class SkillRanksAdapter extends ArrayAdapter<SkillRanks> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_skill_ranks_row;
	private LayoutInflater layoutInflater;
	private SkillRanksAdapterCallbacks callbacks;

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
														   (ImageButton)rowView.findViewById(R.id.decrement_button));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder)convertView.getTag();
		}

		SkillRanks skillRanks = getItem(position);
		holder.skillRanks = skillRanks;
		if(skillRanks != null) {
			holder.nameView.setText(skillRanks.toString());
			holder.ranksView.setText(String.valueOf(skillRanks.getStartingRanks() + skillRanks.getEndingRanks()));
		}

		return rowView;
	}

	private class ViewHolder {
		private SkillRanks skillRanks;
		private TextView nameView;
		private TextView ranksView;
		private ImageButton incrementButton;
		private ImageButton decrementButton;

		public ViewHolder(TextView nameView, TextView ranksView, ImageButton incrementButton, ImageButton decrementButton) {
			this.nameView = nameView;
			this.ranksView = ranksView;
			this.incrementButton = incrementButton;
			initIncrementButton();
			this.decrementButton = decrementButton;
			initDecrementButton();
		}

		private void initIncrementButton() {
			incrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(skillRanks.getEndingRanks() < 2 && callbacks.purchaseRank(
							skillRanks.getSkill(), skillRanks.getSpecialization(), skillRanks.getEndingRanks())) {
						skillRanks.setEndingRanks((short)(skillRanks.getEndingRanks() + 1));
						ranksView.setText(String.valueOf(skillRanks.getStartingRanks() + skillRanks.getEndingRanks()));
					}
				}
			});
		}

		private void initDecrementButton() {
			decrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(skillRanks.getEndingRanks() > 0 && callbacks.sellRank(
							skillRanks.getSkill(), skillRanks.getSpecialization(), skillRanks.getEndingRanks())) {
						skillRanks.setEndingRanks((short)(skillRanks.getEndingRanks() - 1));
						ranksView.setText(String.valueOf(skillRanks.getStartingRanks() + skillRanks.getEndingRanks()));
					}
				}
			});
		}
	}

	public interface SkillRanksAdapterCallbacks {
		boolean purchaseRank(Skill skill, Specialization specialization, short purchasedThisLevel);
		boolean sellRank(Skill skill, Specialization specialization, short purchasedThisLevel);
	}
}

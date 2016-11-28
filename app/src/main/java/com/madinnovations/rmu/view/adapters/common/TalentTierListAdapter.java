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
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentTier;

/**
 * Populates a ListView with {@link TalentTier} information
 */
public class TalentTierListAdapter extends ArrayAdapter<TalentTier> {
	private static final int LAYOUT_RESOURCE_ID = R.layout.list_talent_tiers_row;
	private ListView listView = null;
	private LayoutInflater              layoutInflater;
	private TalentTiersAdapterCallbacks callbacks;

	/**
	 * Creates a new TalentTierListAdapter instance.
	 *
	 * @param context the view {@link Context} the adapter will be attached to.
	 */
	public TalentTierListAdapter(Context context, TalentTiersAdapterCallbacks callbacks) {
		super(context, LAYOUT_RESOURCE_ID);
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callbacks = callbacks;
	}

	@Override
	@NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		View rowView;
		ViewHolder holder;

		if(listView == null && parent instanceof ListView) {
			listView = (ListView)parent;
		}
		if (convertView == null) {
			rowView = layoutInflater.inflate(LAYOUT_RESOURCE_ID, parent, false);
			holder = new ViewHolder((TextView) rowView.findViewById(R.id.talent_name_view),
									(TextView) rowView.findViewById(R.id.talent_tiers_view),
									(ImageButton) rowView.findViewById(R.id.increment_button),
									(ImageButton) rowView.findViewById(R.id.decrement_button));
			rowView.setTag(holder);
		}
		else {
			rowView = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		TalentTier talentTier = getItem(position);
		holder.talentTier = talentTier;
		if(talentTier != null && talentTier.getTalent() != null) {
			String builder = talentTier.getTalent().getName() + " (" + talentTier.getTalent().getDpCost() + "/"
					+ talentTier.getTalent().getDpCostPerTier() + ")";
			holder.talentNameView.setText(builder);
			holder.tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
		}

		return rowView;
	}

	public class ViewHolder {
		private TalentTier  talentTier;
		private TextView    talentNameView;
		private TextView    tiersView;
		private ImageButton incrementButton;
		private ImageButton decrementButton;

		ViewHolder(TextView talentNameView, TextView tiersView, ImageButton incrementButton, ImageButton decrementButton) {
			this.talentNameView = talentNameView;
			this.tiersView = tiersView;
			this.incrementButton = incrementButton;
			this.decrementButton = decrementButton;

			initIncrementButton();
			initDecrementButton();
		}

		private void initIncrementButton() {
			incrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(talentTier.getStartingTiers() < 2 && callbacks.purchaseTier(
							talentTier.getTalent(),
							talentTier.getStartingTiers(),
							(short)(talentTier.getEndingTiers() - talentTier.getStartingTiers()))) {
						talentTier.setEndingTiers((short)(talentTier.getEndingTiers() + 1));
						tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
					}
				}
			});
		}

		private void initDecrementButton() {
			decrementButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(talentTier.getEndingTiers() > 0 && callbacks.sellTier(
							talentTier.getTalent(),
							talentTier.getStartingTiers(),
							(short)(talentTier.getEndingTiers() - talentTier.getStartingTiers()))) {
						talentTier.setEndingTiers((short)(talentTier.getEndingTiers() - 1));
						tiersView.setText(String.valueOf(talentTier.getStartingTiers() + talentTier.getEndingTiers()));
					}
				}
			});
		}

		public TalentTier getTalentTier() {
			return talentTier;
		}
	}

	public interface TalentTiersAdapterCallbacks {
		boolean purchaseTier(Talent talent, short startingTiers, short purchasedThisLevel);
		boolean sellTier(Talent talent, short startingTiers, short purchasedThisLevel);
	}
}

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
package com.madinnovations.rmu.view.activities.creature;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.widgets.VerticalTextView;

/**
 * Handles interactions with the UI for creature archetypes.
 */
public class CreatureArchetypesLevelsFragment extends Fragment {
	private static final String TAG = "CreatureArchetypesLevel";
	private CreatureArchetypesFragment               creatureArchetypesFragment;
	private ArrayAdapter<CreatureArchetypeLevel>     levelsAdapter;
	private ListView                                 levelsListView;

	/**
	 * Creates a new CreatureArchetypesLevelsFragment instance.
	 *
	 * @param creatureArchetypesFragment  the CreatureArchetypesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureArchetypesLevelsFragment newInstance(CreatureArchetypesFragment creatureArchetypesFragment) {
		CreatureArchetypesLevelsFragment fragment = new CreatureArchetypesLevelsFragment();
		fragment.creatureArchetypesFragment = creatureArchetypesFragment;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_archetypes_levels_page, container, false);

		initStatLabels(layout);
		initLevelsList(layout);
		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			creatureArchetypesFragment.saveItem();
		}
		super.onPause();
	}

	public boolean copyViewsToItem() {
		boolean changed = false;

		return changed;
	}

	public void copyItemToViews() {
	}

	private void initStatLabels(View layout) {
		TextView label;

		label = (VerticalTextView)layout.findViewById(R.id.agility_label);
		label.setText(Statistic.AGILITY.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.constitution_label);
		label.setText(Statistic.CONSTITUTION.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.constitution_stat_label);
		label.setText(String.format(getString(R.string.stat_literal_string), Statistic.CONSTITUTION.getAbbreviation()));

		label = (VerticalTextView)layout.findViewById(R.id.empathy_label);
		label.setText(Statistic.EMPATHY.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.intuition_label);
		label.setText(Statistic.INTUITION.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.memory_label);
		label.setText(Statistic.MEMORY.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.presence_label);
		label.setText(Statistic.PRESENCE.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.reasoning_label);
		label.setText(Statistic.REASONING.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.quickness_label);
		label.setText(Statistic.QUICKNESS.getAbbreviation());

		label = (VerticalTextView)layout.findViewById(R.id.self_discipline_label);
		label.setText(Statistic.SELF_DISCIPLINE.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.strength_label);
		label.setText(Statistic.STRENGTH.getAbbreviation());
	}

	private void initLevelsList(View layout) {
		levelsListView = (ListView)layout.findViewById(R.id.levels_list_view);
		levelsAdapter = new ArrayAdapter<CreatureArchetypeLevel>(getActivity(), R.layout.creature_archetypes_level_row);
		levelsListView.setAdapter(levelsAdapter);
	}
}

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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

/**
 * Handles interactions with the UI for Race talents.
 */
public class RacesTalentsPageFragment extends Fragment {
	private static final String TAG = "RacesTalentsPageFragmen";
	private   RacesFragment                racesFragment;

	/**
	 * Creates a new RacesTalentsPageFragment instance.
	 *
	 * @param racesFragment  the RacesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static RacesTalentsPageFragment newInstance(RacesFragment racesFragment) {
		RacesTalentsPageFragment fragment = new RacesTalentsPageFragment();
		fragment.racesFragment = racesFragment;
		Log.d(TAG, "newInstance: fragment = " + fragment);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity) getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_talents_page, container, false);

		return layout;
	}

	// <editor-fold desc="Copy to/from views/entity methods">
	public boolean copyViewsToItem() {
		Race currentInstance = racesFragment.getCurrentInstance();
		boolean changed = false;
		return changed;
	}

	public void copyItemToViews() {
		Race currentInstance = racesFragment.getCurrentInstance();
	}
	// </editor-fold>
}

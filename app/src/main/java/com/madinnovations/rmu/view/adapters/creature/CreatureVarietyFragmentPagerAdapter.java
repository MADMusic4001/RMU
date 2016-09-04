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

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyAttackPageFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyMainPageFragment;

/**
 * Manages the page fragments for a ViewPager
 */
public class CreatureVarietyFragmentPagerAdapter extends FragmentPagerAdapter {
	CreatureVarietiesFragment varietiesFragment;

	public CreatureVarietyFragmentPagerAdapter(FragmentManager fm, CreatureVarietiesFragment varietiesFragment) {
		super(fm);
		this.varietiesFragment = varietiesFragment;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;

		switch (position) {
			case 0:
				CreatureVarietyMainPageFragment mainPageFragment = new CreatureVarietyMainPageFragment();
				mainPageFragment.setVarietiesFragment(varietiesFragment);
				fragment = mainPageFragment;
				break;
			case 1:
				CreatureVarietyAttackPageFragment attackPageFragment = new CreatureVarietyAttackPageFragment();
				attackPageFragment.setVarietiesFragment(varietiesFragment);
				fragment = attackPageFragment;
				break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String title = null;

		switch (position) {
			case 0:
				title = varietiesFragment.getString(R.string.title_creature_variety_main_page);
				break;
			case 1:
				title = varietiesFragment.getString(R.string.title_creature_variety_attacks_page);
				break;
		}
		return title;
	}
}

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
package com.madinnovations.rmu.view.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.madinnovations.rmu.view.RMUAppException;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyAttackPageFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyMainPageFragment;

/**
 * FragmentPagerAdapter implementation for use in the various fragments that are using ViewPagers
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
	private      SparseArray<Fragment> registeredFragments   = new SparseArray<>();
	private int numPages;
	private String[] titles;
	private Instantiator instantiator;

	/**
	 * Creates a new ViewPagerAdapter instance.
	 *
	 * @param fm  the FragmentManager instance to use to manage the pages
	 */
	public ViewPagerAdapter(FragmentManager fm, Context context, int numPages, @NonNull Instantiator instantiator,
					 @ArrayRes int titlesArrayId) {
		super(fm);
		this.numPages = numPages;
		this.instantiator = instantiator;
		this.titles = context.getResources().getStringArray(titlesArrayId);}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = instantiator.newInstance(position);

		registeredFragments.put(position, fragment);

		return fragment;
	}

	@Override
	public int getCount() {
		return numPages;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment createdFragment = (Fragment)super.instantiateItem(container, position);
		registeredFragments.put(position, createdFragment);
		return createdFragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	/**
	 * Gets the Fragment instance at the given position
	 *
	 * @param position  the position of the Fragment instance to retrieve
	 * @return  the Fragment instance.
	 */
	public Fragment getFragment(int position) {
		return registeredFragments.get(position);
	}

	public interface Instantiator {
		Fragment newInstance(int position);
	}
}

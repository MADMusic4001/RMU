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
package com.madinnovations.rmu.view.activities.character;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.view.RMUAppException;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.ViewPagerAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for body parts.
 */
public class RacesFragment extends Fragment implements TwoFieldListAdapter.GetValues<Race>, ViewPagerAdapter.Instantiator {
	private static final String TAG = "RacesFragment";
	private static final int NUM_PAGES = 3;
	private static final int MAIN_PAGE = 0;
	private static final int TALENTS_PAGE = 1;
	private static final int CULTURES_PAGE = 2;
	@Inject
	protected RaceRxHandler                raceRxHandler;
	private   TwoFieldListAdapter<Race>    listAdapter;
	private   ListView                     listView;
	private   ViewPagerAdapter             viewPagerAdapter;
	private   Race                         currentInstance = new Race();
	private   boolean                      isNew           = true;

	// <editor-fold desc="method overrides/implementations">
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.races_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_race_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_race_description));

		initListView(layout);

		viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), NUM_PAGES, this,
												R.array.race_page_names);
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.pager);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause: ");
		if(copyViewsToItem()) {
			saveItem();
		}
		if(viewPagerAdapter.getFragment(MAIN_PAGE) != null) {
			getChildFragmentManager().beginTransaction().remove(viewPagerAdapter.getFragment(MAIN_PAGE)).commit();
		}
		if(viewPagerAdapter.getFragment(TALENTS_PAGE) != null) {
			getChildFragmentManager().beginTransaction().remove(viewPagerAdapter.getFragment(TALENTS_PAGE)).commit();
		}
		if(viewPagerAdapter.getFragment(CULTURES_PAGE) != null) {
			getChildFragmentManager().beginTransaction().remove(viewPagerAdapter.getFragment(CULTURES_PAGE)).commit();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.races_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_race) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new Race();
			isNew = true;
			copyItemToViews();
			listView.clearChoices();
			listAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.race_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Race race;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_race:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new Race();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_race:
				race = (Race)listView.getItemAtPosition(info.position);
				if(race != null) {
					deleteItem(race);
					return true;
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public CharSequence getField1Value(Race race) {
		return race.getName();
	}

	@Override
	public CharSequence getField2Value(Race race) {
		return race.getDescription();
	}

	@Override
	public Fragment newInstance(int position) {
		Fragment result = null;

		switch (position) {
			case MAIN_PAGE:
				result = RacesMainPageFragment.newInstance(this);
				break;
			case TALENTS_PAGE:
				result = RacesTalentsPageFragment.newInstance(this);
				break;
			case CULTURES_PAGE:
				result = RacesCulturesPageFragment.newInstance(this);
				break;
		}

		return result;
	}
	// </editor-fold>

	// <editor-fold desc="Copy to/from views/entity methods">
	private boolean copyViewsToItem() {
		boolean changed = false;

		RacesMainPageFragment racesMainPageFragment = (RacesMainPageFragment)viewPagerAdapter.getFragment(MAIN_PAGE);
		if(racesMainPageFragment != null) {
			changed = racesMainPageFragment.copyViewsToItem();
		}

		RacesTalentsPageFragment racesTalentsPageFragment = (RacesTalentsPageFragment)viewPagerAdapter.getFragment(TALENTS_PAGE);
		if(racesTalentsPageFragment != null) {
			changed = racesTalentsPageFragment.copyViewsToItem();
		}

		RacesCulturesPageFragment racesCulturesPageFragment = (RacesCulturesPageFragment)viewPagerAdapter
				.getFragment(CULTURES_PAGE);
		if(racesCulturesPageFragment != null) {
			changed = racesCulturesPageFragment.copyViewsToItem();
		}

		return changed;
	}

	private void copyItemToViews() {
		try {
			throw new RMUAppException();
		}
		catch (RMUAppException e) {
			Log.e(TAG, "copyItemToViews: stack trace", e);
		}
		RacesMainPageFragment racesMainPageFragment = (RacesMainPageFragment)viewPagerAdapter.getFragment(MAIN_PAGE);
		if(racesMainPageFragment != null) {
			racesMainPageFragment.copyItemToViews();
		}

		RacesTalentsPageFragment racesTalentsPageFragment = (RacesTalentsPageFragment)viewPagerAdapter.getFragment(TALENTS_PAGE);
		if(racesTalentsPageFragment != null) {
			racesTalentsPageFragment.copyItemToViews();
		}

		RacesCulturesPageFragment racesCulturesPageFragment = (RacesCulturesPageFragment)viewPagerAdapter
				.getFragment(CULTURES_PAGE);
		if(racesCulturesPageFragment != null) {
			racesCulturesPageFragment.copyItemToViews();
		}
	}
	// </editor-fold>

	// <editor-fold desc="Save/delete entity methods">
	private void deleteItem(final Race item) {
		raceRxHandler.deleteById(item.getId())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Boolean>() {
				@Override
				public void onCompleted() {}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception when deleting: " + item, e);
					Toast.makeText(getActivity(), getString(R.string.toast_race_delete_failed), Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Boolean success) {
					if(success) {
						int position = listAdapter.getPosition(item);
						if(position == listAdapter.getCount() -1) {
							position--;
						}
						listAdapter.remove(item);
						listAdapter.notifyDataSetChanged();
						if(position >= 0) {
							listView.setSelection(position);
							listView.setItemChecked(position, true);
							currentInstance = listAdapter.getItem(position);
						}
						else {
							currentInstance = new Race();
							isNew = true;
						}
						copyItemToViews();
						Toast.makeText(getActivity(), getString(R.string.toast_race_deleted), Toast.LENGTH_SHORT).show();
					}
				}
			});
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			raceRxHandler.save(currentInstance)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Race>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception saving Race", e);
						String toastString = getString(R.string.toast_race_save_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Race savedRace) {
						if (wasNew) {
							listAdapter.add(savedRace);
							if(savedRace == currentInstance) {
								listView.setSelection(listAdapter.getPosition(savedRace));
								listView.setItemChecked(listAdapter.getPosition(savedRace), true);
							}
							listAdapter.notifyDataSetChanged();
						}
						if (getActivity() != null) {
							String toastString;
							toastString = getString(R.string.toast_race_saved);
							Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();

							int position = listAdapter.getPosition(currentInstance);
							LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
							if (v != null) {
								TextView textView = (TextView) v.findViewById(R.id.row_field1);
								textView.setText(currentInstance.getName());
								textView = (TextView) v.findViewById(R.id.row_field2);
								textView.setText(currentInstance.getDescription());
							}
						}
					}
				});
		}
	}
	// </editor-fold>

	// <editor-fold desc="Widget initialization methods">
	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		raceRxHandler.getAll()
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.subscribe(new Subscriber<Collection<Race>>() {
				@Override
				public void onCompleted() {
					if(listAdapter.getCount() > 0) {
						currentInstance = listAdapter.getItem(0);
						isNew = false;
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						listAdapter.notifyDataSetChanged();
						copyItemToViews();
					}
				}
				@Override
				public void onError(Throwable e) {
					Log.e(TAG, "Exception caught getting all Race instances", e);
					Toast.makeText(RacesFragment.this.getActivity(),
							getString(R.string.toast_races_load_failed),
							Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onNext(Collection<Race> races) {
					listAdapter.clear();
					listAdapter.addAll(races);
					listAdapter.notifyDataSetChanged();
					if(races.size() > 0) {
						listView.setSelection(0);
						listView.setItemChecked(0, true);
						currentInstance = listAdapter.getItem(0);
						isNew = false;
					}
					String toastString;
					toastString = String.format(getString(R.string.toast_races_loaded), races.size());
					Toast.makeText(RacesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
				}
			});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (Race) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new Race();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}
	// </editor-fold>

	public Race getCurrentInstance() {
		return currentInstance;
	}
}

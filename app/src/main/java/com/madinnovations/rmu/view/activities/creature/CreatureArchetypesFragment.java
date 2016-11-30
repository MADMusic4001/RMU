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
import android.support.annotation.NonNull;
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
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureArchetypeRxHandler;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.ViewPagerAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature archetypes.
 */
public class CreatureArchetypesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureArchetype>,
		ViewPagerAdapter.Instantiator {
	private static final int                         NUM_PAGES             = 2;
	private static final int                         MAIN_PAGE_INDEX       = 0;
	private static final int                         LEVELS_PAGE_INDEX     = 1;
	@Inject
	protected CreatureArchetypeRxHandler             creatureArchetypeRxHandler;
	@Inject
	protected SkillCategoryRxHandler                 skillCategoryRxHandler;
	private   TwoFieldListAdapter<CreatureArchetype> listAdapter;
	private   ListView                               listView;
	private ViewPagerAdapter                         pagerAdapter    = null;
	private CreatureArchetype                        currentInstance = new CreatureArchetype();
	private boolean                                  isNew           = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_archetypes_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_archetype_name));
		((LinearLayout.LayoutParams)layout.findViewById(R.id.header_field2).getLayoutParams()).weight = 4;
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_archetype_description));

		initViewPager(layout);
		initListView(layout);

		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			saveItem();
		}
		if(pagerAdapter.getFragment(MAIN_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(MAIN_PAGE_INDEX)).commit();
		}
		if(pagerAdapter.getFragment(LEVELS_PAGE_INDEX) != null) {
			getChildFragmentManager().beginTransaction().remove(pagerAdapter.getFragment(LEVELS_PAGE_INDEX)).commit();
		}
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.creature_archetypes_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_archetype) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CreatureArchetype();
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
		getActivity().getMenuInflater().inflate(R.menu.creature_archetype_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureArchetype creatureArchetype;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_archetype:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CreatureArchetype();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature_archetype:
				creatureArchetype = (CreatureArchetype)listView.getItemAtPosition(info.position);
				if(creatureArchetype != null) {
					deleteItem(creatureArchetype);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public Fragment newInstance(int position) {
		Fragment fragment = null;

		switch (position) {
			case MAIN_PAGE_INDEX:
				fragment = CreatureArchetypesMainPageFragment.newInstance(this);
				break;
			case LEVELS_PAGE_INDEX:
				fragment = CreatureArchetypesLevelsFragment.newInstance(this);
				break;
		}

		return fragment;
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		View currentFocusView = getActivity().getCurrentFocus();
		if(currentFocusView != null) {
			currentFocusView.clearFocus();
		}

		CreatureArchetypesMainPageFragment mainPageFragment = (CreatureArchetypesMainPageFragment)pagerAdapter
				.getFragment(MAIN_PAGE_INDEX);
		if(mainPageFragment != null) {
			changed = mainPageFragment.copyViewsToItem();
		}

		CreatureArchetypesLevelsFragment levelsFragment = (CreatureArchetypesLevelsFragment)pagerAdapter
				.getFragment(LEVELS_PAGE_INDEX);
		if(levelsFragment != null) {
			changed = levelsFragment.copyViewsToItem();
		}

		return changed;
	}

	private void copyItemToViews() {
		CreatureArchetypesMainPageFragment mainPageFragment = (CreatureArchetypesMainPageFragment)pagerAdapter
				.getFragment(MAIN_PAGE_INDEX);
		if(mainPageFragment != null) {
			mainPageFragment.copyItemToViews();
		}

		CreatureArchetypesLevelsFragment levelsFragment = (CreatureArchetypesLevelsFragment)pagerAdapter
				.getFragment(LEVELS_PAGE_INDEX);
		if(levelsFragment != null) {
			levelsFragment.copyItemToViews();
		}
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureArchetypeRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CreatureArchetype>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CreatureArchetypesFrag", "Exception saving new CreatureArchetype.", e);
							Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CreatureArchetype savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_saved), Toast.LENGTH_SHORT).show();
								int position = listAdapter.getPosition(savedItem);
								LinearLayout v = (LinearLayout) listView.getChildAt(position - listView.getFirstVisiblePosition());
								if (v != null) {
									TextView textView = (TextView) v.findViewById(R.id.row_field1);
									textView.setText(savedItem.getName());
									textView = (TextView) v.findViewById(R.id.row_field2);
									textView.setText(savedItem.getDescription());
								}
							}
						}
					});
		}
	}

	private void deleteItem(@NonNull final CreatureArchetype item) {
		creatureArchetypeRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureArchetypesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_creature_archetype_delete_failed);
						Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
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
								currentInstance = new CreatureArchetype();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_creature_archetype_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initViewPager(View layout) {
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.pager);
		pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), NUM_PAGES, this,
											R.array.creature_archetype_page_names);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(MAIN_PAGE_INDEX);
	}

 	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 4, this);
		listView.setAdapter(listAdapter);

		creatureArchetypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureArchetype>>() {
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
						Log.e("CreatureArchetypesFrag",
								"Exception caught getting all CreatureArchetype instances in onCreateView", e);
						Toast.makeText(CreatureArchetypesFragment.this.getActivity(),
								getString(R.string.toast_creature_archetypes_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CreatureArchetype> creatureArchetypes) {
						listAdapter.clear();
						listAdapter.addAll(creatureArchetypes);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_archetypes_loaded), creatureArchetypes.size());
						Toast.makeText(CreatureArchetypesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CreatureArchetype) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CreatureArchetype();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(CreatureArchetype creatureArchetype) {
		return creatureArchetype.getName();
	}

	@Override
	public CharSequence getField2Value(CreatureArchetype creatureArchetype) {
		return creatureArchetype.getDescription();
	}

	// Getter
	public CreatureArchetype getCurrentInstance() {
		return currentInstance;
	}
}

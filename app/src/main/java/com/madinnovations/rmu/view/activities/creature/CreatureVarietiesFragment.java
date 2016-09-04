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
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.creature.CreatureVarietyFragmentPagerAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietiesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureVariety> {
	@Inject
	protected CreatureVarietyRxHandler           creatureVarietyRxHandler;
	private TwoFieldListAdapter<CreatureVariety> listAdapter;
	private CreatureVarietyFragmentPagerAdapter  pagerAdapter;
	private ListView                             listView;
	private CreatureVariety currentInstance = new CreatureVariety();
	private boolean         isNew           = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_varieties_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_variety_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_variety_description));

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
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.creature_varieties_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_creature_variety) {
			if(copyViewsToItem()) {
				saveItem();
			}
			currentInstance = new CreatureVariety();
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
		getActivity().getMenuInflater().inflate(R.menu.creature_variety_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final CreatureVariety creatureVariety;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_creature_variety:
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = new CreatureVariety();
				isNew = true;
				copyItemToViews();
				listView.clearChoices();
				listAdapter.notifyDataSetChanged();
				return true;
			case R.id.context_delete_creature_variety:
				creatureVariety = (CreatureVariety)listView.getItemAtPosition(info.position);
				if(creatureVariety != null) {
					deleteItem(creatureVariety);
					return true;
				}
		}
		return super.onContextItemSelected(item);
	}

	private boolean copyViewsToItem() {
		boolean changed = false;

		CreatureVarietyMainPageFragment mainPageFragment = (CreatureVarietyMainPageFragment)pagerAdapter.getItem(0);
		if(mainPageFragment != null) {
			changed = mainPageFragment.copyViewsToItem();
		}

		CreatureVarietyAttackPageFragment attackPageFragment = (CreatureVarietyAttackPageFragment)pagerAdapter.getItem(1);
		if(attackPageFragment != null) {
			changed |= attackPageFragment.copyViewsToItem();
		}

		return changed;
	}

	private void copyItemToViews() {
		CreatureVarietyMainPageFragment mainPageFragment = (CreatureVarietyMainPageFragment)pagerAdapter.getItem(0);
		if(mainPageFragment != null) {
			mainPageFragment.copyItemToViews();
		}

		CreatureVarietyAttackPageFragment attackPageFragment = (CreatureVarietyAttackPageFragment)pagerAdapter.getItem(1);
		if(attackPageFragment != null) {
			attackPageFragment.copyItemToViews();
		}
	}

	public void saveItem() {
		if(currentInstance.isValid()) {
			final boolean wasNew = isNew;
			isNew = false;
			creatureVarietyRxHandler.save(currentInstance)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<CreatureVariety>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e("CreatureVarietiesFrag", "Exception saving new CreatureVariety: " + currentInstance, e);
							Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_save_failed), Toast.LENGTH_SHORT).show();
						}
						@Override
						public void onNext(CreatureVariety savedItem) {
							if (wasNew) {
								listAdapter.add(savedItem);
								if(savedItem == currentInstance) {
									listView.setSelection(listAdapter.getPosition(savedItem));
									listView.setItemChecked(listAdapter.getPosition(savedItem), true);
								}
								listAdapter.notifyDataSetChanged();
							}
							if(getActivity() != null) {
								Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_saved), Toast.LENGTH_SHORT).show();
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

	private void deleteItem(@NonNull final CreatureVariety item) {
		creatureVarietyRxHandler.deleteById(item.getId())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Boolean>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception when deleting: " + item, e);
						String toastString = getString(R.string.toast_creature_variety_delete_failed);
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
								currentInstance = new CreatureVariety();
								isNew = true;
							}
							copyItemToViews();
							Toast.makeText(getActivity(), getString(R.string.toast_creature_variety_deleted), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initViewPager(View layout) {
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.pager);
		pagerAdapter = new CreatureVarietyFragmentPagerAdapter(getFragmentManager(), this);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}

	private void initListView(View layout) {
		listView = (ListView) layout.findViewById(R.id.list_view);
		listAdapter = new TwoFieldListAdapter<>(this.getActivity(), 1, 5, this);
		listView.setAdapter(listAdapter);

		creatureVarietyRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureVariety>>() {
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
						Log.e("CreatureVarietiesFrag",
								"Exception caught getting all CreatureVariety instances in initListView", e);
						Toast.makeText(CreatureVarietiesFragment.this.getActivity(),
								getString(R.string.toast_creature_varieties_load_failed),
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onNext(Collection<CreatureVariety> creatureVarieties) {
						listAdapter.clear();
						listAdapter.addAll(creatureVarieties);
						listAdapter.notifyDataSetChanged();
						String toastString;
						toastString = String.format(getString(R.string.toast_creature_varieties_loaded), creatureVarieties.size());
						Toast.makeText(CreatureVarietiesFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		// Clicking a row in the listView will send the user to the edit world activity
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(copyViewsToItem()) {
					saveItem();
				}
				currentInstance = (CreatureVariety) listView.getItemAtPosition(position);
				isNew = false;
				if (currentInstance == null) {
					currentInstance = new CreatureVariety();
					isNew = true;
				}
				copyItemToViews();
			}
		});
		registerForContextMenu(listView);
	}

	@Override
	public CharSequence getField1Value(CreatureVariety variety) {
		return variety.getName();
	}

	@Override
	public CharSequence getField2Value(CreatureVariety variety) {
		return variety.getDescription();
	}

	public CreatureVariety getCurrentInstance() {
		return this.currentInstance;
	}
}

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
package com.madinnovations.rmu.view.activities.common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TalentCategoryListAdapter;
import com.madinnovations.rmu.view.di.modules.FragmentModule;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/25/2016.
 */
public class TalentCategoryFragment extends Fragment {
	@Inject
	protected TalentCategoryRxHandler talentCategoryRxHandler;
	@Inject
	protected TalentCategoryListAdapter listAdapter;
	private EditText nameEdit;
	private EditText descriptionEdit;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talent_categories_fragment, container, false);

		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);

		initListView(layout);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("TalentCategoryFragment", "Exception occured loading talent categories", e);
					}

					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						String toastString;

						toastString = String.format(getString(R.string.toast_talent_categories_loaded), talentCategories.size());
						listAdapter.clear();
						listAdapter.addAll(talentCategories);
						listAdapter.notifyDataSetChanged();
						Toast.makeText(TalentCategoryFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		setHasOptionsMenu(true);
		return layout;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.e("TalentCategoryFragment", "Creating options menu");
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.talent_category_action_bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_new_talent_category) {
			TalentCategory talentCategory = new TalentCategory();
			talentCategory.setName(getString(R.string.default_talent_category_name));
			talentCategory.setDescription(getString(R.string.default_talent_category_description));
			talentCategoryRxHandler.save(talentCategory)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribeOn(Schedulers.io())
					.subscribe(new Subscriber<TalentCategory>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(TalentCategory talentCategory) {
							listAdapter.add(talentCategory);
							nameEdit.setText(talentCategory.getName());
							descriptionEdit.setText(talentCategory.getDescription());
						}
					});
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.talent_category_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TalentCategory talentCategory;

		AdapterView.AdapterContextMenuInfo info =
				(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

		switch (item.getItemId()) {
			case R.id.context_new_talent_category:
				talentCategory = (TalentCategory) listView.getItemAtPosition(info.position);
				if(talentCategory != null) {
//					eventBus.post(new RegionEvent.Selected(region, true));
					return true;
				}
				else {
					return false;
				}
			case R.id.context_delete_talent_category:
				talentCategory = (TalentCategory)listView.getItemAtPosition(info.position);
				if(talentCategory != null) {
					talentCategoryRxHandler.deleteById(talentCategory.getId())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe(new Subscriber<Collection<TalentCategory>>() {
								@Override
								public void onCompleted() {

								}

								@Override
								public void onError(Throwable e) {
									String toastString = getString(R.string.toast_worlds_deleted_error);
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onNext(Collection<Region> regions) {
									String toastString;

									for (Region deletedRegion : regions) {
										regionListAdapter.remove(deletedRegion);
									}
									if (regionListAdapter.getCount() > 0) {
										regionListAdapter.notifyDataSetChanged();
									}
									else {
										regionListAdapter.notifyDataSetInvalidated();
									}
									toastString = String.format(getString(R.string.toast_worlds_deleted), regions.size());
									Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
								}
							});
					return true;
				}
				else {
					return false;
				}
			default:
				return super.onContextItemSelected(item);
		}
	}

	private void initListView(View layout) {
		View headerView;

		ListView listView = (ListView) layout.findViewById(R.id.list_view);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.name_description_header, listView, false);
		listView.addHeaderView(headerView);

		listView.setAdapter(listAdapter);

		// Clicking a row in the listView will send the user to the edit world activity
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				World theWorld = (World) listView.getItemAtPosition(position);
//				if (theWorld != null) {
//					editWorld(theWorld);
//				}
//			}
//		});
		registerForContextMenu(listView);
	}
}

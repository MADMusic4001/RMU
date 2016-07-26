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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.data.entities.common.TalentCategory;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TalentCategoryListAdapter;
import com.madinnovations.rmu.view.di.modules.FragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 * Created 7/25/2016.
 */
public class CommonDataFragment extends Fragment {
	@Inject
	protected TalentCategoryRxHandler talentCategoryRxHandler;
	@Inject
	protected TalentCategoryListAdapter listAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newFragmentComponent(new FragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.talent_categories_fragment, container, false);

		initListView(layout);
//		ListView list = (ListView)layout.findViewById(R.id.listView);
//		list.setAdapter(listAdapter);

		talentCategoryRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<TalentCategory>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Collection<TalentCategory> talentCategories) {
						String toastString;

						toastString = String.format(getString(R.string.toast_talent_categories_loaded), talentCategories.size());
						listAdapter.clear();
						listAdapter.addAll(talentCategories);
						listAdapter.notifyDataSetChanged();
						Toast.makeText(CommonDataFragment.this.getActivity(), toastString, Toast.LENGTH_SHORT).show();
					}
				});

		return layout;
	}

	private void initListView(View layout) {
		View headerView;

		ListView listView = (ListView) layout.findViewById(R.id.list_view);
		headerView = getActivity().getLayoutInflater().inflate(
				R.layout.name_description_header, listView, false);
		listView.addHeaderView(headerView);

		// Create and set onClick methods for sorting the {@link World} list.
//		headerView.findViewById(R.id.nameHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				listAdapter.sort(comparatorUtils.getWorldNameComparator());
//			}
//		});
//		headerView.findViewById(R.id.createdHeader).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				adapter.sort(comparatorUtils.getWorldCreateTsComparator());
//			}
//		});

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

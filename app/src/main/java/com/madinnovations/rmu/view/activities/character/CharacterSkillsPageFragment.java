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
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.SkillCostEntry;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillCost;
import com.madinnovations.rmu.data.entities.common.SpecializationCostEntry;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.character.AssignableCostsAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterSkillsPageFragment extends Fragment {
	private static final String LOG_TAG = "CharacterSkillsPageFrag";
	private static final String DRAG_COST = "drag-cost";
	@Inject
	protected SkillRxHandler              skillRxHandler;
	@Inject
	protected SpecializationRxHandler     specializationRxHandler;
	private CharactersFragment            charactersFragment;
	private AssignableCostsAdapter[]      assignableSkillsAdapters;
	private LinearLayout                  assignableCostLayoutRows;
	private ListView[]                    skillCostsListViews;
	private ListView                      skillRanksListView;
	private ListView                      talentTiersListView;
	private Map<Stat, EditText>           statEditTextMap;
	private List<SpecializationCostEntry> skillCostsMap;
	private LinearLayout                  draggingView = null;
	private SkillCostEntry                draggingEntry = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_skills_talents_stats_fragment, container, false);

		initSkillCostsListView(layout);
//		skillRanksListView = initSkillRanksListView(layout);
//		talentTiersListView = initTalentTiersListView(layout);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			charactersFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;
		return changed;
	}

	public void copyItemToViews() {
	}

	public void setCharactersFragment(CharactersFragment charactersFragment) {
		this.charactersFragment = charactersFragment;
	}

	private void initSkillCostsListView(View layout) {
		assignableCostLayoutRows = (LinearLayout)layout.findViewById(R.id.assignable_cost_layout_rows);

		changeProfession();
	}

	public void changeProfession() {
		if(charactersFragment.getCurrentInstance().getProfession() != null &&
				charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size() > 0) {
			int size = charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size();
			if(skillCostsListViews == null || skillCostsListViews.length < size) {
				size = ((size + 2) / 3) * 3;
				skillCostsListViews = new ListView[size];
				assignableSkillsAdapters = new AssignableCostsAdapter[size];
			}
			for(int i = 0; i < charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().size(); i += 3) {
				addAssignableCostRow(i);
			}
			int i = 0;
			for(final SkillCategory category :
					charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCosts().keySet()) {
				final int index = i++;
				skillCostsListViews[index].setVisibility(View.VISIBLE);
				skillRxHandler.getSkillsForCategory(category)
						.subscribe(new Subscriber<Collection<Skill>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(LOG_TAG, "Exception caught getting Skill for category " + category.getName() + ".", e);
							}
							@Override
							public void onNext(Collection<Skill> skillCollection) {
								List<SkillCost> skillCosts = charactersFragment.getCurrentInstance().getProfession()
										.getAssignableSkillCosts().get(category);
								if(skillCollection.size() == skillCosts.size()) {
									assignableSkillsAdapters[index].clear();
									int i = 0;
									for(Skill skill : skillCollection) {
										SkillCostEntry entry = new SkillCostEntry(skill, skillCosts.get(i++));
										assignableSkillsAdapters[index].add(entry);
									}
									Log.d(LOG_TAG, "Loaded " + i + " skillCosts");
									assignableSkillsAdapters[index].notifyDataSetChanged();
								}
							}
						});
			}
			for(int j = i; j < skillCostsListViews.length; j++) {
				skillCostsListViews[j].setVisibility(View.INVISIBLE);
			}
		}
	}

	private void addAssignableCostRow(int startIndex) {
		LayoutInflater.from(getActivity()).inflate(R.layout.assignable_costs_header, assignableCostLayoutRows);
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		for(int i = startIndex; i < startIndex + 3; i++) {
			final ListView listView = new ListView(getActivity());
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
			if(params == null) {
				params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
				listView.setLayoutParams(params);
			}
			params.weight = 1;
			skillCostsListViews[i] = listView;
			final AssignableCostsAdapter adapter = new AssignableCostsAdapter(getActivity());
			listView.setAdapter(adapter);
			assignableSkillsAdapters[i] = adapter;
			listView.setVisibility(View.INVISIBLE);
			linearLayout.addView(listView);
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					ClipData dragData;

					ClipData.Item clipDataItem = new ClipData.Item(String.valueOf(position));
					dragData = new ClipData(DRAG_COST, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
					List<View> dragViewList = new ArrayList<>(1);
					dragViewList.add(view);
					View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(dragViewList);
					draggingView = (LinearLayout)view;
					draggingEntry = adapter.getItem(position);

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						view.startDragAndDrop(dragData, myShadow, position, 0);
					}
					else {
						//noinspection deprecation
						view.startDrag(dragData, myShadow, position, 0);
					}
					return false;
				}
			});

			listView.setOnDragListener(new AssignableCostDragListener());
		}
		assignableCostLayoutRows.addView(linearLayout);
	}


	protected class AssignableCostDragListener implements View.OnDragListener {
		private Animation slideUpAnim;
		private Animation slideDownAnim;
		private SkillCostEntry[] entries;
		private int draggedPosition = -1;
		private int currentPosition = -1;

		AssignableCostDragListener() {
			slideUpAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
			slideUpAnim.setDuration(500);
			slideDownAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
			slideDownAnim.setDuration(500);
		}

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			if(event.getClipDescription() == null || !DRAG_COST.equals(event.getClipDescription().getLabel())) {
				return false;
			}

			ListView listView = (ListView)v;
			AssignableCostsAdapter adapter = (AssignableCostsAdapter)listView.getAdapter();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					draggedPosition = currentPosition = (Integer)event.getLocalState();
					entries = new SkillCostEntry[adapter.getCount()];
					for(int i = 0; i < adapter.getCount(); i++) {
						entries[i] = adapter.getItem(i);
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					for(int i = 0; i < listView.getChildCount(); i++) {
						LinearLayout childView = (LinearLayout)listView.getChildAt(i);
						Rect hitRect = new Rect();
						childView.getHitRect(hitRect);
						if(hitRect.contains((int)event.getX(), (int)event.getY())) {
							if(currentPosition != i) {
								if(i > currentPosition) {
									while(i > currentPosition ) {
										animate(listView, currentPosition, currentPosition + 1, slideDownAnim, slideUpAnim);
										currentPosition++;
									}
								}
								else {
									while(currentPosition > i) {
										animate(listView, currentPosition, currentPosition - 1, slideUpAnim, slideDownAnim);
										currentPosition--;
									}
								}
								v.invalidate();
							}
						}
					}
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					reset(listView);
					break;
				case DragEvent.ACTION_DROP:
					adapter.clear();
					adapter.addAll(entries);
					adapter.notifyDataSetChanged();
					v.invalidate();
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					if(!event.getResult()) {
						reset(listView);
					}
					v.invalidate();
					break;
			}

			return true;
		}

		private void reset(ListView listView) {
			int offset;
			Animation fromAnimation;
			Animation toAnimation;
			if(currentPosition > draggedPosition) {
				offset = -1;
				fromAnimation = slideUpAnim;
				toAnimation = slideDownAnim;
			}
			else {
				offset = 1;
				fromAnimation = slideDownAnim;
				toAnimation = slideUpAnim;
			}
			while(currentPosition != draggedPosition) {
				animate(listView, currentPosition, currentPosition + offset, fromAnimation, toAnimation);
				currentPosition += offset;
			}
		}

		private void animate(ListView listView, int from, int to, Animation fromAnimation, Animation toAnimation) {
			TextView newFromNameView = newNameView();
			TextView newToNameView = newNameView();
			LinearLayout fromLayout = (LinearLayout)listView.getChildAt(from);
			LinearLayout toLayout = (LinearLayout)listView.getChildAt(to);
			Skill swap = entries[to].getSkill();
			entries[to].setSkill(entries[from].getSkill());
			entries[from].setSkill(swap);
			newFromNameView.setText(entries[from].getSkill().getName());
			newToNameView.setText(entries[to].getSkill().getName());
			fromLayout.removeView(fromLayout.findViewById(R.id.name_view));
			fromLayout.addView(newFromNameView, 0);
			newFromNameView.startAnimation(fromAnimation);
			toLayout.removeView(toLayout.findViewById(R.id.name_view));
			toLayout.addView(newToNameView, 0);
			newToNameView.startAnimation(toAnimation);
		}

		private TextView newNameView() {
			TextView newNameView = new TextView(getActivity());
			newNameView.setId(R.id.name_view);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
			params.weight = 1;
			newNameView.setLayoutParams(params);
			return newNameView;
		}
	}
}

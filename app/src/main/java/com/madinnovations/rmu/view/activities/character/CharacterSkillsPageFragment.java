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

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.character.SkillCostGroup;
import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillCategory;
import com.madinnovations.rmu.data.entities.common.SkillRanks;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.character.AssignableCostsAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillRanksAdapter;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterSkillsPageFragment extends Fragment implements SkillRanksAdapter.SkillRanksAdapterCallbacks {
	private static final String LOG_TAG = "CharacterSkillsPageFrag";
	private static final String DRAG_COST = "drag-cost";
	private static final String HEADER_TAG = "Header%d";
	private static final String LISTS_ROW_TAG = "ListsRow%d";
	@Inject
	protected SkillRxHandler              skillRxHandler;
	@Inject
	protected SpecializationRxHandler     specializationRxHandler;
	private SkillRanksAdapter             skillRanksAdapter;
	private CharactersFragment            charactersFragment;
	private LinearLayout                  assignableCostLayoutRows;
	private ListView[]                    skillCostsListViews;
	private SkillCategory[]               skillCategories = new SkillCategory[0];
	private TextView                      currentDpText;
//	private List<SpecializationCostEntry> skillCostsMap;

	/**
	 * Creates new CharacterSkillsPageFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterSkillsPageFragment newInstance(CharactersFragment charactersFragment) {
		CharacterSkillsPageFragment fragment = new CharacterSkillsPageFragment();
		fragment.charactersFragment = charactersFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(charactersFragment == null) {
			return null;
		}

		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_skills_talents_stats_fragment, container, false);

		currentDpText = (TextView)layout.findViewById(R.id.current_dp_text);
		initSkillCostsListView(layout);
		initSkillRanksListView(layout);
//		initTalentTiersListView(layout);

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

	@Override
	public boolean purchaseRank(Skill skill, Specialization specialization, short purchasedThisLevel) {
		boolean result = false;
		DevelopmentCostGroup costGroup;
		short cost;
		Character character = charactersFragment.getCurrentInstance();

		costGroup = getSkillCost(skill, specialization);

		if(costGroup != null) {
			if(purchasedThisLevel == 0) {
				cost = costGroup.getFirstCost();
			}
			else {
				cost = costGroup.getAdditionalCost();
			}
			if(cost < character.getCurrentDevelopmentPoints()) {
				character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() - cost));
				currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
				if(skill != null) {
					character.getSkillRanks().put(skill, (short) (character.getSkillRanks().get(skill) + 1));
				}
				else {
					character.getSpecializationRanks().put(
							specialization, (short)(character.getSpecializationRanks().get(specialization) + 1));
				}
				result = true;
			}
		}

		return result;
	}

	@Override
	public boolean sellRank(Skill skill, Specialization specialization, short purchasedThisLevel) {
		boolean result = false;
		DevelopmentCostGroup costGroup;
		short cost;
		Character character = charactersFragment.getCurrentInstance();

		costGroup = getSkillCost(skill, specialization);

		if(costGroup != null) {
			if(purchasedThisLevel == 1) {
				cost = costGroup.getFirstCost();
			}
			else {
				cost = costGroup.getAdditionalCost();
			}
			character.setCurrentDevelopmentPoints((short)(character.getCurrentDevelopmentPoints() + cost));
			currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
			if(skill != null) {
				character.getSkillRanks().put(skill, (short) (character.getSkillRanks().get(skill) - 1));
			}
			else {
				character.getSpecializationRanks().put(
						specialization, (short)(character.getSpecializationRanks().get(specialization) - 1));
			}
			result = true;
		}

		return result;
	}

	@Override
	public DevelopmentCostGroup getSkillCost(Skill skill, Specialization specialization) {
		DevelopmentCostGroup costGroup = null;
		Character character = charactersFragment.getCurrentInstance();

		if(skill != null) {
			costGroup = character.getSkillCosts().get(skill);
			if(costGroup == null) {
				costGroup = character.getProfession().getSkillCosts().get(skill);
				if(costGroup == null) {
					costGroup = character.getProfession().getSkillCategoryCosts().get(skill.getCategory());
				}
			}
		}
		else if(specialization != null) {
			costGroup = character.getSkillCosts().get(specialization.getSkill());
			if(costGroup == null) {
				costGroup = character.getProfession().getSkillCosts().get(specialization.getSkill());
				if(costGroup == null) {
					costGroup = character.getProfession().getSkillCategoryCosts().get(specialization.getSkill().getCategory());
				}
			}
		}

		return costGroup;
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		boolean changed = false;

		for(ListView listView : skillCostsListViews) {
			if(listView != null) {
				AssignableCostsAdapter adapter = (AssignableCostsAdapter)listView.getAdapter();
				if(adapter != null) {
					for (int i = 0; i < adapter.getCount(); i++) {
						SkillCostGroup entry = adapter.getItem(i);
						charactersFragment.getCurrentInstance().getSkillCosts().put(entry.getSkill(), entry.getCostGroup());
					}
				}
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		Character character = charactersFragment.getCurrentInstance();
		currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
		if(character.getProfession() != null) {
			for (Map.Entry<SkillCategory, List<DevelopmentCostGroup>> entry : character.getProfession()
					.getAssignableSkillCostsMap().entrySet()) {
				int index = Arrays.asList(skillCategories).indexOf(entry.getKey());
				if (index != -1) {
					setSkillCosts((AssignableCostsAdapter) skillCostsListViews[index].getAdapter(), new ArrayList<Skill>(),
								  skillCategories[index]);
				}
				else {
					for (int i = 0; i < skillCategories.length; i++) {
						SkillCategory category = skillCategories[i];
						if (category == null || ((!entry.getKey().equals(category)) &&
								(!character.getProfession().getAssignableSkillCostsMap().containsKey(category)))) {
							hideAssignableList(i);
						}
					}
				}
			}
		}
	}

	private void initSkillCostsListView(View layout) {
		assignableCostLayoutRows = (LinearLayout)layout.findViewById(R.id.assignable_cost_layout_rows);

		if(skillCostsListViews == null || skillCostsListViews.length == 0) {
			addAssignableCostsRow();
		}
		changeProfession();
	}

	private void initSkillRanksListView(View layout) {
		ListView skillRanksListView = (ListView) layout.findViewById(R.id.skill_ranks_list);
		skillRanksAdapter = new SkillRanksAdapter(getActivity(), this);
		skillRanksListView.setAdapter(skillRanksAdapter);

		skillRxHandler.getCharacterPurchasableSkills()
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.d(LOG_TAG, "Exception caught getting all purchasable Skill instances.", e);
					}
					@Override
					public void onNext(Collection<Skill> skillCollection) {
						Character character = charactersFragment.getCurrentInstance();
						List<SkillRanks> ranksList = new ArrayList<>();
						for(Skill skill : skillCollection) {
							if(character.getSkillRanks().get(skill) == null) {
								character.getSkillRanks().put(skill, (short)0);
							}
							SkillRanks skillRanks = new SkillRanks();
							skillRanks.setSkill(skill);
							skillRanks.setStartingRanks(character.getSkillRanks().get(skill));
							ranksList.add(skillRanks);
						}
						skillRanksAdapter.addAll(ranksList);
						skillRanksAdapter.notifyDataSetChanged();
					}
				});
		specializationRxHandler.getCharacterPurchasableSpecializations()
				.subscribe(new Subscriber<Collection<Specialization>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(LOG_TAG, "Exception caught getting all purchasable Specialization instances.", e);
					}
					@Override
					public void onNext(Collection<Specialization> specializationCollection) {
						Character character = charactersFragment.getCurrentInstance();
						List<SkillRanks> ranksList = new ArrayList<>();
						for(Specialization specialization : specializationCollection) {
							if(character.getSpecializationRanks().get(specialization) == null) {
								character.getSpecializationRanks().put(specialization, (short)0);
							}
							SkillRanks skillRanks = new SkillRanks();
							skillRanks.setSpecialization(specialization);
							skillRanks.setStartingRanks(character.getSpecializationRanks().get(specialization));
							ranksList.add(skillRanks);
						}
						skillRanksAdapter.addAll(ranksList);
						skillRanksAdapter.notifyDataSetChanged();
					}
				});
	}

	private void hideAssignableList(int index) {
		if(skillCostsListViews.length > index) {
			ListView listView = skillCostsListViews[index];
			if (listView != null) {
				listView.setVisibility(View.INVISIBLE);
				AssignableCostsAdapter adapter = (AssignableCostsAdapter)listView.getAdapter();
				if(adapter != null) {
					adapter.clear();
				}
			}
		}
		if(skillCategories.length > index) {
			skillCategories[index] = null;
		}
	}

	@SuppressLint("DefaultLocale")
	public void changeProfession() {
		if (charactersFragment.getCurrentInstance().getProfession() != null &&
				charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCostsMap().size() > 0) {
			int i = 0;
			for (final SkillCategory category :
					charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCostsMap().keySet()) {
				final int index = i++;
				while (skillCostsListViews.length <= index) {
					addAssignableCostsRow();
				}
				skillCategories[index] = category;
				skillCostsListViews[index].setVisibility(View.VISIBLE);
				skillRxHandler.getSkillsForCategory(category)
						.subscribe(new Subscriber<Collection<Skill>>() {
							@Override
							public void onCompleted() {
							}

							@Override
							public void onError(Throwable e) {
								Log.e(LOG_TAG, "Exception caught getting Skill for category " + category.getName() + ".", e);
							}

							@SuppressWarnings("unchecked")
							@Override
							public void onNext(Collection<Skill> skillCollection) {
								setSkillCosts((AssignableCostsAdapter) skillCostsListViews[index].getAdapter(),
											  skillCollection,
											  category);
							}
						});
			}
			for (int j = skillCostsListViews.length - 1; j > i; j--) {
				if (j % 3 == 2
						&& charactersFragment.getCurrentInstance().getProfession().getAssignableSkillCostsMap().size() <= j
						- 2) {
					LinearLayout header = (LinearLayout) assignableCostLayoutRows.findViewWithTag(
							String.format(HEADER_TAG, j - 2));
					assignableCostLayoutRows.removeView(header);
					LinearLayout listsRow = (LinearLayout) assignableCostLayoutRows.findViewWithTag(
							String.format(LISTS_ROW_TAG, j - 2));
					assignableCostLayoutRows.removeView(listsRow);
					j -= 2;
				}
				else {
					skillCostsListViews[j].setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setSkillCosts(AssignableCostsAdapter adapter, Collection<Skill> skills, SkillCategory category) {
		List<DevelopmentCostGroup> skillCosts = new ArrayList<>(charactersFragment.getCurrentInstance().getProfession()
				.getAssignableSkillCostsMap().get(category));
		List<Skill> skillsCopy = new ArrayList<>(skills);
		if (skills.size() == skillCosts.size()) {
			List<SkillCostGroup> entries = new ArrayList<>(skills.size());
			Character character = charactersFragment.getCurrentInstance();
			SkillCostGroup entry;
			for (Skill skill : skills) {
				DevelopmentCostGroup costGroup;
				if (character != null) {
					costGroup = character.getSkillCosts().get(skill);
					if (costGroup != null) {
						skillCosts.remove(skillCosts.indexOf(costGroup));
						skillsCopy.remove(skill);
						entry = new SkillCostGroup(skill, costGroup);
						entries.add(entry);
					}
				}
			}
			for (DevelopmentCostGroup costGroup : skillCosts) {
				entry = new SkillCostGroup(skillsCopy.get(0), costGroup);
				entries.add(entry);
				skillsCopy.remove(0);
			}
			Collections.sort(entries);
			adapter.clear();
			adapter.addAll(entries);
			adapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("DefaultLocale")
	private void addAssignableCostsRow() {
		LinearLayout header = (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.assignable_costs_header,
																	   assignableCostLayoutRows);
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		int oldSize = skillCostsListViews == null ? 0 : skillCostsListViews.length;
		header.setTag(String.format(HEADER_TAG, oldSize));
		linearLayout.setTag(String.format(LISTS_ROW_TAG, oldSize));
		ListView[] listViews = new ListView[oldSize + 3];
		if(skillCostsListViews != null && skillCostsListViews.length > 0) {
			System.arraycopy(skillCostsListViews, 0, listViews, 0, skillCostsListViews.length);
		}
		skillCostsListViews = listViews;

		SkillCategory[] categories = new SkillCategory[oldSize + 3];
		if(skillCategories != null && skillCategories.length > 0) {
			System.arraycopy(skillCategories, 0, categories, 0, skillCategories.length);
		}
		skillCategories = categories;

		for(int i = oldSize; i < oldSize + 3; i++) {
			final ListView listView = new ListView(getActivity());
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
			if(params == null) {
				params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
				listView.setLayoutParams(params);
			}
			params.weight = 1;
			final AssignableCostsAdapter adapter = new AssignableCostsAdapter(getActivity());
			listView.setAdapter(adapter);
			listView.setVisibility(View.INVISIBLE);
			skillCostsListViews[i] = listView;
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
		private Animation        slideUpAnim;
		private Animation        slideDownAnim;
		private SkillCostGroup[] entries;
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
					entries = new SkillCostGroup[adapter.getCount()];
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
					for(SkillCostGroup entry : entries) {
						charactersFragment.getCurrentInstance().getSkillCosts().put(entry.getSkill(), entry.getCostGroup());
					}
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

	// Getters and setters
	public CharactersFragment getCharactersFragment() {
		return charactersFragment;
	}
	public void setCharactersFragment(CharactersFragment charactersFragment) {
		this.charactersFragment = charactersFragment;
	}
}

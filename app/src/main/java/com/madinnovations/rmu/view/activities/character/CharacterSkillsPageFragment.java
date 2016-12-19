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
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
	private static final String TAG = "CharacterSkillsPageFrag";
	private static final String DRAG_COST = "drag-cost";
	private static final String HEADER_TAG = "Header%d";
	private static final String LISTS_ROW_TAG = "ListsRow%d";
	@Inject
	protected SkillRxHandler          skillRxHandler;
	@Inject
	protected SpecializationRxHandler specializationRxHandler;
	private   View                    fragmentView;
	private   SkillRanksAdapter       skillRanksAdapter;
	private   CharactersFragment      charactersFragment;
	private   LinearLayout            assignableCostLayoutRows;
	private   ListView[]              skillCostsListViews = new ListView[0];
	private   SkillCategory[]         skillCategories = new SkillCategory[0];
	private   EditText                currentDpText;
	private   List<Skill>             skillList = null;

	// TODO: Check to see if Lore Skill selection on Skills UI is used to determine "Other Lores" when showing Culture Ranks in Character Selection Screen and update label if necessary.

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

		fragmentView = inflater.inflate(R.layout.character_skills_page, container, false);

		currentDpText = (EditText) fragmentView.findViewById(R.id.current_dp_text);
		initSkillCostsListView(fragmentView);
		initSkillRanksListView(fragmentView);

		fragmentView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.d(TAG, "onLongClick: ");
				return false;
			}
		});
		copyAssignableCosts();
		return fragmentView;
	}

	@Override
	public short purchaseRank(@NonNull SkillRanks skillRanks) {
		DevelopmentCostGroup costGroup;
		short cost;

		Character character = charactersFragment.getCurrentInstance();
		short result = getCurrentRanks(skillRanks);
		short purchasedThisLevel = getRanksPurchasedThisLevel(skillRanks);
		short remainingCultureRanks = getRemainingCultureRanks(skillRanks);

		if(remainingCultureRanks > 0) {
			result = purchaseWithCultureRanks(skillRanks);
		}
		else {
			costGroup = getSkillCost(skillRanks);

			if (costGroup != null) {
				if (purchasedThisLevel == 0) {
					cost = costGroup.getFirstCost();
				}
				else {
					cost = costGroup.getAdditionalCost();
				}
				if (cost < character.getCurrentDevelopmentPoints() && (getRanksPurchasedThisLevel(skillRanks) < 2 ||
						character.getCampaign().isIntenseTrainingAllowed())) {
					if (skillRanks.getSkill() != null) {
						purchasedThisLevel += (short)1;
						character.getCurrentLevelSkillRanks().put(skillRanks.getSkill(), purchasedThisLevel);

						result += (short)1;
						character.getSkillRanks().put(skillRanks.getSkill(), result);
						character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() - cost));
						currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
						charactersFragment.saveItem();
					}
					else if(skillRanks.getSpecialization() != null) {
						purchasedThisLevel += (short)1;
						character.getCurrentLevelSpecializationRanks().put(skillRanks.getSpecialization(), purchasedThisLevel);

						result += (short)1;
						character.getSpecializationRanks().put(skillRanks.getSpecialization(), result);
						character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() - cost));
						currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
						charactersFragment.saveItem();
					}
				}
			}
		}

		return result;
	}

	@Override
	public short sellRank(SkillRanks skillRanks) {
		short cost;
		boolean changed = false;
		Character character = charactersFragment.getCurrentInstance();
		short result = getCurrentRanks(skillRanks);
		short purchasedThisLevel = getRanksPurchasedThisLevel(skillRanks);
		short purchasedCultureRanks = getPurchasedCultureRanks(skillRanks);
		DevelopmentCostGroup costGroup = getSkillCost(skillRanks);

		if(purchasedThisLevel > 0 && costGroup != null) {
			if (purchasedThisLevel == 1) {
				cost = costGroup.getFirstCost();
			}
			else {
				cost = costGroup.getAdditionalCost();
			}
			if (skillRanks.getSkill() != null) {
				purchasedThisLevel -= (short) 1;
				if (purchasedThisLevel > 0) {
					character.getCurrentLevelSkillRanks().put(skillRanks.getSkill(), purchasedThisLevel);
				}
				else {
					character.getCurrentLevelSkillRanks().remove(skillRanks.getSkill());
				}

				result -= (short) 1;
				if (result > 0) {
					character.getSkillRanks().put(skillRanks.getSkill(), result);
				}
				else {
					result = 0;
					character.getSkillRanks().remove(skillRanks.getSkill());
				}
				character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() + cost));
				currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
				changed = true;
			}
			else if (skillRanks.getSpecialization() != null) {
				purchasedThisLevel -= (short) 1;
				if (purchasedThisLevel > 0) {
					character.getCurrentLevelSpecializationRanks()
							.put(skillRanks.getSpecialization(), purchasedThisLevel);
				}
				else {
					character.getCurrentLevelSpecializationRanks().remove(skillRanks.getSpecialization());
				}

				result -= (short) 1;
				if (result > 0) {
					character.getSpecializationRanks().put(skillRanks.getSpecialization(), result);
				}
				else {
					result = 0;
					character.getSpecializationRanks().remove(skillRanks.getSpecialization());
				}
				character.setCurrentDevelopmentPoints((short) (character.getCurrentDevelopmentPoints() + cost));
				currentDpText.setText((String.valueOf(character.getCurrentDevelopmentPoints())));
				changed = true;
			}
		}
		else if(character.getCurrentLevel() == 0 && purchasedCultureRanks > 0) {
			result = sellCultureRank(skillRanks);
		}

		if(changed) {
			charactersFragment.saveItem();
		}
		return result;
	}

	@Override
	public DevelopmentCostGroup getSkillCost(SkillRanks skillRanks) {
		DevelopmentCostGroup costGroup;
		Character character = charactersFragment.getCurrentInstance();
		Skill baseSkill = skillRanks.getSkill();

		if(skillRanks.getSpecialization() != null) {
			baseSkill = skillRanks.getSpecialization().getSkill();
		}
		costGroup = character.getSkillCosts().get(baseSkill);
		if(costGroup == null && character.getProfession() != null) {
			costGroup = character.getProfession().getSkillCosts().get(baseSkill);
			if(costGroup == null) {
				costGroup = character.getProfession().getSkillCategoryCosts().get(baseSkill.getCategory());
			}
		}

		return costGroup;
	}

	@Override
	public short getCultureRanks(SkillRanks skillRanks) {
		return getRemainingCultureRanks(skillRanks);
	}

	@Override
	public short getRanks(SkillRanks skillRanks) {
		return getCurrentRanks(skillRanks);
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
		copyAssignableCosts();
		if(skillList == null) {
			initSkillRanksListView(fragmentView);
		}
		else {
			copySkillRanksToView();
		}
		changeProfession();
	}

	private void copyAssignableCosts() {
		Character character = charactersFragment.getCurrentInstance();

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

	private void initSkillRanksListView(final View layout) {
		ListView skillRanksListView = (ListView) layout.findViewById(R.id.skill_ranks_list);
		skillRanksAdapter = new SkillRanksAdapter(getActivity(), this);
		skillRanksListView.setAdapter(skillRanksAdapter);

		if(skillList != null) {
			copySkillRanksToView();
		}
		else {
			skillRxHandler.getCharacterPurchasableSkills()
					.subscribe(new Subscriber<Collection<Skill>>() {
						@Override
						public void onCompleted() {}
						@Override
						public void onError(Throwable e) {
							Log.e(TAG, "Exception caught getting all purchasable Skill instances.", e);
						}
						@SuppressWarnings("unchecked")
						@Override
						public void onNext(Collection<Skill> skillCollection) {
							if(skillCollection instanceof List) {
								skillList = (List)skillCollection;
							}
							else {
								skillList = new ArrayList<>(skillCollection);
							}
							copySkillRanksToView();
						}
					});
		}

		skillRanksListView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.d(TAG, "onLongClick: ");
				TextView popupContent = new TextView(getActivity());
				popupContent.setMinimumWidth(layout.getWidth()/2);
				popupContent.setMinimumHeight(layout.getHeight()/2);
				SkillRanks skillRanks = ((SkillRanksAdapter.ViewHolder)v.getTag()).getSkillRanks();
				popupContent.setText(skillRanks.getSkill().getDescription());
				PopupWindow popupWindow = new PopupWindow(popupContent);
				popupWindow.showAtLocation(fragmentView, Gravity.CENTER, 0, 0);
				return true;
			}
		});
		registerForContextMenu(skillRanksListView);
	}

	private void copySkillRanksToView() {
		List<SkillRanks> ranksList = new ArrayList<>();
		for (Skill skill : skillList) {
			SkillRanks skillRanks;
			if(!skill.isRequiresSpecialization()) {
				skillRanks = new SkillRanks();
				skillRanks.setSkill(skill);
				ranksList.add(skillRanks);
			}
			else {
				for (Specialization specialization : skill.getSpecializations()) {
					if(!specialization.isCreatureOnly()) {
						skillRanks = new SkillRanks();
						skillRanks.setSpecialization(specialization);
						ranksList.add(skillRanks);
					}
				}
			}
		}
		//noinspection unchecked
		Collections.sort(ranksList);
		skillRanksAdapter.clear();
		skillRanksAdapter.addAll(ranksList);
		skillRanksAdapter.notifyDataSetChanged();
	}

	private short getRemainingCultureRanks(SkillRanks skillRanks) {
		Character character = charactersFragment.getCurrentInstance();
		short remainingRanks = 0;
		Skill baseSkill = null;

		if(skillRanks.getSkill() != null) {
			baseSkill = skillRanks.getSkill();
		}
		else if(skillRanks.getSpecialization() != null) {
			baseSkill = skillRanks.getSpecialization().getSkill();
		}

		if(character.getCulture() != null && baseSkill != null) {
			Short cultureRanks = character.getCulture().getSkillRanks().get(baseSkill);
			if (cultureRanks != null) {
				short purchasedRanks = 0;
				for(Map.Entry<Object, Short> entry : character.getPurchasedCultureRanks().entrySet()) {
					if(isSkillOrSpecialization(baseSkill, entry.getKey())) {
						if(entry.getValue() != null) {
							purchasedRanks += entry.getValue();
						}
					}
				}
				remainingRanks = (short)(cultureRanks - purchasedRanks);
			}
			else {
				if(baseSkill.isLore()) {
					cultureRanks = character.getCulture().getOtherLoreRanks();
					short purchasedRanks = 0;
					for(Map.Entry<Object, Short> entry : character.getPurchasedCultureRanks().entrySet()) {
						if(isSkillOrSpecialization(baseSkill, entry.getKey())) {
							if(entry.getValue() != null) {
								purchasedRanks += entry.getValue();
							}
						}
					}
					remainingRanks = (short)(cultureRanks - purchasedRanks);
				}
			}
		}

		return remainingRanks;
	}

	private boolean isSkillOrSpecialization(Skill skill, Object skillOrSpecialization) {
		boolean result = false;

		if(skill.equals(skillOrSpecialization)) {
			result = true;
		}
		else if(skillOrSpecialization instanceof Specialization &&
				skill.equals(((Specialization)skillOrSpecialization).getSkill())) {
			result = true;
		}

		return result;
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
		Character character = charactersFragment.getCurrentInstance();
		if (character.getProfession() != null &&
				character.getProfession().getAssignableSkillCostsMap().size() > 0) {
			int i = 0;
			for (final SkillCategory category :
					character.getProfession().getAssignableSkillCostsMap().keySet()) {
				final int index = i++;
				while (skillCostsListViews.length <= index) {
					addAssignableCostsRow();
				}
				skillCategories[index] = category;
				skillCostsListViews[index].setVisibility(View.VISIBLE);
				skillRxHandler.getSkillsForCategory(category)
						.subscribe(new Subscriber<Collection<Skill>>() {
							@Override
							public void onCompleted() {}
							@Override
							public void onError(Throwable e) {
								Log.e(TAG, "Exception caught getting Skill for category " + category.getName() + ".", e);
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
						&& character.getProfession().getAssignableSkillCostsMap().size() <= j
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
					if (costGroup != null && skillCosts.indexOf(costGroup) != -1) {
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
					if(charactersFragment.getCurrentInstance().getExperiencePoints() == 0) {
						ClipData dragData;

						ClipData.Item clipDataItem = new ClipData.Item(String.valueOf(position));
						dragData = new ClipData(DRAG_COST, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						List<View> dragViewList = new ArrayList<>(1);
						dragViewList.add(view);
						View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(dragViewList);

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
							view.startDragAndDrop(dragData, myShadow, position, 0);
						} else {
							//noinspection deprecation
							view.startDrag(dragData, myShadow, position, 0);
						}
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
					charactersFragment.saveItem();
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

	private short purchaseWithCultureRanks(SkillRanks skillRanks) {
		Character character = charactersFragment.getCurrentInstance();
		short purchasedCultureRanks = getPurchasedCultureRanks(skillRanks);
		short result = getCurrentRanks(skillRanks);

		if(skillRanks.getSpecialization() != null) {
			purchasedCultureRanks += (short)1;
			character.getPurchasedCultureRanks().put(skillRanks.getSpecialization(), purchasedCultureRanks);

			result += 1;
			character.getSpecializationRanks().put(skillRanks.getSpecialization(), result);
			charactersFragment.saveItem();
		}
		else if(skillRanks.getSkill() != null) {
			purchasedCultureRanks += (short)1;
			character.getPurchasedCultureRanks().put(skillRanks.getSkill(), purchasedCultureRanks);

			result += 1;
			character.getSkillRanks().put(skillRanks.getSkill(), result);
			charactersFragment.saveItem();
		}

		skillRanksAdapter.notifyDataSetChanged();

		return result;
	}

	private short sellCultureRank(SkillRanks skillRanks) {
		Character character = charactersFragment.getCurrentInstance();
		short purchasedRanks = getPurchasedCultureRanks(skillRanks);
		short result = getCurrentRanks(skillRanks);
		boolean changed = false;

		if(skillRanks.getSpecialization() != null && purchasedRanks > 0) {
			purchasedRanks -= (short)1;
			if(purchasedRanks > 0) {
				character.getPurchasedCultureRanks().put(skillRanks.getSpecialization(), purchasedRanks);
			}
			else {
				character.getPurchasedCultureRanks().remove(skillRanks.getSpecialization());
			}
			changed = true;

			if(result > 0) {
				result -= (short)1;
				if(result > 0) {
					character.getSpecializationRanks().put(skillRanks.getSpecialization(), result);
				}
				else {
					character.getSpecializationRanks().remove(skillRanks.getSpecialization());
				}
			}
		}
		else if(skillRanks.getSkill() != null) {
			purchasedRanks -= (short)1;
			if(purchasedRanks > 0) {
				character.getPurchasedCultureRanks().put(skillRanks.getSkill(), purchasedRanks);
			}
			else {
				character.getPurchasedCultureRanks().remove(skillRanks.getSkill());
			}
			changed = true;

			if(result > 0) {
				result -= (short)1;
				if(result > 0) {
					character.getSkillRanks().put(skillRanks.getSkill(), result);
				}
				else {
					character.getSkillRanks().remove(skillRanks.getSkill());
				}
			}
		}

		if(changed) {
			charactersFragment.saveItem();
		}
		skillRanksAdapter.notifyDataSetChanged();

		return purchasedRanks;
	}

	private short getRanksPurchasedThisLevel(SkillRanks skillRanks) {
		Character character = charactersFragment.getCurrentInstance();
		short purchasedThisLevel = 0;

		if(skillRanks.getSkill() != null) {
			Short tempShort = character.getCurrentLevelSkillRanks().get(skillRanks.getSkill());
			if(tempShort != null) {
				purchasedThisLevel = tempShort;
			}
		}
		else {
			Short tempShort = character.getCurrentLevelSpecializationRanks().get(skillRanks.getSpecialization());
			if(tempShort != null) {
				purchasedThisLevel = tempShort;
			}
		}

		return purchasedThisLevel;
	}

	private short getCurrentRanks(@NonNull SkillRanks skillRanks) {
		Character character = charactersFragment.getCurrentInstance();
		short purchasedRanks = 0;

		if(skillRanks.getSkill() != null) {
			Short ranks = character.getSkillRanks().get(skillRanks.getSkill());
			if (ranks != null) {
				purchasedRanks = ranks;
			}
		}
		else if(skillRanks.getSpecialization() != null) {
			Short ranks = character.getSpecializationRanks().get(skillRanks.getSpecialization());
			if (ranks != null) {
				purchasedRanks = ranks;
			}
		}

		return purchasedRanks;
	}

	private short getPurchasedCultureRanks(SkillRanks skillRanks) {
		short purchasedRanks = 0;
		Character character = charactersFragment.getCurrentInstance();
		Short tempShort = null;

		if(skillRanks.getSkill() != null) {
			tempShort = character.getPurchasedCultureRanks().get(skillRanks.getSkill());
		}
		else if(skillRanks.getSpecialization() != null) {
			tempShort = character.getPurchasedCultureRanks().get(skillRanks.getSpecialization());
		}

		if(tempShort != null) {
			purchasedRanks = tempShort;
		}

		return purchasedRanks;
	}
}

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
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Specialization;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.RacialStatBonus;
import com.madinnovations.rmu.data.entities.spells.SpellList;
import com.madinnovations.rmu.view.RMUDragShadowBuilder;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.common.SkillBonusListAdapter;
import com.madinnovations.rmu.view.adapters.creature.RacialStatBonusListAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Handles interactions with the UI for creature varieties.
 */
public class CreatureVarietyStatsAndSkillsPageFragment extends Fragment implements SkillBonusListAdapter.SetSkillBonus,
		RacialStatBonusListAdapter.SetRacialStatBonus {
	private static final String TAG = "CVMainPageFragment";
	private static final String DRAG_ADD_SKILL = "add-skill";
	private static final String DRAG_REMOVE_SKILL = "remove-skill";
	private static final String SKILL_TYPE_STRING = "skill";
	private static final String SPECIALIZATION_TYPE_STRING = "specialization";
	private static final String SPELL_LIST_TYPE_STRING = "spell-list";
	@Inject
	protected SkillRxHandler             skillRxHandler;
	private   RacialStatBonusListAdapter racialStatBonusListAdapter;
	private   ArrayAdapter<SkillBonus>   skillsListAdapter;
	private   SkillBonusListAdapter      skillBonusesListAdapter;
	private   ListView                   racialStatBonusList;
	private   ListView                   skillsList;
	private   ListView                   skillBonusesList;
	private   CreatureVarietiesFragment  varietiesFragment;
	private   StatBonusComparator        statBonusComparator = new StatBonusComparator();
	private   SkillBonusComparator       skillBonusComparator = new SkillBonusComparator();

	/**
	 * Creates a new CreatureVarietyStatsAndSkillsPageFragment instance.
	 *
	 * @param varietiesFragment  the CreatureVarietiesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureVarietyStatsAndSkillsPageFragment newInstance(CreatureVarietiesFragment varietiesFragment) {
		CreatureVarietyStatsAndSkillsPageFragment fragment = new CreatureVarietyStatsAndSkillsPageFragment();
		fragment.varietiesFragment = varietiesFragment;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_variety_stats_and_skills_page, container, false);

		initRacialStatBonusList(layout);
		initSkillsList(layout);
		initSkillBonusesList(layout);

		return layout;
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			varietiesFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		copyItemToViews();
	}

	@Override
	public void setSkillBonus(SkillBonus skillBonus) {
		boolean changed = false;

		if(varietiesFragment.getCurrentInstance().getSkillBonusesList().contains(skillBonus)) {
			int index = varietiesFragment.getCurrentInstance().getSkillBonusesList().indexOf(skillBonus);
			if(varietiesFragment.getCurrentInstance().getSkillBonusesList().get(index).getBonus() != skillBonus.getBonus()) {
				varietiesFragment.getCurrentInstance().getSkillBonusesList().add(skillBonus);
				changed = true;
			}
		}
		else {
			varietiesFragment.getCurrentInstance().getSkillBonusesList().add(skillBonus);
			changed = true;
		}
		if(changed) {
			varietiesFragment.saveItem();
		}
	}

	@SuppressWarnings("ConstantConditions")
	public boolean copyViewsToItem() {
		CreatureVariety creatureVariety = varietiesFragment.getCurrentInstance();
		boolean changed = false;
		SparseBooleanArray checkedItemPositions;
		RacialStatBonus racialStatBonus;
		Map<Statistic, Short> newStatBonusMap;
		List<SkillBonus> newSkillList;
		SkillBonus newSkillBonus;

		newSkillList = new ArrayList<>(skillBonusesListAdapter.getCount());
		for (int i = 0; i < skillBonusesListAdapter.getCount(); i++) {
			newSkillBonus = skillBonusesListAdapter.getItem(i);
			if(newSkillBonus != null) {
				if (creatureVariety.getSkillBonusesList().contains(newSkillBonus)) {
					int index = creatureVariety.getSkillBonusesList().indexOf(newSkillBonus);
					if (creatureVariety.getSkillBonusesList().get(index).getBonus() != newSkillBonus.getBonus()) {
						changed = true;
					}
					creatureVariety.getSkillBonusesList().remove(newSkillBonus);
				} else {
					changed = true;
				}
				newSkillList.add(newSkillBonus);
			}
		}
		if (!creatureVariety.getSkillBonusesList().isEmpty() && !newSkillList.isEmpty()) {
			changed = true;
		}
		creatureVariety.setSkillBonusesList(newSkillList);

		if(this.getView() != null) {
			checkedItemPositions = racialStatBonusList.getCheckedItemPositions();
			if (checkedItemPositions != null) {
				newStatBonusMap = new HashMap<>(checkedItemPositions.size());
				for (int i = 0; i < checkedItemPositions.size(); i++) {
					racialStatBonus = racialStatBonusListAdapter.getItem(checkedItemPositions.keyAt(i));
					if(racialStatBonus != null) {
						if (creatureVariety.getRacialStatBonuses().containsKey(racialStatBonus.getStat())) {
							if (!creatureVariety
									.getRacialStatBonuses()
									.get(racialStatBonus.getStat())
									.equals(racialStatBonus.getBonus())) {
								changed = true;
							}
							creatureVariety.getRacialStatBonuses().remove(racialStatBonus.getStat());
						} else {
							changed = true;
						}
						newStatBonusMap.put(racialStatBonus.getStat(), racialStatBonus.getBonus());
					}
				}
				if (!creatureVariety.getRacialStatBonuses().isEmpty() && !newStatBonusMap.isEmpty()) {
					changed = true;
				}
				creatureVariety.setRacialStatBonuses(newStatBonusMap);
			}
			else {
				creatureVariety.getRacialStatBonuses().clear();
			}
		}

		return changed;
	}

	public void copyItemToViews() {
		CreatureVariety creatureVariety = varietiesFragment.getCurrentInstance();

		racialStatBonusListAdapter.clear();
		for(Statistic statistic : Statistic.getAllStats()) {
			RacialStatBonus racialStatBonus = new RacialStatBonus(statistic, (short)0);
			Short bonus = creatureVariety.getRacialStatBonuses().get(statistic);
			if(bonus != null) {
				racialStatBonus.setBonus(bonus);
			}
			racialStatBonusListAdapter.add(racialStatBonus);
		}
		racialStatBonusListAdapter.sort(statBonusComparator);
		racialStatBonusListAdapter.notifyDataSetChanged();

		skillBonusesList.clearChoices();
		skillBonusesListAdapter.clear();
		for(SkillBonus skillBonus : creatureVariety.getSkillBonusesList()) {
			SkillBonus newSkillBonus = new SkillBonus(skillBonus.getSkill(), skillBonus.getSpecialization(),
													  skillBonus.getSpellList(), skillBonus.getBonus());
			skillBonusesListAdapter.add(newSkillBonus);
		}
		skillBonusesListAdapter.sort(skillBonusComparator);
		skillBonusesListAdapter.notifyDataSetChanged();
	}

	private void initRacialStatBonusList(View layout) {
		racialStatBonusList = (ListView) layout.findViewById(R.id.racial_stat_bonuses_list);
		racialStatBonusListAdapter = new RacialStatBonusListAdapter(this.getActivity(), this);
		racialStatBonusList.setAdapter(racialStatBonusListAdapter);

		for(Statistic statistic : Statistic.getAllStats()) {
			RacialStatBonus racialStatBonus = new RacialStatBonus(statistic, (short)0);
			Short bonus = varietiesFragment.getCurrentInstance().getRacialStatBonuses().get(statistic);
			if(bonus != null) {
				racialStatBonus.setBonus(bonus);
			}
			racialStatBonusListAdapter.add(racialStatBonus);
		}
		//noinspection unchecked
		racialStatBonusListAdapter.sort(statBonusComparator);
		racialStatBonusListAdapter.notifyDataSetChanged();
	}

	@Override
	public void setRacialStatBonus(RacialStatBonus racialStatBonus) {
		boolean changed = false;

		if(varietiesFragment.getCurrentInstance().getRacialStatBonuses().containsKey(racialStatBonus.getStat())) {
			if(varietiesFragment.getCurrentInstance().getRacialStatBonuses().get(racialStatBonus.getStat()) != racialStatBonus.getBonus()) {
				varietiesFragment.getCurrentInstance().getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
				changed = true;
			}
		}
		else {
			varietiesFragment.getCurrentInstance().getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
			changed = true;
		}
		if(changed) {
			varietiesFragment.saveItem();
		}
	}


	private void initSkillsList(View layout) {
		skillsList = (ListView) layout.findViewById(R.id.skills_list);
		skillsListAdapter = new ArrayAdapter<>(getActivity(), R.layout.single_field_row);
		skillsList.setAdapter(skillsListAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG,
							  "Exception caught loading all Skill instances in initSecondaryAttacksList", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						skillsListAdapter.clear();
						for(Skill skill : skills) {
							if(skill.isRequiresSpecialization()) {
								for(Specialization specialization : skill.getSpecializations()) {
									SkillBonus skillBonus = new SkillBonus();
									skillBonus.setSpecialization(specialization);
									skillsListAdapter.add(skillBonus);
								}
							}
							else {
								SkillBonus skillBonus = new SkillBonus();
								skillBonus.setSkill(skill);
								skillsListAdapter.add(skillBonus);
							}
						}
						//noinspection unchecked
						skillsList.clearChoices();
						skillsListAdapter.sort(skillBonusComparator);
						skillsListAdapter.notifyDataSetChanged();
					}
				});

		skillsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!skillsList.isItemChecked(position)) {
					skillsList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = skillsList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(skillsList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					if(checkedItems.valueAt(i)) {
						int currentPosition = checkedItems.keyAt(i);
						SkillBonus skillBonus = skillsListAdapter.getItem(currentPosition);
						if (skillBonus != null) {
							String typeString = null;
							String idString = null;
							if (skillBonus.getSkill() != null) {
								idString = String.valueOf(skillBonus.getSkill().getId());
								typeString = SKILL_TYPE_STRING;
							}
							else if (skillBonus.getSpecialization() != null) {
								idString = String.valueOf(skillBonus.getSpecialization().getId());
								typeString = SPECIALIZATION_TYPE_STRING;
							}
							else if (skillBonus.getSpellList() != null) {
								idString = String.valueOf(skillBonus.getSpellList().getId());
								typeString = SPELL_LIST_TYPE_STRING;
							}
							if (dragData == null) {
								dragData = new ClipData(DRAG_ADD_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
														new ClipData.Item(typeString));
								dragData.addItem(new ClipData.Item(idString));
							}
							else {
								dragData.addItem(new ClipData.Item(typeString));
								dragData.addItem(new ClipData.Item(idString));
							}
							checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillsList));
						}
					}
				}
				View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
				}
				return false;
			}
		});

		skillsList.setOnDragListener(new SkillDragListener());
	}

	private void initSkillBonusesList(View layout) {
		skillBonusesList = (ListView) layout.findViewById(R.id.skill_bonuses_list);
		skillBonusesListAdapter = new SkillBonusListAdapter(this.getActivity(), this, skillBonusesList);
		skillBonusesList.setAdapter(skillBonusesListAdapter);

		skillBonusesListAdapter.clear();
		for(SkillBonus skillBonus : varietiesFragment.getCurrentInstance().getSkillBonusesList()) {
			skillBonusesListAdapter.add(new SkillBonus(skillBonus.getSkill(), skillBonus.getSpecialization(),
													   skillBonus.getSpellList(), skillBonus.getBonus()));
		}
		skillBonusesListAdapter.sort(skillBonusComparator);
		skillBonusesListAdapter.notifyDataSetChanged();

		skillBonusesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!skillBonusesList.isItemChecked(position)) {
					skillBonusesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = skillBonusesList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(skillBonusesList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					SkillBonus skillBonus = skillBonusesListAdapter.getItem(currentPosition);
					if (checkedItems.valueAt(i) && skillBonus != null) {
						String typeString = null;
						String idString = null;
						if(skillBonus.getSkill() != null) {
							typeString = SKILL_TYPE_STRING;
							idString = String.valueOf(skillBonus.getSkill().getId());
						}
						else if(skillBonus.getSpecialization() != null) {
							typeString = SPECIALIZATION_TYPE_STRING;
							idString = String.valueOf(skillBonus.getSpecialization().getId());
						}
						else if(skillBonus.getSpellList() != null) {
							typeString = SPELL_LIST_TYPE_STRING;
							idString = String.valueOf(skillBonus.getSpellList().getId());
						}
						if (dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
													new ClipData.Item(typeString));
							dragData.addItem(new ClipData.Item(idString));
						}
						else {
							dragData.addItem(new ClipData.Item(typeString));
							dragData.addItem(new ClipData.Item(idString));
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillBonusesList));
					}
				}
				View.DragShadowBuilder myShadow = new RMUDragShadowBuilder(checkedViews);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					view.startDragAndDrop(dragData, myShadow, null, 0);
				}
				else {
					//noinspection deprecation
					view.startDrag(dragData, myShadow, null, 0);
				}
				return false;
			}
		});

		skillBonusesList.setOnDragListener(new SkillBonusDragListener());
	}

	private View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	@SuppressWarnings("unused")
	public void setVarietiesFragment(CreatureVarietiesFragment varietiesFragment) {
		this.varietiesFragment = varietiesFragment;
	}

	protected class SkillBonusDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape  = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = skillBonusesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(hoverShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_SKILL.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i += 2) {
							String type = event.getClipData().getItemAt(i).getText().toString();
							int id = Integer.valueOf(event.getClipData().getItemAt(i+1).getText().toString());
							SkillBonus skillBonus = new SkillBonus();
							if(SKILL_TYPE_STRING.equals(type)) {
								skillBonus.setSkill(new Skill(id));
							}
							else if (SPECIALIZATION_TYPE_STRING.equals(type)) {
								skillBonus.setSpecialization(new Specialization(id));
							}
							else if (SPELL_LIST_TYPE_STRING.equals(type)) {
								skillBonus.setSpellList(new SpellList(id));
							}
							int position = skillsListAdapter.getPosition(skillBonus);
							if(position != AdapterView.INVALID_POSITION) {
								skillBonus = skillsListAdapter.getItem(position);
								if (skillBonusesListAdapter.getPosition(skillBonus) == AdapterView.INVALID_POSITION) {
									skillBonusesListAdapter.add(skillBonus);
									varietiesFragment.getCurrentInstance().getSkillBonusesList().add(new SkillBonus(skillBonus));
									changed = true;
								}
							}
						}
						if(changed) {
							varietiesFragment.saveItem();
							skillBonusesListAdapter.sort(skillBonusComparator);
							skillBonusesListAdapter.notifyDataSetChanged();
						}
						v.setBackground(normalShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackground(normalShape);
					v.invalidate();
					break;
			}

			return true;
		}
	}

	protected class SkillDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = skillsList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(hoverShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_LOCATION:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_SKILL.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i+=2) {
							// We just send skill ID but since that is the only field used in the Skill.equals method and skill is the
							// only field used in the SkillBonus.equals method we can create a temporary Skill and set its id field then
							// create a new SkillBonus and set its skill field then use the new SkillBonus to find the position of the
							// complete SkillBonus instance in the adapter
							String type = event.getClipData().getItemAt(i).getText().toString();
							int id = Integer.valueOf(event.getClipData().getItemAt(i+1).getText().toString());
							SkillBonus skillBonus = new SkillBonus();
							if(SKILL_TYPE_STRING.equals(type)) {
								skillBonus.setSkill(new Skill(id));
							}
							else if (SPECIALIZATION_TYPE_STRING.equals(type)) {
								skillBonus.setSpecialization(new Specialization(id));
							}
							else if (SPELL_LIST_TYPE_STRING.equals(type)) {
								skillBonus.setSpellList(new SpellList(id));
							}
							int position = skillBonusesListAdapter.getPosition(skillBonus);
							if(position != AdapterView.INVALID_POSITION) {
								varietiesFragment.getCurrentInstance().getSkillBonusesList().remove(skillBonus);
								skillBonusesListAdapter.remove(skillBonus);
							}
						}
						varietiesFragment.saveItem();
						skillBonusesList.clearChoices();
						skillBonusesListAdapter.sort(skillBonusComparator);
						skillBonusesListAdapter.notifyDataSetChanged();
						v.setBackground(normalShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					v.setBackground(normalShape);
					v.invalidate();
					break;
			}
			return true;
		}
	}

	private class StatBonusComparator implements Comparator<RacialStatBonus> {
		@Override
		public int compare(RacialStatBonus o1, RacialStatBonus o2) {
			if(o1.getStat() == null) {
				if(o2.getStat() == null) {
					return 0;
				}
				else {
					return 1;
				}
			}
			else if (o2.getStat() == null){
				return  -1;
			}
			else {
				return o1.getStat().getAbbreviation().compareTo(o2.getStat().getAbbreviation());
			}
		}
	}

	private class SkillBonusComparator implements Comparator<SkillBonus> {
		@Override
		public int compare(SkillBonus o1, SkillBonus o2) {
			String name1 = null;
			String name2 = null;

			if(o1.getSkill() != null) {
				name1 = o1.getSkill().getName();
			}
			else if(o1.getSpecialization() != null) {
				name1 = o1.getSpecialization().getName();
			}
			else if(o1.getSpellList() != null) {
				name1 = o1.getSpellList().getName();
			}

			if(o2.getSkill() != null) {
				name2 = o2.getSkill().getName();
			}
			else if(o2.getSpecialization() != null) {
				name2 = o2.getSpecialization().getName();
			}
			else if(o2.getSpellList() != null) {
				name2 = o2.getSpellList().getName();
			}

			if(name1 == null) {
				if(name2 == null) {
					return 0;
				}
				else {
					return 1;
				}
			}
			else if (name2 == null){
				return  -1;
			}
			else {
				return name1.compareTo(name2);
			}
		}
	}
}
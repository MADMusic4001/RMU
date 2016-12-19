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
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetype;
import com.madinnovations.rmu.data.entities.creature.CreatureArchetypeLevel;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.creature.CreatureArchetypeLevelAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Handles interactions with the UI for creature archetypes.
 */
public class CreatureArchetypesLevelsFragment extends Fragment
		implements CreatureArchetypeLevelAdapter.CreatureArchetypeLevelCallbacks {
	@SuppressWarnings("unused")
	private static final String TAG = "CreatureArchetypesLevel";
	private CreatureArchetypesFragment    creatureArchetypesFragment;
	private CreatureArchetypeLevelAdapter levelsAdapter;
	private Set<Short>                    selectedLevels = new TreeSet<>();

	/**
	 * Creates a new CreatureArchetypesLevelsFragment instance.
	 *
	 * @param creatureArchetypesFragment  the CreatureArchetypesFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CreatureArchetypesLevelsFragment newInstance(CreatureArchetypesFragment creatureArchetypesFragment) {
		CreatureArchetypesLevelsFragment fragment = new CreatureArchetypesLevelsFragment();
		fragment.creatureArchetypesFragment = creatureArchetypesFragment;
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_archetypes_levels_page, container, false);

		initStatLabels(layout);
		initRangeButtons(layout);
		initLevelsList(layout);
		setHasOptionsMenu(true);

		return layout;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.creature_archetype_fill_context_menu, menu);
		MenuItem menuItem = menu.findItem(R.id.context_fill_range);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_double);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_by_twos);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_by_threes);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_by_fours);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_by_fives);
		menuItem.setActionView(v);
		menuItem = menu.findItem(R.id.context_fill_range_by_sixes);
		menuItem.setActionView(v);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean consumed = false;

		if(item.getActionView() != null) {
			int actionViewId = item.getActionView().getId();
			switch (item.getItemId()) {
				case R.id.context_fill_range:
					fillRangeByMultiples(actionViewId, (short)1);
					consumed = true;
					break;
				case R.id.context_fill_range_double:
					fillRangeDouble(actionViewId);
					consumed = true;
					break;
				case R.id.context_fill_range_by_twos:
					fillRangeByMultiples(actionViewId, (short)2);
					consumed = true;
					break;
				case R.id.context_fill_range_by_threes:
					fillRangeByMultiples(actionViewId, (short)3);
					consumed = true;
					break;
				case R.id.context_fill_range_by_fours:
					fillRangeByMultiples(actionViewId, (short)4);
					consumed = true;
					break;
				case R.id.context_fill_range_by_fives:
					fillRangeByMultiples(actionViewId, (short)5);
					consumed = true;
					break;
				case R.id.context_fill_range_by_sixes:
					fillRangeByMultiples(actionViewId, (short)6);
					consumed = true;
					break;
			}
		}

		return consumed || super.onContextItemSelected(item);
	}

	@Override
	public void onPause() {
		if(copyViewsToItem()) {
			creatureArchetypesFragment.saveItem();
		}
		super.onPause();
	}

	@Override
	public void save() {
		creatureArchetypesFragment.saveItem();
	}

	@Override
	public void toggleSelection(short level, boolean selected) {
		Short newLevel = level;
		if(selected && !selectedLevels.contains(newLevel)) {
			selectedLevels.add(newLevel);
		}
		else {
			selectedLevels.remove(newLevel);
		}
	}

	@Override
	public boolean isSelected(short level) {
		return selectedLevels.contains(level);
	}

	public boolean copyViewsToItem() {
		return false;
	}

	public void copyItemToViews() {
		setLevels();
	}

	private void initStatLabels(View layout) {
		TextView label;

		label = (TextView) layout.findViewById(R.id.agility_label);
		label.setText(Statistic.AGILITY.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.constitution_label);
		label.setText(Statistic.CONSTITUTION.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.constitution_stat_label);
		label.setText(String.format(getString(R.string.stat_literal_string), Statistic.CONSTITUTION.getAbbreviation()));

		label = (TextView)layout.findViewById(R.id.empathy_label);
		label.setText(Statistic.EMPATHY.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.intuition_label);
		label.setText(Statistic.INTUITION.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.memory_label);
		label.setText(Statistic.MEMORY.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.presence_label);
		label.setText(Statistic.PRESENCE.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.reasoning_label);
		label.setText(Statistic.REASONING.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.quickness_label);
		label.setText(Statistic.QUICKNESS.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.self_discipline_label);
		label.setText(Statistic.SELF_DISCIPLINE.getAbbreviation());

		label = (TextView)layout.findViewById(R.id.strength_label);
		label.setText(Statistic.STRENGTH.getAbbreviation());
	}

	private void initRangeButtons(View layout) {
		ImageButton button = (ImageButton)layout.findViewById(R.id.attack_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.attack_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.attack_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.attack2_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.attack2_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.attack2_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.db_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.db_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.db_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.body_dev_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.body_dev_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.body_dev_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.prime_skill_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.prime_skill_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.prime_skill_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.secondary_skill_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.secondary_skill_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.secondary_skill_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.power_dev_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.power_dev_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.power_dev_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.spells_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.spells_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.spells_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.talent_dp_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.talent_dp_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.talent_dp_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.agility_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.agility_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.agility_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.constitution_stat_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.constitution_stat_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.constitution_stat_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.constitution_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.constitution_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.constitution_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.empathy_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.empathy_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.empathy_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.intuition_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.intuition_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.intuition_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.memory_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.memory_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.memory_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.presence_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.presence_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.presence_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.quickness_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.quickness_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.quickness_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.reasoning_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.reasoning_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.reasoning_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.self_discipline_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.self_discipline_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.self_discipline_edit);
			}
		});

		button = (ImageButton)layout.findViewById(R.id.strength_fill_range_button);
		registerForContextMenu(layout.findViewById(R.id.strength_layout));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fillRange(R.id.strength_edit);
			}
		});
	}

	private void fillRange(@IdRes int fieldId) {
		boolean changed = false;
		Iterator<Short> iterator = selectedLevels.iterator();
		short startLevel = 1;
		if(iterator.hasNext()) {
			startLevel = iterator.next();
		}
		while(iterator.hasNext()) {
			short endLevel = iterator.next();
			short startValue = getValueForLevel(startLevel, fieldId);
			short endValue = getValueForLevel(endLevel, fieldId);
			float diff = (float)(endValue - startValue)/(float)(endLevel - startLevel);
			float currentValue = startValue;
			for(short j = (short)(startLevel + 1); j < endLevel; j++) {
				currentValue += diff;
				changed |= setValueForLevel(j, fieldId, (short)Math.round(currentValue));
			}
			startLevel = endLevel;
		}
		if(changed) {
			levelsAdapter.notifyDataSetChanged();
			creatureArchetypesFragment.saveItem();
		}
	}

	private void fillRangeDouble(@IdRes int fieldId) {
		boolean changed = false;
		Iterator<Short> iterator = selectedLevels.iterator();
		short startLevel = 1;
		if(iterator.hasNext()) {
			startLevel = iterator.next();
		}
		while(iterator.hasNext()) {
			short endLevel = iterator.next();
			short startValue = getValueForLevel(startLevel, fieldId);
			short endValue = getValueForLevel(endLevel, fieldId);
			float diff = (float)(endValue - startValue)/(float)((endLevel - startLevel)/2);
			float currentValue = startValue;
			int count = 0;
			for(short j = (short)(startLevel + 1); j < endLevel; j++) {
				count++;
				if((count % 2) == 0) {
					currentValue += diff;
				}
				changed |= setValueForLevel(j, fieldId, (short)Math.round(currentValue));
			}
			startLevel = endLevel;
		}
		if(changed) {
			levelsAdapter.notifyDataSetChanged();
			creatureArchetypesFragment.saveItem();
		}
	}

	private void fillRangeByMultiples(@IdRes int fieldId, short multiple) {
		boolean changed = false;
		Iterator<Short> iterator = selectedLevels.iterator();
		short startLevel = 1;
		if(iterator.hasNext()) {
			startLevel = iterator.next();
		}
		while(iterator.hasNext()) {
			short endLevel = iterator.next();
			short startValue = getValueForLevel(startLevel, fieldId);
			short endValue = getValueForLevel(endLevel, fieldId);
			short levels = (short)(endLevel - startLevel);
			float diff = (float)(endValue - startValue);
			float divisions = diff/multiple;
			float divisionsDiff = (levels - divisions)/levels;
			short currentValue = startValue;
			float diffAccumulator = 0;
			int duplicates = 1;
			int skips = -1;
			for(short j = (short)(startLevel + 1); j < endLevel; j++) {
				diffAccumulator += divisionsDiff;
				while (diffAccumulator <= skips) {
					currentValue += multiple;
					skips--;
				}
				if(diffAccumulator >= duplicates) {
					duplicates++;
					changed |= setValueForLevel(j, fieldId, currentValue);
				}
				else {
					currentValue += multiple;
					changed |= setValueForLevel(j, fieldId, (short) Math.round(currentValue));
				}
			}
			startLevel = endLevel;
		}
		if(changed) {
			levelsAdapter.notifyDataSetChanged();
			creatureArchetypesFragment.saveItem();
		}
	}

	private short getValueForLevel(short level, @IdRes int fieldId) {
		CreatureArchetypeLevel currentInstance = creatureArchetypesFragment.getCurrentInstance().getLevels().get((int)level);
		short result = 0;

		switch (fieldId) {
			case R.id.attack_edit:
			case R.id.attack_layout:
				result = currentInstance.getAttack();
				break;
			case R.id.attack2_edit:
			case R.id.attack2_layout:
				result = currentInstance.getAttack2();
				break;
			case R.id.db_edit:
			case R.id.db_layout:
				result = currentInstance.getDefensiveBonus();
				break;
			case R.id.body_dev_edit:
			case R.id.body_dev_layout:
				result = currentInstance.getBodyDevelopment();
				break;
			case R.id.prime_skill_edit:
			case R.id.prime_skill_layout:
				result = currentInstance.getPrimeSkill();
				break;
			case R.id.secondary_skill_edit:
			case R.id.secondary_skill_layout:
				result = currentInstance.getSecondarySkill();
				break;
			case R.id.power_dev_edit:
			case R.id.power_dev_layout:
				result = currentInstance.getPowerDevelopment();
				break;
			case R.id.spells_edit:
			case R.id.spells_layout:
				result = currentInstance.getSpells();
				break;
			case R.id.talent_dp_edit:
			case R.id.talent_dp_layout:
				result = currentInstance.getTalentDP();
				break;
			case R.id.agility_edit:
			case R.id.agility_layout:
				result = currentInstance.getAgility();
				break;
			case R.id.constitution_stat_edit:
			case R.id.constitution_stat_layout:
				result = currentInstance.getConstitutionStat();
				break;
			case R.id.constitution_edit:
			case R.id.constitution_layout:
				result = currentInstance.getConstitution();
				break;
			case R.id.empathy_edit:
			case R.id.empathy_layout:
				result = currentInstance.getEmpathy();
				break;
			case R.id.intuition_edit:
			case R.id.intuition_layout:
				result = currentInstance.getIntuition();
				break;
			case R.id.memory_edit:
			case R.id.memory_layout:
				result = currentInstance.getMemory();
				break;
			case R.id.presence_edit:
			case R.id.presence_layout:
				result = currentInstance.getPresence();
				break;
			case R.id.quickness_edit:
			case R.id.quickness_layout:
				result = currentInstance.getQuickness();
				break;
			case R.id.reasoning_edit:
			case R.id.reasoning_layout:
				result = currentInstance.getReasoning();
				break;
			case R.id.self_discipline_edit:
			case R.id.self_discipline_layout:
				result = currentInstance.getSelfDiscipline();
				break;
			case R.id.strength_edit:
			case R.id.strength_layout:
				result = currentInstance.getStrength();
				break;
		}

		return result;
	}

	private boolean setValueForLevel(short level, @IdRes int fieldId, short newValue) {
		CreatureArchetypeLevel currentInstance = creatureArchetypesFragment.getCurrentInstance().getLevels().get((int)level);
		boolean changed = false;

		switch (fieldId) {
			case R.id.attack_edit:
			case R.id.attack_layout:
				if(newValue != currentInstance.getAttack()) {
					currentInstance.setAttack(newValue);
					changed = true;
				}
				break;
			case R.id.attack2_edit:
			case R.id.attack2_layout:
				if(newValue != currentInstance.getAttack2()) {
					currentInstance.setAttack2(newValue);
					changed = true;
				}
				break;
			case R.id.db_edit:
			case R.id.db_layout:
				if(newValue != currentInstance.getDefensiveBonus()) {
					currentInstance.setDefensiveBonus(newValue);
					changed = true;
				}
				break;
			case R.id.body_dev_edit:
			case R.id.body_dev_layout:
				if(newValue != currentInstance.getBodyDevelopment()) {
					currentInstance.setBodyDevelopment(newValue);
					changed = true;
				}
				break;
			case R.id.prime_skill_edit:
			case R.id.prime_skill_layout:
				if(newValue != currentInstance.getPrimeSkill()) {
					currentInstance.setPrimeSkill(newValue);
					changed = true;
				}
				break;
			case R.id.secondary_skill_edit:
			case R.id.secondary_skill_layout:
				if(newValue != currentInstance.getSecondarySkill()) {
					currentInstance.setSecondarySkill(newValue);
					changed = true;
				}
				break;
			case R.id.power_dev_edit:
			case R.id.power_dev_layout:
				if(newValue != currentInstance.getPowerDevelopment()) {
					currentInstance.setPowerDevelopment(newValue);
					changed = true;
				}
				break;
			case R.id.spells_edit:
			case R.id.spells_layout:
				if(newValue != currentInstance.getSpells()) {
					currentInstance.setSpells(newValue);
					changed = true;
				}
				break;
			case R.id.talent_dp_edit:
			case R.id.talent_dp_layout:
				if(newValue != currentInstance.getTalentDP()) {
					currentInstance.setTalentDP(newValue);
					changed = true;
				}
				break;
			case R.id.agility_edit:
			case R.id.agility_layout:
				if(newValue != currentInstance.getAgility()) {
					currentInstance.setAgility(newValue);
					changed = true;
				}
				break;
			case R.id.constitution_stat_edit:
			case R.id.constitution_stat_layout:
				if(newValue != currentInstance.getConstitutionStat()) {
					currentInstance.setConstitutionStat(newValue);
					changed = true;
				}
				break;
			case R.id.constitution_edit:
			case R.id.constitution_layout:
				if(newValue != currentInstance.getConstitution()) {
					currentInstance.setConstitution(newValue);
					changed = true;
				}
				break;
			case R.id.empathy_edit:
			case R.id.empathy_layout:
				if(newValue != currentInstance.getEmpathy()) {
					currentInstance.setEmpathy(newValue);
					changed = true;
				}
				break;
			case R.id.intuition_edit:
			case R.id.intuition_layout:
				if(newValue != currentInstance.getIntuition()) {
					currentInstance.setIntuition(newValue);
					changed = true;
				}
				break;
			case R.id.memory_edit:
			case R.id.memory_layout:
				if(newValue != currentInstance.getMemory()) {
					currentInstance.setMemory(newValue);
					changed = true;
				}
				break;
			case R.id.presence_edit:
			case R.id.presence_layout:
				if(newValue != currentInstance.getPresence()) {
					currentInstance.setPresence(newValue);
					changed = true;
				}
				break;
			case R.id.quickness_edit:
			case R.id.quickness_layout:
				if(newValue != currentInstance.getQuickness()) {
					currentInstance.setQuickness(newValue);
					changed = true;
				}
				break;
			case R.id.reasoning_edit:
			case R.id.reasoning_layout:
				if(newValue != currentInstance.getReasoning()) {
					currentInstance.setReasoning(newValue);
					changed = true;
				}
				break;
			case R.id.self_discipline_edit:
			case R.id.self_discipline_layout:
				if(newValue != currentInstance.getSelfDiscipline()) {
					currentInstance.setSelfDiscipline(newValue);
					changed = true;
				}
				break;
			case R.id.strength_edit:
			case R.id.strength_layout:
				if(newValue != currentInstance.getStrength()) {
					currentInstance.setStrength(newValue);
					changed = true;
				}
				break;
		}

		return changed;
	}

	private void initLevelsList(View layout) {
		ListView levelsListView = (ListView) layout.findViewById(R.id.levels_list_view);
		levelsAdapter = new CreatureArchetypeLevelAdapter(getActivity(), this);
		levelsListView.setAdapter(levelsAdapter);
		setLevels();
	}

	private void setLevels() {
		CreatureArchetype creatureArchetype = creatureArchetypesFragment.getCurrentInstance();

		levelsAdapter.clear();
		if(creatureArchetype.getLevels().size() == 0) {
			creatureArchetype.generateLevels();
			if(!creatureArchetypesFragment.isNew()) {
				creatureArchetypesFragment.saveItem();
			}
		}
		for(int i = 0; i < creatureArchetype.getLevels().size(); i++) {
			levelsAdapter.add(creatureArchetype.getLevels().valueAt(i));
		}
		levelsAdapter.notifyDataSetChanged();
	}
}

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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalCodeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.StatRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.OutlookRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.combat.Attack;
import com.madinnovations.rmu.data.entities.combat.AttackBonus;
import com.madinnovations.rmu.data.entities.combat.CriticalCode;
import com.madinnovations.rmu.data.entities.common.Size;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.common.Stat;
import com.madinnovations.rmu.data.entities.common.Talent;
import com.madinnovations.rmu.data.entities.common.TalentTier;
import com.madinnovations.rmu.data.entities.creature.CreatureLevelSpreadTable;
import com.madinnovations.rmu.data.entities.creature.CreatureType;
import com.madinnovations.rmu.data.entities.creature.CreatureVariety;
import com.madinnovations.rmu.data.entities.creature.Outlook;
import com.madinnovations.rmu.data.entities.creature.RacialStatBonus;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.adapters.TwoFieldListAdapter;
import com.madinnovations.rmu.view.adapters.combat.AttackBonusListAdapter;
import com.madinnovations.rmu.view.adapters.combat.AttacksAdapter;
import com.madinnovations.rmu.view.adapters.combat.CriticalCodesListAdapter;
import com.madinnovations.rmu.view.adapters.common.SizeSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillBonusListAdapter;
import com.madinnovations.rmu.view.adapters.common.SkillSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentNamesListAdapter;
import com.madinnovations.rmu.view.adapters.common.TalentTierListAdapter;
import com.madinnovations.rmu.view.adapters.creature.CreatureTypeSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.LevelSpreadSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.OutlookSpinnerAdapter;
import com.madinnovations.rmu.view.adapters.creature.RacialStatBonusListAdapter;
import com.madinnovations.rmu.view.adapters.spell.RealmSpinnerAdapter;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
public class CreatureVarietiesFragment extends Fragment implements TwoFieldListAdapter.GetValues<CreatureVariety>,
		RacialStatBonusListAdapter.SetRacialStatBonus, TalentTierListAdapter.SetTalentTier, AttackBonusListAdapter.SetAttackBonus,
		SkillBonusListAdapter.SetSkillBonus {
	private static final String DRAG_ADD_TALENT = "add-talent";
	private static final String DRAG_REMOVE_TALENT = "remove-talent";
	private static final String DRAG_ADD_ATTACK = "add-attack";
	private static final String DRAG_REMOVE_ATTACK = "remove-attack";
	private static final String DRAG_ADD_SKILL = "add-skill";
	private static final String DRAG_REMOVE_SKILL = "remove-skill";
	@Inject
	protected AttackRxHandler            attackRxHandler;
	@Inject
	protected CreatureVarietyRxHandler   creatureVarietyRxHandler;
	@Inject
	protected CreatureTypeRxHandler      creatureTypeRxHandler;
	@Inject
	protected CriticalCodeRxHandler      criticalCodeRxHandler;
	@Inject
	protected OutlookRxHandler           outlookRxHandler;
	@Inject
	protected RealmRxHandler             realmRxHandler;
	@Inject
	protected SizeRxHandler              sizeRxHandler;
	@Inject
	protected SkillRxHandler             skillRxHandler;
	@Inject
	protected StatRxHandler              statRxHandler;
	@Inject
	protected TalentRxHandler            talentRxHandler;
	@Inject
	protected CreatureTypeSpinnerAdapter creatureTypeSpinnerAdapter;
	@Inject
	protected SizeSpinnerAdapter         sizeSpinnerAdapter;
	@Inject
	protected LevelSpreadSpinnerAdapter  levelSpreadSpinnerAdapter;
	@Inject
	protected TalentNamesListAdapter     talentNamesListAdapter;
	@Inject
	protected CriticalCodesListAdapter   criticalCodesListAdapter;
	@Inject
	protected RealmSpinnerAdapter        realm1SpinnerAdapter;
	@Inject
	protected RealmSpinnerAdapter        realm2SpinnerAdapter;
	@Inject
	protected OutlookSpinnerAdapter      outlookSpinnerAdapter;
	@Inject
	protected AttacksAdapter             attacksListAdapter;
	@Inject
	protected SkillSpinnerAdapter        skillsListAdapter;
	protected AttackBonusListAdapter attackBonusesListAdapter;
	protected SkillBonusListAdapter skillBonusesListAdapter;
	protected RacialStatBonusListAdapter racialStatBonusListAdapter;
	protected TalentTierListAdapter      talentTiersListAdapter;
	private   TwoFieldListAdapter<CreatureVariety> listAdapter;
	private   ListView                    listView;
	private   EditText                    nameEdit;
	private   EditText                    descriptionEdit;
	private   Spinner                     creatureTypeSpinner;
	private   EditText                    typicalLevelEdit;
	private   Spinner                     levelSpreadSpinner;
	private   Spinner                     sizeSpinner;
	private   EditText                    heightEdit;
	private   EditText                    lengthEdit;
	private   EditText                    weightEdit;
	private   EditText                    healingRateEdit;
	private   EditText                    baseHitsEdit;
	private   EditText                    baseEnduranceEdit;
	private   EditText                    armorTypeEdit;
	private   EditText                    channelingRREdit;
	private   EditText                    essenceRREdit;
	private   EditText                    mentalismRREdit;
	private   EditText                    physicalRREdit;
	private   EditText                    fearRREdit;
	private   Spinner                     realm1Spinner;
	private   Spinner                     realm2Spinner;
	private   Spinner                     outlookSpinner;
	private   ListView                    attacksList;
	private   ListView                    attackBonusesList;
	private   EditText                    attackSequenceEdit;
	private   ListView                    skillsList;
	private   ListView                    skillBonusesList;
	private   ListView                    racialStatBonusList;
	private   ListView                    talentNamesList;
	private   ListView                    talentTiersList;
	private   ListView                    criticalCodesList;
	private CreatureVariety currentInstance = new CreatureVariety();
	private boolean         isNew           = true;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		((CampaignActivity)getActivity()).getActivityComponent().
				newCreatureFragmentComponent(new CreatureFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.creature_varieties_fragment, container, false);

		((TextView)layout.findViewById(R.id.header_field1)).setText(getString(R.string.label_creature_variety_name));
		((TextView)layout.findViewById(R.id.header_field2)).setText(getString(R.string.label_creature_variety_description));

		initNameEdit(layout);
		initDescriptionEdit(layout);
		initCretureTypeSpinner(layout);
		initTypicalLevelEdit(layout);
		initLevelSpreadSpinner(layout);
		initSizeSpinner(layout);
		initHeightEdit(layout);
		initLengthEdit(layout);
		initWeightEdit(layout);
		initHealingRateEdit(layout);
		initBaseHitsEdit(layout);
		initBaseEnduranceEdit(layout);
		initArmorTypeEdit(layout);
		initChannelingRREdit(layout);
		initEssenceRREdit(layout);
		initMentalismRREdit(layout);
		initPhysicalRREdit(layout);
		initFearRREdit(layout);
		initRealm1Spinner(layout);
		initRealm2Spinner(layout);
		initOutlookSpinner(layout);
		initAttacksList(layout);
		initAttackBonusesList(layout);
		initAttackSequenceEdit(layout);
		initSkillsList(layout);
		initSkillBonusesList(layout);
		initRacialStatBonusList(layout);
		initTalentNamesList(layout);
		initTalentTiersList(layout);
		initCriticalCodesList(layout);
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
		String newString;
		int position;
		CreatureType newType;
		Size newSize;
		short newShort;
		char newLevelSpread;
		SparseBooleanArray checkedItemPositions;
		RacialStatBonus racialStatBonus;
		Map<Stat, Short> newStatBonusMap;
		Map<Talent, Short> newTalentTiersMap;
		List<CriticalCode> newCriticalCodesList;
		CriticalCode newCriticalCode;
		TalentTier newTalentTier;
		Realm newRealm;
		Outlook newOutlook;
		Map<Attack, Short> newAttackMap;
		AttackBonus newAttackBonus;
		Map<Skill, Short> newSkillMap;
		SkillBonus newSkillBonus;

		newString = nameEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getName() != null) ||
				(newString != null && !newString.equals(currentInstance.getName()))) {
			currentInstance.setName(newString);
			changed = true;
		}

		newString = descriptionEdit.getText().toString();
		if(newString.isEmpty()) {
			newString = null;
		}
		if((newString == null && currentInstance.getDescription() != null) ||
				(newString != null && !newString.equals(currentInstance.getDescription()))) {
			currentInstance.setDescription(newString);
			changed = true;
		}

		position = creatureTypeSpinner.getSelectedItemPosition();
		if(position != -1) {
			newType = creatureTypeSpinnerAdapter.getItem(position);
			if(!newType.equals(currentInstance.getType())) {
				currentInstance.setType(newType);
				changed = true;
			}
		}

		if(typicalLevelEdit.length() > 0) {
			newShort = Short.valueOf(typicalLevelEdit.getText().toString());
			if(currentInstance.getTypicalLevel() != newShort) {
				currentInstance.setTypicalLevel(newShort);
				changed = true;
			}
		}

		position = levelSpreadSpinner.getSelectedItemPosition();
		if(position != -1) {
			newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
			if(newLevelSpread != currentInstance.getLevelSpread()) {
				currentInstance.setLevelSpread(newLevelSpread);
				changed = true;
			}
		}

		position = sizeSpinner.getSelectedItemPosition();
		if(position != -1) {
			newSize = sizeSpinnerAdapter.getItem(position);
			if(!newSize.equals(currentInstance.getSize())) {
				currentInstance.setSize(newSize);
				changed = true;
			}
		}

		if(heightEdit.length() > 0) {
			newShort = Short.valueOf(heightEdit.getText().toString());
			if(currentInstance.getHeight() != newShort) {
				currentInstance.setHeight(newShort);
				changed = true;
			}
		}

		if(lengthEdit.length() > 0) {
			newShort = Short.valueOf(lengthEdit.getText().toString());
			if(currentInstance.getLength() != newShort) {
				currentInstance.setLength(newShort);
				changed = true;
			}
		}

		if(weightEdit.length() > 0) {
			newShort = Short.valueOf(weightEdit.getText().toString());
			if(currentInstance.getWeight() != newShort) {
				currentInstance.setWeight(newShort);
				changed = true;
			}
		}

		if(healingRateEdit.length() > 0) {
			newShort = Short.valueOf(healingRateEdit.getText().toString());
			if(currentInstance.getHealingRate() != newShort) {
				currentInstance.setHealingRate(newShort);
				changed = true;
			}
		}

		if(baseHitsEdit.length() > 0) {
			newShort = Short.valueOf(baseHitsEdit.getText().toString());
			if(currentInstance.getBaseHits() != newShort) {
				currentInstance.setBaseHits(newShort);
				changed = true;
			}
		}

		if(baseEnduranceEdit.length() > 0) {
			newShort = Short.valueOf(baseEnduranceEdit.getText().toString());
			if(currentInstance.getBaseEndurance() != newShort) {
				currentInstance.setBaseEndurance(newShort);
				changed = true;
			}
		}

		if(armorTypeEdit.length() > 0) {
			newShort = Short.valueOf(armorTypeEdit.getText().toString());
			if(currentInstance.getArmorType() != newShort) {
				currentInstance.setArmorType(newShort);
				changed = true;
			}
		}

		if(channelingRREdit.length() > 0) {
			newShort = Short.valueOf(channelingRREdit.getText().toString());
			if(currentInstance.getBaseChannellingRR() != newShort) {
				currentInstance.setBaseChannellingRR(newShort);
				changed = true;
			}
		}

		if(essenceRREdit.length() > 0) {
			newShort = Short.valueOf(essenceRREdit.getText().toString());
			if(currentInstance.getBaseEssenceRR() != newShort) {
				currentInstance.setBaseEssenceRR(newShort);
				changed = true;
			}
		}

		if(mentalismRREdit.length() > 0) {
			newShort = Short.valueOf(mentalismRREdit.getText().toString());
			if(currentInstance.getBaseMentalismRR() != newShort) {
				currentInstance.setBaseMentalismRR(newShort);
				changed = true;
			}
		}

		if(physicalRREdit.length() > 0) {
			newShort = Short.valueOf(physicalRREdit.getText().toString());
			if(currentInstance.getBasePhysicalRR() != newShort) {
				currentInstance.setBasePhysicalRR(newShort);
				changed = true;
			}
		}

		if(fearRREdit.length() > 0) {
			newShort = Short.valueOf(fearRREdit.getText().toString());
			if(currentInstance.getBaseFearRR() != newShort) {
				currentInstance.setBaseFearRR(newShort);
				changed = true;
			}
		}

		position = realm1Spinner.getSelectedItemPosition();
		if(position != -1) {
			newRealm = realm1SpinnerAdapter.getItem(position);
			if(!newRealm.equals(currentInstance.getRealm1())) {
				currentInstance.setRealm1(newRealm);
				changed = true;
			}
		}

		position = realm2Spinner.getSelectedItemPosition();
		if(position != -1) {
			newRealm = realm2SpinnerAdapter.getItem(position);
			if(!newRealm.equals(currentInstance.getRealm2())) {
				if(newRealm.getId() == -1) {
					currentInstance.setRealm2(null);
				}
				else {
					currentInstance.setRealm2(newRealm);
				}
				changed = true;
			}
		}

		position = outlookSpinner.getSelectedItemPosition();
		if(position != -1) {
			newOutlook = outlookSpinnerAdapter.getItem(position);
			if (!newOutlook.equals(currentInstance.getOutlook())) {
				currentInstance.setOutlook(newOutlook);
				changed = true;
			}
		}

		newAttackMap = new HashMap<>(attackBonusesListAdapter.getCount());
		for(int i = 0; i < attackBonusesListAdapter.getCount(); i++) {
			newAttackBonus = attackBonusesListAdapter.getItem(i);
			if(currentInstance.getAttackBonusesMap().containsKey(newAttackBonus.getAttack())) {
				if(!currentInstance.getAttackBonusesMap().get(newAttackBonus.getAttack()).equals(newAttackBonus.getBonus())) {
					changed = true;
				}
				currentInstance.getAttackBonusesMap().remove(newAttackBonus.getAttack());
			}
			else {
				changed = true;
			}
			newAttackMap.put(newAttackBonus.getAttack(), newAttackBonus.getBonus());
		}
		if(!currentInstance.getAttackBonusesMap().isEmpty() && !newAttackMap.isEmpty()) {
			changed = true;
		}
		currentInstance.setAttackBonusesMap(newAttackMap);

		newSkillMap = new HashMap<>(skillBonusesListAdapter.getCount());
		for(int i = 0; i < skillBonusesListAdapter.getCount(); i++) {
			newSkillBonus = skillBonusesListAdapter.getItem(i);
			if(currentInstance.getSkillBonusesMap().containsKey(newSkillBonus.getSkill())) {
				if(!currentInstance.getSkillBonusesMap().get(newSkillBonus.getSkill()).equals(newSkillBonus.getBonus())) {
					changed = true;
				}
				currentInstance.getSkillBonusesMap().remove(newSkillBonus.getSkill());
			}
			else {
				changed = true;
			}
			newSkillMap.put(newSkillBonus.getSkill(), newSkillBonus.getBonus());
		}
		if(!currentInstance.getSkillBonusesMap().isEmpty() && !newSkillMap.isEmpty()) {
			changed = true;
		}
		currentInstance.setSkillBonusesMap(newSkillMap);

		checkedItemPositions = racialStatBonusList.getCheckedItemPositions();
		if(checkedItemPositions != null) {
			newStatBonusMap = new HashMap<>(checkedItemPositions.size());
			for (int i = 0; i < checkedItemPositions.size(); i++) {
				racialStatBonus = racialStatBonusListAdapter.getItem(checkedItemPositions.keyAt(i));
				if (currentInstance.getRacialStatBonuses().containsKey(racialStatBonus.getStat())) {
					if (!currentInstance.getRacialStatBonuses().get(racialStatBonus.getStat()).equals(racialStatBonus.getBonus())) {
						changed = true;
					}
					currentInstance.getRacialStatBonuses().remove(racialStatBonus.getStat());
				} else {
					changed = true;
				}
				newStatBonusMap.put(racialStatBonus.getStat(), racialStatBonus.getBonus());
			}
			if (!currentInstance.getRacialStatBonuses().isEmpty() && !newStatBonusMap.isEmpty()) {
				changed = true;
			}
			currentInstance.setRacialStatBonuses(newStatBonusMap);
		}
		else {
			currentInstance.getRacialStatBonuses().clear();
		}

		newTalentTiersMap = new HashMap<>(talentTiersListAdapter.getCount());
		for(int i = 0; i < talentTiersListAdapter.getCount(); i++) {
			newTalentTier = talentTiersListAdapter.getItem(i);
			if(currentInstance.getTalentTiersMap().containsKey(newTalentTier.getTalent())) {
				if(!currentInstance.getTalentTiersMap().get(newTalentTier.getTalent()).equals(newTalentTier.getTier())) {
					changed = true;
				}
				currentInstance.getTalentTiersMap().remove(newTalentTier.getTalent());
			}
			else {
				changed = true;
			}
			newTalentTiersMap.put(newTalentTier.getTalent(), newTalentTier.getTier());
		}
		if(!currentInstance.getTalentTiersMap().isEmpty() && !newTalentTiersMap.isEmpty()) {
			changed = true;
		}
		currentInstance.setTalentTiersMap(newTalentTiersMap);

		checkedItemPositions = criticalCodesList.getCheckedItemPositions();
		if(checkedItemPositions != null) {
			newCriticalCodesList = new ArrayList<>(checkedItemPositions.size());
			for (int i = 0; i < checkedItemPositions.size(); i++) {
				newCriticalCode = criticalCodesListAdapter.getItem(checkedItemPositions.keyAt(i));
				if (!currentInstance.getCriticalCodes().contains(newCriticalCode)) {
					changed = true;
				} else {
					currentInstance.getCriticalCodes().remove(newCriticalCode);
				}
				newCriticalCodesList.add(newCriticalCode);
			}
			if (!currentInstance.getCriticalCodes().isEmpty() && !newCriticalCodesList.isEmpty()) {
				changed = true;
			}
			currentInstance.setCriticalCodes(newCriticalCodesList);
		}
		else {
			currentInstance.getCriticalCodes().clear();
		}

		return changed;
	}

	private void copyItemToViews() {
		nameEdit.setText(currentInstance.getName());
		creatureTypeSpinner.setSelection(creatureTypeSpinnerAdapter.getPosition(currentInstance.getType()));
		typicalLevelEdit.setText(String.valueOf(currentInstance));
		levelSpreadSpinner.setSelection(levelSpreadSpinnerAdapter.getPosition(currentInstance.getLevelSpread()));
		descriptionEdit.setText(currentInstance.getDescription());
		sizeSpinner.setSelection(sizeSpinnerAdapter.getPosition(currentInstance.getSize()));
		heightEdit.setText(String.valueOf(currentInstance.getHeight()));
		lengthEdit.setText(String.valueOf(currentInstance.getLength()));
		weightEdit.setText(String.valueOf(currentInstance.getWeight()));
		healingRateEdit.setText(String.valueOf(currentInstance.getHealingRate()));
		baseHitsEdit.setText(String.valueOf(currentInstance.getBaseHits()));
		baseEnduranceEdit.setText(String.valueOf(currentInstance.getBaseEndurance()));
		armorTypeEdit.setText(String.valueOf(currentInstance.getArmorType()));
		channelingRREdit.setText(String.valueOf(currentInstance.getBaseChannellingRR()));
		essenceRREdit.setText(String.valueOf(currentInstance.getBaseEssenceRR()));
		mentalismRREdit.setText(String.valueOf(currentInstance.getBaseMentalismRR()));
		physicalRREdit.setText(String.valueOf(currentInstance.getBasePhysicalRR()));
		fearRREdit.setText(String.valueOf(currentInstance.getBaseFearRR()));
		realm1Spinner.setSelection(realm1SpinnerAdapter.getPosition(currentInstance.getRealm1()));
		if(currentInstance.getRealm2() == null) {
			Realm noRealm = new Realm();
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(noRealm));
		}
		else {
			realm2Spinner.setSelection(realm2SpinnerAdapter.getPosition(currentInstance.getRealm2()));
		}
		outlookSpinner.setSelection(outlookSpinnerAdapter.getPosition(currentInstance.getOutlook()));

		attackBonusesList.clearChoices();
		attackBonusesListAdapter.clear();
		for(Map.Entry<Attack, Short> entry : currentInstance.getAttackBonusesMap().entrySet()) {
			AttackBonus attackBonus = new AttackBonus(entry.getKey(), entry.getValue());
			attackBonusesListAdapter.add(attackBonus);
		}
		attackBonusesListAdapter.notifyDataSetChanged();

		skillBonusesList.clearChoices();
		skillBonusesListAdapter.clear();
		for(Map.Entry<Skill, Short> entry : currentInstance.getSkillBonusesMap().entrySet()) {
			SkillBonus skillBonus = new SkillBonus(entry.getKey(), entry.getValue());
			skillBonusesListAdapter.add(skillBonus);
		}
		skillBonusesListAdapter.notifyDataSetChanged();

		racialStatBonusListAdapter.clear();
		for(Map.Entry<Stat, Short> entry : currentInstance.getRacialStatBonuses().entrySet()) {
			RacialStatBonus racialStatBonus = new RacialStatBonus(entry.getKey(), entry.getValue());
			racialStatBonusListAdapter.add(racialStatBonus);
		}
		racialStatBonusListAdapter.notifyDataSetChanged();

		talentTiersList.clearChoices();
		talentTiersListAdapter.clear();
		for(Map.Entry<Talent, Short> entry : currentInstance.getTalentTiersMap().entrySet()) {
			TalentTier talentTier = new TalentTier(entry.getKey(), entry.getValue());
			talentTiersListAdapter.add(talentTier);
		}
		talentTiersListAdapter.notifyDataSetChanged();

		criticalCodesList.clearChoices();
		for(CriticalCode criticalCode : currentInstance.getCriticalCodes()) {
			criticalCodesList.setItemChecked(criticalCodesListAdapter.getPosition(criticalCode), true);
		}

		if(currentInstance.getName() != null && !currentInstance.getName().isEmpty()) {
			nameEdit.setError(null);
		}
		if(currentInstance.getDescription() != null && !currentInstance.getDescription().isEmpty()) {
			descriptionEdit.setError(null);
		}
	}

	private void saveItem() {
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

	private void initNameEdit(View layout) {
		nameEdit = (EditText)layout.findViewById(R.id.name_edit);
		nameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && nameEdit != null) {
					nameEdit.setError(getString(R.string.validation_creature_variety_name_required));
				}
			}
		});
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newName = nameEdit.getText().toString();
					if (!newName.equals(currentInstance.getName())) {
						currentInstance.setName(newName);
						saveItem();
					}
				}
			}
		});
	}

	private void initDescriptionEdit(View layout) {
		descriptionEdit = (EditText)layout.findViewById(R.id.description_edit);
		descriptionEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0 && descriptionEdit != null) {
					descriptionEdit.setError(getString(R.string.validation_creature_variety_description_required));
				}
			}
		});
		descriptionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newDescription = descriptionEdit.getText().toString();
					if (!newDescription.equals(currentInstance.getDescription())) {
						currentInstance.setDescription(newDescription);
						saveItem();
					}
				}
			}
		});
	}

	private void initCretureTypeSpinner(View layout) {
		creatureTypeSpinner = (Spinner)layout.findViewById(R.id.creature_type_spinner);
		creatureTypeSpinner.setAdapter(creatureTypeSpinnerAdapter);

		creatureTypeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<CreatureType>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all CreatureType instances", e);
					}
					@Override
					public void onNext(Collection<CreatureType> items) {
						creatureTypeSpinnerAdapter.clear();
						creatureTypeSpinnerAdapter.addAll(items);
						creatureTypeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		creatureTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getType() == null || creatureTypeSpinnerAdapter.getPosition(currentInstance.getType()) != position) {
					currentInstance.setType(creatureTypeSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getType() != null) {
					currentInstance.setType(null);
					saveItem();
				}
			}
		});
	}

	private void initTypicalLevelEdit(View layout) {
		typicalLevelEdit = (EditText)layout.findViewById(R.id.typical_level_edit);
		typicalLevelEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					typicalLevelEdit.setError(getString(R.string.validation_creature_variety_typical_level_required));
				}
			}
		});
		typicalLevelEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(typicalLevelEdit.length() > 0) {
						final short newTypicalLevel = Short.valueOf(typicalLevelEdit.getText().toString());
						if (newTypicalLevel != currentInstance.getTypicalLevel()) {
							currentInstance.setTypicalLevel(newTypicalLevel);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initLevelSpreadSpinner(View layout) {
		levelSpreadSpinner = (Spinner)layout.findViewById(R.id.level_spread_spinner);
		levelSpreadSpinner.setAdapter(levelSpreadSpinnerAdapter);

		levelSpreadSpinnerAdapter.clear();
		levelSpreadSpinnerAdapter.addAll(Arrays.asList(CreatureLevelSpreadTable.LEVEL_SPREAD_CODES));
		levelSpreadSpinnerAdapter.notifyDataSetChanged();

		levelSpreadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				char newLevelSpread = levelSpreadSpinnerAdapter.getItem(position);
				if(newLevelSpread != currentInstance.getLevelSpread()) {
					currentInstance.setLevelSpread(newLevelSpread);
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void initSizeSpinner(View layout) {
		sizeSpinner = (Spinner)layout.findViewById(R.id.size_spinner);
		sizeSpinner.setAdapter(sizeSpinnerAdapter);

		sizeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Size>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Size instances", e);
					}
					@Override
					public void onNext(Collection<Size> items) {
						sizeSpinnerAdapter.clear();
						sizeSpinnerAdapter.addAll(items);
						sizeSpinnerAdapter.notifyDataSetChanged();
					}
				});

		sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(currentInstance.getSize() == null || sizeSpinnerAdapter.getPosition(currentInstance.getSize()) != position) {
					currentInstance.setSize(sizeSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getSize() != null) {
					currentInstance.setSize(null);
					saveItem();
				}
			}
		});
	}

	private void initHeightEdit(View layout) {
		heightEdit = (EditText)layout.findViewById(R.id.height_edit);
		heightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					heightEdit.setError(getString(R.string.validation_creature_variety_height_required));
				}
			}
		});
		typicalLevelEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(heightEdit.length() > 0) {
						final short newShort = Short.valueOf(heightEdit.getText().toString());
						if (newShort != currentInstance.getHeight()) {
							currentInstance.setHeight(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initLengthEdit(View layout) {
		lengthEdit = (EditText)layout.findViewById(R.id.length_edit);
		lengthEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					lengthEdit.setError(getString(R.string.validation_creature_variety_length_required));
				}
			}
		});
		lengthEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(lengthEdit.length() > 0) {
						final short newShort = Short.valueOf(lengthEdit.getText().toString());
						if (newShort != currentInstance.getLength()) {
							currentInstance.setLength(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initWeightEdit(View layout) {
		weightEdit = (EditText)layout.findViewById(R.id.weight_edit);
		weightEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					weightEdit.setError(getString(R.string.validation_creature_variety_weight_required));
				}
			}
		});
		weightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(weightEdit.length() > 0) {
						final short newShort = Short.valueOf(weightEdit.getText().toString());
						if (newShort != currentInstance.getLength()) {
							currentInstance.setLength(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initHealingRateEdit(View layout) {
		healingRateEdit = (EditText)layout.findViewById(R.id.healing_rate_edit);
		healingRateEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					healingRateEdit.setError(getString(R.string.validation_creature_variety_healing_rate_required));
				}
			}
		});
		healingRateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(healingRateEdit.length() > 0) {
						final short newShort = Short.valueOf(healingRateEdit.getText().toString());
						if (newShort != currentInstance.getHealingRate()) {
							currentInstance.setHealingRate(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBaseHitsEdit(View layout) {
		baseHitsEdit = (EditText)layout.findViewById(R.id.base_hits_edit);
		baseHitsEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					baseHitsEdit.setError(getString(R.string.validation_creature_variety_base_hits_required));
				}
			}
		});
		baseHitsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(baseHitsEdit.length() > 0) {
						final short newShort = Short.valueOf(baseHitsEdit.getText().toString());
						if (newShort != currentInstance.getBaseHits()) {
							currentInstance.setBaseHits(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initBaseEnduranceEdit(View layout) {
		baseEnduranceEdit = (EditText)layout.findViewById(R.id.base_endurance_edit);
		baseEnduranceEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					baseEnduranceEdit.setError(getString(R.string.validation_creature_variety_base_endurance_required));
				}
			}
		});
		baseEnduranceEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(baseEnduranceEdit.length() > 0) {
						final short newShort = Short.valueOf(baseEnduranceEdit.getText().toString());
						if (newShort != currentInstance.getBaseEndurance()) {
							currentInstance.setBaseEndurance(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initArmorTypeEdit(View layout) {
		armorTypeEdit = (EditText)layout.findViewById(R.id.armor_type_edit);
		armorTypeEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					armorTypeEdit.setError(getString(R.string.validation_creature_variety_armor_type_required));
				}
			}
		});
		armorTypeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(armorTypeEdit.length() > 0) {
						final short newShort = Short.valueOf(armorTypeEdit.getText().toString());
						if (newShort != currentInstance.getArmorType()) {
							currentInstance.setArmorType(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initChannelingRREdit(View layout) {
		channelingRREdit = (EditText)layout.findViewById(R.id.base_channeling_rr_edit);
		channelingRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					channelingRREdit.setError(getString(R.string.validation_creature_variety_channeling_rr_required));
				}
			}
		});
		channelingRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(channelingRREdit.length() > 0) {
						final short newShort = Short.valueOf(channelingRREdit.getText().toString());
						if (newShort != currentInstance.getBaseChannellingRR()) {
							currentInstance.setBaseChannellingRR(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initEssenceRREdit(View layout) {
		essenceRREdit = (EditText)layout.findViewById(R.id.base_essence_rr_edit);
		essenceRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					essenceRREdit.setError(getString(R.string.validation_creature_variety_essence_rr_required));
				}
			}
		});
		essenceRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(essenceRREdit.length() > 0) {
						final short newShort = Short.valueOf(essenceRREdit.getText().toString());
						if (newShort != currentInstance.getBaseEssenceRR()) {
							currentInstance.setBaseEssenceRR(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initMentalismRREdit(View layout) {
		mentalismRREdit = (EditText)layout.findViewById(R.id.base_mentalism_rr_edit);
		mentalismRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					mentalismRREdit.setError(getString(R.string.validation_creature_variety_mentalism_rr_required));
				}
			}
		});
		mentalismRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(mentalismRREdit.length() > 0) {
						final short newShort = Short.valueOf(mentalismRREdit.getText().toString());
						if (newShort != currentInstance.getBaseMentalismRR()) {
							currentInstance.setBaseMentalismRR(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initPhysicalRREdit(View layout) {
		physicalRREdit = (EditText)layout.findViewById(R.id.base_physical_rr_edit);
		physicalRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					physicalRREdit.setError(getString(R.string.validation_creature_variety_physical_rr_required));
				}
			}
		});
		physicalRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(physicalRREdit.length() > 0) {
						final short newShort = Short.valueOf(physicalRREdit.getText().toString());
						if (newShort != currentInstance.getBasePhysicalRR()) {
							currentInstance.setBasePhysicalRR(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initFearRREdit(View layout) {
		fearRREdit = (EditText)layout.findViewById(R.id.base_fear_rr_edit);
		fearRREdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					fearRREdit.setError(getString(R.string.validation_creature_variety_fear_rr_required));
				}
			}
		});
		fearRREdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					if(fearRREdit.length() > 0) {
						final short newShort = Short.valueOf(fearRREdit.getText().toString());
						if (newShort != currentInstance.getBaseFearRR()) {
							currentInstance.setBaseFearRR(newShort);
							saveItem();
						}
					}
				}
			}
		});
	}

	private void initRealm1Spinner(View layout) {
		realm1Spinner = (Spinner)layout.findViewById(R.id.realm1_spinner);
		realm1Spinner.setAdapter(realm1SpinnerAdapter);

		realmRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Realm>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Realm instances in initRealm1Spinner", e);
					}
					@Override
					public void onNext(Collection<Realm> realms) {
						realm1SpinnerAdapter.clear();
						realm1SpinnerAdapter.addAll(realms);
						realm1SpinnerAdapter.notifyDataSetChanged();
					}
				});
		realm1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(!realm1SpinnerAdapter.getItem(position).equals(currentInstance.getRealm1())) {
					currentInstance.setRealm1(realm1SpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getRealm1() != null) {
					currentInstance.setRealm1(null);
					saveItem();
				}
			}
		});
	}

	private void initRealm2Spinner(View layout) {
		realm2Spinner = (Spinner)layout.findViewById(R.id.realm2_spinner);
		realm2Spinner.setAdapter(realm2SpinnerAdapter);

		realmRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Realm>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Realm instances in initRealm2Spinner", e);
					}
					@Override
					public void onNext(Collection<Realm> realms) {
						Realm noRealm = new Realm();
						noRealm.setName(getActivity().getString(R.string.label_no_realm));
						realm2SpinnerAdapter.clear();
						realm2SpinnerAdapter.add(noRealm);
						realm2SpinnerAdapter.addAll(realms);
						realm2SpinnerAdapter.notifyDataSetChanged();
					}
				});
		realm2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Realm newRealm = realm2SpinnerAdapter.getItem(position);
				if(!newRealm.equals(currentInstance.getRealm2())) {
					if(newRealm.getId() == -1) {
						currentInstance.setRealm2(null);
					}
					else {
						currentInstance.setRealm1(realm2SpinnerAdapter.getItem(position));
					}
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getRealm2() != null) {
					currentInstance.setRealm2(null);
					saveItem();
				}
			}
		});
	}

	private void initOutlookSpinner(View layout) {
		outlookSpinner = (Spinner)layout.findViewById(R.id.outlook_spinner);
		outlookSpinner.setAdapter(outlookSpinnerAdapter);

		outlookRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Outlook>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Outlook instances", e);
					}
					@Override
					public void onNext(Collection<Outlook> outlooks) {
						outlookSpinnerAdapter.clear();
						outlookSpinnerAdapter.addAll(outlooks);
						outlookSpinnerAdapter.notifyDataSetChanged();
					}
				});
		outlookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(!outlookSpinnerAdapter.getItem(position).equals(currentInstance.getOutlook())) {
					currentInstance.setOutlook(outlookSpinnerAdapter.getItem(position));
					saveItem();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if(currentInstance.getOutlook() != null) {
					currentInstance.setOutlook(null);
					saveItem();
				}
			}
		});
	}

	private void initAttacksList(View layout) {
		attacksList = (ListView) layout.findViewById(R.id.attacks_list);
		attacksList.setAdapter(attacksListAdapter);

		attackRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Attack>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
								"Exception caught loading all Attack instances in initSecondaryAttacksList", e);
					}
					@Override
					public void onNext(Collection<Attack> attacks) {
						attacksListAdapter.clear();
						attacksListAdapter.addAll(attacks);
						attacksList.clearChoices();
						attacksListAdapter.notifyDataSetChanged();
					}
				});

		attacksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!attacksList.isItemChecked(position)) {
					attacksList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = attacksList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(attacksList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Attack attack = attacksListAdapter.getItem(currentPosition);
					if(attack != null) {
						String attackIdString = String.valueOf(adapterView.getId());
						ClipData.Item clipDataItem = new ClipData.Item(attackIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_ATTACK, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), attacksList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

		attacksList.setOnDragListener(new AttackDragListener());
	}

	private void initAttackBonusesList(View layout) {
		attackBonusesList = (ListView) layout.findViewById(R.id.attack_bonuses_list);
		attackBonusesListAdapter = new AttackBonusListAdapter(this.getActivity(), this);
		attackBonusesList.setAdapter(attackBonusesListAdapter);

		attackBonusesListAdapter.clear();
		for(Map.Entry<Attack, Short> entry : currentInstance.getAttackBonusesMap().entrySet()) {
			attackBonusesListAdapter.add(new AttackBonus(entry.getKey(), entry.getValue()));
		}
		attackBonusesListAdapter.notifyDataSetChanged();

		attackBonusesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!attackBonusesList.isItemChecked(position)) {
					attackBonusesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = attackBonusesList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(attackBonusesList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					AttackBonus attackBonus = attackBonusesListAdapter.getItem(currentPosition);
					if(checkedItems.valueAt(i) && attackBonus != null) {
						String attackIdString = String.valueOf(attackBonus.getAttack().getId());
						ClipData.Item clipDataItem = new ClipData.Item(attackIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_ATTACK, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), attackBonusesList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

		attackBonusesList.setOnDragListener(new AttackBonusDragListener());
	}

	private void initAttackSequenceEdit(View layout) {
		attackSequenceEdit = (EditText)layout.findViewById(R.id.attack_sequence_edit);
		attackSequenceEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.length() == 0) {
					attackSequenceEdit.setError(getString(R.string.validation_creature_variety_attack_sequence_required));
				}
			}
		});
		attackSequenceEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(!hasFocus) {
					final String newAttackSequence = attackSequenceEdit.getText().toString();
					if (!newAttackSequence.equals(currentInstance.getAttackSequence())) {
						currentInstance.setAttackSequence(newAttackSequence);
						saveItem();
					}
				}
			}
		});
	}

	private void initSkillsList(View layout) {
		skillsList = (ListView) layout.findViewById(R.id.skills_list);
		skillsList.setAdapter(skillsListAdapter);

		skillRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Skill>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
								"Exception caught loading all Skill instances in initSecondaryAttacksList", e);
					}
					@Override
					public void onNext(Collection<Skill> skills) {
						skillsListAdapter.clear();
						skillsListAdapter.addAll(skills);
						skillsList.clearChoices();
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
					int currentPosition = checkedItems.keyAt(i);
					Skill skill = skillsListAdapter.getItem(currentPosition);
					if(skill != null) {
						String skillIdString = String.valueOf(adapterView.getId());
						ClipData.Item clipDataItem = new ClipData.Item(skillIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillsList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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
		skillBonusesListAdapter = new SkillBonusListAdapter(this.getActivity(), this);
		skillBonusesList.setAdapter(skillBonusesListAdapter);

		skillBonusesListAdapter.clear();
		for(Map.Entry<Skill, Short> entry : currentInstance.getSkillBonusesMap().entrySet()) {
			skillBonusesListAdapter.add(new SkillBonus(entry.getKey(), entry.getValue()));
		}
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
					if(checkedItems.valueAt(i) && skillBonus != null) {
						String skillIdString = String.valueOf(skillBonus.getSkill().getId());
						ClipData.Item clipDataItem = new ClipData.Item(skillIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_SKILL, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), skillBonusesList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

	private void initRacialStatBonusList(View layout) {
		racialStatBonusList = (ListView) layout.findViewById(R.id.racial_stat_bonuses_list);
		racialStatBonusListAdapter = new RacialStatBonusListAdapter(this.getActivity(), this);
		racialStatBonusList.setAdapter(racialStatBonusListAdapter);

		statRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Collection<Stat>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag",
								"Exception caught getting all Stat instances in initRacialStatBonusList", e);
					}
					@Override
					public void onNext(Collection<Stat> stats) {
						for(Stat stat : stats) {
							if(!currentInstance.getRacialStatBonuses().containsKey(stat)) {
								currentInstance.getRacialStatBonuses().put(stat, (short)0);
							}
						}
						Collection<RacialStatBonus> listItems = new ArrayList<>(currentInstance.getRacialStatBonuses().size());
						for(Map.Entry<Stat, Short> entry : currentInstance.getRacialStatBonuses().entrySet()) {
							listItems.add(new RacialStatBonus(entry.getKey(), entry.getValue()));
						}
						racialStatBonusListAdapter.clear();
						racialStatBonusListAdapter.addAll(listItems);
						racialStatBonusListAdapter.notifyDataSetChanged();
					}
				});
	}

	private void initTalentNamesList(View layout) {
		talentNamesList = (ListView) layout.findViewById(R.id.talents_list);
		talentNamesList.setAdapter(talentNamesListAdapter);

		talentRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<Talent>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught getting all Talent instances in initTalentNamesList", e);
					}
					@Override
					public void onNext(Collection<Talent> talents) {
						talentNamesListAdapter.clear();
						talentNamesListAdapter.addAll(talents);
						talentNamesListAdapter.notifyDataSetChanged();
					}
				});

		talentNamesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(!talentNamesList.isItemChecked(position)) {
					talentNamesList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = talentNamesList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(talentNamesList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					Talent talent = talentNamesListAdapter.getItem(currentPosition);
					if(talent != null) {
						String talentIdString = String.valueOf(talent.getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_ADD_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), talentNamesList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

		talentNamesList.setOnDragListener(new SkillDragListener());
	}

	private void initTalentTiersList(View layout) {
		talentTiersList = (ListView) layout.findViewById(R.id.talent_tiers_list);
		talentTiersListAdapter = new TalentTierListAdapter(this.getActivity(), this);
		talentTiersList.setAdapter(talentTiersListAdapter);

		talentTiersListAdapter.clear();
		for(Map.Entry<Talent, Short> entry : currentInstance.getTalentTiersMap().entrySet()) {
			talentTiersListAdapter.add(new TalentTier(entry.getKey(), entry.getValue()));
		}
		talentTiersListAdapter.notifyDataSetChanged();

		talentTiersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if(!talentTiersList.isItemChecked(position)) {
					talentTiersList.setItemChecked(position, true);
				}
				ClipData dragData = null;

				SparseBooleanArray checkedItems = talentTiersList.getCheckedItemPositions();
				List<View> checkedViews = new ArrayList<>(talentTiersList.getCheckedItemCount());
				for(int i = 0; i < checkedItems.size(); i++) {
					int currentPosition = checkedItems.keyAt(i);
					TalentTier talentTier = talentTiersListAdapter.getItem(currentPosition);
					if(checkedItems.valueAt(i) && talentTier != null) {
						String talentIdString = String.valueOf(talentTier.getTalent().getId());
						ClipData.Item clipDataItem = new ClipData.Item(talentIdString);
						if(dragData == null) {
							dragData = new ClipData(DRAG_REMOVE_TALENT, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, clipDataItem);
						}
						else {
							dragData.addItem(clipDataItem);
						}
						checkedViews.add(getViewByPosition(checkedItems.keyAt(i), talentTiersList));
					}
				}
				View.DragShadowBuilder myShadow = new CustomDragShadowBuilder(checkedViews);

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

		talentTiersList.setOnDragListener(new TalentTierDragListener());
	}

	private void initCriticalCodesList(View layout) {
		criticalCodesList = (ListView) layout.findViewById(R.id.critical_codes_list);
		criticalCodesList.setAdapter(criticalCodesListAdapter);

		criticalCodeRxHandler.getAll()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Subscriber<Collection<CriticalCode>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e("CreatureVarietiesFrag", "Exception caught loading all CriticalCode instances", e);
					}
					@Override
					public void onNext(Collection<CriticalCode> criticalCodes) {
						criticalCodesListAdapter.clear();
						criticalCodesListAdapter.addAll(criticalCodes);
						criticalCodesListAdapter.notifyDataSetChanged();
					}
				});

		criticalCodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CriticalCode criticalCode = criticalCodesListAdapter.getItem(position);
				boolean changed = false;
				if(criticalCodesList.isItemChecked(position)) {
					if(!currentInstance.getCriticalCodes().contains(criticalCode)) {
						currentInstance.getCriticalCodes().add(criticalCode);
						changed = true;
					}
				}
				else {
					if(currentInstance.getCriticalCodes().contains(criticalCode)) {
						currentInstance.getCriticalCodes().remove(criticalCode);
						changed = true;
					}
				}
				if(changed) {
					saveItem();
				}
			}
		});
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

	@Override
	public void setRacialStatBonus(RacialStatBonus racialStatBonus) {
		boolean changed = false;

		if(currentInstance.getRacialStatBonuses().containsKey(racialStatBonus.getStat())) {
			if(currentInstance.getRacialStatBonuses().get(racialStatBonus.getStat()) != racialStatBonus.getBonus()) {
				currentInstance.getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
				changed = true;
			}
		}
		else {
			currentInstance.getRacialStatBonuses().put(racialStatBonus.getStat(), racialStatBonus.getBonus());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	@Override
	public void setTalentTier(TalentTier talentTier) {
		boolean changed = false;

		if(currentInstance.getTalentTiersMap().containsKey(talentTier.getTalent())) {
			if(currentInstance.getTalentTiersMap().get(talentTier.getTalent()) != talentTier.getTier()) {
				currentInstance.getTalentTiersMap().put(talentTier.getTalent(), talentTier.getTier());
				changed = true;
			}
		}
		else {
			currentInstance.getTalentTiersMap().put(talentTier.getTalent(), talentTier.getTier());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	@Override
	public void setAttackBonus(AttackBonus attackBonus) {
		boolean changed = false;

		if(currentInstance.getAttackBonusesMap().containsKey(attackBonus.getAttack())) {
			if(currentInstance.getAttackBonusesMap().get(attackBonus.getAttack()) != attackBonus.getBonus()) {
				currentInstance.getAttackBonusesMap().put(attackBonus.getAttack(), attackBonus.getBonus());
				changed = true;
			}
		}
		else {
			currentInstance.getAttackBonusesMap().put(attackBonus.getAttack(), attackBonus.getBonus());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	@Override
	public void setSkillBonus(SkillBonus skillBonus) {
		boolean changed = false;

		if(currentInstance.getSkillBonusesMap().containsKey(skillBonus.getSkill())) {
			if(currentInstance.getSkillBonusesMap().get(skillBonus.getSkill()) != skillBonus.getBonus()) {
				currentInstance.getSkillBonusesMap().put(skillBonus.getSkill(), skillBonus.getBonus());
				changed = true;
			}
		}
		else {
			currentInstance.getSkillBonusesMap().put(skillBonus.getSkill(), skillBonus.getBonus());
			changed = true;
		}
		if(changed) {
			saveItem();
		}
	}

	protected class TalentTierDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = talentTiersList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_TALENT.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send talent ID but since that is the only field used in the Talent.equals method we can create a
							// temporary talent and set its id field then use the new Talent to find the position of the actual Talent
							// instance in the adapter
							int talentId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(talentId);
							int position = talentNamesListAdapter.getPosition(newTalent);
							Talent talent = talentNamesListAdapter.getItem(position);
							TalentTier talentTier = new TalentTier(talent, (short) 1);
							if (talentTiersListAdapter.getPosition(talentTier) == -1) {
								talentTiersListAdapter.add(talentTier);
								currentInstance.getTalentTiersMap().put(talent, (short)1);
								changed = true;
							}
						}
						if(changed) {
							saveItem();
							talentTiersListAdapter.notifyDataSetChanged();
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

	protected class TalentNamesDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = talentNamesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_TALENT.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send talent ID but since that is the only field used in the Talent.equals method and talent is the
							// only field used in the TalentTier.equals method we can create a temporary talent and set its id field then
							// create a new TalentTier and set its talent field then use the new TalentTier to find the position of the
							// complete TalentTier instance in the adapter
							int talentId = Integer.valueOf(item.getText().toString());
							Talent newTalent = new Talent();
							newTalent.setId(talentId);
							TalentTier newTalentTier = new TalentTier(newTalent, (short)0);
							int position = talentTiersListAdapter.getPosition(newTalentTier);
							TalentTier talentTier = talentTiersListAdapter.getItem(position);
							currentInstance.getTalentTiersMap().remove(talentTier.getTalent());
							talentTiersListAdapter.remove(talentTier);
						}
						saveItem();
						talentTiersList.clearChoices();
						talentTiersListAdapter.notifyDataSetChanged();
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

	protected class AttackBonusDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = attackBonusesList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch(action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
						break;
					}
					return false;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_ADD_ATTACK.equals(event.getClipDescription().getLabel())) {
						boolean changed = false;
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send attack ID but since that is the only field used in the Attack.equals method we can create a
							// temporary attack and set its id field then use the new Attack to find the position of the actual Attack
							// instance in the adapter
							int attackId = Integer.valueOf(item.getText().toString());
							Attack newAttack = new Attack();
							newAttack.setId(attackId);
							int position = attacksListAdapter.getPosition(newAttack);
							Attack attack = attacksListAdapter.getItem(position);
							AttackBonus attackBonus = new AttackBonus(attack, (short) 0);
							if (attackBonusesListAdapter.getPosition(attackBonus) == -1) {
								attackBonusesListAdapter.add(attackBonus);
								currentInstance.getAttackBonusesMap().put(attack, (short)0);
								changed = true;
							}
						}
						if(changed) {
							saveItem();
							attackBonusesListAdapter.notifyDataSetChanged();
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

	protected class AttackDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
		private Drawable normalShape = attacksList.getBackground();

		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action = event.getAction();

			switch (action) {
				case DragEvent.ACTION_DRAG_STARTED:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
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
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						v.setBackground(targetShape);
						v.invalidate();
					}
					else {
						return false;
					}
					break;
				case DragEvent.ACTION_DROP:
					if(event.getClipDescription() != null && DRAG_REMOVE_ATTACK.equals(event.getClipDescription().getLabel())) {
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send attack ID but since that is the only field used in the Attack.equals method and attack is the
							// only field used in the AttackBonus.equals method we can create a temporary Attack and set its id field then
							// create a new AttackBonus and set its attack field then use the new AttackBonus to find the position of the
							// complete AttackBonus instance in the adapter
							int attackId = Integer.valueOf(item.getText().toString());
							Attack newAttack = new Attack();
							newAttack.setId(attackId);
							AttackBonus newAttackBonus = new AttackBonus(newAttack, (short)0);
							int position = attackBonusesListAdapter.getPosition(newAttackBonus);
							AttackBonus attackBonus = attackBonusesListAdapter.getItem(position);
							currentInstance.getAttackBonusesMap().remove(attackBonus.getAttack());
							attackBonusesListAdapter.remove(attackBonus);
						}
						saveItem();
						attackBonusesList.clearChoices();
						attackBonusesListAdapter.notifyDataSetChanged();
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

	protected class SkillBonusDragListener implements View.OnDragListener {
		private Drawable targetShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_target_background, null);
		private Drawable hoverShape = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.drag_hover_background, null);
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
						for(int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send skill ID but since that is the only field used in the Skill.equals method we can create a
							// temporary skill and set its id field then use the new Skill to find the position of the actual Skill
							// instance in the adapter
							int skillId = Integer.valueOf(item.getText().toString());
							Skill newSkill = new Skill();
							newSkill.setId(skillId);
							int position = skillsListAdapter.getPosition(newSkill);
							Skill skill = skillsListAdapter.getItem(position);
							SkillBonus skillBonus = new SkillBonus(skill, (short) 0);
							if (skillBonusesListAdapter.getPosition(skillBonus) == -1) {
								skillBonusesListAdapter.add(skillBonus);
								currentInstance.getSkillBonusesMap().put(skill, (short)0);
								changed = true;
							}
						}
						if(changed) {
							saveItem();
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
						for (int i = 0; i < event.getClipData().getItemCount(); i++) {
							ClipData.Item item = event.getClipData().getItemAt(i);
							// We just send skill ID but since that is the only field used in the Skill.equals method and skill is the
							// only field used in the SkillBonus.equals method we can create a temporary Skill and set its id field then
							// create a new SkillBonus and set its skill field then use the new SkillBonus to find the position of the
							// complete SkillBonus instance in the adapter
							int skillId = Integer.valueOf(item.getText().toString());
							Skill newSkill = new Skill();
							newSkill.setId(skillId);
							SkillBonus newSkillBonus = new SkillBonus(newSkill, (short)0);
							int position = skillBonusesListAdapter.getPosition(newSkillBonus);
							SkillBonus skillBonus =skillBonusesListAdapter.getItem(position);
							currentInstance.getSkillBonusesMap().remove(skillBonus.getSkill());
							skillBonusesListAdapter.remove(skillBonus);
						}
						saveItem();
						skillBonusesList.clearChoices();
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

	private static class CustomDragShadowBuilder extends View.DragShadowBuilder {
		private Drawable[] shadows;
		private List<View> views;

		public CustomDragShadowBuilder(List<View> views) {
			this.views = views;
			shadows = new Drawable[views.size()];
			for(int i = 0 ; i < shadows.length; i ++) {
				shadows[i] = new ColorDrawable(Color.LTGRAY);
			}
		}

		@Override
		public void onProvideShadowMetrics (Point size, Point touch) {
			int topLeftX = Integer.MAX_VALUE, topLeftY = Integer.MAX_VALUE;
			int bottomRightX = 0, bottomRightY = 0;
			int width, height;

			for(int i = 0; i < views.size(); i++) {
				shadows[i].setBounds(views.get(i).getLeft(), views.get(i).getTop(), views.get(i).getRight(), views.get(i).getBottom());
				if(views.get(i).getLeft() < topLeftX) {
					topLeftX = views.get(i).getLeft();
				}
				if(views.get(i).getTop() < topLeftY) {
					topLeftY = views.get(i).getTop();
				}
				if(views.get(i).getRight() > bottomRightX) {
					bottomRightX = views.get(i).getRight();
				}
				if(views.get(i).getBottom() > bottomRightY) {
					bottomRightY = views.get(i).getBottom();
				}
			}

			width = bottomRightX - topLeftX;
			height = bottomRightY - topLeftY;
			size.set(width, height);
			touch.set(width / 2, height / 2);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			for(Drawable shadow : shadows) {
				shadow.draw(canvas);
			}
		}
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
}

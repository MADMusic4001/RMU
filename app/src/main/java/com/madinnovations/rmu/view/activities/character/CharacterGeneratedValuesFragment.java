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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.Statistic;
import com.madinnovations.rmu.data.entities.spells.Realm;
import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import java.util.Collection;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Handles interactions with the UI for character creation.
 */
public class CharacterGeneratedValuesFragment extends Fragment {
	private static final String TAG = "GeneratedValuesFragment";
	@Inject
	protected SkillRxHandler   skillRxHandler;
	@Inject
	protected RealmRxHandler   realmRxHandler;
	private Skill              bodyDevelopmentSkill = null;
	private Skill              powerDevelopmentSkill = null;
	private Collection<Realm>  realms = null;
	private CharactersFragment charactersFragment;
	private EditText           currentLevelText;
	private EditText           experiencePointsText;
	private EditText           currentHitsText;
	private EditText           maxHitsText;
	private EditText           baseMovementRateText;
	private EditText           defensiveBonusText;
	private EditText           enduranceText;
	private EditText           fatigueText;
	private EditText           encumbranceText;
	private EditText           armorTypeText;
	private EditText           initiativeText;
	private EditText           currentPowerPointsText;
	private EditText           maxPowerPointsText;
	private EditText           channelingRRText;
	private EditText           essenceRRText;
	private EditText           mentalismRRText;
	private EditText           physicalRRText;
	private EditText           fearRRText;
	private EditText           agilityText;
	private EditText           constitutionText;
	private EditText           empathyText;
	private EditText           intuitionText;
	private EditText           memoryText;
	private EditText           presenceText;
	private EditText           quicknessText;
	private EditText           reasoningText;
	private EditText           selfDisciplineText;
	private EditText           strengthText;

	/**
	 * Creates new CharacterGeneratedValuesFragment instance.
	 *
	 * @param charactersFragment  the CharactersFragment instance this fragment is attached to.
	 * @return the new instance.
	 */
	public static CharacterGeneratedValuesFragment newInstance(CharactersFragment charactersFragment) {
		CharacterGeneratedValuesFragment fragment = new CharacterGeneratedValuesFragment();
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

		View layout = inflater.inflate(R.layout.character_generated_values_page, container, false);

		Character character = charactersFragment.getCurrentInstance();
		currentLevelText = (EditText)layout.findViewById(R.id.current_level_text);
		currentLevelText.setText(String.valueOf(character.getExperiencePoints()/10000));
		experiencePointsText = (EditText)layout.findViewById(R.id.experience_points_text);
		experiencePointsText.setText(String.valueOf(character.getExperiencePoints()));
		currentHitsText = (EditText)layout.findViewById(R.id.current_hits_text);
		maxHitsText = (EditText)layout.findViewById(R.id.max_hits_text);
		baseMovementRateText = (EditText)layout.findViewById(R.id.base_movement_rate_text);
		enduranceText = (EditText) layout.findViewById(R.id.endurance_text);
		defensiveBonusText = (EditText)layout.findViewById(R.id.defensive_bonus_text);
		fatigueText = (EditText) layout.findViewById(R.id.fatigue_text);
		if(character.getRace() != null) {
			baseMovementRateText.setText(String.valueOf(character.getRace().getStrideModifier()));
		}
		encumbranceText = (EditText)layout.findViewById(R.id.encumbrance_text);
		encumbranceText.setText(null);
		armorTypeText = (EditText)layout.findViewById(R.id.armor_type_text);
		armorTypeText.setText(null);
		initiativeText = (EditText)layout.findViewById(R.id.initiative_text);
		currentPowerPointsText = (EditText) layout.findViewById(R.id.current_power_points_text);
		maxPowerPointsText = (EditText) layout.findViewById(R.id.max_power_points_text);
		channelingRRText = (EditText)layout.findViewById(R.id.channeling_rr_text);
		essenceRRText = (EditText)layout.findViewById(R.id.essence_rr_text);
		mentalismRRText = (EditText)layout.findViewById(R.id.mentalism_rr_text);
		physicalRRText = (EditText)layout.findViewById(R.id.physical_rr_text);
		fearRRText = (EditText)layout.findViewById(R.id.fear_rr_text);
		initBodyDevelopmentSkill();
		initStatBonusValues();
		initPowerDevelopmentSkill();
		initRealms();
		initStats(layout);
		setStatViews();
		return layout;
	}

	public void copyItemToViews() {
		Character character = charactersFragment.getCurrentInstance();
		currentLevelText.setText(String.valueOf(character.getExperiencePoints()/10000));
		experiencePointsText.setText(String.valueOf(character.getExperiencePoints()));
		baseMovementRateText.setText(String.valueOf(character.getRace().getStrideModifier()));
		encumbranceText.setText(null);
		armorTypeText.setText(null);
		setBodyDevelopmentViews();
		setStatViews();
		setStatAndRealmViews();
		setPowerDevelopmentViews();
	}

	public void initBodyDevelopmentSkill() {
		skillRxHandler.getByName("Body Development")
				.subscribe(new Subscriber<Skill>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception getting Body Development skill instance.", e);
					}
					@Override
					public void onNext(Skill skill) {
						bodyDevelopmentSkill = skill;
						setBodyDevelopmentViews();
					}
				});
	}

	private void initStatBonusValues() {
	}

	public void initPowerDevelopmentSkill() {
		skillRxHandler.getByName("Power Development")
				.subscribe(new Subscriber<Skill>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception getting Power Development skill instance.", e);
					}
					@Override
					public void onNext(Skill skill) {
						powerDevelopmentSkill = skill;
						setPowerDevelopmentViews();
					}
				});
	}

	private void initRealms() {
		realmRxHandler.getAll()
				.subscribe(new Subscriber<Collection<Realm>>() {
					@Override
					public void onCompleted() {}
					@Override
					public void onError(Throwable e) {
						Log.e(TAG, "Exception caught loading Realm instances.", e);
					}
					@Override
					public void onNext(Collection<Realm> realmCollection) {
						realms = realmCollection;
						setStatAndRealmViews();
					}
				});
	}

	private void initStats(View layout) {
		agilityText = (EditText)layout.findViewById(R.id.agility_text);
		constitutionText = (EditText)layout.findViewById(R.id.constitution_text);
		empathyText = (EditText)layout.findViewById(R.id.empathy_text);
		intuitionText = (EditText)layout.findViewById(R.id.intuition_text);
		memoryText = (EditText)layout.findViewById(R.id.memory_text);
		presenceText = (EditText)layout.findViewById(R.id.presence_text);
		quicknessText = (EditText)layout.findViewById(R.id.quickness_text);
		reasoningText = (EditText)layout.findViewById(R.id.reasoning_text);
		selfDisciplineText = (EditText)layout.findViewById(R.id.self_discipline_text);
		strengthText = (EditText)layout.findViewById(R.id.strength_text);
	}

	private void setBodyDevelopmentViews() {
		if(bodyDevelopmentSkill != null) {
			Character character = charactersFragment.getCurrentInstance();
			Short bdRanks = character.getSkillRanks().get(bodyDevelopmentSkill);
			if(bdRanks == null) {
				bdRanks = 0;
			}
			if(character.getRace() != null) {
				short baseHits = character.getRace().getBaseHits();
				baseHits = (short) (baseHits + bdRanks);
				maxHitsText.setText(String.valueOf(baseHits));
				currentHitsText.setText(String.valueOf(baseHits - character.getHitPointLoss()));
				short endurance = (short) (character.getRace().getEnduranceModifier() + bdRanks);
				enduranceText.setText(String.valueOf(endurance));
				fatigueText.setText(String.valueOf(character.getFatigue()));
			}
		}
	}

	private void setStatViews() {
		Character character = charactersFragment.getCurrentInstance();
		for(Statistic stat : Statistic.values()) {
			if(character.getRace() != null && character.getStatTemps().get(stat) != null) {
				short statBonus = Statistic.getBonus(character.getStatTemps().get(stat));
				switch (stat) {
					case AGILITY:
						agilityText.setText(String.valueOf(statBonus));
						break;
					case CONSTITUTION:
						constitutionText.setText(String.valueOf(statBonus));
						physicalRRText.setText(String.valueOf(character.getRace().getPhysicalResistanceModifier() + statBonus));
						break;
					case EMPATHY:
						empathyText.setText(String.valueOf(statBonus));
						break;
					case INTUITION:
						intuitionText.setText(String.valueOf(statBonus));
						break;
					case MEMORY:
						memoryText.setText(String.valueOf(statBonus));
						break;
					case PRESENCE:
						presenceText.setText(String.valueOf(statBonus));
						break;
					case QUICKNESS:
						quicknessText.setText(String.valueOf(statBonus));
						defensiveBonusText.setText(String.valueOf(statBonus * 3));
						initiativeText.setText(String.valueOf(statBonus));
						break;
					case REASONING:
						reasoningText.setText(String.valueOf(statBonus));
						break;
					case SELF_DISCIPLINE:
						selfDisciplineText.setText(String.valueOf(statBonus));
						fearRRText.setText(String.valueOf(statBonus));
						break;
					case STRENGTH:
						strengthText.setText(String.valueOf(statBonus));
						break;
				}
			}
		}
	}

	private void setStatAndRealmViews() {
		if(realms != null) {
			for(Realm realm : realms) {
				switch (realm.getStat()) {
					case INTUITION:
						channelingRRText.setText(String.valueOf(getRRBonus(Statistic.INTUITION, "Channeling", realm)));
						break;
					case EMPATHY:
						essenceRRText.setText(String.valueOf(getRRBonus(Statistic.EMPATHY, "Essence", realm)));
						break;
					case PRESENCE:
						mentalismRRText.setText(String.valueOf(getRRBonus(Statistic.PRESENCE, "Mentalism", realm)));
						break;
					default:
						physicalRRText.setText(String.valueOf(getRRBonus(Statistic.CONSTITUTION, "Physical", null)));
						physicalRRText.setText(String.valueOf(getRRBonus(Statistic.SELF_DISCIPLINE, "Fear", null)));
						break;
				}
			}
		}
	}

	private short getRRBonus(Statistic statistic, String realmName, Realm realm) {
		Character character = charactersFragment.getCurrentInstance();
		Short temp = character.getStatTemps().get(statistic);
		if(temp == null) {
			temp = 0;
		}
		short statBonus = Statistic.getBonus(temp);
		short raceBonus = 0;
		if (realmName.equals("Physical")) {
			raceBonus = character.getRace().getPhysicalResistanceModifier();
		}
		short rrBonus = (short) (statBonus + raceBonus);

		if (realm.equals(character.getRealm()) || realm.equals(character.getRealm2()) || realm.equals(character.getRealm3())) {
			rrBonus += 10;
		}

		return rrBonus;
	}

	public void setPowerDevelopmentViews() {
		if(powerDevelopmentSkill != null) {
			Character character = charactersFragment.getCurrentInstance();
			Short pdRanks = character.getSkillRanks().get(powerDevelopmentSkill);
			if(pdRanks == null) {
				pdRanks = 0;
			}
			if(character.getRace() != null) {
				short basePowerPoints = (short) (character.getRace().getBaseHits() + pdRanks);
				maxPowerPointsText.setText(String.valueOf(basePowerPoints));
				currentPowerPointsText.setText(String.valueOf(basePowerPoints - character.getPowerPointLoss()));
			}
		}
	}
}

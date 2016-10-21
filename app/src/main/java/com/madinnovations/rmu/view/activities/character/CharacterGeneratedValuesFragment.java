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
import android.widget.TextView;

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
	private static final String LOG_TAG = "GeneratedValuesFrag";
	@Inject
	protected SkillRxHandler   skillRxHandler;
	@Inject
	protected RealmRxHandler   realmRxHandler;
	private Skill              bodyDevelopmentSkill = null;
	private Skill              powerDevelopmentSkill = null;
	private Collection<Realm>  realms = null;
	private CharactersFragment charactersFragment;
	private TextView           currentLevelText;
	private TextView           experiencePointsText;
	private TextView           currentHitsText;
	private TextView           maxHitsText;
	private TextView           baseMovementRateText;
	private TextView           defensiveBonusText;
	private TextView           currentEnduranceText;
	private TextView           maxEnduranceText;
	private TextView           encumbranceText;
	private TextView           armorTypeText;
	private TextView           initiativeText;
	private TextView           currentPowerPointsText;
	private TextView           maxPowerPointsText;
	private TextView		   channelingRRText;
	private TextView           essenceRRText;
	private TextView           mentalismRRText;
	private TextView           physicalRRText;
	private TextView           fearRRText;

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
		((CampaignActivity)getActivity()).getActivityComponent().
				newCharacterFragmentComponent(new CharacterFragmentModule(this)).injectInto(this);

		View layout = inflater.inflate(R.layout.character_generated_values_fragment, container, false);

		Character character = charactersFragment.getCurrentInstance();
		currentLevelText = (TextView)layout.findViewById(R.id.current_level_text);
		currentLevelText.setText(String.valueOf(character.getExperiencePoints()/10000));
		experiencePointsText = (TextView)layout.findViewById(R.id.experience_points_text);
		experiencePointsText.setText(String.valueOf(character.getExperiencePoints()));
		currentHitsText = (TextView)layout.findViewById(R.id.current_hits_text);
		maxHitsText = (TextView)layout.findViewById(R.id.max_hits_text);
		baseMovementRateText = (TextView)layout.findViewById(R.id.base_movement_rate_text);
		currentEnduranceText = (TextView)layout.findViewById(R.id.current_endurance_text);
		defensiveBonusText = (TextView)layout.findViewById(R.id.defensive_bonus_text);
		maxEnduranceText = (TextView)layout.findViewById(R.id.max_endurance_text);
		baseMovementRateText.setText(String.valueOf(character.getRace().getStrideModifier()));
		encumbranceText = (TextView)layout.findViewById(R.id.encumbrance_text);
		encumbranceText.setText(null);
		armorTypeText = (TextView)layout.findViewById(R.id.armor_type_text);
		armorTypeText.setText(null);
		initiativeText = (TextView)layout.findViewById(R.id.initiative_text);
		currentPowerPointsText = (TextView)layout.findViewById(R.id.current_power_points_text);
		maxPowerPointsText = (TextView)layout.findViewById(R.id.max_power_points_text);
		channelingRRText = (TextView)layout.findViewById(R.id.channeling_rr_text);
		essenceRRText = (TextView)layout.findViewById(R.id.essence_rr_text);
		mentalismRRText = (TextView)layout.findViewById(R.id.mentalism_rr_text);
		physicalRRText = (TextView)layout.findViewById(R.id.physical_rr_text);
		fearRRText = (TextView)layout.findViewById(R.id.fear_rr_text);
		initBodyDevelopmentSkill();
		initStatBonusValues();
		initPowerDevelopmentSkill();
		initRealms();
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
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
						Log.e(LOG_TAG, "Exception getting Body Development skill instance.", e);
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
						Log.e(LOG_TAG, "Exception getting Power Development skill instance.", e);
					}
					@Override
					public void onNext(Skill skill) {
						powerDevelopmentSkill = skill;
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
						Log.e(LOG_TAG, "Exception caught loading Realm instances.", e);
					}
					@Override
					public void onNext(Collection<Realm> realmCollection) {
						realms = realmCollection;
						setStatAndRealmViews();
					}
				});
	}

	private void setBodyDevelopmentViews() {
		if(bodyDevelopmentSkill != null) {
			Character character = charactersFragment.getCurrentInstance();
			Short bdRanks = character.getSkillRanks().get(bodyDevelopmentSkill);
			if(bdRanks != null && character.getRace() != null) {
				short baseHits = character.getRace().getBaseHits();
				baseHits = (short) (baseHits + bdRanks);
				maxHitsText.setText(String.valueOf(baseHits));
				currentHitsText.setText(String.valueOf(baseHits - character.getHitPointLoss()));
				short maxEndurance = (short) (character.getRace().getEnduranceModifier() + bdRanks);
				maxEnduranceText.setText(String.valueOf(maxEndurance));
				currentEnduranceText.setText(String.valueOf(maxEndurance - character.getEnduranceLoss()));
			}
		}
	}

	private void setStatViews() {
		Character character = charactersFragment.getCurrentInstance();
		for(Statistic stat : Statistic.values()) {
			if(character.getRace() != null && character.getStatTemps().get(stat) != null) {
				short statBonus = Statistic.getBonus(character.getStatTemps().get(stat));
				if (Statistic.QUICKNESS.equals(stat)) {
					defensiveBonusText.setText(String.valueOf(statBonus * 3));
					initiativeText.setText(String.valueOf(statBonus));
				}
				if (Statistic.CONSTITUTION.equals(stat)) {
					physicalRRText.setText(String.valueOf(character.getRace().getPhysicalResistanceModifier() + statBonus));
				}
				if (Statistic.SELF_DISCIPLINE.equals(stat)) {
					fearRRText.setText(String.valueOf(statBonus));
				}
			}
		}
	}

	private void setStatAndRealmViews() {
		if(realms != null) {
			Character character = charactersFragment.getCurrentInstance();
			for(Statistic stat : Statistic.values()) {
				if(character.getRace() != null && character.getProfession() != null &&
						character.getStatTemps().get(stat) != null) {
					short statBonus = Statistic.getBonus(character.getStatTemps().get(stat));
					for (Realm realm : realms) {
						Short raceRRBonus = character.getRace().getRealmResistancesModifiers().get(realm);
						if(raceRRBonus != null) {
							short realmRRBonus = 0;
							if (realm.getName().equals("Channeling") && realm.getStat().equals(stat)) {
								if (realm.equals(character.getProfession().getRealm1()) ||
										realm.equals(character.getProfession().getRealm2()) ||
										realm.equals(character.getRealm())) {
									realmRRBonus = 10;
								}
								channelingRRText.setText(String.valueOf(statBonus + raceRRBonus + realmRRBonus));
							}
							if (realm.getName().equals("Essence") && realm.getStat().equals(stat)) {
								if (realm.equals(character.getProfession().getRealm1()) ||
										realm.equals(character.getProfession().getRealm2()) ||
										realm.equals(character.getRealm())) {
									realmRRBonus = 10;
								}
								essenceRRText.setText(String.valueOf(statBonus + raceRRBonus + realmRRBonus));
							}
							if (realm.getName().equals("Mentalism") && realm.getStat().equals(stat)) {
								if (realm.equals(character.getProfession().getRealm1()) ||
										realm.equals(character.getProfession().getRealm2()) ||
										realm.equals(character.getRealm())) {
									realmRRBonus = 10;
								}
								mentalismRRText.setText(String.valueOf(statBonus + raceRRBonus + realmRRBonus));
							}
						}
					}
				}
			}
		}
	}

	public void setPowerDevelopmentViews() {
		if(powerDevelopmentSkill != null) {
			Character character = charactersFragment.getCurrentInstance();
			Short pdRanks = character.getSkillRanks().get(powerDevelopmentSkill);
			if(pdRanks != null && character.getRace() != null) {
				short basePowerPoints = (short) (character.getRace().getBaseHits() + pdRanks);
				maxPowerPointsText.setText(String.valueOf(basePowerPoints));
				currentPowerPointsText.setText(String.valueOf(basePowerPoints - character.getPowerPointLoss()));
			}
		}
	}
}

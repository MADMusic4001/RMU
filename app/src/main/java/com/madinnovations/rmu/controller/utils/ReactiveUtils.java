/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.controller.utils;

import com.madinnovations.rmu.controller.rxhandler.FileRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.CampaignRxHandler;
import com.madinnovations.rmu.controller.rxhandler.campaign.ImportExportRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CharacterRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.CultureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.ProfessionRxHandler;
import com.madinnovations.rmu.controller.rxhandler.character.RaceRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.AttackRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.BodyPartRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalResultRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.CriticalTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageResultRowRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageResultRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DamageTableRxHandler;
import com.madinnovations.rmu.controller.rxhandler.combat.DiseaseRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SizeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.SpecializationRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.common.TalentRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureArchetypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureCategoryRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.CreatureVarietyRxHandler;
import com.madinnovations.rmu.controller.rxhandler.creature.OutlookRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.ItemRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.ItemTemplateRxHandler;
import com.madinnovations.rmu.controller.rxhandler.item.WeaponTemplateRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.RealmRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellListRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellSubTypeRxHandler;
import com.madinnovations.rmu.controller.rxhandler.spell.SpellTypeRxHandler;

import javax.inject.Inject;

import rx.Observable;

/**
 * Utility class for getting instances of classes that create Reactive Observables.
 */
public class ReactiveUtils {
	public enum Handler {
		ATTACK_RX_HANDLER,
		BODY_PART_RX_HANDLER,
		CAMPAIGN_RX_HANDLER,
		CHARACTER_RX_HANDLER,
		CREATURE_RX_HANDLER,
		CREATURE_ARCHETYPE_RX_HANDLER,
		CREATURE_CATEGORY_RX_HANDLER,
		CREATURE_TYPE_RX_HANDLER,
		CREATURE_VARIETY_RX_HANDLER,
		CRITICAL_RESULT_RX_HANDLER,
		CRITICAL_TYPE_RX_HANDLER,
		CULTURE_RX_HANDLER,
		DAMAGE_RESULT_RX_HANDLER,
		DAMAGE_RESULT_ROW_RX_HANDLER,
		DAMAGE_TABLE_RX_HANDLER,
		DISEASE_RX_HANDLER,
		FILE_RX_HANDLER,
		IMPORT_EXPORT_RX_HANDLER,
		ITEM_RX_HANDLER,
		ITEM_TEMPLATE_RX_HANDLER,
		OUTLOOK_RX_HANDLER,
		PROFESSION_RX_HANDLER,
		RACE_RX_HANDLER,
		REALM_RX_HANDLER,
		SIZE_RX_HANDLER,
		SKILL_RX_HANDLER,
		SKILL_CATEGORY_RX_HANDLER,
		SPECIALIZATION_RX_HANDLER,
		SPELL_RX_HANDLER,
		SPELL_LIST_RX_HANDLER,
		SPELL_SUBTYPE_RX_HANDLER,
		SPELL_TYPE_RX_HANDLER,
		TALENT_RX_HANDLER,
		TALENT_CATEGORY_RX_HANDLER,
		WEAPON_TEMPLATE_RX_HANDLER
	}
	@Inject
	protected AttackRxHandler            attackRxHandler;
	@Inject
	protected BodyPartRxHandler          bodyPartRxHandler;
	@Inject
	protected CampaignRxHandler          campaignRxHandler;
	@Inject
	protected CharacterRxHandler         characterRxHandler;
	@Inject
	protected CreatureRxHandler          creatureRxHandler;
	@Inject
	protected CreatureArchetypeRxHandler creatureArchetypeRxHandler;
	@Inject
	protected CreatureCategoryRxHandler  creatureCategoryRxHandler;
	@Inject
	protected CreatureTypeRxHandler      creatureTypeRxHandler;
	@Inject
	protected CreatureVarietyRxHandler   creatureVarietyRxHandler;
	@Inject
	protected CriticalResultRxHandler    criticalResultRxHandler;
	@Inject
	protected CriticalTypeRxHandler      criticalTypeRxHandler;
	@Inject
	protected CultureRxHandler           cultureRxHandler;
	@Inject
	protected DamageResultRxHandler      damageResultRxHandler;
	@Inject
	protected DamageResultRowRxHandler   damageResultRowRxHandler;
	@Inject
	protected DamageTableRxHandler       damageTableRxHandler;
	@Inject
	protected DiseaseRxHandler           diseaseRxHandler;
	@Inject
	protected FileRxHandler              fileRxHandler;
	@Inject
	protected ImportExportRxHandler      importExportRxHandler;
	@Inject
	protected ItemRxHandler              itemRxHandler;
	@Inject
	protected ItemTemplateRxHandler      itemTemplateRxHandler;
	@Inject
	protected OutlookRxHandler           outlookRxHandler;
	@Inject
	protected ProfessionRxHandler        professionRxHandler;
	@Inject
	protected RaceRxHandler              raceRxHandler;
	@Inject
	protected RealmRxHandler             realmRxHandler;
	@Inject
	protected SizeRxHandler              sizeRxHandler;
	@Inject
	protected SkillRxHandler             skillRxHandler;
	@Inject
	protected SkillCategoryRxHandler     skillCategoryRxHandler;
	@Inject
	protected SpecializationRxHandler    specializationRxHandler;
	@Inject
	protected SpellRxHandler             spellRxHandler;
	@Inject
	protected SpellListRxHandler         spellListRxHandler;
	@Inject
	protected SpellSubTypeRxHandler      spellSubtypeRxHandler;
	@Inject
	protected SpellTypeRxHandler         spellTypeRxHandler;
	@Inject
	protected TalentRxHandler            talentRxHandler;
	@Inject
	protected TalentCategoryRxHandler    talentCategoryRxHandler;
	@Inject
	protected WeaponTemplateRxHandler    weaponTemplateRxHandler;

	@Inject
	public ReactiveUtils() {}

	public Observable<?> getGetAllObservable(Handler handler) {
		Observable<?> result = null;

		switch (handler) {
			case ATTACK_RX_HANDLER:
				result = attackRxHandler.getAll();
				break;
			case BODY_PART_RX_HANDLER:
				result = bodyPartRxHandler.getAll();
				break;
			case CAMPAIGN_RX_HANDLER:
				result = campaignRxHandler.getAll();
				break;
			case CHARACTER_RX_HANDLER:
				result = characterRxHandler.getAll();
				break;
			case CREATURE_RX_HANDLER:
				result = creatureRxHandler.getAll();
				break;
			case CREATURE_ARCHETYPE_RX_HANDLER:
				result = creatureArchetypeRxHandler.getAll();
				break;
			case CREATURE_CATEGORY_RX_HANDLER:
				result = creatureCategoryRxHandler.getAll();
				break;
			case CREATURE_TYPE_RX_HANDLER:
				result = creatureTypeRxHandler.getAll();
				break;
			case CREATURE_VARIETY_RX_HANDLER:
				result = creatureVarietyRxHandler.getAll();
				break;
			case CRITICAL_RESULT_RX_HANDLER:
				result = criticalResultRxHandler.getAll();
				break;
			case CRITICAL_TYPE_RX_HANDLER:
				result = criticalTypeRxHandler.getAll();
				break;
			case CULTURE_RX_HANDLER:
				result = cultureRxHandler.getAll();
				break;
			case DAMAGE_RESULT_RX_HANDLER:
				result = damageResultRxHandler.getAll();
				break;
			case DAMAGE_RESULT_ROW_RX_HANDLER:
				result = damageResultRowRxHandler.getAll();
				break;
			case DAMAGE_TABLE_RX_HANDLER:
				result = damageTableRxHandler.getAll();
				break;
			case DISEASE_RX_HANDLER:
				result = diseaseRxHandler.getAll();
				break;
			case FILE_RX_HANDLER:
				result = null;
				break;
			case IMPORT_EXPORT_RX_HANDLER:
				result = null;
				break;
			case ITEM_RX_HANDLER:
				result = itemRxHandler.getAll();
				break;
			case ITEM_TEMPLATE_RX_HANDLER:
				result = itemTemplateRxHandler.getAll();
				break;
			case OUTLOOK_RX_HANDLER:
				result = outlookRxHandler.getAll();
				break;
			case PROFESSION_RX_HANDLER:
				result = professionRxHandler.getAll();
				break;
			case RACE_RX_HANDLER:
				result = raceRxHandler.getAll();
				break;
			case REALM_RX_HANDLER:
				result = realmRxHandler.getAll();
				break;
			case SIZE_RX_HANDLER:
				result = sizeRxHandler.getAll();
				break;
			case SKILL_RX_HANDLER:
				result = skillRxHandler.getAll();
				break;
			case SKILL_CATEGORY_RX_HANDLER:
				result = skillCategoryRxHandler.getAll();
				break;
			case SPECIALIZATION_RX_HANDLER:
				result = specializationRxHandler.getAll();
				break;
			case SPELL_RX_HANDLER:
				result = spellRxHandler.getAll();
				break;
			case SPELL_LIST_RX_HANDLER:
				result = spellListRxHandler.getAll();
				break;
			case SPELL_SUBTYPE_RX_HANDLER:
				result = spellSubtypeRxHandler.getAll();
				break;
			case SPELL_TYPE_RX_HANDLER:
				result = spellTypeRxHandler.getAll();
				break;
			case TALENT_RX_HANDLER:
				result = talentRxHandler.getAll();
				break;
			case TALENT_CATEGORY_RX_HANDLER:
				result = talentCategoryRxHandler.getAll();
				break;
			case WEAPON_TEMPLATE_RX_HANDLER:
				result = weaponTemplateRxHandler.getAll();
				break;
		}

		return result;
	}
}

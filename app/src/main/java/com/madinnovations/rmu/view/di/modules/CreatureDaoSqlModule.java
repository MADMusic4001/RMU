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
package com.madinnovations.rmu.view.di.modules;

import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.OutlookDao;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureArchetypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureCategoryDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureTypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureVarietyDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.OutlookDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.RealmDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of creature package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CreatureDaoSqlModule {
	@Provides @Singleton
	CreatureArchetypeDao provideCreatureArchetypeDao(RMUDatabaseHelper helper, StatDao statDao, SkillCategoryDao skillCategoryDao) {
		return new CreatureArchetypeDaoDbImpl(helper, statDao, skillCategoryDao);
	}
	@Provides @Singleton
	CreatureCategoryDao provideCreatureCategoryDao(RMUDatabaseHelper helper, TalentDao talentDao) {
		return new CreatureCategoryDaoDbImpl(helper, talentDao);
	}
	@Provides @Singleton
	CreatureDao provideCreatureDao(RMUDatabaseHelper helper, CreatureVarietyDao creatureVarietyDao) {
		return new CreatureDaoDbImpl(helper, creatureVarietyDao);
	}
	@Provides @Singleton
	CreatureTypeDao provideCreatureTypeDao(RMUDatabaseHelper helper, CreatureCategoryDao creatureCategoryDao,
												  TalentDao talentDao) {
		return new CreatureTypeDaoDbImpl(helper, creatureCategoryDao, talentDao);
	}
	@Provides @Singleton
	CreatureVarietyDao provideCreatureVarietyDao(RMUDatabaseHelper helper, CreatureTypeDao creatureTypeDao, SizeDao sizeDao,
														CriticalCodeDao criticalCodeDao, RealmDao realmDao, OutlookDao outlookDao,
														StatDao statDao, SkillDao skillDao, TalentDao talentDao, AttackDao attackDao) {
		return new CreatureVarietyDaoDbImpl(helper, creatureTypeDao, sizeDao, criticalCodeDao, realmDao, outlookDao, statDao, skillDao,
				talentDao, attackDao);
	}
	@Provides @Singleton
	OutlookDao provideOutlookDao(RMUDatabaseHelper helper) {
		return new OutlookDaoDbImpl(helper);
	}
}

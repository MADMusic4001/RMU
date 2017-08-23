/*
  Copyright (C) 2016 MadInnovations
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.view.di.modules;

import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.combat.AdditionalEffectDao;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.DiseaseDao;
import com.madinnovations.rmu.data.dao.combat.impl.AdditionalEffectDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.AttackDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.CriticalResultDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageResultDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageResultRowDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageTableDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DiseaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of combat package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CombatDaoSqlModule {
	@Provides @Singleton
	AttackDao provideAttackDao(RMUDatabaseHelper helper, DamageTableDao damageTableDao, SpecializationDao specializationDao) {
		return new AttackDaoDbImpl(helper, damageTableDao, specializationDao);
	}
	@Provides @Singleton
	AdditionalEffectDao provideAdditionalEffectDao(RMUDatabaseHelper helper, SkillDao skillDao,
												   SpecializationDao specializationDao) {
		return new AdditionalEffectDaoDbImpl(helper, skillDao, specializationDao);
	}
	@Provides @Singleton
	CriticalResultDao provideCriticalResultDao(RMUDatabaseHelper helper, AdditionalEffectDao additionalEffectDao) {
		return new CriticalResultDaoDbImpl(helper, additionalEffectDao);
	}
	@Provides @Singleton
	DamageResultDao provideDamageResultDao(RMUDatabaseHelper helper, DamageResultRowDao damageResultRowDao) {
		return new DamageResultDaoDbImpl(helper, damageResultRowDao);
	}
	@Provides @Singleton
	DamageResultRowDao provideDamageResultRowDao(RMUDatabaseHelper helper, DamageTableDao damageTableDao) {
		return new DamageResultRowDaoDbImpl(helper, damageTableDao);
	}
	@Provides @Singleton
	DamageTableDao provideDamageTableDao(RMUDatabaseHelper helper) {
		return new DamageTableDaoDbImpl(helper);
	}
	@Provides @Singleton
	DiseaseDao provideDiseaseDao(RMUDatabaseHelper helper) {
		return new DiseaseDaoDbImpl(helper);
	}
}

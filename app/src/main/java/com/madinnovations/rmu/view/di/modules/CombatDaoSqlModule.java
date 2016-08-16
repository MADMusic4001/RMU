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
import com.madinnovations.rmu.data.dao.combat.BodyPartDao;
import com.madinnovations.rmu.data.dao.combat.CriticalCodeDao;
import com.madinnovations.rmu.data.dao.combat.CriticalResultDao;
import com.madinnovations.rmu.data.dao.combat.CriticalTypeDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultDao;
import com.madinnovations.rmu.data.dao.combat.DamageResultRowDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.impl.BodyPartDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.CriticalCodeDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.CriticalResultDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.CriticalTypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageResultDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageResultRowDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.impl.DamageTableDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of combat package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CombatDaoSqlModule {
	@Provides @Singleton
	public BodyPartDao provideBodyPartDao(RMUDatabaseHelper helper) {
		return new BodyPartDaoDbImpl(helper);
	}
	@Provides @Singleton
	public CriticalCodeDao provideCriticalCodeDao(RMUDatabaseHelper helper) {
		return new CriticalCodeDaoDbImpl(helper);
	}
	@Provides @Singleton
	public CriticalResultDao provideCriticalResultDao(RMUDatabaseHelper helper, BodyPartDao bodyPartDao, CriticalTypeDao criticalTypeDao) {
		return new CriticalResultDaoDbImpl(helper, bodyPartDao, criticalTypeDao);
	}
	@Provides @Singleton
	public CriticalTypeDao provideCriticalTypeDao(RMUDatabaseHelper helper) {
		return new CriticalTypeDaoDbImpl(helper);
	}
	@Provides @Singleton
	public DamageResultDao provideDamageResultDao(RMUDatabaseHelper helper, CriticalTypeDao criticalTypeDao) {
		return new DamageResultDaoDbImpl(helper, criticalTypeDao);
	}
	@Provides @Singleton
	public DamageResultRowDao provideDamageResultRowDao(RMUDatabaseHelper helper, DamageTableDao damageTableDao,
														DamageResultDao damageResultDao) {
		return new DamageResultRowDaoDbImpl(helper, damageTableDao, damageResultDao);
	}
	@Provides @Singleton
	public DamageTableDao provideDamageTableDao(RMUDatabaseHelper helper) {
		return new DamageTableDaoDbImpl(helper);
	}
}

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
import com.madinnovations.rmu.data.dao.creature.CreatureArchetypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureCategoryDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.creature.CreatureTypeDao;
import com.madinnovations.rmu.data.dao.creature.CreatureVarietyDao;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureArchetypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureCategoryDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureTypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.creature.impl.CreatureVarietyDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of creature package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CreatureDaoSqlModule {
	@Provides @Singleton
	public CreatureArchetypeDao provideCreatureArchetypeDao(RMUDatabaseHelper helper) {
		return new CreatureArchetypeDaoDbImpl(helper);
	}

	@Provides @Singleton
	public CreatureCategoryDao provideCreatureCategoryDao(RMUDatabaseHelper helper) {
		return new CreatureCategoryDaoDbImpl(helper);
	}

	@Provides @Singleton
	public CreatureDao provideCreatureDao(RMUDatabaseHelper helper) {
		return new CreatureDaoDbImpl(helper);
	}

	@Provides @Singleton
	public CreatureTypeDao provideCreatureTypeDao(RMUDatabaseHelper helper) {
		return new CreatureTypeDaoDbImpl(helper);
	}

	@Provides @Singleton
	public CreatureVarietyDao provideCreatureVarietyDao(RMUDatabaseHelper helper) {
		return new CreatureVarietyDaoDbImpl(helper);
	}
}

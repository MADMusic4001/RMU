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
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.impl.CharacterDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.CultureDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.ProfessionDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.RaceDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.MovementDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.impl.TalentCategoryDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of character package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CharacterDaoSqlModule {
	@Provides @Singleton
	public CharacterDao provideCharacterDao(RMUDatabaseHelper helper, RaceDao raceDao, SkillDao skillDao,
											TalentDao talentDao, StatDao statDao) {
		return new CharacterDaoDbImpl(helper, raceDao, skillDao, talentDao, statDao);
	}

	@Provides @Singleton
	public CultureDao provideCultureDao(RMUDatabaseHelper helper) {
		return new CultureDaoDbImpl(helper);
	}

	@Provides @Singleton
	public ProfessionDao provideProfessionDao(RMUDatabaseHelper helper) {
		return new ProfessionDaoDbImpl(helper);
	}

	@Provides @Singleton
	public RaceDao provideRaceDao(RMUDatabaseHelper helper, TalentDao talentDao, MovementDao movementDao) {
		return new RaceDaoDbImpl(helper, talentDao, movementDao);
	}
}

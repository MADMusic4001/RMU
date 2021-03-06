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
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.character.CultureDao;
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.impl.CharacterDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.CultureDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.ProfessionDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.impl.RaceDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of character package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CharacterDaoSqlModule {
	@Provides @Singleton
	CharacterDao provideCharacterDao(RMUDatabaseHelper helper, RaceDao raceDao, SkillDao skillDao, TalentDao talentDao,
									 CultureDao cultureDao, ProfessionDao professionDao, ItemDao itemDao,
									 SpecializationDao specializationDao, CampaignDao campaignDao, SpellListDao spellListDao) {
		return new CharacterDaoDbImpl(helper, raceDao, skillDao, talentDao, cultureDao, professionDao, itemDao,
				specializationDao, campaignDao, spellListDao);
	}

	@Provides @Singleton
	CultureDao provideCultureDao(RMUDatabaseHelper helper, SkillDao skillDao) {
		return new CultureDaoDbImpl(helper, skillDao);
	}

	@Provides @Singleton
	ProfessionDao provideProfessionDao(RMUDatabaseHelper helper, SkillCategoryDao skillCategoryDao, SkillDao skillDao) {
		return new ProfessionDaoDbImpl(helper, skillCategoryDao, skillDao);
	}

	@Provides @Singleton
	RaceDao provideRaceDao(RMUDatabaseHelper helper, TalentDao talentDao, CultureDao cultureDao) {
		return new RaceDaoDbImpl(helper, talentDao, cultureDao);
	}
}

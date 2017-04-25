/**
 * Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.view.di.modules;

import com.madinnovations.rmu.data.dao.RMUDatabaseHelper;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.character.CharacterDao;
import com.madinnovations.rmu.data.dao.creature.CreatureDao;
import com.madinnovations.rmu.data.dao.play.EncounterSetupDao;
import com.madinnovations.rmu.data.dao.play.impl.EncounterSetupDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of play package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class PlayDaoSqlModule {
	@Provides
	@Singleton
	EncounterSetupDao provideCombatSetupDao(RMUDatabaseHelper helper, CharacterDao characterDao, CreatureDao creatureDao,
											CampaignDao campaignDao) {
		return new EncounterSetupDaoDbImpl(helper, characterDao, creatureDao, campaignDao);
	}
}

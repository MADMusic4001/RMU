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
import com.madinnovations.rmu.data.dao.character.ProfessionDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.spells.RealmDao;
import com.madinnovations.rmu.data.dao.spells.SpellDao;
import com.madinnovations.rmu.data.dao.spells.SpellListDao;
import com.madinnovations.rmu.data.dao.spells.SpellListTypeDao;
import com.madinnovations.rmu.data.dao.spells.SpellSubTypeDao;
import com.madinnovations.rmu.data.dao.spells.SpellTypeDao;
import com.madinnovations.rmu.data.dao.spells.impl.RealmDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.impl.SpellDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.impl.SpellListDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.impl.SpellListTypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.impl.SpellSubTypeDaoDbImpl;
import com.madinnovations.rmu.data.dao.spells.impl.SpellTypeDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of item package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class SpellDaoSqlModule {
	@Provides @Singleton
	public RealmDao provideRealmDao(RMUDatabaseHelper helper, StatDao statDao) {
		return new RealmDaoDbImpl(helper, statDao);
	}
	@Provides @Singleton
	public SpellDao provideSpellDao(RMUDatabaseHelper helper, SpellListDao spellListDao, SpellTypeDao spellTypeDao,
									SpellSubTypeDao spellSubTypeDao) {
		return new SpellDaoDbImpl(helper, spellListDao, spellTypeDao, spellSubTypeDao);
	}
	@Provides @Singleton
	public SpellListDao provideSpellListDao(RMUDatabaseHelper helper, RealmDao realmDao, SpellListTypeDao spellListTypeDao,
											ProfessionDao professionDao) {
		return new SpellListDaoDbImpl(helper, realmDao, spellListTypeDao, professionDao);
	}
	@Provides @Singleton
	public SpellListTypeDao provideSpellListTypeDao(RMUDatabaseHelper helper) {
		return new SpellListTypeDaoDbImpl(helper);
	}
	@Provides @Singleton
	public SpellSubTypeDao provideSpellSubTypeDao(RMUDatabaseHelper helper) {
		return new SpellSubTypeDaoDbImpl(helper);
	}
	@Provides @Singleton
	public SpellTypeDao provideSpellTypeDao(RMUDatabaseHelper helper, SpellSubTypeDao spellSubTypeDao) {
		return new SpellTypeDaoDbImpl(helper);
	}
}

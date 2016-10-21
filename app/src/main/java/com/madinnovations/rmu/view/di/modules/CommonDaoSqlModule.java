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
import com.madinnovations.rmu.data.dao.common.SizeDao;
import com.madinnovations.rmu.data.dao.common.SkillCategoryDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.dao.common.StatDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.impl.SizeDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.SkillCategoryDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.SkillDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.SpecializationDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.StatDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.TalentCategoryDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.impl.TalentDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of common package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class CommonDaoSqlModule {
	@Provides @Singleton
	SizeDao provideSizeDao(RMUDatabaseHelper helper) {
		return new SizeDaoDbImpl(helper);
	}

	@Provides @Singleton
	SkillCategoryDao provideSkillCategoryDao(RMUDatabaseHelper helper) {
		return new SkillCategoryDaoDbImpl(helper);
	}

	@Provides @Singleton
	SkillDao provideSkillDao(RMUDatabaseHelper helper, SkillCategoryDao skillCategoryDao) {
		return new SkillDaoDbImpl(helper, skillCategoryDao);
	}

	@Provides @Singleton
	SpecializationDao provideSpecializationDao(RMUDatabaseHelper helper, SkillDao skillDao) {
		return new SpecializationDaoDbImpl(helper, skillDao);
	}

	@Provides @Singleton
	StatDao provideStatDao(RMUDatabaseHelper helper) {
		return new StatDaoDbImpl(helper);
	}

	@Provides @Singleton
	TalentCategoryDao provideTalentCategoryDao(RMUDatabaseHelper helper) {
		return new TalentCategoryDaoDbImpl(helper);
	}

	@Provides @Singleton
	TalentDao provideTalentDao(RMUDatabaseHelper helper, TalentCategoryDao talentCategoryDao) {
		return new TalentDaoDbImpl(helper, talentCategoryDao);
	}
}

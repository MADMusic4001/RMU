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
import com.madinnovations.rmu.data.dao.item.ItemDao;
import com.madinnovations.rmu.data.dao.item.ItemTemplateDao;
import com.madinnovations.rmu.data.dao.item.WeaponTemplateDao;
import com.madinnovations.rmu.data.dao.item.impl.ItemDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.impl.ItemTemplateDaoDbImpl;
import com.madinnovations.rmu.data.dao.item.impl.WeaponTemplateDaoDbImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provides instances of item package DAO SQL implementation classes for dependency injection.
 */
@Module(includes = ApplicationModule.class)
public class ItemDaoSqlModule {
	@Provides @Singleton
	ItemDao provideItemDao(RMUDatabaseHelper helper, ItemTemplateDao itemTemplateDao) {
		return new ItemDaoDbImpl(helper, itemTemplateDao);
	}

	@Provides @Singleton
	ItemTemplateDao provideItemTemplateDap(RMUDatabaseHelper helper) {
		return new ItemTemplateDaoDbImpl(helper);
	}

	@Provides @Singleton
	WeaponTemplateDao provideWeaponTemplateDao(RMUDatabaseHelper helper) {
		return new WeaponTemplateDaoDbImpl(helper);
	}
}

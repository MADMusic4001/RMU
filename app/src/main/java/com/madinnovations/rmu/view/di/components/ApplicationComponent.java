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
package com.madinnovations.rmu.view.di.components;

import com.madinnovations.rmu.view.di.modules.ActivityModule;
import com.madinnovations.rmu.view.di.modules.ApplicationModule;
import com.madinnovations.rmu.view.di.modules.CampaignDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.CharacterDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.CombatDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.CommonDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.CreatureDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.ItemDaoSqlModule;
import com.madinnovations.rmu.view.di.modules.SpellDaoSqlModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The ApplicationComponent dependency injection interface
 */
@Singleton
@Component(modules = {ApplicationModule.class, CampaignDaoSqlModule.class, CharacterDaoSqlModule.class, CommonDaoSqlModule.class,
		CombatDaoSqlModule.class, CreatureDaoSqlModule.class, ItemDaoSqlModule.class, SpellDaoSqlModule.class})
public interface ApplicationComponent {
	ActivityComponent newActivityComponent(ActivityModule activityModule);
}

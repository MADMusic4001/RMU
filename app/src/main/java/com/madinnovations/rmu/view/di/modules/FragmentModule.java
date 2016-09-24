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

import com.madinnovations.rmu.view.activities.campaign.AboutFragment;
import com.madinnovations.rmu.view.activities.campaign.MainMenuFragment;
import com.madinnovations.rmu.view.activities.common.ParametersFragment;
import com.madinnovations.rmu.view.activities.common.StatsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoriesFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into Fragment instances.
 */
@Module
public class FragmentModule {
	private AboutFragment      aboutFragment;
	private MainMenuFragment   mainMenuFragment;
	private ParametersFragment parametersFragment;
	private StatsFragment statsFragment;
	private TalentCategoriesFragment talentCategoriesFragment;

	public FragmentModule(AboutFragment aboutFragment) {
		this.aboutFragment = aboutFragment;
	}
	public FragmentModule(MainMenuFragment mainMenuFragment) {
		this.mainMenuFragment = mainMenuFragment;
	}
	public FragmentModule(ParametersFragment parametersFragment) {
		this.parametersFragment = parametersFragment;
	}
	public FragmentModule(StatsFragment statsFragment) {
		this.statsFragment = statsFragment;
	}
	public FragmentModule(TalentCategoriesFragment talentCategoriesFragment) {
		this.talentCategoriesFragment = talentCategoriesFragment;
	}

	@Provides @PerFragment
	public AboutFragment provideAboutFragment() {
		return this.aboutFragment;
	}
	@Provides @PerFragment
	public MainMenuFragment provideMainMenuFragment() {
		return this.mainMenuFragment;
	}
	@Provides @PerFragment
	public ParametersFragment provideParametersFragment() {
		return this.parametersFragment;
	}
	@Provides @PerFragment
	public StatsFragment provideStatsFragment() {
		return this.statsFragment;
	}
	@Provides @PerFragment
	public TalentCategoriesFragment provideCommonDataFragment() {
		return this.talentCategoriesFragment;
	}
}

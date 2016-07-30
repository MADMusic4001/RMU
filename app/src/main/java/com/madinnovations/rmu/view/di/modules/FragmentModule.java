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
import com.madinnovations.rmu.view.activities.common.LocomotionTypesFragment;
import com.madinnovations.rmu.view.activities.common.ParametersFragment;
import com.madinnovations.rmu.view.activities.common.StatsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoryFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to get instances of Fragment instances for injection into other
 * classes.
 */
@Module
public class FragmentModule {
	private AboutFragment      aboutFragment;
	private MainMenuFragment   mainMenuFragment;
	private LocomotionTypesFragment locomotionTypesFragment;
	private ParametersFragment parametersFragment;
	private StatsFragment statsFragment;
	private TalentCategoryFragment talentCategoryFragment;

	public FragmentModule(AboutFragment aboutFragment) {
		this.aboutFragment = aboutFragment;
	}
	public FragmentModule(MainMenuFragment mainMenuFragment) {
		this.mainMenuFragment = mainMenuFragment;
	}
	public FragmentModule(LocomotionTypesFragment locomotionTypesFragment) {
		this.locomotionTypesFragment = locomotionTypesFragment;
	}
	public FragmentModule(ParametersFragment parametersFragment) {
		this.parametersFragment = parametersFragment;
	}
	public FragmentModule(StatsFragment statsFragment) {
		this.statsFragment = statsFragment;
	}
	public FragmentModule(TalentCategoryFragment talentCategoryFragment) {
		this.talentCategoryFragment = talentCategoryFragment;
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
	public LocomotionTypesFragment provideLocomotionTypesFragment() {
		return this.locomotionTypesFragment;
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
	public TalentCategoryFragment provideCommonDataFragment() {
		return this.talentCategoryFragment;
	}
}

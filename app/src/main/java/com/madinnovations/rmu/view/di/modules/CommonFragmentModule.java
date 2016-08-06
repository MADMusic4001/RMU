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

import com.madinnovations.rmu.view.activities.common.LocomotionTypesFragment;
import com.madinnovations.rmu.view.activities.common.ParametersFragment;
import com.madinnovations.rmu.view.activities.common.SizesFragment;
import com.madinnovations.rmu.view.activities.common.StatsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoryFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into common package Fragment instances.
 */
@Module
public class CommonFragmentModule {
	private LocomotionTypesFragment locomotionTypesFragment;
	private ParametersFragment      parametersFragment;
	private SizesFragment           sizesFragment;
	private StatsFragment           statsFragment;
	private TalentCategoryFragment  talentCategoryFragment;
	private TalentsFragment         talentsFragment;

	public CommonFragmentModule(LocomotionTypesFragment locomotionTypesFragment) {
		this.locomotionTypesFragment = locomotionTypesFragment;
	}
	public CommonFragmentModule(ParametersFragment parametersFragment) {
		this.parametersFragment = parametersFragment;
	}
	public CommonFragmentModule(SizesFragment sizesFragment) {
		this.sizesFragment = sizesFragment;
	}
	public CommonFragmentModule(StatsFragment statsFragment) {
		this.statsFragment = statsFragment;
	}
	public CommonFragmentModule(TalentCategoryFragment talentCategoryFragment) {
		this.talentCategoryFragment = talentCategoryFragment;
	}
	public CommonFragmentModule(TalentsFragment talentsFragment) {
		this.talentsFragment = talentsFragment;
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
	public SizesFragment provideSizesFragment() {
		return this.sizesFragment;
	}
	@Provides @PerFragment
	public StatsFragment provideStatsFragment() {
		return this.statsFragment;
	}
	@Provides @PerFragment
	public TalentCategoryFragment provideTalentCategoryFragment() {
		return this.talentCategoryFragment;
	}
	@Provides @PerFragment
	public TalentsFragment provideTalentsFragment() {
		return this.talentsFragment;
	}
}

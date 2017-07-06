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

import com.madinnovations.rmu.view.activities.common.SkillCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.SkillsFragment;
import com.madinnovations.rmu.view.activities.common.SpecializationsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into common package Fragment instances.
 */
@Module
public class CommonFragmentModule {
	private SkillCategoriesFragment  skillCategoriesFragment;
	private SkillsFragment           skillsFragment;
	private SpecializationsFragment  specializationsFragment;
	private TalentCategoriesFragment talentCategoriesFragment;
	private TalentsFragment          talentsFragment;

	public CommonFragmentModule(SkillCategoriesFragment skillCategoriesFragment) {
		this.skillCategoriesFragment = skillCategoriesFragment;
	}
	public CommonFragmentModule(SkillsFragment skillsFragment) {
		this.skillsFragment = skillsFragment;
	}
	public CommonFragmentModule(SpecializationsFragment specializationsFragment) {
		this.specializationsFragment = specializationsFragment;
	}
	public CommonFragmentModule(TalentCategoriesFragment talentCategoriesFragment) {
		this.talentCategoriesFragment = talentCategoriesFragment;
	}
	public CommonFragmentModule(TalentsFragment talentsFragment) {
		this.talentsFragment = talentsFragment;
	}

	@Provides @PerFragment
	SkillCategoriesFragment provideSkillCategoriesFragment() {
		return this.skillCategoriesFragment;
	}
	@Provides @PerFragment
	SkillsFragment provideSkillsFragment() {
		return this.skillsFragment;
	}
	@Provides @PerFragment
	SpecializationsFragment provideSpecializationsFragment() {
		return this.specializationsFragment;
	}
	@Provides @PerFragment
	TalentCategoriesFragment provideTalentCategoryFragment() {
		return this.talentCategoriesFragment;
	}
	@Provides @PerFragment
	TalentsFragment provideTalentsFragment() {
		return this.talentsFragment;
	}
}

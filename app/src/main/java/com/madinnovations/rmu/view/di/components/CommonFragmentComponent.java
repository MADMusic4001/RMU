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
package com.madinnovations.rmu.view.di.components;

import com.madinnovations.rmu.view.activities.common.SkillCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.SkillsFragment;
import com.madinnovations.rmu.view.activities.common.SpecializationsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoriesFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import dagger.Subcomponent;

/**
 * The CommonFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = CommonFragmentModule.class)
public interface CommonFragmentComponent {
	void injectInto(SkillCategoriesFragment skillCategoriesFragment);
	void injectInto(SkillsFragment skillsFragment);
	void injectInto(SpecializationsFragment specializationsFragment);
	void injectInto(TalentCategoriesFragment talentCategoriesFragment);
	void injectInto(TalentsFragment talentsFragment);
}

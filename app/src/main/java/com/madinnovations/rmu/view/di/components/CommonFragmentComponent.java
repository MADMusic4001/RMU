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

import com.madinnovations.rmu.view.activities.common.LocomotionTypesFragment;
import com.madinnovations.rmu.view.activities.common.ParametersFragment;
import com.madinnovations.rmu.view.activities.common.SizesFragment;
import com.madinnovations.rmu.view.activities.common.SkillCostsFragment;
import com.madinnovations.rmu.view.activities.common.StatsFragment;
import com.madinnovations.rmu.view.activities.common.TalentCategoryFragment;
import com.madinnovations.rmu.view.activities.common.TalentsFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;

import dagger.Subcomponent;

/**
 * The CommonFragmentComponent dependency injection interface.
 */
@PerFragment
@Subcomponent(modules = CommonFragmentModule.class)
public interface CommonFragmentComponent {
	public void injectInto(LocomotionTypesFragment locomotionTypesFragment);
	public void injectInto(ParametersFragment parametersFragment);
	public void injectInto(SizesFragment sizesFragment);
	public void injectInto(SkillCostsFragment skillCostsFragment);
	public void injectInto(StatsFragment statsFragment);
	public void injectInto(TalentCategoryFragment talentCategoryFragment);
	public void injectInto(TalentsFragment talentsFragment);
}

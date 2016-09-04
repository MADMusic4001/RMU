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

import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureCategoriesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureTypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyAttackPageFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyMainPageFragment;
import com.madinnovations.rmu.view.activities.creature.OutlooksFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;

import dagger.Subcomponent;

/**
 * The CreatureFragmentComponent dependency injection interface.
 */
@PerFragment
@Subcomponent(modules = CreatureFragmentModule.class)
public interface CreatureFragmentComponent {
	public void injectInto(CreatureArchetypesFragment creatureArchetypesFragment);
	public void injectInto(CreatureCategoriesFragment creatureCategoriesFragment);
	public void injectInto(CreatureTypesFragment creatureTypesFragment);
	public void injectInto(CreatureVarietiesFragment creatureVarietiesFragment);
	public void injectInto(CreatureVarietyAttackPageFragment creatureVarietyAttackPageFragment);
	public void injectInto(CreatureVarietyMainPageFragment creatureVarietyMainPageFragment);
	public void injectInto(OutlooksFragment outlooksFragment);
}

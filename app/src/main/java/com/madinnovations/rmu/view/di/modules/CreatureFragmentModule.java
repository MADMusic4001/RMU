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

import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesLevelsFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureArchetypesMainPageFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureCategoriesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureTypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyAttackPageFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietyMainPageFragment;
import com.madinnovations.rmu.view.activities.creature.OutlooksFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into creature package Fragment instances.
 */
@Module
public class CreatureFragmentModule {
	private CreatureArchetypesFragment creatureArchetypesFragment;
	private CreatureArchetypesLevelsFragment creatureArchetypesLevelsFragment;
	private CreatureArchetypesMainPageFragment creatureArchetypesMainPageFragment;
	private CreatureCategoriesFragment creatureCategoriesFragment;
	private CreatureTypesFragment      creatureTypesFragment;
	private CreatureVarietiesFragment  creatureVarietiesFragment;
	private CreatureVarietyAttackPageFragment creatureVarietyAttackPageFragment;
	private CreatureVarietyMainPageFragment creatureVarietyMainPageFragment;
	private OutlooksFragment           outlooksFragment;

	public CreatureFragmentModule(CreatureArchetypesFragment creatureArchetypesFragment) {
		this.creatureArchetypesFragment = creatureArchetypesFragment;
	}
	public CreatureFragmentModule(CreatureArchetypesMainPageFragment creatureArchetypesMainPageFragment) {
		this.creatureArchetypesMainPageFragment = creatureArchetypesMainPageFragment;
	}
	public CreatureFragmentModule(CreatureArchetypesLevelsFragment creatureArchetypesLevelsFragment) {
		this.creatureArchetypesLevelsFragment = creatureArchetypesLevelsFragment;
	}
	public CreatureFragmentModule(CreatureCategoriesFragment creatureCategoriesFragment) {
		this.creatureCategoriesFragment = creatureCategoriesFragment;
	}
	public CreatureFragmentModule(CreatureTypesFragment creatureTypesFragment) {
		this.creatureTypesFragment = creatureTypesFragment;
	}
	public CreatureFragmentModule(CreatureVarietiesFragment creatureVarietiesFragment) {
		this.creatureVarietiesFragment = creatureVarietiesFragment;
	}
	public CreatureFragmentModule(CreatureVarietyAttackPageFragment creatureVarietyAttackPageFragment) {
		this.creatureVarietyAttackPageFragment = creatureVarietyAttackPageFragment;
	}
	public CreatureFragmentModule(CreatureVarietyMainPageFragment creatureVarietyMainPageFragment) {
		this.creatureVarietyMainPageFragment = creatureVarietyMainPageFragment;
	}
	public CreatureFragmentModule(OutlooksFragment outlooksFragment) {
		this.outlooksFragment = outlooksFragment;
	}

	@Provides @PerFragment
	CreatureArchetypesFragment provideCreatureArchetypesFragment() {
		return this.creatureArchetypesFragment;
	}
	@Provides @PerFragment
	CreatureArchetypesMainPageFragment provideCreatureArchetypesMainPageFragment() {
		return this.creatureArchetypesMainPageFragment;
	}
	@Provides @PerFragment
	CreatureArchetypesLevelsFragment provideCreatureArchetypesLevelsFragment() {
		return this.creatureArchetypesLevelsFragment;
	}
	@Provides @PerFragment
	CreatureCategoriesFragment provideCreatureCategoriesFragment() {
		return this.creatureCategoriesFragment;
	}
	@Provides @PerFragment
	CreatureTypesFragment provideCreatureTypesFragment() {
		return this.creatureTypesFragment;
	}
	@Provides @PerFragment
	CreatureVarietiesFragment provideCreatureVarietiesFragment() {
		return this.creatureVarietiesFragment;
	}
	@Provides @PerFragment
	CreatureVarietyAttackPageFragment provideCreatureVarietyAttackPageFragment() {
		return this.creatureVarietyAttackPageFragment;
	}
	@Provides @PerFragment
	CreatureVarietyMainPageFragment provideCreatureVarietyMainPageFragment() {
		return this.creatureVarietyMainPageFragment;
	}
	@Provides @PerFragment
	OutlooksFragment provideOutlooksFragment() {
		return this.outlooksFragment;
	}
}

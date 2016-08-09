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
import com.madinnovations.rmu.view.activities.creature.CreatureCategoriesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureTypesFragment;
import com.madinnovations.rmu.view.activities.creature.CreatureVarietiesFragment;
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
	private CreatureCategoriesFragment creatureCategoriesFragment;
	private CreatureTypesFragment      creatureTypesFragment;
	private CreatureVarietiesFragment  creatureVarietiesFragment;
	private OutlooksFragment           outlooksFragment;

	public CreatureFragmentModule(CreatureArchetypesFragment creatureArchetypesFragment) {
		this.creatureArchetypesFragment = creatureArchetypesFragment;
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
	public CreatureFragmentModule(OutlooksFragment outlooksFragment) {
		this.outlooksFragment = outlooksFragment;
	}

	@Provides @PerFragment
	public CreatureArchetypesFragment provideCreatureArchetypesFragment() {
		return this.creatureArchetypesFragment;
	}
	@Provides @PerFragment
	public CreatureCategoriesFragment provideCreatureCategoriesFragment() {
		return this.creatureCategoriesFragment;
	}
	@Provides @PerFragment
	public CreatureTypesFragment provideCreatureTypesFragment() {
		return this.creatureTypesFragment;
	}
	@Provides @PerFragment
	public CreatureVarietiesFragment provideCreatureVarietiesFragment() {
		return this.creatureVarietiesFragment;
	}
	@Provides @PerFragment
	public OutlooksFragment provideOutlooksFragment() {
		return this.outlooksFragment;
	}
}

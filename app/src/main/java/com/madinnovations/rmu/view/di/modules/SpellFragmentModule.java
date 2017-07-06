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

import com.madinnovations.rmu.view.activities.spell.SpellListsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellSubTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into spell package Fragment instances.
 */
@Module
public class SpellFragmentModule {
	private SpellListsFragment     spellListsFragment;
	private SpellsFragment         spellsFragment;
	private SpellSubTypesFragment  spellSubTypesFragment;
	private SpellTypesFragment     spellTypesFragment;

	public SpellFragmentModule(SpellListsFragment spellListsFragment) {
		this.spellListsFragment = spellListsFragment;
	}
	public SpellFragmentModule(SpellsFragment spellsFragment) {
		this.spellsFragment = spellsFragment;
	}
	public SpellFragmentModule(SpellSubTypesFragment spellSubTypesFragment) {
		this.spellSubTypesFragment = spellSubTypesFragment;
	}
	public SpellFragmentModule(SpellTypesFragment spellTypesFragment) {
		this.spellTypesFragment = spellTypesFragment;
	}

	@Provides
	@PerFragment
	SpellListsFragment provideSpellListsFragment() {
		return this.spellListsFragment;
	}
	@Provides
	@PerFragment
	SpellsFragment provideSpellsFragment() {
		return this.spellsFragment;
	}
	@Provides
	@PerFragment
	SpellSubTypesFragment provideSpellSubTypesFragment() {
		return this.spellSubTypesFragment;
	}
	@PerFragment
	public SpellTypesFragment provideSpellTypesFragment() {
		return this.spellTypesFragment;
	}
}

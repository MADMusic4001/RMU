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

import com.madinnovations.rmu.view.activities.spell.RealmsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellListTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellSubTypesFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into spell package Fragment instances.
 */
@Module
public class SpellFragmentModule {
	private RealmsFragment realmsFragment;
	private SpellListTypesFragment spellListTypesFragment;
	private SpellSubTypesFragment spellSubTypesFragment;

	public SpellFragmentModule(RealmsFragment realmsFragment) {
		this.realmsFragment = realmsFragment;
	}
	public SpellFragmentModule(SpellListTypesFragment spellListTypesFragment) {
		this.spellListTypesFragment = spellListTypesFragment;
	}
	public SpellFragmentModule(SpellSubTypesFragment spellSubTypesFragment) {
		this.spellSubTypesFragment = spellSubTypesFragment;
	}

	@Provides
	@PerFragment
	public RealmsFragment provideRealmsFragment() {
		return this.realmsFragment;
	}
	@Provides
	@PerFragment
	public SpellListTypesFragment provideSpellListTypesFragment() {
		return this.spellListTypesFragment;
	}
	@Provides
	@PerFragment
	public SpellSubTypesFragment provideSpellSubTypesFragment() {
		return this.spellSubTypesFragment;
	}
}

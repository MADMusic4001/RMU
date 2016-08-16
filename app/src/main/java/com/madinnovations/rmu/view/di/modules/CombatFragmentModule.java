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

import com.madinnovations.rmu.view.activities.combat.BodyPartsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalCodesFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalResultsFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalTypesFragment;
import com.madinnovations.rmu.view.activities.combat.DamageResultsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into combat Fragment instances.
 */
@Module
public class CombatFragmentModule {
	private BodyPartsFragment bodyPartsFragment;
	private CriticalCodesFragment criticalCodesFragment;
	private CriticalResultsFragment criticalResultsFragment;
	private CriticalTypesFragment criticalTypesFragment;
	private DamageResultsFragment damageResultsFragment;

	public CombatFragmentModule(BodyPartsFragment bodyPartsFragment) {
		this.bodyPartsFragment = bodyPartsFragment;
	}
	public CombatFragmentModule(CriticalCodesFragment criticalCodesFragment) {
		this.criticalCodesFragment = criticalCodesFragment;
	}
	public CombatFragmentModule(CriticalResultsFragment criticalResultsFragment) {
		this.criticalResultsFragment = criticalResultsFragment;
	}
	public CombatFragmentModule(CriticalTypesFragment criticalTypesFragment) {
		this.criticalTypesFragment = criticalTypesFragment;
	}
	public CombatFragmentModule(DamageResultsFragment damageResultsFragment) {
		this.damageResultsFragment = damageResultsFragment;
	}

	@Provides @PerFragment
	public BodyPartsFragment provideBodyPartsFragment() {
		return this.bodyPartsFragment;
	}
	@Provides @PerFragment
	public CriticalCodesFragment provideCriticalCodesFragment() {
		return this.criticalCodesFragment;
	}
	@Provides @PerFragment
	public CriticalResultsFragment provideCriticalResultsFragment() {
		return this.criticalResultsFragment;
	}
	@Provides @PerFragment
	public CriticalTypesFragment provideCriticalTypesFragment() {
		return this.criticalTypesFragment;
	}
	@Provides @PerFragment
	public DamageResultsFragment provideDamageResultsFragment() {
		return this.damageResultsFragment;
	}
}

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

import com.madinnovations.rmu.view.activities.character.CulturesFragment;
import com.madinnovations.rmu.view.activities.character.ProfessionsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into character package Fragment instances.
 */
@Module
public class CharacterFragmentModule {
	private CulturesFragment    culturesFragment;
	private ProfessionsFragment professionsFragment;

	public CharacterFragmentModule(CulturesFragment culturesFragment) {
		this.culturesFragment = culturesFragment;
	}
	public CharacterFragmentModule(ProfessionsFragment professionsFragment) {
		this.professionsFragment = professionsFragment;
	}

	@Provides
	@PerFragment
	public CulturesFragment provideCulturesFragment() {
		return this.culturesFragment;
	}
	@Provides
	@PerFragment
	public ProfessionsFragment provideProfessionsFragment() {
		return this.professionsFragment;
	}
}

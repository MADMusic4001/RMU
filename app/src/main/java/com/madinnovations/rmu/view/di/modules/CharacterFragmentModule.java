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

import com.madinnovations.rmu.view.activities.character.CharacterBackgroundPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterMainPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterSkillsPageFragment;
import com.madinnovations.rmu.view.activities.character.CharactersFragment;
import com.madinnovations.rmu.view.activities.character.CulturesFragment;
import com.madinnovations.rmu.view.activities.character.ProfessionsFragment;
import com.madinnovations.rmu.view.activities.character.RacesFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into character package Fragment instances.
 */
@Module
public class CharacterFragmentModule {
	private CharacterBackgroundPageFragment characterBackgroundPageFragment;
	private CharacterMainPageFragment       characterMainPageFragment;
	private CharactersFragment              charactersFragment;
	private CharacterSkillsPageFragment     characterSkillsPageFragment;
	private CulturesFragment                culturesFragment;
	private ProfessionsFragment             professionsFragment;
	private RacesFragment                   racesFragment;

	public CharacterFragmentModule(CharacterBackgroundPageFragment charactersFragment) {
		this.characterBackgroundPageFragment = characterBackgroundPageFragment;
	}
	public CharacterFragmentModule(CharacterMainPageFragment characterMainPageFragment) {
		this.characterMainPageFragment = characterMainPageFragment;
	}
	public CharacterFragmentModule(CharactersFragment charactersFragment) {
		this.charactersFragment = charactersFragment;
	}
	public CharacterFragmentModule(CharacterSkillsPageFragment characterSkillsPageFragment) {
		this.characterSkillsPageFragment = characterSkillsPageFragment;
	}
	public CharacterFragmentModule(CulturesFragment culturesFragment) {
		this.culturesFragment = culturesFragment;
	}
	public CharacterFragmentModule(ProfessionsFragment professionsFragment) {
		this.professionsFragment = professionsFragment;
	}
	public CharacterFragmentModule(RacesFragment racesFragment) {
		this.racesFragment = racesFragment;
	}

	@Provides
	@PerFragment
	CharacterBackgroundPageFragment provideCharacterBackgroundPageFragment() {
		return this.characterBackgroundPageFragment;
	}
	@Provides
	@PerFragment
	CharacterMainPageFragment provideCharacterMainPageFragment() {
		return this.characterMainPageFragment;
	}
	@Provides
	@PerFragment
	CharactersFragment provideCharactersFragment() {
		return this.charactersFragment;
	}
	@Provides
	@PerFragment
	CharacterSkillsPageFragment provideCharacterSkillsPageFragment() {
		return this.characterSkillsPageFragment;
	}
	@Provides
	@PerFragment
	CulturesFragment provideCulturesFragment() {
		return this.culturesFragment;
	}
	@Provides
	@PerFragment
	ProfessionsFragment provideProfessionsFragment() {
		return this.professionsFragment;
	}
	@Provides
	@PerFragment
	RacesFragment provideRacesFragment() {
		return this.racesFragment;
	}
}

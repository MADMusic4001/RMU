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
import com.madinnovations.rmu.view.activities.character.CharacterGeneratedValuesFragment;
import com.madinnovations.rmu.view.activities.character.CharacterMainPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterProfessionalKnacksPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterSkillsPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterTalentsPageFragment;
import com.madinnovations.rmu.view.activities.character.CharactersFragment;
import com.madinnovations.rmu.view.activities.character.CulturesFragment;
import com.madinnovations.rmu.view.activities.character.ProfessionsFragment;
import com.madinnovations.rmu.view.activities.character.RacesCulturesPageFragment;
import com.madinnovations.rmu.view.activities.character.RacesFragment;
import com.madinnovations.rmu.view.activities.character.RacesMainPageFragment;
import com.madinnovations.rmu.view.activities.character.RacesTalentsPageFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into character package Fragment instances.
 */
@Module
public class CharacterFragmentModule {
	private CharacterBackgroundPageFragment  characterBackgroundPageFragment;
	private CharacterGeneratedValuesFragment characterGeneratedValuesFragment;
	private CharacterMainPageFragment        characterMainPageFragment;
	private CharactersFragment               charactersFragment;
	private CharacterSkillsPageFragment      characterSkillsPageFragment;
	private CharacterProfessionalKnacksPageFragment characterProfessionalKnacksPageFragment;
	private CharacterTalentsPageFragment     characterTalentsPageFragment;
	private CulturesFragment                 culturesFragment;
	private ProfessionsFragment              professionsFragment;
	private RacesFragment                    racesFragment;
	private RacesMainPageFragment            racesMainPageFragment;
	private RacesTalentsPageFragment         racesTalentsPageFragment;
	private RacesCulturesPageFragment        racesCulturesPageFragment;

	public CharacterFragmentModule(CharacterBackgroundPageFragment characterBackgroundPageFragment) {
		this.characterBackgroundPageFragment = characterBackgroundPageFragment;
	}
	public CharacterFragmentModule(CharacterGeneratedValuesFragment characterGeneratedValuesFragment) {
		this.characterGeneratedValuesFragment = characterGeneratedValuesFragment;
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
	public CharacterFragmentModule(CharacterProfessionalKnacksPageFragment characterProfessionalKnacksPageFragment) {
		this.characterProfessionalKnacksPageFragment = characterProfessionalKnacksPageFragment;
	}
	public CharacterFragmentModule(CharacterTalentsPageFragment characterTalentsPageFragment) {
		this.characterTalentsPageFragment = characterTalentsPageFragment;
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
	public CharacterFragmentModule(RacesMainPageFragment racesMainPageFragment) {
		this.racesMainPageFragment = racesMainPageFragment;
	}
	public CharacterFragmentModule(RacesTalentsPageFragment racesTalentsPageFragment) {
		this.racesTalentsPageFragment = racesTalentsPageFragment;
	}
	public CharacterFragmentModule(RacesCulturesPageFragment racesCulturesPageFragment) {
		this.racesCulturesPageFragment = racesCulturesPageFragment;
	}

	@Provides
	@PerFragment
	CharacterBackgroundPageFragment provideCharacterBackgroundPageFragment() {
		return this.characterBackgroundPageFragment;
	}
	@Provides
	@PerFragment
	CharacterGeneratedValuesFragment provideCharacterGeneratedValuesFragment() {
		return this.characterGeneratedValuesFragment;
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
	CharacterProfessionalKnacksPageFragment provideCharacterProfessionalKnacksPageFragment() {
		return this.characterProfessionalKnacksPageFragment;
	}
	@Provides
	@PerFragment
	CharacterTalentsPageFragment provideCharacterTalentsPageFragment() {
		return this.characterTalentsPageFragment;
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
	@Provides
	@PerFragment
	RacesMainPageFragment provideRacesMainPageFragment() {
		return this.racesMainPageFragment;
	}
	@Provides
	@PerFragment
	RacesTalentsPageFragment provideRacesTalentsPageFragment() {
		return this.racesTalentsPageFragment;
	}
	@Provides
	@PerFragment
	RacesCulturesPageFragment provideRacesCulturesPageFragment() {
		return this.racesCulturesPageFragment;
	}
}

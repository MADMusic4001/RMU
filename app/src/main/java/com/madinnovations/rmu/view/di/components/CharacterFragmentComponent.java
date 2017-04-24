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

import com.madinnovations.rmu.view.activities.character.CharacterBackgroundPageFragment;
import com.madinnovations.rmu.view.activities.character.CharacterEquipmentPageFragment;
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
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;

import dagger.Subcomponent;

/**
 * The CharacterFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = CharacterFragmentModule.class)
public interface CharacterFragmentComponent {
	void injectInto(CharacterBackgroundPageFragment characterBackgroundPageFragment);
	void injectInto(CharacterEquipmentPageFragment characterEquipmentPageFragment);
	void injectInto(CharacterGeneratedValuesFragment characterGeneratedValuesFragment);
	void injectInto(CharacterMainPageFragment characterMainPageFragment);
	void injectInto(CharactersFragment charactersFragment);
	void injectInto(CharacterSkillsPageFragment characterSkillsPageFragment);
	void injectInto(CharacterProfessionalKnacksPageFragment characterProfessionalKnacksPageFragment);
	void injectInto(CharacterTalentsPageFragment characterTalentsPageFragment);
	void injectInto(CulturesFragment culturesFragment);
	void injectInto(ProfessionsFragment professionsFragment);
	void injectInto(RacesFragment racesFragment);
	void injectInto(RacesMainPageFragment racesMainPageFragment);
	void injectInto(RacesTalentsPageFragment racesTalentsPageFragment);
	void injectInto(RacesCulturesPageFragment racesCulturesPageFragment);
}

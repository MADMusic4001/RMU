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

import com.madinnovations.rmu.view.activities.spell.RealmsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellListsFragment;
import com.madinnovations.rmu.view.activities.spell.SpellSubTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellTypesFragment;
import com.madinnovations.rmu.view.activities.spell.SpellsFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.SpellFragmentModule;

import dagger.Subcomponent;

/**
 * The SpellFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = SpellFragmentModule.class)
public interface SpellFragmentComponent {
	void injectInto(RealmsFragment realmsFragment);
	void injectInto(SpellListsFragment spellListsFragment);
	void injectInto(SpellsFragment spellsFragment);
	void injectInto(SpellSubTypesFragment spellSubTypesFragment);
	void injectInto(SpellTypesFragment spellTypesFragment);
}

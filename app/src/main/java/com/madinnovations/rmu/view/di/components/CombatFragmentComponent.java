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
package com.madinnovations.rmu.view.di.components;

import com.madinnovations.rmu.view.activities.combat.AttacksFragment;
import com.madinnovations.rmu.view.activities.combat.CriticalResultsFragment;
import com.madinnovations.rmu.view.activities.combat.DamageResultsFragment;
import com.madinnovations.rmu.view.activities.combat.DiseasesFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;

import dagger.Subcomponent;

/**
 * The CombatFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = CombatFragmentModule.class)
public interface CombatFragmentComponent {
	void injectInto(AttacksFragment attacksFragment);
	void injectInto(CriticalResultsFragment criticalResultsFragment);
	void injectInto(DamageResultsFragment damageResultsFragment);
	void injectInto(DiseasesFragment diseasesFragment);
}

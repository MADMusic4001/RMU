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

import com.madinnovations.rmu.view.activities.campaign.CampaignActivity;
import com.madinnovations.rmu.view.di.PerActivity;
import com.madinnovations.rmu.view.di.modules.ActivityModule;
import com.madinnovations.rmu.view.di.modules.CampaignFragmentModule;
import com.madinnovations.rmu.view.di.modules.CharacterFragmentModule;
import com.madinnovations.rmu.view.di.modules.CombatFragmentModule;
import com.madinnovations.rmu.view.di.modules.CommonFragmentModule;
import com.madinnovations.rmu.view.di.modules.CreatureFragmentModule;
import com.madinnovations.rmu.view.di.modules.FragmentModule;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;
import com.madinnovations.rmu.view.di.modules.SpellFragmentModule;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import dagger.Subcomponent;

/**
 * The ActivityComponent dependency injection interface.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
	FragmentComponent newFragmentComponent(FragmentModule fragmentModule);
	CampaignFragmentComponent newCampaignFragmentComponent(CampaignFragmentModule fragmentModule);
	CharacterFragmentComponent newCharacterFragmentComponent(CharacterFragmentModule fragmentModule);
	CombatFragmentComponent newCombatFragmentComponent(CombatFragmentModule fragmentModule);
	CommonFragmentComponent newCommonFragmentComponent(CommonFragmentModule fragmentModule);
	CreatureFragmentComponent newCreatureFragmentComponent(CreatureFragmentModule fragmentModule);
	ItemFragmentComponent newItemFragmentComponent(ItemFragmentModule fragmentModule);
	PlayFragmentComponent newPlayFragmentComponent(PlayFragmentModule fragmentModule);
	SpellFragmentComponent newSpellFragmentComponent(SpellFragmentModule fragmentModule);
	ViewsComponent newViewsComponent(ViewsModule viewsModule);

	void injectInto(CampaignActivity campaignActivity);
}

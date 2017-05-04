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

import com.madinnovations.rmu.view.activities.play.SelectActionDialog;
import com.madinnovations.rmu.view.activities.play.CampaignsFragment;
import com.madinnovations.rmu.view.activities.play.StartEncounterFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into play package Fragment instances.
 */
@Module
public class PlayFragmentModule {
	private SelectActionDialog     selectActionDialog;
	private CampaignsFragment      campaignsFragment;
	private StartEncounterFragment startEncounterFragment;

	public PlayFragmentModule(SelectActionDialog selectActionDialog) {
		this.selectActionDialog = selectActionDialog;
	}
	public PlayFragmentModule(CampaignsFragment campaignsFragment) {
		this.campaignsFragment = campaignsFragment;
	}
	public PlayFragmentModule(StartEncounterFragment startEncounterFragment) {
		this.startEncounterFragment = startEncounterFragment;
	}

	@Provides
	@PerFragment
	SelectActionDialog provideActionDialog() {
		return this.selectActionDialog;
	}
	@Provides
	@PerFragment
	CampaignsFragment provideCampaignsFragment() {
		return this.campaignsFragment;
	}
	@Provides
	@PerFragment
	StartEncounterFragment provideStartCombatFragment() {
		return this.startEncounterFragment;
	}
}

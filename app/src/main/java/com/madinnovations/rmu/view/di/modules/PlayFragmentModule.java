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

import com.madinnovations.rmu.view.activities.play.ActionDialog;
import com.madinnovations.rmu.view.activities.play.CampaignsFragment;
import com.madinnovations.rmu.view.activities.play.StartCombatFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into play package Fragment instances.
 */
@Module
public class PlayFragmentModule {
	private ActionDialog actionDialog;
	private CampaignsFragment campaignsFragment;
	private StartCombatFragment startCombatFragment;

	public PlayFragmentModule(ActionDialog actionDialog) {
		this.actionDialog = actionDialog;
	}
	public PlayFragmentModule(CampaignsFragment campaignsFragment) {
		this.campaignsFragment = campaignsFragment;
	}
	public PlayFragmentModule(StartCombatFragment startCombatFragment) {
		this.startCombatFragment = startCombatFragment;
	}

	@Provides
	@PerFragment
	ActionDialog provideActionDialog() {
		return this.actionDialog;
	}
	@Provides
	@PerFragment
	CampaignsFragment provideCampaignsFragment() {
		return this.campaignsFragment;
	}
	@Provides
	@PerFragment
	StartCombatFragment provideStartCombatFragment() {
		return this.startCombatFragment;
	}
}

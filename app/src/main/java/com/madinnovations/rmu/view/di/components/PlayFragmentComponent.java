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

import com.madinnovations.rmu.view.activities.play.SelectActionDialog;
import com.madinnovations.rmu.view.activities.play.CampaignsFragment;
import com.madinnovations.rmu.view.activities.play.StartEncounterFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.PlayFragmentModule;

import dagger.Subcomponent;

/**
 * The PlayFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = PlayFragmentModule.class)
public interface PlayFragmentComponent {
	void injectInto(SelectActionDialog selectActionDialog);
	void injectInto(CampaignsFragment campaignsFragment);
	void injectInto(StartEncounterFragment startEncounterFragment);
}

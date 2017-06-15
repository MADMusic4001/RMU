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

import com.madinnovations.rmu.view.activities.item.ItemFragment;
import com.madinnovations.rmu.view.activities.item.ItemPaneFragment;
import com.madinnovations.rmu.view.activities.item.ItemTemplatePaneFragment;
import com.madinnovations.rmu.view.activities.item.ItemTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponsFragment;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.modules.ItemFragmentModule;

import dagger.Subcomponent;

/**
 * The ItemFragmentComponent dependency injection interface.
 */
@SuppressWarnings("WeakerAccess")
@PerFragment
@Subcomponent(modules = ItemFragmentModule.class)
public interface ItemFragmentComponent {
	void injectInto(ItemFragment itemFragment);
	void injectInto(ItemPaneFragment itemPaneFragment);
	void injectInto(ItemTemplatesFragment itemTemplatesFragment);
	void injectInto(ItemTemplatePaneFragment itemTemplatePaneFragment);
	void injectInto(WeaponsFragment weaponsFragment);
	void injectInto(WeaponTemplatesFragment weaponTemplatesFragment);
}

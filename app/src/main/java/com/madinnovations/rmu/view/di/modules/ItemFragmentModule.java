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

import com.madinnovations.rmu.view.activities.item.ItemPaneFragment;
import com.madinnovations.rmu.view.activities.item.ItemTemplatePaneFragment;
import com.madinnovations.rmu.view.activities.item.ItemTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponTemplatesFragment;
import com.madinnovations.rmu.view.activities.item.WeaponsFragment;
import com.madinnovations.rmu.view.di.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into item package Fragment instances.
 */
@Module
public class ItemFragmentModule {
	private ItemPaneFragment         itemPaneFragment;
	private ItemTemplatesFragment    itemTemplatesFragment;
	private ItemTemplatePaneFragment itemTemplatePaneFragment;
	private WeaponsFragment          weaponsFragment;
	private WeaponTemplatesFragment  weaponTemplatesFragment;

	public ItemFragmentModule(ItemPaneFragment itemPaneFragment) {
		this.itemPaneFragment = itemPaneFragment;
	}
	public ItemFragmentModule(ItemTemplatesFragment itemTemplatesFragment) {
		this.itemTemplatesFragment = itemTemplatesFragment;
	}
	public ItemFragmentModule(ItemTemplatePaneFragment itemTemplatePaneFragment) {
		this.itemTemplatePaneFragment = itemTemplatePaneFragment;
	}
	public ItemFragmentModule(WeaponsFragment weaponsFragment) {
		this.weaponsFragment = weaponsFragment;
	}
	public ItemFragmentModule(WeaponTemplatesFragment weaponTemplatesFragment) {
		this.weaponTemplatesFragment = weaponTemplatesFragment;
	}

	@Provides @PerFragment
	ItemPaneFragment provideItemPaneFragment() {
		return this.itemPaneFragment;
	}
	@Provides @PerFragment
	ItemTemplatesFragment provideItemTemplatesFragment() {
		return this.itemTemplatesFragment;
	}
	@Provides @PerFragment
	ItemTemplatePaneFragment provideItemTemplateLayoutManager() {
		return this.itemTemplatePaneFragment;
	}
	@Provides @PerFragment
	WeaponsFragment provideWeaponsFragment() {return this.weaponsFragment;}
	@Provides @PerFragment
	WeaponTemplatesFragment provideWeaponTemplatesFragment() {
		return this.weaponTemplatesFragment;
	}
}

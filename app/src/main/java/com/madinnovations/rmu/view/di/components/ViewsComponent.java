/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.view.di.components;

import com.madinnovations.rmu.view.HexView;
import com.madinnovations.rmu.view.TerrainView;
import com.madinnovations.rmu.view.di.PerView;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import dagger.Subcomponent;

/**
 * The ViewsComponent dependency injection interface.
 */
@PerView
@Subcomponent(modules = ViewsModule.class)
public interface ViewsComponent {
	void injectInto(HexView hexView);
	void injectInto(TerrainView terrainView);
}

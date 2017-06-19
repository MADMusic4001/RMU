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
package com.madinnovations.rmu.view.di.modules;

import com.madinnovations.rmu.view.HexView;
import com.madinnovations.rmu.view.TerrainView;
import com.madinnovations.rmu.view.di.PerFragment;
import com.madinnovations.rmu.view.di.PerView;

import dagger.Module;
import dagger.Provides;

/**
 * Provides methods to allow the dependency injection engine to inject dependencies into custom View instances.
 */
@Module
public class ViewsModule {
	private HexView hexView;
	private TerrainView terrainView;

	public ViewsModule(HexView hexView) {
		this.hexView = hexView;
	}
	public ViewsModule(TerrainView terrainView) {
		this.terrainView = terrainView;
	}

	@Provides
	@PerView
	HexView providesHexView() {
		return this.hexView;
	}
	@Provides
	@PerView
	TerrainView providesTerrainView() {
		return this.terrainView;
	}
}

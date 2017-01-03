/**
 * Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.common.Biome;
import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;

/**
 * Attributes of items found in nature (herbs, poisons, etc.)
 */
public abstract class NaturalsTemplate extends ItemTemplate {
	public static final String JSON_NAME = "NaturalsTemplate";
	private Biome biome = null;
	private Form form = null;
	private Prep prep = null;
	private String season = null;
	private String effects = null;

	/**
	 * Checks the validity of the Item instance.
	 *
	 * @return true if the ItemTemplate instance is valid, otherwise false.
	 */
	public boolean isValid() {
		return super.isValid() && biome != null && form != null && prep != null && effects != null;
	}

}

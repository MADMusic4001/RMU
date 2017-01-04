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

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;

/**
 * The various Substance types
 */
public enum SubstanceType {
	METALS(R.string.enum_substance_type_metals),
	OTHERS(R.string.enum_substance_type_others),
	PRECIOUS_STONES(R.string.enum_substance_type_precious_stones),
	SEMI_PRECIOUS_STONES(R.string.enum_substance_type_semi_precious_stones),
	STONES(R.string.enum_substance_type_stones);

	SubstanceType(int typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	private @StringRes int typeResourceId;

	// Getters
	public int getTypeResourceId() {
		return typeResourceId;
	}
}

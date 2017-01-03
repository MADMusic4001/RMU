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
package com.madinnovations.rmu.data.entities.spells;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Spell list type attributes
 */
public enum SpellListType {
	OPEN(R.string.enum_open),
	CLOSED(R.string.enum_closed),
	BASE(R.string.enum_base),
	EVIL(R.string.enum_evil),
	ARCANE(R.string.enum_arcane),
	RESTRICTED(R.string.enum_restricted);

	private @StringRes int labelId;

	/**
	 * Creates a new SpellListType instance with the given string resource id
	 *
	 * @param labelId  the string resource id for the instance
	 */
	SpellListType(int labelId) {
		this.labelId = labelId;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(getLabelId());
	}

	// Getters and setters
	public int getLabelId() {
		return labelId;
	}
}

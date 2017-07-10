/*
  Copyright (C) 2017 MadInnovations
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
package com.madinnovations.rmu.data.entities.combat;

import android.support.annotation.StringRes;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.RMUApp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Enumeration of the 4 possible restricted quarters.
 */
public enum RestrictedQuarters {
	NORMAL(R.string.enum_restricted_quarters_normal, 0),
	CLOSE(R.string.enum_restricted_quarters_close, -25),
	CRAMPED(R.string.enum_restricted_quarters_cramped, -50),
	TIGHT(R.string.enum_restricted_quarters_tight, -75),
	CONFINED(R.string.enum_restricted_quarters_confined, -100);

	private @StringRes int nameResourceId;
	private int modifier;

	RestrictedQuarters(int nameResourceId, int modifier) {
		this.nameResourceId = nameResourceId;
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return RMUApp.getResourceUtils().getString(nameResourceId);
	}

	/**
	 * Returns a string containing the field names and values for this instance.
	 *
	 * @return a string containing the field names and values for this instance.
	 */
	public String print() {
		return new ToStringBuilder(this,
								   ToStringStyle.MULTI_LINE_STYLE)
				.append("nameResourceId", nameResourceId)
				.append("modifier", modifier)
				.toString();
	}

	// Getters
	public int getNameResourceId() {
		return nameResourceId;
	}
	public int getModifier() {
		return modifier;
	}
}

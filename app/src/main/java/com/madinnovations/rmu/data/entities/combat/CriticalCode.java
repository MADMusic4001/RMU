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
package com.madinnovations.rmu.data.entities.combat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Critical code attributes
 */
public enum CriticalCode {
	SEVERITY_REDUCTION("I", "Creature reduces the severity of any critical by 1. Criticals reduced below A are not rolled. This "
			+ "is in addition to any critical modifications due to Size difference."),
	STAGGER_TYPE_IMMUNITY("!", "Creature is immune to stagger, knock back and knock down results."),
	STUN_TYPE_IMMUNITY("@", "Creature is immune to daze, stun, stunned unable to parry and down results. If the creature can be "
			+ "staggered, relegate the result to the comparable Stagger result: Daze - stagger, Stun/ stun unable to parry - "
			+ "knock back, down - knock down."),
	BLEEDING_IMMUNITY("#", "Creature is immune to bleeding results. Convert every hit of bleeding to a concussion hit."),
	KRUSH_IMMUNITY("PK", "No Krush: Creature is only affected by half concussion damage from Krushing attacks and does not "
			+ "suffer critical damage."),
	GRAPPLE_IMMUNITY("PG", "No Grapple: This creature cannot be grappled. Apply only half damage from Hits damage and no "
			+ "critical results."),
	PUNCTURE_IMMUNITY("PP", "No Punctures: creature takes but half damage from attacks delivering puncture damage and no "
			+ "critical is rolled."),
	SLASH_IMMUNITY("PS", "No Slash: creature suffers only half damage from slashing attacks and no critical should be rolled."),
	STRIKES_IMMUNITY("PT", "No Strikes: creature is partly insusceptible by strike attacks and takes only half damage and no "
			+ "critical damage."),
	UNBALANCING_IMMUNITY("PU", "No Unbalancing: creature cannot be unbalanced and receives only half damage from such an attack "
			+ "and never any critical damage.");

	private String code = null;
	private String description = null;

	/**
	 * Creates a new CriticalCode instance
	 *
	 * @param code  the critical code string.
	 * @param description  a human readable description of what the code means.
	 */
	CriticalCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public String toString() {
		return code;
	}

	@SuppressWarnings("unused")
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("code", code)
				.append("description", description)
				.toString();
	}

	// Getters and setters
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

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
package com.madinnovations.rmu.data.entities.common;

import com.madinnovations.rmu.data.entities.combat.Resistance;

/**
 * Talent Effect information
 */
public class TalentEffectRow {
	private Effect         effect                 = Effect.ACTION_POINTS;
	private Skill          affectedSkill          = null;
	private Specialization affectedSpecialization = null;
	private Resistance     affectedResistance     = null;
	private Stat           affectedStat           = null;
	private short          bonus                  = 0;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TalentEffectRow that = (TalentEffectRow) o;

		return getEffect() == that.getEffect();
	}

	@Override
	public int hashCode() {
		return getEffect() != null ? getEffect().hashCode() : 0;
	}

	// Getters and setters
	public Effect getEffect() {
		return effect;
	}
	public void setEffect(Effect effect) {
		this.effect = effect;
	}
	public Skill getAffectedSkill() {
		return affectedSkill;
	}
	public void setAffectedSkill(Skill affectedSkill) {
		this.affectedSkill = affectedSkill;
	}
	public Specialization getAffectedSpecialization() {
		return affectedSpecialization;
	}
	public void setAffectedSpecialization(Specialization affectedSpecialization) {
		this.affectedSpecialization = affectedSpecialization;
	}
	public Resistance getAffectedResistance() {
		return affectedResistance;
	}
	public void setAffectedResistance(Resistance affectedResistance) {
		this.affectedResistance = affectedResistance;
	}
	public Stat getAffectedStat() {
		return affectedStat;
	}
	public void setAffectedStat(Stat affectedStat) {
		this.affectedStat = affectedStat;
	}
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
}

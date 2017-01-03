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
package com.madinnovations.rmu.data.entities.creature;

import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.entities.common.Statistic;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Stat bonus attributes
 */
public class RacialStatBonus implements Comparable {
	private Statistic stat  = null;
	private short     bonus = 0;

	/**
	 * Creates a new RacialStatBonus with the given attribute values
	 *
	 * @param stat  a Stat instance to set the stat value for the new RacialStatBonus instance
	 * @param bonus  a short to set the bonus value for the new RacialStatBonus instance
	 */
	public RacialStatBonus(Statistic stat, short bonus) {
		this.stat = stat;
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("stat", stat)
				.append("bonus", bonus)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RacialStatBonus racialStatBonus = (RacialStatBonus) o;

		return stat != null ? stat.equals(racialStatBonus.stat) : racialStatBonus.stat == null;
	}

	@Override
	public int hashCode() {
		return stat != null ? stat.hashCode() : 0;
	}

	@Override
	public int compareTo(@NonNull Object o) {
		if(!(o instanceof RacialStatBonus)) {
			return 1;
		}
		RacialStatBonus other = (RacialStatBonus)o;
		if(other.getStat() == null) {
			if(this.getStat() == null) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else if (this.getStat() == null){
			return  -1;
		}
		else {
			return this.getStat().getAbbreviation().compareTo(other.getStat().getAbbreviation());
		}
	}

	// Getters and setters
	public Statistic getStat() {
		return stat;
	}
	public void setStat(Statistic stat) {
		this.stat = stat;
	}
	public short getBonus() {
		return bonus;
	}
	public void setBonus(short bonus) {
		this.bonus = bonus;
	}
}

/*
  Copyright (C) 2016 MadInnovations
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

import com.madinnovations.rmu.data.entities.DatabaseObject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Damage result attributes
 */
public class DamageResult extends DatabaseObject {
    public static final String          JSON_NAME        = "DamageResults";
    private             DamageResultRow damageResultRow  = null;
    private             short           armorType        = 1;
    private             short           hits             = 0;
    private             Character       criticalSeverity = null;
    private             CriticalType    criticalType     = null;

	/**
	 * Creates a new DamageResult instance
	 */
	public DamageResult() {
    }

	/**
	 * Creates a new DamageResult instance with the given id
	 *
	 * @param id  the id of the new instance
	 */
    public DamageResult(int id) {
		super(id);
    }

    /**
     * Checks the validity of the DamageResult instance.
     *
     * @return true if the DamageResult instance is valid, otherwise false.
     */
    public boolean isValid() {
        return hits > 0 && damageResultRow != null && armorType >= 1 && armorType <= 10 &&
				((criticalSeverity == null && criticalType == null) ||
                (criticalSeverity != null && criticalType != null));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
				.append("damageResultRow", damageResultRow)
				.append("armorType", armorType)
                .append("hits", hits)
                .append("criticalSeverity", criticalSeverity)
                .append("criticalType", criticalType)
                .toString();
    }

    // Getters and setters
	public DamageResultRow getDamageResultRow() {
		return damageResultRow;
	}
	public void setDamageResultRow(DamageResultRow damageResultRow) {
		this.damageResultRow = damageResultRow;
	}
	public short getArmorType() {
		return armorType;
	}
	public void setArmorType(short armorType) {
		this.armorType = armorType;
	}
	public short getHits() {
        return hits;
    }
    public void setHits(short hits) {
        this.hits = hits;
    }
    public Character getCriticalSeverity() {
        return criticalSeverity;
    }
    public void setCriticalSeverity(Character criticalSeverity) {
        this.criticalSeverity = criticalSeverity;
    }
    public CriticalType getCriticalType() {
        return criticalType;
    }
    public void setCriticalType(CriticalType criticalType) {
        this.criticalType = criticalType;
    }
}

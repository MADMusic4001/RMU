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


/**
 * Damage result attributes
 */
public class DamageResult {
    private int id = -1;
    private short hits = 0;
    private Character criticalSeverity = null;
    private CriticalType criticalType = null;

    /**
     * Checks the validity of the DamageResult instance.
     *
     * @return true if the DamageResult instance is valid, otherwise false.
     */
    public boolean isValid() {
        return hits > 0 && ((criticalSeverity == null && criticalType == null) ||
                (criticalSeverity != null && criticalType != null));
    }

    @Override
    public String toString() {
        return "DamageResult{" +
                "id=" + id +
                ", hits=" + hits +
                ", criticalSeverity='" + criticalSeverity + '\'' +
                ", criticalType=" + criticalType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DamageResult that = (DamageResult) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

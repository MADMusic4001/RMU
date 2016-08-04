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
    private int minRoll;
    private int maxRoll;
    private int hits;
    private String criticalSeverity;
    private CriticalType criticalType;

    /**
     * Checks the validity of the DamageResult instance.
     *
     * @return true if the DamageResult instance is valid, otherwise false.
     */
    public boolean isValid() {
        return criticalSeverity != null && !criticalSeverity.isEmpty() && criticalType != null;
    }

    @Override
    public String toString() {
        return "DamageResult{" +
                "id=" + id +
                ", minRoll=" + minRoll +
                ", maxRoll=" + maxRoll +
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
    public int getMinRoll() {
        return minRoll;
    }
    public void setMinRoll(int minRoll) {
        this.minRoll = minRoll;
    }
    public int getMaxRoll() {
        return maxRoll;
    }
    public void setMaxRoll(int maxRoll) {
        this.maxRoll = maxRoll;
    }
    public int getHits() {
        return hits;
    }
    public void setHits(int hits) {
        this.hits = hits;
    }
    public String getCriticalSeverity() {
        return criticalSeverity;
    }
    public void setCriticalSeverity(String criticalSeverity) {
        this.criticalSeverity = criticalSeverity;
    }
    public CriticalType getCriticalType() {
        return criticalType;
    }
    public void setCriticalType(CriticalType criticalType) {
        this.criticalType = criticalType;
    }
}

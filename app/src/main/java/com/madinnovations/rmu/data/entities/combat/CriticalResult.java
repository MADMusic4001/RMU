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
 * Critical result attributes
 */
public class CriticalResult {
    private int id = -1;
    private char severityCode = 'A';
    private String description;
    private short minRoll = 0;
    private short maxRoll = 1;
    private BodyPart bodyPart;
    private short hits = 0;
    private short bleeding = 0;
    private short fatigue = 0;
    private short breakage = 0;
    private short injury = 0;
    private short dazed = 0;
    private short stunned = 0;
    private short noParry = 0;
    private boolean staggered = false;
    private short knockBack = 0;
    private boolean prone = false;
    private short grappled = 0;

	/**
     * Checks the validity of the CriticalResult instance.
     *
     * @return true if the CriticalResult instance is valid, otherwise false.
     */
    public boolean isValid() {
        return description != null && !description.isEmpty() && bodyPart != null && minRoll <= maxRoll;
    }

    @Override
    public String toString() {
        return "CriticalResult{" +
                "id=" + id +
                ", severityCode=" + severityCode +
                ", description='" + description + '\'' +
                ", minRoll=" + minRoll +
                ", maxRoll=" + maxRoll +
                ", bodyPart=" + bodyPart +
                ", hits=" + hits +
                ", bleeding=" + bleeding +
                ", fatigue=" + fatigue +
                ", breakage=" + breakage +
                ", injury=" + injury +
                ", dazed=" + dazed +
                ", stunned=" + stunned +
                ", noParry=" + noParry +
                ", staggered=" + staggered +
                ", knockBack=" + knockBack +
                ", prone=" + prone +
                ", grappled=" + grappled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CriticalResult that = (CriticalResult) o;

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
    public char getSeverityCode() {
        return severityCode;
    }
    public void setSeverityCode(char severityCode) {
        this.severityCode = severityCode;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public short getMinRoll() {
        return minRoll;
    }
    public void setMinRoll(short minRoll) {
        this.minRoll = minRoll;
    }
    public short getMaxRoll() {
        return maxRoll;
    }
    public void setMaxRoll(short maxRoll) {
        this.maxRoll = maxRoll;
    }
    public BodyPart getBodyPart() {
        return bodyPart;
    }
    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }
    public short getHits() {
        return hits;
    }
    public void setHits(short hits) {
        this.hits = hits;
    }
    public short getBleeding() {
        return bleeding;
    }
    public void setBleeding(short bleeding) {
        this.bleeding = bleeding;
    }
    public short getFatigue() {
        return fatigue;
    }
    public void setFatigue(short fatigue) {
        this.fatigue = fatigue;
    }
    public short getBreakage() {
        return breakage;
    }
    public void setBreakage(short breakage) {
        this.breakage = breakage;
    }
    public short getInjury() {
        return injury;
    }
    public void setInjury(short injury) {
        this.injury = injury;
    }
    public short getDazed() {
        return dazed;
    }
    public void setDazed(short dazed) {
        this.dazed = dazed;
    }
    public short getStunned() {
        return stunned;
    }
    public void setStunned(short stunned) {
        this.stunned = stunned;
    }
    public short getNoParry() {
        return noParry;
    }
    public void setNoParry(short noParry) {
        this.noParry = noParry;
    }
    public boolean isStaggered() {
        return staggered;
    }
    public void setStaggered(boolean staggered) {
        this.staggered = staggered;
    }
    public short getKnockBack() {
        return knockBack;
    }
    public void setKnockBack(short knockBack) {
        this.knockBack = knockBack;
    }
    public boolean isProne() {
        return prone;
    }
    public void setProne(boolean prone) {
        this.prone = prone;
    }
    public short getGrappled() {
        return grappled;
    }
    public void setGrappled(short grappled) {
        this.grappled = grappled;
    }
}

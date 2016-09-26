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

import android.support.annotation.NonNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Critical result attributes
 */
public class CriticalResult {
    public static final String JSON_NAME = "CriticalResults";
    private static short[] rangeStarts = {1, 2, 4, 6, 11, 16, 21, 26, 36, 46, 56, 66, 67, 76, 81, 86, 91, 96, 98, 100, 101};

    private int id = -1;
    private char severityCode = 'A';
    private String resultText = null;
    private short minRoll = 0;
    private short maxRoll = 1;
    private BodyPart bodyPart = null;
    private short hits = 0;
    private short bleeding = 0;
    private short fatigue = 0;
    private Short breakage = null;
    private short injury = 0;
    private short dazed = 0;
    private short stunned = 0;
    private short noParry = 0;
    private boolean staggered = false;
    private short knockBack = 0;
    private boolean prone = false;
    private short grappled = 0;
    private CriticalType criticalType = null;

	/**
     * Creates a new CriticalResult instance with all values set to defaults.
     */
    public CriticalResult() {
    }

	/**
     * Creates a new CriticalResult instance with the given criticalType, severityCode. minRoll, maxRoll, and bodyPart values and all others
     * set to defaults.
     *
     * @param criticalType  a CriticalType instance to use for the initial criticalType value
     * @param severityCode  a character to use for the initial severityCode value
     * @param minRoll  a short to use for the initial minRoll value
     * @param maxRoll  a short to use for the initial maxRoll value
     * @param bodyPart  a BodyPart instance to use for the initial bodyPart value
     */
    private CriticalResult(CriticalType criticalType, char severityCode, short minRoll, short maxRoll, BodyPart bodyPart) {
        this.criticalType = criticalType;
        this.severityCode = severityCode;
        this.minRoll = minRoll;
        this.maxRoll = maxRoll;
        this.bodyPart = bodyPart;
    }

    public static Collection<CriticalResult> generateMissingCriticalResultRows(Collection<CriticalResult> currentRows,
                                                                               @NonNull CriticalType criticalType,
                                                                               char severityCode) {
        Collection<CriticalResult> allRows;
        CriticalResult currentRow;

        if(currentRows.size() == rangeStarts.length - 1) {
            allRows = currentRows;
        }
        else {
            allRows = new ArrayList<>(rangeStarts.length - 1);
            for(int i = 0; i < rangeStarts.length - 1; i++) {
                currentRow = null;
                if(!currentRows.isEmpty()) {
                    for (CriticalResult criticalResult : currentRows) {
                        if (criticalResult.getMinRoll() == rangeStarts[i]) {
                            currentRow = criticalResult;
                            break;
                        }
                    }
                }
                if(currentRow == null) {
                    currentRow = new CriticalResult(criticalType, severityCode, rangeStarts[i], (short)(rangeStarts[i+1] - 1),
                                                    null);
                }
                allRows.add(currentRow);
            }
        }

        return allRows;
    }

	/**
     * Checks the validity of the CriticalResult instance.
     *
     * @return true if the CriticalResult instance is valid, otherwise false.
     */
    public boolean isValid() {
        return resultText != null && !resultText.isEmpty() && bodyPart != null && minRoll <= maxRoll && criticalType != null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("severityCode", severityCode)
                .append("resultText", resultText)
                .append("minRoll", minRoll)
                .append("maxRoll", maxRoll)
                .append("bodyPart", bodyPart)
                .append("hits", hits)
                .append("bleeding", bleeding)
                .append("fatigue", fatigue)
                .append("breakage", breakage)
                .append("injury", injury)
                .append("dazed", dazed)
                .append("stunned", stunned)
                .append("noParry", noParry)
                .append("staggered", staggered)
                .append("knockBack", knockBack)
                .append("prone", prone)
                .append("grappled", grappled)
                .append("criticalType", criticalType)
                .toString();
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
    public String getResultText() {
        return resultText;
    }
    public void setResultText(String resultText) {
        this.resultText = resultText;
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
    public Short getBreakage() {
        return breakage;
    }
    public void setBreakage(Short breakage) {
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
    public CriticalType getCriticalType() {
        return criticalType;
    }
    public void setCriticalType(CriticalType criticalType) {
        this.criticalType = criticalType;
    }
}

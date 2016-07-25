package com.madinnovations.rmu.data.entities.combat;


/**
 * Damage result attributes
 */
public class DamageResult {
    private int id;
    private int minRoll;
    private int maxRoll;
    private int hits;
    private String criticalSeverity;
    private CriticalType criticalType;

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

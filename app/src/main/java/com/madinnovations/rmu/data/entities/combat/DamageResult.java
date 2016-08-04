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

package com.madinnovations.rmu.data.entities.combat;

/**
 * Critical result attributes
 */
public class CriticalResult {
    private int id = -1;
    private String description;
    private int minRoll;
    private int maxRoll;
    private BodyPart bodyPart;
    private short hits;
    private short bleeding;
    private short fatigue;
    private short breakage;
    private short injury;
    private short dazed;
    private short stunned;
    private short noParry;
    private short staggered;
    private short knockBack;
    private boolean prone;
    private short grappled;

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public short getStaggered() {
        return staggered;
    }
    public void setStaggered(short staggered) {
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

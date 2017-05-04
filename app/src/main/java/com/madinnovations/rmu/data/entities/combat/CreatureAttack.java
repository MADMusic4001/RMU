package com.madinnovations.rmu.data.entities.combat;

import com.madinnovations.rmu.data.entities.common.Size;

/**
 * Additional attack information for creatures.
 */
public class CreatureAttack {
	private Attack baseAttack;
	private short offensiveBonus;
	private Size  size;
	private boolean criticalFollowUp = false;
	private boolean sameRoundFollowUp = false;
	private short coopNumber = 0;
	private short kataCount = 0;

	// Getters and setters
	public Attack getBaseAttack() {
		return baseAttack;
	}
	public void setBaseAttack(Attack baseAttack) {
		this.baseAttack = baseAttack;
	}
	public short getOffensiveBonus() {
		return offensiveBonus;
	}
	public void setOffensiveBonus(short offensiveBonus) {
		this.offensiveBonus = offensiveBonus;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public boolean isCriticalFollowUp() {
		return criticalFollowUp;
	}
	public void setCriticalFollowUp(boolean criticalFollowUp) {
		this.criticalFollowUp = criticalFollowUp;
	}
	public boolean isSameRoundFollowUp() {
		return sameRoundFollowUp;
	}
	public void setSameRoundFollowUp(boolean sameRoundFollowUp) {
		this.sameRoundFollowUp = sameRoundFollowUp;
	}
	public short getCoopNumber() {
		return coopNumber;
	}
	public void setCoopNumber(short coopNumber) {
		this.coopNumber = coopNumber;
	}
	public short getKataCount() {
		return kataCount;
	}
	public void setKataCount(short kataCount) {
		this.kataCount = kataCount;
	}
}

/*
 *
 *   Copyright (C) 2017 MadInnovations
 *   <p/>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   <p/>
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   <p/>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *
 */

package com.madinnovations.rmu.data.entities.combat;

/**
 * Attack result information
 */
public class AttackResult {
	private Attack attack;
	private DamageResult damageResult = null;
	private CriticalResult criticalResult = null;

	/**
	 * Creates a new AttackResult instance
	 */
	public AttackResult() {
	}

	/**
	 * Creates a new AttackResult instance for the given Attack, DamageResult, and CriticalResult
	 */
	public AttackResult(Attack attack, DamageResult damageResult, CriticalResult criticalResult) {
		this.attack = attack;
		this.damageResult = damageResult;
		this.criticalResult = criticalResult;
	}

	// Getters
	public Attack getAttack() {
		return attack;
	}
	public void setAttack(Attack attack) {
		this.attack = attack;
	}
	public DamageResult getDamageResult() {
		return damageResult;
	}
	public void setDamageResult(DamageResult damageResult) {
		this.damageResult = damageResult;
	}
	public CriticalResult getCriticalResult() {
		return criticalResult;
	}
	public void setCriticalResult(CriticalResult criticalResult) {
		this.criticalResult = criticalResult;
	}
}

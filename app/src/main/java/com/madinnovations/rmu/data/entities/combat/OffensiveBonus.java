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

import android.util.SparseArray;

/**
 * Container for listing all offensive bonus factors
 */
@SuppressWarnings("unused")
public class OffensiveBonus {
	private SparseArray<Short> attackerAdditions    = new SparseArray<>();
	private SparseArray<Short> attackerSubtractions = new SparseArray<>();
	private SparseArray<Short> defenderAdditions    = new SparseArray<>();
	private SparseArray<Short> defenderSubtractions = new SparseArray<>();

	// <editor-fold desc="Public methods">

	/**
	 * Calculates the total offensiveBonus.
	 *
	 * @return the total offensiveBonus
	 */
	public short calculateOffensiveBonus() {
		short result = 0;
		for (int i = 0, size = attackerAdditions.size(); i < size; i++) {
			result += attackerAdditions.valueAt(i);
		}
		for (int i = 0, size = attackerSubtractions.size(); i < size; i++) {
			result += attackerSubtractions.valueAt(i);
		}
		for (int i = 0, size = defenderAdditions.size(); i < size; i++) {
			result += defenderAdditions.valueAt(i);
		}
		for (int i = 0, size = defenderSubtractions.size(); i < size; i++) {
			result += defenderSubtractions.valueAt(i);
		}

		return result;
	}
	// </editor-fold> Public methods

	// <editor-fold desc="Getters and setters">
	public SparseArray<Short> getAttackerAdditions() {
		return attackerAdditions;
	}

	public void setAttackerAdditions(SparseArray<Short> attackerAdditions) {
		this.attackerAdditions = attackerAdditions;
	}

	public SparseArray<Short> getAttackerSubtractions() {
		return attackerSubtractions;
	}

	public void setAttackerSubtractions(SparseArray<Short> attackerSubtractions) {
		this.attackerSubtractions = attackerSubtractions;
	}

	public SparseArray<Short> getDefenderAdditions() {
		return defenderAdditions;
	}

	public void setDefenderAdditions(SparseArray<Short> defenderAdditions) {
		this.defenderAdditions = defenderAdditions;
	}

	public SparseArray<Short> getDefenderSubtractions() {
		return defenderSubtractions;
	}

	public void setDefenderSubtractions(SparseArray<Short> defenderSubtractions) {
		this.defenderSubtractions = defenderSubtractions;
	}
	// </editor-fold> Getters and setters
}

/*
  Copyright (C) 2017 MadInnovations
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.madinnovations.rmu.data.entities.play;

import android.graphics.Point;

import com.madinnovations.rmu.data.entities.combat.Action;

import java.io.Serializable;

/**
 * Per round combat information
 */
public class CombatRoundInfo implements Serializable {
	private static final long serialVersionUID = 7312314437576720214L;
	private Point  hexCoordinate;
	private short  initiativeRoll;
	private short  baseInitiative;
	private short  offensiveBonus = 0;
	private short  parry = 0;
	private short  defensiveBonus = 0;
	private short  actionPointsRemaining;
	private short  currentActionPointsSpent;
	private Action actionInProgress;

	// Getters and setters
	public Point getHexCoordinate() {
		return hexCoordinate;
	}
	public void setHexCoordinate(Point hexCoordinate) {
		this.hexCoordinate = hexCoordinate;
	}
	public short getInitiativeRoll() {
		return initiativeRoll;
	}
	public void setInitiativeRoll(short initiativeRoll) {
		this.initiativeRoll = initiativeRoll;
	}
	public short getBaseInitiative() {
		return baseInitiative;
	}
	public void setBaseInitiative(short baseInitiative) {
		this.baseInitiative = baseInitiative;
	}
	public short getActionPointsRemaining() {
		return actionPointsRemaining;
	}
	public void setActionPointsRemaining(short actionPointsRemaining) {
		this.actionPointsRemaining = actionPointsRemaining;
	}
	public short getCurrentActionPointsSpent() {
		return currentActionPointsSpent;
	}
	public void setCurrentActionPointsSpent(short currentActionPointsSpent) {
		this.currentActionPointsSpent = currentActionPointsSpent;
	}
	public Action getActionInProgress() {
		return actionInProgress;
	}
	public void setActionInProgress(Action actionInProgress) {
		this.actionInProgress = actionInProgress;
	}
}

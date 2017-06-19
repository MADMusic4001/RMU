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

package com.madinnovations.rmu.data.entities;

import android.graphics.Matrix;

import com.madinnovations.rmu.data.entities.combat.CombatPosition;

/**
 * Represents the position of an object
 *
 * @author Mark
 *         Created 6/18/2017.
 */
public class Position {
	private static final float leftFlank;
	private static final float rightFlank;
	private static final float leftRear;
	private static final float rightRear;
	float x;
	float y;
	float direction;
	float height;
	float weaponLength;

	static {
		leftFlank = (float)Math.toRadians(-90);
		rightFlank = (float) Math.toRadians(90);
		leftRear = (float)Math.toRadians(-150);
		rightRear = (float)Math.toRadians(-180);
	}

	public Position(float x, float y, float direction, float height, float weaponLength) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.height = height;
		this.weaponLength = weaponLength;
	}

	public CombatPosition canAttack(Position position) {
		float distance = (float)Math.sqrt(((getX() - position.getX())*(getX() - position.getX()) +
				((getY() - position.getY())*(getY() - position.getY()))));
		float theta = 0;
		if(distance < height/2 + weaponLength + position.getHeight() / 2) {
			theta = (float)Math.atan2((position.getY() - getY()), (position.getX() - getX()));
			theta = position.getDirection() - theta;
			if(theta < 0) {
				if(theta < leftRear) {
					return CombatPosition.REAR;
				}
				if(theta < leftFlank) {
					return CombatPosition.LEFT_FLANK;
				}
			}
			else if (theta > 0) {
				if(theta > rightRear) {
					return CombatPosition.REAR;
				}
				if(theta > rightFlank) {
					return CombatPosition.RIGHT_FLANK;
				}
			}
			return CombatPosition.FRONT;
		}
		else {
			return CombatPosition.OUT_OF_RANGE;
		}
	}

	public CombatPosition getAttackPosition(Position defenderPosition) {
		float theta = (float)Math.atan2((getY() - defenderPosition.getY()), (getX() - defenderPosition.getX()));
		theta = getDirection() - theta;
		if(theta < 0) {
			if(theta < leftRear) {
				return CombatPosition.REAR;
			}
			if(theta < leftFlank) {
				return CombatPosition.LEFT_FLANK;
			}
		}
		else if (theta > 0) {
			if(theta > rightRear) {
				return CombatPosition.REAR;
			}
			if(theta > rightFlank) {
				return CombatPosition.RIGHT_FLANK;
			}
		}
		return CombatPosition.FRONT;
	}

	// Getters and setters
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getDirection() {
		return direction;
	}
	public void setDirection(float direction) {
		this.direction = direction;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeaponLength() {
		return weaponLength;
	}
	public void setWeaponLength(float weaponLength) {
		this.weaponLength = weaponLength;
	}
}

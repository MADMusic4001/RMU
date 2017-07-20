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

import com.madinnovations.rmu.data.entities.combat.CombatPosition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the position of an object
 *
 * @author Mark
 *         Created 6/18/2017.
 */
public class Position {
	@SuppressWarnings("unused")
	private static final String TAG = "Position";
	private static final float LEFT_FLANK;
	private static final float RIGHT_FLANK;
	private static final float LEFT_REAR;
	private static final float RIGHT_REAR;
	private float x = 0.0f;
	private float y = 0.0f;
	private float direction = 0.0f;

	static {
		LEFT_FLANK = (float)Math.toRadians(-90);
		RIGHT_FLANK = (float) Math.toRadians(90);
		LEFT_REAR = (float)Math.toRadians(-150);
		RIGHT_REAR = (float)Math.toRadians(150);
	}

	/**
	 * Creates a new Position instance
	 */
	public Position() {
	}

	/**
	 * Creates a new Position instance
	 *
	 * @param x  the x value of the position coordinate
	 * @param y  the y value of the position coordinate
	 * @param direction  the direction the combatant at this position is facing
	 */
	public Position(float x, float y, float direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	/**
	 * Determines if the given x,y coordinate is within the range of this position with the given combatant height and weapon
	 * length.
	 *
	 * @param x  the x value of the coordinate to check
	 * @param y  the y value of the coordinate to check
	 * @param height  the height of the combatant at this position
	 * @param weaponLength  the length of the weapon being wielded by the combatant at this position
	 * @return the CombatPosition of the coordinate
	 */
	public CombatPosition getPointIn(float x, float y, float height, float weaponLength) {
		CombatPosition result = CombatPosition.OUT_OF_RANGE;
		float distance = (float)Math.sqrt(((x - this.x)*(x - this.x)) + ((y - this.y)*(y - this.y)));
		if(distance <= (height/2 + weaponLength*12)) {
			float angle = (float)Math.atan2(y - this.y, x - this.x) - direction;
			if(angle >= -Math.PI/2 && angle <= Math.PI/2) {
				result = CombatPosition.FRONT;
			}
			else if(angle >= (LEFT_REAR) && angle <= LEFT_FLANK) {
				result = CombatPosition.LEFT_FLANK;
			}
			else if(angle >= RIGHT_FLANK && angle <= RIGHT_REAR) {
				result = CombatPosition.RIGHT_FLANK;
			}
			else {
				result = CombatPosition.REAR;
			}
		}

		return result;
	}

	public CombatPosition canAttack(Position position, float defenderHeight, float attackerHeight, float attackerWeaponLength) {
		float distance = (float)Math.sqrt(((getX() - position.getX())*(getX() - position.getX()) +
				((getY() - position.getY())*(getY() - position.getY()))));
		float theta;
		if(distance < defenderHeight/2 + attackerWeaponLength + attackerHeight / 2) {
			theta = (float)Math.atan2((position.getY() - getY()), (position.getX() - getX()));
			theta = position.getDirection() - theta;
			if(theta < 0) {
				if(theta < LEFT_REAR) {
					return CombatPosition.REAR;
				}
				if(theta < LEFT_FLANK) {
					return CombatPosition.LEFT_FLANK;
				}
			}
			else if (theta > 0) {
				if(theta > RIGHT_REAR) {
					return CombatPosition.REAR;
				}
				if(theta > RIGHT_FLANK) {
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
			if(theta < LEFT_REAR) {
				return CombatPosition.REAR;
			}
			if(theta < LEFT_FLANK) {
				return CombatPosition.LEFT_FLANK;
			}
		}
		else if (theta > 0) {
			if(theta > RIGHT_REAR) {
				return CombatPosition.REAR;
			}
			if(theta > RIGHT_FLANK) {
				return CombatPosition.RIGHT_FLANK;
			}
		}
		return CombatPosition.FRONT;
	}

	@Override
	public String toString() {
		return "{" + x + ", " + y + "}";
	}

	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("x", x)
				.append("y", y)
				.append("direction", direction)
				.toString();
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
}

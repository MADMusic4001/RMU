/**
 * Copyright (C) 2016 MadInnovations
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.object;

import com.madinnovations.rmu.data.entities.common.ManeuverDifficulty;

/**
 * Armor Attributes
 */
public class ArmorTemplate extends ItemTemplate {
	public static final String JSON_NAME = "ArmorTemplate";
	private float              smallCost          = 0.0f;
	private float              mediumCost         = 0.0f;
	private float              bigCost            = 0.0f;
	private float              largeCost          = 0.0f;
	private float              weightPercent      = 0.f;
	private short              armorType          = 0;

	// Getters and setters
	public float getSmallCost() {
		return smallCost;
	}
	public void setSmallCost(float smallCost) {
		this.smallCost = smallCost;
	}
	public float getMediumCost() {
		return mediumCost;
	}
	public void setMediumCost(float mediumCost) {
		this.mediumCost = mediumCost;
	}
	public float getBigCost() {
		return bigCost;
	}
	public void setBigCost(float bigCost) {
		this.bigCost = bigCost;
	}
	public float getLargeCost() {
		return largeCost;
	}
	public void setLargeCost(float largeCost) {
		this.largeCost = largeCost;
	}
	public float getWeightPercent() {
		return weightPercent;
	}
	public void setWeightPercent(float weightPercent) {
		this.weightPercent = weightPercent;
	}
	public short getArmorType() {
		return armorType;
	}
	public void setArmorType(short armorType) {
		this.armorType = armorType;
	}
}

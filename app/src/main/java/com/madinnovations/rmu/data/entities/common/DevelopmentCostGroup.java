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
package com.madinnovations.rmu.data.entities.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Enum of possible skill development point costs.
 */
public enum DevelopmentCostGroup {
	NONE(Short.MAX_VALUE, Short.MAX_VALUE),
	GROUP1((short)1, (short)2),
	GROUP2((short)1, (short)3),
	GROUP3((short)2, (short)3),
	GROUP4((short)2, (short)4),
	GROUP5((short)3, (short)4),
	GROUP6((short)3, (short)5),
	GROUP7((short)4, (short)6),
	GROUP8((short)5, (short)7),
	GROUP9((short)6, (short)8),
	GROUP10((short)9, (short)12),
	GROUP11((short)12, (short)15),
	GROUP12((short)16, (short)20),
	GROUP13((short)20, (short)24);

	private short firstCost;
	private short additionalCost;

	/**
	 * Creates a new DevelopmentCostGroup instance.
	 *
	 * @param firstCost  the firstCost value of the new instance
	 * @param additionalCost  the additionalCost value of the new instance
	 */
	DevelopmentCostGroup(short firstCost, short additionalCost) {
		this.firstCost = firstCost;
		this.additionalCost = additionalCost;
	}

	public static DevelopmentCostGroup find(short firstCost, short additionalCost) {
		DevelopmentCostGroup result = null;

		for(DevelopmentCostGroup costGroup : DevelopmentCostGroup.values()) {
			if(costGroup.firstCost == firstCost && costGroup.additionalCost == additionalCost) {
				result = costGroup;
				break;
			}
		}

		return result;
	}

	/**
	 * Creates a formatted string containing the attributes of this instance for debugging.
	 *
	 * @return a formatted string containing the attributes of this instance.
	 */
	@SuppressWarnings("unused")
	public String print() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("firstCost", firstCost)
				.append("additionalCost", additionalCost)
				.toString();
	}

	@Override
	public String toString() {
		String result = "";
		if(!this.equals(NONE)) {
			result = firstCost + "/" + additionalCost;
		}
		return result;
	}

	// Getters
	public short getFirstCost() {
		return firstCost;
	}
	public short getAdditionalCost() {
		return additionalCost;
	}
}

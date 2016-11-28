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
package com.madinnovations.rmu.data.utils;

import com.madinnovations.rmu.data.entities.common.DevelopmentCostGroup;

import java.util.Comparator;

/**
 * Compares two DevelopmentCostGroup enum instances for differences in cost values.
 */
public class DevelopmentCostGroupComparator implements Comparator<DevelopmentCostGroup> {
	@Override
	public int compare(DevelopmentCostGroup o1, DevelopmentCostGroup o2) {
		int result;

		if(o1 == null && o2 == null) {
			result = 0;
		}
		else if(o1 != null && o2 == null) {
			result = 1;
		}
		else if(o1 == null) {
			result = -1;
		}
		else {
			result = o1.getFirstCost() - o2.getFirstCost();
			if(result == 0) {
				result = o1.getAdditionalCost() - o2.getAdditionalCost();
			}
		}

		return result;
	}
}
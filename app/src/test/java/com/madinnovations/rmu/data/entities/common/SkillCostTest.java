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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Unit test methods for {@link SkillCost}
 */
public class SkillCostTest {

	@DataProvider(name = "compareData")
	public Object[][] createCompareData() {
		return new Object[][] {
				{new SkillCost(), new SkillCost(), 0},
				{new SkillCost((short)1, null), new SkillCost(null, null), -1},
				{new SkillCost((short)-1, null), new SkillCost(null, null), -1},
				{new SkillCost((short)1, (short)1), new SkillCost(null, null), -1},
				{new SkillCost((short)1, (short)1), new SkillCost(null, (short)1), -1},
				{new SkillCost((short)1, null), new SkillCost(null, (short)1), -1},
				{new SkillCost((short)10, null), new SkillCost((short)0, null), 10},
				{new SkillCost((short)0, null), new SkillCost((short)10, null), -10},
				{new SkillCost((short)10, null), new SkillCost((short)10, null), 0},
				{new SkillCost((short)-10, null), new SkillCost((short)-10, null), 0},
				{new SkillCost(null, null), new SkillCost((short)1, null), 1},
				{new SkillCost(null, null), new SkillCost((short)-1, null), 1},
				{new SkillCost(null, (short)1), new SkillCost((short)1, null), 1},
				{new SkillCost(null, (short)1), new SkillCost((short)1, (short)1), 1},
				{new SkillCost(null, null), new SkillCost((short)1, (short)1), 1},
				{new SkillCost(null, (short)1), new SkillCost(null, null), -1},
				{new SkillCost(null, (short)-1), new SkillCost(null, null), -1},
				{new SkillCost(null, null), new SkillCost(null, (short)1), 1},
				{new SkillCost(null, null), new SkillCost(null, (short)-1), 1},
				{new SkillCost(null, (short)10), new SkillCost(null, (short)0), 10},
				{new SkillCost(null, (short)-10), new SkillCost(null, (short)0), -10},
				{new SkillCost(null, (short)0), new SkillCost(null, (short)0), 0},
				{new SkillCost(null, (short)0), new SkillCost(null, (short)10), -10},
				{new SkillCost(null, (short)0), new SkillCost(null, (short)-10), 10},
				{new SkillCost(null, (short)-10), new SkillCost(null, (short)-10), 0},
				{new SkillCost((short)10, (short)10), new SkillCost((short)10, (short)10), 0},
				{new SkillCost((short)10, (short)10), new SkillCost((short)10, null), -1},
				{new SkillCost((short)10, null), new SkillCost((short)10, (short)10), 1}
		};
	}

	@Test(dataProvider = "compareData")
	public void testCompare(SkillCost cost1, SkillCost cost2, int expectedResult) {
		int result = cost1.compareTo(cost2);
		assertEquals("Inputs = " + cost1.toString() + cost2.toString(), expectedResult, result);
	}
}

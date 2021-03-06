/*
  Copyright (C) 2016 MadInnovations
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
package com.madinnovations.rmu.data.entities.common;

import android.support.annotation.NonNull;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Unit test methods for {@link Skill}
 */
public class SkillTest {
	@DataProvider(name = "compareData")
	public Object[][] createCompareData() {
		return new Object[][] {
				{new Skill(), "Not a Skill", -1},
				{new Skill(), new Skill(), 0},
				{new Skill("Skill1"), new Skill(), -1},
				{new Skill(), new Skill("Skill2"), 1},
				{new Skill("Skill1"), new Skill("Skill2"), -1},
				{new Skill("Skill1"), new Skill("Skill0"), 1},
				{new Skill("Skill1"), new Skill("Skill1"), 0}
		};
	}

	@Test(dataProvider = "compareData")
	public void testCompare(Skill cost1, Object cost2, int expectedResult) {
		int result = cost1.compareTo(cost2);
		assertEquals("Inputs = " + cost1.toString() + cost2.toString(), expectedResult, result);
	}

	@DataProvider(name = "getRankBonusData")
	public Object[][] createRankBonusData() {
		return new Object[][]{
				{new Skill(), (short)0, -25},
				{new Skill(), (short)1, 5},
				{new Skill(), (short)10, 50},
				{new Skill(), (short)11, 53},
				{new Skill(), (short)20, 80},
				{new Skill(), (short)21, 82},
				{new Skill(), (short)30, 100},
				{new Skill(), (short)31, 101},
				{new Skill(), (short)50, 120}
		};
	}

	@Test(dataProvider = "getRankBonusData")
	public void testGetRankBonus(@NonNull Skill skill, short ranks, int expectedResult) {
		int result = skill.getRankBonus(ranks);
		assertEquals(expectedResult, result);
	}
}

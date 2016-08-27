/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.entities.creature;

/**
 * Coce implementation of the creature level spread table on pages 44 and 45 of RMU Creature Law Beta Revision 2015-June-5
 */
public final class CreatureLevelSpreadTable {
	public static final Character[] LEVEL_SPREAD_CODES = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
	private static final short[] rangeStarts = {0, 1, 11, 16, 21, 26, 36, 46, 56, 66, 76, 81, 86, 91, 101, 141, 171, 191, 201, 251, 301};

	private static final Integer[][] results = {
			{null, null, null, null, null, null, null, -3},
			{-1, -2, -3, -4, -5, -6, -10, -2},
			{ 0, -1, -2, -3, -4, -5, -8, -2},
			{ 0,  0, -1, -2, -3, -4, -6, -1},
			{ 0,  0,  0, -1, -2, -3, -4, -1},
			{ 0,  0,  0,  0, -1, -2, -2, -1},
			{ 0,  0,  0,  0,  0, -1, -1,  0},
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 0,  0,  0,  0,  0,  1,  1,  0},
			{ 0,  0,  0,  0,  1,  2,  2,  1},
			{ 0,  0,  0,  1,  2,  3,  4,  1},
			{ 0,  0,  1,  2,  3,  4,  6,  1},
			{ 0,  1,  1,  3,  4,  5,  8,  2},
			{ 1,  1,  2,  4,  5,  6, 10,  2},
			{ 1,  1,  2,  5,  6,  7, 11,  3},
			{ 1,  2,  3,  6,  7,  8, 12,  3},
			{ 1,  2,  4,  7,  8,  9, 13,  3},
			{ 2,  3,  5,  8,  9, 10, 14,  3},
			{ 2,  4,  5,  9, 10, 11, 15,  3},
			{ 3,  5,  7, 10, 11, 12, 16,  4},
			{ 4,  6,  7, 11, 12, 13, 17,  4}
	};

	/**
	 * Looks up the level difference to use based on the given roll and levelSpreadCode values.
	 *
	 * @param roll  the result of an open ended d100 roll (capped at the max value for a short)
	 * @param levelSpreadCode  the level spread code (value is given for each creature variety)
	 * @return  an Integer representing how many levels above or below a creature variety's typical level a creature should be generated at.
	 * Null is returned if a young of the variety should be generated.
	 * @throws IllegalArgumentException  if roll is < 0 or levelSpreadCode is not between 'A' and 'H' inclusive.
	 */
	public static Integer getLevelDifference(short roll, char levelSpreadCode) {
		if(roll < 0) {
			throw new IllegalArgumentException("roll must be >= 0");
		}
		if(levelSpreadCode < 'A' || levelSpreadCode > 'H') {
			throw new IllegalArgumentException("levelSpreadCode must be a character between 'A' and 'H' inclusive");
		}
		int rollIndex = 0;
		for(int i = 0; i < rangeStarts.length; i++) {
			if( roll >= rangeStarts[i]) {
				rollIndex = i;
				break;
			}
		}
		return results[levelSpreadCode - 'A'][rollIndex];
	}
}

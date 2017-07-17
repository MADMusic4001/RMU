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

package com.madinnovations.rmu.data.entities.creature;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 *         Created 7/15/2017.
 */

public enum LevelSpread {
	A(new short[]{Short.MIN_VALUE, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 4}),
	B(new short[]{Short.MIN_VALUE, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 4, 5, 6}),
	C(new short[]{Short.MIN_VALUE, -3, -2, -1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3, 4, 5, 6, 7, 8}),
	D(new short[]{Short.MIN_VALUE, -4, -3, -2, -2, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}),
	E(new short[]{Short.MIN_VALUE, -5, -4, -3, -2, -1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}),
	F(new short[]{Short.MIN_VALUE, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}),
	G(new short[]{Short.MIN_VALUE, -10, -8, -6, -4, -2, -1, 0, 1, 2, 4, 6, 8, 10, 11, 12, 13, 14, 15, 16, 17}),
	H(new short[]{-3, -2, -2, -1, -1, -1, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 4, 4});
	private static final short[] indexMap = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,		// 0 - 10
											 2, 2, 2, 2, 2, 3, 3, 3, 3, 3,          // 11 - 20
											 4, 4, 4, 4, 4, 5, 5, 5, 5, 5,          // 21 - 30
											 5, 5, 5, 5, 5, 6, 6, 6, 6, 6,          // 31 - 40
											 6, 6, 6, 6, 6, 7, 7, 7, 7, 7,          // 41 - 50
											 7, 7, 7, 7, 7, 8, 8, 8, 8, 8,          // 51 - 60
											 8, 8, 8, 8, 8, 9, 9, 9, 9, 9,          // 61 - 70
											 9, 9, 9, 9, 9, 10, 10, 10, 10, 10,     // 71 - 80
											 11, 11, 11, 11, 11, 12, 12, 12, 12, 12,// 80 - 90
											 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, // 91 - 100
											 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, // 101 - 110
											 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, // 111 - 120
											 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, // 121 - 130
											 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, // 131 - 140
											 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, // 141 - 150
											 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, // 151 - 160
											 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, // 161 - 170
											 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, // 171 - 180
											 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, // 181 - 190
											 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, // 191 - 200
											 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, // 201 - 210
											 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, // 211 - 220
											 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, // 221 - 230
											 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, // 231 - 240
											 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, // 241 - 250
											 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, // 251 - 260
											 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, // 261 - 270
											 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, // 271 - 280
											 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, // 281 - 290
											 19, 19, 19, 19, 19, 19, 19, 19, 19, 19}; // 291 - 300

	private short[] levelOffsets;

	LevelSpread(short[] levelOffsets) {
		this.levelOffsets = levelOffsets;
	}

	/**
	 * Gets the amount to offset the level from the typical level. A result of Short.MIN_VALUE indicates a young
	 * of this creature variety should be use instead.
	 *
	 * @param randomResult  the result of an open ended d100 roll.
	 * @return the amount to offset the creatures level from the typical level. A result of Short.MIN_VALUE indicates a young
	 * of this creature variety should be use instead.
	 */
	public short getOffset(short randomResult) {
		short result = levelOffsets[0];

		if(randomResult > 300) {
			result = levelOffsets[20];
		}
		else if(randomResult > 0) {
			result = levelOffsets[indexMap[randomResult]];
		}

		return result;
	}
}

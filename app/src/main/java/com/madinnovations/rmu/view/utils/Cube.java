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
package com.madinnovations.rmu.view.utils;

/**
 * Class representing x,y,z coordinates
 */
public class Cube {
	public float x = 0f;
	public float y = 0f;
	public float z = 0f;

	/**
	 * Creates a new Cube instance
	 */
	public Cube() {
	}

	/**
	 * Creates a new Cube instance with the given coordinates
	 *
	 * @param x  the x coordinate for the new Cube
	 * @param y  the y coordinate for the new Cube
	 * @param z  the z coordinate for the new Cube
	 */
	public Cube(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "Cube(" +
				x + ", " +
				y + ", " +
				z + ")";
	}
}

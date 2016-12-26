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

import android.graphics.PointF;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Utilities for polygons
 */
@Singleton
public class PolygonUtils {

	/**
	 * Creates a new PolygonUtils instance
	 */
	@Inject
	public PolygonUtils() {
	}

	/**
	 * Determines is the given point is inside a polygon.
	 *
	 * @param point  the point to be tested
	 * @param vertices  the vertices of the polygon with vertices[n] == vertices[0]
	 * @return  true if the point is inside the polygon, otherwise false.
	 */
	public boolean isPointInPolygon(PointF point, PointF[] vertices) {
		int windingNumber = 0;

		for(int i = 0; i < vertices.length - 1; i++) {
			if(vertices[i].y <= point.y) {
				if(vertices[i+1].y > point.y) {
					if(isLeft(vertices[i], vertices[i+1], point) > 0) {
						++windingNumber;
					}
				}
			}
			else {
				if(vertices[i+1].y <= point.y) {
					if(isLeft(vertices[i], vertices[i+1], point) < 0) {
						--windingNumber;
					}
				}
			}
		}

		return windingNumber == 0;
	}

	private float isLeft(PointF point, PointF point1, PointF point2) {
		return ((point1.x - point.x) * (point2.y - point.y) - (point2.x - point.x) * (point1.y - point.y));
	}
}

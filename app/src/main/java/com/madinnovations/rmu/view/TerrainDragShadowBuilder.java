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

package com.madinnovations.rmu.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import com.madinnovations.rmu.data.dao.common.Being;
import com.madinnovations.rmu.data.entities.Position;

/**
 * A DragShadowBuilder that draws a hex shaped drag shadow.
 */
public class TerrainDragShadowBuilder extends View.DragShadowBuilder {
	private static final String TAG = "TerrainDragShadowBuilde";
	private Paint    linePaint;
	private Position position;
	private Being    being;

	/**
	 * Creates a new TerrainDragShadowBuilder instance.
	 *
	 * @param view  the view that this TerrainDragShadowBuilder will be associated with
	 */
	public TerrainDragShadowBuilder(TerrainView view, Position position, Being being) {
		super(view);
		this.position = position;
		this.being = being;
		init();
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		float radius = being.getHeight() / 2 + being.getWeaponLength();
		canvas.drawCircle(position.getX() + radius, position.getY() + radius, radius, linePaint);
	}

	@Override
	public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
		float radius = being.getHeight() / 2 + being.getWeaponLength();
		float width = radius * 2;
		outShadowSize.set((int)width, (int) width);
		outShadowTouchPoint.set((int) radius, (int) radius);
	}

	private void init() {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(2f);
		linePaint.setColor(Color.BLUE);
		linePaint.setStyle(Paint.Style.STROKE);
	}
}

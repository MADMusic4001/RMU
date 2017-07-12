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

import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;

/**
 * A DragShadowBuilder that draws a line between the combatant's location to the touch point.
 */
public class DirectionDragShadowBuilder extends View.DragShadowBuilder {
	private static final String TAG = "DirectionDragShadowBuil";
	private TerrainView        terrainView;
	private Paint              linePaint;
	private Being              being;
	private EncounterRoundInfo encounterRoundInfo;

	/**
	 * Creates a new DirectionDragShadowBuilder instance.
	 *
	 * @param view  the view that this DirectionDragShadowBuilder will be associated with
	 */
	public DirectionDragShadowBuilder(TerrainView view, EncounterRoundInfo encounterRoundInfo, Being being) {
		super(view);
		this.terrainView = view;
		this.being = being;
		this.encounterRoundInfo = encounterRoundInfo;
		init();
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		// Drawing is done directly in TerrainView
	}

	@Override
	public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
		try {
			float width = (being.getHeight() / 2 + being.getWeaponLength()) * 2;
			outShadowSize.set((int) width, (int) width);
			outShadowTouchPoint.set((int) width / 2, (int) width / 2);
		}
		catch(NullPointerException e) {
			Log.e(TAG, "onProvideShadowMetrics: this = " + this, e);
			throw e;
		}
	}

	private void init() {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(2f);
		linePaint.setColor(Color.BLUE);
		linePaint.setStyle(Paint.Style.STROKE);
	}
}

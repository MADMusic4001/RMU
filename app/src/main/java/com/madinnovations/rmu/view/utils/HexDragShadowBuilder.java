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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.View;

import com.madinnovations.rmu.view.HexView;

/**
 * A DragShadowBuilder that draws a hex shaped drag shadow.
 */
public class HexDragShadowBuilder extends View.DragShadowBuilder {
	private PointF               hexCorners[];
	private Paint                linePaint;

	/**
	 * Creates a new HexDragShadowBuilder instance.
	 *
	 * @param view  the view that this HexDragShadowBuilder will be associated with
	 */
	public HexDragShadowBuilder(HexView view) {
		super(view);
		init();
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		float dx = HexView.SIZE * ((HexView)getView()).getScaleFactor();
		float dy = (float)(Math.sqrt(3) / 2 * HexView.SIZE) * ((HexView)getView()).getScaleFactor();
		canvas.save();
		canvas.translate(dx, dy);
		drawHexagon(canvas, HexView.SIZE * ((HexView)getView()).getScaleFactor());
		canvas.restore();
	}

	@Override
	public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
		float width = HexView.SIZE * ((HexView)getView()).getScaleFactor() * 2;
		float height = (float)((Math.sqrt(3) / 2 * HexView.SIZE) * ((HexView)getView()).getScaleFactor()) * 2;
		outShadowSize.set((int)width, (int)height);
		outShadowTouchPoint.set((int)width/2, (int)height/2);
	}

	private void init() {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(6f);
		linePaint.setColor(Color.GREEN);
		linePaint.setStyle(Paint.Style.FILL);
	}

	private void drawHexagon(@NonNull Canvas canvas, float size) {
		if (hexCorners == null) {
			hexCorners = new PointF[6];
			for (int i = 0; i < 6; i++) {
				float angleDegrees = 60 * i;
				float angleRadians = (float) Math.PI / 180 * angleDegrees;
				hexCorners[i] = new PointF((float) (size * Math.cos(angleRadians)), (float) (size * Math.sin(angleRadians)));
			}
		}
		Path hexPath = new Path();
		hexPath.moveTo(hexCorners[5].x, hexCorners[5].y);
		for (int i = 0; i < 6; i++) {
			hexPath.lineTo(hexCorners[i].x, hexCorners[i].y);
		}
		canvas.drawPath(hexPath, linePaint);
	}
}

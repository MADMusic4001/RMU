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

package com.madinnovations.rmu.view.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.TerrainView;

/**
 * View that displays the current scale of the TerrainView
 */
public class ScaleView extends View {
	@SuppressWarnings("unused")
	private static final String TAG                = "ScaleView";
	private static final int    HORIZONTAL_PADDING = 2;
	private static final int    VERTICAL_PADDING   = 4;
	private static final int    TEN_FEET_IN_INCHES = 120;
	private TerrainView terrainView;
	private Paint       linePaint;
	private Paint       fontPaint;
	private Rect        textBounds = new Rect();
	private int         textSize;
	private String      text;

	public ScaleView(Context context) {
		super(context);
		init();
	}

	public ScaleView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScaleView(Context context, @Nullable AttributeSet attrs,
					 int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(4f);
		linePaint.setColor(Color.RED);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeJoin(Paint.Join.ROUND);
		fontPaint = new Paint();
//		Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway-bold.ttf");
//		fontPaint.setTypeface(raleway);
		fontPaint.setStrokeWidth(4f);
		fontPaint.setAntiAlias(true);
		textSize = getContext().getResources().getDimensionPixelSize(R.dimen.textSizeInSp);
		fontPaint.setTextSize(textSize);
		fontPaint.setColor(Color.GREEN);
		fontPaint.setTextAlign(Paint.Align.LEFT);
		text = getContext().getResources().getString(R.string.ten_feet);
		fontPaint.getTextBounds(text, 0, text.length(), textBounds);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = textBounds.width() + HORIZONTAL_PADDING * 2;
		int desiredHeight = textSize * 2 + VERTICAL_PADDING * 4;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = Math.max(desiredWidth, widthSize);
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			height = Math.min(desiredHeight, heightSize);
		} else {
			height = desiredHeight;
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(HORIZONTAL_PADDING,
				textSize + VERTICAL_PADDING * 3,
				HORIZONTAL_PADDING,
				textSize*2 + VERTICAL_PADDING * 3,
				linePaint);
		canvas.drawLine(HORIZONTAL_PADDING,
				(float)(textSize * 1.5 + VERTICAL_PADDING * 3),
				terrainView.getScaleFactor() * TEN_FEET_IN_INCHES + HORIZONTAL_PADDING,
				(float)(textSize * 1.5 + VERTICAL_PADDING * 3),
				linePaint);
		canvas.drawLine(TEN_FEET_IN_INCHES * terrainView.getScaleFactor() + HORIZONTAL_PADDING,
				textSize + VERTICAL_PADDING * 3,
				TEN_FEET_IN_INCHES * terrainView.getScaleFactor() + HORIZONTAL_PADDING,
				textSize * 2 + VERTICAL_PADDING * 3,
				linePaint);
		canvas.drawText(text, HORIZONTAL_PADDING, textSize + VERTICAL_PADDING, fontPaint);
	}

	// Getters and setters
	public void setTerrainView(TerrainView terrainView) {
		this.terrainView = terrainView;
	}
}

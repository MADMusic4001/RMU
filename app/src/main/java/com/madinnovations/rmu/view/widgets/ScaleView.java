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
import android.util.Log;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.view.TerrainView;

/**
 * View that displays the current scale of the TerrainView
 */
public class ScaleView extends View {
	private static final String TAG = "ScaleView";
	private TerrainView terrainView;
	private Paint       linePaint;
	private Paint       fontPaint;
	private int         textSize;
	private Rect        textBounds = new Rect();
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
		linePaint.setStrokeWidth(2f);
		linePaint.setColor(Color.RED);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeJoin(Paint.Join.ROUND);
		textSize = getContext().getResources().getDimensionPixelSize(R.dimen.textSizeInSp);
		fontPaint = new Paint();
//		Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway-bold.ttf");
//		fontPaint.setTypeface(raleway);
		fontPaint.setAntiAlias(true);
		fontPaint.setTextSize(textSize);
		fontPaint.setColor(Color.GREEN);
		fontPaint.setTextAlign(Paint.Align.CENTER);
		text = getContext().getResources().getString(R.string.ten_feet);
		fontPaint.getTextBounds(text, 0, text.length(), textBounds);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = textBounds.width();
		int desiredHeight = textBounds.height() + 8;

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

		//Measure Height
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			//Can't be bigger than...
			height = Math.min(desiredHeight, heightSize);
		} else {
			//Be whatever you want
			height = desiredHeight;
		}

		Log.d(TAG, "onMeasure: text = " + text);
		Log.d(TAG, "onMeasure: textBounds.left = " + textBounds.left);
		Log.d(TAG, "onMeasure: textBounds.top = " + textBounds.top);
		Log.d(TAG, "onMeasure: textBounds.right = " + textBounds.right);
		Log.d(TAG, "onMeasure: textBounds.bottom = " + textBounds.bottom);
		Log.d(TAG, "onMeasure: textBounds.width = " + textBounds.width());
		Log.d(TAG, "onMeasure: textBounds.height = " + textBounds.height());
		Log.d(TAG, "onMeasure: width = " + width);
		Log.d(TAG, "onMeasure: height = " + height);
		//MUST CALL THIS
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(0, textBounds.top + 8, 0, getHeight(), linePaint);
		canvas.drawLine(0,  textBounds.top + 8 + (getHeight() - textBounds.top + 8)/ 2, terrainView.getScaleFactor() * 10,
						textBounds.top + 8 + (getHeight() - textBounds.top + 8)/ 2, linePaint);
		canvas.drawLine(10 * terrainView.getScaleFactor(), textBounds.top + 8, 10 * terrainView.getScaleFactor(), getHeight(),
						linePaint);
		canvas.drawText(text, 0, textBounds.top, fontPaint);
	}

	// Getters and setters
	public TerrainView getTerrainView() {
		return terrainView;
	}
	public void setTerrainView(TerrainView terrainView) {
		this.terrainView = terrainView;
	}
}

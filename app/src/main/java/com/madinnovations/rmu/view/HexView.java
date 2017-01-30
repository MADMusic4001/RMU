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
package com.madinnovations.rmu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.play.CombatSetup;
import com.madinnovations.rmu.view.di.modules.ViewsModule;
import com.madinnovations.rmu.view.utils.Cube;
import com.madinnovations.rmu.view.utils.PolygonUtils;

import java.util.Map;

import javax.inject.Inject;

/**
 * View that displays a hex grid for combat positioning.
 */
public class HexView extends View {
	public static final  float  SIZE = 60f;
	@SuppressWarnings("unused")
	private static final String TAG  = "HexView";
	@Inject
	protected PolygonUtils       polygonUtils = new PolygonUtils();
	private Callbacks            callbacks;
	private Bitmap               hexGrid;
	private PointF               hexCorners[];
	private PointF               highlightCenterPoint = null;
	private Paint                linePaint;
	private Paint                fontPaint;
	private Paint                highlightPaint;
	private ScaleGestureDetector scaleGestureDetector;
	private float scaleFactor = 1.f;

	/**
	 * Creates a new HexView instance
	 *
	 * @param context the android context the view should use
	 */
	public HexView(Context context) {
		super(context);
		init();
	}

	/**
	 * Creates a new HexView instance
	 *
	 * @param context the android context the view should use
	 * @param attrs   the attributes to use when creating the view
	 */
	public HexView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Creates a new HexView instance
	 *
	 * @param context      the android context the view should use
	 * @param attrs        the attributes to use when creating the view
	 * @param defStyleAttr the default style attributes to use for the view
	 */
	public HexView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		createGridBitmap();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawGrid(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		scaleGestureDetector.onTouchEvent(event);
		return true;
	}

	/**
	 * Gets the center point of the hex that contains the given point.
	 *
	 * @param point  the point to search
	 * @return a Point representing the center point of the hex containing the given point, otherwise Point(0,0) will be returned.
	 */
	public PointF getCenterPoint(PointF point) {
		PointF result = new PointF();
		PointF coords;

		float height = (float) Math.sqrt(3) / 2 * SIZE;
		PointF pointf = new PointF();
		pointf.x = point.x / scaleFactor - SIZE;
		pointf.y = point.y / scaleFactor - height;
		float q = pointf.x * 2/3 / SIZE;
		float xAdjust = pointf.x % SIZE*2;
		float r = (float)(-xAdjust/3.0 + Math.sqrt(3.0f)/3.0f * pointf.y) / SIZE;

		coords = polygonUtils.cubeToHex(polygonUtils.cubeRound(new Cube(q, -q-r, r)));
		result.x = coords.x * SIZE * 3 / 2 + SIZE;
		result.y = coords.y * height * 2 + height + 1;
		if(coords.x % 2 == 1) {
			result.y += height;
		}

		return result;
	}

	/**
	 * Sets the hex containing the given point to be highlighted.
	 *
	 * @param point  a {@link PointF} instance
	 * @return true if the current highlight hex was changed, otherwise false.
	 */
	public boolean setHighlightHex(PointF point) {
		boolean changed = false;
		PointF centerPoint = getCenterPoint(point);
		if(highlightCenterPoint == null || centerPoint.x != highlightCenterPoint.x || centerPoint.y != highlightCenterPoint.y) {
			highlightCenterPoint = centerPoint;
			changed = true;
		}

		return changed;
	}

	// Getters
	public float getScaleFactor() {
		return scaleFactor;
	}

	private void init() {
		((RMUApp)getContext().getApplicationContext()).getApplicationComponent().newViewsComponent(new ViewsModule(this));
		Log.d(TAG, "init: polygonUtils = " + polygonUtils);
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(6f);
		linePaint.setColor(Color.BLACK);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeJoin(Paint.Join.ROUND);
		fontPaint = new Paint();
		Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf");
		fontPaint.setTypeface(raleway);
		fontPaint.setAntiAlias(true);
		fontPaint.setTextSize(20);
		fontPaint.setColor(Color.BLACK);
		fontPaint.setTextAlign(Paint.Align.CENTER);
		highlightPaint = new Paint();
		highlightPaint.setAntiAlias(true);
		highlightPaint.setStrokeWidth(6f);
		highlightPaint.setColor(Color.RED);
		highlightPaint.setStyle(Paint.Style.STROKE);
		highlightPaint.setStrokeJoin(Paint.Join.ROUND);
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}

	private void drawGrid(Canvas canvas) {
		Rect drawRect = new Rect();
		getDrawingRect(drawRect);
		canvas.drawColor(Color.WHITE);
		if (hexGrid != null) {
			canvas.scale(scaleFactor, scaleFactor);
			canvas.drawBitmap(hexGrid, 0, 0, null);
		}
		if(highlightCenterPoint != null) {
			canvas.save();
			canvas.translate(highlightCenterPoint.x, highlightCenterPoint.y);
			canvas.scale(scaleFactor, scaleFactor);
			drawHexagon(canvas, SIZE, highlightPaint);
			canvas.restore();
		}
		if(callbacks != null) {
			for(Map.Entry<Character, PointF> entry : callbacks.getCombatSetup().getCharacterLocations().entrySet()) {
				canvas.save();
				canvas.scale(scaleFactor, scaleFactor);
				drawHexagon(canvas, SIZE, highlightPaint);
				String initials = entry.getKey().getKnownAs().substring(0, entry.getKey().getKnownAs().length() < 3 ?
					entry.getKey().getKnownAs().length() : 3);
				canvas.drawText(initials, entry.getValue().x, entry.getValue().y, fontPaint);
				canvas.restore();
			}
		}
	}

	private void createGridBitmap() {
		if (hexGrid != null) {
			hexGrid.recycle();
			hexGrid = null;
		}
		hexGrid = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas bitmapCanvas = new Canvas(hexGrid);
		bitmapCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
		float height = (float) Math.sqrt(3) / 2 * SIZE;
		for (int i = (int) SIZE; i < getWidth(); i += SIZE * 3) {
			float y = height;
			while (y < getHeight()) {
				bitmapCanvas.save();
				bitmapCanvas.translate(i, y);
				drawHexagon(bitmapCanvas, SIZE, linePaint);
				bitmapCanvas.restore();
				y += height * 2;
			}
		}
		for (int i = (int) (SIZE * 5 / 2); i < getWidth(); i += SIZE * 3) {
			float y = height * 2;
			while (y < getHeight()) {
				bitmapCanvas.save();
				bitmapCanvas.translate(i, y);
				drawHexagon(bitmapCanvas, SIZE, linePaint);
				bitmapCanvas.restore();
				y += height * 2;
			}
		}
	}

	private void drawHexagon(@NonNull Canvas canvas, float size, Paint linePaint) {
		if (hexCorners == null) {
			hexCorners = new PointF[7];
			for (int i = 0; i < 6; i++) {
				float angleDegrees = 60 * i;
				float angleRadians = (float) Math.PI / 180 * angleDegrees;
				hexCorners[i] = new PointF((float) (size * Math.cos(angleRadians)), (float) (size * Math.sin(angleRadians)));
			}
			hexCorners[6] = hexCorners[0];
		}
		for (int i = 0; i < 5; i++) {
			canvas.drawLine(hexCorners[i].x, hexCorners[i].y, hexCorners[i + 1].x, hexCorners[i + 1].y, linePaint);
		}
		canvas.drawLine(hexCorners[5].x, hexCorners[5].y, hexCorners[0].x, hexCorners[0].y, linePaint);
	}

	private class ScaleListener
			extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 5.0f));

			invalidate();
			return true;
		}
	}

	// Getters and setters
	public PointF getHighlightCenterPoint() {
		return highlightCenterPoint;
	}
	public void clearHighlightCenterPoint() {
		this.highlightCenterPoint = null;
	}
	public Callbacks getCallbacks() {
		return callbacks;
	}
	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}

	public interface Callbacks {
		CombatSetup getCombatSetup();
	}
}
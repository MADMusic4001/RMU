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
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.di.modules.ViewsModule;
import com.madinnovations.rmu.view.utils.Cube;
import com.madinnovations.rmu.view.utils.PolygonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	protected PolygonUtils         polygonUtils = new PolygonUtils();
	private Callbacks              callbacks;
	private Bitmap                 hexGrid;
	private PointF                 hexCorners[];
	private PointF                 highlightCenterPoint = null;
	private Paint                  linePaint;
	private Paint                  fontPaint;
	private Paint                  highlightPaint;
	private ScaleGestureDetector   scaleGestureDetector;
	private GestureDetector        gestureDetector;
	private HexViewContextMenuInfo hexViewContextMenuInfo;
	private float                  scaleFactor = 1.f;
	private int                    textSize;

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
		boolean result = gestureDetector.onTouchEvent(event);
		result |= scaleGestureDetector.onTouchEvent(event);

		return  result;
	}

	@Override
	protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
		Log.d(TAG, "getContextMenuInfo: ");
		return hexViewContextMenuInfo;
	}

	/**
	 * Gets the hex coordinate that contains the given pixel coordinates.
	 *
	 * @param pixelCoordinates  the point to locate
	 * @return a Point instance containing the coordinates
	 */
	public Point getHexCoordinates(PointF pixelCoordinates) {
		PointF floatCoords;

		float height = (float) Math.sqrt(3) / 2 * SIZE;
		PointF pointf = new PointF();
		pointf.x = pixelCoordinates.x / scaleFactor - SIZE;
		pointf.y = pixelCoordinates.y / scaleFactor - height;
		float q = pointf.x * 2/3 / SIZE;
		float xAdjust = pointf.x % SIZE*2;
		float r = (float)(-xAdjust/3.0 + Math.sqrt(3.0f)/3.0f * pointf.y) / SIZE;

		floatCoords = polygonUtils.cubeToHex(polygonUtils.cubeRound(new Cube(q, -q-r, r)));

		return new Point((int)floatCoords.x, (int)floatCoords.y);
	}

	/**
	 * Gets the center point of the hex that contains the given point.
	 *
	 * @param pixelCoordinates  the point to search
	 * @return a PointF instance representing the center point of the hex containing the given point.
	 */
	public PointF getCenterPoint(PointF pixelCoordinates) {
		Point hexCoords = getHexCoordinates(pixelCoordinates);
		return getCenterPoint(hexCoords);
	}

	/**
	 * Gets the center point of the hex that contains the given point.
	 *
	 * @param hexCoordinates  the hex coordinate
	 * @return a PointF instance representing the center point of the hex containing the given point.
	 */
	public PointF getCenterPoint(Point hexCoordinates) {
		PointF result = new PointF();
		float height = (float) Math.sqrt(3) / 2 * SIZE;
		result.x = hexCoordinates.x * SIZE * 3 / 2 + SIZE;
		result.y = hexCoordinates.y * height * 2 + height + 1;
		if(hexCoordinates.x % 2 == 1) {
			result.y += height;
		}

		return result;
	}

	/**
	 * Sets the hex containing the given point to be highlighted.
	 *
	 * @param pixelCoordinates  a {@link PointF} instance with pixel coordinates.
	 * @return true if the current highlight hex was changed, otherwise false.
	 */
	public boolean setHighlightHex(PointF pixelCoordinates) {
		return setHighlightHex(getHexCoordinates(pixelCoordinates));
	}

	/**
	 * Sets the hex containing the given point to be highlighted.
	 *
	 * @param hexCoordinates  a {@link Point} instance with hex coordinates
	 * @return true if the current highlight hex was changed, otherwise false.
	 */
	public boolean setHighlightHex(Point hexCoordinates) {
		boolean changed = false;
		PointF centerPoint = getCenterPoint(hexCoordinates);
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
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(6f);
		linePaint.setColor(Color.BLACK);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeJoin(Paint.Join.ROUND);
		textSize = getContext().getResources().getDimensionPixelSize(R.dimen.textSizeInSp);
		fontPaint = new Paint();
		Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf");
		fontPaint.setTypeface(raleway);
		fontPaint.setAntiAlias(true);
		fontPaint.setTextSize(textSize);
		fontPaint.setColor(Color.BLACK);
		fontPaint.setTextAlign(Paint.Align.CENTER);
		highlightPaint = new Paint();
		highlightPaint.setAntiAlias(true);
		highlightPaint.setStrokeWidth(6f);
		highlightPaint.setColor(Color.RED);
		highlightPaint.setStyle(Paint.Style.STROKE);
		highlightPaint.setStrokeJoin(Paint.Join.ROUND);
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		gestureDetector = new GestureDetector(getContext(), new HexViewGestureListener());
	}

	private void drawGrid(Canvas canvas) {
		Rect drawRect = new Rect();
		getDrawingRect(drawRect);
		canvas.drawColor(Color.WHITE);
		canvas.save();
		canvas.scale(scaleFactor, scaleFactor);
		if (hexGrid != null) {
			canvas.drawBitmap(hexGrid, 0, 0, null);
		}
		if(highlightCenterPoint != null) {
			canvas.save();
			canvas.translate(highlightCenterPoint.x, highlightCenterPoint.y);
			drawHexagon(canvas, SIZE, highlightPaint);
			canvas.restore();
		}
		if(callbacks != null) {
			Map<Point, List<String>> hexStringsMap = new HashMap<>();
			for(Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup().getCharacterCombatInfo().entrySet()) {
				String initials = entry.getKey().getKnownAs().substring(0, entry.getKey().getKnownAs().length() < 3 ?
																	entry.getKey().getKnownAs().length() : 3);
				List<String> hexStrings = hexStringsMap.get(entry.getValue().getHexCoordinate());
				if(hexStrings == null) {
					hexStrings = new ArrayList<>();
					hexStringsMap.put(entry.getValue().getHexCoordinate(), hexStrings);
				}
				hexStrings.add(initials);
			}
			for(Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup().getEnemyCombatInfo().entrySet()) {
				String abbreviation = entry.getKey().getCreatureVariety().getName().substring(0, 3);
				List<String> hexStrings = hexStringsMap.get(entry.getValue().getHexCoordinate());
				if(hexStrings == null) {
					hexStrings = new ArrayList<>();
					hexStringsMap.put(entry.getValue().getHexCoordinate(), hexStrings);
				}
				hexStrings.add(abbreviation);
			}

			for(Map.Entry<Point, List<String>> entry : hexStringsMap.entrySet()) {
				int offset = (entry.getValue().size() * textSize / 2) - textSize/2;
				PointF pixelCoords = getCenterPoint(entry.getKey());
				for(String initials : entry.getValue()) {
					canvas.drawText(initials, pixelCoords.x, pixelCoords.y + offset, fontPaint);
					offset -= textSize;
				}
			}
		}
		canvas.restore();
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
		EncounterSetup getEncounterSetup();
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 5.0f));

			invalidate();
			return true;
		}
	}

	private class HexViewGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			Log.d(TAG, "onLongPress: ");
			hexViewContextMenuInfo = new HexViewContextMenuInfo();
			hexViewContextMenuInfo.hexView = HexView.this;
			hexViewContextMenuInfo.hexCoordinates = HexView.this.getHexCoordinates(new PointF(e.getX(), e.getY()));
			HexView.this.showContextMenu();
		}
	}

	public class HexViewContextMenuInfo implements ContextMenu.ContextMenuInfo {
		public HexView hexView;
		public Point  hexCoordinates;
	}
}
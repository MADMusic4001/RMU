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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${CLASS_DESCRIPTION}
 *
 * @author Mark
 *         Created 6/18/2017.
 */
public class TerrainView extends View{
	public static final  float  SIZE = 60f;
	private static final String TAG = "TerrainView";
	private static final float                      SIXTY_DEGREE_RADIANS;
	private              Callbacks                  callbacks;
	private              Bitmap                     frameBuffer;
	private              Canvas                     bufferCanvas;
	private              PointF                     highlightPoint;
	private              Rect                       srcRect;
	private              RectF                      destRect;
	private              Paint                      linePaint;
	private              Paint                      fontPaint;
	private              Paint                      frontPaint;
	private              Paint                      flankPaint;
	private              Paint                      rearPaint;
	private              ScaleGestureDetector       scaleGestureDetector;
	private              GestureDetector            gestureDetector;
	private              TerrainViewContextMenuInfo terrainViewContextMenuInfo;
	private float                                   scaleFactor = 1.f;
	private int                                     textSize;

	static {
		SIXTY_DEGREE_RADIANS = (float)Math.toRadians(60);
	}
	/**
	 * Creates a new TerrainView instance
	 *
	 * @param context the android context the view should use
	 */
	public TerrainView(Context context) {
		super(context);
		init();
	}

	/**
	 * Creates a new TerrainView instance
	 *
	 * @param context the android context the view should use
	 * @param attrs   the attributes to use when creating the view
	 */
	public TerrainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Creates a new TerrainView instance
	 *
	 * @param context      the android context the view should use
	 * @param attrs        the attributes to use when creating the view
	 * @param defStyleAttr the default style attributes to use for the view
	 */
	public TerrainView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		bufferCanvas.drawColor(Color.WHITE);
		drawScale(canvas);
		if(callbacks != null) {
			for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getCharacterCombatInfo()
					.entrySet()) {
				drawCombatant(bufferCanvas, entry.getKey().getPosition());
			}
			for (Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getEnemyCombatInfo()
					.entrySet()) {
				drawCombatant(bufferCanvas, entry.getKey().getPosition());
			}
		}

		canvas.drawBitmap(frameBuffer, srcRect, destRect, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = gestureDetector.onTouchEvent(event);
		result |= scaleGestureDetector.onTouchEvent(event);
		return  result;
	}

	@Override
	protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
		return terrainViewContextMenuInfo;
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
//		Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "fonts/raleway-bold.ttf");
//		fontPaint.setTypeface(raleway);
		fontPaint.setAntiAlias(true);
		fontPaint.setTextSize(textSize);
		fontPaint.setColor(Color.BLACK);
		fontPaint.setTextAlign(Paint.Align.CENTER);
		frontPaint = new Paint();
		frontPaint.setAntiAlias(true);
		frontPaint.setStrokeWidth(6f);
		frontPaint.setColor(Color.GREEN);
		frontPaint.setStyle(Paint.Style.STROKE);
		frontPaint.setStrokeJoin(Paint.Join.ROUND);
		flankPaint = new Paint();
		flankPaint.setAntiAlias(true);
		flankPaint.setStrokeWidth(6f);
		flankPaint.setColor(Color.YELLOW);
		flankPaint.setStyle(Paint.Style.STROKE);
		flankPaint.setStrokeJoin(Paint.Join.ROUND);
		rearPaint = new Paint();
		rearPaint.setAntiAlias(true);
		rearPaint.setStrokeWidth(6f);
		rearPaint.setColor(Color.RED);
		rearPaint.setStyle(Paint.Style.STROKE);
		rearPaint.setStrokeJoin(Paint.Join.ROUND);
		srcRect = new Rect(-500, 500, 500, -500);
		destRect = new RectF(0, 0, getWidth(), getHeight());
		frameBuffer = Bitmap.createBitmap(1000, 1000, Config.ARGB_8888);
		bufferCanvas = new Canvas(frameBuffer);
		bufferCanvas.translate(-500, -500);
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		gestureDetector = new GestureDetector(getContext(), new TerrainViewGestureListener());
	}

	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}

	private void drawScale(Canvas canvas) {
		canvas.drawLine(-5, 490, -5, 489, linePaint);
		canvas.drawLine(5, 490, 5, 489, linePaint);
		canvas.drawLine(-5, 489.5f, 5, 489.5f, linePaint);
		canvas.drawText("10 feet", -3, 492, linePaint);
	}

	private void drawCombatant(Canvas canvas, Position position) {
		float innerRadius = position.getHeight() / 2;
		float outerRadius = innerRadius + position.getWeaponLength();
		RectF oval = new RectF(getX() - outerRadius, getY() + outerRadius, getX() + outerRadius, getY() - outerRadius);
		canvas.drawArc(oval, (float)(position.getDirection() - Math.PI /2),
					   (float)(position.getDirection() + Math.PI/2), true, frontPaint);
		canvas.drawArc(oval, (float)(position.getDirection() - Math.PI/2 - SIXTY_DEGREE_RADIANS),
					   (float)(position.getDirection() - Math.PI/2), true, flankPaint);
		canvas.drawArc(oval, (float)(position.getDirection() +Math.PI/2),
					   (float)(position.getDirection() + Math.PI/2 + SIXTY_DEGREE_RADIANS), true, flankPaint);
		canvas.drawArc(oval, (float)(position.getDirection() + Math.PI/2 + SIXTY_DEGREE_RADIANS),
					   (float)(position.getDirection() + Math.PI/2 + SIXTY_DEGREE_RADIANS*2), true, rearPaint);
		canvas.drawCircle(position.getX(), position.getY(), innerRadius, linePaint);
		if(position.getWeaponLength() > 0) {
			canvas.drawCircle(position.getX(), position.getY(), outerRadius, linePaint);
		}
	}

	// Getters and setters
	public interface Callbacks {
		EncounterSetup getEncounterSetup();
	}
	public float getScaleFactor() {
		return scaleFactor;
	}
	public PointF getHighlightPoint() {
		return highlightPoint;
	}
	public void setHighlightPoint(PointF highlightPoint) {
		this.highlightPoint = highlightPoint;
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

	private class TerrainViewGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			terrainViewContextMenuInfo = new TerrainViewContextMenuInfo();
			terrainViewContextMenuInfo.terrainView = TerrainView.this;
			terrainViewContextMenuInfo.terrainCoordinates = new PointF(e.getX(), e.getY());
			TerrainView.this.showContextMenu();
		}
	}

	public class TerrainViewContextMenuInfo implements ContextMenu.ContextMenuInfo {
		public TerrainView terrainView;
		public PointF       terrainCoordinates;
	}
}

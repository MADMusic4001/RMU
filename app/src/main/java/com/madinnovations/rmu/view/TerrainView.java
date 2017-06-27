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

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.dao.common.Being;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.CombatPosition;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import java.util.Collection;
import java.util.Map;

/**
 * View of the 'battlefield'
 */
public class TerrainView extends View{
	private static final String               TAG = "TerrainView";
	public  static final String               DRAG_DIRECTION = "drag-direction";
	public  static final String               DRAG_LOCATION = "drag-location";
	public  static final float                SIZE = 60f;
	private static final float                SIXTY_DEGREE_RADIANS;
	private              Callbacks            callbacks;
	private              Rect                 srcRect;
	private              RectF                destRect;
	private              Paint                linePaint;
	private              Paint                fontPaint;
	private              Paint                frontPaint;
	private              Paint                flankPaint;
	private              Paint                rearPaint;
	private              ScaleGestureDetector scaleGestureDetector;
	private              GestureDetector      gestureDetector;
	private              float                scaleFactor = 1.0f;
	private              int                  textSize;
	private              float                lastX;
	private              float                lastY;

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
		if(callbacks != null) {
			for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getCharacterCombatInfo()
					.entrySet()) {
				if(entry.getValue().getPosition() != null) {
					drawCombatant(canvas, entry.getValue().getPosition(), entry.getKey().getHeight(),
								  entry.getKey().getWeaponLength());
				}
			}
			for (Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getEnemyCombatInfo()
					.entrySet()) {
				if (entry.getValue().getPosition() != null) {
					drawCombatant(canvas, entry.getValue().getPosition(), entry.getKey().getCreatureVariety().getHeight(),
								  entry.getKey().getWeaponLength());
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = gestureDetector.onTouchEvent(event);
		result |= scaleGestureDetector.onTouchEvent(event);
		return  result;
	}

	private void init() {
		((RMUApp)getContext().getApplicationContext()).getApplicationComponent().newViewsComponent(new ViewsModule(this));
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(2f);
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
		frontPaint.setStrokeWidth(2f);
		frontPaint.setColor(Color.GREEN);
		frontPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		frontPaint.setStrokeJoin(Paint.Join.ROUND);
		flankPaint = new Paint();
		flankPaint.setAntiAlias(true);
		flankPaint.setStrokeWidth(2f);
		flankPaint.setColor(Color.YELLOW);
		flankPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		flankPaint.setStrokeJoin(Paint.Join.ROUND);
		rearPaint = new Paint();
		rearPaint.setAntiAlias(true);
		rearPaint.setStrokeWidth(2f);
		rearPaint.setColor(Color.RED);
		rearPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		rearPaint.setStrokeJoin(Paint.Join.ROUND);
		srcRect = new Rect(-500, 500, 500, -500);
		destRect = new RectF(0, 0, getWidth(), getHeight());
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		gestureDetector = new GestureDetector(getContext(), new TerrainViewGestureListener());

		setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();
				PointF pointf = new PointF(event.getX(), event.getY());
				TerrainView.this.lastX = event.getX();
				TerrainView.this.lastY = event.getY();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null &&
								(DRAG_LOCATION.equals(event.getClipDescription().getLabel()) ||
								DRAG_DIRECTION.equals(event.getClipDescription().getLabel()))) {
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if(event.getClipDescription() != null &&
								DRAG_LOCATION.equals(event.getClipDescription().getLabel()) ||
								DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						if(event.getClipDescription() != null &&
								(DRAG_LOCATION.equals(event.getClipDescription().getLabel()) ||
								DRAG_DIRECTION.equals(event.getClipDescription().getLabel()))) {
							v.invalidate();
						}
						if(DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
							Being being = (Being)event.getLocalState();
							EncounterRoundInfo encounterRoundInfo = null;
							if(being instanceof Character) {
								encounterRoundInfo = callbacks.getEncounterSetup().getCharacterCombatInfo().get((Character)being);
							}
							else if(being instanceof Creature) {
								encounterRoundInfo = callbacks.getEncounterSetup().getEnemyCombatInfo().get((Creature)being);
							}
							if(encounterRoundInfo != null) {
								Position position = encounterRoundInfo.getPosition();
								float angle = (float)Math.atan2(pointf.y - position.getY(),
																pointf.x - position.getX());
								position.setDirection(angle);
								v.invalidate();
							}
						}
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						if(event.getClipDescription() != null &&
								(DRAG_LOCATION.equals(event.getClipDescription().getLabel()) ||
								DRAG_DIRECTION.equals(event.getClipDescription().getLabel()))) {
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DROP:
						if(event.getClipDescription() != null) {
							if (DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								EncounterSetup encounterSetup = callbacks.getEncounterSetup();
								Being being = (Being)event.getLocalState();
								Character character = null;
								Creature creature = null;
								EncounterRoundInfo encounterRoundInfo = null;
								if(being instanceof Character) {
									character = (Character)being;
									encounterRoundInfo = encounterSetup.getCharacterCombatInfo().get(character);
									if(encounterRoundInfo == null) {
										encounterRoundInfo = new EncounterRoundInfo();
									}
									Position position = encounterRoundInfo.getPosition();
									if(position == null) {
										position = new Position();
									}
									position.setX(pointf.x);
									position.setY(pointf.y);
									encounterRoundInfo.setPosition(position);
									encounterSetup.getCharacterCombatInfo().put(character, encounterRoundInfo);
								}
								else if(being instanceof Creature) {
									creature = (Creature)being;
									encounterRoundInfo = encounterSetup.getEnemyCombatInfo().get(creature);
									if(encounterRoundInfo == null) {
										encounterRoundInfo = new EncounterRoundInfo();
									}
									Position position = encounterRoundInfo.getPosition();
									if(position == null) {
										position = new Position();
									}
									position.setX(pointf.x);
									position.setY(pointf.y);
									encounterRoundInfo.setPosition(position);
									encounterSetup.getEnemyCombatInfo().put(creature, encounterRoundInfo);
								}
								callbacks.enableEncounterButton(encounterSetup.getCharacterCombatInfo().size() > 0 &&
																		encounterSetup.getEnemyCombatInfo().size() > 0);
							}
							else if(DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int characterId = Integer.valueOf(item.getText().toString());
								for (Character aCharacter : callbacks.getCharacters()) {
									if (aCharacter.getId() == characterId) {
										EncounterRoundInfo encounterRoundInfo
												= callbacks.getEncounterSetup().getCharacterCombatInfo().get(aCharacter);
										if(encounterRoundInfo == null) {
											encounterRoundInfo = new EncounterRoundInfo();
										}
										Position position = encounterRoundInfo.getPosition();
										float angle = (float)Math.atan2(pointf.y - position.getY(),
																		pointf.x - position.getX());
										position.setDirection(angle);
										break;
									}
								}
							}
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						v.invalidate();
						break;
				}
				return true;
			}
		});

	}

	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}

	private void drawCombatant(Canvas canvas, Position position, float height, float weaponLength) {
		float innerRadius = (height / 2) * scaleFactor;
		float outerRadius = (innerRadius + weaponLength * 12) * scaleFactor;
		RectF oval = new RectF(position.getX() - outerRadius, position.getY() - outerRadius,
							   position.getX() + outerRadius, position.getY() + outerRadius);
		float directionDegrees = (float)Math.toDegrees(position.getDirection());
		canvas.drawArc(oval, directionDegrees - 90, 180, true, frontPaint);
		canvas.drawArc(oval, directionDegrees - 150, 60, true, flankPaint);
		canvas.drawArc(oval, directionDegrees + 90, 60, true, flankPaint);
		canvas.drawArc(oval, directionDegrees + 150, 60, true, rearPaint);
		canvas.drawCircle(position.getX(), position.getY(), innerRadius, linePaint);
		if(weaponLength > 0) {
			canvas.drawCircle(position.getX(), position.getY(), outerRadius, linePaint);
		}
	}

	// Getters and setters
	public float getScaleFactor() {
		return scaleFactor;
	}
	public float getLastX() {
		return lastX;
	}
	public float getLastY() {
		return lastY;
	}

	public interface Callbacks {
		Collection<Character> getCharacters();
		Collection<Creature> getCreatures();
		EncounterSetup getEncounterSetup();
		void enableEncounterButton(boolean enable);
		void scaleChanged(float newScaleFactor);
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			scaleFactor = Math.max(0.25f, Math.min(scaleFactor, 5.0f));

			callbacks.scaleChanged(scaleFactor);
			invalidate();
			return true;
		}
	}

	private class TerrainViewGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
			if(callbacks != null) {
				ClipData dragData = null;
				for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
						.getCharacterCombatInfo()
						.entrySet()) {
					if(entry.getValue().getPosition() != null) {
						float weaponLength = entry.getKey().getWeaponLength();
						CombatPosition combatPosition = entry.getValue().getPosition().getPointIn(
								lastX, lastY, entry.getKey().getHeight(), weaponLength);
						String characterIdString = String.valueOf(entry.getKey().getId());
						ClipData.Item clipDataItem = new ClipData.Item(characterIdString);
						DragShadowBuilder myShadowBuilder = null;
						switch (combatPosition) {
							case FRONT:
								dragData = new ClipData(DRAG_DIRECTION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
										clipDataItem);
								myShadowBuilder = new DirectionDragShadowBuilder(
										TerrainView.this, entry.getValue(), entry.getKey());
								break;
							case LEFT_FLANK:
							case RIGHT_FLANK:
							case REAR:
								dragData = new ClipData(DRAG_LOCATION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
										clipDataItem);
								myShadowBuilder = new TerrainDragShadowBuilder(
										TerrainView.this, entry.getValue().getPosition(), entry.getKey());
								break;
						}
						if(dragData != null) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								TerrainView.this.startDragAndDrop(dragData, myShadowBuilder, entry.getKey(), 0);
							}
							else {
								//noinspection deprecation
								TerrainView.this.startDrag(dragData, myShadowBuilder, entry.getKey(), 0);
							}
						}
					}
				}
				if(dragData == null) {
					for (Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
							.getEnemyCombatInfo()
							.entrySet()) {
						if (entry.getValue().getPosition() != null) {
							float weaponLength = entry.getKey().getWeaponLength();
							CombatPosition combatPosition = entry.getValue().getPosition().getPointIn(
									lastX, lastY, entry.getKey().getHeight(), weaponLength);
							String creatureIdString = String.valueOf(entry.getKey().getId());
							ClipData.Item clipDataItem = new ClipData.Item(creatureIdString);
							DragShadowBuilder myShadowBuilder = null;
							switch (combatPosition) {
								case FRONT:
									dragData = new ClipData(DRAG_DIRECTION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
															clipDataItem);
									myShadowBuilder = new DirectionDragShadowBuilder(
											TerrainView.this, entry.getValue(), entry.getKey());
									break;
								case LEFT_FLANK:
								case RIGHT_FLANK:
								case REAR:
									dragData = new ClipData(DRAG_LOCATION, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
															clipDataItem);
									myShadowBuilder = new TerrainDragShadowBuilder(
											TerrainView.this, entry.getValue().getPosition(), entry.getKey());
									break;
							}
							if (dragData != null) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									TerrainView.this.startDragAndDrop(dragData, myShadowBuilder, entry.getKey(), 0);
								}
								else {
									//noinspection deprecation
									TerrainView.this.startDrag(dragData, myShadowBuilder, entry.getKey(), 0);
								}
							}
						}
					}
				}
			}
		}

		@Override
		public boolean onDown(MotionEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			return super.onDown(e);
		}
	}
}

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
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.controller.rxhandler.common.SkillRxHandler;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.CombatPosition;
import com.madinnovations.rmu.data.entities.common.Being;
import com.madinnovations.rmu.data.entities.common.Skill;
import com.madinnovations.rmu.data.entities.common.SkillBonus;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * View of the 'battlefield'
 */
public class TerrainView extends View {
	private static final String TAG            = "TerrainView";
	public static final  String DRAG_DIRECTION = "drag-direction";
	public static final  String DRAG_LOCATION  = "drag-location";
	private static final int    SPACING        = 4;
	@Inject
	protected SkillRxHandler       skillRxHandler;
	private   Skill                bodyDevelopmentSkill = null;
	private   Callbacks            callbacks;
	private   Paint                linePaint;
	private   Paint                fontPaint;
	private   Paint                frontPaint;
	private   Paint                flankPaint;
	private   Paint                rearPaint;
	private   Paint                healthPaint;
	private   Paint                unconsciousPaint;
	private   ScaleGestureDetector scaleGestureDetector;
	private   GestureDetector      gestureDetector;
	private   float                scaleFactor = 1.0f;
	private   int                  textSize;
	private   Position             sourcePoint;
	private   float                lastX;
	private   float                lastY;
	private   float                offsetX = 0.0f;
	private   float                offsetY = 0.0f;
	private   boolean              directionDragging = false;

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
		if (callbacks != null) {
			for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getCharacterCombatInfo()
					.entrySet()) {
				if (entry.getValue().getPosition() != null) {
					int currentHitPoints  = entry.getKey().getCurrentHits();
					int maxHitPoints      = entry.getKey().getMaxHits();
					int bodyDevSkillBonus = 0;
					if(bodyDevelopmentSkill != null) {
						bodyDevSkillBonus = Skill.getRankBonus(entry.getKey().getSkillRanks().get(bodyDevelopmentSkill));
					}

					if(maxHitPoints == 0) {
						if(currentHitPoints == 0) {
							if(bodyDevSkillBonus == 0) {
								maxHitPoints = currentHitPoints = 25;
								bodyDevSkillBonus = 5;
							}
							else {
								maxHitPoints = currentHitPoints = bodyDevSkillBonus;
							}
						}
						else {
							maxHitPoints = currentHitPoints;
						}
					}

					drawCombatant(canvas, entry.getValue().getPosition(), entry.getKey().getHeight(),
							entry.getKey().getWeaponLength(), entry.getKey().getKnownAs(), maxHitPoints,
							currentHitPoints, bodyDevSkillBonus);
				}
			}
			for (Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
					.getEnemyCombatInfo()
					.entrySet()) {
				if (entry.getValue().getPosition() != null) {
					int currentHitPoints  = entry.getKey().getCurrentHits();
					int maxHitPoints      = entry.getKey().getMaxHits();
					int bodyDevSkillBonus = 0;
					if(bodyDevelopmentSkill != null) {
						for(SkillBonus skillBonus : entry.getKey().getCreatureVariety().getSkillBonusesList()) {
							if(skillBonus.getSkill().equals(bodyDevelopmentSkill)) {
								bodyDevSkillBonus = skillBonus.getBonus();
								break;
							}
						}
					}

					if(maxHitPoints == 0) {
						if(currentHitPoints == 0) {
							if(bodyDevSkillBonus == 0) {
								maxHitPoints = currentHitPoints = 25;
								bodyDevSkillBonus = 5;
							}
							else {
								maxHitPoints = currentHitPoints = bodyDevSkillBonus;
							}
						}
						else {
							maxHitPoints = currentHitPoints;
						}
					}

					drawCombatant(canvas, entry.getValue().getPosition(), entry.getKey().getCreatureVariety().getHeight(),
							entry.getKey().getWeaponLength(), entry.getKey().getCreatureVariety().getName(), maxHitPoints,
							currentHitPoints, bodyDevSkillBonus);
				}
			}
		}
		if (directionDragging && sourcePoint != null) {
			canvas.drawLine(sourcePoint.getX(), sourcePoint.getY(), lastX, lastY, linePaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = gestureDetector.onTouchEvent(event);
		result |= scaleGestureDetector.onTouchEvent(event);
		return result;
	}

	private void init() {
		((RMUApp) getContext().getApplicationContext()).getApplicationComponent().newViewsComponent(new ViewsModule(this)).injectInto(this);
		skillRxHandler.getByName("Body Development").subscribe(new Subscriber<Skill>() {
			@Override
			public void onCompleted() {}
			@Override
			public void onError(Throwable e) {
				Log.e(TAG, "onError: Exception caught loading Body Development skill", e);
			}
			@Override
			public void onNext(Skill skill) {
				bodyDevelopmentSkill = skill;
			}
		});
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
		healthPaint = new Paint();
		healthPaint.setAntiAlias(true);
		healthPaint.setStrokeWidth(2f);
		healthPaint.setColor(Color.CYAN);
		healthPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		healthPaint.setStrokeJoin(Paint.Join.ROUND);
		unconsciousPaint = new Paint();
		unconsciousPaint.setAntiAlias(true);
		unconsciousPaint.setStrokeWidth(2f);
		unconsciousPaint.setColor(Color.MAGENTA);
		unconsciousPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		unconsciousPaint.setStrokeJoin(Paint.Join.ROUND);
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		gestureDetector = new GestureDetector(getContext(), new TerrainViewGestureListener());

		setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();
				PointF    pointf = new PointF(event.getX(), event.getY());
				TerrainView.this.lastX = event.getX();
				TerrainView.this.lastY = event.getY();
				boolean result = false;

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if (event.getClipDescription() != null) {
							Being being = (Being)event.getLocalState();
							sourcePoint = getSourcePoint(being, pointf);
							if(DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								result = true;
							}
							else if(DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								directionDragging = true;
								result = true;
							}
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if (event.getClipDescription() != null) {
							Being being = (Being)event.getLocalState();
							sourcePoint = getSourcePoint(being, pointf);
							if (DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								v.invalidate();
								result = true;
							} else if (DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								directionDragging = true;
								updateDirection(being, pointf);
								v.invalidate();
								result = true;
							}
						}
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						if (event.getClipDescription() != null) {
							if (DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								v.invalidate();
								result = true;
							} else if (DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								directionDragging = true;
								Being being = (Being) event.getLocalState();
								updateDirection(being, pointf);
								v.invalidate();
								result = true;
							}
						}
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						if (event.getClipDescription() != null) {
							if (DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								v.invalidate();
								result = true;
							} else if (DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								directionDragging = false;
								sourcePoint = null;
								v.invalidate();
								result = true;
							}
						}
						break;
					case DragEvent.ACTION_DROP:
						if (event.getClipDescription() != null) {
							if (DRAG_LOCATION.equals(event.getClipDescription().getLabel())) {
								Being being = (Being) event.getLocalState();
								updateLocation(being, pointf);
								result = true;
							} else if (DRAG_DIRECTION.equals(event.getClipDescription().getLabel())) {
								Being being = (Being) event.getLocalState();
								updateDirection(being, pointf);
								directionDragging = false;
								result = true;
							}
							v.invalidate();
						}
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						directionDragging = false;
						result = true;
						v.invalidate();
						break;
				}
				return result;
			}
		});

	}

	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}

	private Position getSourcePoint(Being being, PointF pointf) {
		Position position;
		EncounterRoundInfo encounterRoundInfo = null;
		if (being instanceof Character) {
			encounterRoundInfo = callbacks.getEncounterSetup().getCharacterCombatInfo().get(being);
		} else if (being instanceof Creature) {
			encounterRoundInfo = callbacks.getEncounterSetup().getEnemyCombatInfo().get(being);
		}
		if (encounterRoundInfo != null) {
			position = encounterRoundInfo.getPosition();
		} else {
			position = new Position(pointf.x, pointf.y, 0);
		}

		return position;
	}

	private void updateLocation(Being being, PointF pointf) {
		if (callbacks != null) {
			EncounterSetup encounterSetup = callbacks.getEncounterSetup();
			if (encounterSetup != null) {
				Character character;
				Creature creature;
				EncounterRoundInfo encounterRoundInfo = null;
				if (being instanceof Character) {
					character = (Character) being;
					encounterRoundInfo = encounterSetup.getCharacterCombatInfo().get(character);
					if (encounterRoundInfo == null) {
						encounterRoundInfo = new EncounterRoundInfo();
					}
					encounterRoundInfo.setCombatant(character);
					encounterSetup.getCharacterCombatInfo().put(character, encounterRoundInfo);
				} else if (being instanceof Creature) {
					creature = (Creature) being;
					encounterRoundInfo = encounterSetup.getEnemyCombatInfo().get(creature);
					if (encounterRoundInfo == null) {
						encounterRoundInfo = new EncounterRoundInfo();
					}
					encounterRoundInfo.setCombatant(creature);
					encounterSetup.getEnemyCombatInfo().put(creature, encounterRoundInfo);
				}
				if (encounterRoundInfo != null) {
					Position position = encounterRoundInfo.getPosition();
					if (position == null) {
						position = new Position();
					}
					Log.d(TAG, "updateLocation: pointf.x = " + pointf.x);
					Log.d(TAG, "updateLocation: pointf.y = " + pointf.y);
					Log.d(TAG, "updateLocation: lastX = " + lastX);
					Log.d(TAG, "updateLocation: kastY = " + lastY);
					position.setX(pointf.x/scaleFactor - offsetX);
					position.setY(pointf.y/scaleFactor - offsetY);
					encounterRoundInfo.setPosition(position);
					callbacks.enableEncounterButton(encounterSetup.getCharacterCombatInfo().size() > 0 &&
							encounterSetup.getEnemyCombatInfo().size() > 0);
					Log.d(TAG, "updateLocation: position = " + encounterRoundInfo.getPosition().print());
				}
			}
		}
	}

	private void updateDirection(Being being, PointF pointf) {
		EncounterRoundInfo encounterRoundInfo = null;
		if (being instanceof Character) {
			encounterRoundInfo = callbacks.getEncounterSetup().getCharacterCombatInfo().get(being);
		} else if (being instanceof Creature) {
			encounterRoundInfo = callbacks.getEncounterSetup().getEnemyCombatInfo().get(being);
		}
		if (encounterRoundInfo != null) {
			Position position = encounterRoundInfo.getPosition();
			float angle = (float) Math.atan2(pointf.y - position.getY(),
					pointf.x - position.getX());
			position.setDirection(angle);
			invalidate();
		}
	}

	private void drawCombatant(Canvas canvas, Position position, float height, float weaponLength, String name, int maxHitPoints,
							   int currentHitPoints, int bodyDevSkillBonus ) {
		canvas.save();
		canvas.translate(offsetX, offsetY);
		canvas.scale(scaleFactor,scaleFactor);
		float innerRadius = (height / 2);
		float outerRadius = innerRadius + (weaponLength * 12);
		RectF oval = new RectF(position.getX() - outerRadius,
							   position.getY() - outerRadius,
							   position.getX() + outerRadius,
							   position.getY() + outerRadius);
		float directionDegrees = (float) Math.toDegrees(position.getDirection());
		canvas.drawArc(oval, directionDegrees - 90, 180, true, frontPaint);
		canvas.drawArc(oval, directionDegrees - 150, 60, true, flankPaint);
		canvas.drawArc(oval, directionDegrees + 90, 60, true, flankPaint);
		canvas.drawArc(oval, directionDegrees + 150, 60, true, rearPaint);
		canvas.drawCircle(position.getX(),
						  position.getY(),
						  innerRadius,
						  linePaint);
		if (weaponLength > 0) {
			canvas.drawCircle(position.getX(),
							  position.getY(),
							  outerRadius,
							  linePaint);
		}
		canvas.drawText(name.substring(0, 3),
						position.getX(),
						position.getY() + textSize / 2,
						fontPaint);

		RectF healthRect = new RectF(oval);
		healthRect.top = oval.bottom + SPACING;
		healthRect.bottom = healthRect.top + textSize;
		float zeroPoint = oval.left + oval.width() * ((float)bodyDevSkillBonus / (float)(bodyDevSkillBonus + maxHitPoints));

		if(currentHitPoints > 0) {
			healthRect.left = zeroPoint;
			healthRect.right = zeroPoint + (oval.right - zeroPoint) * (currentHitPoints / maxHitPoints);
			canvas.drawRect(healthRect, healthPaint);
		}

		healthRect.left = oval.left;
		healthRect.right = zeroPoint;
		if(currentHitPoints < 0) {
			healthRect.right = healthRect.left + (zeroPoint - healthRect.left) * (-currentHitPoints / bodyDevSkillBonus);
		}
		canvas.drawRect(healthRect, unconsciousPaint);

		healthRect.left = oval.left;
		healthRect.right = oval.right;
		canvas.drawRect(healthRect, linePaint);

		canvas.drawText(String.valueOf(currentHitPoints) + "/" + String.valueOf(maxHitPoints),
						oval.left + oval.width()/2, healthRect.bottom - 2, fontPaint);
		canvas.restore();
	}

	// Getters and setters
	public float getScaleFactor() {
		return scaleFactor;
	}
	public float getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}
	public float getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
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
			Log.d(TAG, "onLongPress: ");
			super.onLongPress(e);
			Log.d(TAG, "onLongPress: scaleFactor = " + scaleFactor);
			Log.d(TAG, "onLongPress: offsetX = " + offsetX);
			Log.d(TAG, "onLongPress: offsetY = " + offsetY);
			float x = e.getX()/scaleFactor - offsetX;
			float y = e.getY()/scaleFactor - offsetY;
			Log.d(TAG, "onLongPress: x = " + x);
			Log.d(TAG, "onLongPress: y = " + y);
			if (callbacks != null) {
				ClipData dragData = null;
				for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
						.getCharacterCombatInfo()
						.entrySet()) {
					if (entry.getValue().getPosition() != null) {
						float weaponLength = entry.getKey().getWeaponLength();
						CombatPosition combatPosition = entry.getValue().getPosition().getPointIn(
								x, y, entry.getKey().getHeight(), weaponLength);
						Log.d(TAG, "onLongPress: combatPosition = " + combatPosition.toString());
						String            characterIdString = String.valueOf(entry.getKey().getId());
						ClipData.Item     clipDataItem      = new ClipData.Item(characterIdString);
						DragShadowBuilder myShadowBuilder   = null;
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
						if(myShadowBuilder != null) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								TerrainView.this.startDragAndDrop(dragData, myShadowBuilder, entry.getKey(), 0);
							}
							else {
								//noinspection deprecation
								TerrainView.this.startDrag(dragData, myShadowBuilder, entry.getKey(), 0);
							}
							break;
						}
					}
				}
				if (dragData == null) {
					for (Map.Entry<Creature, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
							.getEnemyCombatInfo()
							.entrySet()) {
						if (entry.getValue().getPosition() != null) {
							float weaponLength = entry.getKey().getWeaponLength();
							CombatPosition combatPosition = entry.getValue().getPosition().getPointIn(
									lastX, lastY, entry.getKey().getHeight(), weaponLength);
							String            creatureIdString = String.valueOf(entry.getKey().getId());
							ClipData.Item     clipDataItem     = new ClipData.Item(creatureIdString);
							DragShadowBuilder myShadowBuilder  = null;
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
							if(myShadowBuilder != null) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									TerrainView.this.startDragAndDrop(dragData, myShadowBuilder, entry.getKey(), 0);
								}
								else {
									//noinspection deprecation
									TerrainView.this.startDrag(dragData, myShadowBuilder, entry.getKey(), 0);
								}
								break;
							}
						}
					}
				}
			}
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			offsetX -= distanceX*scaleFactor;
			offsetY -= distanceY*scaleFactor;
			invalidate();
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Log.d(TAG, "onDown: ");
			lastX = e.getX();
			lastY = e.getY();
			return super.onDown(e);
		}
	}
}

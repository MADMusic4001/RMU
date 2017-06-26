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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.madinnovations.rmu.R;
import com.madinnovations.rmu.data.entities.Position;
import com.madinnovations.rmu.data.entities.character.Character;
import com.madinnovations.rmu.data.entities.combat.CombatPosition;
import com.madinnovations.rmu.data.entities.creature.Creature;
import com.madinnovations.rmu.data.entities.object.Weapon;
import com.madinnovations.rmu.data.entities.object.WeaponTemplate;
import com.madinnovations.rmu.data.entities.play.EncounterRoundInfo;
import com.madinnovations.rmu.data.entities.play.EncounterSetup;
import com.madinnovations.rmu.view.activities.play.StartEncounterFragment;
import com.madinnovations.rmu.view.di.modules.ViewsModule;

import java.util.Collection;
import java.util.Map;

/**
 * View of the 'battlefield'
 */
public class TerrainView extends View{
	private static final String DRAG_DIRECTION = "drag-direction";
	private static final String DRAG_LOCATION = "drag-location";
	public static final  float  SIZE = 60f;
	private static final String TAG = "TerrainView";
	private static final float                      SIXTY_DEGREE_RADIANS;
	private              Callbacks                  callbacks;
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
	private              float                      scaleFactor = 5.f;
	private              int                        textSize;
	private              float                      lastX;
	private              float                      lastY;

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
		drawScale(canvas);
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

		setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				if(callbacks != null) {
					for (Map.Entry<Character, EncounterRoundInfo> entry : callbacks.getEncounterSetup()
							.getCharacterCombatInfo()
							.entrySet()) {
						if(entry.getValue().getPosition() != null) {
							float weaponLength = 0.0f;
							Weapon weapon = entry.getKey().getWeapon();
							if(weapon != null) {
								weaponLength = ((WeaponTemplate)weapon.getItemTemplate()).getLength();
							}
							CombatPosition combatPosition = entry.getValue().getPosition().getPointIn(
									lastX, lastY, entry.getKey().getHeight(), weaponLength);
							ClipData dragData = null;
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
											TerrainView.this, new Position(0, 0, 0), entry.getKey());

									break;
							}
							if(dragData != null) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									view.startDragAndDrop(dragData, myShadowBuilder, null, 0);
								}
								else {
									//noinspection deprecation
									view.startDrag(dragData, myShadowBuilder, null, 0);
								}
							}
						}
					}
				}
				return false;
			}
		});

		setOnDragListener(new View.OnDragListener() {
			private Drawable targetShape = ResourcesCompat.getDrawable(getContext().getResources(),
																	   R.drawable.drag_target_background, null);
			private Drawable hoverShape  = ResourcesCompat.getDrawable(getContext().getResources(),
																	   R.drawable.drag_hover_background, null);
			private Drawable normalShape = getBackground();

			@Override
			public boolean onDrag(View v, DragEvent event) {
				final int action = event.getAction();
				PointF pointf = new PointF(event.getX(), event.getY());
				TerrainView.this.lastX = event.getX();
				TerrainView.this.lastY = event.getY();

				switch (action) {
					case DragEvent.ACTION_DRAG_STARTED:
						if(event.getClipDescription() != null &&
								(StartEncounterFragment.DRAG_CHARACTER.equals(event.getClipDescription().getLabel()) ||
								StartEncounterFragment.DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENTERED:
						if(event.getClipDescription() != null &&
								(StartEncounterFragment.DRAG_CHARACTER.equals(event.getClipDescription().getLabel()) ||
								StartEncounterFragment.DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							v.setBackground(hoverShape);
							((TerrainView)v).setHighlightPoint(pointf);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_LOCATION:
						if(event.getClipDescription() != null &&
								(StartEncounterFragment.DRAG_CHARACTER.equals(event.getClipDescription().getLabel()) ||
								StartEncounterFragment.DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							((TerrainView)v).setHighlightPoint(pointf);
							v.invalidate();
						}
						break;
					case DragEvent.ACTION_DRAG_EXITED:
						if(event.getClipDescription() != null &&
								(StartEncounterFragment.DRAG_CHARACTER.equals(event.getClipDescription().getLabel()) ||
								StartEncounterFragment.DRAG_OPPONENT.equals(event.getClipDescription().getLabel()))) {
							((TerrainView)v).setHighlightPoint(null);
							v.setBackground(targetShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DROP:
						if(event.getClipDescription() != null) {
							((TerrainView)v).setHighlightPoint(pointf);
							if (StartEncounterFragment.DRAG_CHARACTER.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int characterId = Integer.valueOf(item.getText().toString());
								for (Character aCharacter : callbacks.getCharacters()) {
									if (aCharacter.getId() == characterId) {
										EncounterRoundInfo encounterRoundInfo
												= callbacks.getEncounterSetup().getCharacterCombatInfo().get(aCharacter);
										if(encounterRoundInfo == null) {
											encounterRoundInfo = new EncounterRoundInfo();
										}
										encounterRoundInfo.setPosition(new Position(pointf.x, pointf.y, 0));
										callbacks.getEncounterSetup().getCharacterCombatInfo().put(aCharacter, encounterRoundInfo);
										callbacks.enableEncounterButton(!callbacks.getEncounterSetup().getEnemyCombatInfo().isEmpty());
										break;
									}
								}
							} else if(StartEncounterFragment.DRAG_OPPONENT.equals(event.getClipDescription().getLabel())) {
								ClipData.Item item = event.getClipData().getItemAt(0);
								int opponentId = Integer.valueOf(item.getText().toString());
								for (Creature anOpponent : callbacks.getCreatures()) {
									if (anOpponent.getId() == opponentId) {
										EncounterRoundInfo encounterRoundInfo
												= callbacks.getEncounterSetup().getEnemyCombatInfo().get(anOpponent);
										if(encounterRoundInfo == null) {
											encounterRoundInfo = new EncounterRoundInfo();
										}
										encounterRoundInfo.setPosition(new Position(pointf.x, pointf.y, 0));
										callbacks.getEncounterSetup().getEnemyCombatInfo().put(anOpponent, encounterRoundInfo);
										callbacks.enableEncounterButton(!callbacks.getEncounterSetup().getCharacterCombatInfo().isEmpty());
										break;
									}
								}
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
							((TerrainView)v).setHighlightPoint(null);
							v.setBackground(normalShape);
							v.invalidate();
						}
						else {
							return false;
						}
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						v.setBackground(normalShape);
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

	private void drawScale(Canvas canvas) {
		canvas.drawLine(-5, 490, -5, 489, linePaint);
		canvas.drawLine(5, 490, 5, 489, linePaint);
		canvas.drawLine(-5, 489.5f, 5, 489.5f, linePaint);
		canvas.drawText("10 feet", -3, 492, linePaint);
	}

	private void drawCombatant(Canvas canvas, Position position, float height, float weaponLength) {
		float innerRadius = height / 2;
		float outerRadius = innerRadius + weaponLength;
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
	public PointF getHighlightPoint() {
		return highlightPoint;
	}
	public void setHighlightPoint(PointF highlightPoint) {
		this.highlightPoint = highlightPoint;
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
		public boolean onDown(MotionEvent e) {
			lastX = e.getX();
			lastY = e.getY();
			return super.onDown(e);
		}
	}
}

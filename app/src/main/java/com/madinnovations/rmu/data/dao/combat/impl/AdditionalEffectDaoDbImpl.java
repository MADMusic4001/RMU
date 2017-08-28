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
package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.AdditionalEffectDao;
import com.madinnovations.rmu.data.dao.combat.schemas.AdditionalEffectSchema;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.entities.combat.AdditionalEffect;
import com.madinnovations.rmu.data.entities.combat.CriticalResult;
import com.madinnovations.rmu.data.entities.combat.CriticalSeverity;
import com.madinnovations.rmu.data.entities.combat.CriticalType;
import com.madinnovations.rmu.data.entities.combat.Effect;
import com.madinnovations.rmu.data.entities.combat.Fumble;
import com.madinnovations.rmu.data.entities.combat.RelativeTo;
import com.madinnovations.rmu.data.entities.combat.TargetType;
import com.madinnovations.rmu.data.entities.common.Dice;
import com.madinnovations.rmu.data.entities.common.TimeUnit;
import com.madinnovations.rmu.view.RMUAppException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link AdditionalEffect} objects in a SQLite database.
 */
@Singleton
public class AdditionalEffectDaoDbImpl extends BaseDaoDbImpl<AdditionalEffect> implements AdditionalEffectDao,
        AdditionalEffectSchema {
	private SkillDao          skillDao;
	private SpecializationDao specializationDao;

    /**
     * Creates a new instance of AdditionalEffectDaoDbImpl
     *
	 * @param skillDao  a SkillDao instance
	 * @param specializationDao  a SpecializationDao instance
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public AdditionalEffectDaoDbImpl(SQLiteOpenHelper helper, SkillDao skillDao, SpecializationDao specializationDao) {
        super(helper);
        this.skillDao = skillDao;
        this.specializationDao = specializationDao;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return COLUMNS;
    }

    @Override
    protected String getIdColumnName() {
        return COLUMN_ID;
    }

    @Override
    protected int getId(AdditionalEffect instance) {
        return instance.getId();
    }

    @Override
    protected void setId(AdditionalEffect instance, int id) {
        instance.setId(id);
    }

	@Override
    public List<AdditionalEffect> getForCriticalResult(@NonNull CriticalResult criticalResult) {
        final String selectionArgs[] = { String.valueOf(criticalResult.getId()) };
        final String selection = COLUMN_CRITICAL_RESULT_ID + " = ?";
		List<AdditionalEffect> additionalEffects = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = query(getTableName(), getColumns(), selection,
                                  selectionArgs, getSortString());
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    AdditionalEffect instance = cursorToEntity(cursor);
                    additionalEffects.add(instance);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        finally {
            if(newTransaction) {
                db.endTransaction();
            }
        }

        return additionalEffects;
    }

	@Override
	public List<AdditionalEffect> getForFumble(@NonNull Fumble fumble) {
		final String selectionArgs[] = { String.valueOf(fumble.getId()) };
		final String selection = COLUMN_FUMBLE_ID + " = ?";
		List<AdditionalEffect> additionalEffects = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		boolean newTransaction = !db.inTransaction();
		if(newTransaction) {
			db.beginTransaction();
		}
		try {
			Cursor cursor = query(getTableName(), getColumns(), selection,
								  selectionArgs, getSortString());
			if (cursor != null) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					AdditionalEffect instance = cursorToEntity(cursor);
					additionalEffects.add(instance);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		finally {
			if(newTransaction) {
				db.endTransaction();
			}
		}

		return additionalEffects;
	}

	@Override
    protected AdditionalEffect cursorToEntity(@NonNull Cursor cursor) {
        AdditionalEffect instance = new AdditionalEffect();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_RESULT_ID))) {
        	instance.setCriticalResult(new CriticalResult(cursor.getInt(
        			cursor.getColumnIndexOrThrow(COLUMN_CRITICAL_RESULT_ID))));
		}
		if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_FUMBLE_ID))) {
			instance.setFumble(new Fumble(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FUMBLE_ID))));
		}
		instance.setTargetType(TargetType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARGET_TYPE_NAME))));
        instance.setEffect(Effect.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EFFECT_NAME))));
        switch (instance.getEffect()) {
			case ATTACK:
			case BLEED:
			case BREAKAGE:
			case DAZE:
			case FATIGUE:
			case GRAPPLE:
			case DEFENSIVE_BONUS:
			case INITIATIVE_BONUS:
			case INJURY:
			case MORALE:
			case OFFENSIVE_BONUS:
			case PRONE:
			case STAGGER:
			case STUN:
			case STUN_NO_PARRY:
				instance.setValue1(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				break;
			case CRITICAL:
				instance.setValue1(CriticalSeverity.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				instance.setValue2(CriticalType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))));
				break;
			case DEATH:
				instance.setValue1(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				instance.setValue2(TimeUnit.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))));
				break;
			case DROP_ITEM:
			case HIT_POINTS:
			case MOVE:
				String value1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1));
				try {
					instance.setValue1(Integer.valueOf(value1));
				}
				catch (NumberFormatException e) {
					instance.setValue1(Dice.valueOf(value1));
					if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))) {
						instance.setValue2(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))));
					}
				}
				if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_VALUE3))) {
					String value3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE3));
					try {
						instance.setValue3(Integer.valueOf(value3));
					}
					catch (NumberFormatException e) {
						instance.setValue3(RelativeTo.valueOf(value3));
						if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_VALUE4))) {
							instance.setValue4(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE4))));
						}
					}
				}
				break;
			case KNOCK_BACK:
				if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))) {
					instance.setValue1(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				}
				else {
					instance.setValue2(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))));
					instance.setValue3(Dice.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE3))));
				}
				if(!cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_VALUE4))) {
					instance.setValue4(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE4))));
				}
				break;
			case SKILL_BONUS:
				int skillId = Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1)));
				instance.setValue2(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				int type = Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE3)));
				if(type == 1) {
					instance.setValue1(skillDao.getById(skillId));
				}
				else if(type == 2) {
					instance.setValue1(specializationDao.getById(skillId));
				}
				break;
			case UNCONSCIOUS:
				instance.setValue1(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE1))));
				instance.setValue2(TimeUnit.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE2))));
				break;
		}

        return instance;
    }

	@Override
	protected ContentValues getContentValues(AdditionalEffect instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(9);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(8);
        }
        if(instance.getCriticalResult() != null) {
			values.put(COLUMN_CRITICAL_RESULT_ID, instance.getCriticalResult().getId());
			values.putNull(COLUMN_FUMBLE_ID);
		}
		else if(instance.getFumble() != null) {
        	values.putNull(COLUMN_CRITICAL_RESULT_ID);
        	values.put(COLUMN_FUMBLE_ID, instance.getFumble().getId());
		}
		else {
        	throw new RMUAppException("Illegal state: CriticalResult and Fumble are both null.");
		}
        values.put(COLUMN_TARGET_TYPE_NAME, instance.getTargetType().name());
        values.put(COLUMN_EFFECT_NAME, instance.getEffect().name());
        if(instance.getValue1() != null) {
			if(instance.getValue1() instanceof Enum) {
				values.put(COLUMN_VALUE1, ((Enum)instance.getValue1()).name());
			}
			else {
				values.put(COLUMN_VALUE1, String.valueOf(instance.getValue1()));
			}
		}
		else {
        	values.putNull(COLUMN_VALUE1);
		}
		if(instance.getValue2() != null) {
        	if(instance.getValue2() instanceof Enum) {
        		values.put(COLUMN_VALUE2, ((Enum)instance.getValue2()).name());
			}
			else {
				values.put(COLUMN_VALUE2, String.valueOf(instance.getValue2()));
			}
		}
		else {
			values.putNull(COLUMN_VALUE2);
		}
		if(instance.getValue3() != null) {
			if(instance.getValue3() instanceof Enum) {
				values.put(COLUMN_VALUE3, ((Enum)instance.getValue3()).name());
			}
			else {
				values.put(COLUMN_VALUE3, String.valueOf(instance.getValue3()));
			}
		}
		else {
			values.putNull(COLUMN_VALUE3);
		}
		if(instance.getValue4() != null) {
			if(instance.getValue4() instanceof Enum) {
				values.put(COLUMN_VALUE4, ((Enum)instance.getValue4()).name());
			}
			else {
				values.put(COLUMN_VALUE4, String.valueOf(instance.getValue4()));
			}
		}
		else {
			values.putNull(COLUMN_VALUE4);
		}

		return values;
	}
}

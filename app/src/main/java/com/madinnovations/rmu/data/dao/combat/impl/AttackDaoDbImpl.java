/**
 * Copyright (C) 2016 MadInnovations
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madinnovations.rmu.data.dao.combat.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.combat.AttackDao;
import com.madinnovations.rmu.data.dao.combat.DamageTableDao;
import com.madinnovations.rmu.data.dao.combat.schemas.AttackSchema;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.entities.combat.Attack;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Attack} objects in a SQLite database.
 */
@Singleton
public class AttackDaoDbImpl extends BaseDaoDbImpl<Attack> implements AttackDao, AttackSchema {
    DamageTableDao damageTableDao;
    SpecializationDao specializationDao;

    /**
     * Creates a new instance of AttackDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public AttackDaoDbImpl(SQLiteOpenHelper helper, DamageTableDao damageTableDao, SpecializationDao specializationDao) {
        super(helper);
        this.damageTableDao = damageTableDao;
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
    protected int getId(Attack instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Attack instance, int id) {
        instance.setId(id);
    }

    @Override
    protected Attack cursorToEntity(@NonNull Cursor cursor) {
        Attack instance = new Attack();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODE)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        instance.setDamageTable(damageTableDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_TABLE_ID))));
        if(cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_SPECIALIZATION_ID))) {
            instance.setSpecialization(null);
        }
        else {
            instance.setSpecialization(specializationDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPECIALIZATION_ID))));
        }

        return instance;
    }

	@Override
	protected ContentValues getContentValues(Attack instance) {
        ContentValues values = new ContentValues(4);

        values.put(COLUMN_CODE, instance.getCode());
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_DAMAGE_TABLE_ID, instance.getDamageTable().getId());
        if(instance.getSpecialization() == null) {
            values.putNull(COLUMN_SPECIALIZATION_ID);
        }
        else {
            values.put(COLUMN_SPECIALIZATION_ID, instance.getSpecialization().getId());
        }

		return values;
	}
}

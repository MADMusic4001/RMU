package com.madinnovations.rmu.data.dao.common.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.common.ParameterDao;
import com.madinnovations.rmu.data.dao.common.SkillDao;
import com.madinnovations.rmu.data.dao.common.TalentCategoryDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.dao.common.schemas.TalentParametersSchema;
import com.madinnovations.rmu.data.dao.common.schemas.TalentSchema;
import com.madinnovations.rmu.data.entities.common.Parameter;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Talent} objects in a SQLite database.
 */
@Singleton
public class TalentDaoDbImpl extends BaseDaoDbImpl implements TalentDao, TalentSchema {
    private TalentCategoryDao talentCategoryDao;
    private SkillDao skillDao;
    private ParameterDao parameterDao;

    /**
     * Creates a new instance of TalentDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public TalentDaoDbImpl(SQLiteOpenHelper helper, TalentCategoryDao talentCategoryDao, SkillDao skillDao, ParameterDao parameterDao) {
        super(helper);
        this.talentCategoryDao = talentCategoryDao;
        this.skillDao = skillDao;
        this.parameterDao = parameterDao;
    }

    @Override
    public Talent getById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";
        Talent instance = new Talent();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = super.query(TABLE_NAME, COLUMNS, selection,
                    selectionArgs, COLUMN_ID);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    instance = cursorToEntity(cursor);
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

        return instance;
    }

    @Override
    public List<Talent> getAll() {
        List<Talent> list = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        boolean newTransaction = !db.inTransaction();
        if(newTransaction) {
            db.beginTransaction();
        }
        try {
            Cursor cursor = super.query(TABLE_NAME, COLUMNS, null,
                    null, COLUMN_ID);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Talent talent = cursorToEntity(cursor);
                    list.add(talent);
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

        return list;
    }

    @Override
    public boolean save(Talent instance) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }

    @Override
    protected Talent cursorToEntity(Cursor cursor) {
        Talent instance = null;
        int columnIndex;

        if (cursor != null) {
            instance = new Talent();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setInitialCost(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INITIAL_COST)));
            instance.setCostPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_COST_PER_TIER)));
            instance.setBonusPerTier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BONUS_PER_TIER)));
            instance.setSituational(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SITUATIONAL)) == 1 ? true : false);
            instance.setActionPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ACTION_POINTS)));
            instance.setCategory(talentCategoryDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))));
            instance.setAffectedSkill(skillDao.getById(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AFFECTED_SKILL_ID))));
            instance.setParameters(getParameters(instance.getId()));
        }
        return instance;
    }

    private List<Parameter> getParameters(int talentId) {
        final String selectionArgs[] = { String.valueOf(talentId) };
        final String selection = TalentParametersSchema.COLUMN_TALENT_ID + " = ?";

        Cursor cursor = super.query(TalentParametersSchema.TABLE_NAME, TalentParametersSchema.COLUMNS, selection,
                selectionArgs, TalentParametersSchema.COLUMN_PARAMETER_ID);
        List<Parameter> list = new ArrayList<>(cursor.getCount());
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(TalentParametersSchema.COLUMN_PARAMETER_ID));
                Parameter instance = parameterDao.getById(id);
                if(instance != null) {
                    list.add(instance);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return list;
    }
}

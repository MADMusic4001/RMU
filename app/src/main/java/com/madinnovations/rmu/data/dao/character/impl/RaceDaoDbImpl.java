package com.madinnovations.rmu.data.dao.character.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.character.RaceDao;
import com.madinnovations.rmu.data.dao.character.schemas.RaceMovementSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceSchema;
import com.madinnovations.rmu.data.dao.character.schemas.RaceTalentsSchema;
import com.madinnovations.rmu.data.dao.common.LocomotionTypeDao;
import com.madinnovations.rmu.data.dao.common.TalentDao;
import com.madinnovations.rmu.data.entities.character.Race;
import com.madinnovations.rmu.data.entities.common.LocomotionType;
import com.madinnovations.rmu.data.entities.common.Talent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Race} objects in a SQLite database.
 */
@Singleton
public class RaceDaoDbImpl extends BaseDaoDbImpl implements RaceDao, RaceSchema {
    TalentDao talentDao;
    LocomotionTypeDao locomotionTypeDao;

    /**
     * Creates a new instance of RaceDaoDbImpl
     *
     * @param helper  an SQLiteOpenHelper instance
     */
    @Inject
    public RaceDaoDbImpl(SQLiteOpenHelper helper, TalentDao talentDao, LocomotionTypeDao locomotionTypeDao) {
        super(helper);
        this.talentDao = talentDao;
        this.locomotionTypeDao = locomotionTypeDao;
    }

    @Override
    public Race getById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";
        Race instance = new Race();

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
    public List<Race> getAll() {
        List<Race> list = new ArrayList<>();

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
                    Race instance = cursorToEntity(cursor);
                    list.add(instance);
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
    public boolean save(Race instance) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    protected Race cursorToEntity(Cursor cursor) {
        Race instance = null;
        int columnIndex;

        if (cursor != null) {
            instance = new Race();
            instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            instance.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            instance.setBonusDevelopmentPoints(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BONUS_DEVELOPMENT_POINTS)));
            instance.setAgilityModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AGILITY_MODIFIER)));
            instance.setConstitutionModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CONSTITUTION_MODIFIER)));
            instance.setEmpathyModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_EMPATHY_MODIFIER)));
            instance.setIntuitionModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INTUITION_MODIFIER)));
            instance.setMemoryModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MEMORY_MODIFIER)));
            instance.setPresenceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_PRESENCE_MODIFIER)));
            instance.setQuicknessModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_QUICKNESS_MODIFIER)));
            instance.setReasoningModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_REASONING_MODIFIER)));
            instance.setSelfDisciplineModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SELF_DISCIPLINE_MODIFIER)));
            instance.setStrengthModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRENGTH_MODIFIER)));
            instance.setChannelingResistanceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_CHANNELING_RESISTANCE_MODIFIER)));
            instance.setEssenceResistanceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ESSENCE_RESISTANCE_MODIFIER)));
            instance.setMentalismResistanceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_MENTALISM_RESISTANCE_MODIFIER)));
            instance.setPhysicalResistanceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_PHYSICAL_RESISTANCE_MODIFIER)));
            instance.setEnduranceModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ENDURANCE_MODIFIER)));
            instance.setBaseHits(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BASE_HITS)));
            instance.setRecoveryMultiplier(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RECOVERY_MULTIPLIER)));
            instance.setSizeCategory(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_SIZE_CATEGORY)));
            instance.setStrideModifier(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_STRIDE_MODIFIER)));
            instance.setAverageHeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_HEIGHT)));
            instance.setAverageWeight(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AVERAGE_WEIGHT)));
            instance.setPoundsPerInch(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_POUNDS_PER_INCH)));
            instance.setTalentsAndFlaws(getTalentsAndFlaws(instance.getId()));
            instance.setLocomotionTypes(getMovements(instance.getId()));
        }
        return instance;
    }

    private Map<Talent, Short> getTalentsAndFlaws(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = RaceTalentsSchema.COLUMN_RACE_ID + " = ?";

        Cursor cursor = super.query(RaceTalentsSchema.TABLE_NAME, RaceTalentsSchema.COLUMNS, selection,
                selectionArgs, RaceTalentsSchema.COLUMN_TALENT_ID);
        Map<Talent, Short> map = new HashMap<>(cursor.getCount());
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TALENT_ID));
                Talent instance = talentDao.getById(mappedId);
                if(instance != null) {
                    map.put(instance, cursor.getShort(cursor.getColumnIndexOrThrow(RaceTalentsSchema.COLUMN_TIERS)));
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return map;
    }

    private List<LocomotionType> getMovements(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = RaceMovementSchema.COLUMN_RACE_ID + " = ?";

        Cursor cursor = super.query(RaceMovementSchema.TABLE_NAME, RaceMovementSchema.COLUMNS, selection,
                selectionArgs, RaceMovementSchema.COLUMN_MOVEMENT_ID);
        List<LocomotionType> locomotionTypeList = new ArrayList<>(cursor.getCount());
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int movementId = cursor.getInt(cursor.getColumnIndexOrThrow(RaceMovementSchema.COLUMN_MOVEMENT_ID));
                LocomotionType locomotionType = locomotionTypeDao.getById(movementId);
                if(locomotionType != null) {
                    locomotionTypeList.add(locomotionType);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return locomotionTypeList;
    }
}

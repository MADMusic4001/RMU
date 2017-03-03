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
package com.madinnovations.rmu.data.dao.campaign.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.madinnovations.rmu.data.dao.BaseDaoDbImpl;
import com.madinnovations.rmu.data.dao.CacheConfig;
import com.madinnovations.rmu.data.dao.campaign.CampaignDao;
import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignAttackRestrictionsSchema;
import com.madinnovations.rmu.data.dao.campaign.schemas.CampaignSchema;
import com.madinnovations.rmu.data.dao.common.SpecializationDao;
import com.madinnovations.rmu.data.entities.campaign.Campaign;
import com.madinnovations.rmu.data.entities.common.PowerLevel;
import com.madinnovations.rmu.data.entities.common.Specialization;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Methods for managing {@link Campaign} objects in a SQLite database.
 */
@Singleton
public class CampaignDaoDbImpl extends BaseDaoDbImpl<Campaign> implements CampaignDao, CampaignSchema {
    private LruCache<Integer, Campaign> campaignLruCache = new LruCache<>(CacheConfig.CAMPAIGN_CACHE_SIZE);
    private SpecializationDao specializationDao;

	/**
	 * Creates a new instance of CampaignDaoDbImpl

	 * @param helper  an SQLiteOpenHelper instance
	 */
	public CampaignDaoDbImpl(SQLiteOpenHelper helper) {
		super(helper);
	}

	/**
     * Creates a new CampaignDaoDbImpl instance
     *
     * @param helper  an SQLiteOpenHelper instance
     * @param specializationDao  a {@link SpecializationDao} instance
     */
    @Inject
	protected CampaignDaoDbImpl(SQLiteOpenHelper helper, SpecializationDao specializationDao) {
        super(helper);
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
    protected int getId(Campaign instance) {
        return instance.getId();
    }

    @Override
    protected void setId(Campaign instance, int id) {
        instance.setId(id);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected Campaign cursorToEntity(@NonNull Cursor cursor) {
        Campaign instance = new Campaign();

        instance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        instance.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATE_DATE)));
        instance.setCreateDate(calendar);
        instance.setPowerLevel(PowerLevel.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POWER_LEVEL))));
        instance.setAwardDP(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_AWARD_DP)) == 1);
        instance.setIntenseTrainingAllowed(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INTENSE_TRAINING_ALLOWED)) == 1);
        instance.setIndividualStride(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_INDIVIDUAL_STRIDE)) == 1);
        instance.setNoProfessions(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_NO_PROFESSIONS)) == 1);
        instance.setBuyStats(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_BUY_STATS)) == 1);
        instance.setAllowTalentsBeyondFirst(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_ALLOW_TALENTS)) == 1);
        instance.setOpenRounds(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_OPEN_ROUNDS)) == 1);
        instance.setGrittyPoisonAndDisease(cursor.getShort(cursor.getColumnIndexOrThrow(COLUMN_GRITTY_POISON_AND_DISEASE)) == 1);
        instance.setRestrictedSpecializations(getAttackRestrictions(instance.getId()));

        return instance;
    }

    @Override
	protected ContentValues getContentValues(Campaign instance) {
        ContentValues values;

        if(instance.getId() != -1) {
            values = new ContentValues(12);
            values.put(COLUMN_ID, instance.getId());
        }
        else {
            values = new ContentValues(11);
        }
        values.put(COLUMN_NAME, instance.getName());
        values.put(COLUMN_CREATE_DATE, instance.getCreateDate().getTimeInMillis());
		values.put(COLUMN_POWER_LEVEL, instance.getPowerLevel().name());
        values.put(COLUMN_AWARD_DP, instance.isAwardDP());
        values.put(COLUMN_INTENSE_TRAINING_ALLOWED, instance.isIntenseTrainingAllowed());
        values.put(COLUMN_INDIVIDUAL_STRIDE, instance.isIndividualStride());
        values.put(COLUMN_NO_PROFESSIONS, instance.isNoProfessions());
        values.put(COLUMN_BUY_STATS, instance.isBuyStats());
		values.put(COLUMN_ALLOW_TALENTS, instance.isAllowTalentsBeyondFirst());
        values.put(COLUMN_OPEN_ROUNDS, instance.isOpenRounds());
        values.put(COLUMN_GRITTY_POISON_AND_DISEASE, instance.isGrittyPoisonAndDisease());

        return values;
	}

    @Override
    protected boolean saveRelationships(SQLiteDatabase db, Campaign instance) {
        return saveCampaignAttackRestrictions(db, instance.getId(), instance.getRestrictedSpecializations());
    }

    private boolean saveCampaignAttackRestrictions(SQLiteDatabase db, int campaignId, List<Specialization> restrictions) {
        boolean result = true;
        final String selectionArgs[] = { String.valueOf(campaignId) };
        final String selection = CampaignAttackRestrictionsSchema.COLUMN_CAMPAIGN_ID + " = ?";

        db.delete(CampaignAttackRestrictionsSchema.TABLE_NAME, selection, selectionArgs);

        for(Specialization specialization : restrictions) {
            result &= (db.insertWithOnConflict(CampaignAttackRestrictionsSchema.TABLE_NAME, null,
                    getCampaignAttackRestrictionsValues(campaignId, specialization.getId()), SQLiteDatabase.CONFLICT_NONE) != -1);
        }
        return result;
    }

    private ContentValues getCampaignAttackRestrictionsValues(int campaignId, int specializationId) {
        ContentValues values = new ContentValues(2);

        values.put(CampaignAttackRestrictionsSchema.COLUMN_CAMPAIGN_ID, campaignId);
        values.put(CampaignAttackRestrictionsSchema.COLUMN_SPECIALIZATION_ID, specializationId);

        return values;
    }

    private List<Specialization> getAttackRestrictions(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = CampaignAttackRestrictionsSchema.COLUMN_CAMPAIGN_ID + " = ?";

        Cursor cursor = super.query(CampaignAttackRestrictionsSchema.TABLE_NAME, CampaignAttackRestrictionsSchema.COLUMNS, selection,
                selectionArgs, CampaignAttackRestrictionsSchema.COLUMN_SPECIALIZATION_ID);
        List<Specialization> list = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mappedId = cursor.getInt(cursor.getColumnIndexOrThrow(CampaignAttackRestrictionsSchema.COLUMN_SPECIALIZATION_ID));
            Specialization instance = specializationDao.getById(mappedId);
            if(instance != null) {
                list.add(instance);
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }
}
